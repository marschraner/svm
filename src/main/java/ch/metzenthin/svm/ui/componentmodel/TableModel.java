package ch.metzenthin.svm.ui.componentmodel;

import ch.metzenthin.svm.domain.model.AbstractTableData;
import java.io.Serial;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * @param <T> Table-Data-Typ, z.B. KursortTableData
 * @param <U> Typ der Table-Data-Rows, z.B. Kursort
 * @author Hans Stamm
 */
public class TableModel<T extends AbstractTableData<U>, U> extends AbstractTableModel {

  @Serial private static final long serialVersionUID = 1L;

  private final transient T tableData;

  public TableModel(T tableData) {
    super();
    this.tableData = tableData;
  }

  @Override
  public int getRowCount() {
    return tableData.size();
  }

  @Override
  public int getColumnCount() {
    return tableData.getColumnCount();
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    return tableData.getValueAt(rowIndex, columnIndex);
  }

  @Override
  public String getColumnName(int column) {
    return tableData.getColumnName(column);
  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    return tableData.getColumnClass(columnIndex);
  }

  public U getRowAt(int rowIndex) {
    return tableData.getRowAt(rowIndex);
  }

  public void setData(List<U> data) {
    tableData.setData(data);
  }
}
