package ch.metzenthin.svm.domain.model.validation;

import ch.metzenthin.svm.common.datatypes.Field;
import java.util.List;
import java.util.Set;

/**
 * @author Martin Schraner
 */
public record ValidationResult(boolean isValid, String errorMessage, Set<Field> affectedFields) {

  public ValidationResult() {
    this(true, null, null);
  }

  public ValidationResult(String errorMessage, Set<Field> affectedFields) {
    this(false, errorMessage, affectedFields);
  }

  public static boolean allValidationResultsValid(List<ValidationResult> validationResults) {
    return validationResults.stream().allMatch(ValidationResult::isValid);
  }
}
