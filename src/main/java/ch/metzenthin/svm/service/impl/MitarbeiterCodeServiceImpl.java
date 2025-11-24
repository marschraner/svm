package ch.metzenthin.svm.service.impl;

import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;
import ch.metzenthin.svm.persistence.repository.MitarbeiterCodeRepository;
import ch.metzenthin.svm.service.MitarbeiterCodeService;
import ch.metzenthin.svm.service.MitarbeiterMitarbeiterCodeService;
import org.springframework.stereotype.Service;

/**
 * @author Hans Stamm
 */
@Service
public class MitarbeiterCodeServiceImpl extends AbstractCodeServiceImpl<MitarbeiterCode>
    implements MitarbeiterCodeService {

  public MitarbeiterCodeServiceImpl(
      MitarbeiterMitarbeiterCodeService mitarbeiterMitarbeiterCodeService,
      MitarbeiterCodeRepository mitarbeiterCodeRepository) {
    super(mitarbeiterMitarbeiterCodeService, mitarbeiterCodeRepository);
  }
}
