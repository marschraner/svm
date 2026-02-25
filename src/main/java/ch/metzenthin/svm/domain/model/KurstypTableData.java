package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.persistence.entities.Kurstyp;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class KurstypTableData extends AbstractTableData<Kurstyp> {

  private static final Field[] COLUMNS = {Field.BEZEICHNUNG, Field.SELEKTIERBAR};

  public KurstypTableData(List<Kurstyp> kurstypen) {
    super(COLUMNS, kurstypen);
  }

  @SuppressWarnings("DuplicatedCode")
  public Object getValueAt(int rowIndex, int columnIndex) {
    Kurstyp kurstyp = data.get(rowIndex);
    Object value = null;
    switch (COLUMNS[columnIndex]) {
      case BEZEICHNUNG -> value = kurstyp.getBezeichnung();
      case SELEKTIERBAR -> value = (kurstyp.isSelektierbar()) ? "ja" : "nein";
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
