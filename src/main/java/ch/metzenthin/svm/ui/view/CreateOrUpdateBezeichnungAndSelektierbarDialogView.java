package ch.metzenthin.svm.ui.view;

import ch.metzenthin.svm.ui.components.CreateOrUpdateBezeichnungAndSelektierbarDialog;
import ch.metzenthin.svm.ui.components.TextFieldWithErrorLabelComponent;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import javax.swing.JCheckBox;

/**
 * @param <T> Dialog-Typ, z.B. CreateOrUpdateKursortDialog
 * @author Hans Stamm
 */
public abstract class CreateOrUpdateBezeichnungAndSelektierbarDialogView<
        T extends CreateOrUpdateBezeichnungAndSelektierbarDialog>
    extends SpeichernAbbrechenDialogView<T> {

  private final TextFieldWithErrorLabelComponent bezeichnungWithErrorLabel;
  private final JCheckBox checkBoxSelektierbar;

  protected CreateOrUpdateBezeichnungAndSelektierbarDialogView(T dialog) {
    super(dialog);
    this.bezeichnungWithErrorLabel =
        new TextFieldWithErrorLabelComponent(
            dialog.getTxtBezeichnung(), dialog.getErrLblBezeichnung());
    this.checkBoxSelektierbar = dialog.getCheckBoxSelektierbar();
  }

  public void setErrorLabelBezeichnungVisible(String errorMessage) {
    bezeichnungWithErrorLabel.setErrorLabelVisible(true);
    bezeichnungWithErrorLabel.setErrorLabelText(errorMessage);
  }

  public void setErrorLabelBezeichnungInvisible() {
    bezeichnungWithErrorLabel.setErrorLabelVisible(false);
    bezeichnungWithErrorLabel.setToolTipText(null);
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

  public boolean isCheckBoxSelektierbarSelected() {
    return checkBoxSelektierbar.isSelected();
  }

  public void setCheckBoxSelektierbarSelected(boolean selected) {
    checkBoxSelektierbar.setSelected(selected);
  }
}
