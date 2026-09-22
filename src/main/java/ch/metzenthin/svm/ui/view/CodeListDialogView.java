package ch.metzenthin.svm.ui.view;

import ch.metzenthin.svm.ui.componentmodel.TableModel;
import ch.metzenthin.svm.ui.components.CodeListDialog;

/**
 * @author Hans Stamm
 */
public class CodeListDialogView extends AbstractListDialogView {

  public CodeListDialogView(TableModel<?, ?> tableModel, String dialogTitle) {
    super(tableModel, new CodeListDialog(dialogTitle));
  }
}
