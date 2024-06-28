package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.MonatsstatistikKurseModel;
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
public class MonatsstatistikKurseController extends AbstractController {

    private static final Logger LOGGER = LogManager.getLogger(MonatsstatistikKurseController.class);

    // Möglichkeit zum Umschalten des validation modes (nicht dynamisch)
    private static final boolean MODEL_VALIDATION_MODE = false;

    private static final String MONAT_JAHR_DATE_FORMAT_STRING = "MM.yyyy";

    private final MonatsstatistikKurseModel monatsstatistikKurseModel;
    private final boolean defaultButtonEnabled;
    private ActionListener closeListener;
    private JTextField txtMonatJahr;
    private JLabel errLblMonatJahr;
    private JButton btnSuchen;
    private JButton btnAbbrechen;

    public MonatsstatistikKurseController(MonatsstatistikKurseModel monatsstatistikKurseModel, boolean defaultButtonEnabled) {
        super(monatsstatistikKurseModel);
        this.monatsstatistikKurseModel = monatsstatistikKurseModel;
        this.defaultButtonEnabled = defaultButtonEnabled;
        this.monatsstatistikKurseModel.addPropertyChangeListener(this);
        this.monatsstatistikKurseModel.addDisableFieldsListener(this);
        this.monatsstatistikKurseModel.addMakeErrorLabelsInvisibleListener(this);
        this.monatsstatistikKurseModel.addCompletedListener(this::onMonatsstatistikModelCompleted);
        this.setModelValidationMode(MODEL_VALIDATION_MODE);
    }

    public void setTxtMonatJahr(JTextField txtMonatJahr) {
        this.txtMonatJahr = txtMonatJahr;
        if (!defaultButtonEnabled) {
            this.txtMonatJahr.addActionListener(e -> onMonatJahrEvent());
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
            monatsstatistikKurseModel.setMonatJahr(asString(monatJahrInit, MONAT_JAHR_DATE_FORMAT_STRING));
        } catch (SvmValidationException ignore) {
        }
    }

    public void setBtnSuchen(JButton btnSuchen) {
        this.btnSuchen = btnSuchen;
        if (isModelValidationMode()) {
            btnSuchen.setEnabled(false);
        }
        this.btnSuchen.addActionListener(e -> onSuchen());
    }

    public void setBtnAbbrechen(JButton btnAbbrechen) {
        this.btnAbbrechen = btnAbbrechen;
        this.btnAbbrechen.addActionListener(e -> onAbbrechen());
    }

    public void setErrLblMonatJahr(JLabel errLblMonatJahr) {
        this.errLblMonatJahr = errLblMonatJahr;
    }

    @SuppressWarnings("DuplicatedCode")
    private void onMonatJahrEvent() {
        LOGGER.trace("MonatsstatistikController Event Monat/Jahr");
        boolean equalFieldAndModelValue = equalsNullSafe(txtMonatJahr.getText(), monatsstatistikKurseModel.getMonatJahr(), MONAT_JAHR_DATE_FORMAT_STRING);
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

    @SuppressWarnings("DuplicatedCode")
    private void setModelMonatJahr() throws SvmValidationException {
        makeErrorLabelInvisible(Field.MONAT_JAHR);
        try {
            monatsstatistikKurseModel.setMonatJahr(txtMonatJahr.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("MonatsstatistikController setModelMonatJahr RequiredException={}", e.getMessage());
            if (isModelValidationMode()) {
                txtMonatJahr.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut, nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("MonatsstatistikController setModelMonatJahr Exception={}", e.getMessage());
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
        btnSuchen.setFocusPainted(true);
        if (!isModelValidationMode() && !validateOnSpeichern()) {
            btnSuchen.setFocusPainted(false);
            return;
        }
        int[] result = monatsstatistikKurseModel.calculateMonatsstatistik();
        String statistik = "Anmeldungen: " + result[0] +
                "\nAbmeldungen: " + result[1] +
                "\nBesuchte Lektionen: " + result[2];
        JOptionPane.showMessageDialog(null, statistik, "Statistik per Ende Monat", JOptionPane.INFORMATION_MESSAGE);
        btnSuchen.setFocusPainted(false);
    }

    private void onMonatsstatistikModelCompleted(boolean completed) {
        LOGGER.trace("MonatsstatistikModel completed={}", completed);
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

    @Override
    void doPropertyChange(PropertyChangeEvent evt) {
        super.doPropertyChange(evt);
        if (checkIsFieldChange(Field.MONAT_JAHR, evt)) {
            txtMonatJahr.setText(asString(monatsstatistikKurseModel.getMonatJahr(), MONAT_JAHR_DATE_FORMAT_STRING));
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

}
