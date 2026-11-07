package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.domain.model.AbstractAddOrRemoveCodeAssignmentListModel;
import ch.metzenthin.svm.domain.model.CodeSelectionModel;
import ch.metzenthin.svm.domain.model.CodeTableData;
import ch.metzenthin.svm.listener.SchuelerCodesZurueckListener;
import ch.metzenthin.svm.persistence.entities.Code;
import ch.metzenthin.svm.persistence.entities.SchuelerCode;
import ch.metzenthin.svm.ui.componentmodel.TableModel;
import ch.metzenthin.svm.ui.view.AddOrRemoveCodeAssignmentListPanelView;
import ch.metzenthin.svm.ui.view.CodeSelectionView;
import lombok.Getter;

/**
 * @author Hans Stamm
 */
public class AddOrRemoveSchuelerCodeAssignmentListController {

  protected final AbstractAddOrRemoveCodeAssignmentListModel<SchuelerCode> model;
  private final SchuelerCodesZurueckListener schuelerCodesZurueckListener;
  @Getter public final AddOrRemoveCodeAssignmentListPanelView view;

  protected AddOrRemoveSchuelerCodeAssignmentListController(
      AbstractAddOrRemoveCodeAssignmentListModel<SchuelerCode> model,
      String title,
      SchuelerCodesZurueckListener schuelerCodesZurueckListener) {
    this.model = model;
    this.schuelerCodesZurueckListener = schuelerCodesZurueckListener;
    this.view = createView(model.getTableModel(), title);
    configView();
    configButtons();
  }

  private static <T extends Code> AddOrRemoveCodeAssignmentListPanelView createView(
      TableModel<CodeTableData<T>, T> tableModel, String title) {
    return new AddOrRemoveCodeAssignmentListPanelView(tableModel, title);
  }

  private void configView() {
    view.configListeners(() -> {}, this::onListSelection);
  }

  private void onListSelection() {
    int selectedRowIndex = view.getIndexOfSelectedRow();
    view.setButtonLoeschenEnabled(selectedRowIndex >= 0);
  }

  private void configButtons() {
    configBtnNeu();
    configBtnLoeschen();
    configBtnZurueck();
  }

  private void configBtnNeu() {
    view.setButtonNeuFocusPainted(false);
    view.addButtonNeuActionListener(e -> showOnNeuDialog());
  }

  protected void showOnNeuDialog() {
    CodeSelectionModel<SchuelerCode> codeSelectionModel = model.createCodeSelectionModel();
    CodeSelectionController<SchuelerCode> codeSelectionController =
        new CodeSelectionController<>(
            codeSelectionModel,
            new CodeSelectionView<>("Schueler-Code hinzufügen"),
            model.getAssignedCodesAsSortedList());
    codeSelectionController.showDialog();
    if (codeSelectionModel.getSelectedCode() != null) {
      model.addCode(codeSelectionModel.getSelectedCode());
      view.fireTableDataChanged();
    }
  }

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
            String.format("Soll der %s entfernt werden?", model.getListItemName()),
            model.getListItemName() + " entfernen");
    if (n == 0) {
      SchuelerCode codeToBeRemoved = model.getSelectedRow(view.convertRowIndexToModel());
      model.removeCode(codeToBeRemoved);
      view.fireTableDataChanged();
    }
  }

  private void configBtnZurueck() {
    view.addButtonZurueckActionListener(e -> onZurueck());
  }

  private void onZurueck() {
    schuelerCodesZurueckListener.onSchuelerCodesZurueck();
  }
}
