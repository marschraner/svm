package ch.metzenthin.svm.ui.control;

import static ch.metzenthin.svm.common.utils.Converter.asString;

import ch.metzenthin.svm.common.SvmRuntimeException;
import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.common.datatypes.Semesterbezeichnung;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.CreateOrUpdateSemesterModel;
import ch.metzenthin.svm.service.result.SaveSemesterResult;
import ch.metzenthin.svm.ui.view.CreateOrUpdateSemesterView;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import javax.swing.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Martin Schraner
 */
public class CreateOrUpdateSemesterController
    extends SpeichernAbbrechenDialogController<
        CreateOrUpdateSemesterModel, CreateOrUpdateSemesterView, SaveSemesterResult> {

  private static final Logger LOGGER = LogManager.getLogger(CreateOrUpdateSemesterController.class);

  public CreateOrUpdateSemesterController(
      CreateOrUpdateSemesterModel createOrUpdateSemesterModel, boolean isBearbeiten, String title) {
    super(createOrUpdateSemesterModel, new CreateOrUpdateSemesterView(title), isBearbeiten);
    configSpinnerSchuljahre();
    configComboBoxSemesterbezeichnung();
    configTxtSemesterbeginn();
    configTxtSemesterende();
    configTxtFerienbeginn1();
    configTxtFerienende1();
    configTxtFerienbeginn2();
    configTxtFerienende2();

    onConstructionFinished();

    // Die Initialisierung ist erfolgt, jetzt können die Listener für Spinner und ComboBox
    // registriert werden.
    // Ist der Model-Validation-Mode eingeschaltet (isModelValidationMode()==true), gilt folgendes:
    // Die folgenden Listeners dürfen erst nach der Model-Initialisierung registriert werden, da
    // sonst zu früh die Validierung aufgerufen wird. Die Listener reagieren auch auf das Setzen des
    // Model-Wertes in den View-Komponenten während der Initialisierung. Dadurch würde vom Model ein
    // PropertyChange ausgelöst werden, auch dann, wenn der alte und der neue Wert identisch sind.
    // Das wiederum löst eine Validierung aus in der alle Werte von der View ins Model übertragen
    // werden. Alle View-Werte, die noch nicht initialisiert wurden, würden in die Model-Werte
    // übertragen und somit auf "null" bzw. nichts gesetzt werden.
    view.addSpinnerSchuljahreChangeListener(e -> onSchuljahrSelected());
    view.addComboBoxSemesterbezeichnungActionListener(e -> onSemesterbezeichnungSelected());

    if (!isBearbeiten) {
      initSchuljahr();
      initSemesterbezeichnung();
    }
  }

  private ModelAndViewAccessor<String> schuljarModelAndViewAccessor;

  private void configSpinnerSchuljahre() {
    schuljarModelAndViewAccessor =
        new ModelAndViewAccessor<>(
            Field.SCHULJAHR,
            model::getSchuljahr,
            model::setSchuljahr,
            view::getSpinnerSchuljahreValue,
            view::setSpinnerSchuljahreToolTipText);
  }

  private void initSchuljahr() {
    String schuljahr = model.getNaechstesNochNichtErfasstesSemester().getSchuljahr();
    try {
      model.setSchuljahr(schuljahr);
    } catch (SvmValidationException e) {
      LOGGER.error(e.getMessage());
    }
  }

  private void onSchuljahrSelected() {
    LOGGER.trace(
        "CreateOrUpdateSemesterController Event Schuljahre selected ={}",
        view.getSpinnerSchuljahreValue());
    setModelValueFromViewWithViewValueChangedCheck(schuljarModelAndViewAccessor, false);
  }

  private ModelAndViewAccessor<Semesterbezeichnung> semesterbezeichnungModelAndViewAccessor;

  private void configComboBoxSemesterbezeichnung() {
    semesterbezeichnungModelAndViewAccessor =
        new ModelAndViewAccessor<>(
            Field.SEMESTERBEZEICHNUNG,
            model::getSemesterbezeichnung,
            model::setSemesterbezeichnung,
            view::getComboBoxSemesterbezeichnungSelectedItem,
            view::setComboBoxSemesterbezeichnungToolTipText);
    view.setComboBoxSemesterbezeichnungValues(Semesterbezeichnung.values());
  }

  private void initSemesterbezeichnung() {
    Semesterbezeichnung semesterbezeichnung =
        model.getNaechstesNochNichtErfasstesSemester().getSemesterbezeichnung();
    try {
      model.setSemesterbezeichnung(semesterbezeichnung);
    } catch (SvmValidationException e) {
      LOGGER.error(e.getMessage());
    }
  }

  private void onSemesterbezeichnungSelected() {
    LOGGER.trace(
        "CreateOrUpdateSemesterController Event Semesterbezeichnung selected={}",
        view.getComboBoxSemesterbezeichnungSelectedItem());
    setModelValueFromViewWithViewValueChangedCheck(semesterbezeichnungModelAndViewAccessor, false);
  }

  private ModelAndViewAccessor<String> semesterbeginnModelAndViewAccessor;

  private void configTxtSemesterbeginn() {
    semesterbeginnModelAndViewAccessor =
        new ModelAndViewAccessor<>(
            Field.SEMESTERBEGINN,
            () -> asString(model.getSemesterbeginn()),
            model::setSemesterbeginn,
            view::getTxtSemesterbeginnText,
            view::setTxtSemesterbeginnToolTipText);
    view.addTxtSemesterbeginnActionListener(e -> onSemesterbeginnEvent(true));
    view.addTxtSemesterbeginnFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onSemesterbeginnEvent(false);
          }
        });
  }

  private void onSemesterbeginnEvent(boolean showRequiredErrMsg) {
    LOGGER.trace("CreateOrUpdateSemesterController Event Semesterbeginn");
    setModelValueFromViewWithViewValueChangedCheck(
        semesterbeginnModelAndViewAccessor, showRequiredErrMsg);
  }

  private ModelAndViewAccessor<String> semesterendeModelAndViewAccessor;

  private void configTxtSemesterende() {
    semesterendeModelAndViewAccessor =
        new ModelAndViewAccessor<>(
            Field.SEMESTERENDE,
            () -> asString(model.getSemesterende()),
            model::setSemesterende,
            view::getTxtSemesterendeText,
            view::setTxtSemesterendeToolTipText);
    view.addTxtSemesterendeActionListener(e -> onSemesterendeEvent(true));
    view.addTxtSemesterendeFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onSemesterendeEvent(false);
          }
        });
  }

  private void onSemesterendeEvent(boolean showRequiredErrMsg) {
    LOGGER.trace("CreateOrUpdateSemesterController Event Semesterende");
    setModelValueFromViewWithViewValueChangedCheck(
        semesterendeModelAndViewAccessor, showRequiredErrMsg);
  }

  private ModelAndViewAccessor<String> ferienbeginn1ModelAndViewAccessor;

  private void configTxtFerienbeginn1() {
    ferienbeginn1ModelAndViewAccessor =
        new ModelAndViewAccessor<>(
            Field.FERIENBEGINN1,
            () -> asString(model.getFerienbeginn1()),
            model::setFerienbeginn1,
            view::getTxtFerienbeginn1Text,
            view::setTxtFerienbeginn1ToolTipText);
    view.addTxtFerienbeginn1ActionListener(e -> onFerienbeginn1Event(true));
    view.addTxtFerienbeginn1FocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onFerienbeginn1Event(false);
          }
        });
  }

  private void onFerienbeginn1Event(boolean showRequiredErrMsg) {
    LOGGER.trace("CreateOrUpdateSemesterController Event Ferienbeginn1");
    setModelValueFromViewWithViewValueChangedCheck(
        ferienbeginn1ModelAndViewAccessor, showRequiredErrMsg);
  }

  private ModelAndViewAccessor<String> ferienende1ModelAndViewAccessor;

  private void configTxtFerienende1() {
    ferienende1ModelAndViewAccessor =
        new ModelAndViewAccessor<>(
            Field.FERIENENDE1,
            () -> asString(model.getFerienende1()),
            model::setFerienende1,
            view::getTxtFerienende1Text,
            view::setTxtFerienende1ToolTipText);
    view.addTxtFerienende1ActionListener(e -> onFerienende1Event(true));
    view.addTxtFerienende1FocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onFerienende1Event(false);
          }
        });
  }

  private void onFerienende1Event(boolean showRequiredErrMsg) {
    LOGGER.trace("CreateOrUpdateSemesterController Event Ferienende1");
    setModelValueFromViewWithViewValueChangedCheck(
        ferienende1ModelAndViewAccessor, showRequiredErrMsg);
  }

  private ModelAndViewAccessor<String> ferienbeginn2ModelAndViewAccessor;

  private void configTxtFerienbeginn2() {
    ferienbeginn2ModelAndViewAccessor =
        new ModelAndViewAccessor<>(
            Field.FERIENBEGINN2,
            () -> asString(model.getFerienbeginn2()),
            model::setFerienbeginn2,
            view::getTxtFerienbeginn2Text,
            view::setTxtFerienbeginn2ToolTipText);
    view.addTxtFerienbeginn2ActionListener(e -> onFerienbeginn2Event(true));
    view.addTxtFerienbeginn2FocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onFerienbeginn2Event(false);
          }
        });
  }

  private void onFerienbeginn2Event(boolean showRequiredErrMsg) {
    LOGGER.trace("CreateOrUpdateSemesterController Event Ferienbeginn2");
    setModelValueFromViewWithViewValueChangedCheck(
        ferienbeginn2ModelAndViewAccessor, showRequiredErrMsg);
  }

  private ModelAndViewAccessor<String> ferienende2ModelAndViewAccessor;

  private void configTxtFerienende2() {
    ferienende2ModelAndViewAccessor =
        new ModelAndViewAccessor<>(
            Field.FERIENENDE2,
            () -> asString(model.getFerienende2()),
            model::setFerienende2,
            view::getTxtFerienende2Text,
            view::setTxtFerienende2ToolTipText);
    view.addTxtFerienende2ActionListener(e -> onFerienende2Event(true));
    view.addTxtFerienende2FocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onFerienende2Event(false);
          }
        });
  }

  private void onFerienende2Event(boolean showRequiredErrMsg) {
    LOGGER.trace("CreateOrUpdateSemesterController Event Ferienende2");
    setModelValueFromViewWithViewValueChangedCheck(
        ferienende2ModelAndViewAccessor, showRequiredErrMsg);
  }

  @Override
  protected void speichern() {
    boolean affectsSemesterrechnungen = model.checkIfUpdateAffectsSemesterrechnungen();
    SaveSemesterResult saveSemesterResult;
    if (affectsSemesterrechnungen) {
      int n =
          view.showYesNoDialog(
              "Soll die Anzahl Semesterwochen auch bei den Semesterrechnungen geändert werden? \n"
                  + "(Semesterrechnungen mit gesetztem Rechnungsdatum werden nicht verändert.)",
              "Anzahl Semesterwochen auch bei Semesterrechnungen ändern?");
      if (n == 0) {
        JDialog semesterSpeichernBusyDialog =
            view.createBusyDialog(
                "Das Semester und die betroffenen Semesterrechnungen werden gespeichert.");
        SwingWorker<SaveSemesterResult, Void> semesterSpeichernSwingWorker =
            getSpeichernSwingWorker(semesterSpeichernBusyDialog);
        semesterSpeichernBusyDialog.setVisible(true);
        // der nachfolgende Code ist blockiert, bis der Worker beendet ist und den BusyDialog
        // schliesst
        saveSemesterResult = getSaveSemesterResult(semesterSpeichernSwingWorker);

      } else {
        saveSemesterResult = model.speichern(false);
      }
    } else {
      saveSemesterResult = model.speichern(false);
    }

    switch (saveSemesterResult) {
      case SEMESTER_BEREITS_ERFASST, SEMESTER_UEBERLAPPT_MIT_ANDEREM_SEMESTER -> {
        showMessageDialogError(saveSemesterResult.getMessage());
        view.setButtonSpeichernFocusPainted(false);
      }
      case SEMESTER_DURCH_ANDEREN_BENUTZER_VERAENDERT -> {
        closeDialog();
        showMessageDialogError(saveSemesterResult.getMessage());
      }
      case SPEICHERN_ERFOLGREICH -> {
        closeDialog();
        view.showInfoMessageDialog(saveSemesterResult.getMessage(), "Speichern erfolgreich");
      }
    }
  }

  private SwingWorker<SaveSemesterResult, Void> getSpeichernSwingWorker(JDialog busyDialog) {
    SwingWorker<SaveSemesterResult, Void> worker =
        new SwingWorker<>() {
          @Override
          protected SaveSemesterResult doInBackground() {
            return model.speichern(true);
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
    view.showErrorMessageDialog(message, "Fehler");
  }

  @Override
  void doPropertyChange(PropertyChangeEvent evt) {
    super.doPropertyChange(evt);
    if (checkIsFieldChange(Field.SCHULJAHR, evt)) {
      view.setSpinnerSchuljahreValue(model.getSchuljahr());
    } else if (checkIsFieldChange(Field.SEMESTERBEZEICHNUNG, evt)) {
      view.setComboBoxSemesterbezeichnungSelectedItem(model.getSemesterbezeichnung());
    } else if (checkIsFieldChange(Field.SEMESTERBEGINN, evt)) {
      view.setTxtSemesterbeginnText(asString(model.getSemesterbeginn()));
    } else if (checkIsFieldChange(Field.SEMESTERENDE, evt)) {
      view.setTxtSemesterendeText(asString(model.getSemesterende()));
    } else if (checkIsFieldChange(Field.FERIENBEGINN1, evt)) {
      view.setTxtFerienbeginn1Text(asString(model.getFerienbeginn1()));
    } else if (checkIsFieldChange(Field.FERIENENDE1, evt)) {
      view.setTxtFerienende1Text(asString(model.getFerienende1()));
    } else if (checkIsFieldChange(Field.FERIENBEGINN2, evt)) {
      view.setTxtFerienbeginn2Text(asString(model.getFerienbeginn2()));
    } else if (checkIsFieldChange(Field.FERIENENDE2, evt)) {
      view.setTxtFerienende2Text(asString(model.getFerienende2()));
    }
  }

  @Override
  void validateFields() throws SvmValidationException {
    if (view.isTxtSemesterbeginnEnabled()) {
      LOGGER.trace("Validate field Semesterbeginn");
      setModelValueFromView(semesterbeginnModelAndViewAccessor, true);
    }
    if (view.isTxtSemesterendeEnabled()) {
      LOGGER.trace("Validate field Semesterende");
      setModelValueFromView(semesterendeModelAndViewAccessor, true);
    }
    if (view.isTxtFerienbeginn1Enabled()) {
      LOGGER.trace("Validate field Ferienbeginn1");
      setModelValueFromView(ferienbeginn1ModelAndViewAccessor, true);
    }
    if (view.isTxtFerienende1Enabled()) {
      LOGGER.trace("Validate field Ferienende1");
      setModelValueFromView(ferienende1ModelAndViewAccessor, true);
    }
    if (view.isTxtFerienbeginn2Enabled()) {
      LOGGER.trace("Validate field Ferienbeginn2");
      setModelValueFromView(ferienbeginn2ModelAndViewAccessor, true);
    }
    if (view.isTxtFerienende2Enabled()) {
      LOGGER.trace("Validate field Ferienende2");
      setModelValueFromView(ferienende2ModelAndViewAccessor, true);
    }
  }

  @Override
  void showErrMsg(SvmValidationException e) {
    if (e.getAffectedFields().contains(Field.SEMESTERBEGINN)) {
      view.setErrLblSemesterbeginnVisible(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.SEMESTERENDE)) {
      view.setErrLblSemesterendeVisible(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.FERIENBEGINN1)) {
      view.setErrLblFerienbeginn1Visible(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.FERIENENDE1)) {
      view.setErrLblFerienende1Visible(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.FERIENBEGINN2)) {
      view.setErrLblFerienbeginn2Visible(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.FERIENENDE2)) {
      view.setErrLblFerienende2Visible(e.getMessage());
    }
  }

  @Override
  void showErrMsgAsToolTip(SvmValidationException e) {
    if (e.getAffectedFields().contains(Field.SEMESTERBEGINN)) {
      view.setTxtSemesterbeginnToolTipText(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.SEMESTERENDE)) {
      view.setTxtSemesterendeToolTipText(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.FERIENBEGINN1)) {
      view.setTxtFerienbeginn1ToolTipText(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.FERIENENDE1)) {
      view.setTxtFerienende1ToolTipText(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.FERIENBEGINN2)) {
      view.setTxtFerienbeginn2ToolTipText(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.FERIENENDE2)) {
      view.setTxtFerienende2ToolTipText(e.getMessage());
    }
  }

  @Override
  public void makeErrorLabelsInvisible(Set<Field> fields) {
    if (fields.contains(Field.ALLE) || fields.contains(Field.SEMESTERBEGINN)) {
      view.setErrLblSemesterbeginnInvisible();
    }
    if (fields.contains(Field.ALLE) || fields.contains(Field.SEMESTERENDE)) {
      view.setErrLblSemesterendeInvisible();
    }
    if (fields.contains(Field.ALLE) || fields.contains(Field.FERIENBEGINN1)) {
      view.setErrLblFerienbeginn1Invisible();
    }
    if (fields.contains(Field.ALLE) || fields.contains(Field.FERIENENDE1)) {
      view.setErrLblFerienende1Invisible();
    }
    if (fields.contains(Field.ALLE) || fields.contains(Field.FERIENBEGINN2)) {
      view.setErrLblFerienbeginn2Invisible();
    }
    if (fields.contains(Field.ALLE) || fields.contains(Field.FERIENENDE2)) {
      view.setErrLblFerienende2Invisible();
    }
  }

  @Override
  public void disableFields(boolean disable, Set<Field> fields) {
    // Keine Felder, die inaktiviert werden müssen
  }
}
