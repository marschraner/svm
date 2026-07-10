package ch.metzenthin.svm.service.impl;

import ch.metzenthin.svm.persistence.entities.Kursort;
import ch.metzenthin.svm.persistence.repository.KursortRepository;
import ch.metzenthin.svm.service.KursService;
import ch.metzenthin.svm.service.KursortService;
import ch.metzenthin.svm.service.result.DeleteKursortResult;
import ch.metzenthin.svm.service.result.SaveKursortResult;
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
  @Transactional(readOnly = true)
  public List<Kursort> findAllKursorte() {
    return kursortRepository.findAllOrderByBezeichnung();
  }

  @Override
  @Transactional(readOnly = true)
  public List<Kursort> findSelektierbareKursorte() {
    return kursortRepository.findBySelektierbarTrueOrderByBezeichnung();
  }

  @Override
  @Transactional
  public SaveKursortResult saveKursort(Kursort kursort) {
    long numberOfAlreadyExistingKursorte =
        getNumberOfAlreadyExistingKursorte(kursort.getKursortId(), kursort.getBezeichnung());
    if (numberOfAlreadyExistingKursorte > 0) {
      return SaveKursortResult.KURSORT_BEREITS_ERFASST;
    }

    kursortRepository.save(kursort);
    return SaveKursortResult.SPEICHERN_ERFOLGREICH;
  }

  @Override
  @Transactional
  public DeleteKursortResult deleteKursort(Kursort kursort) {
    if (kursService.existsKursByKursortId(kursort.getKursortId())) {
      return DeleteKursortResult.KURSORT_VON_KURS_REFERENZIERT;
    }

    kursortRepository.delete(kursort);
    return DeleteKursortResult.LOESCHEN_ERFOLGREICH;
  }
}
