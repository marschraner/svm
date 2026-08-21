package ch.metzenthin.svm.service.impl;

import static ch.metzenthin.svm.common.utils.Converter.asString;
import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;
import static ch.metzenthin.svm.common.utils.SvmStringUtils.splitStringIntoMultipleLines;

import ch.metzenthin.svm.common.datatypes.Listentyp;
import ch.metzenthin.svm.config.SvmProperties2;
import ch.metzenthin.svm.domain.model.IdAndCount;
import ch.metzenthin.svm.domain.model.KursAndLehrkraefteAndNumberOfKursanmeldungen;
import ch.metzenthin.svm.domain.model.KursIdAndLehrkraft;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Kurs;
import ch.metzenthin.svm.persistence.entities.KursLehrkraft;
import ch.metzenthin.svm.persistence.entities.Kursanmeldung;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import ch.metzenthin.svm.persistence.entities.Semester;
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
import ch.metzenthin.svm.service.result.SaveKursResult;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
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
  public SaveKursResult saveKurs(Kurs kurs, Mitarbeiter lehrkraft1, Mitarbeiter lehrkraft2) {

    if (lehrkraft1 == null) {
      return SaveKursResult.KEINE_LEHRKRAEFTE_ERFASST;
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
      return SaveKursResult.KURS_BEREITS_ERFASST;
    }

    int numberOfLektionsgebuehren =
        lektionsgebuehrenRepository.countByLektionslaenge(kurs.getKurslaenge());
    if (numberOfLektionsgebuehren == 0) {
      return SaveKursResult.LEKTIONSGEBUEHREN_NICHT_ERFASST;
    }

    if (semesterRepository.countBySemesterId(kurs.getSemester().getSemesterId()) == 0) {
      return SaveKursResult.SEMESTER_DURCH_ANDEREN_BENUTZER_GELOESCHT;
    }

    if (kurstypRepository.countByKurstypId(kurs.getKurstyp().getKurstypId()) == 0) {
      return SaveKursResult.KURSTYP_DURCH_ANDEREN_BENUTZER_GELOESCHT;
    }

    if (kursortRepository.countByKursortId(kurs.getKursort().getKursortId()) == 0) {
      return SaveKursResult.KURSORT_DURCH_ANDEREN_BENUTZER_GELOESCHT;
    }

    Optional<Mitarbeiter> lehrkraft1Optional =
        mitarbeiterRepository.findById(lehrkraft1.getPersonId());
    if (lehrkraft1Optional.isEmpty()) {
      return SaveKursResult.LEHRKRAFT_DURCH_ANDEREN_BENUTZER_GELOESCHT;
    }
    lehrkraft1 = lehrkraft1Optional.get();

    if (lehrkraft2 != null) {
      Optional<Mitarbeiter> lehrkraft2Optional =
          mitarbeiterRepository.findById(lehrkraft2.getPersonId());
      if (lehrkraft2Optional.isEmpty()) {
        return SaveKursResult.LEHRKRAFT_DURCH_ANDEREN_BENUTZER_GELOESCHT;
      }
      lehrkraft2 = lehrkraft2Optional.get();
    }

    kurs = kursRepository.save(kurs);

    createOrUpdateKursLehrkraefte(kurs, lehrkraft1, lehrkraft2);

    return SaveKursResult.SPEICHERN_ERFOLGREICH;
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
    Optional<Semester> nextSemesterOptional =
        semesterService.findNaechstesSemester(currentSemester);

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
                + getSemesterTitel(semester);
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

  private static String getSemesterTitel(Semester semester) {
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
}
