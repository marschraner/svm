package ch.metzenthin.svm.ui.control;

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
import java.util.Set;

import static ch.metzenthin.svm.common.utils.Converter.asString;
import static ch.metzenthin.svm.common.utils.SimpleValidator.equalsNullSafe;

/**
 * @author Martin Schraner
 */
public class SchuelerSuchenController extends PersonController {

    private static final Logger LOGGER = Logger.getLogger(SchuelerSuchenController.class);

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
    private JButton btnSuchen;
    private JButton btnAbbrechen;
    private ActionListener closeListener;
    private ActionListener nextPanelListener;

    private SchuelerSuchenModel schuelerSuchenModel;

    public SchuelerSuchenController(SchuelerSuchenModel schuelerSuchenModel) {
        super(schuelerSuchenModel);
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
        SchuelerSuchenResultPanel schuelerSuchenResultPanel = new SchuelerSuchenResultPanel(schuelerSuchenTableModel);
        schuelerSuchenResultPanel.addNextPanelListener(nextPanelListener);
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

    @Override
    void doPropertyChange(PropertyChangeEvent evt) {
        super.doPropertyChange(evt);
        if (checkIsFieldChange(Field.STICHTAG, evt)) {
            txtStichtag.setText(asString(schuelerSuchenModel.getStichtag()));
        }
    }

    @Override
    void validateFields() throws SvmValidationException {
        super.validateFields();
        if (txtStichtag.isEnabled()) {
            LOGGER.trace("Validate field Stichtag");
            setModelStichtag();
        }
    }

    @Override
    void showErrMsg(SvmValidationException e) {
        super.showErrMsg(e);
        if (e.getAffectedFields().contains(Field.STICHTAG)) {
            errLblStichtag.setVisible(true);
            errLblStichtag.setText(e.getMessage());
        }
    }

    @Override
    void showErrMsgAsToolTip(SvmValidationException e) {
        super.showErrMsgAsToolTip(e);
        if (e.getAffectedFields().contains(Field.STICHTAG)) {
            txtStichtag.setToolTipText(e.getMessage());
        }
    }

    @Override
    public void makeErrorLabelsInvisible(Set<Field> fields) {
        super.makeErrorLabelsInvisible(fields);
        if (fields.contains(Field.STICHTAG)) {
            errLblStichtag.setVisible(false);
            txtStichtag.setToolTipText(null);
        }
    }

    @Override
    public void disableFields(boolean disable, Set<Field> fields) {
        super.disableFields(disable, fields);
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
