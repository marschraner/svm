package ch.metzenthin.svm.ui.view;

import ch.metzenthin.svm.ui.componentmodel.TableModel;
import ch.metzenthin.svm.ui.components.AbstractAbbrechenListPanel;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/**
 * @author Hans Stamm
 */
public abstract class AbstractAbbrechenListPanelView<T extends AbstractAbbrechenListPanel>
    extends AbstractListPanelView<T> {

  private final JButton buttonAbbrechen;

  protected AbstractAbbrechenListPanelView(
      TableModel<?, ?> tableModel, T listPanel, ActionListener closeListener) {
    super(tableModel, listPanel);
    this.buttonAbbrechen = listPanel.getBtnAbbrechen();
    addButtonAbbrechenActionListener(closeListener);
  }

  public void addButtonAbbrechenActionListener(ActionListener actionListener) {
    buttonAbbrechen.addActionListener(actionListener);
  }
}
