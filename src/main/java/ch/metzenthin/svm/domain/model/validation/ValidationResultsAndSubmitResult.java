package ch.metzenthin.svm.domain.model.validation;

import ch.metzenthin.svm.service.result.SubmitDialogResult;
import java.util.List;

/**
 * @author Martin Schraner
 */
public record ValidationResultsAndSubmitResult(
    List<ValidationResult> validationResults, SubmitDialogResult submitResult) {

  public ValidationResultsAndSubmitResult(List<ValidationResult> validationResults) {
    this(validationResults, null);
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  public boolean isValidationSuccessful() {
    return ValidationResult.allValidationResultsValid(validationResults);
  }

  public boolean isSubmitSuccessful() {
    return submitResult != null && submitResult.isSubmitSuccessful();
  }

  public String getErrorMessage() {
    return submitResult != null ? submitResult.getMessage() : null;
  }

  public boolean isDialogToBeClosedAfterSubmit() {
    return submitResult != null && submitResult.isDialogToBeClosed();
  }
}
