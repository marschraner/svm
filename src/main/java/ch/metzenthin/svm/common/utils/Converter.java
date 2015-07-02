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
        // Akzeptiere 00 als Kürzel für 2000 (führt sonst zu ParseException)
        s = s.replaceAll("\\.00", ".2000");
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT_STRING);
        formatter.setLenient(false);
        calendar.setTime(formatter.parse(s));
        if (calendar.get(Calendar.YEAR) < 100) {
            if (calendar.get(Calendar.YEAR) < 40) {
                calendar.add(Calendar.YEAR, 2000);
            } else {
                calendar.add(Calendar.YEAR, 1900);
            }
        }
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

    private static String[] splitStrasseHausnummer(String strasseHausnummer) {
        if (strasseHausnummer == null) {
            return null;
        }
        String[] splitted = strasseHausnummer.trim().split("\\s+");
        // Prüfen, ob mindestens 2 Felder und ob letztes mit Zahlen beginnt
        if (splitted.length > 1 && splitted[splitted.length - 1].matches("\\d+.*")) {
            StringBuilder strasse = new StringBuilder(splitted[0]);
            for (int i = 1; i < splitted.length - 1; i++) {
                strasse.append(" ").append(splitted[i]);
            }
            return new String[]{strasse.toString(), splitted[splitted.length - 1]};
        } else {
            return new String[]{strasseHausnummer};
        }
    }

    public static String strasseHausnummerGetStrasse(String strasseHausnummer) {
        return (splitStrasseHausnummer(strasseHausnummer) == null ? null : splitStrasseHausnummer(strasseHausnummer)[0]);
    }

    public static String strasseHausnummerGetHausnummer(String strasseHausnummer) {
        if (splitStrasseHausnummer(strasseHausnummer) == null) {
            return null;
        }
        return (splitStrasseHausnummer(strasseHausnummer).length > 1 ? splitStrasseHausnummer(strasseHausnummer)[1] : "");
    }

}
