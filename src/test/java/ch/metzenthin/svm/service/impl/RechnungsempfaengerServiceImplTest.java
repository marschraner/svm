package ch.metzenthin.svm.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.persistence.entities.Semesterrechnung;
import ch.metzenthin.svm.persistence.repository.SemesterRepository;
import ch.metzenthin.svm.persistence.repository.SemesterrechnungRepository;
import ch.metzenthin.svm.service.RechnungsempfaengerService;
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
@Sql(scripts = "classpath:RechnungsempfaengerServiceImplTest_Create.sql")
@Sql(
    scripts = "classpath:RechnungsempfaengerServiceImplTest_Delete.sql",
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class RechnungsempfaengerServiceImplTest {

  @Autowired SemesterRepository semesterRepository;
  @Autowired SemesterrechnungRepository semesterrechnungRepository;
  @Autowired RechnungsempfaengerService rechnungsempfaengerService;

  @Test
  void calculateHoechsteAnzahlWochen() {
    Optional<Semester> semesterOptional = semesterRepository.findById(101);
    assertTrue(semesterOptional.isPresent());
    Semester semester = semesterOptional.get();
    List<Semesterrechnung> semesterrechnungenAll = semesterrechnungRepository.findAll();
    Semesterrechnung semesterrechnung;

    // Schüler volles Semester
    semesterrechnung = findSemesterrechnung(semesterrechnungenAll, 501);
    int anzahlWochen =
        rechnungsempfaengerService.calculateHoechsteAnzahlWochen(
            semesterrechnung.getRechnungsempfaenger(), semester);
    assertEquals(21, anzahlWochen);

    // Schüler abgemeldet
    semesterrechnung = findSemesterrechnung(semesterrechnungenAll, 503);
    anzahlWochen =
        rechnungsempfaengerService.calculateHoechsteAnzahlWochen(
            semesterrechnung.getRechnungsempfaenger(), semester);
    assertEquals(21, anzahlWochen);

    // zwei Schüler Kursanmeldung abgemeldet innerhalb Semester
    semesterrechnung = findSemesterrechnung(semesterrechnungenAll, 505);
    anzahlWochen =
        rechnungsempfaengerService.calculateHoechsteAnzahlWochen(
            semesterrechnung.getRechnungsempfaenger(), semester);
    assertEquals(20, anzahlWochen);
  }

  @SuppressWarnings("OptionalGetWithoutIsPresent")
  private static Semesterrechnung findSemesterrechnung(
      List<Semesterrechnung> semesterrechnungenAll, int personId) {
    Semesterrechnung semesterrechnung;
    semesterrechnung =
        semesterrechnungenAll.stream()
            .filter(
                sr ->
                    sr.getSemester().getSemesterId() == 101
                        && sr.getRechnungsempfaenger().getPersonId() == personId)
            .findFirst()
            .get();
    return semesterrechnung;
  }
}
