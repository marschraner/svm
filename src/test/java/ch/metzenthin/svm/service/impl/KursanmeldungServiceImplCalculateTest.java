package ch.metzenthin.svm.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import ch.metzenthin.svm.common.datatypes.Semesterbezeichnung;
import ch.metzenthin.svm.common.datatypes.Wochentag;
import ch.metzenthin.svm.persistence.entities.Kurs;
import ch.metzenthin.svm.persistence.entities.Kursanmeldung;
import ch.metzenthin.svm.persistence.entities.Semester;
import java.sql.Time;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.junit.jupiter.api.Test;

/**
 * @author Hans Stamm
 */
@SuppressWarnings("java:S5961")
class KursanmeldungServiceImplCalculateTest {

  private final Semester erstesSemester =
      new Semester(
          "2015/2016",
          Semesterbezeichnung.ERSTES_SEMESTER,
          new GregorianCalendar(2015, Calendar.AUGUST, 17),
          new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
          new GregorianCalendar(2015, Calendar.OCTOBER, 5),
          new GregorianCalendar(2015, Calendar.OCTOBER, 17),
          new GregorianCalendar(2015, Calendar.DECEMBER, 21),
          new GregorianCalendar(2016, Calendar.JANUARY, 2));
  private final Semester zweitesSemester =
      new Semester(
          "2015/2015",
          Semesterbezeichnung.ZWEITES_SEMESTER,
          new GregorianCalendar(2016, Calendar.FEBRUARY, 29),
          new GregorianCalendar(2016, Calendar.JULY, 16),
          new GregorianCalendar(2016, Calendar.APRIL, 25),
          new GregorianCalendar(2016, Calendar.MAY, 7),
          null,
          null);

