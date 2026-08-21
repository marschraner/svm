package ch.metzenthin.svm.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import ch.metzenthin.svm.common.datatypes.Anrede;
import ch.metzenthin.svm.common.datatypes.Geschlecht;
import ch.metzenthin.svm.common.datatypes.Rechnungstyp;
import ch.metzenthin.svm.common.datatypes.Semesterbezeichnung;
import ch.metzenthin.svm.common.datatypes.Wochentag;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Anmeldung;
import ch.metzenthin.svm.persistence.entities.Kurs;
import ch.metzenthin.svm.persistence.entities.Kursanmeldung;
import ch.metzenthin.svm.persistence.entities.Kursort;
import ch.metzenthin.svm.persistence.entities.Kurstyp;
import ch.metzenthin.svm.persistence.entities.Schueler;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.persistence.entities.Semesterrechnung;
import ch.metzenthin.svm.persistence.repository.AnmeldungRepository;
import ch.metzenthin.svm.persistence.repository.SchuelerRepository;
import ch.metzenthin.svm.service.KursanmeldungService;
import ch.metzenthin.svm.service.result.CalculateMaxAnzahlWochenKursanmeldungenResult;
import ch.metzenthin.svm.service.result.CalculateWochenbetragKurseResult;
import java.math.BigDecimal;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * @author Hans Stamm
 */
@ExtendWith(MockitoExtension.class)
class RechnungsempfaengerServiceImplMockTest {

  @InjectMocks private RechnungsempfaengerServiceImpl rechnungsempfaengerService;
  @Mock private KursanmeldungService kursanmeldungService;
  @Mock private AnmeldungRepository anmeldungRepository;
  @Mock private SchuelerRepository schuelerRepository;

  @Test
  void calculateMaxAnzahlWochen_keineKursanmeldungen() {
    Semester semester = createSemester_20252026_ErstesSemester();
    Angehoeriger rechnungsempfaenger = createRechnungsempfaenger();
    Schueler schueler1 = createSchueler(501, "Peter1", "Test1", rechnungsempfaenger);
    Schueler schueler2 = createSchueler(502, "Peter2", "Test2", rechnungsempfaenger);
    Schueler schueler3 = createSchueler(503, "Peter3", "Test3", rechnungsempfaenger);

    when(schuelerRepository.findSchuelerByRechnungsempfaengerId(rechnungsempfaenger.getPersonId()))
        .thenReturn(List.of(schueler2, schueler3, schueler1));
    when(kursanmeldungService.calculateMaxAnzahlWochen(any(), any()))
        .thenReturn(new CalculateMaxAnzahlWochenKursanmeldungenResult(0, false));
    when(anmeldungRepository.findBySchuelerIdOrderByAnmeldedatumDesc(501))
        .thenReturn(List.of(createAnmeldung(601, "2025-01-01", null, schueler1)));
    when(anmeldungRepository.findBySchuelerIdOrderByAnmeldedatumDesc(502))
        .thenReturn(List.of(createAnmeldung(602, "2025-01-01", null, schueler2)));
    when(anmeldungRepository.findBySchuelerIdOrderByAnmeldedatumDesc(503))
        .thenReturn(List.of(createAnmeldung(603, "2025-01-01", null, schueler3)));
    CalculateMaxAnzahlWochenKursanmeldungenResult calculateMaxAnzahlWochenKursanmeldungenResult =
        rechnungsempfaengerService.calculateMaxAnzahlWochen(rechnungsempfaenger, semester);
    assertEquals(21, calculateMaxAnzahlWochenKursanmeldungenResult.maxAnzahlWochen());
    assertFalse(
        calculateMaxAnzahlWochenKursanmeldungenResult.kursanmeldungenWithDifferentAnzahlWochen());
  }

  @Test
  void calculateMaxAnzahlWochen_Abgemeldet() {
    Semester semester = createSemester_20252026_ErstesSemester();
    Angehoeriger rechnungsempfaenger = createRechnungsempfaenger();
    Schueler schueler1 = createSchueler(501, "Peter1", "Test1", rechnungsempfaenger);

    when(schuelerRepository.findSchuelerByRechnungsempfaengerId(rechnungsempfaenger.getPersonId()))
        .thenReturn(List.of(schueler1));
    // Anmeldung bis 2025-08-18 am Semesterbeginn 2025-08-18 und
    // Anmeldung am 2026-03-01 nach Semesterende 2026-02-07
    when(anmeldungRepository.findBySchuelerIdOrderByAnmeldedatumDesc(501))
        .thenReturn(
            List.of(
                createAnmeldung(601, "2025-01-01", "2025-08-18", schueler1),
                createAnmeldung(602, "2026-03-01", null, schueler1)));
    CalculateMaxAnzahlWochenKursanmeldungenResult calculateMaxAnzahlWochenKursanmeldungenResult =
        rechnungsempfaengerService.calculateMaxAnzahlWochen(rechnungsempfaenger, semester);
    assertEquals(21, calculateMaxAnzahlWochenKursanmeldungenResult.maxAnzahlWochen());
    assertFalse(
        calculateMaxAnzahlWochenKursanmeldungenResult.kursanmeldungenWithDifferentAnzahlWochen());
  }

