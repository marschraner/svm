package ch.metzenthin.svm.ui.view;

import ch.metzenthin.svm.ui.componentmodel.TableModel;
import ch.metzenthin.svm.ui.components.KursortListPanel;
import java.awt.event.ActionListener;

/**
 * @author Hans Stamm
 */
public class KursortListView extends AbstractCreateOrUpdateOrDeleteListPanelView<KursortListPanel> {

  public KursortListView(TableModel<?, ?> tableModel, ActionListener closeListener) {
    super(tableModel, new KursortListPanel(), true, closeListener);
  }
}
