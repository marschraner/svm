package ch.metzenthin.svm.common.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Martin Schraner
 */
public class MapUtilsTest {

  @Test
  public void testSortByValue() {
    // http://stackoverflow.com/questions/109383/how-to-sort-a-mapkey-value-on-the-values-in-java
    Random random = new Random(System.currentTimeMillis());
    Map<String, Integer> testMap = new HashMap<>(1000);
    for (int i = 0; i < 1000; ++i) {
      testMap.put("SomeString" + random.nextInt(), random.nextInt());
    }

    testMap = MapUtils.sortByValue(testMap);
    Assert.assertEquals(1000, testMap.size());

    Integer previous = null;
    for (Map.Entry<String, Integer> entry : testMap.entrySet()) {
      Assert.assertNotNull(entry.getValue());
      if (previous != null) {
        Assert.assertTrue(entry.getValue() >= previous);
      }
      previous = entry.getValue();
    }
  }
}
