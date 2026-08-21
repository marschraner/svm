package ch.metzenthin.svm.service.impl;

import static ch.metzenthin.svm.common.utils.DateAndTimeUtils.getNumberOfDaysOfPeriod;

import ch.metzenthin.svm.common.datatypes.Rechnungstyp;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Anmeldung;
import ch.metzenthin.svm.persistence.entities.Kurs;
import ch.metzenthin.svm.persistence.entities.Kursanmeldung;
import ch.metzenthin.svm.persistence.entities.Lektionsgebuehren;
import ch.metzenthin.svm.persistence.entities.Schueler;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.persistence.entities.Semesterrechnung;
import ch.metzenthin.svm.persistence.repository.AnmeldungRepository;
import ch.metzenthin.svm.persistence.repository.SchuelerRepository;
import ch.metzenthin.svm.service.KursanmeldungService;
import ch.metzenthin.svm.service.RechnungsempfaengerService;
import ch.metzenthin.svm.service.result.CalculateMaxAnzahlWochenKursanmeldungenResult;
import ch.metzenthin.svm.service.result.CalculateWochenbetragKurseResult;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RechnungsempfaengerServiceImpl implements RechnungsempfaengerService {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(RechnungsempfaengerServiceImpl.class);

  private final KursanmeldungService kursanmeldungService;
  private final AnmeldungRepository anmeldungRepository;
  private final SchuelerRepository schuelerRepository;

  public RechnungsempfaengerServiceImpl(
      KursanmeldungService kursanmeldungService,
      AnmeldungRepository anmeldungRepository,
      SchuelerRepository schuelerRepository) {
    this.kursanmeldungService = kursanmeldungService;
    this.anmeldungRepository = anmeldungRepository;
    this.schuelerRepository = schuelerRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public CalculateMaxAnzahlWochenKursanmeldungenResult calculateMaxAnzahlWochen(
      Angehoeriger rechnungsempfaenger, Semester semester) {

    List<Schueler> schuelerList =
        schuelerRepository.findSchuelerByRechnungsempfaengerId(rechnungsempfaenger.getPersonId());
    List<CalculateMaxAnzahlWochenKursanmeldungenResult>
        calculateMaxAnzahlWochenKursanmeldungenResults =
            schuelerList.stream()
                .filter(schueler -> isAngemeldetOnSemesterbeginn(schueler, semester))
                .map(schueler -> kursanmeldungService.calculateMaxAnzahlWochen(schueler, semester))
                .toList();

    Set<Integer> anzahlWochenKursanmeldungenSet =
        calculateMaxAnzahlWochenKursanmeldungenResults.stream()
            .map(CalculateMaxAnzahlWochenKursanmeldungenResult::maxAnzahlWochen)
            .collect(Collectors.toSet());

    // Default-Wert Anzahl Semesterwochen, falls noch keine Kursanmeldung
    Optional<Integer> maxAnzahlWochenKursanmeldungenOptional =
        anzahlWochenKursanmeldungenSet.stream().max(Integer::compareTo);
    int maxAnzahlWochenKursanmeldungen =
        (maxAnzahlWochenKursanmeldungenOptional.isEmpty()
                || maxAnzahlWochenKursanmeldungenOptional.get() == 0)
            ? semester.getAnzahlSchulwochen()
            : maxAnzahlWochenKursanmeldungenOptional.get();

    boolean kursanmeldungenWithDifferentAnzahlWochen =
        calculateMaxAnzahlWochenKursanmeldungenResults.stream()
                .anyMatch(
                    CalculateMaxAnzahlWochenKursanmeldungenResult
                        ::kursanmeldungenWithDifferentAnzahlWochen)
            || (anzahlWochenKursanmeldungenSet.size() > 1);

    return new CalculateMaxAnzahlWochenKursanmeldungenResult(
        maxAnzahlWochenKursanmeldungen, kursanmeldungenWithDifferentAnzahlWochen);
  }

  @Override
  @Transactional(readOnly = true)
  public CalculateWochenbetragKurseResult calculateWochenbetrag(
      Semesterrechnung semesterrechnung,
      Semester relevantesSemester,
      Rechnungstyp rechnungstyp,
      Map<Integer, BigDecimal[]> lektionsgebuehrenMap) {

    Semester semesterOfSemesterrechnung = semesterrechnung.getSemester();
    Angehoeriger rechnungsempfaenger = semesterrechnung.getRechnungsempfaenger();

    Map<Schueler, List<Kursanmeldung>> kursanmeldungenAngemeldeteSchuelerMap =
        getKursanmeldungenAngemeldeteSchueler(
            relevantesSemester, rechnungsempfaenger, semesterOfSemesterrechnung);

    // 1. Anzahl Kurse Rechnungsempfänger
    int anzahlKurseRechnungsempfaenger =
        getAnzahlKurseRechnungsempfaenger(rechnungstyp, kursanmeldungenAngemeldeteSchuelerMap);

    // 2. Wochenbetrag berechnen
    BigDecimal wochenbetrag = BigDecimal.ZERO;
    for (Entry<Schueler, List<Kursanmeldung>> entry :
        kursanmeldungenAngemeldeteSchuelerMap.entrySet()) {

      // 2.a abgemeldete Schüler nicht berücksichtigen: bereits herausgefiltert

      // 2.b relevante Kurse für einen Schüler (Nachrechnung oder nicht abgemeldet)
      List<Kurs> relevanteKurseSchueler = getRelevanteKurseSchueler(rechnungstyp, entry);

      // 2.c Wochenbetrag Kurse Schüler ohne 6-Jahres-Rabatt
      wochenbetrag =
          getWochenbetragOhneSechsJahresRabatt(
              lektionsgebuehrenMap,
              relevanteKurseSchueler,
              anzahlKurseRechnungsempfaenger,
              wochenbetrag);
      if (wochenbetrag == null) {
        // Es wurden nicht alle Lektionsgebühren gefunden.
        return new CalculateWochenbetragKurseResult(BigDecimal.ZERO, false);
      }

      // 2.d Schüler 6-Jahres-Rabatt-berechtigt? (Hier immer Semester der Semesterrechnung als
      // Referenz!)
      boolean schuelerHatSechsJahresRabatt =
          hasSchuelerSechsJahresRabatt(entry, relevanteKurseSchueler, semesterOfSemesterrechnung);

      // 2.e Reduktion 6-Jahres-Rabatt
      if (schuelerHatSechsJahresRabatt) {
        // Kurs mit kürzester Lektionsdauer suchen (= günstigster Kurs)
        int minimaleKurslaenge = getMinimaleKurslaenge(relevanteKurseSchueler);
        wochenbetrag =
            calculateReductionSechsJahresRabatt(
                lektionsgebuehrenMap,
                minimaleKurslaenge,
                anzahlKurseRechnungsempfaenger,
                wochenbetrag);
      }
    }

    // 3. Runden auf 2 Nachkommastellen
    wochenbetrag = wochenbetrag.setScale(2, RoundingMode.HALF_EVEN);

    return new CalculateWochenbetragKurseResult(wochenbetrag, true);
  }

  boolean isAngemeldetOnSemesterbeginn(Schueler schueler, Semester semester) {
    List<Anmeldung> anmeldungen = getAnmeldungen(schueler);
    if (anmeldungen.isEmpty()) {
      return false;
    }

    Anmeldung anmeldung = anmeldungen.get(0);
    return anmeldung.getAbmeldedatum() == null
        || anmeldung.getAbmeldedatum().after(semester.getSemesterbeginn());
  }

  private List<Anmeldung> getAnmeldungen(Schueler schueler) {
    return anmeldungRepository.findBySchuelerIdOrderByAnmeldedatumDesc(schueler.getPersonId());
  }

  private Map<Schueler, List<Kursanmeldung>> getKursanmeldungenAngemeldeteSchueler(
      Semester relevantesSemester,
      Angehoeriger rechnungsempfaenger,
      Semester semesterOfSemesterrechnung) {
    Map<Schueler, List<Kursanmeldung>> kursanmeldungenBySchuelerIdMap =
        kursanmeldungService.findKursanmeldungenForSemesterAndRechnungsempfaengerBySchueler(
            relevantesSemester, rechnungsempfaenger);

    // Abgemeldete Schüler nicht berücksichtigen
    return filterAbgemeldeteSchueler(semesterOfSemesterrechnung, kursanmeldungenBySchuelerIdMap);
  }

  Map<Schueler, List<Kursanmeldung>> filterAbgemeldeteSchueler(
      Semester semester, Map<Schueler, List<Kursanmeldung>> kursanmeldungenBySchuelerMap) {
    return kursanmeldungenBySchuelerMap.entrySet().stream()
        .filter(
            schuelerListEntry -> isAngemeldetOnSemesterbeginn(schuelerListEntry.getKey(), semester))
        .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
  }

  int getAnzahlKurseRechnungsempfaenger(
      Rechnungstyp rechnungstyp,
      Map<Schueler, List<Kursanmeldung>> kursanmeldungenAngemeldeteSchuelerMap) {
    int anzahlKurseRechnungsempfaenger = 0;
    for (Entry<Schueler, List<Kursanmeldung>> entry :
        kursanmeldungenAngemeldeteSchuelerMap.entrySet()) {
      anzahlKurseRechnungsempfaenger +=
          (int)
              entry.getValue().stream()
                  .filter(
                      kursanmeldung -> isNachrechnungOrNotAbgemeldet(rechnungstyp, kursanmeldung))
                  .count();
    }
    // Nur Rabatte bis Lektionsgebühren.MAX_KINDER
    if (anzahlKurseRechnungsempfaenger > Lektionsgebuehren.MAX_KINDER) {
      anzahlKurseRechnungsempfaenger = Lektionsgebuehren.MAX_KINDER;
    }
    return anzahlKurseRechnungsempfaenger;
  }

  boolean isNachrechnungOrNotAbgemeldet(Rechnungstyp rechnungstyp, Kursanmeldung kursanmeldung) {
    return rechnungstyp == Rechnungstyp.NACHRECHNUNG || kursanmeldung.getAbmeldedatum() == null;
  }

  /**
   * @return null, wenn Lektionslänge nicht vorhanden
   */
  static BigDecimal getWochenbetragOhneSechsJahresRabatt(
      Map<Integer, BigDecimal[]> lektionsgebuehrenMap,
      List<Kurs> relevanteKurseSchueler,
      int anzahlKurseRechnungsempfaenger,
      BigDecimal wochenbetrag) {
    for (Kurs kurs : relevanteKurseSchueler) {
      int kurslaenge = kurs.getKurslaenge();
      BigDecimal[] lektionsgebuehrenKurs = lektionsgebuehrenMap.get(kurslaenge);
      if (lektionsgebuehrenKurs == null) {
        LOGGER.warn(
            "Bei der Berechnung des Wochenbetrags wurden nicht alle Lektionsgebühren gefunden. Kurs-ID: {}, Kurslänge: {}",
            kurs.getKursId(),
            kurslaenge);
        return null;
      }
      BigDecimal betragKurs = lektionsgebuehrenKurs[anzahlKurseRechnungsempfaenger - 1];
      wochenbetrag = wochenbetrag.add(betragKurs);
    }
    return wochenbetrag;
  }

  List<Kurs> getRelevanteKurseSchueler(
      Rechnungstyp rechnungstyp, Entry<Schueler, List<Kursanmeldung>> entry) {
    return entry.getValue().stream()
        .filter(kursanmeldung -> isNachrechnungOrNotAbgemeldet(rechnungstyp, kursanmeldung))
        .map(Kursanmeldung::getKurs)
        .toList();
  }

  boolean hasSchuelerSechsJahresRabatt(
      Entry<Schueler, List<Kursanmeldung>> entry,
      List<Kurs> relevanteKurseSchueler,
      Semester semesterOfSemesterrechnung) {
    boolean schuelerHatSechsJahresRabatt = false;
    if (relevanteKurseSchueler.size() >= 2) {
      int anmeldungsdauer = getAnmeldungsdauer(entry.getKey(), semesterOfSemesterrechnung);
      schuelerHatSechsJahresRabatt =
          (anmeldungsdauer >= Lektionsgebuehren.MIN_ANZAHL_TAGE_SECHS_JAHRES_RABATT);
    }
    return schuelerHatSechsJahresRabatt;
  }

  int getAnmeldungsdauer(Schueler schueler, Semester semester) {
    List<Anmeldung> anmeldungen = getAnmeldungen(schueler);

    Calendar semesterbeginn = semester.getSemesterbeginn();
    int anmeldungsdauer = 0;
    for (Anmeldung anmeldung : anmeldungen) {

      if (anmeldung.getAnmeldedatum().after(semesterbeginn)) {
        continue;
      }

      Calendar periodeEnde;
      if (anmeldung.getAbmeldedatum() == null
          || anmeldung.getAbmeldedatum().after(semesterbeginn)) {
        periodeEnde = semesterbeginn;
      } else {
        periodeEnde = anmeldung.getAbmeldedatum();
      }

      anmeldungsdauer += getNumberOfDaysOfPeriod(anmeldung.getAnmeldedatum(), periodeEnde);
    }

    return anmeldungsdauer;
  }

  static BigDecimal calculateReductionSechsJahresRabatt(
      Map<Integer, BigDecimal[]> lektionsgebuehrenMap,
      int minimaleKurslaenge,
      int anzahlKurseRechnungsempfaenger,
      BigDecimal wochenbetrag) {
    // Bereits erfasster (reduzierter) Preis des günstigsten Kurses wieder subtrahieren
    wochenbetrag =
        wochenbetrag.subtract(
            lektionsgebuehrenMap.get(minimaleKurslaenge)[anzahlKurseRechnungsempfaenger - 1]);
    // 0.5*voller Preis des Kurses addieren
    BigDecimal sechsJahresRabatt = lektionsgebuehrenMap.get(minimaleKurslaenge)[0];
    sechsJahresRabatt = sechsJahresRabatt.multiply(new BigDecimal("0.5"));
    wochenbetrag = wochenbetrag.add(sechsJahresRabatt);
    return wochenbetrag;
  }

  static int getMinimaleKurslaenge(List<Kurs> relevanteKurseSchueler) {
    return relevanteKurseSchueler.stream()
        .map(Kurs::getKurslaenge)
        .min(Integer::compareTo)
        .orElse(Integer.MAX_VALUE);
  }
}
