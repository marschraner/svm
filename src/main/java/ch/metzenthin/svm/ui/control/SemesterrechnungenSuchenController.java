package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.common.dataTypes.Wochentag;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.CompletedListener;
import ch.metzenthin.svm.domain.model.SemesterrechnungenSuchenModel;
import ch.metzenthin.svm.domain.model.SemesterrechnungenTableData;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.ui.componentmodel.SemesterrechnungenTableModel;
import ch.metzenthin.svm.ui.components.SemesterrechnungenPanel;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.util.HashSet;
import java.util.Set;

import static ch.metzenthin.svm.common.utils.Converter.asString;
import static ch.metzenthin.svm.common.utils.SimpleValidator.equalsNullSafe;

/**
 * @author Martin Schraner
 */
public class SemesterrechnungenSuchenController extends SemesterrechnungController {

    private static final Logger LOGGER = Logger.getLogger(SemesterrechnungenSuchenController.class);

    // Möglichkeit zum Umschalten des validation modes (nicht dynamisch)
    private static final boolean MODEL_VALIDATION_MODE = false;

    private JSpinner spinnerSemester;
    private JTextField txtNachname;
    private JTextField txtVorname;
    private JTextField txtZeitBeginn;
    private JTextField txtRechnungsbetragVorrechnung;
    private JTextField txtRestbetragVorrechnung;
    private JTextField txtRechnungsbetragNachrechnung;
    private JTextField txtRestbetragNachrechnung;
    private JTextField txtDifferenzSchulgeld;
    private JLabel errLblNachname;
    private JLabel errLblVorname;
    private JLabel errLblZeitBeginn;
    private JLabel errLblRechnungsbetragVorrechnung;
    private JLabel errLblRestbetragVorrechnung;
    private JLabel errLblRechnungsbetragNachrechnung;
    private JLabel errLblRestbetragNachrechnung;
    private JLabel errLblDifferenzSchulgeld;
    private JRadioButton radioBtnRechnungsempfaenger;
    private JRadioButton radioBtnSchueler;
    private JRadioButton radioBtnEltern;
    private JRadioButton radioBtnRolleAlle;
    private JRadioButton radioBtnSemesterrechnungCodeJa;
    private JRadioButton radioBtnSemesterrechnungCodeNein;
    private JRadioButton radioBtnSemesterrechnungCodeAlle;
    private JRadioButton radioBtnStipendiumJa;
    private JRadioButton radioBtnStipendiumNein;
    private JRadioButton radioBtnStipendiumAlle;
    private JRadioButton radioBtnRechnungsdatumGesetztVorrechnung;
    private JRadioButton radioBtnRechnungsdatumNichtGesetztVorrechnung;
    private JRadioButton radioBtnRechnungsdatumGesetztAlleVorrechnung;
    private JRadioButton radioBtnAmVorrechnung;
    private JRadioButton radioBtnVorVorrechnung;
    private JRadioButton radioBtnNachVorrechnung;
    private JRadioButton radioBtnGleichErmaessigungVorrechnung;
    private JRadioButton radioBtnKleinerErmaessigungVorrechnung;
    private JRadioButton radioBtnGroesserErmaessigungVorrechnung;
    private JRadioButton radioBtnGleichZuschlagVorrechnung;
    private JRadioButton radioBtnKleinerZuschlagVorrechnung;
    private JRadioButton radioBtnGroesserZuschlagVorrechnung;
    private JRadioButton radioBtnGleichAnzahlWochenVorrechnung;
    private JRadioButton radioBtnKleinerAnzahlWochenVorrechnung;
    private JRadioButton radioBtnGroesserAnzahlWochenVorrechnung;
    private JRadioButton radioBtnGleichWochenbetragVorrechnung;
    private JRadioButton radioBtnKleinerWochenbetragVorrechnung;
    private JRadioButton radioBtnGroesserWochenbetragVorrechnung;
    private JRadioButton radioBtnGleichRechnungsbetragVorrechnung;
    private JRadioButton radioBtnKleinerRechnungsbetragVorrechnung;
    private JRadioButton radioBtnGroesserRechnungsbetragVorrechnung;
    private JRadioButton radioBtnGleichRestbetragVorrechnung;
    private JRadioButton radioBtnKleinerRestbetragVorrechnung;
    private JRadioButton radioBtnGroesserRestbetragVorrechnung;
    private JRadioButton radioBtnSechsJahresRabattJaVorrechnung;
    private JRadioButton radioBtnSechsJahresRabattNeinVorrechnung;
    private JRadioButton radioBtnSechsJahresRabattAlleVorrechnung;
    private JRadioButton radioBtnRechnungsdatumGesetztNachrechnung;
    private JRadioButton radioBtnRechnungsdatumNichtGesetztNachrechnung;
    private JRadioButton radioBtnRechnungsdatumGesetztAlleNachrechnung;
    private JRadioButton radioBtnAmNachrechnung;
    private JRadioButton radioBtnVorNachrechnung;
    private JRadioButton radioBtnNachNachrechnung;
    private JRadioButton radioBtnGleichErmaessigungNachrechnung;
    private JRadioButton radioBtnKleinerErmaessigungNachrechnung;
    private JRadioButton radioBtnGroesserErmaessigungNachrechnung;
    private JRadioButton radioBtnGleichZuschlagNachrechnung;
    private JRadioButton radioBtnKleinerZuschlagNachrechnung;
    private JRadioButton radioBtnGroesserZuschlagNachrechnung;
    private JRadioButton radioBtnGleichAnzahlWochenNachrechnung;
    private JRadioButton radioBtnKleinerAnzahlWochenNachrechnung;
    private JRadioButton radioBtnGroesserAnzahlWochenNachrechnung;
    private JRadioButton radioBtnGleichWochenbetragNachrechnung;
    private JRadioButton radioBtnKleinerWochenbetragNachrechnung;
    private JRadioButton radioBtnGroesserWochenbetragNachrechnung;
    private JRadioButton radioBtnGleichRechnungsbetragNachrechnung;
    private JRadioButton radioBtnKleinerRechnungsbetragNachrechnung;
    private JRadioButton radioBtnGroesserRechnungsbetragNachrechnung;
    private JRadioButton radioBtnGleichRestbetragNachrechnung;
    private JRadioButton radioBtnKleinerRestbetragNachrechnung;
    private JRadioButton radioBtnGroesserRestbetragNachrechnung;
    private JRadioButton radioBtnSechsJahresRabattJaNachrechnung;
    private JRadioButton radioBtnSechsJahresRabattNeinNachrechnung;
    private JRadioButton radioBtnSechsJahresRabattAlleNachrechnung;
    private JRadioButton radioBtnGleichDifferenzSchulgeld;
    private JRadioButton radioBtnKleinerDifferenzSchulgeld;
    private JRadioButton radioBtnGroesserDifferenzSchulgeld;
    private JComboBox<Wochentag> comboBoxWochentag;
    private JComboBox<Mitarbeiter> comboBoxLehrkraft;
    private JCheckBox checkBoxGeloescht;
    private JButton btnSuchen;
    private JButton btnAbbrechen;
    private ActionListener closeListener;
    private ActionListener nextPanelListener;
    private final SvmContext svmContext;
    private SemesterrechnungenSuchenModel semesterrechnungenSuchenModel;
    private ActionListener zurueckListener;

    public SemesterrechnungenSuchenController(SvmContext svmContext, SemesterrechnungenSuchenModel semesterrechnungenSuchenModel) {
        super(svmContext, semesterrechnungenSuchenModel);
        this.svmContext = svmContext;
        this.semesterrechnungenSuchenModel = semesterrechnungenSuchenModel;
        this.semesterrechnungenSuchenModel.addPropertyChangeListener(this);
        this.semesterrechnungenSuchenModel.addDisableFieldsListener(this);
        this.semesterrechnungenSuchenModel.addMakeErrorLabelsInvisibleListener(this);
        this.semesterrechnungenSuchenModel.addCompletedListener(new CompletedListener() {
            @Override
            public void completed(boolean completed) {
                onSemesterrechnungenSuchenModelCompleted(completed);
            }
        });
        this.setModelValidationMode(MODEL_VALIDATION_MODE);
    }

