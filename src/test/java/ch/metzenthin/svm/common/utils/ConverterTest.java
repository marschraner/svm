package ch.metzenthin.svm.common.utils;

import org.junit.Test;

import java.sql.Time;
import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static ch.metzenthin.svm.common.utils.Converter.*;
import static org.junit.Assert.*;

/**
 * @author Hans Stamm
 */
public class ConverterTest {

    @Test
    public void testToIntegerOrNull_Number() {
        assertEquals("Resultat ist nicht 44", Integer.valueOf(44), toIntegerOrNull("44"));
    }

    @Test
    public void testToIntegerOrNull_Null() {
        assertNull("Resultat ist nicht null", toIntegerOrNull("xx"));
    }

    @Test
    public void testToInteger_Number() {
        assertEquals("Resultat ist nicht 44", Integer.valueOf(44), toInteger("44"));
    }

    @Test
    public void testToInteger_Exception() {
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

   @Test
    public void test_Lenient_Year_Zweistellig_1900() throws ParseException {
       assertEquals("30.04.1941", asString(toCalendar("30.04.41")));
    }

   @Test
    public void test_Lenient_Year_Zweistellig_2000() throws ParseException {
       assertEquals("30.04.2015", asString(toCalendar("30.04.15")));
    }

    @Test
    public void test_Lenient_Year_Zweistellig_Jahr_2000() throws ParseException {
        assertEquals("30.04.2000", asString(toCalendar("30.04.00")));
    }

    @Test
    public void test_MM_YYYY() throws ParseException {
        assertEquals("11.2011", asString(toCalendar("11.2011", "MM.yyyy"),"MM.yyyy"));
    }

    @Test
    public void test_MM_YYYY_Zweistellig() throws ParseException {
        assertEquals("11.2011", asString(toCalendar("11.11", "MM.yyyy"), "MM.yyyy"));
    }

    @Test(expected = ParseException.class)
    public void test_MM_YYYY_Mit_Tag() throws ParseException {
        assertEquals("11.2011", asString(toCalendar("11.11.2011", "MM.yyyy"), "MM.yyyy"));
    }

    @Test(expected = ParseException.class)
    public void test_MM_YYYY_Zweistellig_Mit_Tag() throws ParseException {
        assertEquals("11.2011", asString(toCalendar("11.11.11", "MM.yyyy"), "MM.yyyy"));
    }

    @Test
    public void testDetermineDateFormatString_dd_MM() throws ParseException {
        assertEquals("dd.MM.", determineDateFormatString("12.12."));
    }

    @Test
    public void testDetermineDateFormatString_dd_MM_Einstellig() throws ParseException {
        assertEquals("dd.MM.", determineDateFormatString("1.1."));
    }

    @Test
    public void testDetermineDateFormatString_MM_yyyy() throws ParseException {
        assertEquals("MM.yyyy", determineDateFormatString("12.2012"));
    }

    @Test
    public void testDetermineDateFormatString_MM_yyyy_Monat_einstellig() throws ParseException {
        assertEquals("MM.yyyy", determineDateFormatString("1.2012"));
    }

    @Test
    public void testDetermineDateFormatString_MM_yyyy_Jahr_zweistellig() throws ParseException {
        assertEquals("MM.yyyy", determineDateFormatString("12.12"));
    }

    @Test
    public void testDetermineDateFormatString_dd_MM_yyyy() throws ParseException {
        assertEquals("dd.MM.yyyy", determineDateFormatString("12.12.2012"));
    }

    @Test
    public void testDetermineDateFormatString_dd_MM_yyyy_Tag_Monat_einstellig() throws ParseException {
        assertEquals("dd.MM.yyyy", determineDateFormatString("1.1.2012"));
    }

    @Test
    public void testDetermineDateFormatString_dd_MM_yyyy_Jahr_zweistellig() throws ParseException {
        assertEquals("dd.MM.yyyy", determineDateFormatString("12.12.12"));
    }

    @Test
    public void testDetermineDateFormatString_yyyy_Jahr() throws ParseException {
        assertEquals("yyyy", determineDateFormatString("2012"));
    }

    @Test
    public void testDetermineDateFormatString_yyyy_Jahr_zweistellig() throws ParseException {
        assertEquals("yyyy", determineDateFormatString("12"));
    }

    @Test(expected = ParseException.class)
    public void testDetermineDateFormatString_falscher_Separator() throws ParseException {
        assertNull(determineDateFormatString("12/2012"));
    }

    @Test
    public void testToTime() throws ParseException {
        assertEquals(Time.valueOf("12:13:00"), toTime("12.13"));
    }

    @Test(expected = ParseException.class)
    public void testToTime_invalidHh() throws ParseException {
        toTime("24.59");
    }

    @Test(expected = ParseException.class)
    public void testToTime_invalidMm() throws ParseException {
        toTime("23.60");
    }

    @Test
    public void testCalendarToDdMmYy() {
        assertEquals("18.06.87", calendarToDdMmYy(new GregorianCalendar(1987, Calendar.JUNE, 18)));
    }

    @Test
    public void testEmptyStringAsNull_NotEmpty() {
        assertEquals("abc", emptyStringAsNull("abc"));
    }

    @Test
    @SuppressWarnings("ConstantValue")
    public void testEmptyStringAsNull_Null() {
        assertNull(emptyStringAsNull(null));
    }

    @Test
    public void testEmptyStringAsNull_Empty() {
        assertNull(emptyStringAsNull(""));
    }

    @Test
    @SuppressWarnings({"ObviousNullCheck"})
    public void testNullAsEmptyString_NotEmpty() {
        assertEquals("abc", nullAsEmptyString("abc"));
    }

    @Test
    public void testNullAsEmptyString_Null() {
        //noinspection ConstantConditions
        assertTrue(nullAsEmptyString(null).isEmpty());
    }

    @Test
    @SuppressWarnings({"ConstantValue", "ObviousNullCheck"})
    public void testNullAsEmptyString_Empty() {
        assertTrue(nullAsEmptyString("").isEmpty());
    }

}
