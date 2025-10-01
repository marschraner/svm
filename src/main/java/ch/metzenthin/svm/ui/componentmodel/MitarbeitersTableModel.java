package ch.metzenthin.svm.ui.componentmodel;

import ch.metzenthin.svm.domain.model.MitarbeitersTableData;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import java.io.Serial;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import lombok.Getter;

@Getter
public class MitarbeitersTableModel extends AbstractTableModel {

  @Serial private static final long serialVersionUID = 1L;

  private final transient MitarbeitersTableData mitarbeitersTableData;

  public MitarbeitersTableModel(MitarbeitersTableData mitarbeitersTableData) {
    super();
    this.mitarbeitersTableData = mitarbeitersTableData;
  }

  @Override
  public int getRowCount() {
    return mitarbeitersTableData.size();
  }

  @Override
  public int getColumnCount() {
    return mitarbeitersTableData.getColumnCount();
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    return mitarbeitersTableData.getValueAt(rowIndex, columnIndex);
  }

  @Override
  public void setValueAt(Object value, int rowIndex, int columnIndex) {
    mitarbeitersTableData.setValueAt(value, rowIndex, columnIndex);
    fireTableCellUpdated(rowIndex, columnIndex);
  }

  @Override
  public boolean isCellEditable(int rowIndex, int columnIndex) {
    return mitarbeitersTableData.isCellEditable(columnIndex);
  }

  @Override
  public String getColumnName(int columnIndex) {
    return mitarbeitersTableData.getColumnName(columnIndex);
  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    return mitarbeitersTableData.getColumnClass(columnIndex);
  }

  public List<Mitarbeiter> getMitarbeiters() {
    return mitarbeitersTableData.mitarbeiters();
  }

  public int getAnzSelektiert() {
    return mitarbeitersTableData.getAnzSelektiert();
  }

  public List<Mitarbeiter> getSelektierteMitarbeiters() {
    return mitarbeitersTableData.getSelektierteMitarbeiters();
  }

  public void alleMitarbeiterSelektieren() {
    mitarbeitersTableData.alleMitarbeiterSelektieren();
  }

  public void alleMitarbeiterDeselektieren() {
    mitarbeitersTableData.alleMitarbeiterDeselektieren();
  }
}
