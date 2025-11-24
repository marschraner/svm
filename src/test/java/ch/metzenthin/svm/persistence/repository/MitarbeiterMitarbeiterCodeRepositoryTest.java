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
@Sql(scripts = "classpath:MitarbeiterMitarbeiterCodeRepositoryTest_Create.sql")
@Sql(
    scripts = "classpath:MitarbeiterMitarbeiterCodeRepositoryTest_Delete.sql",
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class MitarbeiterMitarbeiterCodeRepositoryTest {

  @Autowired private MitarbeiterMitarbeiterCodeRepository mitarbeiterMitarbeiterCodeRepository;

  @Test
  void testCountByElternmithilfeCodeId() {
    int numberOfMitarbeiterMitarbeiterCodes =
        mitarbeiterMitarbeiterCodeRepository.countByMitarbeiterMitarbeiterCodeCodeId(30);
    assertEquals(1, numberOfMitarbeiterMitarbeiterCodes);

    numberOfMitarbeiterMitarbeiterCodes =
        mitarbeiterMitarbeiterCodeRepository.countByMitarbeiterMitarbeiterCodeCodeId(9999);
    assertEquals(0, numberOfMitarbeiterMitarbeiterCodes);
  }
}
