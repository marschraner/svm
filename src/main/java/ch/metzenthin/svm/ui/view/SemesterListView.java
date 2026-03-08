package ch.metzenthin.svm.ui.view;

import ch.metzenthin.svm.ui.componentmodel.TableModel;
import ch.metzenthin.svm.ui.components.SemesterListPanel;
import java.awt.event.ActionListener;

/**
 * @author Hans Stamm
 */
public class SemesterListView extends AbstractListPanelView {

  public SemesterListView(TableModel<?, ?> tableModel, ActionListener closeListener) {
    super(tableModel, new SemesterListPanel(), closeListener);
  }
}
