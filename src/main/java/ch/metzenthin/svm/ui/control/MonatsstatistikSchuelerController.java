package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.CompletedListener;
import ch.metzenthin.svm.domain.model.MonatsstatistikSchuelerModel;
import ch.metzenthin.svm.domain.model.SchuelerSuchenTableData;
import ch.metzenthin.svm.ui.componentmodel.SchuelerSuchenTableModel;
import ch.metzenthin.svm.ui.components.SchuelerSuchenResultPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Set;

import static ch.metzenthin.svm.common.utils.Converter.asString;
import static ch.metzenthin.svm.common.utils.SimpleValidator.equalsNullSafe;

/**
 * @author Martin Schraner
 */
public class MonatsstatistikSchuelerController extends AbstractController {

    private static final Logger LOGGER = LogManager.getLogger(MonatsstatistikSchuelerController.class);

    // Möglichkeit zum Umschalten des validation modes (nicht dynamisch)
    private static final boolean MODEL_VALIDATION_MODE = false;

    private static final String MONAT_JAHR_DATE_FORMAT_STRING = "MM.yyyy";

    private final SvmContext svmContext;
    private MonatsstatistikSchuelerModel monatsstatistikSchuelerModel;
    private boolean defaultButtonEnabled;
    private ActionListener closeListener;
    private ActionListener nextPanelListener;
    private JTextField txtMonatJahr;
    private JLabel errLblMonatJahr;
    private JRadioButton radioBtnAnmeldungenKindertheater;
    private JRadioButton radioBtnAbmeldungenKindertheater;
    private JRadioButton radioBtnAnmeldungenKurse;
    private JRadioButton radioBtnAbmeldungenKurse;
    private JRadioButton radioBtnDispensationen;
    private JButton btnSuchen;
    private JButton btnAbbrechen;
    private ActionListener zurueckListener;

    public MonatsstatistikSchuelerController(SvmContext svmContext, MonatsstatistikSchuelerModel monatsstatistikSchuelerModel, boolean defaultButtonEnabled) {
        super(monatsstatistikSchuelerModel);
        this.svmContext = svmContext;
        this.monatsstatistikSchuelerModel = monatsstatistikSchuelerModel;
        this.defaultButtonEnabled = defaultButtonEnabled;
        this.monatsstatistikSchuelerModel.addPropertyChangeListener(this);
        this.monatsstatistikSchuelerModel.addDisableFieldsListener(this);
        this.monatsstatistikSchuelerModel.addMakeErrorLabelsInvisibleListener(this);
        this.monatsstatistikSchuelerModel.addCompletedListener(new CompletedListener() {
            @Override
            public void completed(boolean completed) {
                onMonatsstatistikModelCompleted(completed);
            }
        });
        this.setModelValidationMode(MODEL_VALIDATION_MODE);
    }

