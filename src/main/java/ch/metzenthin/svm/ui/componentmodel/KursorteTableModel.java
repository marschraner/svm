package ch.metzenthin.svm.ui.componentmodel;

import ch.metzenthin.svm.domain.model.KursorteTableData;
import java.io.Serial;
import javax.swing.table.AbstractTableModel;
import lombok.Getter;

@Getter
public class KursorteTableModel extends AbstractTableModel {

  @Serial private static final long serialVersionUID = 1L;

  private final transient KursorteTableData kursorteTableData;

  public KursorteTableModel(KursorteTableData kursorteTableData) {
    super();
    this.kursorteTableData = kursorteTableData;
  }

  @Override
  public int getRowCount() {
    return kursorteTableData.size();
  }

  @Override
  public int getColumnCount() {
    return kursorteTableData.getColumnCount();
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    return kursorteTableData.getValueAt(rowIndex, columnIndex);
  }

  @Override
  public String getColumnName(int column) {
    return kursorteTableData.getColumnName(column);
  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    return kursorteTableData.getColumnClass();
  }
}
