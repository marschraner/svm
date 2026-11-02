package ch.metzenthin.svm.service.impl;

import static ch.metzenthin.svm.common.utils.Converter.asString;
import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;
import static ch.metzenthin.svm.common.utils.SvmStringUtils.splitStringIntoMultipleLines;

import ch.metzenthin.svm.common.datatypes.Listentyp;
import ch.metzenthin.svm.common.datatypes.Semesterbezeichnung;
import ch.metzenthin.svm.config.SvmProperties2;
import ch.metzenthin.svm.domain.model.IdAndCount;
import ch.metzenthin.svm.domain.model.KursAndLehrkraefteAndNumberOfKursanmeldungen;
import ch.metzenthin.svm.domain.model.KursIdAndLehrkraft;
import ch.metzenthin.svm.domain.model.MonatsstatistikKurse;
import ch.metzenthin.svm.domain.model.PreviousCurrentNextSemester;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Anmeldung;
import ch.metzenthin.svm.persistence.entities.Kurs;
import ch.metzenthin.svm.persistence.entities.KursLehrkraft;
import ch.metzenthin.svm.persistence.entities.Kursanmeldung;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import ch.metzenthin.svm.persistence.entities.Schueler;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.persistence.repository.AnmeldungRepository;
import ch.metzenthin.svm.persistence.repository.KursLehrkraftRepository;
import ch.metzenthin.svm.persistence.repository.KursRepository;
import ch.metzenthin.svm.persistence.repository.KursanmeldungRepository;
import ch.metzenthin.svm.persistence.repository.KursortRepository;
import ch.metzenthin.svm.persistence.repository.KurstypRepository;
import ch.metzenthin.svm.persistence.repository.LektionsgebuehrenRepository;
import ch.metzenthin.svm.persistence.repository.MitarbeiterRepository;
import ch.metzenthin.svm.persistence.repository.SemesterRepository;
import ch.metzenthin.svm.service.KursService;
import ch.metzenthin.svm.service.SemesterService;
import ch.metzenthin.svm.service.SemesterrechnungService;
import ch.metzenthin.svm.service.export.csv.CsvExportService;
import ch.metzenthin.svm.service.export.word.CellLayout;
import ch.metzenthin.svm.service.export.word.WordExportService;
import ch.metzenthin.svm.service.export.word.WordTableLayout;
import ch.metzenthin.svm.service.result.DeleteKursResult;
import ch.metzenthin.svm.service.result.ExportListResult;
import ch.metzenthin.svm.service.result.ImportKurseResult;
import ch.metzenthin.svm.service.result.SaveKursResult;
import ch.metzenthin.svm.service.result.SaveResultAndIdOfSavedEntity;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Martin Schraner
 */
@Service
public class KursServiceImpl implements KursService {

  record NumberOfAnmeldungenAndAbmeldungen(long anmeldungen, long abmeldungen) {}

  private final SemesterService semesterService;
  private final SemesterrechnungService semesterrechnungService;
  private final CsvExportService csvExportService;
  private final WordExportService wordExportService;
  private final KursRepository kursRepository;
  private final KursLehrkraftRepository kursLehrkraftRepository;
  private final LektionsgebuehrenRepository lektionsgebuehrenRepository;
  private final SemesterRepository semesterRepository;
  private final KurstypRepository kurstypRepository;
  private final KursortRepository kursortRepository;
  private final KursanmeldungRepository kursanmeldungRepository;
  private final MitarbeiterRepository mitarbeiterRepository;
  private final AnmeldungRepository anmeldungRepository;
  private final SvmProperties2 svmProperties2;

  public KursServiceImpl(
      SemesterService semesterService,
      SemesterrechnungService semesterrechnungService,
      CsvExportService csvExportService,
      WordExportService wordExportService,
      KursRepository kursRepository,
      KursLehrkraftRepository kursLehrkraftRepository,
      LektionsgebuehrenRepository lektionsgebuehrenRepository,
      SemesterRepository semesterRepository,
      KurstypRepository kurstypRepository,
      KursortRepository kursortRepository,
      KursanmeldungRepository kursanmeldungRepository,
      MitarbeiterRepository mitarbeiterRepository,
      AnmeldungRepository anmeldungRepository,
      SvmProperties2 svmProperties2) {
    this.semesterService = semesterService;
    this.semesterrechnungService = semesterrechnungService;
    this.csvExportService = csvExportService;
    this.wordExportService = wordExportService;
    this.kursRepository = kursRepository;
    this.kursLehrkraftRepository = kursLehrkraftRepository;
    this.lektionsgebuehrenRepository = lektionsgebuehrenRepository;
    this.semesterRepository = semesterRepository;
    this.kurstypRepository = kurstypRepository;
    this.kursortRepository = kursortRepository;
    this.kursanmeldungRepository = kursanmeldungRepository;
    this.mitarbeiterRepository = mitarbeiterRepository;
    this.anmeldungRepository = anmeldungRepository;
    this.svmProperties2 = svmProperties2;
  }

