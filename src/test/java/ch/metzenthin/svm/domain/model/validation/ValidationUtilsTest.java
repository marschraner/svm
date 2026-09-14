package ch.metzenthin.svm.domain.model.validation;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * @author Martin Schraner
 */
class ValidationUtilsTest {

  // Source:
  // http://www.mkyong.com/regular-expressions/how-to-validate-email-address-with-regular-expression/

  @Test
  void testValidateEmailValid_ifNotEmptyEmails() {
    assertTrue(ValidationUtils.validateEmail("mkyong@yahoo.com"));
    assertTrue(ValidationUtils.validateEmail("mkyong-100@yahoo.com"));
    assertTrue(ValidationUtils.validateEmail("mkyong.100@yahoo.com"));
    assertTrue(ValidationUtils.validateEmail("mkyong111@mkyong.com"));
    assertTrue(ValidationUtils.validateEmail("mkyong-100@mkyong.net"));
    assertTrue(ValidationUtils.validateEmail("mkyong.100@mkyong.com.au"));
    assertTrue(ValidationUtils.validateEmail("mkyong@1.com"));
    assertTrue(ValidationUtils.validateEmail("mkyong@gmail.com.com"));
    assertTrue(ValidationUtils.validateEmail("mkyong+100@gmail.com"));
    assertTrue(ValidationUtils.validateEmail("mkyong-100@yahoo-test.com"));
    assertTrue(ValidationUtils.validateEmail("mk.young@alumni.uni-honkong.de"));
  }

  @Test
  void testValidateEmail_invalidEmails() {
    assertFalse(ValidationUtils.validateEmail("mkyong"));
    assertFalse(ValidationUtils.validateEmail("mkyong@.com.my"));
    assertFalse(ValidationUtils.validateEmail("mkyong123@gmail.a"));
    assertFalse(ValidationUtils.validateEmail("mkyong123@.com"));
    assertFalse(ValidationUtils.validateEmail("mkyong123@.com.com"));
    assertFalse(ValidationUtils.validateEmail(".mkyong@mkyong.com"));
    assertFalse(ValidationUtils.validateEmail("mkyong()*@gmail.com"));
    assertFalse(ValidationUtils.validateEmail("mkyong@%*.com"));
    assertFalse(ValidationUtils.validateEmail("mkyong..2002@gmail.com"));
    assertFalse(ValidationUtils.validateEmail("mkyong.@gmail.com"));
    assertFalse(ValidationUtils.validateEmail("mkyong@mkyong@gmail.com"));
    assertFalse(ValidationUtils.validateEmail("mkyong@gmail.com.1a"));
  }

  @Test
  void testValidateIbanNummer_Ch() {

    // Genau 21 Zeichen
    assertTrue(ValidationUtils.validateIbanNummer("CH3181239000001245689"));
    assertTrue(ValidationUtils.validateIbanNummer("CH31 8123 9000 0012 4568 9"));
    assertTrue(ValidationUtils.validateIbanNummer("CH31 8123 9000 0012 4568 A"));
    assertTrue(ValidationUtils.validateIbanNummer("CH31 81 23 90 00 00 12 45 68  9"));

    // Mehr oder weniger als 21 Stellen
    assertFalse(ValidationUtils.validateIbanNummer("CH31 8123 9000 0012 4568"));
    assertFalse(ValidationUtils.validateIbanNummer("CH31 8123 9000 0012 4568 99"));

    // Land mit Kleinschreibung
    assertFalse(ValidationUtils.validateIbanNummer("ch3181239000001245689"));

    // Konventionelle Kontonummer
    assertFalse(ValidationUtils.validateIbanNummer("12-1234-2-5"));

    // Keine alphanumerische Zeichen
    assertFalse(ValidationUtils.validateIbanNummer("CH31 8123 9000 0012 4568 -"));
  }

  @Test
  void testValidateIbanNummerIfNotEmpty_notCh() {

    // Mindestens 15, aber höchstens 34 Zeichen
    assertTrue(ValidationUtils.validateIbanNummer("NO31 8123 9000 001"));
    assertTrue(ValidationUtils.validateIbanNummer("DE31 8123 9000 9999 AB"));
    assertTrue(ValidationUtils.validateIbanNummer("XX31 8123 9000 0012 4444 4444 4444 4444 44"));

    // Weniger als 15 Zeichen
    assertFalse(ValidationUtils.validateIbanNummer("NO31 8123 9000 00"));

    // Mehr als 34 Zeichen
    assertFalse(ValidationUtils.validateIbanNummer("XX31 8123 9000 0012 4444 4444 4444 4444 445"));

    // Keine alphanumerische Zeichen
    assertFalse(ValidationUtils.validateIbanNummer("NO31 8123 9000 0012 -"));
  }
}
