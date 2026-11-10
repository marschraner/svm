package ch.metzenthin.svm.ui.view;

import ch.metzenthin.svm.ui.componentmodel.TableModel;
import ch.metzenthin.svm.ui.components.AbstractBearbeitenZurueckListPanel;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/**
 * @author Hans Stamm
 */
public abstract class AbstractBearbeitenZurueckListPanelView<
        T extends AbstractBearbeitenZurueckListPanel>
    extends AbstractZurueckListPanelView<T> {

  private final JButton buttonBearbeiten;

  protected AbstractBearbeitenZurueckListPanelView(TableModel<?, ?> tableModel, T listPanel) {
    super(tableModel, listPanel);
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
