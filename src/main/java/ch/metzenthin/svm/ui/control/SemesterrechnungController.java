package ch.metzenthin.svm.ui.control;

import static ch.metzenthin.svm.common.utils.Converter.asString;
import static ch.metzenthin.svm.common.utils.SimpleValidator.equalsNullSafe;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.common.datatypes.Stipendium;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.SemesterrechnungModel;
import ch.metzenthin.svm.persistence.entities.SemesterrechnungCode;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.util.Set;
import javax.swing.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Martin Schraner
 */
@SuppressWarnings("LoggingSimilarMessage")
public abstract class SemesterrechnungController extends AbstractController {

  private static final Logger LOGGER = LogManager.getLogger(SemesterrechnungController.class);
  private static final String VALIDIERUNG_WEGEN_EQUAL_FIELD_AND_MODEL_VALUE =
      "Validierung wegen equalFieldAndModelValue";

  // Möglichkeit zum Umschalten des validation modes (nicht dynamisch)
  private static final boolean MODEL_VALIDATION_MODE = false;

  JTextField txtRechnungsdatumVorrechnung;
  JTextField txtErmaessigungVorrechnung;
  JTextField txtErmaessigungsgrundVorrechnung;
  JTextField txtZuschlagVorrechnung;
  JTextField txtZuschlagsgrundVorrechnung;
  JTextField txtAnzahlWochenVorrechnung;
  JTextField txtWochenbetragVorrechnung;
  JTextField txtDatumZahlung1Vorrechnung;
  JTextField txtBetragZahlung1Vorrechnung;
  JTextField txtDatumZahlung2Vorrechnung;
  JTextField txtBetragZahlung2Vorrechnung;
  JTextField txtDatumZahlung3Vorrechnung;
  JTextField txtBetragZahlung3Vorrechnung;
  JTextField txtRechnungsdatumNachrechnung;
  JTextField txtErmaessigungNachrechnung;
  JTextField txtErmaessigungsgrundNachrechnung;
  JTextField txtZuschlagNachrechnung;
  JTextField txtZuschlagsgrundNachrechnung;
  JTextField txtAnzahlWochenNachrechnung;
  JTextField txtWochenbetragNachrechnung;
  JTextField txtDatumZahlung1Nachrechnung;
  JTextField txtBetragZahlung1Nachrechnung;
  JTextField txtDatumZahlung2Nachrechnung;
  JTextField txtBetragZahlung2Nachrechnung;
  JTextField txtDatumZahlung3Nachrechnung;
  JTextField txtBetragZahlung3Nachrechnung;
  JTextArea textAreaBemerkungen;
  private JLabel errLblRechnungsdatumVorrechnung;
  private JLabel errLblErmaessigungVorrechnung;
  private JLabel errLblErmaessigungsgrundVorrechnung;
  private JLabel errLblZuschlagVorrechnung;
  private JLabel errLblZuschlagsgrundVorrechnung;
  private JLabel errLblAnzahlWochenVorrechnung;
  private JLabel errLblWochenbetragVorrechnung;
  private JLabel errLblDatumZahlung1Vorrechnung;
  private JLabel errLblBetragZahlung1Vorrechnung;
  private JLabel errLblDatumZahlung2Vorrechnung;
  private JLabel errLblBetragZahlung2Vorrechnung;
  private JLabel errLblDatumZahlung3Vorrechnung;
  private JLabel errLblBetragZahlung3Vorrechnung;
  private JLabel errLblRechnungsdatumNachrechnung;
  private JLabel errLblErmaessigungNachrechnung;
  private JLabel errLblErmaessigungsgrundNachrechnung;
  private JLabel errLblZuschlagNachrechnung;
  private JLabel errLblZuschlagsgrundNachrechnung;
  private JLabel errLblAnzahlWochenNachrechnung;
  private JLabel errLblWochenbetragNachrechnung;
  private JLabel errLblDatumZahlung1Nachrechnung;
  private JLabel errLblBetragZahlung1Nachrechnung;
  private JLabel errLblDatumZahlung2Nachrechnung;
  private JLabel errLblBetragZahlung2Nachrechnung;
  private JLabel errLblDatumZahlung3Nachrechnung;
  private JLabel errLblBetragZahlung3Nachrechnung;
  private JLabel errLblStipendium;
  private JLabel errLblBemerkungen;
  private JComboBox<SemesterrechnungCode> comboBoxSemesterrechnungCode;
  private JComboBox<Stipendium> comboBoxStipendium;
  private JCheckBox checkBoxGratiskinder;
  SemesterrechnungModel semesterrechnungModel;
  private final boolean defaultButtonEnabled;
  private final SvmContext svmContext;

  protected SemesterrechnungController(
      SvmContext svmContext,
      SemesterrechnungModel semesterrechnungModel,
      boolean defaultButtonEnabled) {
    super(semesterrechnungModel);
    this.svmContext = svmContext;
    this.semesterrechnungModel = semesterrechnungModel;
    this.defaultButtonEnabled = defaultButtonEnabled;
    this.semesterrechnungModel.addPropertyChangeListener(this);
    this.semesterrechnungModel.addDisableFieldsListener(this);
    this.semesterrechnungModel.addMakeErrorLabelsInvisibleListener(this);
    this.setModelValidationMode(MODEL_VALIDATION_MODE);
  }

  public void setComboBoxSemesterrechnungCode(JComboBox<SemesterrechnungCode> comboBoxCode) {
    this.comboBoxSemesterrechnungCode = comboBoxCode;
    SemesterrechnungCode[] selectableSemesterrechnungCodes =
        semesterrechnungModel.getSelectableSemesterrechnungCodes(svmContext.getSvmModel());
    comboBoxCode.setModel(new DefaultComboBoxModel<>(selectableSemesterrechnungCodes));
    // Model initialisieren mit erstem ComboBox-Wert
    semesterrechnungModel.setSemesterrechnungCode(selectableSemesterrechnungCodes[0]);
    this.comboBoxSemesterrechnungCode.addActionListener(e -> onSemesterrechnungCodeSelected());
  }

  private void onSemesterrechnungCodeSelected() {
    LOGGER.trace(
        "SemesterrechnungSuchenController Event SemesterrechnungCode selected={}",
        comboBoxSemesterrechnungCode.getSelectedItem());
    setModelSemesterrechnungCode();
  }

  private void setModelSemesterrechnungCode() {
    semesterrechnungModel.setSemesterrechnungCode(
        (SemesterrechnungCode) comboBoxSemesterrechnungCode.getSelectedItem());
  }

  public void setComboBoxStipendium(JComboBox<Stipendium> comboBoxStipendium) {
    this.comboBoxStipendium = comboBoxStipendium;
    Stipendium[] selectableStipendien = semesterrechnungModel.getSelectableStipendien();
    comboBoxStipendium.setModel(new DefaultComboBoxModel<>(selectableStipendien));
    comboBoxStipendium.addActionListener(e -> onStipendiumSelected());
    // Model initialisieren mit erstem ComboBox-Wert
    semesterrechnungModel.setStipendium(selectableStipendien[0]);
  }

