package ch.metzenthin.svm.persistence.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;
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
@Sql(scripts = "classpath:MitarbeiterCodeRepositoryTest_Create.sql")
@Sql(
    scripts = "classpath:MitarbeiterCodeRepositoryTest_Delete.sql",
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class MitarbeiterCodeRepositoryTest {

  @Autowired private MitarbeiterCodeRepository mitarbeiterCodeRepository;

  @Test
  void testCountByKuerzel() {
    int numberOfMitarbeiterCodes = mitarbeiterCodeRepository.countByKuerzel("MB");
    assertEquals(1, numberOfMitarbeiterCodes);

    numberOfMitarbeiterCodes = mitarbeiterCodeRepository.countByKuerzel("MX");
    assertEquals(0, numberOfMitarbeiterCodes);
  }

  @Test
  void testCountByKuerzelAndIdNe() {
    int numberOfMitarbeiterCodes = mitarbeiterCodeRepository.countByKuerzelAndIdNe("MB", 1);
    assertEquals(1, numberOfMitarbeiterCodes);

    numberOfMitarbeiterCodes = mitarbeiterCodeRepository.countByKuerzelAndIdNe("MB", 2);
    assertEquals(0, numberOfMitarbeiterCodes);
  }

  @Test
  void testFindAllOrderByKuerzel() {
    List<MitarbeiterCode> mitarbeiterCodesFound = mitarbeiterCodeRepository.findAllOrderByKuerzel();

    assertEquals(2, mitarbeiterCodesFound.size());
    assertEquals("MA", mitarbeiterCodesFound.get(0).getKuerzel());
    assertEquals("MB", mitarbeiterCodesFound.get(1).getKuerzel());
  }
}
