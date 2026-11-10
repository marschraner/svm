package ch.metzenthin.svm.domain.model;

import static ch.metzenthin.svm.common.utils.Converter.getNYearsAfterNow;
import static ch.metzenthin.svm.common.utils.Converter.getNYearsBeforeNow;
import static ch.metzenthin.svm.domain.model.validation.ValidationUtils.validateLengthIfNotEmpty;
import static ch.metzenthin.svm.domain.model.validation.ValidationUtils.validateNotNullAndWithinPeriod;
import static ch.metzenthin.svm.domain.model.validation.ValidationUtils.validatePeriod;
import static ch.metzenthin.svm.domain.model.validation.ValidationUtils.validateWithinPeriod;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.domain.model.conversion.ConvertedFieldsAndConversionResults;
import ch.metzenthin.svm.domain.model.entityfields.ConvertedDispensationFields;
import ch.metzenthin.svm.domain.model.entityfields.DispensationFields;
import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResultsAndSubmitResult;
import ch.metzenthin.svm.persistence.entities.Dispensation;
import ch.metzenthin.svm.persistence.entities.Schueler;
import ch.metzenthin.svm.service.DispensationService;
import ch.metzenthin.svm.service.result.SaveDispensationResult;
import ch.metzenthin.svm.service.result.SaveResultAndIdOfSavedEntity;
import jakarta.persistence.OptimisticLockException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 * @author Martin Schraner
 */
public class CreateOrUpdateDispensationModelImpl implements CreateOrUpdateDispensationModel {

  private final boolean neu;
  private final Dispensation dispensation;
  private final Schueler schueler;
  private SaveSuccessfulAndEntityIdOptional saveSuccessfulAndDispensationIdOptional;
  private final DispensationService dispensationService;

  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  public CreateOrUpdateDispensationModelImpl(
      Optional<Dispensation> dispensationToBeModifiedOptional,
      Schueler schueler,
      DispensationService dispensationService) {
    this.neu = dispensationToBeModifiedOptional.isEmpty();
    this.dispensation = dispensationToBeModifiedOptional.orElseGet(Dispensation::new);
    this.schueler = schueler;
    saveSuccessfulAndDispensationIdOptional =
        new SaveSuccessfulAndEntityIdOptional(
            false,
            (dispensation.getDispensationId() != null)
                ? Optional.of(dispensation.getDispensationId())
                : Optional.empty());
    this.dispensationService = dispensationService;
  }

  @Override
  public boolean isNeu() {
    return neu;
  }

  @Override
  public SaveSuccessfulAndEntityIdOptional getSaveSuccessfulAndDispensationIdOptional() {
    return saveSuccessfulAndDispensationIdOptional;
  }

  @Override
  public DispensationFields getDispensationFields() {
    return DispensationFields.of(dispensation);
  }

  @Override
  public ValidationResult validateDispensationsbeginn(Calendar dispensationsbeginn) {
    return validateNotNullAndWithinPeriod(
        dispensationsbeginn,
        getNYearsBeforeNow(5),
        getNYearsAfterNow(5),
        Field.DISPENSATIONSBEGINN);
  }

  @Override
  public ValidationResult validateDispensationsende(Calendar dispensationsende) {
    return validateWithinPeriod(
        dispensationsende, getNYearsBeforeNow(5), getNYearsAfterNow(5), Field.DISPENSATIONSENDE);
  }

  @Override
  public ValidationResult validateVoraussichtlicheDauer(String voraussichtlicheDauer) {
    return validateLengthIfNotEmpty(voraussichtlicheDauer, 5, 20, Field.VORAUSSICHTLICHE_DAUER);
  }

  @Override
  public ValidationResult validateGrund(String grund) {
    return validateLengthIfNotEmpty(grund, 5, 30, Field.GRUND);
  }

  // Übergreifende Validierungen
  private static ValidationResult validateDispensationPeriod(Calendar beginn, Calendar ende) {
    return validatePeriod(beginn, ende, Field.DISPENSATIONSBEGINN, Field.DISPENSATIONSBEGINN);
  }

