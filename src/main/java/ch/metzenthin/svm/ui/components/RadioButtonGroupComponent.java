package ch.metzenthin.svm.ui.components;

import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import javax.swing.ButtonGroup;

/**
 * @author Hans Stamm
 */
public class RadioButtonGroupComponent {

  private final List<RadioButtonComponent> radioButtonGroup;

  public RadioButtonGroupComponent(RadioButtonComponent... radioButtonComponents) {
    this.radioButtonGroup = Arrays.stream(radioButtonComponents).toList();
  }

  public void addActionListener(ActionListener actionListener) {
    for (RadioButtonComponent radioButtonComponent : radioButtonGroup) {
      radioButtonComponent.addActionListener(actionListener);
    }
  }

  public void setSelected(String actionCommand) {
    for (RadioButtonComponent radioButtonComponent : radioButtonGroup) {
      if (radioButtonComponent.equalsActionCommand(actionCommand)) {
        radioButtonComponent.setSelected();
      }
    }
  }

  public String getSelected() {
    ButtonGroup buttonGroup = getButtonGroup();
    return buttonGroup.getSelection().getActionCommand();
  }

  private ButtonGroup getButtonGroup() {
    return radioButtonGroup.get(0).getButtonGroup();
  }
}
