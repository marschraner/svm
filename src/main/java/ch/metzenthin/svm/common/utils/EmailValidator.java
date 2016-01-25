package ch.metzenthin.svm.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Martin Schraner
 */
public class EmailValidator {

    // Source: http://www.mkyong.com/regular-expressions/how-to-validate-email-address-with-regular-expression/

    private Pattern pattern;

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public EmailValidator() {
        pattern = Pattern.compile(EMAIL_PATTERN);
    }

    /**
     * Validate hex with regular expression
     *
     * @param email
     *            hex for validation
     * @return true valid hex, false invalid hex
     */
    public boolean isValid(final String email) {

        Matcher matcher = pattern.matcher(email);
        return matcher.matches();

    }
}
