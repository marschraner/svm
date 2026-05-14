package ch.metzenthin.svm.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Martin Schraner
 */
class IbanNummerFormatterTest {

  private IbanNummerFormatter ibanNummerFormatter;

  @BeforeEach
  void before() {
    ibanNummerFormatter = new IbanNummerFormatter();
  }

  @Test
  void testFormat() {
    assertEquals("CH31 8123 9000 0012 4568 9", ibanNummerFormatter.format("CH3181239000001245689"));
  }
}
