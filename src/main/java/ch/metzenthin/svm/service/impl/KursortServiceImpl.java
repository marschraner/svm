package ch.metzenthin.svm.service.impl;

import ch.metzenthin.svm.domain.EntityAlreadyExistsException;
import ch.metzenthin.svm.domain.EntityStillReferencedException;
import ch.metzenthin.svm.persistence.entities.Kursort;
import ch.metzenthin.svm.persistence.repository.KursortRepository;
import ch.metzenthin.svm.service.KursService;
import ch.metzenthin.svm.service.KursortService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Martin Schraner
 */
@Service
public class KursortServiceImpl implements KursortService {

  private final KursService kursService;
  private final KursortRepository kursortRepository;

  public KursortServiceImpl(KursService kursService, KursortRepository kursortRepository) {
    this.kursService = kursService;
    this.kursortRepository = kursortRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public List<Kursort> findAllKursorte() {
    return kursortRepository.findAllOrderByBezeichnung();
  }

  @Override
  @Transactional(readOnly = true)
  public boolean doesKursortAlreadyExist(Integer kursortId, String kursortBezeichnung) {
    long numberOfAlreadyExistingKursorte =
        getNumberOfAlreadyExistingKursorte(kursortId, kursortBezeichnung);
    return numberOfAlreadyExistingKursorte > 0;
  }

  private long getNumberOfAlreadyExistingKursorte(Integer kursortId, String kursortBezeichnung) {
    return (kursortId != null)
        ? kursortRepository.countByBezeichnungAndIdNe(kursortBezeichnung, kursortId)
        : kursortRepository.countByBezeichnung(kursortBezeichnung);
  }

  @Override
  @Transactional
  public void saveKursort(Kursort kursort) throws EntityAlreadyExistsException {
    long numberOfAlreadyExistingKursorte =
        getNumberOfAlreadyExistingKursorte(kursort.getKursortId(), kursort.getBezeichnung());
    if (numberOfAlreadyExistingKursorte > 0) {
      throw new EntityAlreadyExistsException();
    }
    kursortRepository.save(kursort);
  }

  @Override
  @Transactional
  public void deleteKursort(Kursort kursort) throws EntityStillReferencedException {
    if (kursService.existsKursByKursortId(kursort.getKursortId())) {
      throw new EntityStillReferencedException();
    }

    kursortRepository.delete(kursort);
  }
}
