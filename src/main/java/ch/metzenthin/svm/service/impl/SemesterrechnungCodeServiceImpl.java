package ch.metzenthin.svm.service.impl;

import ch.metzenthin.svm.persistence.entities.SemesterrechnungCode;
import ch.metzenthin.svm.persistence.repository.SemesterrechnungCodeRepository;
import ch.metzenthin.svm.service.SemesterrechnungCodeService;
import ch.metzenthin.svm.service.SemesterrechnungService;
import org.springframework.stereotype.Service;

/**
 * @author Hans Stamm
 */
@Service
public class SemesterrechnungCodeServiceImpl extends AbstractCodeServiceImpl<SemesterrechnungCode>
    implements SemesterrechnungCodeService {

  public SemesterrechnungCodeServiceImpl(
      SemesterrechnungService semesterrechnungService,
      SemesterrechnungCodeRepository semesterrechnungCodeRepository) {
    super(semesterrechnungService, semesterrechnungCodeRepository);
  }
}
