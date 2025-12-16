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
    int numberOfKurse = kursRepository.countByKursortId(301);
    assertEquals(1, numberOfKurse);

    numberOfKurse = kursRepository.countByKursortId(9999);
    assertEquals(0, numberOfKurse);
  }

  @Test
  void testCountByKurstypId() {
    int numberOfKurse = kursRepository.countByKurstypId(201);
    assertEquals(1, numberOfKurse);

    numberOfKurse = kursRepository.countByKurstypId(9999);
    assertEquals(0, numberOfKurse);
  }

  @Test
  void testCountBySemesterId() {
    int numberOfKurse = kursRepository.countBySemesterId(102);
    assertEquals(2, numberOfKurse);

    numberOfKurse = kursRepository.countBySemesterId(9999);
    assertEquals(0, numberOfKurse);
  }

  @Test
  void testCountKurseGroupBySemesterId() {
    List<IdAndCount> idAndCounts = kursRepository.countKurseGroupBySemesterId(List.of(101));
    assertEquals(1, idAndCounts.size());
    assertEquals(101, idAndCounts.get(0).id());
    assertEquals(1, idAndCounts.get(0).count());

    idAndCounts = kursRepository.countKurseGroupBySemesterId(List.of(102));
    assertEquals(1, idAndCounts.size());
    assertEquals(102, idAndCounts.get(0).id());
    assertEquals(2, idAndCounts.get(0).count());

    idAndCounts = kursRepository.countKurseGroupBySemesterId(List.of(102, 101));
    assertEquals(2, idAndCounts.size());
    assertEquals(101, idAndCounts.get(0).id());
    assertEquals(1, idAndCounts.get(0).count());
    assertEquals(102, idAndCounts.get(1).id());
    assertEquals(2, idAndCounts.get(1).count());
  }
}
