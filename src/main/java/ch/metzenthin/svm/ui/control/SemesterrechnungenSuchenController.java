package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.CompletedListener;
import ch.metzenthin.svm.domain.model.SemesterrechnungBearbeitenModel;
import ch.metzenthin.svm.domain.model.SemesterrechnungenSuchenModel;
import ch.metzenthin.svm.domain.model.SemesterrechnungenTableData;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.ui.componentmodel.SemesterrechnungenTableModel;
import ch.metzenthin.svm.ui.components.SemesterrechnungBearbeitenPanel;
import ch.metzenthin.svm.ui.components.SemesterrechnungenPanel;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.util.HashSet;
import java.util.Set;

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
    private JTextField txtSchulgeldVorrechnung;
    private JTextField txtSchulgeldNachrechnung;
    private JTextField txtDifferenzSchulgeld;
    private JTextField txtRestbetrag;
    private JLabel errLblNachname;
    private JLabel errLblVorname;
    private JLabel errLblSchulgeldVorrechnung;
    private JLabel errLblSchulgeldNachrechnung;
    private JLabel errLblDifferenzSchulgeld;
    private JLabel errLblRestbetrag;
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
    private JRadioButton radioBtnGleichWochenbetragVorrechnung;
    private JRadioButton radioBtnKleinerWochenbetragVorrechnung;
    private JRadioButton radioBtnGroesserWochenbetragVorrechnung;
    private JRadioButton radioBtnGleichSchulgeldVorrechnung;
    private JRadioButton radioBtnKleinerSchulgeldVorrechnung;
    private JRadioButton radioBtnGroesserSchulgeldVorrechnung;
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
    private JRadioButton radioBtnGleichSchulgeldNachrechnung;
    private JRadioButton radioBtnKleinerSchulgeldNachrechnung;
    private JRadioButton radioBtnGroesserSchulgeldNachrechnung;
    private JRadioButton radioBtnSechsJahresRabattJaNachrechnung;
    private JRadioButton radioBtnSechsJahresRabattNeinNachrechnung;
    private JRadioButton radioBtnSechsJahresRabattAlleNachrechnung;
    private JRadioButton radioBtnGleichDifferenzSchulgeld;
    private JRadioButton radioBtnKleinerDifferenzSchulgeld;
    private JRadioButton radioBtnGroesserDifferenzSchulgeld;
    private JRadioButton radioBtnGleichRestbetrag;
    private JRadioButton radioBtnKleinerRestbetrag;
    private JRadioButton radioBtnGroesserRestbetrag;
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

    public void setTxtSchulgeldVorrechnung(JTextField txtSchulgeldVorrechnung) {
        this.txtSchulgeldVorrechnung = txtSchulgeldVorrechnung;
        this.txtSchulgeldVorrechnung.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSchulgeldVorrechnungEvent();
            }
        });
        this.txtSchulgeldVorrechnung.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onSchulgeldVorrechnungEvent();
            }
        });
    }

    private void onSchulgeldVorrechnungEvent() {
        LOGGER.trace("SemesterrechnungenSuchenController Event SchulgeldVorrechnung");
        boolean equalFieldAndModelValue = equalsNullSafe(txtSchulgeldVorrechnung.getText(), semesterrechnungenSuchenModel.getSchulgeldVorrechnung());
        try {
            setModelSchulgeldVorrechnung();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelSchulgeldVorrechnung() throws SvmValidationException {
        makeErrorLabelInvisible(Field.SCHULGELD_VORRECHNUNG);
        try {
            semesterrechnungenSuchenModel.setSchulgeldVorrechnung(txtSchulgeldVorrechnung.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("SemesterrechnungenSuchenController setModelSchulgeldVorrechnung Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtSchulgeldNachrechnung(JTextField txtSchulgeldNachrechnung) {
        this.txtSchulgeldNachrechnung = txtSchulgeldNachrechnung;
        this.txtSchulgeldNachrechnung.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSchulgeldNachrechnungEvent();
            }
        });
        this.txtSchulgeldNachrechnung.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onSchulgeldNachrechnungEvent();
            }
        });
    }

    private void onSchulgeldNachrechnungEvent() {
        LOGGER.trace("SemesterrechnungenSuchenController Event SchulgeldNachrechnung");
        boolean equalFieldAndModelValue = equalsNullSafe(txtSchulgeldNachrechnung.getText(), semesterrechnungenSuchenModel.getSchulgeldNachrechnung());
        try {
            setModelSchulgeldNachrechnung();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelSchulgeldNachrechnung() throws SvmValidationException {
        makeErrorLabelInvisible(Field.SCHULGELD_NACHRECHNUNG);
        try {
            semesterrechnungenSuchenModel.setSchulgeldNachrechnung(txtSchulgeldNachrechnung.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("SemesterrechnungenSuchenController setModelSchulgeldNachrechnung Exception=" + e.getMessage());
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

    public void setTxtRestbetrag(JTextField txtRestbetrag) {
        this.txtRestbetrag = txtRestbetrag;
        this.txtRestbetrag.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onRestbetragEvent();
            }
        });
        this.txtRestbetrag.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onRestbetragEvent();
            }
        });
    }

    private void onRestbetragEvent() {
        LOGGER.trace("SemesterrechnungenSuchenController Event Restbetrag");
        boolean equalFieldAndModelValue = equalsNullSafe(txtRestbetrag.getText(), semesterrechnungenSuchenModel.getRestbetrag());
        try {
            setModelRestbetrag();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelRestbetrag() throws SvmValidationException {
        makeErrorLabelInvisible(Field.RESTBETRAG);
        try {
            semesterrechnungenSuchenModel.setRestbetrag(txtRestbetrag.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("SemesterrechnungenSuchenController setModelRestbetrag Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setErrLblNachname(JLabel errLblNachname) {
        this.errLblNachname = errLblNachname;
    }

    public void setErrLblVorname(JLabel errLblVorname) {
        this.errLblVorname = errLblVorname;
    }

    public void setErrLblSchulgeldVorrechnung(JLabel errLblSchulgeldVorrechnung) {
        this.errLblSchulgeldVorrechnung = errLblSchulgeldVorrechnung;
    }

    public void setErrLblSchulgeldNachrechnung(JLabel errLblSchulgeldNachrechnung) {
        this.errLblSchulgeldNachrechnung = errLblSchulgeldNachrechnung;
    }

    public void setErrLblDifferenzSchulgeld(JLabel errLblDifferenzSchulgeld) {
        this.errLblDifferenzSchulgeld = errLblDifferenzSchulgeld;
    }

    public void setErrLblRestbetrag(JLabel errLblRestbetrag) {
        this.errLblRestbetrag = errLblRestbetrag;
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

    public void setRadioBtnGroupPraezisierungSchulgeldVorrechnung(JRadioButton radioBtnGleichSchulgeldVorrechnung, JRadioButton radioBtnKleinerSchulgeldVorrechnung, JRadioButton radioBtnGroesserSchulgeldVorrechnung) {
        this.radioBtnGleichSchulgeldVorrechnung = radioBtnGleichSchulgeldVorrechnung;
        this.radioBtnKleinerSchulgeldVorrechnung = radioBtnKleinerSchulgeldVorrechnung;
        this.radioBtnGroesserSchulgeldVorrechnung = radioBtnGroesserSchulgeldVorrechnung;
        // Action Commands
        this.radioBtnGleichSchulgeldVorrechnung.setActionCommand(SemesterrechnungenSuchenModel.PraezisierungSchulgeldVorrechnungSelected.GLEICH.toString());
        this.radioBtnKleinerSchulgeldVorrechnung.setActionCommand(SemesterrechnungenSuchenModel.PraezisierungSchulgeldVorrechnungSelected.KLEINER.toString());
        this.radioBtnGroesserSchulgeldVorrechnung.setActionCommand(SemesterrechnungenSuchenModel.PraezisierungSchulgeldVorrechnungSelected.GROESSER.toString());
        // Listener
        RadioBtnGroupPraezisierungSchulgeldVorrechnungListener radioBtnGroupPraezisierungSchulgeldVorrechnungListener = new RadioBtnGroupPraezisierungSchulgeldVorrechnungListener();
        this.radioBtnGleichSchulgeldVorrechnung.addActionListener(radioBtnGroupPraezisierungSchulgeldVorrechnungListener);
        this.radioBtnKleinerSchulgeldVorrechnung.addActionListener(radioBtnGroupPraezisierungSchulgeldVorrechnungListener);
        this.radioBtnGroesserSchulgeldVorrechnung.addActionListener(radioBtnGroupPraezisierungSchulgeldVorrechnungListener);
        // Initialisieren mit gleich
        semesterrechnungenSuchenModel.setPraezisierungSchulgeldVorrechnungSelected(SemesterrechnungenSuchenModel.PraezisierungSchulgeldVorrechnungSelected.GLEICH);
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

    public void setRadioBtnGroupPraezisierungSchulgeldNachrechnung(JRadioButton radioBtnGleichSchulgeldNachrechnung, JRadioButton radioBtnKleinerSchulgeldNachrechnung, JRadioButton radioBtnGroesserSchulgeldNachrechnung) {
        this.radioBtnGleichSchulgeldNachrechnung = radioBtnGleichSchulgeldNachrechnung;
        this.radioBtnKleinerSchulgeldNachrechnung = radioBtnKleinerSchulgeldNachrechnung;
        this.radioBtnGroesserSchulgeldNachrechnung = radioBtnGroesserSchulgeldNachrechnung;
        // Action Commands
        this.radioBtnGleichSchulgeldNachrechnung.setActionCommand(SemesterrechnungenSuchenModel.PraezisierungSchulgeldNachrechnungSelected.GLEICH.toString());
        this.radioBtnKleinerSchulgeldNachrechnung.setActionCommand(SemesterrechnungenSuchenModel.PraezisierungSchulgeldNachrechnungSelected.KLEINER.toString());
        this.radioBtnGroesserSchulgeldNachrechnung.setActionCommand(SemesterrechnungenSuchenModel.PraezisierungSchulgeldNachrechnungSelected.GROESSER.toString());
        // Listener
        RadioBtnGroupPraezisierungSchulgeldNachrechnungListener radioBtnGroupPraezisierungSchulgeldNachrechnungListener = new RadioBtnGroupPraezisierungSchulgeldNachrechnungListener();
        this.radioBtnGleichSchulgeldNachrechnung.addActionListener(radioBtnGroupPraezisierungSchulgeldNachrechnungListener);
        this.radioBtnKleinerSchulgeldNachrechnung.addActionListener(radioBtnGroupPraezisierungSchulgeldNachrechnungListener);
        this.radioBtnGroesserSchulgeldNachrechnung.addActionListener(radioBtnGroupPraezisierungSchulgeldNachrechnungListener);
        // Initialisieren mit gleich
        semesterrechnungenSuchenModel.setPraezisierungSchulgeldNachrechnungSelected(SemesterrechnungenSuchenModel.PraezisierungSchulgeldNachrechnungSelected.GLEICH);
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

    public void setRadioBtnGroupPraezisierungRestbetrag(JRadioButton radioBtnGleichRestbetrag, JRadioButton radioBtnKleinerRestbetrag, JRadioButton radioBtnGroesserRestbetrag) {
        this.radioBtnGleichRestbetrag = radioBtnGleichRestbetrag;
        this.radioBtnKleinerRestbetrag = radioBtnKleinerRestbetrag;
        this.radioBtnGroesserRestbetrag = radioBtnGroesserRestbetrag;
        // Action Commands
        this.radioBtnGleichRestbetrag.setActionCommand(SemesterrechnungenSuchenModel.PraezisierungRestbetragSelected.GLEICH.toString());
        this.radioBtnKleinerRestbetrag.setActionCommand(SemesterrechnungenSuchenModel.PraezisierungRestbetragSelected.KLEINER.toString());
        this.radioBtnGroesserRestbetrag.setActionCommand(SemesterrechnungenSuchenModel.PraezisierungRestbetragSelected.GROESSER.toString());
        // Listener
        RadioBtnGroupPraezisierungRestbetragListener radioBtnGroupPraezisierungRestbetragListener = new RadioBtnGroupPraezisierungRestbetragListener();
        this.radioBtnGleichRestbetrag.addActionListener(radioBtnGroupPraezisierungRestbetragListener);
        this.radioBtnKleinerRestbetrag.addActionListener(radioBtnGroupPraezisierungRestbetragListener);
        this.radioBtnGroesserRestbetrag.addActionListener(radioBtnGroupPraezisierungRestbetragListener);
        // Initialisieren mit gleich
        semesterrechnungenSuchenModel.setPraezisierungRestbetragSelected(SemesterrechnungenSuchenModel.PraezisierungRestbetragSelected.GLEICH);
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
        SemesterrechnungenTableData semesterrechnungenTableData = semesterrechnungenSuchenModel.suchen();
        SemesterrechnungenTableModel semesterrechnungenTableModel = new SemesterrechnungenTableModel(semesterrechnungenTableData);
        if (semesterrechnungenTableData.size() > 1 || !semesterrechnungenSuchenModel.isSuchkriterienSelected()) {
            SemesterrechnungenPanel semesterrechnungenPanel = new SemesterrechnungenPanel(svmContext, semesterrechnungenTableModel);
            semesterrechnungenPanel.addNextPanelListener(nextPanelListener);
            semesterrechnungenPanel.addCloseListener(closeListener);
            semesterrechnungenPanel.addZurueckListener(zurueckListener);
            nextPanelListener.actionPerformed(new ActionEvent(new Object[]{semesterrechnungenPanel.$$$getRootComponent$$$(), "Suchresultat"}, ActionEvent.ACTION_PERFORMED, "Suchresultat verfügbar"));
        } else if (semesterrechnungenTableData.size() == 1) {
            // Direkt zu Semesterrechnung bearbeiten
            SemesterrechnungBearbeitenModel semesterrechnungBearbeitenModel = semesterrechnungenSuchenModel.getSemesterrechnungBearbeitenModel(svmContext, semesterrechnungenTableModel);
            SemesterrechnungBearbeitenPanel semesterrechnungBearbeitenPanel = new SemesterrechnungBearbeitenPanel(svmContext, semesterrechnungBearbeitenModel, null, semesterrechnungenTableModel, null, 0);
            semesterrechnungBearbeitenPanel.addNextPanelListener(nextPanelListener);
            semesterrechnungBearbeitenPanel.addCloseListener(closeListener);
            semesterrechnungBearbeitenPanel.addZurueckZuSemesterrechnungSuchenListener(zurueckListener);
            String title = "Semesterrechnung " + semesterrechnungenSuchenModel.getSemester().getSemesterbezeichnung() + " " + semesterrechnungenSuchenModel.getSemester().getSchuljahr();
            nextPanelListener.actionPerformed(new ActionEvent(new Object[]{semesterrechnungBearbeitenPanel.$$$getRootComponent$$$(), title}, ActionEvent.ACTION_PERFORMED, "Semesterrechnung ausgewählt"));
        } else {
            JOptionPane.showMessageDialog(null, "Es wurden keine Semesterrechnungen gefunden, welche auf die Suchabfrage passen.", "Keine Semesterrechnungen gefunden", JOptionPane.INFORMATION_MESSAGE);
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
        else if (checkIsFieldChange(Field.PRAEZISIERUNG_WOCHENBETRAG_VORRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.PraezisierungWochenbetragVorrechnungSelected.GLEICH) {
            radioBtnGleichWochenbetragVorrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.PRAEZISIERUNG_WOCHENBETRAG_VORRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.PraezisierungWochenbetragVorrechnungSelected.KLEINER) {
            radioBtnKleinerWochenbetragVorrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.PRAEZISIERUNG_WOCHENBETRAG_VORRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.PraezisierungWochenbetragVorrechnungSelected.GROESSER) {
            radioBtnGroesserWochenbetragVorrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.PRAEZISIERUNG_SCHULGELD_VORRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.PraezisierungSchulgeldVorrechnungSelected.GLEICH) {
            radioBtnGleichSchulgeldVorrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.PRAEZISIERUNG_SCHULGELD_VORRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.PraezisierungSchulgeldVorrechnungSelected.KLEINER) {
            radioBtnKleinerSchulgeldVorrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.PRAEZISIERUNG_SCHULGELD_VORRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.PraezisierungSchulgeldVorrechnungSelected.GROESSER) {
            radioBtnGroesserSchulgeldVorrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.SCHULGELD_VORRECHNUNG, evt)) {
            txtSchulgeldVorrechnung.setText(semesterrechnungenSuchenModel.getSchulgeldVorrechnung() == null ? null : semesterrechnungenSuchenModel.getSchulgeldVorrechnung().toString());
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
        else if (checkIsFieldChange(Field.RECHNUNGSDATUM_GESETZT_VORRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.RechnungsdatumGesetztVorrechnungSelected.GESETZT) {
            radioBtnRechnungsdatumGesetztVorrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.RECHNUNGSDATUM_GESETZT_VORRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.RechnungsdatumGesetztVorrechnungSelected.NICHT_GESETZT) {
            radioBtnRechnungsdatumNichtGesetztVorrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.RECHNUNGSDATUM_GESETZT_VORRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.RechnungsdatumGesetztVorrechnungSelected.ALLE) {
            radioBtnRechnungsdatumGesetztAlleVorrechnung.setSelected(true);
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
        else if (checkIsFieldChange(Field.PRAEZISIERUNG_SCHULGELD_NACHRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.PraezisierungSchulgeldNachrechnungSelected.GLEICH) {
            radioBtnGleichSchulgeldNachrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.PRAEZISIERUNG_SCHULGELD_NACHRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.PraezisierungSchulgeldNachrechnungSelected.KLEINER) {
            radioBtnKleinerSchulgeldNachrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.PRAEZISIERUNG_SCHULGELD_NACHRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.PraezisierungSchulgeldNachrechnungSelected.GROESSER) {
            radioBtnGroesserSchulgeldNachrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.SCHULGELD_NACHRECHNUNG, evt)) {
            txtSchulgeldNachrechnung.setText(semesterrechnungenSuchenModel.getSchulgeldNachrechnung() == null ? null : semesterrechnungenSuchenModel.getSchulgeldNachrechnung().toString());
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
        else if (checkIsFieldChange(Field.RECHNUNGSDATUM_GESETZT_NACHRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.RechnungsdatumGesetztNachrechnungSelected.GESETZT) {
            radioBtnRechnungsdatumGesetztNachrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.RECHNUNGSDATUM_GESETZT_NACHRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.RechnungsdatumGesetztNachrechnungSelected.NICHT_GESETZT) {
            radioBtnRechnungsdatumNichtGesetztNachrechnung.setSelected(true);
        }
        else if (checkIsFieldChange(Field.RECHNUNGSDATUM_GESETZT_NACHRECHNUNG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.RechnungsdatumGesetztNachrechnungSelected.ALLE) {
            radioBtnRechnungsdatumGesetztAlleNachrechnung.setSelected(true);
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
        else if (checkIsFieldChange(Field.PRAEZISIERUNG_RESTBETRAG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.PraezisierungRestbetragSelected.GLEICH) {
            radioBtnGleichRestbetrag.setSelected(true);
        }
        else if (checkIsFieldChange(Field.PRAEZISIERUNG_RESTBETRAG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.PraezisierungRestbetragSelected.KLEINER) {
            radioBtnKleinerRestbetrag.setSelected(true);
        }
        else if (checkIsFieldChange(Field.PRAEZISIERUNG_RESTBETRAG, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.PraezisierungRestbetragSelected.GROESSER) {
            radioBtnGroesserRestbetrag.setSelected(true);
        }
        else if (checkIsFieldChange(Field.RESTBETRAG, evt)) {
            txtRestbetrag.setText(semesterrechnungenSuchenModel.getRestbetrag() == null ? null : semesterrechnungenSuchenModel.getRestbetrag().toString());
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
        if (txtSchulgeldVorrechnung.isEnabled()) {
            LOGGER.trace("Validate field SchulgeldVorrechnung");
            setModelSchulgeldVorrechnung();
        }
        if (txtSchulgeldNachrechnung.isEnabled()) {
            LOGGER.trace("Validate field SchulgeldNachrechnung");
            setModelSchulgeldNachrechnung();
        }
        if (txtDifferenzSchulgeld.isEnabled()) {
            LOGGER.trace("Validate field DifferenzSchulgeld");
            setModelDifferenzSchulgeld();
        }
        if (txtRestbetrag.isEnabled()) {
            LOGGER.trace("Validate field Restbetrag");
            setModelRestbetrag();
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
        if (e.getAffectedFields().contains(Field.SCHULGELD_VORRECHNUNG)) {
            errLblSchulgeldVorrechnung.setVisible(true);
            errLblSchulgeldVorrechnung.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.SCHULGELD_NACHRECHNUNG)) {
            errLblSchulgeldNachrechnung.setVisible(true);
            errLblSchulgeldNachrechnung.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.DIFFERENZ_SCHULGELD)) {
            errLblDifferenzSchulgeld.setVisible(true);
            errLblDifferenzSchulgeld.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.RESTBETRAG)) {
            errLblRestbetrag.setVisible(true);
            errLblRestbetrag.setText(e.getMessage());
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
        if (e.getAffectedFields().contains(Field.SCHULGELD_VORRECHNUNG)) {
            txtSchulgeldVorrechnung.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.SCHULGELD_NACHRECHNUNG)) {
            txtSchulgeldNachrechnung.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.DIFFERENZ_SCHULGELD)) {
            txtDifferenzSchulgeld.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.RESTBETRAG)) {
            txtRestbetrag.setToolTipText(e.getMessage());
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
        if (fields.contains(Field.ALLE) || fields.contains(Field.SCHULGELD_VORRECHNUNG)) {
            if (errLblSchulgeldVorrechnung != null) {
                errLblSchulgeldVorrechnung.setVisible(false);
            }
            if (txtSchulgeldVorrechnung != null) {
                txtSchulgeldVorrechnung.setToolTipText(null);
            }
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.SCHULGELD_NACHRECHNUNG)) {
            if (errLblSchulgeldNachrechnung != null) {
                errLblSchulgeldNachrechnung.setVisible(false);
            }
            if (txtSchulgeldNachrechnung != null) {
                txtSchulgeldNachrechnung.setToolTipText(null);
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
        if (fields.contains(Field.ALLE) || fields.contains(Field.RESTBETRAG)) {
            if (errLblRestbetrag != null) {
                errLblRestbetrag.setVisible(false);
            }
            if (txtRestbetrag != null) {
                txtRestbetrag.setToolTipText(null);
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

    class RadioBtnGroupPraezisierungWochenbetragVorrechnungListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            LOGGER.trace("SemesterrechnungenSuchenController PraezisierungWochenbetragVorrechnung Event");
            semesterrechnungenSuchenModel.setPraezisierungWochenbetragVorrechnungSelected(SemesterrechnungenSuchenModel.PraezisierungWochenbetragVorrechnungSelected.valueOf(e.getActionCommand()));
        }
    }

    class RadioBtnGroupPraezisierungSchulgeldVorrechnungListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            LOGGER.trace("SemesterrechnungenSuchenController PraezisierungSchulgeldVorrechnung Event");
            semesterrechnungenSuchenModel.setPraezisierungSchulgeldVorrechnungSelected(SemesterrechnungenSuchenModel.PraezisierungSchulgeldVorrechnungSelected.valueOf(e.getActionCommand()));
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

    class RadioBtnGroupPraezisierungSchulgeldNachrechnungListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            LOGGER.trace("SemesterrechnungenSuchenController PraezisierungSchulgeldNachrechnung Event");
            semesterrechnungenSuchenModel.setPraezisierungSchulgeldNachrechnungSelected(SemesterrechnungenSuchenModel.PraezisierungSchulgeldNachrechnungSelected.valueOf(e.getActionCommand()));
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

    class RadioBtnGroupPraezisierungRestbetragListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            LOGGER.trace("SemesterrechnungenSuchenController PraezisierungRestbetrag Event");
            semesterrechnungenSuchenModel.setPraezisierungRestbetragSelected(SemesterrechnungenSuchenModel.PraezisierungRestbetragSelected.valueOf(e.getActionCommand()));
        }
    }
}