  @Test
  void calculateMaxAnzahlWochen_gleicheAnzahlWochen() {
    Semester semester = createSemester_20252026_ErstesSemester();
    Angehoeriger rechnungsempfaenger = createRechnungsempfaenger();
    Schueler schueler1 = createSchueler(501, "Peter", "Test1", rechnungsempfaenger);
    Schueler schueler2 = createSchueler(502, "Peter", "Test2", rechnungsempfaenger);
    Schueler schueler3 = createSchueler(503, "Peter", "Test3", rechnungsempfaenger);

    when(schuelerRepository.findSchuelerByRechnungsempfaengerId(rechnungsempfaenger.getPersonId()))
        .thenReturn(List.of(schueler2, schueler3, schueler1));
    when(kursanmeldungService.calculateMaxAnzahlWochen(schueler1, semester))
        .thenReturn(new CalculateMaxAnzahlWochenKursanmeldungenResult(20, false));
    when(kursanmeldungService.calculateMaxAnzahlWochen(schueler2, semester))
        .thenReturn(new CalculateMaxAnzahlWochenKursanmeldungenResult(20, false));
    when(kursanmeldungService.calculateMaxAnzahlWochen(schueler3, semester))
        .thenReturn(new CalculateMaxAnzahlWochenKursanmeldungenResult(20, false));
    when(anmeldungRepository.findBySchuelerIdOrderByAnmeldedatumDesc(501))
        .thenReturn(List.of(createAnmeldung(601, "2025-01-01", null, schueler1)));
    when(anmeldungRepository.findBySchuelerIdOrderByAnmeldedatumDesc(502))
        .thenReturn(List.of(createAnmeldung(602, "2025-01-01", null, schueler2)));
    when(anmeldungRepository.findBySchuelerIdOrderByAnmeldedatumDesc(503))
        .thenReturn(List.of(createAnmeldung(603, "2025-01-01", null, schueler3)));
    CalculateMaxAnzahlWochenKursanmeldungenResult calculateMaxAnzahlWochenKursanmeldungenResult =
        rechnungsempfaengerService.calculateMaxAnzahlWochen(rechnungsempfaenger, semester);
    assertEquals(20, calculateMaxAnzahlWochenKursanmeldungenResult.maxAnzahlWochen());
    assertFalse(
        calculateMaxAnzahlWochenKursanmeldungenResult.kursanmeldungenWithDifferentAnzahlWochen());
  }

  @Test
  void calculateMaxAnzahlWochen_unterschiedlicheAnzahlWochen() {
    Semester semester = createSemester_20252026_ErstesSemester();
    Angehoeriger rechnungsempfaenger = createRechnungsempfaenger();
    Schueler schueler1 = createSchueler(501, "Peter", "Test1", rechnungsempfaenger);
    Schueler schueler2 = createSchueler(502, "Peter", "Test2", rechnungsempfaenger);
    Schueler schueler3 = createSchueler(503, "Peter", "Test3", rechnungsempfaenger);

    when(schuelerRepository.findSchuelerByRechnungsempfaengerId(rechnungsempfaenger.getPersonId()))
        .thenReturn(List.of(schueler2, schueler3, schueler1));
    when(kursanmeldungService.calculateMaxAnzahlWochen(schueler1, semester))
        .thenReturn(new CalculateMaxAnzahlWochenKursanmeldungenResult(10, false));
    when(kursanmeldungService.calculateMaxAnzahlWochen(schueler2, semester))
        .thenReturn(new CalculateMaxAnzahlWochenKursanmeldungenResult(20, true));
    when(kursanmeldungService.calculateMaxAnzahlWochen(schueler3, semester))
        .thenReturn(new CalculateMaxAnzahlWochenKursanmeldungenResult(30, false));
    when(anmeldungRepository.findBySchuelerIdOrderByAnmeldedatumDesc(501))
        .thenReturn(List.of(createAnmeldung(601, "2025-01-01", null, schueler1)));
    when(anmeldungRepository.findBySchuelerIdOrderByAnmeldedatumDesc(502))
        .thenReturn(List.of(createAnmeldung(602, "2025-01-01", null, schueler2)));
    when(anmeldungRepository.findBySchuelerIdOrderByAnmeldedatumDesc(503))
        .thenReturn(List.of(createAnmeldung(603, "2025-01-01", null, schueler3)));
    CalculateMaxAnzahlWochenKursanmeldungenResult calculateMaxAnzahlWochenKursanmeldungenResult =
        rechnungsempfaengerService.calculateMaxAnzahlWochen(rechnungsempfaenger, semester);
    assertEquals(30, calculateMaxAnzahlWochenKursanmeldungenResult.maxAnzahlWochen());
    assertTrue(
        calculateMaxAnzahlWochenKursanmeldungenResult.kursanmeldungenWithDifferentAnzahlWochen());
  }

  @Test
  void calculateWochenbetrag() {
    Semester semester = createSemester_20252026_ErstesSemester();
    Angehoeriger rechnungsempfaenger = createRechnungsempfaenger();
    Semesterrechnung semesterrechnung = createSemesterrechnung(semester, rechnungsempfaenger);
    Map<Integer, BigDecimal[]> lektionsgebuehrenMap = getLektionsgebuehreMap();

    when(kursanmeldungService.findKursanmeldungenForSemesterAndRechnungsempfaengerBySchueler(
            semester, rechnungsempfaenger))
        .thenReturn(Map.of());
    CalculateWochenbetragKurseResult calculateWochenbetragKurseResult =
        rechnungsempfaengerService.calculateWochenbetrag(
            semesterrechnung, semester, Rechnungstyp.VORRECHNUNG, lektionsgebuehrenMap);

    assertEquals(
        0, new BigDecimal(0).compareTo(calculateWochenbetragKurseResult.wochenbetragKurse()));
    assertTrue(calculateWochenbetragKurseResult.allLektionsgebuehrenForKurslaengenFound());
  }

  @Test
  void calculateWochenbetrag_NotAllLektionsgebuehrenForKurslaengenFound() {
    Semester semester = createSemester_20252026_ErstesSemester();
    Angehoeriger rechnungsempfaenger = createRechnungsempfaenger();
    Semesterrechnung semesterrechnung = createSemesterrechnung(semester, rechnungsempfaenger);
    Map<Integer, BigDecimal[]> lektionsgebuehrenMap = getLektionsgebuehreMap();
    Schueler schueler = createSchueler(501, "Peter", "Test1", rechnungsempfaenger);
    List<Anmeldung> anmeldungenAngemeldet = createAnmeldungenAngemeldet(schueler);
    Kurstyp kurstyp = createKurstyp("Tanzen Test", false);
    Kursort kursort = createKursort("Saal Test", false);
    Kurs kurs =
        createKurs(
            semester,
            kurstyp,
            kursort,
            603,
            "7 - 8 J",
            "Kindergarten",
            Wochentag.MITTWOCH,
            "08:15:00",
            "09:00:00");
    Kursanmeldung kursanmeldung = createKursanmeldung(schueler, kurs, "2025-01-01", null);

    when(kursanmeldungService.findKursanmeldungenForSemesterAndRechnungsempfaengerBySchueler(
            semester, rechnungsempfaenger))
        .thenReturn(Map.of(schueler, List.of(kursanmeldung)));
    when(anmeldungRepository.findBySchuelerIdOrderByAnmeldedatumDesc(schueler.getPersonId()))
        .thenReturn(anmeldungenAngemeldet);
    CalculateWochenbetragKurseResult calculateWochenbetragKurseResult =
        rechnungsempfaengerService.calculateWochenbetrag(
            semesterrechnung, semester, Rechnungstyp.VORRECHNUNG, lektionsgebuehrenMap);

    assertEquals(
        0, BigDecimal.ZERO.compareTo(calculateWochenbetragKurseResult.wochenbetragKurse()));
    assertFalse(calculateWochenbetragKurseResult.allLektionsgebuehrenForKurslaengenFound());
  }

