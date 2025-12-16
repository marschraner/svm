package ch.metzenthin.svm.persistence.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
  void testFindKursanmeldungenBySchuelerIdAndSemesterId() {
    List<Kursanmeldung> kursanmeldungenBySchuelerIdAndSemesterId =
        kursanmeldungRepository.findKursanmeldungenBySchuelerIdAndSemesterId(502, 101);
    assertEquals(1, kursanmeldungenBySchuelerIdAndSemesterId.size());
    assertEquals(502, kursanmeldungenBySchuelerIdAndSemesterId.get(0).getSchueler().getPersonId());
    assertEquals(
        101,
        kursanmeldungenBySchuelerIdAndSemesterId.get(0).getKurs().getSemester().getSemesterId());

    kursanmeldungenBySchuelerIdAndSemesterId =
        kursanmeldungRepository.findKursanmeldungenBySchuelerIdAndSemesterId(503, 101);
    assertTrue(kursanmeldungenBySchuelerIdAndSemesterId.isEmpty());
  }
}
