package ch.metzenthin.svm.ui.componentmodel;

import ch.metzenthin.svm.domain.model.MitarbeiterAndMitarbeiterCodesAsStringAndSelektiert;
import ch.metzenthin.svm.domain.model.MitarbeiterTableData;

/**
 * @author Hans Stamm
 */
public class MitarbeiterTableModel
    extends TableModel<MitarbeiterTableData, MitarbeiterAndMitarbeiterCodesAsStringAndSelektiert> {

  public MitarbeiterTableModel(MitarbeiterTableData tableData) {
    super(tableData, true);
  }

  @Override
  public void setValueAt(Object value, int rowIndex, int columnIndex) {
    tableData.setValueAt(value, rowIndex, columnIndex);
    fireTableCellUpdated(rowIndex, columnIndex);
  }

  @Override
  public boolean isCellEditable(int rowIndex, int columnIndex) {
    return tableData.isCellEditable(columnIndex);
  }

  public void alleMitarbeiterSelektieren() {
    tableData.alleMitarbeiterSelektieren();
    fireTableDataChanged();
  }

  public void alleMitarbeiterDeselektieren() {
    tableData.alleMitarbeiterDeselektieren();
    fireTableDataChanged();
  }

  public int getAnzahlSelektiert() {
    return tableData.getAnzahlSelektiert();
  }
}
