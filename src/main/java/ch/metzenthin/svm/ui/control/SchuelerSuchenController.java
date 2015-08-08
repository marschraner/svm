package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.common.dataTypes.Semesterbezeichnung;
import ch.metzenthin.svm.common.dataTypes.Wochentag;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.FindSemesterForCalendarCommand;
import ch.metzenthin.svm.domain.model.CompletedListener;
import ch.metzenthin.svm.domain.model.SchuelerSuchenModel;
import ch.metzenthin.svm.domain.model.SchuelerSuchenTableData;
import ch.metzenthin.svm.persistence.entities.SchuelerCode;
import ch.metzenthin.svm.persistence.entities.Lehrkraft;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.ui.componentmodel.SchuelerSuchenTableModel;
import ch.metzenthin.svm.ui.components.SchuelerDatenblattPanel;
import ch.metzenthin.svm.ui.components.SchuelerSuchenResultPanel;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
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
    private JTextField txtZeitBeginn;
    private JTextField txtStichtag;
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
    private JSpinner spinnerSchuljahreKurs;
    private JComboBox<Semesterbezeichnung> comboBoxSemesterbezeichnung;
    private JComboBox<Wochentag> comboBoxWochentag;
    private JComboBox<Lehrkraft> comboBoxLehrkraft;
    private JComboBox<SchuelerCode> comboBoxCode;
    private JCheckBox checkBoxKursFuerSucheBeruecksichtigen;
    private JLabel errLblGeburtsdatumSuchperiode;
    private JLabel errLblStichtag;
    private JLabel errLblZeitBeginn;
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

    public void setSpinnerSchuljahreKurs(JSpinner spinnerSchuljahreKurs) {
        this.spinnerSchuljahreKurs = spinnerSchuljahreKurs;
        spinnerSchuljahreKurs.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                onSchuljahrKursSelected();
            }
        });
        initSchuljahrKurs();
    }

    private void initSchuljahrKurs() {
        String schuljahr;
        FindSemesterForCalendarCommand findSemesterForCalendarCommand = new FindSemesterForCalendarCommand(svmContext.getSvmModel().getSemestersAll());
        findSemesterForCalendarCommand.execute();
        Semester currentSemester = findSemesterForCalendarCommand.getCurrentSemester();
        Semester nextSemester = findSemesterForCalendarCommand.getNextSemester();
        if (currentSemester != null) {
            // Innerhalb Semester
            schuljahr = currentSemester.getSchuljahr();
        } else if (nextSemester != null) {
            // Ferien zwischen 2 Semestern
            schuljahr = nextSemester.getSchuljahr();
        } else {
            // Kein passendes Semester erfasst
            Calendar today = new GregorianCalendar();
            int schuljahr1;
            if (today.get(Calendar.MONTH) <= Calendar.JUNE) {
                schuljahr1 = today.get(Calendar.YEAR) - 1;
            } else {
                schuljahr1 = today.get(Calendar.YEAR);
            }
            int schuljahr2 = schuljahr1 + 1;
            schuljahr = schuljahr1 + "/" + schuljahr2;
        }
        try {
            schuelerSuchenModel.setSchuljahrKurs(schuljahr);
        } catch (SvmValidationException ignore) {
        }
    }

    private void onSchuljahrKursSelected() {
        LOGGER.trace("KurseSemesterwahlController Event SchuljahrKurs selected =" + spinnerSchuljahreKurs.getValue());
        boolean equalFieldAndModelValue = equalsNullSafe(spinnerSchuljahreKurs.getValue(), schuelerSuchenModel.getSchuljahrKurs());
        try {
            setModelSchuljahrKurs();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelSchuljahrKurs() throws SvmValidationException {
        makeErrorLabelInvisible(Field.SCHULJAHR_KURS);
        try {
            schuelerSuchenModel.setSchuljahrKurs((String) spinnerSchuljahreKurs.getValue());
        } catch (SvmValidationException e) {
            LOGGER.trace("PersonController setModelSchuljahrKurs Exception=" + e.getMessage());
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
        Semesterbezeichnung semesterbezeichnung;
        FindSemesterForCalendarCommand findSemesterForCalendarCommand = new FindSemesterForCalendarCommand(svmContext.getSvmModel().getSemestersAll());
        findSemesterForCalendarCommand.execute();
        Semester currentSemester = findSemesterForCalendarCommand.getCurrentSemester();
        Semester nextSemester = findSemesterForCalendarCommand.getNextSemester();
        if (currentSemester != null) {
            // Innerhalb Semester
            semesterbezeichnung = currentSemester.getSemesterbezeichnung();
        } else if (nextSemester != null) {
            // Ferien zwischen 2 Semestern
            semesterbezeichnung = nextSemester.getSemesterbezeichnung();
        } else {
            // Kein passendes Semester erfasst
            Calendar today = new GregorianCalendar();
            if (today.get(Calendar.MONTH) >= Calendar.FEBRUARY && today.get(Calendar.MONTH) <= Calendar.JUNE) {
                semesterbezeichnung = Semesterbezeichnung.ZWEITES_SEMESTER;
            } else {
                semesterbezeichnung = Semesterbezeichnung.ERSTES_SEMESTER;
            }
        }
        schuelerSuchenModel.setSemesterbezeichnung(semesterbezeichnung);
    }

    private void onSemesterbezeichnungSelected() {
        LOGGER.trace("KurseSemesterwahlController Event Semesterbezeichnung selected=" + comboBoxSemesterbezeichnung.getSelectedItem());
        boolean equalFieldAndModelValue = equalsNullSafe(comboBoxSemesterbezeichnung.getSelectedItem(), schuelerSuchenModel.getSemesterbezeichnung());
        setModelSemesterbezeichnung();
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelSemesterbezeichnung() {
        makeErrorLabelInvisible(Field.SEMESTERBEZEICHNUNG);
        schuelerSuchenModel.setSemesterbezeichnung((Semesterbezeichnung) comboBoxSemesterbezeichnung.getSelectedItem());
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
        // SchuelerCode in Model initialisieren mit erstem ComboBox-Wert
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
                onZeitBeginnEvent(true);
            }
        });
        this.txtZeitBeginn.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onZeitBeginnEvent(false);
            }
        });
    }

    private void onZeitBeginnEvent(boolean showRequiredErrMsg) {
        LOGGER.trace("schuelerSuchenController Event ZeitBeginn");
        boolean equalFieldAndModelValue = equalsNullSafe(txtZeitBeginn.getText(), schuelerSuchenModel.getZeitBeginn());
        try {
            setModelZeitBeginn(showRequiredErrMsg);
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelZeitBeginn(boolean showRequiredErrMsg) throws SvmValidationException {
        makeErrorLabelInvisible(Field.ZEIT_BEGINN);
        try {
            schuelerSuchenModel.setZeitBeginn(txtZeitBeginn.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("SchuelerSuchenController setModelZeitBeginn RequiredException=" + e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtZeitBeginn.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("SchuelerSuchenController setModelZeitBeginn Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setComboBoxLehrkraft(JComboBox<Lehrkraft> comboBoxLehrkraft) {
        this.comboBoxLehrkraft = comboBoxLehrkraft;
        Lehrkraft[] selectableLehrkraefte = schuelerSuchenModel.getSelectableLehrkraefte(svmContext.getSvmModel());
        comboBoxLehrkraft.setModel(new DefaultComboBoxModel<>(selectableLehrkraefte));
        // Model initialisieren mit erstem ComboBox-Wert
        schuelerSuchenModel.setLehrkraft(selectableLehrkraefte[0]);
        comboBoxLehrkraft.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onLehrkraftSelected();
            }
        });
    }

    private void onLehrkraftSelected() {
        LOGGER.trace("SchuelerSuchenController Event Lehrkraft selected=" + comboBoxLehrkraft.getSelectedItem());
        boolean equalFieldAndModelValue = equalsNullSafe(comboBoxLehrkraft.getSelectedItem(), schuelerSuchenModel.getLehrkraft());
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
        schuelerSuchenModel.setLehrkraft((Lehrkraft) comboBoxLehrkraft.getSelectedItem());
    }

    public void setCheckBoxKursFuerSucheBeruecksichtigen(JCheckBox checkBoxKursFuerSucheBeruecksichtigen) {
        this.checkBoxKursFuerSucheBeruecksichtigen = checkBoxKursFuerSucheBeruecksichtigen;
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

    public void setComboBoxCode(JComboBox<SchuelerCode> comboBoxCode) {
        this.comboBoxCode = comboBoxCode;
        SchuelerCode[] selectableSchuelerCodes = schuelerSuchenModel.getSelectableCodes(svmContext.getSvmModel());
        comboBoxCode.setModel(new DefaultComboBoxModel<>(selectableSchuelerCodes));
        // Model initialisieren mit erstem ComboBox-Wert
        schuelerSuchenModel.setSchuelerCode(selectableSchuelerCodes[0]);
        this.comboBoxCode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCodeSelected();
            }
        });
    }

    private void onCodeSelected() {
        LOGGER.trace("SchuelerSuchenController Event SchuelerCode selected=" + comboBoxCode.getSelectedItem());
        setModelCombobox();
    }

    private void setModelCombobox() {
        schuelerSuchenModel.setSchuelerCode((SchuelerCode) comboBoxCode.getSelectedItem());
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
        SchuelerSuchenTableData schuelerSuchenTableData = schuelerSuchenModel.suchen(svmContext.getSvmModel());
        SchuelerSuchenTableModel schuelerSuchenTableModel = new SchuelerSuchenTableModel(schuelerSuchenTableData);
        if (schuelerSuchenTableData.size() > 1 || (schuelerSuchenTableData.size() == 1 && (schuelerSuchenTableData.getWochentag() != null || schuelerSuchenTableData.getZeitBeginn() != null || schuelerSuchenTableData.getLehrkraft() != null))) {
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
            JOptionPane.showMessageDialog(null, "Es wurden keine Schüler gefunden, welche auf die Suchabfrage passen.", "Keine Schüler gefunden", JOptionPane.INFORMATION_MESSAGE);
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
        kursFields.add(Field.SCHULJAHR_KURS);
        kursFields.add(Field.SEMESTERBEZEICHNUNG);
        kursFields.add(Field.WOCHENTAG);
        kursFields.add(Field.ZEIT_BEGINN);
        kursFields.add(Field.LEHRKRAFT);
        return kursFields;
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
        else if (checkIsFieldChange(Field.SCHULJAHR_KURS, evt)) {
            spinnerSchuljahreKurs.setValue(schuelerSuchenModel.getSchuljahrKurs());
        }
        else if (checkIsFieldChange(Field.SEMESTERBEZEICHNUNG, evt)) {
            comboBoxSemesterbezeichnung.setSelectedItem(schuelerSuchenModel.getSemesterbezeichnung());
        }
        else if (checkIsFieldChange(Field.WOCHENTAG, evt)) {
            comboBoxWochentag.setSelectedItem(schuelerSuchenModel.getWochentag());
        }
        else if (checkIsFieldChange(Field.ZEIT_BEGINN, evt)) {
            txtZeitBeginn.setText(asString(schuelerSuchenModel.getZeitBeginn()));
        }
        else if (checkIsFieldChange(Field.LEHRKRAFT, evt)) {
            comboBoxLehrkraft.setSelectedItem(schuelerSuchenModel.getLehrkraft());
        }
        else if (checkIsFieldChange(Field.KURS_FUER_SUCHE_BERUECKSICHTIGEN, evt)) {
            checkBoxKursFuerSucheBeruecksichtigen.setSelected(schuelerSuchenModel.isKursFuerSucheBeruecksichtigen());
        }
        else if (checkIsFieldChange(Field.CODE, evt)) {
            comboBoxCode.setSelectedItem(schuelerSuchenModel.getSchuelerCode());
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
            setModelZeitBeginn(true);
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
        if (spinnerSchuljahreKurs != null && (fields.contains(Field.ALLE) || fields.contains(Field.SCHULJAHR_KURS))) {
            spinnerSchuljahreKurs.setEnabled(!disable);
        }
        if (comboBoxSemesterbezeichnung != null && (fields.contains(Field.ALLE) || fields.contains(Field.SEMESTERBEZEICHNUNG))) {
            comboBoxSemesterbezeichnung.setEnabled(!disable);
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
        if (comboBoxCode != null && (fields.contains(Field.ALLE) || fields.contains(Field.CODE))) {
            comboBoxCode.setEnabled(!disable);
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
