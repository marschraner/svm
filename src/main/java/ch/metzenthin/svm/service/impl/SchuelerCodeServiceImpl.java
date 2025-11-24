package ch.metzenthin.svm.service.impl;

import ch.metzenthin.svm.persistence.entities.SchuelerCode;
import ch.metzenthin.svm.persistence.repository.SchuelerCodeRepository;
import ch.metzenthin.svm.service.SchuelerCodeService;
import ch.metzenthin.svm.service.SchuelerSchuelerCodeService;
import org.springframework.stereotype.Service;

/**
 * @author Hans Stamm
 */
@Service
public class SchuelerCodeServiceImpl extends AbstractCodeServiceImpl<SchuelerCode>
    implements SchuelerCodeService {

  public SchuelerCodeServiceImpl(
      SchuelerSchuelerCodeService schuelerSchuelerCodeService,
      SchuelerCodeRepository schuelerCodeRepository) {
    super(schuelerSchuelerCodeService, schuelerCodeRepository);
  }
}
