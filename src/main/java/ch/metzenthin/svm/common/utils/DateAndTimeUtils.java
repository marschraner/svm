package ch.metzenthin.svm.common.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * @author Martin Schraner
 */
public class DateAndTimeUtils {

  private DateAndTimeUtils() {}

  public static boolean checkIfTwoPeriodsOverlap(
      Calendar startTime1, Calendar endTime1, Calendar startTime2, Calendar endTime2) {
    // EndTime1 = null oder endTime2 = null bedeutet, dass die Perioden offen sind
    if (endTime1 == null && endTime2 == null) {
      return true;
    }
    if (endTime1 == null) {
      return !endTime2.before(startTime1);
    }
    if (endTime2 == null) {
      return !endTime1.before(startTime2);
    }
    if (startTime1.before(endTime2) && endTime1.after(startTime2)) {
      return true;
    }
    return startTime1.equals(endTime2) || endTime1.equals(startTime2);
  }

  @SuppressWarnings("java:S2234")
  public static int getNumberOfDaysOfPeriod(Calendar a, Calendar b) {
    if (a == null || b == null) {
      throw new IllegalArgumentException("Start- oder Enddatum der Periode nicht gesetzt");
    }

    if (b.before(a)) {
      return -getNumberOfDaysOfPeriod(b, a);
    }
    return (int)
        (((b.getTimeInMillis() - a.getTimeInMillis()) + 3600000)
            / 86400000); // + 366000000 um auch bei Zeitumstellung korrekten Wert zu erhalten
  }

  @SuppressWarnings("java:S2234")
  public static int getNumberOfWeeksBetween(Calendar a, Calendar b) {
    if (a == null || b == null) {
      throw new IllegalArgumentException("Start- oder Enddatum der Periode nicht gesetzt");
    }

    if (b.before(a)) {
      return -getNumberOfWeeksBetween(b, a);
    }

    Calendar cal =
        new GregorianCalendar(
            a.get(Calendar.YEAR), a.get(Calendar.MONTH), a.get(Calendar.DAY_OF_MONTH));
    int weeks = 0;
    while (cal.before(b)) {
      // add another week
      cal.add(Calendar.WEEK_OF_YEAR, 1);
      weeks++;
    }
    return weeks;
  }

  public static String getCalendarAsDDMMYYYY(Calendar calendar) {
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    return sdf.format(calendar.getTime());
  }
}
