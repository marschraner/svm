package ch.metzenthin.svm.domain.model;

import static ch.metzenthin.svm.domain.model.validation.ValidationUtils.validateNotEmptyAndLength;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.domain.model.entityfields.CodeFields;
import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResultsAndSubmitResult;
import ch.metzenthin.svm.persistence.entities.Code;
import ch.metzenthin.svm.service.CodeService;
import ch.metzenthin.svm.service.result.SaveCodeResult;
import ch.metzenthin.svm.service.result.SaveResultAndIdOfSavedEntity;
import jakarta.persistence.OptimisticLockException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 * @author Martin Schraner
 */
public abstract class CreateOrUpdateCodeModelImpl<T extends Code>
    implements CreateOrUpdateCodeModel {

  private final boolean neu;
  private final T code;
  private SaveSuccessfulAndEntityIdOptional saveSuccessfulAndCodeIdOptional;
  private final CodeService<T> codeService;

  protected CreateOrUpdateCodeModelImpl(boolean neu, T code, CodeService<T> codeService) {
    this.neu = neu;
    this.code = code;
    saveSuccessfulAndCodeIdOptional =
        new SaveSuccessfulAndEntityIdOptional(
            false, (code.getCodeId() != null) ? Optional.of(code.getCodeId()) : Optional.empty());
    this.codeService = codeService;
  }

  @Override
  public boolean isNeu() {
    return neu;
  }

  @Override
  public SaveSuccessfulAndEntityIdOptional getSaveSuccessfulAndCodeIdOptional() {
    return saveSuccessfulAndCodeIdOptional;
  }

  @Override
  public CodeFields getCodeFields() {
    return CodeFields.of(code);
  }

  @Override
  public ValidationResult validateKuerzel(String kuerzel) {
    ValidationResult validationResult = validateNotEmptyAndLength(kuerzel, 1, 3, Field.KUERZEL);
    if (!validationResult.isValid()) {
      return validationResult;
    }
    return validateKuerzelNotAlreadyExists(kuerzel);
  }

  private ValidationResult validateKuerzelNotAlreadyExists(String kuerzel) {
    return (codeService.doesKuerzelAlreadyExist(code.getCodeId(), kuerzel))
        ? new ValidationResult("Kuerzel bereits in Verwendung!", Set.of(Field.KUERZEL))
        : new ValidationResult();
  }

  @Override
  public ValidationResult validateBeschreibung(String beschreibung) {
    return validateNotEmptyAndLength(beschreibung, 2, 50, Field.BESCHREIBUNG);
  }

  @Override
  public ValidationResultsAndSubmitResult speichern(CodeFields codeFields) {
    List<ValidationResult> validationResults = validateAll(codeFields);
    if (!ValidationResult.allValidationResultsValid(validationResults)) {
      return new ValidationResultsAndSubmitResult(validationResults);
    }

    updateModel(codeFields);

    SaveCodeResult saveCodeResult = saveCode();
    return new ValidationResultsAndSubmitResult(validationResults, saveCodeResult);
  }

  private List<ValidationResult> validateAll(CodeFields codeFields) {
    return List.of(
        validateKuerzel(codeFields.kuerzel()), validateBeschreibung(codeFields.beschreibung()));
  }

  void updateModel(CodeFields codeFields) {
    codeFields.mergeIntoEntity(code);
  }

  public SaveCodeResult saveCode() {
    SaveCodeResult saveCodeResult;
    try {
      SaveResultAndIdOfSavedEntity<SaveCodeResult> saveResultAndIdOfSavedEntity =
          codeService.saveCode(code);
      saveCodeResult = saveResultAndIdOfSavedEntity.saveResult();
      saveSuccessfulAndCodeIdOptional =
          new SaveSuccessfulAndEntityIdOptional(
              saveCodeResult.isSubmitSuccessful(),
              Optional.of(saveResultAndIdOfSavedEntity.idOfSavedEntity()));
    } catch (OptimisticLockException | OptimisticLockingFailureException e) {
      saveCodeResult = SaveCodeResult.CODE_DURCH_ANDEREN_BENUTZER_VERAENDERT;
    }
    return saveCodeResult;
  }
}
