package ch.metzenthin.svm.service.impl;

import ch.metzenthin.svm.domain.EntityAlreadyExistsException;
import ch.metzenthin.svm.domain.EntityStillReferencedException;
import ch.metzenthin.svm.persistence.entities.Lektionsgebuehren;
import ch.metzenthin.svm.persistence.repository.LektionsgebuehrenRepository;
import ch.metzenthin.svm.service.KursService;
import ch.metzenthin.svm.service.LektionsgebuehrenService;
import java.util.List;
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
  public List<Lektionsgebuehren> findAllLektionsgebuehren() {
    return lektionsgebuehrenRepository.findAllOrderByLektionslaenge();
  }

  @Override
  @Transactional
  public void saveLektionsgebuehren(Lektionsgebuehren lektionsgebuehren)
      throws EntityAlreadyExistsException {
    long numberOfAlreadyExistingLektionsgebuehren =
        (lektionsgebuehren.getId() != null)
            ? lektionsgebuehrenRepository.countByLektionslaengeAndIdNe(
                lektionsgebuehren.getLektionslaenge(), lektionsgebuehren.getId())
            : lektionsgebuehrenRepository.countByLektionslaenge(
                lektionsgebuehren.getLektionslaenge());
    if (numberOfAlreadyExistingLektionsgebuehren > 0) {
      throw new EntityAlreadyExistsException();
    }
    lektionsgebuehrenRepository.save(lektionsgebuehren);
  }

  @Override
  @Transactional
  public void deleteLektionsgebuehren(Lektionsgebuehren lektionsgebuehren)
      throws EntityStillReferencedException {
    if (kursService.existsKursByLektionslaenge(lektionsgebuehren.getLektionslaenge())) {
      throw new EntityStillReferencedException();
    }

    lektionsgebuehrenRepository.delete(lektionsgebuehren);
  }
}
