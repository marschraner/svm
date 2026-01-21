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
@SuppressWarnings({"java:S5976", "java:S4144"})
class StringModelAttributeTest {

  TestModelAttributeListener testModelAttributeListener;

  @BeforeEach
  void setUp() {
    testModelAttributeListener = new TestModelAttributeListener();
  }

  @Test
  void testGetValue_Null() {
    StringModelAttribute stringModelAttribute =
        new StringModelAttribute(
            testModelAttributeListener, Field.NACHNAME, 0, 8, new TestAttributeAccessor<>(null));
    assertEquals("", stringModelAttribute.getValue());
  }

  @Test
  void testGetValue_NotNull() throws SvmValidationException {
    StringModelAttribute stringModelAttribute =
        new StringModelAttribute(
            testModelAttributeListener, Field.NACHNAME, 0, 8, new TestAttributeAccessor<>(null));
    stringModelAttribute.setNewValue(false, "abc", false);
    assertEquals("abc", stringModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals("", testModelAttributeListener.getOldValue());
    assertEquals("abc", testModelAttributeListener.getNewValue());
  }

  @Test
  void testGetValue_Empty() throws SvmValidationException {
    StringModelAttribute stringModelAttribute =
        new StringModelAttribute(
            testModelAttributeListener, Field.NACHNAME, 0, 8, new TestAttributeAccessor<>("abc"));
    stringModelAttribute.setNewValue(false, "", false);
    assertEquals("", stringModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals("abc", testModelAttributeListener.getOldValue());
    assertEquals("", testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_IsRequired_Null() {
    StringModelAttribute stringModelAttribute =
        new StringModelAttribute(
            testModelAttributeListener, Field.NACHNAME, 0, 8, new TestAttributeAccessor<>(null));
    assertThrows(
        SvmRequiredException.class, () -> stringModelAttribute.setNewValue(true, null, false));
    assertEquals(1, testModelAttributeListener.getInvalidateCounter());
    assertEquals(0, testModelAttributeListener.getFireCounter());
    assertNull(testModelAttributeListener.getOldValue());
    assertNull(testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_IsRequired_Null_BulkUpdate() throws SvmValidationException {
    StringModelAttribute stringModelAttribute =
        new StringModelAttribute(
            testModelAttributeListener, Field.NACHNAME, 0, 8, new TestAttributeAccessor<>(null));
    stringModelAttribute.setNewValue(true, null, true);
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals("", testModelAttributeListener.getOldValue());
    assertEquals("", testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_IsRequired_Empty() {
    StringModelAttribute stringModelAttribute =
        new StringModelAttribute(
            testModelAttributeListener, Field.NACHNAME, 0, 8, new TestAttributeAccessor<>(null));
    assertThrows(
        SvmRequiredException.class, () -> stringModelAttribute.setNewValue(true, "", false));
    assertEquals(1, testModelAttributeListener.getInvalidateCounter());
    assertEquals(0, testModelAttributeListener.getFireCounter());
  }

  @Test
  void testSetNewValue_IsRequired_NotEmpty() throws SvmValidationException {
    StringModelAttribute stringModelAttribute =
        new StringModelAttribute(
            testModelAttributeListener, Field.NACHNAME, 0, 8, new TestAttributeAccessor<>(null));
    stringModelAttribute.setNewValue(true, "abc", false);
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals("", testModelAttributeListener.getOldValue());
    assertEquals("abc", testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_IsNotRequired_Null() throws SvmValidationException {
    StringModelAttribute stringModelAttribute =
        new StringModelAttribute(
            testModelAttributeListener, Field.NACHNAME, 0, 8, new TestAttributeAccessor<>(null));
    stringModelAttribute.setNewValue(false, null, false);
    assertEquals("", stringModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals("", testModelAttributeListener.getOldValue());
    assertEquals("", testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_IsNotRequired_Empty() throws SvmValidationException {
    StringModelAttribute stringModelAttribute =
        new StringModelAttribute(
            testModelAttributeListener, Field.NACHNAME, 0, 8, new TestAttributeAccessor<>("abc"));
    stringModelAttribute.setNewValue(false, "", false);
    assertEquals("", stringModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals("abc", testModelAttributeListener.getOldValue());
    assertEquals("", testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_IsNotRequired_NotEmpty() throws SvmValidationException {
    StringModelAttribute stringModelAttribute =
        new StringModelAttribute(
            testModelAttributeListener, Field.NACHNAME, 0, 8, new TestAttributeAccessor<>("xxx"));
    stringModelAttribute.setNewValue(false, "abc", false);
    assertEquals("abc", stringModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals("xxx", testModelAttributeListener.getOldValue());
    assertEquals("abc", testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_MinLength_Greater() {
    StringModelAttribute stringModelAttribute =
        new StringModelAttribute(
            testModelAttributeListener, Field.NACHNAME, 2, 8, new TestAttributeAccessor<>(null));
    assertThrows(
        SvmValidationException.class, () -> stringModelAttribute.setNewValue(true, "a", false));
    assertEquals(1, testModelAttributeListener.getInvalidateCounter());
    assertEquals(0, testModelAttributeListener.getFireCounter());
    assertNull(testModelAttributeListener.getOldValue());
    assertNull(testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_MinLength_Equal() throws SvmValidationException {
    StringModelAttribute stringModelAttribute =
        new StringModelAttribute(
            testModelAttributeListener, Field.NACHNAME, 2, 8, new TestAttributeAccessor<>(null));
    stringModelAttribute.setNewValue(true, "ab", false);
    assertEquals("ab", stringModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals("", testModelAttributeListener.getOldValue());
    assertEquals("ab", testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_MinLength_Lesser() throws SvmValidationException {
    StringModelAttribute stringModelAttribute =
        new StringModelAttribute(
            testModelAttributeListener, Field.NACHNAME, 2, 8, new TestAttributeAccessor<>(null));
    stringModelAttribute.setNewValue(true, "abc", false);
    assertEquals("abc", stringModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals("", testModelAttributeListener.getOldValue());
    assertEquals("abc", testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_MinLength_Zero() throws SvmValidationException {
    StringModelAttribute stringModelAttribute =
        new StringModelAttribute(
            testModelAttributeListener, Field.NACHNAME, 0, 8, new TestAttributeAccessor<>(null));
    stringModelAttribute.setNewValue(true, "cde", false);
    assertEquals("cde", stringModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals("", testModelAttributeListener.getOldValue());
    assertEquals("cde", testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_MaxLength_Lesser() {
    StringModelAttribute stringModelAttribute =
        new StringModelAttribute(
            testModelAttributeListener, Field.NACHNAME, 2, 4, new TestAttributeAccessor<>(null));
    assertThrows(
        SvmValidationException.class, () -> stringModelAttribute.setNewValue(true, "abcde", false));
    assertEquals(1, testModelAttributeListener.getInvalidateCounter());
    assertEquals(0, testModelAttributeListener.getFireCounter());
    assertNull(testModelAttributeListener.getOldValue());
    assertNull(testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_MaxLength_Equal() throws SvmValidationException {
    StringModelAttribute stringModelAttribute =
        new StringModelAttribute(
            testModelAttributeListener, Field.NACHNAME, 2, 4, new TestAttributeAccessor<>("abc"));
    stringModelAttribute.setNewValue(true, "abcd", false);
    assertEquals("abcd", stringModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals("abc", testModelAttributeListener.getOldValue());
    assertEquals("abcd", testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_MaxLength_Greater() throws SvmValidationException {
    StringModelAttribute stringModelAttribute =
        new StringModelAttribute(
            testModelAttributeListener, Field.NACHNAME, 2, 4, new TestAttributeAccessor<>(null));
    stringModelAttribute.setNewValue(true, "abc", false);
    assertEquals("abc", stringModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals("", testModelAttributeListener.getOldValue());
    assertEquals("abc", testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_Trim() throws SvmValidationException {
    TestAttributeAccessor<String> testAttributeAccessor = new TestAttributeAccessor<>(null);
    StringModelAttribute stringModelAttribute =
        new StringModelAttribute(
            testModelAttributeListener, Field.NACHNAME, 0, 10, testAttributeAccessor);
    stringModelAttribute.setNewValue(true, "  abc  ", false);
    assertEquals("abc", testAttributeAccessor.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals("", testModelAttributeListener.getOldValue());
    assertEquals("abc", testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_EnforcePropertyChangeEvent() throws SvmValidationException {
    TestAttributeAccessor<String> testAttributeAccessor = new TestAttributeAccessor<>("abc");
    StringModelAttribute stringModelAttribute =
        new StringModelAttribute(
            testModelAttributeListener, Field.NACHNAME, 0, 10, testAttributeAccessor);
    stringModelAttribute.setNewValue(true, "abc", false, true);
    assertEquals("abc", testAttributeAccessor.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals("", testModelAttributeListener.getOldValue());
    assertEquals("abc", testModelAttributeListener.getNewValue());
  }

  @Test
  void testInitValue_Null() {
    TestAttributeAccessor<String> testAttributeAccessor = new TestAttributeAccessor<>("TestValue");
    StringModelAttribute stringModelAttribute =
        new StringModelAttribute(
            testModelAttributeListener, Field.NACHNAME, 0, 10, testAttributeAccessor);
    stringModelAttribute.initValue(null);
    assertNull(testAttributeAccessor.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals("TestValue", testModelAttributeListener.getOldValue());
    assertEquals("", testModelAttributeListener.getNewValue());
  }

  @Test
  void testInitValue_Value() {
    TestAttributeAccessor<String> testAttributeAccessor = new TestAttributeAccessor<>("TestValue");
    StringModelAttribute stringModelAttribute =
        new StringModelAttribute(
            testModelAttributeListener, Field.NACHNAME, 0, 10, testAttributeAccessor);
    stringModelAttribute.initValue("initValue");
    assertEquals("initValue", testAttributeAccessor.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals("TestValue", testModelAttributeListener.getOldValue());
    assertEquals("initValue", testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_NoFormatter() throws SvmValidationException {
    String strasse = "Austrasse 5";
    TestAttributeAccessor<String> testAttributeAccessor = new TestAttributeAccessor<>(strasse);
    StringModelAttribute stringModelAttribute =
        new StringModelAttribute(
            testModelAttributeListener, Field.STRASSE_HAUSNUMMER, 0, 50, testAttributeAccessor);
    String input = "Austr. 5";
    stringModelAttribute.setNewValue(true, input, false);
    assertEquals(input, testAttributeAccessor.getValue());
    assertEquals(strasse, testModelAttributeListener.getOldValue());
    assertEquals(input, testModelAttributeListener.getNewValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
  }

  @Test
  void testSetNewValue_FormatterChanged() throws SvmValidationException {
    String strasse = "Austrasse 5";
    TestAttributeAccessor<String> testAttributeAccessor = new TestAttributeAccessor<>(strasse);
    StringModelAttribute stringModelAttribute =
        new StringModelAttribute(
            testModelAttributeListener,
            Field.STRASSE_HAUSNUMMER,
            0,
            50,
            testAttributeAccessor,
            new StrasseFormatter());
    String input = "Austr. 5";
    stringModelAttribute.setNewValue(true, input, false);
    assertEquals(strasse, testAttributeAccessor.getValue());
    assertEquals(input, testModelAttributeListener.getOldValue());
    assertEquals(strasse, testModelAttributeListener.getNewValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
  }

  @Test
  void testSetNewValue_FormatterUnchanged() throws SvmValidationException {
    String strasse = "Austrasse 5";
    TestAttributeAccessor<String> testAttributeAccessor = new TestAttributeAccessor<>(strasse);
    StringModelAttribute stringModelAttribute =
        new StringModelAttribute(
            testModelAttributeListener,
            Field.STRASSE_HAUSNUMMER,
            0,
            50,
            testAttributeAccessor,
            new StrasseFormatter());
    stringModelAttribute.setNewValue(true, strasse, false);
    assertEquals(strasse, testAttributeAccessor.getValue());
    assertEquals(strasse, testModelAttributeListener.getOldValue());
    assertEquals(strasse, testModelAttributeListener.getNewValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
  }
}
