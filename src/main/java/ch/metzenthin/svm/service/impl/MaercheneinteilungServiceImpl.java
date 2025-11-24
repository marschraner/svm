package ch.metzenthin.svm.service.impl;

import ch.metzenthin.svm.persistence.repository.MaercheneinteilungRepository;
import ch.metzenthin.svm.service.MaercheneinteilungService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MaercheneinteilungServiceImpl implements MaercheneinteilungService {

  private final MaercheneinteilungRepository maercheneinteilungRepository;

  public MaercheneinteilungServiceImpl(MaercheneinteilungRepository maercheneinteilungRepository) {
    this.maercheneinteilungRepository = maercheneinteilungRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public boolean existsReferencedCodeByCodeId(int codeId) {
    return maercheneinteilungRepository.countByElternmithilfeCodeId(codeId) > 0;
  }
}
