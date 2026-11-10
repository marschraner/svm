package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.persistence.entities.Dispensation;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import javax.swing.RowSorter;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;

/**
 * @author Hans Stamm
 */
public class DispensationTableData extends AbstractTableData<Dispensation> {

  private static final Field[] COLUMNS = {
    Field.DISPENSATIONSBEGINN, Field.DISPENSATIONSENDE, Field.VORAUSSICHTLICHE_DAUER, Field.GRUND
  };

  public DispensationTableData(List<Dispensation> dispensationen) {
    super(COLUMNS, dispensationen);
  }

  public Object getValueAt(int rowIndex, int columnIndex) {
    Dispensation dispensation = data.get(rowIndex);
    Object value = null;
    switch (COLUMNS[columnIndex]) {
      case DISPENSATIONSBEGINN -> value = dispensation.getDispensationsbeginn();
      case DISPENSATIONSENDE -> value = dispensation.getDispensationsende();
      case VORAUSSICHTLICHE_DAUER -> value = dispensation.getVoraussichtlicheDauer();
      case GRUND -> value = dispensation.getGrund();
      default -> {
        // Nothing to do
      }
    }
    return value;
  }

  public Class<?> getColumnClass(int columnIndex) {
    return switch (COLUMNS[columnIndex]) {
      case DISPENSATIONSBEGINN, DISPENSATIONSENDE -> Calendar.class;
      default -> String.class;
    };
  }

  @Override
  public List<SortKey> getSortKeys() {
    List<Field> columnsList = Arrays.asList(COLUMNS);
    return List.of(
        new RowSorter.SortKey(columnsList.indexOf(Field.DISPENSATIONSBEGINN), SortOrder.ASCENDING));
  }
}
