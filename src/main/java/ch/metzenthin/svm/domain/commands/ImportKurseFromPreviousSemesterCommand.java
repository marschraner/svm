package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Semesterbezeichnung;
import ch.metzenthin.svm.persistence.daos.KursDao;
import ch.metzenthin.svm.persistence.daos.KursanmeldungDao;
import ch.metzenthin.svm.persistence.entities.*;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class ImportKurseFromPreviousSemesterCommand extends GenericDaoCommand {

    // input
    private List<Kurs> kurseCurrentSemester;
    private Semester currentSemester;

    public ImportKurseFromPreviousSemesterCommand(List<Kurs> kurseCurrentSemester, Semester currentSemester) {
        this.kurseCurrentSemester = kurseCurrentSemester;
        this.currentSemester = currentSemester;
    }

    @Override
    public void execute() {

        // Vorhergehendes Semester
        FindPreviousSemesterCommand findPreviousSemesterCommand = new FindPreviousSemesterCommand(currentSemester);
        findPreviousSemesterCommand.setEntityManager(entityManager);
        findPreviousSemesterCommand.execute();
        Semester previousSemester = findPreviousSemesterCommand.getPreviousSemester();
        if (previousSemester == null) {
            return;
        }

        KursDao kursDao = new KursDao(entityManager);
        KursanmeldungDao kursanmeldungDao = new KursanmeldungDao(entityManager);
        List<Kurs> kursePreviousSemester = kursDao.findKurseSemester(previousSemester);

        KursePreviousSemester:
        for (Kurs kursPreviousSemester : kursePreviousSemester) {

            // Kurs kopieren
            Kurs kurs = new Kurs();
            kurs.copyAttributesFrom(kursPreviousSemester);
            kurs.setKurstyp(kursPreviousSemester.getKurstyp());
            kurs.setKursort(kursPreviousSemester.getKursort());
            for (Mitarbeiter mitarbeiter : kursPreviousSemester.getLehrkraefte()) {
                kurs.addLehrkraft(mitarbeiter);
            }
            kurs.setSemester(currentSemester);

            // Existiert der Kurs im jetzigen Semester bereits? Wenn ja, unverändert lassen.
            for (Kurs kursCurrentSemester : kurseCurrentSemester) {
                if (kurs.isIdenticalWith(kursCurrentSemester)) {
                    continue KursePreviousSemester;
                }
            }

            // Kurs speichern
            kursDao.save(kurs);
            kurseCurrentSemester.add(kurs);

            // Falls 2. Semester, auch Schüler bzw. Kurseinteilungen importieren
            if (currentSemester.getSemesterbezeichnung() == Semesterbezeichnung.ZWEITES_SEMESTER) {
                for (Kursanmeldung kursanmeldungPreviousSemester : kursPreviousSemester.getKursanmeldungen()) {

                    // Nur Kurse ohne Kursabmeldungen und nur für nicht abgemeldete Schüler importieren
                    Anmeldung anmeldung = kursanmeldungPreviousSemester.getSchueler().getAnmeldungen().get(0);
                    if (kursanmeldungPreviousSemester.getAbmeldedatum() != null
                            && (anmeldung.getAbmeldedatum() == null || anmeldung.getAbmeldedatum().after(currentSemester.getSemesterbeginn()))) {
                        Kursanmeldung kursanmeldung = new Kursanmeldung(kursanmeldungPreviousSemester.getSchueler(), kurs, null, null, null);
                        kursanmeldungDao.save(kursanmeldung);

                        // Semesterrechnungen updaten
                        UpdateWochenbetragCommand updateWochenbetragCommand = new UpdateWochenbetragCommand(kursanmeldungPreviousSemester.getSchueler().getRechnungsempfaenger(), currentSemester);
                        updateWochenbetragCommand.setEntityManager(entityManager);
                        updateWochenbetragCommand.execute();
                    }
                }
            }

        }
    }

}
