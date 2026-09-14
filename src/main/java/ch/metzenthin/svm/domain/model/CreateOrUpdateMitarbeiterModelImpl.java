package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.common.utils.DateAndTimeUtils;
import ch.metzenthin.svm.domain.model.conversion.ConvertedFieldsAndConversionResults;
import ch.metzenthin.svm.domain.model.entityfields.AdresseFields;
import ch.metzenthin.svm.domain.model.entityfields.ConvertedMitarbeiterFields;
import ch.metzenthin.svm.domain.model.entityfields.ConvertedPersonFields;
import ch.metzenthin.svm.domain.model.entityfields.MitarbeiterFields;
import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResultsAndSubmitResult;
import ch.metzenthin.svm.domain.model.validation.ValidationUtils;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;
import ch.metzenthin.svm.service.MitarbeiterMitarbeiterCodeService;
import ch.metzenthin.svm.service.MitarbeiterService;
import ch.metzenthin.svm.service.result.SaveMitarbeiterResult;
import jakarta.persistence.OptimisticLockException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.Setter;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 * @author Martin Schraner
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class CreateOrUpdateMitarbeiterModelImpl extends CreateOrUpdatePersonModelImpl<Mitarbeiter>
    implements CreateOrUpdateMitarbeiterModel {

  private final Mitarbeiter mitarbeiter;
  private Optional<Adresse> adresseOptional;
  @Setter private Set<MitarbeiterCode> mitarbeiterCodes;
  private final MitarbeiterService mitarbeiterService;

  public CreateOrUpdateMitarbeiterModelImpl(
      Optional<Mitarbeiter> mitarbeiterToBeModifiedOptional,
      MitarbeiterService mitarbeiterService,
      MitarbeiterMitarbeiterCodeService mitarbeiterMitarbeiterCodeService) {
    super(
        mitarbeiterToBeModifiedOptional.isEmpty(),
        mitarbeiterToBeModifiedOptional.orElseGet(Mitarbeiter::new),
        false,
        Optional.of(DateAndTimeUtils.getNYearsBeforeNow(80)),
        Optional.of(DateAndTimeUtils.getNYearsBeforeNow(10)),
        false);
    this.mitarbeiter = person;
    this.adresseOptional =
        (mitarbeiter.getAdresse() != null)
            ? Optional.of(mitarbeiter.getAdresse())
            : Optional.empty();
    this.mitarbeiterCodes =
        mitarbeiterToBeModifiedOptional
            .map(
                mitarbeiterToBeModified ->
                    new HashSet<>(
                        mitarbeiterMitarbeiterCodeService.findMitarbeiterCodesByMitarbeiterId(
                            mitarbeiterToBeModified.getPersonId())))
            .orElseGet(HashSet::new);
    this.mitarbeiterService = mitarbeiterService;
  }

  @Override
  public String getMitarbeiterCodesAsStr() {
    if (mitarbeiterCodes.isEmpty()) {
      return "-";
    }
    List<MitarbeiterCode> mitarbeiterCodesAsList = getMitarbeiterCodesAsSortedList();
    StringBuilder mitarbeiterCodesAsStr =
        new StringBuilder(mitarbeiterCodesAsList.get(0).getKuerzel());
    for (int i = 1; i < mitarbeiterCodesAsList.size(); i++) {
      mitarbeiterCodesAsStr.append(", ").append(mitarbeiterCodesAsList.get(i).getKuerzel());
    }
    return mitarbeiterCodesAsStr.toString();
  }

  private List<MitarbeiterCode> getMitarbeiterCodesAsSortedList() {
    List<MitarbeiterCode> mitarbeiterCodesAsList = new ArrayList<>(mitarbeiterCodes);
    Collections.sort(mitarbeiterCodesAsList);
    return mitarbeiterCodesAsList;
  }

  @Override
  public MitarbeiterFieldsAndAdresseFields getMitarbeiterFieldsAndAdresseFields() {
    MitarbeiterFields mitarbeiterFields = MitarbeiterFields.of(mitarbeiter);
    AdresseFields adresseFields = AdresseFields.of(mitarbeiter.getAdresse());
    return new MitarbeiterFieldsAndAdresseFields(mitarbeiterFields, adresseFields);
  }

  @Override
  public ValidationResult validateAhvNummer(String ahvNummer) {
    return ValidationUtils.validateLengthIfNotEmpty(ahvNummer, 16, 16, Field.AHV_NUMMER);
  }

  @Override
  public ValidationResult validateIbanNummer(String ibanNummer) {
    ValidationResult validationResult =
        ValidationUtils.validateLengthIfNotEmpty(ibanNummer, 15, 40, Field.IBAN_NUMMER);
    if (!validationResult.isValid()) {
      return validationResult;
    }

    return ValidationUtils.validateIbanNummer(ibanNummer, Field.IBAN_NUMMER);
  }

  @Override
  public ValidationResult validateVertretungsmoeglichkeiten(String vertretungsmoeglichkeiten) {
    return ValidationUtils.validateLengthIfNotEmpty(
        vertretungsmoeglichkeiten, 0, 1000, Field.VERTRETUNGSMOEGLICHKEITEN);
  }

  @Override
  public ValidationResult validateBemerkungen(String bemerkungen) {
    return ValidationUtils.validateLengthIfNotEmpty(bemerkungen, 0, 1000, Field.BEMERKUNGEN);
  }

  @Override
  public ValidationResultsAndSubmitResult speichern(
      MitarbeiterFieldsAndAdresseFields mitarbeiterFieldsAndAdresseFields,
      Set<MitarbeiterCode> mitarbeiterCodes) {

    ConvertedFieldsAndConversionResults<ConvertedMitarbeiterFields>
        convertedMitarbeiterFieldsAndConversionResults =
            convertAll(mitarbeiterFieldsAndAdresseFields.mitarbeiterFields());
    if (!convertedMitarbeiterFieldsAndConversionResults.isValid()) {
      List<ValidationResult> invalidConversionResultsAsValidationResults =
          convertedMitarbeiterFieldsAndConversionResults
              .getInvalidConversionResultsAsValidationResults();
      return new ValidationResultsAndSubmitResult(invalidConversionResultsAsValidationResults);
    }
    ConvertedMitarbeiterFields convertedMitarbeiterFields =
        convertedMitarbeiterFieldsAndConversionResults.convertedFields();

    List<ValidationResult> validationResults =
        validateAll(convertedMitarbeiterFields, mitarbeiterFieldsAndAdresseFields.adresseFields());
    if (!ValidationResult.allValidationResultsValid(validationResults)) {
      return new ValidationResultsAndSubmitResult(validationResults);
    }

    updateModel(convertedMitarbeiterFields, mitarbeiterFieldsAndAdresseFields.adresseFields());

    SaveMitarbeiterResult saveMitarbeiterResult = saveMitarbeiter();
    return new ValidationResultsAndSubmitResult(validationResults, saveMitarbeiterResult);
  }

  private ConvertedFieldsAndConversionResults<ConvertedMitarbeiterFields> convertAll(
      MitarbeiterFields mitarbeiterFields) {
    return mitarbeiterFields.convert();
  }

  protected List<ValidationResult> validateAll(
      ConvertedMitarbeiterFields convertedMitarbeiterFields, AdresseFields adresseFields) {

    // Person-Felder validieren
    ConvertedPersonFields convertedPersonFields =
        new ConvertedPersonFields(
            convertedMitarbeiterFields.anrede(),
            convertedMitarbeiterFields.vorname(),
            convertedMitarbeiterFields.nachname(),
            convertedMitarbeiterFields.geburtsdatum(),
            convertedMitarbeiterFields.festnetz(),
            convertedMitarbeiterFields.natel(),
            convertedMitarbeiterFields.email());
    List<ValidationResult> validationResults = validateAll(convertedPersonFields, adresseFields);

    boolean errorsFound =
        validationResults.stream().anyMatch(validationResult -> !validationResult.isValid());
    if (errorsFound) {
      return validationResults;
    }

    // Einzelne Felder Mitarbeiter validieren
    validationResults.add(validateAhvNummer(convertedMitarbeiterFields.ahvNummer()));
    validationResults.add(validateIbanNummer(convertedMitarbeiterFields.ibanNummer()));
    validationResults.add(
        validateVertretungsmoeglichkeiten(convertedMitarbeiterFields.vertretungsmoeglichkeiten()));
    validationResults.add(validateBemerkungen(convertedMitarbeiterFields.bemerkungen()));

    return validationResults;
  }

  void updateModel(
      ConvertedMitarbeiterFields convertedMitarbeiterFields, AdresseFields adresseFields) {
    convertedMitarbeiterFields.mergeIntoEntity(mitarbeiter);

    if (adresseFields.ort() != null && !adresseFields.ort().isBlank()) {
      if (adresseOptional.isEmpty()) {
        adresseOptional = Optional.of(new Adresse());
      }
      adresseFields.mergeIntoEntity(adresseOptional.get());
    } else if (adresseFields.ort() == null
        || adresseFields.ort().isBlank() && adresseOptional.isPresent()) {
      adresseOptional = Optional.empty();
    }
  }

  private SaveMitarbeiterResult saveMitarbeiter() {
    SaveMitarbeiterResult saveMitarbeiterResult;
    try {
      saveMitarbeiterResult =
          mitarbeiterService.saveMitarbeiter(mitarbeiter, adresseOptional, mitarbeiterCodes);
    } catch (OptimisticLockException | OptimisticLockingFailureException e) {
      saveMitarbeiterResult = SaveMitarbeiterResult.MITARBEITER_DURCH_ANDEREN_BENUTZER_VERAENDERT;
    }
    return saveMitarbeiterResult;
  }
}
