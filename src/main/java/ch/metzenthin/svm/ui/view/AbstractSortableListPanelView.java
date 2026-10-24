package ch.metzenthin.svm.ui.view;

import ch.metzenthin.svm.ui.componentmodel.TableModel;
import ch.metzenthin.svm.ui.components.AbstractListPanel;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.RowSorter.SortKey;
import javax.swing.table.TableRowSorter;

/**
 * @author Hans Stamm
 */
public abstract class AbstractSortableListPanelView<T extends AbstractListPanel>
    extends AbstractListPanelView<T> {

  protected AbstractSortableListPanelView(
      TableModel<?, ?> tableModel, T listPanel, ActionListener closeListener) {
    super(tableModel, listPanel, closeListener);
    configTableColumnsSort();
  }

  private void configTableColumnsSort() {
    table.setAutoCreateRowSorter(true);
    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

    // Das Setzen der SortKeys muss initial gemacht werden, damit eingefügte Rows am richtigen Ort
    // einsortiert werden.
    List<SortKey> sortKeys = tableModel.getSortKeys();
    if (!sortKeys.isEmpty()) {
      RowSorter<?> rowSorter = table.getRowSorter();
      if (rowSorter instanceof TableRowSorter) {
        // Damit nach dem Ändern oder Löschen von Rows die Tabellen-Sortierung neu berechnet wird.
        ((TableRowSorter<?>) rowSorter).setSortsOnUpdates(true);
      }
      rowSorter.setSortKeys(sortKeys);
    }
  }
}
