package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.domain.model.AbstractListModel;
import ch.metzenthin.svm.domain.model.validation.ValidationResultsAndListModel;
import ch.metzenthin.svm.ui.view.AbstractSearchPanelView;

/**
 * @author Martin Schraner
 */
public abstract class AbstractSearchPanelController<
        T extends AbstractSearchPanelView<?>, U extends AbstractListModel<?, ?, ?, ?>>
    extends AbstractSubmitController<T> {

  protected AbstractSearchPanelController(T view) {
    super(view);
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

  private void configBtnAbbrechen() {
    view.addButtonAbbrechenActionListener(e -> view.abbrechen());
  }

  public T getView() {
    return view;
  }
}
