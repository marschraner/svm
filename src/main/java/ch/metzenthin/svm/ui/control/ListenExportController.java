package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.common.dataTypes.Filetyp;
import ch.metzenthin.svm.common.dataTypes.ListenExportTyp;
import ch.metzenthin.svm.common.dataTypes.Listentyp;
import ch.metzenthin.svm.common.utils.SvmProperties;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CreateListeCommand;
import ch.metzenthin.svm.domain.model.CompletedListener;
import ch.metzenthin.svm.domain.model.ListenExportModel;
import ch.metzenthin.svm.ui.componentmodel.KurseTableModel;
import ch.metzenthin.svm.ui.componentmodel.MitarbeitersTableModel;
import ch.metzenthin.svm.ui.componentmodel.SchuelerSuchenTableModel;
import ch.metzenthin.svm.ui.componentmodel.SemesterrechnungenTableModel;
import org.apache.log4j.Logger;

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
public class ListenExportController extends AbstractController {

    private static final Logger LOGGER = Logger.getLogger(ListenExportController.class);

    // Möglichkeit zum Umschalten des validation modes (nicht dynamisch)
    private static final boolean MODEL_VALIDATION_MODE = false;

    private ListenExportModel listenExportModel;
    private final SchuelerSuchenTableModel schuelerSuchenTableModel;
    private final MitarbeitersTableModel mitarbeitersTableModel;
    private final KurseTableModel kurseTableModel;
    private SemesterrechnungenTableModel semesterrechnungenTableModel;
    private final ListenExportTyp listenExportTyp;
    private JDialog listenExportDialog;
    private JComboBox<Listentyp> comboBoxListentyp;
    private JTextField txtTitel;
    private JLabel errLblListentyp;
    private JLabel errLblTitel;
    private JButton btnOk;
    private JButton btnAbbrechen;

