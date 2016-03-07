package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.common.dataTypes.Wochentag;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CalculateAnzWochenCommand;
import ch.metzenthin.svm.domain.commands.FindKursCommand;
import ch.metzenthin.svm.domain.model.CompletedListener;
import ch.metzenthin.svm.domain.model.KursanmeldungErfassenModel;
import ch.metzenthin.svm.domain.model.SchuelerDatenblattModel;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
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
    private JComboBox<Mitarbeiter> comboBoxLehrkraft;
    private JTextField txtZeitBeginn;
    private JTextField txtAnmeldedatum;
    private JTextField txtAbmeldedatum;
    private JTextField txtBemerkungen;
    private JLabel errLblWochentag;
    private JLabel errLblZeitBeginn;
    private JLabel errLblLehrkraft;
    private JLabel errLblAnmeldedatum;
    private JLabel errLblAbmeldedatum;
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
            kursanmeldungErfassenModel.setSemester(kursanmeldungErfassenModel.getInitSemester(semesterList));
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
        comboBoxWochentag.removeItem(Wochentag.SONNTAG);
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

    public void setComboBoxLehrkraft(JComboBox<Mitarbeiter> comboBoxLehrkraft) {
        this.comboBoxLehrkraft = comboBoxLehrkraft;
        // Darf nicht bearbeitet werden, da Teil von Kurs und dieser Teil der ID!
        if (isBearbeiten) {
            this.comboBoxLehrkraft.setEnabled(false);
        }
        List<Mitarbeiter> lehrkraefteList = svmContext.getSvmModel().getAktiveLehrkraefteAll();
        Mitarbeiter[] selectableLehrkraefte;
        if (isBearbeiten) {
            selectableLehrkraefte = kursanmeldungErfassenModel.getSelectableLehrkraftKursanmeldungOrigin();
        } else {
            selectableLehrkraefte = lehrkraefteList.toArray(new Mitarbeiter[lehrkraefteList.size()]);
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
        boolean equalFieldAndModelValue = equalsNullSafe(comboBoxLehrkraft.getSelectedItem(), kursanmeldungErfassenModel.getMitarbeiter());
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
            kursanmeldungErfassenModel.setMitarbeiter((Mitarbeiter) comboBoxLehrkraft.getSelectedItem());
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

    public void setTxtAnmeldedatum(JTextField txtAnmeldedatum) {
        this.txtAnmeldedatum = txtAnmeldedatum;
        this.txtAnmeldedatum.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onAnmeldedatumEvent(true);
            }
        });
        this.txtAnmeldedatum.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onAnmeldedatumEvent(false);
            }
        });
    }

    private void onAnmeldedatumEvent(boolean showRequiredErrMsg) {
        LOGGER.trace("KurseinteilungErfassenController Event Anmeldedatum");
        boolean equalFieldAndModelValue = equalsNullSafe(txtAnmeldedatum.getText(), kursanmeldungErfassenModel.getAnmeldedatum());
        try {
            setModelAnmeldedatum(showRequiredErrMsg);
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelAnmeldedatum(boolean showRequiredErrMsg) throws SvmValidationException {
        makeErrorLabelInvisible(Field.ANMELDEDATUM);
        try {
            kursanmeldungErfassenModel.setAnmeldedatum(txtAnmeldedatum.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("KurseinteilungErfassenController setModelAnmeldedatum RequiredException=" + e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtAnmeldedatum.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("KurseinteilungErfassenController setModelAnmeldedatum Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtAbmeldedatum(JTextField txtAbmeldedatum) {
        this.txtAbmeldedatum = txtAbmeldedatum;
        this.txtAbmeldedatum.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onAbmeldedatumEvent(true);
            }
        });
        this.txtAbmeldedatum.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onAbmeldedatumEvent(false);
            }
        });
    }

    private void onAbmeldedatumEvent(boolean showRequiredErrMsg) {
        LOGGER.trace("KurseinteilungErfassenController Event Abmeldedatum");
        boolean equalFieldAndModelValue = equalsNullSafe(txtAbmeldedatum.getText(), kursanmeldungErfassenModel.getAbmeldedatum());
        try {
            setModelAbmeldedatum(showRequiredErrMsg);
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelAbmeldedatum(boolean showRequiredErrMsg) throws SvmValidationException {
        makeErrorLabelInvisible(Field.ABMELDEDATUM);
        try {
            kursanmeldungErfassenModel.setAbmeldedatum(txtAbmeldedatum.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("KurseinteilungErfassenController setModelAbmeldedatum RequiredException=" + e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtAbmeldedatum.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("KurseinteilungErfassenController setModelAbmeldedatum Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
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
    
    public void setErrLblAnmeldedatum(JLabel errLblAnmeldedatum) {
        this.errLblAnmeldedatum = errLblAnmeldedatum;
    }

    public void setErrLblAbmeldedatum(JLabel errLblAbmeldedatum) {
        this.errLblAbmeldedatum = errLblAbmeldedatum;
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
        int n1 = 0;
        if (!isBearbeiten && kursanmeldungErfassenModel.checkIfSemesterIsInPast()) {
            Object[] options = {"Ja", "Nein"};
            n1 = JOptionPane.showOptionDialog(
                    null,
                    "Das selektierte Semester liegt in der Vergangenheit. Kursanmeldung trotzdem speichern?",
                    "Semester in Vergangenheit",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    svmContext.getDialogIcons().getWarningIcon(),
                    options,  //the titles of buttons
                    options[1]); //default button title
        }
        if (n1 == 0) {
            // Warnung wenn Anmeldedatum vor Semesterbeginn
            int n2 = 0;
            if (kursanmeldungErfassenModel.isAnmeldedatumBeforeSemesterbeginn()) {
                Object[] options = {"Ja", "Nein"};
                n2 = JOptionPane.showOptionDialog(
                        null,
                        "Das Anmeldedatum liegt vor dem Semesterbeginn. Fortfahren?",
                        "Warnung",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        svmContext.getDialogIcons().getWarningIcon(),
                        options,  //the titles of buttons
                        options[1]); //default button title
            }
            if (n2 == 0) {
                // Warnung wenn Abmeldedatum nach Semesterbeginn Folgesemester
                int n3 = 0;
                if (kursanmeldungErfassenModel.isAbmeldedatumEqualsOrAfterSemesterbeginnNaechstesSemester()) {
                    Object[] options = {"Ja", "Nein"};
                    n3 = JOptionPane.showOptionDialog(
                            null,
                            "Das Abmeldedatum liegt im Folgesemester. Fortfahren?",
                            "Warnung",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE,
                            svmContext.getDialogIcons().getWarningIcon(),
                            options,  //the titles of buttons
                            options[1]); //default button title
                }
                if (n3 == 0) {
                    // Existiert der Kurs?
                    if (!isBearbeiten && kursanmeldungErfassenModel.findKurs() == FindKursCommand.Result.KURS_EXISTIERT_NICHT) {
                        JOptionPane.showMessageDialog(kursanmeldungErfassenDialog, "Kurs existiert nicht.", "Fehler", JOptionPane.ERROR_MESSAGE, svmContext.getDialogIcons().getErrorIcon());
                        btnOk.setFocusPainted(false);
                        return;
                    }
                    // Kurs bereits erfasst
                    if (!isBearbeiten && kursanmeldungErfassenModel.checkIfKursBereitsErfasst(schuelerDatenblattModel)) {
                        JOptionPane.showMessageDialog(kursanmeldungErfassenDialog, "Kurs bereits erfasst.", "Fehler", JOptionPane.ERROR_MESSAGE, svmContext.getDialogIcons().getErrorIcon());
                        btnOk.setFocusPainted(false);
                        return;
                    }

                    // Speichern
                    CalculateAnzWochenCommand.Result speichernResult = kursanmeldungErfassenModel.speichern(kursanmeldungenTableModel, schuelerDatenblattModel);

                    // Warnung, falls Kurse unterschiedliche Anzahl Wochen haben
                    if (speichernResult == CalculateAnzWochenCommand.Result.KURSE_MIT_UNTERSCHIEDLICHER_ANZAHL_WOCHEN) {
                        JOptionPane.showMessageDialog(null, "Die Kurse des Rechnungsempfängers haben nicht alle die gleiche Anzahl Wochen.\n" +
                                "Die Semesterrechnung muss manuell nachbearbeitet werden.", "Warnung", JOptionPane.WARNING_MESSAGE, svmContext.getDialogIcons().getWarningIcon());
                    }

                    if (kursanmeldungErfassenModel.isAnmeldedatumBeforeSemesterbeginn() || kursanmeldungErfassenModel.isAbmeldedatumEqualsOrAfterSemesterbeginnNaechstesSemester()) {
                        JOptionPane.showMessageDialog(null, "Die Anzahl Wochen der Semesterrechnung kannn nicht automatisch berechnet werden.\n" +
                                "Die Semesterrechnung muss manuell nachbearbeitet werden.", "Warnung", JOptionPane.WARNING_MESSAGE, svmContext.getDialogIcons().getWarningIcon());
                    }
                }
            }

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
            comboBoxLehrkraft.setSelectedItem(kursanmeldungErfassenModel.getMitarbeiter());
        } else if (checkIsFieldChange(Field.ANMELDEDATUM, evt)) {
            txtAnmeldedatum.setText(asString(kursanmeldungErfassenModel.getAnmeldedatum()));
        } else if (checkIsFieldChange(Field.ABMELDEDATUM, evt)) {
            txtAbmeldedatum.setText(asString(kursanmeldungErfassenModel.getAbmeldedatum()));
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
        if (txtAnmeldedatum.isEnabled()) {
            LOGGER.trace("Validate field Anmeldedatum");
            setModelAnmeldedatum(true);
        }
        if (txtAbmeldedatum.isEnabled()) {
            LOGGER.trace("Validate field Abmeldedatum");
            setModelAbmeldedatum(true);
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
        if (e.getAffectedFields().contains(Field.ANMELDEDATUM)) {
            errLblAnmeldedatum.setVisible(true);
            errLblAnmeldedatum.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ABMELDEDATUM)) {
            errLblAbmeldedatum.setVisible(true);
            errLblAbmeldedatum.setText(e.getMessage());
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
        if (e.getAffectedFields().contains(Field.ANMELDEDATUM)) {
            txtAnmeldedatum.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ABMELDEDATUM)) {
            txtAbmeldedatum.setToolTipText(e.getMessage());
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
        if (fields.contains(Field.ALLE) || fields.contains(Field.ANMELDEDATUM)) {
            errLblAnmeldedatum.setVisible(false);
            txtAnmeldedatum.setToolTipText(null);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.ABMELDEDATUM)) {
            errLblAbmeldedatum.setVisible(false);
            txtAbmeldedatum.setToolTipText(null);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.BEMERKUNGEN)) {
            errLblBemerkungen.setVisible(false);
            txtBemerkungen.setToolTipText(null);
        }
    }

    @Override
    public void disableFields(boolean disable, Set<Field> fields) {}

}
