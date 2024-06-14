package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.AuswahlRechnungsdateienModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.util.Set;

import static ch.metzenthin.svm.common.utils.SimpleValidator.equalsNullSafe;

/**
 * @author Martin Schraner
 */
@SuppressWarnings("LoggingSimilarMessage")
public class AuswahlRechnungsdateienController extends AbstractController {

    private static final Logger LOGGER = LogManager.getLogger(AuswahlRechnungsdateienController.class);

    // Möglichkeit zum Umschalten des validation modes (nicht dynamisch)
    private static final boolean MODEL_VALIDATION_MODE = false;

    private final AuswahlRechnungsdateienModel auswahlRechnungsdateienModel;

    private final SvmContext svmContext;
    private final JFileChooser serienbriefCsvFileChooser;
    private final JFileChooser ausSerienbriefGenerierteRechnungenPdfFileChooser;

    private JPanel mainPanel;
    private JButton btnSelektionSerienbrief;
    private JButton btnSelektionAusSerienbriefGenerierteRechnungen;
    private JLabel lblSerienbriefPfad;
    private JLabel lblAusSerienbriefGenerierteRechnungenPfad;
    private JLabel errLblSerienbriefCsvDatei;
    private JLabel errLblAusSerienbriefGenerierteRechnungenPdfDatei;
    private JButton btnRechnungsdateienEinlesen;
    private JButton btnAbbrechen;
    private ActionListener closeListener;
    private ActionListener nextPanelListener;
    private ActionListener zurueckListener;

    public AuswahlRechnungsdateienController(SvmContext svmContext, AuswahlRechnungsdateienModel auswahlRechnungsdateienModel) {
        super(auswahlRechnungsdateienModel);
        this.auswahlRechnungsdateienModel = auswahlRechnungsdateienModel;
        this.svmContext = svmContext;
        this.auswahlRechnungsdateienModel.addPropertyChangeListener(this);
        this.auswahlRechnungsdateienModel.addDisableFieldsListener(this);
        this.auswahlRechnungsdateienModel.addMakeErrorLabelsInvisibleListener(this);
        this.auswahlRechnungsdateienModel.addCompletedListener(this::onAuswahlRechnungsdateienModelCompleted);

        serienbriefCsvFileChooser = new JFileChooser();
        FileNameExtensionFilter csvFileFilter = new FileNameExtensionFilter("csv-Dateien", "csv");
        serienbriefCsvFileChooser.setFileFilter(csvFileFilter);

        ausSerienbriefGenerierteRechnungenPdfFileChooser = new JFileChooser();
        FileNameExtensionFilter pdfFileFilter = new FileNameExtensionFilter("pdf-Dateien", "pdf");
        ausSerienbriefGenerierteRechnungenPdfFileChooser.setFileFilter(pdfFileFilter);

        this.setModelValidationMode(MODEL_VALIDATION_MODE);
    }

    public void setMainPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    public void setBtnSelektionSerienbrief(JButton btnSelektionSerienbrief) {
        this.btnSelektionSerienbrief = btnSelektionSerienbrief;
        this.btnSelektionSerienbrief.addActionListener(e -> onBtnSelektionSerienbriefClicked());
    }

    private void onBtnSelektionSerienbriefClicked() {
        int choice = serienbriefCsvFileChooser.showOpenDialog(null);
        if (choice == JFileChooser.APPROVE_OPTION) {
            onSerienbriefCsvFileChooserFileSelected();
        }
    }

