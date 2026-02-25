package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.persistence.entities.Kursort;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class KursortTableData extends AbstractTableData<Kursort> {

  private static final Field[] COLUMNS = {Field.BEZEICHNUNG, Field.SELEKTIERBAR};

  public KursortTableData(List<Kursort> kursortList) {
    super(COLUMNS, kursortList);
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    Kursort kursort = data.get(rowIndex);
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

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    return String.class;
  }
}
