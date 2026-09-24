package ch.metzenthin.svm.ui.componentmodel;

import ch.metzenthin.svm.domain.model.AbstractTableData;
import java.io.Serial;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.table.AbstractTableModel;
import lombok.Getter;

/**
 * @param <T> Table-Data-Typ, z.B. KursortTableData
 * @param <U> Typ der Table-Data-Rows, z.B. Kursort
 * @author Hans Stamm
 */
public class TableModel<T extends AbstractTableData<U>, U> extends AbstractTableModel {

  @Serial private static final long serialVersionUID = 1L;

  private final transient T tableData;
  @Getter private final boolean tableColumnWidthAccordingToCellContentAndHeader;
  @Getter private final double[] columnWidthsPercentages;

  public TableModel(T tableData, double... columnWidthsPercentages) {
    this(tableData, false, columnWidthsPercentages);
  }

  public TableModel(T tableData, boolean tableColumnWidthAccordingToCellContentAndHeader) {
    this(tableData, tableColumnWidthAccordingToCellContentAndHeader, (double[]) null);
  }

  private TableModel(
      T tableData,
      boolean tableColumnWidthAccordingToCellContentAndHeader,
      double... columnWidthsPercentages) {
    super();
    this.tableData = tableData;
    this.tableColumnWidthAccordingToCellContentAndHeader =
        tableColumnWidthAccordingToCellContentAndHeader;
    this.columnWidthsPercentages = columnWidthsPercentages;
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

  public String getTotalText() {
    return "Total: " + getRowCount();
  }

  public void forEachRow(Consumer<U> action) {
    tableData.forEachRow(action);
  }

  public Iterator<U> getRowIterator() {
    return tableData.getIterator();
  }
}
