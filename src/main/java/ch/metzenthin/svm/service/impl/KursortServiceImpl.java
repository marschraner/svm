package ch.metzenthin.svm.service.impl;

import ch.metzenthin.svm.persistence.entities.Kursort;
import ch.metzenthin.svm.persistence.repository.KursortRepository;
import ch.metzenthin.svm.service.KursortService;
import ch.metzenthin.svm.service.result.DeleteKursortResult;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Martin Schraner
 */
@Service
public class KursortServiceImpl implements KursortService {

  private final KursortRepository kursortRepository;

  public KursortServiceImpl(KursortRepository kursortRepository) {
    this.kursortRepository = kursortRepository;
  }

  @Transactional(readOnly = true)
  @Override
  public boolean checkIfAlreadyExists(Kursort kursort) {
    return kursortRepository.countByBezeichnung(kursort.getBezeichnung()) > 0;
  }

  @Transactional(readOnly = true)
  @Override
  public List<Kursort> findAllKursorte() {
    return kursortRepository.findAllOrderByBezeichnung();
  }

  @Transactional
  @Override
  public void saveKursort(Kursort kursort) {
    kursortRepository.save(kursort);
  }

  @Transactional
  @Override
  public DeleteKursortResult deleteKursort(Kursort kursort) {
    return DeleteKursortResult.KURSORT_VON_KURS_REFERENZIERT;
  }
}
