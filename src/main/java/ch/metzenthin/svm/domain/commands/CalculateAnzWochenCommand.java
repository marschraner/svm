package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Semesterbezeichnung;
import ch.metzenthin.svm.persistence.entities.*;

import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;

import static ch.metzenthin.svm.common.utils.CalendarUtils.getNumberOfDaysOfPeriod;

/**
 * @author Martin Schraner
 */
public class CalculateAnzWochenCommand implements Command {

    // input
    private Semester semester;
    private Collection<Schueler> schuelerRechnungsempfaenger;

    // output
    private int anzahlWochen;

    private Calendar repraesenativesDatumFruehlingsferien;
    private Calendar repraesenativesDatumHerbstferien;
    private Calendar repraesenativesDatumWeihnachtsferien;
    private int anzahlWochenSemester;

    public CalculateAnzWochenCommand(Collection<Schueler> schuelerRechnungsempfaenger, Semester semester) {
        this.semester = semester;
        this.schuelerRechnungsempfaenger = schuelerRechnungsempfaenger;

        // Repräsentative Daten für Schulferien (= Daten, die immer in Ferien liegen)
        int year = semester.getSemesterbeginn().get(Calendar.YEAR);
        repraesenativesDatumFruehlingsferien = new GregorianCalendar(year, Calendar.APRIL, 25);
        repraesenativesDatumHerbstferien = new GregorianCalendar(year, Calendar.OCTOBER, 15);
        repraesenativesDatumWeihnachtsferien = new GregorianCalendar(year, Calendar.DECEMBER, 31);
        anzahlWochenSemester = semester.getAnzahlSchulwochen();
    }

    @Override
    public void execute() {

        // Maximum über alle Kurse eines Rechnungsempfängers
        anzahlWochen = 0;
        for (Schueler schueler : schuelerRechnungsempfaenger) {

            // abgemeldete Schüler nicht berücksichtigen
            Anmeldung anmeldung = schueler.getAnmeldungen().get(0);
            if (anmeldung.getAbmeldedatum() != null && !anmeldung.getAbmeldedatum().after(semester.getSemesterbeginn())) {
                continue;
            }

            for (Kursanmeldung kursanmeldung : schueler.getKursanmeldungen()) {
                if (kursanmeldung.getKurs().getSemester().getSemesterId().equals(semester.getSemesterId())) {
                    int anzWochenKursanmeldung = calculateAnzWochenKursanmeldung(kursanmeldung);
                    if (anzWochenKursanmeldung > anzahlWochen) {
                        anzahlWochen = anzWochenKursanmeldung;
                    }
                }
            }
        }

        // Default-Wert Anzahl Semesterwochen, falls noch keine Kursanmeldung
        if (anzahlWochen == 0) {
            anzahlWochen = semester.getAnzahlSchulwochen();
        }
    }


    protected int calculateAnzWochenKursanmeldung(Kursanmeldung kursanmeldung) {

        int anzahlWochenKursanmeldung = anzahlWochenSemester;

        // Später angemeldet?
        if (kursanmeldung.getAnmeldedatum() != null && kursanmeldung.getAnmeldedatum().after(semester.getSemesterbeginn())) {
            anzahlWochenKursanmeldung -= getNumberOfDaysOfPeriod(semester.getSemesterbeginn(), kursanmeldung.getAnmeldedatum()) / 7;

            // Wochentag des Anmeldedatums nach Wochentag des Kurses?
            if (kursanmeldung.getAnmeldedatum().get(Calendar.DAY_OF_WEEK) > kursanmeldung.getKurs().getWochentag().getDayOfWeekCalendar()) {
                anzahlWochenKursanmeldung--;
            }

            if (semester.getSemesterbezeichnung() == Semesterbezeichnung.ERSTES_SEMESTER) {
                if (kursanmeldung.getAnmeldedatum().after(repraesenativesDatumHerbstferien)) {
                    anzahlWochenKursanmeldung += 2;
                    if (kursanmeldung.getAnmeldedatum().after(repraesenativesDatumWeihnachtsferien)) {
                        anzahlWochenKursanmeldung += 2;
                    }
                }
            }

            if (semester.getSemesterbezeichnung() == Semesterbezeichnung.ZWEITES_SEMESTER) {
                if (kursanmeldung.getAnmeldedatum().after(repraesenativesDatumFruehlingsferien)) {
                    anzahlWochenKursanmeldung += 2;
                }
            }
        }

        // Früher abgemeldet?
        if (kursanmeldung.getAbmeldedatum() != null && kursanmeldung.getAbmeldedatum().before(semester.getSemesterende())) {
            anzahlWochenKursanmeldung -= getNumberOfDaysOfPeriod(kursanmeldung.getAbmeldedatum(), semester.getSemesterende()) / 7;

            // Wochentag des Abmeldedatums vor Wochentag des Kurses?
            if (kursanmeldung.getAbmeldedatum().get(Calendar.DAY_OF_WEEK) < kursanmeldung.getKurs().getWochentag().getDayOfWeekCalendar()) {
                anzahlWochenKursanmeldung--;
            }

            if (semester.getSemesterbezeichnung() == Semesterbezeichnung.ERSTES_SEMESTER) {
                if (kursanmeldung.getAbmeldedatum().before(repraesenativesDatumWeihnachtsferien)) {
                    anzahlWochenKursanmeldung += 2;
                    if (kursanmeldung.getAbmeldedatum().before(repraesenativesDatumHerbstferien)) {
                        anzahlWochenKursanmeldung += 2;
                    }
                }
            }

            if (semester.getSemesterbezeichnung() == Semesterbezeichnung.ZWEITES_SEMESTER) {
                if (kursanmeldung.getAbmeldedatum().before(repraesenativesDatumFruehlingsferien)) {
                    anzahlWochenKursanmeldung += 2;
                }
            }
        }

        return anzahlWochenKursanmeldung;
    }

    public int getAnzahlWochen() {
        return anzahlWochen;
    }
}
