package ch.metzenthin.svm.common.utils;

import static ch.metzenthin.svm.common.utils.SimpleValidator.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Time;
import java.util.Date;
import org.junit.jupiter.api.Test;

/**
 * @author Hans Stamm
 */
class SimpleValidatorTest {

  @Test
  void testCheckNotEmpty_Null() {
    assertFalse(checkNotEmpty(null), "False erwartet (null)");
  }

  @Test
  void testCheckNotEmpty_Empty() {
    assertFalse(checkNotEmpty(""), "False erwartet (empty)");
  }

  @Test
  void testCheckNotEmpty_NotEmpty() {
    assertTrue(checkNotEmpty("abc"), "True erwartet (not empty)");
  }

  @SuppressWarnings("ConstantValue")
  @Test
  void testCheckNotNull_Null() {
    assertFalse(checkNotNull(null), "False erwartet (null)");
  }

  @SuppressWarnings("ConstantValue")
  @Test
  void testCheckNotNull_NotNullString() {
    assertTrue(checkNotNull("xx"), "True erwartet (String)");
  }

  @SuppressWarnings("ConstantValue")
  @Test
  void testCheckNotNull_NotNullEmptyString() {
    assertTrue(checkNotNull(""), "True erwartet (empty string)");
  }

  @SuppressWarnings("ConstantValue")
  @Test
  void testCheckNotNull_NotNullDate() {
    assertTrue(checkNotNull(new Date()), "True erwartet (Date)");
  }

  @Test
  void testCheckNumber_Null() {
    assertFalse(checkNumber(null), "False erwartet (null)");
  }

  @Test
  void testCheckNumber_NullEmptyString() {
    assertFalse(checkNumber(""), "False erwartet (empty string)");
  }

  @Test
  void testCheckNumber_NotNumber() {
    assertFalse(checkNumber("5.5"), "False erwartet (not a number)");
  }

  @Test
  void testCheckNumber_Number() {
    assertTrue(checkNumber("5"), "True erwartet (number)");
  }

  @Test
  void testEqualsNullSafe_Equals() {
    assertTrue(equalsNullSafe("1", "1"));
  }

  @Test
  void testEqualsNullSafe_EqualsNullStrings() {
    assertTrue(equalsNullSafe("", ""));
  }

  @Test
  void testEqualsNullSafe_NotEquals() {
    assertFalse(equalsNullSafe("1", "2"));
  }

  @Test
  void testEqualsNullSafe_NotEqualsDifferentObjects() {
    assertFalse(equalsNullSafe("1", 1));
  }

  @SuppressWarnings("ConstantValue")
  @Test
  void testEqualsNullSafe_NotEqualsFirstNull() {
    assertFalse(equalsNullSafe(null, "2"));
  }

  @Test
  void testEqualsNullSafe_NotEqualsSecondNull() {
    assertFalse(equalsNullSafe("1", null));
  }

  @Test
  void testEqualsNullSafe_EqualsBothNull() {
    assertTrue(equalsNullSafe(null, null));
  }

  @Test
  void testEqualsNullSafe_EqualsStringEqualsCalendar() throws Exception {
    assertTrue(equalsNullSafe("01.01.2015", Converter.toCalendar("01.01.2015")));
  }

  @Test
  void testEqualsNullSafe_NotEqualsStringEqualsCalendar() throws Exception {
    assertFalse(equalsNullSafe("01.01.2015", Converter.toCalendar("01.02.2015")));
  }

  @Test
  void testEqualsNullSafe_NotEqualsStringEqualsNull() {
    assertFalse(equalsNullSafe("01.01.2015", null));
  }

  @Test
  void testEqualsNullSafe_NotEqualsNullEqualsCalendar() throws Exception {
    assertFalse(equalsNullSafe(null, Converter.toCalendar("01.01.2015")));
  }

  @Test
  void testIsTimePeriodValid() {
    assertTrue(isTimePeriodValid(Time.valueOf("12:30:00"), Time.valueOf("12:31:00")));
    assertTrue(isTimePeriodValid(Time.valueOf("12:30:00"), Time.valueOf("13:30:00")));
    assertFalse(isTimePeriodValid(Time.valueOf("12:30:00"), Time.valueOf("12:29:00")));
    assertFalse(isTimePeriodValid(Time.valueOf("12:30:00"), Time.valueOf("11:31:00")));
    assertFalse(isTimePeriodValid(Time.valueOf("12:30:00"), Time.valueOf("12:30:00")));
  }
}
