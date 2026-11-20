package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.model.AbstractListModel;
import ch.metzenthin.svm.service.result.SubmitDialogResult;
import ch.metzenthin.svm.ui.view.AbstractCreateOrDeleteListPanelView;
import java.util.Optional;
import lombok.Getter;

/**
 * @param <T> List-Model-Typ, z.B. KursortListModel
 * @param <U> List-Panel-View-Typ, z.B. KursortListView
 * @param <V> Delete-Result-Typ, z.B. DeleteKursortResult
 * @author Hans Stamm
 */
public abstract class AbstractListController<
    T extends AbstractListModel<?, ?, V>,
    U extends AbstractCreateOrDeleteListPanelView<?>,
    V extends SubmitDialogResult> {

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

  protected abstract void configView();

  protected abstract void configButtons();

  protected void onNeu() {
    view.setButtonNeuFocusPainted(true);
    showOnNeuDialog();
    // Dialog wurde geschlossen
    view.setButtonNeuFocusPainted(false);
  }

  protected abstract void showOnNeuDialog();

  protected void configBtnLoeschen() {
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

  protected abstract void onLoeschenDialog();

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

  protected void removeSelectedRow() {
    int modelRowIndex = view.convertRowIndexToModel();
    model.removeDeletedObjectFromTableModel(modelRowIndex);
    view.fireTableRowDeleted(modelRowIndex);
    updateButtonsAndTotalText();
  }

  protected void updateButtonsAndTotalText() {}
}
