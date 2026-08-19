package ch.metzenthin.svm.ui.view;

import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

import ch.metzenthin.svm.ui.components.SwingWorkerWithBusyDialog;
import java.awt.Component;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 * @author Hans Stamm
 */
public abstract class AbstractView<T extends Component> {

  protected final T component;

  protected AbstractView(T component) {
    this.component = component;
  }

  public void showInfoMessageDialog(String message, String title) {
    JOptionPane.showMessageDialog(component, message, title, JOptionPane.INFORMATION_MESSAGE);
  }

  public void showErrorMessageDialog(String message, String title) {
    JOptionPane.showMessageDialog(component, message, title, JOptionPane.ERROR_MESSAGE);
  }

  public int showYesNoDialog(String message, String title) {
    Object[] options = {"Ja", "Nein"};
    return showOptionDialog(component, message, title, options, JOptionPane.QUESTION_MESSAGE);
  }

  public int showIgnorierenAbbrechenDialog(String message, String title) {
    Object[] options = {"Ignorieren", "Abbrechen"};
    return showOptionDialog(component, message, title, options, JOptionPane.WARNING_MESSAGE);
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

  public <U> SwingWorkerWithBusyDialog<U> createSwingWorkerWithBusyDialog(String message) {
    return new SwingWorkerWithBusyDialog<>(createBusyDialog(message));
  }

  private JDialog createBusyDialog(String message) {
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
    dialog.setLocationRelativeTo(component);
    dialog.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    dialog.pack();

    return dialog;
  }
}