  private void onStipendiumSelected() {
    LOGGER.trace(
        "SemesterrechnungController Event Stipendium selected={}",
        comboBoxStipendium.getSelectedItem());
    boolean equalFieldAndModelValue =
        equalsNullSafe(comboBoxStipendium.getSelectedItem(), semesterrechnungModel.getStipendium());
    setModelStipendium();
    if (equalFieldAndModelValue && isModelValidationMode()) {
      // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb
      // muss hier die Validierung angestossen werden.
      LOGGER.trace(VALIDIERUNG_WEGEN_EQUAL_FIELD_AND_MODEL_VALUE);
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
    this.checkBoxGratiskinder.addItemListener(e -> onGratiskinderEvent());
    // Initialisierung
    semesterrechnungModel.setGratiskinder(false);
  }

  private void onGratiskinderEvent() {
    LOGGER.trace(
        "SemesterrechnungController Event Gratiskinder. Selected={}",
        checkBoxGratiskinder.isSelected());
    setModelGratiskinder();
  }

  private void setModelGratiskinder() {
    semesterrechnungModel.setGratiskinder(checkBoxGratiskinder.isSelected());
  }

  public void setTxtRechnungsdatumVorrechnung(JTextField txtRechnungsdatumVorrechnung) {
    this.txtRechnungsdatumVorrechnung = txtRechnungsdatumVorrechnung;
    if (!defaultButtonEnabled) {
      this.txtRechnungsdatumVorrechnung.addActionListener(e -> onRechnungsdatumVorrechnungEvent());
    }
    this.txtRechnungsdatumVorrechnung.addFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onRechnungsdatumVorrechnungEvent();
          }
        });
  }

  private void onRechnungsdatumVorrechnungEvent() {
    LOGGER.trace("SemesterrechnungController Event RechnungsdatumVorrechnung");
    boolean equalFieldAndModelValue =
        equalsNullSafe(
            txtRechnungsdatumVorrechnung.getText(),
            semesterrechnungModel.getRechnungsdatumVorrechnung());
    try {
      setModelRechnungsdatumVorrechnung();
    } catch (SvmValidationException e) {
      return;
    }
    if (equalFieldAndModelValue && isModelValidationMode()) {
      // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb
      // muss hier die Validierung angestossen werden.
      LOGGER.trace(VALIDIERUNG_WEGEN_EQUAL_FIELD_AND_MODEL_VALUE);
      validate();
    }
  }

  private void setModelRechnungsdatumVorrechnung() throws SvmValidationException {
    makeErrorLabelInvisible(Field.RECHNUNGSDATUM_VORRECHNUNG);
    try {
      semesterrechnungModel.setRechnungsdatumVorrechnung(txtRechnungsdatumVorrechnung.getText());
    } catch (SvmValidationException e) {
      LOGGER.trace(
          "SemesterrechnungController setModelRechnungsdatumVorrechnung Exception={}",
          e.getMessage());
      showErrMsg(e);
      throw e;
    }
  }

  public void setTxtErmaessigungVorrechnung(JTextField txtErmaessigungVorrechnung) {
    this.txtErmaessigungVorrechnung = txtErmaessigungVorrechnung;
    if (!defaultButtonEnabled) {
      this.txtErmaessigungVorrechnung.addActionListener(e -> onErmaessigungVorrechnungEvent());
    }
    this.txtErmaessigungVorrechnung.addFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onErmaessigungVorrechnungEvent();
          }
        });
  }

  private void onErmaessigungVorrechnungEvent() {
    LOGGER.trace("SemesterrechnungController Event Ermaessigung");
    boolean equalFieldAndModelValue =
        equalsNullSafe(
            txtErmaessigungVorrechnung.getText(),
            semesterrechnungModel.getErmaessigungVorrechnung());
    try {
      setModelErmaessigungVorrechnung();
    } catch (SvmValidationException e) {
      return;
    }
    if (equalFieldAndModelValue && isModelValidationMode()) {
      // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb
      // muss hier die Validierung angestossen werden.
      LOGGER.trace(VALIDIERUNG_WEGEN_EQUAL_FIELD_AND_MODEL_VALUE);
      validate();
    }
  }

  private void setModelErmaessigungVorrechnung() throws SvmValidationException {
    makeErrorLabelInvisible(Field.ERMAESSIGUNG_VORRECHNUNG);
    try {
      semesterrechnungModel.setErmaessigungVorrechnung(txtErmaessigungVorrechnung.getText());
    } catch (SvmValidationException e) {
      LOGGER.trace(
          "SemesterrechnungController setModelErmaessigungVorrechnung Exception={}",
          e.getMessage());
      showErrMsg(e);
      throw e;
    }
  }

  public void setTxtErmaessigungsgrundVorrechnung(JTextField txtErmaessigungsgrundVorrechnung) {
    this.txtErmaessigungsgrundVorrechnung = txtErmaessigungsgrundVorrechnung;
    if (!defaultButtonEnabled) {
      this.txtErmaessigungsgrundVorrechnung.addActionListener(
          e -> onErmaessigungsgrundVorrechnungEvent());
    }
    this.txtErmaessigungsgrundVorrechnung.addFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onErmaessigungsgrundVorrechnungEvent();
          }
        });
  }

  private void onErmaessigungsgrundVorrechnungEvent() {
    LOGGER.trace("SemesterrechnungController Event Ermaessigungsgrund");
    boolean equalFieldAndModelValue =
        equalsNullSafe(
            txtErmaessigungsgrundVorrechnung.getText(),
            semesterrechnungModel.getErmaessigungsgrundVorrechnung());
    try {
      setModelErmaessigungsgrundVorrechnung();
    } catch (SvmValidationException e) {
      return;
    }
    if (equalFieldAndModelValue && isModelValidationMode()) {
      // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb
      // muss hier die Validierung angestossen werden.
      LOGGER.trace(VALIDIERUNG_WEGEN_EQUAL_FIELD_AND_MODEL_VALUE);
      validate();
    }
  }

  private void setModelErmaessigungsgrundVorrechnung() throws SvmValidationException {
    makeErrorLabelInvisible(Field.ERMAESSIGUNGSGRUND_VORRECHNUNG);
    try {
      semesterrechnungModel.setErmaessigungsgrundVorrechnung(
          txtErmaessigungsgrundVorrechnung.getText());
    } catch (SvmValidationException e) {
      LOGGER.trace(
          "SemesterrechnungController setModelErmaessigungsgrundVorrechnung Exception={}",
          e.getMessage());
      showErrMsg(e);
      throw e;
    }
  }

  public void setTxtZuschlagVorrechnung(JTextField txtZuschlagVorrechnung) {
    this.txtZuschlagVorrechnung = txtZuschlagVorrechnung;
    if (!defaultButtonEnabled) {
      this.txtZuschlagVorrechnung.addActionListener(e -> onZuschlagVorrechnungEvent());
    }
    this.txtZuschlagVorrechnung.addFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onZuschlagVorrechnungEvent();
          }
        });
  }

  private void onZuschlagVorrechnungEvent() {
    LOGGER.trace("SemesterrechnungController Event Zuschlag");
    boolean equalFieldAndModelValue =
        equalsNullSafe(
            txtZuschlagVorrechnung.getText(), semesterrechnungModel.getZuschlagVorrechnung());
    try {
      setModelZuschlagVorrechnung();
    } catch (SvmValidationException e) {
      return;
    }
    if (equalFieldAndModelValue && isModelValidationMode()) {
      // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb
      // muss hier die Validierung angestossen werden.
      LOGGER.trace(VALIDIERUNG_WEGEN_EQUAL_FIELD_AND_MODEL_VALUE);
      validate();
    }
  }

  private void setModelZuschlagVorrechnung() throws SvmValidationException {
    makeErrorLabelInvisible(Field.ZUSCHLAG_VORRECHNUNG);
    try {
      semesterrechnungModel.setZuschlagVorrechnung(txtZuschlagVorrechnung.getText());
    } catch (SvmValidationException e) {
      LOGGER.trace(
          "SemesterrechnungController setModelZuschlagVorrechnung Exception={}", e.getMessage());
      showErrMsg(e);
      throw e;
    }
  }

  public void setTxtZuschlagsgrundVorrechnung(JTextField txtZuschlagsgrundVorrechnung) {
    this.txtZuschlagsgrundVorrechnung = txtZuschlagsgrundVorrechnung;
    if (!defaultButtonEnabled) {
      this.txtZuschlagsgrundVorrechnung.addActionListener(e -> onZuschlagsgrundVorrechnungEvent());
    }
    this.txtZuschlagsgrundVorrechnung.addFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onZuschlagsgrundVorrechnungEvent();
          }
        });
  }

  private void onZuschlagsgrundVorrechnungEvent() {
    LOGGER.trace("SemesterrechnungController Event Zuschlagsgrund");
    boolean equalFieldAndModelValue =
        equalsNullSafe(
            txtZuschlagsgrundVorrechnung.getText(),
            semesterrechnungModel.getZuschlagsgrundVorrechnung());
    try {
      setModelZuschlagsgrundVorrechnung();
    } catch (SvmValidationException e) {
      return;
    }
    if (equalFieldAndModelValue && isModelValidationMode()) {
      // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb
      // muss hier die Validierung angestossen werden.
      LOGGER.trace(VALIDIERUNG_WEGEN_EQUAL_FIELD_AND_MODEL_VALUE);
      validate();
    }
  }

  private void setModelZuschlagsgrundVorrechnung() throws SvmValidationException {
    makeErrorLabelInvisible(Field.ZUSCHLAGSGRUND_VORRECHNUNG);
    try {
      semesterrechnungModel.setZuschlagsgrundVorrechnung(txtZuschlagsgrundVorrechnung.getText());
    } catch (SvmValidationException e) {
      LOGGER.trace(
          "SemesterrechnungController setModelZuschlagsgrundVorrechnung Exception={}",
          e.getMessage());
      showErrMsg(e);
      throw e;
    }
  }

  public void setTxtAnzahlWochenVorrechnung(JTextField txtAnzahlWochenVorrechnung) {
    this.txtAnzahlWochenVorrechnung = txtAnzahlWochenVorrechnung;
    if (!defaultButtonEnabled) {
      this.txtAnzahlWochenVorrechnung.addActionListener(e -> onAnzahlWochenVorrechnungEvent());
    }
    this.txtAnzahlWochenVorrechnung.addFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onAnzahlWochenVorrechnungEvent();
          }
        });
  }

  private void onAnzahlWochenVorrechnungEvent() {
    LOGGER.trace("SemesterrechnungController Event AnzahlWochen");
    boolean equalFieldAndModelValue =
        equalsNullSafe(
            txtAnzahlWochenVorrechnung.getText(),
            semesterrechnungModel.getAnzahlWochenVorrechnung());
    try {
      setModelAnzahlWochenVorrechnung();
    } catch (SvmValidationException e) {
      return;
    }
    if (equalFieldAndModelValue && isModelValidationMode()) {
      // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb
      // muss hier die Validierung angestossen werden.
      LOGGER.trace(VALIDIERUNG_WEGEN_EQUAL_FIELD_AND_MODEL_VALUE);
      validate();
    }
  }

  private void setModelAnzahlWochenVorrechnung() throws SvmValidationException {
    makeErrorLabelInvisible(Field.ANZAHL_WOCHEN_VORRECHNUNG);
    try {
      semesterrechnungModel.setAnzahlWochenVorrechnung(txtAnzahlWochenVorrechnung.getText());
    } catch (SvmValidationException e) {
      LOGGER.trace(
          "SemesterrechnungController setModelAnzahlWochenVorrechnung Exception={}",
          e.getMessage());
      showErrMsg(e);
      throw e;
    }
  }

  public void setTxtWochenbetragVorrechnung(JTextField txtWochenbetragVorrechnung) {
    this.txtWochenbetragVorrechnung = txtWochenbetragVorrechnung;
    if (!defaultButtonEnabled) {
      this.txtWochenbetragVorrechnung.addActionListener(e -> onWochenbetragVorrechnungEvent());
    }
    this.txtWochenbetragVorrechnung.addFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onWochenbetragVorrechnungEvent();
          }
        });
  }

  private void onWochenbetragVorrechnungEvent() {
    LOGGER.trace("SemesterrechnungController Event WochenbetragVorrechnung");
    boolean equalFieldAndModelValue =
        equalsNullSafe(
            txtWochenbetragVorrechnung.getText(),
            semesterrechnungModel.getWochenbetragVorrechnung());
    try {
      setModelWochenbetragVorrechnung();
    } catch (SvmValidationException e) {
      return;
    }
    if (equalFieldAndModelValue && isModelValidationMode()) {
      // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb
      // muss hier die Validierung angestossen werden.
      LOGGER.trace(VALIDIERUNG_WEGEN_EQUAL_FIELD_AND_MODEL_VALUE);
      validate();
    }
  }

  private void setModelWochenbetragVorrechnung() throws SvmValidationException {
    makeErrorLabelInvisible(Field.WOCHENBETRAG_VORRECHNUNG);
    try {
      semesterrechnungModel.setWochenbetragVorrechnung(txtWochenbetragVorrechnung.getText());
    } catch (SvmValidationException e) {
      LOGGER.trace(
          "SemesterrechnungController setModelWochenbetragVorrechnung Exception={}",
          e.getMessage());
      showErrMsg(e);
      throw e;
    }
  }

  public void setTxtDatumZahlung1Vorrechnung(JTextField txtDatumZahlung1Vorrechnung) {
    this.txtDatumZahlung1Vorrechnung = txtDatumZahlung1Vorrechnung;
    if (!defaultButtonEnabled) {
      this.txtDatumZahlung1Vorrechnung.addActionListener(e -> onDatumZahlung1VorrechnungEvent());
    }
    this.txtDatumZahlung1Vorrechnung.addFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onDatumZahlung1VorrechnungEvent();
          }
        });
  }

  private void onDatumZahlung1VorrechnungEvent() {
    LOGGER.trace("SemesterrechnungController Event DatumZahlung1Vorrechnung");
    boolean equalFieldAndModelValue =
        equalsNullSafe(
            txtDatumZahlung1Vorrechnung.getText(),
            semesterrechnungModel.getDatumZahlung1Vorrechnung());
    try {
      setModelDatumZahlung1Vorrechnung();
    } catch (SvmValidationException e) {
      return;
    }
    if (equalFieldAndModelValue && isModelValidationMode()) {
      // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb
      // muss hier die Validierung angestossen werden.
      LOGGER.trace(VALIDIERUNG_WEGEN_EQUAL_FIELD_AND_MODEL_VALUE);
      validate();
    }
  }

  private void setModelDatumZahlung1Vorrechnung() throws SvmValidationException {
    makeErrorLabelInvisible(Field.DATUM_ZAHLUNG_1_VORRECHNUNG);
    try {
      semesterrechnungModel.setDatumZahlung1Vorrechnung(txtDatumZahlung1Vorrechnung.getText());
    } catch (SvmValidationException e) {
      LOGGER.trace(
          "SemesterrechnungController setModelDatumZahlung1Vorrechnung Exception={}",
          e.getMessage());
      showErrMsg(e);
      throw e;
    }
  }

  public void setTxtBetragZahlung1Vorrechnung(JTextField txtBetragZahlung1Vorrechnung) {
    this.txtBetragZahlung1Vorrechnung = txtBetragZahlung1Vorrechnung;
    if (!defaultButtonEnabled) {
      this.txtBetragZahlung1Vorrechnung.addActionListener(e -> onBetragZahlung1VorrechnungEvent());
    }
    this.txtBetragZahlung1Vorrechnung.addFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onBetragZahlung1VorrechnungEvent();
          }
        });
  }

  private void onBetragZahlung1VorrechnungEvent() {
    LOGGER.trace("SemesterrechnungController Event BetragZahlung1Vorrechnung");
    boolean equalFieldAndModelValue =
        equalsNullSafe(
            txtBetragZahlung1Vorrechnung.getText(),
            semesterrechnungModel.getBetragZahlung1Vorrechnung());
    try {
      setModelBetragZahlung1Vorrechnung();
    } catch (SvmValidationException e) {
      return;
    }
    if (equalFieldAndModelValue && isModelValidationMode()) {
      // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb
      // muss hier die Validierung angestossen werden.
      LOGGER.trace(VALIDIERUNG_WEGEN_EQUAL_FIELD_AND_MODEL_VALUE);
      validate();
    }
  }

  private void setModelBetragZahlung1Vorrechnung() throws SvmValidationException {
    makeErrorLabelInvisible(Field.BETRAG_ZAHLUNG_1_VORRECHNUNG);
    try {
      semesterrechnungModel.setBetragZahlung1Vorrechnung(txtBetragZahlung1Vorrechnung.getText());
    } catch (SvmValidationException e) {
      LOGGER.trace(
          "SemesterrechnungController setModelBetragZahlung1Vorrechnung Exception={}",
          e.getMessage());
      showErrMsg(e);
      throw e;
    }
  }

  public void setTxtDatumZahlung2Vorrechnung(JTextField txtDatumZahlung2Vorrechnung) {
    this.txtDatumZahlung2Vorrechnung = txtDatumZahlung2Vorrechnung;
    if (!defaultButtonEnabled) {
      this.txtDatumZahlung2Vorrechnung.addActionListener(e -> onDatumZahlung2VorrechnungEvent());
    }
    this.txtDatumZahlung2Vorrechnung.addFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onDatumZahlung2VorrechnungEvent();
          }
        });
  }

  private void onDatumZahlung2VorrechnungEvent() {
    LOGGER.trace("SemesterrechnungController Event DatumZahlung2Vorrechnung");
    boolean equalFieldAndModelValue =
        equalsNullSafe(
            txtDatumZahlung2Vorrechnung.getText(),
            semesterrechnungModel.getDatumZahlung2Vorrechnung());
    try {
      setModelDatumZahlung2Vorrechnung();
    } catch (SvmValidationException e) {
      return;
    }
    if (equalFieldAndModelValue && isModelValidationMode()) {
      // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb
      // muss hier die Validierung angestossen werden.
      LOGGER.trace(VALIDIERUNG_WEGEN_EQUAL_FIELD_AND_MODEL_VALUE);
      validate();
    }
  }

  private void setModelDatumZahlung2Vorrechnung() throws SvmValidationException {
    makeErrorLabelInvisible(Field.DATUM_ZAHLUNG_2_VORRECHNUNG);
    try {
      semesterrechnungModel.setDatumZahlung2Vorrechnung(txtDatumZahlung2Vorrechnung.getText());
    } catch (SvmValidationException e) {
      LOGGER.trace(
          "SemesterrechnungController setModelDatumZahlung2Vorrechnung Exception={}",
          e.getMessage());
      showErrMsg(e);
      throw e;
    }
  }

  public void setTxtBetragZahlung2Vorrechnung(JTextField txtBetragZahlung2Vorrechnung) {
    this.txtBetragZahlung2Vorrechnung = txtBetragZahlung2Vorrechnung;
    if (!defaultButtonEnabled) {
      this.txtBetragZahlung2Vorrechnung.addActionListener(e -> onBetragZahlung2VorrechnungEvent());
    }
    this.txtBetragZahlung2Vorrechnung.addFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onBetragZahlung2VorrechnungEvent();
          }
        });
  }

  private void onBetragZahlung2VorrechnungEvent() {
    LOGGER.trace("SemesterrechnungController Event BetragZahlung2Vorrechnung");
    boolean equalFieldAndModelValue =
        equalsNullSafe(
            txtBetragZahlung2Vorrechnung.getText(),
            semesterrechnungModel.getBetragZahlung2Vorrechnung());
    try {
      setModelBetragZahlung2Vorrechnung();
    } catch (SvmValidationException e) {
      return;
    }
    if (equalFieldAndModelValue && isModelValidationMode()) {
      // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb
      // muss hier die Validierung angestossen werden.
      LOGGER.trace(VALIDIERUNG_WEGEN_EQUAL_FIELD_AND_MODEL_VALUE);
      validate();
    }
  }

  private void setModelBetragZahlung2Vorrechnung() throws SvmValidationException {
    makeErrorLabelInvisible(Field.BETRAG_ZAHLUNG_2_VORRECHNUNG);
    try {
      semesterrechnungModel.setBetragZahlung2Vorrechnung(txtBetragZahlung2Vorrechnung.getText());
    } catch (SvmValidationException e) {
      LOGGER.trace(
          "SemesterrechnungController setModelBetragZahlung2Vorrechnung Exception={}",
          e.getMessage());
      showErrMsg(e);
      throw e;
    }
  }

  public void setTxtDatumZahlung3Vorrechnung(JTextField txtDatumZahlung3Vorrechnung) {
    this.txtDatumZahlung3Vorrechnung = txtDatumZahlung3Vorrechnung;
    if (!defaultButtonEnabled) {
      this.txtDatumZahlung3Vorrechnung.addActionListener(e -> onDatumZahlung3VorrechnungEvent());
    }
    this.txtDatumZahlung3Vorrechnung.addFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onDatumZahlung3VorrechnungEvent();
          }
        });
  }

  private void onDatumZahlung3VorrechnungEvent() {
    LOGGER.trace("SemesterrechnungController Event DatumZahlung3Vorrechnung");
    boolean equalFieldAndModelValue =
        equalsNullSafe(
            txtDatumZahlung3Vorrechnung.getText(),
            semesterrechnungModel.getDatumZahlung3Vorrechnung());
    try {
      setModelDatumZahlung3Vorrechnung();
    } catch (SvmValidationException e) {
      return;
    }
    if (equalFieldAndModelValue && isModelValidationMode()) {
      // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb
      // muss hier die Validierung angestossen werden.
      LOGGER.trace(VALIDIERUNG_WEGEN_EQUAL_FIELD_AND_MODEL_VALUE);
      validate();
    }
  }

  private void setModelDatumZahlung3Vorrechnung() throws SvmValidationException {
    makeErrorLabelInvisible(Field.DATUM_ZAHLUNG_3_VORRECHNUNG);
    try {
      semesterrechnungModel.setDatumZahlung3Vorrechnung(txtDatumZahlung3Vorrechnung.getText());
    } catch (SvmValidationException e) {
      LOGGER.trace(
          "SemesterrechnungController setModelDatumZahlung3Vorrechnung Exception={}",
          e.getMessage());
      showErrMsg(e);
      throw e;
    }
  }

  public void setTxtBetragZahlung3Vorrechnung(JTextField txtBetragZahlung3Vorrechnung) {
    this.txtBetragZahlung3Vorrechnung = txtBetragZahlung3Vorrechnung;
    if (!defaultButtonEnabled) {
      this.txtBetragZahlung3Vorrechnung.addActionListener(e -> onBetragZahlung3VorrechnungEvent());
    }
    this.txtBetragZahlung3Vorrechnung.addFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onBetragZahlung3VorrechnungEvent();
          }
        });
  }

  private void onBetragZahlung3VorrechnungEvent() {
    LOGGER.trace("SemesterrechnungController Event BetragZahlung3Vorrechnung");
    boolean equalFieldAndModelValue =
        equalsNullSafe(
            txtBetragZahlung3Vorrechnung.getText(),
            semesterrechnungModel.getBetragZahlung3Vorrechnung());
    try {
      setModelBetragZahlung3Vorrechnung();
    } catch (SvmValidationException e) {
      return;
    }
    if (equalFieldAndModelValue && isModelValidationMode()) {
      // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb
      // muss hier die Validierung angestossen werden.
      LOGGER.trace(VALIDIERUNG_WEGEN_EQUAL_FIELD_AND_MODEL_VALUE);
      validate();
    }
  }

  private void setModelBetragZahlung3Vorrechnung() throws SvmValidationException {
    makeErrorLabelInvisible(Field.BETRAG_ZAHLUNG_3_VORRECHNUNG);
    try {
      semesterrechnungModel.setBetragZahlung3Vorrechnung(txtBetragZahlung3Vorrechnung.getText());
    } catch (SvmValidationException e) {
      LOGGER.trace(
          "SemesterrechnungController setModelBetragZahlung3Vorrechnung Exception={}",
          e.getMessage());
      showErrMsg(e);
      throw e;
    }
  }

  public void setTxtRechnungsdatumNachrechnung(JTextField txtRechnungsdatumNachrechnung) {
    this.txtRechnungsdatumNachrechnung = txtRechnungsdatumNachrechnung;
    if (!defaultButtonEnabled) {
      this.txtRechnungsdatumNachrechnung.addActionListener(
          e -> onRechnungsdatumNachrechnungEvent());
    }
    this.txtRechnungsdatumNachrechnung.addFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onRechnungsdatumNachrechnungEvent();
          }
        });
  }

  void onRechnungsdatumNachrechnungEvent() {
    LOGGER.trace("SemesterrechnungController Event RechnungsdatumNachrechnung");
    boolean equalFieldAndModelValue =
        equalsNullSafe(
            txtRechnungsdatumNachrechnung.getText(),
            semesterrechnungModel.getRechnungsdatumNachrechnung());
    try {
      setModelRechnungsdatumNachrechnung();
    } catch (SvmValidationException e) {
      return;
    }
    if (equalFieldAndModelValue && isModelValidationMode()) {
      // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb
      // muss hier die Validierung angestossen werden.
      LOGGER.trace(VALIDIERUNG_WEGEN_EQUAL_FIELD_AND_MODEL_VALUE);
      validate();
    }
  }

  void setModelRechnungsdatumNachrechnung() throws SvmValidationException {
    makeErrorLabelInvisible(Field.RECHNUNGSDATUM_NACHRECHNUNG);
    try {
      semesterrechnungModel.setRechnungsdatumNachrechnung(txtRechnungsdatumNachrechnung.getText());
    } catch (SvmValidationException e) {
      LOGGER.trace(
          "SemesterrechnungController setModelRechnungsdatumNachrechnung Exception={}",
          e.getMessage());
      showErrMsg(e);
      throw e;
    }
  }

  public void setTxtErmaessigungNachrechnung(JTextField txtErmaessigungNachrechnung) {
    this.txtErmaessigungNachrechnung = txtErmaessigungNachrechnung;
    if (!defaultButtonEnabled) {
      this.txtErmaessigungNachrechnung.addActionListener(e -> onErmaessigungNachrechnungEvent());
    }
    this.txtErmaessigungNachrechnung.addFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onErmaessigungNachrechnungEvent();
          }
        });
  }

  private void onErmaessigungNachrechnungEvent() {
    LOGGER.trace("SemesterrechnungController Event Ermaessigung");
    boolean equalFieldAndModelValue =
        equalsNullSafe(
            txtErmaessigungNachrechnung.getText(),
            semesterrechnungModel.getErmaessigungNachrechnung());
    try {
      setModelErmaessigungNachrechnung();
    } catch (SvmValidationException e) {
      return;
    }
    if (equalFieldAndModelValue && isModelValidationMode()) {
      // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb
      // muss hier die Validierung angestossen werden.
      LOGGER.trace(VALIDIERUNG_WEGEN_EQUAL_FIELD_AND_MODEL_VALUE);
      validate();
    }
  }

  private void setModelErmaessigungNachrechnung() throws SvmValidationException {
    makeErrorLabelInvisible(Field.ERMAESSIGUNG_NACHRECHNUNG);
    try {
      semesterrechnungModel.setErmaessigungNachrechnung(txtErmaessigungNachrechnung.getText());
    } catch (SvmValidationException e) {
      LOGGER.trace(
          "SemesterrechnungController setModelErmaessigungNachrechnung Exception={}",
          e.getMessage());
      showErrMsg(e);
      throw e;
    }
  }

  public void setTxtErmaessigungsgrundNachrechnung(JTextField txtErmaessigungsgrundNachrechnung) {
    this.txtErmaessigungsgrundNachrechnung = txtErmaessigungsgrundNachrechnung;
    if (!defaultButtonEnabled) {
      this.txtErmaessigungsgrundNachrechnung.addActionListener(
          e -> onErmaessigungsgrundNachrechnungEvent());
    }
    this.txtErmaessigungsgrundNachrechnung.addFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onErmaessigungsgrundNachrechnungEvent();
          }
        });
  }

  private void onErmaessigungsgrundNachrechnungEvent() {
    LOGGER.trace("SemesterrechnungController Event Ermaessigungsgrund");
    boolean equalFieldAndModelValue =
        equalsNullSafe(
            txtErmaessigungsgrundNachrechnung.getText(),
            semesterrechnungModel.getErmaessigungsgrundNachrechnung());
    try {
      setModelErmaessigungsgrundNachrechnung();
    } catch (SvmValidationException e) {
      return;
    }
    if (equalFieldAndModelValue && isModelValidationMode()) {
      // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb
      // muss hier die Validierung angestossen werden.
      LOGGER.trace(VALIDIERUNG_WEGEN_EQUAL_FIELD_AND_MODEL_VALUE);
      validate();
    }
  }

  private void setModelErmaessigungsgrundNachrechnung() throws SvmValidationException {
    makeErrorLabelInvisible(Field.ERMAESSIGUNGSGRUND_NACHRECHNUNG);
    try {
      semesterrechnungModel.setErmaessigungsgrundNachrechnung(
          txtErmaessigungsgrundNachrechnung.getText());
    } catch (SvmValidationException e) {
      LOGGER.trace(
          "SemesterrechnungController setModelErmaessigungsgrundNachrechnung Exception={}",
          e.getMessage());
      showErrMsg(e);
      throw e;
    }
  }

  public void setTxtZuschlagNachrechnung(JTextField txtZuschlagNachrechnung) {
    this.txtZuschlagNachrechnung = txtZuschlagNachrechnung;
    if (!defaultButtonEnabled) {
      this.txtZuschlagNachrechnung.addActionListener(e -> onZuschlagNachrechnungEvent());
    }
    this.txtZuschlagNachrechnung.addFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onZuschlagNachrechnungEvent();
          }
        });
  }

  private void onZuschlagNachrechnungEvent() {
    LOGGER.trace("SemesterrechnungController Event Zuschlag");
    boolean equalFieldAndModelValue =
        equalsNullSafe(
            txtZuschlagNachrechnung.getText(), semesterrechnungModel.getZuschlagNachrechnung());
    try {
      setModelZuschlagNachrechnung();
    } catch (SvmValidationException e) {
      return;
    }
    if (equalFieldAndModelValue && isModelValidationMode()) {
      // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb
      // muss hier die Validierung angestossen werden.
      LOGGER.trace(VALIDIERUNG_WEGEN_EQUAL_FIELD_AND_MODEL_VALUE);
      validate();
    }
  }

  private void setModelZuschlagNachrechnung() throws SvmValidationException {
    makeErrorLabelInvisible(Field.ZUSCHLAG_NACHRECHNUNG);
    try {
      semesterrechnungModel.setZuschlagNachrechnung(txtZuschlagNachrechnung.getText());
    } catch (SvmValidationException e) {
      LOGGER.trace(
          "SemesterrechnungController setModelZuschlagNachrechnung Exception={}", e.getMessage());
      showErrMsg(e);
      throw e;
    }
  }

  public void setTxtZuschlagsgrundNachrechnung(JTextField txtZuschlagsgrundNachrechnung) {
    this.txtZuschlagsgrundNachrechnung = txtZuschlagsgrundNachrechnung;
    if (!defaultButtonEnabled) {
      this.txtZuschlagsgrundNachrechnung.addActionListener(
          e -> onZuschlagsgrundNachrechnungEvent());
    }
    this.txtZuschlagsgrundNachrechnung.addFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onZuschlagsgrundNachrechnungEvent();
          }
        });
  }

  private void onZuschlagsgrundNachrechnungEvent() {
    LOGGER.trace("SemesterrechnungController Event Zuschlagsgrund");
    boolean equalFieldAndModelValue =
        equalsNullSafe(
            txtZuschlagsgrundNachrechnung.getText(),
            semesterrechnungModel.getZuschlagsgrundNachrechnung());
    try {
      setModelZuschlagsgrundNachrechnung();
    } catch (SvmValidationException e) {
      return;
    }
    if (equalFieldAndModelValue && isModelValidationMode()) {
      // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb
      // muss hier die Validierung angestossen werden.
      LOGGER.trace(VALIDIERUNG_WEGEN_EQUAL_FIELD_AND_MODEL_VALUE);
      validate();
    }
  }

  private void setModelZuschlagsgrundNachrechnung() throws SvmValidationException {
    makeErrorLabelInvisible(Field.ZUSCHLAGSGRUND_NACHRECHNUNG);
    try {
      semesterrechnungModel.setZuschlagsgrundNachrechnung(txtZuschlagsgrundNachrechnung.getText());
    } catch (SvmValidationException e) {
      LOGGER.trace(
          "SemesterrechnungController setModelZuschlagsgrundNachrechnung Exception={}",
          e.getMessage());
      showErrMsg(e);
      throw e;
    }
  }

  public void setTxtAnzahlWochenNachrechnung(JTextField txtAnzahlWochenNachrechnung) {
    this.txtAnzahlWochenNachrechnung = txtAnzahlWochenNachrechnung;
    if (!defaultButtonEnabled) {
      this.txtAnzahlWochenNachrechnung.addActionListener(e -> onAnzahlWochenNachrechnungEvent());
    }
    this.txtAnzahlWochenNachrechnung.addFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onAnzahlWochenNachrechnungEvent();
          }
        });
  }

  private void onAnzahlWochenNachrechnungEvent() {
    LOGGER.trace("SemesterrechnungController Event AnzahlWochen");
    boolean equalFieldAndModelValue =
        equalsNullSafe(
            txtAnzahlWochenNachrechnung.getText(),
            semesterrechnungModel.getAnzahlWochenNachrechnung());
    try {
      setModelAnzahlWochenNachrechnung();
    } catch (SvmValidationException e) {
      return;
    }
    if (equalFieldAndModelValue && isModelValidationMode()) {
      // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb
      // muss hier die Validierung angestossen werden.
      LOGGER.trace(VALIDIERUNG_WEGEN_EQUAL_FIELD_AND_MODEL_VALUE);
      validate();
    }
  }

  private void setModelAnzahlWochenNachrechnung() throws SvmValidationException {
    makeErrorLabelInvisible(Field.ANZAHL_WOCHEN_NACHRECHNUNG);
    try {
      semesterrechnungModel.setAnzahlWochenNachrechnung(txtAnzahlWochenNachrechnung.getText());
    } catch (SvmValidationException e) {
      LOGGER.trace(
          "SemesterrechnungController setModelAnzahlWochenNachrechnung Exception={}",
          e.getMessage());
      showErrMsg(e);
      throw e;
    }
  }

  public void setTxtWochenbetragNachrechnung(JTextField txtWochenbetragNachrechnung) {
    this.txtWochenbetragNachrechnung = txtWochenbetragNachrechnung;
    if (!defaultButtonEnabled) {
      this.txtWochenbetragNachrechnung.addActionListener(e -> onWochenbetragNachrechnungEvent());
    }
    this.txtWochenbetragNachrechnung.addFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onWochenbetragNachrechnungEvent();
          }
        });
  }

  private void onWochenbetragNachrechnungEvent() {
    LOGGER.trace("SemesterrechnungController Event WochenbetragNachrechnung");
    boolean equalFieldAndModelValue =
        equalsNullSafe(
            txtWochenbetragNachrechnung.getText(),
            semesterrechnungModel.getWochenbetragNachrechnung());
    try {
      setModelWochenbetragNachrechnung();
    } catch (SvmValidationException e) {
      return;
    }
    if (equalFieldAndModelValue && isModelValidationMode()) {
      // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb
      // muss hier die Validierung angestossen werden.
      LOGGER.trace(VALIDIERUNG_WEGEN_EQUAL_FIELD_AND_MODEL_VALUE);
      validate();
    }
  }

  private void setModelWochenbetragNachrechnung() throws SvmValidationException {
    makeErrorLabelInvisible(Field.WOCHENBETRAG_NACHRECHNUNG);
    try {
      semesterrechnungModel.setWochenbetragNachrechnung(txtWochenbetragNachrechnung.getText());
    } catch (SvmValidationException e) {
      LOGGER.trace(
          "SemesterrechnungController setModelWochenbetragNachrechnung Exception={}",
          e.getMessage());
      showErrMsg(e);
      throw e;
    }
  }

  public void setTxtDatumZahlung1Nachrechnung(JTextField txtDatumZahlung1Nachrechnung) {
    this.txtDatumZahlung1Nachrechnung = txtDatumZahlung1Nachrechnung;
    if (!defaultButtonEnabled) {
      this.txtDatumZahlung1Nachrechnung.addActionListener(e -> onDatumZahlung1NachrechnungEvent());
    }
    this.txtDatumZahlung1Nachrechnung.addFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onDatumZahlung1NachrechnungEvent();
          }
        });
  }

  private void onDatumZahlung1NachrechnungEvent() {
    LOGGER.trace("SemesterrechnungController Event DatumZahlung1Nachrechnung");
    boolean equalFieldAndModelValue =
        equalsNullSafe(
            txtDatumZahlung1Nachrechnung.getText(),
            semesterrechnungModel.getDatumZahlung1Nachrechnung());
    try {
      setModelDatumZahlung1Nachrechnung();
    } catch (SvmValidationException e) {
      return;
    }
    if (equalFieldAndModelValue && isModelValidationMode()) {
      // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb
      // muss hier die Validierung angestossen werden.
      LOGGER.trace(VALIDIERUNG_WEGEN_EQUAL_FIELD_AND_MODEL_VALUE);
      validate();
    }
  }

  private void setModelDatumZahlung1Nachrechnung() throws SvmValidationException {
    makeErrorLabelInvisible(Field.DATUM_ZAHLUNG_1_NACHRECHNUNG);
    try {
      semesterrechnungModel.setDatumZahlung1Nachrechnung(txtDatumZahlung1Nachrechnung.getText());
    } catch (SvmValidationException e) {
      LOGGER.trace(
          "SemesterrechnungController setModelDatumZahlung1Nachrechnung Exception={}",
          e.getMessage());
      showErrMsg(e);
      throw e;
    }
  }

  public void setTxtBetragZahlung1Nachrechnung(JTextField txtBetragZahlung1Nachrechnung) {
    this.txtBetragZahlung1Nachrechnung = txtBetragZahlung1Nachrechnung;
    if (!defaultButtonEnabled) {
      this.txtBetragZahlung1Nachrechnung.addActionListener(
          e -> onBetragZahlung1NachrechnungEvent());
    }
    this.txtBetragZahlung1Nachrechnung.addFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onBetragZahlung1NachrechnungEvent();
          }
        });
  }

  private void onBetragZahlung1NachrechnungEvent() {
    LOGGER.trace("SemesterrechnungController Event BetragZahlung1Nachrechnung");
    boolean equalFieldAndModelValue =
        equalsNullSafe(
            txtBetragZahlung1Nachrechnung.getText(),
            semesterrechnungModel.getBetragZahlung1Nachrechnung());
    try {
      setModelBetragZahlung1Nachrechnung();
    } catch (SvmValidationException e) {
      return;
    }
    if (equalFieldAndModelValue && isModelValidationMode()) {
      // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb
      // muss hier die Validierung angestossen werden.
      LOGGER.trace(VALIDIERUNG_WEGEN_EQUAL_FIELD_AND_MODEL_VALUE);
      validate();
    }
  }

  private void setModelBetragZahlung1Nachrechnung() throws SvmValidationException {
    makeErrorLabelInvisible(Field.BETRAG_ZAHLUNG_1_NACHRECHNUNG);
    try {
      semesterrechnungModel.setBetragZahlung1Nachrechnung(txtBetragZahlung1Nachrechnung.getText());
    } catch (SvmValidationException e) {
      LOGGER.trace(
          "SemesterrechnungController setModelBetragZahlung1Nachrechnung Exception={}",
          e.getMessage());
      showErrMsg(e);
      throw e;
    }
  }

  public void setTxtDatumZahlung2Nachrechnung(JTextField txtDatumZahlung2Nachrechnung) {
    this.txtDatumZahlung2Nachrechnung = txtDatumZahlung2Nachrechnung;
    if (!defaultButtonEnabled) {
      this.txtDatumZahlung2Nachrechnung.addActionListener(e -> onDatumZahlung2NachrechnungEvent());
    }
    this.txtDatumZahlung2Nachrechnung.addFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onDatumZahlung2NachrechnungEvent();
          }
        });
  }

  private void onDatumZahlung2NachrechnungEvent() {
    LOGGER.trace("SemesterrechnungController Event DatumZahlung2Nachrechnung");
    boolean equalFieldAndModelValue =
        equalsNullSafe(
            txtDatumZahlung2Nachrechnung.getText(),
            semesterrechnungModel.getDatumZahlung2Nachrechnung());
    try {
      setModelDatumZahlung2Nachrechnung();
    } catch (SvmValidationException e) {
      return;
    }
    if (equalFieldAndModelValue && isModelValidationMode()) {
      // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb
      // muss hier die Validierung angestossen werden.
      LOGGER.trace(VALIDIERUNG_WEGEN_EQUAL_FIELD_AND_MODEL_VALUE);
      validate();
    }
  }

  private void setModelDatumZahlung2Nachrechnung() throws SvmValidationException {
    makeErrorLabelInvisible(Field.DATUM_ZAHLUNG_2_NACHRECHNUNG);
    try {
      semesterrechnungModel.setDatumZahlung2Nachrechnung(txtDatumZahlung2Nachrechnung.getText());
    } catch (SvmValidationException e) {
      LOGGER.trace(
          "SemesterrechnungController setModelDatumZahlung2Nachrechnung Exception={}",
          e.getMessage());
      showErrMsg(e);
      throw e;
    }
  }

  public void setTxtBetragZahlung2Nachrechnung(JTextField txtBetragZahlung2Nachrechnung) {
    this.txtBetragZahlung2Nachrechnung = txtBetragZahlung2Nachrechnung;
    if (!defaultButtonEnabled) {
      this.txtBetragZahlung2Nachrechnung.addActionListener(
          e -> onBetragZahlung2NachrechnungEvent());
    }
    this.txtBetragZahlung2Nachrechnung.addFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onBetragZahlung2NachrechnungEvent();
          }
        });
  }

  private void onBetragZahlung2NachrechnungEvent() {
    LOGGER.trace("SemesterrechnungController Event BetragZahlung2Nachrechnung");
    boolean equalFieldAndModelValue =
        equalsNullSafe(
            txtBetragZahlung2Nachrechnung.getText(),
            semesterrechnungModel.getBetragZahlung2Nachrechnung());
    try {
      setModelBetragZahlung2Nachrechnung();
    } catch (SvmValidationException e) {
      return;
    }
    if (equalFieldAndModelValue && isModelValidationMode()) {
      // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb
      // muss hier die Validierung angestossen werden.
      LOGGER.trace(VALIDIERUNG_WEGEN_EQUAL_FIELD_AND_MODEL_VALUE);
      validate();
    }
  }

  private void setModelBetragZahlung2Nachrechnung() throws SvmValidationException {
    makeErrorLabelInvisible(Field.BETRAG_ZAHLUNG_2_NACHRECHNUNG);
    try {
      semesterrechnungModel.setBetragZahlung2Nachrechnung(txtBetragZahlung2Nachrechnung.getText());
    } catch (SvmValidationException e) {
      LOGGER.trace(
          "SemesterrechnungController setModelBetragZahlung2Nachrechnung Exception={}",
          e.getMessage());
      showErrMsg(e);
      throw e;
    }
  }

  public void setTxtDatumZahlung3Nachrechnung(JTextField txtDatumZahlung3Nachrechnung) {
    this.txtDatumZahlung3Nachrechnung = txtDatumZahlung3Nachrechnung;
    if (!defaultButtonEnabled) {
      this.txtDatumZahlung3Nachrechnung.addActionListener(e -> onDatumZahlung3NachrechnungEvent());
    }
    this.txtDatumZahlung3Nachrechnung.addFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onDatumZahlung3NachrechnungEvent();
          }
        });
  }

  private void onDatumZahlung3NachrechnungEvent() {
    LOGGER.trace("SemesterrechnungController Event DatumZahlung3Nachrechnung");
    boolean equalFieldAndModelValue =
        equalsNullSafe(
            txtDatumZahlung3Nachrechnung.getText(),
            semesterrechnungModel.getDatumZahlung3Nachrechnung());
    try {
      setModelDatumZahlung3Nachrechnung();
    } catch (SvmValidationException e) {
      return;
    }
    if (equalFieldAndModelValue && isModelValidationMode()) {
      // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb
      // muss hier die Validierung angestossen werden.
      LOGGER.trace(VALIDIERUNG_WEGEN_EQUAL_FIELD_AND_MODEL_VALUE);
      validate();
    }
  }

  private void setModelDatumZahlung3Nachrechnung() throws SvmValidationException {
    makeErrorLabelInvisible(Field.DATUM_ZAHLUNG_3_NACHRECHNUNG);
    try {
      semesterrechnungModel.setDatumZahlung3Nachrechnung(txtDatumZahlung3Nachrechnung.getText());
    } catch (SvmValidationException e) {
      LOGGER.trace(
          "SemesterrechnungController setModelDatumZahlung3Nachrechnung Exception={}",
          e.getMessage());
      showErrMsg(e);
      throw e;
    }
  }

  public void setTxtBetragZahlung3Nachrechnung(JTextField txtBetragZahlung3Nachrechnung) {
    this.txtBetragZahlung3Nachrechnung = txtBetragZahlung3Nachrechnung;
    if (!defaultButtonEnabled) {
      this.txtBetragZahlung3Nachrechnung.addActionListener(
          e -> onBetragZahlung3NachrechnungEvent());
    }
    this.txtBetragZahlung3Nachrechnung.addFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onBetragZahlung3NachrechnungEvent();
          }
        });
  }

  private void onBetragZahlung3NachrechnungEvent() {
    LOGGER.trace("SemesterrechnungController Event BetragZahlung3Nachrechnung");
    boolean equalFieldAndModelValue =
        equalsNullSafe(
            txtBetragZahlung3Nachrechnung.getText(),
            semesterrechnungModel.getBetragZahlung3Nachrechnung());
    try {
      setModelBetragZahlung3Nachrechnung();
    } catch (SvmValidationException e) {
      return;
    }
    if (equalFieldAndModelValue && isModelValidationMode()) {
      // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb
      // muss hier die Validierung angestossen werden.
      LOGGER.trace(VALIDIERUNG_WEGEN_EQUAL_FIELD_AND_MODEL_VALUE);
      validate();
    }
  }

  private void setModelBetragZahlung3Nachrechnung() throws SvmValidationException {
    makeErrorLabelInvisible(Field.BETRAG_ZAHLUNG_3_NACHRECHNUNG);
    try {
      semesterrechnungModel.setBetragZahlung3Nachrechnung(txtBetragZahlung3Nachrechnung.getText());
    } catch (SvmValidationException e) {
      LOGGER.trace(
          "SemesterrechnungController setModelBetragZahlung3Nachrechnung Exception={}",
          e.getMessage());
      showErrMsg(e);
      throw e;
    }
  }

  public void setTextAreaBemerkungen(JTextArea textAreaBemerkungen) {
    this.textAreaBemerkungen = textAreaBemerkungen;
    textAreaBemerkungen.addFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onBemerkungenEvent();
          }
        });
    textAreaBemerkungen.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, null);
    textAreaBemerkungen.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, null);
  }

  private void onBemerkungenEvent() {
    LOGGER.trace("SemesterrechnungController Event Bemerkungen");
    boolean equalFieldAndModelValue =
        equalsNullSafe(textAreaBemerkungen.getText(), semesterrechnungModel.getBemerkungen());
    try {
      setModelBemerkungen();
    } catch (SvmValidationException e) {
      return;
    }
    if (equalFieldAndModelValue && isModelValidationMode()) {
      // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb
      // muss hier die Validierung angestossen werden.
      LOGGER.trace(VALIDIERUNG_WEGEN_EQUAL_FIELD_AND_MODEL_VALUE);
      validate();
    }
  }

  private void setModelBemerkungen() throws SvmValidationException {
    makeErrorLabelInvisible(Field.BEMERKUNGEN);
    try {
      semesterrechnungModel.setBemerkungen(textAreaBemerkungen.getText());
    } catch (SvmValidationException e) {
      LOGGER.trace("SemesterrechnungController setModelBemerkungen Exception={}", e.getMessage());
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

  public void setErrLblDatumZahlung1Vorrechnung(JLabel errLblDatumZahlung1Vorrechnung) {
    this.errLblDatumZahlung1Vorrechnung = errLblDatumZahlung1Vorrechnung;
  }

  public void setErrLblBetragZahlung1Vorrechnung(JLabel errLblBetragZahlung1Vorrechnung) {
    this.errLblBetragZahlung1Vorrechnung = errLblBetragZahlung1Vorrechnung;
  }

  public void setErrLblDatumZahlung2Vorrechnung(JLabel errLblDatumZahlung2Vorrechnung) {
    this.errLblDatumZahlung2Vorrechnung = errLblDatumZahlung2Vorrechnung;
  }

  public void setErrLblBetragZahlung2Vorrechnung(JLabel errLblBetragZahlung2Vorrechnung) {
    this.errLblBetragZahlung2Vorrechnung = errLblBetragZahlung2Vorrechnung;
  }

  public void setErrLblDatumZahlung3Vorrechnung(JLabel errLblDatumZahlung3Vorrechnung) {
    this.errLblDatumZahlung3Vorrechnung = errLblDatumZahlung3Vorrechnung;
  }

  public void setErrLblBetragZahlung3Vorrechung(JLabel errLblBetragZahlung3Vorrechnung) {
    this.errLblBetragZahlung3Vorrechnung = errLblBetragZahlung3Vorrechnung;
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

  public void setErrLblDatumZahlung1Nachrechnung(JLabel errLblDatumZahlung1Nachrechnung) {
    this.errLblDatumZahlung1Nachrechnung = errLblDatumZahlung1Nachrechnung;
  }

  public void setErrLblBetragZahlung1Nachrechnung(JLabel errLblBetragZahlung1Nachrechnung) {
    this.errLblBetragZahlung1Nachrechnung = errLblBetragZahlung1Nachrechnung;
  }

  public void setErrLblDatumZahlung2Nachrechnung(JLabel errLblDatumZahlung2Nachrechnung) {
    this.errLblDatumZahlung2Nachrechnung = errLblDatumZahlung2Nachrechnung;
  }

  public void setErrLblBetragZahlung2Nachrechnung(JLabel errLblBetragZahlung2Nachrechnung) {
    this.errLblBetragZahlung2Nachrechnung = errLblBetragZahlung2Nachrechnung;
  }

  public void setErrLblDatumZahlung3Nachrechnung(JLabel errLblDatumZahlung3Nachrechnung) {
    this.errLblDatumZahlung3Nachrechnung = errLblDatumZahlung3Nachrechnung;
  }

  public void setErrLblBetragZahlung3Nachrechung(JLabel errLblBetragZahlung3Nachrechnung) {
    this.errLblBetragZahlung3Nachrechnung = errLblBetragZahlung3Nachrechnung;
  }

  public void setErrLblStipendium(JLabel errLblStipendium) {
    this.errLblStipendium = errLblStipendium;
  }

  public void setErrLblBemerkungen(JLabel errLblBemerkungen) {
    this.errLblBemerkungen = errLblBemerkungen;
  }

  @SuppressWarnings("java:S3776")
  @Override
  void doPropertyChange(PropertyChangeEvent evt) {
    super.doPropertyChange(evt);
    if (checkIsFieldChange(Field.SEMESTERRECHNUNG_CODE, evt)) {
      comboBoxSemesterrechnungCode.setSelectedItem(semesterrechnungModel.getSemesterrechnungCode());
    } else if (checkIsFieldChange(Field.STIPENDIUM, evt)) {
      comboBoxStipendium.setSelectedItem(semesterrechnungModel.getStipendium());
    } else if (checkIsFieldChange(Field.GRATISKINDER, evt)) {
      checkBoxGratiskinder.setSelected(semesterrechnungModel.isGratiskinder());
    } else if (checkIsFieldChange(Field.RECHNUNGSDATUM_VORRECHNUNG, evt)) {
      txtRechnungsdatumVorrechnung.setText(
          asString(semesterrechnungModel.getRechnungsdatumVorrechnung()));
    } else if (checkIsFieldChange(Field.ERMAESSIGUNG_VORRECHNUNG, evt)) {
      txtErmaessigungVorrechnung.setText(
          semesterrechnungModel.getErmaessigungVorrechnung() == null
              ? null
              : semesterrechnungModel.getErmaessigungVorrechnung().toString());
    } else if (checkIsFieldChange(Field.ERMAESSIGUNGSGRUND_VORRECHNUNG, evt)) {
      txtErmaessigungsgrundVorrechnung.setText(
          semesterrechnungModel.getErmaessigungsgrundVorrechnung());
    } else if (checkIsFieldChange(Field.ZUSCHLAG_VORRECHNUNG, evt)) {
      txtZuschlagVorrechnung.setText(
          semesterrechnungModel.getZuschlagVorrechnung() == null
              ? null
              : semesterrechnungModel.getZuschlagVorrechnung().toString());
    } else if (checkIsFieldChange(Field.ZUSCHLAGSGRUND_VORRECHNUNG, evt)) {
      txtZuschlagsgrundVorrechnung.setText(semesterrechnungModel.getZuschlagsgrundVorrechnung());
    } else if (checkIsFieldChange(Field.ANZAHL_WOCHEN_VORRECHNUNG, evt)) {
      txtAnzahlWochenVorrechnung.setText(
          semesterrechnungModel.getAnzahlWochenVorrechnung() == null
              ? null
              : Integer.toString(semesterrechnungModel.getAnzahlWochenVorrechnung()));
    } else if (checkIsFieldChange(Field.WOCHENBETRAG_VORRECHNUNG, evt)) {
      txtWochenbetragVorrechnung.setText(
          semesterrechnungModel.getWochenbetragVorrechnung() == null
              ? null
              : semesterrechnungModel.getWochenbetragVorrechnung().toString());
    } else if (checkIsFieldChange(Field.DATUM_ZAHLUNG_1_VORRECHNUNG, evt)) {
      txtDatumZahlung1Vorrechnung.setText(
          asString(semesterrechnungModel.getDatumZahlung1Vorrechnung()));
    } else if (checkIsFieldChange(Field.BETRAG_ZAHLUNG_1_VORRECHNUNG, evt)) {
      txtBetragZahlung1Vorrechnung.setText(
          semesterrechnungModel.getBetragZahlung1Vorrechnung() == null
              ? null
              : semesterrechnungModel.getBetragZahlung1Vorrechnung().toString());
    } else if (checkIsFieldChange(Field.DATUM_ZAHLUNG_2_VORRECHNUNG, evt)) {
      txtDatumZahlung2Vorrechnung.setText(
          asString(semesterrechnungModel.getDatumZahlung2Vorrechnung()));
    } else if (checkIsFieldChange(Field.BETRAG_ZAHLUNG_2_VORRECHNUNG, evt)) {
      txtBetragZahlung2Vorrechnung.setText(
          semesterrechnungModel.getBetragZahlung2Vorrechnung() == null
              ? null
              : semesterrechnungModel.getBetragZahlung2Vorrechnung().toString());
    } else if (checkIsFieldChange(Field.DATUM_ZAHLUNG_3_VORRECHNUNG, evt)) {
      txtDatumZahlung3Vorrechnung.setText(
          asString(semesterrechnungModel.getDatumZahlung3Vorrechnung()));
    } else if (checkIsFieldChange(Field.BETRAG_ZAHLUNG_3_VORRECHNUNG, evt)) {
      txtBetragZahlung3Vorrechnung.setText(
          semesterrechnungModel.getBetragZahlung3Vorrechnung() == null
              ? null
              : semesterrechnungModel.getBetragZahlung3Vorrechnung().toString());
    } else if (checkIsFieldChange(Field.RECHNUNGSDATUM_NACHRECHNUNG, evt)) {
      txtRechnungsdatumNachrechnung.setText(
          asString(semesterrechnungModel.getRechnungsdatumNachrechnung()));
    } else if (checkIsFieldChange(Field.ERMAESSIGUNG_NACHRECHNUNG, evt)) {
      txtErmaessigungNachrechnung.setText(
          semesterrechnungModel.getErmaessigungNachrechnung() == null
              ? null
              : semesterrechnungModel.getErmaessigungNachrechnung().toString());
    } else if (checkIsFieldChange(Field.ERMAESSIGUNGSGRUND_NACHRECHNUNG, evt)) {
      txtErmaessigungsgrundNachrechnung.setText(
          semesterrechnungModel.getErmaessigungsgrundNachrechnung());
    } else if (checkIsFieldChange(Field.ZUSCHLAG_NACHRECHNUNG, evt)) {
      txtZuschlagNachrechnung.setText(
          semesterrechnungModel.getZuschlagNachrechnung() == null
              ? null
              : semesterrechnungModel.getZuschlagNachrechnung().toString());
    } else if (checkIsFieldChange(Field.ZUSCHLAGSGRUND_NACHRECHNUNG, evt)) {
      txtZuschlagsgrundNachrechnung.setText(semesterrechnungModel.getZuschlagsgrundNachrechnung());
    } else if (checkIsFieldChange(Field.ANZAHL_WOCHEN_NACHRECHNUNG, evt)) {
      txtAnzahlWochenNachrechnung.setText(
          semesterrechnungModel.getAnzahlWochenNachrechnung() == null
              ? null
              : Integer.toString(semesterrechnungModel.getAnzahlWochenNachrechnung()));
    } else if (checkIsFieldChange(Field.WOCHENBETRAG_NACHRECHNUNG, evt)) {
      txtWochenbetragNachrechnung.setText(
          semesterrechnungModel.getWochenbetragNachrechnung() == null
              ? null
              : semesterrechnungModel.getWochenbetragNachrechnung().toString());
    } else if (checkIsFieldChange(Field.DATUM_ZAHLUNG_1_NACHRECHNUNG, evt)) {
      txtDatumZahlung1Nachrechnung.setText(
          asString(semesterrechnungModel.getDatumZahlung1Nachrechnung()));
    } else if (checkIsFieldChange(Field.BETRAG_ZAHLUNG_1_NACHRECHNUNG, evt)) {
      txtBetragZahlung1Nachrechnung.setText(
          semesterrechnungModel.getBetragZahlung1Nachrechnung() == null
              ? null
              : semesterrechnungModel.getBetragZahlung1Nachrechnung().toString());
    } else if (checkIsFieldChange(Field.DATUM_ZAHLUNG_2_NACHRECHNUNG, evt)) {
      txtDatumZahlung2Nachrechnung.setText(
          asString(semesterrechnungModel.getDatumZahlung2Nachrechnung()));
    } else if (checkIsFieldChange(Field.BETRAG_ZAHLUNG_2_NACHRECHNUNG, evt)) {
      txtBetragZahlung2Nachrechnung.setText(
          semesterrechnungModel.getBetragZahlung2Nachrechnung() == null
              ? null
              : semesterrechnungModel.getBetragZahlung2Nachrechnung().toString());
    } else if (checkIsFieldChange(Field.DATUM_ZAHLUNG_3_NACHRECHNUNG, evt)) {
      txtDatumZahlung3Nachrechnung.setText(
          asString(semesterrechnungModel.getDatumZahlung3Nachrechnung()));
    } else if (checkIsFieldChange(Field.BETRAG_ZAHLUNG_3_NACHRECHNUNG, evt)) {
      txtBetragZahlung3Nachrechnung.setText(
          semesterrechnungModel.getBetragZahlung3Nachrechnung() == null
              ? null
              : semesterrechnungModel.getBetragZahlung3Nachrechnung().toString());
    } else if (checkIsFieldChange(Field.BEMERKUNGEN, evt)) {
      textAreaBemerkungen.setText(semesterrechnungModel.getBemerkungen());
    }
  }

  @SuppressWarnings("java:S3776")
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
    if (txtDatumZahlung1Vorrechnung != null && txtDatumZahlung1Vorrechnung.isEnabled()) {
      LOGGER.trace("Validate field DatumZahlung1Vorrechnung");
      setModelDatumZahlung1Vorrechnung();
    }
    if (txtBetragZahlung1Vorrechnung != null && txtBetragZahlung1Vorrechnung.isEnabled()) {
      LOGGER.trace("Validate field BetragZahlung1Vorrechnung");
      setModelBetragZahlung1Vorrechnung();
    }
    if (txtDatumZahlung2Vorrechnung != null && txtDatumZahlung2Vorrechnung.isEnabled()) {
      LOGGER.trace("Validate field DatumZahlung2Vorrechnung");
      setModelDatumZahlung2Vorrechnung();
    }
    if (txtBetragZahlung2Vorrechnung != null && txtBetragZahlung2Vorrechnung.isEnabled()) {
      LOGGER.trace("Validate field BetragZahlung2Vorrechnung");
      setModelBetragZahlung2Vorrechnung();
    }
    if (txtDatumZahlung3Vorrechnung != null && txtDatumZahlung3Vorrechnung.isEnabled()) {
      LOGGER.trace("Validate field DatumZahlung3Vorrechnung");
      setModelDatumZahlung3Vorrechnung();
    }
    if (txtBetragZahlung3Vorrechnung != null && txtBetragZahlung3Vorrechnung.isEnabled()) {
      LOGGER.trace("Validate field BetragZahlung3Vorrechnung");
      setModelBetragZahlung3Vorrechnung();
    }
    if (txtRechnungsdatumNachrechnung != null && txtRechnungsdatumNachrechnung.isEnabled()) {
      LOGGER.trace("Validate field RechnungsdatumNachrechnung");
      setModelRechnungsdatumNachrechnung();
    }
    if (txtErmaessigungNachrechnung != null && txtErmaessigungNachrechnung.isEnabled()) {
      LOGGER.trace("Validate field ErmaessigungNachrechnung");
      setModelErmaessigungNachrechnung();
    }
    if (txtErmaessigungsgrundNachrechnung != null
        && txtErmaessigungsgrundNachrechnung.isEnabled()) {
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
    if (txtDatumZahlung1Nachrechnung != null && txtDatumZahlung1Nachrechnung.isEnabled()) {
      LOGGER.trace("Validate field DatumZahlung1Nachrechnung");
      setModelDatumZahlung1Nachrechnung();
    }
    if (txtBetragZahlung1Nachrechnung != null && txtBetragZahlung1Nachrechnung.isEnabled()) {
      LOGGER.trace("Validate field BetragZahlung1Nachrechnung");
      setModelBetragZahlung1Nachrechnung();
    }
    if (txtDatumZahlung2Nachrechnung != null && txtDatumZahlung2Nachrechnung.isEnabled()) {
      LOGGER.trace("Validate field DatumZahlung2Nachrechnung");
      setModelDatumZahlung2Nachrechnung();
    }
    if (txtBetragZahlung2Nachrechnung != null && txtBetragZahlung2Nachrechnung.isEnabled()) {
      LOGGER.trace("Validate field BetragZahlung2Nachrechnung");
      setModelBetragZahlung2Nachrechnung();
    }
    if (txtDatumZahlung3Nachrechnung != null && txtDatumZahlung3Nachrechnung.isEnabled()) {
      LOGGER.trace("Validate field DatumZahlung3Nachrechnung");
      setModelDatumZahlung3Nachrechnung();
    }
    if (txtBetragZahlung3Nachrechnung != null && txtBetragZahlung3Nachrechnung.isEnabled()) {
      LOGGER.trace("Validate field BetragZahlung3Nachrechnung");
      setModelBetragZahlung3Nachrechnung();
    }
    if (textAreaBemerkungen != null && textAreaBemerkungen.isEnabled()) {
      LOGGER.trace("Validate field Bemerkungen");
      setModelBemerkungen();
    }
  }

  @SuppressWarnings("java:S3776")
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
    if (e.getAffectedFields().contains(Field.DATUM_ZAHLUNG_1_VORRECHNUNG)) {
      errLblDatumZahlung1Vorrechnung.setVisible(true);
      errLblDatumZahlung1Vorrechnung.setText(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.BETRAG_ZAHLUNG_1_VORRECHNUNG)) {
      errLblBetragZahlung1Vorrechnung.setVisible(true);
      errLblBetragZahlung1Vorrechnung.setText(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.DATUM_ZAHLUNG_2_VORRECHNUNG)) {
      errLblDatumZahlung2Vorrechnung.setVisible(true);
      errLblDatumZahlung2Vorrechnung.setText(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.BETRAG_ZAHLUNG_2_VORRECHNUNG)) {
      errLblBetragZahlung2Vorrechnung.setVisible(true);
      errLblBetragZahlung2Vorrechnung.setText(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.DATUM_ZAHLUNG_3_VORRECHNUNG)) {
      errLblDatumZahlung3Vorrechnung.setVisible(true);
      errLblDatumZahlung3Vorrechnung.setText(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.BETRAG_ZAHLUNG_3_VORRECHNUNG)) {
      errLblBetragZahlung3Vorrechnung.setVisible(true);
      errLblBetragZahlung3Vorrechnung.setText(e.getMessage());
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
    if (e.getAffectedFields().contains(Field.DATUM_ZAHLUNG_1_NACHRECHNUNG)) {
      errLblDatumZahlung1Nachrechnung.setVisible(true);
      errLblDatumZahlung1Nachrechnung.setText(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.BETRAG_ZAHLUNG_1_NACHRECHNUNG)) {
      errLblBetragZahlung1Nachrechnung.setVisible(true);
      errLblBetragZahlung1Nachrechnung.setText(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.DATUM_ZAHLUNG_2_NACHRECHNUNG)) {
      errLblDatumZahlung2Nachrechnung.setVisible(true);
      errLblDatumZahlung2Nachrechnung.setText(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.BETRAG_ZAHLUNG_2_NACHRECHNUNG)) {
      errLblBetragZahlung2Nachrechnung.setVisible(true);
      errLblBetragZahlung2Nachrechnung.setText(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.DATUM_ZAHLUNG_3_NACHRECHNUNG)) {
      errLblDatumZahlung3Nachrechnung.setVisible(true);
      errLblDatumZahlung3Nachrechnung.setText(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.BETRAG_ZAHLUNG_3_NACHRECHNUNG)) {
      errLblBetragZahlung3Nachrechnung.setVisible(true);
      errLblBetragZahlung3Nachrechnung.setText(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.STIPENDIUM)) {
      errLblStipendium.setVisible(true);
      errLblStipendium.setText(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.BEMERKUNGEN)) {
      errLblBemerkungen.setVisible(true);
      errLblBemerkungen.setText(e.getMessage());
    }
  }

  @SuppressWarnings("java:S3776")
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
    if (e.getAffectedFields().contains(Field.DATUM_ZAHLUNG_1_VORRECHNUNG)) {
      txtDatumZahlung1Vorrechnung.setToolTipText(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.BETRAG_ZAHLUNG_1_VORRECHNUNG)) {
      txtBetragZahlung1Vorrechnung.setToolTipText(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.DATUM_ZAHLUNG_2_VORRECHNUNG)) {
      txtDatumZahlung2Vorrechnung.setToolTipText(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.BETRAG_ZAHLUNG_2_VORRECHNUNG)) {
      txtBetragZahlung2Vorrechnung.setToolTipText(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.DATUM_ZAHLUNG_3_VORRECHNUNG)) {
      txtDatumZahlung3Vorrechnung.setToolTipText(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.BETRAG_ZAHLUNG_3_VORRECHNUNG)) {
      txtBetragZahlung3Vorrechnung.setToolTipText(e.getMessage());
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
    if (e.getAffectedFields().contains(Field.DATUM_ZAHLUNG_1_NACHRECHNUNG)) {
      txtDatumZahlung1Nachrechnung.setToolTipText(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.BETRAG_ZAHLUNG_1_NACHRECHNUNG)) {
      txtBetragZahlung1Nachrechnung.setToolTipText(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.DATUM_ZAHLUNG_2_NACHRECHNUNG)) {
      txtDatumZahlung2Nachrechnung.setToolTipText(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.BETRAG_ZAHLUNG_2_NACHRECHNUNG)) {
      txtBetragZahlung2Nachrechnung.setToolTipText(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.DATUM_ZAHLUNG_3_NACHRECHNUNG)) {
      txtDatumZahlung3Nachrechnung.setToolTipText(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.BETRAG_ZAHLUNG_3_NACHRECHNUNG)) {
      txtBetragZahlung3Nachrechnung.setToolTipText(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.BEMERKUNGEN)) {
      textAreaBemerkungen.setToolTipText(e.getMessage());
    }
  }

  @SuppressWarnings("java:S3776")
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
    if (fields.contains(Field.ALLE) || fields.contains(Field.DATUM_ZAHLUNG_1_VORRECHNUNG)) {
      if (errLblDatumZahlung1Vorrechnung != null) {
        errLblDatumZahlung1Vorrechnung.setVisible(false);
      }
      if (txtDatumZahlung1Vorrechnung != null) {
        txtDatumZahlung1Vorrechnung.setToolTipText(null);
      }
    }
    if (fields.contains(Field.ALLE) || fields.contains(Field.BETRAG_ZAHLUNG_1_VORRECHNUNG)) {
      if (errLblBetragZahlung1Vorrechnung != null) {
        errLblBetragZahlung1Vorrechnung.setVisible(false);
      }
      if (txtBetragZahlung1Vorrechnung != null) {
        txtBetragZahlung1Vorrechnung.setToolTipText(null);
      }
    }
    if (fields.contains(Field.ALLE) || fields.contains(Field.DATUM_ZAHLUNG_2_VORRECHNUNG)) {
      if (errLblDatumZahlung2Vorrechnung != null) {
        errLblDatumZahlung2Vorrechnung.setVisible(false);
      }
      if (txtDatumZahlung2Vorrechnung != null) {
        txtDatumZahlung2Vorrechnung.setToolTipText(null);
      }
    }
    if (fields.contains(Field.ALLE) || fields.contains(Field.BETRAG_ZAHLUNG_2_VORRECHNUNG)) {
      if (errLblBetragZahlung2Vorrechnung != null) {
        errLblBetragZahlung2Vorrechnung.setVisible(false);
      }
      if (txtBetragZahlung2Vorrechnung != null) {
        txtBetragZahlung2Vorrechnung.setToolTipText(null);
      }
    }
    if (fields.contains(Field.ALLE) || fields.contains(Field.DATUM_ZAHLUNG_3_VORRECHNUNG)) {
      if (errLblDatumZahlung3Vorrechnung != null) {
        errLblDatumZahlung3Vorrechnung.setVisible(false);
      }
      if (txtDatumZahlung3Vorrechnung != null) {
        txtDatumZahlung3Vorrechnung.setToolTipText(null);
      }
    }
    if (fields.contains(Field.ALLE) || fields.contains(Field.BETRAG_ZAHLUNG_3_VORRECHNUNG)) {
      if (errLblBetragZahlung3Vorrechnung != null) {
        errLblBetragZahlung3Vorrechnung.setVisible(false);
      }
      if (txtBetragZahlung3Vorrechnung != null) {
        txtBetragZahlung3Vorrechnung.setToolTipText(null);
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
    if (fields.contains(Field.ALLE) || fields.contains(Field.DATUM_ZAHLUNG_1_NACHRECHNUNG)) {
      if (errLblDatumZahlung1Nachrechnung != null) {
        errLblDatumZahlung1Nachrechnung.setVisible(false);
      }
      if (txtDatumZahlung1Nachrechnung != null) {
        txtDatumZahlung1Nachrechnung.setToolTipText(null);
      }
    }
    if (fields.contains(Field.ALLE) || fields.contains(Field.BETRAG_ZAHLUNG_1_NACHRECHNUNG)) {
      if (errLblBetragZahlung1Nachrechnung != null) {
        errLblBetragZahlung1Nachrechnung.setVisible(false);
      }
      if (txtBetragZahlung1Nachrechnung != null) {
        txtBetragZahlung1Nachrechnung.setToolTipText(null);
      }
    }
    if (fields.contains(Field.ALLE) || fields.contains(Field.DATUM_ZAHLUNG_2_NACHRECHNUNG)) {
      if (errLblDatumZahlung2Nachrechnung != null) {
        errLblDatumZahlung2Nachrechnung.setVisible(false);
      }
      if (txtDatumZahlung2Nachrechnung != null) {
        txtDatumZahlung2Nachrechnung.setToolTipText(null);
      }
    }
    if (fields.contains(Field.ALLE) || fields.contains(Field.BETRAG_ZAHLUNG_2_NACHRECHNUNG)) {
      if (errLblBetragZahlung2Nachrechnung != null) {
        errLblBetragZahlung2Nachrechnung.setVisible(false);
      }
      if (txtBetragZahlung2Nachrechnung != null) {
        txtBetragZahlung2Nachrechnung.setToolTipText(null);
      }
    }
    if (fields.contains(Field.ALLE) || fields.contains(Field.DATUM_ZAHLUNG_3_NACHRECHNUNG)) {
      if (errLblDatumZahlung3Nachrechnung != null) {
        errLblDatumZahlung3Nachrechnung.setVisible(false);
      }
      if (txtDatumZahlung3Nachrechnung != null) {
        txtDatumZahlung3Nachrechnung.setToolTipText(null);
      }
    }
    if (fields.contains(Field.ALLE) || fields.contains(Field.BETRAG_ZAHLUNG_3_NACHRECHNUNG)) {
      if (errLblBetragZahlung3Nachrechnung != null) {
        errLblBetragZahlung3Nachrechnung.setVisible(false);
      }
      if (txtBetragZahlung3Nachrechnung != null) {
        txtBetragZahlung3Nachrechnung.setToolTipText(null);
      }
    }
    if (fields.contains(Field.ALLE) || fields.contains(Field.STIPENDIUM)) {
      if (errLblStipendium != null) {
        errLblStipendium.setVisible(false);
      }
      if (comboBoxStipendium != null) {
        comboBoxStipendium.setToolTipText(null);
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

  @SuppressWarnings("java:S3776")
  @Override
  public void disableFields(boolean disable, Set<Field> fields) {
    if (comboBoxSemesterrechnungCode != null
        && (fields.contains(Field.ALLE) || fields.contains(Field.SEMESTERRECHNUNG_CODE))) {
      comboBoxSemesterrechnungCode.setEnabled(!disable);
    }
    if (comboBoxStipendium != null
        && (fields.contains(Field.ALLE) || fields.contains(Field.STIPENDIUM))) {
      comboBoxStipendium.setEnabled(!disable);
    }
    if (txtRechnungsdatumVorrechnung != null
        && (fields.contains(Field.ALLE) || fields.contains(Field.RECHNUNGSDATUM_VORRECHNUNG))) {
      txtRechnungsdatumVorrechnung.setEnabled(!disable);
    }
    if (txtBetragZahlung1Vorrechnung != null
        && (fields.contains(Field.ALLE) || fields.contains(Field.BETRAG_ZAHLUNG_1_VORRECHNUNG))) {
      txtBetragZahlung1Vorrechnung.setEnabled(!disable);
    }
    if (txtDatumZahlung1Vorrechnung != null
        && (fields.contains(Field.ALLE) || fields.contains(Field.DATUM_ZAHLUNG_1_VORRECHNUNG))) {
      txtDatumZahlung1Vorrechnung.setEnabled(!disable);
    }
    if (txtBetragZahlung2Vorrechnung != null
        && (fields.contains(Field.ALLE) || fields.contains(Field.BETRAG_ZAHLUNG_2_VORRECHNUNG))) {
      txtBetragZahlung2Vorrechnung.setEnabled(!disable);
    }
    if (txtDatumZahlung2Vorrechnung != null
        && (fields.contains(Field.ALLE) || fields.contains(Field.DATUM_ZAHLUNG_2_VORRECHNUNG))) {
      txtDatumZahlung2Vorrechnung.setEnabled(!disable);
    }
    if (txtBetragZahlung3Vorrechnung != null
        && (fields.contains(Field.ALLE) || fields.contains(Field.BETRAG_ZAHLUNG_3_VORRECHNUNG))) {
      txtBetragZahlung3Vorrechnung.setEnabled(!disable);
    }
    if (txtDatumZahlung3Vorrechnung != null
        && (fields.contains(Field.ALLE) || fields.contains(Field.DATUM_ZAHLUNG_3_VORRECHNUNG))) {
      txtDatumZahlung3Vorrechnung.setEnabled(!disable);
    }
    if (txtRechnungsdatumNachrechnung != null
        && (fields.contains(Field.ALLE) || fields.contains(Field.RECHNUNGSDATUM_NACHRECHNUNG))) {
      txtRechnungsdatumNachrechnung.setEnabled(!disable);
    }
    if (txtBetragZahlung1Nachrechnung != null
        && (fields.contains(Field.ALLE) || fields.contains(Field.BETRAG_ZAHLUNG_1_NACHRECHNUNG))) {
      txtBetragZahlung1Nachrechnung.setEnabled(!disable);
    }
    if (txtDatumZahlung1Nachrechnung != null
        && (fields.contains(Field.ALLE) || fields.contains(Field.DATUM_ZAHLUNG_1_NACHRECHNUNG))) {
      txtDatumZahlung1Nachrechnung.setEnabled(!disable);
    }
    if (txtBetragZahlung2Nachrechnung != null
        && (fields.contains(Field.ALLE) || fields.contains(Field.BETRAG_ZAHLUNG_2_NACHRECHNUNG))) {
      txtBetragZahlung2Nachrechnung.setEnabled(!disable);
    }
    if (txtDatumZahlung2Nachrechnung != null
        && (fields.contains(Field.ALLE) || fields.contains(Field.DATUM_ZAHLUNG_2_NACHRECHNUNG))) {
      txtDatumZahlung2Nachrechnung.setEnabled(!disable);
    }
    if (txtBetragZahlung3Nachrechnung != null
        && (fields.contains(Field.ALLE) || fields.contains(Field.BETRAG_ZAHLUNG_3_NACHRECHNUNG))) {
      txtBetragZahlung3Nachrechnung.setEnabled(!disable);
    }
    if (txtDatumZahlung3Nachrechnung != null
        && (fields.contains(Field.ALLE) || fields.contains(Field.DATUM_ZAHLUNG_3_NACHRECHNUNG))) {
      txtDatumZahlung3Nachrechnung.setEnabled(!disable);
    }
  }

  void setSemesterrechnungModel(SemesterrechnungModel semesterrechnungModel) {
    this.semesterrechnungModel.removePropertyChangeListener(this);
    this.semesterrechnungModel.removeDisableFieldsListener(this);
    this.semesterrechnungModel.removeMakeErrorLabelsInvisibleListener(this);
    this.semesterrechnungModel = semesterrechnungModel;
    super.setUntypedModel(semesterrechnungModel);
    this.semesterrechnungModel.addPropertyChangeListener(this);
    this.semesterrechnungModel.addDisableFieldsListener(this);
    this.semesterrechnungModel.addMakeErrorLabelsInvisibleListener(this);
  }
}
