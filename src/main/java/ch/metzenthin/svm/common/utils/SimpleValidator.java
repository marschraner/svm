package ch.metzenthin.svm.common.utils;

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

}
