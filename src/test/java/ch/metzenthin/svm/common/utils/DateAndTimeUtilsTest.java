package ch.metzenthin.svm.common.utils;

import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static ch.metzenthin.svm.common.utils.DateAndTimeUtils.*;
import static org.junit.Assert.*;

/**
 * @author Martin Schraner
 */
public class DateAndTimeUtilsTest {

    @Test
    public void testGetNumberOfDaysOfPeriod() throws Exception {
        assertEquals(0, getNumberOfDaysOfPeriod(new GregorianCalendar(2014, Calendar.MARCH, 30), new GregorianCalendar(2014, Calendar.MARCH, 30)));
        assertEquals(1, getNumberOfDaysOfPeriod(new GregorianCalendar(2014, Calendar.MARCH, 30), new GregorianCalendar(2014, Calendar.MARCH, 31)));
        assertEquals(2, getNumberOfDaysOfPeriod(new GregorianCalendar(2014, Calendar.MARCH, 30), new GregorianCalendar(2014, Calendar.APRIL, 1)));
        assertEquals(365, getNumberOfDaysOfPeriod(new GregorianCalendar(2014, Calendar.MARCH, 30), new GregorianCalendar(2015, Calendar.MARCH, 30)));
        assertEquals(3652, getNumberOfDaysOfPeriod(new GregorianCalendar(2004, Calendar.MARCH, 30), new GregorianCalendar(2014, Calendar.MARCH, 30)));
    }

    @Test
    public void testGetNumberOfWeeksBetween() throws Exception {
        assertEquals(0, getNumberOfWeeksBetween(new GregorianCalendar(2015, Calendar.JULY, 1), new GregorianCalendar(2015, Calendar.JULY, 1)));
        assertEquals(1, getNumberOfWeeksBetween(new GregorianCalendar(2015, Calendar.JULY, 1), new GregorianCalendar(2015, Calendar.JULY, 2)));
        assertEquals(1, getNumberOfWeeksBetween(new GregorianCalendar(2015, Calendar.JULY, 1), new GregorianCalendar(2015, Calendar.JULY, 8)));
        assertEquals(2, getNumberOfWeeksBetween(new GregorianCalendar(2015, Calendar.JULY, 1), new GregorianCalendar(2015, Calendar.JULY, 9)));
    }
}