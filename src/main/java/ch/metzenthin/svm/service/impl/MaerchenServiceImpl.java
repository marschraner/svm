package ch.metzenthin.svm.service.impl;

import ch.metzenthin.svm.common.datatypes.Schuljahre;
import ch.metzenthin.svm.domain.model.IdAndCount;
import ch.metzenthin.svm.domain.model.MaerchenAndNumberOfMaercheneinteilungen;
import ch.metzenthin.svm.persistence.entities.Maerchen;
import ch.metzenthin.svm.persistence.repository.MaerchenRepository;
import ch.metzenthin.svm.persistence.repository.MaercheneinteilungRepository;
import ch.metzenthin.svm.service.MaerchenService;
import ch.metzenthin.svm.service.MaercheneinteilungService;
import ch.metzenthin.svm.service.result.DeleteMaerchenResult;
import ch.metzenthin.svm.service.result.SaveMaerchenResult;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Martin Schraner
 */
@Service
public class MaerchenServiceImpl implements MaerchenService {

  private final MaercheneinteilungService maercheneinteilungService;
  private final MaerchenRepository maerchenRepository;
  private final MaercheneinteilungRepository maercheneinteilungRepository;

  public MaerchenServiceImpl(
      MaercheneinteilungService maercheneinteilungService,
      MaerchenRepository maerchenRepository,
      MaercheneinteilungRepository maercheneinteilungRepository) {
    this.maercheneinteilungService = maercheneinteilungService;
    this.maerchenRepository = maerchenRepository;
    this.maercheneinteilungRepository = maercheneinteilungRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public List<MaerchenAndNumberOfMaercheneinteilungen>
      findAllMaerchenAndNumberOfMaercheneinteilungen() {

    List<IdAndCount> maerchenIdAndNumberOfMaercheneinteilungenList =
        maercheneinteilungRepository.countMaercheneinteilungenGroupByMaerchenId();
    Map<Integer, Long> maerchenIdsAndNumberOfMaercheneinteilungenAsMap =
        maerchenIdAndNumberOfMaercheneinteilungenList.stream()
            .collect(Collectors.toMap(IdAndCount::id, IdAndCount::count));

    List<Maerchen> maerchenList = doFindAllMaerchen();
    List<MaerchenAndNumberOfMaercheneinteilungen> maerchenAndNumberOfMaercheneinteilungenList =
        new ArrayList<>();
    for (Maerchen maerchen : maerchenList) {
      long numberOfMaercheneinteilungen =
          maerchenIdsAndNumberOfMaercheneinteilungenAsMap.getOrDefault(
              maerchen.getMaerchenId(), 0L);
      MaerchenAndNumberOfMaercheneinteilungen maerchenAndNumberOfMaercheneinteilungen =
          new MaerchenAndNumberOfMaercheneinteilungen(maerchen, numberOfMaercheneinteilungen);
      maerchenAndNumberOfMaercheneinteilungenList.add(maerchenAndNumberOfMaercheneinteilungen);
    }
    return maerchenAndNumberOfMaercheneinteilungenList;
  }

  private List<Maerchen> doFindAllMaerchen() {
    return maerchenRepository.findAllOrderBySchuljahrDesc();
  }

  @SuppressWarnings("DuplicatedCode")
  @Override
  @Transactional(readOnly = true)
  public String findNaechstesNochErfasstesSchuljahr() {
    List<Maerchen> erfassteMaerchen = doFindAllMaerchen();
    Calendar today = new GregorianCalendar();
    int schuljahr1;
    if (today.get(Calendar.MONTH) <= Calendar.MAY) {
      schuljahr1 = today.get(Calendar.YEAR) - 1;
    } else {
      schuljahr1 = today.get(Calendar.YEAR);
    }
    int schuljahr2 = schuljahr1 + 1;
    String naechstesSchuljahr = schuljahr1 + "/" + schuljahr2;
    while (isMaerchenBereitsErfasst(naechstesSchuljahr, erfassteMaerchen)
        && schuljahr1 < Schuljahre.SCHULJAHR_VALID_MAX) {
      schuljahr1++;
      schuljahr2++;
      naechstesSchuljahr = schuljahr1 + "/" + schuljahr2;
    }
    return naechstesSchuljahr;
  }

  @Override
  @Transactional(readOnly = true)
  public boolean existsMaerchenForSchuljahr(Integer maerchenId, String schuljahr) {
    int numberOfAlreadyExistingMaerchenInSchuljahr =
        getNumberOfAlreadyExistingMaerchenInSchuljahr(maerchenId, schuljahr);
    return numberOfAlreadyExistingMaerchenInSchuljahr > 0;
  }

  private int getNumberOfAlreadyExistingMaerchenInSchuljahr(Integer maerchenId, String schuljahr) {
    return (maerchenId != null)
        ? maerchenRepository.countBySchuljahrAndIdNe(schuljahr, maerchenId)
        : maerchenRepository.countBySchuljahr(schuljahr);
  }

  private static boolean isMaerchenBereitsErfasst(
      String naechstesSchuljahr, List<Maerchen> erfassteMaerchen) {
    return erfassteMaerchen.stream()
        .anyMatch(maerchen -> maerchen.getSchuljahr().equals(naechstesSchuljahr));
  }

  @Override
  @Transactional
  public SaveMaerchenResult saveMaerchen(Maerchen maerchen) {
    long numberOfAlreadyExistingMaerchen =
        getNumberOfAlreadyExistingMaerchenInSchuljahr(
            maerchen.getMaerchenId(), maerchen.getSchuljahr());
    if (numberOfAlreadyExistingMaerchen > 0) {
      return SaveMaerchenResult.MAERCHEN_BEREITS_ERFASST;
    }

    maerchenRepository.save(maerchen);
    return SaveMaerchenResult.SPEICHERN_ERFOLGREICH;
  }

  @Override
  @Transactional
  public DeleteMaerchenResult deleteMaerchen(Maerchen maerchen) {
    if (maercheneinteilungService.existsReferencedMaerchenByMaercheinteilung(
        maerchen.getMaerchenId())) {
      return DeleteMaerchenResult.MAERCHEN_VON_MAERCHENEINTEILUNGEN_REFERENZIERT;
    }

    maerchenRepository.delete(maerchen);
    return DeleteMaerchenResult.LOESCHEN_ERFOLGREICH;
  }
}
