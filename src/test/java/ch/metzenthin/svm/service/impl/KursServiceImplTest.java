package ch.metzenthin.svm.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import ch.metzenthin.svm.common.datatypes.Anrede;
import ch.metzenthin.svm.common.datatypes.Listentyp;
import ch.metzenthin.svm.common.datatypes.Semesterbezeichnung;
import ch.metzenthin.svm.common.datatypes.Wochentag;
import ch.metzenthin.svm.domain.model.IdAndCount;
import ch.metzenthin.svm.domain.model.KursAndLehrkraefteAndNumberOfKursanmeldungen;
import ch.metzenthin.svm.domain.model.KursIdAndLehrkraft;
import ch.metzenthin.svm.persistence.entities.Kurs;
import ch.metzenthin.svm.persistence.entities.KursLehrkraft;
import ch.metzenthin.svm.persistence.entities.Kursort;
import ch.metzenthin.svm.persistence.entities.Kurstyp;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.persistence.repository.KursLehrkraftRepository;
import ch.metzenthin.svm.persistence.repository.KursRepository;
import ch.metzenthin.svm.persistence.repository.KursanmeldungRepository;
import ch.metzenthin.svm.persistence.repository.KursortRepository;
import ch.metzenthin.svm.persistence.repository.KurstypRepository;
import ch.metzenthin.svm.persistence.repository.MitarbeiterRepository;
import ch.metzenthin.svm.persistence.repository.SemesterRepository;
import ch.metzenthin.svm.service.ServiceTestConfiguration;
import ch.metzenthin.svm.service.result.ImportKurseResult;
import ch.metzenthin.svm.service.result.SaveKursResult;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

/**
 * @author Hans Stamm
 */
