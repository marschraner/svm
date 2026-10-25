package ch.metzenthin.svm.service.impl;

import ch.metzenthin.svm.persistence.entities.Code;
import ch.metzenthin.svm.persistence.repository.CodeRepository;
import ch.metzenthin.svm.service.CodeService;
import ch.metzenthin.svm.service.ReferencedCodeService;
import ch.metzenthin.svm.service.result.DeleteCodeResult;
import ch.metzenthin.svm.service.result.SaveCodeResult;
import ch.metzenthin.svm.service.result.SaveResultAndIdOfSavedEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Hans Stamm
 */
public abstract class AbstractCodeServiceImpl<T extends Code> implements CodeService<T> {

  private final ReferencedCodeService referencedCodeService;
  private final CodeRepository<T> codeRepository;

  protected AbstractCodeServiceImpl(
      ReferencedCodeService referencedCodeService, CodeRepository<T> codeRepository) {
    this.referencedCodeService = referencedCodeService;
    this.codeRepository = codeRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public boolean doesKuerzelAlreadyExist(Integer codeId, String kuerzel) {
    long numberOfAlreadyExistingKuerzel = getNumberOfAlreadyExistingKuerzel(codeId, kuerzel);
    return numberOfAlreadyExistingKuerzel > 0;
  }

  private int getNumberOfAlreadyExistingKuerzel(Integer codeId, String kuerzel) {
    return (codeId != null)
        ? codeRepository.countByKuerzelAndIdNe(kuerzel, codeId)
        : codeRepository.countByKuerzel(kuerzel);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<T> findCodeById(int codeId) {
    return codeRepository.findById(codeId);
  }

  @Override
  @Transactional(readOnly = true)
  public List<T> findAllCodes() {
    return codeRepository.findAllOrderByKuerzel();
  }

  @Override
  @Transactional(readOnly = true)
  public List<T> findAllSelectableCodes() {
    return codeRepository.findAllSelectableOrderByKuerzel();
  }

  @Override
  @Transactional
  public SaveResultAndIdOfSavedEntity<SaveCodeResult> saveCode(T code) {
    long numberOfAlreadyExistingCodes =
        getNumberOfAlreadyExistingKuerzel(code.getCodeId(), code.getKuerzel());
    if (numberOfAlreadyExistingCodes > 0) {
      return new SaveResultAndIdOfSavedEntity<>(SaveCodeResult.CODE_BEREITS_ERFASST, -1);
    }

    code = codeRepository.save(code);
    return new SaveResultAndIdOfSavedEntity<>(
        SaveCodeResult.SPEICHERN_ERFOLGREICH, code.getCodeId());
  }

  @Override
  @Transactional
  public DeleteCodeResult deleteCode(T code) {
    if (referencedCodeService.existsReferencedCodeByCodeId(code.getCodeId())) {
      return DeleteCodeResult.CODE_REFERENZIERT;
    }

    codeRepository.delete(code);
    return DeleteCodeResult.LOESCHEN_ERFOLGREICH;
  }
}
