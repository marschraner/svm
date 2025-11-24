package ch.metzenthin.svm.ui.componentmodel;

import ch.metzenthin.svm.domain.model.DispensationenTableData;
import java.io.Serial;
import javax.swing.table.AbstractTableModel;
import lombok.Getter;

@Getter
public class DispensationenTableModel extends AbstractTableModel {

  @Serial private static final long serialVersionUID = 1L;

  private final transient DispensationenTableData dispensationenTableData;

  public DispensationenTableModel(DispensationenTableData dispensationenTableData) {
    super();
    this.dispensationenTableData = dispensationenTableData;
  }

  @Override
  public int getRowCount() {
    return dispensationenTableData.size();
  }

  @Override
  public int getColumnCount() {
    return dispensationenTableData.getColumnCount();
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    return dispensationenTableData.getValueAt(rowIndex, columnIndex);
  }

  @Override
  public String getColumnName(int column) {
    return dispensationenTableData.getColumnName(column);
  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    return dispensationenTableData.getColumnClass();
  }
}
