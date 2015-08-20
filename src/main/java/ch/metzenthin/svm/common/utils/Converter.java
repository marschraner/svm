package ch.metzenthin.svm.common.utils;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;
import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNumber;

/**
 * @author Hans Stamm
 */
public class Converter {

    public static final String DD_MM_YYYY_DATE_FORMAT_STRING = "dd.MM.yyyy";

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
        return toCalendar(s, DD_MM_YYYY_DATE_FORMAT_STRING);
    }


    public static Calendar toCalendar(String s, String dateFormatString) throws ParseException {
        String errMsg = "Kein gültiges Datum im Format '" + getDeutscheBezeichnungOfDateFormatString(dateFormatString) + "'";
        if (!checkNotEmpty(s)) {
            return null;
        }
        // Verhindern, dass für einen dateFormatString MM.yyyy Eingaben wie 11.09,2011 akzeptiert werden (-> 11.2009 !)
        if (getCharacterOccurrencesInString(s, '.') != getCharacterOccurrencesInString(dateFormatString, '.')) {
            throw new ParseException(errMsg, 0);
        }
        // Akzeptiere 00 als Kürzel für 2000 (führt sonst zu ParseException)
        s = s.replaceAll("\\.00", ".2000");
        // Schärfere Prüfung, ob Datenformat korrekt, als mit formatter möglich
        try {
            determineDateFormatString(s);
        } catch (ParseException e) {
            throw new ParseException(errMsg, 0);
        }
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormatString);
        try {
            formatter.parse(s);
        } catch (ParseException e) {
            throw new ParseException(errMsg, 0);
        }
        formatter.setLenient(false);
        Date parsedDate;
        try {
            parsedDate = formatter.parse(s);
        } catch (ParseException e) {
            throw new ParseException("Kein gültiges Datum", 0);
        }
        calendar.setTime(parsedDate);
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

    public static String determineDateFormatString(String dateAsString) throws ParseException {
        if (dateAsString.matches("\\d{1,2}\\.\\d{1,2}\\.")) {
            return "dd.MM.";
        }
        if (dateAsString.matches("\\d{1,2}\\.\\d{2}")) {
            return "MM.yyyy";
        }
        if (dateAsString.matches("\\d{1,2}\\.\\d{4}")) {
            return "MM.yyyy";
        }
        if (dateAsString.matches("\\d{1,2}\\.\\d{1,2}\\.\\d{2}")) {
            return "dd.MM.yyyy";
        }
        if (dateAsString.matches("\\d{1,2}\\.\\d{1,2}\\.\\d{4}")) {
            return "dd.MM.yyyy";
        }
        if (dateAsString.matches("\\d{4}")) {
            return "yyyy";
        }
        if (dateAsString.matches("\\d{2}")) {
            return "yyyy";
        }
        throw new ParseException("Ungültiges Datenformat", 0);
    }

    public static String getDeutscheBezeichnungOfDateFormatString(String dateFormatString) {
        dateFormatString = dateFormatString.replace('y', 'J');
        return dateFormatString.replace('d', 'T');
    }

    public static String asString(Time time) {
        if (time == null) {
            return "";
        }
        return time.toString().substring(0, 2) + "." + time.toString().substring(3, 5);
    }

    public static Time toTime(String s) throws ParseException {
        String errMsg = "Keine gültige Zeit im Format 'HH.MM'";
        if (!checkNotEmpty(s)) {
            return null;
        }
        if (s.length() != 5 || s.charAt(2) != '.') {
            throw new ParseException(errMsg, 0);
        }
        int hour;
        int min;
        try {
            hour = Integer.parseInt(s.substring(0, 2));
            min = Integer.parseInt(s.substring(3, 5));
        } catch (NumberFormatException e) {
            throw new ParseException(errMsg, 0);
        }
        if (hour < 0 || hour > 23 || min < 0 || min > 59) {
            throw new ParseException(errMsg, 0);
        }
        return Time.valueOf(s.substring(0, 2) + ":" + s.substring(3, 5) + ":00");
    }

    public static String asString(Calendar calendar) {
        return asString(calendar, DD_MM_YYYY_DATE_FORMAT_STRING);
    }

    public static String asString(Calendar calendar, String dateFormatString) {
        if (calendar == null) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormatString);
        return formatter.format(calendar.getTime());
    }

    public static String calendarToDdMmYy(Calendar calendar) {
        String ddMmYy = asString(calendar, DD_MM_YYYY_DATE_FORMAT_STRING);
        return ddMmYy.substring(0,6) + ddMmYy.substring(8,10);
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

    private static String[] splitPeriode(String periode) throws ParseException {
        if (!checkNotEmpty(periode)) {
            return null;
        }
        if (getCharacterOccurrencesInString(periode, '-') > 1) {
            throw new ParseException("Ungültige Periode: mehr als ein '-'-Zeichen", 0);
        } else if (getCharacterOccurrencesInString(periode, '-') == 1) {
            String periodeBeginn = periode.trim().split("-")[0].trim();
            String periodeEnde = periode.trim().split("-")[1].trim();
            String periodeDateFormatString = determineDateFormatString(periodeBeginn);
            if (!periodeDateFormatString.equals(determineDateFormatString(periodeEnde))) {
                throw new ParseException("Beginn und Ende der Periode inkonsistent", 0);
            }
            if (periodeDateFormatString.equals("dd.MM.")) {
                throw new ParseException("Keine gültige Periode", 0);
            }
            Calendar periodeBeginnAsCalendar = toCalendar(periodeBeginn, periodeDateFormatString);
            Calendar periodeEndeAsCalendar = toCalendar(periodeEnde, periodeDateFormatString);
            if (periodeBeginnAsCalendar != null && periodeEndeAsCalendar != null && !periodeBeginnAsCalendar.before(periodeEndeAsCalendar)) {
                throw new ParseException("Keine gültige Periode", 0);
            }
            return new String[]{periodeBeginn, periodeEnde, periodeDateFormatString};
        } else {
            String periodeDateFormatString = determineDateFormatString(periode);
            return new String[]{periode, periodeDateFormatString};
        }
    }

    public static String getPeriodeBeginn(String periode) throws ParseException {
        if (splitPeriode(periode) == null) {
            return "";
        }
        //noinspection ConstantConditions
        return splitPeriode(periode)[0];
    }

    public static String getPeriodeEnde(String periode) throws ParseException {
        if (splitPeriode(periode) == null) {
            return "";
        }
        //noinspection ConstantConditions
        return ((splitPeriode(periode).length > 2) ? splitPeriode(periode)[1] : "");
    }

    public static String getPeriodeDateFormatString(String periode) throws ParseException {
        if (splitPeriode(periode) == null) {
            return null;
        }
        //noinspection ConstantConditions
        return splitPeriode(periode)[splitPeriode(periode).length - 1];
    }

    public static Calendar getNYearsBeforeNow(int n) {
        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.YEAR, -n);
        return cal;
    }

    public static Calendar getNMonthsBeforeNow(int n) {
        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.MONTH, -n);
        return cal;
    }

    public static Calendar getNMonthsAfterNow(int n) {
        return getNMonthsBeforeNow(-n);
    }

    public static int getCharacterOccurrencesInString(String s, char c) {
        int counter = 0;
        for( int i = 0; i < s.length(); i++ ) {
            if( s.charAt(i) == c ) {
                counter++;
            }
        }
        return counter;
    }

}
