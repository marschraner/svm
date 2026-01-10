package ch.metzenthin.svm.ui.control;

import static ch.metzenthin.svm.common.utils.SimpleValidator.equalsNullSafe;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.CreateOrUpdateKursortModel;
import ch.metzenthin.svm.service.result.SaveKursortResult;
import ch.metzenthin.svm.ui.components.CreateOrUpdateKursortView;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Martin Schraner
 */
public class CreateOrUpdateKursortController extends AbstractController {

  private static final Logger LOGGER = LogManager.getLogger(CreateOrUpdateKursortController.class);

  // Möglichkeit zum Umschalten des validation modes (nicht dynamisch)
  private static final boolean MODEL_VALIDATION_MODE = false;

  private final CreateOrUpdateKursortView createOrUpdateKursortView;
  private final CreateOrUpdateKursortModel createOrUpdateKursortModel;
  private final boolean isBearbeiten;

  public CreateOrUpdateKursortController(
      CreateOrUpdateKursortModel createOrUpdateKursortModel, boolean isBearbeiten, String title) {
    super(createOrUpdateKursortModel);
    this.createOrUpdateKursortView = new CreateOrUpdateKursortView(title, e -> onAbbrechen());
    this.createOrUpdateKursortModel = createOrUpdateKursortModel;
    this.isBearbeiten = isBearbeiten;
    this.createOrUpdateKursortModel.addPropertyChangeListener(this);
    this.createOrUpdateKursortModel.addDisableFieldsListener(this);
    this.createOrUpdateKursortModel.addMakeErrorLabelsInvisibleListener(this);
    this.createOrUpdateKursortModel.addCompletedListener(
        this::onCreateOrUpdateKursortModelCompleted);
    this.setModelValidationMode(MODEL_VALIDATION_MODE);
    configView();
    configBtnSpeichern();
    configBtnAbbrechen();
    configTxtBezeichnung();
    configCheckBoxSelektierbar();
    constructionDone();
  }

  private void constructionDone() {
    createOrUpdateKursortModel.initializeCompleted();
  }

  public void showDialog() {
    createOrUpdateKursortView.showDialog();
  }

  private void configView() {
    // call onAbbrechen() when cross is clicked
    createOrUpdateKursortView.addWindowListener(
        new WindowAdapter() {
          @Override
          public void windowClosing(WindowEvent e) {
            onAbbrechen();
          }
        });
  }

