package ch.metzenthin.svm.service.impl;

import ch.metzenthin.svm.domain.EntityAlreadyExistsException;
import ch.metzenthin.svm.persistence.entities.Kurstyp;
import ch.metzenthin.svm.persistence.repository.KurstypRepository;
import ch.metzenthin.svm.service.KursService;
import ch.metzenthin.svm.service.KurstypService;
import ch.metzenthin.svm.service.result.DeleteKurstypResult;
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
  public List<Kurstyp> findAllKurstypen() {
    return kurstypRepository.findAllOrderByBezeichnung();
  }

  @Override
  @Transactional
  public void saveKurstyp(Kurstyp kurstyp) throws EntityAlreadyExistsException {
    long numberOfAlreadyExistingKurstypen =
        (kurstyp.getKurstypId() != null)
            ? kurstypRepository.countByBezeichnungAndIdNe(
                kurstyp.getBezeichnung(), kurstyp.getKurstypId())
            : kurstypRepository.countByBezeichnung(kurstyp.getBezeichnung());
    if (numberOfAlreadyExistingKurstypen > 0) {
      throw new EntityAlreadyExistsException();
    }
    kurstypRepository.save(kurstyp);
  }

  @Override
  @Transactional
  public DeleteKurstypResult deleteKurstyp(Kurstyp kurstyp) {
    if (kursService.existsKurseByKurstypId(kurstyp.getKurstypId())) {
      return DeleteKurstypResult.KURSTYP_VON_KURS_REFERENZIERT;
    }

    kurstypRepository.delete(kurstyp);
    return DeleteKurstypResult.LOESCHEN_ERFOLGREICH;
  }
}
