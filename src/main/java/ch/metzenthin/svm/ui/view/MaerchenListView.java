package ch.metzenthin.svm.ui.view;

import ch.metzenthin.svm.ui.componentmodel.TableModel;
import ch.metzenthin.svm.ui.components.MaerchenListPanel;
import java.awt.event.ActionListener;

/**
 * @author Hans Stamm
 */
public class MaerchenListView extends AbstractListPanelView<MaerchenListPanel> {

  public MaerchenListView(TableModel<?, ?> tableModel, ActionListener closeListener) {
    super(tableModel, new MaerchenListPanel(), closeListener);
  }
}
