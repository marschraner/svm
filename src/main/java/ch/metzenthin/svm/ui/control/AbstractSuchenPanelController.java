package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.domain.model.AbstractListModel;
import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResultsAndListModel;
import ch.metzenthin.svm.ui.view.SuchenPanelView;
import java.util.List;
import lombok.Getter;

/**
 * @author Martin Schraner
 */
public abstract class AbstractSuchenPanelController<
    T extends SuchenPanelView, U extends AbstractListModel<?, ?, ?, ?>> {

  @Getter protected final T view;

  protected AbstractSuchenPanelController(T view) {
    this.view = view;
    configBtnSuchen();
    configBtnAbbrechen();
  }

  protected void configBtnSuchen() {
    view.addButtonSubmitActionListener(e -> onSuchen());
  }

  private void onSuchen() {
    setAllErrorLabelsInvisible();
    ValidationResultsAndListModel<U> validationResultsAndListModel = suchen();
    if (!validationResultsAndListModel.isValidationSuccessful()) {
      setErrorLabelsVisible(validationResultsAndListModel.validationResults());
      showErrorMessageDialog(validationResultsAndListModel.validationResults());
    } else {
      showNextPanel(validationResultsAndListModel.listModel());
    }
  }

  protected abstract ValidationResultsAndListModel<U> suchen();

  protected abstract void showNextPanel(U abstractListModel);

  private void setErrorLabelsVisible(List<ValidationResult> validationResults) {
    for (ValidationResult validationResult : validationResults) {
      if (!validationResult.isValid() && validationResult.affectedFields() != null) {
        for (Field field : validationResult.affectedFields()) {
          setErrorLabelVisible(validationResult, field);
        }
      }
    }
  }

  protected abstract void setErrorLabelVisible(ValidationResult validationResult, Field field);

  protected abstract void setAllErrorLabelsInvisible();

  private void showErrorMessageDialog(List<ValidationResult> validationResults) {
    for (ValidationResult validationResult : validationResults) {
      if (!validationResult.isValid()
          && (validationResult.affectedFields() == null
              || validationResult.affectedFields().isEmpty())) {
        view.showErrorMessageDialog(validationResult.errorMessage(), "Fehler");
      }
    }
  }

  private void configBtnAbbrechen() {
    view.addButtonAbbrechenActionListener(e -> view.abbrechen());
  }
}
