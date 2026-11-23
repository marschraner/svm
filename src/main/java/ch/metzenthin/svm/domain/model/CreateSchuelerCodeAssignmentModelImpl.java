package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResultsAndSubmitResult;
import ch.metzenthin.svm.persistence.entities.SchuelerCode;
import ch.metzenthin.svm.service.SchuelerSchuelerCodeService;
import ch.metzenthin.svm.service.result.SaveResultAndIdOfSavedEntity;
import ch.metzenthin.svm.service.result.SaveSchuelerCodeAssignmentResult;
import java.util.List;
import java.util.Optional;

/**
 * @author Hans Stamm
 */
public class CreateSchuelerCodeAssignmentModelImpl
    extends AbstractCreateCodeAssignmentModel<SchuelerCode> {

  private final int schuelerId;
  private SaveSuccessfulAndEntityIdOptional saveSuccessfulAndSchuelerCodeIdOptional;
  private final SchuelerSchuelerCodeService schuelerSchuelerCodeService;

  public CreateSchuelerCodeAssignmentModelImpl(
      int schuelerId, SchuelerSchuelerCodeService schuelerSchuelerCodeService) {
    this.schuelerId = schuelerId;
    saveSuccessfulAndSchuelerCodeIdOptional =
        new SaveSuccessfulAndEntityIdOptional(false, Optional.empty());
    this.schuelerSchuelerCodeService = schuelerSchuelerCodeService;
  }

  @Override
  public SaveSuccessfulAndEntityIdOptional getSaveSuccessfulAndCodeIdOptional() {
    return saveSuccessfulAndSchuelerCodeIdOptional;
  }

  @Override
  public SchuelerCode[] getSelectableCodes() {
    List<SchuelerCode> schuelerCodes =
        schuelerSchuelerCodeService.findNotAssignedSelektierbareSchuelerCodesBySchuelerId(
            schuelerId);
    return schuelerCodes.toArray(new SchuelerCode[0]);
  }

  @Override
  public ValidationResultsAndSubmitResult speichern(SchuelerCode schuelerCode) {
    ValidationResult validationResult = validateCode(schuelerCode);
    if (!validationResult.isValid()) {
      return new ValidationResultsAndSubmitResult(List.of(validationResult));
    }
    SaveSchuelerCodeAssignmentResult saveSchuelerCodeAssignmentResult =
        saveSchuelerCodeAssignment(schuelerCode);
    return new ValidationResultsAndSubmitResult(
        List.of(validationResult), saveSchuelerCodeAssignmentResult);
  }

  private SaveSchuelerCodeAssignmentResult saveSchuelerCodeAssignment(SchuelerCode schuelerCode) {
    SaveResultAndIdOfSavedEntity<SaveSchuelerCodeAssignmentResult>
        saveSchuelerCodeAssignmentResultAndIdOfSavedEntity =
            schuelerSchuelerCodeService.saveSchuelerCodeAssignment(
                schuelerId, schuelerCode.getCodeId());
    SaveSchuelerCodeAssignmentResult saveSchuelerCodeAssignmentResult =
        saveSchuelerCodeAssignmentResultAndIdOfSavedEntity.saveResult();
    saveSuccessfulAndSchuelerCodeIdOptional =
        new SaveSuccessfulAndEntityIdOptional(
            saveSchuelerCodeAssignmentResult.isSubmitSuccessful(),
            Optional.of(saveSchuelerCodeAssignmentResultAndIdOfSavedEntity.idOfSavedEntity()));
    return saveSchuelerCodeAssignmentResult;
  }
}
