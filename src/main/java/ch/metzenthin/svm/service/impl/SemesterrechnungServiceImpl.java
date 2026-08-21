package ch.metzenthin.svm.service.impl;

import ch.metzenthin.svm.common.datatypes.Rechnungstyp;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Lektionsgebuehren;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.persistence.entities.Semesterrechnung;
import ch.metzenthin.svm.persistence.repository.LektionsgebuehrenRepository;
import ch.metzenthin.svm.persistence.repository.SemesterrechnungRepository;
import ch.metzenthin.svm.service.RechnungsempfaengerService;
import ch.metzenthin.svm.service.SemesterrechnungService;
import ch.metzenthin.svm.service.result.CalculateMaxAnzahlWochenKursanmeldungenResult;
import ch.metzenthin.svm.service.result.CalculateWochenbetragKurseResult;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SemesterrechnungServiceImpl implements SemesterrechnungService {

  private static final Logger LOGGER = LoggerFactory.getLogger(SemesterrechnungServiceImpl.class);

  private final RechnungsempfaengerService rechnungsempfaengerService;
  private final SemesterrechnungRepository semesterrechnungRepository;
  private final LektionsgebuehrenRepository lektionsgebuehrenRepository;

  public SemesterrechnungServiceImpl(
      RechnungsempfaengerService rechnungsempfaengerService,
      SemesterrechnungRepository semesterrechnungRepository,
      LektionsgebuehrenRepository lektionsgebuehrenRepository) {
    this.rechnungsempfaengerService = rechnungsempfaengerService;
    this.semesterrechnungRepository = semesterrechnungRepository;
    this.lektionsgebuehrenRepository = lektionsgebuehrenRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public boolean existsReferencedCodeByCodeId(int codeId) {
    return semesterrechnungRepository.countBySemesterrechnungCodeId(codeId) > 0;
  }

  @Override
  @Transactional(readOnly = true)
  public int countSemesterrechnungenBySemesterId(int semesterId) {
    return semesterrechnungRepository.countBySemesterId(semesterId);
  }

  @Override
  @Transactional
  public int calculateAndUpdateAnzahlWochen(Semester semester) {
    LOGGER.debug(
        "Berechnung Anzahl Wochen für Semesterrechnungen des Semesters mit ID {}, "
            + "Anzahl Schulwochen des Semesters: {}",
        semester.getSemesterId(),
        semester.getAnzahlSchulwochen());
    int semesterrechnungUpdatedCount = 0;
    List<Semesterrechnung> semesterrechnungen =
        semesterrechnungRepository.findBySemesterId(semester.getSemesterId());
    LOGGER.debug("Anzahl Semesterrechnungen gefunden: {}", semesterrechnungen.size());
    for (Semesterrechnung semesterrechnung : semesterrechnungen) {

      if (semesterrechnung.getRechnungsdatumVorrechnung() != null
          && semesterrechnung.getRechnungsdatumNachrechnung() != null) {
        continue;
      }

      boolean semesterrechnungUpdated = calculateAndUpdateAnzahlWochen(semesterrechnung, semester);

      if (semesterrechnungUpdated) {
        semesterrechnungUpdatedCount++;
        semesterrechnungRepository.save(semesterrechnung);
      }
    }
    LOGGER.debug("Anzahl Semesterrechnungen geändert: {}", semesterrechnungUpdatedCount);

    return semesterrechnungUpdatedCount;
  }

  @SuppressWarnings("OptionalIsPresent")
  @Override
  @Transactional
  public int calculateAndUpdateAnzahlWochenAndWochenbetrag(
      Semester currentSemester,
      Optional<Semester> nextSemesterOptional,
      Angehoeriger rechnungsempfaenger) {
    if (LOGGER.isDebugEnabled()) {
      String nextSemesterInfo =
          (nextSemesterOptional.isPresent())
              ? String.format(
                  "des nächsten Semesters (ID %s, Anzahl Schulwochen %s), ",
                  nextSemesterOptional.get().getSemesterId(),
                  nextSemesterOptional.get().getAnzahlSchulwochen())
              : "";
      LOGGER.debug(
          "Berechnung Anzahl Wochen und Wochenbetrag für Semesterrechnungen "
              + "des jetzigen Semesters (ID {}, Anzahl Schulwochen {}), "
              + "{}"
              + "für den Rechnungsempfänger mit der ID {}",
          currentSemester.getSemesterId(),
          currentSemester.getAnzahlSchulwochen(),
          nextSemesterInfo,
          rechnungsempfaenger.getPersonId());
    }

    int numberOfSemesterrechnungenUpdated = 0;

    // Lektionsgebühren
    Map<Integer, BigDecimal[]> lektionsgebuehrenMap = getLektionsgebuehrenByLektionslaengeMap();

    // Semesterrechnung des jetzigen Semesters
    Optional<Semesterrechnung> semesterrechnungCurrentSemesterOptional =
        semesterrechnungRepository.findBySemesterIdAndRechnungsempfaengerId(
            currentSemester.getSemesterId(), rechnungsempfaenger.getPersonId());
    if (semesterrechnungCurrentSemesterOptional.isPresent()) {
      Semesterrechnung semesterrechnungCurrentSemester =
          semesterrechnungCurrentSemesterOptional.get();
      if (calculateAndUpdateSemesterrechnungCurrentSemester(
          currentSemester, semesterrechnungCurrentSemester, lektionsgebuehrenMap)) {
        numberOfSemesterrechnungenUpdated++;
      }
    }

    if (nextSemesterOptional.isPresent()) {
      // Semesterrechnung des nachfolgenden Semesters
      Optional<Semesterrechnung> semesterrechnungNextSemesterOptional =
          semesterrechnungRepository.findBySemesterIdAndRechnungsempfaengerId(
              nextSemesterOptional.get().getSemesterId(), rechnungsempfaenger.getPersonId());
      if (semesterrechnungNextSemesterOptional.isPresent()) {
        Semesterrechnung semesterrechnungNextSemester = semesterrechnungNextSemesterOptional.get();
        if (calculateAndUpdateSemesterrechnungNextSemester(
            currentSemester, semesterrechnungNextSemester, lektionsgebuehrenMap)) {
          numberOfSemesterrechnungenUpdated++;
        }
      }
    }

    return numberOfSemesterrechnungenUpdated;
  }

  private boolean calculateAndUpdateAnzahlWochen(
      Semesterrechnung semesterrechnung, Semester semester) {
    CalculateMaxAnzahlWochenKursanmeldungenResult calculateMaxAnzahlWochenKursanmeldungenResult =
        rechnungsempfaengerService.calculateMaxAnzahlWochen(
            semesterrechnung.getRechnungsempfaenger(), semester);

    boolean isSemesterrechnungVorrechnungUpdated =
        setAnzahlWochenVorrechnung(
            semesterrechnung, calculateMaxAnzahlWochenKursanmeldungenResult.maxAnzahlWochen());

    boolean isSemesterrechnungNachrechnungUpdated =
        setAnzahlWochenNachrechnung(
            semesterrechnung, calculateMaxAnzahlWochenKursanmeldungenResult.maxAnzahlWochen());
    return isSemesterrechnungVorrechnungUpdated || isSemesterrechnungNachrechnungUpdated;
  }

  static boolean setAnzahlWochenVorrechnung(
      Semesterrechnung semesterrechnung, int maxAnzahlWochen) {
    if (semesterrechnung.getRechnungsdatumVorrechnung() == null
        && !semesterrechnung.getAnzahlWochenVorrechnung().equals(maxAnzahlWochen)) {
      LOGGER.debug(
          "Semesterrechnung (Semester-ID: {}, Rechnungsempfänger-ID: {}) "
              + "Anzahl Wochen Vorrechnung geändert (alt: {}, neu: {})",
          semesterrechnung.getSemester().getSemesterId(),
          semesterrechnung.getRechnungsempfaenger().getPersonId(),
          semesterrechnung.getAnzahlWochenVorrechnung(),
          maxAnzahlWochen);
      semesterrechnung.setAnzahlWochenVorrechnung(maxAnzahlWochen);
      return true;
    }
    return false;
  }

  static boolean setAnzahlWochenNachrechnung(
      Semesterrechnung semesterrechnung, int maxAnzahlWochen) {
    if (semesterrechnung.getRechnungsdatumNachrechnung() == null
        && !semesterrechnung.getAnzahlWochenNachrechnung().equals(maxAnzahlWochen)) {
      LOGGER.debug(
          "Semesterrechnung (Semester-ID: {}, Rechnungsempfänger-ID: {}) "
              + "Anzahl Wochen Nachrechnung geändert (alt: {}, neu: {})",
          semesterrechnung.getSemester().getSemesterId(),
          semesterrechnung.getRechnungsempfaenger().getPersonId(),
          semesterrechnung.getAnzahlWochenNachrechnung(),
          maxAnzahlWochen);
      semesterrechnung.setAnzahlWochenNachrechnung(maxAnzahlWochen);
      return true;
    }
    return false;
  }

  boolean calculateAndUpdateSemesterrechnungCurrentSemester(
      Semester currentSemester,
      Semesterrechnung semesterrechnungCurrentSemester,
      Map<Integer, BigDecimal[]> lektionsgebuehrenMap) {
    // Vorrechnung und Nachrechnung: Berechnung und Update Anzahl Wochen
    boolean isAnzahlWochenUpdated =
        calculateAndUpdateAnzahlWochen(semesterrechnungCurrentSemester, currentSemester);

    // Nachrechnung: Berechnung und Update des Wochenbetrags
    boolean isWochenbetragUpdated = false;
    if (semesterrechnungCurrentSemester.getRechnungsdatumNachrechnung() == null) {
      isWochenbetragUpdated =
          calculateAndUpdateWochenbetrag(
              semesterrechnungCurrentSemester,
              currentSemester,
              Rechnungstyp.NACHRECHNUNG,
              lektionsgebuehrenMap);
    }

    // Update Semesterrechnung jetziges Semester
    if (isAnzahlWochenUpdated || isWochenbetragUpdated) {
      saveOrDeleteSemesterrechnung(semesterrechnungCurrentSemester);
      return true;
    }
    return false;
  }

  boolean calculateAndUpdateSemesterrechnungNextSemester(
      Semester currentSemester,
      Semesterrechnung semesterrechnungNextSemester,
      Map<Integer, BigDecimal[]> lektionsgebuehrenMap) {
    // Berechnung und Update Wochenbetrag Vorrechnung
    if (semesterrechnungNextSemester.getRechnungsdatumVorrechnung() == null) {
      boolean isSemesterrechnungNextSemesterUpdated =
          calculateAndUpdateWochenbetrag(
              semesterrechnungNextSemester,
              currentSemester,
              Rechnungstyp.VORRECHNUNG,
              lektionsgebuehrenMap);

      // Update Semesterrechnung nächstes Semester
      if (isSemesterrechnungNextSemesterUpdated) {
        saveOrDeleteSemesterrechnung(semesterrechnungNextSemester);
        return true;
      }
    }
    return false;
  }

  boolean calculateAndUpdateWochenbetrag(
      Semesterrechnung semesterrechnung,
      Semester relevantesSemester,
      Rechnungstyp rechnungstyp,
      Map<Integer, BigDecimal[]> lektionsgebuehrenMap) {
    CalculateWochenbetragKurseResult calculateWochenbetragKurseResult =
        rechnungsempfaengerService.calculateWochenbetrag(
            semesterrechnung, relevantesSemester, rechnungstyp, lektionsgebuehrenMap);

    if (rechnungstyp == Rechnungstyp.VORRECHNUNG) {
      return setWochenbetragVorrechnung(semesterrechnung, calculateWochenbetragKurseResult);
    } else if (rechnungstyp == Rechnungstyp.NACHRECHNUNG) {
      return setWochenbetragNachrechnung(semesterrechnung, calculateWochenbetragKurseResult);
    } else {
      throw new IllegalArgumentException("Unbekannter Rechnungstyp");
    }
  }

  private static boolean setWochenbetragVorrechnung(
      Semesterrechnung semesterrechnung,
      CalculateWochenbetragKurseResult calculateWochenbetragKurseResult) {
    BigDecimal wochenbetrag =
        calculateWochenbetragKurseResult.allLektionsgebuehrenForKurslaengenFound()
            ? calculateWochenbetragKurseResult.wochenbetragKurse()
            : new BigDecimal("-99999.99"); // Sollte nicht vorkommen!
    if (wochenbetrag.compareTo(semesterrechnung.getWochenbetragVorrechnung()) != 0) {
      LOGGER.debug(
          "Semesterrechnung (Semester-ID: {}, Rechnungsempfänger-ID: {}) "
              + "Wochenbetrag Vorrechnung geändert (alt: {}, neu: {})",
          semesterrechnung.getSemester().getSemesterId(),
          semesterrechnung.getRechnungsempfaenger().getPersonId(),
          semesterrechnung.getWochenbetragVorrechnung(),
          wochenbetrag);
      semesterrechnung.setWochenbetragVorrechnung(wochenbetrag);
      return true;
    }
    return false;
  }

  private static boolean setWochenbetragNachrechnung(
      Semesterrechnung semesterrechnung,
      CalculateWochenbetragKurseResult calculateWochenbetragKurseResult) {
    BigDecimal wochenbetrag =
        calculateWochenbetragKurseResult.allLektionsgebuehrenForKurslaengenFound()
            ? calculateWochenbetragKurseResult.wochenbetragKurse()
            : new BigDecimal("-99999.99"); // Sollte nicht vorkommen!
    if (wochenbetrag.compareTo(semesterrechnung.getWochenbetragNachrechnung()) != 0) {
      LOGGER.debug(
          "Semesterrechnung (Semester-ID: {}, Rechnungsempfänger-ID: {}) "
              + "Wochenbetrag Nachrechnung geändert (alt: {}, neu: {})",
          semesterrechnung.getSemester().getSemesterId(),
          semesterrechnung.getRechnungsempfaenger().getPersonId(),
          semesterrechnung.getWochenbetragNachrechnung(),
          wochenbetrag);
      semesterrechnung.setWochenbetragNachrechnung(wochenbetrag);
      return true;
    }
    return false;
  }

  void saveOrDeleteSemesterrechnung(Semesterrechnung semesterrechnung) {
    if (!semesterrechnung.isNullrechnung()) {
      semesterrechnungRepository.save(semesterrechnung);
    } else {
      semesterrechnungRepository.delete(semesterrechnung);
    }
  }

  Map<Integer, BigDecimal[]> getLektionsgebuehrenByLektionslaengeMap() {
    List<Lektionsgebuehren> lektionsgebuehrenList =
        lektionsgebuehrenRepository.findAllOrderByLektionslaenge();
    return lektionsgebuehrenList.stream()
        .collect(
            Collectors.toMap(
                Lektionsgebuehren::getLektionslaenge,
                lektionsgebuehren ->
                    new BigDecimal[] {
                      lektionsgebuehren.getBetrag1Kind(),
                      lektionsgebuehren.getBetrag2Kinder(),
                      lektionsgebuehren.getBetrag3Kinder(),
                      lektionsgebuehren.getBetrag4Kinder(),
                      lektionsgebuehren.getBetrag5Kinder(),
                      lektionsgebuehren.getBetrag6Kinder()
                    }));
  }

  @Override
  @Transactional
  public void deleteSemesterrechnungenBySemesterId(int semesterId) {
    semesterrechnungRepository.deleteBySemesterId(semesterId);
  }
}
