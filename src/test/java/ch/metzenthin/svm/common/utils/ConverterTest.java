package ch.metzenthin.svm.common.utils;

import org.junit.Test;

import java.text.ParseException;
import java.util.Calendar;

import static ch.metzenthin.svm.common.utils.Converter.*;
import static org.junit.Assert.*;

/**
 * @author Hans Stamm
 */
public class ConverterTest {

    @Test
    public void testToIntegerOrNull_Number() throws Exception {
        assertEquals("Resultat ist nicht 44", new Integer(44), toIntegerOrNull("44"));
    }

    @Test
    public void testToIntegerOrNull_Null() throws Exception {
        assertNull("Resultat ist nicht null", toIntegerOrNull("xx"));
    }

    @Test
    public void testToInteger_Number() throws Exception {
        assertEquals("Resultat ist nicht 44", new Integer(44), toInteger("44"));
    }

    @Test
    public void testToInteger_Exception() throws Exception {
        try {
            toInteger("xx");
            fail("NumberFormatException erwartet");
        } catch (NumberFormatException ignore) {
        }
    }

    @Test
    public void testToCalendarIgnoreException_Null() {
        assertNull("null erwartet", toCalendarIgnoreException("xxx"));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void testToCalendarIgnoreException_NotNull() {
        assertEquals("Datum erwartet", -3600000L, toCalendarIgnoreException("01.01.1970").getTimeInMillis());
    }

    @Test
    public void testToCalendar_Exception() {
        try {
            toCalendar("xxx");
            fail("ParseException erwartet");
        } catch (ParseException ignore) {
        }
    }

    @Test
    public void testAsString_Null() {
        assertNull("Null erwartet", asString(null));
    }

    @Test
    public void testAsString_NotNull() {
        Calendar calendar = toCalendarIgnoreException("01.01.2015");
        assertEquals("\"01.01.2015\" erwartet", "01.01.2015", asString(calendar));
    }

   @Test(expected = ParseException.class)
    public void test_Lenient_Order() throws ParseException {
        toCalendar("2015.01.01");
    }

   @Test(expected = ParseException.class)
    public void test_Lenient_Month() throws ParseException {
        toCalendar("31.04.2001");
    }

}
