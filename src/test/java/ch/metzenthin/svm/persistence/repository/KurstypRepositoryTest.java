package ch.metzenthin.svm.persistence.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.metzenthin.svm.persistence.entities.Kurstyp;
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
@Sql(scripts = "classpath:KurstypRepositoryTest_Create.sql")
@Sql(
    scripts = "classpath:KurstypRepositoryTest_Delete.sql",
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class KurstypRepositoryTest {

  @Autowired private KurstypRepository kurstypRepository;

  @Test
  void testCountByBezeichnung() {
    int numberOfKurstypen = kurstypRepository.countByBezeichnung("Tanzen Test1");
    assertEquals(1, numberOfKurstypen);

    numberOfKurstypen = kurstypRepository.countByBezeichnung("Tanzen Test99");
    assertEquals(0, numberOfKurstypen);
  }

  @Test
  void testCountByBezeichnungAndIdNe() {
    int numberOfKurstypen = kurstypRepository.countByBezeichnungAndIdNe("Tanzen Test1", 11);
    assertEquals(1, numberOfKurstypen);

    numberOfKurstypen = kurstypRepository.countByBezeichnungAndIdNe("Tanzen Test1", 12);
    assertEquals(0, numberOfKurstypen);
  }

  @Test
  void testFindAllOrderByBezeichnung() {
    List<Kurstyp> kurstypenFound = kurstypRepository.findAllOrderByBezeichnung();

    assertEquals(2, kurstypenFound.size());
    assertEquals("Tanzen Test1", kurstypenFound.get(0).getBezeichnung());
    assertEquals("Tanzen Test2", kurstypenFound.get(1).getBezeichnung());
  }
}
