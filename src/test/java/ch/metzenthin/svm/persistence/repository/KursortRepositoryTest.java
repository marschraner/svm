package ch.metzenthin.svm.persistence.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.metzenthin.svm.persistence.entities.Kursort;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

/**
 * @author Martin Schraner
 */
@DataJpaTest
@ContextConfiguration(classes = RepositoryTestConfiguration.class)
@Sql(scripts = "classpath:KursortRepositoryTest_Create.sql")
@Sql(
    scripts = "classpath:KursortRepositoryTest_Delete.sql",
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class KursortRepositoryTest {

  @Autowired private KursortRepository kursortRepository;

  @Test
  void testCountByBezeichnung() {
    int numberOfKursorte = kursortRepository.countByBezeichnung("Saal Test1");
    assertEquals(1, numberOfKursorte);

    numberOfKursorte = kursortRepository.countByBezeichnung("Saal Test99");
    assertEquals(0, numberOfKursorte);
  }

  @Test
  void testCountByBezeichnungAndIdNe() {
    int numberOfKursorte = kursortRepository.countByBezeichnungAndIdNe("Saal Test1", 1);
    assertEquals(1, numberOfKursorte);

    numberOfKursorte = kursortRepository.countByBezeichnungAndIdNe("Saal Test1", 2);
    assertEquals(0, numberOfKursorte);
  }

  @Test
  void testFindAllOrderByBezeichnung() {
    List<Kursort> kursortsFound = kursortRepository.findAllOrderByBezeichnung();

    assertEquals(2, kursortsFound.size());
    assertEquals("Saal Test1", kursortsFound.get(0).getBezeichnung());
    assertEquals("Saal Test2", kursortsFound.get(1).getBezeichnung());
  }
}
