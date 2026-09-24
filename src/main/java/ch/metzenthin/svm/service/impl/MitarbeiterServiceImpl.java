package ch.metzenthin.svm.service.impl;

import ch.metzenthin.svm.domain.model.searchfields.MitarbeiterSearchFields;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;
import ch.metzenthin.svm.persistence.entities.MitarbeiterMitarbeiterCode;
import ch.metzenthin.svm.persistence.repository.AdresseRepository;
import ch.metzenthin.svm.persistence.repository.MitarbeiterMitarbeiterCodeRepository;
import ch.metzenthin.svm.persistence.repository.MitarbeiterRepository;
import ch.metzenthin.svm.service.MitarbeiterService;
import ch.metzenthin.svm.service.result.DeleteMitarbeiterResult;
import ch.metzenthin.svm.service.result.SaveMitarbeiterResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Martin Schraner
 */
@Service
public class MitarbeiterServiceImpl implements MitarbeiterService {

  private final MitarbeiterRepository mitarbeiterRepository;
  private final AdresseRepository adresseRepository;
  private final MitarbeiterMitarbeiterCodeRepository mitarbeiterMitarbeiterCodeRepository;

  public MitarbeiterServiceImpl(
      MitarbeiterRepository mitarbeiterRepository,
      AdresseRepository adresseRepository,
      MitarbeiterMitarbeiterCodeRepository mitarbeiterMitarbeiterCodeRepository) {
    this.mitarbeiterRepository = mitarbeiterRepository;
    this.adresseRepository = adresseRepository;
    this.mitarbeiterMitarbeiterCodeRepository = mitarbeiterMitarbeiterCodeRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public List<Mitarbeiter> findAktiveLehrkraefte() {
    return mitarbeiterRepository.findByLehrkraftTrueAndAktivTrueOrderByNachnameVorname();
  }

  @Override
  public List<Mitarbeiter> findMitarbeiterBySearchFields(
      MitarbeiterSearchFields mitarbeiterSearchFields) {
    return mitarbeiterRepository
        .findByNachnameLikeAndVornameLikeAndLehrkraftAndAktivAndCodeIdOrderByNachnameVornameGeburtsdatumAsc(
            mitarbeiterSearchFields.nachname(),
            mitarbeiterSearchFields.vorname(),
            mitarbeiterSearchFields.lehrkraftJaNeinSelected().getValue(),
            mitarbeiterSearchFields.statusSelected().getValue(),
            (mitarbeiterSearchFields.mitarbeiterCode() != null)
                ? mitarbeiterSearchFields.mitarbeiterCode().getCodeId()
                : null);
  }

  @Override
  @Transactional
  public SaveMitarbeiterResult saveMitarbeiter(
      Mitarbeiter mitarbeiter,
      Optional<Adresse> adresseOptional,
      Set<MitarbeiterCode> mitarbeiterCodes) {

    int numberOfAlreadyExistingMitarbeiter =
        (mitarbeiter.getPersonId() != null)
            ? mitarbeiterRepository.countByNachnameAndVornameAndGeburtsdatumAndPersonIdNe(
                mitarbeiter.getNachname(),
                mitarbeiter.getVorname(),
                mitarbeiter.getGeburtsdatum(),
                mitarbeiter.getPersonId())
            : mitarbeiterRepository.countByNachnameAndVornameAndGeburtsdatum(
                mitarbeiter.getNachname(), mitarbeiter.getVorname(), mitarbeiter.getGeburtsdatum());
    if (numberOfAlreadyExistingMitarbeiter > 0) {
      return SaveMitarbeiterResult.MITARBEITER_BEREITS_ERFASST;
    }

    Adresse adresse = adresseOptional.map(adresseRepository::save).orElse(null);
    mitarbeiter.setAdresse(adresse);

    mitarbeiter = mitarbeiterRepository.save(mitarbeiter);

    List<MitarbeiterCode> existingMitarbeiterCodes =
        mitarbeiterMitarbeiterCodeRepository.findMitarbeiterCodesByMitarbeiterId(
            mitarbeiter.getPersonId());

    List<Integer> mitarbeiterCodeIds =
        mitarbeiterCodes.stream().map(MitarbeiterCode::getCodeId).toList();
    List<Integer> codeIdsOfExistingMitarbeiterCodes =
        existingMitarbeiterCodes.stream().map(MitarbeiterCode::getCodeId).toList();

    List<Integer> codeIdsOfMitarbeiterCodesToBeRemoved = new ArrayList<>();
    for (MitarbeiterCode existingMitarbeiterCode : existingMitarbeiterCodes) {
      if (!mitarbeiterCodeIds.contains(existingMitarbeiterCode.getCodeId())) {
        codeIdsOfMitarbeiterCodesToBeRemoved.add(existingMitarbeiterCode.getCodeId());
      }
    }
    if (!codeIdsOfMitarbeiterCodesToBeRemoved.isEmpty()) {
      mitarbeiterMitarbeiterCodeRepository.deleteByMitabeiterIdEqAndMitarbeiterCodeIdIn(
          mitarbeiter.getPersonId(), codeIdsOfMitarbeiterCodesToBeRemoved);
    }

    for (MitarbeiterCode mitarbeiterCode : mitarbeiterCodes) {
      if (!codeIdsOfExistingMitarbeiterCodes.contains(mitarbeiterCode.getCodeId())) {
        MitarbeiterMitarbeiterCode mitarbeiterMitarbeiterCode =
            new MitarbeiterMitarbeiterCode(mitarbeiter, mitarbeiterCode);
        mitarbeiterMitarbeiterCodeRepository.save(mitarbeiterMitarbeiterCode);
      }
    }

    return SaveMitarbeiterResult.SPEICHERN_ERFOLGREICH;
  }

  @Override
  public DeleteMitarbeiterResult deleteMitarbeiter(Mitarbeiter mitarbeiterToBeDeleted) {
    return null;
  }
}
