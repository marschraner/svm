package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.common.dataTypes.Wochentag;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.FindKursCommand;
import ch.metzenthin.svm.domain.model.CompletedListener;
import ch.metzenthin.svm.domain.model.KursanmeldungErfassenModel;
import ch.metzenthin.svm.domain.model.SchuelerDatenblattModel;
import ch.metzenthin.svm.persistence.entities.Lehrkraft;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.ui.componentmodel.KursanmeldungenTableModel;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.util.List;
import java.util.Set;

import static ch.metzenthin.svm.common.utils.Converter.asString;
import static ch.metzenthin.svm.common.utils.SimpleValidator.equalsNullSafe;

/**
 * @author Martin Schraner
 */
public class KursanmeldungErfassenController extends AbstractController {

    private static final Logger LOGGER = Logger.getLogger(KursanmeldungErfassenController.class);

    // Möglichkeit zum Umschalten des validation modes (nicht dynamisch)
    private static final boolean MODEL_VALIDATION_MODE = false;

    private final SvmContext svmContext;
    private KursanmeldungenTableModel kursanmeldungenTableModel;
    private KursanmeldungErfassenModel kursanmeldungErfassenModel;
    private SchuelerDatenblattModel schuelerDatenblattModel;
    private boolean isBearbeiten;
    private JDialog kursanmeldungErfassenDialog;
    private JSpinner spinnerSemester;
    private JComboBox<Wochentag> comboBoxWochentag;
    private JComboBox<Lehrkraft> comboBoxLehrkraft;
    private JCheckBox checkBoxAbmeldungPerEndeSemester;
    private JTextField txtZeitBeginn;
    private JTextField txtBemerkungen;
    private JLabel errLblWochentag;
    private JLabel errLblZeitBeginn;
    private JLabel errLblLehrkraft;
    private JLabel errLblBemerkungen;
    private JButton btnOk;

    public KursanmeldungErfassenController(SvmContext svmContext, KursanmeldungErfassenModel kursanmeldungErfassenModel, KursanmeldungenTableModel kursanmeldungenTableModel, SchuelerDatenblattModel schuelerDatenblattModel, boolean isBearbeiten) {
        super(kursanmeldungErfassenModel);
        this.svmContext = svmContext;
        this.kursanmeldungenTableModel = kursanmeldungenTableModel;
        this.kursanmeldungErfassenModel = kursanmeldungErfassenModel;
        this.schuelerDatenblattModel = schuelerDatenblattModel;
        this.isBearbeiten = isBearbeiten;
        this.kursanmeldungErfassenModel.addPropertyChangeListener(this);
        this.kursanmeldungErfassenModel.addDisableFieldsListener(this);
        this.kursanmeldungErfassenModel.addMakeErrorLabelsInvisibleListener(this);
        this.kursanmeldungErfassenModel.addCompletedListener(new CompletedListener() {
            @Override
            public void completed(boolean completed) {
                onKursanmeldungErfassenModelCompleted(completed);
            }
        });
        this.setModelValidationMode(MODEL_VALIDATION_MODE);
    }

    public void constructionDone() {
        kursanmeldungErfassenModel.initializeCompleted();
    }

    public void setKursanmeldungErfassenDialog(JDialog kursanmeldungErfassenDialog) {
        // call onCancel() when cross is clicked
        this.kursanmeldungErfassenDialog = kursanmeldungErfassenDialog;
        kursanmeldungErfassenDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        kursanmeldungErfassenDialog.addWindowListener(new WindowAdapter() {
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
    
    public void setSpinnerSemester(JSpinner spinnerSemester) {
        this.spinnerSemester = spinnerSemester;
        // Darf nicht bearbeitet werden, da Teil von Kurs und dieser Teil der ID!
        if (isBearbeiten) {
            spinnerSemester.setEnabled(false);
        }
        List<Semester> semesterList = svmContext.getSvmModel().getSemestersAll();
        if (semesterList.isEmpty()) {
            return;
        }
        Semester[] semesters;
        if (isBearbeiten) {
            semesters = kursanmeldungErfassenModel.getSelectableSemesterKursanmeldungOrigin();
        } else {
            semesters = semesterList.toArray(new Semester[semesterList.size()]);
        }
        SpinnerModel spinnerModelSemester = new SpinnerListModel(semesters);
        spinnerSemester.setModel(spinnerModelSemester);
        spinnerSemester.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                onSemesterSelected();
            }
        });
        if (!isBearbeiten) {
            // Model initialisieren
            kursanmeldungErfassenModel.setSemester(kursanmeldungErfassenModel.getInitSemester(svmContext.getSvmModel()));
        }
    }

