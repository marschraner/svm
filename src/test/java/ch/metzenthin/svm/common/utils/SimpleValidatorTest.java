package ch.metzenthin.svm.common.utils;

import org.junit.Test;

import java.sql.Time;
import java.util.Date;

import static ch.metzenthin.svm.common.utils.SimpleValidator.*;
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

    @SuppressWarnings("ConstantValue")
    @Test
    public void testCheckNotNull_Null() {
        assertFalse("False erwartet (null)", checkNotNull(null));
    }

    @SuppressWarnings("ConstantValue")
    @Test
    public void testCheckNotNull_NotNullString() {
        assertTrue("True erwartet (String)", checkNotNull("xx"));
    }

    @SuppressWarnings("ConstantValue")
    @Test
    public void testCheckNotNull_NotNullEmptyString() {
        assertTrue("True erwartet (empty string)", checkNotNull(""));
    }

    @SuppressWarnings("ConstantValue")
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
    public void testEqualsNullSafe_Equals() {
        assertTrue(equalsNullSafe("1", "1"));
    }

    @Test
    public void testEqualsNullSafe_EqualsNullStrings() {
        assertTrue(equalsNullSafe("", ""));
    }

    @Test
    public void testEqualsNullSafe_NotEquals() {
        assertFalse(equalsNullSafe("1", "2"));
    }

    @Test
    public void testEqualsNullSafe_NotEqualsDifferentObjects() {
        assertFalse(equalsNullSafe("1", 1));
    }

    @SuppressWarnings("ConstantValue")
    @Test
    public void testEqualsNullSafe_NotEqualsFirstNull() {
        assertFalse(equalsNullSafe(null, "2"));
    }

    @Test
    public void testEqualsNullSafe_NotEqualsSecondNull() {
        assertFalse(equalsNullSafe("1", null));
    }

    @Test
    public void testEqualsNullSafe_EqualsBothNull() {
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
    public void testEqualsNullSafe_NotEqualsStringEqualsNull() {
        assertFalse(equalsNullSafe("01.01.2015", null));
    }

    @Test
    public void testEqualsNullSafe_NotEqualsNullEqualsCalendar() throws Exception {
        assertFalse(equalsNullSafe(null, Converter.toCalendar("01.01.2015")));
    }

    @Test
    public void testIsTimePeriodValid() {
        assertTrue(isTimePeriodValid(Time.valueOf("12:30:00"), Time.valueOf("12:31:00")));
        assertTrue(isTimePeriodValid(Time.valueOf("12:30:00"), Time.valueOf("13:30:00")));
        assertFalse(isTimePeriodValid(Time.valueOf("12:30:00"), Time.valueOf("12:29:00")));
        assertFalse(isTimePeriodValid(Time.valueOf("12:30:00"), Time.valueOf("11:31:00")));
        assertFalse(isTimePeriodValid(Time.valueOf("12:30:00"), Time.valueOf("12:30:00")));
    }

}