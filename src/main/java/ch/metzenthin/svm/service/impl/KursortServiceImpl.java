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
  @Transactional
  public void saveKursort(Kursort kursort) throws EntityAlreadyExistsException {
    long numberOfAlreadyExistingKursorte =
        (kursort.getKursortId() != null)
            ? kursortRepository.countByBezeichnungAndIdNe(
                kursort.getBezeichnung(), kursort.getKursortId())
            : kursortRepository.countByBezeichnung(kursort.getBezeichnung());
    if (numberOfAlreadyExistingKursorte > 0) {
      throw new EntityAlreadyExistsException();
    }
    kursortRepository.save(kursort);
  }

  @Override
  @Transactional
  public void deleteKursort(Kursort kursort) throws EntityStillReferencedException {
    if (kursService.existsKurseByKursortId(kursort.getKursortId())) {
      throw new EntityStillReferencedException();
    }

    kursortRepository.delete(kursort);
  }
}