  @Test
  void calculateWochenbetrag_OhneSechsJahresrabatt() {
    Semester semester = createSemester_20252026_ErstesSemester();
    Angehoeriger rechnungsempfaenger = createRechnungsempfaenger();
    Semesterrechnung semesterrechnung = createSemesterrechnung(semester, rechnungsempfaenger);
    Map<Integer, BigDecimal[]> lektionsgebuehrenMap = getLektionsgebuehreMap();
    Schueler schueler = createSchueler(501, "Peter", "Test1", rechnungsempfaenger);
    List<Anmeldung> anmeldungen =
        List.of(
            createAnmeldung(601, "2024-01-01", "2026-02-28", schueler),
            createAnmeldung(602, "2026-04-01", null, schueler));
    Kurstyp kurstyp = createKurstyp("Tanzen Test", false);
    Kursort kursort = createKursort("Saal Test", false);
    Kurs kurs1 =
        createKurs(
            semester,
            kurstyp,
            kursort,
            603,
            "7 - 8 J",
            "Kindergarten",
            Wochentag.MITTWOCH,
            "08:00:00",
            "09:00:00");
    Kurs kurs2 =
        createKurs(
            semester,
            kurstyp,
            kursort,
            603,
            "7 - 8 J",
            "Kindergarten",
            Wochentag.DONNERSTAG,
            "08:10:00",
            "09:00:00");
    Kursanmeldung kursanmeldung1 = createKursanmeldung(schueler, kurs1, "2025-01-01", null);
    Kursanmeldung kursanmeldung2 = createKursanmeldung(schueler, kurs2, "2025-01-01", null);

    when(kursanmeldungService.findKursanmeldungenForSemesterAndRechnungsempfaengerBySchueler(
            semester, rechnungsempfaenger))
        .thenReturn(Map.of(schueler, List.of(kursanmeldung1, kursanmeldung2)));
    when(anmeldungRepository.findBySchuelerIdOrderByAnmeldedatumDesc(schueler.getPersonId()))
        .thenReturn(anmeldungen);
    CalculateWochenbetragKurseResult calculateWochenbetragKurseResult =
        rechnungsempfaengerService.calculateWochenbetrag(
            semesterrechnung, semester, Rechnungstyp.VORRECHNUNG, lektionsgebuehrenMap);

    assertEquals(
        0, new BigDecimal(90).compareTo(calculateWochenbetragKurseResult.wochenbetragKurse()));
    assertTrue(calculateWochenbetragKurseResult.allLektionsgebuehrenForKurslaengenFound());
  }

  @Test
  void calculateWochenbetrag_MitSechsJahresrabatt() {
    Semester semester = createSemester_20252026_ErstesSemester();
    Angehoeriger rechnungsempfaenger = createRechnungsempfaenger();
    Semesterrechnung semesterrechnung = createSemesterrechnung(semester, rechnungsempfaenger);
    Map<Integer, BigDecimal[]> lektionsgebuehrenMap = getLektionsgebuehreMap();
    Schueler schueler = createSchueler(501, "Peter", "Test1", rechnungsempfaenger);
    List<Anmeldung> anmeldungen = List.of(createAnmeldung(601, "2018-01-01", null, schueler));
    Kurstyp kurstyp = createKurstyp("Tanzen Test", false);
    Kursort kursort = createKursort("Saal Test", false);
    Kurs kurs1 =
        createKurs(
            semester,
            kurstyp,
            kursort,
            603,
            "7 - 8 J",
            "Kindergarten",
            Wochentag.MITTWOCH,
            "08:00:00",
            "09:00:00");
    Kurs kurs2 =
        createKurs(
            semester,
            kurstyp,
            kursort,
            603,
            "7 - 8 J",
            "Kindergarten",
            Wochentag.DONNERSTAG,
            "08:10:00",
            "09:00:00");
    Kursanmeldung kursanmeldung1 = createKursanmeldung(schueler, kurs1, "2025-01-01", null);
    Kursanmeldung kursanmeldung2 = createKursanmeldung(schueler, kurs2, "2025-01-01", null);

    when(kursanmeldungService.findKursanmeldungenForSemesterAndRechnungsempfaengerBySchueler(
            semester, rechnungsempfaenger))
        .thenReturn(Map.of(schueler, List.of(kursanmeldung1, kursanmeldung2)));
    when(anmeldungRepository.findBySchuelerIdOrderByAnmeldedatumDesc(schueler.getPersonId()))
        .thenReturn(anmeldungen);
    CalculateWochenbetragKurseResult calculateWochenbetragKurseResult =
        rechnungsempfaengerService.calculateWochenbetrag(
            semesterrechnung, semester, Rechnungstyp.VORRECHNUNG, lektionsgebuehrenMap);

    // 50 (Kurs1 (Kurslänge 60, 2 Anmeldungen)) 40 (Kurs2 (Kurslänge 50, 2 Anmeldungen)) = 90 - 40
    // (Betrag min. Kurslänge (50), 2 Anmeldungen) + (50 (höchster Betrag der min. Kurslänge) * 0.5
    // = 25) = 75.
    assertEquals(
        0, new BigDecimal(75).compareTo(calculateWochenbetragKurseResult.wochenbetragKurse()));
    assertTrue(calculateWochenbetragKurseResult.allLektionsgebuehrenForKurslaengenFound());
  }

