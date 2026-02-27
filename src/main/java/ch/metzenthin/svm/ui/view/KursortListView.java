package ch.metzenthin.svm.ui.view;

import ch.metzenthin.svm.ui.components.KursortListPanel;
import java.awt.event.ActionListener;
import javax.swing.table.AbstractTableModel;

/**
 * @author Hans Stamm
 */
public class KursortListView extends AbstractListPanelView {

  public KursortListView(AbstractTableModel tableModel, ActionListener closeListener) {
    super(tableModel, new KursortListPanel(), closeListener);
  }
}
