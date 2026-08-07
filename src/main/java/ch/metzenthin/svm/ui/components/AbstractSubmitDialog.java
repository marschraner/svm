package ch.metzenthin.svm.ui.components;

import javax.swing.JButton;
import javax.swing.JDialog;

/**
 * @author Hans Stamm
 */
public abstract class AbstractSubmitDialog extends JDialog {

  public abstract JButton getSubmitButton();
  public abstract JButton getAbbrechenButton();
}
