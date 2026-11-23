package ch.metzenthin.svm.service.impl;

import ch.metzenthin.svm.persistence.entities.Schueler;
import ch.metzenthin.svm.persistence.entities.SchuelerCode;
import ch.metzenthin.svm.persistence.entities.SchuelerSchuelerCode;
import ch.metzenthin.svm.persistence.repository.SchuelerCodeRepository;
import ch.metzenthin.svm.persistence.repository.SchuelerRepository;
import ch.metzenthin.svm.persistence.repository.SchuelerSchuelerCodeRepository;
import ch.metzenthin.svm.service.SchuelerSchuelerCodeService;
import ch.metzenthin.svm.service.result.DeleteAssignmentResult;
import ch.metzenthin.svm.service.result.SaveResultAndIdOfSavedEntity;
import ch.metzenthin.svm.service.result.SaveSchuelerCodeAssignmentResult;
import java.util.Collections;
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
  private final SchuelerRepository schuelerRepository;
  private final SchuelerCodeRepository schuelerCodeRepository;

  public SchuelerSchuelerCodeServiceImpl(
      SchuelerSchuelerCodeRepository schuelerSchuelerCodeRepository,
      SchuelerRepository schuelerRepository,
      SchuelerCodeRepository schuelerCodeRepository) {
    this.schuelerSchuelerCodeRepository = schuelerSchuelerCodeRepository;
    this.schuelerRepository = schuelerRepository;
    this.schuelerCodeRepository = schuelerCodeRepository;
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
    return schuelerSchuelerCodeRepository.findAssignedSchuelerCodeBySchuelerIdAndSchuelerCodeId(
        schuelerId, schuelerCodeId);
  }

  @Override
  @Transactional(readOnly = true)
  public List<SchuelerCode> findAssignedSchuelerCodesBySchuelerId(int schuelerId) {
    List<SchuelerCode> schuelerCodes =
        schuelerSchuelerCodeRepository.findAssignedSchuelerCodesBySchuelerId(schuelerId);
    Collections.sort(schuelerCodes);
    return schuelerCodes;
  }

  @Override
  @Transactional(readOnly = true)
  public List<SchuelerCode> findNotAssignedSelektierbareSchuelerCodesBySchuelerId(int schuelerId) {
    List<SchuelerCode> schuelerCodes =
        schuelerSchuelerCodeRepository.findNotAssignedSelektierbareSchuelerCodesBySchuelerId(
            schuelerId);
    Collections.sort(schuelerCodes);
    return schuelerCodes;
  }

  @Override
  @Transactional
  public SaveResultAndIdOfSavedEntity<SaveSchuelerCodeAssignmentResult> saveSchuelerCodeAssignment(
      int schuelerId, int schuelerCodeId) {

    Optional<SchuelerSchuelerCode> schuelerSchuelerCodeOptional =
        schuelerSchuelerCodeRepository.findBySchuelerIdAndSchuelerCodeId(
            schuelerId, schuelerCodeId);
    if (schuelerSchuelerCodeOptional.isPresent()) {
      return new SaveResultAndIdOfSavedEntity<>(
          SaveSchuelerCodeAssignmentResult.SPEICHERN_ERFOLGREICH,
          schuelerSchuelerCodeOptional.get().getSchuelerCode().getCodeId());
    }

    Optional<Schueler> schuelerOptional = schuelerRepository.findById(schuelerId);
    if (schuelerOptional.isEmpty()) {
      return new SaveResultAndIdOfSavedEntity<>(
          SaveSchuelerCodeAssignmentResult.SCHUELER_GELOESCHT, -1);
    }

    Optional<SchuelerCode> schuelerCodeOptional = schuelerCodeRepository.findById(schuelerCodeId);
    if (schuelerCodeOptional.isEmpty()) {
      return new SaveResultAndIdOfSavedEntity<>(
          SaveSchuelerCodeAssignmentResult.CODE_GELOESCHT, -1);
    }

    SchuelerSchuelerCode schuelerSchuelerCode = new SchuelerSchuelerCode();
    schuelerSchuelerCode.setSchueler(schuelerOptional.get());
    schuelerSchuelerCode.setSchuelerCode(schuelerCodeOptional.get());
    schuelerSchuelerCode = schuelerSchuelerCodeRepository.save(schuelerSchuelerCode);

    return new SaveResultAndIdOfSavedEntity<>(
        SaveSchuelerCodeAssignmentResult.SPEICHERN_ERFOLGREICH,
        schuelerSchuelerCode.getSchuelerCode().getCodeId());
  }

  @Override
  @Transactional
  public DeleteAssignmentResult deleteSchuelerCodeAssignment(
      int schuelerId, SchuelerCode schuelerCode) {

    schuelerSchuelerCodeRepository.deleteBySchuelerIdAndSchuelerCodeId(
        schuelerId, schuelerCode.getCodeId());

    return DeleteAssignmentResult.LOESCHEN_ERFOLGREICH;
  }
}
