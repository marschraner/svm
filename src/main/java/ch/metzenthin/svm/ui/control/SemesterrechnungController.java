package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.common.dataTypes.Stipendium;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.SemesterrechnungModel;
import ch.metzenthin.svm.domain.model.SemesterrechnungenSuchenModel;
import ch.metzenthin.svm.persistence.entities.SemesterrechnungCode;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.util.Set;

import static ch.metzenthin.svm.common.utils.Converter.asString;
import static ch.metzenthin.svm.common.utils.SimpleValidator.equalsNullSafe;

/**
 * @author Martin Schraner
 */
public abstract class SemesterrechnungController extends AbstractController {

    private static final Logger LOGGER = Logger.getLogger(SemesterrechnungController.class);

    // Möglichkeit zum Umschalten des validation modes (nicht dynamisch)
    private static final boolean MODEL_VALIDATION_MODE = false;

    private JTextField txtRechnungsdatumVorrechnung;
    private JTextField txtErmaessigungVorrechnung;
    private JTextField txtErmaessigungsgrundVorrechnung;
    private JTextField txtZuschlagVorrechnung;
    private JTextField txtZuschlagsgrundVorrechnung;
    private JTextField txtAnzahlWochenVorrechnung;
    private JTextField txtWochenbetragVorrechnung;
    private JTextField txtRechnungsdatumNachrechnung;
    private JTextField txtErmaessigungNachrechnung;
    private JTextField txtErmaessigungsgrundNachrechnung;
    private JTextField txtZuschlagNachrechnung;
    private JTextField txtZuschlagsgrundNachrechnung;
    private JTextField txtAnzahlWochenNachrechnung;
    private JTextField txtWochenbetragNachrechnung;
    private JTextField txtDatumZahlung1;
    private JTextField txtBetragZahlung1;
    private JTextField txtDatumZahlung2;
    private JTextField txtBetragZahlung2;
    private JTextField txtDatumZahlung3;
    private JTextField txtBetragZahlung3;
    private JTextArea textAreaBemerkungen;
    private JLabel errLblRechnungsdatumVorrechnung;
    private JLabel errLblErmaessigungVorrechnung;
    private JLabel errLblErmaessigungsgrundVorrechnung;
    private JLabel errLblZuschlagVorrechnung;
    private JLabel errLblZuschlagsgrundVorrechnung;
    private JLabel errLblAnzahlWochenVorrechnung;
    private JLabel errLblWochenbetragVorrechnung;
    private JLabel errLblRechnungsdatumNachrechnung;
    private JLabel errLblErmaessigungNachrechnung;
    private JLabel errLblErmaessigungsgrundNachrechnung;
    private JLabel errLblZuschlagNachrechnung;
    private JLabel errLblZuschlagsgrundNachrechnung;
    private JLabel errLblAnzahlWochenNachrechnung;
    private JLabel errLblWochenbetragNachrechnung;
    private JLabel errLblDatumZahlung1;
    private JLabel errLblBetragZahlung1;
    private JLabel errLblDatumZahlung2;
    private JLabel errLblBetragZahlung2;
    private JLabel errLblDatumZahlung3;
    private JLabel errLblBetragZahlung3;
    private JLabel errLblBemerkungen;
    private JComboBox<SemesterrechnungCode> comboBoxSemesterrechnungCode;
    private JComboBox<Stipendium> comboBoxStipendium;
    private JCheckBox checkBoxGratiskinder;
    private SemesterrechnungModel semesterrechnungModel;
    private SvmContext svmContext;

    public SemesterrechnungController(SvmContext svmContext, SemesterrechnungenSuchenModel semesterrechnungModel) {
        super(semesterrechnungModel);
        this.svmContext = svmContext;
        this.semesterrechnungModel = semesterrechnungModel;
        this.semesterrechnungModel.addPropertyChangeListener(this);
        this.semesterrechnungModel.addDisableFieldsListener(this);
        this.semesterrechnungModel.addMakeErrorLabelsInvisibleListener(this);
        this.setModelValidationMode(MODEL_VALIDATION_MODE);
        svmContext.getSvmModel().loadAll();
    }

