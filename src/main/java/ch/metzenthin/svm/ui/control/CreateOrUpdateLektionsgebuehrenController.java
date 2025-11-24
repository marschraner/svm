package ch.metzenthin.svm.ui.control;

import static ch.metzenthin.svm.common.utils.SimpleValidator.equalsNullSafe;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.CreateOrUpdateLektionsgebuehrenModel;
import ch.metzenthin.svm.domain.model.DialogClosedListener;
import ch.metzenthin.svm.service.result.SaveLektionsgebuehrenResult;
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
public class CreateOrUpdateLektionsgebuehrenController extends AbstractController {

  private static final Logger LOGGER =
      LogManager.getLogger(CreateOrUpdateLektionsgebuehrenController.class);
  private static final String VALIDIERUNG_WEGEN_EQUAL_FIELD_AND_MODEL_VALUE =
      "Validierung wegen equalFieldAndModelValue";

  // Möglichkeit zum Umschalten des validation modes (nicht dynamisch)
  private static final boolean MODEL_VALIDATION_MODE = false;

  private final CreateOrUpdateLektionsgebuehrenModel createOrUpdateLektionsgebuehrenModel;
  private final boolean isBearbeiten;
  private final boolean defaultButtonEnabled;
  private final DialogClosedListener dialogClosedListener;
  private JDialog createOrUpdatelektionsgebuehrenDialog;
  private JTextField txtLektionslaenge;
  private JTextField txtBetrag1Kind;
  private JTextField txtBetrag2Kinder;
  private JTextField txtBetrag3Kinder;
  private JTextField txtBetrag4Kinder;
  private JTextField txtBetrag5Kinder;
  private JTextField txtBetrag6Kinder;
  @Setter private JLabel errLblLektionslaenge;
  @Setter private JLabel errLblBetrag1Kind;
  @Setter private JLabel errLblBetrag2Kinder;
  @Setter private JLabel errLblBetrag3Kinder;
  @Setter private JLabel errLblBetrag4Kinder;
  @Setter private JLabel errLblBetrag5Kinder;
  @Setter private JLabel errLblBetrag6Kinder;
  private JButton btnSpeichern;

  public CreateOrUpdateLektionsgebuehrenController(
      CreateOrUpdateLektionsgebuehrenModel createOrUpdateLektionsgebuehrenModel,
      boolean isBearbeiten,
      boolean defaultButtonEnabled,
      DialogClosedListener dialogClosedListener) {
    super(createOrUpdateLektionsgebuehrenModel);
    this.createOrUpdateLektionsgebuehrenModel = createOrUpdateLektionsgebuehrenModel;
    this.isBearbeiten = isBearbeiten;
    this.defaultButtonEnabled = defaultButtonEnabled;
    this.dialogClosedListener = dialogClosedListener;
    this.createOrUpdateLektionsgebuehrenModel.addPropertyChangeListener(this);
    this.createOrUpdateLektionsgebuehrenModel.addDisableFieldsListener(this);
    this.createOrUpdateLektionsgebuehrenModel.addMakeErrorLabelsInvisibleListener(this);
    this.createOrUpdateLektionsgebuehrenModel.addCompletedListener(
        this::onCreateOrUpdateLektionsgebuehrenModelCompleted);
    this.setModelValidationMode(MODEL_VALIDATION_MODE);
  }

  public void constructionDone() {
    createOrUpdateLektionsgebuehrenModel.initializeCompleted();
  }

