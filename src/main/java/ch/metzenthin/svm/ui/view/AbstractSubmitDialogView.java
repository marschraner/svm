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
public abstract class AbstractSubmitDialogView<T extends AbstractSubmitDialog> extends AbstractView
    implements SubmitDialogView {

  protected final T dialog;
  private final JButton buttonSubmit;
  private final JButton buttonAbbrechen;

  protected AbstractSubmitDialogView(T dialog) {
    this.dialog = dialog;
    this.buttonSubmit = dialog.getSubmitButton();
    this.buttonAbbrechen = dialog.getAbbrechenButton();
  }

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
  public void showDialog() {
    dialog.pack();
    dialog.setVisible(true);
  }

  public void closeDialog() {
    dialog.dispose();
  }

  public void showErrorMessageDialog(String message, String title) {
    showErrorMessageDialog(dialog, message, title);
  }

  public int showYesNoDialog(String message, String title) {
    return showYesNoDialog(dialog, message, title);
  }

  public int showIgnorierenAbbrechenDialog(String message, String title) {
    return showIgnorierenAbrechenDialog(dialog, message, title);
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
