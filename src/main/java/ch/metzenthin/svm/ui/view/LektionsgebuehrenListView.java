package ch.metzenthin.svm.ui.view;

import ch.metzenthin.svm.ui.componentmodel.TableModel;
import ch.metzenthin.svm.ui.components.LektionsgebuehrenListPanel;
import java.awt.event.ActionListener;

/**
 * @author Hans Stamm
 */
public class LektionsgebuehrenListView extends AbstractListPanelView<LektionsgebuehrenListPanel> {

  public LektionsgebuehrenListView(TableModel<?, ?> tableModel, ActionListener closeListener) {
    super(tableModel, new LektionsgebuehrenListPanel(), closeListener);
  }
}
