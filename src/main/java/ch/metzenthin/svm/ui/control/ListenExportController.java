package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.datatypes.*;
import ch.metzenthin.svm.common.utils.SvmProperties;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CreateListeCommand;
import ch.metzenthin.svm.domain.model.ListenExportModel;
import ch.metzenthin.svm.ui.componentmodel.KurseTableModel;
import ch.metzenthin.svm.ui.componentmodel.MitarbeitersTableModel;
import ch.metzenthin.svm.ui.componentmodel.SchuelerSuchenTableModel;
import ch.metzenthin.svm.ui.componentmodel.SemesterrechnungenTableModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static ch.metzenthin.svm.common.utils.SimpleValidator.equalsNullSafe;

/**
 * @author Martin Schraner
 */
@SuppressWarnings("LoggingSimilarMessage")
public class ListenExportController extends AbstractController {

    private static final Logger LOGGER = LogManager.getLogger(ListenExportController.class);

    // Möglichkeit zum Umschalten des validation modes (nicht dynamisch)
    private static final boolean MODEL_VALIDATION_MODE = false;

    private final ListenExportModel listenExportModel;
    private final SchuelerSuchenTableModel schuelerSuchenTableModel;
    private final MitarbeitersTableModel mitarbeitersTableModel;
    private final KurseTableModel kurseTableModel;
    private final SemesterrechnungenTableModel semesterrechnungenTableModel;
    private final ListenExportTyp listenExportTyp;
    private final boolean defaultButtonEnabled;
    private JDialog listenExportDialog;
    private JComboBox<Listentyp> comboBoxListentyp;
    private JTextField txtTitel;
    private JLabel errLblListentyp;
    private JLabel errLblTitel;
    private JButton btnOk;
    private JButton btnAbbrechen;

    public ListenExportController(ListenExportModel listenExportModel, SchuelerSuchenTableModel schuelerSuchenTableModel, MitarbeitersTableModel mitarbeitersTableModel, KurseTableModel kurseTableModel, SemesterrechnungenTableModel semesterrechnungenTableModel, ListenExportTyp listenExportTyp, boolean defaultButtonEnabled) {
        super(listenExportModel);
        this.listenExportModel = listenExportModel;
        this.schuelerSuchenTableModel = schuelerSuchenTableModel;
        this.mitarbeitersTableModel = mitarbeitersTableModel;
        this.kurseTableModel = kurseTableModel;
        this.semesterrechnungenTableModel = semesterrechnungenTableModel;
        this.listenExportTyp = listenExportTyp;
        this.defaultButtonEnabled = defaultButtonEnabled;
        this.listenExportModel.addPropertyChangeListener(this);
        this.listenExportModel.addDisableFieldsListener(this);
        this.listenExportModel.addMakeErrorLabelsInvisibleListener(this);
        this.listenExportModel.addCompletedListener(this::onListenExportModelCompleted);
        this.setModelValidationMode(MODEL_VALIDATION_MODE);
    }

    public void constructionDone() {
        listenExportModel.initializeCompleted();
    }

