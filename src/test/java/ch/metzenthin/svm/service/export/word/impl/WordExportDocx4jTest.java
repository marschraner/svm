package ch.metzenthin.svm.service.export.word.impl;

import static org.junit.jupiter.api.Assertions.*;

import ch.metzenthin.svm.service.export.word.CellLayout;
import org.junit.jupiter.api.Test;

/**
 * @author Hans Stamm
 */
class WordExportDocx4jTest {

  @Test
  void testGetFontSize() {
    // Max length:   22    23    24    25    26    28
    // Text length:  23    24    25    26    27    29
    //                                       28    30 ...
    // FontSize:    "20", "19", "18", "17", "16", "14", "12", "10"
    // FontSize = FontSize[erster Index (Text length > Max length) + 1]
    CellLayout cellLayout = new CellLayout(false, 0, new int[] {22, 23, 24, 25, 26, 28});
    String fontSize;
    fontSize = WordExportDocx4j.getFontSize(cellLayout, "123");
    assertEquals("20", fontSize);
    fontSize = WordExportDocx4j.getFontSize(cellLayout, "1234567890123456789012");
    assertEquals("20", fontSize);
    fontSize = WordExportDocx4j.getFontSize(cellLayout, "12345678901234567890123");
    assertEquals("19", fontSize);
    fontSize = WordExportDocx4j.getFontSize(cellLayout, "123456789012345678901234");
    assertEquals("18", fontSize);
    fontSize = WordExportDocx4j.getFontSize(cellLayout, "1234567890123456789012345");
    assertEquals("17", fontSize);
    fontSize = WordExportDocx4j.getFontSize(cellLayout, "12345678901234567890123456");
    assertEquals("16", fontSize);
    fontSize = WordExportDocx4j.getFontSize(cellLayout, "123456789012345678901234567");
    assertEquals("14", fontSize);
    fontSize = WordExportDocx4j.getFontSize(cellLayout, "1234567890123456789012345678");
    assertEquals("14", fontSize);
    fontSize = WordExportDocx4j.getFontSize(cellLayout, "12345678901234567890123456789");
    assertEquals("12", fontSize);
    fontSize = WordExportDocx4j.getFontSize(cellLayout, "123456789012345678901234567890");
    assertEquals("12", fontSize);
    fontSize = WordExportDocx4j.getFontSize(cellLayout, "1234567890123456789012345678901");
    assertEquals("12", fontSize);
  }
}
