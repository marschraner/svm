package ch.metzenthin.svm.domain.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Martin Schraner
 */
public class BindestrichLeerzeichenFormatterTest {

    private final BindestrichLeerzeichenFormatter bindestrichLeerzeichenFormatter = new BindestrichLeerzeichenFormatter();

    @Test
    public void testFormatStr1() {
        assertEquals("Müller-Meier", bindestrichLeerzeichenFormatter.format("Müller - Meier"));
    }

    @Test
    public void testFormatStr2() {
        assertEquals("Müller Meier", bindestrichLeerzeichenFormatter.format("Müller   Meier"));
    }
}