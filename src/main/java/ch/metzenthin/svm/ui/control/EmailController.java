package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.dataTypes.EmailEmpfaenger;
import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CallDefaultEmailClientCommand;
import ch.metzenthin.svm.domain.model.EmailModel;
import ch.metzenthin.svm.domain.model.SchuelerDatenblattModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.util.Set;

import static ch.metzenthin.svm.common.utils.SimpleValidator.equalsNullSafe;

/**
 * @author Martin Schraner
 */
public class EmailController extends AbstractController {

    private static final Logger LOGGER = LogManager.getLogger(EmailController.class);

    // Möglichkeit zum Umschalten des validation modes (nicht dynamisch)
    private static final boolean MODEL_VALIDATION_MODE = false;

    private final EmailModel emailModel;
    private final SchuelerDatenblattModel schuelerDatenblattModel;
    private JDialog emailDialog;
    private JComboBox<EmailEmpfaenger> comboBoxEmailEmpfaenger;
    private JButton btnOk;
    private EmailEmpfaenger[] selectableEmailEmpfaengers;

    public EmailController(EmailModel emailModel, SchuelerDatenblattModel schuelerDatenblattModel) {
        super(emailModel);
        this.emailModel = emailModel;
        this.schuelerDatenblattModel = schuelerDatenblattModel;
        this.emailModel.addPropertyChangeListener(this);
        this.emailModel.addDisableFieldsListener(this);
        this.emailModel.addMakeErrorLabelsInvisibleListener(this);
        this.emailModel.addCompletedListener(this::onEmailModelCompleted);
        this.setModelValidationMode(MODEL_VALIDATION_MODE);
    }

    public void constructionDone() {
        emailModel.initializeCompleted();
    }

    public void setEmailDialog(JDialog emailDialog) {
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
        contentPane.registerKeyboardAction(e -> onAbbrechen(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public void setComboBoxEmailEmpfaenger(JComboBox<EmailEmpfaenger> comboBoxEmailEmpfaenger) {
        this.comboBoxEmailEmpfaenger = comboBoxEmailEmpfaenger;
        selectableEmailEmpfaengers = emailModel.getSelectableEmailEmpfaengers(schuelerDatenblattModel);
        comboBoxEmailEmpfaenger.setModel(new DefaultComboBoxModel<>(selectableEmailEmpfaengers));
        // Model initialisieren mit erstem ComboBox-Wert
        if (selectableEmailEmpfaengers.length > 0) {
            emailModel.setEmailEmpfaenger(selectableEmailEmpfaengers[0]);
        }
        comboBoxEmailEmpfaenger.addActionListener(e -> onEmailEmpfaengerSelected());
    }

    private void onEmailEmpfaengerSelected() {
        LOGGER.trace("EmailController Event EmailEmpfaenger selected=" + comboBoxEmailEmpfaenger.getSelectedItem());
        boolean equalFieldAndModelValue = equalsNullSafe(comboBoxEmailEmpfaenger.getSelectedItem(), emailModel.getEmailEmpfaenger());
        try {
            setModelEmailEmpfaenger();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelEmailEmpfaenger() throws SvmRequiredException {
        emailModel.setEmailEmpfaenger((EmailEmpfaenger) comboBoxEmailEmpfaenger.getSelectedItem());
    }

    public void setBtnOk(JButton btnOk) {
        this.btnOk = btnOk;
        if (selectableEmailEmpfaengers.length == 0) {
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
        CallDefaultEmailClientCommand.Result result = emailModel.callEmailClient(schuelerDatenblattModel);
        if (result == CallDefaultEmailClientCommand.Result.FEHLER_BEIM_AUFRUF_DES_EMAIL_CLIENT) {
            JOptionPane.showMessageDialog(emailDialog, "Beim Aufruf des Email-Client ist ein Fehler aufgetreten.", "Fehler", JOptionPane.ERROR_MESSAGE);
        }
        emailDialog.dispose();
    }

    public void setBtnAbbrechen(JButton btnAbbrechen) {
        btnAbbrechen.addActionListener(e -> onAbbrechen());
    }

    private void onAbbrechen() {
        emailDialog.dispose();
    }

    private void onEmailModelCompleted(boolean completed) {
        LOGGER.trace("EmailModel completed=" + completed);
        if (completed) {
            btnOk.setToolTipText(null);
            btnOk.setEnabled(true);
        } else {
            btnOk.setToolTipText("Bitte Eingabedaten vervollständigen");
            btnOk.setEnabled(false);
        }
    }

    @Override
    void doPropertyChange(PropertyChangeEvent evt) {
        super.doPropertyChange(evt);
        if (checkIsFieldChange(Field.EMAIL_EMPFAENGER, evt)) {
            comboBoxEmailEmpfaenger.setSelectedItem(emailModel.getEmailEmpfaenger());
        }
    }

    @Override
    void validateFields() throws SvmValidationException {
    }

    @Override
    void showErrMsg(SvmValidationException e) {
    }

    @Override
    void showErrMsgAsToolTip(SvmValidationException e) {
    }

    @Override
    public void makeErrorLabelsInvisible(Set<Field> fields) {
    }

    @Override
    public void disableFields(boolean disable, Set<Field> fields) {
    }

}
