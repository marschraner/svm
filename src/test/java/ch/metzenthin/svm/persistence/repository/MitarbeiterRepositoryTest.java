package ch.metzenthin.svm.persistence.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
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
@Sql(scripts = "classpath:MitarbeiterRepositoryTest_Create.sql")
@Sql(
    scripts = "classpath:MitarbeiterRepositoryTest_Delete.sql",
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class MitarbeiterRepositoryTest {

  @Autowired private MitarbeiterRepository mitarbeiterRepository;

  @Test
  void testFindByLehrkraftTrueAndAktivTrueOrderByNachnameVorname() {
    List<Mitarbeiter> mitarbeiterList =
        mitarbeiterRepository.findByLehrkraftTrueAndAktivTrueOrderByNachnameVorname();
    assertEquals(2, mitarbeiterList.size());
    assertEquals("Kummer", mitarbeiterList.get(0).getNachname());
    assertEquals("Muster", mitarbeiterList.get(1).getNachname());
  }
}
