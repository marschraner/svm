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
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        calendar.setTime(formatter.parse(s));
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

}
