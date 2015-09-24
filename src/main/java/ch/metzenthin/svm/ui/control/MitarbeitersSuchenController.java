package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.CompletedListener;
import ch.metzenthin.svm.domain.model.MitarbeitersSuchenModel;
import ch.metzenthin.svm.domain.model.MitarbeitersTableData;
import ch.metzenthin.svm.ui.componentmodel.MitarbeitersTableModel;
import ch.metzenthin.svm.ui.components.MitarbeitersPanel;
import org.apache.log4j.Logger;

import javax.swing.*;
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
public class MitarbeitersSuchenController extends AbstractController {

    private static final Logger LOGGER = Logger.getLogger(MitarbeitersSuchenController.class);

    // Möglichkeit zum Umschalten des validation modes (nicht dynamisch)
    private static final boolean MODEL_VALIDATION_MODE = false;

    private final SvmContext svmContext;
    private MitarbeitersSuchenModel mitarbeitersSuchenModel;
    private JTextField txtNachname;
    private JTextField txtVorname;
    private JTextField txtMitarbeiterCodes;
    private JLabel errLblNachname;
    private JLabel errLblVorname;
    private JLabel errLblMitarbeiterCodes;
    private JRadioButton radioBtnLehrkraftJa;
    private JRadioButton radioBtnLehrkraftNein;
    private JRadioButton radioBtnLehrkraftAlle;
    private JRadioButton radioBtnAktivJa;
    private JRadioButton radioBtnAktivNein;
    private JRadioButton radioBtnAktivAlle;
    private JButton btnSuchen;
    private JButton btnAbbrechen;
    private ActionListener closeListener;
    private ActionListener nextPanelListener;
    private ActionListener zurueckListener;