    public ListenExportController(ListenExportModel listenExportModel, SchuelerSuchenTableModel schuelerSuchenTableModel, MitarbeitersTableModel mitarbeitersTableModel, KurseTableModel kurseTableModel, SemesterrechnungenTableModel semesterrechnungenTableModel, ListenExportTyp listenExportTyp) {
        super(listenExportModel);
        this.listenExportModel = listenExportModel;
        this.schuelerSuchenTableModel = schuelerSuchenTableModel;
        this.mitarbeitersTableModel = mitarbeitersTableModel;
        this.kurseTableModel = kurseTableModel;
        this.semesterrechnungenTableModel = semesterrechnungenTableModel;
        this.listenExportTyp = listenExportTyp;
        this.listenExportModel.addPropertyChangeListener(this);
        this.listenExportModel.addDisableFieldsListener(this);
        this.listenExportModel.addMakeErrorLabelsInvisibleListener(this);
        this.listenExportModel.addCompletedListener(new CompletedListener() {
            @Override
            public void completed(boolean completed) {
                onListenExportModelCompleted(completed);
            }
        });
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

    public void setComboBoxListentyp(JComboBox<Listentyp> comboBoxListentyp) {
        this.comboBoxListentyp = comboBoxListentyp;
        comboBoxListentyp.setModel(new DefaultComboBoxModel<>(Listentyp.values()));
        comboBoxListentyp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onListentypSelected();
            }
        });
        initComboBoxListentyp();
    }

    private void onListentypSelected() {
        LOGGER.trace("ListenExportController Event Listentyp selected=" + comboBoxListentyp.getSelectedItem());
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
            LOGGER.trace("KursErfassenController setModelListentyp RequiredException=" + e.getMessage());
            if (isModelValidationMode()) {
                comboBoxListentyp.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        }
    }

    public void initComboBoxListentyp() {
        if (listenExportTyp == ListenExportTyp.SCHUELER) {
            if (schuelerSuchenTableModel.getWochentag() == null || schuelerSuchenTableModel.getZeitBeginn() == null || schuelerSuchenTableModel.getLehrkraft() == null) {
                // Keine Absenzenlisten, falls in Suche nicht nach einem spezifischen Kurs gesucht wurde
                comboBoxListentyp.removeItem(Listentyp.SCHUELER_ABSENZENLISTE);
            }
            if (schuelerSuchenTableModel.getMaerchen() == null || schuelerSuchenTableModel.getMaercheneinteilungen().size() == 0) {
                // Keine Märchenlisten, falls kein Märchen oder keine Märcheneinteilungen
                comboBoxListentyp.removeItem(Listentyp.ROLLENLISTE);
                comboBoxListentyp.removeItem(Listentyp.ELTERNMITHILFE_LISTE);
                comboBoxListentyp.removeItem(Listentyp.ELTERNMITHILFE_ADRESSETIKETTEN);
            }
            if (schuelerSuchenTableModel.getSchuelerList().size() == 0) {
                // Keine Adressliste und Etiketten, falls keine Schüler (bei leerer Kurstabelle)
                comboBoxListentyp.removeItem(Listentyp.SCHUELER_ADRESSLISTE);
                comboBoxListentyp.removeItem(Listentyp.SCHUELER_ADRESSETIKETTEN);
                comboBoxListentyp.removeItem(Listentyp.RECHNUNGSEMPFAENGER_ADRESSETIKETTEN);
                comboBoxListentyp.removeItem(Listentyp.MUTTER_ODER_VATER_ADRESSETIKETTEN);
                comboBoxListentyp.removeItem(Listentyp.ELTERNMITHILFE_ADRESSETIKETTEN);
                comboBoxListentyp.setSelectedItem(Listentyp.SCHUELER_ABSENZENLISTE);
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
        } else {
            comboBoxListentyp.removeItem(Listentyp.SCHUELER_ADRESSLISTE);
            comboBoxListentyp.removeItem(Listentyp.SCHUELER_ABSENZENLISTE);
            comboBoxListentyp.removeItem(Listentyp.ROLLENLISTE);
            comboBoxListentyp.removeItem(Listentyp.ELTERNMITHILFE_LISTE);
            comboBoxListentyp.removeItem(Listentyp.SCHUELER_ADRESSETIKETTEN);
            comboBoxListentyp.removeItem(Listentyp.RECHNUNGSEMPFAENGER_ADRESSETIKETTEN);
            comboBoxListentyp.removeItem(Listentyp.MUTTER_ODER_VATER_ADRESSETIKETTEN);
            comboBoxListentyp.removeItem(Listentyp.ELTERNMITHILFE_ADRESSETIKETTEN);
        }
        if (listenExportTyp == ListenExportTyp.MITARBEITERS) {
            // Initialisierung
            comboBoxListentyp.setSelectedItem(Listentyp.LEHRKRAEFTE_ADRESSLISTE);
        } else {
            comboBoxListentyp.removeItem(Listentyp.LEHRKRAEFTE_ADRESSLISTE);
            comboBoxListentyp.removeItem(Listentyp.MITARBEITER_LISTE);
            comboBoxListentyp.removeItem(Listentyp.MITARBEITER_ADRESSETIKETTEN);
        }
        if (listenExportTyp == ListenExportTyp.KURSE) {
            // Initialisierung
            comboBoxListentyp.setSelectedItem(Listentyp.KURSLISTE_WORD);
        } else {
            comboBoxListentyp.removeItem(Listentyp.KURSLISTE_WORD);
            comboBoxListentyp.removeItem(Listentyp.KURSLISTE_CSV);
        }
        if (listenExportTyp == ListenExportTyp.SEMESTERRECHNUNGEN) {
            // Initialisierung / Deaktivierungen, falls Rechnungsdatum nicht gesetzt
            boolean rechnungsdatumVorrechnungUeberallGesetzt = listenExportModel.checkIfRechnungsdatumVorrechnungUeberallGesetzt(semesterrechnungenTableModel);
            boolean rechnungsdatumNachrechnungUeberallGesetzt = listenExportModel.checkIfRechnungsdatumNachrechnungUeberallGesetzt(semesterrechnungenTableModel);
            if (!rechnungsdatumVorrechnungUeberallGesetzt) {
                comboBoxListentyp.removeItem(Listentyp.VORRECHNUNGEN_SERIENBRIEF);
                comboBoxListentyp.setSelectedItem(Listentyp.RECHNUNGSLISTE);
            } else {
                comboBoxListentyp.setSelectedItem(Listentyp.VORRECHNUNGEN_SERIENBRIEF);
            }
            if (!rechnungsdatumNachrechnungUeberallGesetzt) {
                comboBoxListentyp.removeItem(Listentyp.NACHRECHNUNGEN_SERIENBRIEF);
            }
            if (!rechnungsdatumVorrechnungUeberallGesetzt && !rechnungsdatumNachrechnungUeberallGesetzt) {
                comboBoxListentyp.removeItem(Listentyp.MAHNUNGEN_SERIENBRIEF);
            }
        } else {
            comboBoxListentyp.removeItem(Listentyp.VORRECHNUNGEN_SERIENBRIEF);
            comboBoxListentyp.removeItem(Listentyp.NACHRECHNUNGEN_SERIENBRIEF);
            comboBoxListentyp.removeItem(Listentyp.MAHNUNGEN_SERIENBRIEF);
            comboBoxListentyp.removeItem(Listentyp.RECHNUNGSLISTE);
        }
    }
    
    public void setTxtTitel(JTextField txtTitel) {
        this.txtTitel = txtTitel;
        if (listenExportTyp == ListenExportTyp.SEMESTERRECHNUNGEN) {
            this.txtTitel.setEnabled(false);
        }
        this.txtTitel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onTitelEvent(true);
            }
        });
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
            LOGGER.trace("ListenExportController setModelTitel RequiredException=" + e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtTitel.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("ListenExportController setModelTitel Exception=" + e.getMessage());
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
        this.btnOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onOk();
            }
        });
    }

    private void onOk() {
        if (!isModelValidationMode() && !validateOnSpeichern()) {
            btnOk.setFocusPainted(false);
            return;
        }
        enableButtons(false);
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
                    null,     //do not use a custom Icon
                    options,  //the titles of buttons
                    options[1]); //default button title
            if (n != 0) {
                btnOk.setFocusPainted(false);
                enableButtons(true);
                return;
            }
        }
        // Ouput-File erzeugen
        final JDialog dialog = new JDialog(listenExportDialog);
        dialog.setUndecorated(true);
        JPanel panel = new JPanel();
        final JLabel label = new JLabel("Die Datei wird erstellt. Bitte warten ...");
        panel.add(label);
        dialog.add(panel);
        // Public method to center the dialog after calling pack()
        dialog.pack();
        dialog.setLocationRelativeTo(listenExportDialog);
        SwingWorker<CreateListeCommand.Result, String> worker = new SwingWorker<CreateListeCommand.Result, String>() {
            @Override
            protected CreateListeCommand.Result doInBackground() throws Exception {
                return listenExportModel.createListenFile(outputFile, schuelerSuchenTableModel, mitarbeitersTableModel, kurseTableModel, semesterrechnungenTableModel);
            }
            @Override
            protected void done() {
                dialog.dispose();
                CreateListeCommand.Result result = null;
                try {
                    result = get();
                } catch (InterruptedException | ExecutionException e) {
                    LOGGER.warn("Die Liste konnte nicht erstellt werden:" + e.getMessage());
                    JOptionPane.showMessageDialog(listenExportDialog, "Die Liste konnte nicht erstellt werden.", "Liste nicht erfolgreich erstellt", JOptionPane.ERROR_MESSAGE);
                }
                if (result != null) {
                    switch (result) {
                        case TEMPLATE_FILE_EXISTIERT_NICHT_ODER_NICHT_LESBAR:
                            JOptionPane.showMessageDialog(listenExportDialog, "Template-Datei '" + listenExportModel.getTemplateFile() + "' nicht gefunden. Bitte Template-Datei erstellen.", "Fehler", JOptionPane.ERROR_MESSAGE);
                            break;
                        case FEHLER_BEIM_LESEN_DES_PROPERTY_FILE:
                            JOptionPane.showMessageDialog(listenExportDialog, "Fehler beim Lesen der Svm-Property-Datei '" + SvmProperties.SVM_PROPERTIES_FILE_NAME
                                    + "'. \nDie Datei existiert nicht oder der Eintrag für die Template-Datei fehlt. Bitte Datei überprüfen.", "Fehler", JOptionPane.ERROR_MESSAGE);
                            break;
                        case LISTE_ERFOLGREICH_ERSTELLT:
                            JOptionPane.showMessageDialog(listenExportDialog, "Die Liste wurde erfolgreich erstellt.", "Liste erfolgreich erstellt", JOptionPane.INFORMATION_MESSAGE);
                            break;
                    }
                } else {
                    JOptionPane.showMessageDialog(listenExportDialog, "Die Liste konnte nicht erstellt werden.", "Es konnte kein Resultat ermittelt werden.", JOptionPane.ERROR_MESSAGE);
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
        btnAbbrechen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onAbbrechen();
            }
        });
    }

    private void onAbbrechen() {
        listenExportDialog.dispose();
    }

    private void onListenExportModelCompleted(boolean completed) {
        LOGGER.trace("ListenExportModel completed=" + completed);
        if (completed) {
            btnOk.setToolTipText(null);
            btnOk.setEnabled(true);
        } else {
            btnOk.setToolTipText("Bitte Eingabedaten vervollständigen");
            btnOk.setEnabled(false);
        }
    }

    void enableDisableFields() {
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
        if (e.getAffectedFields().contains(Field.LISTENTYP)) {
            if (errLblListentyp != null) {
                errLblListentyp.setVisible(true);
                errLblListentyp.setText(e.getMessage());
            }
        }
        if (e.getAffectedFields().contains(Field.TITEL)) {
            if (errLblTitel != null) {
                errLblTitel.setVisible(true);
                errLblTitel.setText(e.getMessage());
            }
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
