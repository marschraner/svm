package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Semesterbezeichnung;
import ch.metzenthin.svm.common.dataTypes.Wochentag;
import ch.metzenthin.svm.persistence.entities.Kurs;
import ch.metzenthin.svm.persistence.entities.Kursanmeldung;
import ch.metzenthin.svm.persistence.entities.Semester;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;

/**
 * @author Martin Schraner
 */
public class CalculateAnzWochenCommandTest {

    private Semester erstesSemester = new Semester("2015/2016", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(2015, Calendar.AUGUST, 17), new GregorianCalendar(2016, Calendar.FEBRUARY, 13));
    private Semester zweitesSemester = new Semester("2015/2015", Semesterbezeichnung.ZWEITES_SEMESTER, new GregorianCalendar(2016, Calendar.FEBRUARY, 29), new GregorianCalendar(2016, Calendar.JULY, 16));
    private CalculateAnzWochenCommand calculateAnzWochenCommandErstesSemester = new CalculateAnzWochenCommand(null, erstesSemester);
    private CalculateAnzWochenCommand calculateAnzWochenCommandZweitesSemester = new CalculateAnzWochenCommand(null, zweitesSemester);
    private Kurs kurs = new Kurs(null, null, Wochentag.MITTWOCH, null, null, null);

    @Test
    public void testCalculateAnzWochenKursanmeldung() throws Exception {

        // 1. Semester
        Kursanmeldung kursanmeldung = new Kursanmeldung(null, kurs, new GregorianCalendar(2015, Calendar.AUGUST, 17), new GregorianCalendar(2016, Calendar.FEBRUARY, 13), null);
        assertEquals(22, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

        // Anmeldung vor Semesterbeginn
        kursanmeldung = new Kursanmeldung(null, kurs, new GregorianCalendar(2015, Calendar.AUGUST, 16), new GregorianCalendar(2016, Calendar.FEBRUARY, 13), null);
        assertEquals(22, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

        // Anmeldung am ersten Kurstag
        kursanmeldung = new Kursanmeldung(null, kurs, new GregorianCalendar(2015, Calendar.AUGUST, 19), new GregorianCalendar(2016, Calendar.FEBRUARY, 13), null);
        assertEquals(22, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

        // Spätere Anmeldung
        kursanmeldung = new Kursanmeldung(null, kurs, new GregorianCalendar(2015, Calendar.AUGUST, 20), new GregorianCalendar(2016, Calendar.FEBRUARY, 13), null);
        assertEquals(21, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

        // vor Herbstferien
        kursanmeldung = new Kursanmeldung(null, kurs, new GregorianCalendar(2015, Calendar.OCTOBER, 5), new GregorianCalendar(2016, Calendar.FEBRUARY, 13), null);
        assertEquals(15, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

        // nach Herbstferien
        kursanmeldung = new Kursanmeldung(null, kurs, new GregorianCalendar(2015, Calendar.OCTOBER, 26), new GregorianCalendar(2016, Calendar.FEBRUARY, 13), null);
        assertEquals(14, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

        // Anmeldung nach Semesterende
        kursanmeldung = new Kursanmeldung(null, kurs, new GregorianCalendar(2015, Calendar.AUGUST, 17), new GregorianCalendar(2016, Calendar.FEBRUARY, 14), null);
        assertEquals(22, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

        // Abmeldung am letzten Kurstag
        kursanmeldung = new Kursanmeldung(null, kurs, new GregorianCalendar(2015, Calendar.AUGUST, 17), new GregorianCalendar(2016, Calendar.FEBRUARY, 10), null);
        assertEquals(22, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

        // Frühere Abmeldung
        kursanmeldung = new Kursanmeldung(null, kurs, new GregorianCalendar(2015, Calendar.AUGUST, 17), new GregorianCalendar(2016, Calendar.FEBRUARY, 9), null);
        assertEquals(21, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

        // nach Weihnachtsferien
        kursanmeldung = new Kursanmeldung(null, kurs, new GregorianCalendar(2015, Calendar.AUGUST, 17), new GregorianCalendar(2016, Calendar.JANUARY, 9), null);
        assertEquals(17, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

        // vor Weihnachtsferien
        kursanmeldung = new Kursanmeldung(null, kurs, new GregorianCalendar(2015, Calendar.AUGUST, 17), new GregorianCalendar(2015, Calendar.DECEMBER, 19), null);
        assertEquals(16, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

        // 2. Semester
        kursanmeldung = new Kursanmeldung(null, kurs, new GregorianCalendar(2016, Calendar.FEBRUARY, 29), new GregorianCalendar(2016, Calendar.JULY, 16), null);
        assertEquals(18, calculateAnzWochenCommandZweitesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

        // Spätere Anmeldung
        // Vor Frühlingsferien
        kursanmeldung = new Kursanmeldung(null, kurs, new GregorianCalendar(2016, Calendar.APRIL, 20), new GregorianCalendar(2016, Calendar.JULY, 16), null);
        assertEquals(11, calculateAnzWochenCommandZweitesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

        // Nach Frühlingsferien
        kursanmeldung = new Kursanmeldung(null, kurs, new GregorianCalendar(2016, Calendar.MAY, 17), new GregorianCalendar(2016, Calendar.JULY, 16), null);
        assertEquals(9, calculateAnzWochenCommandZweitesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

        // Frühere Abmeldung
        // Vor Frühlingsferien
        kursanmeldung = new Kursanmeldung(null, kurs, new GregorianCalendar(2016, Calendar.FEBRUARY, 29), new GregorianCalendar(2016, Calendar.APRIL, 2), null);
        assertEquals(5, calculateAnzWochenCommandZweitesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

        // Nach Frühlingsferien
        kursanmeldung = new Kursanmeldung(null, kurs, new GregorianCalendar(2016, Calendar.FEBRUARY, 29), new GregorianCalendar(2016, Calendar.JUNE, 18), null);
        assertEquals(14, calculateAnzWochenCommandZweitesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));
    }
}