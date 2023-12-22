package ch.metzenthin.svm.domain;

import ch.metzenthin.svm.common.dataTypes.Field;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Hans Stamm
 */
@SuppressWarnings("ThrowableInstanceNeverThrown")
public class SvmValidationExceptionTest {

    @Test
    public void testGetMessage_NoFields() {
        SvmValidationException e = new SvmValidationException(1, "Fehler");
        assertEquals("Fehler", e.getMessage());
    }

    @Test
    public void testGetMessageLong_OneField() {
        SvmValidationException e = new SvmValidationException(1, "Fehler", Field.NACHNAME);
        assertEquals("Fehler: [" + Field.NACHNAME + "]", e.getMessageLong());
    }

    @Test
    public void testGetMessageLong_TwoFields() {
        SvmValidationException e = new SvmValidationException(1, "Fehler", Field.NACHNAME, Field.VORNAME);
        assertTrue(e.getMessageLong().startsWith("Fehler: ["));
        assertTrue(e.getMessageLong().endsWith("]"));
        assertEquals(e.getMessageLong().indexOf(","), e.getMessageLong().lastIndexOf(","));
        assertTrue(e.getMessageLong().contains(Field.NACHNAME.toString()));
        assertTrue(e.getMessageLong().contains(Field.VORNAME.toString()));
        System.out.println(e.getMessageLong());
    }

    @Test
    public void testGetMessage_SvmRequiredException() {
        SvmValidationException e = new SvmRequiredException(Field.NACHNAME);
        assertEquals("Eintrag ist obligatorisch", e.getMessage());
    }

    @Test
    public void testGetMessageLong_SvmRequiredException() {
        SvmValidationException e = new SvmRequiredException(Field.NACHNAME);
        assertEquals("Eintrag ist obligatorisch: [" + Field.NACHNAME + "]", e.getMessageLong());
    }

}