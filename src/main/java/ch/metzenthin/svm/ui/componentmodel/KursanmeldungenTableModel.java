package ch.metzenthin.svm.ui.componentmodel;

import ch.metzenthin.svm.domain.model.KursanmeldungenTableData;
import ch.metzenthin.svm.persistence.entities.Kursanmeldung;
import java.io.Serial;
import javax.swing.table.AbstractTableModel;

public class KursanmeldungenTableModel extends AbstractTableModel {

  @Serial private static final long serialVersionUID = 1L;

  private final transient KursanmeldungenTableData kursanmeldungenTableData;

  public KursanmeldungenTableModel(KursanmeldungenTableData kursanmeldungenTableData) {
    super();
    this.kursanmeldungenTableData = kursanmeldungenTableData;
  }

  @Override
  public int getRowCount() {
    return kursanmeldungenTableData.size();
  }

  @Override
  public int getColumnCount() {
    return kursanmeldungenTableData.getColumnCount();
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    return kursanmeldungenTableData.getValueAt(rowIndex, columnIndex);
  }

  @Override
  public String getColumnName(int column) {
    return kursanmeldungenTableData.getColumnName(column);
  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    return kursanmeldungenTableData.getColumnClass();
  }

  public Kursanmeldung getKursanmeldungSelected(int rowIndex) {
    return kursanmeldungenTableData.getKursanmeldungSelected(rowIndex);
  }

  public KursanmeldungenTableData getKursanmeldungenTableData() {
    return kursanmeldungenTableData;
  }
}
