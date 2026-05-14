package ch.metzenthin.svm.common.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.junit.jupiter.api.Test;

/**
 * @author Martin Schraner
 */
class MapUtilsTest {

  @Test
  void testSortByValue() {
    // http://stackoverflow.com/questions/109383/how-to-sort-a-mapkey-value-on-the-values-in-java
    Random random = new Random(System.currentTimeMillis());
    Map<String, Integer> testMap = new HashMap<>(1000);
    for (int i = 0; i < 1000; ++i) {
      testMap.put("SomeString" + random.nextInt(), random.nextInt());
    }

    testMap = MapUtils.sortByValue(testMap);
    assertEquals(1000, testMap.size());

    Integer previous = null;
    for (Map.Entry<String, Integer> entry : testMap.entrySet()) {
      assertNotNull(entry.getValue());
      if (previous != null) {
        assertTrue(entry.getValue() >= previous);
      }
      previous = entry.getValue();
    }
  }
}
