package ch.metzenthin.svm.persistence.entities;

import ch.metzenthin.svm.common.dataTypes.Semesterbezeichnung;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;

/**
 * @author Martin Schraner
 */
public class SemesterTest {

    @Test
    public void testGetAnzahlSchulwochen() throws Exception {
        Semester semester = new Semester("2015/16", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(2015, Calendar.AUGUST, 17), new GregorianCalendar(2016, Calendar.FEBRUARY, 13));
        assertEquals(22, semester.getAnzahlSchulwochen());

        semester = new Semester("2015/16", Semesterbezeichnung.ZWEITES_SEMESTER, new GregorianCalendar(2016, Calendar.FEBRUARY, 29), new GregorianCalendar(2016, Calendar.JULY, 16));
        assertEquals(18, semester.getAnzahlSchulwochen());
    }
}