@DataJpaTest
@ContextConfiguration(classes = ServiceTestConfiguration.class)
@Sql(scripts = "classpath:KursRepositoryTest_Create.sql")
@Sql(
    scripts = "classpath:KursRepositoryTest_Delete.sql",
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class KursServiceImplTest {

  @Autowired private KursServiceImpl kursService;
  @Autowired private KursRepository kursRepository;
  @Autowired private KursortRepository kursortRepository;
  @Autowired private KurstypRepository kurstypRepository;
  @Autowired private KursanmeldungRepository kursanmeldungRepository;
  @Autowired private KursLehrkraftRepository kursLehrkraftRepository;
  @Autowired private SemesterRepository semesterRepository;
  @Autowired private MitarbeiterRepository mitarbeiterRepository;
  @PersistenceContext private EntityManager entityManager;

  @TempDir Path tempDir;

  @Test
  void testExistsKursByLektionslaenge() {
    assertFalse(kursService.existsKursByLektionslaenge(1));
    assertTrue(kursService.existsKursByLektionslaenge(60));
  }

  @Test
  void testFindAllKurseAndLehrkraefteAndNumberOfKursanmeldungenForSemester() {
    List<KursAndLehrkraefteAndNumberOfKursanmeldungen>
        kurseAndLehrkraefteAndNumberOfSchuelerForSemester101 =
            kursService.findAllKurseAndLehrkraefteAndNumberOfKursanmeldungenForSemester(101);
    assertEquals(1, kurseAndLehrkraefteAndNumberOfSchuelerForSemester101.size());
    assertEquals(
        1, kurseAndLehrkraefteAndNumberOfSchuelerForSemester101.get(0).numberOfKursanmeldungen());
    assertEquals(
        1, kurseAndLehrkraefteAndNumberOfSchuelerForSemester101.get(0).lehrkraefte().size());

    List<KursAndLehrkraefteAndNumberOfKursanmeldungen>
        kurseAndLehrkraefteAndNumberOfSchuelerForSemester102 =
            kursService.findAllKurseAndLehrkraefteAndNumberOfKursanmeldungenForSemester(102);
    assertEquals(2, kurseAndLehrkraefteAndNumberOfSchuelerForSemester102.size());
    KursAndLehrkraefteAndNumberOfKursanmeldungen kursAndLehrkraefteAndNumberOfKursanmeldungen402 =
        kurseAndLehrkraefteAndNumberOfSchuelerForSemester102.get(0);
    assertEquals(0, kursAndLehrkraefteAndNumberOfKursanmeldungen402.numberOfKursanmeldungen());
    assertEquals(1, kursAndLehrkraefteAndNumberOfKursanmeldungen402.lehrkraefte().size());
    assertEquals(
        507, kursAndLehrkraefteAndNumberOfKursanmeldungen402.lehrkraefte().get(0).getPersonId());
    KursAndLehrkraefteAndNumberOfKursanmeldungen kursAndLehrkraefteAndNumberOfKursanmeldungen403 =
        kurseAndLehrkraefteAndNumberOfSchuelerForSemester102.get(1);
    assertEquals(2, kursAndLehrkraefteAndNumberOfKursanmeldungen403.numberOfKursanmeldungen());
    assertEquals(2, kursAndLehrkraefteAndNumberOfKursanmeldungen403.lehrkraefte().size());
    assertEquals(
        508, kursAndLehrkraefteAndNumberOfKursanmeldungen403.lehrkraefte().get(0).getPersonId());
    assertEquals(
        509, kursAndLehrkraefteAndNumberOfKursanmeldungen403.lehrkraefte().get(1).getPersonId());
  }

  @Test
  void testSaveKurs() {
    Kurstyp kurstyp = kurstypRepository.findById(201).orElse(null);
    Kursort kursort = kursortRepository.findById(301).orElse(null);
    Semester semester = semesterRepository.findById(102).orElse(null);
    Kurs kurs = createKurs(kurstyp, kursort, semester);
    kurs.setZeitEnde(createTime("14:45:00"));

    SaveKursResult saveKursResult;
    entityManager.clear();
    saveKursResult = kursService.saveKurs(kurs, null, null);
    assertEquals(SaveKursResult.KEINE_LEHRKRAEFTE_ERFASST, saveKursResult);

    Mitarbeiter lehrkraft1 = mitarbeiterRepository.findById(507).orElse(null);
    assertNotNull(lehrkraft1);
    entityManager.clear();
    saveKursResult = kursService.saveKurs(kurs, lehrkraft1, null);
    assertEquals(SaveKursResult.KURS_BEREITS_ERFASST, saveKursResult);

    entityManager.clear();
    kurs.setWochentag(Wochentag.DONNERSTAG);
    saveKursResult = kursService.saveKurs(kurs, lehrkraft1, null);
    assertEquals(SaveKursResult.LEKTIONSGEBUEHREN_NICHT_ERFASST, saveKursResult);

    entityManager.clear();
    kurs.setZeitEnde(createTime("14:50:00"));
    saveKursResult = kursService.saveKurs(kurs, lehrkraft1, null);
    assertEquals(SaveKursResult.SPEICHERN_ERFOLGREICH, saveKursResult);

    entityManager.flush();
    entityManager.clear();
    kurs = kursRepository.findById(kurs.getKursId()).orElse(null);
    assertNotNull(kurs);
    List<KursLehrkraft> lehrkraefte;
    lehrkraefte = kursLehrkraftRepository.findByKursIdOrderByLehrkraefteOrder(kurs.getKursId());
    assertEquals(1, lehrkraefte.size());
    assertEquals(lehrkraft1.getPersonId(), lehrkraefte.get(0).getLehrkraft().getPersonId());

    // Update
    kurs.setWochentag(Wochentag.MITTWOCH);
    saveKursResult = kursService.saveKurs(kurs, lehrkraft1, null);
    assertEquals(SaveKursResult.KURS_BEREITS_ERFASST, saveKursResult);

    Mitarbeiter lehrkraft2;
    lehrkraft2 = mitarbeiterRepository.findById(508).orElse(null);
    assertNotNull(lehrkraft2);
    entityManager.clear();
    kurs.setWochentag(Wochentag.DONNERSTAG);
    saveKursResult = kursService.saveKurs(kurs, lehrkraft1, lehrkraft2);
    assertEquals(SaveKursResult.SPEICHERN_ERFOLGREICH, saveKursResult);

    entityManager.flush();
    entityManager.clear();
    kurs = kursRepository.findById(kurs.getKursId()).orElse(null);
    assertNotNull(kurs);
    lehrkraefte = kursLehrkraftRepository.findByKursIdOrderByLehrkraefteOrder(kurs.getKursId());
    assertEquals(2, lehrkraefte.size());
    assertEquals(lehrkraft1.getPersonId(), lehrkraefte.get(0).getLehrkraft().getPersonId());
    assertEquals(lehrkraft2.getPersonId(), lehrkraefte.get(1).getLehrkraft().getPersonId());
    entityManager.clear();

    lehrkraft2 = mitarbeiterRepository.findById(509).orElse(null);
    saveKursResult = kursService.saveKurs(kurs, lehrkraft1, lehrkraft2);
    assertEquals(SaveKursResult.SPEICHERN_ERFOLGREICH, saveKursResult);

    entityManager.flush();
    entityManager.clear();
    kurs = kursRepository.findById(kurs.getKursId()).orElse(null);
    assertNotNull(kurs);
    entityManager.clear();

    saveKursResult = kursService.saveKurs(kurs, lehrkraft1, null);
    assertEquals(SaveKursResult.SPEICHERN_ERFOLGREICH, saveKursResult);
    lehrkraefte = kursLehrkraftRepository.findByKursIdOrderByLehrkraefteOrder(kurs.getKursId());
    assertEquals(1, lehrkraefte.size());
    assertEquals(lehrkraft1.getPersonId(), lehrkraefte.get(0).getLehrkraft().getPersonId());
  }

  @Test
  void testSaveKurs_SemesterDeleted() {
    Kurstyp kurstyp = kurstypRepository.findById(201).orElse(null);
    Kursort kursort = kursortRepository.findById(301).orElse(null);
    Semester semester = createSemester();
    Kurs kurs = createKurs(kurstyp, kursort, semester);
    kurs.setWochentag(Wochentag.DONNERSTAG);

    semesterRepository.save(semester);
    semester = semesterRepository.findById(semester.getSemesterId()).orElse(null);
    assertNotNull(semester);
    entityManager.flush();
    entityManager.clear();

    semesterRepository.delete(semester);
    entityManager.flush();
    entityManager.clear();

    SaveKursResult saveKursResult;
    Mitarbeiter lehrkraft1 = mitarbeiterRepository.findById(507).orElse(null);
    assertNotNull(lehrkraft1);
    entityManager.clear();
    saveKursResult = kursService.saveKurs(kurs, lehrkraft1, null);
    assertEquals(SaveKursResult.SEMESTER_DURCH_ANDEREN_BENUTZER_GELOESCHT, saveKursResult);
  }

  @Test
  void testSaveKurs_KurstypDeleted() {
    Kurstyp kurstyp = new Kurstyp("Kurstyp to be deleted", false);
    Kursort kursort = kursortRepository.findById(301).orElse(null);
    Semester semester = semesterRepository.findById(102).orElse(null);
    Kurs kurs = createKurs(kurstyp, kursort, semester);
    kurs.setWochentag(Wochentag.DONNERSTAG);

    kurstypRepository.save(kurstyp);
    kurstyp = kurstypRepository.findById(kurstyp.getKurstypId()).orElse(null);
    assertNotNull(kurstyp);
    entityManager.flush();
    entityManager.clear();

    kurstypRepository.delete(kurstyp);
    entityManager.flush();
    entityManager.clear();

    SaveKursResult saveKursResult;
    Mitarbeiter lehrkraft1 = mitarbeiterRepository.findById(507).orElse(null);
    assertNotNull(lehrkraft1);
    entityManager.clear();
    saveKursResult = kursService.saveKurs(kurs, lehrkraft1, null);
    assertEquals(SaveKursResult.KURSTYP_DURCH_ANDEREN_BENUTZER_GELOESCHT, saveKursResult);
  }

  @Test
  void testSaveKurs_KursortDeleted() {
    Kurstyp kurstyp = kurstypRepository.findById(201).orElse(null);
    Kursort kursort = new Kursort("Kursort to be deleted", false);
    Semester semester = semesterRepository.findById(102).orElse(null);
    Kurs kurs = createKurs(kurstyp, kursort, semester);
    kurs.setWochentag(Wochentag.DONNERSTAG);

    kursortRepository.save(kursort);
    kursort = kursortRepository.findById(kursort.getKursortId()).orElse(null);
    assertNotNull(kursort);
    entityManager.flush();
    entityManager.clear();

    kursortRepository.delete(kursort);
    entityManager.flush();
    entityManager.clear();

    SaveKursResult saveKursResult;
    Mitarbeiter lehrkraft1 = mitarbeiterRepository.findById(507).orElse(null);
    assertNotNull(lehrkraft1);
    entityManager.clear();
    saveKursResult = kursService.saveKurs(kurs, lehrkraft1, null);
    assertEquals(SaveKursResult.KURSORT_DURCH_ANDEREN_BENUTZER_GELOESCHT, saveKursResult);
  }

  @Test
  void testSaveKurs_Lehrkraft1Deleted() {
    Kurstyp kurstyp = kurstypRepository.findById(201).orElse(null);
    Kursort kursort = kursortRepository.findById(301).orElse(null);
    Semester semester = semesterRepository.findById(102).orElse(null);
    Kurs kurs = createKurs(kurstyp, kursort, semester);
    kurs.setWochentag(Wochentag.DONNERSTAG);

    Mitarbeiter lehrkraft1 = createMitarbeiter();
    mitarbeiterRepository.save(lehrkraft1);
    lehrkraft1 = mitarbeiterRepository.findById(lehrkraft1.getPersonId()).orElse(null);
    assertNotNull(lehrkraft1);
    entityManager.flush();
    entityManager.clear();

    mitarbeiterRepository.delete(lehrkraft1);
    entityManager.flush();
    entityManager.clear();

    SaveKursResult saveKursResult;
    entityManager.clear();
    saveKursResult = kursService.saveKurs(kurs, lehrkraft1, null);
    assertEquals(SaveKursResult.LEHRKRAFT_DURCH_ANDEREN_BENUTZER_GELOESCHT, saveKursResult);
  }

  @Test
  void testSaveKurs_Lehrkraft2Deleted() {
    Kurstyp kurstyp = kurstypRepository.findById(201).orElse(null);
    Kursort kursort = kursortRepository.findById(301).orElse(null);
    Semester semester = semesterRepository.findById(102).orElse(null);
    Kurs kurs = createKurs(kurstyp, kursort, semester);
    kurs.setWochentag(Wochentag.DONNERSTAG);

    Mitarbeiter lehrkraft2 = createMitarbeiter();
    mitarbeiterRepository.save(lehrkraft2);
    lehrkraft2 = mitarbeiterRepository.findById(lehrkraft2.getPersonId()).orElse(null);
    assertNotNull(lehrkraft2);
    entityManager.flush();
    entityManager.clear();

    mitarbeiterRepository.delete(lehrkraft2);
    entityManager.flush();
    entityManager.clear();

    SaveKursResult saveKursResult;
    Mitarbeiter lehrkraft1 = mitarbeiterRepository.findById(507).orElse(null);
    assertNotNull(lehrkraft1);
    entityManager.clear();
    saveKursResult = kursService.saveKurs(kurs, lehrkraft1, lehrkraft2);
    assertEquals(SaveKursResult.LEHRKRAFT_DURCH_ANDEREN_BENUTZER_GELOESCHT, saveKursResult);
  }

  @Test
  void testDeleteKurs() {
    Kurs kurs = kursRepository.findById(403).orElseThrow();
    assertTrue(kursanmeldungRepository.countByKursId(kurs.getKursId()) > 0);
    List<KursLehrkraft> lehrkraefteLinksBefore =
        kursLehrkraftRepository.findByKursIdOrderByLehrkraefteOrder(kurs.getKursId());
    assertFalse(lehrkraefteLinksBefore.isEmpty());

    kursService.deleteKurs(kurs.getKursId());

    entityManager.flush();
    entityManager.clear();
    Optional<Kurs> kursOptional = kursRepository.findById(kurs.getKursId());
    assertFalse(kursOptional.isPresent());
    assertFalse(kursanmeldungRepository.countByKursId(kurs.getKursId()) > 0);
    List<KursLehrkraft> lehrkraefteLinksAfter =
        kursLehrkraftRepository.findByKursIdOrderByLehrkraefteOrder(kurs.getKursId());
    assertTrue(lehrkraefteLinksAfter.isEmpty());
  }

  @Test
  void testImportKurseFromPreviousSemester() {

    ImportKurseResult importKurseResult;
    Semester erstesSemesterNoPreviousSemesterTarget =
        new Semester(
            "2024/2025",
            Semesterbezeichnung.ERSTES_SEMESTER,
            createCalendar("2024-08-19"),
            createCalendar("2025-02-01"),
            createCalendar("2024-10-07"),
            createCalendar("2024-10-19"),
            createCalendar("2024-12-23"),
            createCalendar("2025-01-04"));
    // Kein vorheriges Semester: Verarbeitung wird abgebrochen
    importKurseResult =
        kursService.importKurseFromPreviousSemester(erstesSemesterNoPreviousSemesterTarget);
    assertEquals(
        ImportKurseResult.IMPORT_ABGEBROCHEN_KEIN_VORHERGEHENDES_SEMESTER, importKurseResult);

    List<KursIdAndLehrkraft> kursIdAndLehrkraefteBefore =
        kursLehrkraftRepository
            .findKursIdAndLehrkraefteBySemesterIdOrderByKursIdAndLehrkraefteOrder(101);
    assertEquals(1, kursIdAndLehrkraefteBefore.size());

    Semester erstesSemesterTarget =
        new Semester(
            "2026/2027",
            Semesterbezeichnung.ERSTES_SEMESTER,
            createCalendar("2026-08-17"),
            createCalendar("2027-02-13"),
            createCalendar("2026-10-05"),
            createCalendar("2026-10-17"),
            createCalendar("2026-12-21"),
            createCalendar("2027-01-02"));
    semesterRepository.save(erstesSemesterTarget);

    importKurseResult = kursService.importKurseFromPreviousSemester(erstesSemesterTarget);

    assertEquals(ImportKurseResult.IMPORT_ERFOLGREICH, importKurseResult);
    List<Kurs> importedKursList;
    importedKursList = kursRepository.findAllBySemesterId(erstesSemesterTarget.getSemesterId());
    assertEquals(1, importedKursList.size());
    List<KursIdAndLehrkraft> kursIdAndLehrkraefteAfter;
    kursIdAndLehrkraefteAfter =
        kursLehrkraftRepository
            .findKursIdAndLehrkraefteBySemesterIdOrderByKursIdAndLehrkraefteOrder(
                erstesSemesterTarget.getSemesterId());
    assertEquals(kursIdAndLehrkraefteBefore.size(), kursIdAndLehrkraefteAfter.size());
    List<IdAndCount> kursanmeldungenByKursIdTarget;
    kursanmeldungenByKursIdTarget =
        kursanmeldungRepository.countKursanmeldungenBySemesterIdGroupByKursId(
            erstesSemesterTarget.getSemesterId());
    assertTrue(kursanmeldungenByKursIdTarget.isEmpty());

    List<IdAndCount> kursanmeldungenByKursIdSource =
        kursanmeldungRepository.countKursanmeldungenBySemesterIdGroupByKursId(
            erstesSemesterTarget.getSemesterId());
    Semester zweitesSemesterTarget =
        new Semester(
            "2026/2027",
            Semesterbezeichnung.ZWEITES_SEMESTER,
            createCalendar("2027-02-15"),
            createCalendar("2027-07-17"),
            createCalendar("2027-04-26"),
            createCalendar("2027-05-08"),
            null,
            null);
    semesterRepository.save(zweitesSemesterTarget);

    importKurseResult = kursService.importKurseFromPreviousSemester(zweitesSemesterTarget);

    assertEquals(ImportKurseResult.IMPORT_ERFOLGREICH, importKurseResult);
    importedKursList = kursRepository.findAllBySemesterId(zweitesSemesterTarget.getSemesterId());
    assertEquals(1, importedKursList.size());
    kursIdAndLehrkraefteAfter =
        kursLehrkraftRepository
            .findKursIdAndLehrkraefteBySemesterIdOrderByKursIdAndLehrkraefteOrder(
                zweitesSemesterTarget.getSemesterId());
    assertEquals(kursIdAndLehrkraefteBefore.size(), kursIdAndLehrkraefteAfter.size());
    kursanmeldungenByKursIdTarget =
        kursanmeldungRepository.countKursanmeldungenBySemesterIdGroupByKursId(
            zweitesSemesterTarget.getSemesterId());
    assertEquals(kursanmeldungenByKursIdSource.size(), kursanmeldungenByKursIdTarget.size());
  }

  @Test
  void testImportKurseFromPreviousSemester_ZielKurseVorhanden() {
    Semester sourceSemester = semesterRepository.findById(103).orElse(null);
    assertNotNull(sourceSemester);
    Semester targetSemester = semesterRepository.findById(104).orElse(null);
    assertNotNull(targetSemester);
    int numberOfSourceKurseBefore =
        kursRepository.countBySemesterId(sourceSemester.getSemesterId());
    int numberOfTargetKurseBefore =
        kursRepository.countBySemesterId(targetSemester.getSemesterId());

    kursService.importKurseFromPreviousSemester(targetSemester);

    int numberOfSourceKurseAfter = kursRepository.countBySemesterId(sourceSemester.getSemesterId());
    assertEquals(numberOfSourceKurseBefore, numberOfSourceKurseAfter);
    int numberOfTargetKurseAfter = kursRepository.countBySemesterId(targetSemester.getSemesterId());
    assertEquals(numberOfTargetKurseBefore + 1, numberOfTargetKurseAfter);
    List<IdAndCount> kursanmeldungenByKursIdTarget;
    kursanmeldungenByKursIdTarget =
        kursanmeldungRepository.countKursanmeldungenBySemesterIdGroupByKursId(
            targetSemester.getSemesterId());
    assertEquals(1, kursanmeldungenByKursIdTarget.size());
    assertEquals(2, kursanmeldungenByKursIdTarget.get(0).count());
  }

  @Test
  void testExport_CSV() throws IOException {
    Path file = tempDir.resolve("output.txt");
    Semester semester = semesterRepository.findById(102).orElse(null);
    assertNotNull(semester);
    List<KursAndLehrkraefteAndNumberOfKursanmeldungen> kursListe =
        kursService.findAllKurseAndLehrkraefteAndNumberOfKursanmeldungenForSemester(
            semester.getSemesterId());

    File outputFile = file.toFile();
    Iterator<KursAndLehrkraefteAndNumberOfKursanmeldungen> iterator = kursListe.iterator();
    assertThrows(
        IllegalStateException.class,
        () ->
            kursService.exportList(
                Listentyp.MITARBEITER_ADRESSLISTE_MIT_GEBURTSDATUM,
                "Test Export Kursliste (CSV)",
                outputFile,
                iterator,
                semester));

    kursService.exportList(
        Listentyp.KURSLISTE_CSV, "Test Export Kursliste (CSV)", outputFile, iterator, semester);

    String fileContent = Files.readString(file, StandardCharsets.ISO_8859_1);
    assertFalse(fileContent.isEmpty());
    assertTrue(fileContent.startsWith("Kurstyp;Alter;Stufe;Tag;Von;Bis;Ort;Leitung;Bemerkungen"));
    // Strichpunkte in Kommas umwandeln
    assertTrue(fileContent.contains("Bemerkung mit,Strichpunkt"));
  }

  @Test
  void testExport_Word() throws IOException {
    Path file = tempDir.resolve("output.txt");
    Semester semester = semesterRepository.findById(102).orElse(null);
    assertNotNull(semester);
    List<KursAndLehrkraefteAndNumberOfKursanmeldungen> kursListe =
        kursService.findAllKurseAndLehrkraefteAndNumberOfKursanmeldungenForSemester(
            semester.getSemesterId());
    kursService.exportList(
        Listentyp.KURSLISTE_WORD,
        "Test Export Kursliste (Word)",
        file.toFile(),
        kursListe.iterator(),
        semester);
    String fileContent = Files.readString(file, StandardCharsets.ISO_8859_1);
    assertFalse(fileContent.isEmpty());
  }

  private static Kurs createKurs(Kurstyp kurstyp, Kursort kursort, Semester semester) {
    Kurs kurs = new Kurs();
    kurs.setStufe("Stufe");
    kurs.setAltersbereich("Altersbereich");
    kurs.setWochentag(Wochentag.MITTWOCH);
    kurs.setZeitBeginn(createTime("14:00:00"));
    kurs.setZeitEnde(createTime("14:50:00"));
    kurs.setBemerkungen("Bemerkungen");
    kurs.setKurstyp(kurstyp);
    kurs.setKursort(kursort);
    kurs.setSemester(semester);
    return kurs;
  }

  private static Semester createSemester() {
    Semester semester;
    semester = new Semester();
    semester.setSchuljahr("2025/2026");
    semester.setSemesterbezeichnung(Semesterbezeichnung.ERSTES_SEMESTER);
    semester.setSemesterbeginn(createCalendar("2025-08-18"));
    semester.setSemesterende(createCalendar("2026-02-07"));
    semester.setFerienbeginn1(createCalendar("2025-10-06"));
    semester.setFerienende1(createCalendar("2025-10-18"));
    semester.setFerienbeginn2(createCalendar("2025-12-22"));
    semester.setFerienende2(createCalendar("2026-01-03"));
    return semester;
  }

  private static Mitarbeiter createMitarbeiter() {
    Mitarbeiter mitarbeiter;
    mitarbeiter = new Mitarbeiter();
    mitarbeiter.setAnrede(Anrede.KEINE);
    mitarbeiter.setVorname("Markus");
    mitarbeiter.setNachname("Meier");
    mitarbeiter.setSelektiert(false);
    mitarbeiter.setAktiv(true);
    mitarbeiter.setLehrkraft(true);
    return mitarbeiter;
  }

  private static Calendar createCalendar(String dateAsString) {
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    Date date;
    try {
      date = df.parse(dateAsString);
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    return calendar;
  }

  private static Time createTime(String timeAsString) {
    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
    Date date;
    try {
      date = sdf.parse(timeAsString);
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
    return new Time(date.getTime());
  }
}
