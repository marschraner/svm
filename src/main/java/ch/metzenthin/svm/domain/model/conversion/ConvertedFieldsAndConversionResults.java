package ch.metzenthin.svm.domain.model.conversion;

import ch.metzenthin.svm.common.datatypes.Field;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Hans Stamm
 */
public record ConvertedFieldsAndConversionResults<T>(
    T convertedFields, List<ConversionResult<?>> conversionErrors) {

  public boolean isValid() {
    return conversionErrors.isEmpty();
  }

  public Set<Field> getFieldsWithInvalidConversion() {
    return conversionErrors.stream().map(ConversionResult::getField).collect(Collectors.toSet());
  }
}
