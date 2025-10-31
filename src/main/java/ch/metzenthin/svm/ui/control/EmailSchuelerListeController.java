package ch.metzenthin.svm.ui.control;

import static ch.metzenthin.svm.common.utils.SimpleValidator.equalsNullSafe;

import ch.metzenthin.svm.common.datatypes.EmailSchuelerListeEmpfaengerGruppe;
import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CallDefaultEmailClientCommand;
import ch.metzenthin.svm.domain.model.EmailSchuelerListeModel;
import ch.metzenthin.svm.ui.componentmodel.SchuelerSuchenTableModel;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.util.Set;
import javax.swing.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Martin Schraner
 */
public class EmailSchuelerListeController extends AbstractController {

  private static final Logger LOGGER = LogManager.getLogger(EmailSchuelerListeController.class);

  // Möglichkeit zum Umschalten des validation modes (nicht dynamisch)
  private static final boolean MODEL_VALIDATION_MODE = false;

  private final EmailSchuelerListeModel emailSchuelerListeModel;
  private final SchuelerSuchenTableModel schuelerSuchenTableModel;
  private JDialog emailDialog;
  private JCheckBox checkBoxBlindkopien;
  private JComboBox<EmailSchuelerListeEmpfaengerGruppe> comboBoxEmailSchuelerListeEmpfaengerGruppe;
  private JButton btnOk;
  private EmailSchuelerListeEmpfaengerGruppe[] selectableEmailSchuelerListeEmpfaengerGruppen;

  public EmailSchuelerListeController(
      EmailSchuelerListeModel emailSchuelerListeModel,
      SchuelerSuchenTableModel schuelerSuchenTableModel) {
    super(emailSchuelerListeModel);
    this.emailSchuelerListeModel = emailSchuelerListeModel;
    this.schuelerSuchenTableModel = schuelerSuchenTableModel;
    this.emailSchuelerListeModel.addPropertyChangeListener(this);
    this.emailSchuelerListeModel.addDisableFieldsListener(this);
    this.emailSchuelerListeModel.addMakeErrorLabelsInvisibleListener(this);
    this.setModelValidationMode(MODEL_VALIDATION_MODE);
  }

  public void constructionDone() {
    emailSchuelerListeModel.initializeCompleted();
  }

  public void setEmailSchuelerListeDialog(JDialog emailDialog) {
    // call onCancel() when cross is clicked
    this.emailDialog = emailDialog;
    emailDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    emailDialog.addWindowListener(
        new WindowAdapter() {
          @Override
          public void windowClosing(WindowEvent e) {
            onAbbrechen();
          }
        });
  }

