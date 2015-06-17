package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.dataTypes.Anrede;
import ch.metzenthin.svm.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.PersonModel;

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
        System.out.println("PersonController Event Anrede selected=" + comboBoxAnrede.getSelectedItem());
        boolean equalFieldAndModelValue = equalsNullSafe(comboBoxAnrede.getSelectedItem(), personModel.getAnrede());
        try {
            setModelAnrede();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            System.out.println("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelAnrede() throws SvmValidationException {
        makeErrorLabelInvisible(Field.ANREDE);
        try {
            personModel.setAnrede((Anrede) comboBoxAnrede.getSelectedItem());
        } catch (SvmValidationException e) {
            System.out.println("PersonController setModelAnrede Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    private void onNachnameEvent() {
        System.out.println("PersonController Event Nachname");
        boolean equalFieldAndModelValue = equalsNullSafe(txtNachname.getText(), personModel.getNachname());
        try {
            setModelNachname();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            System.out.println("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelNachname() throws SvmValidationException {
        makeErrorLabelInvisible(Field.NACHNAME);
        try {
            personModel.setNachname(txtNachname.getText());
        } catch (SvmRequiredException e) {
            System.out.println("PersonController setModelNachname RequiredException=" + e.getMessage());
            // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            throw e;
        } catch (SvmValidationException e) {
            System.out.println("PersonController setModelNachname Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    private void onVornameEvent() {
        System.out.println("PersonController Event Vorname");
        boolean equalFieldAndModelValue = equalsNullSafe(txtVorname.getText(), personModel.getVorname());
        try {
            setModelVorname();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            System.out.println("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelVorname() throws SvmValidationException {
        makeErrorLabelInvisible(Field.VORNAME);
        try {
            personModel.setVorname(txtVorname.getText());
        } catch (SvmRequiredException e) {
            System.out.println("PersonController setModelVorname RequiredException=" + e.getMessage());
            // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            throw e;
        } catch (SvmValidationException e) {
            System.out.println("PersonController setModelVorname Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    private void onStrasseHausnummerEvent() {
        System.out.println("PersonController Event StrasseHausnummer");
        // Substitution hier, damit neuer Wert auf jeden Fall im Textfeld angezeigt wird!
        txtStrasseHausnummer.setText(replaceStrByStrasse(txtStrasseHausnummer.getText()));
        boolean equalFieldAndModelValue = equalsNullSafe(txtStrasseHausnummer.getText(), personModel.getStrasseHausnummer());
        try {
            setModelStrasseHausnummer();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            System.out.println("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private String replaceStrByStrasse(String strasse) {
        if (strasse == null) {
            return null;
        }
        return strasse.replace("str.", "strasse");
    }

    private void setModelStrasseHausnummer() throws SvmValidationException {
        makeErrorLabelInvisible(Field.STRASSE_HAUSNUMMER);
        try {
            personModel.setStrasseHausnummer(txtStrasseHausnummer.getText());
        } catch (SvmRequiredException e) {
            System.out.println("PersonController setModelStrasseHausnummer RequiredException=" + e.getMessage());
            // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            throw e;
        } catch (SvmValidationException e) {
            System.out.println("PersonController setModelStrasseHausnummer Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    private void onPlzEvent() {
        System.out.println("PersonController Event PLZ");
        boolean equalFieldAndModelValue = equalsNullSafe(txtPlz.getText(), personModel.getPlz());
        try {
            setModelPlz();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            System.out.println("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelPlz() throws SvmValidationException {
        makeErrorLabelInvisible(Field.PLZ);
        try {
            personModel.setPlz(txtPlz.getText());
        } catch (SvmRequiredException e) {
            System.out.println("PersonController setModelPlz RequiredException=" + e.getMessage());
            // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            throw e;
        } catch (SvmValidationException e) {
            System.out.println("PersonController setModelPlz Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    private void onOrtEvent() {
        System.out.println("PersonController Event Ort");
        boolean equalFieldAndModelValue = equalsNullSafe(txtOrt.getText(), personModel.getOrt());
        try {
            setModelOrt();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            System.out.println("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelOrt() throws SvmValidationException {
        makeErrorLabelInvisible(Field.ORT);
        try {
            personModel.setOrt(txtOrt.getText());
        } catch (SvmRequiredException e) {
            System.out.println("PersonController setModelOrt RequiredException=" + e.getMessage());
            // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            throw e;
        } catch (SvmValidationException e) {
            System.out.println("PersonController setModelOrt Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    private void onFestnetzEvent() {
        System.out.println("PersonController Event Festnetz");
        boolean equalFieldAndModelValue = equalsNullSafe(txtFestnetz.getText(), personModel.getFestnetz());
        try {
            setModelFestnetz();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            System.out.println("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelFestnetz() throws SvmValidationException {
        makeErrorLabelInvisible(Field.FESTNETZ);
        try {
            personModel.setFestnetz(txtFestnetz.getText());
        } catch (SvmValidationException e) {
            System.out.println("PersonController setModelFestnetz Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    private void onNatelEvent() {
        System.out.println("PersonController Event Natel");
        boolean equalFieldAndModelValue = equalsNullSafe(txtNatel.getText(), personModel.getNatel());
        try {
            setModelNatel();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            System.out.println("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelNatel() throws SvmValidationException {
        makeErrorLabelInvisible(Field.NATEL);
        try {
            personModel.setNatel(txtNatel.getText());
        } catch (SvmValidationException e) {
            System.out.println("PersonController setModelNatel Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    private void onEmailEvent() {
        System.out.println("PersonController Event Email");
        boolean equalFieldAndModelValue = equalsNullSafe(txtEmail.getText(), personModel.getEmail());
        try {
            setModelEmail();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            System.out.println("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelEmail() throws SvmValidationException {
        makeErrorLabelInvisible(Field.EMAIL);
        try {
            personModel.setEmail(txtEmail.getText());
        } catch (SvmValidationException e) {
            System.out.println("PersonController setModelEmail Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    private void onGeburtsdatumEvent() {
        System.out.println("PersonController Event Geburtsdatum");
        boolean equalFieldAndModelValue = equalsNullSafe(txtGeburtsdatum.getText(), personModel.getGeburtsdatum());
        try {
            setModelGeburtsdatum();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            System.out.println("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelGeburtsdatum() throws SvmValidationException {
        makeErrorLabelInvisible(Field.GEBURTSDATUM);
        try {
            personModel.setGeburtsdatum(txtGeburtsdatum.getText());
        } catch (SvmRequiredException e) {
            System.out.println("PersonController setModelGeburtsdatum RequiredException=" + e.getMessage());
            // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            throw e;
        } catch (SvmValidationException e) {
            System.out.println("PersonController setModelGeburtsdatum Exception=" + e.getMessage());
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
        if (comboBoxAnrede != null) {
            System.out.println("Validate field Anrede");
            setModelAnrede();
        }
        System.out.println("Validate field Nachname");
        setModelNachname();
        System.out.println("Validate field Vorname");
        setModelVorname();
        System.out.println("Validate field StrasseHausnummer");
        setModelStrasseHausnummer();
        System.out.println("Validate field Plz");
        setModelPlz();
        System.out.println("Validate field Ort");
        setModelOrt();
        if (txtGeburtsdatum != null) {
            System.out.println("Validate field Geburtsdatum");
            setModelGeburtsdatum();
        }
        System.out.println("Validate field Festnetz");
        setModelFestnetz();
        System.out.println("Validate field Natel");
        setModelNatel();
        System.out.println("Validate field Email");
        setModelEmail();
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
    public void makeErrorLabelsInvisible(Set<Field> fields) {
        if (fields.contains(Field.ANREDE) && errLblAnrede != null) {
            errLblAnrede.setVisible(false);
        }
        if (fields.contains(Field.VORNAME)) {
            errLblVorname.setVisible(false);
        }
        if (fields.contains(Field.NACHNAME)) {
            errLblNachname.setVisible(false);
        }
        if (fields.contains(Field.STRASSE_HAUSNUMMER)) {
            errLblStrasseHausnummer.setVisible(false);
        }
        if (fields.contains(Field.PLZ)) {
            errLblPlz.setVisible(false);
        }
        if (fields.contains(Field.ORT)) {
            errLblOrt.setVisible(false);
        }
        if (fields.contains(Field.FESTNETZ)) {
            errLblFestnetz.setVisible(false);
        }
        if (fields.contains(Field.NATEL)) {
            errLblNatel.setVisible(false);
        }
        if (fields.contains(Field.EMAIL)) {
            errLblEmail.setVisible(false);
        }
        if (fields.contains(Field.GEBURTSDATUM) && errLblGeburtsdatum != null) {
            errLblGeburtsdatum.setVisible(false);
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
