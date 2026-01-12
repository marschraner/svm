package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.datatypes.Field;
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
public class CreateOrUpdateKursortController
    extends AbstractDialogController<CreateOrUpdateKursortModel, CreateOrUpdateKursortView> {

  private static final Logger LOGGER = LogManager.getLogger(CreateOrUpdateKursortController.class);

  private final boolean isBearbeiten;

  CreateOrUpdateKursortController(
      CreateOrUpdateKursortModel createOrUpdateKursortModel, boolean isBearbeiten, String title) {
    super(createOrUpdateKursortModel, new CreateOrUpdateKursortView(title));
    this.isBearbeiten = isBearbeiten;
    configTxtBezeichnung();
    configCheckBoxSelektierbar();
    configBtnSpeichern();
    configBtnAbbrechen();
    onConstructionFinished(); // damit das Model initialisiert wird!
  }

  private ModelAndViewAccessor<String> bezeichnungModelAndViewAccessor;

  private void configTxtBezeichnung() {
    bezeichnungModelAndViewAccessor =
        new ModelAndViewAccessor<>(
            Field.BEZEICHNUNG,
            model::getBezeichnung,
            model::setBezeichnung,
            view::getTxtBezeichnungText,
            view::setTxtBezeichnungToolTipText);
    view.addTxtBezeichnungActionListener(e -> onBezeichnungEvent(true));
    view.addTxtBezeichnungFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onBezeichnungEvent(false);
          }
        });
  }

  private void onBezeichnungEvent(boolean showRequiredErrMsg) {
    LOGGER.trace("CreateOrUpdateKursortController Event Bezeichnung");
    setModelValueFromViewWithViewValueChangedCheck(
        bezeichnungModelAndViewAccessor, showRequiredErrMsg);
  }

  private void configCheckBoxSelektierbar() {
    // Selektierbar als Default-Wert
    if (!isBearbeiten) {
      model.setSelektierbar(true);
    }
    view.addCheckBoxSelektierbarItemListener(e -> onSelektierbarEvent());
  }

  private void onSelektierbarEvent() {
    LOGGER.trace(
        "AngehoerigerController Event Selektierbar. Selected={}",
        view.isCheckBoxSelektierbarSelected());
    model.setSelektierbar(view.isCheckBoxSelektierbarSelected());
  }

  private void configBtnSpeichern() {
    if (isModelValidationMode()) {
      view.setButtonSpeichernDisabled();
    }
    view.addButtonSpeichernActionListener(e -> onSpeichern());
  }

  @SuppressWarnings("DuplicatedCode")
  private void onSpeichern() {
    if (!isModelValidationMode() && !validateOnSpeichern()) {
      view.setButtonSpeichernFocusPainted(false);
      return;
    }

    SaveKursortResult saveKursortResult = model.speichern();
    switch (saveKursortResult) {
      case KURSORT_BEREITS_ERFASST -> {
        view.showErrorMessageDialog("Bezeichnung bereits in Verwendung.", "Fehler");
        view.setButtonSpeichernFocusPainted(false);
      }
      case KURSORT_DURCH_ANDEREN_BENUTZER_VERAENDERT -> {
        closeDialog();
        view.showErrorMessageDialog(
            "Der Wert konnte nicht gespeichert werden, da der Eintrag unterdessen durch \n"
                + "einen anderen Benutzer verändert oder gelöscht wurde.",
            "Fehler");
      }
      case SPEICHERN_ERFOLGREICH -> closeDialog();
    }
  }

  public void configBtnAbbrechen() {
    view.addButtonAbbrechenActionListener(e -> onAbbrechen());
  }

  private void onAbbrechen() {
    closeDialog();
  }

  @Override
  public void completed(boolean completed) {
    LOGGER.trace("Model completed={}", completed);
    if (completed) {
      view.setButtonSpeichernEnabled();
    } else {
      view.setButtonSpeichernDisabled("Bitte Eingabedaten vervollständigen");
    }
  }

  @Override
  void doPropertyChange(PropertyChangeEvent evt) {
    super.doPropertyChange(evt);
    if (checkIsFieldChange(Field.BEZEICHNUNG, evt)) {
      view.setTxtBezeichnungText(model.getBezeichnung());
    } else if (checkIsFieldChange(Field.SELEKTIERBAR, evt)) {
      view.setCheckBoxSelektierbarSelected(model.isSelektierbar());
    }
  }

  @Override
  void validateFields() throws SvmValidationException {
    if (view.isTxtBezeichnungEnabled()) {
      LOGGER.trace("Validate field Bezeichnung");
      setModelValueFromView(bezeichnungModelAndViewAccessor, true);
    }
  }

  @Override
  void showErrMsg(SvmValidationException e) {
    if (e.getAffectedFields().contains(Field.BEZEICHNUNG)) {
      view.setErrLblBezeichnungVisible(e.getMessage());
    }
  }

  @Override
  void showErrMsgAsToolTip(SvmValidationException e) {
    if (e.getAffectedFields().contains(Field.BEZEICHNUNG)) {
      view.setTxtBezeichnungToolTipText(e.getMessage());
    }
  }

  @Override
  public void makeErrorLabelsInvisible(Set<Field> fields) {
    if (fields.contains(Field.ALLE) || fields.contains(Field.BEZEICHNUNG)) {
      view.setErrLblBezeichnungInvisible();
    }
  }

  @Override
  public void disableFields(boolean disable, Set<Field> fields) {
    // Keine Felder, die inaktiviert werden müssen
  }
}
