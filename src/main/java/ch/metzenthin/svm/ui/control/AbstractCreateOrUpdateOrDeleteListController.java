package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.model.AbstractCreateOrUpdateOrDeleteListModel;
import ch.metzenthin.svm.service.result.SubmitDialogResult;
import ch.metzenthin.svm.ui.view.AbstractCreateOrUpdateOrDeleteListPanelView;

/**
 * @param <T> List-Model-Typ, z.B. KursortListModel
 * @param <U> List-Panel-View-Typ, z.B. KursortListView
 * @param <V> Delete-Result-Typ, z.B. DeleteKursortResult
 * @author Hans Stamm
 */
public abstract class AbstractCreateOrUpdateOrDeleteListController<
        T extends AbstractCreateOrUpdateOrDeleteListModel<?, ?, V>,
        U extends AbstractCreateOrUpdateOrDeleteListPanelView<?>,
        V extends SubmitDialogResult>
    extends AbstractListController<T, U, V> {

  protected AbstractCreateOrUpdateOrDeleteListController(SvmContext svmContext, T model, U view) {
    super(svmContext, model, view);
  }

  @Override
  protected void configView() {
    view.configListeners(this::onBearbeiten, this::onListSelection);
  }

  private void onListSelection() {
    int selectedRowIndex = view.getIndexOfSelectedRow();
    view.setButtonBearbeitenEnabled(selectedRowIndex >= 0);
    view.setButtonLoeschenEnabled(selectedRowIndex >= 0);
  }

  @Override
  protected void configButtons() {
    configBtnNeu();
    configBtnBearbeiten();
    configBtnLoeschen();
  }

  private void configBtnNeu() {
    view.addButtonNeuActionListener(e -> onNeu());
  }

  private void configBtnBearbeiten() {
    view.setButtonBearbeitenEnabled(false);
    view.addButtonBearbeitenActionListener(e -> onBearbeiten());
  }

  private void onBearbeiten() {
    view.setButtonBearbeitenFocusPainted(true);
    showOnBearbeitenDialog();
    // Dialog wurde geschlossen
    view.setButtonBearbeitenFocusPainted(false);
  }

  protected abstract void showOnBearbeitenDialog();

  protected void onLoeschenDialog() {
    int n =
        view.showYesNoDialog(
            "Soll der Eintrag aus der Datenbank gelöscht werden?",
            model.getListItemName() + " löschen");
    if (n == 0) {
      V saveDialogResult = model.eintragLoeschen(view.convertRowIndexToModel());
      if (!saveDialogResult.isSubmitSuccessful()) {
        showErrorMessageDialog(saveDialogResult.getMessage());
        if (saveDialogResult.isDialogToBeClosed()) {
          reloadSelectedRow();
        }
      } else {
        removeSelectedRow();
      }
    }
  }

  private void showErrorMessageDialog(String message) {
    view.showErrorMessageDialog(message, "Fehler");
  }

  protected void reloadSelectedRow() {
    int modelRowIndex = view.convertRowIndexToModel();
    boolean success = model.replaceUpdatedObjectInTableModel(modelRowIndex);
    if (success) {
      view.fireTableRowUpdated(modelRowIndex);
      updateButtonsAndTotalText();
    } else {
      view.showErrorMessageDialog(
          "Beim Laden des geänderten Eintrags ist ein Fehler aufgetreten.\n"
              + "Die Liste wird neu geladen!",
          "Fehler beim Laden des geänderten Eintrags");
      reloadTableData();
    }
  }
}
