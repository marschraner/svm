package ch.metzenthin.svm.service.impl;

import ch.metzenthin.svm.persistence.entities.SchuelerCode;
import ch.metzenthin.svm.persistence.repository.SchuelerSchuelerCodeRepository;
import ch.metzenthin.svm.service.SchuelerSchuelerCodeService;
import ch.metzenthin.svm.service.result.SaveResultAndIdOfSavedEntity;
import ch.metzenthin.svm.service.result.SaveSchuelerCodeAssignmentResult;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Hans Stamm
 */
@Service
public class SchuelerSchuelerCodeServiceImpl implements SchuelerSchuelerCodeService {

  private final SchuelerSchuelerCodeRepository schuelerSchuelerCodeRepository;

  public SchuelerSchuelerCodeServiceImpl(
      SchuelerSchuelerCodeRepository schuelerSchuelerCodeRepository) {
    this.schuelerSchuelerCodeRepository = schuelerSchuelerCodeRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public boolean existsReferencedCodeByCodeId(int codeId) {
    return schuelerSchuelerCodeRepository.countBySchuelerSchuelerCodeCodeId(codeId) > 0;
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<SchuelerCode> findAssignedSchuelerCodeBySchuelerIdAndSchuelerCodeId(
      int schuelerId, int schuelerCodeId) {
    return Optional.empty();
  }

  @Override
  @Transactional(readOnly = true)
  public List<SchuelerCode> findAssignedSchuelerCodesBySchuelerId(int schuelerId) {
    return List.of();
  }

  @Override
  @Transactional(readOnly = true)
  public List<SchuelerCode> findNotAssignedSchuelerCodesBySchuelerId(int schuelerId) {
    return List.of();
  }

  @Override
  @Transactional
  public SaveResultAndIdOfSavedEntity<SaveSchuelerCodeAssignmentResult> saveSchuelerCodeAssignment(
      int schuelerId, int schuelerCodeId) {
    return null;
  }

  @Override
  @Transactional
  public void deleteSchuelerCodeAssignment(int schuelerId, SchuelerCode schuelerCode) {}
}
