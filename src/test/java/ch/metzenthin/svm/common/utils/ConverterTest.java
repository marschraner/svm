package ch.metzenthin.svm.common.utils;

import static ch.metzenthin.svm.common.utils.Converter.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.sql.Time;
import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.junit.jupiter.api.Test;

/**
 * @author Hans Stamm
 */
class ConverterTest {

  @Test
  void testToIntegerOrNull_Number() {
    assertEquals(Integer.valueOf(44), toIntegerOrNull("44"), "Resultat ist nicht 44");
  }

  @Test
  void testToIntegerOrNull_Null() {
    assertNull(toIntegerOrNull("xx"), "Resultat ist nicht null");
  }

  @Test
  void testToInteger_Number() {
    assertEquals(Integer.valueOf(44), toInteger("44"), "Resultat ist nicht 44");
  }

  @Test
  void testToInteger_Exception() {
    try {
      toInteger("xx");
      fail("NumberFormatException erwartet");
    } catch (NumberFormatException ignore) {
      // Nothing to do
    }
  }

  @Test
  void testToCalendarIgnoreException_Null() {
    assertNull(toCalendarIgnoreException("xxx"), "null erwartet");
  }

  @SuppressWarnings("ConstantConditions")
  @Test
  void testToCalendarIgnoreException_NotNull() {
    assertEquals(
        -3600000L, toCalendarIgnoreException("01.01.1970").getTimeInMillis(), "Datum erwartet");
  }

  @Test
  void testToCalendar_Exception() {
    try {
      toCalendar("xxx");
      fail("ParseException erwartet");
    } catch (ParseException ignore) {
      // Nothing to do
    }
  }

  @Test
  void testAsString_NotNull() {
    Calendar calendar = toCalendarIgnoreException("01.01.2015");
    assertEquals("01.01.2015", asString(calendar), "\"01.01.2015\" erwartet");
  }

  @Test
  void test_Lenient_Order() {
    assertThrows(ParseException.class, () -> toCalendar("2015.01.01"));
  }

  @Test
  void test_Lenient_Month() {
    assertThrows(ParseException.class, () -> toCalendar("31.04.2001"));
  }

  @Test
  void test_Lenient_Year_Zweistellig_1900() throws ParseException {
    assertEquals("30.04.1941", asString(toCalendar("30.04.41")));
  }

  @Test
  void test_Lenient_Year_Zweistellig_2000() throws ParseException {
    assertEquals("30.04.2015", asString(toCalendar("30.04.15")));
  }

  @Test
  void test_Lenient_Year_Zweistellig_Jahr_2000() throws ParseException {
    assertEquals("30.04.2000", asString(toCalendar("30.04.00")));
  }

  @Test
  void test_MM_YYYY() throws ParseException {
    assertEquals("11.2011", asString(toCalendar("11.2011", "MM.yyyy"), "MM.yyyy"));
  }

  @Test
  void test_MM_YYYY_Zweistellig() throws ParseException {
    assertEquals("11.2011", asString(toCalendar("11.11", "MM.yyyy"), "MM.yyyy"));
  }

  @Test
  void test_MM_YYYY_Mit_Tag() {
    assertThrows(
        ParseException.class, () -> asString(toCalendar("11.11.2011", "MM.yyyy"), "MM.yyyy"));
  }

  @Test
  void test_MM_YYYY_Zweistellig_Mit_Tag() {
    assertThrows(
        ParseException.class, () -> asString(toCalendar("11.11.11", "MM.yyyy"), "MM.yyyy"));
  }

  @Test
  void testDetermineDateFormatString_dd_MM() throws ParseException {
    assertEquals("dd.MM.", determineDateFormatString("12.12."));
  }

  @Test
  void testDetermineDateFormatString_dd_MM_Einstellig() throws ParseException {
    assertEquals("dd.MM.", determineDateFormatString("1.1."));
  }

  @Test
  void testDetermineDateFormatString_MM_yyyy() throws ParseException {
    assertEquals("MM.yyyy", determineDateFormatString("12.2012"));
  }

  @Test
  void testDetermineDateFormatString_MM_yyyy_Monat_einstellig() throws ParseException {
    assertEquals("MM.yyyy", determineDateFormatString("1.2012"));
  }

  @Test
  void testDetermineDateFormatString_MM_yyyy_Jahr_zweistellig() throws ParseException {
    assertEquals("MM.yyyy", determineDateFormatString("12.12"));
  }

  @Test
  void testDetermineDateFormatString_dd_MM_yyyy() throws ParseException {
    assertEquals("dd.MM.yyyy", determineDateFormatString("12.12.2012"));
  }

  @Test
  void testDetermineDateFormatString_dd_MM_yyyy_Tag_Monat_einstellig() throws ParseException {
    assertEquals("dd.MM.yyyy", determineDateFormatString("1.1.2012"));
  }

  @Test
  void testDetermineDateFormatString_dd_MM_yyyy_Jahr_zweistellig() throws ParseException {
    assertEquals("dd.MM.yyyy", determineDateFormatString("12.12.12"));
  }

  @Test
  void testDetermineDateFormatString_yyyy_Jahr() throws ParseException {
    assertEquals("yyyy", determineDateFormatString("2012"));
  }

  @Test
  void testDetermineDateFormatString_yyyy_Jahr_zweistellig() throws ParseException {
    assertEquals("yyyy", determineDateFormatString("12"));
  }

  @Test
  void testDetermineDateFormatString_falscher_Separator() {
    assertThrows(ParseException.class, () -> determineDateFormatString("12/2012"));
  }

  @Test
  void testToTime() throws ParseException {
    assertEquals(Time.valueOf("12:13:00"), toTime("12.13"));
  }

  @Test
  void testToTime_invalidHh() {
    assertThrows(ParseException.class, () -> toTime("24.59"));
  }

  @Test
  void testToTime_invalidMm() {
    assertThrows(ParseException.class, () -> toTime("23.60"));
  }

  @Test
  void testCalendarToDdMmYy() {
    assertEquals("18.06.87", calendarToDdMmYy(new GregorianCalendar(1987, Calendar.JUNE, 18)));
  }

  @Test
  void testEmptyStringAsNull_NotEmpty() {
    assertEquals("abc", emptyStringAsNull("abc"));
  }

  @Test
  @SuppressWarnings("ConstantValue")
  void testEmptyStringAsNull_Null() {
    assertNull(emptyStringAsNull(null));
  }

  @Test
  void testEmptyStringAsNull_Empty() {
    assertNull(emptyStringAsNull(""));
  }

  @Test
  @SuppressWarnings({"ObviousNullCheck"})
  void testNullAsEmptyString_NotEmpty() {
    assertEquals("abc", nullAsEmptyString("abc"));
  }

  @Test
  void testNullAsEmptyString_Null() {
    //noinspection ConstantConditions
    assertTrue(nullAsEmptyString(null).isEmpty());
  }

  @Test
  @SuppressWarnings({"ConstantValue", "ObviousNullCheck"})
  void testNullAsEmptyString_Empty() {
    assertTrue(nullAsEmptyString("").isEmpty());
  }

  @Test
  void testAsStringNullSafeNullSafe() {
    assertNull(Converter.asStringNullSafe(null));
    assertEquals("1.23", Converter.asStringNullSafe(BigDecimal.valueOf(1.23)));
    assertEquals(Integer.toString(1000), Converter.asStringNullSafe(1000));
  }
}
