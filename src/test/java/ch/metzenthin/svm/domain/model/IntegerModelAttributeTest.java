package ch.metzenthin.svm.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Hans Stamm
 */
class IntegerModelAttributeTest {

  TestModelAttributeListener testModelAttributeListener;

  @BeforeEach
  void setUp() {
    testModelAttributeListener = new TestModelAttributeListener();
  }

  @Test
  void testGetValue_Null() throws SvmValidationException {
    IntegerModelAttribute integerModelAttribute =
        new IntegerModelAttribute(
            testModelAttributeListener,
            Field.LEKTIONSLAENGE,
            10,
            200,
            new TestAttributeAccessor<>(100));
    integerModelAttribute.setNewValue(false, null, false);
    assertNull(integerModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(1, testModelAttributeListener.getPropertyChangeCounter());
    assertEquals("100", testModelAttributeListener.getOldValue());
    assertNull(testModelAttributeListener.getNewValue());
  }

  @Test
  void testGetValue_NotNull() throws SvmValidationException {
    IntegerModelAttribute integerModelAttribute =
        new IntegerModelAttribute(
            testModelAttributeListener,
            Field.LEKTIONSLAENGE,
            10,
            200,
            new TestAttributeAccessor<>(null));
    integerModelAttribute.setNewValue(false, "100", false);
    assertEquals(Integer.valueOf(100), integerModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(1, testModelAttributeListener.getPropertyChangeCounter());
    assertNull(testModelAttributeListener.getOldValue());
    assertEquals("100", testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_IsRequired_Null() {
    IntegerModelAttribute integerModelAttribute =
        new IntegerModelAttribute(
            testModelAttributeListener,
            Field.LEKTIONSLAENGE,
            10,
            200,
            new TestAttributeAccessor<>(null));
    assertThrows(
        SvmRequiredException.class, () -> integerModelAttribute.setNewValue(true, null, false));
    assertEquals(1, testModelAttributeListener.getInvalidateCounter());
    assertEquals(0, testModelAttributeListener.getFireCounter());
    assertEquals(0, testModelAttributeListener.getPropertyChangeCounter());
  }

  @Test
  void testSetNewValue_IsRequired_Null_BulkUpdate() throws SvmValidationException {
    IntegerModelAttribute integerModelAttribute =
        new IntegerModelAttribute(
            testModelAttributeListener,
            Field.LEKTIONSLAENGE,
            10,
            200,
            new TestAttributeAccessor<>(null));
    integerModelAttribute.setNewValue(true, null, true);
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(0, testModelAttributeListener.getPropertyChangeCounter());
    assertNull(testModelAttributeListener.getOldValue());
    assertNull(testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_IsRequired_Empty() {
    IntegerModelAttribute integerModelAttribute =
        new IntegerModelAttribute(
            testModelAttributeListener,
            Field.LEKTIONSLAENGE,
            10,
            200,
            new TestAttributeAccessor<>(null));
    assertThrows(
        SvmRequiredException.class, () -> integerModelAttribute.setNewValue(true, "", false));
    assertEquals(1, testModelAttributeListener.getInvalidateCounter());
    assertEquals(0, testModelAttributeListener.getFireCounter());
    assertEquals(0, testModelAttributeListener.getPropertyChangeCounter());
  }

  @Test
  void testSetNewValue_IsRequired_NotEmpty() throws SvmValidationException {
    IntegerModelAttribute integerModelAttribute =
        new IntegerModelAttribute(
            testModelAttributeListener,
            Field.LEKTIONSLAENGE,
            10,
            200,
            new TestAttributeAccessor<>(null));
    integerModelAttribute.setNewValue(true, "100", false);
    assertEquals(Integer.valueOf(100), integerModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(1, testModelAttributeListener.getPropertyChangeCounter());
    assertNull(testModelAttributeListener.getOldValue());
    assertEquals("100", testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_IsNotRequired_Null() throws SvmValidationException {
    IntegerModelAttribute integerModelAttribute =
        new IntegerModelAttribute(
            testModelAttributeListener,
            Field.LEKTIONSLAENGE,
            10,
            200,
            new TestAttributeAccessor<>(null));
    integerModelAttribute.setNewValue(false, null, false);
    assertNull(integerModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(0, testModelAttributeListener.getPropertyChangeCounter());
    assertNull(testModelAttributeListener.getOldValue());
    assertNull(testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_IsNotRequired_Empty() throws SvmValidationException {
    IntegerModelAttribute integerModelAttribute =
        new IntegerModelAttribute(
            testModelAttributeListener,
            Field.LEKTIONSLAENGE,
            10,
            200,
            new TestAttributeAccessor<>(null));
    integerModelAttribute.setNewValue(false, "", false);
    assertNull(integerModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(0, testModelAttributeListener.getPropertyChangeCounter());
    assertNull(testModelAttributeListener.getOldValue());
    assertNull(testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_IsNotRequired_NotEmpty() throws SvmValidationException {
    IntegerModelAttribute integerModelAttribute =
        new IntegerModelAttribute(
            testModelAttributeListener,
            Field.LEKTIONSLAENGE,
            10,
            200,
            new TestAttributeAccessor<>(99));
    integerModelAttribute.setNewValue(false, "100", false);
    assertEquals(Integer.valueOf(100), integerModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(1, testModelAttributeListener.getPropertyChangeCounter());
    assertEquals("99", testModelAttributeListener.getOldValue());
    assertEquals("100", testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_MinLength_Greater() {
    IntegerModelAttribute integerModelAttribute =
        new IntegerModelAttribute(
            testModelAttributeListener,
            Field.LEKTIONSLAENGE,
            10,
            200,
            new TestAttributeAccessor<>(null));
    assertThrows(
        SvmValidationException.class, () -> integerModelAttribute.setNewValue(true, "9", false));
    assertEquals(1, testModelAttributeListener.getInvalidateCounter());
    assertEquals(0, testModelAttributeListener.getFireCounter());
    assertEquals(0, testModelAttributeListener.getPropertyChangeCounter());
    assertNull(testModelAttributeListener.getOldValue());
    assertNull(testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_MinLength_Equal() throws SvmValidationException {
    IntegerModelAttribute integerModelAttribute =
        new IntegerModelAttribute(
            testModelAttributeListener,
            Field.LEKTIONSLAENGE,
            10,
            200,
            new TestAttributeAccessor<>(99));
    integerModelAttribute.setNewValue(true, "10", false);
    assertEquals(Integer.valueOf(10), integerModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(1, testModelAttributeListener.getPropertyChangeCounter());
    assertEquals("99", testModelAttributeListener.getOldValue());
    assertEquals("10", testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_MinLength_Lesser() throws SvmValidationException {
    IntegerModelAttribute integerModelAttribute =
        new IntegerModelAttribute(
            testModelAttributeListener,
            Field.LEKTIONSLAENGE,
            10,
            200,
            new TestAttributeAccessor<>(99));
    integerModelAttribute.setNewValue(true, "11", false);
    assertEquals(Integer.valueOf(11), integerModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(1, testModelAttributeListener.getPropertyChangeCounter());
    assertEquals("99", testModelAttributeListener.getOldValue());
    assertEquals("11", testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_MinLength_Zero() throws SvmValidationException {
    IntegerModelAttribute integerModelAttribute =
        new IntegerModelAttribute(
            testModelAttributeListener,
            Field.LEKTIONSLAENGE,
            0,
            200,
            new TestAttributeAccessor<>(99));
    integerModelAttribute.setNewValue(true, "11", false);
    assertEquals(Integer.valueOf(11), integerModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(1, testModelAttributeListener.getPropertyChangeCounter());
    assertEquals("99", testModelAttributeListener.getOldValue());
    assertEquals("11", testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_MaxLength_Lesser() {
    IntegerModelAttribute integerModelAttribute =
        new IntegerModelAttribute(
            testModelAttributeListener,
            Field.LEKTIONSLAENGE,
            10,
            200,
            new TestAttributeAccessor<>(null));
    assertThrows(
        SvmValidationException.class, () -> integerModelAttribute.setNewValue(true, "201", false));
    assertEquals(1, testModelAttributeListener.getInvalidateCounter());
    assertEquals(0, testModelAttributeListener.getFireCounter());
    assertEquals(0, testModelAttributeListener.getPropertyChangeCounter());
    assertNull(testModelAttributeListener.getOldValue());
    assertNull(testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_MaxLength_Equal() throws SvmValidationException {
    IntegerModelAttribute integerModelAttribute =
        new IntegerModelAttribute(
            testModelAttributeListener,
            Field.LEKTIONSLAENGE,
            10,
            200,
            new TestAttributeAccessor<>(99));
    integerModelAttribute.setNewValue(true, "200", false);
    assertEquals(Integer.valueOf(200), integerModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(1, testModelAttributeListener.getPropertyChangeCounter());
    assertEquals("99", testModelAttributeListener.getOldValue());
    assertEquals("200", testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_MaxLength_Greater() throws SvmValidationException {
    IntegerModelAttribute integerModelAttribute =
        new IntegerModelAttribute(
            testModelAttributeListener,
            Field.LEKTIONSLAENGE,
            10,
            200,
            new TestAttributeAccessor<>(99));
    integerModelAttribute.setNewValue(true, "199", false);
    assertEquals(Integer.valueOf(199), integerModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(1, testModelAttributeListener.getPropertyChangeCounter());
    assertEquals("99", testModelAttributeListener.getOldValue());
    assertEquals("199", testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_Trim() throws SvmValidationException {
    IntegerModelAttribute integerModelAttribute =
        new IntegerModelAttribute(
            testModelAttributeListener,
            Field.LEKTIONSLAENGE,
            10,
            200,
            new TestAttributeAccessor<>(99));
    integerModelAttribute.setNewValue(true, "  100  ", false);
    assertEquals(Integer.valueOf(100), integerModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(1, testModelAttributeListener.getPropertyChangeCounter());
    assertEquals("99", testModelAttributeListener.getOldValue());
    assertEquals("100", testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_NotAnInteger() {
    IntegerModelAttribute integerModelAttribute =
        new IntegerModelAttribute(
            testModelAttributeListener,
            Field.LEKTIONSLAENGE,
            10,
            200,
            new TestAttributeAccessor<>(null));
    SvmValidationException svmValidationException =
        assertThrows(
            SvmValidationException.class,
            () -> integerModelAttribute.setNewValue(true, "x", false));
    assertEquals("Eingabe muss eine ganze Zahl sein", svmValidationException.getMessage());
    assertEquals(1, testModelAttributeListener.getInvalidateCounter());
    assertEquals(0, testModelAttributeListener.getFireCounter());
    assertEquals(0, testModelAttributeListener.getPropertyChangeCounter());
    assertNull(testModelAttributeListener.getOldValue());
    assertNull(testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_EnforcePropertyChangeEvent() throws SvmValidationException {
    IntegerModelAttribute integerModelAttribute =
        new IntegerModelAttribute(
            testModelAttributeListener,
            Field.LEKTIONSLAENGE,
            10,
            200,
            new TestAttributeAccessor<>(100));
    integerModelAttribute.setNewValue(true, "100", false, true);
    assertEquals(Integer.valueOf(100), integerModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(1, testModelAttributeListener.getPropertyChangeCounter());
    assertEquals("", testModelAttributeListener.getOldValue());
    assertEquals("100", testModelAttributeListener.getNewValue());
  }

  @Test
  void testInitValue_Null() {
    IntegerModelAttribute integerModelAttribute =
        new IntegerModelAttribute(
            testModelAttributeListener,
            Field.LEKTIONSLAENGE,
            10,
            200,
            new TestAttributeAccessor<>(null));
    integerModelAttribute.initValue(null);
    assertNull(integerModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(0, testModelAttributeListener.getPropertyChangeCounter());
    assertNull(testModelAttributeListener.getOldValue());
    assertNull(testModelAttributeListener.getNewValue());
  }

  @Test
  void testInitValue_Value() {
    IntegerModelAttribute integerModelAttribute =
        new IntegerModelAttribute(
            testModelAttributeListener,
            Field.LEKTIONSLAENGE,
            10,
            200,
            new TestAttributeAccessor<>(100));
    integerModelAttribute.initValue("99");
    assertEquals(Integer.valueOf(99), integerModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(1, testModelAttributeListener.getPropertyChangeCounter());
    assertEquals("100", testModelAttributeListener.getOldValue());
    assertEquals("99", testModelAttributeListener.getNewValue());
  }
}
