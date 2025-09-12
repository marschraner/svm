package ch.metzenthin.svm.domain.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

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
  public void testFormatStr1() {
    assertEquals("Austrasse 5", strasseFormatter.format("Austr. 5"));
  }

  @Test
  public void testFormatStr2() {
    assertEquals("Au-Strasse 5", strasseFormatter.format("Au-Str. 5"));
  }

  @Test
  public void testFormat_Null() {
    assertNull(strasseFormatter.format(null));
  }

  @Test
  public void testFormat_Nullstring() {
    assertEquals("", strasseFormatter.format(""));
  }
}