  public void setContentPane(JPanel contentPane) {
    // call onCancel() on ESCAPE
    contentPane.registerKeyboardAction(
        e -> onAbbrechen(),
        KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
        JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
  }

  public void setComboBoxEmailSchuelerListeEmpfaengerGruppe(
      JComboBox<EmailSchuelerListeEmpfaengerGruppe> comboBoxEmailSchuelerListeEmpfaengerGruppe) {
    this.comboBoxEmailSchuelerListeEmpfaengerGruppe = comboBoxEmailSchuelerListeEmpfaengerGruppe;
    selectableEmailSchuelerListeEmpfaengerGruppen =
        emailSchuelerListeModel.getSelectableEmailSchuelerListeEmpfaengerGruppen(
            schuelerSuchenTableModel);
    comboBoxEmailSchuelerListeEmpfaengerGruppe.setModel(
        new DefaultComboBoxModel<>(selectableEmailSchuelerListeEmpfaengerGruppen));
    // Model initialisieren mit erstem ComboBox-Wert
    if (selectableEmailSchuelerListeEmpfaengerGruppen.length > 0) {
      emailSchuelerListeModel.setEmailSchuelerListeEmpfaengerGruppe(
          selectableEmailSchuelerListeEmpfaengerGruppen[0]);
    }
    comboBoxEmailSchuelerListeEmpfaengerGruppe.addActionListener(
        e -> onEmailSchuelerListeEmpfaengerGruppeSelected());
  }

  private void onEmailSchuelerListeEmpfaengerGruppeSelected() {
    LOGGER.trace(
        "EmailSchuelerListeController Event SchuelerListeEmpfaengerGruppe selected={}",
        comboBoxEmailSchuelerListeEmpfaengerGruppe.getSelectedItem());
    boolean equalFieldAndModelValue =
        equalsNullSafe(
            comboBoxEmailSchuelerListeEmpfaengerGruppe.getSelectedItem(),
            emailSchuelerListeModel.getEmailSchuelerListeEmpfaengerGruppe());
    setModelEmailSchuelerListeEmpfaengerGruppe();
    if (equalFieldAndModelValue && isModelValidationMode()) {
      // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb
      // muss hier die Validierung angestossen werden.
      LOGGER.trace("Validierung wegen equalFieldAndModelValue");
      validate();
    }
  }

  private void setModelEmailSchuelerListeEmpfaengerGruppe() {
    emailSchuelerListeModel.setEmailSchuelerListeEmpfaengerGruppe(
        (EmailSchuelerListeEmpfaengerGruppe)
            comboBoxEmailSchuelerListeEmpfaengerGruppe.getSelectedItem());
  }

  public void setCheckBoxBlindkopien(JCheckBox checkBoxBlindkopien) {
    this.checkBoxBlindkopien = checkBoxBlindkopien;
    this.checkBoxBlindkopien.addItemListener(e -> onBlindkopienEvent());
    // Initialisierung
    emailSchuelerListeModel.setBlindkopien(true);
  }

  private void onBlindkopienEvent() {
    LOGGER.trace(
        "EmailSchuelerListeController Event Blindkopien. Selected={}",
        checkBoxBlindkopien.isSelected());
    setModelBlindkopien();
  }

  private void setModelBlindkopien() {
    emailSchuelerListeModel.setBlindkopien(checkBoxBlindkopien.isSelected());
  }

  public void setBtnOk(JButton btnOk) {
    this.btnOk = btnOk;
    if (selectableEmailSchuelerListeEmpfaengerGruppen.length == 0) {
      btnOk.setEnabled(false);
      return;
    }
    if (isModelValidationMode()) {
      btnOk.setEnabled(false);
    }
    this.btnOk.addActionListener(e -> onOk());
  }

  @SuppressWarnings("DuplicatedCode")
  private void onOk() {
    if (!isModelValidationMode() && !validateOnSpeichern()) {
      btnOk.setFocusPainted(false);
      return;
    }
    CallDefaultEmailClientCommand.Result result =
        emailSchuelerListeModel.callEmailClient(schuelerSuchenTableModel);
    if (result == CallDefaultEmailClientCommand.Result.FEHLER_BEIM_AUFRUF_DES_EMAIL_CLIENT) {
      JOptionPane.showMessageDialog(
          emailDialog,
          "Beim Aufruf des Email-Client ist ein Fehler aufgetreten.",
          "Fehler",
          JOptionPane.ERROR_MESSAGE);
    }
    if (!emailSchuelerListeModel.getFehlendeEmailAdressen().isEmpty()) {
      StringBuilder fehlend = new StringBuilder();
      for (String emailAdresse : emailSchuelerListeModel.getFehlendeEmailAdressen()) {
        fehlend.append(emailAdresse);
        fehlend.append("\n");
      }
      fehlend.setLength(fehlend.length() - 1);
      JOptionPane.showMessageDialog(
          emailDialog,
          "Für folgende(n) Schüler (resp. dessen/deren Eltern) ist keine E-Mail-Adresse erfasst:\n"
              + fehlend,
          "Warnung",
          JOptionPane.WARNING_MESSAGE);
    }
    if (!emailSchuelerListeModel.getUngueltigeEmailAdressen().isEmpty()) {
      StringBuilder ungueltig = new StringBuilder();
      for (String emailAdresse : emailSchuelerListeModel.getUngueltigeEmailAdressen()) {
        ungueltig.append(emailAdresse);
        ungueltig.append("\n");
      }
      ungueltig.setLength(ungueltig.length() - 1);
      JOptionPane.showMessageDialog(
          emailDialog,
          "Die folgende(n) E-Mail-Adresse(n) ist/sind ungültig und wurde(n) ignoriert:\n"
              + ungueltig,
          "Warnung",
          JOptionPane.WARNING_MESSAGE);
    }
    emailDialog.dispose();
  }

  public void setBtnAbbrechen(JButton btnAbbrechen) {
    btnAbbrechen.addActionListener(e -> onAbbrechen());
  }

  private void onAbbrechen() {
    emailDialog.dispose();
  }

  @Override
  void doPropertyChange(PropertyChangeEvent evt) {
    super.doPropertyChange(evt);
    if (checkIsFieldChange(Field.EMAIL_SCHUELER_LISTE_EMPFAENGER_GRUPPE, evt)) {
      comboBoxEmailSchuelerListeEmpfaengerGruppe.setSelectedItem(
          emailSchuelerListeModel.getEmailSchuelerListeEmpfaengerGruppe());
    } else if (checkIsFieldChange(Field.BLINDKOPIEN, evt)) {
      checkBoxBlindkopien.setSelected(emailSchuelerListeModel.isBlindkopien());
    }
  }

  @Override
  void validateFields() throws SvmValidationException {
    // Keine zu validierenden Felder
  }

  @Override
  void showErrMsg(SvmValidationException e) {
    // Keine Fehlermeldungen
  }

  @Override
  void showErrMsgAsToolTip(SvmValidationException e) {
    // Keine Fehlermeldungen
  }

  @Override
  public void makeErrorLabelsInvisible(Set<Field> fields) {
    // Keine Fehlermeldungen
  }

  @Override
  public void disableFields(boolean disable, Set<Field> fields) {
    // Keine zu deaktivierenden Felder
  }
}
