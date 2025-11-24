package ch.metzenthin.svm.service.impl;

import ch.metzenthin.svm.persistence.repository.SchuelerSchuelerCodeRepository;
import ch.metzenthin.svm.service.SchuelerSchuelerCodeService;
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
}
