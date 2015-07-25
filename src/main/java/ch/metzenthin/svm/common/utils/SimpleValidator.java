package ch.metzenthin.svm.common.utils;

import java.sql.Time;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static ch.metzenthin.svm.common.utils.Converter.DD_MM_YYYY_DATE_FORMAT_STRING;
import static ch.metzenthin.svm.common.utils.Converter.nullAsEmptyString;

/**
 * @author Hans Stamm
 */
public class SimpleValidator {

    public static boolean checkNotNull(Object o) {
        return (o != null);
    }

    public static boolean checkNotEmpty(String s) {
        return checkNotNull(s) && !s.isEmpty();
    }

    public static boolean checkNumber(String s) {
        if (checkNotEmpty(s)) {
            try {
                //noinspection ResultOfMethodCallIgnored
                Integer.valueOf(s);
                return true;
            } catch (NumberFormatException ignore) {
            }
        }
        return false;
    }

    public static boolean equalsNullSafe(Object anO, Object anotherO) {
        if (anO == null) {
            return anotherO == null;
        }
        return anO.equals(anotherO);
    }

    public static boolean equalsNullSafe(String anO, Calendar anotherO) {
        return equalsNullSafe(anO, anotherO, DD_MM_YYYY_DATE_FORMAT_STRING);
    }

    public static boolean equalsNullSafe(String anO, Calendar anotherO, String dateFormat) {
        if (anO == null) {
            return anotherO == null;
        }
        String anotherOString = nullAsEmptyString(Converter.asString(anotherO, dateFormat));
        return anO.equals(anotherOString);
    }

    public static boolean checkIfTwoPeriodsOverlap(Calendar startTime1, Calendar endTime1, Calendar startTime2, Calendar endTime2) {
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
		if (startTime1.equals(endTime2) || endTime1.equals(startTime2)) {
			return true;
		}
		return false;
	}

    public static int getNumberOfWeeksBetween(Calendar a, Calendar b) {
        if (a == null || b == null) {
            throw new RuntimeException("Start- oder Enddatum der Periode nicht gesetzt");
        }

        if (a.before(a)) {
            return -getNumberOfWeeksBetween(b, a);
        }

        Calendar cal = new GregorianCalendar(a.get(Calendar.YEAR), a.get(Calendar.MONTH), a.get(Calendar.DAY_OF_MONTH));
        int weeks = 0;
        while (cal.before(b)) {
            // add another week
            cal.add(Calendar.WEEK_OF_YEAR, 1);
            weeks++;
        }
        return weeks;
    }

    public static boolean isTimePeriodValid(Time time1, Time time2) {
        int hour1 = Integer.parseInt(time1.toString().substring(0, 2));
        int min1 = Integer.parseInt(time1.toString().substring(3, 5));
        int hour2 = Integer.parseInt(time2.toString().substring(0, 2));
        int min2 = Integer.parseInt(time2.toString().substring(3, 5));
        // Zeitperioden über Mitternacht nicht erlaubt
        if (hour2 < hour1) {
            return false;
        }
        if (hour1 == hour2 && min2 <= min1) {
            return false;
        }
        return true;
    }

}
