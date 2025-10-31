package ch.metzenthin.svm.ui.control;

import static ch.metzenthin.svm.common.utils.Converter.asString;
import static ch.metzenthin.svm.common.utils.SimpleValidator.equalsNullSafe;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.common.datatypes.Geschlecht;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.SchuelerModel;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.util.Set;
import javax.swing.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Hans Stamm
 */
@SuppressWarnings("LoggingSimilarMessage")
public class SchuelerController extends PersonController {

  private static final Logger LOGGER = LogManager.getLogger(SchuelerController.class);
  private static final String VALIDIERUNG_WEGEN_EQUAL_FIELD_AND_MODEL_VALUE =
      "Validierung wegen equalFieldAndModelValue";

  private JTextField txtAnmeldedatum;
  private JTextField txtAbmeldedatum;
  private JTextArea textAreaBemerkungen;
  private JComboBox<Geschlecht> comboBoxGeschlecht;
  private JLabel errLblAnmeldedatum;
  private JLabel errLblAbmeldedatum;
  private JLabel errLblBemerkungen;
  private JLabel errLblGeschlecht;
  private final SchuelerModel schuelerModel;
  private final boolean defaultButtonEnabled;

  public SchuelerController(SchuelerModel schuelerModel, boolean defaultButtonEnabled) {
    super(schuelerModel, defaultButtonEnabled);
    this.schuelerModel = schuelerModel;
    this.defaultButtonEnabled = defaultButtonEnabled;
    this.schuelerModel.addPropertyChangeListener(this);
  }

  public void setComboBoxGeschlecht(JComboBox<Geschlecht> comboBoxGeschlecht) {
    this.comboBoxGeschlecht = comboBoxGeschlecht;
    comboBoxGeschlecht.setModel(new DefaultComboBoxModel<>(Geschlecht.values()));
    comboBoxGeschlecht.addActionListener(e -> onGeschlechtSelected());
    // Leeren ComboBox-Wert anzeigen
    comboBoxGeschlecht.setSelectedItem(null);
  }

