package ch.metzenthin.svm.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * @author Martin Schraner
 */
class BindestrichLeerzeichenFormatterTest {

  private final BindestrichLeerzeichenFormatter bindestrichLeerzeichenFormatter =
      new BindestrichLeerzeichenFormatter();

  @Test
  void testFormatStr1() {
    assertEquals("Müller-Meier", bindestrichLeerzeichenFormatter.format("Müller - Meier"));
  }

  @Test
  void testFormatStr2() {
    assertEquals("Müller Meier", bindestrichLeerzeichenFormatter.format("Müller   Meier"));
  }
}
