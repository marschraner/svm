package ch.metzenthin.svm.domain.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Hans Stamm
 */
public class StrasseFormatterTest {

    private StrasseFormatter strasseFormatter;

    @Before
    public void before() {
        strasseFormatter = new StrasseFormatter();
    }

    @Test
    public void testFormatStr1() throws Exception {
        assertEquals("Austrasse 5", strasseFormatter.format("Austr. 5"));
    }

    @Test
    public void testFormatStr2() throws Exception {
        assertEquals("Au-Strasse 5", strasseFormatter.format("Au-Str. 5"));
    }

    @Test
    public void testFormat_Null() throws Exception {
        assertNull(strasseFormatter.format(null));
    }

    @Test
    public void testFormat_Nullstring() throws Exception {
        assertEquals("", strasseFormatter.format(""));
    }

}