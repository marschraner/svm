package ch.metzenthin.svm.common.utils;

import java.util.Calendar;

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
        if (anO == null) {
            return anotherO == null;
        }
        String anotherOString = nullAsEmptyString(Converter.asString(anotherO));
        return anO.equals(anotherOString);
    }

}
