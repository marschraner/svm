package ch.metzenthin.svm.service.impl;

import ch.metzenthin.svm.persistence.repository.SemesterrechnungRepository;
import ch.metzenthin.svm.service.SemesterrechnungService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SemesterrechnungServiceImpl implements SemesterrechnungService {

  private final SemesterrechnungRepository semesterrechnungRepository;

  public SemesterrechnungServiceImpl(SemesterrechnungRepository semesterrechnungRepository) {
    this.semesterrechnungRepository = semesterrechnungRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public boolean existsReferencedCodeByCodeId(int codeId) {
    return semesterrechnungRepository.countBySemesterrechnungCodeId(codeId) > 0;
  }
}
