package ch.metzenthin.svm.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * @author Martin Schraner
 */
class JahreszahlFormatterTest {

  private final JahreszahlFormatter jahreszahlFormatter = new JahreszahlFormatter();

  @Test
  void testFormat_Zweistellig_vor_00() {
    assertEquals("11.11.1999", jahreszahlFormatter.format("11.11.99"));
  }

  @Test
  void testFormat_Zweistellig_00() {
    assertEquals("11.11.2000", jahreszahlFormatter.format("11.11.00"));
  }

  @Test
  void testFormat_Zweistellig_nach_00() {
    assertEquals("11.11.2001", jahreszahlFormatter.format("11.11.01"));
  }

  @Test
  void testFormat_Vierstellig() {
    assertEquals("11.11.2001", jahreszahlFormatter.format("11.11.2001"));
  }
}