    private void onSemesterSelected() {
        LOGGER.trace("KursSchuelerHinzufuegenController Event Semester selected =" + spinnerSemester.getValue());
        boolean equalFieldAndModelValue = equalsNullSafe(spinnerSemester.getValue(), kursanmeldungErfassenModel.getSemester());
        setModelSemester();
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelSemester() {
        makeErrorLabelInvisible(Field.SEMESTER);
        kursanmeldungErfassenModel.setSemester((Semester) spinnerSemester.getValue());
    }

    public void setComboBoxWochentag(JComboBox<Wochentag> comboBoxWochentag) {
        this.comboBoxWochentag = comboBoxWochentag;
        // Darf nicht bearbeitet werden, da Teil von Kurs und dieser Teil der ID!
        if (isBearbeiten) {
            this.comboBoxWochentag.setEnabled(false);
        }
        comboBoxWochentag.setModel(new DefaultComboBoxModel<>(Wochentag.values()));
        comboBoxWochentag.removeItem(Wochentag.ALLE);
        comboBoxWochentag.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onWochentagSelected();
            }
        });
        if (!isBearbeiten) {
            // Leeren ComboBox-Wert anzeigen
            comboBoxWochentag.setSelectedItem(null);
        }
    }

    private void onWochentagSelected() {
        LOGGER.trace("PersonController Event Wochentag selected=" + comboBoxWochentag.getSelectedItem());
        boolean equalFieldAndModelValue = equalsNullSafe(comboBoxWochentag.getSelectedItem(), kursanmeldungErfassenModel.getWochentag());
        try {
            setModelWochentag();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelWochentag() throws SvmValidationException {
        makeErrorLabelInvisible(Field.WOCHENTAG);
        try {
            kursanmeldungErfassenModel.setWochentag((Wochentag) comboBoxWochentag.getSelectedItem());
        } catch (SvmRequiredException e) {
            LOGGER.trace("KursErfassenController setModelWochentag RequiredException=" + e.getMessage());
            if (isModelValidationMode()) {
                comboBoxWochentag.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        }
    }

    public void setTxtZeitBeginn(JTextField txtZeitBeginn) {
        this.txtZeitBeginn = txtZeitBeginn;
        // Darf nicht bearbeitet werden, da Teil von Kurs und dieser Teil der ID!
        if (isBearbeiten) {
            this.txtZeitBeginn.setEnabled(false);
        }
        this.txtZeitBeginn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onZeitBeginnEvent(true);
            }
        });
        this.txtZeitBeginn.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onZeitBeginnEvent(false);
            }
        });
    }

    private void onZeitBeginnEvent(boolean showRequiredErrMsg) {
        LOGGER.trace("KursSchuelerHinzufuegenController Event ZeitBeginn");
        boolean equalFieldAndModelValue = equalsNullSafe(txtZeitBeginn.getText(), kursanmeldungErfassenModel.getZeitBeginn());
        try {
            setModelZeitBeginn(showRequiredErrMsg);
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelZeitBeginn(boolean showRequiredErrMsg) throws SvmValidationException {
        makeErrorLabelInvisible(Field.ZEIT_BEGINN);
        try {
            kursanmeldungErfassenModel.setZeitBeginn(txtZeitBeginn.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("KursSchuelerHinzufuegenController setModelZeitBeginn RequiredException=" + e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtZeitBeginn.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("KursSchuelerHinzufuegenController setModelZeitBeginn Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setComboBoxLehrkraft(JComboBox<Lehrkraft> comboBoxLehrkraft) {
        this.comboBoxLehrkraft = comboBoxLehrkraft;
        // Darf nicht bearbeitet werden, da Teil von Kurs und dieser Teil der ID!
        if (isBearbeiten) {
            this.comboBoxLehrkraft.setEnabled(false);
        }
        List<Lehrkraft> lehrkraefteList = svmContext.getSvmModel().getAktiveLehrkraefteAll();
        Lehrkraft[] selectableLehrkraefte;
        if (isBearbeiten) {
            selectableLehrkraefte = kursanmeldungErfassenModel.getSelectableLehrkraftKursanmeldungOrigin();
        } else {
            selectableLehrkraefte = lehrkraefteList.toArray(new Lehrkraft[lehrkraefteList.size()]);
        }
        comboBoxLehrkraft.setModel(new DefaultComboBoxModel<>(selectableLehrkraefte));
        comboBoxLehrkraft.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onLehrkraftSelected();
            }
        });
        if (!isBearbeiten) {
            // Leeren ComboBox-Wert anzeigen
            comboBoxLehrkraft.setSelectedItem(null);
        }
    }

    private void onLehrkraftSelected() {
        LOGGER.trace("KursSchuelerHinzufuegenController Event Lehrkraft selected=" + comboBoxLehrkraft.getSelectedItem());
        boolean equalFieldAndModelValue = equalsNullSafe(comboBoxLehrkraft.getSelectedItem(), kursanmeldungErfassenModel.getLehrkraft());
        try {
            setModelLehrkraft();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelLehrkraft() throws SvmValidationException {
        makeErrorLabelInvisible(Field.LEHRKRAFT1);
        try {
            kursanmeldungErfassenModel.setLehrkraft((Lehrkraft) comboBoxLehrkraft.getSelectedItem());
        } catch (SvmRequiredException e) {
            LOGGER.trace("KursSchuelerHinzufuegenController setModelLehrkraft RequiredException=" + e.getMessage());
            if (isModelValidationMode()) {
                comboBoxLehrkraft.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        }
    }

    public void setCheckBoxAbmeldungPerEndeSemester(JCheckBox checkBoxAbmeldungPerEndeSemester) {
        this.checkBoxAbmeldungPerEndeSemester = checkBoxAbmeldungPerEndeSemester;
        // Keine AbmeldungPerEndeSemester als Default-Wert
        if (!isBearbeiten) {
            kursanmeldungErfassenModel.setAbmeldungPerEndeSemester(false);
        }
        this.checkBoxAbmeldungPerEndeSemester.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                onAbmeldungPerEndeSemesterEvent();
            }
        });
    }

    private void setModelAbmeldungPerEndeSemester() {
        kursanmeldungErfassenModel.setAbmeldungPerEndeSemester(checkBoxAbmeldungPerEndeSemester.isSelected());
    }

    private void onAbmeldungPerEndeSemesterEvent() {
        LOGGER.trace("AngehoerigerController Event AbmeldungPerEndeSemester. Selected=" + checkBoxAbmeldungPerEndeSemester.isSelected());
        setModelAbmeldungPerEndeSemester();
    }


    public void setTxtBemerkungen(JTextField txtBemerkungen) {
        this.txtBemerkungen = txtBemerkungen;
        this.txtBemerkungen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onBemerkungenEvent(true);
            }
        });
        this.txtBemerkungen.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onBemerkungenEvent(false);
            }
        });
    }

    private void onBemerkungenEvent(boolean showRequiredErrMsg) {
        LOGGER.trace("KurseinteilungErfassenController Event Bemerkungen");
        boolean equalFieldAndModelValue = equalsNullSafe(txtBemerkungen.getText(), kursanmeldungErfassenModel.getBemerkungen());
        try {
            setModelBemerkungen(showRequiredErrMsg);
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelBemerkungen(boolean showRequiredErrMsg) throws SvmValidationException {
        makeErrorLabelInvisible(Field.BEMERKUNGEN);
        try {
            kursanmeldungErfassenModel.setBemerkungen(txtBemerkungen.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("KurseinteilungErfassenController setModelBemerkungen RequiredException=" + e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtBemerkungen.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("KurseinteilungErfassenController setModelBemerkungen Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setErrLblWochentag(JLabel errLblWochentag) {
        this.errLblWochentag = errLblWochentag;
    }

    public void setErrLblZeitBeginn(JLabel errLblZeitBeginn) {
        this.errLblZeitBeginn = errLblZeitBeginn;
    }

    public void setErrLblLehrkraft(JLabel errLblLehrkraft) {
        this.errLblLehrkraft = errLblLehrkraft;
    }
    
    public void setErrLblBemerkungen(JLabel errLblBemerkungen) {
        this.errLblBemerkungen = errLblBemerkungen;
    }

    public void setBtnOk(JButton btnOk) {
        this.btnOk = btnOk;
        if (isModelValidationMode()) {
            btnOk.setEnabled(false);
        }
        this.btnOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onHinzufuegen();
            }
        });
    }

    private void onHinzufuegen() {
        if (!isModelValidationMode() && !validateOnSpeichern()) {
            btnOk.setFocusPainted(false);
            return;
        }
        int n = 0;
        if (!isBearbeiten && kursanmeldungErfassenModel.checkIfSemesterIsInPast()) {
            Object[] options = {"Ja", "Nein"};
            n = JOptionPane.showOptionDialog(
                    null,
                    "Das selektierte Semester liegt in der Vergangenheit. Kursanmeldung trotzdem speichern?",
                    "Semester in Vergangenheit",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null,     //do not use a custom Icon
                    options,  //the titles of buttons
                    options[1]); //default button title
        }
        if (n == 0) {
            // Existiert der Kurs?
            if (!isBearbeiten && kursanmeldungErfassenModel.findKurs() == FindKursCommand.Result.KURS_EXISTIERT_NICHT) {
                JOptionPane.showMessageDialog(kursanmeldungErfassenDialog, "Kurs existiert nicht.", "Fehler", JOptionPane.ERROR_MESSAGE);
                btnOk.setFocusPainted(false);
                return;
            }
            // Kurs bereits erfasst
            if (!isBearbeiten && kursanmeldungErfassenModel.checkIfKursBereitsErfasst(schuelerDatenblattModel)) {
                JOptionPane.showMessageDialog(kursanmeldungErfassenDialog, "Kurs bereits erfasst.", "Fehler", JOptionPane.ERROR_MESSAGE);
                btnOk.setFocusPainted(false);
                return;
            }
            // Speichern
            kursanmeldungErfassenModel.speichern(kursanmeldungenTableModel, schuelerDatenblattModel);
        }
        kursanmeldungErfassenDialog.dispose();
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
        kursanmeldungErfassenDialog.dispose();
    }

    private void onKursanmeldungErfassenModelCompleted(boolean completed) {
        LOGGER.trace("KursSchuelerHinzufuegenModel completed=" + completed);
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
        if (checkIsFieldChange(Field.SEMESTER, evt)) {
            spinnerSemester.setValue(kursanmeldungErfassenModel.getSemester());
        } else if (checkIsFieldChange(Field.WOCHENTAG, evt)) {
            comboBoxWochentag.setSelectedItem(kursanmeldungErfassenModel.getWochentag());
        } else if (checkIsFieldChange(Field.ZEIT_BEGINN, evt)) {
            txtZeitBeginn.setText(asString(kursanmeldungErfassenModel.getZeitBeginn()));
        } else if (checkIsFieldChange(Field.LEHRKRAFT, evt)) {
            comboBoxLehrkraft.setSelectedItem(kursanmeldungErfassenModel.getLehrkraft());
        } else if (checkIsFieldChange(Field.ABMELDUNG_PER_ENDE_SEMESTER, evt)) {
            checkBoxAbmeldungPerEndeSemester.setSelected(kursanmeldungErfassenModel.isAbmeldungPerEndeSemester());
        } else if (checkIsFieldChange(Field.BEMERKUNGEN, evt)) {
            txtBemerkungen.setText(kursanmeldungErfassenModel.getBemerkungen());
        }
    }

    @Override
    void validateFields() throws SvmValidationException {
        if (spinnerSemester.isEnabled()) {
            LOGGER.trace("Validate field Semester");
            setModelSemester();
        }
        if (comboBoxWochentag.isEnabled()) {
            LOGGER.trace("Validate field Wochentag");
            setModelWochentag();
        }
        if (txtZeitBeginn.isEnabled()) {
            LOGGER.trace("Validate field ZeitBeginn");
            setModelZeitBeginn(true);
        }
        if (comboBoxLehrkraft.isEnabled()) {
            LOGGER.trace("Validate field Lehrkraft");
            setModelLehrkraft();
        }
        if (txtBemerkungen.isEnabled()) {
            LOGGER.trace("Validate field Bemerkungen");
            setModelBemerkungen(true);
        }
    }

    @Override
    void showErrMsg(SvmValidationException e) {
        if (e.getAffectedFields().contains(Field.WOCHENTAG)) {
            errLblWochentag.setVisible(true);
            errLblWochentag.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ZEIT_BEGINN)) {
            errLblZeitBeginn.setVisible(true);
            errLblZeitBeginn.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.LEHRKRAFT)) {
            errLblLehrkraft.setVisible(true);
            errLblLehrkraft.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.BEMERKUNGEN)) {
            errLblBemerkungen.setVisible(true);
            errLblBemerkungen.setText(e.getMessage());
        }
    }

    @Override
    void showErrMsgAsToolTip(SvmValidationException e) {
        if (e.getAffectedFields().contains(Field.WOCHENTAG)) {
            comboBoxWochentag.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ZEIT_BEGINN)) {
            txtZeitBeginn.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.LEHRKRAFT)) {
            comboBoxLehrkraft.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.BEMERKUNGEN)) {
            txtBemerkungen.setToolTipText(e.getMessage());
        }
    }

    @Override
    public void makeErrorLabelsInvisible(Set<Field> fields) {
        if (fields.contains(Field.ALLE) || fields.contains(Field.WOCHENTAG)) {
            errLblWochentag.setVisible(false);
            comboBoxWochentag.setToolTipText(null);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.ZEIT_BEGINN)) {
            errLblZeitBeginn.setVisible(false);
            txtZeitBeginn.setToolTipText(null);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.LEHRKRAFT)) {
            errLblLehrkraft.setVisible(false);
            comboBoxLehrkraft.setToolTipText(null);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.BEMERKUNGEN)) {
            errLblBemerkungen.setVisible(false);
            txtBemerkungen.setToolTipText(null);
        }
    }

    @Override
    public void disableFields(boolean disable, Set<Field> fields) {}

}