package ch.metzenthin.svm.service.impl;

import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import ch.metzenthin.svm.persistence.repository.MitarbeiterRepository;
import ch.metzenthin.svm.service.MitarbeiterService;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * @author Martin Schraner
 */
@Service
public class MitarbeiterServiceImpl implements MitarbeiterService {

  private final MitarbeiterRepository mitarbeiterRepository;

  public MitarbeiterServiceImpl(MitarbeiterRepository mitarbeiterRepository) {
    this.mitarbeiterRepository = mitarbeiterRepository;
  }

  @Override
  public List<Mitarbeiter> findAktiveLehrkraefte() {
    return mitarbeiterRepository.findByLehrkraftTrueAndAktivTrueOrderByNachnameVorname();
  }
}
