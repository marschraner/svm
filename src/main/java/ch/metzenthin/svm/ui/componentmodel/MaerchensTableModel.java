package ch.metzenthin.svm.ui.componentmodel;

import ch.metzenthin.svm.domain.model.MaerchensTableData;
import java.io.Serial;
import javax.swing.table.AbstractTableModel;
import lombok.Getter;

@Getter
public class MaerchensTableModel extends AbstractTableModel {

  @Serial private static final long serialVersionUID = 1L;

  private final transient MaerchensTableData maerchensTableData;

  public MaerchensTableModel(MaerchensTableData maerchensTableData) {
    super();
    this.maerchensTableData = maerchensTableData;
  }

  @Override
  public int getRowCount() {
    return maerchensTableData.size();
  }

  @Override
  public int getColumnCount() {
    return maerchensTableData.getColumnCount();
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    return maerchensTableData.getValueAt(rowIndex, columnIndex);
  }

  @Override
  public String getColumnName(int column) {
    return maerchensTableData.getColumnName(column);
  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    return maerchensTableData.getColumnClass(columnIndex);
  }
}
