package ch.metzenthin.svm.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import ch.metzenthin.svm.common.datatypes.Anrede;
import ch.metzenthin.svm.common.datatypes.Rechnungstyp;
import ch.metzenthin.svm.common.datatypes.Semesterbezeichnung;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.persistence.entities.Semesterrechnung;
import ch.metzenthin.svm.persistence.repository.SemesterrechnungRepository;
import ch.metzenthin.svm.service.RechnungsempfaengerService;
import ch.metzenthin.svm.service.result.CalculateMaxAnzahlWochenKursanmeldungenResult;
import ch.metzenthin.svm.service.result.CalculateWochenbetragKurseResult;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
class SemesterrechnungServiceImplMockTest {

  @InjectMocks private SemesterrechnungServiceImpl semesterrechnungService;
  @Mock private RechnungsempfaengerService rechnungsempfaengerService;
  @Mock private SemesterrechnungRepository semesterrechnungRepository;

  @Test
  void testSetAnzahlWochenVorrechnung() {
    Semesterrechnung semesterrechnung =
        createSemesterrechnung(
            createSemester_20252026_ErstesSemester(), createRechnungsempfaenger());
    semesterrechnung.setAnzahlWochenVorrechnung(1);
    assertTrue(SemesterrechnungServiceImpl.setAnzahlWochenVorrechnung(semesterrechnung, 2));
    assertEquals(2, semesterrechnung.getAnzahlWochenVorrechnung());

    assertFalse(SemesterrechnungServiceImpl.setAnzahlWochenVorrechnung(semesterrechnung, 2));
    assertEquals(2, semesterrechnung.getAnzahlWochenVorrechnung());

    semesterrechnung.setRechnungsdatumVorrechnung(createCalendar("2025-01-01"));
    assertFalse(SemesterrechnungServiceImpl.setAnzahlWochenVorrechnung(semesterrechnung, 1));
    assertEquals(2, semesterrechnung.getAnzahlWochenVorrechnung());
  }

  @Test
  void testSetAnzahlWochenNachrechnung() {
    Semesterrechnung semesterrechnung =
        createSemesterrechnung(
            createSemester_20252026_ErstesSemester(), createRechnungsempfaenger());
    semesterrechnung.setAnzahlWochenNachrechnung(1);
    assertTrue(SemesterrechnungServiceImpl.setAnzahlWochenNachrechnung(semesterrechnung, 2));
    assertEquals(2, semesterrechnung.getAnzahlWochenNachrechnung());

    assertFalse(SemesterrechnungServiceImpl.setAnzahlWochenNachrechnung(semesterrechnung, 2));
    assertEquals(2, semesterrechnung.getAnzahlWochenNachrechnung());

    assertFalse(SemesterrechnungServiceImpl.setAnzahlWochenNachrechnung(semesterrechnung, 2));
    assertEquals(2, semesterrechnung.getAnzahlWochenNachrechnung());

    semesterrechnung.setRechnungsdatumNachrechnung(createCalendar("2025-01-01"));
    assertFalse(SemesterrechnungServiceImpl.setAnzahlWochenNachrechnung(semesterrechnung, 1));
    assertEquals(2, semesterrechnung.getAnzahlWochenNachrechnung());
  }

