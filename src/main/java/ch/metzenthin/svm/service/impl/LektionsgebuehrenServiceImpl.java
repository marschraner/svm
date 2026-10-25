package ch.metzenthin.svm.service.impl;

import ch.metzenthin.svm.persistence.entities.Lektionsgebuehren;
import ch.metzenthin.svm.persistence.repository.LektionsgebuehrenRepository;
import ch.metzenthin.svm.service.KursService;
import ch.metzenthin.svm.service.LektionsgebuehrenService;
import ch.metzenthin.svm.service.result.DeleteLektionsgebuehrenResult;
import ch.metzenthin.svm.service.result.SaveLektionsgebuehrenResult;
import ch.metzenthin.svm.service.result.SaveResultAndIdOfSavedEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Hans Stamm
 */
@Service
public class LektionsgebuehrenServiceImpl implements LektionsgebuehrenService {

  private final KursService kursService;
  private final LektionsgebuehrenRepository lektionsgebuehrenRepository;

  public LektionsgebuehrenServiceImpl(
      KursService kursService, LektionsgebuehrenRepository lektionsgebuehrenRepository) {
    this.kursService = kursService;
    this.lektionsgebuehrenRepository = lektionsgebuehrenRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Lektionsgebuehren> findLektionsgebuehrenById(int lektionsgebuehrenId) {
    return lektionsgebuehrenRepository.findById(lektionsgebuehrenId);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Lektionsgebuehren> findAllLektionsgebuehren() {
    return lektionsgebuehrenRepository.findAllOrderByLektionslaenge();
  }

  @Override
  @Transactional
  public SaveResultAndIdOfSavedEntity<SaveLektionsgebuehrenResult> saveLektionsgebuehren(
      Lektionsgebuehren lektionsgebuehren) {
    long numberOfAlreadyExistingLektionsgebuehren =
        (lektionsgebuehren.getId() != null)
            ? lektionsgebuehrenRepository.countByLektionslaengeAndIdNe(
                lektionsgebuehren.getLektionslaenge(), lektionsgebuehren.getId())
            : lektionsgebuehrenRepository.countByLektionslaenge(
                lektionsgebuehren.getLektionslaenge());
    if (numberOfAlreadyExistingLektionsgebuehren > 0) {
      return new SaveResultAndIdOfSavedEntity<>(
          SaveLektionsgebuehrenResult.LEKTIONSGEBUEHREN_BEREITS_ERFASST, -1);
    }

    lektionsgebuehren = lektionsgebuehrenRepository.save(lektionsgebuehren);
    return new SaveResultAndIdOfSavedEntity<>(
        SaveLektionsgebuehrenResult.SPEICHERN_ERFOLGREICH, lektionsgebuehren.getId());
  }

  @Override
  @Transactional
  public DeleteLektionsgebuehrenResult deleteLektionsgebuehren(
      Lektionsgebuehren lektionsgebuehren) {
    if (kursService.existsKursByLektionslaenge(lektionsgebuehren.getLektionslaenge())) {
      return DeleteLektionsgebuehrenResult.LEKTIONSGEBUEHREN_VON_KURS_REFERENZIERT;
    }

    lektionsgebuehrenRepository.delete(lektionsgebuehren);
    return DeleteLektionsgebuehrenResult.LOESCHEN_ERFOLGREICH;
  }
}
