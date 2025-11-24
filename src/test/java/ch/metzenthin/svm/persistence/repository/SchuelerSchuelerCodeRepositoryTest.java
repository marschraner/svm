package ch.metzenthin.svm.persistence.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
@Sql(scripts = "classpath:SchuelerSchuelerCodeRepositoryTest_Create.sql")
@Sql(
    scripts = "classpath:SchuelerSchuelerCodeRepositoryTest_Delete.sql",
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class SchuelerSchuelerCodeRepositoryTest {

  @Autowired private SchuelerSchuelerCodeRepository schuelerSchuelerCodeRepository;

  @Test
  void testCountBySchueler_SchuelerCodeCodeId() {
    int numberOfSchuelerSchuelerCodes =
        schuelerSchuelerCodeRepository.countBySchuelerSchuelerCodeCodeId(30);
    assertEquals(1, numberOfSchuelerSchuelerCodes);

    numberOfSchuelerSchuelerCodes =
        schuelerSchuelerCodeRepository.countBySchuelerSchuelerCodeCodeId(9999);
    assertEquals(0, numberOfSchuelerSchuelerCodes);
  }
}
