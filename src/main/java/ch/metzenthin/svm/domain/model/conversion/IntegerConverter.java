package ch.metzenthin.svm.domain.model.conversion;

import ch.metzenthin.svm.common.utils.Converter;

/**
 * @author Hans Stamm
 */
public class IntegerConverter {

  private IntegerConverter() {
    /* This utility class should not be instantiated */
  }

  public static String toString(Integer value) {
    return Converter.asStringNullSafe(value);
  }

  public static ConversionResult<Integer> convertToInteger(String fieldName, String string) {
    if (string == null || string.isEmpty()) {
      return new ConversionResult<>(fieldName, (Integer) null);
    }

    try {
      return new ConversionResult<>(fieldName, Converter.toInteger(string));
    } catch (NumberFormatException e) {
      return new ConversionResult<>(fieldName, e.getMessage());
    }
  }
}
