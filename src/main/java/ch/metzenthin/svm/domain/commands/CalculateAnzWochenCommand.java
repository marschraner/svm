package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Semesterbezeichnung;
import ch.metzenthin.svm.persistence.entities.*;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static ch.metzenthin.svm.common.utils.SimpleValidator.getNumberOfDaysOfPeriod;

/**
 * @author Martin Schraner
 */
public class CalculateAnzWochenCommand implements Command {

    // output
    private int anzahlWochen;
    private Semester semester;
    private Kursanmeldung kursanmeldung;

    public CalculateAnzWochenCommand(Semester semester, Kursanmeldung kursanmeldung) {
        this.semester = semester;
        this.kursanmeldung = kursanmeldung;
    }

    @Override
    public void execute() {

        // Repräsentative Daten für Schulferien (= Daten, die immer in Ferien liegen)
        int year = semester.getSemesterbeginn().get(Calendar.YEAR);
        Calendar repraesenativesDatumFruehlingsferien = new GregorianCalendar(year, Calendar.APRIL, 25);
        Calendar repraesenativesDatumHerbstferien  = new GregorianCalendar(year, Calendar.OCTOBER, 15);
        Calendar repraesenativesDatumWeihnachtsferien = new GregorianCalendar(year, Calendar.DECEMBER, 31);

        anzahlWochen = semester.getAnzahlSchulwochen();

        // Später angemeldet?
        if (kursanmeldung.getAnmeldedatum() != null && kursanmeldung.getAnmeldedatum().after(semester.getSemesterbeginn())) {
            anzahlWochen -= getNumberOfDaysOfPeriod(semester.getSemesterbeginn(), kursanmeldung.getAnmeldedatum()) / 7;

            // Wochentag des Anmeldedatums nach Wochentag des Kurses?
            if (kursanmeldung.getAnmeldedatum().get(Calendar.DAY_OF_WEEK) > kursanmeldung.getKurs().getWochentag().getDayOfWeekCalendar()) {
                anzahlWochen--;
            }

            if (semester.getSemesterbezeichnung() == Semesterbezeichnung.ERSTES_SEMESTER) {
                if (kursanmeldung.getAnmeldedatum().after(repraesenativesDatumHerbstferien)) {
                    anzahlWochen += 2;
                    if (kursanmeldung.getAnmeldedatum().after(repraesenativesDatumWeihnachtsferien)) {
                        anzahlWochen += 2;
                    }
                }
            }

            if (semester.getSemesterbezeichnung() == Semesterbezeichnung.ZWEITES_SEMESTER) {
                if (kursanmeldung.getAnmeldedatum().after(repraesenativesDatumFruehlingsferien)) {
                    anzahlWochen += 2;
                }
            }
        }

        // Früher abgemeldet?
        if (kursanmeldung.getAbmeldedatum() != null && kursanmeldung.getAbmeldedatum().before(semester.getSemesterende())) {
            anzahlWochen -= getNumberOfDaysOfPeriod(kursanmeldung.getAbmeldedatum(), semester.getSemesterende()) / 7;

            // Wochentag des Abmeldedatums vor Wochentag des Kurses?
            if (kursanmeldung.getAbmeldedatum().get(Calendar.DAY_OF_WEEK) < kursanmeldung.getKurs().getWochentag().getDayOfWeekCalendar()) {
                anzahlWochen--;
            }

            if (semester.getSemesterbezeichnung() == Semesterbezeichnung.ERSTES_SEMESTER) {
                if (kursanmeldung.getAbmeldedatum().before(repraesenativesDatumWeihnachtsferien)) {
                    anzahlWochen += 2;
                    if (kursanmeldung.getAbmeldedatum().before(repraesenativesDatumHerbstferien)) {
                        anzahlWochen += 2;
                    }
                }
            }

            if (semester.getSemesterbezeichnung() == Semesterbezeichnung.ZWEITES_SEMESTER) {
                if (kursanmeldung.getAbmeldedatum().before(repraesenativesDatumFruehlingsferien)) {
                    anzahlWochen += 2;
                }
            }
        }
    }

    public int getAnzahlWochen() {
        return anzahlWochen;
    }
}
