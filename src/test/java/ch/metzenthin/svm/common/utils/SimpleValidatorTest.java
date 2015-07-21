package ch.metzenthin.svm.common.utils;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static ch.metzenthin.svm.common.utils.SimpleValidator.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Hans Stamm
 */
public class SimpleValidatorTest {

    @Test
    public void testCheckNotEmpty_Null() {
        assertFalse("False erwartet (null)", checkNotEmpty(null));
    }

    @Test
    public void testCheckNotEmpty_Empty() {
        assertFalse("False erwartet (empty)", checkNotEmpty(""));
    }

    @Test
    public void testCheckNotEmpty_NotEmpty() {
        assertTrue("True erwartet (not empty)", checkNotEmpty("abc"));
    }

    @Test
    public void testCheckNotNull_Null() {
        assertFalse("False erwartet (null)", checkNotNull(null));
    }

    @Test
    public void testCheckNotNull_NotNullString() {
        assertTrue("True erwartet (String)", checkNotNull("xx"));
    }

    @Test
    public void testCheckNotNull_NotNullEmptyString() {
        assertTrue("True erwartet (empty string)", checkNotNull(""));
    }

    @Test
    public void testCheckNotNull_NotNullDate() {
        assertTrue("True erwartet (Date)", checkNotNull(new Date()));
    }

    @Test
    public void testCheckNumber_Null() {
        assertFalse("False erwartet (null)", checkNumber(null));
    }

    @Test
    public void testCheckNumber_NullEmptyString() {
        assertFalse("False erwartet (empty string)", checkNumber(""));
    }

    @Test
    public void testCheckNumber_NotNumber() {
        assertFalse("False erwartet (not a number)", checkNumber("5.5"));
    }

    @Test
    public void testCheckNumber_Number() {
        assertTrue("True erwartet (number)", checkNumber("5"));
    }

    @Test
    public void testEqualsNullSafe_Equals() throws Exception {
        assertTrue(equalsNullSafe("1", "1"));
    }

    @Test
    public void testEqualsNullSafe_EqualsNullStrings() throws Exception {
        assertTrue(equalsNullSafe("", ""));
    }

    @Test
    public void testEqualsNullSafe_NotEquals() throws Exception {
        assertFalse(equalsNullSafe("1", "2"));
    }

    @Test
    public void testEqualsNullSafe_NotEqualsDifferentObjects() throws Exception {
        assertFalse(equalsNullSafe("1", 1));
    }

    @Test
    public void testEqualsNullSafe_NotEqualsFirstNull() throws Exception {
        assertFalse(equalsNullSafe(null, "2"));
    }

    @Test
    public void testEqualsNullSafe_NotEqualsSecondNull() throws Exception {
        assertFalse(equalsNullSafe("1", null));
    }

    @Test
    public void testEqualsNullSafe_EqualsBothNull() throws Exception {
        assertTrue(equalsNullSafe(null, null));
    }

    @Test
    public void testEqualsNullSafe_EqualsStringEqualsCalendar() throws Exception {
        assertTrue(equalsNullSafe("01.01.2015", Converter.toCalendar("01.01.2015")));
    }

    @Test
    public void testEqualsNullSafe_NotEqualsStringEqualsCalendar() throws Exception {
        assertFalse(equalsNullSafe("01.01.2015", Converter.toCalendar("01.02.2015")));
    }

    @Test
    public void testEqualsNullSafe_NotEqualsStringEqualsNull() throws Exception {
        assertFalse(equalsNullSafe("01.01.2015", null));
    }

    @Test
    public void testEqualsNullSafe_NotEqualsNullEqualsCalendar() throws Exception {
        assertFalse(equalsNullSafe(null, Converter.toCalendar("01.01.2015")));
    }

    @Test
    public void testGetNumberOfWeeksBetween() throws Exception {
        assertEquals(0, getNumberOfWeeksBetween(new GregorianCalendar(2015, Calendar.JULY, 1), new GregorianCalendar(2015, Calendar.JULY, 1)));
        assertEquals(1, getNumberOfWeeksBetween(new GregorianCalendar(2015, Calendar.JULY, 1), new GregorianCalendar(2015, Calendar.JULY, 2)));
        assertEquals(1, getNumberOfWeeksBetween(new GregorianCalendar(2015, Calendar.JULY, 1), new GregorianCalendar(2015, Calendar.JULY, 8)));
        assertEquals(2, getNumberOfWeeksBetween(new GregorianCalendar(2015, Calendar.JULY, 1), new GregorianCalendar(2015, Calendar.JULY, 9)));
    }

}