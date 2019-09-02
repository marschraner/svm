package ch.metzenthin.svm.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Martin Schraner
 */
public class EmailValidator {

// Source: http://www.mkyong.com/regular-expressions/how-to-validate-email-address-with-regular-expression/
// (modified)

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");

    /**
     * Validate email with regular expression
     *
     * @param email
     *            email for validation
     * @return true valid email, false invalid email
     */
    public boolean isValid(final String email) {

        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.matches();

    }
}
