package ch.metzenthin.svm.ui.components;

import java.awt.Component;
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
    return JOptionPane.showOptionDialog(
        parent,
        message,
        title,
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE,
        null,
        options, // the titles of buttons
        options[1]); // default button title
  }
}
