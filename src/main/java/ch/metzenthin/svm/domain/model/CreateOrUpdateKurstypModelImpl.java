package ch.metzenthin.svm.domain.model;

import static ch.metzenthin.svm.domain.model.validation.ValidationUtils.validateNotEmptyAndLength;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.domain.model.entityfields.KurstypFields;
import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResultsAndSaveResult;
import ch.metzenthin.svm.persistence.entities.Kurstyp;
import ch.metzenthin.svm.service.KurstypService;
import ch.metzenthin.svm.service.result.SaveKurstypResult;
import jakarta.persistence.OptimisticLockException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 * @author Martin Schraner
 */
public class CreateOrUpdateKurstypModelImpl implements CreateOrUpdateKurstypModel {

  private static final int MIN_KURSTYP_LENGTH = 2;
  private static final int MAX_KURSTYP_LENGTH = 50;

  private final boolean neu;
  private final Kurstyp kurstyp;
  private final KurstypService kurstypService;

  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  public CreateOrUpdateKurstypModelImpl(
      Optional<Kurstyp> kurstypToBeModifiedOptional, KurstypService kurstypService) {
    this.neu = kurstypToBeModifiedOptional.isEmpty();
    this.kurstyp = kurstypToBeModifiedOptional.orElseGet(Kurstyp::new);
    this.kurstypService = kurstypService;
  }

  @Override
  public boolean isNeu() {
    return neu;
  }

  @Override
  public KurstypFields getKurstypFields() {
    return KurstypFields.of(kurstyp);
  }

  @Override
  public ValidationResult validateBezeichnung(String bezeichnung) {
    ValidationResult validationResult =
        validateNotEmptyAndLength(
            bezeichnung, MIN_KURSTYP_LENGTH, MAX_KURSTYP_LENGTH, Field.BEZEICHNUNG);
    if (!validationResult.isValid()) {
      return validationResult;
    }
    return validateKurstypNotAlreadyExists(bezeichnung);
  }

  private ValidationResult validateKurstypNotAlreadyExists(String bezeichnung) {
    return (kurstypService.doesKurstypAlreadyExist(kurstyp.getKurstypId(), bezeichnung))
        ? new ValidationResult("Bezeichnung bereits in Verwendung!", Set.of(Field.BEZEICHNUNG))
        : new ValidationResult();
  }

  @Override
  public ValidationResultsAndSaveResult speichern(KurstypFields kurstypFields) {
    List<ValidationResult> validationResults = validateAll(kurstypFields);
    if (!ValidationResult.allValidationResultsValid(validationResults)) {
      return new ValidationResultsAndSaveResult(validationResults);
    }

    updateModel(kurstypFields);

    SaveKurstypResult saveKurstypResult = saveKurstyp();
    return new ValidationResultsAndSaveResult(validationResults, saveKurstypResult);
  }

  private List<ValidationResult> validateAll(KurstypFields kurstypFields) {
    return List.of(validateBezeichnung(kurstypFields.bezeichnung()));
  }

  void updateModel(KurstypFields kurstypFields) {
    kurstypFields.mergeIntoEntity(kurstyp);
  }

  private SaveKurstypResult saveKurstyp() {
    SaveKurstypResult saveKurstypResult;
    try {
      saveKurstypResult = kurstypService.saveKurstyp(kurstyp);
    } catch (OptimisticLockException | OptimisticLockingFailureException e) {
      saveKurstypResult = SaveKurstypResult.KURSTYP_DURCH_ANDEREN_BENUTZER_VERAENDERT;
    }
    return saveKurstypResult;
  }
}
