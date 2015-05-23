package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.dataTypes.Anrede;
import ch.metzenthin.svm.domain.model.PersonModel;

import javax.swing.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static ch.metzenthin.svm.common.utils.Converter.asString;

/**
 * @author Hans Stamm
 */
public abstract class PersonController implements PropertyChangeListener {

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
        personModel.setAnrede((Anrede) comboBoxAnrede.getSelectedItem());
    }

    private void onNachnameEvent() {
        System.out.println("PersonController Event Nachname");
        personModel.setNachname(txtNachname.getText());
    }

    private void onVornameEvent() {
        System.out.println("PersonController Event Vorname");
        personModel.setVorname(txtVorname.getText());
    }

    private void onStrasseEvent() {
        System.out.println("PersonController Event Strasse");
        personModel.setStrasse(txtStrasse.getText());
    }

    private void onHausnummerEvent() {
        System.out.println("PersonController Event Hausnummer");
        personModel.setHausnummer(txtHausnummer.getText());
    }

    private void onPlzEvent() {
        System.out.println("PersonController Event PLZ");
        personModel.setPlz(txtPlz.getText());
    }

    private void onOrtEvent() {
        System.out.println("PersonController Event Ort");
        personModel.setOrt(txtOrt.getText());
    }

    private void onFestnetzEvent() {
        System.out.println("PersonController Event Festnetz");
        personModel.setFestnetz(txtFestnetz.getText());
    }

    private void onNatelEvent() {
        System.out.println("PersonController Event Natel");
        personModel.setNatel(txtNatel.getText());
    }

    private void onEmailEvent() {
        System.out.println("PersonController Event Email");
        personModel.setEmail(txtEmail.getText());
    }

    private void onGeburtsdatumEvent() {
        System.out.println("PersonController Event Geburtsdatum");
        personModel.setGeburtsdatum(txtGeburtsdatum.getText());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
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
            txtGeburtsdatum.setText(asString(personModel.getGeburtsdatum()));
        } else if ("Festnetz".equals(evt.getPropertyName())) {
            txtFestnetz.setText(personModel.getFestnetz());
        } else if ("Natel".equals(evt.getPropertyName())) {
            txtNatel.setText(personModel.getNatel());
        } else if ("Email".equals(evt.getPropertyName())) {
            txtEmail.setText(personModel.getEmail());
        } else if ("Anrede".equals(evt.getPropertyName())) {
            comboBoxAnrede.setSelectedItem(personModel.getAnrede());
        }
    }

}
