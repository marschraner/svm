package ch.metzenthin.svm.ui.components;

import static ch.metzenthin.svm.ui.components.UiComponentsUtils.setColumnCellRenderers;
import static ch.metzenthin.svm.ui.components.UiComponentsUtils.setJTableColumnWidthAsPercentages;

import java.awt.event.ActionListener;
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
public class TablePanelView {

  private final AbstractTableModel tableModel;
  private final JTable table;
  private final JButton buttonNeu;
  private final JButton buttonBearbeiten;
  private final JButton buttonLoeschen;
  private final JButton buttonAbbrechen;
  @Getter private final JComponent rootComponent;

  public TablePanelView(
      AbstractTableModel tableModel,
      ListSelectionListener listSelectionListener,
      MouseListener mouseListener,
      ActionListener closeListener) {
    this.tableModel = tableModel;
    KursortePanel kursortePanel = new KursortePanel();
    this.table = kursortePanel.getKursorteTable();
    this.buttonNeu = kursortePanel.getBtnNeu();
    this.buttonBearbeiten = kursortePanel.getBtnBearbeiten();
    this.buttonLoeschen = kursortePanel.getBtnLoeschen();
    this.buttonAbbrechen = kursortePanel.getBtnAbbrechen();
    this.rootComponent = kursortePanel.$$$getRootComponent$$$();
    configTable(this.table, tableModel);
    addListSelectionListener(listSelectionListener);
    addMouseListener(mouseListener);
    addButtonAbbrechenActionListener(closeListener);
  }

  private static void configTable(JTable table, AbstractTableModel tableModel) {
    table.setModel(tableModel);
    setColumnCellRenderers(table, tableModel);
    setJTableColumnWidthAsPercentages(table, 0.75, 0.25);
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
}
