package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.persistence.entities.*;
import ch.metzenthin.svm.ui.componentmodel.TableModel;
import java.util.Collections;
import java.util.List;
import lombok.Getter;

/**
 * @param <T> Code-Typ, z.B. MitarbeiterCode
 * @author Hans Stamm
 */
public abstract class AbstractAddOrRemoveCodeAssignmentListModel<T extends Code> {

  @Getter private final TableModel<CodeTableData<T>, T> tableModel;
  private final List<T> assignedCodes;

  protected AbstractAddOrRemoveCodeAssignmentListModel(
      TableModel<CodeTableData<T>, T> tableModel, List<T> assignedCodes) {
    this.tableModel = tableModel;
    this.assignedCodes = assignedCodes;
  }

  public void addCode(T code) {
    assignedCodes.add(code);
    reloadData();
  }

  public void removeCode(T code) {
    assignedCodes.remove(code);
    reloadData();
  }

  public List<T> getAssignedCodesAsSortedList() {
    Collections.sort(assignedCodes);
    return assignedCodes;
  }

  public T getSelectedRow(int selectedIndex) {
    return tableModel.getRowAt(selectedIndex);
  }

  public void reloadData() {
    tableModel.setData(getAssignedCodesAsSortedList());
  }

  public abstract CodeSelectionModel<T> createCodeSelectionModel();

  public abstract String getListItemName();
}
