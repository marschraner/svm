package ch.metzenthin.svm.ui.components;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTable;

/**
 * @author Hans Stamm
 */
public abstract class AbstractListPanel {

  public abstract JComponent getRootComponent();

  public abstract JTable getTable();

  public abstract JButton getBtnNeu();

  public abstract JButton getBtnBearbeiten();

  public abstract JButton getBtnLoeschen();

  public abstract JButton getBtnAbbrechen();
}
