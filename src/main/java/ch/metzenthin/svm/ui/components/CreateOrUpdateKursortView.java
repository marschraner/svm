package ch.metzenthin.svm.ui.components;

import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.event.ItemListener;
import javax.swing.JCheckBox;

/**
 * @author Hans Stamm
 */
public class CreateOrUpdateKursortView extends AbstractDialogView<CreateOrUpdateKursortDialog> {

  private final TextFieldWithErrorLabelComponent bezeichnungWithErrorLabel;
  private final JCheckBox checkBoxSelektierbar;

  public CreateOrUpdateKursortView(String title) {
    super(new CreateOrUpdateKursortDialog(title));
    this.bezeichnungWithErrorLabel =
        new TextFieldWithErrorLabelComponent(
            dialog.getTxtBezeichnung(), dialog.getErrLblBezeichnung());
    this.checkBoxSelektierbar = dialog.getCheckBoxSelektierbar();
  }

  public void setErrLblBezeichnungVisible(String errorMessage) {
    bezeichnungWithErrorLabel.setErrorLabelVisible(true);
    bezeichnungWithErrorLabel.setErrorLabelText(errorMessage);
  }

  public void setErrLblBezeichnungInvisible() {
    bezeichnungWithErrorLabel.setErrorLabelVisible(false);
    bezeichnungWithErrorLabel.setErrorLabelText(null);
  }

  public void addTxtBezeichnungActionListener(ActionListener actionListener) {
    bezeichnungWithErrorLabel.addActionListener(actionListener);
  }

  public void addTxtBezeichnungFocusListener(FocusListener focusListener) {
    bezeichnungWithErrorLabel.addFocusListener(focusListener);
  }

  public String getTxtBezeichnungText() {
    return bezeichnungWithErrorLabel.getText();
  }

  public void setTxtBezeichnungText(String text) {
    bezeichnungWithErrorLabel.setText(text);
  }

  public void setTxtBezeichnungToolTipText(String text) {
    bezeichnungWithErrorLabel.setToolTipText(text);
  }

  public boolean isTxtBezeichnungEnabled() {
    return bezeichnungWithErrorLabel.isEnabled();
  }

  public void addCheckBoxSelektierbarItemListener(ItemListener itemListener) {
    checkBoxSelektierbar.addItemListener(itemListener);
  }

  public boolean isCheckBoxSelektierbarSelected() {
    return checkBoxSelektierbar.isSelected();
  }

  public void setCheckBoxSelektierbarSelected(boolean selected) {
    checkBoxSelektierbar.setSelected(selected);
  }
}
