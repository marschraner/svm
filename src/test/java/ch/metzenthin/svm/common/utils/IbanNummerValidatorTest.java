package ch.metzenthin.svm.common.utils;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Martin Schraner
 */
public class IbanNummerValidatorTest {

    private IbanNummerValidator ibanNummerValidator = new IbanNummerValidator();

    @Test
    public void testIsValid_Ch() {

        // Genau 21 Zeichen
        assertTrue(ibanNummerValidator.isValid("CH3181239000001245689"));
        assertTrue(ibanNummerValidator.isValid("CH31 8123 9000 0012 4568 9"));
        assertTrue(ibanNummerValidator.isValid("CH31 8123 9000 0012 4568 A"));
        assertTrue(ibanNummerValidator.isValid("CH31 81 23 90 00 00 12 45 68  9"));

        // Mehr oder weniger als 21 Stellen
        assertFalse(ibanNummerValidator.isValid("CH31 8123 9000 0012 4568"));
        assertFalse(ibanNummerValidator.isValid("CH31 8123 9000 0012 4568 99"));

        // Land mit Kleinschreibung
        assertFalse(ibanNummerValidator.isValid("ch3181239000001245689"));

        // Konventionelle Kontonummer
        assertFalse(ibanNummerValidator.isValid("12-1234-2-5"));

        // Keine alphanumerische Zeichen
        assertFalse(ibanNummerValidator.isValid("CH31 8123 9000 0012 4568 -"));
    }

    @Test
    public void testIsValid_notCh() {

        // Mindestens 15, aber höchstens 34 Zeichen
        assertTrue(ibanNummerValidator.isValid("NO31 8123 9000 001"));
        assertTrue(ibanNummerValidator.isValid("DE31 8123 9000 9999 AB"));
        assertTrue(ibanNummerValidator.isValid("XX31 8123 9000 0012 4444 4444 4444 4444 44"));

        // Weniger als 15 Zeichen
        assertFalse(ibanNummerValidator.isValid("NO31 8123 9000 00"));

        // Mehr als 34 Zeichen
        assertFalse(ibanNummerValidator.isValid("XX31 8123 9000 0012 4444 4444 4444 4444 445"));

        // Keine alphanumerische Zeichen
        assertFalse(ibanNummerValidator.isValid("NO31 8123 9000 0012 -"));
    }


}