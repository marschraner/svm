package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.dataTypes.Field;
import ch.metzenthin.svm.dataTypes.Geschlecht;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.SchuelerModel;
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
public class SchuelerController extends PersonController {

    private static final Logger LOGGER = Logger.getLogger(SchuelerController.class);

    private JTextField txtAnmeldedatum;
    private JTextField txtAbmeldedatum;
    private JTextArea textAreaBemerkungen;
    private JComboBox<Geschlecht> comboBoxGeschlecht;
    private JLabel errLblAnmeldedatum;
    private JLabel errLblAbmeldedatum;
    private JLabel errLblBemerkungen;
    private JLabel errLblGeschlecht;

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
        LOGGER.trace("SchuelerController Event Geschlecht selected=" + comboBoxGeschlecht.getSelectedItem());
        setModelGeschlecht();
    }

    private void setModelGeschlecht() {
        schuelerModel.setGeschlecht((Geschlecht) comboBoxGeschlecht.getSelectedItem());
    }

    private void onAnmeldedatumEvent() {
        LOGGER.trace("SchuelerController Event Anmeldedatum");
        boolean equalFieldAndModelValue = equalsNullSafe(txtAnmeldedatum.getText(), schuelerModel.getAnmeldedatum());
        try {
            setModelAnmeldedatum();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelAnmeldedatum() throws SvmValidationException {
        makeErrorLabelInvisible(Field.ANMELDEDATUM);
        try {
            schuelerModel.setAnmeldedatum(txtAnmeldedatum.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("SchuelerController setModelAnmeldedatum RequiredException=" + e.getMessage());
            txtAnmeldedatum.setToolTipText(e.getMessage());
            // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("SchuelerController setModelAnmeldedatum Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    private void onAbmeldedatumEvent() {
        LOGGER.trace("SchuelerController Event Abmeldedatum");
        boolean equalFieldAndModelValue = equalsNullSafe(txtAbmeldedatum.getText(), asString(schuelerModel.getAbmeldedatum()));
        try {
            setModelAbmeldedatum();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelAbmeldedatum() throws SvmValidationException {
        makeErrorLabelInvisible(Field.ABMELDEDATUM);
        try {
            schuelerModel.setAbmeldedatum(txtAbmeldedatum.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("SchuelerController setModelAbmeldedatum Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    private void onBemerkungenEvent() {
        LOGGER.trace("SchuelerController Event Bemerkungen");
        boolean equalFieldAndModelValue = equalsNullSafe(textAreaBemerkungen.getText(), schuelerModel.getBemerkungen());
        try {
            setModelBemerkungen();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelBemerkungen() throws SvmValidationException {
        makeErrorLabelInvisible(Field.BEMERKUNGEN);
        try {
            schuelerModel.setBemerkungen(textAreaBemerkungen.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("SchuelerController setModelBemerkungen Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setErrLblAnmeldedatum(JLabel errLblAnmeldedatum) {
        this.errLblAnmeldedatum = errLblAnmeldedatum;
    }

    public void setErrLblAbmeldedatum(JLabel errLblAbmeldedatum) {
        this.errLblAbmeldedatum = errLblAbmeldedatum;
    }

    public void setErrLblBemerkungen(JLabel errLblBemerkungen) {
        this.errLblBemerkungen = errLblBemerkungen;
    }

    public void setErrLblGeschlecht(JLabel errLblGeschlecht) {
        this.errLblGeschlecht = errLblGeschlecht;
    }

    @Override
    void doPropertyChange(PropertyChangeEvent evt) {
        LOGGER.trace("SchuelerController PropertyChangeEvent '" + evt.getPropertyName() + "', oldValue='" + evt.getOldValue() + "', newValue='" + evt.getNewValue() + "'");
        if (checkIsFieldChange(Field.GESCHLECHT, evt)) {
            comboBoxGeschlecht.setSelectedItem(schuelerModel.getGeschlecht());
        } else if (checkIsFieldChange(Field.BEMERKUNGEN, evt)) {
            textAreaBemerkungen.setText(schuelerModel.getBemerkungen());
        } else if (checkIsFieldChange(Field.ANMELDEDATUM, evt)) {
            txtAnmeldedatum.setText(asString(schuelerModel.getAnmeldedatum()));
        } else if (checkIsFieldChange(Field.ABMELDEDATUM, evt)) {
            txtAbmeldedatum.setText(asString(schuelerModel.getAbmeldedatum()));
        }
        super.doPropertyChange(evt);
    }

    @Override
    void validateFields() throws SvmValidationException {
        super.validateFields();
        LOGGER.trace("Validate field Geschlecht");
        setModelGeschlecht();
        LOGGER.trace("Validate field Anmeldedatum");
        setModelAnmeldedatum();
        LOGGER.trace("Validate field Abmeldedatum");
        setModelAbmeldedatum();
        LOGGER.trace("Validate field Bemerkungen");
        setModelBemerkungen();
    }

    @Override
    void showErrMsg(SvmValidationException e) {
        super.showErrMsg(e);
        if (e.getAffectedFields().contains(Field.GESCHLECHT)) {
            errLblGeschlecht.setVisible(true);
            errLblGeschlecht.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ANMELDEDATUM)) {
            errLblAnmeldedatum.setVisible(true);
            errLblAnmeldedatum.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ABMELDEDATUM)) {
            errLblAbmeldedatum.setVisible(true);
            errLblAbmeldedatum.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.BEMERKUNGEN)) {
            errLblBemerkungen.setVisible(true);
            errLblBemerkungen.setText(e.getMessage());
        }
    }

    @Override
    void showErrMsgAsToolTip(SvmValidationException e) {
        super.showErrMsgAsToolTip(e);
        if (e.getAffectedFields().contains(Field.GESCHLECHT)) {
            comboBoxGeschlecht.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ANMELDEDATUM)) {
            txtAnmeldedatum.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ABMELDEDATUM)) {
            txtAbmeldedatum.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.BEMERKUNGEN)) {
            textAreaBemerkungen.setToolTipText(e.getMessage());
        }
    }

    @Override
    public void makeErrorLabelsInvisible(Set<Field> fields) {
        super.makeErrorLabelsInvisible(fields);
        if (fields.contains(Field.GESCHLECHT)) {
            errLblGeschlecht.setVisible(false);
            comboBoxGeschlecht.setToolTipText(null);
        }
        if (fields.contains(Field.ANMELDEDATUM)) {
            errLblAnmeldedatum.setVisible(false);
            txtAnmeldedatum.setToolTipText(null);
        }
        if (fields.contains(Field.ABMELDEDATUM)) {
            errLblAbmeldedatum.setVisible(false);
            txtAbmeldedatum.setToolTipText(null);
        }
        if (fields.contains(Field.BEMERKUNGEN)) {
            errLblBemerkungen.setVisible(false);
            textAreaBemerkungen.setToolTipText(null);
        }
    }

    @Override
    public void disableFields(boolean disable, Set<Field> fields) {
        super.disableFields(disable, fields);
        if (fields.contains(Field.ALLE) || fields.contains(Field.GESCHLECHT)) {
            comboBoxGeschlecht.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.ANMELDEDATUM)) {
            txtAnmeldedatum.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.ABMELDEDATUM)) {
            txtAbmeldedatum.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.BEMERKUNGEN)) {
            textAreaBemerkungen.setEnabled(!disable);
        }
    }
}
