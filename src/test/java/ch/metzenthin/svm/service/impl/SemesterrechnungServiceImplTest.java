package ch.metzenthin.svm.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.persistence.entities.Semesterrechnung;
import ch.metzenthin.svm.persistence.repository.SemesterRepository;
import ch.metzenthin.svm.persistence.repository.SemesterrechnungRepository;
import ch.metzenthin.svm.service.SemesterrechnungService;
import ch.metzenthin.svm.service.ServiceTestConfiguration;
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
@Sql(scripts = "classpath:SemesterrechnungServiceImplTest_Create.sql")
@Sql(
    scripts = "classpath:SemesterrechnungServiceImplTest_Delete.sql",
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class SemesterrechnungServiceImplTest {

  @Autowired private SemesterRepository semesterRepository;
  @Autowired private SemesterrechnungRepository semesterrechnungRepository;
  @Autowired private SemesterrechnungService semesterrechnungService;

  @Test
  void calculateAndUpdateAnzahlWochen() {
    Optional<Semester> semesterOptional101 = semesterRepository.findById(101);
    assertTrue(semesterOptional101.isPresent());
    Semester semester101 = semesterOptional101.get();
    List<Semesterrechnung> semesterrechnungenAll = semesterrechnungRepository.findAll();
    Semesterrechnung semesterrechnung;
    int semesterrechnungUpdated;

    // Schüler volles Semester
    semesterrechnung =
        findSemesterrechnung(semesterrechnungenAll, 501, semester101.getSemesterId());
    semesterrechnungUpdated = semesterrechnungService.calculateAndUpdateAnzahlWochen(semester101);
    assertEquals(1, semesterrechnungUpdated);
    assertEquals(21, semesterrechnung.getAnzahlWochenVorrechnung());
    assertEquals(21, semesterrechnung.getAnzahlWochenNachrechnung());

    // Kein Update, da Datum Semesterrechnung bereits vorhanden
    Optional<Semester> semesterOptional102 = semesterRepository.findById(102);
    assertTrue(semesterOptional102.isPresent());
    Semester semester102 = semesterOptional102.get();
    semesterrechnung =
        findSemesterrechnung(semesterrechnungenAll, 503, semester102.getSemesterId());
    semesterrechnungUpdated = semesterrechnungService.calculateAndUpdateAnzahlWochen(semester102);
    assertEquals(0, semesterrechnungUpdated);
    assertEquals(18, semesterrechnung.getAnzahlWochenVorrechnung());
    assertEquals(19, semesterrechnung.getAnzahlWochenNachrechnung());

    // Zwei Schüler Kursanmeldung abgemeldet innerhalb Semester
    Optional<Semester> semesterOptional103 = semesterRepository.findById(103);
    assertTrue(semesterOptional103.isPresent());
    Semester semester103 = semesterOptional103.get();
    semesterrechnung =
        findSemesterrechnung(semesterrechnungenAll, 505, semester103.getSemesterId());
    semesterrechnungUpdated = semesterrechnungService.calculateAndUpdateAnzahlWochen(semester103);
    assertEquals(1, semesterrechnungUpdated);
    assertEquals(20, semesterrechnung.getAnzahlWochenVorrechnung());
    assertEquals(20, semesterrechnung.getAnzahlWochenNachrechnung());
  }

  @SuppressWarnings("OptionalGetWithoutIsPresent")
  private static Semesterrechnung findSemesterrechnung(
      List<Semesterrechnung> semesterrechnungenAll, int personId, int semesterId) {
    Semesterrechnung semesterrechnung;
    semesterrechnung =
        semesterrechnungenAll.stream()
            .filter(
                sr ->
                    sr.getSemester().getSemesterId() == semesterId
                        && sr.getRechnungsempfaenger().getPersonId() == personId)
            .findFirst()
            .get();
    return semesterrechnung;
  }
}