  @Test
  void testCalculateHoechsteAnzahlWochen_MittwochKurs() {

    Kurs kurs =
        new Kurs(
            "2-3 J",
            "Vorkindergarten",
            Wochentag.MITTWOCH,
            Time.valueOf("10:00:00"),
            Time.valueOf("10:50:00"),
            "Dies ist ein Test.");

    // 1. Semester
    // Anmeldung am Semesterbeginn
    Kursanmeldung kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(22, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // Anmeldung vor Semesterbeginn
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 16),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(22, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // Anmeldung einen Tag vor erstem Kurstag
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 18),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(22, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // Anmeldung am ersten Kurstag
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 19),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(22, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // Anmeldung einen Tag nach erstem Kurstag
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 20),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(21, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // Anmeldung Samstag erste Woche
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 22),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(21, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // Anmeldung Sonntag erste Woche
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 23),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(21, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // Anmeldung Montag zweite Woche
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 24),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(21, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // Anmeldung am Samstag erste Schulferienwoche
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 22),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(21, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // Anmeldung am Sonntag erste Schulferienwoche
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 23),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(21, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // Anmeldung am Montag zweite Schulferienwoche
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 24),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(21, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // Anmeldung am Kurstag zweite Schulferienwoche
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 26),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(21, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // letzter Schultag vor Herbstferien
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.OCTOBER, 3),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(15, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // Sonntag vor Herbstferien
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.OCTOBER, 4),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(15, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // während Herbstferien
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.OCTOBER, 10),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(15, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // Sonntag letzte Herbstferienwoche
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.OCTOBER, 18),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(15, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // Erster Schultag nach Herbstferien
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.OCTOBER, 19),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(15, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // erster Kurstag nach Herbstferien
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.OCTOBER, 21),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(15, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // nach Herbstferien
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.OCTOBER, 26),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(14, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // letzter Schultag vor Weihnachtsferien
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.DECEMBER, 19),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(6, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // Sonntag vor Weihnachtsferien
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.DECEMBER, 20),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(6, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // während Weihnachtsferien
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.DECEMBER, 29),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(6, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // Sonntag letzte Weihnachtsferienwoche
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2016, Calendar.JANUARY, 3),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(6, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // Erster Schultag nach Weihnachtsferien
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2016, Calendar.JANUARY, 4),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(6, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // erster Kurstag nach Weihnachtsferien
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2016, Calendar.JANUARY, 6),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(6, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // Anmeldung nach Semesterende
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 14),
            null);
    assertEquals(22, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // Frühere Abmeldung
    // Abmeldung am Semesterende
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(22, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // Abmeldung einen Tag nach letztem Kurstag
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 11),
            null);
    assertEquals(22, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // Abmeldung am letzten Kurstag
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 10),
            null);
    assertEquals(22, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // Abmeldung einen Tag vor letztem Kurstag
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 9),
            null);
    assertEquals(21, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // Abmeldung am Montag letzte Kurswoche
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 8),
            null);
    assertEquals(21, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // Abmeldung am Sonntag vorletzte Kurswoche
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 7),
            null);
    assertEquals(21, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // Abmeldung am Samstag vorletzte Kurswoche
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 6),
            null);
    assertEquals(21, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // Abmeldung am Freitag vorletzte Kurswoche
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 5),
            null);
    assertEquals(21, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // Abmeldung am Kurstag vorletzte Kurswoche
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 3),
            null);
    assertEquals(21, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // nach Weihnachtsferien
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2016, Calendar.JANUARY, 9),
            null);
    assertEquals(17, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // erster Kurstag nach Weihnachtsferien
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2016, Calendar.JANUARY, 6),
            null);
    assertEquals(17, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // Erster Schultag nach Weihnachtsferien
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2016, Calendar.JANUARY, 4),
            null);
    assertEquals(16, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // Sonntag letzte Weihnachtsferienwoche
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2016, Calendar.JANUARY, 3),
            null);
    assertEquals(16, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // während Weihnachtsferien
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2015, Calendar.DECEMBER, 29),
            null);
    assertEquals(16, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // Sonntag vor Weihnachtsferien
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2015, Calendar.DECEMBER, 20),
            null);
    assertEquals(16, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // letzter Schultag vor Weihnachtsferien
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2015, Calendar.DECEMBER, 19),
            null);
    assertEquals(16, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // letzter Kurstag vor Weihnachtsferien
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2015, Calendar.DECEMBER, 16),
            null);
    assertEquals(16, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // erster Kurstag nach Herbstferien
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2015, Calendar.OCTOBER, 21),
            null);
    assertEquals(8, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // Erster Schultag nach Herbstferien
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2015, Calendar.OCTOBER, 19),
            null);
    assertEquals(7, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // Sonntag letzte Herbstferienwoche
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2015, Calendar.OCTOBER, 18),
            null);
    assertEquals(7, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // während Herbstferien
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2015, Calendar.OCTOBER, 8),
            null);
    assertEquals(7, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // Sonntag vor Herbstferien
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2015, Calendar.OCTOBER, 4),
            null);
    assertEquals(7, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // letzter Schultag vor Herbstferien
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2015, Calendar.OCTOBER, 3),
            null);
    assertEquals(7, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // letzter Kurstag vor Herbstferien
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2015, Calendar.SEPTEMBER, 30),
            null);
    assertEquals(7, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // 2. Semester
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2016, Calendar.FEBRUARY, 29),
            new GregorianCalendar(2016, Calendar.JULY, 16),
            null);
    assertEquals(
        18, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, zweitesSemester));

    // Spätere Anmeldung
    // Vor Frühlingsferien
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2016, Calendar.APRIL, 20),
            new GregorianCalendar(2016, Calendar.JULY, 16),
            null);
    assertEquals(
        11, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, zweitesSemester));

    // Nach Frühlingsferien
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2016, Calendar.MAY, 17),
            new GregorianCalendar(2016, Calendar.JULY, 16),
            null);
    assertEquals(9, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, zweitesSemester));

    // Frühere Abmeldung
    // Vor Frühlingsferien
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2016, Calendar.FEBRUARY, 29),
            new GregorianCalendar(2016, Calendar.APRIL, 2),
            null);
    assertEquals(5, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, zweitesSemester));

    // Nach Frühlingsferien
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2016, Calendar.FEBRUARY, 29),
            new GregorianCalendar(2016, Calendar.JUNE, 18),
            null);
    assertEquals(
        14, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, zweitesSemester));
  }

  @Test
  void testCalculateHoechsteAnzahlWochen_MontagKurs() {

    Kurs kurs =
        new Kurs(
            "2-3 J",
            "Vorkindergarten",
            Wochentag.MONTAG,
            Time.valueOf("10:00:00"),
            Time.valueOf("10:50:00"),
            "Dies ist ein Test.");

    // Anmeldung am Semesterbeginn
    Kursanmeldung kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(22, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // Anmeldung vor Semesterbeginn
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 16),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(22, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // Anmeldung am ersten Kurstag
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(22, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // Anmeldung einen Tag nach erstem Kurstag
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 18),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(21, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // Anmeldung Samstag erste Woche
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 22),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(21, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // Anmeldung Sonntag erste Woche
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 23),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(21, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // Anmeldung Montag zweite Woche
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 24),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(21, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // Frühere Abmeldung
    // Abmeldung am Semesterende
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(22, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // Abmeldung einen Tag nach letztem Kurstag
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 9),
            null);
    assertEquals(22, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // Abmeldung am letzten Kurstag
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 8),
            null);
    assertEquals(22, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // Abmeldung am Sonntag vorletzte Kurswoche
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 7),
            null);
    assertEquals(21, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // Abmeldung am Samstag vorletzte Kurswoche
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 6),
            null);
    assertEquals(21, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // Abmeldung am Freitag vorletzte Kurswoche
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 5),
            null);
    assertEquals(21, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // Abmeldung am Kurstag vorletzte Kurswoche
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 1),
            null);
    assertEquals(21, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));
  }

  @Test
  void testCalculateHoechsteAnzahlWochen_SamstagKurs() {

    Kurs kurs =
        new Kurs(
            "2-3 J",
            "Vorkindergarten",
            Wochentag.SAMSTAG,
            Time.valueOf("10:00:00"),
            Time.valueOf("10:50:00"),
            "Dies ist ein Test.");

    // Anmeldung am Semesterbeginn
    Kursanmeldung kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(22, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // Anmeldung vor Semesterbeginn
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 16),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(22, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // Anmeldung einen Tag vor erstem Kurstag
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 21),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(22, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // Anmeldung am ersten Kurstag
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 22),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(22, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // Anmeldung einen Tag nach erstem Kurstag
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 23),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(21, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // Anmeldung Montag zweite Woche
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 24),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(21, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // Frühere Abmeldung
    // Abmeldung am Semesterende
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(22, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // Abmeldung einen Tag nach letztem Kurstag
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(22, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // Abmeldung einen Tag vor letztem Kurstag
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 12),
            null);
    assertEquals(21, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // Abmeldung am Montag letzte Kurswoche
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 8),
            null);
    assertEquals(21, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // Abmeldung am Sonntag vorletzte Kurswoche
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 7),
            null);
    assertEquals(21, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // Abmeldung am Kurstag vorletzte Kurswoche
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 6),
            null);
    assertEquals(21, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));

    // Abmeldung am Freitag vorletzte Kurswoche
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 5),
            null);
    assertEquals(20, KursanmeldungServiceImpl.calculateAnzahlWochen(kursanmeldung, erstesSemester));
  }
}
