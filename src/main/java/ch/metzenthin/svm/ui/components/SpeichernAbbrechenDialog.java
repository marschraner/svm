package ch.metzenthin.svm.ui.components;

import javax.swing.JButton;
import javax.swing.JDialog;

/**
 * @author Hans Stamm
 */
public abstract class SpeichernAbbrechenDialog extends JDialog {

  public abstract JButton getSpeichernButton();
  public abstract JButton getAbbrechenButton();
}