  @Test
  void isAngemeldetOnSemesterbeginn() {
    Semester semester = createSemester_20252026_ErstesSemester();
    Angehoeriger rechnungsempfaenger = createRechnungsempfaenger();
    Schueler schueler = createSchueler(501, "Peter", "Test1", rechnungsempfaenger);

    when(anmeldungRepository.findBySchuelerIdOrderByAnmeldedatumDesc(schueler.getPersonId()))
        .thenReturn(List.of());
    boolean angemeldetOnSemesterbeginn =
        rechnungsempfaengerService.isAngemeldetOnSemesterbeginn(schueler, semester);
    assertFalse(angemeldetOnSemesterbeginn);

    when(anmeldungRepository.findBySchuelerIdOrderByAnmeldedatumDesc(schueler.getPersonId()))
        .thenReturn(createAnmeldungenAngemeldet(schueler));
    angemeldetOnSemesterbeginn =
        rechnungsempfaengerService.isAngemeldetOnSemesterbeginn(schueler, semester);
    assertTrue(angemeldetOnSemesterbeginn);

    when(anmeldungRepository.findBySchuelerIdOrderByAnmeldedatumDesc(schueler.getPersonId()))
        .thenReturn(createAnmeldungenAbgemeldetInSemester(schueler));
    angemeldetOnSemesterbeginn =
        rechnungsempfaengerService.isAngemeldetOnSemesterbeginn(schueler, semester);
    assertTrue(angemeldetOnSemesterbeginn);

    when(anmeldungRepository.findBySchuelerIdOrderByAnmeldedatumDesc(schueler.getPersonId()))
        .thenReturn(createAnmeldungenAbgemeldet(schueler));
    angemeldetOnSemesterbeginn =
        rechnungsempfaengerService.isAngemeldetOnSemesterbeginn(schueler, semester);
    assertFalse(angemeldetOnSemesterbeginn);
  }

  @Test
  void filterAbgemeldeteSchueler() {
    Semester semester = createSemester_20252026_ErstesSemester();
    Angehoeriger rechnungsempfaenger = createRechnungsempfaenger();
    Schueler schueler = createSchueler(501, "Peter", "Test1", rechnungsempfaenger);
    List<Anmeldung> anmeldungenAngemeldet = createAnmeldungenAngemeldet(schueler);
    Kurstyp kurstyp = createKurstyp("Tanzen Test", false);
    Kursort kursort = createKursort("Saal Test", false);
    Kurs kurs =
        createKurs(
            semester,
            kurstyp,
            kursort,
            202,
            "5 - 6 J",
            "Kindergarten",
            Wochentag.DIENSTAG,
            "10:00:00",
            "11:00:00");
    List<Kursanmeldung> kursanmeldungen = createKursanmeldungen(schueler, kurs);
    Map<Schueler, List<Kursanmeldung>> kursanmeldungenBySchuelerMap =
        Map.of(schueler, kursanmeldungen);

    when(anmeldungRepository.findBySchuelerIdOrderByAnmeldedatumDesc(schueler.getPersonId()))
        .thenReturn(anmeldungenAngemeldet);
    Map<Schueler, List<Kursanmeldung>> kursanmeldungenBySchuelerMapResult =
        rechnungsempfaengerService.filterAbgemeldeteSchueler(
            semester, kursanmeldungenBySchuelerMap);
    assertEquals(kursanmeldungenBySchuelerMap.size(), kursanmeldungenBySchuelerMapResult.size());

    List<Anmeldung> abmeldungenAngemeldet = createAnmeldungenAbgemeldet(schueler);
    when(anmeldungRepository.findBySchuelerIdOrderByAnmeldedatumDesc(schueler.getPersonId()))
        .thenReturn(abmeldungenAngemeldet);
    kursanmeldungenBySchuelerMapResult =
        rechnungsempfaengerService.filterAbgemeldeteSchueler(
            semester, kursanmeldungenBySchuelerMap);
    assertTrue(kursanmeldungenBySchuelerMapResult.isEmpty());
  }

  @Test
  void getAnzahlKurseRechnungsempfaenger() {
    Semester semester = createSemester_20252026_ErstesSemester();
    Angehoeriger rechnungsempfaenger = createRechnungsempfaenger();
    Kurstyp kurstyp = createKurstyp("Tanzen Test", true);
    Kursort kursort = createKursort("Saal Test", true);
    Kurs kurs1 =
        createKurs(
            semester,
            kurstyp,
            kursort,
            601,
            "3 - 4 J",
            "Vorkindergarten",
            Wochentag.MONTAG,
            "08:15:00",
            "09:00:00");
    Kurs kurs2 =
        createKurs(
            semester,
            kurstyp,
            kursort,
            602,
            "5 - 6 J",
            "Kindergarten",
            Wochentag.DIENSTAG,
            "08:15:00",
            "09:00:00");
    Kurs kurs3 =
        createKurs(
            semester,
            kurstyp,
            kursort,
            603,
            "7 - 8 J",
            "Kindergarten",
            Wochentag.MITTWOCH,
            "08:15:00",
            "09:00:00");
    Schueler schueler1 = createSchueler(501, "Peter", "Test1", rechnungsempfaenger);
    Kursanmeldung kursanmeldung1Angemeldet =
        createKursanmeldung(schueler1, kurs1, "2025-01-01", null);
    Schueler schueler2 = createSchueler(502, "Peter", "Test2", rechnungsempfaenger);
    Kursanmeldung kursanmeldung2Abgemeldet =
        createKursanmeldung(schueler2, kurs1, "2025-01-01", "2025-12-31");
    Kursanmeldung kursanmeldung2Angemeldet =
        createKursanmeldung(schueler2, kurs2, "2025-01-01", null);
    Schueler schueler3 = createSchueler(503, "Peter", "Test3", rechnungsempfaenger);
    Kursanmeldung kursanmeldung3Angemeldet1 =
        createKursanmeldung(schueler3, kurs1, "2025-01-01", null);
    Kursanmeldung kursanmeldung3Angemeldet2 =
        createKursanmeldung(schueler3, kurs3, "2025-01-01", null);
    Schueler schueler4 = createSchueler(504, "Peter", "Test4", rechnungsempfaenger);
    Kursanmeldung kursanmeldung4Angemeldet1 =
        createKursanmeldung(schueler4, kurs1, "2025-01-01", null);
    Kursanmeldung kursanmeldung4Angemeldet2 =
        createKursanmeldung(schueler4, kurs2, "2025-01-01", null);

    int anzahlKurseRechnungsempfaenger;
    anzahlKurseRechnungsempfaenger =
        rechnungsempfaengerService.getAnzahlKurseRechnungsempfaenger(
            Rechnungstyp.VORRECHNUNG,
            Map.of(
                schueler3,
                List.of(kursanmeldung3Angemeldet1, kursanmeldung3Angemeldet2),
                schueler1,
                List.of(kursanmeldung1Angemeldet),
                schueler2,
                List.of(kursanmeldung2Abgemeldet, kursanmeldung2Angemeldet)));
    // 5 Anmeldungen, 1 abgemeldet
    assertEquals(4, anzahlKurseRechnungsempfaenger);

    anzahlKurseRechnungsempfaenger =
        rechnungsempfaengerService.getAnzahlKurseRechnungsempfaenger(
            Rechnungstyp.NACHRECHNUNG,
            Map.of(
                schueler3,
                List.of(kursanmeldung3Angemeldet1, kursanmeldung3Angemeldet2),
                schueler1,
                List.of(kursanmeldung1Angemeldet),
                schueler2,
                List.of(kursanmeldung2Abgemeldet, kursanmeldung2Angemeldet)));
    // 5 Anmeldungen, Abmeldungen werden auch gezählt bei Rechnungstyp.NACHRECHNUNG
    assertEquals(5, anzahlKurseRechnungsempfaenger);

    anzahlKurseRechnungsempfaenger =
        rechnungsempfaengerService.getAnzahlKurseRechnungsempfaenger(
            Rechnungstyp.NACHRECHNUNG,
            Map.of(
                schueler3,
                List.of(kursanmeldung3Angemeldet1, kursanmeldung3Angemeldet2),
                schueler1,
                List.of(kursanmeldung1Angemeldet),
                schueler2,
                List.of(kursanmeldung2Abgemeldet, kursanmeldung2Angemeldet),
                schueler4,
                List.of(kursanmeldung4Angemeldet1, kursanmeldung4Angemeldet2)));
    // 7 Anmeldungen: Resultat 6 (max.)
    assertEquals(6, anzahlKurseRechnungsempfaenger);
  }

