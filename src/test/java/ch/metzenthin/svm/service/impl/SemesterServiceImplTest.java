package ch.metzenthin.svm.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import ch.metzenthin.svm.common.utils.Converter;
import ch.metzenthin.svm.domain.EntityAlreadyExistsException;
import ch.metzenthin.svm.domain.EntityStillReferencedException;
import ch.metzenthin.svm.domain.EntityWithOverlappingPeriodsException;
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
    assertFalse(semesterService.checkIfUpdateAffectsSemesterrechnungen(semester));

    // Semesterende eine Woche früher. Anzahl Wochen neu: 26
    semester.setSemesterende(Converter.toCalendar("08.02.2026"));
    assertTrue(semesterService.checkIfUpdateAffectsSemesterrechnungen(semester));
  }

  @Test
  void determineNaechstesNochNichtErfasstesSemester() {
    Semester semester = semesterService.determineNaechstesNochNichtErfasstesSemester();
    assertNotNull(semester);
  }

  @Test
  void findAllSemestersAndNumberOfKurse() {
    List<SemesterAndNumberOfKurse> semestersAndNumberOfKurse =
        semesterService.findAllSemestersAndNumberOfKurse();
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
    try {
      semesterService.saveSemesterAndUpdateAnzahlWochenOfSemesterrechnungen(semester, true);
    } catch (EntityAlreadyExistsException | EntityWithOverlappingPeriodsException e) {
      fail("Exception not expected: " + e.getMessage());
    }
    assertEquals("2025-2026", semester.getSchuljahr());
    List<Semesterrechnung> semesterrechnungenBySemesterId =
        semesterrechnungRepository.findSemesterrechnungenBySemesterId(semester.getSemesterId());
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

    try {
      semesterService.saveSemesterAndUpdateAnzahlWochenOfSemesterrechnungen(semester, true);
      Semesterrechnung semesterrechnung = semesterrechnungRepository.findAll().get(0);
      assertEquals(26, semesterrechnung.getAnzahlWochenVorrechnung());
      assertEquals(26, semesterrechnung.getAnzahlWochenNachrechnung());
    } catch (EntityAlreadyExistsException | EntityWithOverlappingPeriodsException e) {
      fail("Exception not expected: " + e.getMessage());
    }
  }

  @Test
  void saveSemesterAndUpdateAnzahlWochenOfSemesterrechnungen_updateFalse() throws ParseException {
    Optional<Semester> semesterOptional = semesterRepository.findById(101);
    assertTrue(semesterOptional.isPresent());
    Semester semester = semesterOptional.get();
    assertEquals(27, semester.getAnzahlSchulwochen());
    // Semesterende eine Woche früher. Anzahl Wochen neu: 26
    semester.setSemesterende(Converter.toCalendar("08.02.2026"));

    try {
      semesterService.saveSemesterAndUpdateAnzahlWochenOfSemesterrechnungen(semester, false);
      Semesterrechnung semesterrechnung = semesterrechnungRepository.findAll().get(0);
      assertEquals(27, semesterrechnung.getAnzahlWochenVorrechnung());
      assertEquals(27, semesterrechnung.getAnzahlWochenNachrechnung());
    } catch (EntityAlreadyExistsException | EntityWithOverlappingPeriodsException e) {
      fail("Exception not expected: " + e.getMessage());
    }
  }

  @Test
  void deleteSemesterrechnungenAndSemester() {
    Optional<Semester> semesterOptional = semesterRepository.findById(101);
    assertTrue(semesterOptional.isPresent());
    Semester semester = semesterOptional.get();
    try {
      semesterService.deleteSemesterrechnungenAndSemester(semester);
      assertTrue(semesterrechnungRepository.findAll().isEmpty());
      Optional<Semester> semesterOptionalAfter = semesterRepository.findById(101);
      assertFalse(semesterOptionalAfter.isPresent());
    } catch (EntityStillReferencedException e) {
      fail("Exception not expected: " + e.getMessage());
    }
  }
}
