package ch.metzenthin.svm.ui.view;

import ch.metzenthin.svm.ui.componentmodel.TableModel;
import ch.metzenthin.svm.ui.components.DispensationListPanel;
import java.awt.event.ActionListener;

/**
 * @author Hans Stamm
 */
public class DispensationListView
    extends AbstractCreateOrUpdateOrDeleteListPanelView<DispensationListPanel> {

  public DispensationListView(
      TableModel<?, ?> tableModel, String title, ActionListener zurueckListener) {
    super(tableModel, new DispensationListPanel(title), zurueckListener);
  }
}
