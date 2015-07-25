package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.dataTypes.Field;
import ch.metzenthin.svm.dataTypes.Semesterbezeichnung;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.CompletedListener;
import ch.metzenthin.svm.domain.model.KurseSemesterwahlModel;
import ch.metzenthin.svm.domain.model.KurseTableData;
import ch.metzenthin.svm.ui.componentmodel.KurseTableModel;
import ch.metzenthin.svm.ui.components.KursePanel;
import ch.metzenthin.svm.ui.components.SemestersPanel;
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
public class KurseSemesterwahlController extends AbstractController {

    private static final Logger LOGGER = Logger.getLogger(KurseSemesterwahlController.class);

    // Möglichkeit zum Umschalten des validation modes (nicht dynamisch)
    private static final boolean MODEL_VALIDATION_MODE = false;

    private ActionListener closeListener;
    private ActionListener nextPanelListener;
    private KurseSemesterwahlModel kurseSemesterwahlModel;
    private final SvmContext svmContext;
    private JSpinner spinnerSchuljahre;
    private JComboBox<Semesterbezeichnung> comboBoxSemesterbezeichnung;
    private JButton btnOk;
    private JButton btnAbbrechen;

    public KurseSemesterwahlController(SvmContext svmContext, KurseSemesterwahlModel kurseSemesterwahlModel) {
        super(kurseSemesterwahlModel);
        this.svmContext = svmContext;
        this.kurseSemesterwahlModel = kurseSemesterwahlModel;
        this.kurseSemesterwahlModel.addPropertyChangeListener(this);
        this.kurseSemesterwahlModel.addDisableFieldsListener(this);
        this.kurseSemesterwahlModel.addMakeErrorLabelsInvisibleListener(this);
        this.kurseSemesterwahlModel.addCompletedListener(new CompletedListener() {
            @Override
            public void completed(boolean completed) {
                onKurseSuchenModelCompleted(completed);
            }
        });
        this.setModelValidationMode(MODEL_VALIDATION_MODE);
        svmContext.getSvmModel().loadSemestersAll();
        svmContext.getSvmModel().loadLehrkraefteAll();
        svmContext.getSvmModel().loadKursorteAll();
        svmContext.getSvmModel().loadKurstypenAll();
    }

    public void constructionDone() {
        kurseSemesterwahlModel.initializeCompleted();
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
            kurseSemesterwahlModel.setSchuljahr(schuljahr);
        } catch (SvmValidationException ignore) {
        }
    }

    private void onSchuljahrSelected() {
        LOGGER.trace("KurseSemesterwahlController Event Schuljahre selected =" + spinnerSchuljahre.getValue());
        boolean equalFieldAndModelValue = equalsNullSafe(spinnerSchuljahre.getValue(), kurseSemesterwahlModel.getSchuljahr());
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
            kurseSemesterwahlModel.setSchuljahr((String) spinnerSchuljahre.getValue());
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
        kurseSemesterwahlModel.setSemesterbezeichnung(semesterbezeichnung);
    }

    private void onSemesterbezeichnungSelected() {
        LOGGER.trace("KurseSemesterwahlController Event Semesterbezeichnung selected=" + comboBoxSemesterbezeichnung.getSelectedItem());
        boolean equalFieldAndModelValue = equalsNullSafe(comboBoxSemesterbezeichnung.getSelectedItem(), kurseSemesterwahlModel.getSemesterbezeichnung());
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
        kurseSemesterwahlModel.setSemesterbezeichnung((Semesterbezeichnung) comboBoxSemesterbezeichnung.getSelectedItem());
    }

    public void setBtnOk(JButton btnOk) {
        this.btnOk = btnOk;
        if (isModelValidationMode()) {
            btnOk.setEnabled(false);
        }
        this.btnOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSuchen();
            }
        });
    }

    private void onSuchen() {
        LOGGER.trace("KurseSemesterwahlController OK gedrückt");
        if (!isModelValidationMode() && !validateOnSpeichern()) {
            return;
        }
        if (!kurseSemesterwahlModel.checkSemesterBereitsErfasst(svmContext.getSvmModel())) {
            JOptionPane.showMessageDialog(null, "Bevor Kurse für das " + kurseSemesterwahlModel.getSemesterbezeichnung() + " " + kurseSemesterwahlModel.getSchuljahr() + " erfasst werden können, muss zuerst das Semester erfasst werden.", "Semester muss zuerst erfasst werden", JOptionPane.INFORMATION_MESSAGE);
            SemestersPanel semestersPanel = new SemestersPanel(svmContext);
            semestersPanel.addCloseListener(closeListener);
            nextPanelListener.actionPerformed(new ActionEvent(new Object[]{semestersPanel.$$$getRootComponent$$$(), "Semester verwalten"}, ActionEvent.ACTION_PERFORMED, "Semester verwalten"));
            return;
        }
        KurseTableData kurseTableData = kurseSemesterwahlModel.suchen();
        KurseTableModel kurseTableModel = new KurseTableModel(kurseTableData);
        String titel = "Kurse " + kurseSemesterwahlModel.getSemesterbezeichnung() + " " + kurseSemesterwahlModel.getSchuljahr();
        KursePanel kursePanel = new KursePanel(svmContext, kurseSemesterwahlModel, kurseTableModel, titel);
        kursePanel.addCloseListener(closeListener);
        nextPanelListener.actionPerformed(new ActionEvent(new Object[]{kursePanel.$$$getRootComponent$$$(), titel}, ActionEvent.ACTION_PERFORMED, "Suchresultat verfügbar"));
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
        LOGGER.trace("KurseSemesterwahlController Abbrechen gedrückt");
        closeListener.actionPerformed(new ActionEvent(btnAbbrechen, ActionEvent.ACTION_PERFORMED, "Close nach Abbrechen"));
    }

    private void onKurseSuchenModelCompleted(boolean completed) {
        LOGGER.trace("KurseSemesterwahlModel completed=" + completed);
        if (completed) {
            btnOk.setToolTipText(null);
            btnOk.setEnabled(true);
        } else {
            btnOk.setToolTipText("Bitte Eingabedaten vervollständigen");
            btnOk.setEnabled(false);
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
            spinnerSchuljahre.setValue(kurseSemesterwahlModel.getSchuljahr());
        } else if (checkIsFieldChange(Field.SEMESTERBEZEICHNUNG, evt)) {
            comboBoxSemesterbezeichnung.setSelectedItem(kurseSemesterwahlModel.getSemesterbezeichnung());
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
