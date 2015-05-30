package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.dataTypes.Geschlecht;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.SchuelerModel;

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
        setModelGeschlecht();
    }

    private boolean setModelGeschlecht() {
        schuelerModel.setGeschlecht((Geschlecht) comboBoxGeschlecht.getSelectedItem());
        return true;
    }

    private void onAnmeldedatumEvent() {
        System.out.println("SchuelerController Event Anmeldedatum");
        boolean equalFieldAndModelValue = equalsNullSafe(txtAnmeldedatum.getText(), schuelerModel.getAnmeldedatum());
        if (setModelAnmeldedatum() && equalFieldAndModelValue) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            System.out.println("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private boolean setModelAnmeldedatum() {
        try {
            schuelerModel.setAnmeldedatum(txtAnmeldedatum.getText());
        } catch (SvmValidationException e) {
            System.out.println("SchuelerController setModelAnmeldedatum Exception=" + e.getMessage());
            // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            return false;
        }
        return true;
    }

    private void onAbmeldedatumEvent() {
        System.out.println("SchuelerController Event Abmeldedatum");
        boolean equalFieldAndModelValue = equalsNullSafe(txtAbmeldedatum.getText(), schuelerModel.getAbmeldedatum());
        if (setModelAbmeldedatum() && equalFieldAndModelValue) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            System.out.println("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private boolean setModelAbmeldedatum() {
        try {
            schuelerModel.setAbmeldedatum(txtAbmeldedatum.getText());
        } catch (SvmValidationException e) {
            System.out.println("SchuelerController setModelAbmeldedatum Exception=" + e.getMessage());
            // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            return false;
        }
        return true;
    }

    private void onBemerkungenEvent() {
        System.out.println("SchuelerController Event Bemerkungen");
        setModelBemerkungen();
    }

    private boolean setModelBemerkungen() {
        schuelerModel.setBemerkungen(textAreaBemerkungen.getText());
        return true;
    }

    @Override
    void doPropertyChange(PropertyChangeEvent evt) {
        System.out.println("SchuelerController PropertyChangeEvent '" + evt.getPropertyName() + "', oldValue='" + evt.getOldValue() + "', newValue='" + evt.getNewValue() + "'");
        if ("Geschlecht".equals(evt.getPropertyName())) {
            comboBoxGeschlecht.setSelectedItem(schuelerModel.getGeschlecht());
        } else if ("Bermerkungen".equals(evt.getPropertyName())) {
            textAreaBemerkungen.setText(schuelerModel.getBemerkungen());
        } else if ("Anmeldedatum".equals(evt.getPropertyName())) {
            txtAnmeldedatum.setText(asString(schuelerModel.getAnmeldedatum()));
        } else if ("Abmeldedatum".equals(evt.getPropertyName())) {
            txtAbmeldedatum.setText(asString(schuelerModel.getAbmeldedatum()));
        }
        super.doPropertyChange(evt);
    }

    @Override
    boolean validateFields() {
        if (!super.validateFields()) {
            return false;
        }
        System.out.println("Validate field Geschlecht");
        if (!setModelGeschlecht()) {
            return false;
        }
        System.out.println("Validate field Anmeldedatum");
        if (!setModelAnmeldedatum()) {
            return false;
        }
        System.out.println("Validate field Abmeldedatum");
        if (!setModelAbmeldedatum()) {
            return false;
        }
        System.out.println("Validate field Bemerkungen");
        if (!setModelBemerkungen()) {
            return false;
        }
        return true;
    }

    @Override
    void show(SvmValidationException e) {
        super.show(e);
        // todo $$$
    }

}
