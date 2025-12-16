package ch.metzenthin.svm.persistence.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ch.metzenthin.svm.persistence.entities.Schueler;
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
@Sql(scripts = "classpath:SchuelerRepositoryTest_Create.sql")
@Sql(
    scripts = "classpath:SchuelerRepositoryTest_Delete.sql",
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class SchuelerRepositoryTest {

  @Autowired private SchuelerRepository schuelerRepository;

  @Test
  void testFindSchuelerByRechnungsempfaengerId() {
    List<Schueler> schuelerList = schuelerRepository.findSchuelerByRechnungsempfaengerId(101);
    assertEquals(1, schuelerList.size());
    assertEquals(102, schuelerList.get(0).getPersonId());

    schuelerList = schuelerRepository.findSchuelerByRechnungsempfaengerId(102);
    assertTrue(schuelerList.isEmpty());
  }
}
