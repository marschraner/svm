package ch.metzenthin.svm.ui.components;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * @author Hans Stamm
 */
public abstract class AbstractSuchenPanel extends JPanel {

  public abstract JComponent getRootComponent();

  public abstract JPanel getMainPanel();

  public abstract JButton getSuchenButton();

  public abstract JButton getAbbrechenButton();
}
