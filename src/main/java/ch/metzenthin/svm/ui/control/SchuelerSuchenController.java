package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.dataTypes.Field;
import ch.metzenthin.svm.dataTypes.Wochentag;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.CompletedListener;
import ch.metzenthin.svm.domain.model.SchuelerSuchenModel;
import ch.metzenthin.svm.domain.model.SchuelerSuchenResult;
import ch.metzenthin.svm.ui.componentmodel.SchuelerSuchenTableModel;
import ch.metzenthin.svm.ui.components.SchuelerSuchenResultPanel;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.util.HashSet;
import java.util.Set;

import static ch.metzenthin.svm.common.utils.Converter.asString;
import static ch.metzenthin.svm.common.utils.SimpleValidator.equalsNullSafe;

/**
 * @author Martin Schraner
 */
public class SchuelerSuchenController extends PersonController {

    private static final Logger LOGGER = Logger.getLogger(SchuelerSuchenController.class);

    private JTextField txtGeburtsdatumSuchperiode;
    private JTextField txtCodes;
    private JTextField txtLehrkraft;
    private JTextField txtVon;
    private JTextField txtBis;
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
    private JComboBox<Wochentag> comboBoxWochentag;
    private JLabel errLblStichtag;
    private JLabel errLblGeburtsdatumSuchperiode;
    private JButton btnSuchen;
    private JButton btnAbbrechen;
    private ActionListener closeListener;
    private ActionListener nextPanelListener;

    private final SvmContext svmContext;
    private SchuelerSuchenModel schuelerSuchenModel;

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

    public void setTxtLehrkraft(JTextField txtLehrkraft) {
        this.txtLehrkraft = txtLehrkraft;
    }

    public void setComboBoxWochentag(JComboBox<Wochentag> comboBoxWochentag) {
        this.comboBoxWochentag = comboBoxWochentag;
    }

    public void setTxtVon(JTextField txtVon) {
        this.txtVon = txtVon;
    }

    public void setTxtBis(JTextField txtBis) {
        this.txtBis = txtBis;
    }

    public void setTxtCodes(JTextField txtCodes) {
        this.txtCodes = txtCodes;
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
    }

