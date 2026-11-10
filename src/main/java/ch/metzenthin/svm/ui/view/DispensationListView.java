package ch.metzenthin.svm.ui.view;

import ch.metzenthin.svm.ui.componentmodel.CalendarTableCellRenderer;
import ch.metzenthin.svm.ui.componentmodel.TableModel;
import ch.metzenthin.svm.ui.components.DispensationListPanel;
import java.awt.event.ActionListener;
import java.util.Calendar;

/**
 * @author Hans Stamm
 */
public class DispensationListView
    extends AbstractBearbeitenAbbrechenListPanelView<DispensationListPanel> {

  public DispensationListView(
      TableModel<?, ?> tableModel, String title, ActionListener zurueckListener) {
    super(tableModel, new DispensationListPanel(title), zurueckListener);
    // TODO:   addButtonZurueckActionListener(zurueckListener);
    setTableDefaultRenderer();
  }

  private void setTableDefaultRenderer() {
    table.setDefaultRenderer(Calendar.class, new CalendarTableCellRenderer());
  }
}
