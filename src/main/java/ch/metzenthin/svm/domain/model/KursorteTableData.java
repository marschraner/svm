package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.persistence.entities.Kursort;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class KursorteTableData {

  private static final Field[] COLUMNS = {Field.BEZEICHNUNG, Field.SELEKTIERBAR};

  private List<Kursort> kursorte;

  public KursorteTableData(List<Kursort> kursorte) {
    this.kursorte = kursorte;
  }

  public int getColumnCount() {
    return COLUMNS.length;
  }

  public int size() {
    return kursorte.size();
  }

  @SuppressWarnings("DuplicatedCode")
  public Object getValueAt(int rowIndex, int columnIndex) {
    Kursort kursort = kursorte.get(rowIndex);
    Object value = null;
    switch (COLUMNS[columnIndex]) {
      case BEZEICHNUNG -> value = kursort.getBezeichnung();
      case SELEKTIERBAR -> value = (kursort.isSelektierbar()) ? "ja" : "nein";
      default -> {
        // Nothing to do
      }
    }
    return value;
  }

  public Class<?> getColumnClass() {
    return String.class;
  }

  public String getColumnName(int column) {
    return COLUMNS[column].toString();
  }

  public void setKursorte(List<Kursort> kursorte) {
    this.kursorte = kursorte;
  }

  public List<Kursort> getKursorte() {
    return kursorte;
  }
}
