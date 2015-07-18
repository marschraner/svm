package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.dataTypes.Anrede;
import ch.metzenthin.svm.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.PersonModel;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.util.Set;

import static ch.metzenthin.svm.common.utils.Converter.asString;
import static ch.metzenthin.svm.common.utils.SimpleValidator.equalsNullSafe;

/**
 * @author Hans Stamm
 */
public abstract class PersonController extends AbstractController {

    private static final Logger LOGGER = Logger.getLogger(PersonController.class);

    private JComboBox<Anrede> comboBoxAnrede;
    private JTextField txtNachname;
    private JTextField txtVorname;
    private JTextField txtStrasseHausnummer;
    private JTextField txtPlz;
    private JTextField txtOrt;
    private JTextField txtFestnetz;
    private JTextField txtNatel;
    private JTextField txtEmail;
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

    private PersonModel personModel;

    public PersonController(PersonModel personModel) {
        super(personModel);
        this.personModel = personModel;
    }

    public void setComboBoxAnrede(JComboBox<Anrede> comboBoxAnrede) {
        this.comboBoxAnrede = comboBoxAnrede;
        comboBoxAnrede.setModel(new DefaultComboBoxModel<>(Anrede.values()));
        comboBoxAnrede.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onAnredeSelected();
            }
        });
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

    public void setTxtStrasseHausnummer(JTextField txtStrasseHausnummer) {
        this.txtStrasseHausnummer = txtStrasseHausnummer;
        this.txtStrasseHausnummer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onStrasseHausnummerEvent();
            }
        });
        this.txtStrasseHausnummer.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onStrasseHausnummerEvent();
            }
        });
    }

    public void setTxtPlz(JTextField txtPlz) {
        this.txtPlz = txtPlz;
        this.txtPlz.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onPlzEvent();
            }
        });
        this.txtPlz.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onPlzEvent();
            }
        });
    }

    public void setTxtOrt(JTextField txtOrt) {
        this.txtOrt = txtOrt;
        this.txtOrt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onOrtEvent();
            }
        });
        this.txtOrt.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onOrtEvent();
            }
        });
    }

    public void setTxtFestnetz(JTextField txtFestnetz) {
        this.txtFestnetz = txtFestnetz;
        this.txtFestnetz.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onFestnetzEvent();
            }
        });
        this.txtFestnetz.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onFestnetzEvent();
            }
        });
    }

    public void setTxtNatel(JTextField txtNatel) {
        this.txtNatel = txtNatel;
        this.txtNatel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onNatelEvent();
            }
        });
        this.txtNatel.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onNatelEvent();
            }
        });
    }

    public void setTxtEmail(JTextField txtEmail) {
        this.txtEmail = txtEmail;
        this.txtEmail.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onEmailEvent();
            }
        });
        this.txtEmail.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onEmailEvent();
            }
        });
    }

    public void setTxtGeburtsdatum(JTextField txtGeburtsdatum) {
        this.txtGeburtsdatum = txtGeburtsdatum;
        this.txtGeburtsdatum.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onGeburtsdatumEvent();
            }
        });
        this.txtGeburtsdatum.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onGeburtsdatumEvent();
            }
        });
    }

    private void onAnredeSelected() {
        LOGGER.trace("PersonController Event Anrede selected=" + comboBoxAnrede.getSelectedItem());
        boolean equalFieldAndModelValue = equalsNullSafe(comboBoxAnrede.getSelectedItem(), personModel.getAnrede());
        try {
            setModelAnrede();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelAnrede() throws SvmValidationException {
        makeErrorLabelInvisible(Field.ANREDE);
        try {
            personModel.setAnrede((Anrede) comboBoxAnrede.getSelectedItem());
        } catch (SvmValidationException e) {
            LOGGER.trace("PersonController setModelAnrede Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    private void onNachnameEvent() {
        LOGGER.trace("PersonController Event Nachname");
        boolean equalFieldAndModelValue = equalsNullSafe(txtNachname.getText(), personModel.getNachname());
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
            personModel.setNachname(txtNachname.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("PersonController setModelNachname RequiredException=" + e.getMessage());
            if (isModelValidationMode()) {
                txtNachname.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("PersonController setModelNachname Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    private void onVornameEvent() {
        LOGGER.trace("PersonController Event Vorname");
        boolean equalFieldAndModelValue = equalsNullSafe(txtVorname.getText(), personModel.getVorname());
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
            personModel.setVorname(txtVorname.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("PersonController setModelVorname RequiredException=" + e.getMessage());
            if (isModelValidationMode()) {
                txtVorname.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("PersonController setModelVorname Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    private void onStrasseHausnummerEvent() {
        LOGGER.trace("PersonController Event StrasseHausnummer");
        boolean equalFieldAndModelValue = equalsNullSafe(txtStrasseHausnummer.getText(), personModel.getStrasseHausnummer());
        try {
            setModelStrasseHausnummer();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelStrasseHausnummer() throws SvmValidationException {
        makeErrorLabelInvisible(Field.STRASSE_HAUSNUMMER);
        try {
            personModel.setStrasseHausnummer(txtStrasseHausnummer.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("PersonController setModelStrasseHausnummer RequiredException=" + e.getMessage());
            if (isModelValidationMode()) {
                txtStrasseHausnummer.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("PersonController setModelStrasseHausnummer Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    private void onPlzEvent() {
        LOGGER.trace("PersonController Event PLZ");
        boolean equalFieldAndModelValue = equalsNullSafe(txtPlz.getText(), personModel.getPlz());
        try {
            setModelPlz();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelPlz() throws SvmValidationException {
        makeErrorLabelInvisible(Field.PLZ);
        try {
            personModel.setPlz(txtPlz.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("PersonController setModelPlz RequiredException=" + e.getMessage());
            if (isModelValidationMode()) {
                txtPlz.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("PersonController setModelPlz Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    private void onOrtEvent() {
        LOGGER.trace("PersonController Event Ort");
        boolean equalFieldAndModelValue = equalsNullSafe(txtOrt.getText(), personModel.getOrt());
        try {
            setModelOrt();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelOrt() throws SvmValidationException {
        makeErrorLabelInvisible(Field.ORT);
        try {
            personModel.setOrt(txtOrt.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("PersonController setModelOrt RequiredException=" + e.getMessage());
            if (isModelValidationMode()) {
                txtOrt.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("PersonController setModelOrt Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    private void onFestnetzEvent() {
        LOGGER.trace("PersonController Event Festnetz");
        boolean equalFieldAndModelValue = equalsNullSafe(txtFestnetz.getText(), personModel.getFestnetz());
        try {
            setModelFestnetz();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelFestnetz() throws SvmValidationException {
        makeErrorLabelInvisible(Field.FESTNETZ);
        try {
            personModel.setFestnetz(txtFestnetz.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("PersonController setModelFestnetz Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    private void onNatelEvent() {
        LOGGER.trace("PersonController Event Natel");
        boolean equalFieldAndModelValue = equalsNullSafe(txtNatel.getText(), personModel.getNatel());
        try {
            setModelNatel();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelNatel() throws SvmValidationException {
        makeErrorLabelInvisible(Field.NATEL);
        try {
            personModel.setNatel(txtNatel.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("PersonController setModelNatel Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    private void onEmailEvent() {
        LOGGER.trace("PersonController Event Email");
        boolean equalFieldAndModelValue = equalsNullSafe(txtEmail.getText(), personModel.getEmail());
        try {
            setModelEmail();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelEmail() throws SvmValidationException {
        makeErrorLabelInvisible(Field.EMAIL);
        try {
            personModel.setEmail(txtEmail.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("PersonController setModelEmail Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    private void onGeburtsdatumEvent() {
        LOGGER.trace("PersonController Event Geburtsdatum");
        boolean equalFieldAndModelValue = equalsNullSafe(txtGeburtsdatum.getText(), personModel.getGeburtsdatum());
        try {
            setModelGeburtsdatum();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelGeburtsdatum() throws SvmValidationException {
        makeErrorLabelInvisible(Field.GEBURTSDATUM);
        try {
            personModel.setGeburtsdatum(txtGeburtsdatum.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("PersonController setModelGeburtsdatum RequiredException=" + e.getMessage());
            if (isModelValidationMode()) {
                txtGeburtsdatum.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("PersonController setModelGeburtsdatum Exception=" + e.getMessage());
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
        } else if (checkIsFieldChange(Field.ANREDE, evt)) {
            // nicht alle Subklassen von Person haben eine Anrede
            if (comboBoxAnrede != null) {
                comboBoxAnrede.setSelectedItem(personModel.getAnrede());
            }
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
            setModelNachname();
        }
        if (txtVorname.isEnabled()) {
            LOGGER.trace("Validate field Vorname");
            setModelVorname();
        }
        if (txtStrasseHausnummer.isEnabled()) {
            LOGGER.trace("Validate field StrasseHausnummer");
            setModelStrasseHausnummer();
        }
        if (txtPlz.isEnabled()) {
            LOGGER.trace("Validate field Plz");
            setModelPlz();
        }
        if (txtOrt.isEnabled()) {
            LOGGER.trace("Validate field Ort");
            setModelOrt();
        }
        if (txtGeburtsdatum != null && txtGeburtsdatum.isEnabled()) {
            LOGGER.trace("Validate field Geburtsdatum");
            setModelGeburtsdatum();
        }
        if (txtFestnetz.isEnabled()) {
            LOGGER.trace("Validate field Festnetz");
            setModelFestnetz();
        }
        if (txtNatel.isEnabled()) {
            LOGGER.trace("Validate field Natel");
            setModelNatel();
        }
        if (txtEmail.isEnabled()) {
            LOGGER.trace("Validate field Email");
            setModelEmail();
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

    @Override
    public void makeErrorLabelsInvisible(Set<Field> fields) {
        if ((fields.contains(Field.ALLE) || fields.contains(Field.ANREDE)) && errLblAnrede != null) {
            errLblAnrede.setVisible(false);
            comboBoxAnrede.setToolTipText(null);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.VORNAME)) {
            errLblVorname.setVisible(false);
            txtVorname.setToolTipText(null);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.NACHNAME)) {
            errLblNachname.setVisible(false);
            txtNachname.setToolTipText(null);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.STRASSE_HAUSNUMMER)) {
            errLblStrasseHausnummer.setVisible(false);
            txtStrasseHausnummer.setToolTipText(null);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.PLZ)) {
            errLblPlz.setVisible(false);
            txtPlz.setToolTipText(null);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.ORT)) {
            errLblOrt.setVisible(false);
            txtOrt.setToolTipText(null);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.FESTNETZ)) {
            errLblFestnetz.setVisible(false);
            txtFestnetz.setToolTipText(null);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.NATEL)) {
            errLblNatel.setVisible(false);
            txtNatel.setToolTipText(null);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.EMAIL)) {
            errLblEmail.setVisible(false);
            txtEmail.setToolTipText(null);
        }
        if ((fields.contains(Field.ALLE) || fields.contains(Field.GEBURTSDATUM)) && errLblGeburtsdatum != null) {
            errLblGeburtsdatum.setVisible(false);
            txtGeburtsdatum.setToolTipText(null);
        }
    }

    @Override
    public void disableFields(boolean disable, Set<Field> fields) {
        if (fields.contains(Field.ALLE) || fields.contains(Field.ANREDE)) {
            comboBoxAnrede.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.NACHNAME)) {
            txtNachname.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.VORNAME)) {
            txtVorname.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.STRASSE_HAUSNUMMER)) {
            txtStrasseHausnummer.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.PLZ)) {
            txtPlz.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.ORT)) {
            txtOrt.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.FESTNETZ)) {
            txtFestnetz.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.NATEL)) {
            txtNatel.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.EMAIL)) {
            txtEmail.setEnabled(!disable);
        }
        if ((fields.contains(Field.ALLE) || fields.contains(Field.GEBURTSDATUM)) && txtGeburtsdatum != null) {
            txtGeburtsdatum.setEnabled(!disable);
        }
    }

}
