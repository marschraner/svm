package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.domain.model.CreateOrUpdateDispensationModel;
import ch.metzenthin.svm.domain.model.entityfields.DispensationFields;
import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResultsAndSubmitResult;
import ch.metzenthin.svm.ui.view.CreateOrUpdateDispensationView;
import java.awt.event.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Martin Schraner
 */
public class CreateOrUpdateDispensationController
    extends AbstractSubmitDialogController<CreateOrUpdateDispensationView> {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(CreateOrUpdateDispensationController.class);

  private final CreateOrUpdateDispensationModel model;

  public CreateOrUpdateDispensationController(
      CreateOrUpdateDispensationModel createOrUpdateDispensationModel, String title) {
    super(createView(title));
    model = createOrUpdateDispensationModel;
    configTxtDispensationbeginn();
    configTxtDispensationsende();
    configTxtVoraussichtlicheDauer();
    configTxtGrund();
    initialiseViewFields();
  }

  private static CreateOrUpdateDispensationView createView(String title) {
    return new CreateOrUpdateDispensationView(title);
  }

  private void configTxtDispensationbeginn() {
    view.addTxtDispensationsbeginnActionListener(e -> onDispensationsbeginnEvent());
    view.addTxtDispensationsbeginnFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onDispensationsbeginnEvent();
          }
        });
  }

  private void onDispensationsbeginnEvent() {
    LOGGER.trace("CreateOrUpdateDispensationController Event Dispensationsbeginn");
    formatConvertAndValidateDate(
        view.getTxtDispensationsbeginnText(),
        model::validateDispensationsbeginn,
        view::setTxtDispensationsbeginnText,
        view::setErrorLabelDispensationsbeginnVisible,
        view::setErrorLabelDispensationsbeginnInvisible);
  }

  private void configTxtDispensationsende() {
    view.addTxtDispensationsendeActionListener(e -> onDispensationsendeEvent());
    view.addTxtDispensationsendeFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onDispensationsendeEvent();
          }
        });
  }

  private void onDispensationsendeEvent() {
    LOGGER.trace("CreateOrUpdateDispensationController Event Dispensationende");
    formatConvertAndValidateDate(
        view.getTxtDispensationsendeText(),
        model::validateDispensationsende,
        view::setTxtDispensationsendeText,
        view::setErrorLabelDispensationsendeVisible,
        view::setErrorLabelDispensationsendeInvisible);
  }

  private void configTxtVoraussichtlicheDauer() {
    view.addTxtVoraussichtlicheDauerActionListener(e -> onVoraussichtlicheDauerEvent());
    view.addTxtVoraussichtlicheDauerFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onVoraussichtlicheDauerEvent();
          }
        });
  }

  private void onVoraussichtlicheDauerEvent() {
    LOGGER.trace("CreateOrUpdateDispensationController Event VoraussichtlicheDauer");
    formatAndValidateString(
        view.getTxtVoraussichtlicheDauerText(),
        model::validateVoraussichtlicheDauer,
        view::setTxtVoraussichtlicheDauerText,
        view::setErrorLabelVoraussichtlicheDauerVisible,
        view::setErrorLabelVoraussichtlicheDauerInvisible);
  }

  private void configTxtGrund() {
    view.addTxtGrundActionListener(e -> onGrund());
    view.addTxtGrundFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onGrund();
          }
        });
  }

  private void onGrund() {
    LOGGER.trace("CreateOrUpdateDispensationController Event Grund");
    formatAndValidateString(
        view.getTxtGrundText(),
        model::validateGrund,
        view::setTxtGrundText,
        view::setErrorLabelGrundVisible,
        view::setErrorLabelGrundInvisible);
  }

  private void initialiseViewFields() {
    if (!model.isNeu()) {
      DispensationFields dispensationFields = model.getDispensationFields();
      view.setTxtDispensationsbeginnText(dispensationFields.dispensationsbeginn());
      view.setTxtDispensationsendeText(dispensationFields.dispensationsende());
      view.setTxtVoraussichtlicheDauerText(dispensationFields.voraussichtlicheDauer());
      view.setTxtGrundText(dispensationFields.grund());
    }
  }

  @Override
  protected ValidationResultsAndSubmitResult submit() {
    return model.speichern(createDispensationFieldsFromView());
  }

  private DispensationFields createDispensationFieldsFromView() {
    return new DispensationFields(
        view.getTxtDispensationsbeginnText(),
        view.getTxtDispensationsendeText(),
        view.getTxtVoraussichtlicheDauerText(),
        view.getTxtGrundText());
  }

  @Override
  protected void setErrorLabelVisible(ValidationResult validationResult, Field field) {
    switch (field) {
      case DISPENSATIONSBEGINN ->
          setErrorLabelVisibleIfRequired(
              validationResult, field, view::setErrorLabelDispensationsbeginnVisible);
      case DISPENSATIONSENDE ->
          setErrorLabelVisibleIfRequired(
              validationResult, field, view::setErrorLabelDispensationsendeVisible);
      case VORAUSSICHTLICHE_DAUER ->
          setErrorLabelVisibleIfRequired(
              validationResult, field, view::setErrorLabelVoraussichtlicheDauerVisible);
      case GRUND ->
          setErrorLabelVisibleIfRequired(validationResult, field, view::setErrorLabelGrundVisible);
      default -> throw new IllegalStateException("Unexpected value: " + field);
    }
  }

  @Override
  protected void setAllErrorLabelsInvisible() {
    view.setErrorLabelDispensationsbeginnInvisible();
    view.setErrorLabelDispensationsendeInvisible();
    view.setErrorLabelVoraussichtlicheDauerInvisible();
    view.setErrorLabelGrundInvisible();
  }
}
