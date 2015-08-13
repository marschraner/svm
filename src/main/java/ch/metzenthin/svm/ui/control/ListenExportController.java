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
import ch.metzenthin.svm.ui.componentmodel.LehrkraefteTableModel;
import ch.metzenthin.svm.ui.componentmodel.SchuelerSuchenTableModel;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static ch.metzenthin.svm.common.utils.Converter.asString;
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
    private final LehrkraefteTableModel lehrkraefteTableModel;
    private final KurseTableModel kurseTableModel;
    private final ListenExportTyp listenExportTyp;
    private JDialog listenExportDialog;
    private JComboBox<Listentyp> comboBoxListentyp;
    private JTextField txtTitel;
    private JLabel errLblListentyp;
    private JLabel errLblTitel;
    private JButton btnOk;

    public ListenExportController(ListenExportModel listenExportModel, SchuelerSuchenTableModel schuelerSuchenTableModel, LehrkraefteTableModel lehrkraefteTableModel, KurseTableModel kurseTableModel, ListenExportTyp listenExportTyp) {
        super(listenExportModel);
        this.listenExportModel = listenExportModel;
        this.schuelerSuchenTableModel = schuelerSuchenTableModel;
        this.lehrkraefteTableModel = lehrkraefteTableModel;
        this.kurseTableModel = kurseTableModel;
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
        if (listenExportTyp == ListenExportTyp.SCHUELER) {
            if (schuelerSuchenTableModel.getWochentag() == null || schuelerSuchenTableModel.getZeitBeginn() == null || schuelerSuchenTableModel.getLehrkraft() == null) {
                // Keine Absenzenlisten, falls in Suche nicht nach einem spezifischen Kurs gesucht wurde
                comboBoxListentyp.removeItem(Listentyp.SCHUELER_ABSENZENLISTE);
            }
            // Initialisierung
            comboBoxListentyp.setSelectedItem(Listentyp.SCHUELER_ADRESSLISTE);
        } else {
            comboBoxListentyp.removeItem(Listentyp.SCHUELER_ADRESSLISTE);
            comboBoxListentyp.removeItem(Listentyp.SCHUELER_ABSENZENLISTE);
            comboBoxListentyp.removeItem(Listentyp.SCHUELER_ADRESSETIKETTEN);
        }
        if (listenExportTyp == ListenExportTyp.LEHRKRAEFTE) {
            // Initialisierung
            comboBoxListentyp.setSelectedItem(Listentyp.LEHRKRAEFTE_ADRESSLISTE);
        } else {
            comboBoxListentyp.removeItem(Listentyp.LEHRKRAEFTE_ADRESSLISTE);
            comboBoxListentyp.removeItem(Listentyp.LEHRKRAEFTE_ADRESSETIKETTEN);
        }
        if (listenExportTyp == ListenExportTyp.KURSE) {
            // Initialisierung
            comboBoxListentyp.setSelectedItem(Listentyp.KURSELISTE);
        } else {
            comboBoxListentyp.removeItem(Listentyp.KURSELISTE);
        }
        comboBoxListentyp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onListentypSelected();
            }
        });
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
    
    public void setTxtTitel(JTextField txtTitel) {
        this.txtTitel = txtTitel;
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
        String titel;
        if (listenExportTyp == ListenExportTyp.SCHUELER && schuelerSuchenTableModel.getLehrkraft() != null) {
            titel = schuelerSuchenTableModel.getLehrkraft().toString();
            // Es wurde nach einem spezifischen Kurs gesucht
            if (schuelerSuchenTableModel.getWochentag() != null && schuelerSuchenTableModel.getZeitBeginn() != null) {
                titel = titel + " (" + schuelerSuchenTableModel.getWochentag() + " " + asString(schuelerSuchenTableModel.getZeitBeginn()) + "-" + asString(schuelerSuchenTableModel.getSchuelerList().get(0).getKurseAsList().get(0).getZeitEnde()) + ")";
            }
        } else {
            titel = "Schüler";
        }
        if (listenExportTyp == ListenExportTyp.LEHRKRAEFTE) {
            titel = "Lehrkräfte";
        }
        if (listenExportTyp == ListenExportTyp.KURSE) {
            titel = "Kurse";
        }
        txtTitel.setText(titel);
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
        // Speichern-Dialog
        JFileChooser fileChooser = setupFileChooser();
        int returnVal = fileChooser.showSaveDialog(listenExportDialog);
        if (returnVal != JFileChooser.APPROVE_OPTION) {
            btnOk.setFocusPainted(false);
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
                return listenExportModel.createListenFile(outputFile, schuelerSuchenTableModel, lehrkraefteTableModel, kurseTableModel);
            }
            @Override
            protected void done() {
                dialog.dispose();
                CreateListeCommand.Result result = null;
                try {
                    result = get();
                } catch (InterruptedException | ExecutionException e) {
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
        listenExportDialog.setEnabled(false);
        listenExportDialog.repaint();
        dialog.setVisible(true);
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
        if (listenExportModel.getListentyp() != null && (listenExportModel.getListentyp() == Listentyp.SCHUELER_ADRESSETIKETTEN || listenExportModel.getListentyp() == Listentyp.LEHRKRAEFTE_ADRESSETIKETTEN)) {
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
            errLblListentyp.setVisible(true);
            errLblListentyp.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.TITEL)) {
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
            errLblListentyp.setVisible(false);
            comboBoxListentyp.setToolTipText(null);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.TITEL)) {
            errLblTitel.setVisible(false);
            txtTitel.setToolTipText(null);
        }
    }

    @Override
    public void disableFields(boolean disable, Set<Field> fields) {
        if (fields.contains(Field.ALLE) || fields.contains(Field.LISTENTYP)) {
            comboBoxListentyp.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.TITEL)) {
            txtTitel.setEnabled(!disable);
        }
    }

}
