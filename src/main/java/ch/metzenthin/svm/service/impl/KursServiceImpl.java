package ch.metzenthin.svm.service.impl;

import ch.metzenthin.svm.persistence.repository.KursRepository;
import ch.metzenthin.svm.service.KursService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Martin Schraner
 */
@Service
public class KursServiceImpl implements KursService {

  private final KursRepository kursRepository;

  public KursServiceImpl(KursRepository kursRepository) {
    this.kursRepository = kursRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public boolean existsKurseByKursortId(int kursortId) {
    return kursRepository.countByKursortId(kursortId) > 0;
  }
}
