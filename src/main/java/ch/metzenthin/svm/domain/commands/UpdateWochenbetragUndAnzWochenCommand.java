package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.datatypes.Rechnungstyp;
import ch.metzenthin.svm.persistence.daos.SemesterrechnungDao;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.persistence.entities.Semesterrechnung;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author Martin Schraner
 */
public class UpdateWochenbetragUndAnzWochenCommand implements Command {

    private final SemesterrechnungDao semesterrechnungDao = new SemesterrechnungDao();

    // input
    private final Angehoeriger rechnungsempfaenger;
    private final Semester currentSemester;

    // output
    private CalculateAnzWochenCommand.Result result;

    public UpdateWochenbetragUndAnzWochenCommand(Angehoeriger rechnungsempfaenger, Semester currentSemester) {
        this.rechnungsempfaenger = rechnungsempfaenger;
        this.currentSemester = currentSemester;
    }

    @SuppressWarnings("java:S3776")
    @Override
    public void execute() {

        result = CalculateAnzWochenCommand.Result.ALLE_KURSE_GLEICHE_ANZAHL_WOCHEN;

        // 1. Nachfolgendes Semester
        FindNextSemesterCommand findNextSemesterCommand = new FindNextSemesterCommand(currentSemester);
        findNextSemesterCommand.execute();
        Semester nextSemester = findNextSemesterCommand.getNextSemester();

        // 2. Semesterrechnung des jetzigen und nachfolgenden Semesters
        Semesterrechnung semesterrechnungCurrentSemester = null;
        Semesterrechnung semesterrechnungNextSemester = null;
        for (Semesterrechnung semesterrechnung1 : rechnungsempfaenger.getSemesterrechnungen()) {
            if (semesterrechnung1.getSemester().getSemesterId().equals(currentSemester.getSemesterId())) {
                semesterrechnungCurrentSemester = semesterrechnung1;
            }
            if (nextSemester != null && semesterrechnung1.getSemester().getSemesterId().equals(nextSemester.getSemesterId())) {
                semesterrechnungNextSemester = semesterrechnung1;
            }
        }

        // 3. Lektionsgebühren
        FindAllLektionsgebuehrenCommand findAllLektionsgebuehrenCommand = new FindAllLektionsgebuehrenCommand();
        findAllLektionsgebuehrenCommand.execute();
        Map<Integer, BigDecimal[]> lektionsgebuehrenMap = findAllLektionsgebuehrenCommand.getLektionsgebuehrenAllMap();

        // 4.a Jetziges Semesters
        if (semesterrechnungCurrentSemester != null) {

            boolean dataChanged = false;

            // Berechnung der Anzahl Wochen
            CalculateAnzWochenCommand calculateAnzWochenCommand = new CalculateAnzWochenCommand(semesterrechnungCurrentSemester.getRechnungsempfaenger().getSchuelerRechnungsempfaenger(), currentSemester);
            calculateAnzWochenCommand.execute();
            result = calculateAnzWochenCommand.getResult();

            // Vorrechnung: Update Anzahl Wochen
            if (semesterrechnungCurrentSemester.getRechnungsdatumVorrechnung() == null) {
                semesterrechnungCurrentSemester.setAnzahlWochenVorrechnung(calculateAnzWochenCommand.getAnzahlWochen());
                dataChanged = true;
            }

            // Nachrechnung: Update Anzahl Wochen und Berechnung des Wochenbetrags
            if (semesterrechnungCurrentSemester.getRechnungsdatumNachrechnung() == null) {
                semesterrechnungCurrentSemester.setAnzahlWochenNachrechnung(calculateAnzWochenCommand.getAnzahlWochen());
                CalculateWochenbetragCommand calculateWochenbetragCommand = new CalculateWochenbetragCommand(semesterrechnungCurrentSemester, currentSemester, Rechnungstyp.NACHRECHNUNG, lektionsgebuehrenMap);
                calculateWochenbetragCommand.execute();
                if (calculateAnzWochenCommand.getResult() != CalculateAnzWochenCommand.Result.ALLE_KURSE_GLEICHE_ANZAHL_WOCHEN) {
                    result = calculateAnzWochenCommand.getResult();
                }
                if (calculateWochenbetragCommand.getResult() == CalculateWochenbetragCommand.Result.WOCHENBETRAG_ERFOLGREICH_BERECHNET) {
                    semesterrechnungCurrentSemester.setWochenbetragNachrechnung(calculateWochenbetragCommand.getWochenbetrag());
                } else {  // sollte nie eintreten
                    semesterrechnungCurrentSemester.setWochenbetragNachrechnung(new BigDecimal("-99999.99"));
                }
                dataChanged = true;
            }

            // Update
            if (dataChanged) {
                if (!semesterrechnungCurrentSemester.isNullrechnung()) {
                    semesterrechnungDao.save(semesterrechnungCurrentSemester);
                } else {
                    // löschen, falls Nullrechnung
                    semesterrechnungDao.remove(semesterrechnungCurrentSemester);
                }
            }
        }

        // 4.b Nachfolgendes Semesters: Berechnung des Wochenbetrags Vorrechnung und Update
        if (semesterrechnungNextSemester != null && semesterrechnungNextSemester.getRechnungsdatumVorrechnung() == null) {
            CalculateWochenbetragCommand calculateWochenbetragCommand = new CalculateWochenbetragCommand(semesterrechnungNextSemester, currentSemester, Rechnungstyp.VORRECHNUNG, lektionsgebuehrenMap);
            calculateWochenbetragCommand.execute();
            if (calculateWochenbetragCommand.getResult() == CalculateWochenbetragCommand.Result.WOCHENBETRAG_ERFOLGREICH_BERECHNET) {
                semesterrechnungNextSemester.setWochenbetragVorrechnung(calculateWochenbetragCommand.getWochenbetrag());
            } else {  // sollte nie eintreten
                semesterrechnungNextSemester.setWochenbetragVorrechnung(new BigDecimal("-99999.99"));
            }
            if (!semesterrechnungNextSemester.isNullrechnung()) {
                semesterrechnungDao.save(semesterrechnungNextSemester);
            } else {
                // löschen, falls Nullrechnung
                semesterrechnungDao.remove(semesterrechnungNextSemester);
            }
        }
    }

    public CalculateAnzWochenCommand.Result getResult() {
        return result;
    }
}
