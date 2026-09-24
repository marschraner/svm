package ch.metzenthin.svm.persistence.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
@Sql(scripts = "classpath:MitarbeiterRepositoryTest_Create.sql")
@Sql(
    scripts = "classpath:MitarbeiterRepositoryTest_Delete.sql",
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class MitarbeiterRepositoryTest {

  @Autowired private MitarbeiterRepository mitarbeiterRepository;

  @Test
  void testFindByLehrkraftTrueAndAktivTrueOrderByNachnameVorname() {
    List<Mitarbeiter> mitarbeiterList =
        mitarbeiterRepository.findByLehrkraftTrueAndAktivTrueOrderByNachnameVorname();
    assertEquals(2, mitarbeiterList.size());
    assertEquals("Kummer", mitarbeiterList.get(0).getNachname());
    assertEquals("Muster", mitarbeiterList.get(1).getNachname());
  }

  @Test
  void testCountByNachnameAndVornameAndGeburtsdatum() {
    int numberFound;
    numberFound =
        mitarbeiterRepository.countByNachnameAndVornameAndGeburtsdatum("Kummer", "Lea", null);
    assertEquals(1, numberFound);
    numberFound =
        mitarbeiterRepository.countByNachnameAndVornameAndGeburtsdatum(
            "Kummer", "Lea", createCalendar("2000-01-01"));
    assertEquals(0, numberFound);
    numberFound =
        mitarbeiterRepository.countByNachnameAndVornameAndGeburtsdatum("Meier", "Lea", null);
    assertEquals(0, numberFound);
    numberFound =
        mitarbeiterRepository.countByNachnameAndVornameAndGeburtsdatum(
            "Kuster", "Monika", createCalendar("2000-01-01"));
    assertEquals(1, numberFound);
    numberFound =
        mitarbeiterRepository.countByNachnameAndVornameAndGeburtsdatum(
            "Kuster", "Monika", createCalendar("2000-01-02"));
    assertEquals(0, numberFound);
    numberFound =
        mitarbeiterRepository.countByNachnameAndVornameAndGeburtsdatum("Kuster", "Monika", null);
    assertEquals(0, numberFound);
    numberFound =
        mitarbeiterRepository.countByNachnameAndVornameAndGeburtsdatum(
            "Kuster", "Lea", createCalendar("2000-01-01"));
    assertEquals(0, numberFound);
  }

  @Test
  void testCountByNachnameAndVornameAndGeburtsdatumAndPersonIdNe() {
    int numberFound;
    numberFound =
        mitarbeiterRepository.countByNachnameAndVornameAndGeburtsdatumAndPersonIdNe(
            "Kummer", "Lea", null, 23);
    assertEquals(0, numberFound);
    numberFound =
        mitarbeiterRepository.countByNachnameAndVornameAndGeburtsdatumAndPersonIdNe(
            "Kummer", "Lea", null, 99);
    assertEquals(1, numberFound);
    numberFound =
        mitarbeiterRepository.countByNachnameAndVornameAndGeburtsdatumAndPersonIdNe(
            "Kummer", "Lea", createCalendar("2000-01-01"), 99);
    assertEquals(0, numberFound);
    numberFound =
        mitarbeiterRepository.countByNachnameAndVornameAndGeburtsdatumAndPersonIdNe(
            "Meier", "Lea", null, 99);
    assertEquals(0, numberFound);
    numberFound =
        mitarbeiterRepository.countByNachnameAndVornameAndGeburtsdatumAndPersonIdNe(
            "Kuster", "Monika", createCalendar("2000-01-01"), 21);
    assertEquals(0, numberFound);
    numberFound =
        mitarbeiterRepository.countByNachnameAndVornameAndGeburtsdatumAndPersonIdNe(
            "Kuster", "Monika", createCalendar("2000-01-01"), 99);
    assertEquals(1, numberFound);
    numberFound =
        mitarbeiterRepository.countByNachnameAndVornameAndGeburtsdatumAndPersonIdNe(
            "Kuster", "Monika", createCalendar("2000-01-02"), 99);
    assertEquals(0, numberFound);
    numberFound =
        mitarbeiterRepository.countByNachnameAndVornameAndGeburtsdatumAndPersonIdNe(
            "Kuster", "Monika", null, 99);
    assertEquals(0, numberFound);
    numberFound =
        mitarbeiterRepository.countByNachnameAndVornameAndGeburtsdatumAndPersonIdNe(
            "Kuster", "Lea", createCalendar("2000-01-01"), 99);
    assertEquals(0, numberFound);
  }

  @Test
  void
      testFindByNachnameLikeAndVornameLikeAndLehrkraftAndAktivAndCodeIdOrderByNachnameVornameGeburtsdatumAsc() {
    List<Mitarbeiter> mitarbeiterFound;
    mitarbeiterFound =
        mitarbeiterRepository
            .findByNachnameLikeAndVornameLikeAndLehrkraftAndAktivAndCodeIdOrderByNachnameVornameGeburtsdatumAsc(
                null, null, null, null, null);
    assertEquals(4, mitarbeiterFound.size());
    assertEquals(23, mitarbeiterFound.get(0).getPersonId());
    assertEquals(21, mitarbeiterFound.get(1).getPersonId());
    assertEquals(22, mitarbeiterFound.get(2).getPersonId());
    assertEquals(20, mitarbeiterFound.get(3).getPersonId());

    mitarbeiterFound =
        mitarbeiterRepository
            .findByNachnameLikeAndVornameLikeAndLehrkraftAndAktivAndCodeIdOrderByNachnameVornameGeburtsdatumAsc(
                null, null, Boolean.TRUE, null, null);
    assertEquals(3, mitarbeiterFound.size());

    mitarbeiterFound =
        mitarbeiterRepository
            .findByNachnameLikeAndVornameLikeAndLehrkraftAndAktivAndCodeIdOrderByNachnameVornameGeburtsdatumAsc(
                null, null, Boolean.TRUE, Boolean.TRUE, null);
    assertEquals(2, mitarbeiterFound.size());

    mitarbeiterFound =
        mitarbeiterRepository
            .findByNachnameLikeAndVornameLikeAndLehrkraftAndAktivAndCodeIdOrderByNachnameVornameGeburtsdatumAsc(
                "mus", null, Boolean.TRUE, Boolean.TRUE, null);
    assertEquals(1, mitarbeiterFound.size());

    mitarbeiterFound =
        mitarbeiterRepository
            .findByNachnameLikeAndVornameLikeAndLehrkraftAndAktivAndCodeIdOrderByNachnameVornameGeburtsdatumAsc(
                "mus", "Milka", Boolean.TRUE, Boolean.TRUE, null);
    assertEquals(1, mitarbeiterFound.size());

    mitarbeiterFound =
        mitarbeiterRepository
            .findByNachnameLikeAndVornameLikeAndLehrkraftAndAktivAndCodeIdOrderByNachnameVornameGeburtsdatumAsc(
                "mus", "Milka", Boolean.TRUE, Boolean.TRUE, 101);
    assertEquals(1, mitarbeiterFound.size());

    mitarbeiterFound =
        mitarbeiterRepository
            .findByNachnameLikeAndVornameLikeAndLehrkraftAndAktivAndCodeIdOrderByNachnameVornameGeburtsdatumAsc(
                "Kuster", "Monika", Boolean.TRUE, Boolean.FALSE, 101);
    assertEquals(0, mitarbeiterFound.size());

    mitarbeiterFound =
        mitarbeiterRepository
            .findByNachnameLikeAndVornameLikeAndLehrkraftAndAktivAndCodeIdOrderByNachnameVornameGeburtsdatumAsc(
                null, null, null, null, 101);
    assertEquals(2, mitarbeiterFound.size());
  }

  private static Calendar createCalendar(String dateAsString) {
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
}
