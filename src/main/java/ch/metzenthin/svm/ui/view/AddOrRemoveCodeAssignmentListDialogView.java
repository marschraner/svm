package ch.metzenthin.svm.ui.view;

import ch.metzenthin.svm.ui.componentmodel.TableModel;
import ch.metzenthin.svm.ui.components.CodeAssignmentListDialog;

/**
 * @author Hans Stamm
 */
public class AddOrRemoveCodeAssignmentListDialogView extends AbstractListDialogView {

  public AddOrRemoveCodeAssignmentListDialogView(TableModel<?, ?> tableModel, String dialogTitle) {
    super(tableModel, new CodeAssignmentListDialog(dialogTitle));
  }
}