  @Test
  void isNachrechnungOrNotAbgemeldet() {
    Semester semester = createSemester_20252026_ErstesSemester();
    Angehoeriger rechnungsempfaenger = createRechnungsempfaenger();
    Schueler schueler = createSchueler(501, "Peter", "Test1", rechnungsempfaenger);
    Kurstyp kurstyp = createKurstyp("Tanzen Test1", true);
    Kursort kursort = createKursort("Saal Test1", true);
    Kurs kurs =
        createKurs(
            semester,
            kurstyp,
            kursort,
            201,
            "3 - 4 J",
            "Vorkindergarten",
            Wochentag.MONTAG,
            "14:00:00",
            "15:00:00");

    Kursanmeldung kursanmeldungAngemeldet = createKursanmeldung(schueler, kurs, "2024-01-01", null);
    assertTrue(
        rechnungsempfaengerService.isNachrechnungOrNotAbgemeldet(
            Rechnungstyp.VORRECHNUNG, kursanmeldungAngemeldet));

    Kursanmeldung kursanmeldungAbgemeldet =
        createKursanmeldung(schueler, kurs, "2025-01-01", "2025-09-30");
    assertFalse(
        rechnungsempfaengerService.isNachrechnungOrNotAbgemeldet(
            Rechnungstyp.VORRECHNUNG, kursanmeldungAbgemeldet));

    assertTrue(
        rechnungsempfaengerService.isNachrechnungOrNotAbgemeldet(
            Rechnungstyp.NACHRECHNUNG, kursanmeldungAbgemeldet));
  }

  @Test
  void getRelevanteKurseSchueler() {
    Semester semester = createSemester_20252026_ErstesSemester();
    Angehoeriger rechnungsempfaenger = createRechnungsempfaenger();
    Kurstyp kurstyp = createKurstyp("Tanzen Test", true);
    Kursort kursort = createKursort("Saal Test", true);
    Kurs kurs1 =
        createKurs(
            semester,
            kurstyp,
            kursort,
            601,
            "3 - 4 J",
            "Vorkindergarten",
            Wochentag.MONTAG,
            "08:15:00",
            "09:00:00");
    Kurs kurs2 =
        createKurs(
            semester,
            kurstyp,
            kursort,
            602,
            "5 - 6 J",
            "Kindergarten",
            Wochentag.DIENSTAG,
            "08:15:00",
            "09:00:00");
    Kurs kurs3 =
        createKurs(
            semester,
            kurstyp,
            kursort,
            603,
            "7 - 8 J",
            "Kindergarten",
            Wochentag.MITTWOCH,
            "08:15:00",
            "09:00:00");
    Schueler schueler1 = createSchueler(501, "Peter", "Test1", rechnungsempfaenger);
    Kursanmeldung kursanmeldung1Angemeldet =
        createKursanmeldung(schueler1, kurs1, "2025-01-01", null);
    Schueler schueler2 = createSchueler(502, "Peter", "Test2", rechnungsempfaenger);
    Kursanmeldung kursanmeldung2Abgemeldet =
        createKursanmeldung(schueler2, kurs1, "2025-01-01", "2025-12-31");
    Kursanmeldung kursanmeldung2Angemeldet =
        createKursanmeldung(schueler2, kurs2, "2025-01-01", null);
    Schueler schueler3 = createSchueler(503, "Peter", "Test3", rechnungsempfaenger);
    Kursanmeldung kursanmeldung3Angemeldet1 =
        createKursanmeldung(schueler3, kurs1, "2025-01-01", null);
    Kursanmeldung kursanmeldung3Angemeldet2 =
        createKursanmeldung(schueler3, kurs3, "2025-01-01", null);

    List<Kurs> relevanteKurseSchueler;
    relevanteKurseSchueler =
        rechnungsempfaengerService.getRelevanteKurseSchueler(
            Rechnungstyp.VORRECHNUNG, Map.entry(schueler1, List.of(kursanmeldung1Angemeldet)));
    assertEquals(1, relevanteKurseSchueler.size());

    relevanteKurseSchueler =
        rechnungsempfaengerService.getRelevanteKurseSchueler(
            Rechnungstyp.VORRECHNUNG,
            Map.entry(schueler2, List.of(kursanmeldung2Angemeldet, kursanmeldung2Abgemeldet)));
    assertEquals(1, relevanteKurseSchueler.size());

    relevanteKurseSchueler =
        rechnungsempfaengerService.getRelevanteKurseSchueler(
            Rechnungstyp.NACHRECHNUNG,
            Map.entry(schueler2, List.of(kursanmeldung2Angemeldet, kursanmeldung2Abgemeldet)));
    assertEquals(2, relevanteKurseSchueler.size());

    relevanteKurseSchueler =
        rechnungsempfaengerService.getRelevanteKurseSchueler(
            Rechnungstyp.VORRECHNUNG,
            Map.entry(schueler3, List.of(kursanmeldung3Angemeldet1, kursanmeldung3Angemeldet2)));
    assertEquals(2, relevanteKurseSchueler.size());

    relevanteKurseSchueler =
        rechnungsempfaengerService.getRelevanteKurseSchueler(
            Rechnungstyp.NACHRECHNUNG,
            Map.entry(schueler3, List.of(kursanmeldung3Angemeldet1, kursanmeldung3Angemeldet2)));
    assertEquals(2, relevanteKurseSchueler.size());
  }

