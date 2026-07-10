package ch.metzenthin.svm.persistence.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.metzenthin.svm.common.datatypes.Wochentag;
import ch.metzenthin.svm.common.utils.DateAndTimeUtils;
import ch.metzenthin.svm.domain.model.KursIdAndLehrkraft;
import ch.metzenthin.svm.persistence.entities.KursLehrkraft;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

/**
 * @author Hans Stamm
 */
@DataJpaTest
@ContextConfiguration(classes = RepositoryTestConfiguration.class)
@Sql(scripts = "classpath:KursLehrkraftRepositoryTest_Create.sql")
@Sql(
    scripts = "classpath:KursLehrkraftRepositoryTest_Delete.sql",
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class KursLehrkraftRepositoryTest {

  @Autowired private KursLehrkraftRepository kursLehrkraftRepository;

  @Test
  void testCountKurseBySemesterIdAndWochentagAndZeitBeginnAndMitarbeiterIdIn() {
    Date date =
        DateAndTimeUtils.getLocalDateTimeAsDate(LocalDateTime.of(2026, Month.NOVEMBER, 5, 14, 0));
    Time time = new Time(date.getTime());
    int numberOfKurseFound =
        kursLehrkraftRepository.countKurseBySemesterIdAndWochentagAndZeitBeginnAndMitarbeiterIdIn(
            102, Wochentag.MITTWOCH, time, List.of(22));
    assertEquals(1, numberOfKurseFound);
  }

  @Test
  void testCountKurseBySemesterIdAndWochentagAndZeitBeginnAndMitarbeiterIdInAndKursIdNe() {
    Date date =
        DateAndTimeUtils.getLocalDateTimeAsDate(LocalDateTime.of(2026, Month.NOVEMBER, 5, 14, 0));
    Time time = new Time(date.getTime());
    int numberOfKurseFound =
        kursLehrkraftRepository
            .countKurseBySemesterIdAndWochentagAndZeitBeginnAndMitarbeiterIdInAndKursIdNe(
                102, Wochentag.MITTWOCH, time, List.of(22), 402);
    assertEquals(0, numberOfKurseFound);

    numberOfKurseFound =
        kursLehrkraftRepository
            .countKurseBySemesterIdAndWochentagAndZeitBeginnAndMitarbeiterIdInAndKursIdNe(
                102, Wochentag.MITTWOCH, time, List.of(22), 999);
    assertEquals(1, numberOfKurseFound);
  }

  @Test
  void testFindByKursIdOrderByLehrkraefteOrder() {
    List<KursLehrkraft> kursLehrkraftList =
        kursLehrkraftRepository.findByKursIdOrderByLehrkraefteOrder(402);
    assertEquals(2, kursLehrkraftList.size());
    assertEquals(21, kursLehrkraftList.get(0).getLehrkraft().getPersonId());
    assertEquals(22, kursLehrkraftList.get(1).getLehrkraft().getPersonId());
  }

  @Test
  void testFindLehrkraefteByKursIdOrderByLehrkraefteOrder() {
    List<Mitarbeiter> lehrkraefte =
        kursLehrkraftRepository.findLehrkraefteByKursIdOrderByLehrkraefteOrder(402);
    assertEquals(2, lehrkraefte.size());
    assertEquals("Kuster", lehrkraefte.get(0).getNachname());
    assertEquals("Meier", lehrkraefte.get(1).getNachname());
  }

  @Test
  void testFindKursIdAndLehrkraefteBySemesterIdOrderByKursIdAndLehrkraefteOrder() {
    List<KursIdAndLehrkraft> kursLehrkraftList =
        kursLehrkraftRepository
            .findKursIdAndLehrkraefteBySemesterIdOrderByKursIdAndLehrkraefteOrder(102);
    assertEquals(2, kursLehrkraftList.size());
    assertEquals(402, kursLehrkraftList.get(0).kursId());
    assertEquals(21, kursLehrkraftList.get(0).lehrkraft().getPersonId());
    assertEquals(402, kursLehrkraftList.get(1).kursId());
    assertEquals(22, kursLehrkraftList.get(1).lehrkraft().getPersonId());
  }
}
