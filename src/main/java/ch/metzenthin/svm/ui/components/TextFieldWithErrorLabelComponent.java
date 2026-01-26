package ch.metzenthin.svm.ui.components;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * @author Hans Stamm
 */
public class TextFieldWithErrorLabelComponent {

  private final JTextField textField;
  private final JLabel errorLabel;

  public TextFieldWithErrorLabelComponent(JTextField textField, JLabel errorLabel) {
    this.textField = textField;
    this.errorLabel = errorLabel;
    initializeErrorLabel();
  }

  private void initializeErrorLabel() {
    errorLabel.setVisible(false);
    errorLabel.setForeground(Color.RED);
  }

  public void addActionListener(ActionListener actionListener) {
    textField.addActionListener(actionListener);
  }

  public void addFocusListener(FocusListener focusListener) {
    textField.addFocusListener(focusListener);
  }

  public String getText() {
    return textField.getText();
  }

  public void setText(String text) {
    textField.setText(text);
  }

  public void setToolTipText(String text) {
    textField.setToolTipText(text);
  }

  public boolean isEnabled() {
    return textField.isEnabled();
  }

  public void setErrorLabelText(String errorLabelText) {
    this.errorLabel.setText(errorLabelText);
  }

  public void setErrorLabelVisible(boolean visible) {
    errorLabel.setVisible(visible);
  }
}
