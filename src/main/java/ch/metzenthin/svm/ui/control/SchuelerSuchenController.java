package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.dataTypes.*;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.CompletedListener;
import ch.metzenthin.svm.domain.model.SchuelerSuchenModel;
import ch.metzenthin.svm.domain.model.SchuelerSuchenTableData;
import ch.metzenthin.svm.persistence.entities.*;
import ch.metzenthin.svm.ui.componentmodel.SchuelerSuchenTableModel;
import ch.metzenthin.svm.ui.components.SchuelerDatenblattPanel;
import ch.metzenthin.svm.ui.components.SchuelerSuchenResultPanel;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.util.*;

import static ch.metzenthin.svm.common.utils.Converter.asString;
import static ch.metzenthin.svm.common.utils.SimpleValidator.equalsNullSafe;

/**
 * @author Martin Schraner
 */
public class SchuelerSuchenController extends PersonController {

    private static final Logger LOGGER = Logger.getLogger(SchuelerSuchenController.class);

    // Möglichkeit zum Umschalten des validation modes (nicht dynamisch)
    private static final boolean MODEL_VALIDATION_MODE = false;

    private JTextField txtGeburtsdatumSuchperiode;
    private JTextField txtStichtag;
    private JTextField txtZeitBeginn;
    private JTextField txtKuchenVorstellung;
    private JTextField txtZusatzattributMaerchen;
    private JTextArea txtAreaRollen;
    private JRadioButton radioBtnSchueler;
    private JRadioButton radioBtnEltern;
    private JRadioButton radioBtnRechnungsempfaenger;
    private JRadioButton radioBtnRolleAlle;
    private JRadioButton radioBtnAngemeldet;
    private JRadioButton radioBtnAbgemeldet;
    private JRadioButton radioBtnAnmeldestatusAlle;
    private JRadioButton radioBtnDispensiert;
    private JRadioButton radioBtnNichtDispensiert;
    private JRadioButton radioBtnDispensationAlle;
    private JRadioButton radioBtnWeiblich;
    private JRadioButton radioBtnMaennlich;
    private JRadioButton radioBtnGeschlechtAlle;
    private JSpinner spinnerSemesterKurs;
    private JSpinner spinnerMaerchen;
    private JComboBox<Wochentag> comboBoxWochentag;
    private JComboBox<Mitarbeiter> comboBoxLehrkraft;
    private JComboBox<SchuelerCode> comboBoxSchuelerCode;
    private JComboBox<Gruppe> comboBoxGruppe;
    private JComboBox<ElternmithilfeCode> comboBoxElternmithilfeCode;
    private JCheckBox checkBoxKursFuerSucheBeruecksichtigen;
    private JCheckBox checkBoxMaerchenFuerSucheBeruecksichtigen;
    private JLabel errLblGeburtsdatumSuchperiode;
    private JLabel errLblStichtag;
    private JLabel errLblZeitBeginn;
    private JLabel errLblRollen;
    private JLabel errLblKuchenVorstellung;
    private JLabel errLblZusatzattributMaerchen;
    private JButton btnSuchen;
    private JButton btnAbbrechen;
    private ActionListener closeListener;
    private ActionListener nextPanelListener;
    private final SvmContext svmContext;
    private SchuelerSuchenModel schuelerSuchenModel;
    private ActionListener zurueckListener;

    public SchuelerSuchenController(SvmContext svmContext, SchuelerSuchenModel schuelerSuchenModel) {
        super(schuelerSuchenModel);
        this.svmContext = svmContext;
        this.schuelerSuchenModel = schuelerSuchenModel;
        this.schuelerSuchenModel.addPropertyChangeListener(this);
        this.schuelerSuchenModel.addDisableFieldsListener(this);
        this.schuelerSuchenModel.addMakeErrorLabelsInvisibleListener(this);
        this.schuelerSuchenModel.addCompletedListener(new CompletedListener() {
            @Override
            public void completed(boolean completed) {
                onSchuelerSuchenModelCompleted(completed);
            }
        });
        this.setModelValidationMode(MODEL_VALIDATION_MODE);
    }

