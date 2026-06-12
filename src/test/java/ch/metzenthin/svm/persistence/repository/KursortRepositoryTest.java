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
  void testCountByKursortId() {
    int numberOfKursorts = kursortRepository.countByKursortId(2);
    assertEquals(1, numberOfKursorts);

    numberOfKursorts = kursortRepository.countByKursortId(999);
    assertEquals(0, numberOfKursorts);
  }

  @Test
  void testCountByBezeichnung() {
    int numberOfKursorte = kursortRepository.countByBezeichnung("Saal A Test");
    assertEquals(1, numberOfKursorte);

    numberOfKursorte = kursortRepository.countByBezeichnung("Saal Test99");
    assertEquals(0, numberOfKursorte);
  }

  @Test
  void testCountByBezeichnungAndIdNe() {
    int numberOfKursorte = kursortRepository.countByBezeichnungAndIdNe("Saal A Test", 1);
    assertEquals(1, numberOfKursorte);

    numberOfKursorte = kursortRepository.countByBezeichnungAndIdNe("Saal A Test", 2);
    assertEquals(0, numberOfKursorte);
  }

  @Test
  void testFindAllOrderByBezeichnung() {
    List<Kursort> kursorteFound = kursortRepository.findAllOrderByBezeichnung();

    assertEquals(4, kursorteFound.size());
    assertEquals("Saal A Test", kursorteFound.get(0).getBezeichnung());
    assertEquals("Saal B Test", kursorteFound.get(1).getBezeichnung());
    assertEquals("Saal C Test", kursorteFound.get(2).getBezeichnung());
    assertEquals("Studio S Test", kursorteFound.get(3).getBezeichnung());
  }

  @Test
  void testFindBySelektierbarTrueOrderByBezeichnung() {
    List<Kursort> kursorteFound = kursortRepository.findBySelektierbarTrueOrderByBezeichnung();

    assertEquals(3, kursorteFound.size());
    assertEquals("Saal A Test", kursorteFound.get(0).getBezeichnung());
    assertEquals("Saal B Test", kursorteFound.get(1).getBezeichnung());
    assertEquals("Studio S Test", kursorteFound.get(2).getBezeichnung());
  }
}
