package ch.metzenthin.svm.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import ch.metzenthin.svm.common.datatypes.Semesterbezeichnung;
import ch.metzenthin.svm.persistence.entities.Anmeldung;
import ch.metzenthin.svm.persistence.entities.Kursanmeldung;
import ch.metzenthin.svm.persistence.entities.Schueler;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.persistence.repository.AnmeldungRepository;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * @author Hans Stamm
 */
@ExtendWith(MockitoExtension.class)
class KursServiceImplMockTest {

  @InjectMocks private KursServiceImpl kursService;
  @Mock private AnmeldungRepository anmeldungRepository;

  @Test
  void testGetSemesterTitle() {
    assertEquals("", KursServiceImpl.getSemesterTitle(null));

    Semester semester = new Semester();
    semester.setSchuljahr("2024/2025");
    semester.setSemesterbezeichnung(Semesterbezeichnung.ERSTES_SEMESTER);
    assertEquals("Schuljahr 2024/2025, 1. Semester", KursServiceImpl.getSemesterTitle(semester));
  }

  @Test
  void testIsAngemeldetForSemesterAndKurs() {
    Semester semester = new Semester();
    semester.setSchuljahr("2024/2025");
    semester.setSemesterbeginn(createCalendar("2024-08-19"));
    semester.setSemesterende(createCalendar("2025-02-01"));
    Schueler schueler = new Schueler();
    schueler.setPersonId(100);
    Kursanmeldung kursanmeldungAngemeldet = new Kursanmeldung();
    kursanmeldungAngemeldet.setAnmeldedatum(createCalendar("2024-06-29"));
    kursanmeldungAngemeldet.setSchueler(schueler);
    Anmeldung anmeldungAngemeldet = new Anmeldung();
    anmeldungAngemeldet.setAnmeldedatum(createCalendar("2024-01-01"));

    when(anmeldungRepository.findBySchuelerIdOrderByAnmeldedatumDesc(anyInt()))
        .thenReturn(List.of(anmeldungAngemeldet));
    assertTrue(kursService.isAngemeldetForSemesterAndKurs(semester, kursanmeldungAngemeldet));

    Kursanmeldung kursanmeldungAbgemeldet = new Kursanmeldung();
    kursanmeldungAbgemeldet.setAnmeldedatum(createCalendar("2024-06-29"));
    kursanmeldungAbgemeldet.setAbmeldedatum(createCalendar("2024-08-19"));
    kursanmeldungAbgemeldet.setSchueler(schueler);
    when(anmeldungRepository.findBySchuelerIdOrderByAnmeldedatumDesc(anyInt()))
        .thenReturn(List.of(anmeldungAngemeldet));
    assertFalse(kursService.isAngemeldetForSemesterAndKurs(semester, kursanmeldungAbgemeldet));

    Anmeldung anmeldungAbgemeldetNachSemesterBeginn = new Anmeldung();
    anmeldungAbgemeldetNachSemesterBeginn.setAnmeldedatum(createCalendar("2024-01-01"));
    anmeldungAbgemeldetNachSemesterBeginn.setAbmeldedatum(createCalendar("2024-08-31"));
    when(anmeldungRepository.findBySchuelerIdOrderByAnmeldedatumDesc(anyInt()))
        .thenReturn(List.of(anmeldungAbgemeldetNachSemesterBeginn));
    assertTrue(kursService.isAngemeldetForSemesterAndKurs(semester, kursanmeldungAngemeldet));

    Anmeldung anmeldungAbgemeldetVorSemesterBeginn = new Anmeldung();
    anmeldungAbgemeldetVorSemesterBeginn.setAnmeldedatum(createCalendar("2024-01-01"));
    anmeldungAbgemeldetVorSemesterBeginn.setAbmeldedatum(createCalendar("2024-07-31"));
    when(anmeldungRepository.findBySchuelerIdOrderByAnmeldedatumDesc(anyInt()))
        .thenReturn(List.of(anmeldungAbgemeldetVorSemesterBeginn));
    assertFalse(kursService.isAngemeldetForSemesterAndKurs(semester, kursanmeldungAngemeldet));
  }

  private Calendar createCalendar(String dateAsString) {
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    Date date;
    try {
      date = df.parse(dateAsString);
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    return calendar;
  }
}