  @Test
  void getWochenbetragOhneSechsJahresRabatt() {
    Semester semester = createSemester_20252026_ErstesSemester();
    Kurstyp kurstyp = createKurstyp("Tanzen Test", true);
    Kursort kursort = createKursort("Saal Test", true);
    Kurs kurs1 =
        createKurs(
            semester,
            kurstyp,
            kursort,
            601,
            "3 - 4 J",
            "Vorkindergarten",
            Wochentag.MONTAG,
            "08:10:00",
            "09:00:00");
    Kurs kurs2 =
        createKurs(
            semester,
            kurstyp,
            kursort,
            602,
            "5 - 6 J",
            "Kindergarten",
            Wochentag.DIENSTAG,
            "08:00:00",
            "09:00:00");
    Kurs kurs3 =
        createKurs(
            semester,
            kurstyp,
            kursort,
            603,
            "7 - 8 J",
            "Kindergarten",
            Wochentag.MITTWOCH,
            "08:15:00",
            "09:00:00");

    BigDecimal wochenbetrag;
    wochenbetrag =
        RechnungsempfaengerServiceImpl.getWochenbetragOhneSechsJahresRabatt(
            getLektionsgebuehreMap(), List.of(kurs1), 1, BigDecimal.ZERO);
    // Kurslänge 50, Anzahl Kurse 1: 50
    assertEquals(0, new BigDecimal(50).compareTo(wochenbetrag));

    wochenbetrag =
        RechnungsempfaengerServiceImpl.getWochenbetragOhneSechsJahresRabatt(
            getLektionsgebuehreMap(), List.of(kurs1, kurs2), 1, BigDecimal.ZERO);
    // Kurslänge 50 und 60, Anzahl Kurse 1: 50 + 60 = 110
    assertEquals(0, new BigDecimal(110).compareTo(wochenbetrag));

    wochenbetrag =
        RechnungsempfaengerServiceImpl.getWochenbetragOhneSechsJahresRabatt(
            getLektionsgebuehreMap(), List.of(kurs1, kurs2), 2, BigDecimal.ZERO);
    // Kurslänge 50 und 60, Anzahl Kurse 2: 40 + 50 = 90
    assertEquals(0, new BigDecimal(90).compareTo(wochenbetrag));

    wochenbetrag =
        RechnungsempfaengerServiceImpl.getWochenbetragOhneSechsJahresRabatt(
            getLektionsgebuehreMap(), List.of(), 0, BigDecimal.ZERO);
    // Kein Kurs, Anzahl Kurse 0: 0
    assertEquals(0, BigDecimal.ZERO.compareTo(wochenbetrag));

    wochenbetrag =
        RechnungsempfaengerServiceImpl.getWochenbetragOhneSechsJahresRabatt(
            getLektionsgebuehreMap(), List.of(kurs3), 1, BigDecimal.ZERO);
    // Lektionslänge nicht gefunden: null
    assertNull(wochenbetrag);
  }

  @Test
  void hasSchuelerSechsJahresRabatt() {
    Semester semester = createSemester_20252026_ErstesSemester();
    Angehoeriger rechnungsempfaenger = createRechnungsempfaenger();
    Kurstyp kurstyp = createKurstyp("Tanzen Test", true);
    Kursort kursort = createKursort("Saal Test", true);
    Kurs kurs1 =
        createKurs(
            semester,
            kurstyp,
            kursort,
            601,
            "3 - 4 J",
            "Vorkindergarten",
            Wochentag.MONTAG,
            "08:15:00",
            "09:00:00");
    Kurs kurs2 =
        createKurs(
            semester,
            kurstyp,
            kursort,
            602,
            "5 - 6 J",
            "Kindergarten",
            Wochentag.DIENSTAG,
            "08:15:00",
            "09:00:00");
    Schueler schueler1 = createSchueler(501, "Peter", "Test1", rechnungsempfaenger);
    Kursanmeldung kursanmeldung1Angemeldet =
        createKursanmeldung(schueler1, kurs1, "2025-01-01", null);
    Schueler schueler2 = createSchueler(502, "Peter", "Test2", rechnungsempfaenger);
    Anmeldung anmeldungSchuelerLongAgo = createAnmeldung(601, "2025-01-01", null, schueler2);
    Anmeldung anmeldungNotLongAgo = createAnmeldung(602, "2018-01-01", null, schueler2);
    Kursanmeldung kursanmeldung2Abgemeldet =
        createKursanmeldung(schueler2, kurs1, "2025-01-01", "2025-12-31");
    Kursanmeldung kursanmeldung2Angemeldet =
        createKursanmeldung(schueler2, kurs2, "2025-01-01", null);

    boolean hasSchuelerSechsJahresRabatt;
    hasSchuelerSechsJahresRabatt =
        rechnungsempfaengerService.hasSchuelerSechsJahresRabatt(
            Map.entry(schueler1, List.of(kursanmeldung1Angemeldet)), List.of(kurs1), semester);
    // zu wenig Kurse (< 2)
    assertFalse(hasSchuelerSechsJahresRabatt);

    when(anmeldungRepository.findBySchuelerIdOrderByAnmeldedatumDesc(schueler2.getPersonId()))
        .thenReturn(List.of(anmeldungSchuelerLongAgo));
    hasSchuelerSechsJahresRabatt =
        rechnungsempfaengerService.hasSchuelerSechsJahresRabatt(
            Map.entry(schueler2, List.of(kursanmeldung2Angemeldet, kursanmeldung2Abgemeldet)),
            List.of(kurs1, kurs2),
            semester);
    // Anmeldung vor weniger als 6 Jahren
    assertFalse(hasSchuelerSechsJahresRabatt);

    when(anmeldungRepository.findBySchuelerIdOrderByAnmeldedatumDesc(schueler2.getPersonId()))
        .thenReturn(List.of(anmeldungNotLongAgo));
    hasSchuelerSechsJahresRabatt =
        rechnungsempfaengerService.hasSchuelerSechsJahresRabatt(
            Map.entry(schueler2, List.of(kursanmeldung2Angemeldet, kursanmeldung2Abgemeldet)),
            List.of(kurs1, kurs2),
            semester);
    // Anmeldung vor mehr als 6 Jahren
    assertTrue(hasSchuelerSechsJahresRabatt);
  }

