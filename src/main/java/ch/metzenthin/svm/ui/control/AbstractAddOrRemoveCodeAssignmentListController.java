package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.model.AbstractAddOrRemoveCodeAssignmentListModel;
import ch.metzenthin.svm.domain.model.CodeTableData;
import ch.metzenthin.svm.domain.model.DialogClosingListener;
import ch.metzenthin.svm.persistence.entities.Code;
import ch.metzenthin.svm.ui.componentmodel.TableModel;
import ch.metzenthin.svm.ui.view.CodeListDialogView;
import lombok.Getter;

/**
 * @param <T> Code-Typ, z.B. MitarbeiterCode
 * @author Hans Stamm
 */
public abstract class AbstractAddOrRemoveCodeAssignmentListController<T extends Code>
    implements DialogClosingListener {

  protected final SvmContext svmContext;
  protected final AbstractAddOrRemoveCodeAssignmentListModel<T> model;
  @Getter public final CodeListDialogView view;

  protected AbstractAddOrRemoveCodeAssignmentListController(
      SvmContext svmContext,
      AbstractAddOrRemoveCodeAssignmentListModel<T> model,
      String dialogTitle) {
    this.svmContext = svmContext;
    this.model = model;
    this.view = createView(model.getTableModel(), dialogTitle);
    view.configDialogClosing(this);
    configView();
    configButtons();
  }

  private static <T extends Code> CodeListDialogView createView(
      TableModel<CodeTableData<T>, T> tableModel, String dialogTitle) {
    return new CodeListDialogView(tableModel, dialogTitle);
  }

  private void configView() {
    view.configListeners(() -> {}, this::onListSelection);
  }

  private void onListSelection() {
    int selectedRowIndex = view.getSelectedRow();
    view.setButtonLoeschenEnabled(selectedRowIndex >= 0);
  }

  private void configButtons() {
    configBtnNeu();
    configBtnLoeschen();
  }

  private void configBtnNeu() {
    view.setButtonNeuFocusPainted(false);
    view.addButtonNeuActionListener(e -> showOnNeuDialog());
  }

  protected abstract void showOnNeuDialog();

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
            String.format("%s entfernen", model.getListItemName()));
    if (n == 0) {
      T codeToBeRemoved = model.getSelectedRow(view.convertRowIndexToModel());
      model.removeCode(codeToBeRemoved);
      view.fireTableDataChanged();
    }
  }

  public void showDialog() {
    view.showDialog();
  }

  @Override
  public void onCloseDialog() {
    view.closeDialog();
  }
}
