package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.CompletedListener;
import ch.metzenthin.svm.domain.model.KurseSemesterwahlModel;
import ch.metzenthin.svm.domain.model.KurseTableData;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.ui.componentmodel.KurseTableModel;
import ch.metzenthin.svm.ui.components.KursePanel;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
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
    private JSpinner spinnerSemester;
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

    public void setSpinnerSemester(JSpinner spinnerSemester) {
        this.spinnerSemester = spinnerSemester;
        java.util.List<Semester> semesterList = svmContext.getSvmModel().getSemestersAll();
        if (semesterList.isEmpty()) {
            // keine Semester erfasst
            SpinnerModel spinnerModel = new SpinnerListModel(new String[]{""});
            spinnerSemester.setModel(spinnerModel);
            spinnerSemester.setEnabled(false);
            return;
        }
        Semester[] semesters = semesterList.toArray(new Semester[semesterList.size()]);
        SpinnerModel spinnerModelSemester = new SpinnerListModel(semesters);
        spinnerSemester.setModel(spinnerModelSemester);
        spinnerSemester.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                onSemesterSelected();
            }
        });
        // Model initialisieren
        kurseSemesterwahlModel.setSemester(kurseSemesterwahlModel.getInitSemester(svmContext.getSvmModel()));
    }

    private void onSemesterSelected() {
        LOGGER.trace("KurseSemesterwahlController Event Semester selected =" + spinnerSemester.getValue());
        boolean equalFieldAndModelValue = equalsNullSafe(spinnerSemester.getValue(), kurseSemesterwahlModel.getSemester());
        setModelSemester();
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelSemester() {
        makeErrorLabelInvisible(Field.SEMESTER_KURS);
        kurseSemesterwahlModel.setSemester((Semester) spinnerSemester.getValue());
    }
    
    public void setBtnOk(JButton btnOk) {
        this.btnOk = btnOk;
        if (svmContext.getSvmModel().getSemestersAll().isEmpty() || isModelValidationMode()) {
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
            btnOk.setFocusPainted(false);
            return;
        }
        KurseTableData kurseTableData = kurseSemesterwahlModel.suchen();
        KurseTableModel kurseTableModel = new KurseTableModel(kurseTableData);
        String titel = "Kurse " + kurseSemesterwahlModel.getSemester().getSemesterbezeichnung() + " " + kurseSemesterwahlModel.getSemester().getSchuljahr();
        KursePanel kursePanel = new KursePanel(svmContext, kurseSemesterwahlModel, kurseTableModel, null, null, null, 0, false, false, titel);
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
        if (checkIsFieldChange(Field.SEMESTER, evt)) {
            spinnerSemester.setValue(kurseSemesterwahlModel.getSemester());
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
