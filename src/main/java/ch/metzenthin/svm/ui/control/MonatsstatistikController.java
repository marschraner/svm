package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.CompletedListener;
import ch.metzenthin.svm.domain.model.MonatsstatistikModel;
import ch.metzenthin.svm.domain.model.SchuelerSuchenTableData;
import ch.metzenthin.svm.ui.componentmodel.SchuelerSuchenTableModel;
import ch.metzenthin.svm.ui.components.SchuelerSuchenResultPanel;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.util.Set;

import static ch.metzenthin.svm.common.utils.Converter.asString;
import static ch.metzenthin.svm.common.utils.SimpleValidator.equalsNullSafe;

/**
 * @author Martin Schraner
 */
public class MonatsstatistikController extends AbstractController {

    private static final Logger LOGGER = Logger.getLogger(MonatsstatistikController.class);

    // Möglichkeit zum Umschalten des validation modes (nicht dynamisch)
    private static final boolean MODEL_VALIDATION_MODE = false;

    private static final String MONAT_JAHR_DATE_FORMAT_STRING = "MM.yyyy";

    private final SvmContext svmContext;
    private MonatsstatistikModel monatsstatistikModel;
    private ActionListener closeListener;
    private ActionListener nextPanelListener;
    private JTextField txtMonatJahr;
    private JLabel errLblMonatJahr;
    private JRadioButton radioBtnAnmeldungen;
    private JRadioButton radioBtnAbmeldungen;
    private JRadioButton radioBtnDispensationen;
    private JButton btnSuchen;
    private JButton btnAbbrechen;
    private ActionListener zurueckListener;

    public MonatsstatistikController(SvmContext svmContext, MonatsstatistikModel monatsstatistikModel) {
        super(monatsstatistikModel);
        this.svmContext = svmContext;
        this.monatsstatistikModel = monatsstatistikModel;
        this.monatsstatistikModel.addPropertyChangeListener(this);
        this.monatsstatistikModel.addDisableFieldsListener(this);
        this.monatsstatistikModel.addMakeErrorLabelsInvisibleListener(this);
        this.monatsstatistikModel.addCompletedListener(new CompletedListener() {
            @Override
            public void completed(boolean completed) {
                onMonatsstatistikModelCompleted(completed);
            }
        });
        this.setModelValidationMode(MODEL_VALIDATION_MODE);
    }