    public void setTxtMonatJahr(JTextField txtMonatJahr) {
        this.txtMonatJahr = txtMonatJahr;
        if (!defaultButtonEnabled) {
            this.txtMonatJahr.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    onMonatJahrEvent();
                }
            });
        }
        this.txtMonatJahr.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onMonatJahrEvent();
            }
        });
        initMonatJahr();
    }

    private void initMonatJahr() {
        Calendar monatJahrInit = new GregorianCalendar();
        monatJahrInit.add(Calendar.MONTH, -1);
        try {
            monatsstatistikSchuelerModel.setMonatJahr(asString(monatJahrInit, MONAT_JAHR_DATE_FORMAT_STRING));
        } catch (SvmValidationException ignore) {
        }
    }

    public void setRadioBtnGroupAnAbmeldungenDispensationen(JRadioButton radioBtnAnmeldungenKindertheater, JRadioButton radioBtnAbmeldungenKindertheater, JRadioButton radioBtnAnmeldungenKurse, JRadioButton radioBtnAbmeldungenKurse, JRadioButton radioBtnDispensationen) {
        this.radioBtnAnmeldungenKindertheater = radioBtnAnmeldungenKindertheater;
        this.radioBtnAbmeldungenKindertheater = radioBtnAbmeldungenKindertheater;
        this.radioBtnAnmeldungenKurse = radioBtnAnmeldungenKurse;
        this.radioBtnAbmeldungenKurse = radioBtnAbmeldungenKurse;
        this.radioBtnDispensationen = radioBtnDispensationen;
        // Action Commands
        this.radioBtnAnmeldungenKindertheater.setActionCommand(MonatsstatistikSchuelerModel.AnAbmeldungenDispensationenSelected.ANMELDUNGEN_KINDERTHEATER.toString());
        this.radioBtnAbmeldungenKindertheater.setActionCommand(MonatsstatistikSchuelerModel.AnAbmeldungenDispensationenSelected.ABMELDUNGEN_KINDERTHEATER.toString());
        this.radioBtnAnmeldungenKurse.setActionCommand(MonatsstatistikSchuelerModel.AnAbmeldungenDispensationenSelected.ANMELDUNGEN_KURSE.toString());
        this.radioBtnAbmeldungenKurse.setActionCommand(MonatsstatistikSchuelerModel.AnAbmeldungenDispensationenSelected.ABMELDUNGEN_KURSE.toString());
        this.radioBtnDispensationen.setActionCommand(MonatsstatistikSchuelerModel.AnAbmeldungenDispensationenSelected.DISPENSATIONEN.toString());
        // Listener
        RadioBtnGroupAnAbmeldungenDispensationenListener radioBtnGroupAnAbmeldungenDispensationenListener = new RadioBtnGroupAnAbmeldungenDispensationenListener();
        this.radioBtnAnmeldungenKindertheater.addActionListener(radioBtnGroupAnAbmeldungenDispensationenListener);
        this.radioBtnAbmeldungenKindertheater.addActionListener(radioBtnGroupAnAbmeldungenDispensationenListener);
        this.radioBtnAnmeldungenKurse.addActionListener(radioBtnGroupAnAbmeldungenDispensationenListener);
        this.radioBtnAbmeldungenKurse.addActionListener(radioBtnGroupAnAbmeldungenDispensationenListener);
        this.radioBtnDispensationen.addActionListener(radioBtnGroupAnAbmeldungenDispensationenListener);
        // Initialisieren mit Anmeldungen Kindertheater
        monatsstatistikSchuelerModel.setAnAbmeldungenDispensationen(MonatsstatistikSchuelerModel.AnAbmeldungenDispensationenSelected.ANMELDUNGEN_KINDERTHEATER);
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
        boolean equalFieldAndModelValue = equalsNullSafe(txtMonatJahr.getText(), monatsstatistikSchuelerModel.getMonatJahr(), MONAT_JAHR_DATE_FORMAT_STRING);
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
            monatsstatistikSchuelerModel.setMonatJahr(txtMonatJahr.getText());
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
            btnSuchen.setFocusPainted(false);
            return;
        }
        SchuelerSuchenTableData schuelerSuchenTableData = monatsstatistikSchuelerModel.suchen(svmContext.getSvmModel());
        SchuelerSuchenTableModel schuelerSuchenTableModel = new SchuelerSuchenTableModel(schuelerSuchenTableData);
        if (schuelerSuchenTableData.size() >= 1) {
            SchuelerSuchenResultPanel schuelerSuchenResultPanel = new SchuelerSuchenResultPanel(svmContext, schuelerSuchenTableModel);
            schuelerSuchenResultPanel.addNextPanelListener(nextPanelListener);
            schuelerSuchenResultPanel.addCloseListener(closeListener);
            schuelerSuchenResultPanel.addZurueckListener(zurueckListener);
            nextPanelListener.actionPerformed(new ActionEvent(new Object[]{schuelerSuchenResultPanel.$$$getRootComponent$$$(), "Suchresultat"}, ActionEvent.ACTION_PERFORMED, "Suchresultat verfügbar"));
        } else {
            JOptionPane.showMessageDialog(null, "Es wurden keine Schüler gefunden, welche auf die Suchabfrage passen.", "Keine Schüler gefunden", JOptionPane.INFORMATION_MESSAGE);
            btnSuchen.setFocusPainted(false);
        }
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
            txtMonatJahr.setText(asString(monatsstatistikSchuelerModel.getMonatJahr(), MONAT_JAHR_DATE_FORMAT_STRING));
        } else if (checkIsFieldChange(Field.AN_ABMELDUNGEN_DISPENSATIONEN, evt) && evt.getNewValue() == MonatsstatistikSchuelerModel.AnAbmeldungenDispensationenSelected.ANMELDUNGEN_KINDERTHEATER) {
            radioBtnAnmeldungenKindertheater.setSelected(true);
        } else if (checkIsFieldChange(Field.AN_ABMELDUNGEN_DISPENSATIONEN, evt) && evt.getNewValue() == MonatsstatistikSchuelerModel.AnAbmeldungenDispensationenSelected.ABMELDUNGEN_KINDERTHEATER) {
            radioBtnAbmeldungenKindertheater.setSelected(true);
        } else if (checkIsFieldChange(Field.AN_ABMELDUNGEN_DISPENSATIONEN, evt) && evt.getNewValue() == MonatsstatistikSchuelerModel.AnAbmeldungenDispensationenSelected.ANMELDUNGEN_KURSE) {
            radioBtnAnmeldungenKurse.setSelected(true);
        } else if (checkIsFieldChange(Field.AN_ABMELDUNGEN_DISPENSATIONEN, evt) && evt.getNewValue() == MonatsstatistikSchuelerModel.AnAbmeldungenDispensationenSelected.ABMELDUNGEN_KURSE) {
            radioBtnAbmeldungenKurse.setSelected(true);
        } else if (checkIsFieldChange(Field.AN_ABMELDUNGEN_DISPENSATIONEN, evt) && evt.getNewValue() == MonatsstatistikSchuelerModel.AnAbmeldungenDispensationenSelected.DISPENSATIONEN) {
            radioBtnDispensationen.setSelected(true);
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
    }

    class RadioBtnGroupAnAbmeldungenDispensationenListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            LOGGER.trace("MonatsstatistikController AnAbmeldungenDispensationen Event");
            monatsstatistikSchuelerModel.setAnAbmeldungenDispensationen(MonatsstatistikSchuelerModel.AnAbmeldungenDispensationenSelected.valueOf(e.getActionCommand()));
        }
    }

}
