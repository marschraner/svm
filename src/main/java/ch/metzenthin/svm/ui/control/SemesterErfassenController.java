package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.common.dataTypes.Semesterbezeichnung;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.CompletedListener;
import ch.metzenthin.svm.domain.model.SemesterErfassenModel;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.util.Set;

import static ch.metzenthin.svm.common.utils.Converter.asString;
import static ch.metzenthin.svm.common.utils.SimpleValidator.equalsNullSafe;

/**
 * @author Martin Schraner
 */
public class SemesterErfassenController extends AbstractController {

    private static final Logger LOGGER = Logger.getLogger(SemesterErfassenController.class);

    // Möglichkeit zum Umschalten des validation modes (nicht dynamisch)
    private static final boolean MODEL_VALIDATION_MODE = false;

    private SemesterErfassenModel semesterErfassenModel;
    private final SvmContext svmContext;
    private boolean isBearbeiten;
    private JDialog semesterErfassenDialog;
    private JSpinner spinnerSchuljahre;
    private JComboBox<Semesterbezeichnung> comboBoxSemesterbezeichnung;
    private JTextField txtSemesterbeginn;
    private JTextField txtSemesterende;
    private JTextField txtAnzahlSchulwochen;
    private JLabel errLblSemesterbeginn;
    private JLabel errLblSemesterende;
    private JLabel errLblAnzahlSchulwochen;
    private JButton btnSpeichern;

    public SemesterErfassenController(SvmContext svmContext, SemesterErfassenModel semesterErfassenModel, boolean isBearbeiten) {
        super(semesterErfassenModel);
        this.svmContext = svmContext;
        this.semesterErfassenModel = semesterErfassenModel;
        this.isBearbeiten = isBearbeiten;
        this.semesterErfassenModel.addPropertyChangeListener(this);
        this.semesterErfassenModel.addDisableFieldsListener(this);
        this.semesterErfassenModel.addMakeErrorLabelsInvisibleListener(this);
        this.semesterErfassenModel.addCompletedListener(new CompletedListener() {
            @Override
            public void completed(boolean completed) {
                onSemesterErfassenModelCompleted(completed);
            }
        });
        this.setModelValidationMode(MODEL_VALIDATION_MODE);
    }

    public void constructionDone() {
        semesterErfassenModel.initializeCompleted();
    }

    public void setSemesterErfassenDialog(JDialog semesterErfassenDialog) {
        // call onCancel() when cross is clicked
        this.semesterErfassenDialog = semesterErfassenDialog;
        semesterErfassenDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        semesterErfassenDialog.addWindowListener(new WindowAdapter() {
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
        String schuljahr = semesterErfassenModel.getNaechstesNochNichtErfasstesSemester(svmContext.getSvmModel()).getSchuljahr();
        try {
            semesterErfassenModel.setSchuljahr(schuljahr);
        } catch (SvmValidationException ignore) {
        }
    }

    private void onSchuljahrSelected() {
        LOGGER.trace("PersonController Event Schuljahre selected =" + spinnerSchuljahre.getValue());
        boolean equalFieldAndModelValue = equalsNullSafe(spinnerSchuljahre.getValue(), semesterErfassenModel.getSchuljahr());
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
            semesterErfassenModel.setSchuljahr((String) spinnerSchuljahre.getValue());
        } catch (SvmValidationException e) {
            LOGGER.trace("PersonController setModelSchuljahr Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setComboBoxSemesterbezeichnung(JComboBox<Semesterbezeichnung> comboBoxSemesterbezeichnung) {
        this.comboBoxSemesterbezeichnung = comboBoxSemesterbezeichnung;
        comboBoxSemesterbezeichnung.setModel(new DefaultComboBoxModel<>(Semesterbezeichnung.values()));
        comboBoxSemesterbezeichnung.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSemesterbezeichnungSelected();
            }
        });
        if (!isBearbeiten) {
            initSemesterbezeichnung();
        }
    }

    private void initSemesterbezeichnung() {
        Semesterbezeichnung semesterbezeichnung = semesterErfassenModel.getNaechstesNochNichtErfasstesSemester(svmContext.getSvmModel()).getSemesterbezeichnung();
        try {
            semesterErfassenModel.setSemesterbezeichnung(semesterbezeichnung);
        } catch (SvmValidationException ignore) {
        }
    }

