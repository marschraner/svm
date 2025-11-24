package ch.metzenthin.svm.persistence.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.metzenthin.svm.persistence.entities.SchuelerCode;
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
@Sql(scripts = "classpath:SchuelerCodeRepositoryTest_Create.sql")
@Sql(
    scripts = "classpath:SchuelerCodeRepositoryTest_Delete.sql",
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class SchuelerCodeRepositoryTest {

  @Autowired private SchuelerCodeRepository schuelerCodeRepository;

  @Test
  void testCountByKuerzel() {
    int numberOfSchuelerCodes = schuelerCodeRepository.countByKuerzel("vb");
    assertEquals(1, numberOfSchuelerCodes);

    numberOfSchuelerCodes = schuelerCodeRepository.countByKuerzel("vx");
    assertEquals(0, numberOfSchuelerCodes);
  }

  @Test
  void testCountByKuerzelAndIdNe() {
    int numberOfSchuelerCodes = schuelerCodeRepository.countByKuerzelAndIdNe("vb", 1);
    assertEquals(1, numberOfSchuelerCodes);

    numberOfSchuelerCodes = schuelerCodeRepository.countByKuerzelAndIdNe("vb", 2);
    assertEquals(0, numberOfSchuelerCodes);
  }

  @Test
  void testFindAllOrderByKuerzel() {
    List<SchuelerCode> schuelerCodesFound = schuelerCodeRepository.findAllOrderByKuerzel();

    assertEquals(2, schuelerCodesFound.size());
    assertEquals("va", schuelerCodesFound.get(0).getKuerzel());
    assertEquals("vb", schuelerCodesFound.get(1).getKuerzel());
  }
}
