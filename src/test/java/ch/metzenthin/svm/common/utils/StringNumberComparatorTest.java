package ch.metzenthin.svm.common.utils;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Martin Schraner
 */
public class StringNumberComparatorTest {

    @Test
    public void testCompare() {
        List<String> strings = Arrays.asList("2. Klasse", "Tanztaube 3", "Schulkind 10", "Schulkind 2", "1./2. Klasse", "Tanztaube 7", "Aschenputtel", "10. Klasse", "Vorkindergarten", "1. Klasse");
        Collections.sort(strings, new StringNumberComparator());

        List<String> stringsOrderedExpected = Arrays.asList("Aschenputtel", "Schulkind 2", "Schulkind 10", "Tanztaube 3", "Tanztaube 7", "Vorkindergarten", "1. Klasse", "1./2. Klasse", "2. Klasse", "10. Klasse");
        for (int i = 0; i < strings.size(); i++) {
            assertEquals(strings.get(i), stringsOrderedExpected.get(i));
        }
    }

}