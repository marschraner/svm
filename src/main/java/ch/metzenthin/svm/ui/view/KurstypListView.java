package ch.metzenthin.svm.ui.view;

import ch.metzenthin.svm.ui.components.KurstypListPanel;
import java.awt.event.ActionListener;
import javax.swing.table.AbstractTableModel;

/**
 * @author Hans Stamm
 */
public class KurstypListView extends AbstractListPanelView {

  public KurstypListView(AbstractTableModel tableModel, ActionListener closeListener) {
    super(tableModel, new KurstypListPanel(), closeListener);
  }
}