    private void onSerienbriefCsvFileChooserFileSelected() {
        LOGGER.trace("AuswahlRechnungsdateienController onSerienbriefCsvDateiSelected");
        boolean equalFieldAndModelValue = equalsNullSafe(
                serienbriefCsvFileChooser.getSelectedFile(),
                auswahlRechnungsdateienModel.getSerienbriefCsvDatei());
        try {
            setModelSerienbriefCsvDatei();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelSerienbriefCsvDatei() throws SvmValidationException {
        makeErrorLabelInvisible(Field.SERIENBRIEF_CSV_DATEI);
        try {
            auswahlRechnungsdateienModel.setSerienbriefCsvDatei(serienbriefCsvFileChooser.getSelectedFile());
        } catch (SvmRequiredException e) {
            LOGGER.trace("AuswahlRechnungsdateienController setModelSerienbriefCsvDatei RequiredException={}", e.getMessage());
            if (isModelValidationMode()) {
                lblSerienbriefPfad.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut, nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("AuswahlRechnungsdateienController setModelSerienbriefCsvDatei Exception={}", e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setBtnSelektionAusSerienbriefGenerierteRechnungen(JButton btnSelektionAusSerienbriefGenerierteRechnungen) {
        this.btnSelektionAusSerienbriefGenerierteRechnungen = btnSelektionAusSerienbriefGenerierteRechnungen;
        this.btnSelektionAusSerienbriefGenerierteRechnungen.addActionListener(e -> onBtnSelektionAusSerienbriefGenerierteRechnungenClicked());
    }

    private void onBtnSelektionAusSerienbriefGenerierteRechnungenClicked() {
        int choice = ausSerienbriefGenerierteRechnungenPdfFileChooser.showOpenDialog(null);
        if (choice == JFileChooser.APPROVE_OPTION) {
            onAusSerienbriefGenerierteRechnungenPdfFileChooserFileSelected();
        }
    }

    private void onAusSerienbriefGenerierteRechnungenPdfFileChooserFileSelected() {
        LOGGER.trace("AuswahlRechnungsdateienController onAusSerienbriefGenerierteRechnungenPdfDateiSelected");
        boolean equalFieldAndModelValue = equalsNullSafe(
                ausSerienbriefGenerierteRechnungenPdfFileChooser.getSelectedFile(),
                auswahlRechnungsdateienModel.getAusSerienbriefGenerierteRechnungenPdfDatei());
        try {
            setModelAusSerienbriefGenerierteRechnungenPdfDatei();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelAusSerienbriefGenerierteRechnungenPdfDatei() throws SvmValidationException {
        makeErrorLabelInvisible(Field.AUS_SERIENBRIEF_GENERIERTE_RECHNUNGEN_PDF_DATEI);
        try {
            auswahlRechnungsdateienModel.setAusSerienbriefGenerierteRechnungenPdfDatei(
                    ausSerienbriefGenerierteRechnungenPdfFileChooser.getSelectedFile());
        } catch (SvmRequiredException e) {
            LOGGER.trace("AuswahlRechnungsdateienController setModelAusSerienbriefGenerierteRechnungenPdfDatei RequiredException={}",
                    e.getMessage());
            if (isModelValidationMode()) {
                lblAusSerienbriefGenerierteRechnungenPfad.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut, nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("AuswahlRechnungsdateienController setModelAusSerienbriefGenerierteRechnungenPdfDatei Exception={}",
                    e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setLblSerienbriefPfad(JLabel lblSerienbriefPfad) {
        this.lblSerienbriefPfad = lblSerienbriefPfad;
    }

    public void setLblAusSerienbriefGenerierteRechnungenPfad(JLabel lblAusSerienbriefGenerierteRechnungenPfad) {
        this.lblAusSerienbriefGenerierteRechnungenPfad = lblAusSerienbriefGenerierteRechnungenPfad;
    }

    public void setErrLblSerienbriefCsvDatei(JLabel errLblSerienbriefCsvDatei) {
        this.errLblSerienbriefCsvDatei = errLblSerienbriefCsvDatei;
    }

    public void setErrLblAusSerienbriefGenerierteRechnungenPdfDatei(
            JLabel errLblAusSerienbriefGenerierteRechnungenPdfDatei) {
        this.errLblAusSerienbriefGenerierteRechnungenPdfDatei = errLblAusSerienbriefGenerierteRechnungenPdfDatei;
    }

    public void setBtnRechnungsdateienEinlesen(JButton btnRechnungsdateienEinlesen) {
        this.btnRechnungsdateienEinlesen = btnRechnungsdateienEinlesen;
        if (isModelValidationMode()) {
            btnRechnungsdateienEinlesen.setEnabled(false);
        }
        this.btnRechnungsdateienEinlesen.addActionListener(e -> onRechnungsdateienEinlesen());
    }

    private void onRechnungsdateienEinlesen() {
        LOGGER.trace("AuswahlRechnungsdateienPanel Rechnungsdateien einlesen gedrückt");
        if (!validateOnSpeichern()) {
            btnRechnungsdateienEinlesen.setFocusPainted(false);
            return;
        }
        setWaitCursorAllComponents();
        auswahlRechnungsdateienModel.rechnungsdateienEinlesen();
        resetCursorAllComponents();
    }

    public void setBtnAbbrechen(JButton btnAbbrechen) {
        this.btnAbbrechen = btnAbbrechen;
        this.btnAbbrechen.addActionListener(e -> onAbbrechen());
    }

    private void onAbbrechen() {
        LOGGER.trace("AuswahlRechnungsdateienPanel Abbrechen gedrückt");
        closeListener.actionPerformed(new ActionEvent(btnAbbrechen, ActionEvent.ACTION_PERFORMED, "Close nach Abbrechen"));
    }

    private void onAuswahlRechnungsdateienModelCompleted(boolean completed) {
        LOGGER.trace("AuswahlRechnungsdateienModel completed={}", completed);
        if (completed) {
            btnRechnungsdateienEinlesen.setToolTipText(null);
            btnRechnungsdateienEinlesen.setEnabled(true);
        } else {
            btnRechnungsdateienEinlesen.setToolTipText("Bitte Eingabedaten vervollständigen");
            btnRechnungsdateienEinlesen.setEnabled(false);
        }
    }

    public void addCloseListener(ActionListener closeListener) {
        this.closeListener = closeListener;
    }

    public void addZurueckListener(ActionListener zurueckListener) {
        this.zurueckListener = zurueckListener;
    }

    public void addNextPanelListener(ActionListener nextPanelListener) {
        this.nextPanelListener = nextPanelListener;
    }

    private void setWaitCursorAllComponents() {
        Cursor waitCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
        mainPanel.setCursor(waitCursor);
    }

    private void resetCursorAllComponents() {
        mainPanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    @Override
    void doPropertyChange(PropertyChangeEvent evt) {
        if (checkIsFieldChange(Field.SERIENBRIEF_CSV_DATEI, evt)) {
            serienbriefCsvFileChooser.setSelectedFile(
                    auswahlRechnungsdateienModel.getSerienbriefCsvDatei());
            lblSerienbriefPfad.setText(
                    auswahlRechnungsdateienModel.getSerienbriefCsvDatei().toString());
        } else if (checkIsFieldChange(Field.AUS_SERIENBRIEF_GENERIERTE_RECHNUNGEN_PDF_DATEI, evt)) {
            ausSerienbriefGenerierteRechnungenPdfFileChooser.setSelectedFile(
                    auswahlRechnungsdateienModel.getAusSerienbriefGenerierteRechnungenPdfDatei());
            lblAusSerienbriefGenerierteRechnungenPfad.setText(
                    auswahlRechnungsdateienModel.getAusSerienbriefGenerierteRechnungenPdfDatei().toString());
        }
    }

    @Override
    void validateFields() throws SvmValidationException {
        setModelSerienbriefCsvDatei();
        setModelAusSerienbriefGenerierteRechnungenPdfDatei();
    }

    @Override
    void showErrMsg(SvmValidationException e) {
        if (e.getAffectedFields().contains(Field.SERIENBRIEF_CSV_DATEI)) {
            errLblSerienbriefCsvDatei.setVisible(true);
            errLblSerienbriefCsvDatei.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.AUS_SERIENBRIEF_GENERIERTE_RECHNUNGEN_PDF_DATEI)) {
            errLblAusSerienbriefGenerierteRechnungenPdfDatei.setVisible(true);
            errLblAusSerienbriefGenerierteRechnungenPdfDatei.setText(e.getMessage());
        }
    }

    @Override
    void showErrMsgAsToolTip(SvmValidationException e) {
        if (e.getAffectedFields().contains(Field.SERIENBRIEF_CSV_DATEI)) {
            btnSelektionSerienbrief.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.AUS_SERIENBRIEF_GENERIERTE_RECHNUNGEN_PDF_DATEI)) {
            btnSelektionAusSerienbriefGenerierteRechnungen.setToolTipText(e.getMessage());
        }
    }

    @Override
    public void makeErrorLabelsInvisible(Set<Field> fields) {
        if (fields.contains(Field.ALLE) || fields.contains(Field.SERIENBRIEF_CSV_DATEI)) {
            errLblSerienbriefCsvDatei.setVisible(false);
            btnSelektionSerienbrief.setToolTipText(null);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.AUS_SERIENBRIEF_GENERIERTE_RECHNUNGEN_PDF_DATEI)) {
            errLblAusSerienbriefGenerierteRechnungenPdfDatei.setVisible(false);
            btnSelektionAusSerienbriefGenerierteRechnungen.setToolTipText(null);
        }
    }

    @Override
    public void disableFields(boolean disable, Set<Field> fields) {
    }
}
