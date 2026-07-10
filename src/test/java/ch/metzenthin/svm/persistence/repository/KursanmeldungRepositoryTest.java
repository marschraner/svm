package ch.metzenthin.svm.persistence.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ch.metzenthin.svm.domain.model.IdAndCount;
import ch.metzenthin.svm.persistence.entities.Kursanmeldung;
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
@Sql(scripts = "classpath:KursanmeldungRepositoryTest_Create.sql")
@Sql(
    scripts = "classpath:KursanmeldungRepositoryTest_Delete.sql",
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class KursanmeldungRepositoryTest {

  @Autowired private KursanmeldungRepository kursanmeldungRepository;

  @Test
  void testCountByKursId() {
    assertEquals(1, kursanmeldungRepository.countByKursId(401));
    assertEquals(0, kursanmeldungRepository.countByKursId(402));
    assertEquals(2, kursanmeldungRepository.countByKursId(403));
    assertEquals(0, kursanmeldungRepository.countByKursId(999));
  }

  @Test
  void testCountKursanmeldungenBySemesterIdGroupByKursId() {
    List<IdAndCount> idAndCountList101 =
        kursanmeldungRepository.countKursanmeldungenBySemesterIdGroupByKursId(101);
    assertEquals(1, idAndCountList101.size());
    assertEquals(1, idAndCountList101.get(0).count());

    List<IdAndCount> idAndCountList102 =
        kursanmeldungRepository.countKursanmeldungenBySemesterIdGroupByKursId(102);
    assertEquals(1, idAndCountList102.size());
    assertEquals(2, idAndCountList102.get(0).count());

    assertTrue(
        kursanmeldungRepository.countKursanmeldungenBySemesterIdGroupByKursId(999).isEmpty());
  }

  @Test
  void testFindBySchuelerIdAndSemesterId() {
    List<Kursanmeldung> kursanmeldungenBySchuelerIdAndSemesterId =
        kursanmeldungRepository.findBySchuelerIdAndSemesterId(502, 101);
    assertEquals(1, kursanmeldungenBySchuelerIdAndSemesterId.size());
    assertEquals(502, kursanmeldungenBySchuelerIdAndSemesterId.get(0).getSchueler().getPersonId());
    assertEquals(
        101,
        kursanmeldungenBySchuelerIdAndSemesterId.get(0).getKurs().getSemester().getSemesterId());

    kursanmeldungenBySchuelerIdAndSemesterId =
        kursanmeldungRepository.findBySchuelerIdAndSemesterId(503, 101);
    assertTrue(kursanmeldungenBySchuelerIdAndSemesterId.isEmpty());
  }
}
