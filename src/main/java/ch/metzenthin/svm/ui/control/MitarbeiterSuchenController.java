package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.CompletedListener;
import ch.metzenthin.svm.domain.model.MitarbeiterSuchenModel;
import ch.metzenthin.svm.domain.model.MitarbeitersTableData;
import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;
import ch.metzenthin.svm.ui.componentmodel.MitarbeitersTableModel;
import ch.metzenthin.svm.ui.components.MitarbeitersPanel;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.util.Set;

import static ch.metzenthin.svm.common.utils.SimpleValidator.equalsNullSafe;

/**
 * @author Martin Schraner
 */
public class MitarbeiterSuchenController extends AbstractController {

    private static final Logger LOGGER = Logger.getLogger(MitarbeiterSuchenController.class);

    // Möglichkeit zum Umschalten des validation modes (nicht dynamisch)
    private static final boolean MODEL_VALIDATION_MODE = false;

    private final SvmContext svmContext;
    private MitarbeiterSuchenModel mitarbeiterSuchenModel;
    private JTextField txtNachname;
    private JTextField txtVorname;
    private JLabel errLblNachname;
    private JLabel errLblVorname;
    private JComboBox<MitarbeiterCode> comboBoxMitarbeiterCode;
    private JRadioButton radioBtnLehrkraftJa;
    private JRadioButton radioBtnLehrkraftNein;
    private JRadioButton radioBtnLehrkraftAlle;
    private JRadioButton radioBtnStatusAktiv;
    private JRadioButton radioBtnStatusNichtAktiv;
    private JRadioButton radioBtnStatusAlle;
    private JButton btnSuchen;
    private JButton btnAbbrechen;
    private ActionListener closeListener;
    private ActionListener nextPanelListener;
    private ActionListener zurueckListener;

    public MitarbeiterSuchenController(SvmContext svmContext, MitarbeiterSuchenModel mitarbeiterSuchenModel) {
        super(mitarbeiterSuchenModel);
        this.svmContext = svmContext;
        this.mitarbeiterSuchenModel = mitarbeiterSuchenModel;
        this.mitarbeiterSuchenModel.addPropertyChangeListener(this);
        this.mitarbeiterSuchenModel.addDisableFieldsListener(this);
        this.mitarbeiterSuchenModel.addMakeErrorLabelsInvisibleListener(this);
        this.mitarbeiterSuchenModel.addCompletedListener(new CompletedListener() {
            @Override
            public void completed(boolean completed) {
                onMitarbeitersSuchenModelCompleted(completed);
            }
        });
        this.setModelValidationMode(MODEL_VALIDATION_MODE);
    }

