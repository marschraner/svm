package ch.metzenthin.svm.ui.view;

import ch.metzenthin.svm.ui.componentmodel.TableModel;
import ch.metzenthin.svm.ui.components.CodeListPanel;
import java.awt.event.ActionListener;

/**
 * @author Hans Stamm
 */
public class CodeListView extends AbstractListPanelView {

  public CodeListView(
      TableModel<?, ?> tableModel, String panelTitle, ActionListener closeListener) {
    super(tableModel, new CodeListPanel(panelTitle), closeListener);
  }
}
