package ch.metzenthin.svm.ui.components;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

/**
 * @author Hans Stamm
 */
public abstract class AbstractDialogView<T extends SpeichernAbbrechenDialog> {

  protected final T dialog;
  private final JButton buttonSpeichern;
  private final JButton buttonAbbrechen;

  protected AbstractDialogView(T dialog) {
    this.dialog = dialog;
    this.buttonSpeichern = dialog.getSpeichernButton();
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
    JOptionPane.showMessageDialog(dialog, message, title, JOptionPane.ERROR_MESSAGE);
  }

  public void addButtonSpeichernActionListener(ActionListener actionListener) {
    buttonSpeichern.addActionListener(actionListener);
  }

  public void setButtonSpeichernEnabled() {
    buttonSpeichern.setEnabled(true);
    buttonSpeichern.setToolTipText(null);
  }

  public void setButtonSpeichernDisabled() {
    setButtonSpeichernDisabled(null);
  }

  public void setButtonSpeichernDisabled(String toolTipText) {
    buttonSpeichern.setEnabled(false);
    buttonSpeichern.setToolTipText(toolTipText);
  }

  public void setButtonSpeichernFocusPainted(boolean focusPainted) {
    buttonSpeichern.setFocusPainted(focusPainted);
  }

  public void addButtonAbbrechenActionListener(ActionListener actionListener) {
    buttonAbbrechen.addActionListener(actionListener);
  }
}
