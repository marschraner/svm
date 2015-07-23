package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.dataTypes.Field;
import ch.metzenthin.svm.dataTypes.Semesterbezeichnung;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.CompletedListener;
import ch.metzenthin.svm.domain.model.KurseSuchenModel;
import ch.metzenthin.svm.domain.model.KurseTableData;
import ch.metzenthin.svm.ui.componentmodel.KurseTableModel;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Set;

import static ch.metzenthin.svm.common.utils.SimpleValidator.equalsNullSafe;

/**
 * @author Martin Schraner
 */
public class KurseSuchenController extends AbstractController {

    private static final Logger LOGGER = Logger.getLogger(KurseSuchenController.class);

    // Möglichkeit zum Umschalten des validation modes (nicht dynamisch)
    private static final boolean MODEL_VALIDATION_MODE = false;

    private ActionListener closeListener;
    private ActionListener nextPanelListener;
    private KurseSuchenModel kurseSuchenModel;
    private final SvmContext svmContext;
    private JSpinner spinnerSchuljahre;
    private JComboBox<Semesterbezeichnung> comboBoxSemesterbezeichnung;
    private JButton btnSuchen;
    private JButton btnAbbrechen;

    public KurseSuchenController(SvmContext svmContext, KurseSuchenModel kurseSuchenModel) {
        super(kurseSuchenModel);
        this.svmContext = svmContext;
        this.kurseSuchenModel = kurseSuchenModel;
        this.kurseSuchenModel.addPropertyChangeListener(this);
        this.kurseSuchenModel.addDisableFieldsListener(this);
        this.kurseSuchenModel.addMakeErrorLabelsInvisibleListener(this);
        this.kurseSuchenModel.addCompletedListener(new CompletedListener() {
            @Override
            public void completed(boolean completed) {
                onKurseSuchenModelCompleted(completed);
            }
        });
        this.setModelValidationMode(MODEL_VALIDATION_MODE);
    }

    public void constructionDone() {
        kurseSuchenModel.initializeCompleted();
    }

    public void setSpinnerSchuljahre(JSpinner spinnerSchuljahre) {
        this.spinnerSchuljahre = spinnerSchuljahre;
        spinnerSchuljahre.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                onSchuljahrSelected();
            }
        });
        initSchuljahr();
    }

    private void initSchuljahr() {
        Calendar today = new GregorianCalendar();
        int schuljahr1;
        if (today.get(Calendar.MONTH) <= Calendar.MAY) {
            schuljahr1 = today.get(Calendar.YEAR) - 1;
        } else {
            schuljahr1 = today.get(Calendar.YEAR);
        }
        int schuljahr2 = schuljahr1 + 1;
        String schuljahr = schuljahr1 + "/" + schuljahr2;
        try {
            kurseSuchenModel.setSchuljahr(schuljahr);
        } catch (SvmValidationException ignore) {
        }
    }

    private void onSchuljahrSelected() {
        LOGGER.trace("KurseSuchenController Event Schuljahre selected =" + spinnerSchuljahre.getValue());
        boolean equalFieldAndModelValue = equalsNullSafe(spinnerSchuljahre.getValue(), kurseSuchenModel.getSchuljahr());
        try {
            setModelSchuljahr();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelSchuljahr() throws SvmValidationException {
        makeErrorLabelInvisible(Field.SCHULJAHR);
        try {
            kurseSuchenModel.setSchuljahr((String) spinnerSchuljahre.getValue());
        } catch (SvmValidationException e) {
            LOGGER.trace("PersonController setModelSchuljahr Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setComboBoxSemesterbezeichnung(JComboBox<Semesterbezeichnung> comboBoxSemesterbezeichnung) {
        this.comboBoxSemesterbezeichnung = comboBoxSemesterbezeichnung;
        comboBoxSemesterbezeichnung.setModel(new DefaultComboBoxModel<>(Semesterbezeichnung.values()));
        comboBoxSemesterbezeichnung.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSemesterbezeichnungSelected();
            }
        });
        initSemesterbezeichnung();
    }

    private void initSemesterbezeichnung() {
        Calendar today = new GregorianCalendar();
        Semesterbezeichnung semesterbezeichnung;
        if (today.get(Calendar.MONTH) >= Calendar.FEBRUARY || today.get(Calendar.MONTH) <= Calendar.MAY) {
            semesterbezeichnung = Semesterbezeichnung.ZWEITES_SEMESTER;
        } else {
            semesterbezeichnung = Semesterbezeichnung.ERSTES_SEMESTER;
        }
        kurseSuchenModel.setSemesterbezeichnung(semesterbezeichnung);
    }

    private void onSemesterbezeichnungSelected() {
        LOGGER.trace("KurseSuchenController Event Semesterbezeichnung selected=" + comboBoxSemesterbezeichnung.getSelectedItem());
        boolean equalFieldAndModelValue = equalsNullSafe(comboBoxSemesterbezeichnung.getSelectedItem(), kurseSuchenModel.getSemesterbezeichnung());
        try {
            setModelSemesterbezeichnung();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelSemesterbezeichnung() throws SvmValidationException {
        makeErrorLabelInvisible(Field.SEMESTERBEZEICHNUNG);
        kurseSuchenModel.setSemesterbezeichnung((Semesterbezeichnung) comboBoxSemesterbezeichnung.getSelectedItem());
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

    private void onSuchen() {
        LOGGER.trace("KurseSuchenController Suchen gedrückt");
        if (!isModelValidationMode() && !validateOnSpeichern()) {
            return;
        }
        KurseTableData kurseTableData = kurseSuchenModel.suchen();
        KurseTableModel kurseTableModel = new KurseTableModel(kurseTableData);
        //TODO
//        KursePanel kursePanel = new KursePanel(svmContext, kurseTableModel);
//        kursePanel.addNextPanelListener(nextPanelListener);
//        kursePanel.addCloseListener(closeListener);
//        nextPanelListener.actionPerformed(new ActionEvent(new Object[]{kursePanel.$$$getRootComponent$$$(), "Suchresultat"}, ActionEvent.ACTION_PERFORMED, "Suchresultat verfügbar"));
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

    private void onAbbrechen() {
        LOGGER.trace("KurseSuchenController Abbrechen gedrückt");
        closeListener.actionPerformed(new ActionEvent(btnAbbrechen, ActionEvent.ACTION_PERFORMED, "Close nach Abbrechen"));
    }

    private void onKurseSuchenModelCompleted(boolean completed) {
        LOGGER.trace("KurseSuchenModel completed=" + completed);
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

    @Override
    void doPropertyChange(PropertyChangeEvent evt) {
        super.doPropertyChange(evt);
        if (checkIsFieldChange(Field.SCHULJAHR, evt)) {
            spinnerSchuljahre.setValue(kurseSuchenModel.getSchuljahr());
        } else if (checkIsFieldChange(Field.SEMESTERBEZEICHNUNG, evt)) {
            comboBoxSemesterbezeichnung.setSelectedItem(kurseSuchenModel.getSemesterbezeichnung());
        }
    }

    @Override
    void validateFields() throws SvmValidationException {}

    @Override
    void showErrMsg(SvmValidationException e) {}

    @Override
    void showErrMsgAsToolTip(SvmValidationException e) {}

    @Override
    public void makeErrorLabelsInvisible(Set<Field> fields) {}

    @Override
    public void disableFields(boolean disable, Set<Field> fields) {}

}
