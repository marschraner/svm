package ch.metzenthin.svm.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.common.utils.Converter;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import java.sql.Time;
import java.text.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Hans Stamm
 */
class TimeModelAttributeTest {

  TestModelAttributeListener testModelAttributeListener;

  private static final Time DEFAULT_TIME = toTime("08.15");
  private static final String DEFAULT_TIME_AS_STRING = Converter.asString(DEFAULT_TIME);

  private static Time toTime(String s) {
    try {
      return Converter.toTime(s);
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }

  private static Time createTime(Time base, int addMinutes) {
    return new Time(base.getTime() + (addMinutes * 60L * 1000));
  }

  @BeforeEach
  void setUp() {
    testModelAttributeListener = new TestModelAttributeListener();
  }

  @Test
  void testGetValue_Null() throws SvmValidationException {
    TimeModelAttribute timeModelAttribute =
        new TimeModelAttribute(
            testModelAttributeListener,
            Field.ZEIT_BEGINN,
            new TestAttributeAccessor<>(DEFAULT_TIME));
    timeModelAttribute.setNewValue(false, null, false);
    assertNull(timeModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(1, testModelAttributeListener.getPropertyChangeCounter());
    assertEquals(DEFAULT_TIME_AS_STRING, testModelAttributeListener.getOldValue());
    assertNull(testModelAttributeListener.getNewValue());
  }

  @Test
  void testGetValue_NotNull() throws SvmValidationException {
    TimeModelAttribute timeModelAttribute =
        new TimeModelAttribute(
            testModelAttributeListener, Field.ZEIT_BEGINN, new TestAttributeAccessor<>(null));
    timeModelAttribute.setNewValue(false, DEFAULT_TIME_AS_STRING, false);
    assertEquals(DEFAULT_TIME, timeModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(1, testModelAttributeListener.getPropertyChangeCounter());
    assertNull(testModelAttributeListener.getOldValue());
    assertEquals(DEFAULT_TIME_AS_STRING, testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_IsRequired_Null() {
    TimeModelAttribute timeModelAttribute =
        new TimeModelAttribute(
            testModelAttributeListener, Field.ZEIT_BEGINN, new TestAttributeAccessor<>(null));
    assertThrows(
        SvmRequiredException.class, () -> timeModelAttribute.setNewValue(true, null, false));
    assertEquals(0, testModelAttributeListener.getPropertyChangeCounter());
    assertEquals(1, testModelAttributeListener.getInvalidateCounter());
    assertEquals(0, testModelAttributeListener.getFireCounter());
  }

  @Test
  void testSetNewValue_IsRequired_Null_BulkUpdate() throws SvmValidationException {
    TimeModelAttribute timeModelAttribute =
        new TimeModelAttribute(
            testModelAttributeListener, Field.ZEIT_BEGINN, new TestAttributeAccessor<>(null));
    timeModelAttribute.setNewValue(true, null, true);
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(0, testModelAttributeListener.getPropertyChangeCounter());
    assertNull(testModelAttributeListener.getOldValue());
    assertNull(testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_IsRequired_Empty() {
    TimeModelAttribute timeModelAttribute =
        new TimeModelAttribute(
            testModelAttributeListener, Field.ZEIT_BEGINN, new TestAttributeAccessor<>(null));
    assertThrows(SvmRequiredException.class, () -> timeModelAttribute.setNewValue(true, "", false));
    assertEquals(1, testModelAttributeListener.getInvalidateCounter());
    assertEquals(0, testModelAttributeListener.getFireCounter());
    assertEquals(0, testModelAttributeListener.getPropertyChangeCounter());
  }

  @Test
  void testSetNewValue_IsRequired_NotEmpty() throws SvmValidationException {
    TimeModelAttribute timeModelAttribute =
        new TimeModelAttribute(
            testModelAttributeListener, Field.ZEIT_BEGINN, new TestAttributeAccessor<>(null));
    timeModelAttribute.setNewValue(true, DEFAULT_TIME_AS_STRING, false);
    assertEquals(DEFAULT_TIME, timeModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(1, testModelAttributeListener.getPropertyChangeCounter());
    assertNull(testModelAttributeListener.getOldValue());
    assertEquals(DEFAULT_TIME_AS_STRING, testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_IsNotRequired_Null() throws SvmValidationException {
    TimeModelAttribute timeModelAttribute =
        new TimeModelAttribute(
            testModelAttributeListener, Field.ZEIT_BEGINN, new TestAttributeAccessor<>(null));
    timeModelAttribute.setNewValue(false, null, false);
    assertNull(timeModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(0, testModelAttributeListener.getPropertyChangeCounter());
    assertNull(testModelAttributeListener.getOldValue());
    assertNull(testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_IsNotRequired_Empty() throws SvmValidationException {
    TimeModelAttribute timeModelAttribute =
        new TimeModelAttribute(
            testModelAttributeListener, Field.ZEIT_BEGINN, new TestAttributeAccessor<>(null));
    timeModelAttribute.setNewValue(false, "", false);
    assertNull(timeModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(0, testModelAttributeListener.getPropertyChangeCounter());
    assertNull(testModelAttributeListener.getOldValue());
    assertNull(testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_IsNotRequired_NotEmpty() throws SvmValidationException, ParseException {
    Time oldTimeValue = Converter.toTime("13.15");
    String oldTimeValueAsString = Converter.asString(oldTimeValue);
    TimeModelAttribute timeModelAttribute =
        new TimeModelAttribute(
            testModelAttributeListener,
            Field.ZEIT_BEGINN,
            new TestAttributeAccessor<>(oldTimeValue));
    timeModelAttribute.setNewValue(false, DEFAULT_TIME_AS_STRING, false);
    assertEquals(DEFAULT_TIME, timeModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(1, testModelAttributeListener.getPropertyChangeCounter());
    assertEquals(oldTimeValueAsString, testModelAttributeListener.getOldValue());
    assertEquals(DEFAULT_TIME_AS_STRING, testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_Trim() throws SvmValidationException {
    Time newTimeValue = createTime(DEFAULT_TIME, 100);
    String newTimeValueAsString = Converter.asString(newTimeValue);
    TimeModelAttribute timeModelAttribute =
        new TimeModelAttribute(
            testModelAttributeListener,
            Field.ZEIT_BEGINN,
            new TestAttributeAccessor<>(DEFAULT_TIME));
    timeModelAttribute.setNewValue(true, "  " + newTimeValueAsString + "  ", false);
    assertEquals(newTimeValue, timeModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(1, testModelAttributeListener.getPropertyChangeCounter());
    assertEquals(DEFAULT_TIME_AS_STRING, testModelAttributeListener.getOldValue());
    assertEquals(newTimeValueAsString, testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_NotATime() {
    TimeModelAttribute timeModelAttribute =
        new TimeModelAttribute(
            testModelAttributeListener, Field.ZEIT_BEGINN, new TestAttributeAccessor<>(null));
    SvmValidationException svmValidationException =
        assertThrows(
            SvmValidationException.class, () -> timeModelAttribute.setNewValue(true, "x", false));
    assertEquals("Keine gültige Zeit im Format 'HH.MM'", svmValidationException.getMessage());
    assertEquals(1, testModelAttributeListener.getInvalidateCounter());
    assertEquals(0, testModelAttributeListener.getFireCounter());
    assertEquals(0, testModelAttributeListener.getPropertyChangeCounter());
    assertNull(testModelAttributeListener.getOldValue());
    assertNull(testModelAttributeListener.getNewValue());
  }

  @Test
  void testInitValue_Null() {
    TimeModelAttribute timeModelAttribute =
        new TimeModelAttribute(
            testModelAttributeListener, Field.ZEIT_BEGINN, new TestAttributeAccessor<>(null));
    timeModelAttribute.initValue(null);
    assertNull(timeModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(0, testModelAttributeListener.getPropertyChangeCounter());
    assertNull(testModelAttributeListener.getOldValue());
    assertNull(testModelAttributeListener.getNewValue());
  }

  @Test
  void testInitValue_Value() {
    Time newTimeValue = createTime(toTime("10.30"), 30);
    String newTimeValueAsString = Converter.asString(newTimeValue);
    TimeModelAttribute timeModelAttribute =
        new TimeModelAttribute(
            testModelAttributeListener,
            Field.ZEIT_BEGINN,
            new TestAttributeAccessor<>(DEFAULT_TIME));
    timeModelAttribute.initValue(newTimeValueAsString);
    assertEquals(newTimeValue, timeModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(1, testModelAttributeListener.getPropertyChangeCounter());
    assertEquals(DEFAULT_TIME_AS_STRING, testModelAttributeListener.getOldValue());
    assertEquals(newTimeValueAsString, testModelAttributeListener.getNewValue());
  }
}
