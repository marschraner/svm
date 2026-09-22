package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.datatypes.Anrede;
import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.domain.model.AddOrRemoveMitarbeiterCodeAssignmentListModel;
import ch.metzenthin.svm.domain.model.BindestrichLeerzeichenFormatter;
import ch.metzenthin.svm.domain.model.CreateOrUpdateMitarbeiterModel;
import ch.metzenthin.svm.domain.model.IbanNummerFormatter;
import ch.metzenthin.svm.domain.model.MitarbeiterFieldsAndAdresseFields;
import ch.metzenthin.svm.domain.model.StrasseFormatter;
import ch.metzenthin.svm.domain.model.StrasseHausnummer;
import ch.metzenthin.svm.domain.model.ValidationError;
import ch.metzenthin.svm.domain.model.entityfields.AdresseFields;
import ch.metzenthin.svm.domain.model.entityfields.MitarbeiterFields;
import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResultsAndSubmitResult;
import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;
import ch.metzenthin.svm.ui.view.CreateOrUpdateMitarbeiterView;
import java.awt.event.*;
import java.util.HashSet;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Martin Schraner
 */
@SuppressWarnings("LoggingSimilarMessage")
public class CreateOrUpdateMitarbeiterController
    extends AbstractSubmitDialogController<CreateOrUpdateMitarbeiterView> {

  private static final String FEHLER = "Fehler";

  private static final Logger LOGGER =
      LoggerFactory.getLogger(CreateOrUpdateMitarbeiterController.class);

  private final SvmContext svmContext;

  private final CreateOrUpdateMitarbeiterModel model;

  public CreateOrUpdateMitarbeiterController(
      SvmContext svmContext,
      CreateOrUpdateMitarbeiterModel createOrUpdateMitarbeiterModel,
      String title) {
    super(createView(title));
    this.svmContext = svmContext;
    this.model = createOrUpdateMitarbeiterModel;
    configComboBoxAnrede();
    configTxtNachname();
    configTxtVorname();
    configTxtStrasseHausnummer();
    configTxtPlz();
    configTxtOrt();
    configTxtFestnetz();
    configTxtNatel();
    configTxtEmail();
    configTxtGeburtsdatum();
    configTxtAhvNummer();
    configTxtIbanNummer();
    configTxtVertretungsmoeglichkeiten();
    configTxtBemerkungen();
    configButtonCodesBearbeiten();
    initialiseViewFields();
  }

  private static CreateOrUpdateMitarbeiterView createView(String title) {
    return new CreateOrUpdateMitarbeiterView(title);
  }

  private void configComboBoxAnrede() {
    view.setComboBoxAnredeValues(Anrede.getSelectableValues());
    // ComboBox auf leeren Wert setzen, vor der Registrierung des Listeners, sonst gibt es einen
    // Validierungsfehler!
    view.setComboBoxAnredeSelectedItem(null);
    view.addComboBoxAnredeActionListener(e -> onAnredeSelected());
  }

  private void onAnredeSelected() {
    LOGGER.trace(
        "CreateOrUpdateMitarbeiterController Event Anrede selected={}",
        view.getComboBoxAnredeSelectedItem());
    ValidationResult validationResult = model.validateAnrede(view.getComboBoxAnredeSelectedItem());
    if (!validationResult.isValid()) {
      view.showErrorMessageDialog(validationResult.errorMessage(), FEHLER);
    }
  }

  private void configTxtNachname() {
    view.addTxtNachnameActionListener(e -> onNachnameEvent());
    view.addTxtNachnameFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onNachnameEvent();
          }
        });
  }

  private void onNachnameEvent() {
    LOGGER.trace("CreateOrUpdateMitarbeiterController Event Nachname");
    formatAndValidateString(
        view.getTxtNachnameText(),
        new BindestrichLeerzeichenFormatter()::format,
        model::validateNachname,
        view::setTxtNachnameText,
        view::setErrorLabelNachnameVisible,
        view::setErrorLabelNachnameInvisible);
  }

  private void configTxtVorname() {
    view.addTxtVornameActionListener(e -> onVornameEvent());
    view.addTxtVornameFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onVornameEvent();
          }
        });
  }

  private void onVornameEvent() {
    LOGGER.trace("CreateOrUpdateMitarbeiterController Event Vorname");
    formatAndValidateString(
        view.getTxtVornameText(),
        new BindestrichLeerzeichenFormatter()::format,
        model::validateVorname,
        view::setTxtVornameText,
        view::setErrorLabelVornameVisible,
        view::setErrorLabelVornameInvisible);
  }

  private void configTxtStrasseHausnummer() {
    view.addTxtStrasseHausnummerActionListener(e -> onStrasseHausnummerEvent());
    view.addTxtStrasseHausnummerFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onStrasseHausnummerEvent();
          }
        });
  }

  private void onStrasseHausnummerEvent() {
    LOGGER.trace("CreateOrUpdateMitarbeiterController Event StrasseHausnummer");
    StrasseHausnummer strasseHausnummer = new StrasseHausnummer(view.getTxtStrasseHausnummerText());

    ValidationError strasseValidationError = new ValidationError();
    formatAndValidateString(
        strasseHausnummer.getStrasse(),
        new StrasseFormatter()::format,
        model::validateStrasse,
        strasseHausnummer::setStrasse,
        strasseValidationError::setErrorLabelVisible,
        strasseValidationError::setErrorLabelInvisible);

    ValidationError hausnummerValidationError = new ValidationError();
    formatAndValidateString(
        strasseHausnummer.getHausnummer(),
        model::validateHausnummer,
        strasseHausnummer::setHausnummer,
        hausnummerValidationError::setErrorLabelVisible,
        hausnummerValidationError::setErrorLabelInvisible);

    view.setTxtStrasseHausnummerText(strasseHausnummer.toString());

    String errorMessage = "";
    if (!strasseValidationError.isValid()) {
      errorMessage = strasseValidationError.getErrorMessage();
    }
    if (!hausnummerValidationError.isValid()) {
      errorMessage =
          (errorMessage.isEmpty())
              ? hausnummerValidationError.getErrorMessage()
              : " / " + hausnummerValidationError.getErrorMessage();
    }
    if (!errorMessage.isEmpty()) {
      view.setErrorLabelStrasseHausnummerVisible(errorMessage);
    } else {
      view.setErrorLabelStrasseHausnummerInvisible();
    }
  }

  private void configTxtPlz() {
    view.addTxtPlzActionListener(e -> onPlzEvent());
    view.addTxtPlzFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onPlzEvent();
          }
        });
  }

  private void onPlzEvent() {
    LOGGER.trace("CreateOrUpdateMitarbeiterController Event Plz");
    formatAndValidateString(
        view.getTxtPlzText(),
        model::validatePlz,
        view::setTxtPlzText,
        view::setErrorLabelPlzVisible,
        view::setErrorLabelPlzInvisible);
  }

  private void configTxtOrt() {
    view.addTxtOrtActionListener(e -> onOrtEvent());
    view.addTxtOrtFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onOrtEvent();
          }
        });
  }

  private void onOrtEvent() {
    LOGGER.trace("CreateOrUpdateMitarbeiterController Event Ort");
    formatAndValidateString(
        view.getTxtOrtText(),
        new BindestrichLeerzeichenFormatter()::format,
        model::validateOrt,
        view::setTxtOrtText,
        view::setErrorLabelOrtVisible,
        view::setErrorLabelOrtInvisible);
  }

  private void configTxtFestnetz() {
    view.addTxtFestnetzActionListener(e -> onFestnetzEvent());
    view.addTxtFestnetzFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onFestnetzEvent();
          }
        });
  }

  private void onFestnetzEvent() {
    LOGGER.trace("CreateOrUpdateMitarbeiterController Event Festnetz");
    formatAndValidateString(
        view.getTxtFestnetzText(),
        model::validateFestnetz,
        view::setTxtFestnetzText,
        view::setErrorLabelFestnetzVisible,
        view::setErrorLabelFestnetzInvisible);
  }

  private void configTxtNatel() {
    view.addTxtNatelActionListener(e -> onNatelEvent());
    view.addTxtNatelFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onNatelEvent();
          }
        });
  }

  private void onNatelEvent() {
    LOGGER.trace("CreateOrUpdateMitarbeiterController Event Natel");
    formatAndValidateString(
        view.getTxtNatelText(),
        model::validateNatel,
        view::setTxtNatelText,
        view::setErrorLabelNatelVisible,
        view::setErrorLabelNatelInvisible);
  }

  private void configTxtEmail() {
    view.addTxtEmailActionListener(e -> onEmailEvent());
    view.addTxtEmailFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onEmailEvent();
          }
        });
  }

  private void onEmailEvent() {
    LOGGER.trace("CreateOrUpdateMitarbeiterController Event Email");
    formatAndValidateString(
        view.getTxtEmailText(),
        model::validateEmail,
        view::setTxtEmailText,
        view::setErrorLabelEmailVisible,
        view::setErrorLabelEmailInvisible);
  }

  private void configTxtGeburtsdatum() {
    view.addTxtGeburtsdatumActionListener(e -> onGeburtsdatumEvent());
    view.addTxtGeburtsdatumFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onGeburtsdatumEvent();
          }
        });
  }

  private void onGeburtsdatumEvent() {
    LOGGER.trace("CreateOrUpdateMitarbeiterController Event Geburtsdatum");
    formatConvertAndValidateDate(
        view.getTxtGeburtsdatumText(),
        model::validateGeburtsdatum,
        view::setTxtGeburtsdatumText,
        view::setErrorLabelGeburtsdatumVisible,
        view::setErrorLabelGeburtsdatumInvisible);
  }

  private void configTxtAhvNummer() {
    view.addTxtAhvNummerActionListener(e -> onAhvNummerEvent());
    view.addTxtAhvNummerFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onAhvNummerEvent();
          }
        });
  }

  private void onAhvNummerEvent() {
    LOGGER.trace("CreateOrUpdateMitarbeiterController Event AhvNummer");
    formatAndValidateString(
        view.getTxtAhvNummerText(),
        model::validateAhvNummer,
        view::setTxtAhvNummerText,
        view::setErrorLabelAhvNummerVisible,
        view::setErrorLabelAhvNummerInvisible);
  }

  private void configTxtIbanNummer() {
    view.addTxtIbanNummerActionListener(e -> onIbanNummerEvent());
    view.addTxtIbanNummerFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onIbanNummerEvent();
          }
        });
  }

  private void onIbanNummerEvent() {
    LOGGER.trace("CreateOrUpdateMitarbeiterController Event IbanNummer");
    formatAndValidateString(
        view.getTxtIbanNummerText(),
        new IbanNummerFormatter()::format,
        model::validateIbanNummer,
        view::setTxtIbanNummerText,
        view::setErrorLabelIbanNummerVisible,
        view::setErrorLabelIbanNummerInvisible);
  }

  private void configTxtVertretungsmoeglichkeiten() {
    view.addTxtVertretungsmoeglichkeitenFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onVertretungsmoeglichkeitenEvent();
          }
        });
  }

  private void onVertretungsmoeglichkeitenEvent() {
    LOGGER.trace("CreateOrUpdateMitarbeiterController Event Vertretungsmoeglichkeiten");
    formatAndValidateString(
        view.getTxtVertretungsmoeglichkeitenText(),
        model::validateVertretungsmoeglichkeiten,
        view::setTxtVertretungsmoeglichkeitenText,
        view::setErrorLabelVertretungsmoeglichkeitenVisible,
        view::setErrorLabelVertretungsmoeglichkeitenInvisible);
  }

  private void configTxtBemerkungen() {
    view.addTxtBemerkungenFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onBemerkungenEvent();
          }
        });
  }

  private void onBemerkungenEvent() {
    LOGGER.trace("CreateOrUpdateMitarbeiterController Event Bemerkungen");
    formatAndValidateString(
        view.getTxtBemerkungenText(),
        model::validateBemerkungen,
        view::setTxtBemerkungenText,
        view::setErrorLabelBemerkungenVisible,
        view::setErrorLabelBemerkungenInvisible);
  }

  private void configButtonCodesBearbeiten() {
    view.addButtonCodesBearbeitenActionListener(e -> onCodesBearbeiten());
  }

  private void onCodesBearbeiten() {
    AddOrRemoveMitarbeiterCodeAssignmentListModel addOrRemoveMitarbeiterCodeAssignmentListModel =
        model.createMitarbeiterCodeAssignmentListModel();
    String mitarbeiterNameVorname = model.getPersonVornameNachname();
    String dialogTitel = "Mitarbeiter-Codes";
    if (!mitarbeiterNameVorname.isEmpty()) {
      dialogTitel = dialogTitel + " " + mitarbeiterNameVorname;
    }
    AddOrRemoveMitarbeiterCodeAssignmentListController
        addOrRemoveMitarbeiterCodeAssignmentListController =
            new AddOrRemoveMitarbeiterCodeAssignmentListController(
                svmContext, addOrRemoveMitarbeiterCodeAssignmentListModel, dialogTitel);
    addOrRemoveMitarbeiterCodeAssignmentListController.showDialog();
    List<MitarbeiterCode> assignedCodes =
        addOrRemoveMitarbeiterCodeAssignmentListModel.getAssignedCodesAsSortedList();
    model.setMitarbeiterCodes(new HashSet<>(assignedCodes));
    setCodesLabel();
  }

  private void setCodesLabel() {
    String mitarbeiterCodesAsStr = model.getMitarbeiterCodesAsStr();
    view.setLabelCodesText(mitarbeiterCodesAsStr);
  }

  private void initialiseViewFields() {
    if (!model.isNeu()) {
      MitarbeiterFieldsAndAdresseFields mitarbeiterFieldsAndAdresseFields =
          model.getMitarbeiterFieldsAndAdresseFields();
      MitarbeiterFields mitarbeiterFields = mitarbeiterFieldsAndAdresseFields.mitarbeiterFields();
      AdresseFields adresseFields = mitarbeiterFieldsAndAdresseFields.adresseFields();
      view.setComboBoxAnredeSelectedItem(mitarbeiterFields.anrede());
      view.setTxtNachnameText(mitarbeiterFields.nachname());
      view.setTxtVornameText(mitarbeiterFields.vorname());
      if (adresseFields != null) {
        StrasseHausnummer strasseHausnummer =
            new StrasseHausnummer(adresseFields.strasse(), adresseFields.hausnummer());
        view.setTxtStrasseHausnummerText(strasseHausnummer.toString());
        view.setTxtPlzText(adresseFields.plz());
        view.setTxtOrtText(adresseFields.ort());
      }
      view.setTxtFestnetzText(mitarbeiterFields.festnetz());
      view.setTxtNatelText(mitarbeiterFields.natel());
      view.setTxtEmailText(mitarbeiterFields.email());
      view.setTxtGeburtsdatumText(mitarbeiterFields.geburtsdatum());
      view.setTxtAhvNummerText(mitarbeiterFields.ahvNummer());
      view.setTxtIbanNummerText(mitarbeiterFields.ibanNummer());
      view.setTxtVertretungsmoeglichkeitenText(mitarbeiterFields.vertretungsmoeglichkeiten());
      view.setTxtBemerkungenText(mitarbeiterFields.bemerkungen());
      view.setCheckBoxLehrkraftSelected(mitarbeiterFields.lehrkraft());
      view.setCheckBoxAktivSelected(mitarbeiterFields.aktiv());
      setCodesLabel();
    } else {
      view.setCheckBoxLehrkraftSelected(false);
      view.setCheckBoxAktivSelected(false);
      view.setLabelCodesText("-");
    }
  }

  @Override
  protected ValidationResultsAndSubmitResult submit() {
    MitarbeiterFields mitarbeiterFields =
        new MitarbeiterFields(
            view.getTxtAhvNummerText(),
            view.getTxtIbanNummerText(),
            view.isCheckBoxLehrkraftSelected(),
            view.getTxtVertretungsmoeglichkeitenText(),
            view.getTxtBemerkungenText(),
            view.isCheckBoxAktivSelected(),
            view.getComboBoxAnredeSelectedItem(),
            view.getTxtVornameText(),
            view.getTxtNachnameText(),
            view.getTxtGeburtsdatumText(),
            view.getTxtFestnetzText(),
            view.getTxtNatelText(),
            view.getTxtEmailText());
    StrasseHausnummer strasseHausnummer = new StrasseHausnummer(view.getTxtStrasseHausnummerText());
    AdresseFields adresseFields =
        new AdresseFields(
            strasseHausnummer.getStrasse(),
            strasseHausnummer.getHausnummer(),
            view.getTxtPlzText(),
            view.getTxtOrtText());
    MitarbeiterFieldsAndAdresseFields mitarbeiterFieldsAndAdresseFields =
        new MitarbeiterFieldsAndAdresseFields(mitarbeiterFields, adresseFields);
    return model.speichern(mitarbeiterFieldsAndAdresseFields);
  }

  @Override
  protected void setErrorLabelVisible(ValidationResult validationResult, Field field) {
    switch (field) {
      case ANREDE ->
          setErrorLabelVisibleIfRequired(validationResult, field, view::setErrorLabelAnredeVisible);
      case NACHNAME ->
          setErrorLabelVisibleIfRequired(
              validationResult, field, view::setErrorLabelNachnameVisible);
      case VORNAME ->
          setErrorLabelVisibleIfRequired(
              validationResult, field, view::setErrorLabelVornameVisible);
      case STRASSE_HAUSNUMMER ->
          setErrorLabelVisibleIfRequired(
              validationResult, field, view::setErrorLabelStrasseHausnummerVisible);
      case PLZ ->
          setErrorLabelVisibleIfRequired(validationResult, field, view::setErrorLabelPlzVisible);
      case ORT ->
          setErrorLabelVisibleIfRequired(validationResult, field, view::setErrorLabelOrtVisible);
      case FESTNETZ ->
          setErrorLabelVisibleIfRequired(
              validationResult, field, view::setErrorLabelFestnetzVisible);
      case NATEL ->
          setErrorLabelVisibleIfRequired(validationResult, field, view::setErrorLabelNatelVisible);
      case GEBURTSDATUM ->
          setErrorLabelVisibleIfRequired(
              validationResult, field, view::setErrorLabelGeburtsdatumVisible);
      case AHV_NUMMER ->
          setErrorLabelVisibleIfRequired(
              validationResult, field, view::setErrorLabelAhvNummerVisible);
      case IBAN_NUMMER ->
          setErrorLabelVisibleIfRequired(
              validationResult, field, view::setErrorLabelIbanNummerVisible);
      case VERTRETUNGSMOEGLICHKEITEN ->
          setErrorLabelVisibleIfRequired(
              validationResult, field, view::setErrorLabelVertretungsmoeglichkeitenVisible);
      case BEMERKUNGEN ->
          setErrorLabelVisibleIfRequired(
              validationResult, field, view::setErrorLabelBemerkungenVisible);
      default -> throw new IllegalStateException("Unexpected value: " + field);
    }
  }

  @Override
  protected void setAllErrorLabelsInvisible() {
    view.setErrorLabelAnredeInvisible();
    view.setErrorLabelNachnameInvisible();
    view.setErrorLabelVornameInvisible();
    view.setErrorLabelStrasseHausnummerInvisible();
    view.setErrorLabelPlzInvisible();
    view.setErrorLabelOrtInvisible();
    view.setErrorLabelFestnetzInvisible();
    view.setErrorLabelNatelInvisible();
    view.setErrorLabelEmailInvisible();
    view.setErrorLabelGeburtsdatumInvisible();
    view.setErrorLabelAhvNummerInvisible();
    view.setErrorLabelIbanNummerInvisible();
    view.setErrorLabelVertretungsmoeglichkeitenInvisible();
    view.setErrorLabelBemerkungenInvisible();
  }
}
