package ch.metzenthin.svm.ui.componentmodel;

import ch.metzenthin.svm.domain.model.CodesTableData;
import ch.metzenthin.svm.persistence.entities.Code;
import java.io.Serial;
import javax.swing.table.AbstractTableModel;

public class CodesTableModel extends AbstractTableModel {

  @Serial private static final long serialVersionUID = 1L;

  private final transient CodesTableData codesTableData;

  public CodesTableModel(CodesTableData codesTableData) {
    super();
    this.codesTableData = codesTableData;
  }

  @Override
  public int getRowCount() {
    return codesTableData.size();
  }

  @Override
  public int getColumnCount() {
    return codesTableData.getColumnCount();
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    return codesTableData.getValueAt(rowIndex, columnIndex);
  }

  @Override
  public String getColumnName(int column) {
    return codesTableData.getColumnName(column);
  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    return codesTableData.getColumnClass();
  }

  public CodesTableData getCodesTableData() {
    return codesTableData;
  }

  public Code getCodeAt(int rowIndex) {
    return codesTableData.getCodeAt(rowIndex);
  }
}
