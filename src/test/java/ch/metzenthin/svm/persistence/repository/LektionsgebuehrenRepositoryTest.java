package ch.metzenthin.svm.persistence.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.metzenthin.svm.persistence.entities.Lektionsgebuehren;
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
@Sql(scripts = "classpath:LektionsgebuehrenRepositoryTest_Create.sql")
@Sql(
    scripts = "classpath:LektionsgebuehrenRepositoryTest_Delete.sql",
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class LektionsgebuehrenRepositoryTest {

  @Autowired private LektionsgebuehrenRepository lektionsgebuehrenRepository;

  @Test
  void testCountByLektionslaenge() {
    int numberOfLektionsgebuehrene = lektionsgebuehrenRepository.countByLektionslaenge(50);
    assertEquals(1, numberOfLektionsgebuehrene);

    numberOfLektionsgebuehrene = lektionsgebuehrenRepository.countByLektionslaenge(999);
    assertEquals(0, numberOfLektionsgebuehrene);
  }

  @Test
  void testCountByLektionslaengeAndIdNe() {
    int numberOfLektionsgebuehrene =
        lektionsgebuehrenRepository.countByLektionslaengeAndIdNe(50, 2);
    assertEquals(1, numberOfLektionsgebuehrene);

    numberOfLektionsgebuehrene = lektionsgebuehrenRepository.countByLektionslaengeAndIdNe(50, 1);
    assertEquals(0, numberOfLektionsgebuehrene);
  }

  @Test
  void testFindAllOrderByLektionslaenge() {
    List<Lektionsgebuehren> lektionsgebuehrensFound =
        lektionsgebuehrenRepository.findAllOrderByLektionslaenge();

    assertEquals(2, lektionsgebuehrensFound.size());
    assertEquals(50, lektionsgebuehrensFound.get(0).getLektionslaenge());
    assertEquals(60, lektionsgebuehrensFound.get(1).getLektionslaenge());
  }
}
