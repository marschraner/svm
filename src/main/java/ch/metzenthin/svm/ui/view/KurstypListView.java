package ch.metzenthin.svm.ui.view;

import ch.metzenthin.svm.ui.componentmodel.TableModel;
import ch.metzenthin.svm.ui.components.KurstypListPanel;
import java.awt.event.ActionListener;

/**
 * @author Hans Stamm
 */
public class KurstypListView extends AbstractSortableListPanelView<KurstypListPanel> {

  public KurstypListView(TableModel<?, ?> tableModel, ActionListener closeListener) {
    super(tableModel, new KurstypListPanel(), closeListener);
  }
}
