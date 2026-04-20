package ch.metzenthin.svm.domain.model.conversion;

import ch.metzenthin.svm.common.utils.Converter;

/**
 * @author Hans Stamm
 */
public class BooleanConverter {

  private BooleanConverter() {
    /* This utility class should not be instantiated */
  }

  public static String toString(Boolean value) {
    return Converter.asStringNullSafe(value);
  }

  public static ConversionResult<Boolean> convertToBoolean(String fieldName, String string) {
    if (string == null || string.isEmpty()) {
      return null;
    }

    return new ConversionResult<>(fieldName, Boolean.parseBoolean(string));
  }
}
