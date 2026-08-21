package ch.metzenthin.svm.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.persistence.entities.Semesterrechnung;
import ch.metzenthin.svm.persistence.repository.SemesterRepository;
import ch.metzenthin.svm.persistence.repository.SemesterrechnungRepository;
import ch.metzenthin.svm.service.ServiceTestConfiguration;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
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
  @Autowired private SemesterrechnungServiceImpl semesterrechnungService;

  @Test
  void testExistsReferencedCodeByCodeId() {
    assertTrue(semesterrechnungService.existsReferencedCodeByCodeId(701));
    assertFalse(semesterrechnungService.existsReferencedCodeByCodeId(999));
  }

  @Test
  void testCountSemesterrechnungenBySemesterId() {
    assertEquals(1, semesterrechnungService.countSemesterrechnungenBySemesterId(101));
    assertEquals(0, semesterrechnungService.countSemesterrechnungenBySemesterId(999));
  }

  @Test
  void testCalculateAndUpdateAnzahlWochen() {
    Semester semester101 = findSemester(101);
    List<Semesterrechnung> semesterrechnungenAll = semesterrechnungRepository.findAll();
    Semesterrechnung semesterrechnung;
    int semesterrechnungUpdated;

    // Schüler volles Semester, Anzahl Wochen Semester: 18
    semesterrechnung =
        findSemesterrechnung(semesterrechnungenAll, 501, semester101.getSemesterId());
    semesterrechnungUpdated = semesterrechnungService.calculateAndUpdateAnzahlWochen(semester101);
    assertEquals(1, semesterrechnungUpdated);
    assertEquals(18, semesterrechnung.getAnzahlWochenVorrechnung());
    assertEquals(18, semesterrechnung.getAnzahlWochenNachrechnung());

    // Kein Update, da Rechnungsdatum Vor- und Nachrechnung der Semesterrechnung bereits vorhanden
    Optional<Semester> semesterOptional102 = semesterRepository.findById(102);
    assertTrue(semesterOptional102.isPresent());
    Semester semester102 = semesterOptional102.get();
    semesterrechnung =
        findSemesterrechnung(semesterrechnungenAll, 503, semester102.getSemesterId());
    semesterrechnungUpdated = semesterrechnungService.calculateAndUpdateAnzahlWochen(semester102);
    assertEquals(0, semesterrechnungUpdated);
    assertEquals(21, semesterrechnung.getAnzahlWochenVorrechnung());
    assertEquals(22, semesterrechnung.getAnzahlWochenNachrechnung());

    // Zwei Schüler Kursanmeldung abgemeldet innerhalb Semester
    Optional<Semester> semesterOptional103 = semesterRepository.findById(103);
    assertTrue(semesterOptional103.isPresent());
    Semester semester103 = semesterOptional103.get();
    // Anzahl Wochen Schüler 506: 17, Anzahl Wochen Schüler 507: 15, max. Anzahl Wochen: 17
    semesterrechnung =
        findSemesterrechnung(semesterrechnungenAll, 505, semester103.getSemesterId());
    semesterrechnungUpdated = semesterrechnungService.calculateAndUpdateAnzahlWochen(semester103);
    assertEquals(1, semesterrechnungUpdated);
    assertEquals(17, semesterrechnung.getAnzahlWochenVorrechnung());
    assertEquals(17, semesterrechnung.getAnzahlWochenNachrechnung());

    // Semesterrechnung ohne Kursanmeldung, kein Update
    Optional<Semester> semesterOptional104 = semesterRepository.findById(104);
    assertTrue(semesterOptional104.isPresent());
    Semester semester104 = semesterOptional104.get();
    // Anzahl Wochen Schüler
    semesterrechnung =
        findSemesterrechnung(semesterrechnungenAll, 505, semester104.getSemesterId());
    semesterrechnungUpdated = semesterrechnungService.calculateAndUpdateAnzahlWochen(semester104);
    assertEquals(0, semesterrechnungUpdated);
    // Rechnungsdatum Vorrechnung ist vorhanden: kein Update
    assertEquals(1, semesterrechnung.getAnzahlWochenVorrechnung());
    // Nachrechnung: Default Anzahl Wochen Semester (22): kein Update
    assertEquals(22, semesterrechnung.getAnzahlWochenNachrechnung());
  }

  @Test
  void testCalculateAndUpdateAnzahlWochenAndWochenbetrag() {
    Semester semester101 = findSemester(101);
    Semester semester102 = findSemester(102);
    Semester semester103 = findSemester(103);
    int semesterrechnungUpdated;
    List<Semesterrechnung> semesterrechnungenAll = semesterrechnungRepository.findAll();

    Semesterrechnung semesterrechnung503 =
        findSemesterrechnung(semesterrechnungenAll, 503, semester102.getSemesterId());
    // Rechnungsempfänger 503 hat keine Semesterrechnung im Semester 101
    semesterrechnungUpdated =
        semesterrechnungService.calculateAndUpdateAnzahlWochenAndWochenbetrag(
            semester101, Optional.empty(), semesterrechnung503.getRechnungsempfaenger());
    assertEquals(0, semesterrechnungUpdated);

    Semesterrechnung semesterrechnung;
    semesterrechnung =
        findSemesterrechnung(semesterrechnungenAll, 501, semester101.getSemesterId());
    semesterrechnungUpdated =
        semesterrechnungService.calculateAndUpdateAnzahlWochenAndWochenbetrag(
            semester101, Optional.of(semester102), semesterrechnung.getRechnungsempfaenger());
    assertEquals(1, semesterrechnungUpdated);

    // Nochmals die gleiche Berechnung: kein Update
    semesterrechnungUpdated =
        semesterrechnungService.calculateAndUpdateAnzahlWochenAndWochenbetrag(
            semester101, Optional.of(semester102), semesterrechnung.getRechnungsempfaenger());
    assertEquals(0, semesterrechnungUpdated);

    semesterrechnung =
        findSemesterrechnung(semesterrechnungenAll, 505, semester103.getSemesterId());
    semesterrechnungUpdated =
        semesterrechnungService.calculateAndUpdateAnzahlWochenAndWochenbetrag(
            semester102, Optional.of(semester103), semesterrechnung.getRechnungsempfaenger());
    assertEquals(1, semesterrechnungUpdated);

    // Nochmals die gleiche Berechnung: kein Update
    semesterrechnungUpdated =
        semesterrechnungService.calculateAndUpdateAnzahlWochenAndWochenbetrag(
            semester102, Optional.of(semester103), semesterrechnung.getRechnungsempfaenger());
    assertEquals(0, semesterrechnungUpdated);
  }

  @Test
  void testGetLektionsgebuehrenByLektionslaengeMap() {
    Map<Integer, BigDecimal[]> lektionsgebuehrenByLektionslaengeMap =
        semesterrechnungService.getLektionsgebuehrenByLektionslaengeMap();
    assertEquals(2, lektionsgebuehrenByLektionslaengeMap.size());
    BigDecimal[] fiftyMin = lektionsgebuehrenByLektionslaengeMap.get(50);
    assertEquals(0, new BigDecimal(50).compareTo(fiftyMin[0]));
    assertEquals(0, new BigDecimal(40).compareTo(fiftyMin[1]));
    assertEquals(0, new BigDecimal(30).compareTo(fiftyMin[2]));
    assertEquals(0, new BigDecimal(20).compareTo(fiftyMin[3]));
    assertEquals(0, new BigDecimal(10).compareTo(fiftyMin[4]));
    assertEquals(0, new BigDecimal(1).compareTo(fiftyMin[5]));
  }

  @Test
  void testSaveOrDeleteSemesterrechnung() {
    Semester semester101 = findSemester(101);
    Semester semester104 = findSemester(104);
    List<Semesterrechnung> semesterrechnungenAll = semesterrechnungRepository.findAll();
    Semesterrechnung semesterrechnung;
    semesterrechnung =
        findSemesterrechnung(semesterrechnungenAll, 501, semester101.getSemesterId());
    semesterrechnungService.saveOrDeleteSemesterrechnung(semesterrechnung);
    List<Semesterrechnung> semesterrechnungenAllAfter;
    semesterrechnungenAllAfter = semesterrechnungRepository.findAll();
    assertEquals(semesterrechnungenAllAfter.size(), semesterrechnungenAll.size());
    assertNotNull(
        findSemesterrechnung(semesterrechnungenAllAfter, 501, semester101.getSemesterId()));

    semesterrechnung =
        findSemesterrechnung(semesterrechnungenAll, 505, semester104.getSemesterId());
    semesterrechnungService.saveOrDeleteSemesterrechnung(semesterrechnung);
    semesterrechnungenAllAfter = semesterrechnungRepository.findAll();
    assertEquals(semesterrechnungenAllAfter.size(), semesterrechnungenAll.size() - 1);
    assertNull(findSemesterrechnung(semesterrechnungenAllAfter, 505, semester104.getSemesterId()));
  }

  @Test
  void testDeleteSemesterrechnungenBySemesterId() {
    Semester semester104 = findSemester(104);
    List<Semesterrechnung> semesterrechnungenAll = semesterrechnungRepository.findAll();
    semesterrechnungService.deleteSemesterrechnungenBySemesterId(semester104.getSemesterId());
    List<Semesterrechnung> semesterrechnungenAllAfter = semesterrechnungRepository.findAll();
    assertEquals(semesterrechnungenAllAfter.size(), semesterrechnungenAll.size() - 1);
  }

  private Semester findSemester(int semesterId) {
    Optional<Semester> semesterOptional = semesterRepository.findById(semesterId);
    assertTrue(semesterOptional.isPresent());
    return semesterOptional.get();
  }

  private static Semesterrechnung findSemesterrechnung(
      List<Semesterrechnung> semesterrechnungenAll, int personId, int semesterId) {
    Optional<Semesterrechnung> semesterrechnungOptional;
    semesterrechnungOptional =
        semesterrechnungenAll.stream()
            .filter(
                sr ->
                    sr.getSemester().getSemesterId() == semesterId
                        && sr.getRechnungsempfaenger().getPersonId() == personId)
            .findFirst();
    return semesterrechnungOptional.orElse(null);
  }
}
