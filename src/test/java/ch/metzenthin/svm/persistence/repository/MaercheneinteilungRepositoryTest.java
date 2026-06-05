package ch.metzenthin.svm.persistence.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.metzenthin.svm.domain.model.IdAndCount;
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
@Sql(scripts = "classpath:MaercheneinteilungRepositoryTest_Create.sql")
@Sql(
    scripts = "classpath:MaercheneinteilungRepositoryTest_Delete.sql",
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class MaercheneinteilungRepositoryTest {

  @Autowired private MaercheneinteilungRepository maercheneinteilungRepository;

  @Test
  void testCountByMaerchenId() {
    int numberOfMaercheneinteilungen = maercheneinteilungRepository.countByMaerchenId(10);
    assertEquals(1, numberOfMaercheneinteilungen);

    numberOfMaercheneinteilungen = maercheneinteilungRepository.countByMaerchenId(9999);
    assertEquals(0, numberOfMaercheneinteilungen);
  }

  @Test
  void testCountByElternmithilfeCodeId() {
    int numberOfMaercheneinteilungen = maercheneinteilungRepository.countByElternmithilfeCodeId(30);
    assertEquals(1, numberOfMaercheneinteilungen);

    numberOfMaercheneinteilungen = maercheneinteilungRepository.countByElternmithilfeCodeId(9999);
    assertEquals(0, numberOfMaercheneinteilungen);
  }

  @Test
  void testCountMaercheneinteilungenGroupByMaerchenId() {
    // Achtung: Märchen ohne Märcheneinteilungen werden mit dieser Query nicht gefunden
    List<IdAndCount> idAndCounts =
        maercheneinteilungRepository.countMaercheneinteilungenGroupByMaerchenId();
    assertEquals(2, idAndCounts.size());
    assertEquals(10, idAndCounts.get(0).id());
    assertEquals(1, idAndCounts.get(0).count());
    assertEquals(20, idAndCounts.get(1).id());
    assertEquals(1, idAndCounts.get(1).count());
  }
}
