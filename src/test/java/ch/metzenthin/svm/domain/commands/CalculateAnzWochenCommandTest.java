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

    private Semester erstesSemester = new Semester("2015/2016", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(2015, Calendar.AUGUST, 17), new GregorianCalendar(2016, Calendar.FEBRUARY, 13), 22);
    private Semester zweitesSemester = new Semester("2015/2015", Semesterbezeichnung.ZWEITES_SEMESTER, new GregorianCalendar(2016, Calendar.FEBRUARY, 29), new GregorianCalendar(2016, Calendar.JULY, 16), 18);
    Kurs kurs = new Kurs(null, null, Wochentag.MITTWOCH, null, null, null);

    @Test
    public void testExecute() throws Exception {

        // 1. Semester
        Kursanmeldung kursanmeldung = new Kursanmeldung(null, kurs, new GregorianCalendar(2015, Calendar.AUGUST, 17), new GregorianCalendar(2016, Calendar.FEBRUARY, 13), null);
        CalculateAnzWochenCommand calculateAnzWochenCommand = new CalculateAnzWochenCommand(erstesSemester, kursanmeldung);
        calculateAnzWochenCommand.execute();
        assertEquals(22, calculateAnzWochenCommand.getAnzahlWochen());

        // Anmeldung vor Semesterbeginn
        kursanmeldung = new Kursanmeldung(null, kurs, new GregorianCalendar(2015, Calendar.AUGUST, 16), new GregorianCalendar(2016, Calendar.FEBRUARY, 13), null);
        calculateAnzWochenCommand = new CalculateAnzWochenCommand(erstesSemester, kursanmeldung);
        calculateAnzWochenCommand.execute();
        assertEquals(22, calculateAnzWochenCommand.getAnzahlWochen());

        // Anmeldung am ersten Kurstag
        kursanmeldung = new Kursanmeldung(null, kurs, new GregorianCalendar(2015, Calendar.AUGUST, 19), new GregorianCalendar(2016, Calendar.FEBRUARY, 13), null);
        calculateAnzWochenCommand = new CalculateAnzWochenCommand(erstesSemester, kursanmeldung);
        calculateAnzWochenCommand.execute();
        assertEquals(22, calculateAnzWochenCommand.getAnzahlWochen());

        // Spätere Anmeldung
        kursanmeldung = new Kursanmeldung(null, kurs, new GregorianCalendar(2015, Calendar.AUGUST, 20), new GregorianCalendar(2016, Calendar.FEBRUARY, 13), null);
        calculateAnzWochenCommand = new CalculateAnzWochenCommand(erstesSemester, kursanmeldung);
        calculateAnzWochenCommand.execute();
        assertEquals(21, calculateAnzWochenCommand.getAnzahlWochen());

        // vor Herbstferien
        kursanmeldung = new Kursanmeldung(null, kurs, new GregorianCalendar(2015, Calendar.OCTOBER, 5), new GregorianCalendar(2016, Calendar.FEBRUARY, 13), null);
        calculateAnzWochenCommand = new CalculateAnzWochenCommand(erstesSemester, kursanmeldung);
        calculateAnzWochenCommand.execute();
        assertEquals(15, calculateAnzWochenCommand.getAnzahlWochen());

        // nach Herbstferien
        kursanmeldung = new Kursanmeldung(null, kurs, new GregorianCalendar(2015, Calendar.OCTOBER, 26), new GregorianCalendar(2016, Calendar.FEBRUARY, 13), null);
        calculateAnzWochenCommand = new CalculateAnzWochenCommand(erstesSemester, kursanmeldung);
        calculateAnzWochenCommand.execute();
        assertEquals(14, calculateAnzWochenCommand.getAnzahlWochen());

        // Anmeldung nach Semesterende
        kursanmeldung = new Kursanmeldung(null, kurs, new GregorianCalendar(2015, Calendar.AUGUST, 17), new GregorianCalendar(2016, Calendar.FEBRUARY, 14), null);
        calculateAnzWochenCommand = new CalculateAnzWochenCommand(erstesSemester, kursanmeldung);
        calculateAnzWochenCommand.execute();
        assertEquals(22, calculateAnzWochenCommand.getAnzahlWochen());

        // Abmeldung am letzten Kurstag
        kursanmeldung = new Kursanmeldung(null, kurs, new GregorianCalendar(2015, Calendar.AUGUST, 17), new GregorianCalendar(2016, Calendar.FEBRUARY, 10), null);
        calculateAnzWochenCommand = new CalculateAnzWochenCommand(erstesSemester, kursanmeldung);
        calculateAnzWochenCommand.execute();
        assertEquals(22, calculateAnzWochenCommand.getAnzahlWochen());

        // Frühere Abmeldung
        kursanmeldung = new Kursanmeldung(null, kurs, new GregorianCalendar(2015, Calendar.AUGUST, 17), new GregorianCalendar(2016, Calendar.FEBRUARY, 9), null);
        calculateAnzWochenCommand = new CalculateAnzWochenCommand(erstesSemester, kursanmeldung);
        calculateAnzWochenCommand.execute();
        assertEquals(21, calculateAnzWochenCommand.getAnzahlWochen());

        // nach Weihnachtsferien
        kursanmeldung = new Kursanmeldung(null, kurs, new GregorianCalendar(2015, Calendar.AUGUST, 17), new GregorianCalendar(2016, Calendar.JANUARY, 9), null);
        calculateAnzWochenCommand = new CalculateAnzWochenCommand(erstesSemester, kursanmeldung);
        calculateAnzWochenCommand.execute();
        assertEquals(17, calculateAnzWochenCommand.getAnzahlWochen());

        // vor Weihnachtsferien
        kursanmeldung = new Kursanmeldung(null, kurs, new GregorianCalendar(2015, Calendar.AUGUST, 17), new GregorianCalendar(2015, Calendar.DECEMBER, 19), null);
        calculateAnzWochenCommand = new CalculateAnzWochenCommand(erstesSemester, kursanmeldung);
        calculateAnzWochenCommand.execute();
        assertEquals(16, calculateAnzWochenCommand.getAnzahlWochen());

        // 2. Semester
        kursanmeldung = new Kursanmeldung(null, kurs, new GregorianCalendar(2016, Calendar.FEBRUARY, 29), new GregorianCalendar(2016, Calendar.JULY, 16), null);
        calculateAnzWochenCommand = new CalculateAnzWochenCommand(zweitesSemester, kursanmeldung);
        calculateAnzWochenCommand.execute();
        assertEquals(18, calculateAnzWochenCommand.getAnzahlWochen());

        // Spätere Anmeldung
        // Vor Frühlingsferien
        kursanmeldung = new Kursanmeldung(null, kurs, new GregorianCalendar(2016, Calendar.APRIL, 20), new GregorianCalendar(2016, Calendar.JULY, 16), null);
        calculateAnzWochenCommand = new CalculateAnzWochenCommand(zweitesSemester, kursanmeldung);
        calculateAnzWochenCommand.execute();
        assertEquals(11, calculateAnzWochenCommand.getAnzahlWochen());

        // Nach Frühlingsferien
        kursanmeldung = new Kursanmeldung(null, kurs, new GregorianCalendar(2016, Calendar.MAY, 17), new GregorianCalendar(2016, Calendar.JULY, 16), null);
        calculateAnzWochenCommand = new CalculateAnzWochenCommand(zweitesSemester, kursanmeldung);
        calculateAnzWochenCommand.execute();
        assertEquals(9, calculateAnzWochenCommand.getAnzahlWochen());

        // Frühere Abmeldung
        // Vor Frühlingsferien
        kursanmeldung = new Kursanmeldung(null, kurs, new GregorianCalendar(2016, Calendar.FEBRUARY, 29), new GregorianCalendar(2016, Calendar.APRIL, 2), null);
        calculateAnzWochenCommand = new CalculateAnzWochenCommand(zweitesSemester, kursanmeldung);
        calculateAnzWochenCommand.execute();
        assertEquals(5, calculateAnzWochenCommand.getAnzahlWochen());

        // Nach Frühlingsferien
        kursanmeldung = new Kursanmeldung(null, kurs, new GregorianCalendar(2016, Calendar.FEBRUARY, 29), new GregorianCalendar(2016, Calendar.JUNE, 18), null);
        calculateAnzWochenCommand = new CalculateAnzWochenCommand(zweitesSemester, kursanmeldung);
        calculateAnzWochenCommand.execute();
        assertEquals(14, calculateAnzWochenCommand.getAnzahlWochen());
    }
}