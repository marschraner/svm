package ch.metzenthin.svm.ui.components;

import java.awt.Color;
import java.awt.event.ActionListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;

/**
 * @author Hans Stamm
 */
public class ComboBoxWithErrorLabelComponent<T> {

  private final JComboBox<T> comboBox;
  private final JLabel errorLabel;

  public ComboBoxWithErrorLabelComponent(JComboBox<T> comboBox, JLabel errorLabel) {
    this.comboBox = comboBox;
    this.errorLabel = errorLabel;
    initializeErrorLabel();
  }

  private void initializeErrorLabel() {
    errorLabel.setVisible(false);
    errorLabel.setForeground(Color.RED);
  }

  public void addActionListener(ActionListener actionListener) {
    comboBox.addActionListener(actionListener);
  }

  public void setValues(T[] values) {
    comboBox.setModel(new DefaultComboBoxModel<>(values));
  }

  @SuppressWarnings("unchecked")
  public T getSelectedItem() {
    return (T) comboBox.getSelectedItem();
  }

  public void setSelectedItem(T item) {
    comboBox.setSelectedItem(item);
  }

  public void setErrorLabelText(String errorLabelText) {
    errorLabel.setText(errorLabelText);
  }

  public void setErrorLabelVisible(boolean visible) {
    errorLabel.setVisible(visible);
  }
}
