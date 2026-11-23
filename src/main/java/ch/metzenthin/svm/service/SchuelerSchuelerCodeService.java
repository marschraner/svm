package ch.metzenthin.svm.service;

import ch.metzenthin.svm.persistence.entities.SchuelerCode;
import ch.metzenthin.svm.service.result.DeleteAssignmentResult;
import ch.metzenthin.svm.service.result.SaveResultAndIdOfSavedEntity;
import ch.metzenthin.svm.service.result.SaveSchuelerCodeAssignmentResult;
import java.util.List;
import java.util.Optional;

/**
 * @author Hans Stamm
 */
public interface SchuelerSchuelerCodeService extends ReferencedCodeService {

  Optional<SchuelerCode> findAssignedSchuelerCodeBySchuelerIdAndSchuelerCodeId(
      int schuelerId, int schuelerCodeId);

  List<SchuelerCode> findAssignedSchuelerCodesBySchuelerId(int schuelerId);

  List<SchuelerCode> findNotAssignedSelektierbareSchuelerCodesBySchuelerId(int schuelerId);

  SaveResultAndIdOfSavedEntity<SaveSchuelerCodeAssignmentResult> saveSchuelerCodeAssignment(
      int schuelerId, int schuelerCodeId);

  DeleteAssignmentResult deleteSchuelerCodeAssignment(int schuelerId, SchuelerCode schuelerCode);
}
