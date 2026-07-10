package ch.metzenthin.svm.ui.view;

import ch.metzenthin.svm.ui.componentmodel.TableModel;
import ch.metzenthin.svm.ui.components.KursListPanel;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;

/**
 * @author Hans Stamm
 */
public class KursListView extends AbstractListPanelView {

  private final JLabel labelTotal;
  private final JButton buttonImportieren;
  private final JButton buttonExportieren;

  public KursListView(
      KursListPanel kursListPanel, TableModel<?, ?> tableModel, ActionListener closeListener) {
    super(tableModel, kursListPanel, closeListener);
    labelTotal = kursListPanel.getLblTotal();
    buttonExportieren = kursListPanel.getBtnExportieren();
    buttonImportieren = kursListPanel.getBtnImportieren();
  }

  public void setTotalText(String totalText) {
    labelTotal.setText(totalText);
  }

  public void addButtonImportierenActionListener(ActionListener actionListener) {
    buttonImportieren.addActionListener(actionListener);
  }

  public void setButtonImportierenFocusPainted(boolean focusPainted) {
    buttonImportieren.setFocusPainted(focusPainted);
  }

  public void addButtonExportierenActionListener(ActionListener actionListener) {
    buttonExportieren.addActionListener(actionListener);
  }

  public void setButtonExportierenFocusPainted(boolean focusPainted) {
    buttonExportieren.setFocusPainted(focusPainted);
  }

  public void setButtonExportierenEnabled(boolean enabled) {
    buttonExportieren.setEnabled(enabled);
  }
}
