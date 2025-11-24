package ch.metzenthin.svm.persistence.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.metzenthin.svm.persistence.entities.ElternmithilfeCode;
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
@Sql(scripts = "classpath:ElternmithilfeCodeRepositoryTest_Create.sql")
@Sql(
    scripts = "classpath:ElternmithilfeCodeRepositoryTest_Delete.sql",
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class ElternmithilfeCodeRepositoryTest {

  @Autowired private ElternmithilfeCodeRepository elternmithilfeCodeRepository;

  @Test
  void testCountByKuerzel() {
    int numberOfElternmithilfeCodes = elternmithilfeCodeRepository.countByKuerzel("bb");
    assertEquals(1, numberOfElternmithilfeCodes);

    numberOfElternmithilfeCodes = elternmithilfeCodeRepository.countByKuerzel("bx");
    assertEquals(0, numberOfElternmithilfeCodes);
  }

  @Test
  void testCountByKuerzelAndIdNe() {
    int numberOfElternmithilfeCodes = elternmithilfeCodeRepository.countByKuerzelAndIdNe("bb", 1);
    assertEquals(1, numberOfElternmithilfeCodes);

    numberOfElternmithilfeCodes = elternmithilfeCodeRepository.countByKuerzelAndIdNe("bb", 2);
    assertEquals(0, numberOfElternmithilfeCodes);
  }

  @Test
  void testFindAllOrderByKuerzel() {
    List<ElternmithilfeCode> elternmithilfeCodesFound =
        elternmithilfeCodeRepository.findAllOrderByKuerzel();

    assertEquals(2, elternmithilfeCodesFound.size());
    assertEquals("ba", elternmithilfeCodesFound.get(0).getKuerzel());
    assertEquals("bb", elternmithilfeCodesFound.get(1).getKuerzel());
  }
}
