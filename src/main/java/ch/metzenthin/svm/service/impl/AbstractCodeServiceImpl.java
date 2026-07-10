package ch.metzenthin.svm.service.impl;

import ch.metzenthin.svm.persistence.entities.Code;
import ch.metzenthin.svm.persistence.repository.CodeRepository;
import ch.metzenthin.svm.service.CodeService;
import ch.metzenthin.svm.service.ReferencedCodeService;
import ch.metzenthin.svm.service.result.DeleteCodeResult;
import ch.metzenthin.svm.service.result.SaveCodeResult;
import java.util.List;
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
  public List<T> findAllCodes() {
    return codeRepository.findAllOrderByKuerzel();
  }

  @Override
  @Transactional
  public SaveCodeResult saveCode(T code) {
    long numberOfAlreadyExistingCodes =
        getNumberOfAlreadyExistingKuerzel(code.getCodeId(), code.getKuerzel());
    if (numberOfAlreadyExistingCodes > 0) {
      return SaveCodeResult.CODE_BEREITS_ERFASST;
    }

    codeRepository.save(code);
    return SaveCodeResult.SPEICHERN_ERFOLGREICH;
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
