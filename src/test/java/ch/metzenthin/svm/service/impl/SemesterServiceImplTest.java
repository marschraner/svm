package ch.metzenthin.svm.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import ch.metzenthin.svm.common.datatypes.Semesterbezeichnung;
import ch.metzenthin.svm.common.utils.Converter;
import ch.metzenthin.svm.domain.model.PreviousCurrentNextSemester;
import ch.metzenthin.svm.domain.model.SemesterAndNumberOfKurse;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.persistence.entities.Semesterrechnung;
import ch.metzenthin.svm.persistence.repository.SemesterRepository;
import ch.metzenthin.svm.persistence.repository.SemesterrechnungRepository;
import ch.metzenthin.svm.service.SemesterService;
import ch.metzenthin.svm.service.ServiceTestConfiguration;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

/**
 * @author Hans Stamm
 */
@DataJpaTest
@ContextConfiguration(classes = ServiceTestConfiguration.class)
@Sql(scripts = "classpath:SemesterServiceImplTest_Create.sql")
@Sql(
    scripts = "classpath:SemesterServiceImplTest_Delete.sql",
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class SemesterServiceImplTest {

  @Autowired private SemesterService semesterService;
  @Autowired private SemesterRepository semesterRepository;
  @Autowired private SemesterrechnungRepository semesterrechnungRepository;

  @PersistenceContext private EntityManager entityManager;

  @Test
  void testCheckIfUpdateAffectsSemesterrechnungen() throws ParseException {
    Optional<Semester> semesterOptional = semesterRepository.findById(101);
    assertTrue(semesterOptional.isPresent());
    Semester semester = semesterOptional.get();
    assertEquals(27, semester.getAnzahlSchulwochen());
    // Detach Semester damit der alte Stand von der DB gelesen werden kann in der Check-Methode
    entityManager.detach(semester);

    // Keine Änderung
    assertFalse(
        semesterService.checkIfUpdateAffectsSemesterrechnungen(
            semester.getSemesterId(),
            semester.getSemesterbeginn(),
            semester.getSemesterende(),
            semester.getFerienbeginn1(),
            semester.getFerienende1(),
            semester.getFerienbeginn2(),
            semester.getFerienende2()));

    // Semesterende eine Woche früher. Anzahl Wochen neu: 26
    Calendar semesterende = Converter.toCalendar("08.02.2026");
    assertTrue(
        semesterService.checkIfUpdateAffectsSemesterrechnungen(
            semester.getSemesterId(),
            semester.getSemesterbeginn(),
            semesterende,
            semester.getFerienbeginn1(),
            semester.getFerienende1(),
            semester.getFerienbeginn2(),
            semester.getFerienende2()));
  }

  @Test
  void testFindNextSemester() {
    Semester semester = new Semester();

    semester.setSchuljahr("2024/2025");
    semester.setSemesterbezeichnung(Semesterbezeichnung.ZWEITES_SEMESTER);
    Optional<Semester> nextSemesterOptional = semesterService.findNextSemester(semester);
    assertTrue(nextSemesterOptional.isPresent());
    assertEquals("2025/2026", nextSemesterOptional.get().getSchuljahr());
    assertEquals(
        Semesterbezeichnung.ERSTES_SEMESTER, nextSemesterOptional.get().getSemesterbezeichnung());

    semester.setSchuljahr("2025/2026");
    semester.setSemesterbezeichnung(Semesterbezeichnung.ERSTES_SEMESTER);
    nextSemesterOptional = semesterService.findNextSemester(semester);
    assertTrue(nextSemesterOptional.isPresent());
    assertEquals("2025/2026", nextSemesterOptional.get().getSchuljahr());
    assertEquals(
        Semesterbezeichnung.ZWEITES_SEMESTER, nextSemesterOptional.get().getSemesterbezeichnung());

    semester.setSemesterbezeichnung(Semesterbezeichnung.ZWEITES_SEMESTER);
    nextSemesterOptional = semesterService.findNextSemester(semester);
    assertTrue(nextSemesterOptional.isEmpty());
  }

  @Test
  void testFindSemesterOneYearBefore() {
    assertTrue(semesterService.findSemesterOneYearBefore(null).isEmpty());

    Semester semester = new Semester();

    semester.setSchuljahr("2026/2027");
    semester.setSemesterbezeichnung(Semesterbezeichnung.ZWEITES_SEMESTER);
    Optional<Semester> semesterOneYearBeforeOptional =
        semesterService.findSemesterOneYearBefore(semester);
    assertTrue(semesterOneYearBeforeOptional.isPresent());
    assertEquals("2025/2026", semesterOneYearBeforeOptional.get().getSchuljahr());
    assertEquals(
        Semesterbezeichnung.ZWEITES_SEMESTER,
        semesterOneYearBeforeOptional.get().getSemesterbezeichnung());

    semester.setSchuljahr("2026/2027");
    semester.setSemesterbezeichnung(Semesterbezeichnung.ERSTES_SEMESTER);
    semesterOneYearBeforeOptional = semesterService.findSemesterOneYearBefore(semester);
    assertTrue(semesterOneYearBeforeOptional.isPresent());
    assertEquals("2025/2026", semesterOneYearBeforeOptional.get().getSchuljahr());
    assertEquals(
        Semesterbezeichnung.ERSTES_SEMESTER,
        semesterOneYearBeforeOptional.get().getSemesterbezeichnung());

    semester.setSchuljahr("2027/2028");
    semester.setSemesterbezeichnung(Semesterbezeichnung.ERSTES_SEMESTER);
    semesterOneYearBeforeOptional = semesterService.findSemesterOneYearBefore(semester);
    assertTrue(semesterOneYearBeforeOptional.isEmpty());
  }

  @Test
  void testFindPreviousSemester() {
    assertTrue(semesterService.findPreviousSemester(null).isEmpty());

    Semester semester = new Semester();

    semester.setSchuljahr("2025/2026");
    semester.setSemesterbezeichnung(Semesterbezeichnung.ZWEITES_SEMESTER);
    Optional<Semester> previousSemesterOptional = semesterService.findPreviousSemester(semester);
    assertTrue(previousSemesterOptional.isPresent());
    assertEquals("2025/2026", previousSemesterOptional.get().getSchuljahr());
    assertEquals(
        Semesterbezeichnung.ERSTES_SEMESTER,
        previousSemesterOptional.get().getSemesterbezeichnung());

    semester.setSchuljahr("2026/2027");
    semester.setSemesterbezeichnung(Semesterbezeichnung.ERSTES_SEMESTER);
    previousSemesterOptional = semesterService.findPreviousSemester(semester);
    assertTrue(previousSemesterOptional.isPresent());
    assertEquals("2025/2026", previousSemesterOptional.get().getSchuljahr());
    assertEquals(
        Semesterbezeichnung.ZWEITES_SEMESTER,
        previousSemesterOptional.get().getSemesterbezeichnung());

    semester.setSchuljahr("2024/2025");
    semester.setSemesterbezeichnung(Semesterbezeichnung.ERSTES_SEMESTER);
    previousSemesterOptional = semesterService.findPreviousSemester(semester);
    assertTrue(previousSemesterOptional.isEmpty());
  }

  @Test
  void testDetermineNaechstesNochNichtErfasstesSemester() {
    Semester semester = semesterService.determineNaechstesNochNichtErfasstesSemester();
    assertNotNull(semester);
  }

  private static List<Semester> initSemesterList() {
    List<Semester> semesterList = new ArrayList<>();
    semesterList.add(
        new Semester(
            "2011/2012",
            Semesterbezeichnung.ERSTES_SEMESTER,
            new GregorianCalendar(2011, Calendar.AUGUST, 20),
            new GregorianCalendar(2012, Calendar.FEBRUARY, 10),
            new GregorianCalendar(2011, Calendar.OCTOBER, 5),
            new GregorianCalendar(2011, Calendar.OCTOBER, 17),
            new GregorianCalendar(2011, Calendar.DECEMBER, 21),
            new GregorianCalendar(2012, Calendar.JANUARY, 2)));
    semesterList.add(
        new Semester(
            "2011/2012",
            Semesterbezeichnung.ZWEITES_SEMESTER,
            new GregorianCalendar(2012, Calendar.FEBRUARY, 20),
            new GregorianCalendar(2012, Calendar.JULY, 10),
            new GregorianCalendar(2012, Calendar.APRIL, 25),
            new GregorianCalendar(2012, Calendar.MAY, 7),
            null,
            null));
    semesterList.add(
        new Semester(
            "2012/2013",
            Semesterbezeichnung.ERSTES_SEMESTER,
            new GregorianCalendar(2012, Calendar.AUGUST, 21),
            new GregorianCalendar(2013, Calendar.FEBRUARY, 11),
            new GregorianCalendar(2012, Calendar.OCTOBER, 5),
            new GregorianCalendar(2012, Calendar.OCTOBER, 17),
            new GregorianCalendar(2012, Calendar.DECEMBER, 21),
            new GregorianCalendar(2013, Calendar.JANUARY, 2)));
    return semesterList;
  }

  @Test
  void testDeterminePreviousCurrentNextSemesterFor_CalendarWaehrendErstemSemester() {
    List<Semester> semesterList = initSemesterList();

    PreviousCurrentNextSemester previousCurrentNextSemester =
        semesterService.determinePreviousCurrentNextSemesterFor(
            new GregorianCalendar(2011, Calendar.AUGUST, 21), semesterList);

    assertTrue(previousCurrentNextSemester.previousSemesterOptional().isEmpty());

    assertTrue(previousCurrentNextSemester.currentSemesterOptional().isPresent());
    assertEquals(
        new GregorianCalendar(2011, Calendar.AUGUST, 20),
        previousCurrentNextSemester.currentSemesterOptional().get().getSemesterbeginn());

    assertTrue(previousCurrentNextSemester.nextSemesterOptional().isPresent());
    assertEquals(
        new GregorianCalendar(2012, Calendar.FEBRUARY, 20),
        previousCurrentNextSemester.nextSemesterOptional().get().getSemesterbeginn());
  }

  @Test
  void testDeterminePreviousCurrentNextSemesterFor_CalendarWaehrendMittleremSemester() {
    List<Semester> semesterList = initSemesterList();

    PreviousCurrentNextSemester previousCurrentNextSemester =
        semesterService.determinePreviousCurrentNextSemesterFor(
            new GregorianCalendar(2012, Calendar.FEBRUARY, 21), semesterList);

    assertTrue(previousCurrentNextSemester.previousSemesterOptional().isPresent());
    assertEquals(
        new GregorianCalendar(2011, Calendar.AUGUST, 20),
        previousCurrentNextSemester.previousSemesterOptional().get().getSemesterbeginn());

    assertTrue(previousCurrentNextSemester.currentSemesterOptional().isPresent());
    assertEquals(
        new GregorianCalendar(2012, Calendar.FEBRUARY, 20),
        previousCurrentNextSemester.currentSemesterOptional().get().getSemesterbeginn());

    assertTrue(previousCurrentNextSemester.nextSemesterOptional().isPresent());
    assertEquals(
        new GregorianCalendar(2012, Calendar.AUGUST, 21),
        previousCurrentNextSemester.nextSemesterOptional().get().getSemesterbeginn());
  }

  @Test
  void testDeterminePreviousCurrentNextSemesterFor_CalendarWaehrendMittleremSemesterErsterTag() {
    List<Semester> semesterList = initSemesterList();

    PreviousCurrentNextSemester previousCurrentNextSemester =
        semesterService.determinePreviousCurrentNextSemesterFor(
            new GregorianCalendar(2012, Calendar.FEBRUARY, 20), semesterList);

    assertTrue(previousCurrentNextSemester.previousSemesterOptional().isPresent());
    assertEquals(
        new GregorianCalendar(2011, Calendar.AUGUST, 20),
        previousCurrentNextSemester.previousSemesterOptional().get().getSemesterbeginn());

    assertTrue(previousCurrentNextSemester.currentSemesterOptional().isPresent());
    assertEquals(
        new GregorianCalendar(2012, Calendar.FEBRUARY, 20),
        previousCurrentNextSemester.currentSemesterOptional().get().getSemesterbeginn());

    assertTrue(previousCurrentNextSemester.nextSemesterOptional().isPresent());
    assertEquals(
        new GregorianCalendar(2012, Calendar.AUGUST, 21),
        previousCurrentNextSemester.nextSemesterOptional().get().getSemesterbeginn());
  }

  @Test
  void testDeterminePreviousCurrentNextSemesterFor_CalendarWaehrendMittleremSemesterLetzterTag() {
    List<Semester> semesterList = initSemesterList();

    PreviousCurrentNextSemester previousCurrentNextSemester =
        semesterService.determinePreviousCurrentNextSemesterFor(
            new GregorianCalendar(2012, Calendar.JULY, 10), semesterList);

    assertTrue(previousCurrentNextSemester.previousSemesterOptional().isPresent());
    assertEquals(
        new GregorianCalendar(2011, Calendar.AUGUST, 20),
        previousCurrentNextSemester.previousSemesterOptional().get().getSemesterbeginn());

    assertTrue(previousCurrentNextSemester.currentSemesterOptional().isPresent());
    assertEquals(
        new GregorianCalendar(2012, Calendar.FEBRUARY, 20),
        previousCurrentNextSemester.currentSemesterOptional().get().getSemesterbeginn());

    assertTrue(previousCurrentNextSemester.nextSemesterOptional().isPresent());
    assertEquals(
        new GregorianCalendar(2012, Calendar.AUGUST, 21),
        previousCurrentNextSemester.nextSemesterOptional().get().getSemesterbeginn());
  }

  @Test
  void testDeterminePreviousCurrentNextSemesterFor_CalendarWaehrendLetztemSemester() {
    List<Semester> semesterList = initSemesterList();

    PreviousCurrentNextSemester previousCurrentNextSemester =
        semesterService.determinePreviousCurrentNextSemesterFor(
            new GregorianCalendar(2012, Calendar.AUGUST, 21), semesterList);

    assertTrue(previousCurrentNextSemester.previousSemesterOptional().isPresent());
    assertEquals(
        new GregorianCalendar(2012, Calendar.FEBRUARY, 20),
        previousCurrentNextSemester.previousSemesterOptional().get().getSemesterbeginn());

    assertTrue(previousCurrentNextSemester.currentSemesterOptional().isPresent());
    assertEquals(
        new GregorianCalendar(2012, Calendar.AUGUST, 21),
        previousCurrentNextSemester.currentSemesterOptional().get().getSemesterbeginn());

    assertTrue(previousCurrentNextSemester.nextSemesterOptional().isEmpty());
  }

  @Test
  void testDeterminePreviousCurrentNextSemesterFor_CalendarVorErstemSemester() {
    List<Semester> semesterList = initSemesterList();

    PreviousCurrentNextSemester previousCurrentNextSemester =
        semesterService.determinePreviousCurrentNextSemesterFor(
            new GregorianCalendar(2011, Calendar.AUGUST, 19), semesterList);

    assertTrue(previousCurrentNextSemester.previousSemesterOptional().isEmpty());

    assertTrue(previousCurrentNextSemester.currentSemesterOptional().isEmpty());

    assertTrue(previousCurrentNextSemester.nextSemesterOptional().isPresent());
    assertEquals(
        new GregorianCalendar(2011, Calendar.AUGUST, 20),
        previousCurrentNextSemester.nextSemesterOptional().get().getSemesterbeginn());
  }

  @Test
  void testDeterminePreviousCurrentNextSemesterFor_CalendarZwischenZweiSemestern() {
    List<Semester> semesterList = initSemesterList();

    PreviousCurrentNextSemester previousCurrentNextSemester =
        semesterService.determinePreviousCurrentNextSemesterFor(
            new GregorianCalendar(2012, Calendar.FEBRUARY, 11), semesterList);

    assertTrue(previousCurrentNextSemester.previousSemesterOptional().isPresent());
    assertEquals(
        new GregorianCalendar(2011, Calendar.AUGUST, 20),
        previousCurrentNextSemester.previousSemesterOptional().get().getSemesterbeginn());

    assertTrue(previousCurrentNextSemester.currentSemesterOptional().isEmpty());

    assertTrue(previousCurrentNextSemester.nextSemesterOptional().isPresent());
    assertEquals(
        new GregorianCalendar(2012, Calendar.FEBRUARY, 20),
        previousCurrentNextSemester.nextSemesterOptional().get().getSemesterbeginn());
  }

  @Test
  void testDeterminePreviousCurrentNextSemesterFor_NachLetztemSemester() {
    List<Semester> semesterList = initSemesterList();

    PreviousCurrentNextSemester previousCurrentNextSemester =
        semesterService.determinePreviousCurrentNextSemesterFor(
            new GregorianCalendar(2013, Calendar.FEBRUARY, 12), semesterList);

    assertTrue(previousCurrentNextSemester.previousSemesterOptional().isPresent());
    assertEquals(
        new GregorianCalendar(2012, Calendar.AUGUST, 21),
        previousCurrentNextSemester.previousSemesterOptional().get().getSemesterbeginn());

    assertTrue(previousCurrentNextSemester.currentSemesterOptional().isEmpty());

    assertTrue(previousCurrentNextSemester.nextSemesterOptional().isEmpty());
  }

  @Test
  void
      testDeterminePreviousCurrentNextSemesterFor_ErfassteSemesterNurEinSemester_WaehrendSemester() {
    List<Semester> semesterList = new ArrayList<>();
    semesterList.add(
        new Semester(
            "2011/2012",
            Semesterbezeichnung.ERSTES_SEMESTER,
            new GregorianCalendar(2011, Calendar.AUGUST, 20),
            new GregorianCalendar(2012, Calendar.FEBRUARY, 10),
            new GregorianCalendar(2011, Calendar.OCTOBER, 5),
            new GregorianCalendar(2011, Calendar.OCTOBER, 17),
            new GregorianCalendar(2011, Calendar.DECEMBER, 21),
            new GregorianCalendar(2012, Calendar.JANUARY, 2)));

    PreviousCurrentNextSemester previousCurrentNextSemester =
        semesterService.determinePreviousCurrentNextSemesterFor(
            new GregorianCalendar(2011, Calendar.AUGUST, 21), semesterList);

    assertTrue(previousCurrentNextSemester.previousSemesterOptional().isEmpty());

    assertTrue(previousCurrentNextSemester.currentSemesterOptional().isPresent());
    assertEquals(
        new GregorianCalendar(2011, Calendar.AUGUST, 20),
        previousCurrentNextSemester.currentSemesterOptional().get().getSemesterbeginn());

    assertTrue(previousCurrentNextSemester.nextSemesterOptional().isEmpty());
  }

  @Test
  void testDeterminePreviousCurrentNextSemesterFor_ErfassteSemesterNurEinSemester_VorSemester() {
    List<Semester> semesterList = new ArrayList<>();
    semesterList.add(
        new Semester(
            "2011/2012",
            Semesterbezeichnung.ERSTES_SEMESTER,
            new GregorianCalendar(2011, Calendar.AUGUST, 20),
            new GregorianCalendar(2012, Calendar.FEBRUARY, 10),
            new GregorianCalendar(2011, Calendar.OCTOBER, 5),
            new GregorianCalendar(2011, Calendar.OCTOBER, 17),
            new GregorianCalendar(2011, Calendar.DECEMBER, 21),
            new GregorianCalendar(2012, Calendar.JANUARY, 2)));

    PreviousCurrentNextSemester previousCurrentNextSemester =
        semesterService.determinePreviousCurrentNextSemesterFor(
            new GregorianCalendar(2011, Calendar.AUGUST, 19), semesterList);

    assertTrue(previousCurrentNextSemester.previousSemesterOptional().isEmpty());

    assertTrue(previousCurrentNextSemester.currentSemesterOptional().isEmpty());

    assertTrue(previousCurrentNextSemester.nextSemesterOptional().isPresent());
    assertEquals(
        new GregorianCalendar(2011, Calendar.AUGUST, 20),
        previousCurrentNextSemester.nextSemesterOptional().get().getSemesterbeginn());
  }

  @Test
  void testDeterminePreviousCurrentNextSemesterFor_ErfassteSemesterNurEinSemester_NachSemester() {
    List<Semester> semesterList = new ArrayList<>();
    semesterList.add(
        new Semester(
            "2011/2012",
            Semesterbezeichnung.ERSTES_SEMESTER,
            new GregorianCalendar(2011, Calendar.AUGUST, 20),
            new GregorianCalendar(2012, Calendar.FEBRUARY, 10),
            new GregorianCalendar(2011, Calendar.OCTOBER, 5),
            new GregorianCalendar(2011, Calendar.OCTOBER, 17),
            new GregorianCalendar(2011, Calendar.DECEMBER, 21),
            new GregorianCalendar(2012, Calendar.JANUARY, 2)));

    PreviousCurrentNextSemester previousCurrentNextSemester =
        semesterService.determinePreviousCurrentNextSemesterFor(
            new GregorianCalendar(2012, Calendar.FEBRUARY, 11), semesterList);

    assertTrue(previousCurrentNextSemester.previousSemesterOptional().isPresent());
    assertEquals(
        new GregorianCalendar(2011, Calendar.AUGUST, 20),
        previousCurrentNextSemester.previousSemesterOptional().get().getSemesterbeginn());

    assertTrue(previousCurrentNextSemester.currentSemesterOptional().isEmpty());

    assertTrue(previousCurrentNextSemester.nextSemesterOptional().isEmpty());
  }

  @Test
  void testFindAllSemestersAndNumberOfKurse() {
    List<SemesterAndNumberOfKurse> semestersAndNumberOfKurse =
        semesterService.findAllSemesterAndNumberOfKurse();
    assertEquals(2, semestersAndNumberOfKurse.size());
    for (SemesterAndNumberOfKurse semesterAndNumberOfKurse : semestersAndNumberOfKurse) {
      if (semesterAndNumberOfKurse.semester().getSemesterId() == 101) {
        assertEquals(0, semesterAndNumberOfKurse.numberOfKurse());
      } else if (semesterAndNumberOfKurse.semester().getSemesterId() == 102) {
        assertEquals(2, semesterAndNumberOfKurse.numberOfKurse());
      } else {
        fail("Semester nicht erwartet: " + semesterAndNumberOfKurse);
      }
    }
  }

  @Test
  void testSaveSemesterAndUpdateAnzahlWochenOfSemesterrechnungen_updateTrueNoUpdates() {
    Optional<Semester> semesterOptional = semesterRepository.findById(101);
    assertTrue(semesterOptional.isPresent());
    Semester semester = semesterOptional.get();
    assertEquals(27, semester.getAnzahlSchulwochen());
    // Mutation, die keine Änderung an den Schulwochen auslöst
    semester.setSchuljahr("2025-2026");

    semesterService.saveSemesterAndUpdateAnzahlWochenOfSemesterrechnungen(semester, true);
    assertEquals("2025-2026", semester.getSchuljahr());
    List<Semesterrechnung> semesterrechnungenBySemesterId =
        semesterrechnungRepository.findBySemesterId(semester.getSemesterId());
    assertEquals(1, semesterrechnungenBySemesterId.size());
    Semesterrechnung semesterrechnung = semesterrechnungenBySemesterId.get(0);
    assertEquals(27, semesterrechnung.getAnzahlWochenVorrechnung());
    assertEquals(27, semesterrechnung.getAnzahlWochenNachrechnung());
  }

  @Test
  void testSaveSemesterAndUpdateAnzahlWochenOfSemesterrechnungen_updateTrueWithUpdates()
      throws ParseException {
    Optional<Semester> semesterOptional = semesterRepository.findById(101);
    assertTrue(semesterOptional.isPresent());
    Semester semester = semesterOptional.get();
    assertEquals(27, semester.getAnzahlSchulwochen());
    // Semesterende eine Woche früher. Anzahl Wochen neu: 26
    semester.setSemesterende(Converter.toCalendar("08.02.2026"));

    semesterService.saveSemesterAndUpdateAnzahlWochenOfSemesterrechnungen(semester, true);
    Semesterrechnung semesterrechnung = semesterrechnungRepository.findAll().get(0);
    assertEquals(26, semesterrechnung.getAnzahlWochenVorrechnung());
    assertEquals(26, semesterrechnung.getAnzahlWochenNachrechnung());
  }

  @Test
  void testSaveSemesterAndUpdateAnzahlWochenOfSemesterrechnungen_updateFalse()
      throws ParseException {
    Optional<Semester> semesterOptional = semesterRepository.findById(101);
    assertTrue(semesterOptional.isPresent());
    Semester semester = semesterOptional.get();
    assertEquals(27, semester.getAnzahlSchulwochen());
    // Semesterende eine Woche früher. Anzahl Wochen neu: 26
    semester.setSemesterende(Converter.toCalendar("08.02.2026"));

    semesterService.saveSemesterAndUpdateAnzahlWochenOfSemesterrechnungen(semester, false);
    Semesterrechnung semesterrechnung = semesterrechnungRepository.findAll().get(0);
    assertEquals(27, semesterrechnung.getAnzahlWochenVorrechnung());
    assertEquals(27, semesterrechnung.getAnzahlWochenNachrechnung());
  }

  @Test
  void testDeleteSemesterrechnungenAndSemester() {
    Optional<Semester> semesterOptional = semesterRepository.findById(101);
    assertTrue(semesterOptional.isPresent());
    Semester semester = semesterOptional.get();
    semesterService.deleteSemesterrechnungenAndSemester(semester);
    assertTrue(semesterrechnungRepository.findAll().isEmpty());
    Optional<Semester> semesterOptionalAfter = semesterRepository.findById(101);
    assertFalse(semesterOptionalAfter.isPresent());
  }
}
