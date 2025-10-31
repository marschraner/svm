package ch.metzenthin.svm.ui.componentmodel;

import ch.metzenthin.svm.domain.model.SemestersTableData;
import java.io.Serial;
import javax.swing.table.AbstractTableModel;

public class SemestersTableModel extends AbstractTableModel {

  @Serial private static final long serialVersionUID = 1L;

  private final transient SemestersTableData semestersTableData;

  public SemestersTableModel(SemestersTableData semestersTableData) {
    super();
    this.semestersTableData = semestersTableData;
  }

  @Override
  public int getRowCount() {
    return semestersTableData.size();
  }

  @Override
  public int getColumnCount() {
    return semestersTableData.getColumnCount();
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    return semestersTableData.getValueAt(rowIndex, columnIndex);
  }

  @Override
  public String getColumnName(int column) {
    return semestersTableData.getColumnName(column);
  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    return semestersTableData.getColumnClass(columnIndex);
  }

  public SemestersTableData getSemestersTableData() {
    return semestersTableData;
  }
}
