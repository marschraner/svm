package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.dataTypes.EmailSchuelerListeEmpfaengerGruppe;
import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CallDefaultEmailClientCommand;
import ch.metzenthin.svm.domain.model.EmailSchuelerListeModel;
import ch.metzenthin.svm.ui.componentmodel.SchuelerSuchenTableModel;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.util.Set;

import static ch.metzenthin.svm.common.utils.SimpleValidator.equalsNullSafe;

/**
 * @author Martin Schraner
 */
public class EmailSchuelerListeController extends AbstractController {

    private static final Logger LOGGER = Logger.getLogger(EmailSchuelerListeController.class);

    // Möglichkeit zum Umschalten des validation modes (nicht dynamisch)
    private static final boolean MODEL_VALIDATION_MODE = false;

    private final EmailSchuelerListeModel emailSchuelerListeModel;
    private final SvmContext svmContext;
    private final SchuelerSuchenTableModel schuelerSuchenTableModel;
    private JDialog emailDialog;
    private JCheckBox checkBoxBlindkopien;
    private JComboBox<EmailSchuelerListeEmpfaengerGruppe> comboBoxEmailSchuelerListeEmpfaengerGruppe;
    private JButton btnOk;
    private EmailSchuelerListeEmpfaengerGruppe[] selectableEmailSchuelerListeEmpfaengerGruppen;

