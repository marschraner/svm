package ch.metzenthin.svm.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import ch.metzenthin.svm.domain.model.KursAndLehrkraefteAndNumberOfKursanmeldungen;
import ch.metzenthin.svm.persistence.entities.Kurs;
import ch.metzenthin.svm.persistence.entities.KursLehrkraft;
import ch.metzenthin.svm.persistence.repository.KursLehrkraftRepository;
import ch.metzenthin.svm.persistence.repository.KursRepository;
import ch.metzenthin.svm.persistence.repository.KursanmeldungRepository;
import ch.metzenthin.svm.service.ServiceTestConfiguration;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

/**
 * @author Hans Stamm
 */
@DataJpaTest
@ContextConfiguration(classes = ServiceTestConfiguration.class)
@Sql(scripts = "classpath:KursRepositoryTest_Create.sql")
@Sql(
    scripts = "classpath:KursRepositoryTest_Delete.sql",
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class KursServiceImplTest {

  @Autowired private KursServiceImpl kursService;
  @Autowired private KursRepository kursRepository;
  @Autowired private KursanmeldungRepository kursanmeldungRepository;
  @Autowired private KursLehrkraftRepository kursLehrkraftRepository;
  @PersistenceContext private EntityManager entityManager;

  @Test
  void existsKursByLektionslaenge() {
    assertFalse(kursService.existsKursByLektionslaenge(1));
    assertTrue(kursService.existsKursByLektionslaenge(60));
  }

  @Test
  void findAllKurseAndLehrkraefteAndNumberOfKursanmeldungenForSemester() {
    List<KursAndLehrkraefteAndNumberOfKursanmeldungen>
        kurseAndLehrkraefteAndNumberOfSchuelerForSemester101 =
            kursService.findAllKurseAndLehrkraefteAndNumberOfKursanmeldungenForSemester(101);
    assertEquals(1, kurseAndLehrkraefteAndNumberOfSchuelerForSemester101.size());
    assertEquals(
        1, kurseAndLehrkraefteAndNumberOfSchuelerForSemester101.get(0).numberOfKursanmeldungen());
    assertTrue(kurseAndLehrkraefteAndNumberOfSchuelerForSemester101.get(0).lehrkraefte().isEmpty());

    List<KursAndLehrkraefteAndNumberOfKursanmeldungen>
        kurseAndLehrkraefteAndNumberOfSchuelerForSemester102 =
            kursService.findAllKurseAndLehrkraefteAndNumberOfKursanmeldungenForSemester(102);
    assertEquals(2, kurseAndLehrkraefteAndNumberOfSchuelerForSemester102.size());
    KursAndLehrkraefteAndNumberOfKursanmeldungen kursAndLehrkraefteAndNumberOfKursanmeldungen402 =
        kurseAndLehrkraefteAndNumberOfSchuelerForSemester102.get(0);
    assertEquals(0, kursAndLehrkraefteAndNumberOfKursanmeldungen402.numberOfKursanmeldungen());
    assertEquals(1, kursAndLehrkraefteAndNumberOfKursanmeldungen402.lehrkraefte().size());
    assertEquals(
        507, kursAndLehrkraefteAndNumberOfKursanmeldungen402.lehrkraefte().get(0).getPersonId());
    KursAndLehrkraefteAndNumberOfKursanmeldungen kursAndLehrkraefteAndNumberOfKursanmeldungen403 =
        kurseAndLehrkraefteAndNumberOfSchuelerForSemester102.get(1);
    assertEquals(2, kursAndLehrkraefteAndNumberOfKursanmeldungen403.numberOfKursanmeldungen());
    assertEquals(2, kursAndLehrkraefteAndNumberOfKursanmeldungen403.lehrkraefte().size());
    assertEquals(
        508, kursAndLehrkraefteAndNumberOfKursanmeldungen403.lehrkraefte().get(0).getPersonId());
    assertEquals(
        509, kursAndLehrkraefteAndNumberOfKursanmeldungen403.lehrkraefte().get(1).getPersonId());
  }

  @Test
  void saveKurs() {}

  @Test
  void deleteKurs() {
    Kurs kurs = kursRepository.findById(403).orElseThrow();
    assertTrue(kursanmeldungRepository.countByKursId(kurs.getKursId()) > 0);
    List<KursLehrkraft> lehrkraefteLinksBefore =
        kursLehrkraftRepository.findByKursIdOrderByLehrkraefteOrder(kurs.getKursId());
    assertFalse(lehrkraefteLinksBefore.isEmpty());

    kursService.deleteKurs(kurs.getKursId());

    entityManager.flush();
    entityManager.clear();
    Optional<Kurs> kursOptional = kursRepository.findById(kurs.getKursId());
    assertFalse(kursOptional.isPresent());
    assertFalse(kursanmeldungRepository.countByKursId(kurs.getKursId()) > 0);
    List<KursLehrkraft> lehrkraefteLinksAfter =
        kursLehrkraftRepository.findByKursIdOrderByLehrkraefteOrder(kurs.getKursId());
    assertTrue(lehrkraefteLinksAfter.isEmpty());
  }
}
