package ch.metzenthin.svm.common.utils;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Martin Schraner
 */
public class MapUtils {

  private MapUtils() {}

  // Source:
  // http://stackoverflow.com/questions/109383/how-to-sort-a-mapkey-value-on-the-values-in-java
  public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
    List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
    list.sort(Map.Entry.comparingByValue());

    Map<K, V> result = new LinkedHashMap<>();
    for (Map.Entry<K, V> entry : list) {
      result.put(entry.getKey(), entry.getValue());
    }
    return result;
  }
}
