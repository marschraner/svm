package ch.metzenthin.svm.persistence.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ch.metzenthin.svm.domain.model.MitarbeiterAndMitarbeiterCode;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import ch.metzenthin.svm.persistence.entities.Person;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
@ContextConfiguration(classes = RepositoryTestConfiguration.class)
@Sql(scripts = "classpath:MitarbeiterRepositoryTest_Create.sql")
@Sql(
    scripts = "classpath:MitarbeiterRepositoryTest_Delete.sql",
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class MitarbeiterRepositoryTest {

  private record MitarbeiterIdAndCodeId(int mitarbeiterId, int codeId) {}

  @Autowired private MitarbeiterRepository mitarbeiterRepository;

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
  void testCountByNachnameAndVornameAndGeburtsdatumAndMitarbeiterIdNe() {
    int numberFound;
    numberFound =
        mitarbeiterRepository.countByNachnameAndVornameAndGeburtsdatumAndMitarbeiterIdNe(
            "Kummer", "Lea", null, 23);
    assertEquals(0, numberFound);
    numberFound =
        mitarbeiterRepository.countByNachnameAndVornameAndGeburtsdatumAndMitarbeiterIdNe(
            "Kummer", "Lea", null, 99);
    assertEquals(1, numberFound);
    numberFound =
        mitarbeiterRepository.countByNachnameAndVornameAndGeburtsdatumAndMitarbeiterIdNe(
            "Kummer", "Lea", createCalendar("2000-01-01"), 99);
    assertEquals(0, numberFound);
    numberFound =
        mitarbeiterRepository.countByNachnameAndVornameAndGeburtsdatumAndMitarbeiterIdNe(
            "Meier", "Lea", null, 99);
    assertEquals(0, numberFound);
    numberFound =
        mitarbeiterRepository.countByNachnameAndVornameAndGeburtsdatumAndMitarbeiterIdNe(
            "Kuster", "Monika", createCalendar("2000-01-01"), 21);
    assertEquals(0, numberFound);
    numberFound =
        mitarbeiterRepository.countByNachnameAndVornameAndGeburtsdatumAndMitarbeiterIdNe(
            "Kuster", "Monika", createCalendar("2000-01-01"), 99);
    assertEquals(1, numberFound);
    numberFound =
        mitarbeiterRepository.countByNachnameAndVornameAndGeburtsdatumAndMitarbeiterIdNe(
            "Kuster", "Monika", createCalendar("2000-01-02"), 99);
    assertEquals(0, numberFound);
    numberFound =
        mitarbeiterRepository.countByNachnameAndVornameAndGeburtsdatumAndMitarbeiterIdNe(
            "Kuster", "Monika", null, 99);
    assertEquals(0, numberFound);
    numberFound =
        mitarbeiterRepository.countByNachnameAndVornameAndGeburtsdatumAndMitarbeiterIdNe(
            "Kuster", "Lea", createCalendar("2000-01-01"), 99);
    assertEquals(0, numberFound);
  }

  @Test
  void testFindByLehrkraftTrueAndAktivTrueOrderByNachnameVorname() {
    List<Mitarbeiter> mitarbeiterList =
        mitarbeiterRepository.findByLehrkraftTrueAndAktivTrueOrderByNachnameVorname();
    assertEquals(2, mitarbeiterList.size());
    assertEquals("Müller", mitarbeiterList.get(0).getNachname());
    assertEquals("Muster", mitarbeiterList.get(1).getNachname());
  }

  @Test
  void testFindMitarbeitersWithoutMitarbeiterCode_allOptionalsEmpty() {
    List<Mitarbeiter> mitarbeiterListFound =
        mitarbeiterRepository.findMitarbeitersWithoutMitarbeiterCode(
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty());

    assertEquals(3, mitarbeiterListFound.size());
    List<Integer> mitarbeiterIds = mitarbeiterListFound.stream().map(Person::getPersonId).toList();
    assertTrue(mitarbeiterIds.contains(21));
    assertTrue(mitarbeiterIds.contains(24));
    assertTrue(mitarbeiterIds.contains(25));
  }

  @Test
  void testFindMitarbeitersWithoutMitarbeiterCode_nachnamePresent() {
    List<Mitarbeiter> mitarbeiterListFound =
        mitarbeiterRepository.findMitarbeitersWithoutMitarbeiterCode(
            Optional.of("Kuster"),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty());

    assertEquals(1, mitarbeiterListFound.size());
    assertEquals(21, mitarbeiterListFound.get(0).getPersonId());

    mitarbeiterListFound =
        mitarbeiterRepository.findMitarbeitersWithoutMitarbeiterCode(
            Optional.of("M"),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty());

    assertEquals(2, mitarbeiterListFound.size());
    List<Integer> mitarbeiterIds = mitarbeiterListFound.stream().map(Person::getPersonId).toList();
    assertTrue(mitarbeiterIds.contains(24));
    assertTrue(mitarbeiterIds.contains(25));
  }

  @Test
  void testFindMitarbeitersWithoutMitarbeiterCode_vornamePresent() {
    List<Mitarbeiter> mitarbeiterListFound =
        mitarbeiterRepository.findMitarbeitersWithoutMitarbeiterCode(
            Optional.empty(),
            Optional.of("Linda"),
            Optional.empty(),
            Optional.empty(),
            Optional.empty());

    assertEquals(1, mitarbeiterListFound.size());
    assertEquals(25, mitarbeiterListFound.get(0).getPersonId());
  }

  @Test
  void testFindMitarbeitersWithoutMitarbeiterCode_lehrkraftPresent() {
    List<Mitarbeiter> mitarbeiterListFound =
        mitarbeiterRepository.findMitarbeitersWithoutMitarbeiterCode(
            Optional.empty(),
            Optional.empty(),
            Optional.of(true),
            Optional.empty(),
            Optional.empty());

    assertEquals(2, mitarbeiterListFound.size());
    List<Integer> mitarbeiterIds = mitarbeiterListFound.stream().map(Person::getPersonId).toList();
    assertTrue(mitarbeiterIds.contains(21));
    assertTrue(mitarbeiterIds.contains(24));
  }

  @Test
  void testFindMitarbeitersWithoutMitarbeiterCode_aktivPresent() {
    List<Mitarbeiter> mitarbeiterListFound =
        mitarbeiterRepository.findMitarbeitersWithoutMitarbeiterCode(
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.of(true),
            Optional.empty());

    assertEquals(2, mitarbeiterListFound.size());
    List<Integer> mitarbeiterIds = mitarbeiterListFound.stream().map(Person::getPersonId).toList();
    assertTrue(mitarbeiterIds.contains(24));
    assertTrue(mitarbeiterIds.contains(25));
  }

  @Test
  void testFindMitarbeitersWithoutMitarbeiterCode_mitarbeiterIdsPresent() {
    List<Mitarbeiter> mitarbeiterListFound =
        mitarbeiterRepository.findMitarbeitersWithoutMitarbeiterCode(
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.of(List.of(21, 25)));

    assertEquals(2, mitarbeiterListFound.size());
    List<Integer> mitarbeiterIds = mitarbeiterListFound.stream().map(Person::getPersonId).toList();
    assertTrue(mitarbeiterIds.contains(21));
    assertTrue(mitarbeiterIds.contains(25));
  }

  @Test
  void testFindMitarbeitersWithoutMitarbeiterCode_allOptionalsPresent() {
    List<Mitarbeiter> mitarbeiterListFound =
        mitarbeiterRepository.findMitarbeitersWithoutMitarbeiterCode(
            Optional.of("Müller"),
            Optional.of("Mia"),
            Optional.of(true),
            Optional.of(true),
            Optional.of(List.of(21, 24, 25)));

    assertEquals(1, mitarbeiterListFound.size());
    assertEquals(24, mitarbeiterListFound.get(0).getPersonId());
  }

  @Test
  void testFindMitarbeiterAndMitarbeiterCodesOfMitarbeitersWithMitarbeiterCode_allOptionalsEmpty() {
    List<MitarbeiterAndMitarbeiterCode> mitarbeiterAndMitarbeiterCodeListFound =
        mitarbeiterRepository.findMitarbeiterAndMitarbeiterCodesOfMitarbeitersWithMitarbeiterCode(
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty());

    assertEquals(5, mitarbeiterAndMitarbeiterCodeListFound.size());
    List<MitarbeiterIdAndCodeId> mitarbeiterIdAndCodeIdList =
        mitarbeiterAndMitarbeiterCodeListFound.stream()
            .map(
                mitarbeiterAndMitarbeiterCode ->
                    new MitarbeiterIdAndCodeId(
                        mitarbeiterAndMitarbeiterCode.mitarbeiter().getPersonId(),
                        mitarbeiterAndMitarbeiterCode.mitarbeiterCode().getCodeId()))
            .toList();
    assertTrue(mitarbeiterIdAndCodeIdList.contains(new MitarbeiterIdAndCodeId(20, 100)));
    assertTrue(mitarbeiterIdAndCodeIdList.contains(new MitarbeiterIdAndCodeId(20, 101)));
    assertTrue(mitarbeiterIdAndCodeIdList.contains(new MitarbeiterIdAndCodeId(20, 102)));
    assertTrue(mitarbeiterIdAndCodeIdList.contains(new MitarbeiterIdAndCodeId(22, 101)));
    assertTrue(mitarbeiterIdAndCodeIdList.contains(new MitarbeiterIdAndCodeId(23, 100)));
  }

  @Test
  void testFindMitarbeiterAndMitarbeiterCodesOfMitarbeitersWithMitarbeiterCode_nachnamePresent() {
    List<MitarbeiterAndMitarbeiterCode> mitarbeiterAndMitarbeiterCodeListFound =
        mitarbeiterRepository.findMitarbeiterAndMitarbeiterCodesOfMitarbeitersWithMitarbeiterCode(
            Optional.of("Muster"),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty());

    assertEquals(3, mitarbeiterAndMitarbeiterCodeListFound.size());
    List<MitarbeiterIdAndCodeId> mitarbeiterIdAndCodeIdList =
        mitarbeiterAndMitarbeiterCodeListFound.stream()
            .map(
                mitarbeiterAndMitarbeiterCode ->
                    new MitarbeiterIdAndCodeId(
                        mitarbeiterAndMitarbeiterCode.mitarbeiter().getPersonId(),
                        mitarbeiterAndMitarbeiterCode.mitarbeiterCode().getCodeId()))
            .toList();
    assertTrue(mitarbeiterIdAndCodeIdList.contains(new MitarbeiterIdAndCodeId(20, 100)));
    assertTrue(mitarbeiterIdAndCodeIdList.contains(new MitarbeiterIdAndCodeId(20, 101)));
    assertTrue(mitarbeiterIdAndCodeIdList.contains(new MitarbeiterIdAndCodeId(20, 102)));

    mitarbeiterAndMitarbeiterCodeListFound =
        mitarbeiterRepository.findMitarbeiterAndMitarbeiterCodesOfMitarbeitersWithMitarbeiterCode(
            Optional.of("M"),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty());

    assertEquals(4, mitarbeiterAndMitarbeiterCodeListFound.size());
    mitarbeiterIdAndCodeIdList =
        mitarbeiterAndMitarbeiterCodeListFound.stream()
            .map(
                mitarbeiterAndMitarbeiterCode ->
                    new MitarbeiterIdAndCodeId(
                        mitarbeiterAndMitarbeiterCode.mitarbeiter().getPersonId(),
                        mitarbeiterAndMitarbeiterCode.mitarbeiterCode().getCodeId()))
            .toList();
    assertTrue(mitarbeiterIdAndCodeIdList.contains(new MitarbeiterIdAndCodeId(20, 100)));
    assertTrue(mitarbeiterIdAndCodeIdList.contains(new MitarbeiterIdAndCodeId(20, 101)));
    assertTrue(mitarbeiterIdAndCodeIdList.contains(new MitarbeiterIdAndCodeId(20, 102)));
    assertTrue(mitarbeiterIdAndCodeIdList.contains(new MitarbeiterIdAndCodeId(22, 101)));
  }

  @Test
  void testFindMitarbeiterAndMitarbeiterCodesOfMitarbeitersWithMitarbeiterCode_vornamePresent() {
    List<MitarbeiterAndMitarbeiterCode> mitarbeiterAndMitarbeiterCodeListFound =
        mitarbeiterRepository.findMitarbeiterAndMitarbeiterCodesOfMitarbeitersWithMitarbeiterCode(
            Optional.empty(),
            Optional.of("Milka"),
            Optional.empty(),
            Optional.empty(),
            Optional.empty());

    assertEquals(3, mitarbeiterAndMitarbeiterCodeListFound.size());
    List<MitarbeiterIdAndCodeId> mitarbeiterIdAndCodeIdList =
        mitarbeiterAndMitarbeiterCodeListFound.stream()
            .map(
                mitarbeiterAndMitarbeiterCode ->
                    new MitarbeiterIdAndCodeId(
                        mitarbeiterAndMitarbeiterCode.mitarbeiter().getPersonId(),
                        mitarbeiterAndMitarbeiterCode.mitarbeiterCode().getCodeId()))
            .toList();
    assertTrue(mitarbeiterIdAndCodeIdList.contains(new MitarbeiterIdAndCodeId(20, 100)));
    assertTrue(mitarbeiterIdAndCodeIdList.contains(new MitarbeiterIdAndCodeId(20, 101)));
    assertTrue(mitarbeiterIdAndCodeIdList.contains(new MitarbeiterIdAndCodeId(20, 102)));
  }

  @Test
  void testFindMitarbeiterAndMitarbeiterCodesOfMitarbeitersWithMitarbeiterCode_lehrkraftPresent() {
    List<MitarbeiterAndMitarbeiterCode> mitarbeiterAndMitarbeiterCodeListFound =
        mitarbeiterRepository.findMitarbeiterAndMitarbeiterCodesOfMitarbeitersWithMitarbeiterCode(
            Optional.empty(),
            Optional.empty(),
            Optional.of(true),
            Optional.empty(),
            Optional.empty());

    assertEquals(4, mitarbeiterAndMitarbeiterCodeListFound.size());
    List<MitarbeiterIdAndCodeId> mitarbeiterIdAndCodeIdList =
        mitarbeiterAndMitarbeiterCodeListFound.stream()
            .map(
                mitarbeiterAndMitarbeiterCode ->
                    new MitarbeiterIdAndCodeId(
                        mitarbeiterAndMitarbeiterCode.mitarbeiter().getPersonId(),
                        mitarbeiterAndMitarbeiterCode.mitarbeiterCode().getCodeId()))
            .toList();
    assertTrue(mitarbeiterIdAndCodeIdList.contains(new MitarbeiterIdAndCodeId(20, 100)));
    assertTrue(mitarbeiterIdAndCodeIdList.contains(new MitarbeiterIdAndCodeId(20, 101)));
    assertTrue(mitarbeiterIdAndCodeIdList.contains(new MitarbeiterIdAndCodeId(20, 102)));
    assertTrue(mitarbeiterIdAndCodeIdList.contains(new MitarbeiterIdAndCodeId(23, 100)));
  }

  @Test
  void testFindMitarbeiterAndMitarbeiterCodesOfMitarbeitersWithMitarbeiterCode_aktivPresent() {
    List<MitarbeiterAndMitarbeiterCode> mitarbeiterAndMitarbeiterCodeListFound =
        mitarbeiterRepository.findMitarbeiterAndMitarbeiterCodesOfMitarbeitersWithMitarbeiterCode(
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.of(true),
            Optional.empty());

    assertEquals(4, mitarbeiterAndMitarbeiterCodeListFound.size());
    List<MitarbeiterIdAndCodeId> mitarbeiterIdAndCodeIdList =
        mitarbeiterAndMitarbeiterCodeListFound.stream()
            .map(
                mitarbeiterAndMitarbeiterCode ->
                    new MitarbeiterIdAndCodeId(
                        mitarbeiterAndMitarbeiterCode.mitarbeiter().getPersonId(),
                        mitarbeiterAndMitarbeiterCode.mitarbeiterCode().getCodeId()))
            .toList();
    assertTrue(mitarbeiterIdAndCodeIdList.contains(new MitarbeiterIdAndCodeId(20, 100)));
    assertTrue(mitarbeiterIdAndCodeIdList.contains(new MitarbeiterIdAndCodeId(20, 101)));
    assertTrue(mitarbeiterIdAndCodeIdList.contains(new MitarbeiterIdAndCodeId(20, 102)));
    assertTrue(mitarbeiterIdAndCodeIdList.contains(new MitarbeiterIdAndCodeId(22, 101)));
  }

  @Test
  void
      testFindMitarbeiterAndMitarbeiterCodesOfMitarbeitersWithMitarbeiterCode_mitarbeiterIdsPresent() {
    List<MitarbeiterAndMitarbeiterCode> mitarbeiterAndMitarbeiterCodeListFound =
        mitarbeiterRepository.findMitarbeiterAndMitarbeiterCodesOfMitarbeitersWithMitarbeiterCode(
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.of(List.of(20, 22)));

    assertEquals(4, mitarbeiterAndMitarbeiterCodeListFound.size());
    List<MitarbeiterIdAndCodeId> mitarbeiterIdAndCodeIdList =
        mitarbeiterAndMitarbeiterCodeListFound.stream()
            .map(
                mitarbeiterAndMitarbeiterCode ->
                    new MitarbeiterIdAndCodeId(
                        mitarbeiterAndMitarbeiterCode.mitarbeiter().getPersonId(),
                        mitarbeiterAndMitarbeiterCode.mitarbeiterCode().getCodeId()))
            .toList();
    assertTrue(mitarbeiterIdAndCodeIdList.contains(new MitarbeiterIdAndCodeId(20, 100)));
    assertTrue(mitarbeiterIdAndCodeIdList.contains(new MitarbeiterIdAndCodeId(20, 101)));
    assertTrue(mitarbeiterIdAndCodeIdList.contains(new MitarbeiterIdAndCodeId(20, 102)));
    assertTrue(mitarbeiterIdAndCodeIdList.contains(new MitarbeiterIdAndCodeId(22, 101)));
  }

  @Test
  void
      testFindMitarbeiterAndMitarbeiterCodesOfMitarbeitersWithMitarbeiterCode_allOptionalsPresent() {
    List<MitarbeiterAndMitarbeiterCode> mitarbeiterAndMitarbeiterCodeListFound =
        mitarbeiterRepository.findMitarbeiterAndMitarbeiterCodesOfMitarbeitersWithMitarbeiterCode(
            Optional.of("Muster"),
            Optional.of("Milka"),
            Optional.of(true),
            Optional.of(true),
            Optional.of(List.of(20, 22)));

    assertEquals(3, mitarbeiterAndMitarbeiterCodeListFound.size());
    List<MitarbeiterIdAndCodeId> mitarbeiterIdAndCodeIdList =
        mitarbeiterAndMitarbeiterCodeListFound.stream()
            .map(
                mitarbeiterAndMitarbeiterCode ->
                    new MitarbeiterIdAndCodeId(
                        mitarbeiterAndMitarbeiterCode.mitarbeiter().getPersonId(),
                        mitarbeiterAndMitarbeiterCode.mitarbeiterCode().getCodeId()))
            .toList();
    assertTrue(mitarbeiterIdAndCodeIdList.contains(new MitarbeiterIdAndCodeId(20, 100)));
    assertTrue(mitarbeiterIdAndCodeIdList.contains(new MitarbeiterIdAndCodeId(20, 101)));
    assertTrue(mitarbeiterIdAndCodeIdList.contains(new MitarbeiterIdAndCodeId(20, 102)));
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
