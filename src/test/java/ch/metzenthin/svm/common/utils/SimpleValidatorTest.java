package ch.metzenthin.svm.common.utils;

import org.junit.Test;

import java.util.Date;

import static ch.metzenthin.svm.common.utils.SimpleValidator.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Hans Stamm
 */
public class SimpleValidatorTest {

    @Test
    public void testCheckNotEmpty_Null() {
        assertFalse("False erwartet (null)", checkNotEmpty(null));
    }

    @Test
    public void testCheckNotEmpty_Empty() {
        assertFalse("False erwartet (empty)", checkNotEmpty(""));
    }

    @Test
    public void testCheckNotEmpty_NotEmpty() {
        assertTrue("True erwartet (not empty)", checkNotEmpty("abc"));
    }

    @Test
    public void testCheckNotNull_Null() {
        assertFalse("False erwartet (null)", checkNotNull(null));
    }

    @Test
    public void testCheckNotNull_NotNullString() {
        assertTrue("True erwartet (String)", checkNotNull("xx"));
    }

    @Test
    public void testCheckNotNull_NotNullEmptyString() {
        assertTrue("True erwartet (empty string)", checkNotNull(""));
    }

    @Test
    public void testCheckNotNull_NotNullDate() {
        assertTrue("True erwartet (Date)", checkNotNull(new Date()));
    }

    @Test
    public void testCheckNumber_Null() {
        assertFalse("False erwartet (null)", checkNumber(null));
    }

    @Test
    public void testCheckNumber_NullEmptyString() {
        assertFalse("False erwartet (empty string)", checkNumber(""));
    }

    @Test
    public void testCheckNumber_NotNumber() {
        assertFalse("False erwartet (not a number)", checkNumber("5.5"));
    }

    @Test
    public void testCheckNumber_Number() {
        assertTrue("True erwartet (number)", checkNumber("5"));
    }

}