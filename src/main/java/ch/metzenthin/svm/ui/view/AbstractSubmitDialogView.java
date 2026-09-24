package ch.metzenthin.svm.ui.view;

import ch.metzenthin.svm.ui.components.AbstractSubmitDialog;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/**
 * @param <T> Dialog-Typ, z.B. CreateOrUpdateKursortDialog
 * @author Hans Stamm
 */
public abstract class AbstractSubmitDialogView<T extends AbstractSubmitDialog>
    extends AbstractDialogView<T> {

  protected final T submitDialog;
  private final JButton buttonSubmit;
  private final JButton buttonAbbrechen;

  protected AbstractSubmitDialogView(T submitDialog) {
    super(submitDialog);
    this.submitDialog = submitDialog;
    this.buttonSubmit = submitDialog.getSubmitButton();
    this.buttonAbbrechen = submitDialog.getAbbrechenButton();
  }

  public void addButtonSubmitActionListener(ActionListener actionListener) {
    buttonSubmit.addActionListener(actionListener);
  }

  public void setButtonSubmitFocusPainted(boolean focusPainted) {
    buttonSubmit.setFocusPainted(focusPainted);
  }

  public void addButtonAbbrechenActionListener(ActionListener actionListener) {
    buttonAbbrechen.addActionListener(actionListener);
  }
}
