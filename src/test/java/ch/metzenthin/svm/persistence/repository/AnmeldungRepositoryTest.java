package ch.metzenthin.svm.persistence.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ch.metzenthin.svm.persistence.entities.Anmeldung;
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
@Sql(scripts = "classpath:AnmeldungRepositoryTest_Create.sql")
@Sql(
    scripts = "classpath:AnmeldungRepositoryTest_Delete.sql",
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class AnmeldungRepositoryTest {

  @Autowired private AnmeldungRepository anmeldungRepository;

  @Test
  void testFindBySchuelerIdOrderByAnmeldedatumDesc() {
    List<Anmeldung> anmeldungen = anmeldungRepository.findBySchuelerIdOrderByAnmeldedatumDesc(102);
    assertEquals(2, anmeldungen.size());
    assertEquals(202, anmeldungen.get(0).getAnmeldungId());
    assertEquals(201, anmeldungen.get(1).getAnmeldungId());

    anmeldungen = anmeldungRepository.findBySchuelerIdOrderByAnmeldedatumDesc(9999);
    assertTrue(anmeldungen.isEmpty());
  }
}
