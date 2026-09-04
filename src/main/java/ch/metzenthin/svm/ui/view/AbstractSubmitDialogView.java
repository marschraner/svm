package ch.metzenthin.svm.ui.view;

import ch.metzenthin.svm.ui.components.AbstractSubmitDialog;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

/**
 * @param <T> Dialog-Typ, z.B. CreateOrUpdateKursortDialog
 * @author Hans Stamm
 */
public abstract class AbstractSubmitDialogView<T extends AbstractSubmitDialog>
    extends AbstractView<T> implements SubmitDialogView {

  protected final T dialog;
  private final JButton buttonSubmit;
  private final JButton buttonAbbrechen;

  protected AbstractSubmitDialogView(T dialog) {
    super(dialog);
    this.dialog = dialog;
    this.buttonSubmit = dialog.getSubmitButton();
    this.buttonAbbrechen = dialog.getAbbrechenButton();
  }

  @Override
  public void configDialogClosing(ActionListener closeActionListener) {
    dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    // call closeActionListener on ESCAPE
    ((JPanel) dialog.getContentPane())
        .registerKeyboardAction(
            closeActionListener,
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
            JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    WindowAdapter windowAdapter =
        new WindowAdapter() {
          @Override
          public void windowClosing(WindowEvent e) {
            closeActionListener.actionPerformed(null);
          }
        };
    dialog.addWindowListener(windowAdapter);
  }

  /**
   * Der Aufruf dieser Methode blockiert das UI. Nach dem Schliessen des Dialogs geht es weiter mit
   * dem Statement nach dem Methodenaufruf.
   */
  @Override
  public void showDialog() {
    dialog.pack();
    dialog.setVisible(true);
  }

  @Override
  public void closeDialog() {
    dialog.dispose();
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
