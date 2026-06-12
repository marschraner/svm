package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.domain.model.CreateOrUpdateLektionsgebuehrenModel;
import ch.metzenthin.svm.domain.model.entityfields.LektionsgebuehrenFields;
import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResultsAndSaveResult;
import ch.metzenthin.svm.ui.view.CreateOrUpdateLektionsgebuehrenView;
import java.awt.event.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Martin Schraner
 */
public class CreateOrUpdateLektionsgebuehrenController
    extends AbstractCreateOrUpdateController<CreateOrUpdateLektionsgebuehrenView> {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(CreateOrUpdateLektionsgebuehrenController.class);

  private final CreateOrUpdateLektionsgebuehrenModel model;

  public CreateOrUpdateLektionsgebuehrenController(
      CreateOrUpdateLektionsgebuehrenModel createOrUpdateLektionsgebuehrenModel, String title) {
    super(createView(title));
    model = createOrUpdateLektionsgebuehrenModel;
    configTxtLektionslaenge();
    configTxtBetrag1Kind();
    configTxtBetrag2Kinder();
    configTxtBetrag3Kinder();
    configTxtBetrag4Kinder();
    configTxtBetrag5Kinder();
    configTxtBetrag6Kinder();
    initialiseViewFields();
  }

  private static CreateOrUpdateLektionsgebuehrenView createView(String title) {
    return new CreateOrUpdateLektionsgebuehrenView(title);
  }

  private void configTxtLektionslaenge() {
    view.addTxtLektionslaengeActionListener(e -> onLektionslaengeEvent());
    view.addTxtLektionslaengeFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onLektionslaengeEvent();
          }
        });
  }

  private void onLektionslaengeEvent() {
    LOGGER.trace("CreateOrUpdateLektionsgebuehrenController Event Lektionslaenge");
    formatConvertAndValidateInt(
        view.getTxtLektionslaengeText(),
        model::validateLektionslaenge,
        view::setTxtLektionslaengeText,
        view::setErrorLabelLektionslaengeVisible,
        view::setErrorLabelLektionslaengeInvisible);
  }

  private void configTxtBetrag1Kind() {
    view.addTxtBetrag1KindActionListener(e -> onBetrag1KindEvent());
    view.addTxtBetrag1KindFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onBetrag1KindEvent();
          }
        });
  }

  private void onBetrag1KindEvent() {
    LOGGER.trace("CreateOrUpdateLektionsgebuehrenController Event Betrag1Kind");
    formatConvertAndValidatePrice(
        view.getTxtBetrag1KindText(),
        model::validateBetrag1Kind,
        view::setTxtBetrag1KindText,
        view::setErrorLabelBetrag1KindVisible,
        view::setErrorLabelBetrag1KindInvisible);
  }

  private void configTxtBetrag2Kinder() {
    view.addTxtBetrag2KinderActionListener(e -> onBetrag2KinderEvent());
    view.addTxtBetrag2KinderFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onBetrag2KinderEvent();
          }
        });
  }

  private void onBetrag2KinderEvent() {
    LOGGER.trace("CreateOrUpdateLektionsgebuehrenController Event Betrag2Kinder");
    formatConvertAndValidatePrice(
        view.getTxtBetrag2KinderText(),
        model::validateBetrag2Kinder,
        view::setTxtBetrag2KinderText,
        view::setErrorLabelBetrag2KinderVisible,
        view::setErrorLabelBetrag2KinderInvisible);
  }

  private void configTxtBetrag3Kinder() {
    view.addTxtBetrag3KinderActionListener(e -> onBetrag3KinderEvent());
    view.addTxtBetrag3KinderFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onBetrag3KinderEvent();
          }
        });
  }

  private void onBetrag3KinderEvent() {
    LOGGER.trace("CreateOrUpdateLektionsgebuehrenController Event Betrag3Kinder");
    formatConvertAndValidatePrice(
        view.getTxtBetrag3KinderText(),
        model::validateBetrag3Kinder,
        view::setTxtBetrag3KinderText,
        view::setErrorLabelBetrag3KinderVisible,
        view::setErrorLabelBetrag3KinderInvisible);
  }

  private void configTxtBetrag4Kinder() {
    view.addTxtBetrag4KinderActionListener(e -> onBetrag4KinderEvent());
    view.addTxtBetrag4KinderFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onBetrag4KinderEvent();
          }
        });
  }

  private void onBetrag4KinderEvent() {
    LOGGER.trace("CreateOrUpdateLektionsgebuehrenController Event Betrag4Kinder");
    formatConvertAndValidatePrice(
        view.getTxtBetrag4KinderText(),
        model::validateBetrag4Kinder,
        view::setTxtBetrag4KinderText,
        view::setErrorLabelBetrag4KinderVisible,
        view::setErrorLabelBetrag4KinderInvisible);
  }

  private void configTxtBetrag5Kinder() {
    view.addTxtBetrag5KinderActionListener(e -> onBetrag5KinderEvent());
    view.addTxtBetrag5KinderFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onBetrag5KinderEvent();
          }
        });
  }

  private void onBetrag5KinderEvent() {
    LOGGER.trace("CreateOrUpdateLektionsgebuehrenController Event Betrag5Kinder");
    formatConvertAndValidatePrice(
        view.getTxtBetrag5KinderText(),
        model::validateBetrag5Kinder,
        view::setTxtBetrag5KinderText,
        view::setErrorLabelBetrag5KinderVisible,
        view::setErrorLabelBetrag5KinderInvisible);
  }

  private void configTxtBetrag6Kinder() {
    view.addTxtBetrag6KinderActionListener(e -> onBetrag6KinderEvent());
    view.addTxtBetrag6KinderFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onBetrag6KinderEvent();
          }
        });
  }

  private void onBetrag6KinderEvent() {
    LOGGER.trace("CreateOrUpdateLektionsgebuehrenController Event Betrag6Kinder");
    formatConvertAndValidatePrice(
        view.getTxtBetrag6KinderText(),
        model::validateBetrag6Kinder,
        view::setTxtBetrag6KinderText,
        view::setErrorLabelBetrag6KinderVisible,
        view::setErrorLabelBetrag6KinderInvisible);
  }

  private void initialiseViewFields() {
    LektionsgebuehrenFields lektionsgebuehrenFields = model.getLektionsgebuehrenFields();
    view.setTxtLektionslaengeText(lektionsgebuehrenFields.lektionslaenge());
    view.setTxtBetrag1KindText(lektionsgebuehrenFields.betrag1Kind());
    view.setTxtBetrag2KinderText(lektionsgebuehrenFields.betrag2Kinder());
    view.setTxtBetrag3KinderText(lektionsgebuehrenFields.betrag3Kinder());
    view.setTxtBetrag4KinderText(lektionsgebuehrenFields.betrag4Kinder());
    view.setTxtBetrag5KinderText(lektionsgebuehrenFields.betrag5Kinder());
    view.setTxtBetrag6KinderText(lektionsgebuehrenFields.betrag6Kinder());
  }

  @Override
  protected ValidationResultsAndSaveResult speichern() {
    LektionsgebuehrenFields lektionsgebuehrenFields =
        new LektionsgebuehrenFields(
            view.getTxtLektionslaengeText(),
            view.getTxtBetrag1KindText(),
            view.getTxtBetrag2KinderText(),
            view.getTxtBetrag3KinderText(),
            view.getTxtBetrag4KinderText(),
            view.getTxtBetrag5KinderText(),
            view.getTxtBetrag6KinderText());
    return model.speichern(lektionsgebuehrenFields);
  }

  @Override
  protected void setErrorLabelVisible(ValidationResult validationResult, Field field) {
    switch (field) {
      case LEKTIONSLAENGE ->
          setErrorLabelVisibleIfRequired(
              validationResult, field, view::setErrorLabelLektionslaengeVisible);
      case BETRAG_1_KIND ->
          setErrorLabelVisibleIfRequired(
              validationResult, field, view::setErrorLabelBetrag1KindVisible);
      case BETRAG_2_KINDER ->
          setErrorLabelVisibleIfRequired(
              validationResult, field, view::setErrorLabelBetrag2KinderVisible);
      case BETRAG_3_KINDER ->
          setErrorLabelVisibleIfRequired(
              validationResult, field, view::setErrorLabelBetrag3KinderVisible);
      case BETRAG_4_KINDER ->
          setErrorLabelVisibleIfRequired(
              validationResult, field, view::setErrorLabelBetrag4KinderVisible);
      case BETRAG_5_KINDER ->
          setErrorLabelVisibleIfRequired(
              validationResult, field, view::setErrorLabelBetrag5KinderVisible);
      case BETRAG_6_KINDER ->
          setErrorLabelVisibleIfRequired(
              validationResult, field, view::setErrorLabelBetrag6KinderVisible);
      default -> throw new IllegalStateException("Unexpected value: " + field);
    }
  }

  @Override
  protected void setAllErrorLabelsInvisible() {
    view.setErrorLabelLektionslaengeInvisible();
    view.setErrorLabelBetrag1KindInvisible();
    view.setErrorLabelBetrag2KinderInvisible();
    view.setErrorLabelBetrag3KinderInvisible();
    view.setErrorLabelBetrag4KinderInvisible();
    view.setErrorLabelBetrag5KinderInvisible();
    view.setErrorLabelBetrag6KinderInvisible();
  }
}
