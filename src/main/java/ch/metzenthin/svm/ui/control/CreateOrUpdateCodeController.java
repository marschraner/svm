package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.domain.model.CreateOrUpdateCodeModel;
import ch.metzenthin.svm.domain.model.entityfields.CodeFields;
import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResultsAndSubmitResult;
import ch.metzenthin.svm.ui.view.CreateOrUpdateCodeView;
import java.awt.event.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Martin Schraner
 */
public class CreateOrUpdateCodeController
    extends AbstractSubmitDialogController<CreateOrUpdateCodeView> {

  private static final Logger LOGGER = LoggerFactory.getLogger(CreateOrUpdateCodeController.class);

  private final CreateOrUpdateCodeModel model;

  public CreateOrUpdateCodeController(
      CreateOrUpdateCodeModel createOrUpdateCodeModel, String title) {
    super(createView(title));
    model = createOrUpdateCodeModel;
    configTxtKuerzel();
    configTxtBeschreibung();
    initialiseViewFields();
  }

  private static CreateOrUpdateCodeView createView(String title) {
    return new CreateOrUpdateCodeView(title);
  }

  private void configTxtKuerzel() {
    view.addTxtKuerzelActionListener(e -> onKuerzelEvent());
    view.addTxtKuerzelFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onKuerzelEvent();
          }
        });
  }

  private void onKuerzelEvent() {
    LOGGER.trace("CreateOrUpdateCodeController Event Kuerzel");
    formatAndValidateString(
        view.getTxtKuerzelText(),
        model::validateKuerzel,
        view::setTxtKuerzelText,
        view::setErrorLabelKuerzelVisible,
        view::setErrorLabelKuerzelInvisible);
  }

  private void configTxtBeschreibung() {
    view.addTxtBeschreibungActionListener(e -> onBeschreibungEvent());
    view.addTxtBeschreibungFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onBeschreibungEvent();
          }
        });
  }

  private void onBeschreibungEvent() {
    LOGGER.trace("CreateOrUpdateCodeController Event Beschreibung");
    formatAndValidateString(
        view.getTxtBeschreibungText(),
        model::validateBeschreibung,
        view::setTxtBeschreibungText,
        view::setErrorLabelBeschreibungVisible,
        view::setErrorLabelBeschreibungInvisible);
  }

  private void initialiseViewFields() {
    if (!model.isNeu()) {
      CodeFields codeFields = model.getCodeFields();
      view.setTxtKuerzelText(codeFields.kuerzel());
      view.setTxtBeschreibungText(codeFields.beschreibung());
      view.setCheckBoxSelektierbarSelected(codeFields.selektierbar());
    } else {
      view.setCheckBoxSelektierbarSelected(true);
    }
  }

  @Override
  protected ValidationResultsAndSubmitResult submit() {
    CodeFields codeFields =
        new CodeFields(
            view.getTxtKuerzelText(),
            view.getTxtBeschreibungText(),
            view.isCheckBoxSelektierbarSelected());
    return model.speichern(codeFields);
  }

  @Override
  protected void setErrorLabelVisible(ValidationResult validationResult, Field field) {
    switch (field) {
      case KUERZEL ->
          setErrorLabelVisibleIfRequired(
              validationResult, field, view::setErrorLabelKuerzelVisible);
      case BESCHREIBUNG ->
          setErrorLabelVisibleIfRequired(
              validationResult, field, view::setErrorLabelBeschreibungVisible);
      default -> throw new IllegalStateException("Unexpected value: " + field);
    }
  }

  @Override
  public void setAllErrorLabelsInvisible() {
    view.setErrorLabelKuerzelInvisible();
    view.setErrorLabelBeschreibungInvisible();
  }
}