  private ValidationResult validateDispensationOverlapsOtherDispensationen(
      Calendar dispensationsbeginn, Calendar dispensationsende) {
    boolean dispensationOverlapsOtherDispensationen =
        dispensationService.checkIfDispensationOverlapsOtherDispensationen(
            schueler.getPersonId(),
            dispensation.getDispensationId(),
            dispensationsbeginn,
            dispensationsende);
    if (dispensationOverlapsOtherDispensationen) {
      return new ValidationResult(
          SaveDispensationResult.DISPENSATION_UEBERLAPPT_MIT_ANDERER_DISPENSATION.getMessage(),
          Set.of(Field.DISPENSATIONSBEGINN, Field.DISPENSATIONSENDE));
    }
    return new ValidationResult();
  }

  @Override
  public ValidationResultsAndSubmitResult speichern(DispensationFields dispensationFields) {

    ConvertedFieldsAndConversionResults<ConvertedDispensationFields>
        convertedDispensationFieldsAndConversionResults = convertAll(dispensationFields);
    if (!convertedDispensationFieldsAndConversionResults.isValid()) {
      List<ValidationResult> invalidConversionResultsAsValidationResults =
          convertedDispensationFieldsAndConversionResults
              .getInvalidConversionResultsAsValidationResults();
      return new ValidationResultsAndSubmitResult(invalidConversionResultsAsValidationResults);
    }
    ConvertedDispensationFields convertedDispensationFields =
        convertedDispensationFieldsAndConversionResults.convertedFields();

    List<ValidationResult> validationResults = validateAll(convertedDispensationFields);
    if (!ValidationResult.allValidationResultsValid(validationResults)) {
      return new ValidationResultsAndSubmitResult(validationResults);
    }

    updateModel(convertedDispensationFields);

    SaveDispensationResult saveDispensationResult = saveDispensation();
    return new ValidationResultsAndSubmitResult(validationResults, saveDispensationResult);
  }

  private static ConvertedFieldsAndConversionResults<ConvertedDispensationFields> convertAll(
      DispensationFields dispensationFields) {
    return dispensationFields.convert();
  }

  private List<ValidationResult> validateAll(
      ConvertedDispensationFields convertedDispensationFields) {
    List<ValidationResult> validationResults = new ArrayList<>();

    // Einzelne Felder validieren
    validationResults.add(
        validateDispensationsbeginn(convertedDispensationFields.dispensationsbeginn()));
    validationResults.add(
        validateDispensationsende(convertedDispensationFields.dispensationsende()));
    validationResults.add(
        validateVoraussichtlicheDauer(convertedDispensationFields.voraussichtlicheDauer()));
    validationResults.add(validateGrund(convertedDispensationFields.grund()));

    boolean errorsFound =
        validationResults.stream().anyMatch(validationResult -> !validationResult.isValid());
    if (errorsFound) {
      return validationResults;
    }

    // Alle Felder sind validiert, jetzt die übergreifenden Validierungen durchführen
    validationResults.add(
        validateDispensationPeriod(
            convertedDispensationFields.dispensationsbeginn(),
            convertedDispensationFields.dispensationsende()));
    validationResults.add(
        validateDispensationOverlapsOtherDispensationen(
            convertedDispensationFields.dispensationsbeginn(),
            convertedDispensationFields.dispensationsende()));

    return validationResults;
  }

  void updateModel(ConvertedDispensationFields convertedDispensationFields) {
    convertedDispensationFields.mergeIntoEntity(dispensation);
    dispensation.setSchueler(schueler);
  }

  private SaveDispensationResult saveDispensation() {
    SaveDispensationResult saveDispensationResult;
    try {
      SaveResultAndIdOfSavedEntity<SaveDispensationResult> saveResultAndIdOfSavedEntity =
          dispensationService.saveDispensation(dispensation);
      saveDispensationResult = saveResultAndIdOfSavedEntity.saveResult();
      saveSuccessfulAndDispensationIdOptional =
          new SaveSuccessfulAndEntityIdOptional(
              saveDispensationResult.isSubmitSuccessful(),
              Optional.of(saveResultAndIdOfSavedEntity.idOfSavedEntity()));
    } catch (OptimisticLockException | OptimisticLockingFailureException e) {
      saveDispensationResult =
          SaveDispensationResult.DISPENSATION_DURCH_ANDEREN_BENUTZER_VERAENDERT;
    }
    return saveDispensationResult;
  }
}
