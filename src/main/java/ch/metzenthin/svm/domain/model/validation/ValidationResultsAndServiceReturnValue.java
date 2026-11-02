package ch.metzenthin.svm.domain.model.validation;

import java.util.List;

/**
 * @author Hans Stamm
 */
public record ValidationResultsAndServiceReturnValue<T>(
    List<ValidationResult> validationResults, T serviceReturnValue) {

  public ValidationResultsAndServiceReturnValue(List<ValidationResult> validationResults) {
    this(validationResults, null);
  }

  public boolean isValidationSuccessful() {
    return ValidationResult.allValidationResultsValid(validationResults);
  }
}
