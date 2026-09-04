package ch.metzenthin.svm.domain.model.validation;

import ch.metzenthin.svm.domain.model.AbstractListModel;
import java.util.List;

/**
 * @author Martin Schraner
 */
public record ValidationResultsAndListModel<T extends AbstractListModel<?, ?, ?, ?>>(
    List<ValidationResult> validationResults, T listModel) {

  public ValidationResultsAndListModel(T listModel) {
    this(List.of(), listModel);
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  public boolean isValidationSuccessful() {
    return ValidationResult.allValidationResultsValid(validationResults);
  }
}
