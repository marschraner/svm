package ch.metzenthin.svm.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import ch.metzenthin.svm.persistence.entities.Kursanmeldung;
import ch.metzenthin.svm.persistence.entities.Schueler;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.persistence.repository.SchuelerRepository;
import ch.metzenthin.svm.persistence.repository.SemesterRepository;
import ch.metzenthin.svm.service.KursanmeldungService;
import ch.metzenthin.svm.service.ServiceTestConfiguration;
import java.util.List;
import java.util.Map;
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
@Sql(scripts = "classpath:KursanmeldungServiceImplTest_Create.sql")
@Sql(
    scripts = "classpath:KursanmeldungServiceImplTest_Delete.sql",
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SuppressWarnings("OptionalGetWithoutIsPresent")
class KursanmeldungServiceImplTest {

  @Autowired SchuelerRepository schuelerRepository;
  @Autowired SemesterRepository semesterRepository;
  @Autowired KursanmeldungService kursanmeldungService;

  @Test
  void testCalculateMaxAnzahlWochen_ganzesSemester() {
    Schueler schueler = schuelerRepository.findById(502).get();
    Semester semester = semesterRepository.findById(101).get();
    int anzahlSchulwochenSemester = semester.getAnzahlSchulwochen();
    int anzahlWochenKursanmeldungen =
        kursanmeldungService.calculateMaxAnzahlWochen(schueler, semester).maxAnzahlWochen();
    assertEquals(anzahlSchulwochenSemester, anzahlWochenKursanmeldungen);
  }

  @Test
  void testCalculateMaxAnzahlWochen_ohneKursanmeldungen() {
    Schueler schueler = schuelerRepository.findById(504).get();
    Semester semester = semesterRepository.findById(101).get();
    int anzahlWochenKursanmeldungen =
        kursanmeldungService.calculateMaxAnzahlWochen(schueler, semester).maxAnzahlWochen();
    assertEquals(0, anzahlWochenKursanmeldungen);
  }

  @Test
  void testCalculateMaxAnzahlWochen_zweiKursanmeldungen() {
    Schueler schueler = schuelerRepository.findById(506).get();
    Semester semester = semesterRepository.findById(101).get();
    int anzahlWochenKursanmeldungen =
        kursanmeldungService.calculateMaxAnzahlWochen(schueler, semester).maxAnzahlWochen();
    assertEquals(20, anzahlWochenKursanmeldungen);
  }

  @Test
  void testFindKursanmeldungenForSemesterAndRechnungsempfaengerBySchueler_ohneKursanmeldungen() {
    Schueler schueler = schuelerRepository.findById(504).get();
    Semester semester = semesterRepository.findById(101).get();
    Map<Schueler, List<Kursanmeldung>> kursanmeldungenBySchueler =
        kursanmeldungService.findKursanmeldungenForSemesterAndRechnungsempfaengerBySchueler(
            semester, schueler.getRechnungsempfaenger());
    assertTrue(kursanmeldungenBySchueler.isEmpty());
  }

  @Test
  void testFindKursanmeldungenForSemesterAndRechnungsempfaengerBySchueler_zweiKursanmeldungen() {
    Schueler schueler = schuelerRepository.findById(506).get();
    Semester semester = semesterRepository.findById(101).get();
    Map<Schueler, List<Kursanmeldung>> kursanmeldungenBySchueler =
        kursanmeldungService.findKursanmeldungenForSemesterAndRechnungsempfaengerBySchueler(
            semester, schueler.getRechnungsempfaenger());
    assertEquals(1, kursanmeldungenBySchueler.size());
    List<Kursanmeldung> kursanmeldungen = kursanmeldungenBySchueler.get(schueler);
    assertEquals(2, kursanmeldungen.size());
  }
}
