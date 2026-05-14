package ch.metzenthin.svm.common.utils;

import static ch.metzenthin.svm.common.utils.DateAndTimeUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Calendar;
import java.util.GregorianCalendar;
import org.junit.jupiter.api.Test;

/**
 * @author Martin Schraner
 */
class DateAndTimeUtilsTest {

  @Test
  void testGetNumberOfDaysOfPeriod() {
    assertEquals(
        0,
        getNumberOfDaysOfPeriod(
            new GregorianCalendar(2014, Calendar.MARCH, 30),
            new GregorianCalendar(2014, Calendar.MARCH, 30)));
    assertEquals(
        1,
        getNumberOfDaysOfPeriod(
            new GregorianCalendar(2014, Calendar.MARCH, 30),
            new GregorianCalendar(2014, Calendar.MARCH, 31)));
    assertEquals(
        2,
        getNumberOfDaysOfPeriod(
            new GregorianCalendar(2014, Calendar.MARCH, 30),
            new GregorianCalendar(2014, Calendar.APRIL, 1)));
    assertEquals(
        365,
        getNumberOfDaysOfPeriod(
            new GregorianCalendar(2014, Calendar.MARCH, 30),
            new GregorianCalendar(2015, Calendar.MARCH, 30)));
    assertEquals(
        3652,
        getNumberOfDaysOfPeriod(
            new GregorianCalendar(2004, Calendar.MARCH, 30),
            new GregorianCalendar(2014, Calendar.MARCH, 30)));
  }

  @Test
  void testGetNumberOfWeeksBetween() {
    assertEquals(
        0,
        getNumberOfWeeksBetween(
            new GregorianCalendar(2015, Calendar.JULY, 1),
            new GregorianCalendar(2015, Calendar.JULY, 1)));
    assertEquals(
        1,
        getNumberOfWeeksBetween(
            new GregorianCalendar(2015, Calendar.JULY, 1),
            new GregorianCalendar(2015, Calendar.JULY, 2)));
    assertEquals(
        1,
        getNumberOfWeeksBetween(
            new GregorianCalendar(2015, Calendar.JULY, 1),
            new GregorianCalendar(2015, Calendar.JULY, 8)));
    assertEquals(
        2,
        getNumberOfWeeksBetween(
            new GregorianCalendar(2015, Calendar.JULY, 1),
            new GregorianCalendar(2015, Calendar.JULY, 9)));
  }

  @Test
  void testGetCalendarAsDDMMYYYY() {
    Calendar calendar = new GregorianCalendar(2014, Calendar.JUNE, 24);
    assertEquals("24.06.2014", getCalendarAsDDMMYYYY(calendar));
  }
}
