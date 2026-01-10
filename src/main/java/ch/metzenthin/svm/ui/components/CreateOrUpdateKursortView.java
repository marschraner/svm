package ch.metzenthin.svm.ui.components;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

/**
 * @author Hans Stamm
 */
public class CreateOrUpdateKursortView {

  // Schalter zur Aktivierung des Default-Button (nicht dynamisch)
  private static final boolean DEFAULT_BUTTON_ENABLED = false;

  private final CreateOrUpdateKursortDialog createOrUpdateKursortDialog;
  private final JTextField txtBezeichnung;
  private final JLabel errLblBezeichnung;
  private final JCheckBox checkBoxSelektierbar;
  private final JButton buttonSpeichern;
  private final JButton buttonAbbrechen;

  public CreateOrUpdateKursortView(String title, ActionListener escapeActionListener) {
    createOrUpdateKursortDialog = new CreateOrUpdateKursortDialog(title);
    this.txtBezeichnung = createOrUpdateKursortDialog.getTxtBezeichnung();
    this.errLblBezeichnung = createOrUpdateKursortDialog.getErrLblBezeichnung();
    this.checkBoxSelektierbar = createOrUpdateKursortDialog.getCheckBoxSelektierbar();
    this.buttonSpeichern = createOrUpdateKursortDialog.getBtnSpeichern();
    this.buttonAbbrechen = createOrUpdateKursortDialog.getBtnAbbrechen();
    if (DEFAULT_BUTTON_ENABLED) {
      createOrUpdateKursortDialog.getRootPane().setDefaultButton(buttonSpeichern);
    }
    createOrUpdateKursortDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    // call escapeActionListener on ESCAPE
    createOrUpdateKursortDialog
        .getContentPane()
        .registerKeyboardAction(
            escapeActionListener,
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
            JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    initializeErrLbls();
  }

  public void addButtonSpeichernActionListener(ActionListener actionListener) {
    buttonSpeichern.addActionListener(actionListener);
  }

  public void addButtonAbbrechenActionListener(ActionListener actionListener) {
    buttonAbbrechen.addActionListener(actionListener);
  }

  public void setButtonSpeichernEnabled(boolean enabled) {
    buttonSpeichern.setEnabled(enabled);
  }

  public void setButtonSpeichernToolTipText(String text) {
    buttonSpeichern.setToolTipText(text);
  }

  public void setButtonSpeichernFocusPainted(boolean focusPainted) {
    buttonSpeichern.setFocusPainted(focusPainted);
  }

  public void showDialog() {
    createOrUpdateKursortDialog.pack();
    createOrUpdateKursortDialog.setVisible(true);
  }

  public void showErrorMessageDialog(String message, String title) {
    JOptionPane.showMessageDialog(
        createOrUpdateKursortDialog, message, title, JOptionPane.ERROR_MESSAGE);
  }

  public void dispose() {
    createOrUpdateKursortDialog.dispose();
  }

  public void addWindowListener(WindowListener windowListener) {
    createOrUpdateKursortDialog.addWindowListener(windowListener);
  }

  public void setErrLblBezeichnungVisible(boolean visible) {
    errLblBezeichnung.setVisible(visible);
  }

  public void setErrLblBezeichnungText(String text) {
    errLblBezeichnung.setText(text);
  }

  public void addTxtBezeichnungActionListener(ActionListener actionListener) {
    if (!DEFAULT_BUTTON_ENABLED) {
      txtBezeichnung.addActionListener(actionListener);
    }
  }

  public void addTxtBezeichnungFocusListener(FocusListener focusListener) {
    txtBezeichnung.addFocusListener(focusListener);
  }

  public String getTxtBezeichnungText() {
    return txtBezeichnung.getText();
  }

  public void setTxtBezeichnungText(String text) {
    txtBezeichnung.setText(text);
  }

  public void setTxtBezeichnungToolTipText(String text) {
    txtBezeichnung.setToolTipText(text);
  }

  public boolean isTxtBezeichnungEnabled() {
    return txtBezeichnung.isEnabled();
  }

  public void addCheckBoxSelektierbarItemListener(ItemListener itemListener) {
    checkBoxSelektierbar.addItemListener(itemListener);
  }

  public boolean isCheckBoxSelektierbarSelected() {
    return checkBoxSelektierbar.isSelected();
  }

  public void setCheckBoxSelektierbarSelected(boolean selected) {
    checkBoxSelektierbar.setSelected(selected);
  }

  private void initializeErrLbls() {
    errLblBezeichnung.setVisible(false);
    errLblBezeichnung.setForeground(Color.RED);
  }
}
