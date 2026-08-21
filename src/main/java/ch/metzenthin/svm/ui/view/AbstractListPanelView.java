package ch.metzenthin.svm.ui.view;

import static ch.metzenthin.svm.ui.components.UiComponentsUtils.setColumnCellRenderers;
import static ch.metzenthin.svm.ui.components.UiComponentsUtils.setJTableColumnWidthAsPercentages;

import ch.metzenthin.svm.ui.componentmodel.TableModel;
import ch.metzenthin.svm.ui.components.AbstractListPanel;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import lombok.Getter;

/**
 * @author Hans Stamm
 */
public abstract class AbstractListPanelView extends AbstractView {

  private final AbstractTableModel tableModel;
  private final JTable table;
  private final JButton buttonNeu;
  private final JButton buttonBearbeiten;
  private final JButton buttonLoeschen;
  private final JButton buttonAbbrechen;
  @Getter private final JComponent rootComponent;

  protected AbstractListPanelView(
      TableModel<?, ?> tableModel, AbstractListPanel listPanel, ActionListener closeListener) {
    this.tableModel = tableModel;
    this.table = listPanel.getTable();
    this.buttonNeu = listPanel.getBtnNeu();
    this.buttonBearbeiten = listPanel.getBtnBearbeiten();
    this.buttonLoeschen = listPanel.getBtnLoeschen();
    this.buttonAbbrechen = listPanel.getBtnAbbrechen();
    this.rootComponent = listPanel.getRootComponent();
    configTable(this.table, tableModel);
    addButtonAbbrechenActionListener(closeListener);
  }

  private static void configTable(JTable table, TableModel<?, ?> tableModel) {
    table.setModel(tableModel);
    setColumnCellRenderers(table, tableModel);
    double[] columnWithsPercentages = tableModel.getColumnWidthsPercentages();
    if (columnWithsPercentages != null && columnWithsPercentages.length > 0) {
      setJTableColumnWidthAsPercentages(table, columnWithsPercentages);
    }
  }

  public void configListeners(Runnable mouseListenerAction, Runnable listSelectionListenerAction) {
    addMouseListener(createMouseListener(mouseListenerAction));
    addListSelectionListener(createListSelectionListener(listSelectionListenerAction));
  }

  private MouseListener createMouseListener(Runnable mouseListenerAction) {
    return new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent me) {
        if (me.getClickCount() == 2) {
          mouseListenerAction.run();
        }
      }
    };
  }

  private ListSelectionListener createListSelectionListener(Runnable listSelectionListenerAction) {
    return e -> {
      if (e.getValueIsAdjusting()) {
        return;
      }
      listSelectionListenerAction.run();
    };
  }

  private void addListSelectionListener(ListSelectionListener listSelectionListener) {
    table.getSelectionModel().addListSelectionListener(listSelectionListener);
  }

  private void addMouseListener(MouseListener mouseListener) {
    table.addMouseListener(mouseListener);
  }

  public int getSelectedRow() {
    return table.getSelectedRow();
  }

  public int convertRowIndexToModel() {
    return table.convertRowIndexToModel(table.getSelectedRow());
  }

  public void clearSelection() {
    table.clearSelection();
  }

  public void fireTableDataChanged() {
    tableModel.fireTableDataChanged();
    table.addNotify();
  }

  public void addButtonNeuActionListener(ActionListener actionListener) {
    buttonNeu.addActionListener(actionListener);
  }

  public void addButtonBearbeitenActionListener(ActionListener actionListener) {
    buttonBearbeiten.addActionListener(actionListener);
  }

  public void addButtonLoeschenActionListener(ActionListener actionListener) {
    buttonLoeschen.addActionListener(actionListener);
  }

  public void addButtonAbbrechenActionListener(ActionListener actionListener) {
    buttonAbbrechen.addActionListener(actionListener);
  }

  public void setButtonBearbeitenEnabled(boolean enabled) {
    buttonBearbeiten.setEnabled(enabled);
  }

  public void setButtonLoeschenEnabled(boolean enabled) {
    buttonLoeschen.setEnabled(enabled);
  }

  public void setButtonNeuFocusPainted(boolean focusPainted) {
    buttonNeu.setFocusPainted(focusPainted);
  }

  public void setButtonBearbeitenFocusPainted(boolean focusPainted) {
    buttonBearbeiten.setFocusPainted(focusPainted);
  }

  public void setButtonLoeschenFocusPainted(boolean focusPainted) {
    buttonLoeschen.setFocusPainted(focusPainted);
  }

  public void showErrorMessageDialog(String message, String title) {
    showErrorMessageDialog(rootComponent, message, title);
  }

  public void showInfoMessageDialog(String message, String title) {
    showInfoMessageDialog(rootComponent, message, title);
  }

  public int showYesNoDialog(String message, String title) {
    return showYesNoDialog(rootComponent, message, title);
  }
}
