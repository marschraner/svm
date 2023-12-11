package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.common.dataTypes.Semesterbezeichnung;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.CompletedListener;
import ch.metzenthin.svm.domain.model.SemesterErfassenModel;
import ch.metzenthin.svm.ui.componentmodel.SemestersTableModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    private static final Logger LOGGER = LogManager.getLogger(SemesterErfassenController.class);

    // Möglichkeit zum Umschalten des validation modes (nicht dynamisch)
    private static final boolean MODEL_VALIDATION_MODE = false;

    private SemestersTableModel semestersTableModel;
    private SemesterErfassenModel semesterErfassenModel;
    private final SvmContext svmContext;
    private boolean isBearbeiten;
    private boolean defaultButtonEnabled;
    private JDialog semesterErfassenDialog;
    private JSpinner spinnerSchuljahre;
    private JComboBox<Semesterbezeichnung> comboBoxSemesterbezeichnung;
    private JTextField txtSemesterbeginn;
    private JTextField txtSemesterende;
    private JTextField txtFerienbeginn1;
    private JTextField txtFerienende1;
    private JTextField txtFerienbeginn2;
    private JTextField txtFerienende2;
    private JLabel errLblSemesterbeginn;
    private JLabel errLblSemesterende;
    private JLabel errLblFerienbeginn1;
    private JLabel errLblFerienende1;
    private JLabel errLblFerienbeginn2;
    private JLabel errLblFerienende2;
    private JButton btnSpeichern;

    public SemesterErfassenController(SvmContext svmContext, SemestersTableModel semestersTableModel, SemesterErfassenModel semesterErfassenModel, boolean isBearbeiten, boolean defaultButtonEnabled) {
        super(semesterErfassenModel);
        this.svmContext = svmContext;
        this.semestersTableModel = semestersTableModel;
        this.semesterErfassenModel = semesterErfassenModel;
        this.isBearbeiten = isBearbeiten;
        this.defaultButtonEnabled = defaultButtonEnabled;
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
            LOGGER.trace("SemesterErfassenController setModelSchuljahr Exception=" + e.getMessage());
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
        LOGGER.trace("SemesterErfasssenController Event Semesterbezeichnung selected=" + comboBoxSemesterbezeichnung.getSelectedItem());
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
        if (!defaultButtonEnabled) {
            this.txtSemesterbeginn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    onSemesterbeginnEvent(true);
                }
            });
        }
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
        if (!defaultButtonEnabled) {
            this.txtSemesterende.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    onSemesterendeEvent(true);
                }
            });
        }
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

    public void setTxtFerienbeginn1(JTextField txtFerienbeginn1) {
        this.txtFerienbeginn1 = txtFerienbeginn1;
        if (!defaultButtonEnabled) {
            this.txtFerienbeginn1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    onFerienbeginn1Event(true);
                }
            });
        }
        this.txtFerienbeginn1.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onFerienbeginn1Event(false);
            }
        });
    }

    private void onFerienbeginn1Event(boolean showRequiredErrMsg) {
        LOGGER.trace("SemesterErfassenController Event Ferienbeginn1");
        boolean equalFieldAndModelValue = equalsNullSafe(txtFerienbeginn1.getText(), semesterErfassenModel.getFerienbeginn1());
        try {
            setModelFerienbeginn1(showRequiredErrMsg);
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelFerienbeginn1(boolean showRequiredErrMsg) throws SvmValidationException {
        makeErrorLabelInvisible(Field.FERIENBEGINN1);
        try {
            semesterErfassenModel.setFerienbeginn1(txtFerienbeginn1.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("SemesterErfassenController setModelFerienbeginn1 RequiredException=" + e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtFerienbeginn1.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("SemesterErfassenController setModelFerienbeginn1 Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtFerienende1(JTextField txtFerienende1) {
        this.txtFerienende1 = txtFerienende1;
        if (!defaultButtonEnabled) {
            this.txtFerienende1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    onFerienende1Event(true);
                }
            });
        }
        this.txtFerienende1.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onFerienende1Event(false);
            }
        });
    }

    private void onFerienende1Event(boolean showRequiredErrMsg) {
        LOGGER.trace("SemesterErfassenController Event Ferienende1");
        boolean equalFieldAndModelValue = equalsNullSafe(txtFerienende1.getText(), semesterErfassenModel.getFerienende1());
        try {
            setModelFerienende1(showRequiredErrMsg);
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelFerienende1(boolean showRequiredErrMsg) throws SvmValidationException {
        makeErrorLabelInvisible(Field.FERIENENDE1);
        try {
            semesterErfassenModel.setFerienende1(txtFerienende1.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("SemesterErfassenController setModelFerienende1 RequiredException=" + e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtFerienende1.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("SemesterErfassenController setModelFerienende1 Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtFerienbeginn2(JTextField txtFerienbeginn2) {
        this.txtFerienbeginn2 = txtFerienbeginn2;
        if (!defaultButtonEnabled) {
            this.txtFerienbeginn2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    onFerienbeginn2Event(true);
                }
            });
        }
        this.txtFerienbeginn2.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onFerienbeginn2Event(false);
            }
        });
    }

    private void onFerienbeginn2Event(boolean showRequiredErrMsg) {
        LOGGER.trace("SemesterErfassenController Event Ferienbeginn2");
        boolean equalFieldAndModelValue = equalsNullSafe(txtFerienbeginn2.getText(), semesterErfassenModel.getFerienbeginn2());
        try {
            setModelFerienbeginn2(showRequiredErrMsg);
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelFerienbeginn2(boolean showRequiredErrMsg) throws SvmValidationException {
        makeErrorLabelInvisible(Field.FERIENBEGINN2);
        try {
            semesterErfassenModel.setFerienbeginn2(txtFerienbeginn2.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("SemesterErfassenController setModelFerienbeginn2 RequiredException=" + e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtFerienbeginn2.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("SemesterErfassenController setModelFerienbeginn2 Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtFerienende2(JTextField txtFerienende2) {
        this.txtFerienende2 = txtFerienende2;
        if (!defaultButtonEnabled) {
            this.txtFerienende2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    onFerienende2Event(true);
                }
            });
        }
        this.txtFerienende2.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onFerienende2Event(false);
            }
        });
    }

    private void onFerienende2Event(boolean showRequiredErrMsg) {
        LOGGER.trace("SemesterErfassenController Event Ferienende2");
        boolean equalFieldAndModelValue = equalsNullSafe(txtFerienende2.getText(), semesterErfassenModel.getFerienende2());
        try {
            setModelFerienende2(showRequiredErrMsg);
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelFerienende2(boolean showRequiredErrMsg) throws SvmValidationException {
        makeErrorLabelInvisible(Field.FERIENENDE2);
        try {
            semesterErfassenModel.setFerienende2(txtFerienende2.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("SemesterErfassenController setModelFerienende2 RequiredException=" + e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtFerienende2.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("SemesterErfassenController setModelFerienende2 Exception=" + e.getMessage());
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

    public void setErrLblFerienbeginn1(JLabel errLblFerienbeginn1) {
        this.errLblFerienbeginn1 = errLblFerienbeginn1;
    }

    public void setErrLblFerienende1(JLabel errLblFerienende1) {
        this.errLblFerienende1 = errLblFerienende1;
    }

    public void setErrLblFerienbeginn2(JLabel errLblFerienbeginn2) {
        this.errLblFerienbeginn2 = errLblFerienbeginn2;
    }

    public void setErrLblFerienende2(JLabel errLblFerienende2) {
        this.errLblFerienende2 = errLblFerienende2;
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
            JOptionPane.showMessageDialog(semesterErfassenDialog, semesterErfassenModel.getSemester() + " bereits erfasst.", "Fehler", JOptionPane.ERROR_MESSAGE, svmContext.getDialogIcons().getErrorIcon());
            btnSpeichern.setFocusPainted(false);
        } else {
            if (semesterErfassenModel.checkSemesterUeberlapptAndereSemester(svmContext.getSvmModel())) {
                JOptionPane.showMessageDialog(semesterErfassenDialog, "Semester dürfen sich nicht überlappen.", "Fehler", JOptionPane.ERROR_MESSAGE, svmContext.getDialogIcons().getErrorIcon());
                btnSpeichern.setFocusPainted(false);
            } else {
                boolean affectsSemesterrechnungen = semesterErfassenModel.checkIfUpdateAffectsSemesterrechnungen();
                int n = 0;
                if (affectsSemesterrechnungen) {
                    Object[] optionsImport = {"Ja", "Nein"};
                    n = JOptionPane.showOptionDialog(
                            null,
                            "Soll die Anzahl Semesterwochen auch bei den Semesterrechnungen geändert werden? \n" +
                                    "(Semesterrechnungen mit gesetztem Rechnungsdatum werden nicht verändert.)",
                            "Anzahl Semesterwochen auch bei Semesterrechnungen ändern?",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            svmContext.getDialogIcons().getQuestionIcon(),
                            optionsImport,  //the titles of buttons
                            optionsImport[0]); //default button title

                }
                semesterErfassenModel.speichern(svmContext.getSvmModel(), semestersTableModel);
                if (affectsSemesterrechnungen && n == 0) {
                    semesterErfassenModel.updateAnzWochenSemesterrechnungen();
                }
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
        } else if (checkIsFieldChange(Field.SEMESTERENDE, evt)) {
            txtSemesterende.setText(asString(semesterErfassenModel.getSemesterende()));
        } else if (checkIsFieldChange(Field.FERIENBEGINN1, evt)) {
            txtFerienbeginn1.setText(asString(semesterErfassenModel.getFerienbeginn1()));
        } else if (checkIsFieldChange(Field.FERIENENDE1, evt)) {
            txtFerienende1.setText(asString(semesterErfassenModel.getFerienende1()));
        } else if (checkIsFieldChange(Field.FERIENBEGINN2, evt)) {
            txtFerienbeginn2.setText(asString(semesterErfassenModel.getFerienbeginn2()));
        } else if (checkIsFieldChange(Field.FERIENENDE2, evt)) {
            txtFerienende2.setText(asString(semesterErfassenModel.getFerienende2()));
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
        if (txtFerienbeginn1.isEnabled()) {
            LOGGER.trace("Validate field Ferienbeginn1");
            setModelFerienbeginn1(true);
        }
        if (txtFerienende1.isEnabled()) {
            LOGGER.trace("Validate field Ferienende1");
            setModelFerienende1(true);
        }
        if (txtFerienbeginn2.isEnabled()) {
            LOGGER.trace("Validate field Ferienbeginn2");
            setModelFerienbeginn2(true);
        }
        if (txtFerienende2.isEnabled()) {
            LOGGER.trace("Validate field Ferienende2");
            setModelFerienende2(true);
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
        if (e.getAffectedFields().contains(Field.FERIENBEGINN1)) {
            errLblFerienbeginn1.setVisible(true);
            errLblFerienbeginn1.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.FERIENENDE1)) {
            errLblFerienende1.setVisible(true);
            errLblFerienende1.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.FERIENBEGINN2)) {
            errLblFerienbeginn2.setVisible(true);
            errLblFerienbeginn2.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.FERIENENDE2)) {
            errLblFerienende2.setVisible(true);
            errLblFerienende2.setText(e.getMessage());
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
        if (e.getAffectedFields().contains(Field.FERIENBEGINN1)) {
            txtFerienbeginn1.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.FERIENENDE1)) {
            txtFerienende1.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.FERIENBEGINN2)) {
            txtFerienbeginn2.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.FERIENENDE2)) {
            txtFerienende2.setToolTipText(e.getMessage());
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
        if (fields.contains(Field.ALLE) || fields.contains(Field.FERIENBEGINN1)) {
            errLblFerienbeginn1.setVisible(false);
            txtFerienbeginn1.setToolTipText(null);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.FERIENENDE1)) {
            errLblFerienende1.setVisible(false);
            txtFerienende1.setToolTipText(null);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.FERIENBEGINN2)) {
            errLblFerienbeginn2.setVisible(false);
            txtFerienbeginn2.setToolTipText(null);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.FERIENENDE2)) {
            errLblFerienende2.setVisible(false);
            txtFerienende2.setToolTipText(null);
        }
    }

    @Override
    public void disableFields(boolean disable, Set<Field> fields) {}

}
