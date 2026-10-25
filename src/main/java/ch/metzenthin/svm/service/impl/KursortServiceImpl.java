package ch.metzenthin.svm.service.impl;

import ch.metzenthin.svm.persistence.entities.Kursort;
import ch.metzenthin.svm.persistence.repository.KursortRepository;
import ch.metzenthin.svm.service.KursService;
import ch.metzenthin.svm.service.KursortService;
import ch.metzenthin.svm.service.result.DeleteKursortResult;
import ch.metzenthin.svm.service.result.SaveKursortResult;
import ch.metzenthin.svm.service.result.SaveResultAndIdOfSavedEntity;
import java.util.List;
import java.util.Optional;
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
  public Optional<Kursort> findKursortById(int kursortId) {
    return kursortRepository.findById(kursortId);
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
  public SaveResultAndIdOfSavedEntity<SaveKursortResult> saveKursort(Kursort kursort) {
    long numberOfAlreadyExistingKursorte =
        getNumberOfAlreadyExistingKursorte(kursort.getKursortId(), kursort.getBezeichnung());
    if (numberOfAlreadyExistingKursorte > 0) {
      return new SaveResultAndIdOfSavedEntity<>(SaveKursortResult.KURSORT_BEREITS_ERFASST, -1);
    }

    kursort = kursortRepository.save(kursort);
    return new SaveResultAndIdOfSavedEntity<>(
        SaveKursortResult.SPEICHERN_ERFOLGREICH, kursort.getKursortId());
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
