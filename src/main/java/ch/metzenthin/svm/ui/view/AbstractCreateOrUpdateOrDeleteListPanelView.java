package ch.metzenthin.svm.ui.view;

import ch.metzenthin.svm.ui.componentmodel.TableModel;
import ch.metzenthin.svm.ui.components.AbstractCreateOrUpdateOrDeleteListPanel;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/**
 * @author Hans Stamm
 */
public abstract class AbstractCreateOrUpdateOrDeleteListPanelView<
        T extends AbstractCreateOrUpdateOrDeleteListPanel>
    extends AbstractCreateOrDeleteListPanelView<T> {

  private final JButton buttonBearbeiten;

  protected AbstractCreateOrUpdateOrDeleteListPanelView(
      TableModel<?, ?> tableModel, T listPanel, boolean sortable, ActionListener closeListener) {
    super(tableModel, listPanel, sortable, closeListener);
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