  public void setTxtAnmeldedatum(JTextField txtAnmeldedatum) {
    this.txtAnmeldedatum = txtAnmeldedatum;
    if (!defaultButtonEnabled) {
      this.txtAnmeldedatum.addActionListener(e -> onAnmeldedatumEvent(true));
    }
    this.txtAnmeldedatum.addFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onAnmeldedatumEvent(false);
          }
        });
  }

  public void setTxtAbmeldedatum(JTextField txtAbmeldedatum) {
    this.txtAbmeldedatum = txtAbmeldedatum;
    if (!defaultButtonEnabled) {
      this.txtAbmeldedatum.addActionListener(e -> onAbmeldedatumEvent());
    }
    this.txtAbmeldedatum.addFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onAbmeldedatumEvent();
          }
        });
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

  private void onGeschlechtSelected() {
    LOGGER.trace(
        "SchuelerController Event Geschlecht selected={}", comboBoxGeschlecht.getSelectedItem());
    boolean equalFieldAndModelValue =
        equalsNullSafe(comboBoxGeschlecht.getSelectedItem(), schuelerModel.getGeschlecht());
    try {
      setModelGeschlecht();
    } catch (SvmRequiredException e) {
      return;
    }
    if (equalFieldAndModelValue && isModelValidationMode()) {
      // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb
      // muss hier die Validierung angestossen werden.
      LOGGER.trace(VALIDIERUNG_WEGEN_EQUAL_FIELD_AND_MODEL_VALUE);
      validate();
    }
  }

  private void setModelGeschlecht() throws SvmRequiredException {
    makeErrorLabelInvisible(Field.GESCHLECHT);
    try {
      schuelerModel.setGeschlecht((Geschlecht) comboBoxGeschlecht.getSelectedItem());
    } catch (SvmRequiredException e) {
      LOGGER.trace("SchuelerController setModelGeschlecht RequiredException={}", e.getMessage());
      if (isModelValidationMode()) {
        comboBoxGeschlecht.setToolTipText(e.getMessage());
        // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut, nachdem alle Field-Prüfungen
        // bestanden sind.
      } else {
        showErrMsg(e);
      }
      throw e;
    }
  }

  private void onAnmeldedatumEvent(boolean showRequiredErrMsg) {
    LOGGER.trace("SchuelerController Event Anmeldedatum");
    boolean equalFieldAndModelValue =
        equalsNullSafe(txtAnmeldedatum.getText(), schuelerModel.getAnmeldedatum());
    try {
      setModelAnmeldedatum(showRequiredErrMsg);
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

  private void setModelAnmeldedatum(boolean showRequiredErrMsg) throws SvmValidationException {
    makeErrorLabelInvisible(Field.ANMELDEDATUM);
    try {
      schuelerModel.setAnmeldedatum(txtAnmeldedatum.getText());
    } catch (SvmRequiredException e) {
      LOGGER.trace("SchuelerController setModelAnmeldedatum RequiredException={}", e.getMessage());
      if (isModelValidationMode() || !showRequiredErrMsg) {
        txtAnmeldedatum.setToolTipText(e.getMessage());
        // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut, nachdem alle Field-Prüfungen
        // bestanden sind.
      } else {
        showErrMsg(e);
      }
      throw e;
    } catch (SvmValidationException e) {
      LOGGER.trace("SchuelerController setModelAnmeldedatum Exception={}", e.getMessage());
      showErrMsg(e);
      throw e;
    }
  }

  private void onAbmeldedatumEvent() {
    LOGGER.trace("SchuelerController Event Abmeldedatum");
    boolean equalFieldAndModelValue =
        equalsNullSafe(txtAbmeldedatum.getText(), schuelerModel.getAbmeldedatum());
    try {
      setModelAbmeldedatum();
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

  private void setModelAbmeldedatum() throws SvmValidationException {
    makeErrorLabelInvisible(Field.ABMELDEDATUM);
    try {
      schuelerModel.setAbmeldedatum(txtAbmeldedatum.getText());
    } catch (SvmValidationException e) {
      LOGGER.trace("SchuelerController setModelAbmeldedatum Exception={}", e.getMessage());
      showErrMsg(e);
      throw e;
    }
  }

  private void onBemerkungenEvent() {
    LOGGER.trace("SchuelerController Event Bemerkungen");
    boolean equalFieldAndModelValue =
        equalsNullSafe(textAreaBemerkungen.getText(), schuelerModel.getBemerkungen());
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
      schuelerModel.setBemerkungen(textAreaBemerkungen.getText());
    } catch (SvmValidationException e) {
      LOGGER.trace("SchuelerController setModelBemerkungen Exception={}", e.getMessage());
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
    LOGGER.trace(
        "SchuelerController PropertyChangeEvent '{}', oldValue='{}', newValue='{}'",
        evt.getPropertyName(),
        evt.getOldValue(),
        evt.getNewValue());
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
    setModelAnmeldedatum(true);
    LOGGER.trace("Validate field Abmeldedatum");
    setModelAbmeldedatum();
    LOGGER.trace("Validate field Bemerkungen");
    setModelBemerkungen();
  }

  @SuppressWarnings("DuplicatedCode")
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

  @SuppressWarnings("DuplicatedCode")
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

  @SuppressWarnings("DuplicatedCode")
  @Override
  public void makeErrorLabelsInvisible(Set<Field> fields) {
    super.makeErrorLabelsInvisible(fields);
    if (fields.contains(Field.ALLE) || fields.contains(Field.GESCHLECHT)) {
      if (errLblGeschlecht != null) {
        errLblGeschlecht.setVisible(false);
      }
      comboBoxGeschlecht.setToolTipText(null);
    }
    if (fields.contains(Field.ALLE) || fields.contains(Field.ANMELDEDATUM)) {
      errLblAnmeldedatum.setVisible(false);
      txtAnmeldedatum.setToolTipText(null);
    }
    if (fields.contains(Field.ALLE) || fields.contains(Field.ABMELDEDATUM)) {
      errLblAbmeldedatum.setVisible(false);
      txtAbmeldedatum.setToolTipText(null);
    }
    if (fields.contains(Field.ALLE) || fields.contains(Field.BEMERKUNGEN)) {
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
