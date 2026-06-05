package ch.metzenthin.svm.domain.model.conversion;

/**
 * @author Hans Stamm
 */
public record ConvertedValueAndConversionResult<T>(
    T convertedValue, boolean isValid, String errorMessage) {

  public ConvertedValueAndConversionResult(T convertedValue) {
    this(convertedValue, true, null);
  }

  public ConvertedValueAndConversionResult(String errorMessage) {
    this(null, false, errorMessage);
  }
}