    private void onSemesterbezeichnungSelected() {
        LOGGER.trace("PersonController Event Semesterbezeichnung selected=" + comboBoxSemesterbezeichnung.getSelectedItem());
        boolean equalFieldAndModelValue = equalsNullSafe(comboBoxSemesterbezeichnung.getSelectedItem(), semesterErfassenModel.getSemesterbezeichnung());
        try {
            setModelSemesterbezeichnung();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelSemesterbezeichnung() throws SvmValidationException {
        makeErrorLabelInvisible(Field.SEMESTERBEZEICHNUNG);
        try {
            semesterErfassenModel.setSemesterbezeichnung((Semesterbezeichnung) comboBoxSemesterbezeichnung.getSelectedItem());
        } catch (SvmValidationException e) {
            LOGGER.trace("PersonController setModelSemesterbezeichnung Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtSemesterbeginn(JTextField txtSemesterbeginn) {
        this.txtSemesterbeginn = txtSemesterbeginn;
        this.txtSemesterbeginn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSemesterbeginnEvent(true);
            }
        });
        this.txtSemesterbeginn.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onSemesterbeginnEvent(false);
            }
        });
    }

    private void onSemesterbeginnEvent(boolean showRequiredErrMsg) {
        LOGGER.trace("SemesterErfassenController Event Semesterbeginn");
        boolean equalFieldAndModelValue = equalsNullSafe(txtSemesterbeginn.getText(), semesterErfassenModel.getSemesterbeginn());
        try {
            setModelSemesterbeginn(showRequiredErrMsg);
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelSemesterbeginn(boolean showRequiredErrMsg) throws SvmValidationException {
        makeErrorLabelInvisible(Field.SEMESTERBEGINN);
        try {
            semesterErfassenModel.setSemesterbeginn(txtSemesterbeginn.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("SemesterErfassenController setModelSemesterbeginn RequiredException=" + e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtSemesterbeginn.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("SemesterErfassenController setModelSemesterbeginn Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtSemesterende(JTextField txtSemesterende) {
        this.txtSemesterende = txtSemesterende;
        this.txtSemesterende.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSemesterendeEvent(true);
            }
        });
        this.txtSemesterende.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onSemesterendeEvent(false);
            }
        });
    }

    private void onSemesterendeEvent(boolean showRequiredErrMsg) {
        LOGGER.trace("SemesterErfassenController Event Semesterende");
        boolean equalFieldAndModelValue = equalsNullSafe(txtSemesterende.getText(), semesterErfassenModel.getSemesterende());
        try {
            setModelSemesterende(showRequiredErrMsg);
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelSemesterende(boolean showRequiredErrMsg) throws SvmValidationException {
        makeErrorLabelInvisible(Field.SEMESTERENDE);
        try {
            semesterErfassenModel.setSemesterende(txtSemesterende.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("SemesterErfassenController setModelSemesterende RequiredException=" + e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtSemesterende.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("SemesterErfassenController setModelSemesterende Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtAnzahlSchulwochen(JTextField txtAnzahlSchulwochen) {
        this.txtAnzahlSchulwochen = txtAnzahlSchulwochen;
        this.txtAnzahlSchulwochen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onAnzahlSchulwochenEvent(true);
            }
        });
        this.txtAnzahlSchulwochen.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onAnzahlSchulwochenEvent(false);
            }
        });
    }

    private void onAnzahlSchulwochenEvent(boolean showRequiredErrMsg) {
        LOGGER.trace("SemesterErfassenController Event AnzahlSchulwochen");
        boolean equalFieldAndModelValue = equalsNullSafe(txtAnzahlSchulwochen.getText(), semesterErfassenModel.getAnzahlSchulwochen());
        try {
            setModelAnzahlSchulwochen(showRequiredErrMsg);
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelAnzahlSchulwochen(boolean showRequiredErrMsg) throws SvmValidationException {
        makeErrorLabelInvisible(Field.ANZAHL_SCHULWOCHEN);
        try {
            semesterErfassenModel.setAnzahlSchulwochen(txtAnzahlSchulwochen.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("SemesterErfassenController setModelAnzahlSchulwochen RequiredException=" + e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtAnzahlSchulwochen.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("SemesterErfassenController setModelAnzahlSchulwochen Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setErrLblSemesterbeginn(JLabel errLblSemesterbeginn) {
        this.errLblSemesterbeginn = errLblSemesterbeginn;
    }

    public void setErrLblSemesterende(JLabel errLblSemesterende) {
        this.errLblSemesterende = errLblSemesterende;
    }

    public void setErrLblAnzahlSchulwochen(JLabel errLblAnzahlSchulwochen) {
        this.errLblAnzahlSchulwochen = errLblAnzahlSchulwochen;
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
        if (semesterErfassenModel.checkSemesterBereitsErfasst(svmContext.getSvmModel())) {
            JOptionPane.showMessageDialog(semesterErfassenDialog, semesterErfassenModel.getSemester() + " bereits erfasst.", "Fehler", JOptionPane.ERROR_MESSAGE);
            btnSpeichern.setFocusPainted(false);
        } else {
            if (semesterErfassenModel.checkSemesterUeberlapptAndereSemester(svmContext.getSvmModel())) {
                JOptionPane.showMessageDialog(semesterErfassenDialog, "Semester dürfen sich nicht überlappen.", "Fehler", JOptionPane.ERROR_MESSAGE);
                btnSpeichern.setFocusPainted(false);
            } else {
                semesterErfassenModel.speichern(svmContext.getSvmModel());
                semesterErfassenDialog.dispose();
            }
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
        semesterErfassenDialog.dispose();
    }

    private void onSemesterErfassenModelCompleted(boolean completed) {
        LOGGER.trace("SemesterErfassenModel completed=" + completed);
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
            spinnerSchuljahre.setValue(semesterErfassenModel.getSchuljahr());
        } else if (checkIsFieldChange(Field.SEMESTERBEZEICHNUNG, evt)) {
            comboBoxSemesterbezeichnung.setSelectedItem(semesterErfassenModel.getSemesterbezeichnung());
        } else if (checkIsFieldChange(Field.SEMESTERBEGINN, evt)) {
            txtSemesterbeginn.setText(asString(semesterErfassenModel.getSemesterbeginn()));
        }
        else if (checkIsFieldChange(Field.SEMESTERENDE, evt)) {
            txtSemesterende.setText(asString(semesterErfassenModel.getSemesterende()));
        }
        else if (checkIsFieldChange(Field.ANZAHL_SCHULWOCHEN, evt)) {
            txtAnzahlSchulwochen.setText(Integer.toString(semesterErfassenModel.getAnzahlSchulwochen()));
        }
    }

    @Override
    void validateFields() throws SvmValidationException {
        if (txtSemesterbeginn.isEnabled()) {
            LOGGER.trace("Validate field Semesterbeginn");
            setModelSemesterbeginn(true);
        }
        if (txtSemesterende.isEnabled()) {
            LOGGER.trace("Validate field Semesterende");
            setModelSemesterende(true);
        }
        if (txtAnzahlSchulwochen.isEnabled()) {
            LOGGER.trace("Validate field AnzahlSchulwochen");
            setModelAnzahlSchulwochen(true);
        }
    }

    @Override
    void showErrMsg(SvmValidationException e) {
        if (e.getAffectedFields().contains(Field.SEMESTERBEGINN)) {
            errLblSemesterbeginn.setVisible(true);
            errLblSemesterbeginn.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.SEMESTERENDE)) {
            errLblSemesterende.setVisible(true);
            errLblSemesterende.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ANZAHL_SCHULWOCHEN)) {
            errLblAnzahlSchulwochen.setVisible(true);
            errLblAnzahlSchulwochen.setText(e.getMessage());
        }
    }

    @Override
    void showErrMsgAsToolTip(SvmValidationException e) {
        if (e.getAffectedFields().contains(Field.SEMESTERBEGINN)) {
            txtSemesterbeginn.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.SEMESTERENDE)) {
            txtSemesterende.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ANZAHL_SCHULWOCHEN)) {
            txtAnzahlSchulwochen.setToolTipText(e.getMessage());
        }
    }

    @Override
    public void makeErrorLabelsInvisible(Set<Field> fields) {
        if (fields.contains(Field.ALLE) || fields.contains(Field.SEMESTERBEGINN)) {
            errLblSemesterbeginn.setVisible(false);
            txtSemesterbeginn.setToolTipText(null);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.SEMESTERENDE)) {
            errLblSemesterende.setVisible(false);
            txtSemesterende.setToolTipText(null);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.ANZAHL_SCHULWOCHEN)) {
            errLblAnzahlSchulwochen.setVisible(false);
            txtAnzahlSchulwochen.setToolTipText(null);
        }
    }

    @Override
    public void disableFields(boolean disable, Set<Field> fields) {}

}
