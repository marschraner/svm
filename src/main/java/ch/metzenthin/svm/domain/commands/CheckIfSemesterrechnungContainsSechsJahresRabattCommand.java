package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Rechnungstyp;
import ch.metzenthin.svm.persistence.entities.*;

/**
 * @author Martin Schraner
 */
public class CheckIfSemesterrechnungContainsSechsJahresRabattCommand implements Command {

    // input
    private Semesterrechnung semesterrechnung;
    private Semester previousSemester;
    private Rechnungstyp rechnungstyp;

    // output
    private boolean semesterrechnungContainsSechsJahresRabatt = false;

    public CheckIfSemesterrechnungContainsSechsJahresRabattCommand(Semesterrechnung semesterrechnung, Semester previousSemester, Rechnungstyp rechnungstyp) {
        this.semesterrechnung = semesterrechnung;
        this.previousSemester = previousSemester;
        this.rechnungstyp = rechnungstyp;
    }

    @Override
    public void execute() {

        // 1. Relevantes Semester
        Semester relevantesSemester = (rechnungstyp == Rechnungstyp.NACHRECHNUNG ? semesterrechnung.getSemester() : previousSemester);

        for (Schueler schueler : semesterrechnung.getRechnungsempfaenger().getSchuelerRechnungsempfaenger()) {

            // 2.a abgemeldete Schüler nicht berücksichtigen
            Anmeldung anmeldung = schueler.getAnmeldungen().get(0);
            if (anmeldung.getAbmeldedatum() != null && !anmeldung.getAbmeldedatum().after(semesterrechnung.getSemester().getSemesterbeginn())) {
                continue;
            }

            // 2.b relevante Kurse für einen Schüler
            int anzahlRelevanteKurseSchueler = 0;
            for (Kursanmeldung kursanmeldung : schueler.getKursanmeldungen()) {
                if (kursanmeldung.getKurs().getSemester().getSemesterId().equals(relevantesSemester.getSemesterId()) && (rechnungstyp == Rechnungstyp.NACHRECHNUNG || !kursanmeldung.getAbmeldungPerEndeSemester())) {
                    anzahlRelevanteKurseSchueler++;
                }
            }

            // Es sind mind. 2 Kurse notwendig
            if (anzahlRelevanteKurseSchueler < 2) {
                continue;
            }

            // Genügend lange Anmeldedauer?
            CheckIfAnmeldungsdauerErlaubtSechsJahresRabattCommand checkIfAnmeldungsdauerErlaubtSechsJahresRabattCommand = new CheckIfAnmeldungsdauerErlaubtSechsJahresRabattCommand(schueler, relevantesSemester.getSemesterbeginn());
            checkIfAnmeldungsdauerErlaubtSechsJahresRabattCommand.execute();
            if (checkIfAnmeldungsdauerErlaubtSechsJahresRabattCommand.isAnmeldungsdauerErlaubtSechsJahresRabatt()) {
                semesterrechnungContainsSechsJahresRabatt = true;
                break;
            }

        }
    }

    public boolean isSemesterrechnungContainsSechsJahresRabatt() {
        return semesterrechnungContainsSechsJahresRabatt;
    }
}
