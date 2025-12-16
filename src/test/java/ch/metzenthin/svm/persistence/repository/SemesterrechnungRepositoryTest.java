package ch.metzenthin.svm.persistence.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ch.metzenthin.svm.persistence.entities.Semesterrechnung;
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
@Sql(scripts = "classpath:SemesterrechnungRepositoryTest_Create.sql")
@Sql(
    scripts = "classpath:SemesterrechnungRepositoryTest_Delete.sql",
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class SemesterrechnungRepositoryTest {

  @Autowired private SemesterrechnungRepository semesterrechnungRepository;

  @Test
  void testCountBySemesterId() {
    int numberOfSemesterrechnungen = semesterrechnungRepository.countBySemesterId(10);
    assertEquals(1, numberOfSemesterrechnungen);

    numberOfSemesterrechnungen = semesterrechnungRepository.countBySemesterId(9999);
    assertEquals(0, numberOfSemesterrechnungen);
  }

  @Test
  void testCountBySemesterrechnungCodeId() {
    int numberOfSemesterrechnungen = semesterrechnungRepository.countBySemesterrechnungCodeId(30);
    assertEquals(1, numberOfSemesterrechnungen);

    numberOfSemesterrechnungen = semesterrechnungRepository.countBySemesterrechnungCodeId(9999);
    assertEquals(0, numberOfSemesterrechnungen);
  }

  @Test
  void testFindSemesterrechnungenBySemesterId() {
    List<Semesterrechnung> semesterrechnungen;
    semesterrechnungen = semesterrechnungRepository.findSemesterrechnungenBySemesterId(10);
    assertEquals(1, semesterrechnungen.size());
    assertEquals(10, semesterrechnungen.get(0).getSemester().getSemesterId());

    semesterrechnungen = semesterrechnungRepository.findSemesterrechnungenBySemesterId(9999);
    assertTrue(semesterrechnungen.isEmpty());
  }

  @Test
  void testDeleteBySemesterId() {
    assertTrue(semesterrechnungRepository.countBySemesterId(10) > 0);
    semesterrechnungRepository.deleteBySemesterId(10);
    assertEquals(0, semesterrechnungRepository.countBySemesterId(10));
  }
}
