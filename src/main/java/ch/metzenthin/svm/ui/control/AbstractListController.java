package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.model.AbstractListModel;
import ch.metzenthin.svm.domain.model.AbstractTableData;
import ch.metzenthin.svm.domain.model.Model;
import ch.metzenthin.svm.service.result.SaveDialogResult;
import ch.metzenthin.svm.ui.view.AbstractListPanelView;
import lombok.Getter;

/**
 * @param <T> List-Model-Typ, z.B. KursortListModel
 * @param <U> Delete-Result-Typ, z.B. DeleteKursortResult
 * @author Hans Stamm
 */
public abstract class AbstractListController<
    T extends AbstractListModel<? extends AbstractTableData<?>, ?, ? extends Model, U>,
    U extends SaveDialogResult> {

  protected final SvmContext svmContext;
  protected final T model;
  @Getter public final AbstractListPanelView view;

  protected AbstractListController(SvmContext svmContext, T model, AbstractListPanelView view) {
    this.svmContext = svmContext;
    this.model = model;
    this.view = view;
    configView();
    configButtons();
  }

  private void configView() {
    view.configListeners(this::onBearbeiten, this::onListSelection);
  }

  private void onListSelection() {
    int selectedRowIndex = view.getSelectedRow();
    view.setButtonBearbeitenEnabled(selectedRowIndex >= 0);
    view.setButtonLoeschenEnabled(selectedRowIndex >= 0);
  }

  private void configButtons() {
    configBtnNeu();
    configBtnBearbeiten();
    configBtnLoeschen();
  }

  private void configBtnNeu() {
    view.addButtonNeuActionListener(e -> onNeu());
  }

  private void onNeu() {
    view.setButtonNeuFocusPainted(true);
    showOnNeuDialog();
    // Dialog wurde geschlossen
    reloadTableData();
    view.setButtonNeuFocusPainted(false);
  }

  protected abstract void showOnNeuDialog();

  private void configBtnBearbeiten() {
    view.setButtonBearbeitenEnabled(false);
    view.addButtonBearbeitenActionListener(e -> onBearbeiten());
  }

  private void onBearbeiten() {
    view.setButtonBearbeitenFocusPainted(true);
    showOnBearbeitenDialog();
    // Dialog wurde geschlossen
    reloadTableData();
    view.setButtonBearbeitenFocusPainted(false);
  }

  protected abstract void showOnBearbeitenDialog();

  private void configBtnLoeschen() {
    view.setButtonLoeschenEnabled(false);
    view.addButtonLoeschenActionListener(e -> onLoeschen());
  }

  private void onLoeschen() {
    view.setButtonLoeschenFocusPainted(true);
    onLoeschenDialog();
    view.setButtonLoeschenFocusPainted(false);
    view.setButtonLoeschenEnabled(false);
    view.clearSelection();
  }

  private void onLoeschenDialog() {
    int n =
        view.showYesNoDialog(
            "Soll der Eintrag aus der Datenbank gelöscht werden?",
            model.getListItemName() + " löschen");
    if (n == 0) {
      U saveDialogResult = model.eintragLoeschen(view.getSelectedRow());
      if (saveDialogResult.isErrorMessage()) {
        showErrorMessageDialog(saveDialogResult.getMessage());
        if (saveDialogResult.isCloseDialog()) {
          reloadTableData();
        }
      } else {
        onLoeschenErfolgreich();
      }
    }
  }

  private void showErrorMessageDialog(String message) {
    view.showErrorMessageDialog(message, "Fehler");
  }

  private void onLoeschenErfolgreich() {
    reloadTableData();
  }

  private void reloadTableData() {
    // TableData mit von der Datenbank upgedateten Objekten updaten
    model.reloadData();
    view.fireTableDataChanged();
  }
}