    public void setTxtGeburtsdatumSuchperiode(JTextField txtGeburtsdatumSuchperiode) {
        this.txtGeburtsdatumSuchperiode = txtGeburtsdatumSuchperiode;
        this.txtGeburtsdatumSuchperiode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onGeburtsdatumSuchperiodeEvent();
            }
        });
        this.txtGeburtsdatumSuchperiode.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onGeburtsdatumSuchperiodeEvent();
            }
        });
    }

    private void onGeburtsdatumSuchperiodeEvent() {
        LOGGER.trace("SchuelerSuchenController Event GeburtsdatumSuchperiode");
        boolean equalFieldAndModelValue = equalsNullSafe(txtGeburtsdatumSuchperiode.getText(), schuelerSuchenModel.getGeburtsdatumSuchperiode());
        try {
            setModelGeburtsdatumSuchperiode();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelGeburtsdatumSuchperiode() throws SvmValidationException {
        makeErrorLabelInvisible(Field.GEBURTSDATUM_SUCHPERIODE);
        try {
            schuelerSuchenModel.setGeburtsdatumSuchperiode(txtGeburtsdatumSuchperiode.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("SchuelerSuchenController setModelGeburtsdatum RequiredException=" + e.getMessage());
            if (isModelValidationMode()) {
                txtGeburtsdatumSuchperiode.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("SchuelerSuchenController setModelGeburtsdatum Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtStichtag(JTextField txtStichtag) {
        this.txtStichtag = txtStichtag;
        this.txtStichtag.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onStichtagEvent();
            }
        });
        this.txtStichtag.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onStichtagEvent();
            }
        });
        // Initialisieren mit heute
        try {
            schuelerSuchenModel.setStichtag(asString(new GregorianCalendar()));
        } catch (SvmValidationException ignore) {
        }
    }

    private void onStichtagEvent() {
        LOGGER.trace("SchuelerSuchenController Event Stichtag");
        boolean equalFieldAndModelValue = equalsNullSafe(txtStichtag.getText(), schuelerSuchenModel.getStichtag());
        try {
            setModelStichtag();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelStichtag() throws SvmValidationException {
        makeErrorLabelInvisible(Field.STICHTAG);
        try {
            schuelerSuchenModel.setStichtag(txtStichtag.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("SchuelerSuchenController setModelStichtag RequiredException=" + e.getMessage());
            if (isModelValidationMode()) {
                txtStichtag.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("SchuelerSuchenController setModelStichtag Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setComboBoxSchuelerCode(JComboBox<SchuelerCode> comboBoxCode) {
        this.comboBoxSchuelerCode = comboBoxCode;
        SchuelerCode[] selectableSchuelerCodes = schuelerSuchenModel.getSelectableSchuelerCodes(svmContext.getSvmModel());
        comboBoxCode.setModel(new DefaultComboBoxModel<>(selectableSchuelerCodes));
        // Model initialisieren mit erstem ComboBox-Wert
        schuelerSuchenModel.setSchuelerCode(selectableSchuelerCodes[0]);
        this.comboBoxSchuelerCode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSchuelerCodeSelected();
            }
        });
    }

    private void onSchuelerCodeSelected() {
        LOGGER.trace("SchuelerSuchenController Event SchuelerCode selected=" + comboBoxSchuelerCode.getSelectedItem());
        setModelSchuelerCode();
    }

    private void setModelSchuelerCode() {
        schuelerSuchenModel.setSchuelerCode((SchuelerCode) comboBoxSchuelerCode.getSelectedItem());
    }

    public void setSpinnerSemesterKurs(JSpinner spinnerSemesterKurs) {
        this.spinnerSemesterKurs = spinnerSemesterKurs;
        java.util.List<Semester> semesterList = svmContext.getSvmModel().getSemestersAll();
        if (semesterList.isEmpty()) {
            // keine Semester erfasst
            SpinnerModel spinnerModel = new SpinnerListModel(new String[]{""});
            spinnerSemesterKurs.setModel(spinnerModel);
            return;
        }
        Semester[] semesters = semesterList.toArray(new Semester[semesterList.size()]);
        SpinnerModel spinnerModelSemester = new SpinnerListModel(semesters);
        spinnerSemesterKurs.setModel(spinnerModelSemester);
        spinnerSemesterKurs.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                onSemesterKursSelected();
            }
        });
        // Model initialisieren
        schuelerSuchenModel.setSemesterKurs(schuelerSuchenModel.getSemesterInit(svmContext.getSvmModel()));
    }

    private void onSemesterKursSelected() {
        LOGGER.trace("SchuelerSuchenController Event Semester selected =" + spinnerSemesterKurs.getValue());
        boolean equalFieldAndModelValue = equalsNullSafe(spinnerSemesterKurs.getValue(), schuelerSuchenModel.getSemesterKurs());
        setModelSemesterKurs();
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelSemesterKurs() {
        makeErrorLabelInvisible(Field.SEMESTER_KURS);
        schuelerSuchenModel.setSemesterKurs((Semester) spinnerSemesterKurs.getValue());
    }

    public void setComboBoxWochentag(JComboBox<Wochentag> comboBoxWochentag) {
        this.comboBoxWochentag = comboBoxWochentag;
        comboBoxWochentag.setModel(new DefaultComboBoxModel<>(Wochentag.values()));
        comboBoxWochentag.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onWochentagSelected();
            }
        });
        // Wochentag in Model initialisieren mit erstem ComboBox-Wert
        schuelerSuchenModel.setWochentag(Wochentag.values()[0]);
    }

    private void onWochentagSelected() {
        LOGGER.trace("KurseSemesterwahlController Event Wochentag selected=" + comboBoxWochentag.getSelectedItem());
        boolean equalFieldAndModelValue = equalsNullSafe(comboBoxWochentag.getSelectedItem(), schuelerSuchenModel.getWochentag());
        setModelWochentag();
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelWochentag() {
        makeErrorLabelInvisible(Field.WOCHENTAG);
        schuelerSuchenModel.setWochentag((Wochentag) comboBoxWochentag.getSelectedItem());
    }

    public void setTxtZeitBeginn(JTextField txtZeitBeginn) {
        this.txtZeitBeginn = txtZeitBeginn;
        this.txtZeitBeginn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onZeitBeginnEvent();
            }
        });
        this.txtZeitBeginn.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onZeitBeginnEvent();
            }
        });
    }

    private void onZeitBeginnEvent() {
        LOGGER.trace("schuelerSuchenController Event ZeitBeginn");
        boolean equalFieldAndModelValue = equalsNullSafe(txtZeitBeginn.getText(), schuelerSuchenModel.getZeitBeginn());
        try {
            setModelZeitBeginn();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelZeitBeginn() throws SvmValidationException {
        makeErrorLabelInvisible(Field.ZEIT_BEGINN);
        try {
            schuelerSuchenModel.setZeitBeginn(txtZeitBeginn.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("SchuelerSuchenController setModelZeitBeginn Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setComboBoxLehrkraft(JComboBox<Mitarbeiter> comboBoxLehrkraft) {
        this.comboBoxLehrkraft = comboBoxLehrkraft;
        Mitarbeiter[] selectableLehrkraefte = schuelerSuchenModel.getSelectableLehrkraefte(svmContext.getSvmModel());
        comboBoxLehrkraft.setModel(new DefaultComboBoxModel<>(selectableLehrkraefte));
        // Model initialisieren mit erstem ComboBox-Wert
        schuelerSuchenModel.setMitarbeiter(selectableLehrkraefte[0]);
        comboBoxLehrkraft.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onLehrkraftSelected();
            }
        });
    }

    private void onLehrkraftSelected() {
        LOGGER.trace("SchuelerSuchenController Event Lehrkraft selected=" + comboBoxLehrkraft.getSelectedItem());
        boolean equalFieldAndModelValue = equalsNullSafe(comboBoxLehrkraft.getSelectedItem(), schuelerSuchenModel.getMitarbeiter());
        try {
            setModelLehrkraft();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelLehrkraft() throws SvmValidationException {
        makeErrorLabelInvisible(Field.LEHRKRAFT);
        schuelerSuchenModel.setMitarbeiter((Mitarbeiter) comboBoxLehrkraft.getSelectedItem());
    }

    public void setCheckBoxKursFuerSucheBeruecksichtigen(JCheckBox checkBoxKursFuerSucheBeruecksichtigen) {
        this.checkBoxKursFuerSucheBeruecksichtigen = checkBoxKursFuerSucheBeruecksichtigen;
        if (svmContext.getSvmModel().getSemestersAll().isEmpty()) {
            checkBoxKursFuerSucheBeruecksichtigen.setEnabled(false);
        }
        this.checkBoxKursFuerSucheBeruecksichtigen.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                onKursFuerSucheBeruecksichtigenEvent();
            }
        });
        // Initialisierung
        schuelerSuchenModel.setKursFuerSucheBeruecksichtigen(false);
    }

    private void onKursFuerSucheBeruecksichtigenEvent() {
        LOGGER.trace("SchuelerSuchenController Event KursFuerSucheBeruecksichtigen. Selected=" + checkBoxKursFuerSucheBeruecksichtigen.isSelected());
        setModelKursFuerSucheBeruecksichtigen();
    }

    private void setModelKursFuerSucheBeruecksichtigen() {
        schuelerSuchenModel.setKursFuerSucheBeruecksichtigen(checkBoxKursFuerSucheBeruecksichtigen.isSelected());
    }

    public void setSpinnerMaerchen(JSpinner spinnerMaerchen) {
        this.spinnerMaerchen = spinnerMaerchen;
        java.util.List<Maerchen> maerchenList = svmContext.getSvmModel().getMaerchensAll();
        if (maerchenList.isEmpty()) {
            // keine Märchen erfasst
            SpinnerModel spinnerModel = new SpinnerListModel(new String[]{""});
            spinnerMaerchen.setModel(spinnerModel);
            return;
        }
        Maerchen[] maerchens = maerchenList.toArray(new Maerchen[maerchenList.size()]);
        SpinnerModel spinnerModelMaerchen = new SpinnerListModel(maerchens);
        spinnerMaerchen.setModel(spinnerModelMaerchen);
        spinnerMaerchen.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                onMaerchenSelected();
            }
        });
        Maerchen maerchenInit = schuelerSuchenModel.getMaerchenInit(svmContext.getSvmModel());
        schuelerSuchenModel.setMaerchen(maerchenInit);
    }

    private void onMaerchenSelected() {
        LOGGER.trace("KurseSemesterwahlController Event Maerchen selected =" + spinnerMaerchen.getValue());
        boolean equalFieldAndModelValue = equalsNullSafe(spinnerMaerchen.getValue(), schuelerSuchenModel.getMaerchen());
        setModelMaerchen();
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelMaerchen() {
        makeErrorLabelInvisible(Field.MAERCHEN);
        schuelerSuchenModel.setMaerchen((Maerchen) spinnerMaerchen.getValue());
    }

    public void setComboBoxGruppe(JComboBox<Gruppe> comboBoxGruppe) {
        this.comboBoxGruppe = comboBoxGruppe;
        comboBoxGruppe.setModel(new DefaultComboBoxModel<>(Gruppe.values()));
        comboBoxGruppe.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onGruppeSelected();
            }
        });
        // Gruppe in Model initialisieren mit erstem ComboBox-Wert
        schuelerSuchenModel.setGruppe(Gruppe.values()[0]);
    }

    private void onGruppeSelected() {
        LOGGER.trace("SchuelerSuchenController Event Gruppe selected=" + comboBoxGruppe.getSelectedItem());
        boolean equalFieldAndModelValue = equalsNullSafe(comboBoxGruppe.getSelectedItem(), schuelerSuchenModel.getGruppe());
        setModelGruppe();
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelGruppe() {
        makeErrorLabelInvisible(Field.GRUPPE);
        schuelerSuchenModel.setGruppe((Gruppe) comboBoxGruppe.getSelectedItem());
    }

    public void setTxtAreaRollen(JTextArea txtAreaRollen) {
        this.txtAreaRollen = txtAreaRollen;
        this.txtAreaRollen.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onRollenEvent();
            }
        });
        this.txtAreaRollen.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, null);
        this.txtAreaRollen.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, null);
    }

    private void onRollenEvent() {
        LOGGER.trace("schuelerSuchenController Event Rollen");
        boolean equalFieldAndModelValue = equalsNullSafe(txtAreaRollen.getText(), schuelerSuchenModel.getRollen());
        try {
            setModelRollen();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelRollen() throws SvmValidationException {
        makeErrorLabelInvisible(Field.ROLLEN);
        try {
            schuelerSuchenModel.setRollen(txtAreaRollen.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("SchuelerSuchenController setModelRollen Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setComboBoxElternmithilfeCode(JComboBox<ElternmithilfeCode> comboBoxCode) {
        this.comboBoxElternmithilfeCode = comboBoxCode;
        ElternmithilfeCode[] selectableElternmithilfeCodes = schuelerSuchenModel.getSelectableElternmithilfeCodes(svmContext.getSvmModel());
        comboBoxCode.setModel(new DefaultComboBoxModel<>(selectableElternmithilfeCodes));
        // Model initialisieren mit erstem ComboBox-Wert
        schuelerSuchenModel.setElternmithilfeCode(selectableElternmithilfeCodes[0]);
        this.comboBoxElternmithilfeCode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onElternmithilfeCodeSelected();
            }
        });
    }

    private void onElternmithilfeCodeSelected() {
        LOGGER.trace("ElternmithilfeSuchenController Event ElternmithilfeCode selected=" + comboBoxElternmithilfeCode.getSelectedItem());
        setModelElternmithilfeCode();
    }

    private void setModelElternmithilfeCode() {
        schuelerSuchenModel.setElternmithilfeCode((ElternmithilfeCode) comboBoxElternmithilfeCode.getSelectedItem());
    }

    public void setTxtKuchenVorstellung(JTextField txtKuchenVorstellung) {
        this.txtKuchenVorstellung = txtKuchenVorstellung;
        this.txtKuchenVorstellung.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onKuchenVorstellungEvent();
            }
        });
        this.txtKuchenVorstellung.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onKuchenVorstellungEvent();
            }
        });
    }

    private void onKuchenVorstellungEvent() {
        LOGGER.trace("schuelerSuchenController Event KuchenVorstellung");
        boolean equalFieldAndModelValue = equalsNullSafe(txtKuchenVorstellung.getText(), schuelerSuchenModel.getKuchenVorstellung());
        try {
            setModelKuchenVorstellung();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelKuchenVorstellung() throws SvmValidationException {
        makeErrorLabelInvisible(Field.KUCHEN_VORSTELLUNG);
        try {
            schuelerSuchenModel.setKuchenVorstellung(txtKuchenVorstellung.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("SchuelerSuchenController setModelKuchenVorstellung Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtZusatzattributMaerchen(JTextField txtZusatzattributMaerchen) {
        this.txtZusatzattributMaerchen = txtZusatzattributMaerchen;
        this.txtZusatzattributMaerchen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onZusatzattributMaerchenEvent();
            }
        });
        this.txtZusatzattributMaerchen.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onZusatzattributMaerchenEvent();
            }
        });
    }

    private void onZusatzattributMaerchenEvent() {
        LOGGER.trace("schuelerSuchenController Event ZusatzattributMaerchen");
        boolean equalFieldAndModelValue = equalsNullSafe(txtZusatzattributMaerchen.getText(), schuelerSuchenModel.getZusatzattributMaerchen());
        try {
            setModelZusatzattributMaerchen();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelZusatzattributMaerchen() throws SvmValidationException {
        makeErrorLabelInvisible(Field.ZUSATZATTRIBUT_MAERCHEN);
        try {
            schuelerSuchenModel.setZusatzattributMaerchen(txtZusatzattributMaerchen.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("SchuelerSuchenController setModelZusatzattributMaerchen Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setCheckBoxMaerchenFuerSucheBeruecksichtigen(JCheckBox checkBoxMaerchenFuerSucheBeruecksichtigen) {
        this.checkBoxMaerchenFuerSucheBeruecksichtigen = checkBoxMaerchenFuerSucheBeruecksichtigen;
        if (svmContext.getSvmModel().getMaerchensAll().isEmpty()) {
            checkBoxMaerchenFuerSucheBeruecksichtigen.setEnabled(false);
        }
        this.checkBoxMaerchenFuerSucheBeruecksichtigen.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                onMaerchenFuerSucheBeruecksichtigenEvent();
            }
        });
        // Initialisierung
        schuelerSuchenModel.setMaerchenFuerSucheBeruecksichtigen(false);
    }

    private void onMaerchenFuerSucheBeruecksichtigenEvent() {
        LOGGER.trace("SchuelerSuchenController Event MaerchenFuerSucheBeruecksichtigen. Selected=" + checkBoxMaerchenFuerSucheBeruecksichtigen.isSelected());
        setModelMaerchenFuerSucheBeruecksichtigen();
    }

    private void setModelMaerchenFuerSucheBeruecksichtigen() {
        schuelerSuchenModel.setMaerchenFuerSucheBeruecksichtigen(checkBoxMaerchenFuerSucheBeruecksichtigen.isSelected());
    }

    public void setErrLblStichtag(JLabel errLblStichtag) {
        this.errLblStichtag = errLblStichtag;
    }

    public void setErrLblGeburtsdatumSuchperiode(JLabel errLblGeburtsdatumSuchperiode) {
        this.errLblGeburtsdatumSuchperiode = errLblGeburtsdatumSuchperiode;
    }

    public void setErrLblZeitBeginn(JLabel errLblZeitBeginn) {
        this.errLblZeitBeginn = errLblZeitBeginn;
    }

    public void setErrLblRollen(JLabel errLblRollen) {
        this.errLblRollen = errLblRollen;
    }

    public void setErrLblKuchenVorstellung(JLabel errLblKuchenVorstellung) {
        this.errLblKuchenVorstellung = errLblKuchenVorstellung;
    }

    public void setErrLblZusatzattributMaerchen(JLabel errLblZusatzattributMaerchen) {
        this.errLblZusatzattributMaerchen = errLblZusatzattributMaerchen;
    }

    public void setRadioBtnGroupRolle(JRadioButton radioBtnSchueler, JRadioButton radioBtnEltern, JRadioButton radioBtnRechnungsempfaenger, JRadioButton radioBtnRolleAlle) {
        this.radioBtnSchueler = radioBtnSchueler;
        this.radioBtnEltern = radioBtnEltern;
        this.radioBtnRechnungsempfaenger = radioBtnRechnungsempfaenger;
        this.radioBtnRolleAlle = radioBtnRolleAlle;
        // Action Commands
        this.radioBtnSchueler.setActionCommand(SchuelerSuchenModel.RolleSelected.SCHUELER.toString());
        this.radioBtnEltern.setActionCommand(SchuelerSuchenModel.RolleSelected.ELTERN.toString());
        this.radioBtnRechnungsempfaenger.setActionCommand(SchuelerSuchenModel.RolleSelected.RECHNUNGSEMPFAENGER.toString());
        this.radioBtnRolleAlle.setActionCommand(SchuelerSuchenModel.RolleSelected.ALLE.toString());
        // Listener
        RadioBtnGroupRolleListener radioBtnGroupRolleListener = new RadioBtnGroupRolleListener();
        this.radioBtnSchueler.addActionListener(radioBtnGroupRolleListener);
        this.radioBtnEltern.addActionListener(radioBtnGroupRolleListener);
        this.radioBtnRechnungsempfaenger.addActionListener(radioBtnGroupRolleListener);
        this.radioBtnRolleAlle.addActionListener(radioBtnGroupRolleListener);
        // Initialisieren mit Schüler
        schuelerSuchenModel.setRolle(SchuelerSuchenModel.RolleSelected.SCHUELER);
    }

    public void setRadioBtnGroupAnmeldestatus(JRadioButton radioBtnAngemeldet, JRadioButton radioBtnAbgemeldet, JRadioButton radioBtnAnmeldestatusAlle) {
        this.radioBtnAngemeldet = radioBtnAngemeldet;
        this.radioBtnAbgemeldet = radioBtnAbgemeldet;
        this.radioBtnAnmeldestatusAlle = radioBtnAnmeldestatusAlle;
        // Action Commands
        this.radioBtnAngemeldet.setActionCommand(SchuelerSuchenModel.AnmeldestatusSelected.ANGEMELDET.toString());
        this.radioBtnAbgemeldet.setActionCommand(SchuelerSuchenModel.AnmeldestatusSelected.ABGEMELDET.toString());
        this.radioBtnAnmeldestatusAlle.setActionCommand(SchuelerSuchenModel.AnmeldestatusSelected.ALLE.toString());
        // Listener
        RadioBtnGroupAnmeldestatusListener radioBtnGroupAnmeldestatusListener = new RadioBtnGroupAnmeldestatusListener();
        this.radioBtnAngemeldet.addActionListener(radioBtnGroupAnmeldestatusListener);
        this.radioBtnAbgemeldet.addActionListener(radioBtnGroupAnmeldestatusListener);
        this.radioBtnAnmeldestatusAlle.addActionListener(radioBtnGroupAnmeldestatusListener);
        // Initialisieren mit angemeldet
        schuelerSuchenModel.setAnmeldestatus(SchuelerSuchenModel.AnmeldestatusSelected.ANGEMELDET);
    }

    public void setRadioBtnGroupDispensation(JRadioButton radioBtnDispensiert, JRadioButton radioBtnNichtDispensiert, JRadioButton radioBtnDispensationAlle) {
        this.radioBtnDispensiert = radioBtnDispensiert;
        this.radioBtnNichtDispensiert = radioBtnNichtDispensiert;
        this.radioBtnDispensationAlle = radioBtnDispensationAlle;
        // Action Commands
        this.radioBtnDispensiert.setActionCommand(SchuelerSuchenModel.DispensationSelected.DISPENSIERT.toString());
        this.radioBtnNichtDispensiert.setActionCommand(SchuelerSuchenModel.DispensationSelected.NICHT_DISPENSIERT.toString());
        this.radioBtnDispensationAlle.setActionCommand(SchuelerSuchenModel.DispensationSelected.ALLE.toString());
        // Listener
        RadioBtnGroupDispensationListener radioBtnGroupDispensationListener = new RadioBtnGroupDispensationListener();
        this.radioBtnDispensiert.addActionListener(radioBtnGroupDispensationListener);
        this.radioBtnNichtDispensiert.addActionListener(radioBtnGroupDispensationListener);
        this.radioBtnDispensationAlle.addActionListener(radioBtnGroupDispensationListener);
        // Initialisieren mit alle
        schuelerSuchenModel.setDispensation(SchuelerSuchenModel.DispensationSelected.ALLE);
    }

    public void setRadioBtnGroupGeschlecht(JRadioButton radioBtnWeiblich, JRadioButton radioBtnMaennlich, JRadioButton radioBtnGeschlechtAlle) {
        this.radioBtnWeiblich = radioBtnWeiblich;
        this.radioBtnMaennlich = radioBtnMaennlich;
        this.radioBtnGeschlechtAlle = radioBtnGeschlechtAlle;
        // Action Commands
        this.radioBtnWeiblich.setActionCommand(SchuelerSuchenModel.GeschlechtSelected.WEIBLICH.toString());
        this.radioBtnMaennlich.setActionCommand(SchuelerSuchenModel.GeschlechtSelected.MAENNLICH.toString());
        this.radioBtnGeschlechtAlle.setActionCommand(SchuelerSuchenModel.GeschlechtSelected.ALLE.toString());
        // Listener
        RadioBtnGroupGeschlechtListener radioBtnGroupGeschlechtListener = new RadioBtnGroupGeschlechtListener();
        this.radioBtnWeiblich.addActionListener(radioBtnGroupGeschlechtListener);
        this.radioBtnMaennlich.addActionListener(radioBtnGroupGeschlechtListener);
        this.radioBtnGeschlechtAlle.addActionListener(radioBtnGroupGeschlechtListener);
        // Initialisieren mit alle
        schuelerSuchenModel.setGeschlecht(SchuelerSuchenModel.GeschlechtSelected.ALLE);
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
        LOGGER.trace("SchuelerSuchenPanel Suchen gedrückt");
        if (!validateOnSpeichern()) {
            btnSuchen.setFocusPainted(false);
            return;
        }
        if (schuelerSuchenModel.isKursFuerSucheBeruecksichtigen() && !schuelerSuchenModel.checkIfKurseExist()) {
            String errMsg;
            String title;
            if (schuelerSuchenModel.searchForSpecificKurs()) {
                errMsg = "Es wurde kein Kurs gefunden, welcher auf die Suchabfrage passt.";
                title = "Kein Kurs gefunden";
            } else {
                errMsg = "Es wurden keine Kurse gefunden, welche auf die Suchabfrage passen.";
                title = "Keine Kurse gefunden";
            }
            JOptionPane.showMessageDialog(null, errMsg, title, JOptionPane.INFORMATION_MESSAGE, svmContext.getDialogIcons().getInformationIcon());
            btnSuchen.setFocusPainted(false);
            return;
        }
        SchuelerSuchenTableData schuelerSuchenTableData = schuelerSuchenModel.suchen(svmContext.getSvmModel());
        SchuelerSuchenTableModel schuelerSuchenTableModel = new SchuelerSuchenTableModel(schuelerSuchenTableData);
        if (schuelerSuchenTableData.size() > 1
                || (schuelerSuchenTableData.size() == 1 && (schuelerSuchenModel.isKursFuerSucheBeruecksichtigen() || schuelerSuchenModel.isMaerchenFuerSucheBeruecksichtigen()))
                || (schuelerSuchenTableData.size() == 0 && schuelerSuchenModel.isKursFuerSucheBeruecksichtigen() && !schuelerSuchenModel.isMaerchenFuerSucheBeruecksichtigen())) {
            SchuelerSuchenResultPanel schuelerSuchenResultPanel = new SchuelerSuchenResultPanel(svmContext, schuelerSuchenTableModel);
            schuelerSuchenResultPanel.addNextPanelListener(nextPanelListener);
            schuelerSuchenResultPanel.addCloseListener(closeListener);
            schuelerSuchenResultPanel.addZurueckListener(zurueckListener);
            nextPanelListener.actionPerformed(new ActionEvent(new Object[]{schuelerSuchenResultPanel.$$$getRootComponent$$$(), "Suchresultat"}, ActionEvent.ACTION_PERFORMED, "Suchresultat verfügbar"));
        } else if (schuelerSuchenTableData.size() == 1) {
            // Direkt zum Datenblatt, falls nur ein Suchresultat und nicht nach einem spezifischen Kurs gesucht wurde
            SchuelerDatenblattPanel schuelerDatenblattPanel = new SchuelerDatenblattPanel(svmContext, schuelerSuchenTableModel, null, 0, true);
            schuelerDatenblattPanel.addCloseListener(closeListener);
            schuelerDatenblattPanel.addNextPanelListener(nextPanelListener);
            schuelerDatenblattPanel.addZurueckZuSchuelerSuchenListener(zurueckListener);
            nextPanelListener.actionPerformed(new ActionEvent(new Object[]{schuelerDatenblattPanel.$$$getRootComponent$$$(), "Datenblatt"}, ActionEvent.ACTION_PERFORMED, "Schüler gespeichert"));
        } else {
            JOptionPane.showMessageDialog(null, "Es wurden keine Schüler gefunden, welche auf die Suchabfrage passen.", "Keine Schüler gefunden", JOptionPane.INFORMATION_MESSAGE, svmContext.getDialogIcons().getInformationIcon());
            btnSuchen.setFocusPainted(false);
        }
    }

    public void setBtnAbbrechen(JButton btnAbbrechen) {
        this.btnAbbrechen = btnAbbrechen;
        this.btnAbbrechen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onAbbrechen();
            }
        });
        btnAbbrechen.setVisible(false); // Ist Startseite. Such-Kritierien abbrechen über Menü.
    }

    private void onAbbrechen() {
        LOGGER.trace("SchuelerSuchenPanel Abbrechen gedrückt");
        closeListener.actionPerformed(new ActionEvent(btnAbbrechen, ActionEvent.ACTION_PERFORMED, "Close nach Abbrechen"));
    }

    private void onSchuelerSuchenModelCompleted(boolean completed) {
        LOGGER.trace("SchuelerSuchenModel completed=" + completed);
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

    void enableDisableFields() {
        if (schuelerSuchenModel.getRolle() != null && schuelerSuchenModel.getRolle() != SchuelerSuchenModel.RolleSelected.SCHUELER) {
            disableGeburtsdatumSuchperiode();
        } else {
            enableGeburtsdatumSuchperiode();
        }
        if (schuelerSuchenModel.getAnmeldestatus() != null && schuelerSuchenModel.getAnmeldestatus() != SchuelerSuchenModel.AnmeldestatusSelected.ANGEMELDET) {
            disableDispensationSelection();
        } else {
            enableDispensationSelection();
        }
        if (schuelerSuchenModel.isKursFuerSucheBeruecksichtigen()) {
            enableKursSuche();
        } else {
            disableKursSuche();
        }
        if (schuelerSuchenModel.isMaerchenFuerSucheBeruecksichtigen()) {
            enableMaerchenSuche();
        } else {
            disableMaerchenSuche();
        }
    }

    private void enableGeburtsdatumSuchperiode() {
        schuelerSuchenModel.enableFields(getGeburtsdatumSuchperiodeFields());
    }

    private void disableGeburtsdatumSuchperiode() {
        schuelerSuchenModel.disableFields(getGeburtsdatumSuchperiodeFields());
        schuelerSuchenModel.makeErrorLabelsInvisible(getGeburtsdatumSuchperiodeFields());
        // Textfeld in jedem Fall leeren, auch wenn Model-Wert noch null und damit von
        // schuelerSuchenModel.invalidateGeburtsdatumSuchperiode() kein Property Change-Event ausgelöst wird
        txtGeburtsdatumSuchperiode.setText("");
        schuelerSuchenModel.invalidateGeburtsdatumSuchperiode();
    }

    private void enableDispensationSelection() {
        schuelerSuchenModel.enableFields(getDispensationFields());
    }

    private void disableDispensationSelection() {
        schuelerSuchenModel.disableFields(getDispensationFields());
        schuelerSuchenModel.setDispensation(SchuelerSuchenModel.DispensationSelected.ALLE);
    }

    private void enableKursSuche() {
        schuelerSuchenModel.enableFields(getKursFields());
    }

    private void disableKursSuche() {
        schuelerSuchenModel.disableFields(getKursFields());
        schuelerSuchenModel.makeErrorLabelsInvisible(getKursFields());
    }

    private void enableMaerchenSuche() {
        schuelerSuchenModel.enableFields(getMaerchenFields());
    }

    private void disableMaerchenSuche() {
        schuelerSuchenModel.disableFields(getMaerchenFields());
        schuelerSuchenModel.makeErrorLabelsInvisible(getMaerchenFields());
    }

    private Set<Field> getGeburtsdatumSuchperiodeFields() {
        Set<Field> geburtsdatumSuchperiodeFields = new HashSet<>();
        geburtsdatumSuchperiodeFields.add(Field.GEBURTSDATUM_SUCHPERIODE);
        return geburtsdatumSuchperiodeFields;
    }

    private Set<Field> getDispensationFields() {
        Set<Field> dispensationFields = new HashSet<>();
        dispensationFields.add(Field.DISPENSIERT);
        dispensationFields.add(Field.NICHT_DISPENSIERT);
        dispensationFields.add(Field.DISPENSATION_ALLE);
        return dispensationFields;
    }

    private Set<Field> getKursFields() {
        Set<Field> kursFields = new HashSet<>();
        kursFields.add(Field.SEMESTER_KURS);
        kursFields.add(Field.SEMESTERBEZEICHNUNG);
        kursFields.add(Field.WOCHENTAG);
        kursFields.add(Field.ZEIT_BEGINN);
        kursFields.add(Field.LEHRKRAFT);
        return kursFields;
    }

    private Set<Field> getMaerchenFields() {
        Set<Field> maerchenFields = new HashSet<>();
        maerchenFields.add(Field.MAERCHEN);
        maerchenFields.add(Field.GRUPPE);
        maerchenFields.add(Field.ROLLEN);
        maerchenFields.add(Field.ELTERNMITHILFE_CODE);
        maerchenFields.add(Field.KUCHEN_VORSTELLUNG);
        maerchenFields.add(Field.ZUSATZATTRIBUT_MAERCHEN);
        return maerchenFields;
    }

    @Override
    void doPropertyChange(PropertyChangeEvent evt) {
        enableDisableFields();
        super.doPropertyChange(evt);
        if (checkIsFieldChange(Field.GEBURTSDATUM_SUCHPERIODE, evt)) {
            txtGeburtsdatumSuchperiode.setText(schuelerSuchenModel.getGeburtsdatumSuchperiode());
        }
        else if (checkIsFieldChange(Field.ROLLE, evt) && evt.getNewValue() == SchuelerSuchenModel.RolleSelected.SCHUELER) {
            radioBtnSchueler.setSelected(true);
        }
        else if (checkIsFieldChange(Field.ROLLE, evt) && evt.getNewValue() == SchuelerSuchenModel.RolleSelected.ELTERN) {
            radioBtnEltern.setSelected(true);
        }
        else if (checkIsFieldChange(Field.ROLLE, evt) && evt.getNewValue() == SchuelerSuchenModel.RolleSelected.RECHNUNGSEMPFAENGER) {
            radioBtnRechnungsempfaenger.setSelected(true);
        }
        else if (checkIsFieldChange(Field.ROLLE, evt) && evt.getNewValue() == SchuelerSuchenModel.RolleSelected.ALLE) {
            radioBtnRolleAlle.setSelected(true);
        }
        else if (checkIsFieldChange(Field.ANMELDESTATUS, evt) && evt.getNewValue() == SchuelerSuchenModel.AnmeldestatusSelected.ANGEMELDET) {
            radioBtnAngemeldet.setSelected(true);
        }
        else if (checkIsFieldChange(Field.ANMELDESTATUS, evt) && evt.getNewValue() == SchuelerSuchenModel.AnmeldestatusSelected.ABGEMELDET) {
            radioBtnAbgemeldet.setSelected(true);
        }
        else if (checkIsFieldChange(Field.ANMELDESTATUS, evt) && evt.getNewValue() == SchuelerSuchenModel.AnmeldestatusSelected.ALLE) {
            radioBtnAnmeldestatusAlle.setSelected(true);
        }
        else if (checkIsFieldChange(Field.DISPENSATION, evt) && evt.getNewValue() == SchuelerSuchenModel.DispensationSelected.DISPENSIERT) {
            radioBtnDispensiert.setSelected(true);
        }
        else if (checkIsFieldChange(Field.DISPENSATION, evt) && evt.getNewValue() == SchuelerSuchenModel.DispensationSelected.NICHT_DISPENSIERT) {
            radioBtnNichtDispensiert.setSelected(true);
        }
        else if (checkIsFieldChange(Field.DISPENSATION, evt) && evt.getNewValue() == SchuelerSuchenModel.DispensationSelected.ALLE) {
            radioBtnDispensationAlle.setSelected(true);
        }
        else if (checkIsFieldChange(Field.GESCHLECHT, evt) && evt.getNewValue() == SchuelerSuchenModel.GeschlechtSelected.WEIBLICH) {
            radioBtnWeiblich.setSelected(true);
        }
        else if (checkIsFieldChange(Field.GESCHLECHT, evt) && evt.getNewValue() == SchuelerSuchenModel.GeschlechtSelected.MAENNLICH) {
            radioBtnMaennlich.setSelected(true);
        }
        else if (checkIsFieldChange(Field.GESCHLECHT, evt) && evt.getNewValue() == SchuelerSuchenModel.GeschlechtSelected.ALLE) {
            radioBtnGeschlechtAlle.setSelected(true);
        }
        else if (checkIsFieldChange(Field.STICHTAG, evt)) {
            txtStichtag.setText(asString(schuelerSuchenModel.getStichtag()));
        }
        else if (checkIsFieldChange(Field.SCHUELER_CODE, evt)) {
            comboBoxSchuelerCode.setSelectedItem(schuelerSuchenModel.getSchuelerCode());
        }
        else if (checkIsFieldChange(Field.SEMESTER_KURS, evt)) {
            spinnerSemesterKurs.setValue(schuelerSuchenModel.getSemesterKurs());
        }
        else if (checkIsFieldChange(Field.WOCHENTAG, evt)) {
            comboBoxWochentag.setSelectedItem(schuelerSuchenModel.getWochentag());
        }
        else if (checkIsFieldChange(Field.ZEIT_BEGINN, evt)) {
            txtZeitBeginn.setText(asString(schuelerSuchenModel.getZeitBeginn()));
        }
        else if (checkIsFieldChange(Field.LEHRKRAFT, evt)) {
            comboBoxLehrkraft.setSelectedItem(schuelerSuchenModel.getMitarbeiter());
        }
        else if (checkIsFieldChange(Field.KURS_FUER_SUCHE_BERUECKSICHTIGEN, evt)) {
            checkBoxKursFuerSucheBeruecksichtigen.setSelected(schuelerSuchenModel.isKursFuerSucheBeruecksichtigen());
        }
        else if (checkIsFieldChange(Field.MAERCHEN, evt)) {
            spinnerMaerchen.setValue(schuelerSuchenModel.getMaerchen());
        }
        else if (checkIsFieldChange(Field.GRUPPE, evt)) {
            comboBoxGruppe.setSelectedItem(schuelerSuchenModel.getGruppe());
        }
        else if (checkIsFieldChange(Field.ROLLEN, evt)) {
            txtAreaRollen.setText(schuelerSuchenModel.getRollen());
        }
        else if (checkIsFieldChange(Field.ELTERNMITHILFE_CODE, evt)) {
            comboBoxElternmithilfeCode.setSelectedItem(schuelerSuchenModel.getElternmithilfeCode());
        }
        else if (checkIsFieldChange(Field.KUCHEN_VORSTELLUNG, evt)) {
            txtKuchenVorstellung.setText(schuelerSuchenModel.getKuchenVorstellung().toString());
        }
        else if (checkIsFieldChange(Field.ZUSATZATTRIBUT_MAERCHEN, evt)) {
            txtZusatzattributMaerchen.setText(schuelerSuchenModel.getZusatzattributMaerchen());
        }
        else if (checkIsFieldChange(Field.MAERCHEN_FUER_SUCHE_BERUECKSICHTIGEN, evt)) {
            checkBoxMaerchenFuerSucheBeruecksichtigen.setSelected(schuelerSuchenModel.isMaerchenFuerSucheBeruecksichtigen());
        }
    }

    @Override
    void validateFields() throws SvmValidationException {
        super.validateFields();
        if (txtGeburtsdatumSuchperiode.isEnabled()) {
            LOGGER.trace("Validate field GeburtsdatumSuchperiode");
            setModelGeburtsdatumSuchperiode();
        }
        if (txtStichtag.isEnabled()) {
            LOGGER.trace("Validate field Stichtag");
            setModelStichtag();
        }
        if (txtZeitBeginn.isEnabled()) {
            LOGGER.trace("Validate field ZeitBeginn");
            setModelZeitBeginn();
        }
        if (txtAreaRollen.isEnabled()) {
            LOGGER.trace("Validate field Rollen");
            setModelRollen();
        }
        if (txtKuchenVorstellung.isEnabled()) {
            LOGGER.trace("Validate field KuchenVorstellung");
            setModelKuchenVorstellung();
        }
        if (txtZusatzattributMaerchen.isEnabled()) {
            LOGGER.trace("Validate field ZusatzattributMaerchen");
            setModelZusatzattributMaerchen();
        }
    }

    @Override
    void showErrMsg(SvmValidationException e) {
        super.showErrMsg(e);
        if (e.getAffectedFields().contains(Field.GEBURTSDATUM_SUCHPERIODE)) {
            errLblGeburtsdatumSuchperiode.setVisible(true);
            errLblGeburtsdatumSuchperiode.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.STICHTAG)) {
            errLblStichtag.setVisible(true);
            errLblStichtag.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ZEIT_BEGINN)) {
            errLblZeitBeginn.setVisible(true);
            errLblZeitBeginn.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ROLLEN)) {
            errLblRollen.setVisible(true);
            errLblRollen.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.KUCHEN_VORSTELLUNG)) {
            errLblKuchenVorstellung.setVisible(true);
            errLblKuchenVorstellung.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ZUSATZATTRIBUT_MAERCHEN)) {
            errLblZusatzattributMaerchen.setVisible(true);
            errLblZusatzattributMaerchen.setText(e.getMessage());
        }
    }

    @Override
    void showErrMsgAsToolTip(SvmValidationException e) {
        super.showErrMsgAsToolTip(e);
        if (e.getAffectedFields().contains(Field.GEBURTSDATUM_SUCHPERIODE)) {
            txtGeburtsdatumSuchperiode.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.STICHTAG)) {
            txtStichtag.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ZEIT_BEGINN)) {
            txtZeitBeginn.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ROLLEN)) {
            txtAreaRollen.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.KUCHEN_VORSTELLUNG)) {
            txtKuchenVorstellung.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ZUSATZATTRIBUT_MAERCHEN)) {
            txtZusatzattributMaerchen.setToolTipText(e.getMessage());
        }
    }

    @Override
    public void makeErrorLabelsInvisible(Set<Field> fields) {
        super.makeErrorLabelsInvisible(fields);
        if (fields.contains(Field.ALLE) || fields.contains(Field.GEBURTSDATUM_SUCHPERIODE)) {
            if (errLblGeburtsdatumSuchperiode != null) {
                errLblGeburtsdatumSuchperiode.setVisible(false);
            }
            if (txtGeburtsdatumSuchperiode != null) {
                txtGeburtsdatumSuchperiode.setToolTipText(null);
            }
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.STICHTAG)) {
            if (errLblStichtag != null) {
                errLblStichtag.setVisible(false);
            }
            if (txtStichtag != null) {
                txtStichtag.setToolTipText(null);
            }
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.ZEIT_BEGINN)) {
            if (errLblZeitBeginn != null) {
                errLblZeitBeginn.setVisible(false);
            }
            if (txtZeitBeginn != null) {
                txtZeitBeginn.setToolTipText(null);
            }
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.ROLLEN)) {
            if (errLblRollen != null) {
                errLblRollen.setVisible(false);
            }
            if (txtAreaRollen != null) {
                txtAreaRollen.setToolTipText(null);
            }
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.KUCHEN_VORSTELLUNG)) {
            if (errLblKuchenVorstellung != null) {
                errLblKuchenVorstellung.setVisible(false);
            }
            if (txtKuchenVorstellung != null) {
                txtKuchenVorstellung.setToolTipText(null);
            }
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.ZUSATZATTRIBUT_MAERCHEN)) {
            if (errLblZusatzattributMaerchen != null) {
                errLblZusatzattributMaerchen.setVisible(false);
            }
            if (txtZusatzattributMaerchen != null) {
                txtZusatzattributMaerchen.setToolTipText(null);
            }
        }
    }

    @Override
    public void disableFields(boolean disable, Set<Field> fields) {
        super.disableFields(disable, fields);
        if (txtGeburtsdatumSuchperiode != null && (fields.contains(Field.ALLE) || fields.contains(Field.GEBURTSDATUM_SUCHPERIODE))) {
            txtGeburtsdatumSuchperiode.setEnabled(!disable);
        }
        if (radioBtnSchueler != null && (fields.contains(Field.ALLE) || fields.contains(Field.SCHUELER))) {
            radioBtnSchueler.setEnabled(!disable);
        }
        if (radioBtnEltern != null && (fields.contains(Field.ALLE) || fields.contains(Field.ELTERN))) {
            radioBtnEltern.setEnabled(!disable);
        }
        if (radioBtnRechnungsempfaenger != null && (fields.contains(Field.ALLE) || fields.contains(Field.RECHNUNGSEMPFAENGER))) {
            radioBtnRechnungsempfaenger.setEnabled(!disable);
        }
        if (radioBtnRolleAlle != null && (fields.contains(Field.ALLE) || fields.contains(Field.ROLLE_ALLE))) {
            radioBtnRolleAlle.setEnabled(!disable);
        }
        if (radioBtnAngemeldet != null && (fields.contains(Field.ALLE) || fields.contains(Field.ANGEMELDET))) {
            radioBtnAngemeldet.setEnabled(!disable);
        }
        if (radioBtnAbgemeldet != null && (fields.contains(Field.ALLE) || fields.contains(Field.ABGEMELDET))) {
            radioBtnAbgemeldet.setEnabled(!disable);
        }
        if (radioBtnAnmeldestatusAlle != null && (fields.contains(Field.ALLE) || fields.contains(Field.ANMELDESTATUS_ALLE))) {
            radioBtnAnmeldestatusAlle.setEnabled(!disable);
        }
        if (radioBtnDispensiert != null && (fields.contains(Field.ALLE) || fields.contains(Field.DISPENSIERT))) {
            radioBtnDispensiert.setEnabled(!disable);
        }
        if (radioBtnNichtDispensiert != null && (fields.contains(Field.ALLE) || fields.contains(Field.NICHT_DISPENSIERT))) {
            radioBtnNichtDispensiert.setEnabled(!disable);
        }
        if (radioBtnDispensationAlle != null && (fields.contains(Field.ALLE) || fields.contains(Field.DISPENSATION_ALLE))) {
            radioBtnDispensationAlle.setEnabled(!disable);
        }
        if (radioBtnWeiblich != null && (fields.contains(Field.ALLE) || fields.contains(Field.WEIBLICH))) {
            radioBtnWeiblich.setEnabled(!disable);
        }
        if (radioBtnMaennlich != null && (fields.contains(Field.ALLE) || fields.contains(Field.MAENNLICH))) {
            radioBtnMaennlich.setEnabled(!disable);
        }
        if (radioBtnGeschlechtAlle != null && (fields.contains(Field.ALLE) || fields.contains(Field.GESCHLECHT_ALLE))) {
            radioBtnGeschlechtAlle.setEnabled(!disable);
        }
        if (txtStichtag != null && (fields.contains(Field.ALLE) || fields.contains(Field.STICHTAG))) {
            txtStichtag.setEnabled(!disable);
        }
        if (comboBoxSchuelerCode != null && (fields.contains(Field.ALLE) || fields.contains(Field.CODE))) {
            comboBoxSchuelerCode.setEnabled(!disable);
        }
        if (spinnerSemesterKurs != null && (fields.contains(Field.ALLE) || fields.contains(Field.SEMESTER_KURS))) {
            spinnerSemesterKurs.setEnabled(!disable);
        }
        if (comboBoxWochentag != null && (fields.contains(Field.ALLE) || fields.contains(Field.WOCHENTAG))) {
            comboBoxWochentag.setEnabled(!disable);
        }
        if (txtZeitBeginn != null && (fields.contains(Field.ALLE) || fields.contains(Field.ZEIT_BEGINN))) {
            txtZeitBeginn.setEnabled(!disable);
        }
        if (comboBoxLehrkraft != null && (fields.contains(Field.ALLE) || fields.contains(Field.LEHRKRAFT))) {
            comboBoxLehrkraft.setEnabled(!disable);
        }
        if (checkBoxKursFuerSucheBeruecksichtigen != null && (fields.contains(Field.ALLE) || fields.contains(Field.KURS_FUER_SUCHE_BERUECKSICHTIGEN))) {
            checkBoxKursFuerSucheBeruecksichtigen.setEnabled(!disable);
        }
        if (spinnerMaerchen != null && (fields.contains(Field.ALLE) || fields.contains(Field.MAERCHEN))) {
            spinnerMaerchen.setEnabled(!disable);
        }
        if (comboBoxGruppe != null && (fields.contains(Field.ALLE) || fields.contains(Field.GRUPPE))) {
            comboBoxGruppe.setEnabled(!disable);
        }
        if (txtAreaRollen != null && (fields.contains(Field.ALLE) || fields.contains(Field.ROLLEN))) {
            txtAreaRollen.setEnabled(!disable);
        }
        if (comboBoxElternmithilfeCode != null && (fields.contains(Field.ALLE) || fields.contains(Field.ELTERNMITHILFE_CODE))) {
            comboBoxElternmithilfeCode.setEnabled(!disable);
        }
        if (txtKuchenVorstellung != null && (fields.contains(Field.ALLE) || fields.contains(Field.KUCHEN_VORSTELLUNG))) {
            txtKuchenVorstellung.setEnabled(!disable);
        }
        if (txtZusatzattributMaerchen != null && (fields.contains(Field.ALLE) || fields.contains(Field.ZUSATZATTRIBUT_MAERCHEN))) {
            txtZusatzattributMaerchen.setEnabled(!disable);
        }
        if (checkBoxMaerchenFuerSucheBeruecksichtigen != null && (fields.contains(Field.ALLE) || fields.contains(Field.MAERCHEN_FUER_SUCHE_BERUECKSICHTIGEN))) {
            checkBoxMaerchenFuerSucheBeruecksichtigen.setEnabled(!disable);
        }
    }

    class RadioBtnGroupRolleListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            LOGGER.trace("SchuelerSuchenController Rolle Event");
            schuelerSuchenModel.setRolle(SchuelerSuchenModel.RolleSelected.valueOf(e.getActionCommand()));
        }
    }

    class RadioBtnGroupAnmeldestatusListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            LOGGER.trace("SchuelerSuchenController Anmeldestatus Event");
            schuelerSuchenModel.setAnmeldestatus(SchuelerSuchenModel.AnmeldestatusSelected.valueOf(e.getActionCommand()));
        }
    }

    class RadioBtnGroupDispensationListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            LOGGER.trace("SchuelerSuchenController Dispensation Event");
            schuelerSuchenModel.setDispensation(SchuelerSuchenModel.DispensationSelected.valueOf(e.getActionCommand()));
        }
    }

    class RadioBtnGroupGeschlechtListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            LOGGER.trace("SchuelerSuchenController Geschlecht Event");
            schuelerSuchenModel.setGeschlecht(SchuelerSuchenModel.GeschlechtSelected.valueOf(e.getActionCommand()));
        }
    }

}
