package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.model.AbstractListModel;
import ch.metzenthin.svm.domain.model.AbstractTableData;
import ch.metzenthin.svm.service.result.SubmitDialogResult;
import ch.metzenthin.svm.ui.componentmodel.TableModel;
import ch.metzenthin.svm.ui.view.AbstractListPanelView;
import java.util.Optional;
import lombok.Getter;

/**
 * @param <T> List-Model-Typ, z.B. KursortListModel
 * @param <U> List-Panel-View-Typ, z.B. KursortListView
 * @param <V> Table-Data-Typ, z.B. KursortTableData
 * @param <W> Typ der Table-Data-Rows, z.B. Kursort
 * @param <X> Delete-Result-Typ, z.B. DeleteKursortResult
 * @author Hans Stamm
 */
public abstract class AbstractListController<
    T extends AbstractListModel<V, W, ? extends TableModel<V, W>, ?, X>,
    U extends AbstractListPanelView<?>,
    V extends AbstractTableData<W>,
    W,
    X extends SubmitDialogResult> {

  protected final SvmContext svmContext;
  protected final T model;
  @Getter public final U view;

  protected AbstractListController(SvmContext svmContext, T model, U view) {
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
    int selectedRowIndex = view.getIndexOfSelectedRow();
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

  protected void onLoeschenDialog() {
    int n =
        view.showYesNoDialog(
            "Soll der Eintrag aus der Datenbank gelöscht werden?",
            model.getListItemName() + " löschen");
    if (n == 0) {
      X saveDialogResult = model.eintragLoeschen(view.convertRowIndexToModel());
      if (!saveDialogResult.isSubmitSuccessful()) {
        showErrorMessageDialog(saveDialogResult.getMessage());
        if (saveDialogResult.isDialogToBeClosed()) {
          reloadTableData();
        }
      } else {
        removeSelectedRow();
      }
    }
  }

  private void showErrorMessageDialog(String message) {
    view.showErrorMessageDialog(message, "Fehler");
  }

  protected void reloadTableData() {
    // TableData mit von der Datenbank upgedateten Objekten updaten
    model.reloadData();
    view.fireTableDataChanged();
    updateButtonsAndTotalText();
  }

  /**
   * @param idOfCreatedEntity ID der Entity, die erstellt wurde und eingefügt werden soll
   */
  protected void insertCreatedObjectToList(int idOfCreatedEntity) {
    Optional<Integer> rowIndexOfInsertedEntityOptional =
        model.insertCreatedObjectToTableModel(idOfCreatedEntity);
    if (rowIndexOfInsertedEntityOptional.isPresent()) {
      view.fireTableRowInserted(rowIndexOfInsertedEntityOptional.get());
      updateButtonsAndTotalText();
    } else {
      view.showErrorMessageDialog(
          "Beim Laden des erstellten Eintrags ist ein Fehler aufgetreten.\n"
              + "Die Liste wird neu geladen!",
          "Fehler beim Laden des neu erstellten Eintrags");
      reloadTableData();
    }
  }

  protected void reloadSelectedRow() {
    int modelRowIndex = view.convertRowIndexToModel();
    boolean success = model.replaceUpdatedObjectInTableModel(modelRowIndex);
    if (success) {
      view.fireTableDataChanged();
      updateButtonsAndTotalText();
    } else {
      view.showErrorMessageDialog(
          "Beim Laden des geänderten Eintrags ist ein Fehler aufgetreten.\n"
              + "Die Liste wird neu geladen!",
          "Fehler beim Laden des geänderten Eintrags");
      reloadTableData();
    }
  }

  protected void removeSelectedRow() {
    int modelRowIndex = view.convertRowIndexToModel();
    model.removeDeletedObjectFromTableModel(modelRowIndex);
    view.fireTableDataChanged();
    updateButtonsAndTotalText();
  }

  protected void updateButtonsAndTotalText() {}
}
