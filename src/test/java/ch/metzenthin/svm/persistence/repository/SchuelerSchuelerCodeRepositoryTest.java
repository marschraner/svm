package ch.metzenthin.svm.persistence.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ch.metzenthin.svm.persistence.entities.SchuelerCode;
import ch.metzenthin.svm.persistence.entities.SchuelerSchuelerCode;
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

  @Test
  void testFindAssignedSchuelerCodeBySchuelerIdAndSchuelerCodeId() {
    Optional<SchuelerCode> schuelerCodeOptional;
    schuelerCodeOptional =
        schuelerSchuelerCodeRepository.findAssignedSchuelerCodeBySchuelerIdAndSchuelerCodeId(1, 2);
    assertTrue(schuelerCodeOptional.isEmpty());

    schuelerCodeOptional =
        schuelerSchuelerCodeRepository.findAssignedSchuelerCodeBySchuelerIdAndSchuelerCodeId(
            21, 30);
    assertTrue(schuelerCodeOptional.isPresent());
    assertEquals(30, schuelerCodeOptional.get().getCodeId());

    schuelerCodeOptional =
        schuelerSchuelerCodeRepository.findAssignedSchuelerCodeBySchuelerIdAndSchuelerCodeId(21, 2);
    assertTrue(schuelerCodeOptional.isEmpty());

    schuelerCodeOptional =
        schuelerSchuelerCodeRepository.findAssignedSchuelerCodeBySchuelerIdAndSchuelerCodeId(1, 30);
    assertTrue(schuelerCodeOptional.isEmpty());
  }

  @Test
  void testFindBySchuelerIdAndSchuelerCodeId() {
    Optional<SchuelerSchuelerCode> schuelerSchuelerCodeOptional;
    schuelerSchuelerCodeOptional =
        schuelerSchuelerCodeRepository.findBySchuelerIdAndSchuelerCodeId(1, 2);
    assertTrue(schuelerSchuelerCodeOptional.isEmpty());

    schuelerSchuelerCodeOptional =
        schuelerSchuelerCodeRepository.findBySchuelerIdAndSchuelerCodeId(21, 30);
    assertTrue(schuelerSchuelerCodeOptional.isPresent());
    assertEquals(21, schuelerSchuelerCodeOptional.get().getSchueler().getPersonId());
    assertEquals(30, schuelerSchuelerCodeOptional.get().getSchuelerCode().getCodeId());

    schuelerSchuelerCodeOptional =
        schuelerSchuelerCodeRepository.findBySchuelerIdAndSchuelerCodeId(21, 2);
    assertTrue(schuelerSchuelerCodeOptional.isEmpty());

    schuelerSchuelerCodeOptional =
        schuelerSchuelerCodeRepository.findBySchuelerIdAndSchuelerCodeId(1, 30);
    assertTrue(schuelerSchuelerCodeOptional.isEmpty());
  }

  @Test
  void testFindAssignedSchuelerCodesBySchuelerId() {
    List<SchuelerCode> schuelerCodes;
    schuelerCodes = schuelerSchuelerCodeRepository.findAssignedSchuelerCodesBySchuelerId(1);
    assertEquals(0, schuelerCodes.size());

    schuelerCodes = schuelerSchuelerCodeRepository.findAssignedSchuelerCodesBySchuelerId(21);
    assertEquals(1, schuelerCodes.size());
    assertEquals(30, schuelerCodes.get(0).getCodeId());
  }

  @Test
  void testFindNotAssignedSelektierbareSchuelerCodesBySchuelerId() {
    List<SchuelerCode> schuelerCodes;
    schuelerCodes =
        schuelerSchuelerCodeRepository.findNotAssignedSelektierbareSchuelerCodesBySchuelerId(1);
    assertEquals(2, schuelerCodes.size());
    List<Integer> expectedIds = List.of(30, 31);
    assertTrue(
        schuelerCodes.stream()
            .filter(schuelerCode -> !expectedIds.contains(schuelerCode.getCodeId()))
            .toList()
            .isEmpty());

    schuelerCodes =
        schuelerSchuelerCodeRepository.findNotAssignedSelektierbareSchuelerCodesBySchuelerId(21);
    assertEquals(1, schuelerCodes.size());
    assertEquals(31, schuelerCodes.get(0).getCodeId());
  }

  @Test
  void testDeleteBySchuelerIdAndSchuelerCodeId() {
    List<SchuelerSchuelerCode> schuelerSchuelerCodes;
    schuelerSchuelerCodes = schuelerSchuelerCodeRepository.findAll();
    assertFalse(schuelerSchuelerCodes.isEmpty());
    int size = schuelerSchuelerCodes.size();

    schuelerSchuelerCodeRepository.deleteBySchuelerIdAndSchuelerCodeId(1, 2);
    schuelerSchuelerCodes = schuelerSchuelerCodeRepository.findAll();
    assertEquals(size, schuelerSchuelerCodes.size());

    schuelerSchuelerCodeRepository.deleteBySchuelerIdAndSchuelerCodeId(21, 30);
    schuelerSchuelerCodes = schuelerSchuelerCodeRepository.findAll();
    assertEquals((size - 1), schuelerSchuelerCodes.size());
  }
}
