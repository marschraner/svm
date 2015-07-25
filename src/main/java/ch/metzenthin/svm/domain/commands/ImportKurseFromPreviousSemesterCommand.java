package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.dataTypes.Schuljahre;
import ch.metzenthin.svm.dataTypes.Semesterbezeichnung;
import ch.metzenthin.svm.persistence.daos.KursDao;
import ch.metzenthin.svm.persistence.entities.Kurs;
import ch.metzenthin.svm.persistence.entities.Lehrkraft;
import ch.metzenthin.svm.persistence.entities.Semester;

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
        String schuljahrPreviouSemester;
        Semesterbezeichnung semesterbezeichnungPreviousSemester;
        if (semester.getSemesterbezeichnung() == Semesterbezeichnung.ERSTES_SEMESTER) {
            schuljahrPreviouSemester = Schuljahre.getPreviousSchuljahr(semester.getSchuljahr());
            semesterbezeichnungPreviousSemester = Semesterbezeichnung.ZWEITES_SEMESTER;

        } else {
            schuljahrPreviouSemester = semester.getSchuljahr();
            semesterbezeichnungPreviousSemester = Semesterbezeichnung.ERSTES_SEMESTER;
        }

        // Kurse kopieren
        List<Kurs> kursePreviousSemester = kursDao.findKurseSemester(new Semester(schuljahrPreviouSemester, semesterbezeichnungPreviousSemester, null, null, 0));
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
        }
    }

}
