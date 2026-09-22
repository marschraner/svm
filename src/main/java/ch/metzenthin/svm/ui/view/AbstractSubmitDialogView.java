package ch.metzenthin.svm.ui.view;

import ch.metzenthin.svm.ui.components.AbstractSubmitDialog;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/**
 * @param <T> Dialog-Typ, z.B. CreateOrUpdateKursortDialog
 * @author Hans Stamm
 */
public abstract class AbstractSubmitDialogView<T extends AbstractSubmitDialog>
    extends AbstractDialogView<T> implements SubmitDialogView {

  protected final T submitDialog;
  private final JButton buttonSubmit;
  private final JButton buttonAbbrechen;

  protected AbstractSubmitDialogView(T submitDialog) {
    super(submitDialog);
    this.submitDialog = submitDialog;
    this.buttonSubmit = submitDialog.getSubmitButton();
    this.buttonAbbrechen = submitDialog.getAbbrechenButton();
  }

  @Override
  public void addButtonSubmitActionListener(ActionListener actionListener) {
    buttonSubmit.addActionListener(actionListener);
  }

  @Override
  public void setButtonSubmitFocusPainted(boolean focusPainted) {
    buttonSubmit.setFocusPainted(focusPainted);
  }

  @Override
  public void addButtonAbbrechenActionListener(ActionListener actionListener) {
    buttonAbbrechen.addActionListener(actionListener);
  }
}
