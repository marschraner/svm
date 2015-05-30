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
        if (setModelAnrede() && equalFieldAndModelValue) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            System.out.println("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private boolean setModelAnrede() {
        try {
            personModel.setAnrede((Anrede) comboBoxAnrede.getSelectedItem());
        } catch (SvmValidationException e) {
            System.out.println("PersonController setModelAnrede Exception=" + e.getMessage());
            // todo $$$ Fehler anzeigen
            return false;
        }
        return true;
    }

    private void onNachnameEvent() {
        System.out.println("PersonController Event Nachname");
        boolean equalFieldAndModelValue = equalsNullSafe(txtNachname.getText(), personModel.getNachname());
        if (setModelNachname() && equalFieldAndModelValue) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            System.out.println("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private boolean setModelNachname() {
        try {
            personModel.setNachname(txtNachname.getText());
        } catch (SvmRequiredException e) {
            System.out.println("PersonController setModelNachname RequiredException=" + e.getMessage());
            // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            return false;
        } catch (SvmValidationException e) {
            System.out.println("PersonController setModelNachname Exception=" + e.getMessage());
            // todo $$$ Fehler anzeigen
            return false;
        }
        return true;
    }

    private void onVornameEvent() {
        System.out.println("PersonController Event Vorname");
        boolean equalFieldAndModelValue = equalsNullSafe(txtVorname.getText(), personModel.getVorname());
        if (setModelVorname() && equalFieldAndModelValue) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            System.out.println("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private boolean setModelVorname() {
        try {
            personModel.setVorname(txtVorname.getText());
        } catch (SvmRequiredException e) {
            System.out.println("PersonController setModelVorname RequiredException=" + e.getMessage());
            // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            return false;
        } catch (SvmValidationException e) {
            System.out.println("PersonController setModelVorname Exception=" + e.getMessage());
            // todo $$$ Fehler anzeigen
            return false;
        }
        return true;
    }

    private void onStrasseEvent() {
        System.out.println("PersonController Event Strasse");
        boolean equalFieldAndModelValue = equalsNullSafe(txtStrasse.getText(), personModel.getStrasse());
        if (setModelStrasse() && equalFieldAndModelValue) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            System.out.println("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private boolean setModelStrasse() {
        try {
            personModel.setStrasse(txtStrasse.getText());
        } catch (SvmRequiredException e) {
            System.out.println("PersonController setModelStrasse RequiredException=" + e.getMessage());
            // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            return false;
        } catch (SvmValidationException e) {
            System.out.println("PersonController setModelStrasse Exception=" + e.getMessage());
            // todo $$$ Fehler anzeigen
            return false;
        }
        return true;
    }

    private void onHausnummerEvent() {
        System.out.println("PersonController Event Hausnummer");
        setModelHausnummer();
    }

    private boolean setModelHausnummer() {
        personModel.setHausnummer(txtHausnummer.getText());
        return true;
    }

    private void onPlzEvent() {
        System.out.println("PersonController Event PLZ");
        boolean equalFieldAndModelValue = equalsNullSafe(txtPlz.getText(), personModel.getPlz());
        if (setModelPlz() && equalFieldAndModelValue) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            System.out.println("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private boolean setModelPlz() {
        try {
            personModel.setPlz(txtPlz.getText());
        } catch (SvmRequiredException e) {
            System.out.println("PersonController setModelPlz RequiredException=" + e.getMessage());
            // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            return false;
        } catch (SvmValidationException e) {
            System.out.println("PersonController setModelPlz Exception=" + e.getMessage());
            // todo $$$ Fehler anzeigen
            return false;
        }
        return true;
    }

    private void onOrtEvent() {
        System.out.println("PersonController Event Ort");
        boolean equalFieldAndModelValue = equalsNullSafe(txtOrt.getText(), personModel.getOrt());
        if (setModelOrt() && equalFieldAndModelValue) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            System.out.println("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private boolean setModelOrt() {
        try {
            personModel.setOrt(txtOrt.getText());
        } catch (SvmRequiredException e) {
            System.out.println("PersonController setModelOrt RequiredException=" + e.getMessage());
            // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            return false;
        } catch (SvmValidationException e) {
            System.out.println("PersonController setModelOrt Exception=" + e.getMessage());
            // todo $$$ Fehler anzeigen
            return false;
        }
        return true;
    }

    private void onFestnetzEvent() {
        System.out.println("PersonController Event Festnetz");
        setModelFestnetz();
    }

    private boolean setModelFestnetz() {
        personModel.setFestnetz(txtFestnetz.getText());
        return true;
    }

    private void onNatelEvent() {
        System.out.println("PersonController Event Natel");
        setModelNatel();
    }

    private boolean setModelNatel() {
        personModel.setNatel(txtNatel.getText());
        return true;
    }

    private void onEmailEvent() {
        System.out.println("PersonController Event Email");
        setModelEmail();
    }

    private boolean setModelEmail() {
        personModel.setEmail(txtEmail.getText());
        return true;
    }

    private void onGeburtsdatumEvent() {
        System.out.println("PersonController Event Geburtsdatum");
        setModelGeburtsdatum();
    }

    private boolean setModelGeburtsdatum() {
        personModel.setGeburtsdatum(txtGeburtsdatum.getText());
        return true;
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
    boolean validateFields() {
        if (comboBoxAnrede != null) {
            System.out.println("Validate field Anrede");
            if (!setModelAnrede()) {
                return false;
            }
        }
        System.out.println("Validate field Nachname");
        if (!setModelNachname()) {
            return false;
        }
        System.out.println("Validate field Vorname");
        if (!setModelVorname()) {
            return false;
        }
        System.out.println("Validate field Strasse");
        if (!setModelStrasse()) {
            return false;
        }
        System.out.println("Validate field Hausnummer");
        if (!setModelHausnummer()) {
            return false;
        }
        System.out.println("Validate field Plz");
        if (!setModelPlz()) {
            return false;
        }
        System.out.println("Validate field Ort");
        if (!setModelOrt()) {
            return false;
        }
        if (txtGeburtsdatum != null) {
            System.out.println("Validate field Geburtsdatum");
            if (!setModelGeburtsdatum()) {
                return false;
            }
        }
        System.out.println("Validate field Festnetz");
        if (!setModelFestnetz()) {
            return false;
        }
        System.out.println("Validate field Natel");
        if (!setModelNatel()) {
            return false;
        }
        System.out.println("Validate field Email");
        if (!setModelEmail()) {
            return false;
        }
        return true;
    }

    @Override
    void show(SvmValidationException e) {
        // todo $$$
    }

}
