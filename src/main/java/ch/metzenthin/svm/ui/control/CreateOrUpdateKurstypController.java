package ch.metzenthin.svm.ui.control;

import static ch.metzenthin.svm.common.utils.SimpleValidator.equalsNullSafe;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.CreateOrUpdateKurstypModel;
import ch.metzenthin.svm.domain.model.DialogClosedListener;
import ch.metzenthin.svm.service.result.SaveKurstypResult;
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
public class CreateOrUpdateKurstypController extends AbstractController {

  private static final Logger LOGGER = LogManager.getLogger(CreateOrUpdateKurstypController.class);

  // Möglichkeit zum Umschalten des validation modes (nicht dynamisch)
  private static final boolean MODEL_VALIDATION_MODE = false;

  private final CreateOrUpdateKurstypModel createOrUpdateKurstypModel;
  private final boolean isBearbeiten;
  private final boolean defaultButtonEnabled;
  private final DialogClosedListener dialogClosedListener;
  private JDialog createOrUpdateKurstypDialog;
  private JTextField txtBezeichnung;
  private JCheckBox checkBoxSelektierbar;
  @Setter private JLabel errLblBezeichnung;
  private JButton btnSpeichern;

  public CreateOrUpdateKurstypController(
      CreateOrUpdateKurstypModel createOrUpdateKurstypModel,
      boolean isBearbeiten,
      boolean defaultButtonEnabled,
      DialogClosedListener dialogClosedListener) {
    super(createOrUpdateKurstypModel);
    this.createOrUpdateKurstypModel = createOrUpdateKurstypModel;
    this.isBearbeiten = isBearbeiten;
    this.defaultButtonEnabled = defaultButtonEnabled;
    this.dialogClosedListener = dialogClosedListener;
    this.createOrUpdateKurstypModel.addPropertyChangeListener(this);
    this.createOrUpdateKurstypModel.addDisableFieldsListener(this);
    this.createOrUpdateKurstypModel.addMakeErrorLabelsInvisibleListener(this);
    this.createOrUpdateKurstypModel.addCompletedListener(
        this::onCreateOrUpdateKurstypModelCompleted);
    this.setModelValidationMode(MODEL_VALIDATION_MODE);
  }

  public void constructionDone() {
    createOrUpdateKurstypModel.initializeCompleted();
  }

