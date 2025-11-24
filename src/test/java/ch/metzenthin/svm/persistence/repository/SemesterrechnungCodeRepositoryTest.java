package ch.metzenthin.svm.persistence.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.metzenthin.svm.persistence.entities.SemesterrechnungCode;
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
@Sql(scripts = "classpath:SemesterrechnungCodeRepositoryTest_Create.sql")
@Sql(
    scripts = "classpath:SemesterrechnungCodeRepositoryTest_Delete.sql",
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class SemesterrechnungCodeRepositoryTest {

  @Autowired private SemesterrechnungCodeRepository semesterrechnungCodeRepository;

  @Test
  void testCountByKuerzel() {
    int numberOfSemesterrechnungCodes = semesterrechnungCodeRepository.countByKuerzel("1b");
    assertEquals(1, numberOfSemesterrechnungCodes);

    numberOfSemesterrechnungCodes = semesterrechnungCodeRepository.countByKuerzel("1x");
    assertEquals(0, numberOfSemesterrechnungCodes);
  }

  @Test
  void testCountByKuerzelAndIdNe() {
    int numberOfSemesterrechnungCodes =
        semesterrechnungCodeRepository.countByKuerzelAndIdNe("1b", 1);
    assertEquals(1, numberOfSemesterrechnungCodes);

    numberOfSemesterrechnungCodes = semesterrechnungCodeRepository.countByKuerzelAndIdNe("1b", 2);
    assertEquals(0, numberOfSemesterrechnungCodes);
  }

  @Test
  void testFindAllOrderByKuerzel() {
    List<SemesterrechnungCode> semesterrechnungCodesFound =
        semesterrechnungCodeRepository.findAllOrderByKuerzel();

    assertEquals(2, semesterrechnungCodesFound.size());
    assertEquals("1a", semesterrechnungCodesFound.get(0).getKuerzel());
    assertEquals("1b", semesterrechnungCodesFound.get(1).getKuerzel());
  }
}