  @Test
  void testCalculateAndUpdateWochenbetrag() {
    Semester semester = createSemester_20252026_ErstesSemester();
    Semesterrechnung semesterrechnung;
    semesterrechnung = createSemesterrechnung(semester, createRechnungsempfaenger());
    semesterrechnung.setWochenbetragVorrechnung(BigDecimal.ZERO);
    Map<Integer, BigDecimal[]> lektionsgebuehrenMap = getLektionsgebuehrenMap();
    BigDecimal wochenbetragCalculated = new BigDecimal(100);
    BigDecimal wochenbetragNachrechnungBefore = semesterrechnung.getWochenbetragNachrechnung();
    when(rechnungsempfaengerService.calculateWochenbetrag(
            semesterrechnung, semester, Rechnungstyp.VORRECHNUNG, lektionsgebuehrenMap))
        .thenReturn(new CalculateWochenbetragKurseResult(wochenbetragCalculated, true));
    boolean isSemesterrechnungUpdated;
    isSemesterrechnungUpdated =
        semesterrechnungService.calculateAndUpdateWochenbetrag(
            semesterrechnung, semester, Rechnungstyp.VORRECHNUNG, lektionsgebuehrenMap);
    assertTrue(isSemesterrechnungUpdated);
    BigDecimal wochenbetragVorrechnungAfter;
    wochenbetragVorrechnungAfter = semesterrechnung.getWochenbetragVorrechnung();
    assertEquals(0, wochenbetragCalculated.compareTo(wochenbetragVorrechnungAfter));
    BigDecimal wochenbetragNachrechnungAfter;
    wochenbetragNachrechnungAfter = semesterrechnung.getWochenbetragNachrechnung();
    assertSame(wochenbetragNachrechnungBefore, wochenbetragNachrechnungAfter);
    // Gleicher Aufruf nochmals: kein Update
    isSemesterrechnungUpdated =
        semesterrechnungService.calculateAndUpdateWochenbetrag(
            semesterrechnung, semester, Rechnungstyp.VORRECHNUNG, lektionsgebuehrenMap);
    assertFalse(isSemesterrechnungUpdated);
    wochenbetragVorrechnungAfter = semesterrechnung.getWochenbetragVorrechnung();
    assertEquals(0, wochenbetragCalculated.compareTo(wochenbetragVorrechnungAfter));
    wochenbetragNachrechnungAfter = semesterrechnung.getWochenbetragNachrechnung();
    assertSame(wochenbetragNachrechnungBefore, wochenbetragNachrechnungAfter);

    semesterrechnung = createSemesterrechnung(semester, createRechnungsempfaenger());
    semesterrechnung.setWochenbetragNachrechnung(BigDecimal.ZERO);
    BigDecimal wochenbetragVorrechnungBefore = semesterrechnung.getWochenbetragVorrechnung();
    when(rechnungsempfaengerService.calculateWochenbetrag(
            semesterrechnung, semester, Rechnungstyp.NACHRECHNUNG, lektionsgebuehrenMap))
        .thenReturn(new CalculateWochenbetragKurseResult(wochenbetragCalculated, true));
    isSemesterrechnungUpdated =
        semesterrechnungService.calculateAndUpdateWochenbetrag(
            semesterrechnung, semester, Rechnungstyp.NACHRECHNUNG, lektionsgebuehrenMap);
    assertTrue(isSemesterrechnungUpdated);
    wochenbetragNachrechnungAfter = semesterrechnung.getWochenbetragNachrechnung();
    assertEquals(0, wochenbetragCalculated.compareTo(wochenbetragNachrechnungAfter));
    wochenbetragVorrechnungAfter = semesterrechnung.getWochenbetragVorrechnung();
    assertSame(wochenbetragVorrechnungBefore, wochenbetragVorrechnungAfter);
    // Gleicher Aufruf nochmals: kein Update
    isSemesterrechnungUpdated =
        semesterrechnungService.calculateAndUpdateWochenbetrag(
            semesterrechnung, semester, Rechnungstyp.NACHRECHNUNG, lektionsgebuehrenMap);
    assertFalse(isSemesterrechnungUpdated);
    wochenbetragNachrechnungAfter = semesterrechnung.getWochenbetragNachrechnung();
    assertEquals(0, wochenbetragCalculated.compareTo(wochenbetragNachrechnungAfter));
    wochenbetragVorrechnungAfter = semesterrechnung.getWochenbetragVorrechnung();
    assertSame(wochenbetragVorrechnungBefore, wochenbetragVorrechnungAfter);

    semesterrechnung = createSemesterrechnung(semester, createRechnungsempfaenger());
    semesterrechnung.setWochenbetragVorrechnung(BigDecimal.ZERO);
    semesterrechnung.setWochenbetragNachrechnung(BigDecimal.ZERO);
    when(rechnungsempfaengerService.calculateWochenbetrag(
            semesterrechnung, semester, Rechnungstyp.VORRECHNUNG, lektionsgebuehrenMap))
        .thenReturn(new CalculateWochenbetragKurseResult(wochenbetragCalculated, false));
    isSemesterrechnungUpdated =
        semesterrechnungService.calculateAndUpdateWochenbetrag(
            semesterrechnung, semester, Rechnungstyp.VORRECHNUNG, lektionsgebuehrenMap);
    assertTrue(isSemesterrechnungUpdated);
    when(rechnungsempfaengerService.calculateWochenbetrag(
            semesterrechnung, semester, Rechnungstyp.NACHRECHNUNG, lektionsgebuehrenMap))
        .thenReturn(new CalculateWochenbetragKurseResult(wochenbetragCalculated, false));
    isSemesterrechnungUpdated =
        semesterrechnungService.calculateAndUpdateWochenbetrag(
            semesterrechnung, semester, Rechnungstyp.NACHRECHNUNG, lektionsgebuehrenMap);
    assertTrue(isSemesterrechnungUpdated);
    when(rechnungsempfaengerService.calculateWochenbetrag(
            semesterrechnung, semester, null, lektionsgebuehrenMap))
        .thenReturn(new CalculateWochenbetragKurseResult(wochenbetragCalculated, true));
    Semesterrechnung finalSemesterrechnung = semesterrechnung;
    assertThrows(
        IllegalArgumentException.class,
        () ->
            semesterrechnungService.calculateAndUpdateWochenbetrag(
                finalSemesterrechnung, semester, null, lektionsgebuehrenMap));
  }

