package ch.metzenthin.svm.ui.components;

import java.awt.Color;
import java.awt.KeyboardFocusManager;
import java.awt.event.FocusListener;
import javax.swing.JLabel;
import javax.swing.JTextArea;

/**
 * @author Hans Stamm
 */
public class TextAreaWithErrorLabelComponent {

  private final JTextArea textArea;
  private final JLabel errorLabel;

  public TextAreaWithErrorLabelComponent(JTextArea textArea, JLabel errorLabel) {
    this.textArea = textArea;
    this.errorLabel = errorLabel;
    textArea.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, null);
    textArea.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, null);
    initializeErrorLabel();
  }

  private void initializeErrorLabel() {
    errorLabel.setVisible(false);
    errorLabel.setForeground(Color.RED);
  }

  public void addFocusListener(FocusListener focusListener) {
    textArea.addFocusListener(focusListener);
  }

  public String getText() {
    return textArea.getText();
  }

  public void setText(String text) {
    textArea.setText(text);
  }

  public void setToolTipText(String text) {
    textArea.setToolTipText(text);
  }

  public boolean isEnabled() {
    return textArea.isEnabled();
  }

  public void setEnabled(boolean enabled) {
    textArea.setEnabled(enabled);
    if (!enabled) {
      setErrorLabelVisible(false);
    }
  }

  public void setErrorLabelText(String errorLabelText) {
    this.errorLabel.setText(errorLabelText);
  }

  public void setErrorLabelVisible(boolean visible) {
    errorLabel.setVisible(visible);
  }
}
