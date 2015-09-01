package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.common.dataTypes.Stipendium;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.CompletedListener;
import ch.metzenthin.svm.domain.model.SemesterrechnungenTableData;
import ch.metzenthin.svm.domain.model.SemesterrechnungenSuchenModel;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.persistence.entities.SemesterrechnungCode;
import ch.metzenthin.svm.ui.componentmodel.SemesterrechnungenTableModel;
import ch.metzenthin.svm.ui.components.SemesterrechnungenPanel;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.util.Set;

import static ch.metzenthin.svm.common.utils.Converter.asString;
import static ch.metzenthin.svm.common.utils.SimpleValidator.equalsNullSafe;

/**
 * @author Martin Schraner
 */
public class SemesterrechnungenSuchenController extends AbstractController {

    private static final Logger LOGGER = Logger.getLogger(SemesterrechnungenSuchenController.class);

    // Möglichkeit zum Umschalten des validation modes (nicht dynamisch)
    private static final boolean MODEL_VALIDATION_MODE = false;

    private JSpinner spinnerSemester;
    private JTextField txtNachname;
    private JTextField txtVorname;
    private JTextField txtRechnungsdatum;
    private JTextField txtWochenbetrag;
    private JTextField txtSchulgeld;
    private JLabel errLblNachname;
    private JLabel errLblVorname;
    private JLabel errLblRechnungsdatum;
    private JLabel errLblWochenbetrag;
    private JLabel errLblSchulgeld;
    private JComboBox<SemesterrechnungCode> comboBoxSemesterrechnungCode;
    private JComboBox<Stipendium> comboBoxStipendium;
    private JCheckBox checkBoxGratiskinder;
    private JCheckBox checkBoxSechsJahresRabatt;
    private JRadioButton radioBtnRechnungsempfaenger;
    private JRadioButton radioBtnSchueler;
    private JRadioButton radioBtnEltern;
    private JRadioButton radioBtnRolleAlle;
    private JRadioButton radioBtnAm;
    private JRadioButton radioBtnVor;
    private JRadioButton radioBtnNach;
    private JRadioButton radioBtnOffen;
    private JRadioButton radioBtnBezahlt;
    private JRadioButton radioBtnRechnungsstatusAlle;
    private JButton btnSuchen;
    private JButton btnAbbrechen;
    private ActionListener closeListener;
    private ActionListener nextPanelListener;
    private final SvmContext svmContext;
    private SemesterrechnungenSuchenModel semesterrechnungenSuchenModel;
    private ActionListener zurueckListener;

