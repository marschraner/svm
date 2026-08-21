package ch.metzenthin.svm.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import ch.metzenthin.svm.common.datatypes.Semesterbezeichnung;
import ch.metzenthin.svm.common.utils.Converter;
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
import java.util.Calendar;
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
  void checkIfUpdateAffectsSemesterrechnungen() throws ParseException {
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
  void testFindNaechstesSemester() {
    Semester semester = new Semester();

    semester.setSchuljahr("2024/2025");
    semester.setSemesterbezeichnung(Semesterbezeichnung.ZWEITES_SEMESTER);
    Optional<Semester> naechstesSemesterOptional = semesterService.findNaechstesSemester(semester);
    assertTrue(naechstesSemesterOptional.isPresent());
    assertEquals("2025/2026", naechstesSemesterOptional.get().getSchuljahr());
    assertEquals(
        Semesterbezeichnung.ERSTES_SEMESTER,
        naechstesSemesterOptional.get().getSemesterbezeichnung());

    semester.setSchuljahr("2025/2026");
    semester.setSemesterbezeichnung(Semesterbezeichnung.ERSTES_SEMESTER);
    naechstesSemesterOptional = semesterService.findNaechstesSemester(semester);
    assertTrue(naechstesSemesterOptional.isPresent());
    assertEquals("2025/2026", naechstesSemesterOptional.get().getSchuljahr());
    assertEquals(
        Semesterbezeichnung.ZWEITES_SEMESTER,
        naechstesSemesterOptional.get().getSemesterbezeichnung());

    semester.setSemesterbezeichnung(Semesterbezeichnung.ZWEITES_SEMESTER);
    naechstesSemesterOptional = semesterService.findNaechstesSemester(semester);
    assertTrue(naechstesSemesterOptional.isEmpty());
  }

  @Test
  void determineNaechstesNochNichtErfasstesSemester() {
    Semester semester = semesterService.determineNaechstesNochNichtErfasstesSemester();
    assertNotNull(semester);
  }

  @Test
  void findAllSemesterAndNumberOfKurse() {
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
  void saveSemesterAndUpdateAnzahlWochenOfSemesterrechnungen_updateTrueNoUpdates() {
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
  void saveSemesterAndUpdateAnzahlWochenOfSemesterrechnungen_updateTrueWithUpdates()
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
  void saveSemesterAndUpdateAnzahlWochenOfSemesterrechnungen_updateFalse() throws ParseException {
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
  void deleteSemesterrechnungenAndSemester() {
    Optional<Semester> semesterOptional = semesterRepository.findById(101);
    assertTrue(semesterOptional.isPresent());
    Semester semester = semesterOptional.get();
    semesterService.deleteSemesterrechnungenAndSemester(semester);
    assertTrue(semesterrechnungRepository.findAll().isEmpty());
    Optional<Semester> semesterOptionalAfter = semesterRepository.findById(101);
    assertFalse(semesterOptionalAfter.isPresent());
  }
}
