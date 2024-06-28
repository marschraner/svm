package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.datatypes.Semesterbezeichnung;
import ch.metzenthin.svm.persistence.daos.KursDao;
import ch.metzenthin.svm.persistence.daos.KursanmeldungDao;
import ch.metzenthin.svm.persistence.entities.*;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class ImportKurseFromPreviousSemesterCommand implements Command {

    private final KursDao kursDao = new KursDao();
    private final KursanmeldungDao kursanmeldungDao = new KursanmeldungDao();

    // input / output
    private final List<Kurs> kurseCurrentSemester;

    // input
    private final Semester currentSemester;

    public ImportKurseFromPreviousSemesterCommand(List<Kurs> kurseCurrentSemester, Semester currentSemester) {
        this.kurseCurrentSemester = kurseCurrentSemester;
        this.currentSemester = currentSemester;
    }

    @SuppressWarnings("ExtractMethodRecommender")
    @Override
    public void execute() {

        Semester oldSemester = null;

        // 1. Semester -> Kurse vom 1. Semester vor einem Jahr importieren
        if (currentSemester.getSemesterbezeichnung() == Semesterbezeichnung.ERSTES_SEMESTER) {
            FindSemesterOneYearBeforeCommand findSemesterOneYearBeforeCommand = new FindSemesterOneYearBeforeCommand(currentSemester);
            findSemesterOneYearBeforeCommand.execute();
            oldSemester = findSemesterOneYearBeforeCommand.getSemesterOneYearBefore();
        }

        // 2. Semester (oder Kurse vom 1. Semester vor einem Jahr nicht vorhanden) -> Kurse und Schüler vom 1. Semester importieren
        if (currentSemester.getSemesterbezeichnung() == Semesterbezeichnung.ZWEITES_SEMESTER || oldSemester == null) {
            FindPreviousSemesterCommand findPreviousSemesterCommand = new FindPreviousSemesterCommand(currentSemester);
            findPreviousSemesterCommand.execute();
            oldSemester = findPreviousSemesterCommand.getPreviousSemester();
        }

        if (oldSemester == null) {
            return;
        }

        List<Kurs> kursePreviousSemester = kursDao.findKurseSemester(oldSemester);

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
                    if (kursanmeldungPreviousSemester.getAbmeldedatum() == null
                            && (anmeldung.getAbmeldedatum() == null || anmeldung.getAbmeldedatum().after(currentSemester.getSemesterbeginn()))) {
                        Kursanmeldung kursanmeldung = new Kursanmeldung(kursanmeldungPreviousSemester.getSchueler(), kurs, currentSemester.getSemesterbeginn(), null, null);
                        kursanmeldungDao.save(kursanmeldung);

                        // Semesterrechnungen updaten
                        UpdateWochenbetragUndAnzWochenCommand updateWochenbetragUndAnzWochenCommand = new UpdateWochenbetragUndAnzWochenCommand(kursanmeldungPreviousSemester.getSchueler().getRechnungsempfaenger(), currentSemester);
                        updateWochenbetragUndAnzWochenCommand.execute();
                    }
                }
            }

        }
    }

}