  public void setCreateOrUpdateKurstypDialog(JDialog createOrUpdateKurstypDialog) {
    // call onCancel() when cross is clicked
    this.createOrUpdateKurstypDialog = createOrUpdateKurstypDialog;
    createOrUpdateKurstypDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    createOrUpdateKurstypDialog.addWindowListener(
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

  public void setTxtBezeichnung(JTextField txtBezeichnung) {
    this.txtBezeichnung = txtBezeichnung;
    if (!defaultButtonEnabled) {
      this.txtBezeichnung.addActionListener(e -> onBezeichnungEvent(true));
    }
    this.txtBezeichnung.addFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onBezeichnungEvent(false);
          }
        });
  }

  private void onBezeichnungEvent(boolean showRequiredErrMsg) {
    LOGGER.trace("CreateOrUpdateKurstypController Event Bezeichnung");
    boolean equalFieldAndModelValue =
        equalsNullSafe(txtBezeichnung.getText(), createOrUpdateKurstypModel.getBezeichnung());
    try {
      setModelBezeichnung(showRequiredErrMsg);
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

  private void setModelBezeichnung(boolean showRequiredErrMsg) throws SvmValidationException {
    makeErrorLabelInvisible(Field.BEZEICHNUNG);
    try {
      createOrUpdateKurstypModel.setBezeichnung(txtBezeichnung.getText());
    } catch (SvmRequiredException e) {
      LOGGER.trace(
          "CreateOrUpdateKurstypController setModelBezeichnung RequiredException={}",
          e.getMessage());
      if (isModelValidationMode() || !showRequiredErrMsg) {
        txtBezeichnung.setToolTipText(e.getMessage());
        // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut, nachdem alle Field-Prüfungen
        // bestanden sind.
      } else {
        showErrMsg(e);
      }
      throw e;
    } catch (SvmValidationException e) {
      LOGGER.trace(
          "CreateOrUpdateKurstypController setModelBezeichnung Exception={}", e.getMessage());
      showErrMsg(e);
      throw e;
    }
  }

  public void setCheckBoxSelektierbar(JCheckBox checkBoxSelektierbar) {
    this.checkBoxSelektierbar = checkBoxSelektierbar;
    // Selektierbar als Default-Wert
    if (!isBearbeiten) {
      createOrUpdateKurstypModel.setSelektierbar(true);
    }
    this.checkBoxSelektierbar.addItemListener(e -> onSelektierbarEvent());
  }

  private void setModelSelektierbar() {
    createOrUpdateKurstypModel.setSelektierbar(checkBoxSelektierbar.isSelected());
  }

  private void onSelektierbarEvent() {
    LOGGER.trace(
        "CreateOrUpdateKurstypController Event Selektierbar. Selected={}",
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

  @SuppressWarnings("DuplicatedCode")
  private void onSpeichern() {
    if (!isModelValidationMode() && !validateOnSpeichern()) {
      btnSpeichern.setFocusPainted(false);
      return;
    }

    SaveKurstypResult saveKurstypResult = createOrUpdateKurstypModel.speichern();
    switch (saveKurstypResult) {
      case KURSTYP_BEREITS_ERFASST -> {
        JOptionPane.showMessageDialog(
            createOrUpdateKurstypDialog,
            "Bezeichnung bereits in Verwendung.",
            "Fehler",
            JOptionPane.ERROR_MESSAGE);
        btnSpeichern.setFocusPainted(false);
      }
      case KURSTYP_DURCH_ANDEREN_BENUTZER_VERAENDERT -> {
        closeDialog();
        JOptionPane.showMessageDialog(
            createOrUpdateKurstypDialog,
            "Der Wert konnte nicht gespeichert werden, da der Eintrag unterdessen durch \n"
                + "einen anderen Benutzer verändert oder gelöscht wurde.",
            "Fehler",
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
    createOrUpdateKurstypDialog.dispose();
    dialogClosedListener.onDialogClosed();
  }

  private void onCreateOrUpdateKurstypModelCompleted(boolean completed) {
    LOGGER.trace("CreateOrUpdateKurstypModel completed={}", completed);
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
    if (checkIsFieldChange(Field.BEZEICHNUNG, evt)) {
      txtBezeichnung.setText(createOrUpdateKurstypModel.getBezeichnung());
    } else if (checkIsFieldChange(Field.SELEKTIERBAR, evt)) {
      checkBoxSelektierbar.setSelected(createOrUpdateKurstypModel.isSelektierbar());
    }
  }

  @Override
  void validateFields() throws SvmValidationException {
    if (txtBezeichnung.isEnabled()) {
      LOGGER.trace("Validate field Bezeichnung");
      setModelBezeichnung(true);
    }
  }

  @Override
  void showErrMsg(SvmValidationException e) {
    if (e.getAffectedFields().contains(Field.BEZEICHNUNG)) {
      errLblBezeichnung.setVisible(true);
      errLblBezeichnung.setText(e.getMessage());
    }
  }

  @Override
  void showErrMsgAsToolTip(SvmValidationException e) {
    if (e.getAffectedFields().contains(Field.BEZEICHNUNG)) {
      txtBezeichnung.setToolTipText(e.getMessage());
    }
  }

  @Override
  public void makeErrorLabelsInvisible(Set<Field> fields) {
    if (fields.contains(Field.ALLE) || fields.contains(Field.BEZEICHNUNG)) {
      errLblBezeichnung.setVisible(false);
      txtBezeichnung.setToolTipText(null);
    }
  }

  @Override
  public void disableFields(boolean disable, Set<Field> fields) {
    // Keine zu deaktivierenden Felder
  }
}
