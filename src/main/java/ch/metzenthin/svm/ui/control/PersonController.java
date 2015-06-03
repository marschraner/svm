package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.dataTypes.Anrede;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.PersonModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;

import static ch.metzenthin.svm.common.utils.Converter.asString;
import static ch.metzenthin.svm.common.utils.SimpleValidator.equalsNullSafe;

/**
 * @author Hans Stamm
 */
public abstract class PersonController extends AbstractController {

    private JComboBox<Anrede> comboBoxAnrede;
    private JTextField txtNachname;
    private JTextField txtVorname;
    private JTextField txtStrasse;
    private JTextField txtHausnummer;
    private JTextField txtPlz;
    private JTextField txtOrt;
    private JTextField txtFestnetz;
    private JTextField txtNatel;
    private JTextField txtEmail;
    private JTextField txtGeburtsdatum;

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

    public void setTxtStrasse(JTextField txtStrasse) {
        this.txtStrasse = txtStrasse;
        this.txtStrasse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onStrasseEvent();
            }
        });
        this.txtStrasse.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onStrasseEvent();
            }
        });
    }

    public void setTxtHausnummer(JTextField txtHausnummer) {
        this.txtHausnummer = txtHausnummer;
        this.txtHausnummer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onHausnummerEvent();
            }
        });
        this.txtHausnummer.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onHausnummerEvent();
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
        try {
            personModel.setAnrede((Anrede) comboBoxAnrede.getSelectedItem());
        } catch (SvmValidationException e) {
            System.out.println("PersonController setModelAnrede Exception=" + e.getMessage());
            // todo $$$ Fehler anzeigen
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
        try {
            personModel.setNachname(txtNachname.getText());
        } catch (SvmRequiredException e) {
            System.out.println("PersonController setModelNachname RequiredException=" + e.getMessage());
            // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            throw e;
        } catch (SvmValidationException e) {
            System.out.println("PersonController setModelNachname Exception=" + e.getMessage());
            // todo $$$ Fehler anzeigen
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
        try {
            personModel.setVorname(txtVorname.getText());
        } catch (SvmRequiredException e) {
            System.out.println("PersonController setModelVorname RequiredException=" + e.getMessage());
            // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            throw e;
        } catch (SvmValidationException e) {
            System.out.println("PersonController setModelVorname Exception=" + e.getMessage());
            // todo $$$ Fehler anzeigen
            throw e;
        }
    }

    private void onStrasseEvent() {
        System.out.println("PersonController Event Strasse");
        boolean equalFieldAndModelValue = equalsNullSafe(txtStrasse.getText(), personModel.getStrasse());
        try {
            setModelStrasse();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            System.out.println("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelStrasse() throws SvmValidationException {
        try {
            personModel.setStrasse(txtStrasse.getText());
        } catch (SvmRequiredException e) {
            System.out.println("PersonController setModelStrasse RequiredException=" + e.getMessage());
            // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            throw e;
        } catch (SvmValidationException e) {
            System.out.println("PersonController setModelStrasse Exception=" + e.getMessage());
            // todo $$$ Fehler anzeigen
            throw e;
        }
    }

    private void onHausnummerEvent() {
        System.out.println("PersonController Event Hausnummer");
        setModelHausnummer();
    }

    private void setModelHausnummer() {
        personModel.setHausnummer(txtHausnummer.getText());
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
        try {
            personModel.setPlz(txtPlz.getText());
        } catch (SvmRequiredException e) {
            System.out.println("PersonController setModelPlz RequiredException=" + e.getMessage());
            // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            throw e;
        } catch (SvmValidationException e) {
            System.out.println("PersonController setModelPlz Exception=" + e.getMessage());
            // todo $$$ Fehler anzeigen
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
        try {
            personModel.setOrt(txtOrt.getText());
        } catch (SvmRequiredException e) {
            System.out.println("PersonController setModelOrt RequiredException=" + e.getMessage());
            // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            throw e;
        } catch (SvmValidationException e) {
            System.out.println("PersonController setModelOrt Exception=" + e.getMessage());
            // todo $$$ Fehler anzeigen
            throw e;
        }
    }

    private void onFestnetzEvent() {
        System.out.println("PersonController Event Festnetz");
        setModelFestnetz();
    }

    private void setModelFestnetz() {
        personModel.setFestnetz(txtFestnetz.getText());
    }

    private void onNatelEvent() {
        System.out.println("PersonController Event Natel");
        setModelNatel();
    }

    private void setModelNatel() {
        personModel.setNatel(txtNatel.getText());
    }

    private void onEmailEvent() {
        System.out.println("PersonController Event Email");
        setModelEmail();
    }

    private void setModelEmail() {
        personModel.setEmail(txtEmail.getText());
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
        try {
            personModel.setGeburtsdatum(txtGeburtsdatum.getText());
        } catch (SvmValidationException e) {
            System.out.println("PersonController setModelGeburtsdatum Exception=" + e.getMessage());
            // todo $$$ Fehler anzeigen
            throw e;
        }
    }

    @Override
    void doPropertyChange(PropertyChangeEvent evt) {
        if ("Nachname".equals(evt.getPropertyName())) {
            txtNachname.setText(personModel.getNachname());
        } else if ("Vorname".equals(evt.getPropertyName())) {
            txtVorname.setText(personModel.getVorname());
        } else if ("Strasse".equals(evt.getPropertyName())) {
            txtStrasse.setText(personModel.getStrasse());
        } else if ("Hausnummer".equals(evt.getPropertyName())) {
            txtHausnummer.setText(personModel.getHausnummer());
        } else if ("Plz".equals(evt.getPropertyName())) {
            txtPlz.setText(personModel.getPlz());
        } else if ("Ort".equals(evt.getPropertyName())) {
            txtOrt.setText(personModel.getOrt());
        } else if ("Geburtsdatum".equals(evt.getPropertyName())) {
            // nicht alle Subklassen von Person haben ein Geburtsdatum
            if (txtGeburtsdatum != null) {
                txtGeburtsdatum.setText(asString(personModel.getGeburtsdatum()));
            }
        } else if ("Festnetz".equals(evt.getPropertyName())) {
            txtFestnetz.setText(personModel.getFestnetz());
        } else if ("Natel".equals(evt.getPropertyName())) {
            txtNatel.setText(personModel.getNatel());
        } else if ("Email".equals(evt.getPropertyName())) {
            txtEmail.setText(personModel.getEmail());
        } else if ("Anrede".equals(evt.getPropertyName())) {
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
        System.out.println("Validate field Strasse");
        setModelStrasse();
        System.out.println("Validate field Hausnummer");
        setModelHausnummer();
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
    void show(SvmValidationException e) {
        // todo $$$
    }

}
