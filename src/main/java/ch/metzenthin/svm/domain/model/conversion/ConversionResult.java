package ch.metzenthin.svm.domain.model.conversion;

import ch.metzenthin.svm.common.datatypes.Field;

/**
 * @author Hans Stamm
 */
public record ConversionResult<T>(
    String fieldName, T convertedValue, boolean isValid, String errorMessage) {

  public ConversionResult(String fieldName, T convertedTypeValue) {
    this(fieldName, convertedTypeValue, true, null);
  }

  public ConversionResult(String fieldName, String errorMessage) {
    this(fieldName, null, false, errorMessage);
  }

  public Field getField() {
    return Field.valueOfFieldName(fieldName);
  }
}
