package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.dataTypes.Field;
import ch.metzenthin.svm.dataTypes.Wochentag;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.AddKursToSchuelerAndSaveCommand;
import ch.metzenthin.svm.domain.model.CompletedListener;
import ch.metzenthin.svm.domain.model.KursSchuelerHinzufuegenModel;
import ch.metzenthin.svm.domain.model.KurseModel;
import ch.metzenthin.svm.domain.model.SchuelerDatenblattModel;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.persistence.entities.Lehrkraft;
import ch.metzenthin.svm.ui.componentmodel.KurseTableModel;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.util.List;
import java.util.Set;

import static ch.metzenthin.svm.common.utils.Converter.asString;
import static ch.metzenthin.svm.common.utils.SimpleValidator.equalsNullSafe;

/**
 * @author Martin Schraner
 */
public class KursSchuelerHinzufuegenController extends AbstractController {

    private static final Logger LOGGER = Logger.getLogger(KursSchuelerHinzufuegenController.class);

    // Möglichkeit zum Umschalten des validation modes (nicht dynamisch)
    private static final boolean MODEL_VALIDATION_MODE = false;

    private KurseTableModel kurseTableModel;
    private KursSchuelerHinzufuegenModel kursSchuelerHinzufuegenModel;
    private KurseModel kurseModel;
    private SchuelerDatenblattModel schuelerDatenblattModel;
    private final SvmContext svmContext;
    private JDialog kursSchuelerHinzufuegenDialog;
    private JComboBox<Semester> comboBoxSemester;
    private JComboBox<Wochentag> comboBoxWochentag;
    private JComboBox<Lehrkraft> comboBoxLehrkraft;
    private JTextField txtZeitBeginn;
    private JLabel errLblWochentag;
    private JLabel errLblZeitBeginn;
    private JLabel errLblLehrkraft;
    private JButton btnOk;

    public KursSchuelerHinzufuegenController(SvmContext svmContext, KurseTableModel kurseTableModel, KursSchuelerHinzufuegenModel kursSchuelerHinzufuegenModel, KurseModel kurseModel, SchuelerDatenblattModel schuelerDatenblattModel) {
        super(kursSchuelerHinzufuegenModel);
        this.svmContext = svmContext;
        this.kurseTableModel = kurseTableModel;
        this.kursSchuelerHinzufuegenModel = kursSchuelerHinzufuegenModel;
        this.kurseModel = kurseModel;
        this.schuelerDatenblattModel = schuelerDatenblattModel;
        this.kursSchuelerHinzufuegenModel.addPropertyChangeListener(this);
        this.kursSchuelerHinzufuegenModel.addDisableFieldsListener(this);
        this.kursSchuelerHinzufuegenModel.addMakeErrorLabelsInvisibleListener(this);
        this.kursSchuelerHinzufuegenModel.addCompletedListener(new CompletedListener() {
            @Override
            public void completed(boolean completed) {
                onKursSchuelerHinzufuegenModelCompleted(completed);
            }
        });
        this.setModelValidationMode(MODEL_VALIDATION_MODE);
    }

    public void constructionDone() {
        kursSchuelerHinzufuegenModel.initializeCompleted();
    }

    public void setKursSchuelerHinzufuegenDialog(JDialog kursSchuelerHinzufuegenDialog) {
        // call onCancel() when cross is clicked
        this.kursSchuelerHinzufuegenDialog = kursSchuelerHinzufuegenDialog;
        kursSchuelerHinzufuegenDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        kursSchuelerHinzufuegenDialog.addWindowListener(new WindowAdapter() {
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
    
    public void setComboBoxSemester(JComboBox<Semester> comboBoxSemester) {
        this.comboBoxSemester = comboBoxSemester;
        Semester[] selectableSemesters = kurseModel.getSelectableSemestersKurseSchueler(svmContext.getSvmModel());
        comboBoxSemester.setModel(new DefaultComboBoxModel<>(selectableSemesters));
        // Model initialisieren mit Default-Semester
        kursSchuelerHinzufuegenModel.setSemester(kursSchuelerHinzufuegenModel.getDefaultSemester(svmContext.getSvmModel(), selectableSemesters));
        comboBoxSemester.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSemesterSelected();
            }
        });
    }

