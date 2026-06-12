package ch.metzenthin.svm.service.impl;

import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import ch.metzenthin.svm.persistence.repository.KursLehrkraftRepository;
import ch.metzenthin.svm.service.KursLehrkraftService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Martin Schraner
 */
@Service
public class KursLehrkraftServiceImpl implements KursLehrkraftService {

  private final KursLehrkraftRepository kursLehrkraftRepository;

  public KursLehrkraftServiceImpl(KursLehrkraftRepository kursLehrkraftRepository) {
    this.kursLehrkraftRepository = kursLehrkraftRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public List<Mitarbeiter> findSortedLehrkraefteByKursId(int kursId) {
    return kursLehrkraftRepository.findLehrkraefteByKursIdOrderByLehrkraefteOrder(kursId);
  }
}
