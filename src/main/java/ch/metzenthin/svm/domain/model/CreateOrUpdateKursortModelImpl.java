package ch.metzenthin.svm.domain.model;

import static ch.metzenthin.svm.domain.model.formatting.FormattingUtils.formatString;
import static ch.metzenthin.svm.domain.model.validation.ValidationUtils.validateNotEmptyAndLength;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.domain.EntityAlreadyExistsException;
import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResultsAndSaveResult;
import ch.metzenthin.svm.persistence.entities.Kursort;
import ch.metzenthin.svm.service.KursortService;
import ch.metzenthin.svm.service.result.SaveKursortResult;
import jakarta.persistence.OptimisticLockException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 * @author Martin Schraner
 */
public class CreateOrUpdateKursortModelImpl implements CreateOrUpdateKursortModel {

  private static final int MIN_KURSORT_LENGTH = 2;
  private static final int MAX_KURSORT_LENGTH = 50;

  private final boolean neu;
  private final Kursort kursort;
  private final KursortService kursortService;

  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  public CreateOrUpdateKursortModelImpl(
      Optional<Kursort> kursortToBeModifiedOptional, KursortService kursortService) {
    this.neu = kursortToBeModifiedOptional.isEmpty();
    this.kursort = kursortToBeModifiedOptional.orElseGet(Kursort::new);
    this.kursortService = kursortService;
  }

  @Override
  public boolean isNeu() {
    return neu;
  }

  @Override
  public KursortFields getKursortFields() {
    return new KursortFields(kursort.getBezeichnung(), kursort.isSelektierbar());
  }

  @Override
  public String formatBezeichnung(String bezeichnung) {
    return formatString(bezeichnung);
  }

  @Override
  public ValidationResult validateBezeichnung(String bezeichnung) {
    ValidationResult validationResult =
        validateNotEmptyAndLength(
            bezeichnung, MIN_KURSORT_LENGTH, MAX_KURSORT_LENGTH, Field.BEZEICHNUNG);
    if (!validationResult.isValid()) {
      return validationResult;
    }
    return validateKursortNotAlreadyExists(bezeichnung);
  }

  private ValidationResult validateKursortNotAlreadyExists(String bezeichnung) {
    return (kursortService.doesKursortAlreadyExist(kursort.getKursortId(), bezeichnung))
        ? new ValidationResult("Bezeichnung bereits in Verwendung!", Set.of(Field.BEZEICHNUNG))
        : new ValidationResult();
  }

  @Override
  public ValidationResultsAndSaveResult speichern(KursortFields kursortFields) {
    List<ValidationResult> validationResults = validateAll(kursortFields);
    if (!ValidationResult.allValidationResultsValid(validationResults)) {
      return new ValidationResultsAndSaveResult(validationResults);
    }
    updateModel(kursortFields);
    SaveKursortResult saveKursortResult = saveKursort();
    return new ValidationResultsAndSaveResult(validationResults, saveKursortResult);
  }

  private List<ValidationResult> validateAll(KursortFields kursortFields) {
    return List.of(validateBezeichnung(kursortFields.bezeichnung()));
  }

  void updateModel(KursortFields kursortFields) {
    kursort.setBezeichnung(kursortFields.bezeichnung());
    kursort.setSelektierbar(kursortFields.selektierbar());
  }

  private SaveKursortResult saveKursort() {
    SaveKursortResult saveKursortResult;
    try {
      kursortService.saveKursort(kursort);
      saveKursortResult = SaveKursortResult.SPEICHERN_ERFOLGREICH;
    } catch (EntityAlreadyExistsException e) {
      saveKursortResult = SaveKursortResult.KURSORT_BEREITS_ERFASST;
    } catch (OptimisticLockException | OptimisticLockingFailureException e) {
      saveKursortResult = SaveKursortResult.KURSORT_DURCH_ANDEREN_BENUTZER_VERAENDERT;
    }
    return saveKursortResult;
  }
}
