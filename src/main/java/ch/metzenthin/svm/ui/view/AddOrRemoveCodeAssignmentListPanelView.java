package ch.metzenthin.svm.ui.view;

import ch.metzenthin.svm.ui.componentmodel.TableModel;
import ch.metzenthin.svm.ui.components.AddOrRemoveCodeAssignmentListPanel;

/**
 * @author Hans Stamm
 */
public class AddOrRemoveCodeAssignmentListPanelView
    extends AbstractZurueckListPanelView<AddOrRemoveCodeAssignmentListPanel> {

  public AddOrRemoveCodeAssignmentListPanelView(TableModel<?, ?> tableModel, String title) {
    super(tableModel, new AddOrRemoveCodeAssignmentListPanel(title));
  }
}