  @Override
  @Transactional(readOnly = true)
  public boolean existsKursByKursortId(int kursortId) {
    return kursRepository.countByKursortId(kursortId) > 0;
  }

  @Override
  @Transactional(readOnly = true)
  public boolean existsKursByKurstypId(int kurstypId) {
    return kursRepository.countByKurstypId(kurstypId) > 0;
  }

  @Override
  @Transactional(readOnly = true)
  public boolean existsKursByLektionslaenge(int lektionslaenge) {
    return kursRepository.findAll().stream()
        .anyMatch(
            kurs -> {
              int kurslaenge =
                  (int) ((kurs.getZeitEnde().getTime() - kurs.getZeitBeginn().getTime()) / 60000);
              return kurslaenge == lektionslaenge;
            });
  }

  @Override
  @Transactional(readOnly = true)
  public boolean existsKursBySemesterId(int semesterId) {
    return kursRepository.countBySemesterId(semesterId) > 0;
  }

  @Override
  @Transactional(readOnly = true)
  public int countKursanmeldungenByKursId(int kursId) {
    return kursanmeldungRepository.countByKursId(kursId);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<KursAndLehrkraefteAndNumberOfKursanmeldungen>
      findKursAndLehrkraefteAndNumberOfKursanmeldungenById(int kursId) {

    Optional<Kurs> kursOptional = kursRepository.findById(kursId);

    List<KursLehrkraft> kursAndLehrkraftList =
        kursLehrkraftRepository.findByKursIdOrderByLehrkraefteOrder(kursId);
    List<Mitarbeiter> lehrkraefte =
        kursAndLehrkraftList.stream().map(KursLehrkraft::getLehrkraft).toList();

    int numberOfKursanmeldungen = kursanmeldungRepository.countByKursId(kursId);

    return kursOptional.map(
        kurs ->
            new KursAndLehrkraefteAndNumberOfKursanmeldungen(
                kurs, lehrkraefte, numberOfKursanmeldungen));
  }

  @Override
  @Transactional(readOnly = true)
  public List<KursAndLehrkraefteAndNumberOfKursanmeldungen>
      findAllKurseAndLehrkraefteAndNumberOfKursanmeldungenForSemester(int semesterId) {

    List<IdAndCount> kursIdAndNumberOfKursanmeldungen =
        kursanmeldungRepository.countKursanmeldungenBySemesterIdGroupByKursId(semesterId);
    Map<Integer, Long> kursIdAndNumberOfKursanmeldungenAsMap =
        kursIdAndNumberOfKursanmeldungen.stream()
            .collect(Collectors.toMap(IdAndCount::id, IdAndCount::count));

    List<KursIdAndLehrkraft> kursIdAndLehrkraftList =
        kursLehrkraftRepository
            .findKursIdAndLehrkraefteBySemesterIdOrderByKursIdAndLehrkraefteOrder(semesterId);
    Map<Integer, List<Mitarbeiter>> kursIdAndLehrkraefteAsMap =
        kursIdAndLehrkraftList.stream()
            .collect(
                Collectors.groupingBy(
                    KursIdAndLehrkraft::kursId,
                    Collectors.mapping(KursIdAndLehrkraft::lehrkraft, Collectors.toList())));

    List<Kurs> kursList = kursRepository.findAllBySemesterId(semesterId);
    Collections.sort(kursList);

    List<KursAndLehrkraefteAndNumberOfKursanmeldungen>
        kursAndLehrkraefteAndNumberOfKursanmeldungenList = new ArrayList<>();
    for (Kurs kurs : kursList) {
      long numberOfKursanmeldungen =
          kursIdAndNumberOfKursanmeldungenAsMap.getOrDefault(kurs.getKursId(), 0L);
      List<Mitarbeiter> lehrkreafte =
          kursIdAndLehrkraefteAsMap.getOrDefault(kurs.getKursId(), List.of());
      KursAndLehrkraefteAndNumberOfKursanmeldungen kursAndLehrkraefteAndNumberOfKursanmeldungen =
          new KursAndLehrkraefteAndNumberOfKursanmeldungen(
              kurs, lehrkreafte, numberOfKursanmeldungen);
      kursAndLehrkraefteAndNumberOfKursanmeldungenList.add(
          kursAndLehrkraefteAndNumberOfKursanmeldungen);
    }
    return kursAndLehrkraefteAndNumberOfKursanmeldungenList;
  }

  @Override
  @Transactional
  public SaveResultAndIdOfSavedEntity<SaveKursResult> saveKurs(
      Kurs kurs, Mitarbeiter lehrkraft1, Mitarbeiter lehrkraft2) {

    if (lehrkraft1 == null) {
      return new SaveResultAndIdOfSavedEntity<>(SaveKursResult.KEINE_LEHRKRAEFTE_ERFASST, -1);
    }

    List<Integer> lehrkraefteIds = new ArrayList<>();
    lehrkraefteIds.add(lehrkraft1.getPersonId());
    if (lehrkraft2 != null) {
      lehrkraefteIds.add(lehrkraft2.getPersonId());
    }

    int numberOfKurse =
        (kurs.getKursId() == null)
            ? kursLehrkraftRepository
                .countKurseBySemesterIdAndWochentagAndZeitBeginnAndMitarbeiterIdIn(
                    kurs.getSemester().getSemesterId(),
                    kurs.getWochentag(),
                    kurs.getZeitBeginn(),
                    lehrkraefteIds)
            : kursLehrkraftRepository
                .countKurseBySemesterIdAndWochentagAndZeitBeginnAndMitarbeiterIdInAndKursIdNe(
                    kurs.getSemester().getSemesterId(),
                    kurs.getWochentag(),
                    kurs.getZeitBeginn(),
                    lehrkraefteIds,
                    kurs.getKursId());
    if (numberOfKurse > 0) {
      return new SaveResultAndIdOfSavedEntity<>(SaveKursResult.KURS_BEREITS_ERFASST, -1);
    }

    int numberOfLektionsgebuehren =
        lektionsgebuehrenRepository.countByLektionslaenge(kurs.getKurslaenge());
    if (numberOfLektionsgebuehren == 0) {
      return new SaveResultAndIdOfSavedEntity<>(SaveKursResult.LEKTIONSGEBUEHREN_NICHT_ERFASST, -1);
    }

    if (semesterRepository.countBySemesterId(kurs.getSemester().getSemesterId()) == 0) {
      return new SaveResultAndIdOfSavedEntity<>(
          SaveKursResult.SEMESTER_DURCH_ANDEREN_BENUTZER_GELOESCHT, -1);
    }

    if (kurstypRepository.countByKurstypId(kurs.getKurstyp().getKurstypId()) == 0) {
      return new SaveResultAndIdOfSavedEntity<>(
          SaveKursResult.KURSTYP_DURCH_ANDEREN_BENUTZER_GELOESCHT, -1);
    }

    if (kursortRepository.countByKursortId(kurs.getKursort().getKursortId()) == 0) {
      return new SaveResultAndIdOfSavedEntity<>(
          SaveKursResult.KURSORT_DURCH_ANDEREN_BENUTZER_GELOESCHT, -1);
    }

    Optional<Mitarbeiter> lehrkraft1Optional =
        mitarbeiterRepository.findById(lehrkraft1.getPersonId());
    if (lehrkraft1Optional.isEmpty()) {
      return new SaveResultAndIdOfSavedEntity<>(
          SaveKursResult.LEHRKRAFT_DURCH_ANDEREN_BENUTZER_GELOESCHT, -1);
    }
    lehrkraft1 = lehrkraft1Optional.get();

    if (lehrkraft2 != null) {
      Optional<Mitarbeiter> lehrkraft2Optional =
          mitarbeiterRepository.findById(lehrkraft2.getPersonId());
      if (lehrkraft2Optional.isEmpty()) {
        return new SaveResultAndIdOfSavedEntity<>(
            SaveKursResult.LEHRKRAFT_DURCH_ANDEREN_BENUTZER_GELOESCHT, -1);
      }
      lehrkraft2 = lehrkraft2Optional.get();
    }

    kurs = kursRepository.save(kurs);

    createOrUpdateKursLehrkraefte(kurs, lehrkraft1, lehrkraft2);

    return new SaveResultAndIdOfSavedEntity<>(
        SaveKursResult.SPEICHERN_ERFOLGREICH, kurs.getKursId());
  }

  private void createOrUpdateKursLehrkraefte(
      Kurs kurs, Mitarbeiter lehrkraft1, Mitarbeiter lehrkraft2) {

    List<KursLehrkraft> kursLehrkraefteFound =
        kursLehrkraftRepository.findByKursIdOrderByLehrkraefteOrder(kurs.getKursId());

    if (kursLehrkraefteFound.isEmpty()) {
      createAndSaveKursLehrkraft(kurs, lehrkraft1, 0);
      createAndSaveKursLehrkraft(kurs, lehrkraft2, 1);
    } else {
      // Lehrkraft1
      KursLehrkraft kursLehrkraft1Found = kursLehrkraefteFound.get(0);
      updateKursLehrkraft(lehrkraft1, kursLehrkraft1Found);
      // Lehrkraft2
      if (kursLehrkraefteFound.size() > 1) {
        KursLehrkraft kursLehrkraft2Found = kursLehrkraefteFound.get(1);
        if (lehrkraft2 != null) {
          updateKursLehrkraft(lehrkraft2, kursLehrkraft2Found);
        } else {
          kursLehrkraftRepository.delete(kursLehrkraft2Found);
        }
      } else {
        createAndSaveKursLehrkraft(kurs, lehrkraft2, 1);
      }
    }
  }

  private void createAndSaveKursLehrkraft(Kurs kurs, Mitarbeiter lehrkraft, int lehrkraefteOrder) {
    if (lehrkraft != null) {
      KursLehrkraft kursLehrkraft = new KursLehrkraft(kurs, lehrkraft, lehrkraefteOrder);
      kursLehrkraftRepository.save(kursLehrkraft);
    }
  }

  private void updateKursLehrkraft(Mitarbeiter lehrkraft, KursLehrkraft kursLehrkraft) {
    if (!Objects.equals(lehrkraft.getPersonId(), kursLehrkraft.getLehrkraft().getPersonId())) {
      kursLehrkraftRepository.delete(kursLehrkraft);
      createAndSaveKursLehrkraft(
          kursLehrkraft.getKurs(), lehrkraft, kursLehrkraft.getLehrkraefteOrder());
    }
  }

  @Override
  @Transactional
  public DeleteKursResult deleteKurs(int kursId) {
    Optional<Semester> currentSemesterOptional = kursRepository.findSemesterByKursId(kursId);
    if (currentSemesterOptional.isEmpty()) {
      return DeleteKursResult.LOESCHEN_ERFOLGREICH;
    }

    Semester currentSemester = currentSemesterOptional.get();
    Optional<Semester> nextSemesterOptional = semesterService.findNextSemester(currentSemester);

    List<Kursanmeldung> kursanmeldungen = kursanmeldungRepository.findByKursId(kursId);
    List<Angehoeriger> rechnungsempfaengerList =
        kursanmeldungen.stream()
            .map(kursanmeldung -> kursanmeldung.getSchueler().getRechnungsempfaenger())
            .toList();

    kursanmeldungRepository.deleteByKursId(kursId);
    kursLehrkraftRepository.deleteByKursId(kursId);
    kursRepository.deleteByKursId(kursId);

    for (Angehoeriger rechnungsempfaenger : rechnungsempfaengerList) {
      semesterrechnungService.calculateAndUpdateAnzahlWochenAndWochenbetrag(
          currentSemester, nextSemesterOptional, rechnungsempfaenger);
    }

    return DeleteKursResult.LOESCHEN_ERFOLGREICH;
  }

  @Override
  @Transactional
  public ImportKurseResult importKurseFromPreviousSemester(Semester targetSemester) {
    Optional<Semester> sourceSemesterOptional = getSourceSemester(targetSemester);

    if (sourceSemesterOptional.isEmpty()) {
      return ImportKurseResult.IMPORT_ABGEBROCHEN_KEIN_VORHERGEHENDES_SEMESTER;
    }

    List<Kurs> sourceKursList =
        kursRepository.findAllBySemesterId(sourceSemesterOptional.get().getSemesterId());

    for (Kurs sourceKurs : sourceKursList) {

      // Kurs kopieren
      Kurs targetKurs = new Kurs();
      targetKurs.copyAttributesFrom(sourceKurs);
      targetKurs.setKurstyp(sourceKurs.getKurstyp());
      targetKurs.setKursort(sourceKurs.getKursort());
      targetKurs.setSemester(targetSemester);
      List<KursLehrkraft> lehrkraefteOfSourceKurs =
          kursLehrkraftRepository.findByKursIdOrderByLehrkraefteOrder(sourceKurs.getKursId());
      List<KursLehrkraft> kursLehrkraefteOfTargetKurs = new ArrayList<>();
      List<Mitarbeiter> lehrkraefteOfTargetKurs = new ArrayList<>();
      for (KursLehrkraft lehrkraftOfSourceKurs : lehrkraefteOfSourceKurs) {
        KursLehrkraft targetKursLehrkraft =
            new KursLehrkraft(
                targetKurs,
                lehrkraftOfSourceKurs.getLehrkraft(),
                lehrkraftOfSourceKurs.getLehrkraefteOrder());
        kursLehrkraefteOfTargetKurs.add(targetKursLehrkraft);
        lehrkraefteOfTargetKurs.add(lehrkraftOfSourceKurs.getLehrkraft());
      }

      // Existiert der Kurs im jetzigen Semester bereits? Wenn ja, unverändert lassen.
      Optional<Kurs> existingTargetKursOptional =
          kursRepository.findBySemesterIdAndWochentagAndZeitBeginn(
              targetSemester.getSemesterId(),
              targetKurs.getWochentag(),
              targetKurs.getZeitBeginn());

      boolean targetKursAlreadyExists = false;
      if (existingTargetKursOptional.isPresent()) {
        List<Mitarbeiter> lehrkraefteOfExistingTargetKurs =
            kursLehrkraftRepository.findLehrkraefteByKursIdOrderByLehrkraefteOrder(
                existingTargetKursOptional.get().getKursId());
        targetKursAlreadyExists =
            existingTargetKursOptional
                .get()
                .isIdenticalWith(
                    targetKurs, lehrkraefteOfTargetKurs, lehrkraefteOfExistingTargetKurs);
      }

      if (!targetKursAlreadyExists) {
        // Kurs speichern
        kursRepository.save(targetKurs);
        kursLehrkraftRepository.saveAll(kursLehrkraefteOfTargetKurs);

        // Falls 2. Semester, auch Schüler bzw. Kurseinteilungen importieren
        if (targetSemester.getSemesterbezeichnung() == Semesterbezeichnung.ZWEITES_SEMESTER) {
          importKursanmeldungen(targetSemester, sourceKurs, targetKurs);
        }
      }
    }

    return ImportKurseResult.IMPORT_ERFOLGREICH;
  }

  private Optional<Semester> getSourceSemester(Semester targetSemester) {
    Optional<Semester> sourceSemesterOptional = Optional.empty();

    // 1. Semester -> Kurse vom 1. Semester vor einem Jahr importieren
    if (targetSemester.getSemesterbezeichnung() == Semesterbezeichnung.ERSTES_SEMESTER) {
      sourceSemesterOptional = semesterService.findSemesterOneYearBefore(targetSemester);
    }

    // 2. Semester (oder Kurse vom 1. Semester vor einem Jahr nicht vorhanden) -> Kurse und Schüler
    // vom 1. Semester importieren
    if (targetSemester.getSemesterbezeichnung() == Semesterbezeichnung.ZWEITES_SEMESTER
        || sourceSemesterOptional.isEmpty()) {
      sourceSemesterOptional = semesterService.findPreviousSemester(targetSemester);
    }

    return sourceSemesterOptional;
  }

  private void importKursanmeldungen(Semester targetSemester, Kurs sourceKurs, Kurs targetKurs) {
    List<Kursanmeldung> kursanmeldungenOfSourceKurs =
        kursanmeldungRepository.findByKursId(sourceKurs.getKursId());

    for (Kursanmeldung kursanmeldungOfSourceKurs : kursanmeldungenOfSourceKurs) {

      // Nur Kurse ohne Kursabmeldungen und nur für nicht abgemeldete Schüler importieren
      if (isAngemeldetForSemesterAndKurs(targetSemester, kursanmeldungOfSourceKurs)) {
        Kursanmeldung targetKursanmeldung =
            new Kursanmeldung(
                kursanmeldungOfSourceKurs.getSchueler(),
                targetKurs,
                targetSemester.getSemesterbeginn(),
                null,
                null);
        kursanmeldungRepository.save(targetKursanmeldung);

        // Semesterrechnungen updaten
        Schueler schueler = kursanmeldungOfSourceKurs.getSchueler();
        Angehoeriger rechnungsempfaenger = schueler.getRechnungsempfaenger();
        semesterrechnungService.calculateAndUpdateAnzahlWochenAndWochenbetrag(
            targetSemester, Optional.empty(), rechnungsempfaenger);
      }
    }
  }

  boolean isAngemeldetForSemesterAndKurs(Semester targetSemester, Kursanmeldung kursanmeldung) {
    Anmeldung anmeldung =
        anmeldungRepository
            .findBySchuelerIdOrderByAnmeldedatumDesc(kursanmeldung.getSchueler().getPersonId())
            .get(0);
    return kursanmeldung.getAbmeldedatum() == null
        && (anmeldung.getAbmeldedatum() == null
            || anmeldung.getAbmeldedatum().after(targetSemester.getSemesterbeginn()));
  }

  @SuppressWarnings("ExtractMethodRecommender")
  @Override
  public ExportListResult exportList(
      Listentyp listentyp,
      String listenTitel,
      File outputFile,
      Iterator<KursAndLehrkraefteAndNumberOfKursanmeldungen> rowIterator,
      Semester semester) {

    switch (listentyp) {
      case KURSLISTE_CSV -> {
        Function<KursAndLehrkraefteAndNumberOfKursanmeldungen, List<String>> columnsSupplier =
            kursAndLehrkraefteAndNumberOfKursanmeldungen -> {
              Kurs kurs = kursAndLehrkraefteAndNumberOfKursanmeldungen.kurs();
              String bemerkungen = "";
              if (checkNotEmpty(kurs.getBemerkungen())) {
                bemerkungen = kurs.getBemerkungen().replace(";", ",");
              }
              return List.of(
                  kurs.getKurstyp().getBezeichnung(),
                  kurs.getAltersbereich(),
                  kurs.getStufe(),
                  kurs.getWochentag().toString(),
                  kurs.getZeitBeginn().toString(),
                  kurs.getZeitEnde().toString(),
                  kurs.getKursort().getBezeichnung(),
                  getMitarbeiterShortAsStr(kursAndLehrkraefteAndNumberOfKursanmeldungen),
                  bemerkungen);
            };
        csvExportService.exportList(
            List.of(
                "Kurstyp", "Alter", "Stufe", "Tag", "Von", "Bis", "Ort", "Leitung", "Bemerkungen"),
            rowIterator,
            columnsSupplier,
            outputFile);
      }

      case KURSLISTE_WORD -> {
        WordTableLayout wordTableLayout = createWordTableLayout();
        String title1 =
            svmProperties2.getTheaterName()
                + "                              "
                + getSemesterTitle(semester);
        List<List<String>> headerColumnsRows =
            List.of(
                List.of("", "Kurstyp", "Alter", "Tag", "Leitung", "Bemerkungen"),
                List.of("", "", "Stufe", "Zeit", "Ort", ""));

        final int[] rowNumber = {0};
        Function<KursAndLehrkraefteAndNumberOfKursanmeldungen, List<List<String>>> columnsSupplier =
            kursAndLehrkraefteAndNumberOfKursanmeldungen -> {
              rowNumber[0]++;
              Kurs kurs = kursAndLehrkraefteAndNumberOfKursanmeldungen.kurs();
              List<String> kurstypLines =
                  splitStringIntoMultipleLines(kurs.getKurstyp().getBezeichnung(), 22, 2);
              List<String> bemerkungenLines =
                  splitStringIntoMultipleLines(kurs.getBemerkungen(), 16, 2);
              return List.of(
                  List.of(
                      Integer.toString(rowNumber[0]),
                      kurstypLines.get(0),
                      kurs.getAltersbereich(),
                      kurs.getWochentag().toString(),
                      getMitarbeiterShortAsStr(kursAndLehrkraefteAndNumberOfKursanmeldungen),
                      (!bemerkungenLines.isEmpty() ? bemerkungenLines.get(0) : "")),
                  List.of(
                      "",
                      ((kurstypLines.size() > 1) ? kurstypLines.get(1) : ""),
                      kurs.getStufe(),
                      asString(kurs.getZeitBeginn()) + " - " + asString(kurs.getZeitEnde()),
                      kurs.getKursort().getBezeichnung(),
                      ((bemerkungenLines.size() > 1) ? bemerkungenLines.get(1) : "")));
            };

        wordExportService.exportList(
            wordTableLayout,
            title1,
            listenTitel,
            headerColumnsRows,
            rowIterator,
            columnsSupplier,
            outputFile);
      }

      default -> throw new IllegalStateException("Unexpected value: " + listentyp);
    }

    return ExportListResult.LISTE_ERFOLGREICH_ERSTELLT;
  }

  private static String getMitarbeiterShortAsStr(
      KursAndLehrkraefteAndNumberOfKursanmeldungen kursAndLehrkraefteAndNumberOfKursanmeldungen) {
    return Mitarbeiter.getMitarbeiterShortAsStr(
        kursAndLehrkraefteAndNumberOfKursanmeldungen.lehrkraefte());
  }

  static String getSemesterTitle(Semester semester) {
    return (semester == null)
        ? ""
        : "Schuljahr " + semester.getSchuljahr() + ", " + semester.getSemesterbezeichnung();
  }

  private static WordTableLayout createWordTableLayout() {
    // Spaltenbreiten
    // ACHTUNG: Summe muss <= 11200 (wenn nicht anders möglich: <= 11500) sein (bei linkem
    // Default-Rand von 600)!
    //          Bei > 11200 hinten schmalerer Rand!
    //          Bei > 11500 Spaltenbreite durch Inhalt beeinflusst!!!
    List<Integer> columnWidths = List.of(500, 2100, 2100, 1900, 2800, 1600);

    List<List<CellLayout>> datasetRowCellLayouts =
        List.of(
            List.of(
                new CellLayout(false, 0, new int[] {0}),
                new CellLayout(false, 0, new int[] {0}),
                new CellLayout(false, 0, new int[] {22, 23, 24, 25, 26, 28}),
                new CellLayout(false, 0, new int[] {0}),
                new CellLayout(false, 0, new int[] {26, 27, 28, 29, 30, 32}),
                new CellLayout(false, 0, new int[] {16, 17, 18, 19, 20, 22})),
            List.of(
                new CellLayout(false, 0, new int[] {0}),
                new CellLayout(false, 0, new int[] {20, 21, 22, 23, 24, 26}),
                new CellLayout(false, 0, new int[] {22, 23, 24, 25, 26, 28}),
                new CellLayout(false, 0, new int[] {0}),
                new CellLayout(false, 0, new int[] {26, 27, 28, 29, 30, 32}),
                new CellLayout(false, 0, new int[] {16, 17, 18, 19, 20, 22})));

    return new WordTableLayout(columnWidths, datasetRowCellLayouts);
  }

  @Override
  @Transactional(readOnly = true)
  public MonatsstatistikKurse calculateMonatsstatistik(Calendar monatJahr) {

    Calendar beginOfMonth = determineStatistikMonatBeginn(monatJahr);
    Calendar endOfMonth = determineStatistikMonatEnde(monatJahr);

    List<Semester> allSemesters = semesterService.findAllSemesters();
    PreviousCurrentNextSemester previousCurrentNextSemester =
        semesterService.determinePreviousCurrentNextSemesterFor(endOfMonth, allSemesters);
    Optional<Semester> relevantesSemesterOptional =
        getRelevantesSemester(previousCurrentNextSemester);

    int numberOfLektionen = 0;
    if (relevantesSemesterOptional.isPresent()) {
      numberOfLektionen =
          kursanmeldungRepository
              .countKursanmeldungenBySemesterIdAndAnmeldedatumLeDatumAndAbmeldedatumGeDatum(
                  relevantesSemesterOptional.get().getSemesterId(), endOfMonth);
    }

    Map<Integer, Long> mapOfSchuelerIdAndNumberOfAnmeldungen =
        calculateNumberOfAnmeldungenGroupBySchueler(beginOfMonth, endOfMonth);

    Map<Integer, Long> mapOfSchuelerIdAndNumberOfAbmeldungen =
        calculateNumberOfAbmeldungenGroupBySchueler(beginOfMonth, endOfMonth);

    Map<Integer, Long> mapOfSchuelerIdAndNumberOfImpliziteAbmeldungen =
        calculateNumberOfImpliziteAbmeldungenGroupBySchueler(
            previousCurrentNextSemester,
            beginOfMonth,
            endOfMonth,
            mapOfSchuelerIdAndNumberOfAnmeldungen);

    Map<Integer, Long> mapOfSchuelerIdAndNumberOfAbmeldungenTotal =
        calculateNumberOfAbmeldungenTotalGroupBySchueler(
            mapOfSchuelerIdAndNumberOfAbmeldungen, mapOfSchuelerIdAndNumberOfImpliziteAbmeldungen);

    NumberOfAnmeldungenAndAbmeldungen numberOfAnmeldungenAndAbmeldungen =
        calculateNumberOfAnmeldungenAndAbmeldungen(
            mapOfSchuelerIdAndNumberOfAnmeldungen, mapOfSchuelerIdAndNumberOfAbmeldungenTotal);

    return new MonatsstatistikKurse(
        (int) numberOfAnmeldungenAndAbmeldungen.anmeldungen,
        (int) numberOfAnmeldungenAndAbmeldungen.abmeldungen,
        numberOfLektionen);
  }

  private static Calendar determineStatistikMonatBeginn(Calendar monatJahr) {
    return new GregorianCalendar(monatJahr.get(Calendar.YEAR), monatJahr.get(Calendar.MONTH), 1);
  }

  @SuppressWarnings("DuplicatedCode")
  private static Calendar determineStatistikMonatEnde(Calendar monatJahr) {
    Calendar statistikMonatEnde;
    if (monatJahr.get(Calendar.MONTH) == Calendar.DECEMBER) {
      statistikMonatEnde =
          new GregorianCalendar(monatJahr.get(Calendar.YEAR) + 1, Calendar.JANUARY, 1);
    } else {
      //noinspection MagicConstant
      statistikMonatEnde =
          new GregorianCalendar(monatJahr.get(Calendar.YEAR), monatJahr.get(Calendar.MONTH) + 1, 1);
    }
    statistikMonatEnde.add(Calendar.DAY_OF_YEAR, -1);
    return statistikMonatEnde;
  }

  private static Optional<Semester> getRelevantesSemester(
      PreviousCurrentNextSemester previousCurrentNextSemester) {
    Optional<Semester> currentSemesterOptional =
        previousCurrentNextSemester.currentSemesterOptional();
    if (currentSemesterOptional.isPresent()) {
      // Innerhalb Semester
      return currentSemesterOptional;
    } else {
      // Semesterferien (-> vorhergehendes Semester)
      return previousCurrentNextSemester.previousSemesterOptional();
    }
  }

  private Map<Integer, Long> calculateNumberOfAnmeldungenGroupBySchueler(
      Calendar beginOfMonth, Calendar endOfMonth) {
    return convertToMapOfIdAndCount(
        kursanmeldungRepository
            .countKursanmeldungenByAnmeldedatumGeBeginDatumAndAnmeldedatumLeEndDatumGroupBySchuelerId(
                beginOfMonth, endOfMonth));
  }

  private Map<Integer, Long> calculateNumberOfAbmeldungenGroupBySchueler(
      Calendar beginOfMonth, Calendar endOfMonth) {
    return convertToMapOfIdAndCount(
        kursanmeldungRepository
            .countKursanmeldungenByAbmeldedatumGeBeginDatumAndAbmeldedatumLeEndDatumGroupBySchuelerId(
                beginOfMonth, endOfMonth));
  }

  private static Map<Integer, Long> calculateNumberOfAbmeldungenTotalGroupBySchueler(
      Map<Integer, Long> mapOfSchuelerIdAndNumberOfAbmeldungen,
      Map<Integer, Long> mapOfSchuelerIdAndNumberOfImpliziteAbmeldungen) {
    return mergeMaps(
        mapOfSchuelerIdAndNumberOfAbmeldungen, mapOfSchuelerIdAndNumberOfImpliziteAbmeldungen);
  }

  private static Map<Integer, Long> convertToMapOfIdAndCount(List<IdAndCount> idAndCountList) {
    return idAndCountList.stream().collect(Collectors.toMap(IdAndCount::id, IdAndCount::count));
  }

  private Map<Integer, Long> calculateNumberOfImpliziteAbmeldungenGroupBySchueler(
      PreviousCurrentNextSemester previousCurrentNextSemester,
      Calendar beginOfMonth,
      Calendar endOfMonth,
      Map<Integer, Long> mapOfSchuelerIdAndNumberOfAnmeldungen) {

    // Implizite Abmeldungen nur berücksichtigen, falls bereits Schüler fürs nächste Semester
    // erfasst wurden
    boolean monatContainsSemesterbeginn =
        checkIfMonatContainsSemesterbeginn(previousCurrentNextSemester, beginOfMonth, endOfMonth);
    Optional<Semester> previousSemesterOptional =
        previousCurrentNextSemester.previousSemesterOptional();
    Map<Integer, Long> mapOfSchuelerIdAndNumberOfImplicitAbmeldungen;
    if (monatContainsSemesterbeginn
        && !mapOfSchuelerIdAndNumberOfAnmeldungen.isEmpty()
        && previousSemesterOptional.isPresent()) {
      mapOfSchuelerIdAndNumberOfImplicitAbmeldungen =
          convertToMapOfIdAndCount(
              kursanmeldungRepository
                  .countKursanmeldungenBySemesterIdAndAbmeldedatumIsNullGroupBySchuelerId(
                      previousSemesterOptional.get().getSemesterId()));
    } else {
      mapOfSchuelerIdAndNumberOfImplicitAbmeldungen = Map.of();
    }
    return mapOfSchuelerIdAndNumberOfImplicitAbmeldungen;
  }

  private static boolean checkIfMonatContainsSemesterbeginn(
      PreviousCurrentNextSemester previousCurrentNextSemester,
      Calendar beginOfMonth,
      Calendar endOfMonth) {
    Optional<Semester> previousSemesterOptional =
        previousCurrentNextSemester.previousSemesterOptional();
    return previousSemesterOptional
        .filter(
            semester ->
                !semester.getSemesterbeginn().before(beginOfMonth)
                    && !semester.getSemesterbeginn().after(endOfMonth))
        .isPresent();
  }

  static NumberOfAnmeldungenAndAbmeldungen calculateNumberOfAnmeldungenAndAbmeldungen(
      Map<Integer, Long> anmeldungen, Map<Integer, Long> abmeldungen) {

    Map<Integer, Long> saldo = new HashMap<>(anmeldungen);
    abmeldungen.forEach((schuelerId, count) -> saldo.merge(schuelerId, -count, Long::sum));

    long numberOfAnmeldungen = 0;
    long numberOfAbmeldungen = 0;

    for (long diff : saldo.values()) {
      if (diff > 0) {
        numberOfAnmeldungen += diff;
      } else if (diff < 0) {
        numberOfAbmeldungen -= diff;
      }
    }

    return new NumberOfAnmeldungenAndAbmeldungen(numberOfAnmeldungen, numberOfAbmeldungen);
  }

  static Map<Integer, Long> mergeMaps(Map<Integer, Long> leftMap, Map<Integer, Long> rightMap) {
    Map<Integer, Long> result = new HashMap<>(leftMap);

    for (Map.Entry<Integer, Long> entry : rightMap.entrySet()) {
      result.merge(entry.getKey(), entry.getValue(), Long::sum);
    }
    return result;
  }
}
