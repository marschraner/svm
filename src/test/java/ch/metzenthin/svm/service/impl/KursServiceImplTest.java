package ch.metzenthin.svm.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import ch.metzenthin.svm.persistence.repository.RepositoryTestConfiguration;
import ch.metzenthin.svm.service.KursService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@ContextConfiguration(classes = RepositoryTestConfiguration.class)
@ComponentScan("ch.metzenthin.svm.service")
@Sql(scripts = "classpath:KursRepositoryTest_Create.sql")
@Sql(
    scripts = "classpath:KursRepositoryTest_Delete.sql",
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class KursServiceImplTest {

  @Autowired private KursService kursService;

  @Test
  void existsKursByLektionslaenge() {
    assertFalse(kursService.existsKursByLektionslaenge(1));
    assertTrue(kursService.existsKursByLektionslaenge(60));
  }
}
