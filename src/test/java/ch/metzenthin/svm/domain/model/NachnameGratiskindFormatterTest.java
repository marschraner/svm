package ch.metzenthin.svm.domain.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Martin Schraner
 */
public class NachnameGratiskindFormatterTest {

    private NachnameGratiskindFormatter nachnameGratiskindFormatter = new NachnameGratiskindFormatter();

    @Test
    public void testFormatStr1() throws Exception {
        assertEquals("Sonja", nachnameGratiskindFormatter.format("Sonja Gratiskind"));
    }

    @Test
    public void testFormatStr2() throws Exception {
        assertEquals("Sonja", nachnameGratiskindFormatter.format("Sonja nicht gratis"));
    }

    @Test
    public void testFormatStr3() throws Exception {
        assertEquals("Sonja", nachnameGratiskindFormatter.format("Sonja Nicht Gratis"));
    }

    @Test
    public void testFormatStr4() throws Exception {
        assertEquals("Sonja nicht", nachnameGratiskindFormatter.format("Sonja nicht"));
    }
}