    public void setListenExportDialog(JDialog listenExportDialog) {
        // call onCancel() when cross is clicked
        this.listenExportDialog = listenExportDialog;
        listenExportDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        listenExportDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onAbbrechen();
            }
        });
    }

    public void setContentPane(JPanel contentPane) {
        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onAbbrechen(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public void setComboBoxListentyp(JComboBox<Listentyp> comboBoxListentyp) {
        this.comboBoxListentyp = comboBoxListentyp;
        comboBoxListentyp.setModel(new DefaultComboBoxModel<>(Listentyp.values()));
        comboBoxListentyp.addActionListener(e -> onListentypSelected());
        initComboBoxListentyp();
    }

    private void onListentypSelected() {
        LOGGER.trace("ListenExportController Event Listentyp selected={}", comboBoxListentyp.getSelectedItem());
        boolean equalFieldAndModelValue = equalsNullSafe(comboBoxListentyp.getSelectedItem(), listenExportModel.getListentyp());
        try {
            setModelListentyp();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelListentyp() throws SvmRequiredException {
        makeErrorLabelInvisible(Field.LISTENTYP);
        try {
            listenExportModel.setListentyp((Listentyp) comboBoxListentyp.getSelectedItem());
        } catch (SvmRequiredException e) {
            LOGGER.trace("KursErfassenController setModelListentyp RequiredException={}", e.getMessage());
            if (isModelValidationMode()) {
                comboBoxListentyp.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut, nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        }
    }

    @SuppressWarnings("java:S3776")
    private void initComboBoxListentyp() {
        if (listenExportTyp == ListenExportTyp.SCHUELER) {
            if (!schuelerSuchenTableModel.isKursFuerSucheBeruecksichtigen() || schuelerSuchenTableModel.getWochentag() == null || schuelerSuchenTableModel.getZeitBeginn() == null || schuelerSuchenTableModel.getLehrkraft() == null) {
                // Keine Absenzenlisten, falls nicht nach einem spezifischen Kurs gesucht wurde
                comboBoxListentyp.removeItem(Listentyp.ABSENZENLISTE_GANZES_SEMESTER);
                comboBoxListentyp.removeItem(Listentyp.ABSENZENLISTE_OKTOBER_FEBRUAR);
                comboBoxListentyp.removeItem(Listentyp.SPEZIELLE_ABSENZENLISTE);
            }
            // Keine Absenzenliste Oktober - Februar, falls 2. Semester
            if (schuelerSuchenTableModel.getSemester() == null || schuelerSuchenTableModel.getSemester().getSemesterbezeichnung() == Semesterbezeichnung.ZWEITES_SEMESTER) {
                comboBoxListentyp.removeItem(Listentyp.ABSENZENLISTE_OKTOBER_FEBRUAR);
            }
            if (schuelerSuchenTableModel.getMaerchen() == null || schuelerSuchenTableModel.getAnzZuExportierendeMaercheneinteilungen() == 0) {
                // Keine Märchenlisten, falls kein Märchen oder keine Märcheneinteilungen
                comboBoxListentyp.removeItem(Listentyp.ROLLENLISTE);
                comboBoxListentyp.removeItem(Listentyp.ELTERNMITHILFE_LISTE);
                comboBoxListentyp.removeItem(Listentyp.ELTERNMITHILFE_ADRESSETIKETTEN);
                comboBoxListentyp.removeItem(Listentyp.PROBEPLAENE_ETIKETTEN);
            }
            if (schuelerSuchenTableModel.getSchuelerList().isEmpty()) {
                // Keine Adressliste und Etiketten, falls keine Schüler (bei leerer Kurstabelle)
                comboBoxListentyp.removeItem(Listentyp.SCHUELER_ADRESSLISTE);
                comboBoxListentyp.removeItem(Listentyp.SCHUELER_ADRESSETIKETTEN);
                comboBoxListentyp.removeItem(Listentyp.RECHNUNGSEMPFAENGER_ADRESSETIKETTEN);
                comboBoxListentyp.removeItem(Listentyp.MUTTER_ODER_VATER_ADRESSETIKETTEN);
                comboBoxListentyp.removeItem(Listentyp.ELTERNMITHILFE_ADRESSETIKETTEN);
                comboBoxListentyp.removeItem(Listentyp.PROBEPLAENE_ETIKETTEN);
                comboBoxListentyp.setSelectedItem(Listentyp.ABSENZENLISTE_GANZES_SEMESTER);
            } else {
                // Initialisierung
                if (schuelerSuchenTableModel.getElternmithilfeCode() != null) {
                    comboBoxListentyp.setSelectedItem(Listentyp.ELTERNMITHILFE_LISTE);
                } else if (schuelerSuchenTableModel.isMaerchenFuerSucheBeruecksichtigen()) {
                    comboBoxListentyp.setSelectedItem(Listentyp.ROLLENLISTE);
                } else {
                    comboBoxListentyp.setSelectedItem(Listentyp.SCHUELER_ADRESSLISTE);
                }
            }
            // Keine Schülerliste und keine Rechnungsempfänger-Adressetiketten, falls nach Märchen gesucht
            if (schuelerSuchenTableModel.isMaerchenFuerSucheBeruecksichtigen() && !schuelerSuchenTableModel.isKursFuerSucheBeruecksichtigen()) {
                comboBoxListentyp.removeItem(Listentyp.SCHUELER_ADRESSLISTE);
                comboBoxListentyp.removeItem(Listentyp.RECHNUNGSEMPFAENGER_ADRESSETIKETTEN);
                comboBoxListentyp.removeItem(Listentyp.SCHUELERLISTE_CSV);
            }
            // Keine Rollen- und Elternmithilfeliste, falls nicht nach Märchen gesucht
            if (!schuelerSuchenTableModel.isMaerchenFuerSucheBeruecksichtigen()) {
                comboBoxListentyp.removeItem(Listentyp.ROLLENLISTE);
                comboBoxListentyp.removeItem(Listentyp.ELTERNMITHILFE_LISTE);
                comboBoxListentyp.removeItem(Listentyp.ELTERNMITHILFE_ADRESSETIKETTEN);
                comboBoxListentyp.removeItem(Listentyp.PROBEPLAENE_ETIKETTEN);
            }
        } else {
            comboBoxListentyp.removeItem(Listentyp.SCHUELER_ADRESSLISTE);
            comboBoxListentyp.removeItem(Listentyp.ABSENZENLISTE_GANZES_SEMESTER);
            comboBoxListentyp.removeItem(Listentyp.ABSENZENLISTE_OKTOBER_FEBRUAR);
            comboBoxListentyp.removeItem(Listentyp.SPEZIELLE_ABSENZENLISTE);
            comboBoxListentyp.removeItem(Listentyp.ROLLENLISTE);
            comboBoxListentyp.removeItem(Listentyp.ELTERNMITHILFE_LISTE);
            comboBoxListentyp.removeItem(Listentyp.SCHUELER_ADRESSETIKETTEN);
            comboBoxListentyp.removeItem(Listentyp.RECHNUNGSEMPFAENGER_ADRESSETIKETTEN);
            comboBoxListentyp.removeItem(Listentyp.MUTTER_ODER_VATER_ADRESSETIKETTEN);
            comboBoxListentyp.removeItem(Listentyp.ELTERNMITHILFE_ADRESSETIKETTEN);
            comboBoxListentyp.removeItem(Listentyp.PROBEPLAENE_ETIKETTEN);
            comboBoxListentyp.removeItem(Listentyp.SCHUELERLISTE_CSV);
        }
        if (listenExportTyp == ListenExportTyp.MITARBEITERS) {
            // Initialisierung
            comboBoxListentyp.setSelectedItem(Listentyp.MITARBEITER_ADRESSLISTE_MIT_GEBURTSDATUM);
        } else {
            comboBoxListentyp.removeItem(Listentyp.MITARBEITER_ADRESSLISTE_OHNE_GEBURTSDATUM);
            comboBoxListentyp.removeItem(Listentyp.MITARBEITER_ADRESSLISTE_MIT_GEBURTSDATUM);
            comboBoxListentyp.removeItem(Listentyp.VERTRETUNGSLISTE);
            comboBoxListentyp.removeItem(Listentyp.MITARBEITER_ADRESSLISTE_MIT_GEBURTSDATUM_AHV_IBAN_VERTRETUNGSMOEGLICHKEITEN);
            comboBoxListentyp.removeItem(Listentyp.MITARBEITER_ADRESSETIKETTEN);
            comboBoxListentyp.removeItem(Listentyp.MITARBEITER_LISTE_NAME_ZWEISPALTIG_CSV);
            comboBoxListentyp.removeItem(Listentyp.MITARBEITER_LISTE_NAME_EINSPALTIG_CSV);
        }
        if (listenExportTyp == ListenExportTyp.KURSE) {
            // Initialisierung
            comboBoxListentyp.setSelectedItem(Listentyp.KURSLISTE_WORD);
        } else {
            comboBoxListentyp.removeItem(Listentyp.KURSLISTE_WORD);
            comboBoxListentyp.removeItem(Listentyp.KURSLISTE_CSV);
        }
        if (listenExportTyp == ListenExportTyp.SEMESTERRECHNUNGEN) {
            comboBoxListentyp.setSelectedItem(Listentyp.VORRECHNUNGEN_SERIENBRIEF);
        } else {
            comboBoxListentyp.removeItem(Listentyp.VORRECHNUNGEN_SERIENBRIEF);
            comboBoxListentyp.removeItem(Listentyp.NACHRECHNUNGEN_SERIENBRIEF);
            comboBoxListentyp.removeItem(Listentyp.MAHNUNGEN_VORRECHNUNGEN_SERIENBRIEF);
            comboBoxListentyp.removeItem(Listentyp.MAHNUNGEN_NACHRECHNUNGEN_SERIENBRIEF);
            comboBoxListentyp.removeItem(Listentyp.SEMESTERRECHNUNGEN_ADRESSETIKETTEN);
            comboBoxListentyp.removeItem(Listentyp.RECHNUNGSLISTE);
        }
    }

    public void setTxtTitel(JTextField txtTitel) {
        this.txtTitel = txtTitel;
        if (listenExportTyp == ListenExportTyp.SEMESTERRECHNUNGEN) {
            this.txtTitel.setEnabled(false);
        }
        if (!defaultButtonEnabled) {
            this.txtTitel.addActionListener(e -> onTitelEvent(true));
        }
        this.txtTitel.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onTitelEvent(false);
            }
        });
        initTitel();
    }

    private void initTitel() {
        // Wird auch nach jeder neuen Selektion des Listentyps aufgerufen
        if (txtTitel == null || listenExportModel.getListentyp() == null) {
            return;
        }
        String titleInit = listenExportModel.getTitleInit(schuelerSuchenTableModel);
        txtTitel.setText(titleInit);
    }

    private void onTitelEvent(boolean showRequiredErrMsg) {
        LOGGER.trace("ListenExportController Event Titel");
        boolean equalFieldAndModelValue = equalsNullSafe(txtTitel.getText(), listenExportModel.getTitel());
        try {
            setModelTitel(showRequiredErrMsg);
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelTitel(boolean showRequiredErrMsg) throws SvmValidationException {
        makeErrorLabelInvisible(Field.TITEL);
        try {
            listenExportModel.setTitel(txtTitel.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("ListenExportController setModelTitel RequiredException={}", e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtTitel.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut, nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("ListenExportController setModelTitel Exception={}", e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setErrLblListentyp(JLabel errLblListentyp) {
        this.errLblListentyp = errLblListentyp;
    }

    public void setErrLblTitel(JLabel errLblTitel) {
        this.errLblTitel = errLblTitel;
    }

    public void setBtnOk(JButton btnOk) {
        this.btnOk = btnOk;
        if (isModelValidationMode()) {
            btnOk.setEnabled(false);
        }
        this.btnOk.addActionListener(e -> onOk());
    }

    @SuppressWarnings("java:S3776")
    private void onOk() {
        if (!isModelValidationMode() && !validateOnSpeichern()) {
            btnOk.setFocusPainted(false);
            return;
        }
        enableButtons(false);
        // Vorprüfung
        String[] listenErstellenWarning = listenExportModel.getListenErstellenWarning(semesterrechnungenTableModel);
        if (listenErstellenWarning != null) {
            Object[] options = {"Ja", "Nein"};
            int n = JOptionPane.showOptionDialog(
                    listenExportDialog,
                    listenErstellenWarning[0],
                    listenErstellenWarning[1],
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null,
                    options,  //the titles of buttons
                    options[1]); //default button title
            if (n != 0) {
                btnOk.setFocusPainted(false);
                enableButtons(true);
                return;
            }
        }
        // Speichern-Dialog
        JFileChooser fileChooser = setupFileChooser();
        int returnVal = fileChooser.showSaveDialog(listenExportDialog);
        if (returnVal != JFileChooser.APPROVE_OPTION) {
            btnOk.setFocusPainted(false);
            enableButtons(true);
            return;
        }
        // Prüfen, ob selektiertes Output-File schon existiert
        final File outputFile = fileChooser.getSelectedFile();
        if (outputFile.exists()) {
            Object[] options = {"Ja", "Nein"};
            int n = JOptionPane.showOptionDialog(
                    listenExportDialog,
                    "Die Datei '" + outputFile.getName() + "' existiert bereits. Soll sie überschrieben werden?",
                    "Datei existiert bereits",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,  //the titles of buttons
                    options[1]); //default button title
            if (n != 0) {
                btnOk.setFocusPainted(false);
                enableButtons(true);
                return;
            }
        }
        // Output-File erzeugen
        final JDialog dialog = new JDialog(listenExportDialog);
        dialog.setModal(true);
        // Kein Maximierung-Button
        dialog.setResizable(false);
        // Schliessen soll keinen Effekt haben
        dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        dialog.setTitle("Datei wird erstellt");
        final JOptionPane optionPane = new JOptionPane("Die Datei wird erstellt. Bitte warten ...", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
        dialog.setContentPane(optionPane);
        // Public method to center the dialog after calling pack()
        dialog.pack();
        dialog.setLocationRelativeTo(listenExportDialog);
        SwingWorker<CreateListeCommand.Result, String> worker = new SwingWorker<>() {
            @Override
            protected CreateListeCommand.Result doInBackground() {
                return listenExportModel.createListenFile(outputFile, schuelerSuchenTableModel, mitarbeitersTableModel, kurseTableModel, semesterrechnungenTableModel);
            }

            @Override
            protected void done() {
                dialog.dispose();
                CreateListeCommand.Result result = null;
                try {
                    result = get();
                } catch (InterruptedException | ExecutionException e) {
                    LOGGER.warn("Die Liste konnte nicht erstellt werden:{}", e.getMessage());
                    JOptionPane.showMessageDialog(
                            listenExportDialog,
                            "Die Liste konnte nicht erstellt werden.",
                            "Liste nicht erfolgreich erstellt",
                            JOptionPane.ERROR_MESSAGE);
                    if (e instanceof InterruptedException) {
                        Thread.currentThread().interrupt();
                    }
                }
                if (result != null) {
                    switch (result) {
                        case TEMPLATE_FILE_EXISTIERT_NICHT_ODER_NICHT_LESBAR ->
                                JOptionPane.showMessageDialog(
                                        listenExportDialog,
                                        "Template-Datei '" +
                                                listenExportModel.getTemplateFile() +
                                                "' nicht gefunden." +
                                        "\nBitte Template-Datei erstellen.",
                                        "Fehler", JOptionPane.ERROR_MESSAGE);
                        case FEHLER_BEIM_LESEN_DES_PROPERTY_FILE ->
                                JOptionPane.showMessageDialog(
                                        listenExportDialog,
                                        "Fehler beim Lesen der Svm-Property-Datei '" +
                                                SvmProperties.SVM_PROPERTIES_FILE_NAME +
                                                "'. \nDie Datei existiert nicht oder der Eintrag für die Template-Datei fehlt. Bitte Datei überprüfen.",
                                        "Fehler", JOptionPane.ERROR_MESSAGE);
                        case LISTE_ERFOLGREICH_ERSTELLT ->
                                JOptionPane.showMessageDialog(
                                        listenExportDialog,
                                        "Die Liste wurde erfolgreich erstellt.",
                                        "Liste erfolgreich erstellt",
                                        JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(
                            listenExportDialog,
                            "Die Liste konnte nicht erstellt werden.",
                            "Es konnte kein Resultat ermittelt werden.",
                            JOptionPane.ERROR_MESSAGE);
                }
                listenExportDialog.dispose();
            }
        };
        worker.execute();
        dialog.setVisible(true);
    }

    private void enableButtons(boolean enable) {
        btnOk.setEnabled(enable);
        btnAbbrechen.setEnabled(enable);
    }

    private JFileChooser setupFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        if (listenExportModel.getSaveFileInit() != null) {
            fileChooser.setSelectedFile(listenExportModel.getSaveFileInit());
        }
        final Filetyp filetyp = listenExportModel.getListentyp().getFiletyp();
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter(filetyp.getBezeichnung() + "-Dateien (*." + filetyp.getFileExtension() + ")", filetyp.getFileExtension()));
        fileChooser.setAcceptAllFileFilterUsed(true);
        return fileChooser;
    }

    public void setBtnAbbrechen(JButton btnAbbrechen) {
        this.btnAbbrechen = btnAbbrechen;
        btnAbbrechen.addActionListener(e -> onAbbrechen());
    }

    private void onAbbrechen() {
        listenExportDialog.dispose();
    }

    private void onListenExportModelCompleted(boolean completed) {
        LOGGER.trace("ListenExportModel completed={}", completed);
        if (completed) {
            btnOk.setToolTipText(null);
            btnOk.setEnabled(true);
        } else {
            btnOk.setToolTipText("Bitte Eingabedaten vervollständigen");
            btnOk.setEnabled(false);
        }
    }

    private void enableDisableFields() {
        if (listenExportModel.getListentyp() != null && listenExportModel.getListentyp().getFiletyp() == Filetyp.CSV) {
            disableTitel();
        } else {
            enableTitel();
        }
    }

    private void enableTitel() {
        listenExportModel.enableFields(getTitelField());
    }

    private void disableTitel() {
        listenExportModel.disableFields(getTitelField());
        listenExportModel.makeErrorLabelsInvisible(getTitelField());
    }

    private Set<Field> getTitelField() {
        Set<Field> titelField = new HashSet<>();
        titelField.add(Field.TITEL);
        return titelField;
    }

    @Override
    void doPropertyChange(PropertyChangeEvent evt) {
        enableDisableFields();
        super.doPropertyChange(evt);
        if (checkIsFieldChange(Field.LISTENTYP, evt)) {
            comboBoxListentyp.setSelectedItem(listenExportModel.getListentyp());
            initTitel();
        } else if (checkIsFieldChange(Field.TITEL, evt)) {
            txtTitel.setText(listenExportModel.getTitel());
        }
    }

    @Override
    void validateFields() throws SvmValidationException {
        if (comboBoxListentyp.isEnabled()) {
            LOGGER.trace("Validate field Listentyp");
            setModelListentyp();
        }
        if (txtTitel.isEnabled()) {
            LOGGER.trace("Validate field Titel");
            setModelTitel(true);
        }
    }

    @Override
    void showErrMsg(SvmValidationException e) {
        if (e.getAffectedFields().contains(Field.LISTENTYP) && errLblListentyp != null) {
                errLblListentyp.setVisible(true);
                errLblListentyp.setText(e.getMessage());
            }

        if (e.getAffectedFields().contains(Field.TITEL) && errLblTitel != null) {
                errLblTitel.setVisible(true);
                errLblTitel.setText(e.getMessage());
            }
    }

    @Override
    void showErrMsgAsToolTip(SvmValidationException e) {
        if (e.getAffectedFields().contains(Field.LISTENTYP)) {
            comboBoxListentyp.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.TITEL)) {
            txtTitel.setToolTipText(e.getMessage());
        }
    }

    @Override
    public void makeErrorLabelsInvisible(Set<Field> fields) {
        if (fields.contains(Field.ALLE) || fields.contains(Field.LISTENTYP)) {
            if (errLblListentyp != null) {
                errLblListentyp.setVisible(false);
            }
            if (comboBoxListentyp != null) {
                comboBoxListentyp.setToolTipText(null);
            }
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.TITEL)) {
            if (errLblTitel != null) {
                errLblTitel.setVisible(false);
            }
            if (txtTitel != null) {
                txtTitel.setToolTipText(null);
            }
        }
    }

    @Override
    public void disableFields(boolean disable, Set<Field> fields) {
        if (comboBoxListentyp != null && (fields.contains(Field.ALLE) || fields.contains(Field.LISTENTYP))) {
            comboBoxListentyp.setEnabled(!disable);
        }
        if (txtTitel != null && (fields.contains(Field.ALLE) || fields.contains(Field.TITEL))) {
            txtTitel.setEnabled(!disable);
        }
    }

}
