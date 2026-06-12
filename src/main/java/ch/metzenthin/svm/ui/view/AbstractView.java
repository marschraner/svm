package ch.metzenthin.svm.ui.view;

import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

import java.awt.Component;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 * @author Hans Stamm
 */
public abstract class AbstractView {

  protected void showErrorMessageDialog(Component parent, String message, String title) {
    JOptionPane.showMessageDialog(parent, message, title, JOptionPane.ERROR_MESSAGE);
  }

  protected int showYesNoDialog(Component parent, String message, String title) {
    Object[] options = {"Ja", "Nein"};
    return showOptionDialog(parent, message, title, options, JOptionPane.QUESTION_MESSAGE);
  }

  protected int showIgnorierenAbrechenDialog(Component parent, String message, String title) {
    Object[] options = {"Ignorieren", "Abbrechen"};
    return showOptionDialog(parent, message, title, options, JOptionPane.WARNING_MESSAGE);
  }

  @SuppressWarnings("MagicConstant")
  private int showOptionDialog(
      Component parent, String message, String title, Object[] options, int messageType) {
    return JOptionPane.showOptionDialog(
        parent,
        message,
        title,
        JOptionPane.YES_NO_OPTION,
        messageType,
        null,
        options, // the titles of buttons
        options[1]); // default button title
  }

  public JDialog createBusyDialog(String message) {
    final JOptionPane optionPane =
        new JOptionPane(
            message,
            JOptionPane.INFORMATION_MESSAGE,
            JOptionPane.DEFAULT_OPTION,
            null,
            new Object[] {},
            null);

    final JDialog dialog = new JDialog();
    dialog.setTitle("Verarbeitung läuft ...");
    dialog.setModal(true);
    dialog.setContentPane(optionPane);
    dialog.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    dialog.pack();

    return dialog;
  }
}