    public SemesterrechnungenSuchenController(SvmContext svmContext, SemesterrechnungenSuchenModel semesterrechnungenSuchenModel) {
        super(semesterrechnungenSuchenModel);
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
        svmContext.getSvmModel().loadAll();
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

    public void setComboBoxSemesterrechnungCode(JComboBox<SemesterrechnungCode> comboBoxCode) {
        this.comboBoxSemesterrechnungCode = comboBoxCode;
        SemesterrechnungCode[] selectableSemesterrechnungCodes = semesterrechnungenSuchenModel.getSelectableSemesterrechnungCodes(svmContext.getSvmModel());
        comboBoxCode.setModel(new DefaultComboBoxModel<>(selectableSemesterrechnungCodes));
        // Model initialisieren mit erstem ComboBox-Wert
        semesterrechnungenSuchenModel.setSemesterrechnungCode(selectableSemesterrechnungCodes[0]);
        this.comboBoxSemesterrechnungCode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSemesterrechnungCodeSelected();
            }
        });
    }

    private void onSemesterrechnungCodeSelected() {
        LOGGER.trace("SemesterrechnungSuchenController Event SemesterrechnungCode selected=" + comboBoxSemesterrechnungCode.getSelectedItem());
        setModelSemesterrechnungCode();
    }

    private void setModelSemesterrechnungCode() {
        semesterrechnungenSuchenModel.setSemesterrechnungCode((SemesterrechnungCode) comboBoxSemesterrechnungCode.getSelectedItem());
    }

    public void setComboBoxStipendium(JComboBox<Stipendium> comboBoxStipendium) {
        this.comboBoxStipendium = comboBoxStipendium;
        comboBoxStipendium.setModel(new DefaultComboBoxModel<>(Stipendium.values()));
        comboBoxStipendium.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onStipendiumSelected();
            }
        });
        // SchuelerCode in Model initialisieren mit erstem ComboBox-Wert
        semesterrechnungenSuchenModel.setStipendium(Stipendium.values()[0]);
    }

    private void onStipendiumSelected() {
        LOGGER.trace("SemesterrechnungenSuchenController Event Stipendium selected=" + comboBoxStipendium.getSelectedItem());
        boolean equalFieldAndModelValue = equalsNullSafe(comboBoxStipendium.getSelectedItem(), semesterrechnungenSuchenModel.getStipendium());
        setModelStipendium();
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelStipendium() {
        makeErrorLabelInvisible(Field.STIPENDIUM);
        semesterrechnungenSuchenModel.setStipendium((Stipendium) comboBoxStipendium.getSelectedItem());
    }

    public void setCheckBoxSechsJahresRabatt(JCheckBox checkBoxSechsJahresRabatt) {
        this.checkBoxSechsJahresRabatt = checkBoxSechsJahresRabatt;
        if (svmContext.getSvmModel().getSemestersAll().isEmpty()) {
            checkBoxSechsJahresRabatt.setEnabled(false);
        }
        this.checkBoxSechsJahresRabatt.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                onSechsJahresRabattEvent();
            }
        });
        // Initialisierung
        semesterrechnungenSuchenModel.setSechsJahresRabatt(false);
    }

    private void onSechsJahresRabattEvent() {
        LOGGER.trace("SemesterrechnungenSuchenController Event SechsJahresRabatt. Selected=" + checkBoxSechsJahresRabatt.isSelected());
        setModelSechsJahresRabatt();
    }

    private void setModelSechsJahresRabatt() {
        semesterrechnungenSuchenModel.setSechsJahresRabatt(checkBoxSechsJahresRabatt.isSelected());
    }

    public void setCheckBoxGratiskinder(JCheckBox checkBoxGratiskinder) {
        this.checkBoxGratiskinder = checkBoxGratiskinder;
        if (svmContext.getSvmModel().getSemestersAll().isEmpty()) {
            checkBoxGratiskinder.setEnabled(false);
        }
        this.checkBoxGratiskinder.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                onGratiskinderEvent();
            }
        });
        // Initialisierung
        semesterrechnungenSuchenModel.setGratiskinder(false);
    }

    private void onGratiskinderEvent() {
        LOGGER.trace("SemesterrechnungenSuchenController Event Gratiskinder. Selected=" + checkBoxGratiskinder.isSelected());
        setModelGratiskinder();
    }

    private void setModelGratiskinder() {
        semesterrechnungenSuchenModel.setGratiskinder(checkBoxGratiskinder.isSelected());
    }

    public void setTxtRechnungsdatum(JTextField txtRechnungsdatum) {
        this.txtRechnungsdatum = txtRechnungsdatum;
        this.txtRechnungsdatum.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onRechnungsdatumEvent();
            }
        });
        this.txtRechnungsdatum.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onRechnungsdatumEvent();
            }
        });
    }

    private void onRechnungsdatumEvent() {
        LOGGER.trace("SemesterrechnungenSuchenController Event Rechnungsdatum");
        boolean equalFieldAndModelValue = equalsNullSafe(txtRechnungsdatum.getText(), semesterrechnungenSuchenModel.getRechnungsdatum());
        try {
            setModelRechnungsdatum();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelRechnungsdatum() throws SvmValidationException {
        makeErrorLabelInvisible(Field.RECHNUNGSDATUM_VORRECHNUNG);
        try {
            semesterrechnungenSuchenModel.setRechnungsdatum(txtRechnungsdatum.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("SemesterrechnungenSuchenController setModelRechnungsdatum Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtWochenbetrag(JTextField txtWochenbetrag) {
        this.txtWochenbetrag = txtWochenbetrag;
        this.txtWochenbetrag.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onWochenbetragEvent();
            }
        });
        this.txtWochenbetrag.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onWochenbetragEvent();
            }
        });
    }

    private void onWochenbetragEvent() {
        LOGGER.trace("SemesterrechnungenSuchenController Event Wochenbetrag");
        boolean equalFieldAndModelValue = equalsNullSafe(txtWochenbetrag.getText(), semesterrechnungenSuchenModel.getWochenbetrag());
        try {
            setModelWochenbetrag();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelWochenbetrag() throws SvmValidationException {
        makeErrorLabelInvisible(Field.WOCHENBETRAG_VORRECHNUNG);
        try {
            semesterrechnungenSuchenModel.setWochenbetrag(txtWochenbetrag.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("SemesterrechnungenSuchenController setModelWochenbetrag Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtSchulgeld(JTextField txtSchulgeld) {
        this.txtSchulgeld = txtSchulgeld;
        this.txtSchulgeld.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSchulgeldEvent();
            }
        });
        this.txtSchulgeld.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onSchulgeldEvent();
            }
        });
    }

    private void onSchulgeldEvent() {
        LOGGER.trace("SemesterrechnungenSuchenController Event Schulgeld");
        boolean equalFieldAndModelValue = equalsNullSafe(txtSchulgeld.getText(), semesterrechnungenSuchenModel.getSchulgeld());
        try {
            setModelSchulgeld();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelSchulgeld() throws SvmValidationException {
        makeErrorLabelInvisible(Field.SCHULGELD_VORRECHNUNG);
        try {
            semesterrechnungenSuchenModel.setSchulgeld(txtSchulgeld.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("SemesterrechnungenSuchenController setModelSchulgeld Exception=" + e.getMessage());
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

    public void setErrLblRechnungsdatum(JLabel errLblRechnungsdatum) {
        this.errLblRechnungsdatum = errLblRechnungsdatum;
    }

    public void setErrLblWochenbetrag(JLabel errLblWochenbetrag) {
        this.errLblWochenbetrag = errLblWochenbetrag;
    }

    public void setErrLblSchulgeld(JLabel errLblSchulgeld) {
        this.errLblSchulgeld = errLblSchulgeld;
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

    public void setRadioBtnGroupRechunungsdatum(JRadioButton radioBtnAm, JRadioButton radioBtnVor, JRadioButton radioBtnNach) {
        this.radioBtnAm = radioBtnAm;
        this.radioBtnVor = radioBtnVor;
        this.radioBtnNach = radioBtnNach;
        // Action Commands
        this.radioBtnAm.setActionCommand(SemesterrechnungenSuchenModel.RechnungsdatumSelected.AM.toString());
        this.radioBtnVor.setActionCommand(SemesterrechnungenSuchenModel.RechnungsdatumSelected.VOR.toString());
        this.radioBtnNach.setActionCommand(SemesterrechnungenSuchenModel.RechnungsdatumSelected.NACH.toString());
        // Listener
        RadioBtnGroupRechnungsdatumListener radioBtnGroupRechnungsdatumListener = new RadioBtnGroupRechnungsdatumListener();
        this.radioBtnAm.addActionListener(radioBtnGroupRechnungsdatumListener);
        this.radioBtnVor.addActionListener(radioBtnGroupRechnungsdatumListener);
        this.radioBtnNach.addActionListener(radioBtnGroupRechnungsdatumListener);
        // Initialisieren mit am
        semesterrechnungenSuchenModel.setRechnungsdatumSelected(SemesterrechnungenSuchenModel.RechnungsdatumSelected.AM);
    }

    public void setRadioBtnGroupRechnungsstatus(JRadioButton radioBtnOffen, JRadioButton radioBtnBezahlt, JRadioButton radioBtnRechnungsstatusAlle) {
        this.radioBtnOffen = radioBtnOffen;
        this.radioBtnBezahlt = radioBtnBezahlt;
        this.radioBtnRechnungsstatusAlle = radioBtnRechnungsstatusAlle;
        // Action Commands
        this.radioBtnOffen.setActionCommand(SemesterrechnungenSuchenModel.RechnungsstatusSelected.OFFEN.toString());
        this.radioBtnBezahlt.setActionCommand(SemesterrechnungenSuchenModel.RechnungsstatusSelected.BEZAHLT.toString());
        this.radioBtnRechnungsstatusAlle.setActionCommand(SemesterrechnungenSuchenModel.RechnungsstatusSelected.ALLE.toString());
        // Listener
        RadioBtnGroupRechnungsstatusListener radioBtnGroupRechnungsstatusListener = new RadioBtnGroupRechnungsstatusListener();
        this.radioBtnOffen.addActionListener(radioBtnGroupRechnungsstatusListener);
        this.radioBtnBezahlt.addActionListener(radioBtnGroupRechnungsstatusListener);
        this.radioBtnRechnungsstatusAlle.addActionListener(radioBtnGroupRechnungsstatusListener);
        // Initialisieren mit alle
        semesterrechnungenSuchenModel.setRechnungsstatus(SemesterrechnungenSuchenModel.RechnungsstatusSelected.ALLE);
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
        SemesterrechnungenPanel semesterrechnungenPanel = new SemesterrechnungenPanel(svmContext, semesterrechnungenTableModel);
        semesterrechnungenPanel.addNextPanelListener(nextPanelListener);
        semesterrechnungenPanel.addCloseListener(closeListener);
        semesterrechnungenPanel.addZurueckListener(zurueckListener);
        nextPanelListener.actionPerformed(new ActionEvent(new Object[]{semesterrechnungenPanel.$$$getRootComponent$$$(), "Suchresultat"}, ActionEvent.ACTION_PERFORMED, "Suchresultat verfügbar"));
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

    @Override
    void doPropertyChange(PropertyChangeEvent evt) {
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
        else if (checkIsFieldChange(Field.SEMESTERRECHNUNG_CODE, evt)) {
            comboBoxSemesterrechnungCode.setSelectedItem(semesterrechnungenSuchenModel.getSemesterrechnungCode());
        }
        else if (checkIsFieldChange(Field.STIPENDIUM, evt)) {
            comboBoxStipendium.setSelectedItem(semesterrechnungenSuchenModel.getStipendium());
        }
        else if (checkIsFieldChange(Field.SECHS_JAHRES_RABATT, evt)) {
            checkBoxSechsJahresRabatt.setSelected(semesterrechnungenSuchenModel.isSechsJahresRabatt());
        }
        else if (checkIsFieldChange(Field.GRATISKINDER, evt)) {
            checkBoxGratiskinder.setSelected(semesterrechnungenSuchenModel.isGratiskinder());
        }
        else if (checkIsFieldChange(Field.RECHNUNGSDATUM_SELECTED, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.RechnungsdatumSelected.AM) {
            radioBtnAm.setSelected(true);
        }
        else if (checkIsFieldChange(Field.RECHNUNGSDATUM_SELECTED, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.RechnungsdatumSelected.VOR) {
            radioBtnVor.setSelected(true);
        }
        else if (checkIsFieldChange(Field.RECHNUNGSDATUM_SELECTED, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.RechnungsdatumSelected.NACH) {
            radioBtnNach.setSelected(true);
        }
        else if (checkIsFieldChange(Field.RECHNUNGSDATUM_VORRECHNUNG, evt)) {
            txtRechnungsdatum.setText(asString(semesterrechnungenSuchenModel.getRechnungsdatum()));
        }
        else if (checkIsFieldChange(Field.RECHNUNGSSTATUS, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.RechnungsstatusSelected.OFFEN) {
            radioBtnOffen.setSelected(true);
        }
        else if (checkIsFieldChange(Field.RECHNUNGSSTATUS, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.RechnungsstatusSelected.BEZAHLT) {
            radioBtnBezahlt.setSelected(true);
        }
        else if (checkIsFieldChange(Field.RECHNUNGSSTATUS, evt) && evt.getNewValue() == SemesterrechnungenSuchenModel.RechnungsstatusSelected.ALLE) {
            radioBtnRechnungsstatusAlle.setSelected(true);
        }
        else if (checkIsFieldChange(Field.WOCHENBETRAG_VORRECHNUNG, evt)) {
            txtWochenbetrag.setText(semesterrechnungenSuchenModel.getWochenbetrag() == null ? null : semesterrechnungenSuchenModel.getWochenbetrag().toString());
        }
        else if (checkIsFieldChange(Field.SCHULGELD_VORRECHNUNG, evt)) {
            txtSchulgeld.setText(semesterrechnungenSuchenModel.getSchulgeld() == null ? null : semesterrechnungenSuchenModel.getSchulgeld().toString());
        }
    }

    @Override
    void validateFields() throws SvmValidationException {
        if (txtNachname.isEnabled()) {
            LOGGER.trace("Validate field Nachname");
            setModelNachname();
        }
        if (txtNachname.isEnabled()) {
            LOGGER.trace("Validate field Vorname");
            setModelVorname();
        }
        if (txtNachname.isEnabled()) {
            LOGGER.trace("Validate field Rechnungsdatum");
            setModelRechnungsdatum();
        }
        if (txtNachname.isEnabled()) {
            LOGGER.trace("Validate field Wochenbetrag");
            setModelWochenbetrag();
        }
        if (txtNachname.isEnabled()) {
            LOGGER.trace("Validate field Schulgeld");
            setModelSchulgeld();
        }
    }

    @Override
    void showErrMsg(SvmValidationException e) {
        if (e.getAffectedFields().contains(Field.NACHNAME)) {
            errLblNachname.setVisible(true);
            errLblNachname.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.VORNAME)) {
            errLblVorname.setVisible(true);
            errLblVorname.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.RECHNUNGSDATUM_VORRECHNUNG)) {
            errLblRechnungsdatum.setVisible(true);
            errLblRechnungsdatum.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.WOCHENBETRAG_VORRECHNUNG)) {
            errLblWochenbetrag.setVisible(true);
            errLblWochenbetrag.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.SCHULGELD_VORRECHNUNG)) {
            errLblSchulgeld.setVisible(true);
            errLblSchulgeld.setText(e.getMessage());
        }
    }

    @Override
    void showErrMsgAsToolTip(SvmValidationException e) {
        if (e.getAffectedFields().contains(Field.NACHNAME)) {
            txtNachname.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.VORNAME)) {
            txtVorname.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.RECHNUNGSDATUM_VORRECHNUNG)) {
            txtRechnungsdatum.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.WOCHENBETRAG_VORRECHNUNG)) {
            txtWochenbetrag.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.SCHULGELD_VORRECHNUNG)) {
            txtSchulgeld.setToolTipText(e.getMessage());
        }
    }

    @Override
    public void makeErrorLabelsInvisible(Set<Field> fields) {
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
        if (fields.contains(Field.ALLE) || fields.contains(Field.RECHNUNGSDATUM_VORRECHNUNG)) {
            if (errLblRechnungsdatum != null) {
                errLblRechnungsdatum.setVisible(false);
            }
            if (txtRechnungsdatum != null) {
                txtRechnungsdatum.setToolTipText(null);
            }
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.WOCHENBETRAG_VORRECHNUNG)) {
            if (errLblWochenbetrag != null) {
                errLblWochenbetrag.setVisible(false);
            }
            if (txtWochenbetrag != null) {
                txtWochenbetrag.setToolTipText(null);
            }
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.SCHULGELD_VORRECHNUNG)) {
            if (errLblSchulgeld != null) {
                errLblSchulgeld.setVisible(false);
            }
            if (txtSchulgeld != null) {
                txtSchulgeld.setToolTipText(null);
            }
        }
    }

    @Override
    public void disableFields(boolean disable, Set<Field> fields) {

    }

    class RadioBtnGroupRolleListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            LOGGER.trace("SemesterrechnungenSuchenController Rolle Event");
            semesterrechnungenSuchenModel.setRolle(SemesterrechnungenSuchenModel.RolleSelected.valueOf(e.getActionCommand()));
        }
    }

    class RadioBtnGroupRechnungsdatumListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            LOGGER.trace("SemesterrechnungenSuchenController Rechnungsdatum Event");
            semesterrechnungenSuchenModel.setRechnungsdatumSelected(SemesterrechnungenSuchenModel.RechnungsdatumSelected.valueOf(e.getActionCommand()));
        }
    }

    class RadioBtnGroupRechnungsstatusListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            LOGGER.trace("SemesterrechnungenSuchenController Rechnungsstatus Event");
            semesterrechnungenSuchenModel.setRechnungsstatus(SemesterrechnungenSuchenModel.RechnungsstatusSelected.valueOf(e.getActionCommand()));
        }
    }

}
