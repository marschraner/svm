package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.domain.model.DialogClosingListener;
import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResultsAndSaveResult;
import ch.metzenthin.svm.ui.view.CreateOrUpdateView;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author Martin Schraner
 */
public abstract class AbstractCreateOrUpdateController<T extends CreateOrUpdateView>
    implements DialogClosingListener {

  protected final T view;

  protected AbstractCreateOrUpdateController(T view) {
    this.view = view;
    view.configDialogClosing(this);
    configBtnSpeichern();
    configBtnAbbrechen();
  }

  protected void configBtnSpeichern() {
    view.addButtonSpeichernActionListener(e -> onSpeichern());
  }

  private void onSpeichern() {
    setAllErrorLabelsInvisible();
    ValidationResultsAndSaveResult validationResultsAndSaveResult = speichern();
    if (!validationResultsAndSaveResult.isValidationSuccessful()) {
      setErrorLabelsVisible(validationResultsAndSaveResult.validationResults());
    } else if (!validationResultsAndSaveResult.isSaveSuccessful()) {
      view.showErrorMessageDialog(validationResultsAndSaveResult.getSaveErrorMessage(), "Fehler");
      if (validationResultsAndSaveResult.isDialogToBeClosedAfterSave()) {
        closeDialog();
      } else {
        view.setButtonSpeichernFocusPainted(false);
      }
    } else {
      closeDialog();
    }
  }

  protected abstract ValidationResultsAndSaveResult speichern();

  protected abstract void setErrorLabelsVisible(List<ValidationResult> validationResults);

  protected abstract void setAllErrorLabelsInvisible();

  protected void setErrorLabelVisibleIfRequired(
      ValidationResult validationResult, Field field, Consumer<String> setErrorLabelVisible) {
    if (validationResult.affectedFields().contains(field)) {
      setErrorLabelVisible.accept(validationResult.errorMessage());
    }
  }

  private void closeDialog() {
    view.closeDialog();
  }

  private void configBtnAbbrechen() {
    view.addButtonAbbrechenActionListener(e -> onAbbrechen());
  }

  private void onAbbrechen() {
    closeDialog();
  }

  @Override
  public void onCloseDialog() {
    closeDialog();
  }

  protected void showDialog() {
    view.showDialog();
  }
}
