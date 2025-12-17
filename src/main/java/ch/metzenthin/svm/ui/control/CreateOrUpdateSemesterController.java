package ch.metzenthin.svm.ui.control;

import static ch.metzenthin.svm.common.utils.Converter.asString;
import static ch.metzenthin.svm.common.utils.SimpleValidator.equalsNullSafe;
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

import ch.metzenthin.svm.common.SvmRuntimeException;
import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.common.datatypes.Semesterbezeichnung;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.CreateOrUpdateSemesterModel;
import ch.metzenthin.svm.domain.model.DialogClosedListener;
import ch.metzenthin.svm.service.result.SaveSemesterResult;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import javax.swing.*;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Martin Schraner
 */
@SuppressWarnings("LoggingSimilarMessage")
public class CreateOrUpdateSemesterController extends AbstractController {

  private static final Logger LOGGER = LogManager.getLogger(CreateOrUpdateSemesterController.class);
  private static final String VALIDIERUNG_WEGEN_EQUAL_FIELD_AND_MODEL_VALUE =
      "Validierung wegen equalFieldAndModelValue";

  // Möglichkeit zum Umschalten des validation modes (nicht dynamisch)
  private static final boolean MODEL_VALIDATION_MODE = false;

  private final CreateOrUpdateSemesterModel createOrUpdateSemesterModel;
  private final boolean isBearbeiten;
  private final boolean defaultButtonEnabled;
  private final DialogClosedListener dialogClosedListener;
  private JDialog createOrUpdateSemesterDialog;
  private JSpinner spinnerSchuljahre;
  private JComboBox<Semesterbezeichnung> comboBoxSemesterbezeichnung;
  private JTextField txtSemesterbeginn;
  private JTextField txtSemesterende;
  private JTextField txtFerienbeginn1;
  private JTextField txtFerienende1;
  private JTextField txtFerienbeginn2;
  private JTextField txtFerienende2;
  @Setter private JLabel errLblSemesterbeginn;
  @Setter private JLabel errLblSemesterende;
  @Setter private JLabel errLblFerienbeginn1;
  @Setter private JLabel errLblFerienende1;
  @Setter private JLabel errLblFerienbeginn2;
  @Setter private JLabel errLblFerienende2;
  private JButton btnSpeichern;

  public CreateOrUpdateSemesterController(
      CreateOrUpdateSemesterModel createOrUpdateSemesterModel,
      boolean isBearbeiten,
      boolean defaultButtonEnabled,
      DialogClosedListener dialogClosedListener) {
    super(createOrUpdateSemesterModel);
    this.createOrUpdateSemesterModel = createOrUpdateSemesterModel;
    this.isBearbeiten = isBearbeiten;
    this.defaultButtonEnabled = defaultButtonEnabled;
    this.dialogClosedListener = dialogClosedListener;
    this.createOrUpdateSemesterModel.addPropertyChangeListener(this);
    this.createOrUpdateSemesterModel.addDisableFieldsListener(this);
    this.createOrUpdateSemesterModel.addMakeErrorLabelsInvisibleListener(this);
    this.createOrUpdateSemesterModel.addCompletedListener(
        this::onCreateOrUpdateSemesterModelCompleted);
    this.setModelValidationMode(MODEL_VALIDATION_MODE);
  }

  public void constructionDone() {
    createOrUpdateSemesterModel.initializeCompleted();
  }

