package ch.metzenthin.svm.ui.components;

import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

/**
 * @author Hans Stamm
 */
public class RadioButtonComponent {

  private final JRadioButton radioButton;

  public RadioButtonComponent(JRadioButton radioButton, String actionCommand) {
    this.radioButton = radioButton;
    this.radioButton.setActionCommand(actionCommand);
  }

  public void addActionListener(ActionListener actionListener) {
    radioButton.addActionListener(actionListener);
  }

  public boolean equalsActionCommand(String actionCommand) {
    return radioButton.getActionCommand().equals(actionCommand);
  }

  public void setSelected() {
    radioButton.setSelected(true);
  }

  public ButtonGroup getButtonGroup() {
    return radioButton.getModel().getGroup();
  }
}
