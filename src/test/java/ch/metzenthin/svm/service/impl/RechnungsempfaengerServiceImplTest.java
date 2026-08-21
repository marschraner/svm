package ch.metzenthin.svm.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import ch.metzenthin.svm.common.datatypes.Rechnungstyp;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.persistence.entities.Semesterrechnung;
import ch.metzenthin.svm.persistence.repository.SemesterRepository;
import ch.metzenthin.svm.persistence.repository.SemesterrechnungRepository;
import ch.metzenthin.svm.service.RechnungsempfaengerService;
import ch.metzenthin.svm.service.ServiceTestConfiguration;
import ch.metzenthin.svm.service.result.CalculateMaxAnzahlWochenKursanmeldungenResult;
import ch.metzenthin.svm.service.result.CalculateWochenbetragKurseResult;
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
@Sql(scripts = "classpath:RechnungsempfaengerServiceImplTest_Create.sql")
@Sql(
    scripts = "classpath:RechnungsempfaengerServiceImplTest_Delete.sql",
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class RechnungsempfaengerServiceImplTest {

  @Autowired SemesterRepository semesterRepository;
  @Autowired SemesterrechnungRepository semesterrechnungRepository;
  @Autowired RechnungsempfaengerService rechnungsempfaengerService;

  @Test
  void calculateMaxAnzahlWochen() {
    Optional<Semester> semesterOptional = semesterRepository.findById(101);
    assertTrue(semesterOptional.isPresent());
    Semester semester = semesterOptional.get();
    List<Semesterrechnung> semesterrechnungenAll = semesterrechnungRepository.findAll();
    Semesterrechnung semesterrechnung;

    // Schüler volles Semester
    semesterrechnung = findSemesterrechnung(semesterrechnungenAll, 501);
    CalculateMaxAnzahlWochenKursanmeldungenResult calculateMaxAnzahlWochenKursanmeldungenResult =
        rechnungsempfaengerService.calculateMaxAnzahlWochen(
            semesterrechnung.getRechnungsempfaenger(), semester);
    assertEquals(21, calculateMaxAnzahlWochenKursanmeldungenResult.maxAnzahlWochen());
    assertFalse(
        calculateMaxAnzahlWochenKursanmeldungenResult.kursanmeldungenWithDifferentAnzahlWochen());

    // Schüler abgemeldet
    semesterrechnung = findSemesterrechnung(semesterrechnungenAll, 503);
    calculateMaxAnzahlWochenKursanmeldungenResult =
        rechnungsempfaengerService.calculateMaxAnzahlWochen(
            semesterrechnung.getRechnungsempfaenger(), semester);
    assertEquals(21, calculateMaxAnzahlWochenKursanmeldungenResult.maxAnzahlWochen());
    assertFalse(
        calculateMaxAnzahlWochenKursanmeldungenResult.kursanmeldungenWithDifferentAnzahlWochen());

    // zwei Schüler Kursanmeldung abgemeldet innerhalb Semester
    semesterrechnung = findSemesterrechnung(semesterrechnungenAll, 505);
    calculateMaxAnzahlWochenKursanmeldungenResult =
        rechnungsempfaengerService.calculateMaxAnzahlWochen(
            semesterrechnung.getRechnungsempfaenger(), semester);
    assertEquals(20, calculateMaxAnzahlWochenKursanmeldungenResult.maxAnzahlWochen());
    assertTrue(
        calculateMaxAnzahlWochenKursanmeldungenResult.kursanmeldungenWithDifferentAnzahlWochen());
  }

  @Test
  void calculateWochenbetrag() {
    Optional<Semesterrechnung> semesterrechnungOptional =
        semesterrechnungRepository.findBySemesterIdAndRechnungsempfaengerId(101, 501);
    assertTrue(semesterrechnungOptional.isPresent());
    Semesterrechnung semesterrechnung = semesterrechnungOptional.get();
    Semester semester = semesterrechnung.getSemester();
    Map<Integer, BigDecimal[]> lektionsgebuehrenMap = getLektionsgebuehreMap();

    CalculateWochenbetragKurseResult calculateWochenbetragKurseResult =
        rechnungsempfaengerService.calculateWochenbetrag(
            semesterrechnung, semester, Rechnungstyp.VORRECHNUNG, lektionsgebuehrenMap);

    assertEquals(
        0, new BigDecimal(100).compareTo(calculateWochenbetragKurseResult.wochenbetragKurse()));
    assertTrue(calculateWochenbetragKurseResult.allLektionsgebuehrenForKurslaengenFound());
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
