package ch.metzenthin.svm.domain.model;

import static ch.metzenthin.svm.domain.model.validation.ValidationUtils.validateNotEmptyAndLength;
import static ch.metzenthin.svm.domain.model.validation.ValidationUtils.validateNotValueNotSetAndWithinRange;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.domain.EntityAlreadyExistsException;
import ch.metzenthin.svm.domain.model.conversion.ConvertedFieldsAndConversionResults;
import ch.metzenthin.svm.domain.model.entityfields.ConvertedMaerchenFields;
import ch.metzenthin.svm.domain.model.entityfields.MaerchenFields;
import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResultsAndSaveResult;
import ch.metzenthin.svm.persistence.entities.Maerchen;
import ch.metzenthin.svm.service.MaerchenService;
import ch.metzenthin.svm.service.result.SaveMaerchenResult;
import jakarta.persistence.OptimisticLockException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 * @author Martin Schraner
 */
public class CreateOrUpdateMaerchenModelImpl implements CreateOrUpdateMaerchenModel {

  private final boolean neu;
  private final Maerchen maerchen;
  private final MaerchenService maerchenService;

  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  public CreateOrUpdateMaerchenModelImpl(
      Optional<Maerchen> maerchenToBeModifiedOptional, MaerchenService maerchenService) {
    this.neu = maerchenToBeModifiedOptional.isEmpty();
    this.maerchen = maerchenToBeModifiedOptional.orElseGet(Maerchen::new);
    this.maerchenService = maerchenService;
  }

  @Override
  public boolean isNeu() {
    return neu;
  }

  @Override
  public MaerchenFields getMaerchenFields() {
    return MaerchenFields.of(maerchen);
  }

  @Override
  public String getNaechstesNochNichtErfasstesSchuljahr() {
    return maerchenService.findNaechstesNochErfasstesSchuljahr();
  }

  @Override
  public ValidationResult validateSchuljahr(String schuljahr) {
    return validateNotEmptyAndLength(schuljahr, 9, 9, Field.SCHULJAHR);
  }

  @Override
  public ValidationResult validateBezeichnung(String bezeichnung) {
    return validateNotEmptyAndLength(bezeichnung, 2, 30, Field.BEZEICHNUNG);
  }

  @Override
  public ValidationResult validateAnzahlVorstellungen(int anzahlVorstellungen) {
    return validateNotValueNotSetAndWithinRange(
        anzahlVorstellungen, 1, 9, Field.ANZAHL_VORSTELLUNGEN);
  }

  @Override
  public ValidationResultsAndSaveResult speichern(
      MaerchenFields maerchenFields, BooleanSupplier checkForMaerchenInPastWarningSupplier) {

    ConvertedFieldsAndConversionResults<ConvertedMaerchenFields>
        convertedMaerchenFieldsAndConversionResults = convertAll(maerchenFields);
    if (!convertedMaerchenFieldsAndConversionResults.isValid()) {
      List<ValidationResult> invalidConversionResultsAsValidationResults =
          convertedMaerchenFieldsAndConversionResults
              .getInvalidConversionResultsAsValidationResults();
      return new ValidationResultsAndSaveResult(invalidConversionResultsAsValidationResults);
    }
    ConvertedMaerchenFields convertedMaerchenFields =
        convertedMaerchenFieldsAndConversionResults.convertedFields();

    List<ValidationResult> validationResults = validateAll(convertedMaerchenFields);
    if (!ValidationResult.allValidationResultsValid(validationResults)) {
      return new ValidationResultsAndSaveResult(validationResults);
    }

    boolean noWarningsOrWarningsIgnored =
        checkForWarnings(checkForMaerchenInPastWarningSupplier, convertedMaerchenFields);
    if (!noWarningsOrWarningsIgnored) {
      return new ValidationResultsAndSaveResult(
          validationResults, SaveMaerchenResult.SPEICHERN_ABBRECHEN_NACH_WARNUNG);
    }

    updateModel(convertedMaerchenFields);

    SaveMaerchenResult saveMaerchenResult = saveMaerchen();
    return new ValidationResultsAndSaveResult(validationResults, saveMaerchenResult);
  }

  private static ConvertedFieldsAndConversionResults<ConvertedMaerchenFields> convertAll(
      MaerchenFields maerchenFields) {
    return maerchenFields.convert();
  }

  private List<ValidationResult> validateAll(ConvertedMaerchenFields convertedMaerchenFields) {
    List<ValidationResult> validationResults = new ArrayList<>();
    validationResults.add(validateSchuljahr(convertedMaerchenFields.schuljahr()));
    validationResults.add(validateBezeichnung(convertedMaerchenFields.bezeichnung()));
    validationResults.add(
        validateAnzahlVorstellungen(convertedMaerchenFields.anzahlVorstellungen()));

    boolean errorsFound =
        validationResults.stream().anyMatch(validationResult -> !validationResult.isValid());
    if (errorsFound) {
      return validationResults;
    }

    // Alle Felder sind validiert, jetzt die übergreifenden Validierungen durchführen
    validationResults.add(validateMaerchenBereitsErfasst(convertedMaerchenFields.schuljahr()));

    return validationResults;
  }

  private ValidationResult validateMaerchenBereitsErfasst(String schuljahr) {
    boolean bereitsErfasst =
        maerchenService.existsMaerchenForSchuljahr(maerchen.getMaerchenId(), schuljahr);
    return (bereitsErfasst)
        ? new ValidationResult(SaveMaerchenResult.MAERCHEN_BEREITS_ERFASST.getMessage(), null)
        : new ValidationResult();
  }

  private static boolean checkForWarnings(
      BooleanSupplier checkForMaerchenInPastWarningSupplier,
      ConvertedMaerchenFields convertedMaerchenFields) {
    if (checkIfMaerchenIsInPast(convertedMaerchenFields.schuljahr())) {
      // Das zu speichernde Märchen liegt in der Vergangenheit. Zurück an den Aufrufer zum
      // Entscheid, ob das Märchen trotzdem gespeichert werden soll.
      return checkForMaerchenInPastWarningSupplier.getAsBoolean();
    }
    return true;
  }

  private static boolean checkIfMaerchenIsInPast(String schuljahr) {
    Calendar today = new GregorianCalendar();
    int schuljahr1;
    if (today.get(Calendar.MONTH) <= Calendar.JUNE) {
      schuljahr1 = today.get(Calendar.YEAR) - 1;
    } else {
      schuljahr1 = today.get(Calendar.YEAR);
    }
    int schuljahr1Maerchen = Integer.parseInt(schuljahr.substring(0, 4));
    return schuljahr1Maerchen < schuljahr1;
  }

  void updateModel(ConvertedMaerchenFields maerchenFields) {
    maerchenFields.mergeIntoEntity(maerchen);
  }

  private SaveMaerchenResult saveMaerchen() {
    SaveMaerchenResult saveMaerchenResult;
    try {
      maerchenService.saveMaerchen(maerchen);
      saveMaerchenResult = SaveMaerchenResult.SPEICHERN_ERFOLGREICH;
    } catch (EntityAlreadyExistsException e) {
      saveMaerchenResult = SaveMaerchenResult.MAERCHEN_BEREITS_ERFASST;
    } catch (OptimisticLockException | OptimisticLockingFailureException e) {
      saveMaerchenResult = SaveMaerchenResult.MAERCHEN_DURCH_ANDEREN_BENUTZER_VERAENDERT;
    }
    return saveMaerchenResult;
  }
}
