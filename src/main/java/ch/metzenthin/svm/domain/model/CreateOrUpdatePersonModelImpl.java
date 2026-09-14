package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.datatypes.Anrede;
import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.domain.model.entityfields.AdresseFields;
import ch.metzenthin.svm.domain.model.entityfields.ConvertedPersonFields;
import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.domain.model.validation.ValidationUtils;
import ch.metzenthin.svm.persistence.entities.Person;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Martin Schraner
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public abstract class CreateOrUpdatePersonModelImpl<T extends Person>
    implements CreateOrUpdatePersonModel {

  protected final boolean neu;
  protected final T person;
  private final boolean geburtsdatumRequired;
  private final Optional<Calendar> earliestValidDateGeburtstagOptional;
  private final Optional<Calendar> latestValidDateGeburtstagOptional;
  private final boolean adresseRequired;

  protected CreateOrUpdatePersonModelImpl(
      boolean neu,
      T person,
      boolean geburtsdatumRequired,
      Optional<Calendar> earliestValidDateGeburtstagOptional,
      Optional<Calendar> latestValidDateGeburtstagOptional,
      boolean adresseRequired) {
    this.neu = neu;
    this.person = person;
    this.geburtsdatumRequired = geburtsdatumRequired;
    this.earliestValidDateGeburtstagOptional = earliestValidDateGeburtstagOptional;
    this.latestValidDateGeburtstagOptional = latestValidDateGeburtstagOptional;
    this.adresseRequired = adresseRequired;
  }

  @Override
  public boolean isNeu() {
    return neu;
  }

  @Override
  public ValidationResult validateAnrede(Anrede anrede) {
    return ValidationUtils.validateNotNull(anrede, Field.ANREDE);
  }

  @Override
  public ValidationResult validateVorname(String vorname) {
    return ValidationUtils.validateNotEmptyAndLength(vorname, 1, 50, Field.VORNAME);
  }

  @Override
  public ValidationResult validateNachname(String nachname) {
    return ValidationUtils.validateNotEmptyAndLength(nachname, 1, 50, Field.NACHNAME);
  }

  @Override
  public ValidationResult validateGeburtsdatum(Calendar geburtsdatum) {
    ValidationResult validationResult =
        (geburtsdatumRequired)
            ? ValidationUtils.validateNotNull(geburtsdatum, Field.GEBURTSDATUM)
            : new ValidationResult();
    if (!validationResult.isValid()) {
      return validationResult;
    }

    if (geburtsdatum != null && earliestValidDateGeburtstagOptional.isPresent()) {
      validationResult =
          ValidationUtils.validateNotBefore(
              geburtsdatum, earliestValidDateGeburtstagOptional.get(), Field.GEBURTSDATUM);
    }
    if (!validationResult.isValid()) {
      return validationResult;
    }

    if (geburtsdatum != null && latestValidDateGeburtstagOptional.isPresent()) {
      validationResult =
          ValidationUtils.validateNotAfter(
              geburtsdatum, latestValidDateGeburtstagOptional.get(), Field.GEBURTSDATUM);
    }
    return validationResult;
  }

  @Override
  public ValidationResult validateStrasse(String strasse) {
    return ValidationUtils.validateLengthIfNotEmpty(strasse, 1, 50, Field.STRASSE_HAUSNUMMER);
  }

  @Override
  public ValidationResult validateHausnummer(String hausnummer) {
    return ValidationUtils.validateLengthIfNotEmpty(hausnummer, 1, 10, Field.STRASSE_HAUSNUMMER);
  }

  @Override
  public ValidationResult validatePlz(String plz) {
    ValidationResult validationResult =
        (adresseRequired)
            ? ValidationUtils.validateNotNull(plz, Field.PLZ)
            : new ValidationResult();
    if (!validationResult.isValid()) {
      return validationResult;
    }
    return ValidationUtils.validateLengthIfNotEmpty(plz, 4, 10, Field.PLZ);
  }

  @Override
  public ValidationResult validateOrt(String ort) {
    ValidationResult validationResult =
        (adresseRequired)
            ? ValidationUtils.validateNotNull(ort, Field.ORT)
            : new ValidationResult();
    if (!validationResult.isValid()) {
      return validationResult;
    }
    return ValidationUtils.validateLengthIfNotEmpty(ort, 1, 50, Field.ORT);
  }

  @Override
  public ValidationResult validateFestnetz(String festnetz) {
    return ValidationUtils.validateLengthIfNotEmpty(festnetz, 13, 20, Field.FESTNETZ);
  }

  @Override
  public ValidationResult validateNatel(String natel) {
    return ValidationUtils.validateLengthIfNotEmpty(natel, 13, 20, Field.NATEL);
  }

  @Override
  public ValidationResult validateEmail(String email) {
    ValidationResult validationResult =
        ValidationUtils.validateLengthIfNotEmpty(email, 1, 150, Field.EMAIL);
    if (!validationResult.isValid()) {
      return validationResult;
    }

    // emailAdresse enthält möglicherweise mehrere, durch Komma getrennte E-Mails
    if ((email.trim().contains(" ") || email.trim().contains("\\t"))
        && email.contains("@")
        && !email.contains(",")
        && !email.contains(";")) {
      return new ValidationResult(
          "Mehrere E-Mails müssen durch Kommas getrennt werden", Set.of(Field.EMAIL));
    }

    String[] emailAdressenSplitted = email.split("[,;][ \\t]*");
    for (String emailAdresseSplitted : emailAdressenSplitted) {
      ValidationResult validationResultEmailAdresseSplitted =
          ValidationUtils.validateEmail(emailAdresseSplitted.trim(), Field.EMAIL);
      if (!validationResultEmailAdresseSplitted.isValid()) {
        return validationResultEmailAdresseSplitted;
      }
    }

    return validationResult;
  }

  // Übergreifende Validierungen
  private static ValidationResult validateStrasseNotEmptyIfHausnummerNotEmpty(
      String hausnummer, String strasse) {
    return (hausnummer != null && !hausnummer.isBlank() && (strasse == null || strasse.isBlank()))
        ? new ValidationResult(
            "Strasse muss angegeben werden, wenn eine Hausnummer gesetzt ist",
            Set.of(Field.STRASSE_HAUSNUMMER))
        : new ValidationResult();
  }

  private static ValidationResult validateOrtNotEmptyIfStrasseNotEmpty(String strasse, String ort) {
    return (strasse != null && !strasse.isBlank() && (ort == null || ort.isBlank()))
        ? new ValidationResult(
            "Ort muss angegeben werden, wenn eine Strasse gesetzt ist",
            Set.of(Field.STRASSE_HAUSNUMMER))
        : new ValidationResult();
  }

  private static ValidationResult validateOrtNotEmptyIfPlzNotEmpty(String plz, String ort) {
    return (plz != null && !plz.isBlank() && (ort == null || ort.isBlank()))
        ? new ValidationResult(
            "Ort muss angegeben werden, wenn eine Postleitzahl gesetzt ist", Set.of(Field.PLZ))
        : new ValidationResult();
  }

  private static ValidationResult validatePlzNotEmptyIfOrtNotEmpty(String ort, String plz) {
    return (ort != null && !ort.isBlank() && (plz == null || plz.isBlank()))
        ? new ValidationResult(
            "Postleitzahl muss angegeben werden, wenn ein Ort gesetzt ist", Set.of(Field.ORT))
        : new ValidationResult();
  }

  protected List<ValidationResult> validateAll(
      ConvertedPersonFields convertedPersonFields, AdresseFields adresseFields) {
    List<ValidationResult> validationResults = new ArrayList<>();

    // Einzelne Felder validieren
    validationResults.add(validateAnrede(convertedPersonFields.anrede()));
    validationResults.add(validateVorname(convertedPersonFields.vorname()));
    validationResults.add(validateNachname(convertedPersonFields.nachname()));
    validationResults.add(validateGeburtsdatum(convertedPersonFields.geburtsdatum()));
    validationResults.add(validateFestnetz(convertedPersonFields.festnetz()));
    validationResults.add(validateNatel(convertedPersonFields.natel()));
    validationResults.add(validateEmail(convertedPersonFields.email()));

    validationResults.add(validateStrasse(adresseFields.strasse()));
    validationResults.add(validateHausnummer(adresseFields.hausnummer()));
    validationResults.add(validatePlz(adresseFields.plz()));
    validationResults.add(validateOrt(adresseFields.ort()));

    boolean errorsFound =
        validationResults.stream().anyMatch(validationResult -> !validationResult.isValid());
    if (errorsFound) {
      return validationResults;
    }

    // Alle Felder sind validiert, jetzt die übergreifenden Validierungen durchführen
    validationResults.add(
        validateStrasseNotEmptyIfHausnummerNotEmpty(
            adresseFields.hausnummer(), adresseFields.strasse()));
    validationResults.add(
        validateOrtNotEmptyIfStrasseNotEmpty(adresseFields.strasse(), adresseFields.ort()));
    validationResults.add(
        validateOrtNotEmptyIfPlzNotEmpty(adresseFields.plz(), adresseFields.ort()));
    validationResults.add(
        validatePlzNotEmptyIfOrtNotEmpty(adresseFields.ort(), adresseFields.plz()));

    return validationResults;
  }
}