  @Test
  void testCalculateAndUpdateSemesterrechnungCurrentSemester() {
    Semester semester = createSemester_20252026_ErstesSemester();
    Semesterrechnung semesterrechnung;
    semesterrechnung = createSemesterrechnung(semester, createRechnungsempfaenger());
    semesterrechnung.setAnzahlWochenVorrechnung(1);
    semesterrechnung.setWochenbetragVorrechnung(BigDecimal.ZERO);
    semesterrechnung.setAnzahlWochenNachrechnung(2);
    semesterrechnung.setWochenbetragNachrechnung(BigDecimal.ZERO);
    Map<Integer, BigDecimal[]> lektionsgebuehrenMap = getLektionsgebuehrenMap();
    BigDecimal wochenbetragCalculated = new BigDecimal(100);
    BigDecimal wochenbetragVorrechnungBefore = semesterrechnung.getWochenbetragVorrechnung();
    when(rechnungsempfaengerService.calculateMaxAnzahlWochen(
            semesterrechnung.getRechnungsempfaenger(), semester))
        .thenReturn(new CalculateMaxAnzahlWochenKursanmeldungenResult(5, false));
    when(rechnungsempfaengerService.calculateWochenbetrag(
            semesterrechnung, semester, Rechnungstyp.NACHRECHNUNG, lektionsgebuehrenMap))
        .thenReturn(new CalculateWochenbetragKurseResult(wochenbetragCalculated, true));
    boolean isSemesterrechnungUpdated;
    isSemesterrechnungUpdated =
        semesterrechnungService.calculateAndUpdateSemesterrechnungCurrentSemester(
            semester, semesterrechnung, lektionsgebuehrenMap);
    assertTrue(isSemesterrechnungUpdated);
    BigDecimal wochenbetragVorrechnungAfter;
    wochenbetragVorrechnungAfter = semesterrechnung.getWochenbetragVorrechnung();
    assertEquals(0, wochenbetragVorrechnungBefore.compareTo(wochenbetragVorrechnungAfter));
    BigDecimal wochenbetragNachrechnungAfter;
    wochenbetragNachrechnungAfter = semesterrechnung.getWochenbetragNachrechnung();
    assertEquals(0, wochenbetragCalculated.compareTo(wochenbetragNachrechnungAfter));
    // Gleicher Aufruf nochmals: kein Update
    isSemesterrechnungUpdated =
        semesterrechnungService.calculateAndUpdateSemesterrechnungCurrentSemester(
            semester, semesterrechnung, lektionsgebuehrenMap);
    assertFalse(isSemesterrechnungUpdated);
    wochenbetragVorrechnungAfter = semesterrechnung.getWochenbetragVorrechnung();
    assertEquals(0, wochenbetragVorrechnungBefore.compareTo(wochenbetragVorrechnungAfter));
    wochenbetragNachrechnungAfter = semesterrechnung.getWochenbetragNachrechnung();
    assertSame(wochenbetragCalculated, wochenbetragNachrechnungAfter);
    // Wochenbetrag ändern, damit ein Update gemacht wird (Anzahl Wochen unverändert, Wochenbetrag
    // geändert)
    semesterrechnung.setWochenbetragNachrechnung(new BigDecimal(99));
    isSemesterrechnungUpdated =
        semesterrechnungService.calculateAndUpdateSemesterrechnungCurrentSemester(
            semester, semesterrechnung, lektionsgebuehrenMap);
    assertTrue(isSemesterrechnungUpdated);
    wochenbetragVorrechnungAfter = semesterrechnung.getWochenbetragVorrechnung();
    assertEquals(0, wochenbetragVorrechnungBefore.compareTo(wochenbetragVorrechnungAfter));
    wochenbetragNachrechnungAfter = semesterrechnung.getWochenbetragNachrechnung();
    assertSame(wochenbetragCalculated, wochenbetragNachrechnungAfter);

    semesterrechnung = createSemesterrechnung(semester, createRechnungsempfaenger());
    semesterrechnung.setAnzahlWochenVorrechnung(50);
    semesterrechnung.setWochenbetragVorrechnung(BigDecimal.ZERO);
    semesterrechnung.setAnzahlWochenNachrechnung(50);
    semesterrechnung.setWochenbetragNachrechnung(BigDecimal.ZERO);
    semesterrechnung.setRechnungsdatumNachrechnung(createCalendar("2025-01-01"));
    when(rechnungsempfaengerService.calculateMaxAnzahlWochen(
            semesterrechnung.getRechnungsempfaenger(), semester))
        .thenReturn(new CalculateMaxAnzahlWochenKursanmeldungenResult(50, false));
    isSemesterrechnungUpdated =
        semesterrechnungService.calculateAndUpdateSemesterrechnungCurrentSemester(
            semester, semesterrechnung, lektionsgebuehrenMap);
    assertFalse(isSemesterrechnungUpdated);
  }

