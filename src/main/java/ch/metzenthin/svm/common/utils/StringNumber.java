package ch.metzenthin.svm.common.utils;

import java.util.Comparator;

/**
 * Für Strings, bei denen Zahlen numerisch Sortiert werden sollen.
 *
 * @author Martin Schraner
 */
public record StringNumber(String stringNumber) implements Comparable<StringNumber> {

    @Override
    public int compareTo(StringNumber otherStringNumber) {
        Comparator<String> stringNumberComparator = new StringNumberComparator();
        return stringNumberComparator.compare(stringNumber, otherStringNumber.stringNumber());
    }

    @Override
    public String toString() {
        return stringNumber;
    }
}