  public void setCreateOrUpdateLektionsgebuehrenDialog(
      JDialog createOrUpdateLektionsgebuehrenDialog) {
    // call onCancel() when cross is clicked
    this.createOrUpdatelektionsgebuehrenDialog = createOrUpdateLektionsgebuehrenDialog;
    createOrUpdateLektionsgebuehrenDialog.setDefaultCloseOperation(
        WindowConstants.DO_NOTHING_ON_CLOSE);
    createOrUpdateLektionsgebuehrenDialog.addWindowListener(
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

  public void setTxtLektionslaenge(JTextField txtLektionslaenge) {
    this.txtLektionslaenge = txtLektionslaenge;
    // ID darf nicht bearbeitet werden!
    if (isBearbeiten) {
      this.txtLektionslaenge.setEnabled(false);
    }
    if (!defaultButtonEnabled) {
      this.txtLektionslaenge.addActionListener(e -> onLektionslaengeEvent(true));
    }
    this.txtLektionslaenge.addFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onLektionslaengeEvent(false);
          }
        });
  }

  private void onLektionslaengeEvent(boolean showRequiredErrMsg) {
    LOGGER.trace("CreateOrUpdateLektionsgebuehrenController Event Lektionslaenge");
    boolean equalFieldAndModelValue =
        equalsNullSafe(
            txtLektionslaenge.getText(), createOrUpdateLektionsgebuehrenModel.getLektionslaenge());
    try {
      setModelLektionslaenge(showRequiredErrMsg);
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

  private void setModelLektionslaenge(boolean showRequiredErrMsg) throws SvmValidationException {
    makeErrorLabelInvisible(Field.LEKTIONSLAENGE);
    try {
      createOrUpdateLektionsgebuehrenModel.setLektionslaenge(txtLektionslaenge.getText());
    } catch (SvmRequiredException e) {
      LOGGER.trace(
          "CreateOrUpdateLektionsgebuehrenController setModelLektionslaenge RequiredException={}",
          e.getMessage());
      if (isModelValidationMode() || !showRequiredErrMsg) {
        txtLektionslaenge.setToolTipText(e.getMessage());
        // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut, nachdem alle Field-Prüfungen
        // bestanden sind.
      } else {
        showErrMsg(e);
      }
      throw e;
    } catch (SvmValidationException e) {
      LOGGER.trace(
          "CreateOrUpdateLektionsgebuehrenController setModelLektionslaenge Exception={}",
          e.getMessage());
      showErrMsg(e);
      throw e;
    }
  }

  public void setTxtBetrag1Kind(JTextField txtBetrag1Kind) {
    this.txtBetrag1Kind = txtBetrag1Kind;
    if (!defaultButtonEnabled) {
      this.txtBetrag1Kind.addActionListener(e -> onBetrag1KindEvent(true));
    }
    this.txtBetrag1Kind.addFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onBetrag1KindEvent(false);
          }
        });
  }

  private void onBetrag1KindEvent(boolean showRequiredErrMsg) {
    LOGGER.trace("CreateOrUpdateLektionsgebuehrenController Event Betrag1Kind");
    boolean equalFieldAndModelValue =
        equalsNullSafe(
            txtBetrag1Kind.getText(), createOrUpdateLektionsgebuehrenModel.getBetrag1Kind());
    try {
      setModelBetrag1Kind(showRequiredErrMsg);
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

  private void setModelBetrag1Kind(boolean showRequiredErrMsg) throws SvmValidationException {
    makeErrorLabelInvisible(Field.BETRAG_1_KIND);
    try {
      createOrUpdateLektionsgebuehrenModel.setBetrag1Kind(txtBetrag1Kind.getText());
    } catch (SvmRequiredException e) {
      LOGGER.trace(
          "CreateOrUpdateLektionsgebuehrenController setModelBetrag1Kind RequiredException={}",
          e.getMessage());
      if (isModelValidationMode() || !showRequiredErrMsg) {
        txtBetrag1Kind.setToolTipText(e.getMessage());
        // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut, nachdem alle Field-Prüfungen
        // bestanden sind.
      } else {
        showErrMsg(e);
      }
      throw e;
    } catch (SvmValidationException e) {
      LOGGER.trace(
          "CreateOrUpdateLektionsgebuehrenController setModelBetrag1Kind Exception={}",
          e.getMessage());
      showErrMsg(e);
      throw e;
    }
  }

  public void setTxtBetrag2Kinder(JTextField txtBetrag2Kinder) {
    this.txtBetrag2Kinder = txtBetrag2Kinder;
    if (!defaultButtonEnabled) {
      this.txtBetrag2Kinder.addActionListener(e -> onBetrag2KinderEvent(true));
    }
    this.txtBetrag2Kinder.addFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onBetrag2KinderEvent(false);
          }
        });
  }

  private void onBetrag2KinderEvent(boolean showRequiredErrMsg) {
    LOGGER.trace("CreateOrUpdateLektionsgebuehrenController Event Betrag2Kinder");
    boolean equalFieldAndModelValue =
        equalsNullSafe(
            txtBetrag2Kinder.getText(), createOrUpdateLektionsgebuehrenModel.getBetrag2Kinder());
    try {
      setModelBetrag2Kinder(showRequiredErrMsg);
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

  private void setModelBetrag2Kinder(boolean showRequiredErrMsg) throws SvmValidationException {
    makeErrorLabelInvisible(Field.BETRAG_2_KINDER);
    try {
      createOrUpdateLektionsgebuehrenModel.setBetrag2Kinder(txtBetrag2Kinder.getText());
    } catch (SvmRequiredException e) {
      LOGGER.trace(
          "CreateOrUpdateLektionsgebuehrenController setModelBetrag2Kinder RequiredException={}",
          e.getMessage());
      if (isModelValidationMode() || !showRequiredErrMsg) {
        txtBetrag2Kinder.setToolTipText(e.getMessage());
        // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut, nachdem alle Field-Prüfungen
        // bestanden sind.
      } else {
        showErrMsg(e);
      }
      throw e;
    } catch (SvmValidationException e) {
      LOGGER.trace(
          "CreateOrUpdateLektionsgebuehrenController setModelBetrag2Kinder Exception={}",
          e.getMessage());
      showErrMsg(e);
      throw e;
    }
  }

  public void setTxtBetrag3Kinder(JTextField txtBetrag3Kinder) {
    this.txtBetrag3Kinder = txtBetrag3Kinder;
    if (!defaultButtonEnabled) {
      this.txtBetrag3Kinder.addActionListener(e -> onBetrag3KinderEvent(true));
    }
    this.txtBetrag3Kinder.addFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onBetrag3KinderEvent(false);
          }
        });
  }

  private void onBetrag3KinderEvent(boolean showRequiredErrMsg) {
    LOGGER.trace("CreateOrUpdateLektionsgebuehrenController Event Betrag3Kinder");
    boolean equalFieldAndModelValue =
        equalsNullSafe(
            txtBetrag3Kinder.getText(), createOrUpdateLektionsgebuehrenModel.getBetrag3Kinder());
    try {
      setModelBetrag3Kinder(showRequiredErrMsg);
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

  private void setModelBetrag3Kinder(boolean showRequiredErrMsg) throws SvmValidationException {
    makeErrorLabelInvisible(Field.BETRAG_3_KINDER);
    try {
      createOrUpdateLektionsgebuehrenModel.setBetrag3Kinder(txtBetrag3Kinder.getText());
    } catch (SvmRequiredException e) {
      LOGGER.trace(
          "CreateOrUpdateLektionsgebuehrenController setModelBetrag3Kinder RequiredException={}",
          e.getMessage());
      if (isModelValidationMode() || !showRequiredErrMsg) {
        txtBetrag3Kinder.setToolTipText(e.getMessage());
        // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut, nachdem alle Field-Prüfungen
        // bestanden sind.
      } else {
        showErrMsg(e);
      }
      throw e;
    } catch (SvmValidationException e) {
      LOGGER.trace(
          "CreateOrUpdateLektionsgebuehrenController setModelBetrag3Kinder Exception={}",
          e.getMessage());
      showErrMsg(e);
      throw e;
    }
  }

  public void setTxtBetrag4Kinder(JTextField txtBetrag4Kinder) {
    this.txtBetrag4Kinder = txtBetrag4Kinder;
    if (!defaultButtonEnabled) {
      this.txtBetrag4Kinder.addActionListener(e -> onBetrag4KinderEvent(true));
    }
    this.txtBetrag4Kinder.addFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onBetrag4KinderEvent(false);
          }
        });
  }

  private void onBetrag4KinderEvent(boolean showRequiredErrMsg) {
    LOGGER.trace("CreateOrUpdateLektionsgebuehrenController Event Betrag4Kinder");
    boolean equalFieldAndModelValue =
        equalsNullSafe(
            txtBetrag4Kinder.getText(), createOrUpdateLektionsgebuehrenModel.getBetrag4Kinder());
    try {
      setModelBetrag4Kinder(showRequiredErrMsg);
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

  private void setModelBetrag4Kinder(boolean showRequiredErrMsg) throws SvmValidationException {
    makeErrorLabelInvisible(Field.BETRAG_4_KINDER);
    try {
      createOrUpdateLektionsgebuehrenModel.setBetrag4Kinder(txtBetrag4Kinder.getText());
    } catch (SvmRequiredException e) {
      LOGGER.trace(
          "CreateOrUpdateLektionsgebuehrenController setModelBetrag4Kinder RequiredException={}",
          e.getMessage());
      if (isModelValidationMode() || !showRequiredErrMsg) {
        txtBetrag4Kinder.setToolTipText(e.getMessage());
        // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut, nachdem alle Field-Prüfungen
        // bestanden sind.
      } else {
        showErrMsg(e);
      }
      throw e;
    } catch (SvmValidationException e) {
      LOGGER.trace(
          "CreateOrUpdateLektionsgebuehrenController setModelBetrag4Kinder Exception={}",
          e.getMessage());
      showErrMsg(e);
      throw e;
    }
  }

  public void setTxtBetrag5Kinder(JTextField txtBetrag5Kinder) {
    this.txtBetrag5Kinder = txtBetrag5Kinder;
    if (!defaultButtonEnabled) {
      this.txtBetrag5Kinder.addActionListener(e -> onBetrag5KinderEvent(true));
    }
    this.txtBetrag5Kinder.addFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onBetrag5KinderEvent(false);
          }
        });
  }

  private void onBetrag5KinderEvent(boolean showRequiredErrMsg) {
    LOGGER.trace("CreateOrUpdateLektionsgebuehrenController Event Betrag5Kinder");
    boolean equalFieldAndModelValue =
        equalsNullSafe(
            txtBetrag5Kinder.getText(), createOrUpdateLektionsgebuehrenModel.getBetrag5Kinder());
    try {
      setModelBetrag5Kinder(showRequiredErrMsg);
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

  private void setModelBetrag5Kinder(boolean showRequiredErrMsg) throws SvmValidationException {
    makeErrorLabelInvisible(Field.BETRAG_5_KINDER);
    try {
      createOrUpdateLektionsgebuehrenModel.setBetrag5Kinder(txtBetrag5Kinder.getText());
    } catch (SvmRequiredException e) {
      LOGGER.trace(
          "CreateOrUpdateLektionsgebuehrenController setModelBetrag5Kinder RequiredException={}",
          e.getMessage());
      if (isModelValidationMode() || !showRequiredErrMsg) {
        txtBetrag5Kinder.setToolTipText(e.getMessage());
        // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut, nachdem alle Field-Prüfungen
        // bestanden sind.
      } else {
        showErrMsg(e);
      }
      throw e;
    } catch (SvmValidationException e) {
      LOGGER.trace(
          "CreateOrUpdateLektionsgebuehrenController setModelBetrag5Kinder Exception={}",
          e.getMessage());
      showErrMsg(e);
      throw e;
    }
  }

  public void setTxtBetrag6Kinder(JTextField txtBetrag6Kinder) {
    this.txtBetrag6Kinder = txtBetrag6Kinder;
    if (!defaultButtonEnabled) {
      this.txtBetrag6Kinder.addActionListener(e -> onBetrag6KinderEvent(true));
    }
    this.txtBetrag6Kinder.addFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onBetrag6KinderEvent(false);
          }
        });
  }

  private void onBetrag6KinderEvent(boolean showRequiredErrMsg) {
    LOGGER.trace("CreateOrUpdateLektionsgebuehrenController Event Betrag6Kinder");
    boolean equalFieldAndModelValue =
        equalsNullSafe(
            txtBetrag6Kinder.getText(), createOrUpdateLektionsgebuehrenModel.getBetrag6Kinder());
    try {
      setModelBetrag6Kinder(showRequiredErrMsg);
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

  private void setModelBetrag6Kinder(boolean showRequiredErrMsg) throws SvmValidationException {
    makeErrorLabelInvisible(Field.BETRAG_6_KINDER);
    try {
      createOrUpdateLektionsgebuehrenModel.setBetrag6Kinder(txtBetrag6Kinder.getText());
    } catch (SvmRequiredException e) {
      LOGGER.trace(
          "CreateOrUpdateLektionsgebuehrenController setModelBetrag6Kinder RequiredException={}",
          e.getMessage());
      if (isModelValidationMode() || !showRequiredErrMsg) {
        txtBetrag6Kinder.setToolTipText(e.getMessage());
        // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut, nachdem alle Field-Prüfungen
        // bestanden sind.
      } else {
        showErrMsg(e);
      }
      throw e;
    } catch (SvmValidationException e) {
      LOGGER.trace(
          "CreateOrUpdateLektionsgebuehrenController setModelBetrag6Kinder Exception={}",
          e.getMessage());
      showErrMsg(e);
      throw e;
    }
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
    SaveLektionsgebuehrenResult saveLektionsgebuehrenResult =
        createOrUpdateLektionsgebuehrenModel.speichern();
    switch (saveLektionsgebuehrenResult) {
      case LEKTIONSGEBUEHREN_BEREITS_ERFASST -> {
        JOptionPane.showMessageDialog(
            createOrUpdatelektionsgebuehrenDialog,
            "Lektionslänge bereits erfasst.",
            "Fehler",
            JOptionPane.ERROR_MESSAGE);
        btnSpeichern.setFocusPainted(false);
      }
      case LEKTIONSGEBUEHREN_DURCH_ANDEREN_BENUTZER_VERAENDERT -> {
        closeDialog();
        JOptionPane.showMessageDialog(
            createOrUpdatelektionsgebuehrenDialog,
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
    createOrUpdatelektionsgebuehrenDialog.dispose();
    dialogClosedListener.onDialogClosed();
  }

  private void onCreateOrUpdateLektionsgebuehrenModelCompleted(boolean completed) {
    LOGGER.trace("CreateOrUpdateLektionsgebuehrenModel completed={}", completed);
    if (completed) {
      btnSpeichern.setToolTipText(null);
      btnSpeichern.setEnabled(true);
    } else {
      btnSpeichern.setToolTipText("Bitte Eingabedaten vervollständigen");
      btnSpeichern.setEnabled(false);
    }
  }

  @SuppressWarnings("java:S3776")
  @Override
  void doPropertyChange(PropertyChangeEvent evt) {
    super.doPropertyChange(evt);
    if (checkIsFieldChange(Field.LEKTIONSLAENGE, evt)) {
      txtLektionslaenge.setText(
          Integer.toString(createOrUpdateLektionsgebuehrenModel.getLektionslaenge()));
    } else if (checkIsFieldChange(Field.BETRAG_1_KIND, evt)) {
      txtBetrag1Kind.setText(
          createOrUpdateLektionsgebuehrenModel.getBetrag1Kind() == null
              ? null
              : createOrUpdateLektionsgebuehrenModel.getBetrag1Kind().toString());
    } else if (checkIsFieldChange(Field.BETRAG_2_KINDER, evt)) {
      txtBetrag2Kinder.setText(
          createOrUpdateLektionsgebuehrenModel.getBetrag2Kinder() == null
              ? null
              : createOrUpdateLektionsgebuehrenModel.getBetrag2Kinder().toString());
    } else if (checkIsFieldChange(Field.BETRAG_3_KINDER, evt)) {
      txtBetrag3Kinder.setText(
          createOrUpdateLektionsgebuehrenModel.getBetrag3Kinder() == null
              ? null
              : createOrUpdateLektionsgebuehrenModel.getBetrag3Kinder().toString());
    } else if (checkIsFieldChange(Field.BETRAG_4_KINDER, evt)) {
      txtBetrag4Kinder.setText(
          createOrUpdateLektionsgebuehrenModel.getBetrag4Kinder() == null
              ? null
              : createOrUpdateLektionsgebuehrenModel.getBetrag4Kinder().toString());
    } else if (checkIsFieldChange(Field.BETRAG_5_KINDER, evt)) {
      txtBetrag5Kinder.setText(
          createOrUpdateLektionsgebuehrenModel.getBetrag5Kinder() == null
              ? null
              : createOrUpdateLektionsgebuehrenModel.getBetrag5Kinder().toString());
    } else if (checkIsFieldChange(Field.BETRAG_6_KINDER, evt)) {
      txtBetrag6Kinder.setText(
          createOrUpdateLektionsgebuehrenModel.getBetrag6Kinder() == null
              ? null
              : createOrUpdateLektionsgebuehrenModel.getBetrag6Kinder().toString());
    }
  }

  @Override
  void validateFields() throws SvmValidationException {
    if (txtLektionslaenge.isEnabled()) {
      LOGGER.trace("Validate field Lektionslaenge");
      setModelLektionslaenge(true);
    }
    if (txtBetrag1Kind.isEnabled()) {
      LOGGER.trace("Validate field Betrag 1 Kind");
      setModelBetrag1Kind(true);
    }
    if (txtBetrag2Kinder.isEnabled()) {
      LOGGER.trace("Validate field Betrag 2 Kinder");
      setModelBetrag2Kinder(true);
    }
    if (txtBetrag3Kinder.isEnabled()) {
      LOGGER.trace("Validate field Betrag 3 Kinder");
      setModelBetrag3Kinder(true);
    }
    if (txtBetrag4Kinder.isEnabled()) {
      LOGGER.trace("Validate field Betrag 4 Kinder");
      setModelBetrag4Kinder(true);
    }
    if (txtBetrag5Kinder.isEnabled()) {
      LOGGER.trace("Validate field Betrag 5 Kinder");
      setModelBetrag5Kinder(true);
    }
    if (txtBetrag6Kinder.isEnabled()) {
      LOGGER.trace("Validate field Betrag 6 Kinder");
      setModelBetrag6Kinder(true);
    }
  }

  @Override
  void showErrMsg(SvmValidationException e) {
    if (e.getAffectedFields().contains(Field.LEKTIONSLAENGE)) {
      errLblLektionslaenge.setVisible(true);
      errLblLektionslaenge.setText(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.BETRAG_1_KIND)) {
      errLblBetrag1Kind.setVisible(true);
      errLblBetrag1Kind.setText(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.BETRAG_2_KINDER)) {
      errLblBetrag2Kinder.setVisible(true);
      errLblBetrag2Kinder.setText(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.BETRAG_3_KINDER)) {
      errLblBetrag3Kinder.setVisible(true);
      errLblBetrag3Kinder.setText(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.BETRAG_4_KINDER)) {
      errLblBetrag4Kinder.setVisible(true);
      errLblBetrag4Kinder.setText(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.BETRAG_5_KINDER)) {
      errLblBetrag5Kinder.setVisible(true);
      errLblBetrag5Kinder.setText(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.BETRAG_6_KINDER)) {
      errLblBetrag6Kinder.setVisible(true);
      errLblBetrag6Kinder.setText(e.getMessage());
    }
  }

  @Override
  void showErrMsgAsToolTip(SvmValidationException e) {
    if (e.getAffectedFields().contains(Field.LEKTIONSLAENGE)) {
      txtLektionslaenge.setToolTipText(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.BETRAG_1_KIND)) {
      txtBetrag1Kind.setToolTipText(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.BETRAG_2_KINDER)) {
      txtBetrag2Kinder.setToolTipText(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.BETRAG_3_KINDER)) {
      txtBetrag3Kinder.setToolTipText(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.BETRAG_4_KINDER)) {
      txtBetrag4Kinder.setToolTipText(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.BETRAG_5_KINDER)) {
      txtBetrag5Kinder.setToolTipText(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.BETRAG_6_KINDER)) {
      txtBetrag6Kinder.setToolTipText(e.getMessage());
    }
  }

  @Override
  public void makeErrorLabelsInvisible(Set<Field> fields) {
    if (fields.contains(Field.ALLE) || fields.contains(Field.LEKTIONSLAENGE)) {
      errLblLektionslaenge.setVisible(false);
      txtLektionslaenge.setToolTipText(null);
    }
    if (fields.contains(Field.ALLE) || fields.contains(Field.BETRAG_1_KIND)) {
      errLblBetrag1Kind.setVisible(false);
      txtBetrag1Kind.setToolTipText(null);
    }
    if (fields.contains(Field.ALLE) || fields.contains(Field.BETRAG_2_KINDER)) {
      errLblBetrag2Kinder.setVisible(false);
      txtBetrag2Kinder.setToolTipText(null);
    }
    if (fields.contains(Field.ALLE) || fields.contains(Field.BETRAG_3_KINDER)) {
      errLblBetrag3Kinder.setVisible(false);
      txtBetrag3Kinder.setToolTipText(null);
    }
    if (fields.contains(Field.ALLE) || fields.contains(Field.BETRAG_4_KINDER)) {
      errLblBetrag4Kinder.setVisible(false);
      txtBetrag4Kinder.setToolTipText(null);
    }
    if (fields.contains(Field.ALLE) || fields.contains(Field.BETRAG_5_KINDER)) {
      errLblBetrag5Kinder.setVisible(false);
      txtBetrag5Kinder.setToolTipText(null);
    }
    if (fields.contains(Field.ALLE) || fields.contains(Field.BETRAG_6_KINDER)) {
      errLblBetrag6Kinder.setVisible(false);
      txtBetrag6Kinder.setToolTipText(null);
    }
  }

  @Override
  public void disableFields(boolean disable, Set<Field> fields) {
    // Keine zu deaktivierenden Felder
  }
}
