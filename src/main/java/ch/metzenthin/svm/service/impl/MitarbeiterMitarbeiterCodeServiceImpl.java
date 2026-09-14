package ch.metzenthin.svm.service.impl;

import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;
import ch.metzenthin.svm.persistence.repository.MitarbeiterMitarbeiterCodeRepository;
import ch.metzenthin.svm.service.MitarbeiterMitarbeiterCodeService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Hans Stamm
 */
@Service
public class MitarbeiterMitarbeiterCodeServiceImpl implements MitarbeiterMitarbeiterCodeService {

  private final MitarbeiterMitarbeiterCodeRepository mitarbeiterMitarbeiterCodeRepository;

  public MitarbeiterMitarbeiterCodeServiceImpl(
      MitarbeiterMitarbeiterCodeRepository mitarbeiterMitarbeiterCodeRepository) {
    this.mitarbeiterMitarbeiterCodeRepository = mitarbeiterMitarbeiterCodeRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public boolean existsReferencedCodeByCodeId(int codeId) {
    return mitarbeiterMitarbeiterCodeRepository.countByMitarbeiterCodeId(codeId) > 0;
  }

  @Override
  @Transactional(readOnly = true)
  public List<MitarbeiterCode> findMitarbeiterCodesByMitarbeiterId(int mitarbeiterId) {
    return mitarbeiterMitarbeiterCodeRepository.findMitarbeiterCodesByMitarbeiterId(mitarbeiterId);
  }
}
