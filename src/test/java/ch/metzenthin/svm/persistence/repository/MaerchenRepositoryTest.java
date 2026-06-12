package ch.metzenthin.svm.persistence.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.metzenthin.svm.persistence.entities.Maerchen;
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
@Sql(scripts = "classpath:MaerchenRepositoryTest_Create.sql")
@Sql(
    scripts = "classpath:MaerchenRepositoryTest_Delete.sql",
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class MaerchenRepositoryTest {

  @Autowired private MaerchenRepository maerchenRepository;

  @Test
  void testCountBySchuljahr() {
    int numberOfMaercheneinteilungen = maerchenRepository.countBySchuljahr("2026/2027");
    assertEquals(1, numberOfMaercheneinteilungen);

    numberOfMaercheneinteilungen = maerchenRepository.countBySchuljahr("2024/2025");
    assertEquals(0, numberOfMaercheneinteilungen);
  }

  @Test
  void testCountBySchuljahrAndIdNe() {
    int numberOfMaercheneinteilungen = maerchenRepository.countBySchuljahrAndIdNe("2026/2027", 10);
    assertEquals(1, numberOfMaercheneinteilungen);

    numberOfMaercheneinteilungen = maerchenRepository.countBySchuljahrAndIdNe("2025/2026", 10);
    assertEquals(0, numberOfMaercheneinteilungen);
  }

  @Test
  void testFindAllBySchuljahrDesc() {
    List<Maerchen> maerchenList = maerchenRepository.findAllOrderBySchuljahrDesc();
    assertEquals(2, maerchenList.size());
    List<String> bezeichnungen = maerchenList.stream().map(Maerchen::getBezeichnung).toList();
    assertEquals("Froschkönig", bezeichnungen.get(0));
    assertEquals("Rumpelstilzchen", bezeichnungen.get(1));
  }
}