    private void onSemesterSelected() {
        LOGGER.trace("PersonController Event Semester selected=" + comboBoxSemester.getSelectedItem());
        boolean equalFieldAndModelValue = equalsNullSafe(comboBoxSemester.getSelectedItem(), kursSchuelerHinzufuegenModel.getSemester());
        try {
            setModelSemester();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelSemester() throws SvmValidationException {
        makeErrorLabelInvisible(Field.SEMESTER);
        kursSchuelerHinzufuegenModel.setSemester((Semester) comboBoxSemester.getSelectedItem());
    }

    public void setComboBoxWochentag(JComboBox<Wochentag> comboBoxWochentag) {
        this.comboBoxWochentag = comboBoxWochentag;
        comboBoxWochentag.setModel(new DefaultComboBoxModel<>(Wochentag.values()));
        comboBoxWochentag.removeItem(Wochentag.ALLE);
        // Leeren ComboBox-Wert anzeigen
        comboBoxWochentag.setSelectedItem(null);
        comboBoxWochentag.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onWochentagSelected();
            }
        });
    }

    private void onWochentagSelected() {
        LOGGER.trace("PersonController Event Wochentag selected=" + comboBoxWochentag.getSelectedItem());
        boolean equalFieldAndModelValue = equalsNullSafe(comboBoxWochentag.getSelectedItem(), kursSchuelerHinzufuegenModel.getWochentag());
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
            kursSchuelerHinzufuegenModel.setWochentag((Wochentag) comboBoxWochentag.getSelectedItem());
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
        boolean equalFieldAndModelValue = equalsNullSafe(txtZeitBeginn.getText(), kursSchuelerHinzufuegenModel.getZeitBeginn());
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
            kursSchuelerHinzufuegenModel.setZeitBeginn(txtZeitBeginn.getText());
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
        List<Lehrkraft> lehrkraefteList = svmContext.getSvmModel().getAktiveLehrkraefteAll();
        Lehrkraft[] selectableLehrkraefte = lehrkraefteList.toArray(new Lehrkraft[lehrkraefteList.size()]);
        comboBoxLehrkraft.setModel(new DefaultComboBoxModel<>(selectableLehrkraefte));
        // Leeren ComboBox-Wert anzeigen
        comboBoxLehrkraft.setSelectedItem(null);
        comboBoxLehrkraft.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onLehrkraftSelected();
            }
        });
    }

    private void onLehrkraftSelected() {
        LOGGER.trace("KursSchuelerHinzufuegenController Event Lehrkraft selected=" + comboBoxLehrkraft.getSelectedItem());
        boolean equalFieldAndModelValue = equalsNullSafe(comboBoxLehrkraft.getSelectedItem(), kursSchuelerHinzufuegenModel.getLehrkraft());
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
            kursSchuelerHinzufuegenModel.setLehrkraft((Lehrkraft) comboBoxLehrkraft.getSelectedItem());
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

    public void setErrLblWochentag(JLabel errLblWochentag) {
        this.errLblWochentag = errLblWochentag;
    }

    public void setErrLblZeitBeginn(JLabel errLblZeitBeginn) {
        this.errLblZeitBeginn = errLblZeitBeginn;
    }

    public void setErrLblLehrkraft(JLabel errLblLehrkraft) {
        this.errLblLehrkraft = errLblLehrkraft;
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
        AddKursToSchuelerAndSaveCommand.Result result = kursSchuelerHinzufuegenModel.hinzufuegen(kurseTableModel, schuelerDatenblattModel);
        if (result == AddKursToSchuelerAndSaveCommand.Result.KURS_EXISTIERT_NICHT) {
            JOptionPane.showMessageDialog(null, "Kurs existiert nicht.", "Fehler", JOptionPane.ERROR_MESSAGE);
            btnOk.setFocusPainted(false);
            return;
        }
        if (result == AddKursToSchuelerAndSaveCommand.Result.KURS_BEREITS_ERFASST) {
            JOptionPane.showMessageDialog(null, "Kurs bereits erfasst.", "Fehler", JOptionPane.ERROR_MESSAGE);
            btnOk.setFocusPainted(false);
            return;
        }
        kursSchuelerHinzufuegenDialog.dispose();
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
        kursSchuelerHinzufuegenDialog.dispose();
    }

    private void onKursSchuelerHinzufuegenModelCompleted(boolean completed) {
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
            comboBoxSemester.setSelectedItem(kursSchuelerHinzufuegenModel.getSemester());
        } else if (checkIsFieldChange(Field.WOCHENTAG, evt)) {
            comboBoxWochentag.setSelectedItem(kursSchuelerHinzufuegenModel.getWochentag());
        } else if (checkIsFieldChange(Field.ZEIT_BEGINN, evt)) {
            txtZeitBeginn.setText(asString(kursSchuelerHinzufuegenModel.getZeitBeginn()));
        } else if (checkIsFieldChange(Field.LEHRKRAFT1, evt)) {
            comboBoxLehrkraft.setSelectedItem(kursSchuelerHinzufuegenModel.getLehrkraft());
        }
    }

    @Override
    void validateFields() throws SvmValidationException {
        if (comboBoxSemester.isEnabled()) {
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
    }

    @Override
    public void disableFields(boolean disable, Set<Field> fields) {}

}
