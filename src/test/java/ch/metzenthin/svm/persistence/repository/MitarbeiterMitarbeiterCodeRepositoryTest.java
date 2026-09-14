package ch.metzenthin.svm.persistence.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ch.metzenthin.svm.persistence.entities.Code;
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
@Sql(scripts = "classpath:MitarbeiterMitarbeiterCodeRepositoryTest_Create.sql")
@Sql(
    scripts = "classpath:MitarbeiterMitarbeiterCodeRepositoryTest_Delete.sql",
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class MitarbeiterMitarbeiterCodeRepositoryTest {

  @Autowired private MitarbeiterMitarbeiterCodeRepository mitarbeiterMitarbeiterCodeRepository;

  @Test
  void testCountByMitarbeiterCodeId() {
    int numberOfMitarbeiterMitarbeiterCodes =
        mitarbeiterMitarbeiterCodeRepository.countByMitarbeiterCodeId(30);
    assertEquals(1, numberOfMitarbeiterMitarbeiterCodes);

    numberOfMitarbeiterMitarbeiterCodes =
        mitarbeiterMitarbeiterCodeRepository.countByMitarbeiterCodeId(9999);
    assertEquals(0, numberOfMitarbeiterMitarbeiterCodes);
  }

  @Test
  void testFindMitarbeiterCodesByMitarbeiterId() {
    List<MitarbeiterCode> mitarbeiterCodes =
        mitarbeiterMitarbeiterCodeRepository.findMitarbeiterCodesByMitarbeiterId(20);
    assertEquals(3, mitarbeiterCodes.size());
    List<Integer> mitarbeiterCodeIds = mitarbeiterCodes.stream().map(Code::getCodeId).toList();
    assertTrue(mitarbeiterCodeIds.contains(30));
    assertTrue(mitarbeiterCodeIds.contains(31));
    assertTrue(mitarbeiterCodeIds.contains(32));
  }

  @Test
  void testDeleteByMitabeiterIdEqAndMitarbeiterCodeIdIn() {
    List<MitarbeiterCode> mitarbeiterCodes =
        mitarbeiterMitarbeiterCodeRepository.findMitarbeiterCodesByMitarbeiterId(20);
    assertEquals(3, mitarbeiterCodes.size());
    mitarbeiterMitarbeiterCodeRepository.deleteByMitabeiterIdEqAndMitarbeiterCodeIdIn(20, List.of(30, 31));
    mitarbeiterCodes =
        mitarbeiterMitarbeiterCodeRepository.findMitarbeiterCodesByMitarbeiterId(20);
    assertEquals(1, mitarbeiterCodes.size());
    List<Integer> mitarbeiterCodeIds = mitarbeiterCodes.stream().map(Code::getCodeId).toList();
    assertTrue(mitarbeiterCodeIds.contains(32));
  }
}
