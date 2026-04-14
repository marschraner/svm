package ch.metzenthin.svm.domain.model.validation;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.service.result.SaveDialogResult;

/**
 * @author Martin Schraner
 */
public record ValidationAndSaveResult(
    ValidationResult validationResult, SaveDialogResult saveResult) {

  public ValidationAndSaveResult(ValidationResult validationResult) {
    this(validationResult, null);
  }

  public boolean isValidationSuccessful() {
    return validationResult.isValid();
  }

  public String getValidationErrorMessage() {
    return validationResult.errorMessage();
  }

  public boolean affectedFieldsOfValidationContains(Field field) {
    return validationResult.affectedFieldsContains(field);
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
