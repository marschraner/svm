package ch.metzenthin.svm.domain.model;

import static ch.metzenthin.svm.domain.model.validation.ValidationUtils.validateNotNullAndPriceFormatAndWithinRange;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.domain.EntityAlreadyExistsException;
import ch.metzenthin.svm.domain.model.conversion.ConvertedFieldsAndConversionResults;
import ch.metzenthin.svm.domain.model.entityfields.ConvertedLektionsgebuehrenFields;
import ch.metzenthin.svm.domain.model.entityfields.LektionsgebuehrenFields;
import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResultsAndSaveResult;
import ch.metzenthin.svm.domain.model.validation.ValidationUtils;
import ch.metzenthin.svm.persistence.entities.Lektionsgebuehren;
import ch.metzenthin.svm.service.LektionsgebuehrenService;
import ch.metzenthin.svm.service.result.SaveLektionsgebuehrenResult;
import jakarta.persistence.OptimisticLockException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 * @author Martin Schraner
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class CreateOrUpdateLektionsgebuehrenModelImpl
    implements CreateOrUpdateLektionsgebuehrenModel {

  private static final BigDecimal MIN_VALID_VALUE = new BigDecimal("0.00");
  private static final BigDecimal MAX_VALID_VALUE = new BigDecimal("999.95");

  private final boolean neu;
  private final Lektionsgebuehren lektionsgebuehren;
  private final LektionsgebuehrenService lektionsgebuehrenService;

  public CreateOrUpdateLektionsgebuehrenModelImpl(
      Optional<Lektionsgebuehren> lektionsgebuehrenToBeModifiedOptional,
      LektionsgebuehrenService lektionsgebuehrenService) {
    this.neu = lektionsgebuehrenToBeModifiedOptional.isEmpty();
    this.lektionsgebuehren =
        lektionsgebuehrenToBeModifiedOptional.orElseGet(Lektionsgebuehren::new);
    this.lektionsgebuehrenService = lektionsgebuehrenService;
  }

  @Override
  public boolean isNeu() {
    return neu;
  }

  @Override
  public LektionsgebuehrenFields getLektionsgebuehrenFields() {
    return LektionsgebuehrenFields.of(lektionsgebuehren);
  }

  @Override
  public ValidationResult validateLektionslaenge(int lektionslaenge) {
    return ValidationUtils.validateNotValueNotSetAndWithinRange(
        lektionslaenge, 10, 200, Field.LEKTIONSLAENGE);
  }

  @Override
  public ValidationResult validateBetrag1Kind(BigDecimal betrag1Kind) {
    return validateNotNullAndPriceFormatAndWithinRange(
        betrag1Kind, MIN_VALID_VALUE, MAX_VALID_VALUE, Field.BETRAG_1_KIND);
  }

  @Override
  public ValidationResult validateBetrag2Kinder(BigDecimal betrag2Kinder) {
    return validateNotNullAndPriceFormatAndWithinRange(
        betrag2Kinder, MIN_VALID_VALUE, MAX_VALID_VALUE, Field.BETRAG_2_KINDER);
  }

  @Override
  public ValidationResult validateBetrag3Kinder(BigDecimal betrag3Kinder) {
    return validateNotNullAndPriceFormatAndWithinRange(
        betrag3Kinder, MIN_VALID_VALUE, MAX_VALID_VALUE, Field.BETRAG_3_KINDER);
  }

  @Override
  public ValidationResult validateBetrag4Kinder(BigDecimal betrag4Kinder) {
    return validateNotNullAndPriceFormatAndWithinRange(
        betrag4Kinder, MIN_VALID_VALUE, MAX_VALID_VALUE, Field.BETRAG_4_KINDER);
  }

  @Override
  public ValidationResult validateBetrag5Kinder(BigDecimal betrag5Kinder) {
    return validateNotNullAndPriceFormatAndWithinRange(
        betrag5Kinder, MIN_VALID_VALUE, MAX_VALID_VALUE, Field.BETRAG_5_KINDER);
  }

  @Override
  public ValidationResult validateBetrag6Kinder(BigDecimal betrag6Kinder) {
    return validateNotNullAndPriceFormatAndWithinRange(
        betrag6Kinder, MIN_VALID_VALUE, MAX_VALID_VALUE, Field.BETRAG_6_KINDER);
  }

  @Override
  public ValidationResultsAndSaveResult speichern(LektionsgebuehrenFields lektionsgebuehrenFields) {
    ConvertedFieldsAndConversionResults<ConvertedLektionsgebuehrenFields>
        convertedLektionsgebuehrenFieldsAndConversionResults = convertAll(lektionsgebuehrenFields);
    if (!convertedLektionsgebuehrenFieldsAndConversionResults.isValid()) {
      Set<Field> fieldsWithInvalidConversion =
          convertedLektionsgebuehrenFieldsAndConversionResults.getFieldsWithInvalidConversion();
      ValidationResult validationResult =
          new ValidationResult("Ungültiges Format!", fieldsWithInvalidConversion);
      return new ValidationResultsAndSaveResult(List.of(validationResult));
    }
    ConvertedLektionsgebuehrenFields convertedLektionsgebuehrenFields =
        convertedLektionsgebuehrenFieldsAndConversionResults.convertedFields();

    List<ValidationResult> validationResults = validateAll(convertedLektionsgebuehrenFields);
    if (!ValidationResult.allValidationResultsValid(validationResults)) {
      return new ValidationResultsAndSaveResult(validationResults);
    }

    updateModel(convertedLektionsgebuehrenFields);

    SaveLektionsgebuehrenResult saveLektionsgebuehrenResult = saveLektionsgebuehren();
    return new ValidationResultsAndSaveResult(validationResults, saveLektionsgebuehrenResult);
  }

  private ConvertedFieldsAndConversionResults<ConvertedLektionsgebuehrenFields> convertAll(
      LektionsgebuehrenFields lektionsgebuehrenFields) {
    return lektionsgebuehrenFields.convert();
  }

  private List<ValidationResult> validateAll(
      ConvertedLektionsgebuehrenFields convertedLektionsgebuehrenFields) {
    List<ValidationResult> validationResults = new ArrayList<>();

    // Einzelne Felder validieren
    validationResults.add(
        validateLektionslaenge(convertedLektionsgebuehrenFields.lektionslaenge()));
    validationResults.add(validateBetrag1Kind(convertedLektionsgebuehrenFields.betrag1Kind()));
    validationResults.add(validateBetrag2Kinder(convertedLektionsgebuehrenFields.betrag2Kinder()));
    validationResults.add(validateBetrag3Kinder(convertedLektionsgebuehrenFields.betrag3Kinder()));
    validationResults.add(validateBetrag4Kinder(convertedLektionsgebuehrenFields.betrag4Kinder()));
    validationResults.add(validateBetrag5Kinder(convertedLektionsgebuehrenFields.betrag5Kinder()));
    validationResults.add(validateBetrag6Kinder(convertedLektionsgebuehrenFields.betrag6Kinder()));

    // Alle Felder sind validiert, keine übergreifenden Validierungen
    return validationResults;
  }

  private void updateModel(ConvertedLektionsgebuehrenFields lektionsgebuehrenFields) {
    lektionsgebuehrenFields.mergeIntoEntity(lektionsgebuehren);
  }

  private SaveLektionsgebuehrenResult saveLektionsgebuehren() {
    SaveLektionsgebuehrenResult saveLektionsgebuehrenResult;
    try {
      lektionsgebuehrenService.saveLektionsgebuehren(lektionsgebuehren);
      saveLektionsgebuehrenResult = SaveLektionsgebuehrenResult.SPEICHERN_ERFOLGREICH;
    } catch (EntityAlreadyExistsException e) {
      saveLektionsgebuehrenResult = SaveLektionsgebuehrenResult.LEKTIONSGEBUEHREN_BEREITS_ERFASST;
    } catch (OptimisticLockException | OptimisticLockingFailureException e) {
      saveLektionsgebuehrenResult =
          SaveLektionsgebuehrenResult.LEKTIONSGEBUEHREN_DURCH_ANDEREN_BENUTZER_VERAENDERT;
    }
    return saveLektionsgebuehrenResult;
  }
}