    public EmailSchuelerListeController(EmailSchuelerListeModel emailSchuelerListeModel, SvmContext svmContext, SchuelerSuchenTableModel schuelerSuchenTableModel) {
        super(emailSchuelerListeModel);
        this.emailSchuelerListeModel = emailSchuelerListeModel;
        this.svmContext = svmContext;
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
        emailDialog.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onAbbrechen();
            }
        });
    }

    public void setContentPane(JPanel contentPane) {
        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onAbbrechen();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public void setComboBoxEmailSchuelerListeEmpfaengerGruppe(JComboBox<EmailSchuelerListeEmpfaengerGruppe> comboBoxEmailSchuelerListeEmpfaengerGruppe) {
        this.comboBoxEmailSchuelerListeEmpfaengerGruppe = comboBoxEmailSchuelerListeEmpfaengerGruppe;
        selectableEmailSchuelerListeEmpfaengerGruppen = emailSchuelerListeModel.getSelectableEmailSchuelerListeEmpfaengerGruppen(schuelerSuchenTableModel);
        comboBoxEmailSchuelerListeEmpfaengerGruppe.setModel(new DefaultComboBoxModel<>(selectableEmailSchuelerListeEmpfaengerGruppen));
        // Model initialisieren mit erstem ComboBox-Wert
        if (selectableEmailSchuelerListeEmpfaengerGruppen.length > 0) {
            emailSchuelerListeModel.setEmailSchuelerListeEmpfaengerGruppe(selectableEmailSchuelerListeEmpfaengerGruppen[0]);
        }
        comboBoxEmailSchuelerListeEmpfaengerGruppe.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onEmailSchuelerListeEmpfaengerGruppeSelected();
            }
        });
    }

    private void onEmailSchuelerListeEmpfaengerGruppeSelected() {
        LOGGER.trace("EmailSchuelerListeController Event SchuelerListeEmpfaengerGruppe selected=" + comboBoxEmailSchuelerListeEmpfaengerGruppe.getSelectedItem());
        boolean equalFieldAndModelValue = equalsNullSafe(comboBoxEmailSchuelerListeEmpfaengerGruppe.getSelectedItem(), emailSchuelerListeModel.getEmailSchuelerListeEmpfaengerGruppe());
        setModelEmailSchuelerListeEmpfaengerGruppe();
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelEmailSchuelerListeEmpfaengerGruppe() {
        emailSchuelerListeModel.setEmailSchuelerListeEmpfaengerGruppe((EmailSchuelerListeEmpfaengerGruppe) comboBoxEmailSchuelerListeEmpfaengerGruppe.getSelectedItem());
    }

    public void setCheckBoxBlindkopien(JCheckBox checkBoxBlindkopien) {
        this.checkBoxBlindkopien = checkBoxBlindkopien;
        this.checkBoxBlindkopien.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                onBlindkopienEvent();
            }
        });
        // Initialisierung
        emailSchuelerListeModel.setBlindkopien(true);
    }

    private void onBlindkopienEvent() {
        LOGGER.trace("EmailSchuelerListeController Event Blindkopien. Selected=" + checkBoxBlindkopien.isSelected());
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
        this.btnOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onOk();
            }
        });
    }

    @SuppressWarnings("DuplicatedCode")
    private void onOk() {
        if (!isModelValidationMode() && !validateOnSpeichern()) {
            btnOk.setFocusPainted(false);
            return;
        }
        CallDefaultEmailClientCommand.Result result = emailSchuelerListeModel.callEmailClient(schuelerSuchenTableModel);
        if (result == CallDefaultEmailClientCommand.Result.FEHLER_BEIM_AUFRUF_DES_EMAIL_CLIENT) {
            JOptionPane.showMessageDialog(emailDialog, "Beim Aufruf des Email-Client ist ein Fehler aufgetreten.", "Fehler", JOptionPane.ERROR_MESSAGE, svmContext.getDialogIcons().getErrorIcon());
        }
        if (!emailSchuelerListeModel.getFehlendeEmailAdressen().isEmpty()) {
            StringBuilder fehlend = new StringBuilder();
            for (String emailAdresse : emailSchuelerListeModel.getFehlendeEmailAdressen()) {
                fehlend.append(emailAdresse);
                fehlend.append("\n");
            }
            fehlend.setLength(fehlend.length() - 1);
            JOptionPane.showMessageDialog(emailDialog, "Für folgende(n) Schüler (resp. dessen/deren Eltern) ist keine E-Mail-Adresse erfasst:\n" + fehlend, "Warnung", JOptionPane.WARNING_MESSAGE, svmContext.getDialogIcons().getWarningIcon());
        }
        if (!emailSchuelerListeModel.getUngueltigeEmailAdressen().isEmpty()) {
            StringBuilder ungueltig = new StringBuilder();
            for (String emailAdresse : emailSchuelerListeModel.getUngueltigeEmailAdressen()) {
                ungueltig.append(emailAdresse);
                ungueltig.append("\n");
            }
            ungueltig.setLength(ungueltig.length() - 1);
            JOptionPane.showMessageDialog(emailDialog, "Die folgende(n) E-Mail-Adresse(n) ist/sind ungültig und wurde(n) ignoriert:\n" + ungueltig, "Warnung", JOptionPane.WARNING_MESSAGE, svmContext.getDialogIcons().getWarningIcon());
        }
        emailDialog.dispose();
    }

    public void setBtnAbbrechen(JButton btnAbbrechen) {
        btnAbbrechen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onAbbrechen();
            }
        });
    }

    private void onAbbrechen() {
        emailDialog.dispose();
    }

    @Override
    void doPropertyChange(PropertyChangeEvent evt) {
        super.doPropertyChange(evt);
        if (checkIsFieldChange(Field.EMAIL_SCHUELER_LISTE_EMPFAENGER_GRUPPE, evt)) {
            comboBoxEmailSchuelerListeEmpfaengerGruppe.setSelectedItem(emailSchuelerListeModel.getEmailSchuelerListeEmpfaengerGruppe());
        } else if (checkIsFieldChange(Field.BLINDKOPIEN, evt)) {
            checkBoxBlindkopien.setSelected(emailSchuelerListeModel.isBlindkopien());
        }
    }

    @Override
    void validateFields() throws SvmValidationException {}

    @Override
    void showErrMsg(SvmValidationException e) {}

    @Override
    void showErrMsgAsToolTip(SvmValidationException e) {}

    @Override
    public void makeErrorLabelsInvisible(Set<Field> fields) {}

    @Override
    public void disableFields(boolean disable, Set<Field> fields) {}

}
