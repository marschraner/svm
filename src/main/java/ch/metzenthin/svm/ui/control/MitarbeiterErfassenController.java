package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.datatypes.Codetyp;
import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.MitarbeiterErfassenModel;
import ch.metzenthin.svm.ui.componentmodel.CodesTableModel;
import ch.metzenthin.svm.ui.componentmodel.MitarbeitersTableModel;
import ch.metzenthin.svm.ui.components.CodesDialog;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.util.Set;

import static ch.metzenthin.svm.common.utils.SimpleValidator.equalsNullSafe;

/**
 * @author Martin Schraner
 */
@SuppressWarnings("LoggingSimilarMessage")
public class MitarbeiterErfassenController extends PersonController {

    private static final Logger LOGGER = LogManager.getLogger(MitarbeiterErfassenController.class);

    // Möglichkeit zum Umschalten des validation modes (nicht dynamisch)
    private static final boolean MODEL_VALIDATION_MODE = false;

    private final MitarbeitersTableModel mitarbeitersTableModel;
    private final MitarbeiterErfassenModel mitarbeiterErfassenModel;
    private final boolean defaultButtonEnabled;
    private final boolean isBearbeiten;
    private final SvmContext svmContext;
    private JDialog mitarbeiterErfassenDialog;
    private JTextField txtAhvNummer;
    private JTextField txtIbanNummer;
    private JTextArea textAreaVertretungsmoeglichkeiten;
    private JTextArea textAreaBemerkungen;
    private JLabel errLblAhvNummer;
    private JLabel errLblIbanNummer;
    private JLabel errLblVertretungsmoeglichkeiten;
    private JLabel errLblBemerkungen;
    private JCheckBox checkBoxLehrkraft;
    private JCheckBox checkBoxAktiv;
    private JLabel lblCodes;
    private JButton btnCodesBearbeiten;
    private JButton btnSpeichern;

    public MitarbeiterErfassenController(SvmContext svmContext, MitarbeitersTableModel mitarbeitersTableModel, MitarbeiterErfassenModel mitarbeiterErfassenModel, boolean isBearbeiten, boolean defaultButtonEnabled) {
        super(mitarbeiterErfassenModel, defaultButtonEnabled);
        this.svmContext = svmContext;
        this.mitarbeitersTableModel = mitarbeitersTableModel;
        this.mitarbeiterErfassenModel = mitarbeiterErfassenModel;
        this.defaultButtonEnabled = defaultButtonEnabled;
        this.mitarbeiterErfassenModel.addPropertyChangeListener(this);
        this.mitarbeiterErfassenModel.addDisableFieldsListener(this);
        this.mitarbeiterErfassenModel.addMakeErrorLabelsInvisibleListener(this);
        this.mitarbeiterErfassenModel.addCompletedListener(this::onMitarbeiterErfassenModelCompleted);
        this.isBearbeiten = isBearbeiten;
        this.setModelValidationMode(MODEL_VALIDATION_MODE);
    }

    public void constructionDone() {
        mitarbeiterErfassenModel.initializeCompleted();
        updateCodesLabel();
    }

