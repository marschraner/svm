package ch.metzenthin.svm.persistence.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ch.metzenthin.svm.domain.model.IdAndCount;
import ch.metzenthin.svm.persistence.entities.Kurs;
import ch.metzenthin.svm.persistence.entities.Semester;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

/**
 * @author Martin Schraner
 */
@DataJpaTest
@ContextConfiguration(classes = RepositoryTestConfiguration.class)
@Sql(scripts = "classpath:KursRepositoryTest_Create.sql")
@Sql(
    scripts = "classpath:KursRepositoryTest_Delete.sql",
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class KursRepositoryTest {

  @Autowired private KursRepository kursRepository;
  @Autowired private KursanmeldungRepository kursanmeldungRepository;

  @Test
  void testCountByKursortId() {
    int numberOfKurse = kursRepository.countByKursortId(301);
    assertEquals(1, numberOfKurse);

    numberOfKurse = kursRepository.countByKursortId(9999);
    assertEquals(0, numberOfKurse);
  }

  @Test
  void testCountByKurstypId() {
    int numberOfKurse = kursRepository.countByKurstypId(201);
    assertEquals(1, numberOfKurse);

    numberOfKurse = kursRepository.countByKurstypId(9999);
    assertEquals(0, numberOfKurse);
  }

  @Test
  void testCountBySemesterId() {
    int numberOfKurse = kursRepository.countBySemesterId(102);
    assertEquals(2, numberOfKurse);

    numberOfKurse = kursRepository.countBySemesterId(9999);
    assertEquals(0, numberOfKurse);
  }

  @Test
  void testCountKurseGroupBySemesterId() {
    List<IdAndCount> idAndCounts = kursRepository.countKurseGroupBySemesterId();
    assertEquals(2, idAndCounts.size());
    assertEquals(101, idAndCounts.get(0).id());
    assertEquals(1, idAndCounts.get(0).count());
    assertEquals(102, idAndCounts.get(1).id());
    assertEquals(2, idAndCounts.get(1).count());
  }

  @Test
  void testFindAllBySemesterId() {
    List<Kurs> kursList = kursRepository.findAllBySemesterId(102);
    assertEquals(2, kursList.size());
    List<Integer> kursIds = kursList.stream().map(Kurs::getKursId).toList();
    assertTrue(kursIds.contains(402));
    assertTrue(kursIds.contains(403));
  }

  @Test
  void testFindSemesterByKursId() {
    Optional<Semester> semesterOptional = kursRepository.findSemesterByKursId(401);
    assertTrue(semesterOptional.isPresent());
    assertEquals(101, semesterOptional.get().getSemesterId());
  }

  @Test
  void testDeleteByKursId() {
    assertTrue(kursRepository.existsById(401));
    kursanmeldungRepository.deleteByKursId(401);
    kursRepository.deleteByKursId(401);
    assertFalse(kursRepository.existsById(401));
  }
}
