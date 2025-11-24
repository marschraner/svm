package ch.metzenthin.svm.ui.control;

import static ch.metzenthin.svm.common.utils.SimpleValidator.equalsNullSafe;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.CodeErfassenModel;
import ch.metzenthin.svm.domain.model.DialogClosedListener;
import ch.metzenthin.svm.service.result.SaveCodeResult;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.util.Set;
import javax.swing.*;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Martin Schraner
 */
@SuppressWarnings("LoggingSimilarMessage")
public class CodeErfassenController extends AbstractController {

  private static final Logger LOGGER = LogManager.getLogger(CodeErfassenController.class);

  // Möglichkeit zum Umschalten des validation modes (nicht dynamisch)
  private static final boolean MODEL_VALIDATION_MODE = false;

  private final CodeErfassenModel codeErfassenModel;
  private final boolean isBearbeiten;
  private final boolean defaultButtonEnabled;
  private final DialogClosedListener dialogClosedListener;
  private JDialog codeErfassenDialog;
  private JTextField txtKuerzel;
  private JTextField txtBeschreibung;
  private JCheckBox checkBoxSelektierbar;
  @Setter private JLabel errLblKuerzel;
  @Setter private JLabel errLblBeschreibung;
  private JButton btnSpeichern;

  public CodeErfassenController(
      CodeErfassenModel codeErfassenModel,
      boolean isBearbeiten,
      boolean defaultButtonEnabled,
      DialogClosedListener dialogClosedListener) {
    super(codeErfassenModel);
    this.codeErfassenModel = codeErfassenModel;
    this.isBearbeiten = isBearbeiten;
    this.defaultButtonEnabled = defaultButtonEnabled;
    this.dialogClosedListener = dialogClosedListener;
    this.codeErfassenModel.addPropertyChangeListener(this);
    this.codeErfassenModel.addDisableFieldsListener(this);
    this.codeErfassenModel.addMakeErrorLabelsInvisibleListener(this);
    this.codeErfassenModel.addCompletedListener(this::onCodeErfassenModelCompleted);
    this.setModelValidationMode(MODEL_VALIDATION_MODE);
  }

  public void constructionDone() {
    codeErfassenModel.initializeCompleted();
  }

  public void setCodeErfassenDialog(JDialog codeErfassenDialog) {
    // call onCancel() when cross is clicked
    this.codeErfassenDialog = codeErfassenDialog;
    codeErfassenDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    codeErfassenDialog.addWindowListener(
        new WindowAdapter() {
          @Override
          public void windowClosing(WindowEvent e) {
            onAbbrechen();
          }
        });
  }

