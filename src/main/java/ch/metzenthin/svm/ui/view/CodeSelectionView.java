package ch.metzenthin.svm.ui.view;

import ch.metzenthin.svm.persistence.entities.Code;
import ch.metzenthin.svm.ui.components.CodeSelectionDialog;
import ch.metzenthin.svm.ui.components.ComboBoxWithErrorLabelComponent;
import java.awt.event.ActionListener;

/**
 * @author Hans Stamm
 */
public class CodeSelectionView<T extends Code>
    extends AbstractSubmitDialogView<CodeSelectionDialog<T>> {

  private final ComboBoxWithErrorLabelComponent<T> comboBoxCode;

  public CodeSelectionView(String title) {
    super(new CodeSelectionDialog<>(title));
    this.comboBoxCode =
        new ComboBoxWithErrorLabelComponent<>(dialog.getComboBoxCode(), dialog.getErrLblCode());
  }

  // Code
  public void setErrorLabelCodeVisible(String errorMessage) {
    comboBoxCode.setErrorLabelVisible(true);
    comboBoxCode.setErrorLabelText(errorMessage);
  }

  public void setErrorLabelCodeInvisible() {
    comboBoxCode.setErrorLabelVisible(false);
  }

  public void addComboBoxCodeActionListener(ActionListener actionListener) {
    comboBoxCode.addActionListener(actionListener);
  }

  public void setComboBoxCodeValues(T[] codes) {
    comboBoxCode.setValues(codes);
  }

  public T getComboBoxCodeSelectedItem() {
    return comboBoxCode.getSelectedItem();
  }

  public void setComboBoxCodeSelectedItem(T code) {
    comboBoxCode.setSelectedItem(code);
  }
}
