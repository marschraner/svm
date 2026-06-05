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
  void testCountBySchuljahrAndBezeichnung() {
    int numberOfMaercheneinteilungen =
        maerchenRepository.countBySchuljahrAndBezeichnung("2026/2027", "Froschkönig");
    assertEquals(1, numberOfMaercheneinteilungen);

    numberOfMaercheneinteilungen =
        maerchenRepository.countBySchuljahrAndBezeichnung("2025/2026", "Froschkönig");
    assertEquals(0, numberOfMaercheneinteilungen);
  }

  @Test
  void testCountBySchuljahrAndBezeichnungAndIdNe() {
    int numberOfMaercheneinteilungen =
        maerchenRepository.countBySchuljahrAndBezeichnungAndIdNe("2026/2027", "Froschkönig", 10);
    assertEquals(1, numberOfMaercheneinteilungen);

    numberOfMaercheneinteilungen =
        maerchenRepository.countBySchuljahrAndBezeichnungAndIdNe("2025/2026", "Froschkönig", 20);
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