  @Test
  void getAnmeldungsdauer() {
    Semester semester = createSemester_20252026_ErstesSemester();
    Angehoeriger rechnungsempfaenger = createRechnungsempfaenger();
    Schueler schueler = createSchueler(501, "Peter", "Test1", rechnungsempfaenger);
    List<Anmeldung> anmeldungen = createAnmeldungenAngemeldet(schueler);

    when(anmeldungRepository.findBySchuelerIdOrderByAnmeldedatumDesc(schueler.getPersonId()))
        .thenReturn(anmeldungen);
    int anmeldungsdauer = rechnungsempfaengerService.getAnmeldungsdauer(schueler, semester);
    assertEquals(410, anmeldungsdauer);

    anmeldungen = createAnmeldungenAbgemeldetInSemester(schueler);
    when(anmeldungRepository.findBySchuelerIdOrderByAnmeldedatumDesc(schueler.getPersonId()))
        .thenReturn(anmeldungen);
    anmeldungsdauer = rechnungsempfaengerService.getAnmeldungsdauer(schueler, semester);
    assertEquals(410, anmeldungsdauer);

    anmeldungen = createAnmeldungenAbgemeldet(schueler);
    when(anmeldungRepository.findBySchuelerIdOrderByAnmeldedatumDesc(schueler.getPersonId()))
        .thenReturn(anmeldungen);
    anmeldungsdauer = rechnungsempfaengerService.getAnmeldungsdauer(schueler, semester);
    assertEquals(361, anmeldungsdauer);
  }

  @Test
  void calculateReductionSechsJahresRabatt() {
    BigDecimal wochenbetragReduced;
    wochenbetragReduced =
        RechnungsempfaengerServiceImpl.calculateReductionSechsJahresRabatt(
            getLektionsgebuehreMap(), 50, 1, new BigDecimal(50));
    // 50 - 50 + (50 * 0.5) = 25
    assertEquals(0, new BigDecimal(25).compareTo(wochenbetragReduced));

    wochenbetragReduced =
        RechnungsempfaengerServiceImpl.calculateReductionSechsJahresRabatt(
            getLektionsgebuehreMap(), 50, 6, new BigDecimal(50));
    // 50 - 1 + (50 * 0.5) = 74
    assertEquals(0, new BigDecimal(74).compareTo(wochenbetragReduced));

    wochenbetragReduced =
        RechnungsempfaengerServiceImpl.calculateReductionSechsJahresRabatt(
            getLektionsgebuehreMap(), 60, 4, new BigDecimal(90));
    // 90 - 30 + (60 * 0.5) = 90
    assertEquals(0, new BigDecimal(90).compareTo(wochenbetragReduced));
  }

  @Test
  void getMinimaleKurslaenge() {
    Semester semester = createSemester_20252026_ErstesSemester();
    Kurstyp kurstyp = createKurstyp("Tanzen Test", true);
    Kursort kursort = createKursort("Saal Test", true);
    Kurs kurs1 =
        createKurs(
            semester,
            kurstyp,
            kursort,
            601,
            "3 - 4 J",
            "Vorkindergarten",
            Wochentag.MONTAG,
            "08:10:00",
            "09:00:00");
    Kurs kurs2 =
        createKurs(
            semester,
            kurstyp,
            kursort,
            602,
            "5 - 6 J",
            "Kindergarten",
            Wochentag.DIENSTAG,
            "08:15:00",
            "09:00:00");
    Kurs kurs3 =
        createKurs(
            semester,
            kurstyp,
            kursort,
            603,
            "7 - 8 J",
            "Kindergarten",
            Wochentag.MITTWOCH,
            "08:15:00",
            "09:00:00");

    int minimaleKurslaenge;
    minimaleKurslaenge = RechnungsempfaengerServiceImpl.getMinimaleKurslaenge(List.of());
    // Keine Kurse
    assertEquals(Integer.MAX_VALUE, minimaleKurslaenge);

    minimaleKurslaenge = RechnungsempfaengerServiceImpl.getMinimaleKurslaenge(List.of(kurs1));
    // 08:10 - 09:00 = 50
    assertEquals(50, minimaleKurslaenge);

    minimaleKurslaenge =
        RechnungsempfaengerServiceImpl.getMinimaleKurslaenge(List.of(kurs3, kurs2));
    // 08:15 - 09:00 = 45, 08:15 - 09:00 = 45
    assertEquals(45, minimaleKurslaenge);
  }

  // -----------------------------------------------------------------------------------------------
  // Testdata
  // -----------------------------------------------------------------------------------------------

