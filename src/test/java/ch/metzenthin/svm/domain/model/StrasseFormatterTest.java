package ch.metzenthin.svm.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Hans Stamm
 */
class StrasseFormatterTest {

  private StrasseFormatter strasseFormatter;

  @BeforeEach
  void before() {
    strasseFormatter = new StrasseFormatter();
  }

  @Test
  void testFormatStr1() {
    assertEquals("Austrasse 5", strasseFormatter.format("Austr. 5"));
  }

  @Test
  void testFormatStr2() {
    assertEquals("Au-Strasse 5", strasseFormatter.format("Au-Str. 5"));
  }

  @Test
  void testFormat_Null() {
    assertNull(strasseFormatter.format(null));
  }

  @Test
  void testFormat_Nullstring() {
    assertEquals("", strasseFormatter.format(""));
  }
}
