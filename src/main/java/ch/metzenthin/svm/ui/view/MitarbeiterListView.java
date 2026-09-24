package ch.metzenthin.svm.ui.view;

import ch.metzenthin.svm.ui.componentmodel.TableModel;
import ch.metzenthin.svm.ui.components.MitarbeiterListPanel;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;

/**
 * @author Hans Stamm
 */
public class MitarbeiterListView extends AbstractListPanelView<MitarbeiterListPanel> {

  private final JButton buttonExportieren;
  private final JButton buttonEmail;
  private final JButton buttonAlleSelektieren;
  private final JButton buttonAlleDeselektieren;
  private final JButton buttonZurueck;
  private final JLabel labelTotal;

  public MitarbeiterListView(
      TableModel<?, ?> tableModel, ActionListener closeListener, ActionListener zurueckListener) {
    super(tableModel, new MitarbeiterListPanel(), closeListener);
    this.buttonExportieren = listPanel.getBtnExportieren();
    this.buttonEmail = listPanel.getBtnEmail();
    this.buttonAlleSelektieren = listPanel.getBtnAlleSelektieren();
    this.buttonAlleDeselektieren = listPanel.getBtnAlleDeselektieren();
    this.buttonZurueck = listPanel.getBtnZurueck();
    this.labelTotal = listPanel.getLblTotal();
    addButtonZurueckActionListener(zurueckListener);
  }

  // Button Exportieren
  public void addButtonExportierenActionListener(ActionListener actionListener) {
    buttonExportieren.addActionListener(actionListener);
  }

  public void setButtonExportierenFocusPainted(boolean focusPainted) {
    buttonExportieren.setFocusPainted(focusPainted);
  }

  public void setButtonExportierenEnabled(boolean enabled) {
    buttonExportieren.setEnabled(enabled);
  }

  // Button Email
  public void addButtonEmailActionListener(ActionListener actionListener) {
    buttonEmail.addActionListener(actionListener);
  }

  public void setButtonEmailFocusPainted(boolean focusPainted) {
    buttonEmail.setFocusPainted(focusPainted);
  }

  public void setButtonEmailEnabled(boolean enabled) {
    buttonEmail.setEnabled(enabled);
  }

  public void setButtonEmailText(String buttonText) {
    buttonEmail.setText(buttonText);
  }

  // Button alle selektieren
  public void addButtonAlleSelektierenActionListener(ActionListener actionListener) {
    buttonAlleSelektieren.addActionListener(actionListener);
  }

  public void setButtonAlleSelektierenVisible(boolean visible) {
    buttonAlleSelektieren.setVisible(visible);
  }

  // Button alle deselektieren
  public void addButtonAlleDeselektierenActionListener(ActionListener actionListener) {
    buttonAlleDeselektieren.addActionListener(actionListener);
  }

  public void setButtonAlleDeselektierenVisible(boolean visible) {
    buttonAlleDeselektieren.setVisible(visible);
  }

  // Button Zurück
  private void addButtonZurueckActionListener(ActionListener actionListener) {
    buttonZurueck.addActionListener(actionListener);
  }

  // Label Total
  public void setLabelTotalText(String labelText) {
    labelTotal.setText(labelText);
  }
}
