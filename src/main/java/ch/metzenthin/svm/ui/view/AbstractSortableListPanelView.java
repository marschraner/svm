package ch.metzenthin.svm.ui.view;

import ch.metzenthin.svm.ui.componentmodel.TableModel;
import ch.metzenthin.svm.ui.components.AbstractListPanel;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.RowSorter.SortKey;

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

    List<SortKey> sortKeys = tableModel.getSortKeys();
    if (!sortKeys.isEmpty()) {
      RowSorter<?> rowSorter = table.getRowSorter();
      rowSorter.setSortKeys(sortKeys);
    }
  }
}
