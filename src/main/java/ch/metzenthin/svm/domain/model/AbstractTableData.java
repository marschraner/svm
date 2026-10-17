package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.datatypes.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.RowSorter.SortKey;
import lombok.Setter;

/**
 * @param <T> Typ der Table-Rows, z.B. Kursort
 * @author Hans Stamm
 */
public abstract class AbstractTableData<T> {

  private final Field[] columns;
  @Setter protected List<T> data;

  protected AbstractTableData(Field[] columns, List<T> data) {
    this.columns = columns;
    this.data = new ArrayList<>(data); // Damit die Liste modifiziert werden kann
  }

  public int getColumnCount() {
    return columns.length;
  }

  public int size() {
    return data.size();
  }

  public abstract Object getValueAt(int rowIndex, int columnIndex);

  public abstract Class<?> getColumnClass(int columnIndex);

  public String getColumnName(int column) {
    return columns[column].toString();
  }

  public T getRowAt(int rowIndex) {
    return data.get(rowIndex);
  }

  public int insertRow(T row) {
    int indexOfInsertedRow = data.size();
    data.add(row);
    return indexOfInsertedRow;
  }

  public void replaceRowAt(int rowIndex, T row) {
    data.set(rowIndex, row);
  }

  public void removeRowAt(int rowIndex) {
    data.remove(rowIndex);
  }

  public void forEachRow(Consumer<T> action) {
    data.forEach(action);
  }

  public Iterator<T> getIterator() {
    return data.iterator();
  }

  public abstract List<SortKey> getSortKeys();
}