  @Test
  void testCalculateAndUpdateSemesterrechnungNextSemester() {
    Semester currentSemester = createSemester_20252026_ErstesSemester();
    Semester nextSemester = createSemester_20252026_ZweitesSemester();
    Semesterrechnung semesterrechnungNextSemester;
    semesterrechnungNextSemester =
        createSemesterrechnung(nextSemester, createRechnungsempfaenger());
    semesterrechnungNextSemester.setRechnungsdatumVorrechnung(createCalendar("2025-01-01"));
    semesterrechnungNextSemester.setAnzahlWochenVorrechnung(1);
    semesterrechnungNextSemester.setWochenbetragVorrechnung(BigDecimal.ZERO);
    semesterrechnungNextSemester.setAnzahlWochenNachrechnung(2);
    semesterrechnungNextSemester.setWochenbetragNachrechnung(BigDecimal.ZERO);
    Map<Integer, BigDecimal[]> lektionsgebuehrenMap = getLektionsgebuehrenMap();

    // Rechnungsdatum Vorrechnung nicht null: kein Update
    boolean isSemesterrechnungUpdated;
    isSemesterrechnungUpdated =
        semesterrechnungService.calculateAndUpdateSemesterrechnungNextSemester(
            currentSemester, semesterrechnungNextSemester, lektionsgebuehrenMap);
    assertFalse(isSemesterrechnungUpdated);

    // Rechnungsdatum Vorrechnung null: Update
    semesterrechnungNextSemester.setRechnungsdatumVorrechnung(null);
    when(rechnungsempfaengerService.calculateWochenbetrag(
            semesterrechnungNextSemester,
            currentSemester,
            Rechnungstyp.VORRECHNUNG,
            lektionsgebuehrenMap))
        .thenReturn(new CalculateWochenbetragKurseResult(new BigDecimal(100), true));
    isSemesterrechnungUpdated =
        semesterrechnungService.calculateAndUpdateSemesterrechnungNextSemester(
            currentSemester, semesterrechnungNextSemester, lektionsgebuehrenMap);
    assertTrue(isSemesterrechnungUpdated);

    // Nochmals gleicher Aufruf: kein Update
    isSemesterrechnungUpdated =
        semesterrechnungService.calculateAndUpdateSemesterrechnungNextSemester(
            currentSemester, semesterrechnungNextSemester, lektionsgebuehrenMap);
    assertFalse(isSemesterrechnungUpdated);
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

  private Semester createSemester_20252026_ZweitesSemester() {
    Semester semester = new Semester();
    semester.setSchuljahr("2025/2026");
    semester.setSemesterbezeichnung(Semesterbezeichnung.ZWEITES_SEMESTER);
    semester.setSemesterbeginn(createCalendar("2026-02-23"));
    semester.setSemesterende(createCalendar("2026-07-11"));
    semester.setFerienbeginn1(createCalendar("2026-04-20"));
    semester.setFerienende1(createCalendar("2026-05-02"));
    semester.setFerienbeginn2(null);
    semester.setFerienende2(null);
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

  private Map<Integer, BigDecimal[]> getLektionsgebuehrenMap() {
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