    public void setSpinnerSemester(JSpinner spinnerSemester) {
        this.spinnerSemester = spinnerSemester;
        java.util.List<Semester> semesterList = svmContext.getSvmModel().getSemestersAll();
        if (semesterList.isEmpty()) {
            // keine Semester erfasst
            SpinnerModel spinnerModel = new SpinnerListModel(new String[]{""});
            spinnerSemester.setModel(spinnerModel);
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
        semesterrechnungenSuchenModel.setSemester(semesterrechnungenSuchenModel.getSemesterInit(svmContext.getSvmModel()));
    }

    private void onSemesterSelected() {
        LOGGER.trace("SemesterrechnungenSuchenController Event Semester selected =" + spinnerSemester.getValue());
        boolean equalFieldAndModelValue = equalsNullSafe(spinnerSemester.getValue(), semesterrechnungenSuchenModel.getSemester());
        setModelSemester();
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelSemester() {
        makeErrorLabelInvisible(Field.SEMESTER);
        semesterrechnungenSuchenModel.setSemester((Semester) spinnerSemester.getValue());
    }

    public void setTxtNachname(JTextField txtNachname) {
        this.txtNachname = txtNachname;
        this.txtNachname.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onNachnameEvent();
            }
        });
        this.txtNachname.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onNachnameEvent();
            }
        });
    }

    private void onNachnameEvent() {
        LOGGER.trace("semesterrechnungenSuchenController Event Nachname");
        boolean equalFieldAndModelValue = equalsNullSafe(txtNachname.getText(), semesterrechnungenSuchenModel.getNachname());
        try {
            setModelNachname();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelNachname() throws SvmValidationException {
        makeErrorLabelInvisible(Field.NACHNAME);
        try {
            semesterrechnungenSuchenModel.setNachname(txtNachname.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("SemesterrechnungenSuchenController setModelNachname Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtVorname(JTextField txtVorname) {
        this.txtVorname = txtVorname;
        this.txtVorname.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onVornameEvent();
            }
        });
        this.txtVorname.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onVornameEvent();
            }
        });
    }

    private void onVornameEvent() {
        LOGGER.trace("SemesterrechnungenSuchenController Event Vorname");
        boolean equalFieldAndModelValue = equalsNullSafe(txtVorname.getText(), semesterrechnungenSuchenModel.getVorname());
        try {
            setModelVorname();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelVorname() throws SvmValidationException {
        makeErrorLabelInvisible(Field.VORNAME);
        try {
            semesterrechnungenSuchenModel.setVorname(txtVorname.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("SemesterrechnungenSuchenController setModelVorname Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setComboBoxWochentag(JComboBox<Wochentag> comboBoxWochentag) {
        this.comboBoxWochentag = comboBoxWochentag;
        comboBoxWochentag.setModel(new DefaultComboBoxModel<>(Wochentag.values()));
        comboBoxWochentag.removeItem(Wochentag.SONNTAG);
        comboBoxWochentag.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onWochentagSelected();
            }
        });
        // Wochentag in Model initialisieren mit erstem ComboBox-Wert
        semesterrechnungenSuchenModel.setWochentag(Wochentag.values()[0]);
    }

    private void onWochentagSelected() {
        LOGGER.trace("KurseSemesterwahlController Event Wochentag selected=" + comboBoxWochentag.getSelectedItem());
        boolean equalFieldAndModelValue = equalsNullSafe(comboBoxWochentag.getSelectedItem(), semesterrechnungenSuchenModel.getWochentag());
        setModelWochentag();
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelWochentag() {
        makeErrorLabelInvisible(Field.WOCHENTAG);
        semesterrechnungenSuchenModel.setWochentag((Wochentag) comboBoxWochentag.getSelectedItem());
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
        LOGGER.trace("SemesterrechnungenSuchenController Event ZeitBeginn");
        boolean equalFieldAndModelValue = equalsNullSafe(txtZeitBeginn.getText(), semesterrechnungenSuchenModel.getZeitBeginn());
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
            semesterrechnungenSuchenModel.setZeitBeginn(txtZeitBeginn.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("SemesterrechnungenSuchenController setModelZeitBeginn Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setComboBoxLehrkraft(JComboBox<Mitarbeiter> comboBoxLehrkraft) {
        this.comboBoxLehrkraft = comboBoxLehrkraft;
        Mitarbeiter[] selectableLehrkraefte = semesterrechnungenSuchenModel.getSelectableLehrkraefte(svmContext.getSvmModel());
        comboBoxLehrkraft.setModel(new DefaultComboBoxModel<>(selectableLehrkraefte));
        // Model initialisieren mit erstem ComboBox-Wert
        semesterrechnungenSuchenModel.setMitarbeiter(selectableLehrkraefte[0]);
        comboBoxLehrkraft.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onLehrkraftSelected();
            }
        });
    }

    private void onLehrkraftSelected() {
        LOGGER.trace("SemesterrechnungenSuchenController Event Lehrkraft selected=" + comboBoxLehrkraft.getSelectedItem());
        boolean equalFieldAndModelValue = equalsNullSafe(comboBoxLehrkraft.getSelectedItem(), semesterrechnungenSuchenModel.getMitarbeiter());
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
        semesterrechnungenSuchenModel.setMitarbeiter((Mitarbeiter) comboBoxLehrkraft.getSelectedItem());
    }


    public void setTxtRechnungsbetragVorrechnung(JTextField txtRechnungsbetragVorrechnung) {
        this.txtRechnungsbetragVorrechnung = txtRechnungsbetragVorrechnung;
        this.txtRechnungsbetragVorrechnung.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onRechnungsbetragVorrechnungEvent();
            }
        });
        this.txtRechnungsbetragVorrechnung.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onRechnungsbetragVorrechnungEvent();
            }
        });
    }

    private void onRechnungsbetragVorrechnungEvent() {
        LOGGER.trace("SemesterrechnungenSuchenController Event RechnungsbetragVorrechnung");
        boolean equalFieldAndModelValue = equalsNullSafe(txtRechnungsbetragVorrechnung.getText(), semesterrechnungenSuchenModel.getRechnungsbetragVorrechnung());
        try {
            setModelRechnungsbetragVorrechnung();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelRechnungsbetragVorrechnung() throws SvmValidationException {
        makeErrorLabelInvisible(Field.RECHNUNGSBETRAG_VORRECHNUNG);
        try {
            semesterrechnungenSuchenModel.setRechnungsbetragVorrechnung(txtRechnungsbetragVorrechnung.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("SemesterrechnungenSuchenController setModelRechnungsbetragVorrechnung Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtRestbetragVorrechnung(JTextField txtRestbetragVorrechnung) {
        this.txtRestbetragVorrechnung = txtRestbetragVorrechnung;
        this.txtRestbetragVorrechnung.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onRestbetragVorrechnungEvent();
            }
        });
        this.txtRestbetragVorrechnung.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onRestbetragVorrechnungEvent();
            }
        });
    }

    private void onRestbetragVorrechnungEvent() {
        LOGGER.trace("SemesterrechnungenSuchenController Event RestbetragVorrechnung");
        boolean equalFieldAndModelValue = equalsNullSafe(txtRestbetragVorrechnung.getText(), semesterrechnungenSuchenModel.getRestbetragVorrechnung());
        try {
            setModelRestbetragVorrechnung();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelRestbetragVorrechnung() throws SvmValidationException {
        makeErrorLabelInvisible(Field.RESTBETRAG_VORRECHNUNG);
        try {
            semesterrechnungenSuchenModel.setRestbetragVorrechnung(txtRestbetragVorrechnung.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("SemesterrechnungenSuchenController setModelRestbetragVorrechnung Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtRechnungsbetragNachrechnung(JTextField txtRechnungsbetragNachrechnung) {
        this.txtRechnungsbetragNachrechnung = txtRechnungsbetragNachrechnung;
        this.txtRechnungsbetragNachrechnung.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onRechnungsbetragNachrechnungEvent();
            }
        });
        this.txtRechnungsbetragNachrechnung.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onRechnungsbetragNachrechnungEvent();
            }
        });
    }

    private void onRechnungsbetragNachrechnungEvent() {
        LOGGER.trace("SemesterrechnungenSuchenController Event RechnungsbetragNachrechnung");
        boolean equalFieldAndModelValue = equalsNullSafe(txtRechnungsbetragNachrechnung.getText(), semesterrechnungenSuchenModel.getRechnungsbetragNachrechnung());
        try {
            setModelRechnungsbetragNachrechnung();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelRechnungsbetragNachrechnung() throws SvmValidationException {
        makeErrorLabelInvisible(Field.RECHNUNGSBETRAG_NACHRECHNUNG);
        try {
            semesterrechnungenSuchenModel.setRechnungsbetragNachrechnung(txtRechnungsbetragNachrechnung.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("SemesterrechnungenSuchenController setModelRechnungsbetragNachrechnung Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtRestbetragNachrechnung(JTextField txtRestbetragNachrechnung) {
        this.txtRestbetragNachrechnung = txtRestbetragNachrechnung;
        this.txtRestbetragNachrechnung.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onRestbetragNachrechnungEvent();
            }
        });
        this.txtRestbetragNachrechnung.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onRestbetragNachrechnungEvent();
            }
        });
    }

    private void onRestbetragNachrechnungEvent() {
        LOGGER.trace("SemesterrechnungenSuchenController Event RestbetragNachrechnung");
        boolean equalFieldAndModelValue = equalsNullSafe(txtRestbetragNachrechnung.getText(), semesterrechnungenSuchenModel.getRestbetragNachrechnung());
        try {
            setModelRestbetragNachrechnung();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelRestbetragNachrechnung() throws SvmValidationException {
        makeErrorLabelInvisible(Field.RESTBETRAG_NACHRECHNUNG);
        try {
            semesterrechnungenSuchenModel.setRestbetragNachrechnung(txtRestbetragNachrechnung.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("SemesterrechnungenSuchenController setModelRestbetragNachrechnung Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtDifferenzSchulgeld(JTextField txtDifferenzSchulgeld) {
        this.txtDifferenzSchulgeld = txtDifferenzSchulgeld;
        this.txtDifferenzSchulgeld.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onDifferenzSchulgeldEvent();
            }
        });
        this.txtDifferenzSchulgeld.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onDifferenzSchulgeldEvent();
            }
        });
    }

    private void onDifferenzSchulgeldEvent() {
        LOGGER.trace("SemesterrechnungenSuchenController Event DifferenzSchulgeld");
        boolean equalFieldAndModelValue = equalsNullSafe(txtDifferenzSchulgeld.getText(), semesterrechnungenSuchenModel.getDifferenzSchulgeld());
        try {
            setModelDifferenzSchulgeld();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelDifferenzSchulgeld() throws SvmValidationException {
        makeErrorLabelInvisible(Field.DIFFERENZ_SCHULGELD);
        try {
            semesterrechnungenSuchenModel.setDifferenzSchulgeld(txtDifferenzSchulgeld.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("SemesterrechnungenSuchenController setModelDifferenzSchulgeld Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setCheckBoxGeloescht(JCheckBox checkBoxGeloescht) {
        this.checkBoxGeloescht = checkBoxGeloescht;
        if (svmContext.getSvmModel().getSemestersAll().isEmpty()) {
            checkBoxGeloescht.setEnabled(false);
        }
        this.checkBoxGeloescht.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                onGeloeschtEvent();
            }
        });
        // Initialisierung
        semesterrechnungenSuchenModel.setGeloescht(false);
    }

    private void onGeloeschtEvent() {
        LOGGER.trace("SemesterrechnungController Event Geloescht. Selected=" + checkBoxGeloescht.isSelected());
        setModelGeloescht();
    }

    private void setModelGeloescht() {
        semesterrechnungenSuchenModel.setGeloescht(checkBoxGeloescht.isSelected());
    }

    public void setErrLblNachname(JLabel errLblNachname) {
        this.errLblNachname = errLblNachname;
    }

    public void setErrLblVorname(JLabel errLblVorname) {
        this.errLblVorname = errLblVorname;
    }

    public void setErrLblZeitBeginn(JLabel errLblZeitBeginn) {
        this.errLblZeitBeginn = errLblZeitBeginn;
    }

    public void setErrLblRechnungsbetragVorrechnung(JLabel errLblRechnungsbetragVorrechnung) {
        this.errLblRechnungsbetragVorrechnung = errLblRechnungsbetragVorrechnung;
    }

    public void setErrLblRestbetragVorrechnung(JLabel errLblRestbetragVorrechnung) {
        this.errLblRestbetragVorrechnung = errLblRestbetragVorrechnung;
    }

    public void setErrLblRechnungsbetragNachrechnung(JLabel errLblRechnungsbetragNachrechnung) {
        this.errLblRechnungsbetragNachrechnung = errLblRechnungsbetragNachrechnung;
    }

    public void setErrLblRestbetragNachrechnung(JLabel errLblRestbetragNachrechnung) {
        this.errLblRestbetragNachrechnung = errLblRestbetragNachrechnung;
    }

    public void setErrLblDifferenzSchulgeld(JLabel errLblDifferenzSchulgeld) {
        this.errLblDifferenzSchulgeld = errLblDifferenzSchulgeld;
    }

    public void setRadioBtnGroupRolle(JRadioButton radioBtnSchueler, JRadioButton radioBtnEltern, JRadioButton radioBtnRechnungsempfaenger, JRadioButton radioBtnRolleAlle) {
        this.radioBtnSchueler = radioBtnSchueler;
        this.radioBtnEltern = radioBtnEltern;
        this.radioBtnRechnungsempfaenger = radioBtnRechnungsempfaenger;
        this.radioBtnRolleAlle = radioBtnRolleAlle;
        // Action Commands
        this.radioBtnSchueler.setActionCommand(SemesterrechnungenSuchenModel.RolleSelected.SCHUELER.toString());
        this.radioBtnEltern.setActionCommand(SemesterrechnungenSuchenModel.RolleSelected.ELTERN.toString());
        this.radioBtnRechnungsempfaenger.setActionCommand(SemesterrechnungenSuchenModel.RolleSelected.RECHNUNGSEMPFAENGER.toString());
        this.radioBtnRolleAlle.setActionCommand(SemesterrechnungenSuchenModel.RolleSelected.ALLE.toString());
        // Listener
        RadioBtnGroupRolleListener radioBtnGroupRolleListener = new RadioBtnGroupRolleListener();
        this.radioBtnSchueler.addActionListener(radioBtnGroupRolleListener);
        this.radioBtnEltern.addActionListener(radioBtnGroupRolleListener);
        this.radioBtnRechnungsempfaenger.addActionListener(radioBtnGroupRolleListener);
        this.radioBtnRolleAlle.addActionListener(radioBtnGroupRolleListener);
        // Initialisieren mit Rechnungsempfänger
        semesterrechnungenSuchenModel.setRolle(SemesterrechnungenSuchenModel.RolleSelected.RECHNUNGSEMPFAENGER);
    }

    public void setRadioBtnGroupSemesterrechnungCodeJaNein(JRadioButton radioBtnSemesterrechnungCodeJa, JRadioButton radioBtnSemesterrechnungCodeNein, JRadioButton radioBtnSemesterrechnungCodeAlle) {
        this.radioBtnSemesterrechnungCodeJa = radioBtnSemesterrechnungCodeJa;
        this.radioBtnSemesterrechnungCodeNein = radioBtnSemesterrechnungCodeNein;
        this.radioBtnSemesterrechnungCodeAlle = radioBtnSemesterrechnungCodeAlle;
        // Action Commands
        this.radioBtnSemesterrechnungCodeJa.setActionCommand(SemesterrechnungenSuchenModel.SemesterrechnungCodeJaNeinSelected.JA.toString());
        this.radioBtnSemesterrechnungCodeNein.setActionCommand(SemesterrechnungenSuchenModel.SemesterrechnungCodeJaNeinSelected.NEIN.toString());
        this.radioBtnSemesterrechnungCodeAlle.setActionCommand(SemesterrechnungenSuchenModel.SemesterrechnungCodeJaNeinSelected.ALLE.toString());
        // Listener
        RadioBtnGroupSemesterrechnungCodeJaNeinListener radioBtnGroupSemesterrechnungCodeJaNeinListener = new RadioBtnGroupSemesterrechnungCodeJaNeinListener();
        this.radioBtnSemesterrechnungCodeJa.addActionListener(radioBtnGroupSemesterrechnungCodeJaNeinListener);
        this.radioBtnSemesterrechnungCodeNein.addActionListener(radioBtnGroupSemesterrechnungCodeJaNeinListener);
        this.radioBtnSemesterrechnungCodeAlle.addActionListener(radioBtnGroupSemesterrechnungCodeJaNeinListener);
        // Initialisieren mit Rechnungsempfänger
        semesterrechnungenSuchenModel.setSemesterrechnungCodeJaNeinSelected(SemesterrechnungenSuchenModel.SemesterrechnungCodeJaNeinSelected.ALLE);
    }

    public void setRadioBtnGroupStipendiumJaNein(JRadioButton radioBtnStipendiumJa, JRadioButton radioBtnStipendiumNein, JRadioButton radioBtnStipendiumAlle) {
        this.radioBtnStipendiumJa = radioBtnStipendiumJa;
        this.radioBtnStipendiumNein = radioBtnStipendiumNein;
        this.radioBtnStipendiumAlle = radioBtnStipendiumAlle;
        // Action Commands
        this.radioBtnStipendiumJa.setActionCommand(SemesterrechnungenSuchenModel.StipendiumJaNeinSelected.JA.toString());
        this.radioBtnStipendiumNein.setActionCommand(SemesterrechnungenSuchenModel.StipendiumJaNeinSelected.NEIN.toString());
        this.radioBtnStipendiumAlle.setActionCommand(SemesterrechnungenSuchenModel.StipendiumJaNeinSelected.ALLE.toString());
        // Listener
        RadioBtnGroupStipendiumJaNeinListener radioBtnGroupStipendiumJaNeinListener = new RadioBtnGroupStipendiumJaNeinListener();
        this.radioBtnStipendiumJa.addActionListener(radioBtnGroupStipendiumJaNeinListener);
        this.radioBtnStipendiumNein.addActionListener(radioBtnGroupStipendiumJaNeinListener);
        this.radioBtnStipendiumAlle.addActionListener(radioBtnGroupStipendiumJaNeinListener);
        // Initialisieren mit Rechnungsempfänger
        semesterrechnungenSuchenModel.setStipendiumJaNeinSelected(SemesterrechnungenSuchenModel.StipendiumJaNeinSelected.ALLE);
    }

    public void setRadioBtnGroupRechnungsdatumGesetztVorrechnung(JRadioButton radioBtnRechnungsdatumGesetztVorrechnung, JRadioButton radioBtnRechnungsdatumNichtGesetztVorrechnung, JRadioButton radioBtnRechnungsdatumGesetztAlleVorrechnung) {
        this.radioBtnRechnungsdatumGesetztVorrechnung = radioBtnRechnungsdatumGesetztVorrechnung;
        this.radioBtnRechnungsdatumNichtGesetztVorrechnung = radioBtnRechnungsdatumNichtGesetztVorrechnung;
        this.radioBtnRechnungsdatumGesetztAlleVorrechnung = radioBtnRechnungsdatumGesetztAlleVorrechnung;
        // Action Commands
        this.radioBtnRechnungsdatumGesetztVorrechnung.setActionCommand(SemesterrechnungenSuchenModel.RechnungsdatumGesetztVorrechnungSelected.GESETZT.toString());
        this.radioBtnRechnungsdatumNichtGesetztVorrechnung.setActionCommand(SemesterrechnungenSuchenModel.RechnungsdatumGesetztVorrechnungSelected.NICHT_GESETZT.toString());
        this.radioBtnRechnungsdatumGesetztAlleVorrechnung.setActionCommand(SemesterrechnungenSuchenModel.RechnungsdatumGesetztVorrechnungSelected.ALLE.toString());
        // Listener
        RadioBtnGroupRechnungsdatumGesetztVorrechnungListener radioBtnGroupRechnungsdatumGesetztVorrechnungListener = new RadioBtnGroupRechnungsdatumGesetztVorrechnungListener();
        this.radioBtnRechnungsdatumGesetztVorrechnung.addActionListener(radioBtnGroupRechnungsdatumGesetztVorrechnungListener);
        this.radioBtnRechnungsdatumNichtGesetztVorrechnung.addActionListener(radioBtnGroupRechnungsdatumGesetztVorrechnungListener);
        this.radioBtnRechnungsdatumGesetztAlleVorrechnung.addActionListener(radioBtnGroupRechnungsdatumGesetztVorrechnungListener);
        // Initialisieren mit alle
        semesterrechnungenSuchenModel.setRechnungsdatumGesetztVorrechnungSelected(SemesterrechnungenSuchenModel.RechnungsdatumGesetztVorrechnungSelected.ALLE);
    }

    public void setRadioBtnGroupPraezisierungRechnungsdatumVorrechnung(JRadioButton radioBtnAmVorrechnung, JRadioButton radioBtnVorVorrechnung, JRadioButton radioBtnNachVorrechnung) {
        this.radioBtnAmVorrechnung = radioBtnAmVorrechnung;
        this.radioBtnVorVorrechnung = radioBtnVorVorrechnung;
        this.radioBtnNachVorrechnung = radioBtnNachVorrechnung;
        // Action Commands
        this.radioBtnAmVorrechnung.setActionCommand(SemesterrechnungenSuchenModel.PraezisierungRechnungsdatumVorrechnungSelected.AM.toString());
        this.radioBtnVorVorrechnung.setActionCommand(SemesterrechnungenSuchenModel.PraezisierungRechnungsdatumVorrechnungSelected.VOR.toString());
        this.radioBtnNachVorrechnung.setActionCommand(SemesterrechnungenSuchenModel.PraezisierungRechnungsdatumVorrechnungSelected.NACH.toString());
        // Listener
        RadioBtnGroupPraezisierungRechnungsdatumVorrechnungListener radioBtnGroupPraezisierungRechnungsdatumVorrechnungListener = new RadioBtnGroupPraezisierungRechnungsdatumVorrechnungListener();
        this.radioBtnAmVorrechnung.addActionListener(radioBtnGroupPraezisierungRechnungsdatumVorrechnungListener);
        this.radioBtnVorVorrechnung.addActionListener(radioBtnGroupPraezisierungRechnungsdatumVorrechnungListener);
        this.radioBtnNachVorrechnung.addActionListener(radioBtnGroupPraezisierungRechnungsdatumVorrechnungListener);
        // Initialisieren mit am
        semesterrechnungenSuchenModel.setPraezisierungRechnungsdatumVorrechnungSelected(SemesterrechnungenSuchenModel.PraezisierungRechnungsdatumVorrechnungSelected.AM);
    }

    public void setRadioBtnGroupPraezisierungErmaessigungVorrechnung(JRadioButton radioBtnGleichErmaessigungVorrechnung, JRadioButton radioBtnKleinerErmaessigungVorrechnung, JRadioButton radioBtnGroesserErmaessigungVorrechnung) {
        this.radioBtnGleichErmaessigungVorrechnung = radioBtnGleichErmaessigungVorrechnung;
        this.radioBtnKleinerErmaessigungVorrechnung = radioBtnKleinerErmaessigungVorrechnung;
        this.radioBtnGroesserErmaessigungVorrechnung = radioBtnGroesserErmaessigungVorrechnung;
        // Action Commands
        this.radioBtnGleichErmaessigungVorrechnung.setActionCommand(SemesterrechnungenSuchenModel.PraezisierungErmaessigungVorrechnungSelected.GLEICH.toString());
        this.radioBtnKleinerErmaessigungVorrechnung.setActionCommand(SemesterrechnungenSuchenModel.PraezisierungErmaessigungVorrechnungSelected.KLEINER.toString());
        this.radioBtnGroesserErmaessigungVorrechnung.setActionCommand(SemesterrechnungenSuchenModel.PraezisierungErmaessigungVorrechnungSelected.GROESSER.toString());
        // Listener
        RadioBtnGroupPraezisierungErmaessigungVorrechnungListener radioBtnGroupPraezisierungErmaessigungVorrechnungListener = new RadioBtnGroupPraezisierungErmaessigungVorrechnungListener();
        this.radioBtnGleichErmaessigungVorrechnung.addActionListener(radioBtnGroupPraezisierungErmaessigungVorrechnungListener);
        this.radioBtnKleinerErmaessigungVorrechnung.addActionListener(radioBtnGroupPraezisierungErmaessigungVorrechnungListener);
        this.radioBtnGroesserErmaessigungVorrechnung.addActionListener(radioBtnGroupPraezisierungErmaessigungVorrechnungListener);
        // Initialisieren mit gleich
        semesterrechnungenSuchenModel.setPraezisierungErmaessigungVorrechnungSelected(SemesterrechnungenSuchenModel.PraezisierungErmaessigungVorrechnungSelected.GLEICH);
    }

    public void setRadioBtnGroupPraezisierungZuschlagVorrechnung(JRadioButton radioBtnGleichZuschlagVorrechnung, JRadioButton radioBtnKleinerZuschlagVorrechnung, JRadioButton radioBtnGroesserZuschlagVorrechnung) {
        this.radioBtnGleichZuschlagVorrechnung = radioBtnGleichZuschlagVorrechnung;
        this.radioBtnKleinerZuschlagVorrechnung = radioBtnKleinerZuschlagVorrechnung;
        this.radioBtnGroesserZuschlagVorrechnung = radioBtnGroesserZuschlagVorrechnung;
        // Action Commands
        this.radioBtnGleichZuschlagVorrechnung.setActionCommand(SemesterrechnungenSuchenModel.PraezisierungZuschlagVorrechnungSelected.GLEICH.toString());
        this.radioBtnKleinerZuschlagVorrechnung.setActionCommand(SemesterrechnungenSuchenModel.PraezisierungZuschlagVorrechnungSelected.KLEINER.toString());
        this.radioBtnGroesserZuschlagVorrechnung.setActionCommand(SemesterrechnungenSuchenModel.PraezisierungZuschlagVorrechnungSelected.GROESSER.toString());
        // Listener
        RadioBtnGroupPraezisierungZuschlagVorrechnungListener radioBtnGroupPraezisierungZuschlagVorrechnungListener = new RadioBtnGroupPraezisierungZuschlagVorrechnungListener();
        this.radioBtnGleichZuschlagVorrechnung.addActionListener(radioBtnGroupPraezisierungZuschlagVorrechnungListener);
        this.radioBtnKleinerZuschlagVorrechnung.addActionListener(radioBtnGroupPraezisierungZuschlagVorrechnungListener);
        this.radioBtnGroesserZuschlagVorrechnung.addActionListener(radioBtnGroupPraezisierungZuschlagVorrechnungListener);
        // Initialisieren mit gleich
        semesterrechnungenSuchenModel.setPraezisierungZuschlagVorrechnungSelected(SemesterrechnungenSuchenModel.PraezisierungZuschlagVorrechnungSelected.GLEICH);
    }

    public void setRadioBtnGroupPraezisierungAnzahlWochenVorrechnung(JRadioButton radioBtnGleichAnzahlWochenVorrechnung, JRadioButton radioBtnKleinerAnzahlWochenVorrechnung, JRadioButton radioBtnGroesserAnzahlWochenVorrechnung) {
        this.radioBtnGleichAnzahlWochenVorrechnung = radioBtnGleichAnzahlWochenVorrechnung;
        this.radioBtnKleinerAnzahlWochenVorrechnung = radioBtnKleinerAnzahlWochenVorrechnung;
        this.radioBtnGroesserAnzahlWochenVorrechnung = radioBtnGroesserAnzahlWochenVorrechnung;
        // Action Commands
        this.radioBtnGleichAnzahlWochenVorrechnung.setActionCommand(SemesterrechnungenSuchenModel.PraezisierungAnzahlWochenVorrechnungSelected.GLEICH.toString());
        this.radioBtnKleinerAnzahlWochenVorrechnung.setActionCommand(SemesterrechnungenSuchenModel.PraezisierungAnzahlWochenVorrechnungSelected.KLEINER.toString());
        this.radioBtnGroesserAnzahlWochenVorrechnung.setActionCommand(SemesterrechnungenSuchenModel.PraezisierungAnzahlWochenVorrechnungSelected.GROESSER.toString());
        // Listener
        RadioBtnGroupPraezisierungAnzahlWochenVorrechnungListener radioBtnGroupPraezisierungAnzahlWochenVorrechnungListener = new RadioBtnGroupPraezisierungAnzahlWochenVorrechnungListener();
        this.radioBtnGleichAnzahlWochenVorrechnung.addActionListener(radioBtnGroupPraezisierungAnzahlWochenVorrechnungListener);
        this.radioBtnKleinerAnzahlWochenVorrechnung.addActionListener(radioBtnGroupPraezisierungAnzahlWochenVorrechnungListener);
        this.radioBtnGroesserAnzahlWochenVorrechnung.addActionListener(radioBtnGroupPraezisierungAnzahlWochenVorrechnungListener);
        // Initialisieren mit gleich
        semesterrechnungenSuchenModel.setPraezisierungAnzahlWochenVorrechnungSelected(SemesterrechnungenSuchenModel.PraezisierungAnzahlWochenVorrechnungSelected.GLEICH);
    }

    public void setRadioBtnGroupPraezisierungWochenbetragVorrechnung(JRadioButton radioBtnGleichWochenbetragVorrechnung, JRadioButton radioBtnKleinerWochenbetragVorrechnung, JRadioButton radioBtnGroesserWochenbetragVorrechnung) {
        this.radioBtnGleichWochenbetragVorrechnung = radioBtnGleichWochenbetragVorrechnung;
        this.radioBtnKleinerWochenbetragVorrechnung = radioBtnKleinerWochenbetragVorrechnung;
        this.radioBtnGroesserWochenbetragVorrechnung = radioBtnGroesserWochenbetragVorrechnung;
        // Action Commands
        this.radioBtnGleichWochenbetragVorrechnung.setActionCommand(SemesterrechnungenSuchenModel.PraezisierungWochenbetragVorrechnungSelected.GLEICH.toString());
        this.radioBtnKleinerWochenbetragVorrechnung.setActionCommand(SemesterrechnungenSuchenModel.PraezisierungWochenbetragVorrechnungSelected.KLEINER.toString());
        this.radioBtnGroesserWochenbetragVorrechnung.setActionCommand(SemesterrechnungenSuchenModel.PraezisierungWochenbetragVorrechnungSelected.GROESSER.toString());
        // Listener
        RadioBtnGroupPraezisierungWochenbetragVorrechnungListener radioBtnGroupPraezisierungWochenbetragVorrechnungListener = new RadioBtnGroupPraezisierungWochenbetragVorrechnungListener();
        this.radioBtnGleichWochenbetragVorrechnung.addActionListener(radioBtnGroupPraezisierungWochenbetragVorrechnungListener);
        this.radioBtnKleinerWochenbetragVorrechnung.addActionListener(radioBtnGroupPraezisierungWochenbetragVorrechnungListener);
        this.radioBtnGroesserWochenbetragVorrechnung.addActionListener(radioBtnGroupPraezisierungWochenbetragVorrechnungListener);
        // Initialisieren mit gleich
        semesterrechnungenSuchenModel.setPraezisierungWochenbetragVorrechnungSelected(SemesterrechnungenSuchenModel.PraezisierungWochenbetragVorrechnungSelected.GLEICH);
    }

    public void setRadioBtnGroupPraezisierungRechnungsbetragVorrechnung(JRadioButton radioBtnGleichRechnungsbetragVorrechnung, JRadioButton radioBtnKleinerRechnungsbetragVorrechnung, JRadioButton radioBtnGroesserRechnungsbetragVorrechnung) {
        this.radioBtnGleichRechnungsbetragVorrechnung = radioBtnGleichRechnungsbetragVorrechnung;
        this.radioBtnKleinerRechnungsbetragVorrechnung = radioBtnKleinerRechnungsbetragVorrechnung;
        this.radioBtnGroesserRechnungsbetragVorrechnung = radioBtnGroesserRechnungsbetragVorrechnung;
        // Action Commands
        this.radioBtnGleichRechnungsbetragVorrechnung.setActionCommand(SemesterrechnungenSuchenModel.PraezisierungRechnungsbetragVorrechnungSelected.GLEICH.toString());
        this.radioBtnKleinerRechnungsbetragVorrechnung.setActionCommand(SemesterrechnungenSuchenModel.PraezisierungRechnungsbetragVorrechnungSelected.KLEINER.toString());
        this.radioBtnGroesserRechnungsbetragVorrechnung.setActionCommand(SemesterrechnungenSuchenModel.PraezisierungRechnungsbetragVorrechnungSelected.GROESSER.toString());
        // Listener
        RadioBtnGroupPraezisierungRechnungsbetragVorrechnungListener radioBtnGroupPraezisierungRechnungsbetragVorrechnungListener = new RadioBtnGroupPraezisierungRechnungsbetragVorrechnungListener();
        this.radioBtnGleichRechnungsbetragVorrechnung.addActionListener(radioBtnGroupPraezisierungRechnungsbetragVorrechnungListener);
        this.radioBtnKleinerRechnungsbetragVorrechnung.addActionListener(radioBtnGroupPraezisierungRechnungsbetragVorrechnungListener);
        this.radioBtnGroesserRechnungsbetragVorrechnung.addActionListener(radioBtnGroupPraezisierungRechnungsbetragVorrechnungListener);
        // Initialisieren mit gleich
        semesterrechnungenSuchenModel.setPraezisierungRechnungsbetragVorrechnungSelected(SemesterrechnungenSuchenModel.PraezisierungRechnungsbetragVorrechnungSelected.GLEICH);
    }

    public void setRadioBtnGroupPraezisierungRestbetragVorrechnung(JRadioButton radioBtnGleichRestbetragVorrechnung, JRadioButton radioBtnKleinerRestbetragVorrechnung, JRadioButton radioBtnGroesserRestbetragVorrechnung) {
        this.radioBtnGleichRestbetragVorrechnung = radioBtnGleichRestbetragVorrechnung;
        this.radioBtnKleinerRestbetragVorrechnung = radioBtnKleinerRestbetragVorrechnung;
        this.radioBtnGroesserRestbetragVorrechnung = radioBtnGroesserRestbetragVorrechnung;
        // Action Commands
        this.radioBtnGleichRestbetragVorrechnung.setActionCommand(SemesterrechnungenSuchenModel.PraezisierungRestbetragVorrechnungSelected.GLEICH.toString());
        this.radioBtnKleinerRestbetragVorrechnung.setActionCommand(SemesterrechnungenSuchenModel.PraezisierungRestbetragVorrechnungSelected.KLEINER.toString());
        this.radioBtnGroesserRestbetragVorrechnung.setActionCommand(SemesterrechnungenSuchenModel.PraezisierungRestbetragVorrechnungSelected.GROESSER.toString());
        // Listener
        RadioBtnGroupPraezisierungRestbetragVorrechnungListener radioBtnGroupPraezisierungRestbetragVorrechnungListener = new RadioBtnGroupPraezisierungRestbetragVorrechnungListener();
        this.radioBtnGleichRestbetragVorrechnung.addActionListener(radioBtnGroupPraezisierungRestbetragVorrechnungListener);
        this.radioBtnKleinerRestbetragVorrechnung.addActionListener(radioBtnGroupPraezisierungRestbetragVorrechnungListener);
        this.radioBtnGroesserRestbetragVorrechnung.addActionListener(radioBtnGroupPraezisierungRestbetragVorrechnungListener);
        // Initialisieren mit gleich
        semesterrechnungenSuchenModel.setPraezisierungRestbetragVorrechnungSelected(SemesterrechnungenSuchenModel.PraezisierungRestbetragVorrechnungSelected.GLEICH);
    }

    public void setRadioBtnGroupSechsJahresRabattJaNeinVorrechnung(JRadioButton radioBtnSechsJahresRabattJaVorrechnung, JRadioButton radioBtnSechsJahresRabattNeinVorrechnung, JRadioButton radioBtnSechsJahresRabattAlleVorrechnung) {
        this.radioBtnSechsJahresRabattJaVorrechnung = radioBtnSechsJahresRabattJaVorrechnung;
        this.radioBtnSechsJahresRabattNeinVorrechnung = radioBtnSechsJahresRabattNeinVorrechnung;
        this.radioBtnSechsJahresRabattAlleVorrechnung = radioBtnSechsJahresRabattAlleVorrechnung;
        // Action Commands
        this.radioBtnSechsJahresRabattJaVorrechnung.setActionCommand(SemesterrechnungenSuchenModel.SechsJahresRabattJaNeinVorrechnungSelected.JA.toString());
        this.radioBtnSechsJahresRabattNeinVorrechnung.setActionCommand(SemesterrechnungenSuchenModel.SechsJahresRabattJaNeinVorrechnungSelected.NEIN.toString());
        this.radioBtnSechsJahresRabattAlleVorrechnung.setActionCommand(SemesterrechnungenSuchenModel.SechsJahresRabattJaNeinVorrechnungSelected.ALLE.toString());
        // Listener
        RadioBtnGroupSechsJahresRabattJaNeinVorrechnungListener radioBtnGroupSechsJahresRabattJaNeinVorrechnungListener = new RadioBtnGroupSechsJahresRabattJaNeinVorrechnungListener();
        this.radioBtnSechsJahresRabattJaVorrechnung.addActionListener(radioBtnGroupSechsJahresRabattJaNeinVorrechnungListener);
        this.radioBtnSechsJahresRabattNeinVorrechnung.addActionListener(radioBtnGroupSechsJahresRabattJaNeinVorrechnungListener);
        this.radioBtnSechsJahresRabattAlleVorrechnung.addActionListener(radioBtnGroupSechsJahresRabattJaNeinVorrechnungListener);
        // Initialisieren mit alle
        semesterrechnungenSuchenModel.setSechsJahresRabattJaNeinVorrechnungSelected(SemesterrechnungenSuchenModel.SechsJahresRabattJaNeinVorrechnungSelected.ALLE);
    }

    public void setRadioBtnGroupRechungsdatumGesetztNachrechnung(JRadioButton radioBtnRechnungsdatumGesetztNachrechnung, JRadioButton radioBtnRechnungsdatumNichtGesetztNachrechnung, JRadioButton radioBtnRechungsdatumGesetztAlleNachrechnung) {
        this.radioBtnRechnungsdatumGesetztNachrechnung = radioBtnRechnungsdatumGesetztNachrechnung;
        this.radioBtnRechnungsdatumNichtGesetztNachrechnung = radioBtnRechnungsdatumNichtGesetztNachrechnung;
        this.radioBtnRechnungsdatumGesetztAlleNachrechnung = radioBtnRechungsdatumGesetztAlleNachrechnung;
        // Action Commands
        this.radioBtnRechnungsdatumGesetztNachrechnung.setActionCommand(SemesterrechnungenSuchenModel.RechnungsdatumGesetztNachrechnungSelected.GESETZT.toString());
        this.radioBtnRechnungsdatumNichtGesetztNachrechnung.setActionCommand(SemesterrechnungenSuchenModel.RechnungsdatumGesetztNachrechnungSelected.NICHT_GESETZT.toString());
        this.radioBtnRechnungsdatumGesetztAlleNachrechnung.setActionCommand(SemesterrechnungenSuchenModel.RechnungsdatumGesetztNachrechnungSelected.ALLE.toString());
        // Listener
        RadioBtnGroupRechnungsdatumGesetztNachrechnungListener radioBtnGroupRechnungsdatumGesetztNachrechnungListener = new RadioBtnGroupRechnungsdatumGesetztNachrechnungListener();
        this.radioBtnRechnungsdatumGesetztNachrechnung.addActionListener(radioBtnGroupRechnungsdatumGesetztNachrechnungListener);
        this.radioBtnRechnungsdatumNichtGesetztNachrechnung.addActionListener(radioBtnGroupRechnungsdatumGesetztNachrechnungListener);
        this.radioBtnRechnungsdatumGesetztAlleNachrechnung.addActionListener(radioBtnGroupRechnungsdatumGesetztNachrechnungListener);
        // Initialisieren mit alle
        semesterrechnungenSuchenModel.setRechnungsdatumGesetztNachrechnungSelected(SemesterrechnungenSuchenModel.RechnungsdatumGesetztNachrechnungSelected.ALLE);
    }

    public void setRadioBtnGroupPraezisierungRechnungsdatumNachrechnung(JRadioButton radioBtnAmNachrechnung, JRadioButton radioBtnVorNachrechnung, JRadioButton radioBtnNachNachrechnung) {
        this.radioBtnAmNachrechnung = radioBtnAmNachrechnung;
        this.radioBtnVorNachrechnung = radioBtnVorNachrechnung;
        this.radioBtnNachNachrechnung = radioBtnNachNachrechnung;
        // Action Commands
        this.radioBtnAmNachrechnung.setActionCommand(SemesterrechnungenSuchenModel.PraezisierungRechnungsdatumNachrechnungSelected.AM.toString());
        this.radioBtnVorNachrechnung.setActionCommand(SemesterrechnungenSuchenModel.PraezisierungRechnungsdatumNachrechnungSelected.VOR.toString());
        this.radioBtnNachNachrechnung.setActionCommand(SemesterrechnungenSuchenModel.PraezisierungRechnungsdatumNachrechnungSelected.NACH.toString());
        // Listener
        RadioBtnGroupPraezisierungRechnungsdatumNachrechnungListener radioBtnGroupPraezisierungRechnungsdatumNachrechnungListener = new RadioBtnGroupPraezisierungRechnungsdatumNachrechnungListener();
        this.radioBtnAmNachrechnung.addActionListener(radioBtnGroupPraezisierungRechnungsdatumNachrechnungListener);
        this.radioBtnVorNachrechnung.addActionListener(radioBtnGroupPraezisierungRechnungsdatumNachrechnungListener);
        this.radioBtnNachNachrechnung.addActionListener(radioBtnGroupPraezisierungRechnungsdatumNachrechnungListener);
        // Initialisieren mit am
        semesterrechnungenSuchenModel.setPraezisierungRechnungsdatumNachrechnungSelected(SemesterrechnungenSuchenModel.PraezisierungRechnungsdatumNachrechnungSelected.AM);
    }

    public void setRadioBtnGroupPraezisierungErmaessigungNachrechnung(JRadioButton radioBtnGleichErmaessigungNachrechnung, JRadioButton radioBtnKleinerErmaessigungNachrechnung, JRadioButton radioBtnGroesserErmaessigungNachrechnung) {
        this.radioBtnGleichErmaessigungNachrechnung = radioBtnGleichErmaessigungNachrechnung;
        this.radioBtnKleinerErmaessigungNachrechnung = radioBtnKleinerErmaessigungNachrechnung;
        this.radioBtnGroesserErmaessigungNachrechnung = radioBtnGroesserErmaessigungNachrechnung;
        // Action Commands
        this.radioBtnGleichErmaessigungNachrechnung.setActionCommand(SemesterrechnungenSuchenModel.PraezisierungErmaessigungNachrechnungSelected.GLEICH.toString());
        this.radioBtnKleinerErmaessigungNachrechnung.setActionCommand(SemesterrechnungenSuchenModel.PraezisierungErmaessigungNachrechnungSelected.KLEINER.toString());
        this.radioBtnGroesserErmaessigungNachrechnung.setActionCommand(SemesterrechnungenSuchenModel.PraezisierungErmaessigungNachrechnungSelected.GROESSER.toString());
        // Listener
        RadioBtnGroupPraezisierungErmaessigungNachrechnungListener radioBtnGroupPraezisierungErmaessigungNachrechnungListener = new RadioBtnGroupPraezisierungErmaessigungNachrechnungListener();
        this.radioBtnGleichErmaessigungNachrechnung.addActionListener(radioBtnGroupPraezisierungErmaessigungNachrechnungListener);
        this.radioBtnKleinerErmaessigungNachrechnung.addActionListener(radioBtnGroupPraezisierungErmaessigungNachrechnungListener);
        this.radioBtnGroesserErmaessigungNachrechnung.addActionListener(radioBtnGroupPraezisierungErmaessigungNachrechnungListener);
        // Initialisieren mit gleich
        semesterrechnungenSuchenModel.setPraezisierungErmaessigungNachrechnungSelected(SemesterrechnungenSuchenModel.PraezisierungErmaessigungNachrechnungSelected.GLEICH);
    }

    public void setRadioBtnGroupPraezisierungZuschlagNachrechnung(JRadioButton radioBtnGleichZuschlagNachrechnung, JRadioButton radioBtnKleinerZuschlagNachrechnung, JRadioButton radioBtnGroesserZuschlagNachrechnung) {
        this.radioBtnGleichZuschlagNachrechnung = radioBtnGleichZuschlagNachrechnung;
        this.radioBtnKleinerZuschlagNachrechnung = radioBtnKleinerZuschlagNachrechnung;
        this.radioBtnGroesserZuschlagNachrechnung = radioBtnGroesserZuschlagNachrechnung;
        // Action Commands
        this.radioBtnGleichZuschlagNachrechnung.setActionCommand(SemesterrechnungenSuchenModel.PraezisierungZuschlagNachrechnungSelected.GLEICH.toString());
        this.radioBtnKleinerZuschlagNachrechnung.setActionCommand(SemesterrechnungenSuchenModel.PraezisierungZuschlagNachrechnungSelected.KLEINER.toString());
        this.radioBtnGroesserZuschlagNachrechnung.setActionCommand(SemesterrechnungenSuchenModel.PraezisierungZuschlagNachrechnungSelected.GROESSER.toString());
        // Listener
        RadioBtnGroupPraezisierungZuschlagNachrechnungListener radioBtnGroupPraezisierungZuschlagNachrechnungListener = new RadioBtnGroupPraezisierungZuschlagNachrechnungListener();
        this.radioBtnGleichZuschlagNachrechnung.addActionListener(radioBtnGroupPraezisierungZuschlagNachrechnungListener);
        this.radioBtnKleinerZuschlagNachrechnung.addActionListener(radioBtnGroupPraezisierungZuschlagNachrechnungListener);
        this.radioBtnGroesserZuschlagNachrechnung.addActionListener(radioBtnGroupPraezisierungZuschlagNachrechnungListener);
        // Initialisieren mit gleich
        semesterrechnungenSuchenModel.setPraezisierungZuschlagNachrechnungSelected(SemesterrechnungenSuchenModel.PraezisierungZuschlagNachrechnungSelected.GLEICH);
    }

    public void setRadioBtnGroupPraezisierungAnzahlWochenNachrechnung(JRadioButton radioBtnGleichAnzahlWochenNachrechnung, JRadioButton radioBtnKleinerAnzahlWochenNachrechnung, JRadioButton radioBtnGroesserAnzahlWochenNachrechnung) {
        this.radioBtnGleichAnzahlWochenNachrechnung = radioBtnGleichAnzahlWochenNachrechnung;
        this.radioBtnKleinerAnzahlWochenNachrechnung = radioBtnKleinerAnzahlWochenNachrechnung;
        this.radioBtnGroesserAnzahlWochenNachrechnung = radioBtnGroesserAnzahlWochenNachrechnung;
        // Action Commands
        this.radioBtnGleichAnzahlWochenNachrechnung.setActionCommand(SemesterrechnungenSuchenModel.PraezisierungAnzahlWochenNachrechnungSelected.GLEICH.toString());
        this.radioBtnKleinerAnzahlWochenNachrechnung.setActionCommand(SemesterrechnungenSuchenModel.PraezisierungAnzahlWochenNachrechnungSelected.KLEINER.toString());
        this.radioBtnGroesserAnzahlWochenNachrechnung.setActionCommand(SemesterrechnungenSuchenModel.PraezisierungAnzahlWochenNachrechnungSelected.GROESSER.toString());
        // Listener
        RadioBtnGroupPraezisierungAnzahlWochenNachrechnungListener radioBtnGroupPraezisierungAnzahlWochenNachrechnungListener = new RadioBtnGroupPraezisierungAnzahlWochenNachrechnungListener();
        this.radioBtnGleichAnzahlWochenNachrechnung.addActionListener(radioBtnGroupPraezisierungAnzahlWochenNachrechnungListener);
        this.radioBtnKleinerAnzahlWochenNachrechnung.addActionListener(radioBtnGroupPraezisierungAnzahlWochenNachrechnungListener);
        this.radioBtnGroesserAnzahlWochenNachrechnung.addActionListener(radioBtnGroupPraezisierungAnzahlWochenNachrechnungListener);
        // Initialisieren mit gleich
        semesterrechnungenSuchenModel.setPraezisierungAnzahlWochenNachrechnungSelected(SemesterrechnungenSuchenModel.PraezisierungAnzahlWochenNachrechnungSelected.GLEICH);
    }

    public void setRadioBtnGroupPraezisierungWochenbetragNachrechnung(JRadioButton radioBtnGleichWochenbetragNachrechnung, JRadioButton radioBtnKleinerWochenbetragNachrechnung, JRadioButton radioBtnGroesserWochenbetragNachrechnung) {
        this.radioBtnGleichWochenbetragNachrechnung = radioBtnGleichWochenbetragNachrechnung;
        this.radioBtnKleinerWochenbetragNachrechnung = radioBtnKleinerWochenbetragNachrechnung;
        this.radioBtnGroesserWochenbetragNachrechnung = radioBtnGroesserWochenbetragNachrechnung;
        // Action Commands
        this.radioBtnGleichWochenbetragNachrechnung.setActionCommand(SemesterrechnungenSuchenModel.PraezisierungWochenbetragNachrechnungSelected.GLEICH.toString());
        this.radioBtnKleinerWochenbetragNachrechnung.setActionCommand(SemesterrechnungenSuchenModel.PraezisierungWochenbetragNachrechnungSelected.KLEINER.toString());
        this.radioBtnGroesserWochenbetragNachrechnung.setActionCommand(SemesterrechnungenSuchenModel.PraezisierungWochenbetragNachrechnungSelected.GROESSER.toString());
        // Listener
        RadioBtnGroupPraezisierungWochenbetragNachrechnungListener radioBtnGroupPraezisierungWochenbetragNachrechnungListener = new RadioBtnGroupPraezisierungWochenbetragNachrechnungListener();
        this.radioBtnGleichWochenbetragNachrechnung.addActionListener(radioBtnGroupPraezisierungWochenbetragNachrechnungListener);
        this.radioBtnKleinerWochenbetragNachrechnung.addActionListener(radioBtnGroupPraezisierungWochenbetragNachrechnungListener);
        this.radioBtnGroesserWochenbetragNachrechnung.addActionListener(radioBtnGroupPraezisierungWochenbetragNachrechnungListener);
        // Initialisieren mit gleich
        semesterrechnungenSuchenModel.setPraezisierungWochenbetragNachrechnungSelected(SemesterrechnungenSuchenModel.PraezisierungWochenbetragNachrechnungSelected.GLEICH);
    }

    public void setRadioBtnGroupPraezisierungRechnungsbetragNachrechnung(JRadioButton radioBtnGleichRechnungsbetragNachrechnung, JRadioButton radioBtnKleinerRechnungsbetragNachrechnung, JRadioButton radioBtnGroesserRechnungsbetragNachrechnung) {
        this.radioBtnGleichRechnungsbetragNachrechnung = radioBtnGleichRechnungsbetragNachrechnung;
        this.radioBtnKleinerRechnungsbetragNachrechnung = radioBtnKleinerRechnungsbetragNachrechnung;
        this.radioBtnGroesserRechnungsbetragNachrechnung = radioBtnGroesserRechnungsbetragNachrechnung;
        // Action Commands
        this.radioBtnGleichRechnungsbetragNachrechnung.setActionCommand(SemesterrechnungenSuchenModel.PraezisierungRechnungsbetragNachrechnungSelected.GLEICH.toString());
        this.radioBtnKleinerRechnungsbetragNachrechnung.setActionCommand(SemesterrechnungenSuchenModel.PraezisierungRechnungsbetragNachrechnungSelected.KLEINER.toString());
        this.radioBtnGroesserRechnungsbetragNachrechnung.setActionCommand(SemesterrechnungenSuchenModel.PraezisierungRechnungsbetragNachrechnungSelected.GROESSER.toString());
        // Listener
        RadioBtnGroupPraezisierungRechnungsbetragNachrechnungListener radioBtnGroupPraezisierungRechnungsbetragNachrechnungListener = new RadioBtnGroupPraezisierungRechnungsbetragNachrechnungListener();
        this.radioBtnGleichRechnungsbetragNachrechnung.addActionListener(radioBtnGroupPraezisierungRechnungsbetragNachrechnungListener);
        this.radioBtnKleinerRechnungsbetragNachrechnung.addActionListener(radioBtnGroupPraezisierungRechnungsbetragNachrechnungListener);
        this.radioBtnGroesserRechnungsbetragNachrechnung.addActionListener(radioBtnGroupPraezisierungRechnungsbetragNachrechnungListener);
        // Initialisieren mit gleich
        semesterrechnungenSuchenModel.setPraezisierungRechnungsbetragNachrechnungSelected(SemesterrechnungenSuchenModel.PraezisierungRechnungsbetragNachrechnungSelected.GLEICH);
    }

    public void setRadioBtnGroupPraezisierungRestbetragNachrechnung(JRadioButton radioBtnGleichRestbetragNachrechnung, JRadioButton radioBtnKleinerRestbetragNachrechnung, JRadioButton radioBtnGroesserRestbetragNachrechnung) {
        this.radioBtnGleichRestbetragNachrechnung = radioBtnGleichRestbetragNachrechnung;
        this.radioBtnKleinerRestbetragNachrechnung = radioBtnKleinerRestbetragNachrechnung;
        this.radioBtnGroesserRestbetragNachrechnung = radioBtnGroesserRestbetragNachrechnung;
        // Action Commands
        this.radioBtnGleichRestbetragNachrechnung.setActionCommand(SemesterrechnungenSuchenModel.PraezisierungRestbetragNachrechnungSelected.GLEICH.toString());
        this.radioBtnKleinerRestbetragNachrechnung.setActionCommand(SemesterrechnungenSuchenModel.PraezisierungRestbetragNachrechnungSelected.KLEINER.toString());
        this.radioBtnGroesserRestbetragNachrechnung.setActionCommand(SemesterrechnungenSuchenModel.PraezisierungRestbetragNachrechnungSelected.GROESSER.toString());
        // Listener
        RadioBtnGroupPraezisierungRestbetragNachrechnungListener radioBtnGroupPraezisierungRestbetragNachrechnungListener = new RadioBtnGroupPraezisierungRestbetragNachrechnungListener();
        this.radioBtnGleichRestbetragNachrechnung.addActionListener(radioBtnGroupPraezisierungRestbetragNachrechnungListener);
        this.radioBtnKleinerRestbetragNachrechnung.addActionListener(radioBtnGroupPraezisierungRestbetragNachrechnungListener);
        this.radioBtnGroesserRestbetragNachrechnung.addActionListener(radioBtnGroupPraezisierungRestbetragNachrechnungListener);
        // Initialisieren mit gleich
        semesterrechnungenSuchenModel.setPraezisierungRestbetragNachrechnungSelected(SemesterrechnungenSuchenModel.PraezisierungRestbetragNachrechnungSelected.GLEICH);
    }

    public void setRadioBtnGroupSechsJahresRabattJaNeinNachrechnung(JRadioButton radioBtnSechsJahresRabattJaNachrechnung, JRadioButton radioBtnSechsJahresRabattNeinNachrechnung, JRadioButton radioBtnSechsJahresRabattAlleNachrechnung) {
        this.radioBtnSechsJahresRabattJaNachrechnung = radioBtnSechsJahresRabattJaNachrechnung;
        this.radioBtnSechsJahresRabattNeinNachrechnung = radioBtnSechsJahresRabattNeinNachrechnung;
        this.radioBtnSechsJahresRabattAlleNachrechnung = radioBtnSechsJahresRabattAlleNachrechnung;
        // Action Commands
        this.radioBtnSechsJahresRabattJaNachrechnung.setActionCommand(SemesterrechnungenSuchenModel.SechsJahresRabattJaNeinNachrechnungSelected.JA.toString());
        this.radioBtnSechsJahresRabattNeinNachrechnung.setActionCommand(SemesterrechnungenSuchenModel.SechsJahresRabattJaNeinNachrechnungSelected.NEIN.toString());
        this.radioBtnSechsJahresRabattAlleNachrechnung.setActionCommand(SemesterrechnungenSuchenModel.SechsJahresRabattJaNeinNachrechnungSelected.ALLE.toString());
        // Listener
        RadioBtnGroupSechsJahresRabattJaNeinNachrechnungListener radioBtnGroupSechsJahresRabattJaNeinNachrechnungListener = new RadioBtnGroupSechsJahresRabattJaNeinNachrechnungListener();
        this.radioBtnSechsJahresRabattJaNachrechnung.addActionListener(radioBtnGroupSechsJahresRabattJaNeinNachrechnungListener);
        this.radioBtnSechsJahresRabattNeinNachrechnung.addActionListener(radioBtnGroupSechsJahresRabattJaNeinNachrechnungListener);
        this.radioBtnSechsJahresRabattAlleNachrechnung.addActionListener(radioBtnGroupSechsJahresRabattJaNeinNachrechnungListener);
        // Initialisieren mit alle
        semesterrechnungenSuchenModel.setSechsJahresRabattJaNeinNachrechnungSelected(SemesterrechnungenSuchenModel.SechsJahresRabattJaNeinNachrechnungSelected.ALLE);
    }

    public void setRadioBtnGroupPraezisierungDifferenzSchulgeld(JRadioButton radioBtnGleichDifferenzSchulgeld, JRadioButton radioBtnKleinerDifferenzSchulgeld, JRadioButton radioBtnGroesserDifferenzSchulgeld) {
        this.radioBtnGleichDifferenzSchulgeld = radioBtnGleichDifferenzSchulgeld;
        this.radioBtnKleinerDifferenzSchulgeld = radioBtnKleinerDifferenzSchulgeld;
        this.radioBtnGroesserDifferenzSchulgeld = radioBtnGroesserDifferenzSchulgeld;
        // Action Commands
        this.radioBtnGleichDifferenzSchulgeld.setActionCommand(SemesterrechnungenSuchenModel.PraezisierungDifferenzSchulgeldSelected.GLEICH.toString());
        this.radioBtnKleinerDifferenzSchulgeld.setActionCommand(SemesterrechnungenSuchenModel.PraezisierungDifferenzSchulgeldSelected.KLEINER.toString());
        this.radioBtnGroesserDifferenzSchulgeld.setActionCommand(SemesterrechnungenSuchenModel.PraezisierungDifferenzSchulgeldSelected.GROESSER.toString());
        // Listener
        RadioBtnGroupPraezisierungDifferenzSchulgeldListener radioBtnGroupPraezisierungDifferenzSchulgeldListener = new RadioBtnGroupPraezisierungDifferenzSchulgeldListener();
        this.radioBtnGleichDifferenzSchulgeld.addActionListener(radioBtnGroupPraezisierungDifferenzSchulgeldListener);
        this.radioBtnKleinerDifferenzSchulgeld.addActionListener(radioBtnGroupPraezisierungDifferenzSchulgeldListener);
        this.radioBtnGroesserDifferenzSchulgeld.addActionListener(radioBtnGroupPraezisierungDifferenzSchulgeldListener);
        // Initialisieren mit gleich
        semesterrechnungenSuchenModel.setPraezisierungDifferenzSchulgeldSelected(SemesterrechnungenSuchenModel.PraezisierungDifferenzSchulgeldSelected.GLEICH);
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
        LOGGER.trace("SemesterrechnungenSuchenPanel Suchen gedrückt");
        if (!validateOnSpeichern()) {
            btnSuchen.setFocusPainted(false);
            return;
        }
        btnSuchen.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        SemesterrechnungenTableData semesterrechnungenTableData = semesterrechnungenSuchenModel.suchen();
        SemesterrechnungenTableModel semesterrechnungenTableModel = new SemesterrechnungenTableModel(semesterrechnungenTableData);
        // Auch bei einem Suchresultat Liste anzeigen, da nur von dort gelöscht werden kann
        if (semesterrechnungenTableData.size() > 0) {
            SemesterrechnungenPanel semesterrechnungenPanel = new SemesterrechnungenPanel(svmContext, semesterrechnungenTableModel, semesterrechnungenSuchenModel.isGeloescht());
            semesterrechnungenPanel.addNextPanelListener(nextPanelListener);
            semesterrechnungenPanel.addCloseListener(closeListener);
            semesterrechnungenPanel.addZurueckListener(zurueckListener);
            btnSuchen.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            nextPanelListener.actionPerformed(new ActionEvent(new Object[]{semesterrechnungenPanel.$$$getRootComponent$$$(), "Suchresultat"}, ActionEvent.ACTION_PERFORMED, "Suchresultat verfügbar"));
        } else {
            btnSuchen.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            JOptionPane.showMessageDialog(null, "Es wurden keine Semesterrechnungen gefunden, welche auf die Suchabfrage passen.", "Keine Semesterrechnungen gefunden", JOptionPane.INFORMATION_MESSAGE, svmContext.getDialogIcons().getInformationIcon());
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
    }

    private void onAbbrechen() {
        LOGGER.trace("SemesterrechnungenSuchenPanel Abbrechen gedrückt");
        closeListener.actionPerformed(new ActionEvent(btnAbbrechen, ActionEvent.ACTION_PERFORMED, "Close nach Abbrechen"));
    }

    private void onSemesterrechnungenSuchenModelCompleted(boolean completed) {
        LOGGER.trace("SemesterrechnungenSuchenModel completed=" + completed);
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
        if (semesterrechnungenSuchenModel.getSemesterrechnungCodeJaNeinSelected() != null && semesterrechnungenSuchenModel.getSemesterrechnungCodeJaNeinSelected() != SemesterrechnungenSuchenModel.SemesterrechnungCodeJaNeinSelected.JA) {
            disableSemesterrechnungCode();
        } else {
            enableSemesterrechnungCode();
        }
        if (semesterrechnungenSuchenModel.getStipendiumJaNeinSelected() != null && semesterrechnungenSuchenModel.getStipendiumJaNeinSelected() != SemesterrechnungenSuchenModel.StipendiumJaNeinSelected.JA) {
            disableStipendium();
        } else {
            enableStipendium();
        }
        if (semesterrechnungenSuchenModel.getRechnungsdatumGesetztVorrechnungSelected() != null && semesterrechnungenSuchenModel.getRechnungsdatumGesetztVorrechnungSelected() != SemesterrechnungenSuchenModel.RechnungsdatumGesetztVorrechnungSelected.GESETZT) {
            disableRechungsdatumVorrechnung();
        } else {
            enableRechungsdatumVorrechnung();
        }
        if (semesterrechnungenSuchenModel.getRechnungsdatumGesetztNachrechnungSelected() != null && semesterrechnungenSuchenModel.getRechnungsdatumGesetztNachrechnungSelected() != SemesterrechnungenSuchenModel.RechnungsdatumGesetztNachrechnungSelected.GESETZT) {
            disableRechungsdatumNachrechnung();
        } else {
            enableRechungsdatumNachrechnung();
        }
    }

    private void enableSemesterrechnungCode() {
        semesterrechnungenSuchenModel.enableFields(getSemesterrechnungCodeFields());
    }

    private void disableSemesterrechnungCode() {
        semesterrechnungenSuchenModel.disableFields(getSemesterrechnungCodeFields());
    }

    private void enableStipendium() {
        semesterrechnungenSuchenModel.enableFields(getStipendiumFields());
    }

    private void disableStipendium() {
        semesterrechnungenSuchenModel.disableFields(getStipendiumFields());
    }

    private void enableRechungsdatumVorrechnung() {
        semesterrechnungenSuchenModel.enableFields(getRechnungsdatumVorrechnungFields());
    }

    private void disableRechungsdatumVorrechnung() {
        semesterrechnungenSuchenModel.disableFields(getRechnungsdatumVorrechnungFields());
        semesterrechnungenSuchenModel.makeErrorLabelsInvisible(getRechnungsdatumVorrechnungFields());
        // Textfeld in jedem Fall leeren, auch wenn Model-Wert noch null und damit von
        // semesterrechnungenSuchenModel.invalidateGeburtsdatumSuchperiode() kein Property Change-Event ausgelöst wird
        txtRechnungsdatumVorrechnung.setText("");
        semesterrechnungenSuchenModel.invalidateRechnungsdatumVorrechnung();
    }

    private void enableRechungsdatumNachrechnung() {
        semesterrechnungenSuchenModel.enableFields(getRechnungsdatumNachrechnungFields());
    }

    private void disableRechungsdatumNachrechnung() {
        semesterrechnungenSuchenModel.disableFields(getRechnungsdatumNachrechnungFields());
        semesterrechnungenSuchenModel.makeErrorLabelsInvisible(getRechnungsdatumNachrechnungFields());
        // Textfeld in jedem Fall leeren, auch wenn Model-Wert noch null und damit von
        // semesterrechnungenSuchenModel.invalidateGeburtsdatumSuchperiode() kein Property Change-Event ausgelöst wird
        txtRechnungsdatumNachrechnung.setText("");
        semesterrechnungenSuchenModel.invalidateRechnungsdatumNachrechnung();
    }

    private Set<Field> getSemesterrechnungCodeFields() {
        Set<Field> semesterrechungCodeComboBoxFields = new HashSet<>();
        semesterrechungCodeComboBoxFields.add(Field.SEMESTERRECHNUNG_CODE);
        return semesterrechungCodeComboBoxFields;
    }

    private Set<Field> getStipendiumFields() {
        Set<Field> stipendiumComboBoxFields = new HashSet<>();
        stipendiumComboBoxFields.add(Field.STIPENDIUM);
        return stipendiumComboBoxFields;
    }

    private Set<Field> getRechnungsdatumVorrechnungFields() {
        Set<Field> rechnungsdatumVorrechnungFields = new HashSet<>();
        rechnungsdatumVorrechnungFields.add(Field.AM_VORRECHNUNG);
        rechnungsdatumVorrechnungFields.add(Field.VOR_VORRECHNUNG);
        rechnungsdatumVorrechnungFields.add(Field.NACH_VORRECHNUNG);
        rechnungsdatumVorrechnungFields.add(Field.RECHNUNGSDATUM_VORRECHNUNG);
        return rechnungsdatumVorrechnungFields;
    }

    private Set<Field> getRechnungsdatumNachrechnungFields() {
        Set<Field> rechnungsdatumNachrechnungFields = new HashSet<>();
        rechnungsdatumNachrechnungFields.add(Field.AM_NACHRECHNUNG);
        rechnungsdatumNachrechnungFields.add(Field.VOR_NACHRECHNUNG);
        rechnungsdatumNachrechnungFields.add(Field.NACH_NACHRECHNUNG);
        rechnungsdatumNachrechnungFields.add(Field.RECHNUNGSDATUM_NACHRECHNUNG);
        return rechnungsdatumNachrechnungFields;
    }

    @Override
    void doPropertyChange(PropertyChangeEvent evt) {
        enableDisableFields();
        super.doPropertyChange(evt);
        if (checkIsFieldChange(Field.SEMESTER, evt)) {
            spinnerSemester.setValue(semesterrechnungenSuchenModel.getSemester());
        }
        else if (checkIsFieldChange(Field.NACHNAME, evt)) {
            txtNachname.setText(semesterrechnungenSuchenModel.getNachname());
        }
        else if (checkIsFieldChange(Field.VORNAME, evt)) {
            txtVorname.setText(semesterrechnungenSuchenModel.getVorname());
        }
        else if (checkIsFieldChange(Field.WOCHENTAG, evt)) {
            comboBoxWochentag.setSelectedItem(semesterrechnungenSuchenModel.getWochentag());
        }
        else if (checkIsFieldChange(Field.ZEIT_BEGINN, evt)) {
            txtZeitBeginn.setText(asString(semesterrechnungenSuchenModel.getZeitBeginn()));
        }
        else if (checkIsFieldChange(Field.LEHRKRAFT, evt)) {
            comboBoxLehrkraft.setSelectedItem(semesterrechnungenSuchenModel.getMitarbeiter());
        }
        else if (checkIsFieldChange(Field.ROLLE, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.RolleSelected.SCHUELER) {
            radioBtnSchueler.setSelected(true);
        }
        else if (checkIsFieldChange(Field.ROLLE, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.RolleSelected.ELTERN) {
            radioBtnEltern.setSelected(true);
        }
        else if (checkIsFieldChange(Field.ROLLE, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.RolleSelected.RECHNUNGSEMPFAENGER) {
            radioBtnRechnungsempfaenger.setSelected(true);
        }
        else if (checkIsFieldChange(Field.ROLLE, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.RolleSelected.ALLE) {
            radioBtnRolleAlle.setSelected(true);
        }
        else if (checkIsFieldChange(Field.SEMESTERRECHNUNG_CODE_JA_NEIN, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.SemesterrechnungCodeJaNeinSelected.JA) {
            radioBtnSemesterrechnungCodeJa.setSelected(true);
        }
        else if (checkIsFieldChange(Field.SEMESTERRECHNUNG_CODE_JA_NEIN, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.SemesterrechnungCodeJaNeinSelected.NEIN) {
            radioBtnSemesterrechnungCodeNein.setSelected(true);
        }
        else if (checkIsFieldChange(Field.SEMESTERRECHNUNG_CODE_JA_NEIN, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.SemesterrechnungCodeJaNeinSelected.ALLE) {
            radioBtnSemesterrechnungCodeAlle.setSelected(true);
        }
        else if (checkIsFieldChange(Field.STIPENDIUM_JA_NEIN, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.StipendiumJaNeinSelected.JA) {
            radioBtnStipendiumJa.setSelected(true);
        }
        else if (checkIsFieldChange(Field.STIPENDIUM_JA_NEIN, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.StipendiumJaNeinSelected.NEIN) {
            radioBtnStipendiumNein.setSelected(true);
        }
        else if (checkIsFieldChange(Field.STIPENDIUM_JA_NEIN, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.StipendiumJaNeinSelected.ALLE) {
            radioBtnStipendiumAlle.setSelected(true);
        }
        else if (checkIsFieldChange(Field.PRAEZISIERUNG_RECHNUNGSDATUM_VORRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.PraezisierungRechnungsdatumVorrechnungSelected.AM) {
            radioBtnAmVorrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.PRAEZISIERUNG_RECHNUNGSDATUM_VORRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.PraezisierungRechnungsdatumVorrechnungSelected.VOR) {
            radioBtnVorVorrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.PRAEZISIERUNG_RECHNUNGSDATUM_VORRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.PraezisierungRechnungsdatumVorrechnungSelected.NACH) {
            radioBtnNachVorrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.PRAEZISIERUNG_ERMAESSIGUNG_VORRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.PraezisierungErmaessigungVorrechnungSelected.GLEICH) {
            radioBtnGleichErmaessigungVorrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.PRAEZISIERUNG_ERMAESSIGUNG_VORRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.PraezisierungErmaessigungVorrechnungSelected.KLEINER) {
            radioBtnKleinerErmaessigungVorrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.PRAEZISIERUNG_ERMAESSIGUNG_VORRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.PraezisierungErmaessigungVorrechnungSelected.GROESSER) {
            radioBtnGroesserErmaessigungVorrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.PRAEZISIERUNG_ZUSCHLAG_VORRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.PraezisierungZuschlagVorrechnungSelected.GLEICH) {
            radioBtnGleichZuschlagVorrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.PRAEZISIERUNG_ZUSCHLAG_VORRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.PraezisierungZuschlagVorrechnungSelected.KLEINER) {
            radioBtnKleinerZuschlagVorrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.PRAEZISIERUNG_ZUSCHLAG_VORRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.PraezisierungZuschlagVorrechnungSelected.GROESSER) {
            radioBtnGroesserZuschlagVorrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.PRAEZISIERUNG_ANZAHL_WOCHEN_VORRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.PraezisierungAnzahlWochenVorrechnungSelected.GLEICH) {
            radioBtnGleichAnzahlWochenVorrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.PRAEZISIERUNG_ANZAHL_WOCHEN_VORRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.PraezisierungAnzahlWochenVorrechnungSelected.KLEINER) {
            radioBtnKleinerAnzahlWochenVorrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.PRAEZISIERUNG_ANZAHL_WOCHEN_VORRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.PraezisierungAnzahlWochenVorrechnungSelected.GROESSER) {
            radioBtnGroesserAnzahlWochenVorrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.PRAEZISIERUNG_WOCHENBETRAG_VORRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.PraezisierungWochenbetragVorrechnungSelected.GLEICH) {
            radioBtnGleichWochenbetragVorrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.PRAEZISIERUNG_WOCHENBETRAG_VORRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.PraezisierungWochenbetragVorrechnungSelected.KLEINER) {
            radioBtnKleinerWochenbetragVorrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.PRAEZISIERUNG_WOCHENBETRAG_VORRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.PraezisierungWochenbetragVorrechnungSelected.GROESSER) {
            radioBtnGroesserWochenbetragVorrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.PRAEZISIERUNG_RECHNUNGSBETRAG_VORRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.PraezisierungRechnungsbetragVorrechnungSelected.GLEICH) {
            radioBtnGleichRechnungsbetragVorrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.PRAEZISIERUNG_RECHNUNGSBETRAG_VORRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.PraezisierungRechnungsbetragVorrechnungSelected.KLEINER) {
            radioBtnKleinerRechnungsbetragVorrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.PRAEZISIERUNG_RECHNUNGSBETRAG_VORRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.PraezisierungRechnungsbetragVorrechnungSelected.GROESSER) {
            radioBtnGroesserRechnungsbetragVorrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.RECHNUNGSBETRAG_VORRECHNUNG, evt)) {
            txtRechnungsbetragVorrechnung.setText(semesterrechnungenSuchenModel.getRechnungsbetragVorrechnung() == null ? null : semesterrechnungenSuchenModel.getRechnungsbetragVorrechnung().toString());
        }
        else if (checkIsFieldChange(Field.RECHNUNGSDATUM_GESETZT_VORRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.RechnungsdatumGesetztVorrechnungSelected.GESETZT) {
            radioBtnRechnungsdatumGesetztVorrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.RECHNUNGSDATUM_GESETZT_VORRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.RechnungsdatumGesetztVorrechnungSelected.NICHT_GESETZT) {
            radioBtnRechnungsdatumNichtGesetztVorrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.RECHNUNGSDATUM_GESETZT_VORRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.RechnungsdatumGesetztVorrechnungSelected.ALLE) {
            radioBtnRechnungsdatumGesetztAlleVorrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.PRAEZISIERUNG_RESTBETRAG_VORRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.PraezisierungRestbetragVorrechnungSelected.GLEICH) {
            radioBtnGleichRestbetragVorrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.PRAEZISIERUNG_RESTBETRAG_VORRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.PraezisierungRestbetragVorrechnungSelected.KLEINER) {
            radioBtnKleinerRestbetragVorrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.PRAEZISIERUNG_RESTBETRAG_VORRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.PraezisierungRestbetragVorrechnungSelected.GROESSER) {
            radioBtnGroesserRestbetragVorrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.RESTBETRAG_VORRECHNUNG, evt)) {
            txtRestbetragVorrechnung.setText(semesterrechnungenSuchenModel.getRestbetragVorrechnung() == null ? null : semesterrechnungenSuchenModel.getRestbetragVorrechnung().toString());
        }
        else if (checkIsFieldChange(Field.SECHS_JAHRES_RABATT_VORRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.SechsJahresRabattJaNeinVorrechnungSelected.JA) {
            radioBtnSechsJahresRabattJaVorrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.SECHS_JAHRES_RABATT_VORRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.SechsJahresRabattJaNeinVorrechnungSelected.NEIN) {
            radioBtnSechsJahresRabattNeinVorrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.SECHS_JAHRES_RABATT_VORRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.SechsJahresRabattJaNeinVorrechnungSelected.ALLE) {
            radioBtnSechsJahresRabattAlleVorrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.PRAEZISIERUNG_RECHNUNGSDATUM_NACHRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.PraezisierungRechnungsdatumNachrechnungSelected.AM) {
            radioBtnAmNachrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.PRAEZISIERUNG_RECHNUNGSDATUM_NACHRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.PraezisierungRechnungsdatumNachrechnungSelected.VOR) {
            radioBtnVorNachrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.PRAEZISIERUNG_RECHNUNGSDATUM_NACHRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.PraezisierungRechnungsdatumNachrechnungSelected.NACH) {
            radioBtnNachNachrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.PRAEZISIERUNG_ERMAESSIGUNG_NACHRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.PraezisierungErmaessigungNachrechnungSelected.GLEICH) {
            radioBtnGleichErmaessigungNachrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.PRAEZISIERUNG_ERMAESSIGUNG_NACHRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.PraezisierungErmaessigungNachrechnungSelected.KLEINER) {
            radioBtnKleinerErmaessigungNachrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.PRAEZISIERUNG_ERMAESSIGUNG_NACHRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.PraezisierungErmaessigungNachrechnungSelected.GROESSER) {
            radioBtnGroesserErmaessigungNachrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.PRAEZISIERUNG_ZUSCHLAG_NACHRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.PraezisierungZuschlagNachrechnungSelected.GLEICH) {
            radioBtnGleichZuschlagNachrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.PRAEZISIERUNG_ZUSCHLAG_NACHRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.PraezisierungZuschlagNachrechnungSelected.KLEINER) {
            radioBtnKleinerZuschlagNachrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.PRAEZISIERUNG_ZUSCHLAG_NACHRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.PraezisierungZuschlagNachrechnungSelected.GROESSER) {
            radioBtnGroesserZuschlagNachrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.PRAEZISIERUNG_ANZAHL_WOCHEN_NACHRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.PraezisierungAnzahlWochenNachrechnungSelected.GLEICH) {
            radioBtnGleichAnzahlWochenNachrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.PRAEZISIERUNG_ANZAHL_WOCHEN_NACHRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.PraezisierungAnzahlWochenNachrechnungSelected.KLEINER) {
            radioBtnKleinerAnzahlWochenNachrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.PRAEZISIERUNG_ANZAHL_WOCHEN_NACHRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.PraezisierungAnzahlWochenNachrechnungSelected.GROESSER) {
            radioBtnGroesserAnzahlWochenNachrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.PRAEZISIERUNG_WOCHENBETRAG_NACHRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.PraezisierungWochenbetragNachrechnungSelected.GLEICH) {
            radioBtnGleichWochenbetragNachrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.PRAEZISIERUNG_WOCHENBETRAG_NACHRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.PraezisierungWochenbetragNachrechnungSelected.KLEINER) {
            radioBtnKleinerWochenbetragNachrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.PRAEZISIERUNG_WOCHENBETRAG_NACHRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.PraezisierungWochenbetragNachrechnungSelected.GROESSER) {
            radioBtnGroesserWochenbetragNachrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.PRAEZISIERUNG_RECHNUNGSBETRAG_NACHRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.PraezisierungRechnungsbetragNachrechnungSelected.GLEICH) {
            radioBtnGleichRechnungsbetragNachrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.PRAEZISIERUNG_RECHNUNGSBETRAG_NACHRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.PraezisierungRechnungsbetragNachrechnungSelected.KLEINER) {
            radioBtnKleinerRechnungsbetragNachrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.PRAEZISIERUNG_RECHNUNGSBETRAG_NACHRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.PraezisierungRechnungsbetragNachrechnungSelected.GROESSER) {
            radioBtnGroesserRechnungsbetragNachrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.RECHNUNGSBETRAG_NACHRECHNUNG, evt)) {
            txtRechnungsbetragNachrechnung.setText(semesterrechnungenSuchenModel.getRechnungsbetragNachrechnung() == null ? null : semesterrechnungenSuchenModel.getRechnungsbetragNachrechnung().toString());
        }
        else if (checkIsFieldChange(Field.RECHNUNGSDATUM_GESETZT_NACHRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.RechnungsdatumGesetztNachrechnungSelected.GESETZT) {
            radioBtnRechnungsdatumGesetztNachrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.RECHNUNGSDATUM_GESETZT_NACHRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.RechnungsdatumGesetztNachrechnungSelected.NICHT_GESETZT) {
            radioBtnRechnungsdatumNichtGesetztNachrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.RECHNUNGSDATUM_GESETZT_NACHRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.RechnungsdatumGesetztNachrechnungSelected.ALLE) {
            radioBtnRechnungsdatumGesetztAlleNachrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.PRAEZISIERUNG_RESTBETRAG_NACHRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.PraezisierungRestbetragNachrechnungSelected.GLEICH) {
            radioBtnGleichRestbetragNachrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.PRAEZISIERUNG_RESTBETRAG_NACHRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.PraezisierungRestbetragNachrechnungSelected.KLEINER) {
            radioBtnKleinerRestbetragNachrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.PRAEZISIERUNG_RESTBETRAG_NACHRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.PraezisierungRestbetragNachrechnungSelected.GROESSER) {
            radioBtnGroesserRestbetragNachrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.RESTBETRAG_NACHRECHNUNG, evt)) {
            txtRestbetragNachrechnung.setText(semesterrechnungenSuchenModel.getRestbetragNachrechnung() == null ? null : semesterrechnungenSuchenModel.getRestbetragNachrechnung().toString());
        }
        else if (checkIsFieldChange(Field.SECHS_JAHRES_RABATT_NACHRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.SechsJahresRabattJaNeinNachrechnungSelected.JA) {
            radioBtnSechsJahresRabattJaNachrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.SECHS_JAHRES_RABATT_NACHRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.SechsJahresRabattJaNeinNachrechnungSelected.NEIN) {
            radioBtnSechsJahresRabattNeinNachrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.SECHS_JAHRES_RABATT_NACHRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.SechsJahresRabattJaNeinNachrechnungSelected.ALLE) {
            radioBtnSechsJahresRabattAlleNachrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.PRAEZISIERUNG_DIFFERENZ_SCHULGELD, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.PraezisierungDifferenzSchulgeldSelected.GLEICH) {
            radioBtnGleichDifferenzSchulgeld.setSelected(true);
        }
        else if (checkIsFieldChange(Field.PRAEZISIERUNG_DIFFERENZ_SCHULGELD, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.PraezisierungDifferenzSchulgeldSelected.KLEINER) {
            radioBtnKleinerDifferenzSchulgeld.setSelected(true);
        }
        else if (checkIsFieldChange(Field.PRAEZISIERUNG_DIFFERENZ_SCHULGELD, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.PraezisierungDifferenzSchulgeldSelected.GROESSER) {
            radioBtnGroesserDifferenzSchulgeld.setSelected(true);
        }
        else if (checkIsFieldChange(Field.DIFFERENZ_SCHULGELD, evt)) {
            txtDifferenzSchulgeld.setText(semesterrechnungenSuchenModel.getDifferenzSchulgeld() == null ? null : semesterrechnungenSuchenModel.getDifferenzSchulgeld().toString());
        }
        else if (checkIsFieldChange(Field.GELOESCHT, evt)) {
            checkBoxGeloescht.setSelected(semesterrechnungenSuchenModel.isGeloescht());
        }
    }

    @Override
    void validateFields() throws SvmValidationException {
        super.validateFields();
        if (txtNachname.isEnabled()) {
            LOGGER.trace("Validate field Nachname");
            setModelNachname();
        }
        if (txtVorname.isEnabled()) {
            LOGGER.trace("Validate field Vorname");
            setModelVorname();
        }
        if (txtZeitBeginn.isEnabled()) {
            LOGGER.trace("Validate field ZeitBeginn");
            setModelZeitBeginn();
        }
        if (txtRechnungsbetragVorrechnung.isEnabled()) {
            LOGGER.trace("Validate field RechnungsbetragVorrechnung");
            setModelRechnungsbetragVorrechnung();
        }
        if (txtRestbetragVorrechnung.isEnabled()) {
            LOGGER.trace("Validate field RestbetragVorrechnung");
            setModelRestbetragVorrechnung();
        }
        if (txtRechnungsbetragNachrechnung.isEnabled()) {
            LOGGER.trace("Validate field RechnungsbetragNachrechnung");
            setModelRechnungsbetragNachrechnung();
        }
        if (txtRestbetragNachrechnung.isEnabled()) {
            LOGGER.trace("Validate field RestbetragNachrechnung");
            setModelRestbetragNachrechnung();
        }
        if (txtDifferenzSchulgeld.isEnabled()) {
            LOGGER.trace("Validate field DifferenzSchulgeld");
            setModelDifferenzSchulgeld();
        }
    }

    @Override
    void showErrMsg(SvmValidationException e) {
        super.showErrMsg(e);
        if (e.getAffectedFields().contains(Field.NACHNAME)) {
            errLblNachname.setVisible(true);
            errLblNachname.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.VORNAME)) {
            errLblVorname.setVisible(true);
            errLblVorname.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ZEIT_BEGINN)) {
            errLblZeitBeginn.setVisible(true);
            errLblZeitBeginn.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.RECHNUNGSBETRAG_VORRECHNUNG)) {
            errLblRechnungsbetragVorrechnung.setVisible(true);
            errLblRechnungsbetragVorrechnung.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.RESTBETRAG_VORRECHNUNG)) {
            errLblRestbetragVorrechnung.setVisible(true);
            errLblRestbetragVorrechnung.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.RECHNUNGSBETRAG_NACHRECHNUNG)) {
            errLblRechnungsbetragNachrechnung.setVisible(true);
            errLblRechnungsbetragNachrechnung.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.RESTBETRAG_NACHRECHNUNG)) {
            errLblRestbetragNachrechnung.setVisible(true);
            errLblRestbetragNachrechnung.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.DIFFERENZ_SCHULGELD)) {
            errLblDifferenzSchulgeld.setVisible(true);
            errLblDifferenzSchulgeld.setText(e.getMessage());
        }
    }

    @Override
    void showErrMsgAsToolTip(SvmValidationException e) {
        super.showErrMsgAsToolTip(e);
        if (e.getAffectedFields().contains(Field.NACHNAME)) {
            txtNachname.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.VORNAME)) {
            txtVorname.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ZEIT_BEGINN)) {
            txtZeitBeginn.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.RECHNUNGSBETRAG_VORRECHNUNG)) {
            txtRechnungsbetragVorrechnung.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.RESTBETRAG_VORRECHNUNG)) {
            txtRestbetragVorrechnung.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.RECHNUNGSBETRAG_NACHRECHNUNG)) {
            txtRechnungsbetragNachrechnung.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.RESTBETRAG_NACHRECHNUNG)) {
            txtRestbetragNachrechnung.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.DIFFERENZ_SCHULGELD)) {
            txtDifferenzSchulgeld.setToolTipText(e.getMessage());
        }
    }

    @Override
    public void makeErrorLabelsInvisible(Set<Field> fields) {
        super.makeErrorLabelsInvisible(fields);
        if (fields.contains(Field.ALLE) || fields.contains(Field.NACHNAME)) {
            if (errLblNachname != null) {
                errLblNachname.setVisible(false);
            }
            if (txtNachname != null) {
                txtNachname.setToolTipText(null);
            }
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.VORNAME)) {
            if (errLblVorname != null) {
                errLblVorname.setVisible(false);
            }
            if (txtVorname != null) {
                txtVorname.setToolTipText(null);
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
        if (fields.contains(Field.ALLE) || fields.contains(Field.RECHNUNGSBETRAG_VORRECHNUNG)) {
            if (errLblRechnungsbetragVorrechnung != null) {
                errLblRechnungsbetragVorrechnung.setVisible(false);
            }
            if (txtRechnungsbetragVorrechnung != null) {
                txtRechnungsbetragVorrechnung.setToolTipText(null);
            }
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.RESTBETRAG_VORRECHNUNG)) {
            if (errLblRestbetragVorrechnung != null) {
                errLblRestbetragVorrechnung.setVisible(false);
            }
            if (txtRestbetragVorrechnung != null) {
                txtRestbetragVorrechnung.setToolTipText(null);
            }
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.RECHNUNGSBETRAG_NACHRECHNUNG)) {
            if (errLblRechnungsbetragNachrechnung != null) {
                errLblRechnungsbetragNachrechnung.setVisible(false);
            }
            if (txtRechnungsbetragNachrechnung != null) {
                txtRechnungsbetragNachrechnung.setToolTipText(null);
            }
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.RESTBETRAG_NACHRECHNUNG)) {
            if (errLblRestbetragNachrechnung != null) {
                errLblRestbetragNachrechnung.setVisible(false);
            }
            if (txtRestbetragNachrechnung != null) {
                txtRestbetragNachrechnung.setToolTipText(null);
            }
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.DIFFERENZ_SCHULGELD)) {
            if (errLblDifferenzSchulgeld != null) {
                errLblDifferenzSchulgeld.setVisible(false);
            }
            if (txtDifferenzSchulgeld != null) {
                txtDifferenzSchulgeld.setToolTipText(null);
            }
        }
    }

    @Override
    public void disableFields(boolean disable, Set<Field> fields) {
        super.disableFields(disable, fields);
        if (radioBtnAmVorrechnung != null && (fields.contains(Field.ALLE) || fields.contains(Field.AM_VORRECHNUNG))) {
            radioBtnAmVorrechnung.setEnabled(!disable);
        }
        if (radioBtnVorVorrechnung != null && (fields.contains(Field.ALLE) || fields.contains(Field.VOR_VORRECHNUNG))) {
            radioBtnVorVorrechnung.setEnabled(!disable);
        }
        if (radioBtnNachVorrechnung != null && (fields.contains(Field.ALLE) || fields.contains(Field.NACH_VORRECHNUNG))) {
            radioBtnNachVorrechnung.setEnabled(!disable);
        }
        if (radioBtnAmNachrechnung != null && (fields.contains(Field.ALLE) || fields.contains(Field.AM_NACHRECHNUNG))) {
            radioBtnAmNachrechnung.setEnabled(!disable);
        }
        if (radioBtnVorNachrechnung != null && (fields.contains(Field.ALLE) || fields.contains(Field.VOR_NACHRECHNUNG))) {
            radioBtnVorNachrechnung.setEnabled(!disable);
        }
        if (radioBtnNachNachrechnung != null && (fields.contains(Field.ALLE) || fields.contains(Field.NACH_NACHRECHNUNG))) {
            radioBtnNachNachrechnung.setEnabled(!disable);
        }
    }

    class RadioBtnGroupRolleListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            LOGGER.trace("SemesterrechnungenSuchenController Rolle Event");
            semesterrechnungenSuchenModel.setRolle(SemesterrechnungenSuchenModel.RolleSelected.valueOf(e.getActionCommand()));
        }
    }

    class RadioBtnGroupSemesterrechnungCodeJaNeinListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            LOGGER.trace("SemesterrechnungenSuchenController SemesterrechnungCodeJaNein Event");
            semesterrechnungenSuchenModel.setSemesterrechnungCodeJaNeinSelected(SemesterrechnungenSuchenModel.SemesterrechnungCodeJaNeinSelected.valueOf(e.getActionCommand()));
        }
    }

    class RadioBtnGroupStipendiumJaNeinListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            LOGGER.trace("SemesterrechnungenSuchenController StipendiumJaNein Event");
            semesterrechnungenSuchenModel.setStipendiumJaNeinSelected(SemesterrechnungenSuchenModel.StipendiumJaNeinSelected.valueOf(e.getActionCommand()));
        }
    }

    class RadioBtnGroupRechnungsdatumGesetztVorrechnungListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            LOGGER.trace("SemesterrechnungenSuchenController RechungsdatumGesetztVorrechnung Event");
            semesterrechnungenSuchenModel.setRechnungsdatumGesetztVorrechnungSelected(SemesterrechnungenSuchenModel.RechnungsdatumGesetztVorrechnungSelected.valueOf(e.getActionCommand()));
        }
    }

    class RadioBtnGroupPraezisierungRechnungsdatumVorrechnungListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            LOGGER.trace("SemesterrechnungenSuchenController PraezisierungRechnungsdatumVorrechnung Event");
            semesterrechnungenSuchenModel.setPraezisierungRechnungsdatumVorrechnungSelected(SemesterrechnungenSuchenModel.PraezisierungRechnungsdatumVorrechnungSelected.valueOf(e.getActionCommand()));
        }
    }

    class RadioBtnGroupPraezisierungErmaessigungVorrechnungListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            LOGGER.trace("SemesterrechnungenSuchenController PraezisierungErmaessigungVorrechnung Event");
            semesterrechnungenSuchenModel.setPraezisierungErmaessigungVorrechnungSelected(SemesterrechnungenSuchenModel.PraezisierungErmaessigungVorrechnungSelected.valueOf(e.getActionCommand()));
        }
    }

    class RadioBtnGroupPraezisierungZuschlagVorrechnungListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            LOGGER.trace("SemesterrechnungenSuchenController PraezisierungZuschlagVorrechnung Event");
            semesterrechnungenSuchenModel.setPraezisierungZuschlagVorrechnungSelected(SemesterrechnungenSuchenModel.PraezisierungZuschlagVorrechnungSelected.valueOf(e.getActionCommand()));
        }
    }

    class RadioBtnGroupPraezisierungAnzahlWochenVorrechnungListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            LOGGER.trace("SemesterrechnungenSuchenController PraezisierungAnzahlWochenVorrechnung Event");
            semesterrechnungenSuchenModel.setPraezisierungAnzahlWochenVorrechnungSelected(SemesterrechnungenSuchenModel.PraezisierungAnzahlWochenVorrechnungSelected.valueOf(e.getActionCommand()));
        }
    }

    class RadioBtnGroupPraezisierungWochenbetragVorrechnungListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            LOGGER.trace("SemesterrechnungenSuchenController PraezisierungWochenbetragVorrechnung Event");
            semesterrechnungenSuchenModel.setPraezisierungWochenbetragVorrechnungSelected(SemesterrechnungenSuchenModel.PraezisierungWochenbetragVorrechnungSelected.valueOf(e.getActionCommand()));
        }
    }

    class RadioBtnGroupPraezisierungRechnungsbetragVorrechnungListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            LOGGER.trace("SemesterrechnungenSuchenController PraezisierungRechnungsbetragVorrechnung Event");
            semesterrechnungenSuchenModel.setPraezisierungRechnungsbetragVorrechnungSelected(SemesterrechnungenSuchenModel.PraezisierungRechnungsbetragVorrechnungSelected.valueOf(e.getActionCommand()));
        }
    }

    class RadioBtnGroupPraezisierungRestbetragVorrechnungListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            LOGGER.trace("SemesterrechnungenSuchenController PraezisierungRestbetragVorrechnung Event");
            semesterrechnungenSuchenModel.setPraezisierungRestbetragVorrechnungSelected(SemesterrechnungenSuchenModel.PraezisierungRestbetragVorrechnungSelected.valueOf(e.getActionCommand()));
        }
    }

    class RadioBtnGroupSechsJahresRabattJaNeinVorrechnungListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            LOGGER.trace("SemesterrechnungenSuchenController SechsJahresRabattJaNeinVorrechnung Event");
            semesterrechnungenSuchenModel.setSechsJahresRabattJaNeinVorrechnungSelected(SemesterrechnungenSuchenModel.SechsJahresRabattJaNeinVorrechnungSelected.valueOf(e.getActionCommand()));
        }
    }

    class RadioBtnGroupRechnungsdatumGesetztNachrechnungListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            LOGGER.trace("SemesterrechnungenSuchenController RechnungsdatumGesetztNachrechnung Event");
            semesterrechnungenSuchenModel.setRechnungsdatumGesetztNachrechnungSelected(SemesterrechnungenSuchenModel.RechnungsdatumGesetztNachrechnungSelected.valueOf(e.getActionCommand()));
        }
    }

    class RadioBtnGroupPraezisierungRechnungsdatumNachrechnungListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            LOGGER.trace("SemesterrechnungenSuchenController PraezisierungRechnungsdatumNachrechnung Event");
            semesterrechnungenSuchenModel.setPraezisierungRechnungsdatumNachrechnungSelected(SemesterrechnungenSuchenModel.PraezisierungRechnungsdatumNachrechnungSelected.valueOf(e.getActionCommand()));
        }
    }

    class RadioBtnGroupPraezisierungErmaessigungNachrechnungListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            LOGGER.trace("SemesterrechnungenSuchenController PraezisierungErmaessigungNachrechnung Event");
            semesterrechnungenSuchenModel.setPraezisierungErmaessigungNachrechnungSelected(SemesterrechnungenSuchenModel.PraezisierungErmaessigungNachrechnungSelected.valueOf(e.getActionCommand()));
        }
    }

    class RadioBtnGroupPraezisierungZuschlagNachrechnungListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            LOGGER.trace("SemesterrechnungenSuchenController PraezisierungZuschlagNachrechnung Event");
            semesterrechnungenSuchenModel.setPraezisierungZuschlagNachrechnungSelected(SemesterrechnungenSuchenModel.PraezisierungZuschlagNachrechnungSelected.valueOf(e.getActionCommand()));
        }
    }

    class RadioBtnGroupPraezisierungAnzahlWochenNachrechnungListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            LOGGER.trace("SemesterrechnungenSuchenController PraezisierungAnzahlWochenNachrechnung Event");
            semesterrechnungenSuchenModel.setPraezisierungAnzahlWochenNachrechnungSelected(SemesterrechnungenSuchenModel.PraezisierungAnzahlWochenNachrechnungSelected.valueOf(e.getActionCommand()));
        }
    }

    class RadioBtnGroupPraezisierungWochenbetragNachrechnungListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            LOGGER.trace("SemesterrechnungenSuchenController PraezisierungWochenbetragNachrechnung Event");
            semesterrechnungenSuchenModel.setPraezisierungWochenbetragNachrechnungSelected(SemesterrechnungenSuchenModel.PraezisierungWochenbetragNachrechnungSelected.valueOf(e.getActionCommand()));
        }
    }

    class RadioBtnGroupPraezisierungRechnungsbetragNachrechnungListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            LOGGER.trace("SemesterrechnungenSuchenController PraezisierungRechnungsbetragNachrechnung Event");
            semesterrechnungenSuchenModel.setPraezisierungRechnungsbetragNachrechnungSelected(SemesterrechnungenSuchenModel.PraezisierungRechnungsbetragNachrechnungSelected.valueOf(e.getActionCommand()));
        }
    }

    class RadioBtnGroupPraezisierungRestbetragNachrechnungListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            LOGGER.trace("SemesterrechnungenSuchenController PraezisierungRestbetragNachrechnung Event");
            semesterrechnungenSuchenModel.setPraezisierungRestbetragNachrechnungSelected(SemesterrechnungenSuchenModel.PraezisierungRestbetragNachrechnungSelected.valueOf(e.getActionCommand()));
        }
    }

    class RadioBtnGroupSechsJahresRabattJaNeinNachrechnungListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            LOGGER.trace("SemesterrechnungenSuchenController SechsJahresRabattJaNeinNachrechnung Event");
            semesterrechnungenSuchenModel.setSechsJahresRabattJaNeinNachrechnungSelected(SemesterrechnungenSuchenModel.SechsJahresRabattJaNeinNachrechnungSelected.valueOf(e.getActionCommand()));
        }
    }

    class RadioBtnGroupPraezisierungDifferenzSchulgeldListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            LOGGER.trace("SemesterrechnungenSuchenController PraezisierungDifferenzSchulgeld Event");
            semesterrechnungenSuchenModel.setPraezisierungDifferenzSchulgeldSelected(SemesterrechnungenSuchenModel.PraezisierungDifferenzSchulgeldSelected.valueOf(e.getActionCommand()));
        }
    }
}
