package ch.metzenthin.svm.domain;

import ch.metzenthin.svm.dataTypes.Field;
import org.junit.Test;

import static org.junit.Assert.*;

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
    public void testGetMessage_OneField() {
        SvmValidationException e = new SvmValidationException(1, "Fehler", Field.NACHNAME);
        assertEquals("Fehler: [" + Field.NACHNAME.toString() + "]", e.getMessage());
    }

    @Test
    public void testGetMessage_TwoFields() {
        SvmValidationException e = new SvmValidationException(1, "Fehler", Field.NACHNAME, Field.VORNAME);
        assertTrue(e.getMessage().startsWith("Fehler: ["));
        assertTrue(e.getMessage().endsWith("]"));
        assertTrue(e.getMessage().indexOf(",") == e.getMessage().lastIndexOf(","));
        assertTrue(e.getMessage().contains(Field.NACHNAME.toString()));
        assertTrue(e.getMessage().contains(Field.VORNAME.toString()));
        System.out.println(e.getMessage());
    }

    @Test
    public void testGetMessage_SvmRequiredException() {
        SvmValidationException e = new SvmRequiredException(Field.NACHNAME);
        assertEquals("Attribut ist obligatorisch: [" + Field.NACHNAME.toString() + "]", e.getMessage());
    }

}