    public void setTxtMonatJahr(JTextField txtMonatJahr) {
        this.txtMonatJahr = txtMonatJahr;
        this.txtMonatJahr.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onMonatJahrEvent();
            }
        });
        this.txtMonatJahr.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onMonatJahrEvent();
            }
        });
    }

    public void setRadioBtnGroupAnAbmeldungenDispensationen(JRadioButton radioBtnAnmeldungen, JRadioButton radioBtnAbmeldungen, JRadioButton radioBtnDispensationen) {
        this.radioBtnAnmeldungen = radioBtnAnmeldungen;
        this.radioBtnAbmeldungen = radioBtnAbmeldungen;
        this.radioBtnDispensationen = radioBtnDispensationen;
        // Action Commands
        this.radioBtnAnmeldungen.setActionCommand(MonatsstatistikModel.AnAbmeldungenDispensationenSelected.ANMELDUNGEN.toString());
        this.radioBtnAbmeldungen.setActionCommand(MonatsstatistikModel.AnAbmeldungenDispensationenSelected.ABMELDUNGEN.toString());
        this.radioBtnDispensationen.setActionCommand(MonatsstatistikModel.AnAbmeldungenDispensationenSelected.DISPENSATIONEN.toString());
        // Listener
        RadioBtnGroupAnAbmeldungenDispensationenListener radioBtnGroupAnAbmeldungenDispensationenListener = new RadioBtnGroupAnAbmeldungenDispensationenListener();
        this.radioBtnAnmeldungen.addActionListener(radioBtnGroupAnAbmeldungenDispensationenListener);
        this.radioBtnAbmeldungen.addActionListener(radioBtnGroupAnAbmeldungenDispensationenListener);
        this.radioBtnDispensationen.addActionListener(radioBtnGroupAnAbmeldungenDispensationenListener);
    }

    public void setBtnSuchen(JButton btnSuchen) {
        this.btnSuchen = btnSuchen;
        if (isModelValidationMode()) {
            btnSuchen.setEnabled(false);
        }
        this.btnSuchen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSuchen();
            }
        });
    }

    public void setBtnAbbrechen(JButton btnAbbrechen) {
        this.btnAbbrechen = btnAbbrechen;
        this.btnAbbrechen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onAbbrechen();
            }
        });
    }

    public void setErrLblMonatJahr(JLabel errLblMonatJahr) {
        this.errLblMonatJahr = errLblMonatJahr;
    }

    private void onMonatJahrEvent() {
        LOGGER.trace("MonatsstatistikController Event Monat/Jahr");
        boolean equalFieldAndModelValue = equalsNullSafe(txtMonatJahr.getText(), monatsstatistikModel.getMonatJahr(), MONAT_JAHR_DATE_FORMAT_STRING);
        try {
            setModelMonatJahr();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelMonatJahr() throws SvmValidationException {
        makeErrorLabelInvisible(Field.MONAT_JAHR);
        try {
            monatsstatistikModel.setMonatJahr(txtMonatJahr.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("MonatsstatistikController setModelMonatJahr RequiredException=" + e.getMessage());
            if (isModelValidationMode()) {
                txtMonatJahr.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("MonatsstatistikController setModelMonatJahr Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    private void onAbbrechen() {
        LOGGER.trace("MonatsstatistikController Abbrechen gedrückt");
        closeListener.actionPerformed(new ActionEvent(btnAbbrechen, ActionEvent.ACTION_PERFORMED, "Close nach Abbrechen"));
    }

    private void onSuchen() {
        LOGGER.trace("MonatsstatistikController Suchen gedrückt");
        if (!isModelValidationMode() && !validateOnSpeichern()) {
            return;
        }
        SchuelerSuchenTableData schuelerSuchenTableData = monatsstatistikModel.suchen();
        SchuelerSuchenTableModel schuelerSuchenTableModel = new SchuelerSuchenTableModel(schuelerSuchenTableData);
        SchuelerSuchenResultPanel schuelerSuchenResultPanel = new SchuelerSuchenResultPanel(svmContext, schuelerSuchenTableModel);
        schuelerSuchenResultPanel.addNextPanelListener(nextPanelListener);
        schuelerSuchenResultPanel.addCloseListener(closeListener);
        schuelerSuchenResultPanel.addZurueckListener(zurueckListener);
        nextPanelListener.actionPerformed(new ActionEvent(new Object[]{schuelerSuchenResultPanel.$$$getRootComponent$$$(), "Suchresultat"}, ActionEvent.ACTION_PERFORMED, "Suchresultat verfügbar"));
    }

    private void onMonatsstatistikModelCompleted(boolean completed) {
        LOGGER.trace("MonatsstatistikModel completed=" + completed);
        if (completed) {
            btnSuchen.setToolTipText(null);
            btnSuchen.setEnabled(true);
        } else {
            btnSuchen.setToolTipText("Bitte Eingabedaten vervollständigen");
            btnSuchen.setEnabled(false);
        }
    }

    public void addCloseListener(ActionListener closeListener) {
        this.closeListener = closeListener;
    }

    public void addNextPanelListener(ActionListener nextPanelListener) {
        this.nextPanelListener = nextPanelListener;
    }

    public void addZurueckListener(ActionListener zurueckListener) {
        this.zurueckListener = zurueckListener;
    }

    @Override
    void doPropertyChange(PropertyChangeEvent evt) {
        super.doPropertyChange(evt);
        if (checkIsFieldChange(Field.MONAT_JAHR, evt)) {
            txtMonatJahr.setText(asString(monatsstatistikModel.getMonatJahr(), MONAT_JAHR_DATE_FORMAT_STRING));
        }
    }

    @Override
    void validateFields() throws SvmValidationException {
        if (txtMonatJahr.isEnabled()) {
            LOGGER.trace("Validate field Monat/Jahr");
            setModelMonatJahr();
        }
    }

    @Override
    void showErrMsg(SvmValidationException e) {
        if (e.getAffectedFields().contains(Field.MONAT_JAHR)) {
            errLblMonatJahr.setVisible(true);
            errLblMonatJahr.setText(e.getMessage());
        }
    }

    @Override
    void showErrMsgAsToolTip(SvmValidationException e) {
        if (e.getAffectedFields().contains(Field.MONAT_JAHR)) {
            txtMonatJahr.setToolTipText(e.getMessage());
        }
    }

    @Override
    public void makeErrorLabelsInvisible(Set<Field> fields) {
        if (fields.contains(Field.ALLE) || fields.contains(Field.MONAT_JAHR)) {
            errLblMonatJahr.setVisible(false);
            txtMonatJahr.setToolTipText(null);
        }
    }

    @Override
    public void disableFields(boolean disable, Set<Field> fields) {
        if (fields.contains(Field.ALLE) || fields.contains(Field.MONAT_JAHR)) {
            txtMonatJahr.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.ANMELDUNGEN)) {
            radioBtnAnmeldungen.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.ABMELDUNGEN)) {
            radioBtnAbmeldungen.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.DISPENSATIONEN)) {
            radioBtnDispensationen.setEnabled(!disable);
        }
    }

    class RadioBtnGroupAnAbmeldungenDispensationenListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            LOGGER.trace("MonatsstatistikController AnAbmeldungenDispensationen Event");
            monatsstatistikModel.setAnAbmeldungenDispensationen(MonatsstatistikModel.AnAbmeldungenDispensationenSelected.valueOf(e.getActionCommand()));
        }
    }

}
