package ch.metzenthin.svm.domain.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * @author Hans Stamm
 */
public class AbstractModelTest {

    private AbstractModel abstractModel;

    @Before
    public void setUp() throws Exception {
        abstractModel = new AbstractModel(null) {
            @Override
            public boolean isValid() {
                return false;
            }
        };
    }

    @After
    public void tearDown() throws Exception {
        abstractModel = null;
    }

    @Test
    public void testToIntegerOrNull_Number() throws Exception {
        assertEquals("Resultat ist nicht 44", new Integer(44), abstractModel.toIntegerOrNull("44"));
    }

    @Test
    public void testToIntegerOrNull_Null() throws Exception {
        assertNull("Resultat ist nicht null", abstractModel.toIntegerOrNull("xx"));
    }

    @Test
    public void testToInteger_Number() throws Exception {
        assertEquals("Resultat ist nicht 44", new Integer(44), abstractModel.toInteger("44"));
    }

    @Test
    public void testToInteger_Exception() throws Exception {
        try {
            abstractModel.toInteger("xx");
            fail("NumberFormatException erwartet");
        } catch (NumberFormatException ignore) {
        }
    }

    @Test
    public void testToCalendarIgnoreException_Null() {
        assertNull("null erwartet", abstractModel.toCalendarIgnoreException("xxx"));
    }

    @Test
    public void testToCalendarIgnoreException_NotNull() {
        assertEquals("Datum erwartet", -3600000L, abstractModel.toCalendarIgnoreException("01.01.1970").getTimeInMillis());
    }

    @Test
    public void testToCalendar_Exception() {
        try {
            abstractModel.toCalendar("xxx");
            fail("ParseException erwartet");
        } catch (ParseException ignore) {
        }
    }

    @Test
    public void testCheckNotEmpty_Null() {
        assertFalse("False erwartet (null)", abstractModel.checkNotEmpty(null));
    }

    @Test
    public void testCheckNotEmpty_Empty() {
        assertFalse("False erwartet (empty)", abstractModel.checkNotEmpty(""));
    }

    @Test
    public void testCheckNotEmpty_NotEmpty() {
        assertTrue("True erwartet (not empty)", abstractModel.checkNotEmpty("abc"));
    }

    @Test
    public void testCheckNotNull_Null() {
        assertFalse("False erwartet (null)", abstractModel.checkNotNull(null));
    }

    @Test
    public void testCheckNotNull_NotNullString() {
        assertTrue("True erwartet (String)", abstractModel.checkNotNull("xx"));
    }

    @Test
    public void testCheckNotNull_NotNullEmptyString() {
        assertTrue("True erwartet (empty string)", abstractModel.checkNotNull(""));
    }

    @Test
    public void testCheckNotNull_NotNullDate() {
        assertTrue("True erwartet (Date)", abstractModel.checkNotNull(new Date()));
    }

    @Test
    public void testCheckNumber_Null() {
        assertFalse("False erwartet (null)", abstractModel.checkNumber(null));
    }

    @Test
    public void testCheckNumber_NullEmptyString() {
        assertFalse("False erwartet (empty string)", abstractModel.checkNumber(""));
    }

    @Test
    public void testCheckNumber_NotNumber() {
        assertFalse("False erwartet (not a number)", abstractModel.checkNumber("5.5"));
    }

    @Test
    public void testCheckNumber_Number() {
        assertTrue("True erwartet (number)", abstractModel.checkNumber("5"));
    }

}