    public void setMitarbeiterErfassenDialog(JDialog mitarbeiterErfassenDialog) {
        // call onCancel() when cross is clicked
        this.mitarbeiterErfassenDialog = mitarbeiterErfassenDialog;
        mitarbeiterErfassenDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        mitarbeiterErfassenDialog.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onAbbrechen();
            }
        });
    }

    public void setContentPane(JPanel contentPane) {
        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onAbbrechen(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public void setTxtAhvNummer(JTextField txtAhvNummer) {
        this.txtAhvNummer = txtAhvNummer;
        if (!defaultButtonEnabled) {
            this.txtAhvNummer.addActionListener(e -> onAhvNummerEvent(true));
        }
        this.txtAhvNummer.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onAhvNummerEvent(false);
            }
        });
    }

    private void onAhvNummerEvent(boolean showRequiredErrMsg) {
        LOGGER.trace("MitarbeiterErfassenController Event AhvNummer");
        boolean equalFieldAndModelValue = equalsNullSafe(txtAhvNummer.getText(), mitarbeiterErfassenModel.getAhvNummer());
        try {
            setModelAhvNummer(showRequiredErrMsg);
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelAhvNummer(boolean showRequiredErrMsg) throws SvmValidationException {
        makeErrorLabelInvisible(Field.AHV_NUMMER);
        try {
            mitarbeiterErfassenModel.setAhvNummer(txtAhvNummer.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("MitarbeiterErfassenController setModelAhvNummer RequiredException={}", e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtAhvNummer.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut, nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("MitarbeiterErfassenController setModelAhvNummer Exception={}", e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtIbanNummer(JTextField txtIbanNummer) {
        this.txtIbanNummer = txtIbanNummer;
        if (!defaultButtonEnabled) {
            this.txtIbanNummer.addActionListener(e -> onIbanNummerEvent(true));
        }
        this.txtIbanNummer.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onIbanNummerEvent(false);
            }
        });
    }

    private void onIbanNummerEvent(boolean showRequiredErrMsg) {
        LOGGER.trace("MitarbeiterErfassenController Event IbanNummer");
        boolean equalFieldAndModelValue = equalsNullSafe(txtIbanNummer.getText(), mitarbeiterErfassenModel.getIbanNummer());
        try {
            setModelIbanNummer(showRequiredErrMsg);
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelIbanNummer(boolean showRequiredErrMsg) throws SvmValidationException {
        makeErrorLabelInvisible(Field.IBAN_NUMMER);
        try {
            mitarbeiterErfassenModel.setIbanNummer(txtIbanNummer.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("MitarbeiterErfassenController setModelIbanNummer RequiredException={}", e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtIbanNummer.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut, nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("MitarbeiterErfassenController setModelIbanNummer Exception={}", e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setCheckBoxLehrkraft(JCheckBox checkBoxLehrkraft) {
        this.checkBoxLehrkraft = checkBoxLehrkraft;
        // keine Lehrkraft als Default-Wert
        if (!isBearbeiten) {
            mitarbeiterErfassenModel.setLehrkraft(false);
        }
        this.checkBoxLehrkraft.addItemListener(e -> onLehrkraftEvent());
    }

    private void setModelLehrkraft() {
        mitarbeiterErfassenModel.setLehrkraft(checkBoxLehrkraft.isSelected());
    }

    private void onLehrkraftEvent() {
        LOGGER.trace("MitarbeiterErfassenController Event Lehrkraft. Selected={}", checkBoxLehrkraft.isSelected());
        setModelLehrkraft();
    }

    public void setTextAreaVertretungsmoeglichkeiten(JTextArea textAreaVertretungsmoeglichkeiten) {
        this.textAreaVertretungsmoeglichkeiten = textAreaVertretungsmoeglichkeiten;
        textAreaVertretungsmoeglichkeiten.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onVertretungsmoeglichkeitenEvent();
            }
        });
        textAreaVertretungsmoeglichkeiten.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, null);
        textAreaVertretungsmoeglichkeiten.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, null);
    }

    private void onVertretungsmoeglichkeitenEvent() {
        LOGGER.trace("MitarbeiterErfassenController Event Vertretungsmoeglichkeiten");
        boolean equalFieldAndModelValue = equalsNullSafe(textAreaVertretungsmoeglichkeiten.getText(), mitarbeiterErfassenModel.getVertretungsmoeglichkeiten());
        try {
            setModelVertretungsmoeglichkeiten();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelVertretungsmoeglichkeiten() throws SvmValidationException {
        makeErrorLabelInvisible(Field.VERTRETUNGSMOEGLICHKEITEN);
        try {
            mitarbeiterErfassenModel.setVertretungsmoeglichkeiten(textAreaVertretungsmoeglichkeiten.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("MitarbeiterErfassenController setModelVertretungsmoeglichkeiten Exception={}", e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTextAreaBemerkungen(JTextArea textAreaBemerkungen) {
        this.textAreaBemerkungen = textAreaBemerkungen;
        textAreaBemerkungen.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onBemerkungenEvent();
            }
        });
        textAreaBemerkungen.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, null);
        textAreaBemerkungen.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, null);
    }

    private void onBemerkungenEvent() {
        LOGGER.trace("MitarbeiterErfassenController Event Bemerkungen");
        boolean equalFieldAndModelValue = equalsNullSafe(textAreaBemerkungen.getText(), mitarbeiterErfassenModel.getBemerkungen());
        try {
            setModelBemerkungen();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelBemerkungen() throws SvmValidationException {
        makeErrorLabelInvisible(Field.BEMERKUNGEN);
        try {
            mitarbeiterErfassenModel.setBemerkungen(textAreaBemerkungen.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("MitarbeiterErfassenController setModelBemerkungen Exception={}", e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setCheckBoxAktiv(JCheckBox checkBoxAktiv) {
        this.checkBoxAktiv = checkBoxAktiv;
        // Aktiv als Default-Wert
        if (!isBearbeiten) {
            mitarbeiterErfassenModel.setAktiv(true);
        }
        this.checkBoxAktiv.addItemListener(e -> onAktivEvent());
    }

    private void setModelAktiv() {
        mitarbeiterErfassenModel.setAktiv(checkBoxAktiv.isSelected());
    }

    private void onAktivEvent() {
        LOGGER.trace("MitarbeiterErfassenController Event Aktiv. Selected={}", checkBoxAktiv.isSelected());
        setModelAktiv();
    }

    public void setLblCodes(JLabel lblCodes) {
        this.lblCodes = lblCodes;
        setLblCodes();
    }

    private void setLblCodes() {
        lblCodes.setText(mitarbeiterErfassenModel.getCodesAsStr());
    }

    public void setErrLblAhvNummer(JLabel errLblAhvNummer) {
        this.errLblAhvNummer = errLblAhvNummer;
    }

    public void setErrLblIbanNummer(JLabel errLblIbanNummer) {
        this.errLblIbanNummer = errLblIbanNummer;
    }

    public void setErrLblVertretungsmoeglichkeiten(JLabel errLblVertretungsmoeglichkeiten) {
        this.errLblVertretungsmoeglichkeiten = errLblVertretungsmoeglichkeiten;
    }

    public void setErrLblBemerkungen(JLabel errLblBemerkungen) {
        this.errLblBemerkungen = errLblBemerkungen;
    }

    public void setBtnCodesBearbeiten(JButton btnCodesBearbeiten) {
        this.btnCodesBearbeiten = btnCodesBearbeiten;
        if (isModelValidationMode()) {
            btnCodesBearbeiten.setEnabled(false);
        }
        btnCodesBearbeiten.addActionListener(e -> onCodesBearbeiten());
    }

    private void onCodesBearbeiten() {
        CodesTableModel codesTableModel = new CodesTableModel(mitarbeiterErfassenModel.getCodesTableData());
        String title = mitarbeiterErfassenModel.getCodesBearbeitenTitle();
        CodesDialog codesDialog = new CodesDialog(svmContext, codesTableModel, mitarbeiterErfassenModel, true, Codetyp.MITARBEITER, title);
        codesDialog.pack();
        codesDialog.setVisible(true);
        btnCodesBearbeiten.setFocusPainted(false);
        updateCodesLabel();
    }

    public void setBtnSpeichern(JButton btnSpeichern) {
        this.btnSpeichern = btnSpeichern;
        if (isModelValidationMode()) {
            btnSpeichern.setEnabled(false);
        }
        this.btnSpeichern.addActionListener(e -> onSpeichern());
    }

    private void onSpeichern() {
        if (!isModelValidationMode() && !validateOnSpeichern()) {
            btnSpeichern.setFocusPainted(false);
            return;
        }
        if (mitarbeiterErfassenModel.checkMitarbeiterBereitsErfasst(svmContext.getSvmModel())) {
            JOptionPane.showMessageDialog(mitarbeiterErfassenDialog, "Der Mitarbeiter ist bereits in der Datenbank gespeichert und kann nicht ein weiteres Mal erfasst werden.", "Fehler", JOptionPane.ERROR_MESSAGE);
            btnSpeichern.setFocusPainted(false);
        } else {
            mitarbeiterErfassenModel.speichern(svmContext.getSvmModel(), mitarbeitersTableModel);
            mitarbeiterErfassenDialog.dispose();
        }
    }

    public void setBtnAbbrechen(JButton btnAbbrechen) {
        btnAbbrechen.addActionListener(e -> onAbbrechen());
    }

    private void onAbbrechen() {
        mitarbeiterErfassenDialog.dispose();
    }

    private void onMitarbeiterErfassenModelCompleted(boolean completed) {
        LOGGER.trace("MitarbeiterErfassenModel completed={}", completed);
        if (completed) {
            btnSpeichern.setToolTipText(null);
            btnSpeichern.setEnabled(true);
        } else {
            btnSpeichern.setToolTipText("Bitte Eingabedaten vervollständigen");
            btnSpeichern.setEnabled(false);
        }
    }

    @Override
    void doPropertyChange(PropertyChangeEvent evt) {
        super.doPropertyChange(evt);
        if (checkIsFieldChange(Field.AHV_NUMMER, evt)) {
            txtAhvNummer.setText(mitarbeiterErfassenModel.getAhvNummer());
        } else if (checkIsFieldChange(Field.IBAN_NUMMER, evt)) {
            txtIbanNummer.setText(mitarbeiterErfassenModel.getIbanNummer());
        } else if (checkIsFieldChange(Field.LEHRKRAFT, evt)) {
            checkBoxLehrkraft.setSelected(mitarbeiterErfassenModel.isLehrkraft());
        } else if (checkIsFieldChange(Field.VERTRETUNGSMOEGLICHKEITEN, evt)) {
            textAreaVertretungsmoeglichkeiten.setText(mitarbeiterErfassenModel.getVertretungsmoeglichkeiten());
        } else if (checkIsFieldChange(Field.AKTIV, evt)) {
            checkBoxAktiv.setSelected(mitarbeiterErfassenModel.isAktiv());
        } else if (checkIsFieldChange(Field.BEMERKUNGEN, evt)) {
            textAreaBemerkungen.setText(mitarbeiterErfassenModel.getBemerkungen());
        }
    }

    private void updateCodesLabel() {
        setLblCodes();
    }

    @Override
    void validateFields() throws SvmValidationException {
        super.validateFields();
        if (txtAhvNummer.isEnabled()) {
            LOGGER.trace("Validate field AhvNummer");
            setModelAhvNummer(true);
        }
        if (txtIbanNummer.isEnabled()) {
            LOGGER.trace("Validate field IbanNummer");
            setModelIbanNummer(true);
        }
        if (textAreaVertretungsmoeglichkeiten != null && textAreaVertretungsmoeglichkeiten.isEnabled()) {
            LOGGER.trace("Validate field Vertretungsmoeglichkeiten");
            setModelVertretungsmoeglichkeiten();
        }
        if (textAreaBemerkungen != null && textAreaBemerkungen.isEnabled()) {
            LOGGER.trace("Validate field Bemerkungen");
            setModelBemerkungen();
        }
    }

    @Override
    void showErrMsg(SvmValidationException e) {
        super.showErrMsg(e);
        if (e.getAffectedFields().contains(Field.AHV_NUMMER)) {
            errLblAhvNummer.setVisible(true);
            errLblAhvNummer.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.IBAN_NUMMER)) {
            errLblIbanNummer.setVisible(true);
            errLblIbanNummer.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.VERTRETUNGSMOEGLICHKEITEN)) {
            errLblVertretungsmoeglichkeiten.setVisible(true);
            errLblVertretungsmoeglichkeiten.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.BEMERKUNGEN)) {
            errLblBemerkungen.setVisible(true);
            errLblBemerkungen.setText(e.getMessage());
        }
    }

    @Override
    void showErrMsgAsToolTip(SvmValidationException e) {
        super.showErrMsgAsToolTip(e);
        if (e.getAffectedFields().contains(Field.AHV_NUMMER)) {
            txtAhvNummer.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.IBAN_NUMMER)) {
            txtIbanNummer.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.VERTRETUNGSMOEGLICHKEITEN)) {
            textAreaVertretungsmoeglichkeiten.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.BEMERKUNGEN)) {
            textAreaBemerkungen.setToolTipText(e.getMessage());
        }
    }

    @Override
    public void makeErrorLabelsInvisible(Set<Field> fields) {
        super.makeErrorLabelsInvisible(fields);
        if (fields.contains(Field.ALLE) || fields.contains(Field.AHV_NUMMER)) {
            errLblAhvNummer.setVisible(false);
            txtAhvNummer.setToolTipText(null);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.IBAN_NUMMER)) {
            errLblIbanNummer.setVisible(false);
            txtIbanNummer.setToolTipText(null);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.VERTRETUNGSMOEGLICHKEITEN)) {
            errLblVertretungsmoeglichkeiten.setVisible(false);
            textAreaVertretungsmoeglichkeiten.setToolTipText(null);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.BEMERKUNGEN)) {
            errLblBemerkungen.setVisible(false);
            textAreaBemerkungen.setToolTipText(null);
        }
    }

    @Override
    public void disableFields(boolean disable, Set<Field> fields) {
    }

}