    public void setComboBoxSemesterrechnungCode(JComboBox<SemesterrechnungCode> comboBoxCode) {
        this.comboBoxSemesterrechnungCode = comboBoxCode;
        SemesterrechnungCode[] selectableSemesterrechnungCodes = semesterrechnungModel.getSelectableSemesterrechnungCodes(svmContext.getSvmModel());
        comboBoxCode.setModel(new DefaultComboBoxModel<>(selectableSemesterrechnungCodes));
        // Model initialisieren mit erstem ComboBox-Wert
        semesterrechnungModel.setSemesterrechnungCode(selectableSemesterrechnungCodes[0]);
        this.comboBoxSemesterrechnungCode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSemesterrechnungCodeSelected();
            }
        });
    }

    private void onSemesterrechnungCodeSelected() {
        LOGGER.trace("SemesterrechnungSuchenController Event SemesterrechnungCode selected=" + comboBoxSemesterrechnungCode.getSelectedItem());
        setModelSemesterrechnungCode();
    }

    private void setModelSemesterrechnungCode() {
        semesterrechnungModel.setSemesterrechnungCode((SemesterrechnungCode) comboBoxSemesterrechnungCode.getSelectedItem());
    }

    public void setComboBoxStipendium(JComboBox<Stipendium> comboBoxStipendium) {
        this.comboBoxStipendium = comboBoxStipendium;
        comboBoxStipendium.setModel(new DefaultComboBoxModel<>(Stipendium.values()));
        comboBoxStipendium.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onStipendiumSelected();
            }
        });
        // SchuelerCode in Model initialisieren mit erstem ComboBox-Wert
        semesterrechnungModel.setStipendium(Stipendium.values()[0]);
    }

    private void onStipendiumSelected() {
        LOGGER.trace("SemesterrechnungenSuchenController Event Stipendium selected=" + comboBoxStipendium.getSelectedItem());
        boolean equalFieldAndModelValue = equalsNullSafe(comboBoxStipendium.getSelectedItem(), semesterrechnungModel.getStipendium());
        setModelStipendium();
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelStipendium() {
        makeErrorLabelInvisible(Field.STIPENDIUM);
        semesterrechnungModel.setStipendium((Stipendium) comboBoxStipendium.getSelectedItem());
    }

    public void setCheckBoxGratiskinder(JCheckBox checkBoxGratiskinder) {
        this.checkBoxGratiskinder = checkBoxGratiskinder;
        if (svmContext.getSvmModel().getSemestersAll().isEmpty()) {
            checkBoxGratiskinder.setEnabled(false);
        }
        this.checkBoxGratiskinder.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                onGratiskinderEvent();
            }
        });
        // Initialisierung
        semesterrechnungModel.setGratiskinder(false);
    }

    private void onGratiskinderEvent() {
        LOGGER.trace("SemesterrechnungenSuchenController Event Gratiskinder. Selected=" + checkBoxGratiskinder.isSelected());
        setModelGratiskinder();
    }

    private void setModelGratiskinder() {
        semesterrechnungModel.setGratiskinder(checkBoxGratiskinder.isSelected());
    }

    public void setTxtRechnungsdatumVorrechnung(JTextField txtRechnungsdatumVorrechnung) {
        this.txtRechnungsdatumVorrechnung = txtRechnungsdatumVorrechnung;
        this.txtRechnungsdatumVorrechnung.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onRechnungsdatumVorrechnungEvent();
            }
        });
        this.txtRechnungsdatumVorrechnung.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onRechnungsdatumVorrechnungEvent();
            }
        });
    }

    private void onRechnungsdatumVorrechnungEvent() {
        LOGGER.trace("SemesterrechnungenSuchenController Event Rechnungsdatum");
        boolean equalFieldAndModelValue = equalsNullSafe(txtRechnungsdatumVorrechnung.getText(), semesterrechnungModel.getRechnungsdatumVorrechnung());
        try {
            setModelRechnungsdatumVorrechnung();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelRechnungsdatumVorrechnung() throws SvmValidationException {
        makeErrorLabelInvisible(Field.RECHNUNGSDATUM_VORRECHNUNG);
        try {
            semesterrechnungModel.setRechnungsdatumVorrechnung(txtRechnungsdatumVorrechnung.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("SemesterrechnungenSuchenController setModelRechnungsdatumVorrechnung Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtErmaessigungVorrechnung(JTextField txtErmaessigungVorrechnung) {
        this.txtErmaessigungVorrechnung = txtErmaessigungVorrechnung;
        this.txtErmaessigungVorrechnung.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onErmaessigungVorrechnungEvent();
            }
        });
        this.txtErmaessigungVorrechnung.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onErmaessigungVorrechnungEvent();
            }
        });
    }

    private void onErmaessigungVorrechnungEvent() {
        LOGGER.trace("SemesterrechnungenSuchenController Event Ermaessigung");
        boolean equalFieldAndModelValue = equalsNullSafe(txtErmaessigungVorrechnung.getText(), semesterrechnungModel.getErmaessigungVorrechnung());
        try {
            setModelErmaessigungVorrechnung();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelErmaessigungVorrechnung() throws SvmValidationException {
        makeErrorLabelInvisible(Field.ERMAESSIGUNG_VORRECHNUNG);
        try {
            semesterrechnungModel.setErmaessigungVorrechnung(txtErmaessigungVorrechnung.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("SemesterrechnungenSuchenController setModelErmaessigungVorrechnung Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtErmaessigungsgrundVorrechnung(JTextField txtErmaessigungsgrundVorrechnung) {
        this.txtErmaessigungsgrundVorrechnung = txtErmaessigungsgrundVorrechnung;
        this.txtErmaessigungsgrundVorrechnung.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onErmaessigungsgrundVorrechnungEvent();
            }
        });
        this.txtErmaessigungsgrundVorrechnung.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onErmaessigungsgrundVorrechnungEvent();
            }
        });
    }

    private void onErmaessigungsgrundVorrechnungEvent() {
        LOGGER.trace("SemesterrechnungenSuchenController Event Ermaessigungsgrund");
        boolean equalFieldAndModelValue = equalsNullSafe(txtErmaessigungsgrundVorrechnung.getText(), semesterrechnungModel.getErmaessigungsgrundVorrechnung());
        try {
            setModelErmaessigungsgrundVorrechnung();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelErmaessigungsgrundVorrechnung() throws SvmValidationException {
        makeErrorLabelInvisible(Field.ERMAESSIGUNGSGRUND_VORRECHNUNG);
        try {
            semesterrechnungModel.setErmaessigungsgrundVorrechnung(txtErmaessigungsgrundVorrechnung.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("SemesterrechnungenSuchenController setModelErmaessigungsgrundVorrechnung Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtZuschlagVorrechnung(JTextField txtZuschlagVorrechnung) {
        this.txtZuschlagVorrechnung = txtZuschlagVorrechnung;
        this.txtZuschlagVorrechnung.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onZuschlagVorrechnungEvent();
            }
        });
        this.txtZuschlagVorrechnung.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onZuschlagVorrechnungEvent();
            }
        });
    }

    private void onZuschlagVorrechnungEvent() {
        LOGGER.trace("SemesterrechnungenSuchenController Event Zuschlag");
        boolean equalFieldAndModelValue = equalsNullSafe(txtZuschlagVorrechnung.getText(), semesterrechnungModel.getZuschlagVorrechnung());
        try {
            setModelZuschlagVorrechnung();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelZuschlagVorrechnung() throws SvmValidationException {
        makeErrorLabelInvisible(Field.ZUSCHLAG_VORRECHNUNG);
        try {
            semesterrechnungModel.setZuschlagVorrechnung(txtZuschlagVorrechnung.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("SemesterrechnungenSuchenController setModelZuschlagVorrechnung Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtZuschlagsgrundVorrechnung(JTextField txtZuschlagsgrundVorrechnung) {
        this.txtZuschlagsgrundVorrechnung = txtZuschlagsgrundVorrechnung;
        this.txtZuschlagsgrundVorrechnung.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onZuschlagsgrundVorrechnungEvent();
            }
        });
        this.txtZuschlagsgrundVorrechnung.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onZuschlagsgrundVorrechnungEvent();
            }
        });
    }

    private void onZuschlagsgrundVorrechnungEvent() {
        LOGGER.trace("SemesterrechnungenSuchenController Event Zuschlagsgrund");
        boolean equalFieldAndModelValue = equalsNullSafe(txtZuschlagsgrundVorrechnung.getText(), semesterrechnungModel.getZuschlagsgrundVorrechnung());
        try {
            setModelZuschlagsgrundVorrechnung();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelZuschlagsgrundVorrechnung() throws SvmValidationException {
        makeErrorLabelInvisible(Field.ZUSCHLAGSGRUND_VORRECHNUNG);
        try {
            semesterrechnungModel.setZuschlagsgrundVorrechnung(txtZuschlagsgrundVorrechnung.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("SemesterrechnungenSuchenController setModelZuschlagsgrundVorrechnung Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtAnzahlWochenVorrechnung(JTextField txtAnzahlWochenVorrechnung) {
        this.txtAnzahlWochenVorrechnung = txtAnzahlWochenVorrechnung;
        this.txtAnzahlWochenVorrechnung.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onAnzahlWochenVorrechnungEvent();
            }
        });
        this.txtAnzahlWochenVorrechnung.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onAnzahlWochenVorrechnungEvent();
            }
        });
    }

    private void onAnzahlWochenVorrechnungEvent() {
        LOGGER.trace("SemesterrechnungenSuchenController Event AnzahlWochen");
        boolean equalFieldAndModelValue = equalsNullSafe(txtAnzahlWochenVorrechnung.getText(), semesterrechnungModel.getAnzahlWochenVorrechnung());
        try {
            setModelAnzahlWochenVorrechnung();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelAnzahlWochenVorrechnung() throws SvmValidationException {
        makeErrorLabelInvisible(Field.ANZAHL_WOCHEN_VORRECHNUNG);
        try {
            semesterrechnungModel.setAnzahlWochenVorrechnung(txtAnzahlWochenVorrechnung.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("SemesterrechnungenSuchenController setModelAnzahlWochenVorrechnung Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtWochenbetragVorrechnung(JTextField txtWochenbetragVorrechnung) {
        this.txtWochenbetragVorrechnung = txtWochenbetragVorrechnung;
        this.txtWochenbetragVorrechnung.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onWochenbetragVorrechnungEvent();
            }
        });
        this.txtWochenbetragVorrechnung.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onWochenbetragVorrechnungEvent();
            }
        });
    }

    private void onWochenbetragVorrechnungEvent() {
        LOGGER.trace("SemesterrechnungenSuchenController Event WochenbetragVorrechnung");
        boolean equalFieldAndModelValue = equalsNullSafe(txtWochenbetragVorrechnung.getText(), semesterrechnungModel.getWochenbetragVorrechnung());
        try {
            setModelWochenbetragVorrechnung();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelWochenbetragVorrechnung() throws SvmValidationException {
        makeErrorLabelInvisible(Field.WOCHENBETRAG_VORRECHNUNG);
        try {
            semesterrechnungModel.setWochenbetragVorrechnung(txtWochenbetragVorrechnung.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("SemesterrechnungenSuchenController setModelWochenbetragVorrechnung Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtRechnungsdatumNachrechnung(JTextField txtRechnungsdatumNachrechnung) {
        this.txtRechnungsdatumNachrechnung = txtRechnungsdatumNachrechnung;
        this.txtRechnungsdatumNachrechnung.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onRechnungsdatumNachrechnungEvent();
            }
        });
        this.txtRechnungsdatumNachrechnung.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onRechnungsdatumNachrechnungEvent();
            }
        });
    }

    private void onRechnungsdatumNachrechnungEvent() {
        LOGGER.trace("SemesterrechnungenSuchenController Event Rechnungsdatum");
        boolean equalFieldAndModelValue = equalsNullSafe(txtRechnungsdatumNachrechnung.getText(), semesterrechnungModel.getRechnungsdatumNachrechnung());
        try {
            setModelRechnungsdatumNachrechnung();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelRechnungsdatumNachrechnung() throws SvmValidationException {
        makeErrorLabelInvisible(Field.RECHNUNGSDATUM_NACHRECHNUNG);
        try {
            semesterrechnungModel.setRechnungsdatumNachrechnung(txtRechnungsdatumNachrechnung.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("SemesterrechnungenSuchenController setModelRechnungsdatumNachrechnung Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtErmaessigungNachrechnung(JTextField txtErmaessigungNachrechnung) {
        this.txtErmaessigungNachrechnung = txtErmaessigungNachrechnung;
        this.txtErmaessigungNachrechnung.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onErmaessigungNachrechnungEvent();
            }
        });
        this.txtErmaessigungNachrechnung.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onErmaessigungNachrechnungEvent();
            }
        });
    }

    private void onErmaessigungNachrechnungEvent() {
        LOGGER.trace("SemesterrechnungenSuchenController Event Ermaessigung");
        boolean equalFieldAndModelValue = equalsNullSafe(txtErmaessigungNachrechnung.getText(), semesterrechnungModel.getErmaessigungNachrechnung());
        try {
            setModelErmaessigungNachrechnung();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelErmaessigungNachrechnung() throws SvmValidationException {
        makeErrorLabelInvisible(Field.ERMAESSIGUNG_NACHRECHNUNG);
        try {
            semesterrechnungModel.setErmaessigungNachrechnung(txtErmaessigungNachrechnung.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("SemesterrechnungenSuchenController setModelErmaessigungNachrechnung Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtErmaessigungsgrundNachrechnung(JTextField txtErmaessigungsgrundNachrechnung) {
        this.txtErmaessigungsgrundNachrechnung = txtErmaessigungsgrundNachrechnung;
        this.txtErmaessigungsgrundNachrechnung.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onErmaessigungsgrundNachrechnungEvent();
            }
        });
        this.txtErmaessigungsgrundNachrechnung.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onErmaessigungsgrundNachrechnungEvent();
            }
        });
    }

    private void onErmaessigungsgrundNachrechnungEvent() {
        LOGGER.trace("SemesterrechnungenSuchenController Event Ermaessigungsgrund");
        boolean equalFieldAndModelValue = equalsNullSafe(txtErmaessigungsgrundNachrechnung.getText(), semesterrechnungModel.getErmaessigungsgrundNachrechnung());
        try {
            setModelErmaessigungsgrundNachrechnung();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelErmaessigungsgrundNachrechnung() throws SvmValidationException {
        makeErrorLabelInvisible(Field.ERMAESSIGUNGSGRUND_NACHRECHNUNG);
        try {
            semesterrechnungModel.setErmaessigungsgrundNachrechnung(txtErmaessigungsgrundNachrechnung.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("SemesterrechnungenSuchenController setModelErmaessigungsgrundNachrechnung Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtZuschlagNachrechnung(JTextField txtZuschlagNachrechnung) {
        this.txtZuschlagNachrechnung = txtZuschlagNachrechnung;
        this.txtZuschlagNachrechnung.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onZuschlagNachrechnungEvent();
            }
        });
        this.txtZuschlagNachrechnung.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onZuschlagNachrechnungEvent();
            }
        });
    }

    private void onZuschlagNachrechnungEvent() {
        LOGGER.trace("SemesterrechnungenSuchenController Event Zuschlag");
        boolean equalFieldAndModelValue = equalsNullSafe(txtZuschlagNachrechnung.getText(), semesterrechnungModel.getZuschlagNachrechnung());
        try {
            setModelZuschlagNachrechnung();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelZuschlagNachrechnung() throws SvmValidationException {
        makeErrorLabelInvisible(Field.ZUSCHLAG_NACHRECHNUNG);
        try {
            semesterrechnungModel.setZuschlagNachrechnung(txtZuschlagNachrechnung.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("SemesterrechnungenSuchenController setModelZuschlagNachrechnung Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtZuschlagsgrundNachrechnung(JTextField txtZuschlagsgrundNachrechnung) {
        this.txtZuschlagsgrundNachrechnung = txtZuschlagsgrundNachrechnung;
        this.txtZuschlagsgrundNachrechnung.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onZuschlagsgrundNachrechnungEvent();
            }
        });
        this.txtZuschlagsgrundNachrechnung.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onZuschlagsgrundNachrechnungEvent();
            }
        });
    }

    private void onZuschlagsgrundNachrechnungEvent() {
        LOGGER.trace("SemesterrechnungenSuchenController Event Zuschlagsgrund");
        boolean equalFieldAndModelValue = equalsNullSafe(txtZuschlagsgrundNachrechnung.getText(), semesterrechnungModel.getZuschlagsgrundNachrechnung());
        try {
            setModelZuschlagsgrundNachrechnung();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelZuschlagsgrundNachrechnung() throws SvmValidationException {
        makeErrorLabelInvisible(Field.ZUSCHLAGSGRUND_NACHRECHNUNG);
        try {
            semesterrechnungModel.setZuschlagsgrundNachrechnung(txtZuschlagsgrundNachrechnung.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("SemesterrechnungenSuchenController setModelZuschlagsgrundNachrechnung Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtAnzahlWochenNachrechnung(JTextField txtAnzahlWochenNachrechnung) {
        this.txtAnzahlWochenNachrechnung = txtAnzahlWochenNachrechnung;
        this.txtAnzahlWochenNachrechnung.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onAnzahlWochenNachrechnungEvent();
            }
        });
        this.txtAnzahlWochenNachrechnung.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onAnzahlWochenNachrechnungEvent();
            }
        });
    }

    private void onAnzahlWochenNachrechnungEvent() {
        LOGGER.trace("SemesterrechnungenSuchenController Event AnzahlWochen");
        boolean equalFieldAndModelValue = equalsNullSafe(txtAnzahlWochenNachrechnung.getText(), semesterrechnungModel.getAnzahlWochenNachrechnung());
        try {
            setModelAnzahlWochenNachrechnung();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelAnzahlWochenNachrechnung() throws SvmValidationException {
        makeErrorLabelInvisible(Field.ANZAHL_WOCHEN_NACHRECHNUNG);
        try {
            semesterrechnungModel.setAnzahlWochenNachrechnung(txtAnzahlWochenNachrechnung.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("SemesterrechnungenSuchenController setModelAnzahlWochenNachrechnung Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtWochenbetragNachrechnung(JTextField txtWochenbetragNachrechnung) {
        this.txtWochenbetragNachrechnung = txtWochenbetragNachrechnung;
        this.txtWochenbetragNachrechnung.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onWochenbetragNachrechnungEvent();
            }
        });
        this.txtWochenbetragNachrechnung.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onWochenbetragNachrechnungEvent();
            }
        });
    }

    private void onWochenbetragNachrechnungEvent() {
        LOGGER.trace("SemesterrechnungenSuchenController Event WochenbetragNachrechnung");
        boolean equalFieldAndModelValue = equalsNullSafe(txtWochenbetragNachrechnung.getText(), semesterrechnungModel.getWochenbetragNachrechnung());
        try {
            setModelWochenbetragNachrechnung();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelWochenbetragNachrechnung() throws SvmValidationException {
        makeErrorLabelInvisible(Field.WOCHENBETRAG_NACHRECHNUNG);
        try {
            semesterrechnungModel.setWochenbetragNachrechnung(txtWochenbetragNachrechnung.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("SemesterrechnungenSuchenController setModelWochenbetragNachrechnung Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtDatumZahlung1(JTextField txtDatumZahlung1) {
        this.txtDatumZahlung1 = txtDatumZahlung1;
        this.txtDatumZahlung1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onDatumZahlung1Event();
            }
        });
        this.txtDatumZahlung1.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onDatumZahlung1Event();
            }
        });
    }

    private void onDatumZahlung1Event() {
        LOGGER.trace("SemesterrechnungenSuchenController Event DatumZahlung1");
        boolean equalFieldAndModelValue = equalsNullSafe(txtDatumZahlung1.getText(), semesterrechnungModel.getDatumZahlung1());
        try {
            setModelDatumZahlung1();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelDatumZahlung1() throws SvmValidationException {
        makeErrorLabelInvisible(Field.DATUM_ZAHLUNG_1);
        try {
            semesterrechnungModel.setDatumZahlung1(txtDatumZahlung1.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("SemesterrechnungenSuchenController setModelDatumZahlung1 Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtBetragZahlung1(JTextField txtBetragZahlung1) {
        this.txtBetragZahlung1 = txtBetragZahlung1;
        this.txtBetragZahlung1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onBetragZahlung1Event();
            }
        });
        this.txtBetragZahlung1.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onBetragZahlung1Event();
            }
        });
    }

    private void onBetragZahlung1Event() {
        LOGGER.trace("SemesterrechnungenSuchenController Event BetragZahlung1");
        boolean equalFieldAndModelValue = equalsNullSafe(txtBetragZahlung1.getText(), semesterrechnungModel.getBetragZahlung1());
        try {
            setModelBetragZahlung1();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelBetragZahlung1() throws SvmValidationException {
        makeErrorLabelInvisible(Field.BETRAG_ZAHLUNG_1);
        try {
            semesterrechnungModel.setBetragZahlung1(txtBetragZahlung1.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("SemesterrechnungenSuchenController setModelBetragZahlung1 Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtDatumZahlung2(JTextField txtDatumZahlung2) {
        this.txtDatumZahlung2 = txtDatumZahlung2;
        this.txtDatumZahlung2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onDatumZahlung2Event();
            }
        });
        this.txtDatumZahlung2.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onDatumZahlung2Event();
            }
        });
    }

    private void onDatumZahlung2Event() {
        LOGGER.trace("SemesterrechnungenSuchenController Event DatumZahlung2");
        boolean equalFieldAndModelValue = equalsNullSafe(txtDatumZahlung2.getText(), semesterrechnungModel.getDatumZahlung2());
        try {
            setModelDatumZahlung2();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelDatumZahlung2() throws SvmValidationException {
        makeErrorLabelInvisible(Field.DATUM_ZAHLUNG_2);
        try {
            semesterrechnungModel.setDatumZahlung2(txtDatumZahlung2.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("SemesterrechnungenSuchenController setModelDatumZahlung2 Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtBetragZahlung2(JTextField txtBetragZahlung2) {
        this.txtBetragZahlung2 = txtBetragZahlung2;
        this.txtBetragZahlung2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onBetragZahlung2Event();
            }
        });
        this.txtBetragZahlung2.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onBetragZahlung2Event();
            }
        });
    }

    private void onBetragZahlung2Event() {
        LOGGER.trace("SemesterrechnungenSuchenController Event BetragZahlung2");
        boolean equalFieldAndModelValue = equalsNullSafe(txtBetragZahlung2.getText(), semesterrechnungModel.getBetragZahlung2());
        try {
            setModelBetragZahlung2();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelBetragZahlung2() throws SvmValidationException {
        makeErrorLabelInvisible(Field.BETRAG_ZAHLUNG_2);
        try {
            semesterrechnungModel.setBetragZahlung2(txtBetragZahlung2.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("SemesterrechnungenSuchenController setModelBetragZahlung2 Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtDatumZahlung3(JTextField txtDatumZahlung3) {
        this.txtDatumZahlung3 = txtDatumZahlung3;
        this.txtDatumZahlung3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onDatumZahlung3Event();
            }
        });
        this.txtDatumZahlung3.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onDatumZahlung3Event();
            }
        });
    }

    private void onDatumZahlung3Event() {
        LOGGER.trace("SemesterrechnungenSuchenController Event DatumZahlung3");
        boolean equalFieldAndModelValue = equalsNullSafe(txtDatumZahlung3.getText(), semesterrechnungModel.getDatumZahlung3());
        try {
            setModelDatumZahlung3();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelDatumZahlung3() throws SvmValidationException {
        makeErrorLabelInvisible(Field.DATUM_ZAHLUNG_3);
        try {
            semesterrechnungModel.setDatumZahlung3(txtDatumZahlung3.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("SemesterrechnungenSuchenController setModelDatumZahlung3 Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtBetragZahlung3(JTextField txtBetragZahlung3) {
        this.txtBetragZahlung3 = txtBetragZahlung3;
        this.txtBetragZahlung3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onBetragZahlung3Event();
            }
        });
        this.txtBetragZahlung3.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onBetragZahlung3Event();
            }
        });
    }

    private void onBetragZahlung3Event() {
        LOGGER.trace("SemesterrechnungenSuchenController Event BetragZahlung3");
        boolean equalFieldAndModelValue = equalsNullSafe(txtBetragZahlung3.getText(), semesterrechnungModel.getBetragZahlung3());
        try {
            setModelBetragZahlung3();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelBetragZahlung3() throws SvmValidationException {
        makeErrorLabelInvisible(Field.BETRAG_ZAHLUNG_3);
        try {
            semesterrechnungModel.setBetragZahlung3(txtBetragZahlung3.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("SemesterrechnungenSuchenController setModelBetragZahlung3 Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTextAreaBemerkungen(JTextArea textAreaBemerkungen) {
        this.textAreaBemerkungen = textAreaBemerkungen;
        textAreaBemerkungen.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onBemerkungenEvent();
            }
        });
        textAreaBemerkungen.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, null);
        textAreaBemerkungen.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, null);
    }

    private void onBemerkungenEvent() {
        LOGGER.trace("SemesterrechnungenSuchenController Event Bemerkungen");
        boolean equalFieldAndModelValue = equalsNullSafe(textAreaBemerkungen.getText(), semesterrechnungModel.getBemerkungen());
        try {
            setModelBemerkungen();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelBemerkungen() throws SvmValidationException {
        makeErrorLabelInvisible(Field.BETRAG_ZAHLUNG_3);
        try {
            semesterrechnungModel.setBemerkungen(textAreaBemerkungen.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("SemesterrechnungenSuchenController setModelBemerkungen Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setErrLblRechnungsdatumVorrechnung(JLabel errLblRechnungsdatumVorrechnung) {
        this.errLblRechnungsdatumVorrechnung = errLblRechnungsdatumVorrechnung;
    }

    public void setErrLblErmaessigungVorrechnung(JLabel errLblErmaessigungVorrechnung) {
        this.errLblErmaessigungVorrechnung = errLblErmaessigungVorrechnung;
    }

    public void setErrLblErmaessigungsgrundVorrechnung(JLabel errLblErmaessigungsgrundVorrechnung) {
        this.errLblErmaessigungsgrundVorrechnung = errLblErmaessigungsgrundVorrechnung;
    }

    public void setErrLblZuschlagVorrechnung(JLabel errLblZuschlagVorrechnung) {
        this.errLblZuschlagVorrechnung = errLblZuschlagVorrechnung;
    }

    public void setErrLblZuschlagsgrundVorrechnung(JLabel errLblZuschlagsgrundVorrechnung) {
        this.errLblZuschlagsgrundVorrechnung = errLblZuschlagsgrundVorrechnung;
    }

    public void setErrLblAnzahlWochenVorrechnung(JLabel errLblAnzahlWochenVorrechnung) {
        this.errLblAnzahlWochenVorrechnung = errLblAnzahlWochenVorrechnung;
    }

    public void setErrLblWochenbetragVorrechnung(JLabel errLblWochenbetragVorrechnung) {
        this.errLblWochenbetragVorrechnung = errLblWochenbetragVorrechnung;
    }

    public void setErrLblRechnungsdatumNachrechnung(JLabel errLblRechnungsdatumNachrechnung) {
        this.errLblRechnungsdatumNachrechnung = errLblRechnungsdatumNachrechnung;
    }

    public void setErrLblErmaessigungNachrechnung(JLabel errLblErmaessigungNachrechnung) {
        this.errLblErmaessigungNachrechnung = errLblErmaessigungNachrechnung;
    }

    public void setErrLblErmaessigungsgrundNachrechnung(JLabel errLblErmaessigungsgrundNachrechnung) {
        this.errLblErmaessigungsgrundNachrechnung = errLblErmaessigungsgrundNachrechnung;
    }

    public void setErrLblZuschlagNachrechnung(JLabel errLblZuschlagNachrechnung) {
        this.errLblZuschlagNachrechnung = errLblZuschlagNachrechnung;
    }

    public void setErrLblZuschlagsgrundNachrechnung(JLabel errLblZuschlagsgrundNachrechnung) {
        this.errLblZuschlagsgrundNachrechnung = errLblZuschlagsgrundNachrechnung;
    }

    public void setErrLblAnzahlWochenNachrechnung(JLabel errLblAnzahlWochenNachrechnung) {
        this.errLblAnzahlWochenNachrechnung = errLblAnzahlWochenNachrechnung;
    }

    public void setErrLblWochenbetragNachrechnung(JLabel errLblWochenbetragNachrechnung) {
        this.errLblWochenbetragNachrechnung = errLblWochenbetragNachrechnung;
    }

    public void setErrLblDatumZahlung1(JLabel errLblDatumZahlung1) {
        this.errLblDatumZahlung1 = errLblDatumZahlung1;
    }

    public void setErrLblBetragZahlung1(JLabel errLblBetragZahlung1) {
        this.errLblBetragZahlung1 = errLblBetragZahlung1;
    }

    public void setErrLblDatumZahlung2(JLabel errLblDatumZahlung2) {
        this.errLblDatumZahlung2 = errLblDatumZahlung2;
    }

    public void setErrLblBetragZahlung2(JLabel errLblBetragZahlung2) {
        this.errLblBetragZahlung2 = errLblBetragZahlung2;
    }

    public void setErrLblDatumZahlung3(JLabel errLblDatumZahlung3) {
        this.errLblDatumZahlung3 = errLblDatumZahlung3;
    }

    public void setErrLblBetragZahlung3(JLabel errLblBetragZahlung3) {
        this.errLblBetragZahlung3 = errLblBetragZahlung3;
    }

    public void setErrLblBemerkungen(JLabel errLblBemerkungen) {
        this.errLblBemerkungen = errLblBemerkungen;
    }

    @Override
    void doPropertyChange(PropertyChangeEvent evt) {
        super.doPropertyChange(evt);
        if (checkIsFieldChange(Field.SEMESTERRECHNUNG_CODE, evt)) {
            comboBoxSemesterrechnungCode.setSelectedItem(semesterrechnungModel.getSemesterrechnungCode());
        }
        else if (checkIsFieldChange(Field.STIPENDIUM, evt)) {
            comboBoxStipendium.setSelectedItem(semesterrechnungModel.getStipendium());
        }
        else if (checkIsFieldChange(Field.GRATISKINDER, evt)) {
            checkBoxGratiskinder.setSelected(semesterrechnungModel.isGratiskinder());
        }
        else if (checkIsFieldChange(Field.RECHNUNGSDATUM_VORRECHNUNG, evt)) {
            txtRechnungsdatumVorrechnung.setText(asString(semesterrechnungModel.getRechnungsdatumVorrechnung()));
        }
        else if (checkIsFieldChange(Field.ERMAESSIGUNG_VORRECHNUNG, evt)) {
            txtErmaessigungVorrechnung.setText(semesterrechnungModel.getErmaessigungVorrechnung() == null ? null : semesterrechnungModel.getErmaessigungVorrechnung().toString());
        }
        else if (checkIsFieldChange(Field.ERMAESSIGUNGSGRUND_VORRECHNUNG, evt)) {
            txtErmaessigungsgrundVorrechnung.setText(semesterrechnungModel.getErmaessigungsgrundVorrechnung());
        }
        else if (checkIsFieldChange(Field.ZUSCHLAG_VORRECHNUNG, evt)) {
            txtZuschlagVorrechnung.setText(semesterrechnungModel.getZuschlagVorrechnung() == null ? null : semesterrechnungModel.getZuschlagVorrechnung().toString());
        }
        else if (checkIsFieldChange(Field.ZUSCHLAGSGRUND_VORRECHNUNG, evt)) {
            txtZuschlagsgrundVorrechnung.setText(semesterrechnungModel.getZuschlagsgrundVorrechnung());
        }
        else if (checkIsFieldChange(Field.ANZAHL_WOCHEN_VORRECHNUNG, evt)) {
            txtAnzahlWochenVorrechnung.setText(Integer.toString(semesterrechnungModel.getAnzahlWochenVorrechnung()));
        }
        else if (checkIsFieldChange(Field.WOCHENBETRAG_VORRECHNUNG, evt)) {
            txtWochenbetragVorrechnung.setText(semesterrechnungModel.getWochenbetragVorrechnung() == null ? null : semesterrechnungModel.getWochenbetragVorrechnung().toString());
        }
        else if (checkIsFieldChange(Field.RECHNUNGSDATUM_NACHRECHNUNG, evt)) {
            txtRechnungsdatumNachrechnung.setText(asString(semesterrechnungModel.getRechnungsdatumNachrechnung()));
        }
        else if (checkIsFieldChange(Field.ERMAESSIGUNG_NACHRECHNUNG, evt)) {
            txtErmaessigungNachrechnung.setText(semesterrechnungModel.getErmaessigungNachrechnung() == null ? null : semesterrechnungModel.getErmaessigungNachrechnung().toString());
        }
        else if (checkIsFieldChange(Field.ERMAESSIGUNGSGRUND_NACHRECHNUNG, evt)) {
            txtErmaessigungsgrundNachrechnung.setText(semesterrechnungModel.getErmaessigungsgrundNachrechnung());
        }
        else if (checkIsFieldChange(Field.ZUSCHLAG_NACHRECHNUNG, evt)) {
            txtZuschlagNachrechnung.setText(semesterrechnungModel.getZuschlagNachrechnung() == null ? null : semesterrechnungModel.getZuschlagNachrechnung().toString());
        }
        else if (checkIsFieldChange(Field.ZUSCHLAGSGRUND_NACHRECHNUNG, evt)) {
            txtZuschlagsgrundNachrechnung.setText(semesterrechnungModel.getZuschlagsgrundNachrechnung());
        }
        else if (checkIsFieldChange(Field.ANZAHL_WOCHEN_NACHRECHNUNG, evt)) {
            txtAnzahlWochenNachrechnung.setText(Integer.toString(semesterrechnungModel.getAnzahlWochenNachrechnung()));
        }
        else if (checkIsFieldChange(Field.WOCHENBETRAG_NACHRECHNUNG, evt)) {
            txtWochenbetragNachrechnung.setText(semesterrechnungModel.getWochenbetragNachrechnung() == null ? null : semesterrechnungModel.getWochenbetragNachrechnung().toString());
        }
        else if (checkIsFieldChange(Field.DATUM_ZAHLUNG_1, evt)) {
            txtDatumZahlung1.setText(asString(semesterrechnungModel.getDatumZahlung1()));
        }
        else if (checkIsFieldChange(Field.BETRAG_ZAHLUNG_1, evt)) {
            txtBetragZahlung1.setText(semesterrechnungModel.getBetragZahlung1() == null ? null : semesterrechnungModel.getBetragZahlung1().toString());
        }
        else if (checkIsFieldChange(Field.DATUM_ZAHLUNG_2, evt)) {
            txtDatumZahlung2.setText(asString(semesterrechnungModel.getDatumZahlung2()));
        }
        else if (checkIsFieldChange(Field.BETRAG_ZAHLUNG_2, evt)) {
            txtBetragZahlung2.setText(semesterrechnungModel.getBetragZahlung2() == null ? null : semesterrechnungModel.getBetragZahlung2().toString());
        }
        else if (checkIsFieldChange(Field.DATUM_ZAHLUNG_3, evt)) {
            txtDatumZahlung3.setText(asString(semesterrechnungModel.getDatumZahlung3()));
        }
        else if (checkIsFieldChange(Field.BETRAG_ZAHLUNG_3, evt)) {
            txtBetragZahlung3.setText(semesterrechnungModel.getBetragZahlung3() == null ? null : semesterrechnungModel.getBetragZahlung3().toString());
        }
        else if (checkIsFieldChange(Field.BEMERKUNGEN, evt)) {
            textAreaBemerkungen.setText(semesterrechnungModel.getBemerkungen());
        }
    }

    @Override
    void validateFields() throws SvmValidationException {
        if (txtRechnungsdatumVorrechnung != null && txtRechnungsdatumVorrechnung.isEnabled()) {
            LOGGER.trace("Validate field RechnungsdatumVorrechnung");
            setModelRechnungsdatumVorrechnung();
        }
        if (txtErmaessigungVorrechnung != null && txtErmaessigungVorrechnung.isEnabled()) {
            LOGGER.trace("Validate field ErmaessigungVorrechnung");
            setModelErmaessigungVorrechnung();
        }
        if (txtErmaessigungsgrundVorrechnung != null && txtErmaessigungsgrundVorrechnung.isEnabled()) {
            LOGGER.trace("Validate field ErmaessigungsgrundVorrechnung");
            setModelErmaessigungsgrundVorrechnung();
        }
        if (txtZuschlagVorrechnung != null && txtZuschlagVorrechnung.isEnabled()) {
            LOGGER.trace("Validate field ZuschlagVorrechnung");
            setModelZuschlagVorrechnung();
        }
        if (txtZuschlagsgrundVorrechnung != null && txtZuschlagsgrundVorrechnung.isEnabled()) {
            LOGGER.trace("Validate field ZuschlagsgrundVorrechnung");
            setModelZuschlagsgrundVorrechnung();
        }
        if (txtAnzahlWochenVorrechnung != null && txtAnzahlWochenVorrechnung.isEnabled()) {
            LOGGER.trace("Validate field AnzahlWochenVorrechnung");
            setModelAnzahlWochenVorrechnung();
        }
        if (txtWochenbetragVorrechnung != null && txtWochenbetragVorrechnung.isEnabled()) {
            LOGGER.trace("Validate field WochenbetragVorrechnung");
            setModelWochenbetragVorrechnung();
        }
        if (txtRechnungsdatumNachrechnung != null && txtRechnungsdatumNachrechnung.isEnabled()) {
            LOGGER.trace("Validate field RechnungsdatumNachrechnung");
            setModelRechnungsdatumNachrechnung();
        }
        if (txtErmaessigungNachrechnung != null && txtErmaessigungNachrechnung.isEnabled()) {
            LOGGER.trace("Validate field ErmaessigungNachrechnung");
            setModelErmaessigungNachrechnung();
        }
        if (txtErmaessigungsgrundNachrechnung != null && txtErmaessigungsgrundNachrechnung.isEnabled()) {
            LOGGER.trace("Validate field ErmaessigungsgrundNachrechnung");
            setModelErmaessigungsgrundNachrechnung();
        }
        if (txtZuschlagNachrechnung != null && txtZuschlagNachrechnung.isEnabled()) {
            LOGGER.trace("Validate field ZuschlagNachrechnung");
            setModelZuschlagNachrechnung();
        }
        if (txtZuschlagsgrundNachrechnung != null && txtZuschlagsgrundNachrechnung.isEnabled()) {
            LOGGER.trace("Validate field ZuschlagsgrundNachrechnung");
            setModelZuschlagsgrundNachrechnung();
        }
        if (txtAnzahlWochenNachrechnung != null && txtAnzahlWochenNachrechnung.isEnabled()) {
            LOGGER.trace("Validate field AnzahlWochenNachrechnung");
            setModelAnzahlWochenNachrechnung();
        }
        if (txtWochenbetragNachrechnung != null && txtWochenbetragNachrechnung.isEnabled()) {
            LOGGER.trace("Validate field WochenbetragNachrechnung");
            setModelWochenbetragNachrechnung();
        }
        if (txtDatumZahlung1 != null && txtDatumZahlung1.isEnabled()) {
            LOGGER.trace("Validate field DatumZahlung1");
            setModelDatumZahlung1();
        }
        if (txtBetragZahlung1 != null && txtBetragZahlung1.isEnabled()) {
            LOGGER.trace("Validate field BetragZahlung1");
            setModelBetragZahlung1();
        }
        if (txtDatumZahlung2 != null && txtDatumZahlung2.isEnabled()) {
            LOGGER.trace("Validate field DatumZahlung2");
            setModelDatumZahlung2();
        }
        if (txtBetragZahlung2 != null && txtBetragZahlung2.isEnabled()) {
            LOGGER.trace("Validate field BetragZahlung2");
            setModelBetragZahlung2();
        }
        if (txtDatumZahlung3 != null && txtDatumZahlung3.isEnabled()) {
            LOGGER.trace("Validate field DatumZahlung3");
            setModelDatumZahlung3();
        }
        if (txtBetragZahlung3 != null && txtBetragZahlung3.isEnabled()) {
            LOGGER.trace("Validate field BetragZahlung3");
            setModelBetragZahlung3();
        }
        if (textAreaBemerkungen != null && textAreaBemerkungen.isEnabled()) {
            LOGGER.trace("Validate field Bemerkungen");
            setModelBemerkungen();
        }
    }

    @Override
    void showErrMsg(SvmValidationException e) {
        if (e.getAffectedFields().contains(Field.RECHNUNGSDATUM_VORRECHNUNG)) {
            errLblRechnungsdatumVorrechnung.setVisible(true);
            errLblRechnungsdatumVorrechnung.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ERMAESSIGUNG_VORRECHNUNG)) {
            errLblErmaessigungVorrechnung.setVisible(true);
            errLblErmaessigungVorrechnung.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ERMAESSIGUNGSGRUND_VORRECHNUNG)) {
            errLblErmaessigungsgrundVorrechnung.setVisible(true);
            errLblErmaessigungsgrundVorrechnung.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ZUSCHLAG_VORRECHNUNG)) {
            errLblZuschlagVorrechnung.setVisible(true);
            errLblZuschlagVorrechnung.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ZUSCHLAGSGRUND_VORRECHNUNG)) {
            errLblZuschlagsgrundVorrechnung.setVisible(true);
            errLblZuschlagsgrundVorrechnung.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ANZAHL_WOCHEN_VORRECHNUNG)) {
            errLblAnzahlWochenVorrechnung.setVisible(true);
            errLblAnzahlWochenVorrechnung.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.WOCHENBETRAG_VORRECHNUNG)) {
            errLblWochenbetragVorrechnung.setVisible(true);
            errLblWochenbetragVorrechnung.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.RECHNUNGSDATUM_NACHRECHNUNG)) {
            errLblRechnungsdatumNachrechnung.setVisible(true);
            errLblRechnungsdatumNachrechnung.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ERMAESSIGUNG_NACHRECHNUNG)) {
            errLblErmaessigungNachrechnung.setVisible(true);
            errLblErmaessigungNachrechnung.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ERMAESSIGUNGSGRUND_NACHRECHNUNG)) {
            errLblErmaessigungsgrundNachrechnung.setVisible(true);
            errLblErmaessigungsgrundNachrechnung.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ZUSCHLAG_NACHRECHNUNG)) {
            errLblZuschlagNachrechnung.setVisible(true);
            errLblZuschlagNachrechnung.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ZUSCHLAGSGRUND_NACHRECHNUNG)) {
            errLblZuschlagsgrundNachrechnung.setVisible(true);
            errLblZuschlagsgrundNachrechnung.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ANZAHL_WOCHEN_NACHRECHNUNG)) {
            errLblAnzahlWochenNachrechnung.setVisible(true);
            errLblAnzahlWochenNachrechnung.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.WOCHENBETRAG_NACHRECHNUNG)) {
            errLblWochenbetragNachrechnung.setVisible(true);
            errLblWochenbetragNachrechnung.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.DATUM_ZAHLUNG_1)) {
            errLblDatumZahlung1.setVisible(true);
            errLblDatumZahlung1.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.BETRAG_ZAHLUNG_1)) {
            errLblBetragZahlung1.setVisible(true);
            errLblBetragZahlung1.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.DATUM_ZAHLUNG_2)) {
            errLblDatumZahlung2.setVisible(true);
            errLblDatumZahlung2.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.BETRAG_ZAHLUNG_2)) {
            errLblBetragZahlung2.setVisible(true);
            errLblBetragZahlung2.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.DATUM_ZAHLUNG_3)) {
            errLblDatumZahlung3.setVisible(true);
            errLblDatumZahlung3.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.BETRAG_ZAHLUNG_3)) {
            errLblBetragZahlung3.setVisible(true);
            errLblBetragZahlung3.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.BEMERKUNGEN)) {
            errLblBemerkungen.setVisible(true);
            errLblBemerkungen.setText(e.getMessage());
        }
    }

    @Override
    void showErrMsgAsToolTip(SvmValidationException e) {
        if (e.getAffectedFields().contains(Field.RECHNUNGSDATUM_VORRECHNUNG)) {
            txtRechnungsdatumVorrechnung.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ERMAESSIGUNG_VORRECHNUNG)) {
            txtErmaessigungVorrechnung.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ERMAESSIGUNGSGRUND_VORRECHNUNG)) {
            txtErmaessigungsgrundVorrechnung.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ZUSCHLAG_VORRECHNUNG)) {
            txtZuschlagVorrechnung.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ZUSCHLAGSGRUND_VORRECHNUNG)) {
            txtZuschlagsgrundVorrechnung.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ANZAHL_WOCHEN_VORRECHNUNG)) {
            txtAnzahlWochenVorrechnung.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.WOCHENBETRAG_VORRECHNUNG)) {
            txtWochenbetragVorrechnung.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.RECHNUNGSDATUM_NACHRECHNUNG)) {
            txtRechnungsdatumNachrechnung.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ERMAESSIGUNG_NACHRECHNUNG)) {
            txtErmaessigungNachrechnung.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ERMAESSIGUNGSGRUND_NACHRECHNUNG)) {
            txtErmaessigungsgrundNachrechnung.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ZUSCHLAG_NACHRECHNUNG)) {
            txtZuschlagNachrechnung.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ZUSCHLAGSGRUND_NACHRECHNUNG)) {
            txtZuschlagsgrundNachrechnung.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ANZAHL_WOCHEN_NACHRECHNUNG)) {
            txtAnzahlWochenNachrechnung.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.WOCHENBETRAG_NACHRECHNUNG)) {
            txtWochenbetragNachrechnung.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.DATUM_ZAHLUNG_1)) {
            txtDatumZahlung1.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.BETRAG_ZAHLUNG_1)) {
            txtBetragZahlung1.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.DATUM_ZAHLUNG_2)) {
            txtDatumZahlung2.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.BETRAG_ZAHLUNG_2)) {
            txtBetragZahlung2.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.DATUM_ZAHLUNG_3)) {
            txtDatumZahlung3.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.BETRAG_ZAHLUNG_3)) {
            txtBetragZahlung3.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.BEMERKUNGEN)) {
            textAreaBemerkungen.setToolTipText(e.getMessage());
        }
    }

    @Override
    public void makeErrorLabelsInvisible(Set<Field> fields) {
        if (fields.contains(Field.ALLE) || fields.contains(Field.RECHNUNGSDATUM_VORRECHNUNG)) {
            if (errLblRechnungsdatumVorrechnung != null) {
                errLblRechnungsdatumVorrechnung.setVisible(false);
            }
            if (txtRechnungsdatumVorrechnung != null) {
                txtRechnungsdatumVorrechnung.setToolTipText(null);
            }
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.ERMAESSIGUNG_VORRECHNUNG)) {
            if (errLblErmaessigungVorrechnung != null) {
                errLblErmaessigungVorrechnung.setVisible(false);
            }
            if (txtErmaessigungVorrechnung != null) {
                txtErmaessigungVorrechnung.setToolTipText(null);
            }
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.ERMAESSIGUNGSGRUND_VORRECHNUNG)) {
            if (errLblErmaessigungsgrundVorrechnung != null) {
                errLblErmaessigungsgrundVorrechnung.setVisible(false);
            }
            if (txtErmaessigungsgrundVorrechnung != null) {
                txtErmaessigungsgrundVorrechnung.setToolTipText(null);
            }
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.ZUSCHLAG_VORRECHNUNG)) {
            if (errLblZuschlagVorrechnung != null) {
                errLblZuschlagVorrechnung.setVisible(false);
            }
            if (txtZuschlagVorrechnung != null) {
                txtZuschlagVorrechnung.setToolTipText(null);
            }
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.ZUSCHLAGSGRUND_VORRECHNUNG)) {
            if (errLblZuschlagsgrundVorrechnung != null) {
                errLblZuschlagsgrundVorrechnung.setVisible(false);
            }
            if (txtZuschlagsgrundVorrechnung != null) {
                txtZuschlagsgrundVorrechnung.setToolTipText(null);
            }
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.ANZAHL_WOCHEN_VORRECHNUNG)) {
            if (errLblAnzahlWochenVorrechnung != null) {
                errLblAnzahlWochenVorrechnung.setVisible(false);
            }
            if (txtAnzahlWochenVorrechnung != null) {
                txtAnzahlWochenVorrechnung.setToolTipText(null);
            }
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.WOCHENBETRAG_VORRECHNUNG)) {
            if (errLblWochenbetragVorrechnung != null) {
                errLblWochenbetragVorrechnung.setVisible(false);
            }
            if (txtWochenbetragVorrechnung != null) {
                txtWochenbetragVorrechnung.setToolTipText(null);
            }
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.RECHNUNGSDATUM_NACHRECHNUNG)) {
            if (errLblRechnungsdatumNachrechnung != null) {
                errLblRechnungsdatumNachrechnung.setVisible(false);
            }
            if (txtRechnungsdatumNachrechnung != null) {
                txtRechnungsdatumNachrechnung.setToolTipText(null);
            }
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.ERMAESSIGUNG_NACHRECHNUNG)) {
            if (errLblErmaessigungNachrechnung != null) {
                errLblErmaessigungNachrechnung.setVisible(false);
            }
            if (txtErmaessigungNachrechnung != null) {
                txtErmaessigungNachrechnung.setToolTipText(null);
            }
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.ERMAESSIGUNGSGRUND_NACHRECHNUNG)) {
            if (errLblErmaessigungsgrundNachrechnung != null) {
                errLblErmaessigungsgrundNachrechnung.setVisible(false);
            }
            if (txtErmaessigungsgrundNachrechnung != null) {
                txtErmaessigungsgrundNachrechnung.setToolTipText(null);
            }
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.ZUSCHLAG_NACHRECHNUNG)) {
            if (errLblZuschlagNachrechnung != null) {
                errLblZuschlagNachrechnung.setVisible(false);
            }
            if (txtZuschlagNachrechnung != null) {
                txtZuschlagNachrechnung.setToolTipText(null);
            }
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.ZUSCHLAGSGRUND_NACHRECHNUNG)) {
            if (errLblZuschlagsgrundNachrechnung != null) {
                errLblZuschlagsgrundNachrechnung.setVisible(false);
            }
            if (txtZuschlagsgrundNachrechnung != null) {
                txtZuschlagsgrundNachrechnung.setToolTipText(null);
            }
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.ANZAHL_WOCHEN_NACHRECHNUNG)) {
            if (errLblAnzahlWochenNachrechnung != null) {
                errLblAnzahlWochenNachrechnung.setVisible(false);
            }
            if (txtAnzahlWochenNachrechnung != null) {
                txtAnzahlWochenNachrechnung.setToolTipText(null);
            }
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.WOCHENBETRAG_NACHRECHNUNG)) {
            if (errLblWochenbetragNachrechnung != null) {
                errLblWochenbetragNachrechnung.setVisible(false);
            }
            if (txtWochenbetragNachrechnung != null) {
                txtWochenbetragNachrechnung.setToolTipText(null);
            }
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.DATUM_ZAHLUNG_1)) {
            if (errLblDatumZahlung1 != null) {
                errLblDatumZahlung1.setVisible(false);
            }
            if (txtDatumZahlung1 != null) {
                txtDatumZahlung1.setToolTipText(null);
            }
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.BETRAG_ZAHLUNG_1)) {
            if (errLblBetragZahlung1 != null) {
                errLblBetragZahlung1.setVisible(false);
            }
            if (txtBetragZahlung1 != null) {
                txtBetragZahlung1.setToolTipText(null);
            }
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.DATUM_ZAHLUNG_2)) {
            if (errLblDatumZahlung2 != null) {
                errLblDatumZahlung2.setVisible(false);
            }
            if (txtDatumZahlung2 != null) {
                txtDatumZahlung2.setToolTipText(null);
            }
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.BETRAG_ZAHLUNG_2)) {
            if (errLblBetragZahlung2 != null) {
                errLblBetragZahlung2.setVisible(false);
            }
            if (txtBetragZahlung2 != null) {
                txtBetragZahlung2.setToolTipText(null);
            }
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.DATUM_ZAHLUNG_3)) {
            if (errLblDatumZahlung3 != null) {
                errLblDatumZahlung3.setVisible(false);
            }
            if (txtDatumZahlung3 != null) {
                txtDatumZahlung3.setToolTipText(null);
            }
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.BETRAG_ZAHLUNG_3)) {
            if (errLblBetragZahlung3 != null) {
                errLblBetragZahlung3.setVisible(false);
            }
            if (txtBetragZahlung3 != null) {
                txtBetragZahlung3.setToolTipText(null);
            }
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.BEMERKUNGEN)) {
            if (errLblBemerkungen != null) {
                errLblBemerkungen.setVisible(false);
            }
            if (textAreaBemerkungen != null) {
                textAreaBemerkungen.setToolTipText(null);
            }
        }
    }

    @Override
    public void disableFields(boolean disable, Set<Field> fields) {
    }

}