    public void setTxtNachname(JTextField txtNachname) {
        this.txtNachname = txtNachname;
        this.txtNachname.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onNachnameEvent();
            }
        });
    }

    private void onNachnameEvent() {
        LOGGER.trace("mitarbeitersSuchenController Event Nachname");
        boolean equalFieldAndModelValue = equalsNullSafe(txtNachname.getText(), mitarbeiterSuchenModel.getNachname());
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
            mitarbeiterSuchenModel.setNachname(txtNachname.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("MitarbeitersSuchenController setModelNachname Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtVorname(JTextField txtVorname) {
        this.txtVorname = txtVorname;
        this.txtVorname.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onVornameEvent();
            }
        });
    }

    private void onVornameEvent() {
        LOGGER.trace("MitarbeitersSuchenController Event Vorname");
        boolean equalFieldAndModelValue = equalsNullSafe(txtVorname.getText(), mitarbeiterSuchenModel.getVorname());
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
            mitarbeiterSuchenModel.setVorname(txtVorname.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("MitarbeitersSuchenController setModelVorname Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setComboBoxMitarbeiterCode(JComboBox<MitarbeiterCode> comboBoxCode) {
        this.comboBoxMitarbeiterCode = comboBoxCode;
        MitarbeiterCode[] selectableMitarbeiterCodes = mitarbeiterSuchenModel.getSelectableMitarbeiterCodes(svmContext.getSvmModel());
        comboBoxCode.setModel(new DefaultComboBoxModel<>(selectableMitarbeiterCodes));
        // Model initialisieren mit erstem ComboBox-Wert
        mitarbeiterSuchenModel.setMitarbeiterCode(selectableMitarbeiterCodes[0]);
        this.comboBoxMitarbeiterCode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onMitarbeiterCodeSelected();
            }
        });
    }

    private void onMitarbeiterCodeSelected() {
        LOGGER.trace("MitarbeiterSuchenController Event MitarbeiterCode selected=" + comboBoxMitarbeiterCode.getSelectedItem());
        setModelMitarbeiterCode();
    }

    private void setModelMitarbeiterCode() {
        mitarbeiterSuchenModel.setMitarbeiterCode((MitarbeiterCode) comboBoxMitarbeiterCode.getSelectedItem());
    }

    public void setErrLblNachname(JLabel errLblNachname) {
        this.errLblNachname = errLblNachname;
    }

    public void setErrLblVorname(JLabel errLblVorname) {
        this.errLblVorname = errLblVorname;
    }

    public void setRadioBtnGroupLehrkraftJaNein(JRadioButton radioBtnLehrkraftJa, JRadioButton radioBtnLehrkraftNein, JRadioButton radioBtnLehrkraftAlle) {
        this.radioBtnLehrkraftJa = radioBtnLehrkraftJa;
        this.radioBtnLehrkraftNein = radioBtnLehrkraftNein;
        this.radioBtnLehrkraftAlle = radioBtnLehrkraftAlle;
        // Action Commands
        this.radioBtnLehrkraftJa.setActionCommand(MitarbeiterSuchenModel.LehrkraftJaNeinSelected.JA.toString());
        this.radioBtnLehrkraftNein.setActionCommand(MitarbeiterSuchenModel.LehrkraftJaNeinSelected.NEIN.toString());
        this.radioBtnLehrkraftAlle.setActionCommand(MitarbeiterSuchenModel.LehrkraftJaNeinSelected.ALLE.toString());
        // Listener
        RadioBtnGroupLehrkraftJaNeinListener radioBtnGroupLehrkraftJaNeinListener = new RadioBtnGroupLehrkraftJaNeinListener();
        this.radioBtnLehrkraftJa.addActionListener(radioBtnGroupLehrkraftJaNeinListener);
        this.radioBtnLehrkraftNein.addActionListener(radioBtnGroupLehrkraftJaNeinListener);
        this.radioBtnLehrkraftAlle.addActionListener(radioBtnGroupLehrkraftJaNeinListener);
        // Initialisieren mit ALLE
        mitarbeiterSuchenModel.setLehrkraftJaNeinSelected(MitarbeiterSuchenModel.LehrkraftJaNeinSelected.ALLE);
    }

    public void setRadioBtnGroupStatus(JRadioButton radioBtnStatusAktiv, JRadioButton radioBtnStatusNichtAktiv, JRadioButton radioBtnStatusAlle) {
        this.radioBtnStatusAktiv = radioBtnStatusAktiv;
        this.radioBtnStatusNichtAktiv = radioBtnStatusNichtAktiv;
        this.radioBtnStatusAlle = radioBtnStatusAlle;
        // Action Commands
        this.radioBtnStatusAktiv.setActionCommand(MitarbeiterSuchenModel.StatusSelected.AKTIV.toString());
        this.radioBtnStatusNichtAktiv.setActionCommand(MitarbeiterSuchenModel.StatusSelected.NICHT_AKTIV.toString());
        this.radioBtnStatusAlle.setActionCommand(MitarbeiterSuchenModel.StatusSelected.ALLE.toString());
        // Listener
        RadioBtnGroupStatusListener radioBtnGroupStatusListener = new RadioBtnGroupStatusListener();
        this.radioBtnStatusAktiv.addActionListener(radioBtnGroupStatusListener);
        this.radioBtnStatusNichtAktiv.addActionListener(radioBtnGroupStatusListener);
        this.radioBtnStatusAlle.addActionListener(radioBtnGroupStatusListener);
        // Initialisieren mit AKTIV
        mitarbeiterSuchenModel.setStatusSelected(MitarbeiterSuchenModel.StatusSelected.AKTIV);
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
        LOGGER.trace("MitarbeitersSuchenPanel Suchen gedrückt");
        if (!validateOnSpeichern()) {
            btnSuchen.setFocusPainted(false);
            return;
        }
        btnSuchen.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        MitarbeitersTableData mitarbeitersTableData = mitarbeiterSuchenModel.suchen();
        MitarbeitersTableModel mitarbeitersTableModel = new MitarbeitersTableModel(mitarbeitersTableData);
        // Auch bei einem Suchresultat Liste anzeigen, da nur von dort gelöscht werden kann
        if (mitarbeitersTableData.size() > 0 || !mitarbeiterSuchenModel.isSuchkriterienSelected()) {
            MitarbeitersPanel mitarbeitersPanel = new MitarbeitersPanel(svmContext, mitarbeitersTableModel);
            mitarbeitersPanel.addCloseListener(closeListener);
            mitarbeitersPanel.addZurueckListener(zurueckListener);
            btnSuchen.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            nextPanelListener.actionPerformed(new ActionEvent(new Object[]{mitarbeitersPanel.$$$getRootComponent$$$(), "Suchresultat"}, ActionEvent.ACTION_PERFORMED, "Suchresultat verfügbar"));
        } else {
            btnSuchen.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            JOptionPane.showMessageDialog(null, "Es wurden keine Mitarbeiter gefunden, welche auf die Suchabfrage passen.", "Keine Mitarbeiter gefunden", JOptionPane.INFORMATION_MESSAGE, svmContext.getDialogIcons().getInformationIcon());
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
        LOGGER.trace("MitarbeitersSuchenPanel Abbrechen gedrückt");
        closeListener.actionPerformed(new ActionEvent(btnAbbrechen, ActionEvent.ACTION_PERFORMED, "Close nach Abbrechen"));
    }

    private void onMitarbeitersSuchenModelCompleted(boolean completed) {
        LOGGER.trace("MitarbeitersSuchenModel completed=" + completed);
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

    public void addZurueckListener(ActionListener zurueckListener) {
        this.zurueckListener = zurueckListener;
    }

    public void addNextPanelListener(ActionListener nextPanelListener) {
        this.nextPanelListener = nextPanelListener;
    }

    @Override
    void doPropertyChange(PropertyChangeEvent evt) {
        if (checkIsFieldChange(Field.NACHNAME, evt)) {
            txtNachname.setText(mitarbeiterSuchenModel.getNachname());
        }
        else if (checkIsFieldChange(Field.VORNAME, evt)) {
            txtVorname.setText(mitarbeiterSuchenModel.getVorname());
        }
        else if (checkIsFieldChange(Field.MITARBEITER_CODE, evt)) {
            comboBoxMitarbeiterCode.setSelectedItem(mitarbeiterSuchenModel.getMitarbeiterCode());
        }
        else if (checkIsFieldChange(Field.LEHRKRAFT_JA_NEIN, evt) && evt.getNewValue() == MitarbeiterSuchenModel.LehrkraftJaNeinSelected.JA) {
            radioBtnLehrkraftJa.setSelected(true);
        }
        else if (checkIsFieldChange(Field.LEHRKRAFT_JA_NEIN, evt) && evt.getNewValue() == MitarbeiterSuchenModel.LehrkraftJaNeinSelected.NEIN) {
            radioBtnLehrkraftNein.setSelected(true);
        }
        else if (checkIsFieldChange(Field.LEHRKRAFT_JA_NEIN, evt) && evt.getNewValue() == MitarbeiterSuchenModel.LehrkraftJaNeinSelected.ALLE) {
            radioBtnLehrkraftAlle.setSelected(true);
        }
        else if (checkIsFieldChange(Field.STATUS, evt) && evt.getNewValue() == MitarbeiterSuchenModel.StatusSelected.AKTIV) {
            radioBtnStatusAktiv.setSelected(true);
        }
        else if (checkIsFieldChange(Field.STATUS, evt) && evt.getNewValue() == MitarbeiterSuchenModel.StatusSelected.NICHT_AKTIV) {
            radioBtnStatusNichtAktiv.setSelected(true);
        }
        else if (checkIsFieldChange(Field.STATUS, evt) && evt.getNewValue() == MitarbeiterSuchenModel.StatusSelected.ALLE) {
            radioBtnStatusAlle.setSelected(true);
        }
    }

    @Override
    void validateFields() throws SvmValidationException {
        if (txtNachname.isEnabled()) {
            LOGGER.trace("Validate field Nachname");
            setModelNachname();
        }
        if (txtVorname.isEnabled()) {
            LOGGER.trace("Validate field Vorname");
            setModelVorname();
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
    }

    @Override
    void showErrMsgAsToolTip(SvmValidationException e) {
        if (e.getAffectedFields().contains(Field.NACHNAME)) {
            txtNachname.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.VORNAME)) {
            txtVorname.setToolTipText(e.getMessage());
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
    }

    @Override
    public void disableFields(boolean disable, Set<Field> fields) {
    }

    class RadioBtnGroupLehrkraftJaNeinListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            LOGGER.trace("MitarbeitersSuchenController LehrkraftJaNein Event");
            mitarbeiterSuchenModel.setLehrkraftJaNeinSelected(MitarbeiterSuchenModel.LehrkraftJaNeinSelected.valueOf(e.getActionCommand()));
        }
    }

    class RadioBtnGroupStatusListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            LOGGER.trace("MitarbeitersSuchenController Status Event");
            mitarbeiterSuchenModel.setStatusSelected(MitarbeiterSuchenModel.StatusSelected.valueOf(e.getActionCommand()));
        }
    }
}
