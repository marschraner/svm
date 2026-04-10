package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.CreateOrUpdateCodeModel;
import ch.metzenthin.svm.service.result.SaveCodeResult;
import ch.metzenthin.svm.ui.view.CreateOrUpdateCodeView;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Martin Schraner
 */
public class CreateOrUpdateCodeController
    extends SpeichernAbbrechenDialogController<
        CreateOrUpdateCodeModel, CreateOrUpdateCodeView, SaveCodeResult> {

  private static final Logger LOGGER = LogManager.getLogger(CreateOrUpdateCodeController.class);

  public CreateOrUpdateCodeController(
      CreateOrUpdateCodeModel createOrUpdateCodeModel, boolean isBearbeiten, String title) {
    super(createOrUpdateCodeModel, new CreateOrUpdateCodeView(title), isBearbeiten);
    configTxtKuerzel();
    configTxtBeschreibung();
    configCheckBoxSelektierbar();
  }

  private ModelAndViewAccessor<String> kuerzelModelAndViewAccessor;

  private void configTxtKuerzel() {
    kuerzelModelAndViewAccessor =
        new ModelAndViewAccessor<>(
            Field.KUERZEL,
            model::getKuerzel,
            model::setKuerzel,
            view::getTxtKuerzelText,
            view::setTxtKuerzelToolTipText);
    view.addTxtKuerzelActionListener(e -> onKuerzelEvent(true));
    view.addTxtKuerzelFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onKuerzelEvent(false);
          }
        });
  }

  private void onKuerzelEvent(boolean showRequiredErrMsg) {
    LOGGER.trace("CreateOrUpdateCodeController Event Kuerzel");
    setModelValueFromViewWithViewValueChangedCheck(kuerzelModelAndViewAccessor, showRequiredErrMsg);
  }

  private ModelAndViewAccessor<String> beschreibungModelAndViewAccessor;

  private void configTxtBeschreibung() {
    beschreibungModelAndViewAccessor =
        new ModelAndViewAccessor<>(
            Field.BESCHREIBUNG,
            model::getBeschreibung,
            model::setBeschreibung,
            view::getTxtBeschreibungText,
            view::setTxtBeschreibungToolTipText);
    view.addTxtBeschreibungActionListener(e -> onBeschreibungEvent(true));
    view.addTxtBeschreibungFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onBeschreibungEvent(false);
          }
        });
  }

  private void onBeschreibungEvent(boolean showRequiredErrMsg) {
    LOGGER.trace("CreateOrUpdateCodeController Event Beschreibung");
    setModelValueFromViewWithViewValueChangedCheck(
        beschreibungModelAndViewAccessor, showRequiredErrMsg);
  }

  private void configCheckBoxSelektierbar() {
    view.addCheckBoxSelektierbarItemListener(e -> onSelektierbarEvent());
  }

  private void onSelektierbarEvent() {
    LOGGER.trace(
        "CreateOrUpdateCodeController Event Selektierbar. Selected={}",
        view.isCheckBoxSelektierbarSelected());
    model.setSelektierbar(view.isCheckBoxSelektierbarSelected());
  }

  @Override
  void doPropertyChange(PropertyChangeEvent evt) {
    super.doPropertyChange(evt);
    if (checkIsFieldChange(Field.KUERZEL, evt)) {
      view.setTxtKuerzelText(model.getKuerzel());
    } else if (checkIsFieldChange(Field.BESCHREIBUNG, evt)) {
      view.setTxtBeschreibungText(model.getBeschreibung());
    } else if (checkIsFieldChange(Field.SELEKTIERBAR, evt)) {
      view.setCheckBoxSelektierbarSelected(model.isSelektierbar());
    }
  }

  @Override
  void validateFields() throws SvmValidationException {
    if (view.isTxtKuerzelEnabled()) {
      LOGGER.trace("Validate field Kuerzel");
      setModelValueFromView(kuerzelModelAndViewAccessor, true);
    }
    if (view.isTxtBeschreibungEnabled()) {
      LOGGER.trace("Validate field Beschreibung");
      setModelValueFromView(beschreibungModelAndViewAccessor, true);
    }
  }

  @Override
  void showErrMsg(SvmValidationException e) {
    if (e.getAffectedFields().contains(Field.KUERZEL)) {
      view.setErrLblKuerzelVisible(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.BESCHREIBUNG)) {
      view.setErrLblBeschreibungVisible(e.getMessage());
    }
  }

  @Override
  void showErrMsgAsToolTip(SvmValidationException e) {
    if (e.getAffectedFields().contains(Field.KUERZEL)) {
      view.setTxtKuerzelToolTipText(e.getMessage());
    }
    if (e.getAffectedFields().contains(Field.BESCHREIBUNG)) {
      view.setTxtBeschreibungToolTipText(e.getMessage());
    }
  }

  @Override
  public void makeErrorLabelsInvisible(Set<Field> fields) {
    if (fields.contains(Field.ALLE) || fields.contains(Field.KUERZEL)) {
      view.setErrLblKuerzelInvisible();
    }
    if (fields.contains(Field.ALLE) || fields.contains(Field.BESCHREIBUNG)) {
      view.setErrLblBeschreibungInvisible();
    }
  }

  @Override
  public void disableFields(boolean disable, Set<Field> fields) {
    // Keine Felder, die inaktiviert werden müssen
  }
}
