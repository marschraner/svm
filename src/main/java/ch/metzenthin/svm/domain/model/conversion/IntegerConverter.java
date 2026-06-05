package ch.metzenthin.svm.domain.model.conversion;

import ch.metzenthin.svm.common.utils.Converter;

/**
 * @author Hans Stamm
 */
public class IntegerConverter {

  public static final int VALUE_NOT_SET = Integer.MIN_VALUE;

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

  public static String toString(int value) {
    return (value == VALUE_NOT_SET) ? null : Integer.toString(value);
  }

  public static ConversionResult<Integer> convertToInt(String fieldName, String string) {
    if (string == null || string.isEmpty()) {
      return new ConversionResult<>(fieldName, VALUE_NOT_SET);
    }

    try {
      return new ConversionResult<>(fieldName, Converter.toInteger(string));
    } catch (NumberFormatException e) {
      return new ConversionResult<>(fieldName, e.getMessage());
    }
  }

  public static ConvertedValueAndConversionResult<Integer> convertToInteger(String string) {
    if (string == null || string.isEmpty()) {
      return new ConvertedValueAndConversionResult<>((Integer) null);
    }

    try {
      return new ConvertedValueAndConversionResult<>(Converter.toInteger(string));
    } catch (NumberFormatException e) {
      return new ConvertedValueAndConversionResult<>(e.getMessage());
    }
  }

  public static ConvertedValueAndConversionResult<Integer> convertToInt(String string) {
    if (string == null || string.isEmpty()) {
      return new ConvertedValueAndConversionResult<>(VALUE_NOT_SET);
    }

    try {
      return new ConvertedValueAndConversionResult<>(Converter.toInteger(string));
    } catch (NumberFormatException e) {
      return new ConvertedValueAndConversionResult<>(e.getMessage());
    }
  }
}