  public void setContentPane(JPanel contentPane) {
    // call onCancel() on ESCAPE
    contentPane.registerKeyboardAction(
        e -> onAbbrechen(),
        KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
        JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
  }

  public void setTxtKuerzel(JTextField txtKuerzel) {
    this.txtKuerzel = txtKuerzel;
    if (!defaultButtonEnabled) {
      this.txtKuerzel.addActionListener(e -> onKuerzelEvent(true));
    }
    this.txtKuerzel.addFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onKuerzelEvent(false);
          }
        });
  }

  private void onKuerzelEvent(boolean showRequiredErrMsg) {
    LOGGER.trace("CodeErfassenController Event Kuerzel");
    boolean equalFieldAndModelValue =
        equalsNullSafe(txtKuerzel.getText(), codeErfassenModel.getKuerzel());
    try {
      setModelKuerzel(showRequiredErrMsg);
    } catch (SvmValidationException e) {
      return;
    }
    if (equalFieldAndModelValue && isModelValidationMode()) {
      // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb
      // muss hier die Validierung angestossen werden.
      LOGGER.trace("Validierung wegen equalFieldAndModelValue");
      validate();
    }
  }

  private void setModelKuerzel(boolean showRequiredErrMsg) throws SvmValidationException {
    makeErrorLabelInvisible(Field.KUERZEL);
    try {
      codeErfassenModel.setKuerzel(txtKuerzel.getText());
    } catch (SvmRequiredException e) {
      LOGGER.trace("CodeErfassenController setModelKuerzel RequiredException={}", e.getMessage());
      if (isModelValidationMode() || !showRequiredErrMsg) {
        txtKuerzel.setToolTipText(e.getMessage());
        // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut, nachdem alle Field-Prüfungen
        // bestanden sind.
      } else {
        showErrMsg(e);
      }
      throw e;
    } catch (SvmValidationException e) {
      LOGGER.trace("CodeErfassenController setModelKuerzel Exception={}", e.getMessage());
      showErrMsg(e);
      throw e;
    }
  }

  public void setTxtBeschreibung(JTextField txtBeschreibung) {
    this.txtBeschreibung = txtBeschreibung;
    if (!defaultButtonEnabled) {
      this.txtBeschreibung.addActionListener(e -> onBeschreibungEvent(true));
    }
    this.txtBeschreibung.addFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onBeschreibungEvent(false);
          }
        });
  }

  private void onBeschreibungEvent(boolean showRequiredErrMsg) {
    LOGGER.trace("CodeErfassenController Event Beschreibung");
    boolean equalFieldAndModelValue =
        equalsNullSafe(txtBeschreibung.getText(), codeErfassenModel.getBeschreibung());
    try {
      setModelBeschreibung(showRequiredErrMsg);
    } catch (SvmValidationException e) {
      return;
    }
    if (equalFieldAndModelValue && isModelValidationMode()) {
      // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb
      // muss hier die Validierung angestossen werden.
      LOGGER.trace("Validierung wegen equalFieldAndModelValue");
      validate();
    }
  }

  private void setModelBeschreibung(boolean showRequiredErrMsg) throws SvmValidationException {
    makeErrorLabelInvisible(Field.BESCHREIBUNG);
    try {
      codeErfassenModel.setBeschreibung(txtBeschreibung.getText());
    } catch (SvmRequiredException e) {
      LOGGER.trace(
          "CodeErfassenController setModelBeschreibung RequiredException={}", e.getMessage());
      if (isModelValidationMode() || !showRequiredErrMsg) {
        txtBeschreibung.setToolTipText(e.getMessage());
        // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut, nachdem alle Field-Prüfungen
        // bestanden sind.
      } else {
        showErrMsg(e);
      }
      throw e;
    } catch (SvmValidationException e) {
      LOGGER.trace("CodeErfassenController setModelBeschreibung Exception={}", e.getMessage());
      showErrMsg(e);
      throw e;
    }
  }

  public void setCheckBoxSelektierbar(JCheckBox checkBoxSelektierbar) {
    this.checkBoxSelektierbar = checkBoxSelektierbar;
    // Selektierbar als Default-Wert
    if (!isBearbeiten) {
      codeErfassenModel.setSelektierbar(true);
    }
    this.checkBoxSelektierbar.addItemListener(e -> onSelektierbarEvent());
  }

  private void setModelSelektierbar() {
    codeErfassenModel.setSelektierbar(checkBoxSelektierbar.isSelected());
  }

  private void onSelektierbarEvent() {
    LOGGER.trace(
        "AngehoerigerController Event Selektierbar. Selected={}",
        checkBoxSelektierbar.isSelected());
    setModelSelektierbar();
  }

  public void setBtnSpeichern(JButton btnSpeichern) {
    this.btnSpeichern = btnSpeichern;
    if (isModelValidationMode()) {
      btnSpeichern.setEnabled(false);
    }
    this.btnSpeichern.addActionListener(e -> onSpeichern());
  }

  private void onSpeichern() {
    if (!isModelValidationMode() && !validateOnSpeichern()) {
      btnSpeichern.setFocusPainted(false);
      return;
    }
    String messageDialogTitle = "Fehler";
    SaveCodeResult saveSchuelerCodeResult = codeErfassenModel.speichern();
    switch (saveSchuelerCodeResult) {
      case CODE_BEREITS_ERFASST -> {
        JOptionPane.showMessageDialog(
            codeErfassenDialog,
            "Kürzel bereits in Verwendung.",
            messageDialogTitle,
            JOptionPane.ERROR_MESSAGE);
        btnSpeichern.setFocusPainted(false);
      }
      case CODE_DURCH_ANDEREN_BENUTZER_VERAENDERT -> {
        closeDialog();
        JOptionPane.showMessageDialog(
            codeErfassenDialog,
            "Der Wert konnte nicht gespeichert werden, da der Eintrag unterdessen durch \n"
                + "einen anderen Benutzer verändert oder gelöscht wurde.",
            messageDialogTitle,
            JOptionPane.ERROR_MESSAGE);
      }
      case SPEICHERN_ERFOLGREICH -> closeDialog();
    }
  }

  public void setBtnAbbrechen(JButton btnAbbrechen) {
    btnAbbrechen.addActionListener(e -> onAbbrechen());
  }

  private void onAbbrechen() {
    closeDialog();
  }

  private void closeDialog() {
    codeErfassenDialog.dispose();
    dialogClosedListener.onDialogClosed();
  }

  private void onCodeErfassenModelCompleted(boolean completed) {
    LOGGER.trace("CodeErfassenModel completed={}", completed);
    if (completed) {
      btnSpeichern.setToolTipText(null);
      btnSpeichern.setEnabled(true);
    } else {
      btnSpeichern.setToolTipText("Bitte Eingabedaten vervollständigen");
      btnSpeichern.setEnabled(false);
    }
  }

  @Override
  void doPropertyChange(PropertyChangeEvent evt) {
    super.doPropertyChange(evt);
    if (checkIsFieldChange(Field.KUERZEL, evt)) {
      txtKuerzel.setText(codeErfassenModel.getKuerzel());
    } else if (checkIsFieldChange(Field.BESCHREIBUNG, evt)) {
      txtBeschreibung.setText(codeErfassenModel.getBeschreibung());
    } else if (checkIsFieldChange(Field.SELEKTIERBAR, evt)) {
      checkBoxSelektierbar.setSelected(codeErfassenModel.isSelektierbar());
    }
  }

  @Override
  void validateFields() throws SvmValidationException {
    if (txtKuerzel.isEnabled()) {
      LOGGER.trace("Validate field Kuerzel");
      setModelKuerzel(true);
    }
    if (txtBeschreibung.isEnabled()) {
      LOGGER.trace("Validate field Beschreibung");
      setModelBeschreibung(true);
    }
  }

  @Override
  void showErrMsg(SvmValidationException e) {
    if (e.getAffectedFields().contains(Field.KUERZEL)) {
      errLblKuerzel.setVisible(true);
      errLblKuerzel.setText(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.BESCHREIBUNG)) {
      errLblBeschreibung.setVisible(true);
      errLblBeschreibung.setText(e.getMessage());
    }
  }

  @Override
  void showErrMsgAsToolTip(SvmValidationException e) {
    if (e.getAffectedFields().contains(Field.KUERZEL)) {
      txtKuerzel.setToolTipText(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.BESCHREIBUNG)) {
      txtBeschreibung.setToolTipText(e.getMessage());
    }
  }

  @Override
  public void makeErrorLabelsInvisible(Set<Field> fields) {
    if (fields.contains(Field.ALLE) || fields.contains(Field.KUERZEL)) {
      errLblKuerzel.setVisible(false);
      txtKuerzel.setToolTipText(null);
    }
    if (fields.contains(Field.ALLE) || fields.contains(Field.BESCHREIBUNG)) {
      errLblBeschreibung.setVisible(false);
      txtBeschreibung.setToolTipText(null);
    }
  }

  @Override
  public void disableFields(boolean disable, Set<Field> fields) {
    // Keine zu deaktivierenden Felder
  }
}