  private Semester createSemester_20252026_ErstesSemester() {
    Semester semester = new Semester();
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

  private Angehoeriger createRechnungsempfaenger() {
    Angehoeriger rechnungsempfaenger = new Angehoeriger();
    rechnungsempfaenger.setPersonId(501);
    rechnungsempfaenger.setVersion(0);
    rechnungsempfaenger.setAnrede(Anrede.FRAU);
    rechnungsempfaenger.setVorname("Marie");
    rechnungsempfaenger.setNachname("Muster");
    rechnungsempfaenger.setWuenschtEmails(false);
    return rechnungsempfaenger;
  }

  private Semesterrechnung createSemesterrechnung(
      Semester semester, Angehoeriger rechnungsempfaenger) {
    Semesterrechnung semesterrechnung = new Semesterrechnung();
    semesterrechnung.setSemester(semester);
    semesterrechnung.setRechnungsempfaenger(rechnungsempfaenger);
    return semesterrechnung;
  }

  private Schueler createSchueler(
      int personId, String vorname, String nachname, Angehoeriger rechnungsempfaenger) {
    Schueler schueler = new Schueler();
    schueler.setPersonId(personId);
    schueler.setVersion(0);
    schueler.setAnrede(Anrede.KEINE);
    schueler.setVorname(vorname);
    schueler.setNachname(nachname);
    schueler.setGeschlecht(Geschlecht.M);
    schueler.setRechnungsempfaenger(rechnungsempfaenger);
    return schueler;
  }

  private List<Anmeldung> createAnmeldungenAngemeldet(Schueler schueler) {
    Anmeldung anmeldungAngemeldet = createAnmeldung(601, "2025-01-01", null, schueler);
    Anmeldung anmeldungAbgemeldet = createAnmeldung(602, "2024-01-01", "2024-06-30", schueler);
    return List.of(anmeldungAngemeldet, anmeldungAbgemeldet);
  }

  private List<Anmeldung> createAnmeldungenAbgemeldetInSemester(Schueler schueler) {
    Anmeldung anmeldungAngemeldet = createAnmeldung(601, "2025-01-01", "2025-09-30", schueler);
    Anmeldung anmeldungAbgemeldet = createAnmeldung(602, "2024-01-01", "2024-06-30", schueler);
    return List.of(anmeldungAngemeldet, anmeldungAbgemeldet);
  }

  private List<Anmeldung> createAnmeldungenAbgemeldet(Schueler schueler) {
    Anmeldung anmeldungAngemeldet = createAnmeldung(601, "2025-01-01", "2025-06-30", schueler);
    Anmeldung anmeldungAbgemeldet = createAnmeldung(602, "2024-01-01", "2024-06-30", schueler);
    return List.of(anmeldungAngemeldet, anmeldungAbgemeldet);
  }

  private Anmeldung createAnmeldung(
      int id, String anmeldedatum, String abmeldedatum, Schueler schueler) {
    Anmeldung anmeldung = new Anmeldung();
    anmeldung.setAnmeldungId(id);
    anmeldung.setAnmeldedatum(createCalendar(anmeldedatum));
    if (abmeldedatum != null) {
      anmeldung.setAbmeldedatum(createCalendar(abmeldedatum));
    }
    anmeldung.setSchueler(schueler);
    return anmeldung;
  }

  private Kurstyp createKurstyp(String bezeichnung, boolean selektierbar) {
    Kurstyp kurstyp = new Kurstyp();
    kurstyp.setKurstypId(201);
    kurstyp.setVersion(0);
    kurstyp.setBezeichnung(bezeichnung);
    kurstyp.setSelektierbar(selektierbar);
    return kurstyp;
  }

  private Kursort createKursort(String bezeichnung, boolean selektierbar) {
    Kursort kursort = new Kursort();
    kursort.setKursortId(301);
    kursort.setVersion(0);
    kursort.setBezeichnung(bezeichnung);
    kursort.setSelektierbar(selektierbar);
    return kursort;
  }

  private Kurs createKurs(
      Semester semester,
      Kurstyp kurstyp,
      Kursort kursort,
      int kursId,
      String altersbereich,
      String stufe,
      Wochentag wochentag,
      String zeitBeginn,
      String zeitEnde) {
    Kurs kurs = new Kurs();
    kurs.setKursId(kursId);
    kurs.setVersion(0);
    kurs.setAltersbereich(altersbereich);
    kurs.setStufe(stufe);
    kurs.setWochentag(wochentag);
    kurs.setZeitBeginn(createTime(zeitBeginn));
    kurs.setZeitEnde(createTime(zeitEnde));
    kurs.setSemester(semester);
    kurs.setKurstyp(kurstyp);
    kurs.setKursort(kursort);
    return kurs;
  }

  private List<Kursanmeldung> createKursanmeldungen(Schueler schueler, Kurs kurs) {
    Kursanmeldung kursanmeldung = createKursanmeldung(schueler, kurs, "2025-01-01", null);
    return List.of(kursanmeldung);
  }

  private Kursanmeldung createKursanmeldung(
      Schueler schueler, Kurs kurs, String anmeldedatum, String abmeldedatum) {
    Kursanmeldung kursanmeldung = new Kursanmeldung();
    kursanmeldung.setVersion(0);
    kursanmeldung.setAnmeldedatum(createCalendar(anmeldedatum));
    if (abmeldedatum != null) {
      kursanmeldung.setAbmeldedatum(createCalendar(abmeldedatum));
    }
    kursanmeldung.setSchueler(schueler);
    kursanmeldung.setKurs(kurs);
    return kursanmeldung;
  }

  private Calendar createCalendar(String dateAsString) {
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

  private Time createTime(String timeAsString) {
    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
    Date date;
    try {
      date = sdf.parse(timeAsString);
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
    return new Time(date.getTime());
  }

  private Map<Integer, BigDecimal[]> getLektionsgebuehreMap() {
    return Map.of(
        50,
        new BigDecimal[] {
          new BigDecimal(50),
          new BigDecimal(40),
          new BigDecimal(30),
          new BigDecimal(20),
          new BigDecimal(10),
          new BigDecimal(1)
        },
        60,
        new BigDecimal[] {
          new BigDecimal(60),
          new BigDecimal(50),
          new BigDecimal(40),
          new BigDecimal(30),
          new BigDecimal(20),
          new BigDecimal(10)
        });
  }
}
