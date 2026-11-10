package ch.metzenthin.svm.persistence.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.metzenthin.svm.persistence.entities.Dispensation;
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
@Sql(scripts = "classpath:DispensationRepositoryTest_Create.sql")
@Sql(
    scripts = "classpath:DispensationRepositoryTest_Delete.sql",
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class DispensationRepositoryTest {

  @Autowired private DispensationRepository dispensationRepository;

  @Test
  void testFindBySchuelerIdOrderByDispensationsbeginnDesc() {
    List<Dispensation> dispensationen;
    dispensationen = dispensationRepository.findBySchuelerIdOrderByDispensationsbeginnDesc(102);
    assertEquals(2, dispensationen.size());

    dispensationen = dispensationRepository.findBySchuelerIdOrderByDispensationsbeginnDesc(104);
    assertEquals(1, dispensationen.size());

    dispensationen = dispensationRepository.findBySchuelerIdOrderByDispensationsbeginnDesc(100);
    assertEquals(0, dispensationen.size());
  }
}
