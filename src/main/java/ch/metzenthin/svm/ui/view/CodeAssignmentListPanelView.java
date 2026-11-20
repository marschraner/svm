package ch.metzenthin.svm.ui.view;

import ch.metzenthin.svm.ui.componentmodel.TableModel;
import ch.metzenthin.svm.ui.components.CodeAssignmentListPanel;
import java.awt.event.ActionListener;

/**
 * @author Hans Stamm
 */
public class CodeAssignmentListPanelView
    extends AbstractCreateOrDeleteListPanelView<CodeAssignmentListPanel> {

  public CodeAssignmentListPanelView(
      TableModel<?, ?> tableModel, String title, ActionListener closeListener) {
    super(tableModel, new CodeAssignmentListPanel(title), closeListener);
  }
}
