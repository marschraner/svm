package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CallDefaultEmailClientCommand;
import ch.metzenthin.svm.domain.model.EmailSemesterrechnungenModel;
import ch.metzenthin.svm.ui.componentmodel.SemesterrechnungenTableModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.util.Set;

/**
 * @author Martin Schraner
 */
public class EmailSemesterrechnungenController extends AbstractController {

    private static final Logger LOGGER = LogManager.getLogger(EmailSemesterrechnungenController.class);

    // Möglichkeit zum Umschalten des validation modes (nicht dynamisch)
    private static final boolean MODEL_VALIDATION_MODE = false;

    private final EmailSemesterrechnungenModel emailSemesterrechnungenModel;
    private final SvmContext svmContext;
    private final SemesterrechnungenTableModel semesterrechnungenTableModel;
    private JDialog emailDialog;
    private JCheckBox checkBoxMutterUndOderVater;
    private JCheckBox checkBoxRechnungsempfaenger;
    private JCheckBox checkBoxBlindkopien;
    private JButton btnOk;

    public EmailSemesterrechnungenController(EmailSemesterrechnungenModel emailSemesterrechnungenModel,
                                             SvmContext svmContext, SemesterrechnungenTableModel semesterrechnungenTableModel) {
        super(emailSemesterrechnungenModel);
        this.emailSemesterrechnungenModel = emailSemesterrechnungenModel;
        this.svmContext = svmContext;
        this.semesterrechnungenTableModel = semesterrechnungenTableModel;
        this.emailSemesterrechnungenModel.addPropertyChangeListener(this);
        this.emailSemesterrechnungenModel.addDisableFieldsListener(this);
        this.emailSemesterrechnungenModel.addMakeErrorLabelsInvisibleListener(this);
        this.setModelValidationMode(MODEL_VALIDATION_MODE);
    }

    public void constructionDone() {
        // Initialisierung
        emailSemesterrechnungenModel.setMutterUndOderVaterSelected(true);
        emailSemesterrechnungenModel.setRechnungsempfaengerSelected(false);
        emailSemesterrechnungenModel.setBlindkopien(true);
        emailSemesterrechnungenModel.initializeCompleted();
    }

    public void setEmailSemesterrechnungenDialog(JDialog emailDialog) {
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

    public void setCheckBoxMutterUndOderVater(JCheckBox checkBoxMutterUndOderVater) {
        this.checkBoxMutterUndOderVater = checkBoxMutterUndOderVater;
        this.checkBoxMutterUndOderVater.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                onMutterUndOderVaterEvent();
            }
        });
    }

    private void onMutterUndOderVaterEvent() {
        LOGGER.trace("EmailSemesterrechnungenController Event MutterUndOderVater. Selected=" + checkBoxMutterUndOderVater.isSelected());
        setModelMutterUndOderVater();
    }

    private void setModelMutterUndOderVater() {
        emailSemesterrechnungenModel.setMutterUndOderVaterSelected(checkBoxMutterUndOderVater.isSelected());
    }

    public void setCheckBoxRechnungsempfaenger(JCheckBox checkBoxRechnungsempfaenger) {
        this.checkBoxRechnungsempfaenger = checkBoxRechnungsempfaenger;
        this.checkBoxRechnungsempfaenger.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                onRechnungsempfaengerEvent();
            }
        });
    }

    private void onRechnungsempfaengerEvent() {
        LOGGER.trace("EmailSemesterrechnungenController Event Rechnungsempfaenger. Selected=" + checkBoxRechnungsempfaenger.isSelected());
        setModelRechnungsempfaenger();
    }

    private void setModelRechnungsempfaenger() {
        emailSemesterrechnungenModel.setRechnungsempfaengerSelected(checkBoxRechnungsempfaenger.isSelected());
    }

    public void setCheckBoxBlindkopien(JCheckBox checkBoxBlindkopien) {
        this.checkBoxBlindkopien = checkBoxBlindkopien;
        this.checkBoxBlindkopien.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                onBlindkopienEvent();
            }
        });
    }

    private void onBlindkopienEvent() {
        LOGGER.trace("EmailSemesterrechnungenController Event Blindkopien. Selected=" + checkBoxBlindkopien.isSelected());
        setModelBlindkopien();
    }

    private void setModelBlindkopien() {
        emailSemesterrechnungenModel.setBlindkopien(checkBoxBlindkopien.isSelected());
    }

    public void setBtnOk(JButton btnOk) {
        this.btnOk = btnOk;
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
        CallDefaultEmailClientCommand.Result result = emailSemesterrechnungenModel.callEmailClient(semesterrechnungenTableModel);
        if (result == CallDefaultEmailClientCommand.Result.FEHLER_BEIM_AUFRUF_DES_EMAIL_CLIENT) {
            JOptionPane.showMessageDialog(emailDialog, "Beim Aufruf des Email-Client ist ein Fehler aufgetreten.", "Fehler", JOptionPane.ERROR_MESSAGE, svmContext.getDialogIcons().getErrorIcon());
        }
        if (!emailSemesterrechnungenModel.getFehlendeEmailAdressen().isEmpty()) {
            StringBuilder fehlend = new StringBuilder();
            for (String emailAdresse : emailSemesterrechnungenModel.getFehlendeEmailAdressen()) {
                fehlend.append(emailAdresse);
                fehlend.append("\n");
            }
            fehlend.setLength(fehlend.length() - 1);
            JOptionPane.showMessageDialog(emailDialog, "Für folgende Semesterrechnung(en) ist keine E-Mail-Adresse erfasst:\n" + fehlend, "Warnung", JOptionPane.WARNING_MESSAGE, svmContext.getDialogIcons().getWarningIcon());
        }
        if (!emailSemesterrechnungenModel.getUngueltigeEmailAdressen().isEmpty()) {
            StringBuilder ungueltig = new StringBuilder();
            for (String emailAdresse : emailSemesterrechnungenModel.getUngueltigeEmailAdressen()) {
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
        if (checkIsFieldChange(Field.RECHNUNGSEMPFAENGER, evt)) {
            checkBoxRechnungsempfaenger.setSelected(emailSemesterrechnungenModel.isRechnungsempfaengerSelected());
        } else if (checkIsFieldChange(Field.MUTTER_ODER_VATER, evt)) {
            checkBoxMutterUndOderVater.setSelected(emailSemesterrechnungenModel.isMutterUndOderVaterSelected());
        } else if (checkIsFieldChange(Field.BLINDKOPIEN, evt)) {
            checkBoxBlindkopien.setSelected(emailSemesterrechnungenModel.isBlindkopien());
        }
        enableOrDisableOkButton();
    }

    private void enableOrDisableOkButton() {
        btnOk.setEnabled(checkBoxRechnungsempfaenger.isSelected() || checkBoxMutterUndOderVater.isSelected());
    }

    @Override
    void validateFields() throws SvmValidationException {}

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
    public void disableFields(boolean disable, Set<Field> fields) {}

}
