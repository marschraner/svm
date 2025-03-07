package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.datatypes.Anrede;
import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.PersonModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.util.Set;

import static ch.metzenthin.svm.common.utils.Converter.asString;
import static ch.metzenthin.svm.common.utils.SimpleValidator.equalsNullSafe;

/**
 * @author Hans Stamm
 */
@SuppressWarnings("LoggingSimilarMessage")
public abstract class PersonController extends AbstractController {

    private static final Logger LOGGER = LogManager.getLogger(PersonController.class);
    private static final String VALIDIERUNG_WEGEN_EQUAL_FIELD_AND_MODEL_VALUE =
            "Validierung wegen equalFieldAndModelValue";

    private JComboBox<Anrede> comboBoxAnrede;
    protected JTextField txtNachname;
    protected JTextField txtVorname;
    protected JTextField txtStrasseHausnummer;
    protected JTextField txtPlz;
    protected JTextField txtOrt;
    protected JTextField txtFestnetz;
    protected JTextField txtNatel;
    protected JTextField txtEmail;
    private JTextField txtGeburtsdatum;
    private JLabel errLblAnrede;
    private JLabel errLblNachname;
    private JLabel errLblVorname;
    private JLabel errLblStrasseHausnummer;
    private JLabel errLblPlz;
    private JLabel errLblOrt;
    private JLabel errLblFestnetz;
    private JLabel errLblNatel;
    private JLabel errLblEmail;
    private JLabel errLblGeburtsdatum;

    private final PersonModel personModel;
    private final boolean defaultButtonEnabled;

    protected PersonController(PersonModel personModel, boolean defaultButtonEnabled) {
        super(personModel);
        this.personModel = personModel;
        this.defaultButtonEnabled = defaultButtonEnabled;
    }

    public void setComboBoxAnrede(JComboBox<Anrede> comboBoxAnrede) {
        this.comboBoxAnrede = comboBoxAnrede;
        comboBoxAnrede.setModel(new DefaultComboBoxModel<>(Anrede.values()));
        comboBoxAnrede.addActionListener(e -> onAnredeSelected());
        // Anrede: KEINE nicht anzeigen:
        comboBoxAnrede.removeItem(Anrede.KEINE);
        // Leeren ComboBox-Wert anzeigen
        comboBoxAnrede.setSelectedItem(null);
    }

