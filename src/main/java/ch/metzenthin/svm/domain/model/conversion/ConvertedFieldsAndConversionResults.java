package ch.metzenthin.svm.domain.model.conversion;

import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import java.util.List;
import java.util.Set;

/**
 * @author Hans Stamm
 */
public record ConvertedFieldsAndConversionResults<T>(
    T convertedFields, List<ConversionResult<?>> conversionErrors) {

  public boolean isValid() {
    return conversionErrors.isEmpty();
  }

  public List<ValidationResult> getInvalidConversionResultsAsValidationResults() {
    return conversionErrors.stream()
        .map(
            conversionResult ->
                new ValidationResult(
                    conversionResult.errorMessage(), Set.of(conversionResult.getField())))
        .toList();
  }
}
