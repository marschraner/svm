package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.domain.model.CreateOrUpdateMaerchenModel;
import ch.metzenthin.svm.domain.model.entityfields.MaerchenFields;
import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResultsAndSaveResult;
import ch.metzenthin.svm.ui.view.CreateOrUpdateMaerchenView;
import java.awt.event.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Martin Schraner
 */
public class CreateOrUpdateMaerchenController
    extends AbstractCreateOrUpdateController<CreateOrUpdateMaerchenView> {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(CreateOrUpdateMaerchenController.class);

  private final CreateOrUpdateMaerchenModel model;

  public CreateOrUpdateMaerchenController(
      CreateOrUpdateMaerchenModel createOrUpdateMaerchenModel, String title) {
    super(createView(title));
    model = createOrUpdateMaerchenModel;
    configSpinnerSchuljahre();
    configTxtBezeichnung();
    configTxtAnzahlVorstellungen();
    initialiseViewFields();
  }

  private static CreateOrUpdateMaerchenView createView(String title) {
    return new CreateOrUpdateMaerchenView(title);
  }

  private void configSpinnerSchuljahre() {
    view.addSpinnerSchuljahreChangeListener(e -> onSchuljahrSelected());
  }

  private void onSchuljahrSelected() {
    LOGGER.trace(
        "CreateOrUpdateMaerchenController Event Schuljahre selected ={}",
        view.getSpinnerSchuljahreValue());
    ValidationResult validationResult = model.validateSchuljahr(view.getSpinnerSchuljahreValue());
    if (!validationResult.isValid()) {
      view.showErrorMessageDialog(validationResult.errorMessage(), "Fehler");
    }
  }

  private void configTxtBezeichnung() {
    view.addTxtBezeichnungActionListener(e -> onBezeichnungEvent());
    view.addTxtBezeichnungFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onBezeichnungEvent();
          }
        });
  }

  private void onBezeichnungEvent() {
    LOGGER.trace("CreateOrUpdateMaerchenController Event Bezeichnung");
    formatAndValidateString(
        view.getTxtBezeichnungText(),
        model::validateBezeichnung,
        view::setTxtBezeichnungText,
        view::setErrorLabelBezeichnungVisible,
        view::setErrorLabelBezeichnungInvisible);
  }

  private void onAnzahlVorstellungenEvent() {
    LOGGER.trace("CreateOrUpdateMaerchenController Event AnzahlVorstellungen");
    formatConvertAndValidateInt(
        view.getTxtAnzahlVorstellungenText(),
        model::validateAnzahlVorstellungen,
        view::setTxtAnzahlVorstellungenText,
        view::setErrorLabelAnzahlVorstellungenVisible,
        view::setErrorLabelAnzahlVorstellungenInvisible);
  }

  private void configTxtAnzahlVorstellungen() {
    view.addTxtAnzahlVorstellungenActionListener(e -> onAnzahlVorstellungenEvent());
    view.addTxtAnzahlVorstellungenFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onAnzahlVorstellungenEvent();
          }
        });
  }

  private void initialiseViewFields() {
    MaerchenFields maerchenFields = model.getMaerchenFields();
    if (model.isNeu()) {
      String naechstesNochNichtErfasstesSchuljahr = model.getNaechstesNochNichtErfasstesSchuljahr();
      view.setSpinnerSchuljahreValue(naechstesNochNichtErfasstesSchuljahr);
    } else {
      view.setSpinnerSchuljahreValue(maerchenFields.schuljahr());
    }
    view.setTxtBezeichnungText(maerchenFields.bezeichnung());
    view.setTxtAnzahlVorstellungenText(maerchenFields.anzahlVorstellungen());
  }

  @Override
  protected ValidationResultsAndSaveResult speichern() {
    MaerchenFields maerchenFields =
        new MaerchenFields(
            view.getSpinnerSchuljahreValue(),
            view.getTxtBezeichnungText(),
            view.getTxtAnzahlVorstellungenText());

    return model.speichern(
        maerchenFields,
        () -> {
          int n =
              view.showIgnorierenAbbrechenDialog(
                  "Das selektierte Schuljahr liegt in der Vergangenheit. Märchen trotzdem speichern?",
                  "Schuljahr in Vergangenheit");
          // Button Ignorieren (n == 0): true (trotzdem speichern)
          // Button Abbrechen (n != 0): false (speichern abbrechen)
          return n == 0;
        });
  }

  @Override
  protected void setErrorLabelVisible(ValidationResult validationResult, Field field) {
    switch (field) {
      case BEZEICHNUNG ->
          setErrorLabelVisibleIfRequired(
              validationResult, field, view::setErrorLabelBezeichnungVisible);
      case ANZAHL_VORSTELLUNGEN ->
          setErrorLabelVisibleIfRequired(
              validationResult, field, view::setErrorLabelAnzahlVorstellungenVisible);
      default -> throw new IllegalStateException("Unexpected value: " + field);
    }
  }

  @Override
  public void setAllErrorLabelsInvisible() {
    view.setErrorLabelBezeichnungInvisible();
    view.setErrorLabelAnzahlVorstellungenInvisible();
  }
}