    public void setBtnSuchen(JButton btnSuchen) {
        this.btnSuchen = btnSuchen;
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

    public void setErrLblStichtag(JLabel errLblStichtag) {
        this.errLblStichtag = errLblStichtag;
    }

    public void setErrLblGeburtsdatumSuchperiode(JLabel errLblGeburtsdatumSuchperiode) {
        this.errLblGeburtsdatumSuchperiode = errLblGeburtsdatumSuchperiode;
    }

    private void onGeburtsdatumSuchperiodeEvent() {
        LOGGER.trace("SchuelerSuchenController Event GeburtsdatumSuchperiode");
        boolean equalFieldAndModelValue = equalsNullSafe(txtGeburtsdatumSuchperiode.getText(), schuelerSuchenModel.getGeburtsdatumSuchperiode());
        setModelGeburtsdatumSuchperiode();
        if (equalFieldAndModelValue) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelGeburtsdatumSuchperiode() {
        errLblGeburtsdatumSuchperiode.setVisible(false);
        try {
            schuelerSuchenModel.setGeburtsdatumSuchperiode(txtGeburtsdatumSuchperiode.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("SchuelerSuchenController setModelGeburtsdatum Exception=" + e.getMessage());
            showErrMsg(e);
        }
    }

    private void onStichtagEvent() {
        LOGGER.trace("SchuelerSuchenController Event Stichtag");
        boolean equalFieldAndModelValue = equalsNullSafe(txtStichtag.getText(), schuelerSuchenModel.getStichtag());
        setModelStichtag();
        if (equalFieldAndModelValue) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelStichtag() {
        errLblStichtag.setVisible(false);
        try {
            schuelerSuchenModel.setStichtag(txtStichtag.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("SchuelerSuchenController setModelStichtag Exception=" + e.getMessage());
            showErrMsg(e);
        }
    }

    private void onAbbrechen() {
        LOGGER.trace("SchuelerSuchenPanel Abbrechen gedrückt");
        closeListener.actionPerformed(new ActionEvent(btnAbbrechen, ActionEvent.ACTION_PERFORMED, "Close nach Abbrechen"));
    }

    private void onSuchen() {
        LOGGER.trace("SchuelerSuchenPanel Suchen gedrückt");
        SchuelerSuchenResult schuelerSuchenResult = schuelerSuchenModel.suchen();
        SchuelerSuchenTableModel schuelerSuchenTableModel = new SchuelerSuchenTableModel(schuelerSuchenResult);
        SchuelerSuchenResultPanel schuelerSuchenResultPanel = new SchuelerSuchenResultPanel(svmContext, schuelerSuchenTableModel);
        schuelerSuchenResultPanel.addNextPanelListener(nextPanelListener);
        schuelerSuchenResultPanel.addCloseListener(closeListener);
        nextPanelListener.actionPerformed(new ActionEvent(new Object[]{schuelerSuchenResultPanel.$$$getRootComponent$$$(), "Suchresultat"}, ActionEvent.ACTION_PERFORMED, "Suchresultat verfügbar"));
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

    void enableDisableFields() {
        if (schuelerSuchenModel.getRolle() != null && schuelerSuchenModel.getRolle() != SchuelerSuchenModel.RolleSelected.SCHUELER) {
            disableGeburtsdatumSuchperiode();
            disableGeschlechtSelection();
        } else {
            enableGeburtsdatumSuchperiode();
            enableGeschlechtSelection();
        }
        if ((schuelerSuchenModel.getRolle() != null && schuelerSuchenModel.getRolle() != SchuelerSuchenModel.RolleSelected.SCHUELER)
                || (schuelerSuchenModel.getAnmeldestatus() != null && schuelerSuchenModel.getAnmeldestatus() != SchuelerSuchenModel.AnmeldestatusSelected.ANGEMELDET)) {
            disableDispensationSelection();
            disableKurs();
            disableCodes();
        } else {
            enableDispensationSelection();
            enableKurs();
            enableCodes();
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

    private void enableGeschlechtSelection() {
        schuelerSuchenModel.enableFields(getGeschlechtFields());
    }

    private void disableGeschlechtSelection() {
        schuelerSuchenModel.disableFields(getGeschlechtFields());
        schuelerSuchenModel.setGeschlecht(SchuelerSuchenModel.GeschlechtSelected.ALLE);
    }

    private void enableKurs() {
        schuelerSuchenModel.enableFields(getKursFields());
    }

    private void disableKurs() {
        schuelerSuchenModel.disableFields(getKursFields());
        schuelerSuchenModel.makeErrorLabelsInvisible(getKursFields());
        // Textfelder in jedem Fall leeren, auch wenn Model-Wert noch null und damit von
        // schuelerSuchenModel.invalidateKurs() kein Property Change-Event ausgelöst wird
        txtLehrkraft.setText("");
        txtVon.setText("");
        txtBis.setText("");
        // TODO
        // schuelerSuchenModel.invalidateKurs(); (-> auch Wochentage auf alle setzen)
    }

    private void enableCodes() {
        schuelerSuchenModel.enableFields(getCodesFields());
    }

    private void disableCodes() {
        schuelerSuchenModel.disableFields(getCodesFields());
        schuelerSuchenModel.makeErrorLabelsInvisible(getCodesFields());
        // Textfeld in jedem Fall leeren, auch wenn Model-Wert noch null und damit von
        // schuelerSuchenModel.invalidateCodes() kein Property Change-Event ausgelöst wird
        txtCodes.setText("");
        // TODO
        // schuelerSuchenModel.invalidateCodes();
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

    private Set<Field> getGeschlechtFields() {
        Set<Field> geschlechtFields = new HashSet<>();
        geschlechtFields.add(Field.WEIBLICH);
        geschlechtFields.add(Field.MAENNLICH);
        geschlechtFields.add(Field.GESCHLECHT_ALLE);
        return geschlechtFields;
    }

    private Set<Field> getKursFields() {
        Set<Field> kursFields = new HashSet<>();
        kursFields.add(Field.LEHRKRAFT);
        kursFields.add(Field.VON);
        kursFields.add(Field.BIS);
        kursFields.add(Field.WOCHENTAG);
        return kursFields;
    }

    private Set<Field> getCodesFields() {
        Set<Field> codesFields = new HashSet<>();
        codesFields.add(Field.CODES);
        return codesFields;
    }

    @Override
    void doPropertyChange(PropertyChangeEvent evt) {
        enableDisableFields();
        super.doPropertyChange(evt);
        if (checkIsFieldChange(Field.GEBURTSDATUM_SUCHPERIODE, evt)) {
            txtGeburtsdatumSuchperiode.setText(schuelerSuchenModel.getGeburtsdatumSuchperiode());
        }
        if (checkIsFieldChange(Field.ROLLE, evt) && evt.getNewValue() == SchuelerSuchenModel.RolleSelected.SCHUELER) {
            radioBtnSchueler.setSelected(true);
        }
        if (checkIsFieldChange(Field.ROLLE, evt) && evt.getNewValue() == SchuelerSuchenModel.RolleSelected.ELTERN) {
            radioBtnEltern.setSelected(true);
        }
        if (checkIsFieldChange(Field.ROLLE, evt) && evt.getNewValue() == SchuelerSuchenModel.RolleSelected.RECHNUNGSEMPFAENGER) {
            radioBtnRechnungsempfaenger.setSelected(true);
        }
        if (checkIsFieldChange(Field.ROLLE, evt) && evt.getNewValue() == SchuelerSuchenModel.RolleSelected.ALLE) {
            radioBtnRolleAlle.setSelected(true);
        }
        if (checkIsFieldChange(Field.ANMELDESTATUS, evt) && evt.getNewValue() == SchuelerSuchenModel.AnmeldestatusSelected.ANGEMELDET) {
            radioBtnAngemeldet.setSelected(true);
        }
        if (checkIsFieldChange(Field.ANMELDESTATUS, evt) && evt.getNewValue() == SchuelerSuchenModel.AnmeldestatusSelected.ABGEMELDET) {
            radioBtnAbgemeldet.setSelected(true);
        }
        if (checkIsFieldChange(Field.ANMELDESTATUS, evt) && evt.getNewValue() == SchuelerSuchenModel.AnmeldestatusSelected.ALLE) {
            radioBtnAnmeldestatusAlle.setSelected(true);
        }
        if (checkIsFieldChange(Field.DISPENSATION, evt) && evt.getNewValue() == SchuelerSuchenModel.DispensationSelected.DISPENSIERT) {
            radioBtnDispensiert.setSelected(true);
        }
        if (checkIsFieldChange(Field.DISPENSATION, evt) && evt.getNewValue() == SchuelerSuchenModel.DispensationSelected.NICHT_DISPENSIERT) {
            radioBtnNichtDispensiert.setSelected(true);
        }
        if (checkIsFieldChange(Field.DISPENSATION, evt) && evt.getNewValue() == SchuelerSuchenModel.DispensationSelected.ALLE) {
            radioBtnDispensationAlle.setSelected(true);
        }
        if (checkIsFieldChange(Field.GESCHLECHT, evt) && evt.getNewValue() == SchuelerSuchenModel.GeschlechtSelected.WEIBLICH) {
            radioBtnWeiblich.setSelected(true);
        }
        if (checkIsFieldChange(Field.GESCHLECHT, evt) && evt.getNewValue() == SchuelerSuchenModel.GeschlechtSelected.MAENNLICH) {
            radioBtnMaennlich.setSelected(true);
        }
        if (checkIsFieldChange(Field.GESCHLECHT, evt) && evt.getNewValue() == SchuelerSuchenModel.GeschlechtSelected.ALLE) {
            radioBtnGeschlechtAlle.setSelected(true);
        }
        if (checkIsFieldChange(Field.STICHTAG, evt)) {
            txtStichtag.setText(asString(schuelerSuchenModel.getStichtag()));
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
    }

    @Override
    public void makeErrorLabelsInvisible(Set<Field> fields) {
        super.makeErrorLabelsInvisible(fields);
        if (fields.contains(Field.GEBURTSDATUM_SUCHPERIODE)) {
            errLblGeburtsdatumSuchperiode.setVisible(false);
            txtGeburtsdatumSuchperiode.setToolTipText(null);
        }
        if (fields.contains(Field.STICHTAG)) {
            errLblStichtag.setVisible(false);
            txtStichtag.setToolTipText(null);
        }
    }

    @Override
    public void disableFields(boolean disable, Set<Field> fields) {
        super.disableFields(disable, fields);
        if (fields.contains(Field.ALLE) || fields.contains(Field.GEBURTSDATUM_SUCHPERIODE)) {
            txtGeburtsdatumSuchperiode.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.SCHUELER)) {
            radioBtnSchueler.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.ELTERN)) {
            radioBtnEltern.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.RECHNUNGSEMPFAENGER)) {
            radioBtnRechnungsempfaenger.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.ROLLE_ALLE)) {
            radioBtnRolleAlle.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.ANGEMELDET)) {
            radioBtnAngemeldet.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.ABGEMELDET)) {
            radioBtnAbgemeldet.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.ANMELDESTATUS_ALLE)) {
            radioBtnAnmeldestatusAlle.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.DISPENSIERT)) {
            radioBtnDispensiert.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.NICHT_DISPENSIERT)) {
            radioBtnNichtDispensiert.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.DISPENSATION_ALLE)) {
            radioBtnDispensationAlle.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.WEIBLICH)) {
            radioBtnWeiblich.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.MAENNLICH)) {
            radioBtnMaennlich.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.GESCHLECHT_ALLE)) {
            radioBtnGeschlechtAlle.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.LEHRKRAFT)) {
            txtLehrkraft.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.WOCHENTAG)) {
            comboBoxWochentag.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.VON)) {
            txtVon.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.BIS)) {
            txtBis.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.CODES)) {
            txtCodes.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.STICHTAG)) {
            txtStichtag.setEnabled(!disable);
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
