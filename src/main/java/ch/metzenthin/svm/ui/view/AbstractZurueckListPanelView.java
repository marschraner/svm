package ch.metzenthin.svm.ui.view;

import ch.metzenthin.svm.ui.componentmodel.TableModel;
import ch.metzenthin.svm.ui.components.AbstractZurueckListPanel;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/**
 * @author Hans Stamm
 */
public abstract class AbstractZurueckListPanelView<T extends AbstractZurueckListPanel>
    extends AbstractListPanelView<T> {

  private final JButton buttonZurueck;

  protected AbstractZurueckListPanelView(TableModel<?, ?> tableModel, T listPanel) {
    super(tableModel, listPanel);
    this.buttonZurueck = listPanel.getBtnZurueck();
  }

  public void addButtonZurueckActionListener(ActionListener actionListener) {
    buttonZurueck.addActionListener(actionListener);
  }
}
