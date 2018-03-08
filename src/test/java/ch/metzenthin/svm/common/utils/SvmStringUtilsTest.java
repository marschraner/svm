package ch.metzenthin.svm.common.utils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Martin Schraner
 */
public class SvmStringUtilsTest {

    @Test
    public void testReplaceLineBreaksBySemicolonOrPeriod() {

        assertEquals("Nur eine Zeile",
                SvmStringUtils.replaceLineBreaksBySemicolonOrPeriod("Nur eine Zeile"));

        assertEquals("Erste Zeile. Zweite Zeile; ditte Zeile, vierte Zeile! Fünfte Zeile! Sechste Zeile",
                SvmStringUtils.replaceLineBreaksBySemicolonOrPeriod("Erste Zeile.\nZweite Zeile;\nditte Zeile,\nvierte Zeile!\nFünfte Zeile!\nSechste Zeile"));
        assertEquals("Zeile 1. Äh Zeile 2; öh Zeile 3, üh Zeile 4! Öh Zeile 5? Üh Zeile 6",
                SvmStringUtils.replaceLineBreaksBySemicolonOrPeriod("Zeile 1. \n Äh Zeile 2; \n öh Zeile 3, \n üh Zeile 4! \n Öh Zeile 5? \n Üh Zeile 6"));

        assertEquals("Erste Zeile; zweite Zeile. Dritte Zeile",
                SvmStringUtils.replaceLineBreaksBySemicolonOrPeriod("Erste Zeile\nzweite Zeile\nDritte Zeile"));
        assertEquals("Zeile 1. Äh Zeile 2; äh Zeile 3",
                SvmStringUtils.replaceLineBreaksBySemicolonOrPeriod("Zeile 1 \n Äh Zeile 2 \n äh Zeile 3"));

        assertNull(SvmStringUtils.replaceLineBreaksBySemicolonOrPeriod(null));

    }

    @Test
    public void testReplaceLineBreaksByCommaOrPeriod() {

        assertEquals("Nur eine Zeile",
                SvmStringUtils.replaceLineBreaksByCommaOrPeriod("Nur eine Zeile"));

        assertEquals("Erste Zeile. Zweite Zeile; ditte Zeile, vierte Zeile! Fünfte Zeile! Sechste Zeile",
                SvmStringUtils.replaceLineBreaksByCommaOrPeriod("Erste Zeile.\nZweite Zeile;\nditte Zeile,\nvierte Zeile!\nFünfte Zeile!\nSechste Zeile"));
        assertEquals("Zeile 1. Äh Zeile 2; öh Zeile 3, üh Zeile 4! Öh Zeile 5? Üh Zeile 6",
                SvmStringUtils.replaceLineBreaksByCommaOrPeriod("Zeile 1. \n Äh Zeile 2; \n öh Zeile 3, \n üh Zeile 4! \n Öh Zeile 5? \n Üh Zeile 6"));

        assertEquals("Erste Zeile, zweite Zeile. Dritte Zeile",
                SvmStringUtils.replaceLineBreaksByCommaOrPeriod("Erste Zeile\nzweite Zeile\nDritte Zeile"));
        assertEquals("Zeile 1. Äh Zeile 2, äh Zeile 3",
                SvmStringUtils.replaceLineBreaksByCommaOrPeriod("Zeile 1 \n Äh Zeile 2 \n äh Zeile 3"));

        assertNull(SvmStringUtils.replaceLineBreaksByCommaOrPeriod(null));

    }
}