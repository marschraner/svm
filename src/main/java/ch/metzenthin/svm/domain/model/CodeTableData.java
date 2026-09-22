package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.persistence.entities.Code;
import java.util.List;
import lombok.Setter;

/**
 * @param <T> Typ der Table-Rows, z.B. Kursort
 * @author Hans Stamm
 */
@Setter
public class CodeTableData<T extends Code> extends AbstractTableData<T> {

  private static final Field[] COLUMNS = {Field.KUERZEL, Field.BESCHREIBUNG, Field.SELEKTIERBAR};

  public CodeTableData(List<T> codeList, boolean isSelektierbarToBeDisplayed) {
    super(getColumns(isSelektierbarToBeDisplayed), codeList);
  }

  private static Field[] getColumns(boolean isSelektierbarToBeDisplayed) {
    if (!isSelektierbarToBeDisplayed) {
      return new Field[] {Field.KUERZEL, Field.BESCHREIBUNG};
    }
    return COLUMNS;
  }

  public Object getValueAt(int rowIndex, int columnIndex) {
    T code = data.get(rowIndex);
    Object value = null;
    switch (COLUMNS[columnIndex]) {
      case KUERZEL -> value = code.getKuerzel();
      case BESCHREIBUNG -> value = code.getBeschreibung();
      case SELEKTIERBAR -> value = (code.isSelektierbar()) ? "ja" : "nein";
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