    public MitarbeitersSuchenController(SvmContext svmContext, MitarbeitersSuchenModel mitarbeitersSuchenModel) {
        super(mitarbeitersSuchenModel);
        this.svmContext = svmContext;
        this.mitarbeitersSuchenModel = mitarbeitersSuchenModel;
        this.mitarbeitersSuchenModel.addPropertyChangeListener(this);
        this.mitarbeitersSuchenModel.addDisableFieldsListener(this);
        this.mitarbeitersSuchenModel.addMakeErrorLabelsInvisibleListener(this);
        this.mitarbeitersSuchenModel.addCompletedListener(new CompletedListener() {
            @Override
            public void completed(boolean completed) {
                onMitarbeitersSuchenModelCompleted(completed);
            }
        });
        this.setModelValidationMode(MODEL_VALIDATION_MODE);
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
        LOGGER.trace("mitarbeitersSuchenController Event Nachname");
        boolean equalFieldAndModelValue = equalsNullSafe(txtNachname.getText(), mitarbeitersSuchenModel.getNachname());
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
            mitarbeitersSuchenModel.setNachname(txtNachname.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("MitarbeitersSuchenController setModelNachname Exception=" + e.getMessage());
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
        LOGGER.trace("MitarbeitersSuchenController Event Vorname");
        boolean equalFieldAndModelValue = equalsNullSafe(txtVorname.getText(), mitarbeitersSuchenModel.getVorname());
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
            mitarbeitersSuchenModel.setVorname(txtVorname.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("MitarbeitersSuchenController setModelVorname Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtMitarbeiterCodes(JTextField txtMitarbeiterCodes) {
        this.txtMitarbeiterCodes = txtMitarbeiterCodes;
        this.txtMitarbeiterCodes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onMitarbeiterCodesEvent();
            }
        });
        this.txtMitarbeiterCodes.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onMitarbeiterCodesEvent();
            }
        });
    }

    private void onMitarbeiterCodesEvent() {
        LOGGER.trace("MitarbeitersSuchenController Event MitarbeiterCodes");
        boolean equalFieldAndModelValue = equalsNullSafe(txtMitarbeiterCodes.getText(), mitarbeitersSuchenModel.getMitarbeiterCodes());
        try {
            setModelMitarbeiterCodes();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelMitarbeiterCodes() throws SvmValidationException {
        makeErrorLabelInvisible(Field.MITARBEITER_CODES);
        try {
            mitarbeitersSuchenModel.setMitarbeiterCodes(txtMitarbeiterCodes.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("MitarbeitersSuchenController setModelMitarbeiterCodes Exception=" + e.getMessage());
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

    public void setErrLblMitarbeiterCodes(JLabel errLblMitarbeiterCodes) {
        this.errLblMitarbeiterCodes = errLblMitarbeiterCodes;
    }

    public void setRadioBtnGroupLehrkraftJaNein(JRadioButton radioBtnLehrkraftJa, JRadioButton radioBtnLehrkraftNein, JRadioButton radioBtnLehrkraftAlle) {
        this.radioBtnLehrkraftJa = radioBtnLehrkraftJa;
        this.radioBtnLehrkraftNein = radioBtnLehrkraftNein;
        this.radioBtnLehrkraftAlle = radioBtnLehrkraftAlle;
        // Action Commands
        this.radioBtnLehrkraftJa.setActionCommand(MitarbeitersSuchenModel.LehrkraftJaNeinSelected.JA.toString());
        this.radioBtnLehrkraftNein.setActionCommand(MitarbeitersSuchenModel.LehrkraftJaNeinSelected.NEIN.toString());
        this.radioBtnLehrkraftAlle.setActionCommand(MitarbeitersSuchenModel.LehrkraftJaNeinSelected.ALLE.toString());
        // Listener
        RadioBtnGroupLehrkraftJaNeinListener radioBtnGroupLehrkraftJaNeinListener = new RadioBtnGroupLehrkraftJaNeinListener();
        this.radioBtnLehrkraftJa.addActionListener(radioBtnGroupLehrkraftJaNeinListener);
        this.radioBtnLehrkraftNein.addActionListener(radioBtnGroupLehrkraftJaNeinListener);
        this.radioBtnLehrkraftAlle.addActionListener(radioBtnGroupLehrkraftJaNeinListener);
        // Initialisieren mit ALLE
        mitarbeitersSuchenModel.setLehrkraftJaNeinSelected(MitarbeitersSuchenModel.LehrkraftJaNeinSelected.ALLE);
    }

    public void setRadioBtnGroupAktivJaNein(JRadioButton radioBtnAktivJa, JRadioButton radioBtnAktivNein, JRadioButton radioBtnAktivAlle) {
        this.radioBtnAktivJa = radioBtnAktivJa;
        this.radioBtnAktivNein = radioBtnAktivNein;
        this.radioBtnAktivAlle = radioBtnAktivAlle;
        // Action Commands
        this.radioBtnAktivJa.setActionCommand(MitarbeitersSuchenModel.AktivJaNeinSelected.JA.toString());
        this.radioBtnAktivNein.setActionCommand(MitarbeitersSuchenModel.AktivJaNeinSelected.NEIN.toString());
        this.radioBtnAktivAlle.setActionCommand(MitarbeitersSuchenModel.AktivJaNeinSelected.ALLE.toString());
        // Listener
        RadioBtnGroupAktivJaNeinListener radioBtnGroupAktivJaNeinListener = new RadioBtnGroupAktivJaNeinListener();
        this.radioBtnAktivJa.addActionListener(radioBtnGroupAktivJaNeinListener);
        this.radioBtnAktivNein.addActionListener(radioBtnGroupAktivJaNeinListener);
        this.radioBtnAktivAlle.addActionListener(radioBtnGroupAktivJaNeinListener);
        // Initialisieren mit JA
        mitarbeitersSuchenModel.setAktivJaNeinSelected(MitarbeitersSuchenModel.AktivJaNeinSelected.JA);
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
        String errMsg = mitarbeitersSuchenModel.checkIfCodeKuerzelsExist(svmContext.getSvmModel());
        if (!errMsg.isEmpty()) {
            JOptionPane.showMessageDialog(null, errMsg, "Fehler", JOptionPane.ERROR_MESSAGE, svmContext.getDialogIcons().getErrorIcon());
            btnSuchen.setFocusPainted(false);
            return;
        }
        MitarbeitersTableData mitarbeitersTableData = mitarbeitersSuchenModel.suchen();
        MitarbeitersTableModel mitarbeitersTableModel = new MitarbeitersTableModel(mitarbeitersTableData);
        // Auch bei einem Suchresultat Liste anzeigen, da nur von dort gelöscht werden kann
        if (mitarbeitersTableData.size() > 0) {
            MitarbeitersPanel mitarbeitersPanel = new MitarbeitersPanel(svmContext, mitarbeitersTableModel);
            mitarbeitersPanel.addCloseListener(closeListener);
            mitarbeitersPanel.addZurueckListener(zurueckListener);
            nextPanelListener.actionPerformed(new ActionEvent(new Object[]{mitarbeitersPanel.$$$getRootComponent$$$(), "Suchresultat"}, ActionEvent.ACTION_PERFORMED, "Suchresultat verfügbar"));
        } else {
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
            txtNachname.setText(mitarbeitersSuchenModel.getNachname());
        }
        else if (checkIsFieldChange(Field.VORNAME, evt)) {
            txtVorname.setText(mitarbeitersSuchenModel.getVorname());
        }
        else if (checkIsFieldChange(Field.MITARBEITER_CODES, evt)) {
            txtMitarbeiterCodes.setText(mitarbeitersSuchenModel.getMitarbeiterCodes());
        }
        else if (checkIsFieldChange(Field.LEHRKRAFT_JA_NEIN, evt) && evt.getNewValue() == MitarbeitersSuchenModel.LehrkraftJaNeinSelected.JA) {
            radioBtnLehrkraftJa.setSelected(true);
        }
        else if (checkIsFieldChange(Field.LEHRKRAFT_JA_NEIN, evt) && evt.getNewValue() == MitarbeitersSuchenModel.LehrkraftJaNeinSelected.NEIN) {
            radioBtnLehrkraftNein.setSelected(true);
        }
        else if (checkIsFieldChange(Field.LEHRKRAFT_JA_NEIN, evt) && evt.getNewValue() == MitarbeitersSuchenModel.LehrkraftJaNeinSelected.ALLE) {
            radioBtnLehrkraftAlle.setSelected(true);
        }
        else if (checkIsFieldChange(Field.AKTIV_JA_NEIN, evt) && evt.getNewValue() == MitarbeitersSuchenModel.AktivJaNeinSelected.JA) {
            radioBtnAktivJa.setSelected(true);
        }
        else if (checkIsFieldChange(Field.AKTIV_JA_NEIN, evt) && evt.getNewValue() == MitarbeitersSuchenModel.AktivJaNeinSelected.NEIN) {
            radioBtnAktivNein.setSelected(true);
        }
        else if (checkIsFieldChange(Field.AKTIV_JA_NEIN, evt) && evt.getNewValue() == MitarbeitersSuchenModel.AktivJaNeinSelected.ALLE) {
            radioBtnAktivAlle.setSelected(true);
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
        if (txtMitarbeiterCodes.isEnabled()) {
            LOGGER.trace("Validate field MitarbeiterCodes");
            setModelMitarbeiterCodes();
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
        if (e.getAffectedFields().contains(Field.MITARBEITER_CODES)) {
            errLblMitarbeiterCodes.setVisible(true);
            errLblMitarbeiterCodes.setText(e.getMessage());
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
        if (e.getAffectedFields().contains(Field.MITARBEITER_CODES)) {
            txtMitarbeiterCodes.setToolTipText(e.getMessage());
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
        if (fields.contains(Field.ALLE) || fields.contains(Field.MITARBEITER_CODES)) {
            if (errLblMitarbeiterCodes != null) {
                errLblMitarbeiterCodes.setVisible(false);
            }
            if (txtMitarbeiterCodes != null) {
                txtMitarbeiterCodes.setToolTipText(null);
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
            mitarbeitersSuchenModel.setLehrkraftJaNeinSelected(MitarbeitersSuchenModel.LehrkraftJaNeinSelected.valueOf(e.getActionCommand()));
        }
    }

    class RadioBtnGroupAktivJaNeinListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            LOGGER.trace("MitarbeitersSuchenController AktivJaNein Event");
            mitarbeitersSuchenModel.setAktivJaNeinSelected(MitarbeitersSuchenModel.AktivJaNeinSelected.valueOf(e.getActionCommand()));
        }
    }
}
