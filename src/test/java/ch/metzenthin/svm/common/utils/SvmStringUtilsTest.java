package ch.metzenthin.svm.common.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * @author Martin Schraner
 */
class SvmStringUtilsTest {

  @Test
  void testReplaceLineBreaksBySemicolonOrPeriod() {

    assertEquals(
        "Nur eine Zeile", SvmStringUtils.replaceLineBreaksBySemicolonOrPeriod("Nur eine Zeile"));

    assertEquals(
        "Erste Zeile. Zweite Zeile; ditte Zeile, vierte Zeile! Fünfte Zeile! Sechste Zeile",
        SvmStringUtils.replaceLineBreaksBySemicolonOrPeriod(
            "Erste Zeile.\nZweite Zeile;\nditte Zeile,\nvierte Zeile!\nFünfte Zeile!\nSechste Zeile"));
    assertEquals(
        "Zeile 1. Äh Zeile 2; öh Zeile 3, üh Zeile 4! Öh Zeile 5? Üh Zeile 6",
        SvmStringUtils.replaceLineBreaksBySemicolonOrPeriod(
            "Zeile 1. \n Äh Zeile 2; \n öh Zeile 3, \n üh Zeile 4! \n Öh Zeile 5? \n Üh Zeile 6"));

    assertEquals(
        "Erste Zeile; zweite Zeile. Dritte Zeile",
        SvmStringUtils.replaceLineBreaksBySemicolonOrPeriod(
            "Erste Zeile\nzweite Zeile\nDritte Zeile"));
    assertEquals(
        "Zeile 1. Äh Zeile 2; äh Zeile 3",
        SvmStringUtils.replaceLineBreaksBySemicolonOrPeriod(
            "\n\nZeile 1 \n Äh Zeile 2 \n äh Zeile 3\n"));

    assertNull(SvmStringUtils.replaceLineBreaksBySemicolonOrPeriod(null));
  }

  @Test
  void testReplaceLineBreaksByCommaOrPeriod() {

    assertEquals(
        "Nur eine Zeile", SvmStringUtils.replaceLineBreaksByCommaOrPeriod("Nur eine Zeile"));

    assertEquals(
        "Erste Zeile. Zweite Zeile; ditte Zeile, vierte Zeile! Fünfte Zeile! Sechste Zeile",
        SvmStringUtils.replaceLineBreaksByCommaOrPeriod(
            "Erste Zeile.\nZweite Zeile;\nditte Zeile,\nvierte Zeile!\nFünfte Zeile!\nSechste Zeile"));
    assertEquals(
        "Zeile 1. Äh Zeile 2; öh Zeile 3, üh Zeile 4! Öh Zeile 5? Üh Zeile 6",
        SvmStringUtils.replaceLineBreaksByCommaOrPeriod(
            "Zeile 1. \n Äh Zeile 2; \n öh Zeile 3, \n üh Zeile 4! \n Öh Zeile 5? \n Üh Zeile 6"));

    assertEquals(
        "Erste Zeile, zweite Zeile. Dritte Zeile",
        SvmStringUtils.replaceLineBreaksByCommaOrPeriod("Erste Zeile\nzweite Zeile\nDritte Zeile"));
    assertEquals(
        "Zeile 1. Äh Zeile 2, äh Zeile 3",
        SvmStringUtils.replaceLineBreaksByCommaOrPeriod(
            "\n\nZeile 1 \n Äh Zeile 2 \n äh Zeile 3\n"));

    assertNull(SvmStringUtils.replaceLineBreaksByCommaOrPeriod(null));
  }

  @Test
  void testSplitStringIntoMultipleLinesLeerschlag() {
    String string = "Dies ist eine Zeile, die zu lange ist.";
    List<String> lines = SvmStringUtils.splitStringIntoMultipleLines(string, 15, 3);
    assertEquals(3, lines.size());
    assertEquals("Dies ist eine", lines.get(0));
    assertEquals("Zeile, die zu", lines.get(1));
    assertEquals("lange ist.", lines.get(2));
  }

  @Test
  void testSplitStringIntoMultipleLinesLeerschlagMaxLines() {
    String string = "Dies ist eine Zeile, die zu lange ist.";
    List<String> lines = SvmStringUtils.splitStringIntoMultipleLines(string, 15, 2);
    assertEquals(2, lines.size());
    assertEquals("Dies ist eine", lines.get(0));
    assertEquals("Zeile, die zu lange ist.", lines.get(1));
  }

  @Test
  void testSplitStringIntoMultipleLinesBindestrich() {
    String string = "Rhythmik-Darstellendes Spiel";
    List<String> lines = SvmStringUtils.splitStringIntoMultipleLines(string, 20, 2);
    assertEquals(2, lines.size());
    assertEquals("Rhythmik-", lines.get(0));
    assertEquals("Darstellendes Spiel", lines.get(1));
  }

  @Test
  void testSplitStringIntoMultipleLinesSchraegstrich() {
    String string = "Mittwoch Morgen/Abend";
    List<String> lines = SvmStringUtils.splitStringIntoMultipleLines(string, 18, 2);
    assertEquals(2, lines.size());
    assertEquals("Mittwoch Morgen/", lines.get(0));
    assertEquals("Abend", lines.get(1));
  }
}