  public void setCreateOrUpdateSemesterDialog(JDialog createOrUpdateSemesterDialog) {
    // call onCancel() when cross is clicked
    this.createOrUpdateSemesterDialog = createOrUpdateSemesterDialog;
    createOrUpdateSemesterDialog.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    createOrUpdateSemesterDialog.addWindowListener(
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

  public void setSpinnerSchuljahre(JSpinner spinnerSchuljahre) {
    this.spinnerSchuljahre = spinnerSchuljahre;
    spinnerSchuljahre.addChangeListener(e -> onSchuljahrSelected());
    if (!isBearbeiten) {
      initSchuljahr();
    }
  }

  private void initSchuljahr() {
    String schuljahr =
        createOrUpdateSemesterModel.getNaechstesNochNichtErfasstesSemester().getSchuljahr();
    try {
      createOrUpdateSemesterModel.setSchuljahr(schuljahr);
    } catch (SvmValidationException e) {
      LOGGER.error(e.getMessage());
    }
  }

  @SuppressWarnings("DuplicatedCode")
  private void onSchuljahrSelected() {
    LOGGER.trace("PersonController Event Schuljahre selected ={}", spinnerSchuljahre.getValue());
    boolean equalFieldAndModelValue =
        equalsNullSafe(spinnerSchuljahre.getValue(), createOrUpdateSemesterModel.getSchuljahr());
    try {
      setModelSchuljahr();
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

  private void setModelSchuljahr() throws SvmValidationException {
    makeErrorLabelInvisible(Field.SCHULJAHR);
    try {
      createOrUpdateSemesterModel.setSchuljahr((String) spinnerSchuljahre.getValue());
    } catch (SvmValidationException e) {
      LOGGER.trace(
          "CreateOrUpdateSemesterController setModelSchuljahr Exception={}", e.getMessage());
      showErrMsg(e);
      throw e;
    }
  }

  public void setComboBoxSemesterbezeichnung(
      JComboBox<Semesterbezeichnung> comboBoxSemesterbezeichnung) {
    this.comboBoxSemesterbezeichnung = comboBoxSemesterbezeichnung;
    comboBoxSemesterbezeichnung.setModel(new DefaultComboBoxModel<>(Semesterbezeichnung.values()));
    comboBoxSemesterbezeichnung.addActionListener(e -> onSemesterbezeichnungSelected());
    if (!isBearbeiten) {
      initSemesterbezeichnung();
    }
  }

  private void initSemesterbezeichnung() {
    Semesterbezeichnung semesterbezeichnung =
        createOrUpdateSemesterModel
            .getNaechstesNochNichtErfasstesSemester()
            .getSemesterbezeichnung();
    try {
      createOrUpdateSemesterModel.setSemesterbezeichnung(semesterbezeichnung);
    } catch (SvmValidationException e) {
      LOGGER.error(e.getMessage());
    }
  }

  private void onSemesterbezeichnungSelected() {
    LOGGER.trace(
        "CreateOrUpdateSemesterController Event Semesterbezeichnung selected={}",
        comboBoxSemesterbezeichnung.getSelectedItem());
    boolean equalFieldAndModelValue =
        equalsNullSafe(
            comboBoxSemesterbezeichnung.getSelectedItem(),
            createOrUpdateSemesterModel.getSemesterbezeichnung());
    try {
      setModelSemesterbezeichnung();
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

  private void setModelSemesterbezeichnung() throws SvmValidationException {
    makeErrorLabelInvisible(Field.SEMESTERBEZEICHNUNG);
    try {
      createOrUpdateSemesterModel.setSemesterbezeichnung(
          (Semesterbezeichnung) comboBoxSemesterbezeichnung.getSelectedItem());
    } catch (SvmValidationException e) {
      LOGGER.trace("PersonController setModelSemesterbezeichnung Exception={}", e.getMessage());
      showErrMsg(e);
      throw e;
    }
  }

  public void setTxtSemesterbeginn(JTextField txtSemesterbeginn) {
    this.txtSemesterbeginn = txtSemesterbeginn;
    if (!defaultButtonEnabled) {
      this.txtSemesterbeginn.addActionListener(e -> onSemesterbeginnEvent(true));
    }
    this.txtSemesterbeginn.addFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onSemesterbeginnEvent(false);
          }
        });
  }

  private void onSemesterbeginnEvent(boolean showRequiredErrMsg) {
    LOGGER.trace("CreateOrUpdateSemesterController Event Semesterbeginn");
    boolean equalFieldAndModelValue =
        equalsNullSafe(
            txtSemesterbeginn.getText(), createOrUpdateSemesterModel.getSemesterbeginn());
    try {
      setModelSemesterbeginn(showRequiredErrMsg);
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

  private void setModelSemesterbeginn(boolean showRequiredErrMsg) throws SvmValidationException {
    makeErrorLabelInvisible(Field.SEMESTERBEGINN);
    try {
      createOrUpdateSemesterModel.setSemesterbeginn(txtSemesterbeginn.getText());
    } catch (SvmRequiredException e) {
      LOGGER.trace(
          "CreateOrUpdateSemesterController setModelSemesterbeginn RequiredException={}",
          e.getMessage());
      if (isModelValidationMode() || !showRequiredErrMsg) {
        txtSemesterbeginn.setToolTipText(e.getMessage());
        // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut, nachdem alle Field-Prüfungen
        // bestanden sind.
      } else {
        showErrMsg(e);
      }
      throw e;
    } catch (SvmValidationException e) {
      LOGGER.trace(
          "CreateOrUpdateSemesterController setModelSemesterbeginn Exception={}", e.getMessage());
      showErrMsg(e);
      throw e;
    }
  }

  public void setTxtSemesterende(JTextField txtSemesterende) {
    this.txtSemesterende = txtSemesterende;
    if (!defaultButtonEnabled) {
      this.txtSemesterende.addActionListener(e -> onSemesterendeEvent(true));
    }
    this.txtSemesterende.addFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onSemesterendeEvent(false);
          }
        });
  }

  private void onSemesterendeEvent(boolean showRequiredErrMsg) {
    LOGGER.trace("CreateOrUpdateSemesterController Event Semesterende");
    boolean equalFieldAndModelValue =
        equalsNullSafe(txtSemesterende.getText(), createOrUpdateSemesterModel.getSemesterende());
    try {
      setModelSemesterende(showRequiredErrMsg);
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

  private void setModelSemesterende(boolean showRequiredErrMsg) throws SvmValidationException {
    makeErrorLabelInvisible(Field.SEMESTERENDE);
    try {
      createOrUpdateSemesterModel.setSemesterende(txtSemesterende.getText());
    } catch (SvmRequiredException e) {
      LOGGER.trace(
          "CreateOrUpdateSemesterController setModelSemesterende RequiredException={}",
          e.getMessage());
      if (isModelValidationMode() || !showRequiredErrMsg) {
        txtSemesterende.setToolTipText(e.getMessage());
        // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut, nachdem alle Field-Prüfungen
        // bestanden sind.
      } else {
        showErrMsg(e);
      }
      throw e;
    } catch (SvmValidationException e) {
      LOGGER.trace(
          "CreateOrUpdateSemesterController setModelSemesterende Exception={}", e.getMessage());
      showErrMsg(e);
      throw e;
    }
  }

  public void setTxtFerienbeginn1(JTextField txtFerienbeginn1) {
    this.txtFerienbeginn1 = txtFerienbeginn1;
    if (!defaultButtonEnabled) {
      this.txtFerienbeginn1.addActionListener(e -> onFerienbeginn1Event(true));
    }
    this.txtFerienbeginn1.addFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onFerienbeginn1Event(false);
          }
        });
  }

  private void onFerienbeginn1Event(boolean showRequiredErrMsg) {
    LOGGER.trace("CreateOrUpdateSemesterController Event Ferienbeginn1");
    boolean equalFieldAndModelValue =
        equalsNullSafe(txtFerienbeginn1.getText(), createOrUpdateSemesterModel.getFerienbeginn1());
    try {
      setModelFerienbeginn1(showRequiredErrMsg);
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

  private void setModelFerienbeginn1(boolean showRequiredErrMsg) throws SvmValidationException {
    makeErrorLabelInvisible(Field.FERIENBEGINN1);
    try {
      createOrUpdateSemesterModel.setFerienbeginn1(txtFerienbeginn1.getText());
    } catch (SvmRequiredException e) {
      LOGGER.trace(
          "CreateOrUpdateSemesterController setModelFerienbeginn1 RequiredException={}",
          e.getMessage());
      if (isModelValidationMode() || !showRequiredErrMsg) {
        txtFerienbeginn1.setToolTipText(e.getMessage());
        // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut, nachdem alle Field-Prüfungen
        // bestanden sind.
      } else {
        showErrMsg(e);
      }
      throw e;
    } catch (SvmValidationException e) {
      LOGGER.trace(
          "CreateOrUpdateSemesterController setModelFerienbeginn1 Exception={}", e.getMessage());
      showErrMsg(e);
      throw e;
    }
  }

  public void setTxtFerienende1(JTextField txtFerienende1) {
    this.txtFerienende1 = txtFerienende1;
    if (!defaultButtonEnabled) {
      this.txtFerienende1.addActionListener(e -> onFerienende1Event(true));
    }
    this.txtFerienende1.addFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onFerienende1Event(false);
          }
        });
  }

