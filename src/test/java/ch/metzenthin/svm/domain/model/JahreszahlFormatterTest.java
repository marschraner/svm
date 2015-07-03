package ch.metzenthin.svm.domain.model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Martin Schraner
 */
public class JahreszahlFormatterTest {

    private JahreszahlFormatter jahreszahlFormatter = new JahreszahlFormatter();

    @Test
    public void testFormat_Zweistellig_vor_00() throws Exception {
        assertEquals("11.11.1999", jahreszahlFormatter.format("11.11.99"));
    }

    @Test
    public void testFormat_Zweistellig_00() throws Exception {
        assertEquals("11.11.2000", jahreszahlFormatter.format("11.11.00"));
    }

    @Test
    public void testFormat_Zweistellig_nach_00() throws Exception {
        assertEquals("11.11.2001", jahreszahlFormatter.format("11.11.01"));
    }

    @Test
    public void testFormat_Vierstellig() throws Exception {
        assertEquals("11.11.2001", jahreszahlFormatter.format("11.11.2001"));
    }
}