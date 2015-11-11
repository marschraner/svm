package ch.metzenthin.svm.domain.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Martin Schraner
 */
public class BindestrichLeerzeichenFormatterTest {

    private BindestrichLeerzeichenFormatter bindestrichLeerzeichenFormatter = new BindestrichLeerzeichenFormatter();

    @Test
    public void testFormatStr1() throws Exception {
        assertEquals("Müller-Meier", bindestrichLeerzeichenFormatter.format("Müller - Meier"));
    }

    @Test
    public void testFormatStr2() throws Exception {
        assertEquals("Müller Meier", bindestrichLeerzeichenFormatter.format("Müller   Meier"));
    }
}