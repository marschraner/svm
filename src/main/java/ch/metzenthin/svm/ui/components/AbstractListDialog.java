package ch.metzenthin.svm.ui.components;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JTable;

/**
 * @author Hans Stamm
 */
public abstract class AbstractListDialog extends JDialog {

  public abstract JComponent getRootComponent();

  public abstract JTable getTable();

  public abstract JButton getBtnNeu();

  public abstract JButton getBtnLoeschen();

  public abstract JButton getBtnSchliessen();
}
