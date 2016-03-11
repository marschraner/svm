package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.CompletedListener;
import ch.metzenthin.svm.domain.model.MaerchenErfassenModel;
import ch.metzenthin.svm.ui.componentmodel.MaerchensTableModel;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.util.Set;

import static ch.metzenthin.svm.common.utils.SimpleValidator.equalsNullSafe;

/**
 * @author Martin Schraner
 */
public class MaerchenErfassenController extends AbstractController {

    private static final Logger LOGGER = Logger.getLogger(MaerchenErfassenController.class);

    // Möglichkeit zum Umschalten des validation modes (nicht dynamisch)
    private static final boolean MODEL_VALIDATION_MODE = false;

    private MaerchensTableModel maerchensTableModel;
    private MaerchenErfassenModel maerchenErfassenModel;
    private final SvmContext svmContext;
    private boolean isBearbeiten;
    private boolean defaultButtonEnabled;
    private JDialog maerchenErfassenDialog;
    private JSpinner spinnerSchuljahre;
    private JTextField txtBezeichnung;
    private JTextField txtAnzahlVorstellungen;
    private JLabel errLblBezeichnung;
    private JLabel errLblAnzahlVorstellungen;
    private JButton btnSpeichern;

    public MaerchenErfassenController(SvmContext svmContext, MaerchensTableModel maerchensTableModel, MaerchenErfassenModel maerchenErfassenModel, boolean isBearbeiten, boolean defaultButtonEnabled) {
        super(maerchenErfassenModel);
        this.svmContext = svmContext;
        this.maerchensTableModel = maerchensTableModel;
        this.maerchenErfassenModel = maerchenErfassenModel;
        this.isBearbeiten = isBearbeiten;
        this.defaultButtonEnabled = defaultButtonEnabled;
        this.maerchenErfassenModel.addPropertyChangeListener(this);
        this.maerchenErfassenModel.addDisableFieldsListener(this);
        this.maerchenErfassenModel.addMakeErrorLabelsInvisibleListener(this);
        this.maerchenErfassenModel.addCompletedListener(new CompletedListener() {
            @Override
            public void completed(boolean completed) {
                onMaerchenErfassenModelCompleted(completed);
            }
        });
        this.setModelValidationMode(MODEL_VALIDATION_MODE);
    }

    public void constructionDone() {
        maerchenErfassenModel.initializeCompleted();
    }

    public void setMaerchenErfassenDialog(JDialog maerchenErfassenDialog) {
        // call onCancel() when cross is clicked
        this.maerchenErfassenDialog = maerchenErfassenDialog;
        maerchenErfassenDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        maerchenErfassenDialog.addWindowListener(new WindowAdapter() {
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

    public void setSpinnerSchuljahre(JSpinner spinnerSchuljahre) {
        this.spinnerSchuljahre = spinnerSchuljahre;
        spinnerSchuljahre.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                onSchuljahrSelected();
            }
        });
        if (!isBearbeiten) {
            initSchuljahr();
        }
    }

    private void initSchuljahr() {
        String initSchuljahr = maerchenErfassenModel.getNaechstesNochNichtErfasstesSchuljahrMaerchen(svmContext.getSvmModel());
        try {
            maerchenErfassenModel.setSchuljahr(initSchuljahr);
        } catch (SvmValidationException ignore) {
        }
    }

