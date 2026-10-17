package ch.metzenthin.svm.service.impl;

import static ch.metzenthin.svm.common.utils.Converter.asString;
import static ch.metzenthin.svm.common.utils.Converter.nullAsEmptyString;
import static ch.metzenthin.svm.common.utils.SvmStringUtils.splitStringIntoMultipleLines;

import ch.metzenthin.svm.common.datatypes.Anrede;
import ch.metzenthin.svm.common.datatypes.Listentyp;
import ch.metzenthin.svm.config.SvmProperties2;
import ch.metzenthin.svm.domain.model.MitarbeiterAndMitarbeiterCode;
import ch.metzenthin.svm.domain.model.MitarbeiterAndMitarbeiterCodes;
import ch.metzenthin.svm.domain.model.MitarbeiterAndMitarbeiterCodesAsStringAndSelektiert;
import ch.metzenthin.svm.domain.model.NachnameGratiskindFormatter;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Code;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;
import ch.metzenthin.svm.persistence.entities.MitarbeiterMitarbeiterCode;
import ch.metzenthin.svm.persistence.repository.AdresseRepository;
import ch.metzenthin.svm.persistence.repository.KursLehrkraftRepository;
import ch.metzenthin.svm.persistence.repository.MitarbeiterMitarbeiterCodeRepository;
import ch.metzenthin.svm.persistence.repository.MitarbeiterRepository;
import ch.metzenthin.svm.service.MitarbeiterService;
import ch.metzenthin.svm.service.export.csv.CsvExportService;
import ch.metzenthin.svm.service.export.word.CellLayout;
import ch.metzenthin.svm.service.export.word.WordExportService;
import ch.metzenthin.svm.service.export.word.WordTableLayout;
import ch.metzenthin.svm.service.result.DeleteMitarbeiterResult;
import ch.metzenthin.svm.service.result.ExportListResult;
import ch.metzenthin.svm.service.result.SaveMitarbeiterResult;
import ch.metzenthin.svm.service.result.SaveResultAndIdOfSavedEntity;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Martin Schraner
 */
@Service
public class MitarbeiterServiceImpl implements MitarbeiterService {

  private static final String ANREDE = "Anrede";
  private static final String NAME = "Name";
  private static final String NACHNAME = "Nachname";
  private static final String VORNAME = "Vorname";
  private static final String FESTNETZ = "Festnetz";
  private static final String STRASSE_NR = "Strasse/Nr.";
  private static final String PLZ = "PLZ";
  private static final String ORT = "Ort";
  private static final String PLZ_ORT = "PLZ/Ort";
  private static final String NATEL = "Natel";
  private static final String GEBURTSDATUM = "Geburtsdatum";
  private static final String E_MAIL = "E-Mail";
  private static final String VERTRETUNGSMOEGLICHKEITEN = "Vertretungsmöglichkeiten";
  private static final String AHV_NUMMER = "AHV-Nummer";
  private static final String IBAN_NUMMER = "IBAN-Nummer";
  private static final String LEHRKRAFT = "Lehrkraft";
  private static final String CODES = "Codes";
  private static final String BEMERKUNGEN = "Bemerkungen";
  private static final String AKTIV = "aktiv";

  private final MitarbeiterRepository mitarbeiterRepository;
  private final AdresseRepository adresseRepository;
  private final MitarbeiterMitarbeiterCodeRepository mitarbeiterMitarbeiterCodeRepository;
  private final KursLehrkraftRepository kursLehrkraftRepository;
  private final WordExportService wordExportService;
  private final CsvExportService csvExportService;
  private final SvmProperties2 svmProperties2;

  public MitarbeiterServiceImpl(
      MitarbeiterRepository mitarbeiterRepository,
      AdresseRepository adresseRepository,
      MitarbeiterMitarbeiterCodeRepository mitarbeiterMitarbeiterCodeRepository,
      KursLehrkraftRepository kursLehrkraftRepository,
      WordExportService wordExportService,
      CsvExportService csvExportService,
      SvmProperties2 svmProperties2) {
    this.mitarbeiterRepository = mitarbeiterRepository;
    this.adresseRepository = adresseRepository;
    this.mitarbeiterMitarbeiterCodeRepository = mitarbeiterMitarbeiterCodeRepository;
    this.kursLehrkraftRepository = kursLehrkraftRepository;
    this.wordExportService = wordExportService;
    this.csvExportService = csvExportService;
    this.svmProperties2 = svmProperties2;
  }

