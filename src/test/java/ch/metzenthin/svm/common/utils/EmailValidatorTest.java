package ch.metzenthin.svm.common.utils;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Martin Schraner
 */
public class EmailValidatorTest {

    // Source: http://www.mkyong.com/regular-expressions/how-to-validate-email-address-with-regular-expression/

    private EmailValidator emailValidator = new EmailValidator();

    @Test
    public void testValidate_validEmails() {
        String[]validEmails = new String[] { "mkyong@yahoo.com",
                "mkyong-100@yahoo.com", "mkyong.100@yahoo.com",
                "mkyong111@mkyong.com", "mkyong-100@mkyong.net",
                "mkyong.100@mkyong.com.au", "mkyong@1.com",
                "mkyong@gmail.com.com", "mkyong+100@gmail.com",
                "mkyong-100@yahoo-test.com" };
        for (String email : validEmails) {
            boolean valid = emailValidator.isValid(email);
            assertTrue(valid);
        }
    }

    @Test
    public void testValidate_invalidEmails() {
        String[] invalidEmails = new String[] { "mkyong", "mkyong@.com.my",
                "mkyong123@gmail.a", "mkyong123@.com", "mkyong123@.com.com",
                ".mkyong@mkyong.com", "mkyong()*@gmail.com", "mkyong@%*.com",
                "mkyong..2002@gmail.com", "mkyong.@gmail.com",
                "mkyong@mkyong@gmail.com", "mkyong@gmail.com.1a" };
        for (String email : invalidEmails) {
            boolean valid = emailValidator.isValid(email);
            assertFalse(valid);
        }
    }

}