  private void configTxtBezeichnung() {
    createOrUpdateKursortView.addTxtBezeichnungActionListener(e -> onBezeichnungEvent(true));
    createOrUpdateKursortView.addTxtBezeichnungFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onBezeichnungEvent(false);
          }
        });
  }

  private void onBezeichnungEvent(boolean showRequiredErrMsg) {
    LOGGER.trace("CreateOrUpdateKursortController Event Bezeichnung");
    boolean equalFieldAndModelValue =
        equalsNullSafe(
            createOrUpdateKursortView.getTxtBezeichnungText(),
            createOrUpdateKursortModel.getBezeichnung());
    try {
      setModelBezeichnung(showRequiredErrMsg);
    } catch (SvmValidationException e) {
      return;
    }
    if (equalFieldAndModelValue && isModelValidationMode()) {
      // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb
      // muss hier die Validierung angestossen werden.
      // Szenario (mit modelValidationMode = true!):
      // - Kursort bearbeiten (z.B. Saal B)
      // - Fehler provozieren (z.B. Bezeichnung statt "Saal B" nur "S". Es gibt einen Fehler wegen
      // min. Länge 2)
      // → der Speichern-Button ist disabled
      // - Fehler entfernen (Wert zurücksetzen: wieder "Saal B" eingeben)
      // → Ohne dieses if-Statement bleibt der Speichern-Button disabled!
      LOGGER.trace("Validierung wegen equalFieldAndModelValue");
      validate();
    }
  }

  private void setModelBezeichnung(boolean showRequiredErrMsg) throws SvmValidationException {
    makeErrorLabelInvisible(Field.BEZEICHNUNG);
    try {
      createOrUpdateKursortModel.setBezeichnung(createOrUpdateKursortView.getTxtBezeichnungText());
    } catch (SvmRequiredException e) {
      LOGGER.trace(
          "CreateOrUpdateKursortController setModelBezeichnung RequiredException={}",
          e.getMessage());
      if (isModelValidationMode() || !showRequiredErrMsg) {
        createOrUpdateKursortView.setTxtBezeichnungToolTipText(e.getMessage());
        // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut, nachdem alle Field-Prüfungen
        // bestanden sind.
      } else {
        showErrMsg(e);
      }
      throw e;
    } catch (SvmValidationException e) {
      LOGGER.trace(
          "CreateOrUpdateKursortController setModelBezeichnung Exception={}", e.getMessage());
      showErrMsg(e);
      throw e;
    }
  }

  private void configCheckBoxSelektierbar() {
    // Selektierbar als Default-Wert
    if (!isBearbeiten) {
      createOrUpdateKursortModel.setSelektierbar(true);
    }
    createOrUpdateKursortView.addCheckBoxSelektierbarItemListener(e -> onSelektierbarEvent());
  }

  private void onSelektierbarEvent() {
    LOGGER.trace(
        "AngehoerigerController Event Selektierbar. Selected={}",
        createOrUpdateKursortView.isCheckBoxSelektierbarSelected());
    createOrUpdateKursortModel.setSelektierbar(
        createOrUpdateKursortView.isCheckBoxSelektierbarSelected());
  }

  private void configBtnSpeichern() {
    if (isModelValidationMode()) {
      createOrUpdateKursortView.setButtonSpeichernEnabled(false);
    }
    createOrUpdateKursortView.addButtonSpeichernActionListener(e -> onSpeichern());
  }

  @SuppressWarnings("DuplicatedCode")
  private void onSpeichern() {
    if (!isModelValidationMode() && !validateOnSpeichern()) {
      createOrUpdateKursortView.setButtonSpeichernFocusPainted(false);
      return;
    }

    SaveKursortResult saveKursortResult = createOrUpdateKursortModel.speichern();
    switch (saveKursortResult) {
      case KURSORT_BEREITS_ERFASST -> {
        createOrUpdateKursortView.showErrorMessageDialog(
            "Bezeichnung bereits in Verwendung.", "Fehler");
        createOrUpdateKursortView.setButtonSpeichernFocusPainted(false);
      }
      case KURSORT_DURCH_ANDEREN_BENUTZER_VERAENDERT -> {
        closeDialog();
        createOrUpdateKursortView.showErrorMessageDialog(
            "Der Wert konnte nicht gespeichert werden, da der Eintrag unterdessen durch \n"
                + "einen anderen Benutzer verändert oder gelöscht wurde.",
            "Fehler");
      }
      case SPEICHERN_ERFOLGREICH -> closeDialog();
    }
  }

  public void configBtnAbbrechen() {
    createOrUpdateKursortView.addButtonAbbrechenActionListener(e -> onAbbrechen());
  }

  private void onAbbrechen() {
    closeDialog();
  }

  private void closeDialog() {
    createOrUpdateKursortView.dispose();
  }

  private void onCreateOrUpdateKursortModelCompleted(boolean completed) {
    LOGGER.trace("CreateOrUpdateKursortModel completed={}", completed);
    if (completed) {
      createOrUpdateKursortView.setButtonSpeichernToolTipText(null);
      createOrUpdateKursortView.setButtonSpeichernEnabled(true);
    } else {
      createOrUpdateKursortView.setButtonSpeichernToolTipText(
          "Bitte Eingabedaten vervollständigen");
      createOrUpdateKursortView.setButtonSpeichernEnabled(false);
    }
  }

  @Override
  void doPropertyChange(PropertyChangeEvent evt) {
    super.doPropertyChange(evt);
    if (checkIsFieldChange(Field.BEZEICHNUNG, evt)) {
      createOrUpdateKursortView.setTxtBezeichnungText(createOrUpdateKursortModel.getBezeichnung());
    } else if (checkIsFieldChange(Field.SELEKTIERBAR, evt)) {
      createOrUpdateKursortView.setCheckBoxSelektierbarSelected(
          createOrUpdateKursortModel.isSelektierbar());
    }
  }

  @Override
  void validateFields() throws SvmValidationException {
    if (createOrUpdateKursortView.isTxtBezeichnungEnabled()) {
      LOGGER.trace("Validate field Bezeichnung");
      setModelBezeichnung(true);
    }
  }

  @Override
  void showErrMsg(SvmValidationException e) {
    if (e.getAffectedFields().contains(Field.BEZEICHNUNG)) {
      createOrUpdateKursortView.setErrLblBezeichnungVisible(true);
      createOrUpdateKursortView.setErrLblBezeichnungText(e.getMessage());
    }
  }

  @Override
  void showErrMsgAsToolTip(SvmValidationException e) {
    if (e.getAffectedFields().contains(Field.BEZEICHNUNG)) {
      createOrUpdateKursortView.setTxtBezeichnungToolTipText(e.getMessage());
    }
  }

  @Override
  public void makeErrorLabelsInvisible(Set<Field> fields) {
    if (fields.contains(Field.ALLE) || fields.contains(Field.BEZEICHNUNG)) {
      createOrUpdateKursortView.setErrLblBezeichnungVisible(false);
      createOrUpdateKursortView.setTxtBezeichnungToolTipText(null);
    }
  }

  @Override
  public void disableFields(boolean disable, Set<Field> fields) {
    // Keine zu validierenden Felder
  }
}
