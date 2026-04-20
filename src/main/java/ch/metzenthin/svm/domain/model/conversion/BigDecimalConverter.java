package ch.metzenthin.svm.domain.model.conversion;

import ch.metzenthin.svm.common.utils.Converter;
import java.math.BigDecimal;

/**
 * @author Hans Stamm
 */
public class BigDecimalConverter {

  private BigDecimalConverter() {
    /* This utility class should not be instantiated */
  }

  public static String toString(BigDecimal value) {
    return Converter.asStringNullSafe(value);
  }

  public static ConversionResult<BigDecimal> convertToBigDecimal(String fieldName, String string) {
    if (string == null || string.isEmpty()) {
      return new ConversionResult<>(fieldName, (BigDecimal) null);
    }

    return new ConversionResult<>(fieldName, new BigDecimal(string));
  }
}
