package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.dataTypes.Geschlecht;
import ch.metzenthin.svm.domain.model.SchuelerModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;

/**
 * @author Hans Stamm
 */
public class SchuelerController extends PersonController {

    private JTextField txtAnmeldedatum;
    private JTextField txtAbmeldedatum;
    private JTextArea textAreaBemerkungen;
    private JComboBox<Geschlecht> comboBoxGeschlecht;

    private SchuelerModel schuelerModel;

    public SchuelerController(SchuelerModel schuelerModel) {
        super(schuelerModel);
        this.schuelerModel = schuelerModel;
        this.schuelerModel.addPropertyChangeListener(this);
    }

    public void setComboBoxGeschlecht(JComboBox<Geschlecht> comboBoxGeschlecht) {
        this.comboBoxGeschlecht = comboBoxGeschlecht;
        comboBoxGeschlecht.setModel(new DefaultComboBoxModel<>(Geschlecht.values()));
        comboBoxGeschlecht.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onGeschlechtSelected();
            }
        });
    }

    public void setTxtAnmeldedatum(JTextField txtAnmeldedatum) {
        this.txtAnmeldedatum = txtAnmeldedatum;
        this.txtAnmeldedatum.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onAnmeldedatumEvent();
            }
        });
        this.txtAnmeldedatum.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onAnmeldedatumEvent();
            }
        });
    }

    public void setTxtAbmeldedatum(JTextField txtAbmeldedatum) {
        this.txtAbmeldedatum = txtAbmeldedatum;
        this.txtAbmeldedatum.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onAbmeldedatumEvent();
            }
        });
        this.txtAbmeldedatum.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onAbmeldedatumEvent();
            }
        });
    }

    public void setTextAreaBemerkungen(JTextArea textAreaBemerkungen) {
        this.textAreaBemerkungen = textAreaBemerkungen;
        // todo weitere listener?
        textAreaBemerkungen.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onBemerkungenEvent();
            }
        });
    }

    private void onGeschlechtSelected() {
        System.out.println("SchuelerController Event Geschlecht selected=" + comboBoxGeschlecht.getSelectedItem());
        schuelerModel.setGeschlecht((Geschlecht) comboBoxGeschlecht.getSelectedItem());
    }

    private void onAnmeldedatumEvent() {
        System.out.println("SchuelerController Event Anmeldedatum");
        schuelerModel.setAnmeldedatum(txtAnmeldedatum.getText());
    }

    private void onAbmeldedatumEvent() {
        System.out.println("SchuelerController Event Abmeldedatum");
        schuelerModel.setAbmeldedatum(txtAbmeldedatum.getText());
    }

    private void onBemerkungenEvent() {
        System.out.println("SchuelerController Event Bemerkungen");
        schuelerModel.setBemerkungen(textAreaBemerkungen.getText());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println("SchuelerController PropertyChangeEvent '" + evt.getPropertyName() + "', oldValue='" + evt.getOldValue() + "', newValue='" + evt.getNewValue() + "'");
        super.propertyChange(evt);
    }

}
