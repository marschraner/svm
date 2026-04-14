package ch.metzenthin.svm.domain.model.validation;

import ch.metzenthin.svm.service.result.SaveDialogResult;
import java.util.List;

/**
 * @author Martin Schraner
 */
public record ValidationResultsAndSaveResult(
    List<ValidationResult> validationResults, SaveDialogResult saveResult) {

  public ValidationResultsAndSaveResult(List<ValidationResult> validationResults) {
    this(validationResults, null);
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  public boolean isValidationSuccessful() {
    return ValidationResult.allValidationResultsValid(validationResults);
  }

  public boolean isSaveSuccessful() {
    return saveResult != null && saveResult.isSaveSuccessful();
  }

  public String getSaveErrorMessage() {
    return saveResult != null ? saveResult.getMessage() : null;
  }

  public boolean isDialogToBeClosedAfterSave() {
    return saveResult != null && saveResult.isDialogToBeClosed();
  }
}
