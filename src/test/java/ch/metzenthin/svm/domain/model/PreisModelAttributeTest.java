package ch.metzenthin.svm.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Hans Stamm
 */
class PreisModelAttributeTest {

  private static final BigDecimal MIN_VALID_VALUE = new BigDecimal("10.00");
  private static final BigDecimal MAX_VALID_VALUE = new BigDecimal("999.95");

  TestModelAttributeListener testModelAttributeListener;

  @BeforeEach
  void setUp() {
    testModelAttributeListener = new TestModelAttributeListener();
  }

  @Test
  void testGetValue_Null() throws SvmValidationException {
    PreisModelAttribute preisModelAttribute =
        new PreisModelAttribute(
            testModelAttributeListener,
            Field.BETRAG_1_KIND,
            MIN_VALID_VALUE,
            MAX_VALID_VALUE,
            new TestAttributeAccessor<>(new BigDecimal("100.00")));
    preisModelAttribute.setNewValue(false, null, false);
    assertNull(preisModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(1, testModelAttributeListener.getPropertyChangeCounter());
    assertEquals("100.00", testModelAttributeListener.getOldValue());
    assertNull(testModelAttributeListener.getNewValue());
  }

  @Test
  void testGetValue_NotNull() throws SvmValidationException {
    PreisModelAttribute preisModelAttribute =
        new PreisModelAttribute(
            testModelAttributeListener,
            Field.BETRAG_1_KIND,
            MIN_VALID_VALUE,
            MAX_VALID_VALUE,
            new TestAttributeAccessor<>(null));
    preisModelAttribute.setNewValue(false, "100", false);
    assertEquals(new BigDecimal("100.00"), preisModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(1, testModelAttributeListener.getPropertyChangeCounter());
    assertNull(testModelAttributeListener.getOldValue());
    assertEquals("100.00", testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_IsRequired_Null() {
    PreisModelAttribute preisModelAttribute =
        new PreisModelAttribute(
            testModelAttributeListener,
            Field.BETRAG_1_KIND,
            MIN_VALID_VALUE,
            MAX_VALID_VALUE,
            new TestAttributeAccessor<>(null));
    assertThrows(
        SvmRequiredException.class, () -> preisModelAttribute.setNewValue(true, null, false));
    assertEquals(1, testModelAttributeListener.getInvalidateCounter());
    assertEquals(0, testModelAttributeListener.getFireCounter());
    assertEquals(0, testModelAttributeListener.getPropertyChangeCounter());
  }

  @Test
  void testSetNewValue_IsRequired_Null_BulkUpdate() throws SvmValidationException {
    PreisModelAttribute preisModelAttribute =
        new PreisModelAttribute(
            testModelAttributeListener,
            Field.BETRAG_1_KIND,
            MIN_VALID_VALUE,
            MAX_VALID_VALUE,
            new TestAttributeAccessor<>(null));
    preisModelAttribute.setNewValue(true, null, true);
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(0, testModelAttributeListener.getPropertyChangeCounter());
    assertNull(testModelAttributeListener.getOldValue());
    assertNull(testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_IsRequired_Empty() {
    PreisModelAttribute preisModelAttribute =
        new PreisModelAttribute(
            testModelAttributeListener,
            Field.BETRAG_1_KIND,
            MIN_VALID_VALUE,
            MAX_VALID_VALUE,
            new TestAttributeAccessor<>(null));
    assertThrows(
        SvmRequiredException.class, () -> preisModelAttribute.setNewValue(true, "", false));
    assertEquals(1, testModelAttributeListener.getInvalidateCounter());
    assertEquals(0, testModelAttributeListener.getFireCounter());
    assertEquals(0, testModelAttributeListener.getPropertyChangeCounter());
  }

  @Test
  void testSetNewValue_IsRequired_NotEmpty() throws SvmValidationException {
    PreisModelAttribute preisModelAttribute =
        new PreisModelAttribute(
            testModelAttributeListener,
            Field.BETRAG_1_KIND,
            MIN_VALID_VALUE,
            MAX_VALID_VALUE,
            new TestAttributeAccessor<>(null));
    preisModelAttribute.setNewValue(true, "100", false);
    assertEquals(new BigDecimal("100.00"), preisModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(1, testModelAttributeListener.getPropertyChangeCounter());
    assertNull(testModelAttributeListener.getOldValue());
    assertEquals("100.00", testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_IsRequired_NotEmpty_BulkUpdate() throws SvmValidationException {
    PreisModelAttribute preisModelAttribute =
        new PreisModelAttribute(
            testModelAttributeListener,
            Field.BETRAG_1_KIND,
            MIN_VALID_VALUE,
            MAX_VALID_VALUE,
            new TestAttributeAccessor<>(null));
    preisModelAttribute.setNewValue(true, "9", true);
    // Value ist kleiner als MIN_VALID_VALUE, aber wegen isBulkUpdate=true wird keine Validation
    // durchgeführt
    assertEquals(new BigDecimal("9.00"), preisModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(1, testModelAttributeListener.getPropertyChangeCounter());
    assertNull(testModelAttributeListener.getOldValue());
    assertEquals("9.00", testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_IsNotRequired_Null() throws SvmValidationException {
    PreisModelAttribute preisModelAttribute =
        new PreisModelAttribute(
            testModelAttributeListener,
            Field.BETRAG_1_KIND,
            MIN_VALID_VALUE,
            MAX_VALID_VALUE,
            new TestAttributeAccessor<>(null));
    preisModelAttribute.setNewValue(false, null, false);
    assertNull(preisModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(0, testModelAttributeListener.getPropertyChangeCounter());
    assertNull(testModelAttributeListener.getOldValue());
    assertNull(testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_IsNotRequired_Empty() throws SvmValidationException {
    PreisModelAttribute preisModelAttribute =
        new PreisModelAttribute(
            testModelAttributeListener,
            Field.BETRAG_1_KIND,
            MIN_VALID_VALUE,
            MAX_VALID_VALUE,
            new TestAttributeAccessor<>(null));
    preisModelAttribute.setNewValue(false, "", false);
    assertNull(preisModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(0, testModelAttributeListener.getPropertyChangeCounter());
    assertNull(testModelAttributeListener.getOldValue());
    assertNull(testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_IsNotRequired_NotEmpty() throws SvmValidationException {
    PreisModelAttribute preisModelAttribute =
        new PreisModelAttribute(
            testModelAttributeListener,
            Field.BETRAG_1_KIND,
            MIN_VALID_VALUE,
            MAX_VALID_VALUE,
            new TestAttributeAccessor<>(new BigDecimal("99.00")));
    preisModelAttribute.setNewValue(false, "100.00", false);
    assertEquals(new BigDecimal("100.00"), preisModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(1, testModelAttributeListener.getPropertyChangeCounter());
    assertEquals("99.00", testModelAttributeListener.getOldValue());
    assertEquals("100.00", testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_MinLength_Greater() {
    PreisModelAttribute preisModelAttribute =
        new PreisModelAttribute(
            testModelAttributeListener,
            Field.BETRAG_1_KIND,
            MIN_VALID_VALUE,
            MAX_VALID_VALUE,
            new TestAttributeAccessor<>(null));
    assertThrows(
        SvmValidationException.class, () -> preisModelAttribute.setNewValue(true, "9.99", false));
    assertEquals(1, testModelAttributeListener.getInvalidateCounter());
    assertEquals(0, testModelAttributeListener.getFireCounter());
    assertEquals(0, testModelAttributeListener.getPropertyChangeCounter());
    assertNull(testModelAttributeListener.getOldValue());
    assertNull(testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_MinLength_Equal() throws SvmValidationException {
    PreisModelAttribute preisModelAttribute =
        new PreisModelAttribute(
            testModelAttributeListener,
            Field.BETRAG_1_KIND,
            MIN_VALID_VALUE,
            MAX_VALID_VALUE,
            new TestAttributeAccessor<>(new BigDecimal("99.00")));
    preisModelAttribute.setNewValue(true, "10.00", false);
    assertEquals(new BigDecimal("10.00"), preisModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(1, testModelAttributeListener.getPropertyChangeCounter());
    assertEquals("99.00", testModelAttributeListener.getOldValue());
    assertEquals("10.00", testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_MinLength_Lesser() throws SvmValidationException {
    PreisModelAttribute preisModelAttribute =
        new PreisModelAttribute(
            testModelAttributeListener,
            Field.BETRAG_1_KIND,
            MIN_VALID_VALUE,
            MAX_VALID_VALUE,
            new TestAttributeAccessor<>(new BigDecimal("99.00")));
    preisModelAttribute.setNewValue(true, "10.01", false);
    assertEquals(new BigDecimal("10.01"), preisModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(1, testModelAttributeListener.getPropertyChangeCounter());
    assertEquals("99.00", testModelAttributeListener.getOldValue());
    assertEquals("10.01", testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_MinLength_Zero() throws SvmValidationException {
    PreisModelAttribute preisModelAttribute =
        new PreisModelAttribute(
            testModelAttributeListener,
            Field.BETRAG_1_KIND,
            new BigDecimal("0.00"),
            MAX_VALID_VALUE,
            new TestAttributeAccessor<>(new BigDecimal("99.00")));
    preisModelAttribute.setNewValue(true, "1.00", false);
    assertEquals(new BigDecimal("1.00"), preisModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(1, testModelAttributeListener.getPropertyChangeCounter());
    assertEquals("99.00", testModelAttributeListener.getOldValue());
    assertEquals("1.00", testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_MaxLength_Lesser() {
    PreisModelAttribute preisModelAttribute =
        new PreisModelAttribute(
            testModelAttributeListener,
            Field.BETRAG_1_KIND,
            MIN_VALID_VALUE,
            MAX_VALID_VALUE,
            new TestAttributeAccessor<>(null));
    assertThrows(
        SvmValidationException.class, () -> preisModelAttribute.setNewValue(true, "999.96", false));
    assertEquals(1, testModelAttributeListener.getInvalidateCounter());
    assertEquals(0, testModelAttributeListener.getFireCounter());
    assertEquals(0, testModelAttributeListener.getPropertyChangeCounter());
    assertNull(testModelAttributeListener.getOldValue());
    assertNull(testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_MaxLength_Equal() throws SvmValidationException {
    PreisModelAttribute preisModelAttribute =
        new PreisModelAttribute(
            testModelAttributeListener,
            Field.BETRAG_1_KIND,
            MIN_VALID_VALUE,
            MAX_VALID_VALUE,
            new TestAttributeAccessor<>(new BigDecimal("99.00")));
    preisModelAttribute.setNewValue(true, "999.95", false);
    assertEquals(new BigDecimal("999.95"), preisModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(1, testModelAttributeListener.getPropertyChangeCounter());
    assertEquals("99.00", testModelAttributeListener.getOldValue());
    assertEquals("999.95", testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_MaxLength_Greater() throws SvmValidationException {
    PreisModelAttribute preisModelAttribute =
        new PreisModelAttribute(
            testModelAttributeListener,
            Field.BETRAG_1_KIND,
            MIN_VALID_VALUE,
            MAX_VALID_VALUE,
            new TestAttributeAccessor<>(new BigDecimal("99.00")));
    preisModelAttribute.setNewValue(true, "999.94", false);
    assertEquals(new BigDecimal("999.94"), preisModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(1, testModelAttributeListener.getPropertyChangeCounter());
    assertEquals("99.00", testModelAttributeListener.getOldValue());
    assertEquals("999.94", testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_Trim() throws SvmValidationException {
    PreisModelAttribute preisModelAttribute =
        new PreisModelAttribute(
            testModelAttributeListener,
            Field.BETRAG_1_KIND,
            MIN_VALID_VALUE,
            MAX_VALID_VALUE,
            new TestAttributeAccessor<>(new BigDecimal("99.00")));
    preisModelAttribute.setNewValue(true, "  100  ", false);
    assertEquals(new BigDecimal("100.00"), preisModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(1, testModelAttributeListener.getPropertyChangeCounter());
    assertEquals("99.00", testModelAttributeListener.getOldValue());
    assertEquals("100.00", testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_SameValueNotFormatted() throws SvmValidationException {
    PreisModelAttribute preisModelAttribute =
        new PreisModelAttribute(
            testModelAttributeListener,
            Field.BETRAG_1_KIND,
            MIN_VALID_VALUE,
            MAX_VALID_VALUE,
            new TestAttributeAccessor<>(new BigDecimal("99.00")));
    preisModelAttribute.setNewValue(true, "99", false);
    assertEquals(new BigDecimal("99.00"), preisModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(0, testModelAttributeListener.getPropertyChangeCounter());
    // Folgender assert müsste eigentlich 99. Fehler im Code!
    assertEquals("99.00", testModelAttributeListener.getOldValue());
    assertEquals("99.00", testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_NotAnInteger() {
    PreisModelAttribute preisModelAttribute =
        new PreisModelAttribute(
            testModelAttributeListener,
            Field.BETRAG_1_KIND,
            MIN_VALID_VALUE,
            MAX_VALID_VALUE,
            new TestAttributeAccessor<>(null));
    SvmValidationException svmValidationException =
        assertThrows(
            SvmValidationException.class, () -> preisModelAttribute.setNewValue(true, "x", false));
    assertEquals("Kein gültiger Preis im Format 'Fr.Rp'", svmValidationException.getMessage());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(0, testModelAttributeListener.getFireCounter());
    assertEquals(0, testModelAttributeListener.getPropertyChangeCounter());
    assertNull(testModelAttributeListener.getOldValue());
    assertNull(testModelAttributeListener.getNewValue());
  }

  @Test
  void testSetNewValue_EnforcePropertyChangeEvent() throws SvmValidationException {
    PreisModelAttribute preisModelAttribute =
        new PreisModelAttribute(
            testModelAttributeListener,
            Field.BETRAG_1_KIND,
            MIN_VALID_VALUE,
            MAX_VALID_VALUE,
            new TestAttributeAccessor<>(new BigDecimal("100.00")));
    preisModelAttribute.setNewValue(true, "100", false, true);
    assertEquals(new BigDecimal("100.00"), preisModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(1, testModelAttributeListener.getPropertyChangeCounter());
    assertEquals("", testModelAttributeListener.getOldValue());
    assertEquals("100.00", testModelAttributeListener.getNewValue());
  }

  @Test
  void testInitValue_Null() {
    PreisModelAttribute preisModelAttribute =
        new PreisModelAttribute(
            testModelAttributeListener,
            Field.BETRAG_1_KIND,
            MIN_VALID_VALUE,
            MAX_VALID_VALUE,
            new TestAttributeAccessor<>(null));
    preisModelAttribute.initValue(null);
    assertNull(preisModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(0, testModelAttributeListener.getPropertyChangeCounter());
    assertNull(testModelAttributeListener.getOldValue());
    assertNull(testModelAttributeListener.getNewValue());
  }

  @Test
  void testInitValue_Value() {
    PreisModelAttribute preisModelAttribute =
        new PreisModelAttribute(
            testModelAttributeListener,
            Field.BETRAG_1_KIND,
            MIN_VALID_VALUE,
            MAX_VALID_VALUE,
            new TestAttributeAccessor<>(new BigDecimal("100.00")));
    preisModelAttribute.initValue("99");
    // in initValue wird weder formatiert noch geprüft!
    assertEquals(new BigDecimal("99"), preisModelAttribute.getValue());
    assertEquals(0, testModelAttributeListener.getInvalidateCounter());
    assertEquals(1, testModelAttributeListener.getFireCounter());
    assertEquals(1, testModelAttributeListener.getPropertyChangeCounter());
    assertEquals("100.00", testModelAttributeListener.getOldValue());
    assertEquals("99", testModelAttributeListener.getNewValue());
  }
}
