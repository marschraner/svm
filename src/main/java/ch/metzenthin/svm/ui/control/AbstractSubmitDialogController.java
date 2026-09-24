package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.domain.model.DialogClosingListener;
import ch.metzenthin.svm.domain.model.validation.ValidationResultsAndSubmitResult;
import ch.metzenthin.svm.ui.view.AbstractSubmitDialogView;

/**
 * @author Martin Schraner
 */
public abstract class AbstractSubmitDialogController<T extends AbstractSubmitDialogView<?>>
    extends AbstractSubmitController<T> implements DialogClosingListener {

  protected AbstractSubmitDialogController(T view) {
    super(view);
    view.configDialogClosing(this);
    configBtnSubmit();
    configBtnAbbrechen();
  }

  protected void configBtnSubmit() {
    view.addButtonSubmitActionListener(e -> onSubmit());
  }

  private void onSubmit() {
    setAllErrorLabelsInvisible();
    ValidationResultsAndSubmitResult validationResultsAndSubmitResult = submit();
    if (!validationResultsAndSubmitResult.isValidationSuccessful()) {
      setErrorLabelsVisible(validationResultsAndSubmitResult.validationResults());
      showErrorMessageDialog(validationResultsAndSubmitResult.validationResults());
    } else if (!validationResultsAndSubmitResult.isSubmitSuccessful()) {
      view.showErrorMessageDialog(validationResultsAndSubmitResult.getErrorMessage(), "Fehler");
      if (validationResultsAndSubmitResult.isDialogToBeClosedAfterSubmit()) {
        closeDialog();
      } else {
        view.setButtonSubmitFocusPainted(false);
      }
    } else {
      if (validationResultsAndSubmitResult.isDialogToBeClosedAfterSubmit()) {
        closeDialog();
      }
    }
  }

  protected abstract ValidationResultsAndSubmitResult submit();

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
