package ch.metzenthin.svm.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * @author Martin Schraner
 */
class NachnameGratiskindFormatterTest {

  private final NachnameGratiskindFormatter nachnameGratiskindFormatter =
      new NachnameGratiskindFormatter();

  @Test
  void testFormatStr1() {
    assertEquals("Sonja", nachnameGratiskindFormatter.format("Sonja Gratiskind"));
  }

  @Test
  void testFormatStr2() {
    assertEquals("Sonja", nachnameGratiskindFormatter.format("Sonja nicht gratis"));
  }

  @Test
  void testFormatStr3() {
    assertEquals("Sonja", nachnameGratiskindFormatter.format("Sonja Nicht Gratis"));
  }

  @Test
  void testFormatStr4() {
    assertEquals("Sonja nicht", nachnameGratiskindFormatter.format("Sonja nicht"));
  }
}
