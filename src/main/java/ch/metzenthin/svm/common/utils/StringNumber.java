package ch.metzenthin.svm.common.utils;

import java.util.Comparator;

/**
 * Für Strings, bei denen Zahlen numerisch Sortiert werden sollen.
 *
 * @author Martin Schraner
 */
public class StringNumber implements Comparable<StringNumber> {

    private final String stringNumber;

    public StringNumber(String stringNumber) {
        this.stringNumber = stringNumber;
    }

    public String getStringNumber() {
        return stringNumber;
    }

    @Override
    public int compareTo(StringNumber otherStringNumber) {
        Comparator<String> stringNumberComparator = new StringNumberComparator();
        return stringNumberComparator.compare(stringNumber, otherStringNumber.getStringNumber());
    }

    @Override
    public String toString() {
        return stringNumber;
    }
}
