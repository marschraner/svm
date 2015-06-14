package ch.metzenthin.svm.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;
import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNumber;

/**
 * @author Hans Stamm
 */
public class Converter {

    public static final String DATE_FORMAT_STRING = "dd.MM.yyyy";

    public static Integer toIntegerOrNull(String s) {
        Integer i = null;
        if (checkNumber(s)) {
            i = toInteger(s);
        }
        return i;
    }

    public static Integer toInteger(String s) {
        return Integer.valueOf(s);
    }

    public static Calendar toCalendar(String s) throws ParseException {
        if (!checkNotEmpty(s)) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT_STRING);
        formatter.setLenient(false);
        calendar.setTime(formatter.parse(s));
        formatter.setLenient(true);
        return calendar;
    }

    public static Calendar toCalendarIgnoreException(String s) {
        if (!checkNotEmpty(s)) {
            return null;
        }
        Calendar calendar = null;
        try {
            calendar = toCalendar(s);
        } catch (ParseException ignore) {
        }
        return calendar;
    }

    public static String asString(Calendar calendar) {
        if (calendar == null) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT_STRING);
        return formatter.format(calendar.getTime());
    }

    public static String emptyStringAsNull(String s) {
        if ((s == null) || s.isEmpty()) {
            return null;
        }
        return s;
    }

    public static String nullAsEmptyString(String s) {
        if (s == null) {
            return "";
        }
        return s;
    }

}