    private void onSchuljahrSelected() {
        LOGGER.trace("PersonController Event Schuljahre selected =" + spinnerSchuljahre.getValue());
        boolean equalFieldAndModelValue = equalsNullSafe(spinnerSchuljahre.getValue(), maerchenErfassenModel.getSchuljahr());
        try {
            setModelSchuljahr();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelSchuljahr() throws SvmValidationException {
        makeErrorLabelInvisible(Field.SCHULJAHR);
        try {
            maerchenErfassenModel.setSchuljahr((String) spinnerSchuljahre.getValue());
        } catch (SvmValidationException e) {
            LOGGER.trace("PersonController setModelSchuljahr Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtBezeichnung(JTextField txtBezeichnung) {
        this.txtBezeichnung = txtBezeichnung;
        if (!defaultButtonEnabled) {
            this.txtBezeichnung.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    onBezeichnungEvent(true);
                }
            });
        }
        this.txtBezeichnung.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onBezeichnungEvent(false);
            }
        });
    }

    private void onBezeichnungEvent(boolean showRequiredErrMsg) {
        LOGGER.trace("MaerchenErfassenController Event Bezeichnung");
        boolean equalFieldAndModelValue = equalsNullSafe(txtBezeichnung.getText(), maerchenErfassenModel.getBezeichnung());
        try {
            setModelBezeichnung(showRequiredErrMsg);
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelBezeichnung(boolean showRequiredErrMsg) throws SvmValidationException {
        makeErrorLabelInvisible(Field.BEZEICHNUNG);
        try {
            maerchenErfassenModel.setBezeichnung(txtBezeichnung.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("MaerchenErfassenController setModelBezeichnung RequiredException=" + e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtBezeichnung.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("MaerchenErfassenController setModelBezeichnung Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtAnzahlVorstellungen(JTextField txtAnzahlVorstellungen) {
        this.txtAnzahlVorstellungen = txtAnzahlVorstellungen;
        if (!defaultButtonEnabled) {
            this.txtAnzahlVorstellungen.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    onAnzahlVorstellungenEvent(true);
                }
            });
        }
        this.txtAnzahlVorstellungen.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onAnzahlVorstellungenEvent(false);
            }
        });
    }

    private void onAnzahlVorstellungenEvent(boolean showRequiredErrMsg) {
        LOGGER.trace("MaerchenErfassenController Event AnzahlVorstellungen");
        boolean equalFieldAndModelValue = equalsNullSafe(txtAnzahlVorstellungen.getText(), maerchenErfassenModel.getAnzahlVorstellungen());
        try {
            setModelAnzahlVorstellungen(showRequiredErrMsg);
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelAnzahlVorstellungen(boolean showRequiredErrMsg) throws SvmValidationException {
        makeErrorLabelInvisible(Field.ANZAHL_VORSTELLUNGEN);
        try {
            maerchenErfassenModel.setAnzahlVorstellungen(txtAnzahlVorstellungen.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("MaerchenErfassenController setModelAnzahlVorstellungen RequiredException=" + e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtAnzahlVorstellungen.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("MaerchenErfassenController setModelAnzahlVorstellungen Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setErrLblBezeichnung(JLabel errLblBezeichnung) {
        this.errLblBezeichnung = errLblBezeichnung;
    }

    public void setErrLblAnzahlVorstellungen(JLabel errLblAnzahlVorstellungen) {
        this.errLblAnzahlVorstellungen = errLblAnzahlVorstellungen;
    }

    public void setBtnSpeichern(JButton btnSpeichern) {
        this.btnSpeichern = btnSpeichern;
        if (isModelValidationMode()) {
            btnSpeichern.setEnabled(false);
        }
        this.btnSpeichern.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSpeichern();
            }
        });
    }

    private void onSpeichern() {
        if (!isModelValidationMode() && !validateOnSpeichern()) {
            btnSpeichern.setFocusPainted(false);
            return;
        }
        if (maerchenErfassenModel.checkMaerchenBereitsErfasst(svmContext.getSvmModel())) {
            JOptionPane.showMessageDialog(maerchenErfassenDialog, "Märchen für Schuljahr " + maerchenErfassenModel.getMaerchen().getSchuljahr() + " bereits erfasst.", "Fehler", JOptionPane.ERROR_MESSAGE, svmContext.getDialogIcons().getErrorIcon());
            btnSpeichern.setFocusPainted(false);
        } else {
            if (maerchenErfassenModel.checkIfMaerchenIsInPast()) {
                Object[] options = {"Ignorieren", "Abbrechen"};
                int n = JOptionPane.showOptionDialog(
                        null,
                        "Das selektierte Schuljahr liegt in der Vergangenheit. Märchen trotzdem speichern?",
                        "Schuljahr in Vergangenheit",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        svmContext.getDialogIcons().getWarningIcon(),
                        options,  //the titles of buttons
                        options[0]); //default button title
                if (n == 1) {
                    return;
                }
            }
            maerchenErfassenModel.speichern(svmContext.getSvmModel(), maerchensTableModel);
            maerchenErfassenDialog.dispose();
        }
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
        maerchenErfassenDialog.dispose();
    }

    private void onMaerchenErfassenModelCompleted(boolean completed) {
        LOGGER.trace("MaerchenErfassenModel completed=" + completed);
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
        if (checkIsFieldChange(Field.SCHULJAHR, evt)) {
            spinnerSchuljahre.setValue(maerchenErfassenModel.getSchuljahr());
        } else if (checkIsFieldChange(Field.BEZEICHNUNG, evt)) {
            txtBezeichnung.setText(maerchenErfassenModel.getBezeichnung());
        } else if (checkIsFieldChange(Field.ANZAHL_VORSTELLUNGEN, evt)) {
            txtAnzahlVorstellungen.setText(Integer.toString(maerchenErfassenModel.getAnzahlVorstellungen()));
        }
    }

    @Override
    void validateFields() throws SvmValidationException {
        if (txtBezeichnung.isEnabled()) {
            LOGGER.trace("Validate field Bezeichnung");
            setModelBezeichnung(true);
        }
        if (txtBezeichnung.isEnabled()) {
            LOGGER.trace("Validate field Anzahl Vorstellungen");
            setModelAnzahlVorstellungen(true);
        }
    }

    @Override
    void showErrMsg(SvmValidationException e) {
        if (e.getAffectedFields().contains(Field.BEZEICHNUNG)) {
            errLblBezeichnung.setVisible(true);
            errLblBezeichnung.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ANZAHL_VORSTELLUNGEN)) {
            errLblAnzahlVorstellungen.setVisible(true);
            errLblAnzahlVorstellungen.setText(e.getMessage());
        }
    }

    @Override
    void showErrMsgAsToolTip(SvmValidationException e) {
        if (e.getAffectedFields().contains(Field.BEZEICHNUNG)) {
            txtBezeichnung.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ANZAHL_VORSTELLUNGEN)) {
            txtAnzahlVorstellungen.setToolTipText(e.getMessage());
        }
    }

    @Override
    public void makeErrorLabelsInvisible(Set<Field> fields) {
        if (fields.contains(Field.ALLE) || fields.contains(Field.BEZEICHNUNG)) {
            errLblBezeichnung.setVisible(false);
            txtBezeichnung.setToolTipText(null);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.ANZAHL_VORSTELLUNGEN)) {
            errLblAnzahlVorstellungen.setVisible(false);
            txtAnzahlVorstellungen.setToolTipText(null);
        }
    }

    @Override
    public void disableFields(boolean disable, Set<Field> fields) {}

}