  @Override
  @Transactional(readOnly = true)
  public List<Mitarbeiter> findAktiveLehrkraefte() {
    return mitarbeiterRepository.findByLehrkraftTrueAndAktivTrueOrderByNachnameVorname();
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<MitarbeiterAndMitarbeiterCodes> findMitarbeiterAndMitarbeiterCodes(
      int mitarbeiterId) {

    List<MitarbeiterAndMitarbeiterCode> mitarbeiterAndMitarbeiterCodeList =
        mitarbeiterRepository.findByMitarbeiterAndMitarbeiterCodesByMitarbeiterId(mitarbeiterId);

    if (mitarbeiterAndMitarbeiterCodeList.isEmpty()) {
      return Optional.empty();
    }

    List<MitarbeiterAndMitarbeiterCodes> mitarbeiterAndMitarbeiterCodesList =
        convertToMitarbeiterAndMitarbeiterCodesList(mitarbeiterAndMitarbeiterCodeList);

    if (mitarbeiterAndMitarbeiterCodesList.size() > 1) {
      throw new IllegalStateException();
    }

    return Optional.of(mitarbeiterAndMitarbeiterCodesList.get(0));
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

  @Override
  @Transactional
  public SaveResultAndIdOfSavedEntity<SaveMitarbeiterResult> saveMitarbeiter(
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
      return new SaveResultAndIdOfSavedEntity<>(
          SaveMitarbeiterResult.MITARBEITER_BEREITS_ERFASST, -1);
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

    return new SaveResultAndIdOfSavedEntity<>(
        SaveMitarbeiterResult.SPEICHERN_ERFOLGREICH, mitarbeiter.getPersonId());
  }

  @Override
  @Transactional
  public DeleteMitarbeiterResult deleteMitarbeiter(Mitarbeiter mitarbeiterToBeDeleted) {

    int numberOfKurse =
        kursLehrkraftRepository.countKurseByLehrkraftId(mitarbeiterToBeDeleted.getPersonId());
    if (numberOfKurse > 0) {
      return DeleteMitarbeiterResult.MITARBEITER_VON_KURS_REFERENZIERT;
    }

    mitarbeiterMitarbeiterCodeRepository.deleteByMitarbeiterId(
        mitarbeiterToBeDeleted.getPersonId());
    mitarbeiterRepository.delete(mitarbeiterToBeDeleted);

    return DeleteMitarbeiterResult.LOESCHEN_ERFOLGREICH;
  }

  @Override
  public ExportListResult exportList(
      Listentyp listentyp,
      String listenTitel,
      File outputFile,
      Iterator<MitarbeiterAndMitarbeiterCodesAsStringAndSelektiert> rowIterator) {

    switch (listentyp) {
      case MITARBEITER_ADRESSLISTE_MIT_GEBURTSDATUM ->
          exportAdresslisteWordWithGeburtsdatum(listenTitel, outputFile, rowIterator);

      case MITARBEITER_ADRESSLISTE_OHNE_GEBURTSDATUM ->
          exportAdresslisteWordWithoutGeburtsdatum(listenTitel, outputFile, rowIterator);

      case MITARBEITER_ADRESSLISTE_MIT_GEBURTSDATUM_AHV_IBAN_VERTRETUNGSMOEGLICHKEITEN ->
          exportAdresslisteWordWithGeburtsdatumAhvIbanVertretungsmoeglichkeiten(
              listenTitel, outputFile, rowIterator);

      case MITARBEITER_ADRESSETIKETTEN -> exportAdressetikettenCsv(outputFile, rowIterator);

      case MITARBEITER_LISTE_NAME_EINSPALTIG_CSV ->
          exportAdresslisteCsv(outputFile, rowIterator, true);

      case MITARBEITER_LISTE_NAME_ZWEISPALTIG_CSV ->
          exportAdresslisteCsv(outputFile, rowIterator, false);

      default -> throw new IllegalStateException("Unexpected value: " + listentyp);
    }

    return ExportListResult.LISTE_ERFOLGREICH_ERSTELLT;
  }

  private void exportAdresslisteWordWithGeburtsdatum(
      String listenTitel,
      File outputFile,
      Iterator<MitarbeiterAndMitarbeiterCodesAsStringAndSelektiert> rowIterator) {
    WordTableLayout wordTableLayout = createMitarbeiterAdresslisteWordTableLayout();
    String title1 = getTitle();
    List<List<String>> headerColumnsRows =
        List.of(
            List.of("", NAME, VORNAME, FESTNETZ, GEBURTSDATUM),
            List.of("", STRASSE_NR, PLZ_ORT, NATEL, E_MAIL));

    Function<MitarbeiterAndMitarbeiterCodesAsStringAndSelektiert, List<List<String>>>
        columnsSupplier = getMitarbeiterAdresslisteRowFunction(true);

    wordExportService.exportList(
        wordTableLayout,
        title1,
        listenTitel,
        headerColumnsRows,
        rowIterator,
        columnsSupplier,
        outputFile);
  }

  private static WordTableLayout createMitarbeiterAdresslisteWordTableLayout() {
    // Spaltenbreiten
    // ACHTUNG: Summe muss <= 11200 (wenn nicht anders möglich: <= 11500) sein (bei linkem
    // Default-Rand von 600)!
    //          Bei > 11200 hinten schmalerer Rand!
    //          Bei > 11500 Spaltenbreite durch Inhalt beeinflusst!!!
    List<Integer> columnWidths = List.of(550, 2700, 2900, 1700, 3350);

    List<List<CellLayout>> datasetRowCellLayouts =
        List.of(
            List.of(
                new CellLayout(false, 0, new int[] {0}),
                new CellLayout(true, 0, new int[] {23, 24, 25, 26, 27, 29}),
                new CellLayout(true, 0, new int[] {24, 25, 26, 27, 28, 30}),
                new CellLayout(false, 0, new int[] {15, 16, 17, 18, 19, 20}),
                new CellLayout(false, 0, new int[] {35, 36, 37, 38, 39, 41})),
            List.of(
                new CellLayout(false, 0, new int[] {0}),
                new CellLayout(false, 0, new int[] {24, 25, 26, 27, 28, 30}),
                new CellLayout(false, 0, new int[] {25, 26, 27, 28, 29, 31}),
                new CellLayout(false, 0, new int[] {15, 16, 17, 18, 19, 20}),
                new CellLayout(false, 0, new int[] {35, 36, 37, 38, 39, 41})));

    return new WordTableLayout(columnWidths, datasetRowCellLayouts);
  }

  private String getTitle() {
    Calendar today = new GregorianCalendar();
    return svmProperties2.getTheaterName()
        + "                                                                           "
        + asString(today);
  }

  private static Function<MitarbeiterAndMitarbeiterCodesAsStringAndSelektiert, List<List<String>>>
      getMitarbeiterAdresslisteRowFunction(boolean withGeburtsdatum) {
    final int[] rowNumber = {0};
    return mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert -> {
      rowNumber[0]++;
      Mitarbeiter mitarbeiter =
          mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert.getMitarbeiter();
      Adresse adresse = mitarbeiter.getAdresse();
      return List.of(
          List.of(
              Integer.toString(rowNumber[0]),
              mitarbeiter.getNachname(),
              mitarbeiter.getVorname(),
              nullAsEmptyString(mitarbeiter.getFestnetz()),
              (withGeburtsdatum)
                  ? nullAsEmptyString(asString(mitarbeiter.getGeburtsdatum()))
                  : mitarbeiter.getEmailToBeDisplayedInWord()),
          List.of(
              "",
              adresse == null ? "" : nullAsEmptyString(adresse.getStrHausnummer()),
              adresse == null ? "" : adresse.getPlzOrt(),
              nullAsEmptyString(mitarbeiter.getNatel()),
              (withGeburtsdatum)
                  ? nullAsEmptyString(mitarbeiter.getEmailToBeDisplayedInWord())
                  : ""));
    };
  }

  private void exportAdresslisteWordWithoutGeburtsdatum(
      String listenTitel,
      File outputFile,
      Iterator<MitarbeiterAndMitarbeiterCodesAsStringAndSelektiert> rowIterator) {
    WordTableLayout wordTableLayout = createMitarbeiterAdresslisteWordTableLayout();
    String title1 = getTitle();
    List<List<String>> headerColumnsRows =
        List.of(
            List.of("", NAME, VORNAME, FESTNETZ, E_MAIL),
            List.of("", STRASSE_NR, PLZ_ORT, NATEL, ""));

    Function<MitarbeiterAndMitarbeiterCodesAsStringAndSelektiert, List<List<String>>>
        columnsSupplier = getMitarbeiterAdresslisteRowFunction(false);

    wordExportService.exportList(
        wordTableLayout,
        title1,
        listenTitel,
        headerColumnsRows,
        rowIterator,
        columnsSupplier,
        outputFile);
  }

  private void exportAdresslisteWordWithGeburtsdatumAhvIbanVertretungsmoeglichkeiten(
      String listenTitel,
      File outputFile,
      Iterator<MitarbeiterAndMitarbeiterCodesAsStringAndSelektiert> rowIterator) {
    WordTableLayout wordTableLayout =
        createMitarbeiterAdresslisteWithGeburtsdatumAhvIbanVertretungsmoeglichkeitenWordTableLayout();
    String title1 = getTitle();
    List<List<String>> headerColumnsRows =
        List.of(
            List.of("", NAME, VORNAME, FESTNETZ, GEBURTSDATUM, VERTRETUNGSMOEGLICHKEITEN),
            List.of("", STRASSE_NR, PLZ_ORT, NATEL, AHV_NUMMER, ""),
            List.of("", "", E_MAIL, "", IBAN_NUMMER, ""));

    Function<MitarbeiterAndMitarbeiterCodesAsStringAndSelektiert, List<List<String>>>
        columnsSupplier =
            getMitarbeiterAdresslisteWithGeburtsdatumAhvIbanVertretungsmoeglichkeitenRowFunction();

    wordExportService.exportList(
        wordTableLayout,
        title1,
        listenTitel,
        headerColumnsRows,
        rowIterator,
        columnsSupplier,
        outputFile);
  }

  private static WordTableLayout
      createMitarbeiterAdresslisteWithGeburtsdatumAhvIbanVertretungsmoeglichkeitenWordTableLayout() {
    // Spaltenbreiten
    // ACHTUNG: Summe muss <= 11200 (wenn nicht anders möglich: <= 11500) sein (bei linkem
    // Default-Rand von 600)!
    //          Bei > 11200 hinten schmalerer Rand!
    //          Bei > 11500 Spaltenbreite durch Inhalt beeinflusst!!!
    List<Integer> columnWidths = List.of(550, 2300, 2500, 1400, 1750, 3000);

    List<List<CellLayout>> datasetRowCellLayouts =
        List.of(
            List.of(
                new CellLayout(false, 0, new int[] {0}),
                new CellLayout(true, 0, new int[] {21, 22, 23, 24, 25, 27}),
                new CellLayout(true, 0, new int[] {22, 23, 24, 25, 26, 28}),
                new CellLayout(false, 0, new int[] {13, 13, 13, 14, 16, 18}),
                new CellLayout(false, 0, new int[] {0}),
                new CellLayout(false, 0, new int[] {29, 30, 31, 32, 33, 35})),
            List.of(
                new CellLayout(false, 0, new int[] {0}),
                new CellLayout(false, 0, new int[] {22, 23, 24, 25, 26, 28}),
                new CellLayout(false, 0, new int[] {23, 24, 25, 26, 27, 29}),
                new CellLayout(false, 0, new int[] {13, 13, 13, 14, 16, 18}),
                new CellLayout(false, 0, new int[] {0}),
                new CellLayout(false, 0, new int[] {29, 30, 31, 32, 33, 35})),
            List.of(
                new CellLayout(false, 0, new int[] {0}),
                new CellLayout(false, 0, new int[] {0}),
                new CellLayout(false, 2, new int[] {38, 39, 40, 41, 43, 45}),
                new CellLayout(false, 0, new int[] {0}),
                new CellLayout(false, 2, new int[] {0}),
                new CellLayout(false, 0, new int[] {0})));

    return new WordTableLayout(columnWidths, datasetRowCellLayouts);
  }

  private static Function<MitarbeiterAndMitarbeiterCodesAsStringAndSelektiert, List<List<String>>>
      getMitarbeiterAdresslisteWithGeburtsdatumAhvIbanVertretungsmoeglichkeitenRowFunction() {
    final int[] rowNumber = {0};
    return mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert -> {
      rowNumber[0]++;
      Mitarbeiter mitarbeiter =
          mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert.getMitarbeiter();
      Adresse adresse = mitarbeiter.getAdresse();
      String vertretungsmoeglichkeiten =
          mitarbeiter.getVertretungsmoeglichkeitenLineBreaksReplacedBySemicolonOrPeriod();
      List<String> vertretungsmoeglichkeitenSplitLines =
          splitVertretungsmoeglichkeitenIntoMultipleLines(vertretungsmoeglichkeiten);
      return List.of(
          List.of(
              Integer.toString(rowNumber[0]),
              mitarbeiter.getNachname(),
              mitarbeiter.getVorname(),
              nullAsEmptyString(mitarbeiter.getFestnetz()),
              nullAsEmptyString(asString(mitarbeiter.getGeburtsdatum())),
              (!vertretungsmoeglichkeitenSplitLines.isEmpty())
                  ? vertretungsmoeglichkeitenSplitLines.get(0)
                  : ""),
          List.of(
              "",
              adresse == null ? "" : nullAsEmptyString(adresse.getStrHausnummer()),
              adresse == null ? "" : adresse.getPlz() + " " + adresse.getOrt(),
              nullAsEmptyString(mitarbeiter.getNatel()),
              nullAsEmptyString(mitarbeiter.getAhvNummer()),
              (vertretungsmoeglichkeitenSplitLines.size() > 1)
                  ? vertretungsmoeglichkeitenSplitLines.get(1)
                  : ""),
          List.of(
              "",
              "",
              nullAsEmptyString(mitarbeiter.getEmailToBeDisplayedInWord()),
              "",
              nullAsEmptyString(mitarbeiter.getIbanNummer()),
              ""));
    };
  }

  private static List<String> splitVertretungsmoeglichkeitenIntoMultipleLines(
      String vertretungsmoeglichkeiten) {
    // Auf mehrere Zeilen aufzusplittende Felder:
    int maxLengthPerLine;
    if (vertretungsmoeglichkeiten == null || vertretungsmoeglichkeiten.length() <= 58) {
      maxLengthPerLine = 29;
    } else if (vertretungsmoeglichkeiten.length() <= 70) {
      maxLengthPerLine = 35;
    } else if (vertretungsmoeglichkeiten.length() <= 80) {
      maxLengthPerLine = 40;
    } else if (vertretungsmoeglichkeiten.length() <= 90) {
      maxLengthPerLine = 45;
    } else if (vertretungsmoeglichkeiten.length() <= 100) {
      maxLengthPerLine = 50;
    } else {
      maxLengthPerLine = 60;
    }
    return splitStringIntoMultipleLines(vertretungsmoeglichkeiten, maxLengthPerLine, 2);
  }

  private void exportAdressetikettenCsv(
      File outputFile, Iterator<MitarbeiterAndMitarbeiterCodesAsStringAndSelektiert> rowIterator) {
    Iterator<MitarbeiterAndMitarbeiterCodesAsStringAndSelektiert>
        rowIteratorMitarbeiterWithAdresse = getIteratorMitarbeiterWithAdresse(rowIterator);

    NachnameGratiskindFormatter nachnameGratiskindFormatter = new NachnameGratiskindFormatter();
    Function<MitarbeiterAndMitarbeiterCodesAsStringAndSelektiert, List<String>> columnsSupplier =
        mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert -> {
          Mitarbeiter mitarbeiter =
              mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert.getMitarbeiter();
          Adresse adresse = mitarbeiter.getAdresse();
          return List.of(
              (mitarbeiter.getAnrede() == Anrede.KEINE ? "" : mitarbeiter.getAnrede().toString()),
              mitarbeiter.getVorname(),
              nachnameGratiskindFormatter.format(mitarbeiter.getNachname()),
              adresse.getStrasseHausnummer(),
              adresse.getPlz(),
              adresse.getOrt());
        };

    csvExportService.exportList(
        List.of(ANREDE, VORNAME, NACHNAME, STRASSE_NR, PLZ, ORT),
        rowIteratorMitarbeiterWithAdresse,
        columnsSupplier,
        outputFile);
  }

  private static Iterator<MitarbeiterAndMitarbeiterCodesAsStringAndSelektiert>
      getIteratorMitarbeiterWithAdresse(
          Iterator<MitarbeiterAndMitarbeiterCodesAsStringAndSelektiert> rowIterator) {

    ArrayList<MitarbeiterAndMitarbeiterCodesAsStringAndSelektiert> mitarbeiterWithAdresseList =
        new ArrayList<>();
    while (rowIterator.hasNext()) {
      MitarbeiterAndMitarbeiterCodesAsStringAndSelektiert next = rowIterator.next();
      if (next.getMitarbeiter().getAdresse() != null) {
        mitarbeiterWithAdresseList.add(next);
      }
    }
    return mitarbeiterWithAdresseList.iterator();
  }

  private void exportAdresslisteCsv(
      File outputFile,
      Iterator<MitarbeiterAndMitarbeiterCodesAsStringAndSelektiert> rowIterator,
      boolean nameEinspaltig) {

    Function<MitarbeiterAndMitarbeiterCodesAsStringAndSelektiert, List<String>> columnsSupplier =
        mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert ->
            getDataColumnsAdresslisteCsv(
                mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert, nameEinspaltig);

    List<String> headerColumns = getHeaderColumnsAdresslisteCsv(nameEinspaltig);
    csvExportService.exportList(headerColumns, rowIterator, columnsSupplier, outputFile);
  }

  private static List<String> getHeaderColumnsAdresslisteCsv(boolean nameEinspaltig) {
    List<String> headerColumns = new ArrayList<>();
    if (nameEinspaltig) {
      headerColumns.add(NAME);
    } else {
      headerColumns.add(NACHNAME);
      headerColumns.add(VORNAME);
    }
    headerColumns.addAll(
        Arrays.asList(
            STRASSE_NR,
            PLZ_ORT,
            FESTNETZ,
            NATEL,
            E_MAIL,
            GEBURTSDATUM,
            AHV_NUMMER,
            IBAN_NUMMER,
            LEHRKRAFT,
            CODES,
            VERTRETUNGSMOEGLICHKEITEN,
            BEMERKUNGEN,
            AKTIV));
    return headerColumns;
  }

  private static List<String> getDataColumnsAdresslisteCsv(
      MitarbeiterAndMitarbeiterCodesAsStringAndSelektiert
          mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert,
      boolean nameEinspaltig) {

    Mitarbeiter mitarbeiter = mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert.getMitarbeiter();
    Adresse adresse = mitarbeiter.getAdresse();
    List<String> dataColumns = new ArrayList<>();
    if (nameEinspaltig) {
      dataColumns.add(mitarbeiter.getNachname() + " " + mitarbeiter.getVorname());
    } else {
      dataColumns.add(mitarbeiter.getNachname());
      dataColumns.add(mitarbeiter.getVorname());
    }
    dataColumns.addAll(
        Arrays.asList(
            adresse == null ? "" : adresse.getStrHausnummer(),
            adresse == null ? "" : adresse.getPlzOrt(),
            nullAsEmptyString(mitarbeiter.getFestnetz()),
            nullAsEmptyString(mitarbeiter.getNatel()),
            replaceSemicolonByComma(mitarbeiter.getEmail()),
            nullAsEmptyString(asString(mitarbeiter.getGeburtsdatum())),
            nullAsEmptyString(mitarbeiter.getAhvNummer()),
            nullAsEmptyString(mitarbeiter.getIbanNummer()),
            mitarbeiter.isLehrkraft() ? "ja" : "nein",
            mitarbeiterAndMitarbeiterCodesAsStringAndSelektiert.getMitarbeiterCodesAsString(),
            replaceSemicolonByComma(
                mitarbeiter.getVertretungsmoeglichkeitenLineBreaksReplacedByCommaOrPeriod()),
            replaceSemicolonByComma(mitarbeiter.getBemerkungenLineBreaksReplacedByCommaOrPeriod()),
            mitarbeiter.isAktiv() ? "ja" : "nein"));
    return dataColumns;
  }

  private static String replaceSemicolonByComma(String string) {
    if (string == null || string.isEmpty()) {
      return "";
    }
    return string.replace(";", ",");
  }
}