    public void setTxtNachname(JTextField txtNachname) {
        this.txtNachname = txtNachname;
        if (!defaultButtonEnabled) {
            this.txtNachname.addActionListener(e -> onNachnameEvent(true));
        }
        this.txtNachname.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onNachnameEvent(false);
            }
        });
    }

    public void setTxtVorname(JTextField txtVorname) {
        this.txtVorname = txtVorname;
        if (!defaultButtonEnabled) {
            this.txtVorname.addActionListener(e -> onVornameEvent(true));
        }
        this.txtVorname.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onVornameEvent(false);
            }
        });
    }

    public void setTxtStrasseHausnummer(JTextField txtStrasseHausnummer) {
        this.txtStrasseHausnummer = txtStrasseHausnummer;
        if (!defaultButtonEnabled) {
            this.txtStrasseHausnummer.addActionListener(e -> onStrasseHausnummerEvent(true));
        }
        this.txtStrasseHausnummer.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onStrasseHausnummerEvent(false);
            }
        });
    }

    public void setTxtPlz(JTextField txtPlz) {
        this.txtPlz = txtPlz;
        if (!defaultButtonEnabled) {
            this.txtPlz.addActionListener(e -> onPlzEvent(true));
        }
        this.txtPlz.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onPlzEvent(false);
            }
        });
    }

    public void setTxtOrt(JTextField txtOrt) {
        this.txtOrt = txtOrt;
        if (!defaultButtonEnabled) {
            this.txtOrt.addActionListener(e -> onOrtEvent(true));
        }
        this.txtOrt.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onOrtEvent(false);
            }
        });
    }

    public void setTxtFestnetz(JTextField txtFestnetz) {
        this.txtFestnetz = txtFestnetz;
        if (!defaultButtonEnabled) {
            this.txtFestnetz.addActionListener(e -> onFestnetzEvent(true));
        }
        this.txtFestnetz.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onFestnetzEvent(false);
            }
        });
    }

    public void setTxtNatel(JTextField txtNatel) {
        this.txtNatel = txtNatel;
        if (!defaultButtonEnabled) {
            this.txtNatel.addActionListener(e -> onNatelEvent(true));
        }
        this.txtNatel.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onNatelEvent(false);
            }
        });
    }

    public void setTxtEmail(JTextField txtEmail) {
        this.txtEmail = txtEmail;
        if (!defaultButtonEnabled) {
            this.txtEmail.addActionListener(e -> onEmailEvent(true));
        }
        this.txtEmail.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onEmailEvent(false);
            }
        });
    }

    public void setTxtGeburtsdatum(JTextField txtGeburtsdatum) {
        this.txtGeburtsdatum = txtGeburtsdatum;
        if (!defaultButtonEnabled) {
            this.txtGeburtsdatum.addActionListener(e -> onGeburtsdatumEvent(true));
        }
        this.txtGeburtsdatum.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onGeburtsdatumEvent(false);
            }
        });
    }

    private void onAnredeSelected() {
        LOGGER.trace("PersonController Event Anrede selected={}", comboBoxAnrede.getSelectedItem());
        boolean equalFieldAndModelValue = equalsNullSafe(comboBoxAnrede.getSelectedItem(), personModel.getAnrede());
        try {
            setModelAnrede();
        } catch (SvmRequiredException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace(VALIDIERUNG_WEGEN_EQUAL_FIELD_AND_MODEL_VALUE);
            validate();
        }
    }

    private void setModelAnrede() throws SvmRequiredException {
        makeErrorLabelInvisible(Field.ANREDE);
        try {
            personModel.setAnrede((Anrede) comboBoxAnrede.getSelectedItem());
        } catch (SvmRequiredException e) {
            LOGGER.trace("PersonController setModelAnrede RequiredException={}", e.getMessage());
            if (isModelValidationMode()) {
                comboBoxAnrede.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut, nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        }
    }

    private void onNachnameEvent(boolean showRequiredErrMsg) {
        LOGGER.trace("PersonController Event Nachname");
        boolean equalFieldAndModelValue = equalsNullSafe(txtNachname.getText(), personModel.getNachname());
        try {
            setModelNachname(showRequiredErrMsg);
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace(VALIDIERUNG_WEGEN_EQUAL_FIELD_AND_MODEL_VALUE);
            validate();
        }
    }

    private void setModelNachname(boolean showRequiredErrMsg) throws SvmValidationException {
        makeErrorLabelInvisible(Field.NACHNAME);
        try {
            personModel.setNachname(txtNachname.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("PersonController setModelNachname RequiredException={}", e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtNachname.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut, nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("PersonController setModelNachname Exception={}", e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    private void onVornameEvent(boolean showRequiredErrMsg) {
        LOGGER.trace("PersonController Event Vorname");
        boolean equalFieldAndModelValue = equalsNullSafe(txtVorname.getText(), personModel.getVorname());
        try {
            setModelVorname(showRequiredErrMsg);
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace(VALIDIERUNG_WEGEN_EQUAL_FIELD_AND_MODEL_VALUE);
            validate();
        }
    }

    private void setModelVorname(boolean showRequiredErrMsg) throws SvmValidationException {
        makeErrorLabelInvisible(Field.VORNAME);
        try {
            personModel.setVorname(txtVorname.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("PersonController setModelVorname RequiredException={}", e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtVorname.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut, nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("PersonController setModelVorname Exception={}", e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    private void onStrasseHausnummerEvent(boolean showRequiredErrMsg) {
        LOGGER.trace("PersonController Event StrasseHausnummer");
        boolean equalFieldAndModelValue = equalsNullSafe(txtStrasseHausnummer.getText(), personModel.getStrasseHausnummer());
        try {
            setModelStrasseHausnummer(showRequiredErrMsg);
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace(VALIDIERUNG_WEGEN_EQUAL_FIELD_AND_MODEL_VALUE);
            validate();
        }
    }

    private void setModelStrasseHausnummer(boolean showRequiredErrMsg) throws SvmValidationException {
        makeErrorLabelInvisible(Field.STRASSE_HAUSNUMMER);
        try {
            personModel.setStrasseHausnummer(txtStrasseHausnummer.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("PersonController setModelStrasseHausnummer RequiredException={}", e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtStrasseHausnummer.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut, nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("PersonController setModelStrasseHausnummer Exception={}", e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    private void onPlzEvent(boolean showRequiredErrMsg) {
        LOGGER.trace("PersonController Event PLZ");
        boolean equalFieldAndModelValue = equalsNullSafe(txtPlz.getText(), personModel.getPlz());
        try {
            setModelPlz(showRequiredErrMsg);
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace(VALIDIERUNG_WEGEN_EQUAL_FIELD_AND_MODEL_VALUE);
            validate();
        }
    }

    private void setModelPlz(boolean showRequiredErrMsg) throws SvmValidationException {
        makeErrorLabelInvisible(Field.PLZ);
        try {
            personModel.setPlz(txtPlz.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("PersonController setModelPlz RequiredException={}", e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtPlz.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut, nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("PersonController setModelPlz Exception={}", e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    private void onOrtEvent(boolean showRequiredErrMsg) {
        LOGGER.trace("PersonController Event Ort");
        boolean equalFieldAndModelValue = equalsNullSafe(txtOrt.getText(), personModel.getOrt());
        try {
            setModelOrt(showRequiredErrMsg);
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace(VALIDIERUNG_WEGEN_EQUAL_FIELD_AND_MODEL_VALUE);
            validate();
        }
    }

    private void setModelOrt(boolean showRequiredErrMsg) throws SvmValidationException {
        makeErrorLabelInvisible(Field.ORT);
        try {
            personModel.setOrt(txtOrt.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("PersonController setModelOrt RequiredException={}", e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtOrt.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut, nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("PersonController setModelOrt Exception={}", e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    private void onFestnetzEvent(boolean showRequiredErrMsg) {
        LOGGER.trace("PersonController Event Festnetz");
        boolean equalFieldAndModelValue = equalsNullSafe(txtFestnetz.getText(), personModel.getFestnetz());
        try {
            setModelFestnetz(showRequiredErrMsg);
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace(VALIDIERUNG_WEGEN_EQUAL_FIELD_AND_MODEL_VALUE);
            validate();
        }
    }

    private void setModelFestnetz(boolean showRequiredErrMsg) throws SvmValidationException {
        makeErrorLabelInvisible(Field.FESTNETZ);
        try {
            personModel.setFestnetz(txtFestnetz.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("PersonController setModelFestnetz RequiredException={}", e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtFestnetz.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut, nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("PersonController setModelFestnetz Exception={}", e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    private void onNatelEvent(boolean showRequiredErrMsg) {
        LOGGER.trace("PersonController Event Natel");
        boolean equalFieldAndModelValue = equalsNullSafe(txtNatel.getText(), personModel.getNatel());
        try {
            setModelNatel(showRequiredErrMsg);
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace(VALIDIERUNG_WEGEN_EQUAL_FIELD_AND_MODEL_VALUE);
            validate();
        }
    }

    private void setModelNatel(boolean showRequiredErrMsg) throws SvmValidationException {
        makeErrorLabelInvisible(Field.NATEL);
        try {
            personModel.setNatel(txtNatel.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("PersonController setModelNatel RequiredException={}", e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtNatel.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut, nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("PersonController setModelNatel Exception={}", e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    private void onEmailEvent(boolean showRequiredErrMsg) {
        LOGGER.trace("PersonController Event Email");
        boolean equalFieldAndModelValue = equalsNullSafe(txtEmail.getText(), personModel.getEmail());
        try {
            setModelEmail(showRequiredErrMsg);
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace(VALIDIERUNG_WEGEN_EQUAL_FIELD_AND_MODEL_VALUE);
            validate();
        }
    }

    private void setModelEmail(boolean showRequiredErrMsg) throws SvmValidationException {
        makeErrorLabelInvisible(Field.EMAIL);
        try {
            personModel.setEmail(txtEmail.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("PersonController setModelEmail RequiredException={}", e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtEmail.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut, nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("PersonController setModelEmail Exception={}", e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    private void onGeburtsdatumEvent(boolean showRequiredErrMsg) {
        LOGGER.trace("PersonController Event Geburtsdatum");
        boolean equalFieldAndModelValue = equalsNullSafe(txtGeburtsdatum.getText(), personModel.getGeburtsdatum());
        try {
            setModelGeburtsdatum(showRequiredErrMsg);
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace(VALIDIERUNG_WEGEN_EQUAL_FIELD_AND_MODEL_VALUE);
            validate();
        }
    }

    private void setModelGeburtsdatum(boolean showRequiredErrMsg) throws SvmValidationException {
        makeErrorLabelInvisible(Field.GEBURTSDATUM);
        try {
            personModel.setGeburtsdatum(txtGeburtsdatum.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("PersonController setModelGeburtsdatum RequiredException={}", e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtGeburtsdatum.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut, nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("PersonController setModelGeburtsdatum Exception={}", e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setErrLblAnrede(JLabel errLblAnrede) {
        this.errLblAnrede = errLblAnrede;
    }

    public void setErrLblNachname(JLabel errLblNachname) {
        this.errLblNachname = errLblNachname;
    }

    public void setErrLblVorname(JLabel errLblVorname) {
        this.errLblVorname = errLblVorname;
    }

    public void setErrLblStrasseHausnummer(JLabel errLblStrasseHausnummer) {
        this.errLblStrasseHausnummer = errLblStrasseHausnummer;
    }

    public void setErrLblPlz(JLabel errLblPlz) {
        this.errLblPlz = errLblPlz;
    }

    public void setErrLblOrt(JLabel errLblOrt) {
        this.errLblOrt = errLblOrt;
    }

    public void setErrLblFestnetz(JLabel errLblFestnetz) {
        this.errLblFestnetz = errLblFestnetz;
    }

    public void setErrLblNatel(JLabel errLblNatel) {
        this.errLblNatel = errLblNatel;
    }

    public void setErrLblEmail(JLabel errLblEmail) {
        this.errLblEmail = errLblEmail;
    }

    public void setErrLblGeburtsdatum(JLabel errLblGeburtsdatum) {
        this.errLblGeburtsdatum = errLblGeburtsdatum;
    }

    @Override
    void doPropertyChange(PropertyChangeEvent evt) {
        super.doPropertyChange(evt);
        if (checkIsFieldChange(Field.NACHNAME, evt)) {
            txtNachname.setText(personModel.getNachname());
        } else if (checkIsFieldChange(Field.VORNAME, evt)) {
            txtVorname.setText(personModel.getVorname());
        } else if (checkIsFieldChange(Field.STRASSE_HAUSNUMMER, evt)) {
            txtStrasseHausnummer.setText(personModel.getStrasseHausnummer());
        } else if (checkIsFieldChange(Field.PLZ, evt)) {
            txtPlz.setText(personModel.getPlz());
        } else if (checkIsFieldChange(Field.ORT, evt)) {
            txtOrt.setText(personModel.getOrt());
        } else if (checkIsFieldChange(Field.GEBURTSDATUM, evt)) {
            // nicht alle Subklassen von Person haben ein Geburtsdatum
            if (txtGeburtsdatum != null) {
                txtGeburtsdatum.setText(asString(personModel.getGeburtsdatum()));
            }
        } else if (checkIsFieldChange(Field.FESTNETZ, evt)) {
            txtFestnetz.setText(personModel.getFestnetz());
        } else if (checkIsFieldChange(Field.NATEL, evt)) {
            txtNatel.setText(personModel.getNatel());
        } else if (checkIsFieldChange(Field.EMAIL, evt)) {
            txtEmail.setText(personModel.getEmail());
        } else if (checkIsFieldChange(Field.ANREDE, evt) && comboBoxAnrede != null) {
            // nicht alle Subklassen von Person haben eine Anrede
            comboBoxAnrede.setSelectedItem(personModel.getAnrede());
        }
    }

    @Override
    void validateFields() throws SvmValidationException {
        if (comboBoxAnrede != null && comboBoxAnrede.isEnabled()) {
            LOGGER.trace("Validate field Anrede");
            setModelAnrede();
        }
        if (txtNachname.isEnabled()) {
            LOGGER.trace("Validate field Nachname");
            setModelNachname(true);
        }
        if (txtVorname.isEnabled()) {
            LOGGER.trace("Validate field Vorname");
            setModelVorname(true);
        }
        if (txtStrasseHausnummer.isEnabled()) {
            LOGGER.trace("Validate field StrasseHausnummer");
            setModelStrasseHausnummer(true);
        }
        if (txtPlz.isEnabled()) {
            LOGGER.trace("Validate field Plz");
            setModelPlz(true);
        }
        if (txtOrt.isEnabled()) {
            LOGGER.trace("Validate field Ort");
            setModelOrt(true);
        }
        if (txtFestnetz.isEnabled()) {
            LOGGER.trace("Validate field Festnetz");
            setModelFestnetz(true);
        }
        if (txtNatel.isEnabled()) {
            LOGGER.trace("Validate field Natel");
            setModelNatel(true);
        }
        if (txtEmail.isEnabled()) {
            LOGGER.trace("Validate field Email");
            setModelEmail(true);
        }
        if (txtGeburtsdatum != null && txtGeburtsdatum.isEnabled()) {
            LOGGER.trace("Validate field Geburtsdatum");
            setModelGeburtsdatum(true);
        }
    }

    @Override
    void showErrMsg(SvmValidationException e) {
        if (errLblAnrede != null && e.getAffectedFields().contains(Field.ANREDE)) {
            errLblAnrede.setVisible(true);
            errLblAnrede.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.VORNAME)) {
            errLblVorname.setVisible(true);
            errLblVorname.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.NACHNAME)) {
            errLblNachname.setVisible(true);
            errLblNachname.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.STRASSE_HAUSNUMMER)) {
            errLblStrasseHausnummer.setVisible(true);
            errLblStrasseHausnummer.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.PLZ)) {
            errLblPlz.setVisible(true);
            errLblPlz.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ORT)) {
            errLblOrt.setVisible(true);
            errLblOrt.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.FESTNETZ)) {
            errLblFestnetz.setVisible(true);
            errLblFestnetz.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.NATEL)) {
            errLblNatel.setVisible(true);
            errLblNatel.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.EMAIL)) {
            errLblEmail.setVisible(true);
            errLblEmail.setText(e.getMessage());
        }
        if (errLblGeburtsdatum != null && e.getAffectedFields().contains(Field.GEBURTSDATUM)) {
            errLblGeburtsdatum.setVisible(true);
            errLblGeburtsdatum.setText(e.getMessage());
        }
    }

    @Override
    void showErrMsgAsToolTip(SvmValidationException e) {
        if (comboBoxAnrede != null && e.getAffectedFields().contains(Field.ANREDE)) {
            comboBoxAnrede.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.VORNAME)) {
            txtVorname.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.NACHNAME)) {
            txtNachname.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.STRASSE_HAUSNUMMER)) {
            txtStrasseHausnummer.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.PLZ)) {
            txtPlz.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ORT)) {
            txtOrt.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.FESTNETZ)) {
            txtFestnetz.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.NATEL)) {
            txtNatel.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.EMAIL)) {
            txtEmail.setToolTipText(e.getMessage());
        }
        if (errLblGeburtsdatum != null && e.getAffectedFields().contains(Field.GEBURTSDATUM)) {
            txtGeburtsdatum.setToolTipText(e.getMessage());
        }
    }

    @SuppressWarnings("java:S3776")
    @Override
    public void makeErrorLabelsInvisible(Set<Field> fields) {
        if ((fields.contains(Field.ALLE) || fields.contains(Field.ANREDE)) && errLblAnrede != null && comboBoxAnrede != null) {
            errLblAnrede.setVisible(false);
            comboBoxAnrede.setToolTipText(null);
        }
        if ((fields.contains(Field.ALLE) || fields.contains(Field.VORNAME)) && errLblAnrede != null && txtVorname != null) {
            errLblVorname.setVisible(false);
            txtVorname.setToolTipText(null);
        }
        if ((fields.contains(Field.ALLE) || fields.contains(Field.NACHNAME)) && errLblNachname != null && txtNachname != null) {
            errLblNachname.setVisible(false);
            txtNachname.setToolTipText(null);
        }
        if ((fields.contains(Field.ALLE) || fields.contains(Field.STRASSE_HAUSNUMMER)) && errLblStrasseHausnummer != null && txtStrasseHausnummer != null) {
            errLblStrasseHausnummer.setVisible(false);
            txtStrasseHausnummer.setToolTipText(null);
        }
        if ((fields.contains(Field.ALLE) || fields.contains(Field.PLZ)) && errLblPlz != null && txtPlz != null) {
            errLblPlz.setVisible(false);
            txtPlz.setToolTipText(null);
        }
        if ((fields.contains(Field.ALLE) || fields.contains(Field.ORT)) && errLblOrt != null && txtOrt != null) {
            errLblOrt.setVisible(false);
            txtOrt.setToolTipText(null);
        }
        if ((fields.contains(Field.ALLE) || fields.contains(Field.FESTNETZ)) && errLblFestnetz != null && txtFestnetz != null) {
            errLblFestnetz.setVisible(false);
            txtFestnetz.setToolTipText(null);
        }
        if ((fields.contains(Field.ALLE) || fields.contains(Field.NATEL)) && errLblNatel != null && txtNatel != null) {
            errLblNatel.setVisible(false);
            txtNatel.setToolTipText(null);
        }
        if ((fields.contains(Field.ALLE) || fields.contains(Field.EMAIL)) && errLblEmail != null && txtEmail != null) {
            errLblEmail.setVisible(false);
            txtEmail.setToolTipText(null);
        }
        if ((fields.contains(Field.ALLE) || fields.contains(Field.GEBURTSDATUM)) && errLblGeburtsdatum != null && txtGeburtsdatum != null) {
            errLblGeburtsdatum.setVisible(false);
            txtGeburtsdatum.setToolTipText(null);
        }
    }

    @SuppressWarnings("java:S3776")
    @Override
    public void disableFields(boolean disable, Set<Field> fields) {
        if (comboBoxAnrede != null && (fields.contains(Field.ALLE) || fields.contains(Field.ANREDE))) {
            comboBoxAnrede.setEnabled(!disable);
        }
        if ((fields.contains(Field.ALLE) || fields.contains(Field.NACHNAME)) && txtNachname != null) {
            txtNachname.setEnabled(!disable);
        }
        if ((fields.contains(Field.ALLE) || fields.contains(Field.VORNAME)) && txtVorname != null) {
            txtVorname.setEnabled(!disable);
        }
        if ((fields.contains(Field.ALLE) || fields.contains(Field.STRASSE_HAUSNUMMER)) && txtStrasseHausnummer != null) {
            txtStrasseHausnummer.setEnabled(!disable);
        }
        if ((fields.contains(Field.ALLE) || fields.contains(Field.PLZ)) && txtPlz != null) {
            txtPlz.setEnabled(!disable);
        }
        if ((fields.contains(Field.ALLE) || fields.contains(Field.ORT)) && txtOrt != null) {
            txtOrt.setEnabled(!disable);
        }
        if ((fields.contains(Field.ALLE) || fields.contains(Field.FESTNETZ)) && txtFestnetz != null) {
            txtFestnetz.setEnabled(!disable);
        }
        if ((fields.contains(Field.ALLE) || fields.contains(Field.NATEL)) && txtNatel != null) {
            txtNatel.setEnabled(!disable);
        }
        if ((fields.contains(Field.ALLE) || fields.contains(Field.EMAIL)) && txtEmail != null) {
            txtEmail.setEnabled(!disable);
        }
        if ((fields.contains(Field.ALLE) || fields.contains(Field.GEBURTSDATUM)) && txtGeburtsdatum != null) {
            txtGeburtsdatum.setEnabled(!disable);
        }
    }

}
