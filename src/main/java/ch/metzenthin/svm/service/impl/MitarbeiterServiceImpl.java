package ch.metzenthin.svm.service.impl;

import ch.metzenthin.svm.domain.model.MitarbeiterAndMitarbeiterCode;
import ch.metzenthin.svm.domain.model.MitarbeiterAndMitarbeiterCodes;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Code;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
  @Transactional(readOnly = true)
  public List<MitarbeiterAndMitarbeiterCodes> findMitarbeiterAndMitarbeiterCodes(
      Optional<String> nachnameOptional,
      Optional<String> vornameOptional,
      Optional<Boolean> lehrkraftOptional,
      Optional<Boolean> aktivOptional,
      Optional<MitarbeiterCode> mitarbeiterCodeOptional) {

    Optional<Integer> mitarbeiterCodeIdOptional = mitarbeiterCodeOptional.map(Code::getCodeId);

    List<MitarbeiterAndMitarbeiterCode> mitarbeiterAndMitarbeiterCodeList =
        findMitarbeiterAndMitarbeiterCodesOfMitarbeitersWithMitarbeiterCode(
            nachnameOptional,
            vornameOptional,
            lehrkraftOptional,
            aktivOptional,
            mitarbeiterCodeIdOptional);

    List<Mitarbeiter> mitarbeiterWithoutMitarbeiterCodeList =
        findMitarbeitersWithoutMitarbeiterCode(
            nachnameOptional,
            vornameOptional,
            lehrkraftOptional,
            aktivOptional,
            mitarbeiterCodeIdOptional);

    List<MitarbeiterAndMitarbeiterCode>
        mitarbeiterAndMitarbeiterCodeOfMitarbeitersWithoutMitarbeiterCodeList =
            mitarbeiterWithoutMitarbeiterCodeList.stream()
                .map(mitarbeiter -> new MitarbeiterAndMitarbeiterCode(mitarbeiter, null))
                .toList();
    mitarbeiterAndMitarbeiterCodeList.addAll(
        mitarbeiterAndMitarbeiterCodeOfMitarbeitersWithoutMitarbeiterCodeList);

    return convertToMitarbeiterAndMitarbeiterCodesList(mitarbeiterAndMitarbeiterCodeList);
  }

  private List<MitarbeiterAndMitarbeiterCode>
      findMitarbeiterAndMitarbeiterCodesOfMitarbeitersWithMitarbeiterCode(
          Optional<String> nachnameOptional,
          Optional<String> vornameOptional,
          Optional<Boolean> lehrkraftOptional,
          Optional<Boolean> aktivOptional,
          Optional<Integer> mitarbeiterCodeIdOptional) {

    Optional<List<Integer>> mitarbeiterIdsOptional;
    if (mitarbeiterCodeIdOptional.isPresent()) {
      List<Integer> mitarbeiterIds =
          mitarbeiterMitarbeiterCodeRepository.findMitarbeiterIdsByCodeId(
              mitarbeiterCodeIdOptional.get());
      if (mitarbeiterIds.isEmpty()) {
        return List.of();
      }
      mitarbeiterIdsOptional = Optional.of(mitarbeiterIds);
    } else {
      mitarbeiterIdsOptional = Optional.empty();
    }

    return mitarbeiterRepository
        .findMitarbeiterAndMitarbeiterCodesOfMitarbeitersWithMitarbeiterCode(
            nachnameOptional,
            vornameOptional,
            lehrkraftOptional,
            aktivOptional,
            mitarbeiterIdsOptional);
  }

  private List<Mitarbeiter> findMitarbeitersWithoutMitarbeiterCode(
      Optional<String> nachnameOptional,
      Optional<String> vornameOptional,
      Optional<Boolean> lehrkraftOptional,
      Optional<Boolean> aktivOptional,
      Optional<Integer> mitarbeiterCodeIdOptional) {

    if (mitarbeiterCodeIdOptional.isPresent()) {
      return List.of();
    }

    return mitarbeiterRepository.findMitarbeitersWithoutMitarbeiterCode(
        nachnameOptional, vornameOptional, lehrkraftOptional, aktivOptional, Optional.empty());
  }

  private static List<MitarbeiterAndMitarbeiterCodes> convertToMitarbeiterAndMitarbeiterCodesList(
      List<MitarbeiterAndMitarbeiterCode> mitarbeiterAndMitarbeiterCodeList) {

    Map<Integer, Mitarbeiter> mitarbeiterIdAndMitarbeiterMap = new HashMap<>();
    for (MitarbeiterAndMitarbeiterCode mitarbeiterAndMitarbeiterCode :
        mitarbeiterAndMitarbeiterCodeList) {
      if (!mitarbeiterIdAndMitarbeiterMap.containsKey(
          mitarbeiterAndMitarbeiterCode.mitarbeiter().getPersonId())) {
        mitarbeiterIdAndMitarbeiterMap.put(
            mitarbeiterAndMitarbeiterCode.mitarbeiter().getPersonId(),
            mitarbeiterAndMitarbeiterCode.mitarbeiter());
      }
    }

    Map<Integer, List<MitarbeiterCode>> mitarbeiterIdAndMitarbeiterCodesMap = new HashMap<>();
    for (MitarbeiterAndMitarbeiterCode mitarbeiterAndMitarbeiterCode :
        mitarbeiterAndMitarbeiterCodeList) {
      if (mitarbeiterIdAndMitarbeiterCodesMap.containsKey(
          mitarbeiterAndMitarbeiterCode.mitarbeiter().getPersonId())) {
        List<MitarbeiterCode> mitarbeiterCodes =
            mitarbeiterIdAndMitarbeiterCodesMap.get(
                mitarbeiterAndMitarbeiterCode.mitarbeiter().getPersonId());
        if (mitarbeiterAndMitarbeiterCode.mitarbeiterCode() != null) {
          mitarbeiterCodes.add(mitarbeiterAndMitarbeiterCode.mitarbeiterCode());
        }
      } else {
        List<MitarbeiterCode> mitarbeiterCodes = new ArrayList<>();
        if (mitarbeiterAndMitarbeiterCode.mitarbeiterCode() != null) {
          mitarbeiterCodes.add(mitarbeiterAndMitarbeiterCode.mitarbeiterCode());
        }
        mitarbeiterIdAndMitarbeiterCodesMap.put(
            mitarbeiterAndMitarbeiterCode.mitarbeiter().getPersonId(), mitarbeiterCodes);
      }
    }

    // Sortierung MitarbeiterCodes
    for (List<MitarbeiterCode> mitarbeiterCodes : mitarbeiterIdAndMitarbeiterCodesMap.values()) {
      Collections.sort(mitarbeiterCodes);
    }

    return mitarbeiterIdAndMitarbeiterCodesMap.entrySet().stream()
        .map(
            mitarbeiterIdAndMitarbeiterCodesMapEntry ->
                new MitarbeiterAndMitarbeiterCodes(
                    mitarbeiterIdAndMitarbeiterMap.get(
                        mitarbeiterIdAndMitarbeiterCodesMapEntry.getKey()),
                    mitarbeiterIdAndMitarbeiterCodesMapEntry.getValue()))
        .sorted() // Sortierung Mitarbeiter
        .toList();
  }

  @Override
  @Transactional
  public SaveMitarbeiterResult saveMitarbeiter(
      Mitarbeiter mitarbeiter,
      Optional<Adresse> adresseOptional,
      Set<MitarbeiterCode> mitarbeiterCodes) {

    int numberOfAlreadyExistingMitarbeiter =
        (mitarbeiter.getPersonId() != null)
            ? mitarbeiterRepository.countByNachnameAndVornameAndGeburtsdatumAndMitarbeiterIdNe(
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
