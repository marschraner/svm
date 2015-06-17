package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.dataTypes.FieldName;
import ch.metzenthin.svm.dataTypes.Geschlecht;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.SchuelerModel;

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

    private void setModelGeschlecht() {
        schuelerModel.setGeschlecht((Geschlecht) comboBoxGeschlecht.getSelectedItem());
    }

    private void onAnmeldedatumEvent() {
        System.out.println("SchuelerController Event Anmeldedatum");
        boolean equalFieldAndModelValue = equalsNullSafe(txtAnmeldedatum.getText(), schuelerModel.getAnmeldedatum());
        try {
            setModelAnmeldedatum();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            System.out.println("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelAnmeldedatum() throws SvmValidationException {
        try {
            schuelerModel.setAnmeldedatum(txtAnmeldedatum.getText());
        } catch (SvmRequiredException e) {
            System.out.println("SchuelerController setModelAnmeldedatum RequiredException=" + e.getMessage());
            // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            throw e;
        } catch (SvmValidationException e) {
            System.out.println("SchuelerController setModelAnmeldedatum Exception=" + e.getMessage());
            // todo $$$ Fehler anzeigen
            throw e;
        }
    }

    private void onAbmeldedatumEvent() {
        System.out.println("SchuelerController Event Abmeldedatum");
        boolean equalFieldAndModelValue = equalsNullSafe(txtAbmeldedatum.getText(), schuelerModel.getAbmeldedatum());
        try {
            setModelAbmeldedatum();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            System.out.println("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelAbmeldedatum() throws SvmValidationException {
        try {
            schuelerModel.setAbmeldedatum(txtAbmeldedatum.getText());
        } catch (SvmValidationException e) {
            System.out.println("SchuelerController setModelAbmeldedatum Exception=" + e.getMessage());
            // todo $$$ Fehler anzeigen
            throw e;
        }
    }

    private void onBemerkungenEvent() {
        System.out.println("SchuelerController Event Bemerkungen");
        boolean equalFieldAndModelValue = equalsNullSafe(textAreaBemerkungen.getText(), schuelerModel.getBemerkungen());
        try {
            setModelBemerkungen();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            System.out.println("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelBemerkungen() throws SvmValidationException {
        try {
            schuelerModel.setBemerkungen(textAreaBemerkungen.getText());
        } catch (SvmValidationException e) {
            System.out.println("SchuelerController setModelBemerkungen Exception=" + e.getMessage());
            // todo $$$ Fehler anzeigen
            throw e;
        }
    }

    @Override
    void doPropertyChange(PropertyChangeEvent evt) {
        System.out.println("SchuelerController PropertyChangeEvent '" + evt.getPropertyName() + "', oldValue='" + evt.getOldValue() + "', newValue='" + evt.getNewValue() + "'");
        if (checkIsFieldNameChange(FieldName.GESCHLECHT, evt)) {
            comboBoxGeschlecht.setSelectedItem(schuelerModel.getGeschlecht());
        } else if (checkIsFieldNameChange(FieldName.BEMERKUNGEN, evt)) {
            textAreaBemerkungen.setText(schuelerModel.getBemerkungen());
        } else if (checkIsFieldNameChange(FieldName.ANMELDEDATUM, evt)) {
            txtAnmeldedatum.setText(asString(schuelerModel.getAnmeldedatum()));
        } else if (checkIsFieldNameChange(FieldName.ABMELDEDATUM, evt)) {
            txtAbmeldedatum.setText(asString(schuelerModel.getAbmeldedatum()));
        }
        super.doPropertyChange(evt);
    }

    @Override
    void validateFields() throws SvmValidationException {
        super.validateFields();
        System.out.println("Validate field Geschlecht");
        setModelGeschlecht();
        System.out.println("Validate field Anmeldedatum");
        setModelAnmeldedatum();
        System.out.println("Validate field Abmeldedatum");
        setModelAbmeldedatum();
        System.out.println("Validate field Bemerkungen");
        setModelBemerkungen();
    }

    @Override
    void show(SvmValidationException e) {
        super.show(e);
        // todo $$$
    }

    @Override
    public void disableFields(boolean disable, Set<FieldName> fieldNames) {
        super.disableFields(disable, fieldNames);
        if (fieldNames.contains(FieldName.ALLE) || fieldNames.contains(FieldName.GESCHLECHT)) {
            comboBoxGeschlecht.setEnabled(!disable);
        }
        if (fieldNames.contains(FieldName.ALLE) || fieldNames.contains(FieldName.ANMELDEDATUM)) {
            txtAnmeldedatum.setEnabled(!disable);
        }
        if (fieldNames.contains(FieldName.ALLE) || fieldNames.contains(FieldName.ABMELDEDATUM)) {
            txtAbmeldedatum.setEnabled(!disable);
        }
        if (fieldNames.contains(FieldName.ALLE) || fieldNames.contains(FieldName.BEMERKUNGEN)) {
            textAreaBemerkungen.setEnabled(!disable);
        }
    }
}
