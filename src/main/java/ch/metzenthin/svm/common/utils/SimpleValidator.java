package ch.metzenthin.svm.common.utils;

import java.sql.Time;
import java.util.Calendar;

import static ch.metzenthin.svm.common.utils.Converter.DD_MM_YYYY_DATE_FORMAT_STRING;
import static ch.metzenthin.svm.common.utils.Converter.nullAsEmptyString;

/**
 * @author Hans Stamm
 */
public class SimpleValidator {

    private SimpleValidator() {
    }

    public static boolean checkNotNull(Object o) {
        return (o != null);
    }

    public static boolean checkNotEmpty(String s) {
        return checkNotNull(s) && !s.isEmpty();
    }

    public static boolean checkNumber(String s) {
        if (checkNotEmpty(s)) {
            try {
                Integer.valueOf(s);
                return true;
            } catch (NumberFormatException ignore) {
                return false;
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

    public static boolean isTimePeriodValid(Time time1, Time time2) {
        int hour1 = Integer.parseInt(time1.toString().substring(0, 2));
        int min1 = Integer.parseInt(time1.toString().substring(3, 5));
        int hour2 = Integer.parseInt(time2.toString().substring(0, 2));
        int min2 = Integer.parseInt(time2.toString().substring(3, 5));
        // Zeitperioden über Mitternacht nicht erlaubt
        if (hour2 < hour1) {
            return false;
        }
        return hour1 != hour2 || min2 > min1;
    }

}
