package ch.metzenthin.svm.service.impl;

import ch.metzenthin.svm.domain.EntityAlreadyExistsException;
import ch.metzenthin.svm.domain.EntityStillReferencedException;
import ch.metzenthin.svm.persistence.entities.Kurstyp;
import ch.metzenthin.svm.persistence.repository.KurstypRepository;
import ch.metzenthin.svm.service.KursService;
import ch.metzenthin.svm.service.KurstypService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Hans Stamm
 */
@Service
public class KurstypServiceImpl implements KurstypService {

  private final KursService kursService;
  private final KurstypRepository kurstypRepository;

  public KurstypServiceImpl(KursService kursService, KurstypRepository kurstypRepository) {
    this.kursService = kursService;
    this.kurstypRepository = kurstypRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public boolean doesKurstypAlreadyExist(Integer kurstypId, String kurstypBezeichnung) {
    long numberOfAlreadyExistingKurstypen =
        getNumberOfAlreadyExistingKurstypen(kurstypId, kurstypBezeichnung);
    return numberOfAlreadyExistingKurstypen > 0;
  }

  private long getNumberOfAlreadyExistingKurstypen(Integer kurstypId, String kurstypBezeichnung) {
    return (kurstypId != null)
        ? kurstypRepository.countByBezeichnungAndIdNe(kurstypBezeichnung, kurstypId)
        : kurstypRepository.countByBezeichnung(kurstypBezeichnung);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Kurstyp> findAllKurstypen() {
    return kurstypRepository.findAllOrderByBezeichnung();
  }

  @Override
  @Transactional(readOnly = true)
  public List<Kurstyp> findSelektierbareKurstypen() {
    return kurstypRepository.findBySelektierbarTrueOrderByBezeichnung();
  }

  @Override
  @Transactional
  public void saveKurstyp(Kurstyp kurstyp) throws EntityAlreadyExistsException {
    long numberOfAlreadyExistingKurstypen =
        getNumberOfAlreadyExistingKurstypen(kurstyp.getKurstypId(), kurstyp.getBezeichnung());
    if (numberOfAlreadyExistingKurstypen > 0) {
      throw new EntityAlreadyExistsException();
    }
    kurstypRepository.save(kurstyp);
  }

  @Override
  @Transactional
  public void deleteKurstyp(Kurstyp kurstyp) throws EntityStillReferencedException {
    if (kursService.existsKursByKurstypId(kurstyp.getKurstypId())) {
      throw new EntityStillReferencedException();
    }

    kurstypRepository.delete(kurstyp);
  }
}
