package ch.metzenthin.svm.ui.view;

import ch.metzenthin.svm.ui.componentmodel.TableModel;
import ch.metzenthin.svm.ui.components.AddOrRemoveCodeAssignmentListPanel;
import java.awt.event.ActionListener;

/**
 * @author Hans Stamm
 */
public class AddOrRemoveCodeAssignmentListPanelView
    extends AbstractZurueckListPanelView<AddOrRemoveCodeAssignmentListPanel> {

  public AddOrRemoveCodeAssignmentListPanelView(
      TableModel<?, ?> tableModel, String title, ActionListener closeListener) {
    super(tableModel, new AddOrRemoveCodeAssignmentListPanel(title), closeListener);
  }
}
