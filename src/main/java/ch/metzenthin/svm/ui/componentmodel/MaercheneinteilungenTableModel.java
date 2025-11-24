package ch.metzenthin.svm.ui.componentmodel;

import ch.metzenthin.svm.domain.model.MaercheneinteilungenTableData;
import ch.metzenthin.svm.persistence.entities.Maercheneinteilung;
import java.io.Serial;
import javax.swing.table.AbstractTableModel;
import lombok.Getter;

@Getter
public class MaercheneinteilungenTableModel extends AbstractTableModel {

  @Serial private static final long serialVersionUID = 1L;

  private final transient MaercheneinteilungenTableData maercheneinteilungenTableData;

  public MaercheneinteilungenTableModel(
      MaercheneinteilungenTableData maercheneinteilungenTableData) {
    super();
    this.maercheneinteilungenTableData = maercheneinteilungenTableData;
  }

  @Override
  public int getRowCount() {
    return maercheneinteilungenTableData.size();
  }

  @Override
  public int getColumnCount() {
    return maercheneinteilungenTableData.getColumnCount();
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    return maercheneinteilungenTableData.getValueAt(rowIndex, columnIndex);
  }

  @Override
  public String getColumnName(int column) {
    return maercheneinteilungenTableData.getColumnName(column);
  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    return maercheneinteilungenTableData.getColumnClass();
  }

  public Maercheneinteilung getMaercheneinteilungSelected(int rowIndex) {
    return maercheneinteilungenTableData.getMaercheneinteilungSelected(rowIndex);
  }
}
