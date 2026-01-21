package ch.metzenthin.svm.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.common.utils.Converter;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Hans Stamm
 */
class CalendarModelAttributeTest {

  TestModelAttributeListener testModelAttributeListener;

  private static final Calendar DEFAULT_CALENDAR = toCalendar("01.01.2025");
  private static final String DEFAULT_CALENDAR_AS_STRING = Converter.asString(DEFAULT_CALENDAR);
  private static final Calendar EARLIEST_VALID_CALENDAR = toCalendar("01.01.1980");
  private static final Calendar LATEST_VALID_CALENDAR = toCalendar("31.12.2040");

  private static Calendar toCalendar(String s) {
    try {
      return Converter.toCalendar(s);
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }

  private static Calendar createCalendar(Calendar base, int addDays) {
    Calendar newCalendar = Calendar.getInstance();
    newCalendar.setTime(base.getTime());
    newCalendar.add(Calendar.DAY_OF_MONTH, addDays);
    return newCalendar;
  }

  @BeforeEach
  void setUp() {
    testModelAttributeListener = new TestModelAttributeListener();
  }

  @Test
  void testGetValue_Null() throws SvmValidationException {
    CalendarModelAttribute calendarModelAttribute =
        new CalendarModelAttribute(
            testModelAttributeListener,
            Field.ANMELDEDATUM,
            EARLIEST_VALID_CALENDAR,
            LATEST_VALID_CALENDAR,
            new TestAttributeAccessor<>(DEFAULT_CALENDAR));
    calendarModelAttribute.setNewValue(false, null, false);
    assertNull(calendarModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(1, testModelAttributeListener.getPropertyChangeCounter());
    assertEquals(DEFAULT_CALENDAR_AS_STRING, testModelAttributeListener.getOldValue());
    assertNull(testModelAttributeListener.getNewValue());
  }

  @Test
  void testGetValue_NotNull() throws SvmValidationException {
    CalendarModelAttribute calendarModelAttribute =
        new CalendarModelAttribute(
            testModelAttributeListener,
            Field.ANMELDEDATUM,
            EARLIEST_VALID_CALENDAR,
            LATEST_VALID_CALENDAR,
            new TestAttributeAccessor<>(null));
    calendarModelAttribute.setNewValue(false, DEFAULT_CALENDAR_AS_STRING, false);
    assertEquals(DEFAULT_CALENDAR, calendarModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(1, testModelAttributeListener.getPropertyChangeCounter());
    assertNull(testModelAttributeListener.getOldValue());
    assertEquals(DEFAULT_CALENDAR_AS_STRING, testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_IsRequired_Null() {
    CalendarModelAttribute calendarModelAttribute =
        new CalendarModelAttribute(
            testModelAttributeListener,
            Field.ANMELDEDATUM,
            EARLIEST_VALID_CALENDAR,
            LATEST_VALID_CALENDAR,
            new TestAttributeAccessor<>(null));
    assertThrows(
        SvmRequiredException.class, () -> calendarModelAttribute.setNewValue(true, null, false));
    assertEquals(1, testModelAttributeListener.getInvalidateCounter());
    assertEquals(0, testModelAttributeListener.getFireCounter());
    assertEquals(0, testModelAttributeListener.getPropertyChangeCounter());
  }

  @Test
  void testSetNewValue_IsRequired_Null_BulkUpdate() throws SvmValidationException {
    CalendarModelAttribute calendarModelAttribute =
        new CalendarModelAttribute(
            testModelAttributeListener,
            Field.ANMELDEDATUM,
            EARLIEST_VALID_CALENDAR,
            LATEST_VALID_CALENDAR,
            new TestAttributeAccessor<>(null));
    calendarModelAttribute.setNewValue(true, null, true);
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(0, testModelAttributeListener.getPropertyChangeCounter());
    assertNull(testModelAttributeListener.getOldValue());
    assertNull(testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_IsRequired_Empty() {
    CalendarModelAttribute calendarModelAttribute =
        new CalendarModelAttribute(
            testModelAttributeListener,
            Field.ANMELDEDATUM,
            EARLIEST_VALID_CALENDAR,
            LATEST_VALID_CALENDAR,
            new TestAttributeAccessor<>(null));
    assertThrows(
        SvmRequiredException.class, () -> calendarModelAttribute.setNewValue(true, "", false));
    assertEquals(1, testModelAttributeListener.getInvalidateCounter());
    assertEquals(0, testModelAttributeListener.getFireCounter());
    assertEquals(0, testModelAttributeListener.getPropertyChangeCounter());
  }

  @Test
  void testSetNewValue_IsRequired_NotEmpty() throws SvmValidationException {
    CalendarModelAttribute calendarModelAttribute =
        new CalendarModelAttribute(
            testModelAttributeListener,
            Field.ANMELDEDATUM,
            EARLIEST_VALID_CALENDAR,
            LATEST_VALID_CALENDAR,
            new TestAttributeAccessor<>(null));
    calendarModelAttribute.setNewValue(true, DEFAULT_CALENDAR_AS_STRING, false);
    assertEquals(DEFAULT_CALENDAR, calendarModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(1, testModelAttributeListener.getPropertyChangeCounter());
    assertNull(testModelAttributeListener.getOldValue());
    assertEquals(DEFAULT_CALENDAR_AS_STRING, testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_IsNotRequired_Null() throws SvmValidationException {
    CalendarModelAttribute calendarModelAttribute =
        new CalendarModelAttribute(
            testModelAttributeListener,
            Field.ANMELDEDATUM,
            EARLIEST_VALID_CALENDAR,
            LATEST_VALID_CALENDAR,
            new TestAttributeAccessor<>(null));
    calendarModelAttribute.setNewValue(false, null, false);
    assertNull(calendarModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(0, testModelAttributeListener.getPropertyChangeCounter());
    assertNull(testModelAttributeListener.getOldValue());
    assertNull(testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_IsNotRequired_Empty() throws SvmValidationException {
    CalendarModelAttribute calendarModelAttribute =
        new CalendarModelAttribute(
            testModelAttributeListener,
            Field.ANMELDEDATUM,
            EARLIEST_VALID_CALENDAR,
            LATEST_VALID_CALENDAR,
            new TestAttributeAccessor<>(null));
    calendarModelAttribute.setNewValue(false, "", false);
    assertNull(calendarModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(0, testModelAttributeListener.getPropertyChangeCounter());
    assertNull(testModelAttributeListener.getOldValue());
    assertNull(testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_IsNotRequired_NotEmpty() throws SvmValidationException, ParseException {
    Calendar oldCalendarValue = Converter.toCalendar("01.01.2015");
    String oldCalendarValueAsString = Converter.asString(oldCalendarValue);
    CalendarModelAttribute calendarModelAttribute =
        new CalendarModelAttribute(
            testModelAttributeListener,
            Field.ANMELDEDATUM,
            EARLIEST_VALID_CALENDAR,
            LATEST_VALID_CALENDAR,
            new TestAttributeAccessor<>(oldCalendarValue));
    calendarModelAttribute.setNewValue(false, DEFAULT_CALENDAR_AS_STRING, false);
    assertEquals(DEFAULT_CALENDAR, calendarModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(1, testModelAttributeListener.getPropertyChangeCounter());
    assertEquals(oldCalendarValueAsString, testModelAttributeListener.getOldValue());
    assertEquals(DEFAULT_CALENDAR_AS_STRING, testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_EarliestValidDate_Greater() {
    Calendar newCalendarValue = createCalendar(EARLIEST_VALID_CALENDAR, -1);
    String newCalendarValueAsString = Converter.asString(newCalendarValue);
    CalendarModelAttribute calendarModelAttribute =
        new CalendarModelAttribute(
            testModelAttributeListener,
            Field.ANMELDEDATUM,
            EARLIEST_VALID_CALENDAR,
            LATEST_VALID_CALENDAR,
            new TestAttributeAccessor<>(null));
    assertThrows(
        SvmValidationException.class,
        () -> calendarModelAttribute.setNewValue(true, newCalendarValueAsString, false));
    assertEquals(1, testModelAttributeListener.getInvalidateCounter());
    assertEquals(0, testModelAttributeListener.getFireCounter());
    assertEquals(0, testModelAttributeListener.getPropertyChangeCounter());
    assertNull(testModelAttributeListener.getOldValue());
    assertNull(testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_MinLength_Equal() throws SvmValidationException {
    Calendar newCalendarValue = createCalendar(EARLIEST_VALID_CALENDAR, 0);
    String newCalendarValueAsString = Converter.asString(newCalendarValue);
    CalendarModelAttribute calendarModelAttribute =
        new CalendarModelAttribute(
            testModelAttributeListener,
            Field.ANMELDEDATUM,
            EARLIEST_VALID_CALENDAR,
            LATEST_VALID_CALENDAR,
            new TestAttributeAccessor<>(DEFAULT_CALENDAR));
    calendarModelAttribute.setNewValue(true, newCalendarValueAsString, false);
    assertEquals(newCalendarValue, calendarModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(1, testModelAttributeListener.getPropertyChangeCounter());
    assertEquals(DEFAULT_CALENDAR_AS_STRING, testModelAttributeListener.getOldValue());
    assertEquals(newCalendarValueAsString, testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_MinLength_Lesser() throws SvmValidationException {
    Calendar newCalendarValue = createCalendar(EARLIEST_VALID_CALENDAR, 1);
    String newCalendarValueAsString = Converter.asString(newCalendarValue);
    CalendarModelAttribute calendarModelAttribute =
        new CalendarModelAttribute(
            testModelAttributeListener,
            Field.ANMELDEDATUM,
            EARLIEST_VALID_CALENDAR,
            LATEST_VALID_CALENDAR,
            new TestAttributeAccessor<>(DEFAULT_CALENDAR));
    calendarModelAttribute.setNewValue(true, newCalendarValueAsString, false);
    assertEquals(newCalendarValue, calendarModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(1, testModelAttributeListener.getPropertyChangeCounter());
    assertEquals(DEFAULT_CALENDAR_AS_STRING, testModelAttributeListener.getOldValue());
    assertEquals(newCalendarValueAsString, testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_MinLength_Zero() throws SvmValidationException {
    Calendar earliestValidCalenderZero = Calendar.getInstance();
    earliestValidCalenderZero.setTime(new Date(0));
    CalendarModelAttribute calendarModelAttribute =
        new CalendarModelAttribute(
            testModelAttributeListener,
            Field.ANMELDEDATUM,
            earliestValidCalenderZero,
            LATEST_VALID_CALENDAR,
            new TestAttributeAccessor<>(null));
    calendarModelAttribute.setNewValue(true, DEFAULT_CALENDAR_AS_STRING, false);
    assertEquals(DEFAULT_CALENDAR, calendarModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(1, testModelAttributeListener.getPropertyChangeCounter());
    assertNull(testModelAttributeListener.getOldValue());
    assertEquals(DEFAULT_CALENDAR_AS_STRING, testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_MaxLength_Lesser() {
    Calendar newCalendarValue = createCalendar(LATEST_VALID_CALENDAR, 1);
    String newCalendarValueAsString = Converter.asString(newCalendarValue);
    CalendarModelAttribute calendarModelAttribute =
        new CalendarModelAttribute(
            testModelAttributeListener,
            Field.ANMELDEDATUM,
            EARLIEST_VALID_CALENDAR,
            LATEST_VALID_CALENDAR,
            new TestAttributeAccessor<>(null));
    assertThrows(
        SvmValidationException.class,
        () -> calendarModelAttribute.setNewValue(true, newCalendarValueAsString, false));
    assertEquals(1, testModelAttributeListener.getInvalidateCounter());
    assertEquals(0, testModelAttributeListener.getFireCounter());
    assertEquals(0, testModelAttributeListener.getPropertyChangeCounter());
    assertNull(testModelAttributeListener.getOldValue());
    assertNull(testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_MaxLength_Equal() throws SvmValidationException {
    Calendar newCalendarValue = createCalendar(LATEST_VALID_CALENDAR, 0);
    String newCalendarValueAsString = Converter.asString(newCalendarValue);
    CalendarModelAttribute calendarModelAttribute =
        new CalendarModelAttribute(
            testModelAttributeListener,
            Field.ANMELDEDATUM,
            EARLIEST_VALID_CALENDAR,
            LATEST_VALID_CALENDAR,
            new TestAttributeAccessor<>(DEFAULT_CALENDAR));
    calendarModelAttribute.setNewValue(true, newCalendarValueAsString, false);
    assertEquals(newCalendarValue, calendarModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(1, testModelAttributeListener.getPropertyChangeCounter());
    assertEquals(DEFAULT_CALENDAR_AS_STRING, testModelAttributeListener.getOldValue());
    assertEquals(newCalendarValueAsString, testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_MaxLength_Greater() throws SvmValidationException {
    Calendar newCalendarValue = createCalendar(LATEST_VALID_CALENDAR, -1);
    String newCalendarValueAsString = Converter.asString(newCalendarValue);
    CalendarModelAttribute calendarModelAttribute =
        new CalendarModelAttribute(
            testModelAttributeListener,
            Field.ANMELDEDATUM,
            EARLIEST_VALID_CALENDAR,
            LATEST_VALID_CALENDAR,
            new TestAttributeAccessor<>(DEFAULT_CALENDAR));
    calendarModelAttribute.setNewValue(true, newCalendarValueAsString, false);
    assertEquals(newCalendarValue, calendarModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(1, testModelAttributeListener.getPropertyChangeCounter());
    assertEquals(DEFAULT_CALENDAR_AS_STRING, testModelAttributeListener.getOldValue());
    assertEquals(newCalendarValueAsString, testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_Trim() throws SvmValidationException {
    Calendar newCalendarValue = createCalendar(DEFAULT_CALENDAR, 100);
    String newCalendarValueAsString = Converter.asString(newCalendarValue);
    CalendarModelAttribute calendarModelAttribute =
        new CalendarModelAttribute(
            testModelAttributeListener,
            Field.ANMELDEDATUM,
            EARLIEST_VALID_CALENDAR,
            LATEST_VALID_CALENDAR,
            new TestAttributeAccessor<>(DEFAULT_CALENDAR));
    calendarModelAttribute.setNewValue(true, "  " + newCalendarValueAsString + "  ", false);
    assertEquals(newCalendarValue, calendarModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(1, testModelAttributeListener.getPropertyChangeCounter());
    assertEquals(DEFAULT_CALENDAR_AS_STRING, testModelAttributeListener.getOldValue());
    assertEquals(newCalendarValueAsString, testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_NotACalendar() {
    CalendarModelAttribute calendarModelAttribute =
        new CalendarModelAttribute(
            testModelAttributeListener,
            Field.ANMELDEDATUM,
            EARLIEST_VALID_CALENDAR,
            LATEST_VALID_CALENDAR,
            new TestAttributeAccessor<>(null));
    SvmValidationException svmValidationException =
        assertThrows(
            SvmValidationException.class,
            () -> calendarModelAttribute.setNewValue(true, "x", false));
    assertEquals("Kein gültiges Datum im Format 'TT.MM.JJJJ'", svmValidationException.getMessage());
    assertEquals(1, testModelAttributeListener.getInvalidateCounter());
    assertEquals(0, testModelAttributeListener.getFireCounter());
    assertEquals(0, testModelAttributeListener.getPropertyChangeCounter());
    assertNull(testModelAttributeListener.getOldValue());
    assertNull(testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_EnforcePropertyChangeEvent() throws SvmValidationException {
    CalendarModelAttribute calendarModelAttribute =
        new CalendarModelAttribute(
            testModelAttributeListener,
            Field.ANMELDEDATUM,
            EARLIEST_VALID_CALENDAR,
            LATEST_VALID_CALENDAR,
            new TestAttributeAccessor<>(DEFAULT_CALENDAR));
    calendarModelAttribute.setNewValue(true, DEFAULT_CALENDAR_AS_STRING, false, true);
    assertEquals(DEFAULT_CALENDAR, calendarModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(1, testModelAttributeListener.getPropertyChangeCounter());
    assertEquals("!" + DEFAULT_CALENDAR_AS_STRING, testModelAttributeListener.getOldValue());
    assertEquals(DEFAULT_CALENDAR_AS_STRING, testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_Format() throws SvmValidationException {
    // 01.01.2025 => 01.01.25
    String input = DEFAULT_CALENDAR_AS_STRING.replace(".20", ".");
    CalendarModelAttribute calendarModelAttribute =
        new CalendarModelAttribute(
            testModelAttributeListener,
            Field.ANMELDEDATUM,
            EARLIEST_VALID_CALENDAR,
            LATEST_VALID_CALENDAR,
            new TestAttributeAccessor<>(DEFAULT_CALENDAR));
    calendarModelAttribute.setNewValue(true, input, false);
    assertEquals(DEFAULT_CALENDAR, calendarModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(1, testModelAttributeListener.getPropertyChangeCounter());
    assertEquals(input, testModelAttributeListener.getOldValue());
    assertEquals(DEFAULT_CALENDAR_AS_STRING, testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_dateFormatString() throws SvmValidationException {
    CalendarModelAttribute calendarModelAttribute =
        new CalendarModelAttribute(
            testModelAttributeListener,
            Field.ANMELDEDATUM,
            EARLIEST_VALID_CALENDAR,
            LATEST_VALID_CALENDAR,
            new TestAttributeAccessor<>(DEFAULT_CALENDAR));
    calendarModelAttribute.setNewValue(false, "", null, false);
    assertNull(calendarModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(1, testModelAttributeListener.getPropertyChangeCounter());
    // Weil der dateFormatString entfernt wurde, kann auch der alte Wert nicht mehr formatiert
    // werden.
    assertEquals("", testModelAttributeListener.getOldValue());
    assertNull(testModelAttributeListener.getNewValue());
  }

  @Test
  void testInitValue_Null() {
    CalendarModelAttribute calendarModelAttribute =
        new CalendarModelAttribute(
            testModelAttributeListener,
            Field.ANMELDEDATUM,
            EARLIEST_VALID_CALENDAR,
            LATEST_VALID_CALENDAR,
            new TestAttributeAccessor<>(null));
    calendarModelAttribute.initValue(null);
    assertNull(calendarModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(0, testModelAttributeListener.getPropertyChangeCounter());
    assertNull(testModelAttributeListener.getOldValue());
    assertNull(testModelAttributeListener.getNewValue());
  }

  @Test
  void testInitValue_Value() {
    Calendar newCalendarValue = createCalendar(DEFAULT_CALENDAR, 100);
    String newCalendarValueAsString = Converter.asString(newCalendarValue);
    CalendarModelAttribute calendarModelAttribute =
        new CalendarModelAttribute(
            testModelAttributeListener,
            Field.ANMELDEDATUM,
            EARLIEST_VALID_CALENDAR,
            LATEST_VALID_CALENDAR,
            new TestAttributeAccessor<>(DEFAULT_CALENDAR));
    calendarModelAttribute.initValue(newCalendarValueAsString);
    assertEquals(newCalendarValue, calendarModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(1, testModelAttributeListener.getPropertyChangeCounter());
    assertEquals(DEFAULT_CALENDAR_AS_STRING, testModelAttributeListener.getOldValue());
    assertEquals(newCalendarValueAsString, testModelAttributeListener.getNewValue());
  }
}
