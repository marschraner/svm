package ch.metzenthin.svm.persistence.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
@Sql(scripts = "classpath:KursRepositoryTest_Create.sql")
@Sql(
    scripts = "classpath:KursRepositoryTest_Delete.sql",
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class KursRepositoryTest {

  @Autowired private KursRepository kursRepository;

  @Test
  void testCountByKursortId() {
    int numberOfKurse = kursRepository.countByKursortId(51);
    assertEquals(1, numberOfKurse);

    numberOfKurse = kursRepository.countByKursortId(9999);
    assertEquals(0, numberOfKurse);
  }

  @Test
  void testCountByKurstypId() {
    int numberOfKurse = kursRepository.countByKurstypId(11);
    assertEquals(1, numberOfKurse);

    numberOfKurse = kursRepository.countByKurstypId(9999);
    assertEquals(0, numberOfKurse);
  }
}
