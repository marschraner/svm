package ch.metzenthin.svm.ui.view;

import ch.metzenthin.svm.ui.componentmodel.TableModel;
import ch.metzenthin.svm.ui.components.AbstractBearbeitenAbbrechenListPanel;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/**
 * @author Hans Stamm
 */
public abstract class AbstractBearbeitenAbbrechenListPanelView<T extends AbstractBearbeitenAbbrechenListPanel>
    extends AbstractAbbrechenListPanelView<T> {

  private final JButton buttonBearbeiten;

  protected AbstractBearbeitenAbbrechenListPanelView(
      TableModel<?, ?> tableModel, T listPanel, ActionListener closeListener) {
    super(tableModel, listPanel, closeListener);
    this.buttonBearbeiten = listPanel.getBtnBearbeiten();
  }

  public void addButtonBearbeitenActionListener(ActionListener actionListener) {
    buttonBearbeiten.addActionListener(actionListener);
  }

  public void setButtonBearbeitenEnabled(boolean enabled) {
    buttonBearbeiten.setEnabled(enabled);
  }

  public void setButtonBearbeitenFocusPainted(boolean focusPainted) {
    buttonBearbeiten.setFocusPainted(focusPainted);
  }
}