  private void onFerienende1Event(boolean showRequiredErrMsg) {
    LOGGER.trace("CreateOrUpdateSemesterController Event Ferienende1");
    boolean equalFieldAndModelValue =
        equalsNullSafe(txtFerienende1.getText(), createOrUpdateSemesterModel.getFerienende1());
    try {
      setModelFerienende1(showRequiredErrMsg);
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

  private void setModelFerienende1(boolean showRequiredErrMsg) throws SvmValidationException {
    makeErrorLabelInvisible(Field.FERIENENDE1);
    try {
      createOrUpdateSemesterModel.setFerienende1(txtFerienende1.getText());
    } catch (SvmRequiredException e) {
      LOGGER.trace(
          "CreateOrUpdateSemesterController setModelFerienende1 RequiredException={}",
          e.getMessage());
      if (isModelValidationMode() || !showRequiredErrMsg) {
        txtFerienende1.setToolTipText(e.getMessage());
        // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut, nachdem alle Field-Prüfungen
        // bestanden sind.
      } else {
        showErrMsg(e);
      }
      throw e;
    } catch (SvmValidationException e) {
      LOGGER.trace(
          "CreateOrUpdateSemesterController setModelFerienende1 Exception={}", e.getMessage());
      showErrMsg(e);
      throw e;
    }
  }

  public void setTxtFerienbeginn2(JTextField txtFerienbeginn2) {
    this.txtFerienbeginn2 = txtFerienbeginn2;
    if (!defaultButtonEnabled) {
      this.txtFerienbeginn2.addActionListener(e -> onFerienbeginn2Event(true));
    }
    this.txtFerienbeginn2.addFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onFerienbeginn2Event(false);
          }
        });
  }

  private void onFerienbeginn2Event(boolean showRequiredErrMsg) {
    LOGGER.trace("CreateOrUpdateSemesterController Event Ferienbeginn2");
    boolean equalFieldAndModelValue =
        equalsNullSafe(txtFerienbeginn2.getText(), createOrUpdateSemesterModel.getFerienbeginn2());
    try {
      setModelFerienbeginn2(showRequiredErrMsg);
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

  private void setModelFerienbeginn2(boolean showRequiredErrMsg) throws SvmValidationException {
    makeErrorLabelInvisible(Field.FERIENBEGINN2);
    try {
      createOrUpdateSemesterModel.setFerienbeginn2(txtFerienbeginn2.getText());
    } catch (SvmRequiredException e) {
      LOGGER.trace(
          "CreateOrUpdateSemesterController setModelFerienbeginn2 RequiredException={}",
          e.getMessage());
      if (isModelValidationMode() || !showRequiredErrMsg) {
        txtFerienbeginn2.setToolTipText(e.getMessage());
        // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut, nachdem alle Field-Prüfungen
        // bestanden sind.
      } else {
        showErrMsg(e);
      }
      throw e;
    } catch (SvmValidationException e) {
      LOGGER.trace(
          "CreateOrUpdateSemesterController setModelFerienbeginn2 Exception={}", e.getMessage());
      showErrMsg(e);
      throw e;
    }
  }

  public void setTxtFerienende2(JTextField txtFerienende2) {
    this.txtFerienende2 = txtFerienende2;
    if (!defaultButtonEnabled) {
      this.txtFerienende2.addActionListener(e -> onFerienende2Event(true));
    }
    this.txtFerienende2.addFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onFerienende2Event(false);
          }
        });
  }

  private void onFerienende2Event(boolean showRequiredErrMsg) {
    LOGGER.trace("CreateOrUpdateSemesterController Event Ferienende2");
    boolean equalFieldAndModelValue =
        equalsNullSafe(txtFerienende2.getText(), createOrUpdateSemesterModel.getFerienende2());
    try {
      setModelFerienende2(showRequiredErrMsg);
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

  private void setModelFerienende2(boolean showRequiredErrMsg) throws SvmValidationException {
    makeErrorLabelInvisible(Field.FERIENENDE2);
    try {
      createOrUpdateSemesterModel.setFerienende2(txtFerienende2.getText());
    } catch (SvmRequiredException e) {
      LOGGER.trace(
          "CreateOrUpdateSemesterController setModelFerienende2 RequiredException={}",
          e.getMessage());
      if (isModelValidationMode() || !showRequiredErrMsg) {
        txtFerienende2.setToolTipText(e.getMessage());
        // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut, nachdem alle Field-Prüfungen
        // bestanden sind.
      } else {
        showErrMsg(e);
      }
      throw e;
    } catch (SvmValidationException e) {
      LOGGER.trace(
          "CreateOrUpdateSemesterController setModelFerienende2 Exception={}", e.getMessage());
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

    boolean affectsSemesterrechnungen =
        createOrUpdateSemesterModel.checkIfUpdateAffectsSemesterrechnungen();
    SaveSemesterResult saveSemesterResult;
    if (affectsSemesterrechnungen) {
      Object[] optionsImport = {"Ja", "Nein"};
      int n =
          JOptionPane.showOptionDialog(
              null,
              "Soll die Anzahl Semesterwochen auch bei den Semesterrechnungen geändert werden? \n"
                  + "(Semesterrechnungen mit gesetztem Rechnungsdatum werden nicht verändert.)",
              "Anzahl Semesterwochen auch bei Semesterrechnungen ändern?",
              JOptionPane.YES_NO_OPTION,
              JOptionPane.QUESTION_MESSAGE,
              null,
              optionsImport, // the titles of buttons
              optionsImport[0]); // default button title
      if (n == 0) {
        JDialog semesterSpeichernBusyDialog = createSemesterSpeichernBusyDialog();
        SwingWorker<SaveSemesterResult, Void> semesterSpeichernSwingWorker =
            getSpeichernSwingWorker(semesterSpeichernBusyDialog);
        semesterSpeichernBusyDialog.setVisible(true);
        // der nachfolgende Code ist blockiert, bis der Worker beendet ist und den BusyDialog
        // schliesst
        saveSemesterResult = getSaveSemesterResult(semesterSpeichernSwingWorker);

      } else {
        saveSemesterResult = createOrUpdateSemesterModel.speichern(false);
      }
    } else {
      saveSemesterResult = createOrUpdateSemesterModel.speichern(false);
    }

    switch (saveSemesterResult) {
      case SEMESTER_BEREITS_ERFASST -> {
        showMessageDialogError(createOrUpdateSemesterModel.getSemester() + " bereits erfasst.");
        btnSpeichern.setFocusPainted(false);
      }
      case SEMESTER_UEBERLAPPT_MIT_ANDEREM_SEMESTER -> {
        showMessageDialogError("Semester dürfen sich nicht überlappen.");
        btnSpeichern.setFocusPainted(false);
      }
      case SEMESTER_DURCH_ANDEREN_BENUTZER_VERAENDERT -> {
        closeDialog();
        showMessageDialogError(
            "Der Wert konnte nicht gespeichert werden, da der Eintrag unterdessen durch \n"
                + "einen anderen Benutzer verändert oder gelöscht wurde.");
      }
      case SPEICHERN_ERFOLGREICH -> {
        closeDialog();
        JOptionPane.showMessageDialog(
            createOrUpdateSemesterDialog,
            "Semester wurde erfolgreich gespeichert.",
            "Speichern erfolgreich",
            JOptionPane.INFORMATION_MESSAGE);
      }
    }
  }

  private SwingWorker<SaveSemesterResult, Void> getSpeichernSwingWorker(JDialog busyDialog) {
    SwingWorker<SaveSemesterResult, Void> worker =
        new SwingWorker<>() {
          @Override
          protected SaveSemesterResult doInBackground() {
            return createOrUpdateSemesterModel.speichern(true);
          }

          @Override
          protected void done() {
            busyDialog.dispose();
          }
        };

    // Worker muss ausgeführt werden bevor der Dialog visible wird, da mit setVisible der
    // nachfolgende Code blockiert ist
    worker.execute();
    return worker;
  }

  private static SaveSemesterResult getSaveSemesterResult(
      SwingWorker<SaveSemesterResult, Void> semesterSpeichernSwingWorker) {
    SaveSemesterResult saveSemesterResult;
    try {
      saveSemesterResult = semesterSpeichernSwingWorker.get();
    } catch (InterruptedException e) {
      LOGGER.warn("Speichern Worker Interrupted!", e);
      Thread.currentThread().interrupt();
      throw new SvmRuntimeException(e.getMessage(), e);
    } catch (ExecutionException e) {
      throw new SvmRuntimeException(e.getMessage(), e);
    }
    return saveSemesterResult;
  }

  private void showMessageDialogError(String message) {
    JOptionPane.showMessageDialog(
        createOrUpdateSemesterDialog, message, "Fehler", JOptionPane.ERROR_MESSAGE);
  }

  private static JDialog createSemesterSpeichernBusyDialog() {

    final JOptionPane optionPane =
        new JOptionPane(
            "Das Semester und die betroffenen Semesterrechnungen werden gespeichert.",
            JOptionPane.INFORMATION_MESSAGE,
            JOptionPane.DEFAULT_OPTION,
            null,
            new Object[] {},
            null);

    final JDialog dialog = new JDialog();
    dialog.setTitle("Verarbeitung läuft ...");
    dialog.setModal(true);
    dialog.setContentPane(optionPane);
    dialog.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    dialog.pack();

    return dialog;
  }

  public void setBtnAbbrechen(JButton btnAbbrechen) {
    btnAbbrechen.addActionListener(e -> onAbbrechen());
  }

  private void onAbbrechen() {
    closeDialog();
  }

  private void closeDialog() {
    createOrUpdateSemesterDialog.dispose();
    dialogClosedListener.onDialogClosed();
  }

  private void onCreateOrUpdateSemesterModelCompleted(boolean completed) {
    LOGGER.trace("CreateOrUpdateSemesterModel completed={}", completed);
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
    if (checkIsFieldChange(Field.SCHULJAHR, evt)) {
      spinnerSchuljahre.setValue(createOrUpdateSemesterModel.getSchuljahr());
    } else if (checkIsFieldChange(Field.SEMESTERBEZEICHNUNG, evt)) {
      comboBoxSemesterbezeichnung.setSelectedItem(
          createOrUpdateSemesterModel.getSemesterbezeichnung());
    } else if (checkIsFieldChange(Field.SEMESTERBEGINN, evt)) {
      txtSemesterbeginn.setText(asString(createOrUpdateSemesterModel.getSemesterbeginn()));
    } else if (checkIsFieldChange(Field.SEMESTERENDE, evt)) {
      txtSemesterende.setText(asString(createOrUpdateSemesterModel.getSemesterende()));
    } else if (checkIsFieldChange(Field.FERIENBEGINN1, evt)) {
      txtFerienbeginn1.setText(asString(createOrUpdateSemesterModel.getFerienbeginn1()));
    } else if (checkIsFieldChange(Field.FERIENENDE1, evt)) {
      txtFerienende1.setText(asString(createOrUpdateSemesterModel.getFerienende1()));
    } else if (checkIsFieldChange(Field.FERIENBEGINN2, evt)) {
      txtFerienbeginn2.setText(asString(createOrUpdateSemesterModel.getFerienbeginn2()));
    } else if (checkIsFieldChange(Field.FERIENENDE2, evt)) {
      txtFerienende2.setText(asString(createOrUpdateSemesterModel.getFerienende2()));
    }
  }

  @Override
  void validateFields() throws SvmValidationException {
    if (txtSemesterbeginn.isEnabled()) {
      LOGGER.trace("Validate field Semesterbeginn");
      setModelSemesterbeginn(true);
    }
    if (txtSemesterende.isEnabled()) {
      LOGGER.trace("Validate field Semesterende");
      setModelSemesterende(true);
    }
    if (txtFerienbeginn1.isEnabled()) {
      LOGGER.trace("Validate field Ferienbeginn1");
      setModelFerienbeginn1(true);
    }
    if (txtFerienende1.isEnabled()) {
      LOGGER.trace("Validate field Ferienende1");
      setModelFerienende1(true);
    }
    if (txtFerienbeginn2.isEnabled()) {
      LOGGER.trace("Validate field Ferienbeginn2");
      setModelFerienbeginn2(true);
    }
    if (txtFerienende2.isEnabled()) {
      LOGGER.trace("Validate field Ferienende2");
      setModelFerienende2(true);
    }
  }

  @Override
  void showErrMsg(SvmValidationException e) {
    if (e.getAffectedFields().contains(Field.SEMESTERBEGINN)) {
      errLblSemesterbeginn.setVisible(true);
      errLblSemesterbeginn.setText(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.SEMESTERENDE)) {
      errLblSemesterende.setVisible(true);
      errLblSemesterende.setText(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.FERIENBEGINN1)) {
      errLblFerienbeginn1.setVisible(true);
      errLblFerienbeginn1.setText(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.FERIENENDE1)) {
      errLblFerienende1.setVisible(true);
      errLblFerienende1.setText(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.FERIENBEGINN2)) {
      errLblFerienbeginn2.setVisible(true);
      errLblFerienbeginn2.setText(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.FERIENENDE2)) {
      errLblFerienende2.setVisible(true);
      errLblFerienende2.setText(e.getMessage());
    }
  }

  @Override
  void showErrMsgAsToolTip(SvmValidationException e) {
    if (e.getAffectedFields().contains(Field.SEMESTERBEGINN)) {
      txtSemesterbeginn.setToolTipText(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.SEMESTERENDE)) {
      txtSemesterende.setToolTipText(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.FERIENBEGINN1)) {
      txtFerienbeginn1.setToolTipText(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.FERIENENDE1)) {
      txtFerienende1.setToolTipText(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.FERIENBEGINN2)) {
      txtFerienbeginn2.setToolTipText(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.FERIENENDE2)) {
      txtFerienende2.setToolTipText(e.getMessage());
    }
  }

  @Override
  public void makeErrorLabelsInvisible(Set<Field> fields) {
    if (fields.contains(Field.ALLE) || fields.contains(Field.SEMESTERBEGINN)) {
      errLblSemesterbeginn.setVisible(false);
      txtSemesterbeginn.setToolTipText(null);
    }
    if (fields.contains(Field.ALLE) || fields.contains(Field.SEMESTERENDE)) {
      errLblSemesterende.setVisible(false);
      txtSemesterende.setToolTipText(null);
    }
    if (fields.contains(Field.ALLE) || fields.contains(Field.FERIENBEGINN1)) {
      errLblFerienbeginn1.setVisible(false);
      txtFerienbeginn1.setToolTipText(null);
    }
    if (fields.contains(Field.ALLE) || fields.contains(Field.FERIENENDE1)) {
      errLblFerienende1.setVisible(false);
      txtFerienende1.setToolTipText(null);
    }
    if (fields.contains(Field.ALLE) || fields.contains(Field.FERIENBEGINN2)) {
      errLblFerienbeginn2.setVisible(false);
      txtFerienbeginn2.setToolTipText(null);
    }
    if (fields.contains(Field.ALLE) || fields.contains(Field.FERIENENDE2)) {
      errLblFerienende2.setVisible(false);
      txtFerienende2.setToolTipText(null);
    }
  }

  @Override
  public void disableFields(boolean disable, Set<Field> fields) {
    // Keine zu deaktivierenden Felder
  }
}
