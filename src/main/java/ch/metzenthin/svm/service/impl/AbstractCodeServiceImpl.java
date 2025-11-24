package ch.metzenthin.svm.service.impl;

import ch.metzenthin.svm.domain.EntityAlreadyExistsException;
import ch.metzenthin.svm.domain.EntityStillReferencedException;
import ch.metzenthin.svm.persistence.entities.Code;
import ch.metzenthin.svm.persistence.repository.CodeRepository;
import ch.metzenthin.svm.service.CodeService;
import ch.metzenthin.svm.service.ReferencedCodeService;
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
  public List<T> findAllCodes() {
    return codeRepository.findAllOrderByKuerzel();
  }

  @Override
  @Transactional
  public void saveCode(T code) throws EntityAlreadyExistsException {
    long numberOfAlreadyExistingCodes =
        (code.getCodeId() != null)
            ? codeRepository.countByKuerzelAndIdNe(code.getKuerzel(), code.getCodeId())
            : codeRepository.countByKuerzel(code.getKuerzel());
    if (numberOfAlreadyExistingCodes > 0) {
      throw new EntityAlreadyExistsException();
    }
    codeRepository.save(code);
  }

  @Override
  @Transactional
  public void deleteCode(T code) throws EntityStillReferencedException {
    if (referencedCodeService.existsReferencedCodeByCodeId(code.getCodeId())) {
      throw new EntityStillReferencedException();
    }

    codeRepository.delete(code);
  }
}
