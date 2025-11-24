package ch.metzenthin.svm.service.impl;

import ch.metzenthin.svm.persistence.entities.ElternmithilfeCode;
import ch.metzenthin.svm.persistence.repository.ElternmithilfeCodeRepository;
import ch.metzenthin.svm.service.ElternmithilfeCodeService;
import ch.metzenthin.svm.service.MaercheneinteilungService;
import org.springframework.stereotype.Service;

/**
 * @author Hans Stamm
 */
@Service
public class ElternmithilfeCodeServiceImpl extends AbstractCodeServiceImpl<ElternmithilfeCode>
    implements ElternmithilfeCodeService {

  public ElternmithilfeCodeServiceImpl(
      MaercheneinteilungService maercheneinteilungService,
      ElternmithilfeCodeRepository elternmithilfeCodeRepository) {
    super(maercheneinteilungService, elternmithilfeCodeRepository);
  }
}
