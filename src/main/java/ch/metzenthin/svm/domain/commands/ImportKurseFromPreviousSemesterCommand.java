package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Schuljahre;
import ch.metzenthin.svm.common.dataTypes.Semesterbezeichnung;
import ch.metzenthin.svm.persistence.daos.KursDao;
import ch.metzenthin.svm.persistence.daos.KursanmeldungDao;
import ch.metzenthin.svm.persistence.daos.SemesterDao;
import ch.metzenthin.svm.persistence.entities.*;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class ImportKurseFromPreviousSemesterCommand extends GenericDaoCommand {

    // input
    private List<Kurs> kurseCurrentSemester;
    private Semester semester;

    public ImportKurseFromPreviousSemesterCommand(List<Kurs> kurseCurrentSemester, Semester semester) {
        this.kurseCurrentSemester = kurseCurrentSemester;
        this.semester = semester;
    }

    @Override
    public void execute() {
        // Kein Import, falls schon Kurse erfasst
        if (kurseCurrentSemester.size() > 0) {
            return;
        }

        KursDao kursDao = new KursDao(entityManager);
        KursanmeldungDao kursanmeldungDao = new KursanmeldungDao(entityManager);
        String schuljahrPreviousSemester;
        Semesterbezeichnung semesterbezeichnungPreviousSemester;
        if (semester.getSemesterbezeichnung() == Semesterbezeichnung.ERSTES_SEMESTER) {
            schuljahrPreviousSemester = Schuljahre.getPreviousSchuljahr(semester.getSchuljahr());
            semesterbezeichnungPreviousSemester = Semesterbezeichnung.ZWEITES_SEMESTER;

        } else {
            schuljahrPreviousSemester = semester.getSchuljahr();
            semesterbezeichnungPreviousSemester = Semesterbezeichnung.ERSTES_SEMESTER;
        }

        SemesterDao semesterDao = new SemesterDao(entityManager);
        List<Semester> semestersAll = semesterDao.findAll();
        Semester previousSemester = null;
        for (Semester semester : semestersAll) {
            if (semester.getSchuljahr().equals(schuljahrPreviousSemester) && semester.getSemesterbezeichnung().equals(semesterbezeichnungPreviousSemester)) {
                previousSemester = semester;
                break;
            }
        }
        if (previousSemester == null) {
            return;
        }

        // Kurse kopieren
        List<Kurs> kursePreviousSemester = kursDao.findKurseSemester(previousSemester);
        for (Kurs kursPreviousSemester : kursePreviousSemester) {
            Kurs kurs = new Kurs();
            kurs.copyAttributesFrom(kursPreviousSemester);
            kurs.setKurstyp(kursPreviousSemester.getKurstyp());
            kurs.setKursort(kursPreviousSemester.getKursort());
            for (Lehrkraft lehrkraft : kursPreviousSemester.getLehrkraefte()) {
                kurs.addLehrkraft(lehrkraft);
            }
            kurs.setSemester(semester);
            kursDao.save(kurs);
            kurseCurrentSemester.add(kurs);

            // Falls 2. Semester, auch Schüler bzw. Kurseinteilungen importieren
            if (semester.getSemesterbezeichnung() == Semesterbezeichnung.ZWEITES_SEMESTER) {
                for (Kursanmeldung kursanmeldungPreviousSemester : kursPreviousSemester.getKursanmeldungen()) {
                    // Nur Kurseinteilungen ohne Abmeldungen importieren
                    if (!kursanmeldungPreviousSemester.getAbmeldungPerEndeSemester()) {
                        Kursanmeldung kursanmeldung = new Kursanmeldung(kursanmeldungPreviousSemester.getSchueler(), kurs, false, null);
                        kursanmeldungDao.save(kursanmeldung);
                    }
                }
            }

        }
    }

}
