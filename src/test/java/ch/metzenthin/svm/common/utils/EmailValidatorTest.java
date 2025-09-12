package ch.metzenthin.svm.common.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * @author Martin Schraner
 */
public class EmailValidatorTest {

  // Source:
  // http://www.mkyong.com/regular-expressions/how-to-validate-email-address-with-regular-expression/

  private final EmailValidator emailValidator = new EmailValidator();

  @Test
  public void testValidate_validEmails() {
    assertTrue(emailValidator.isValid("mkyong@yahoo.com"));
    assertTrue(emailValidator.isValid("mkyong-100@yahoo.com"));
    assertTrue(emailValidator.isValid("mkyong.100@yahoo.com"));
    assertTrue(emailValidator.isValid("mkyong111@mkyong.com"));
    assertTrue(emailValidator.isValid("mkyong-100@mkyong.net"));
    assertTrue(emailValidator.isValid("mkyong.100@mkyong.com.au"));
    assertTrue(emailValidator.isValid("mkyong@1.com"));
    assertTrue(emailValidator.isValid("mkyong@gmail.com.com"));
    assertTrue(emailValidator.isValid("mkyong+100@gmail.com"));
    assertTrue(emailValidator.isValid("mkyong-100@yahoo-test.com"));
    assertTrue(emailValidator.isValid("mk.young@alumni.uni-honkong.de"));
  }

  @Test
  public void testValidate_invalidEmails() {
    assertFalse(emailValidator.isValid("mkyong"));
    assertFalse(emailValidator.isValid("mkyong@.com.my"));
    assertFalse(emailValidator.isValid("mkyong123@gmail.a"));
    assertFalse(emailValidator.isValid("mkyong123@.com"));
    assertFalse(emailValidator.isValid("mkyong123@.com.com"));
    assertFalse(emailValidator.isValid(".mkyong@mkyong.com"));
    assertFalse(emailValidator.isValid("mkyong()*@gmail.com"));
    assertFalse(emailValidator.isValid("mkyong@%*.com"));
    assertFalse(emailValidator.isValid("mkyong..2002@gmail.com"));
    assertFalse(emailValidator.isValid("mkyong.@gmail.com"));
    assertFalse(emailValidator.isValid("mkyong@mkyong@gmail.com"));
    assertFalse(emailValidator.isValid("mkyong@gmail.com.1a"));
  }
}
