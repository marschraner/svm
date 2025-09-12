package ch.metzenthin.svm.domain.commands;

import static org.junit.Assert.assertEquals;

import ch.metzenthin.svm.common.datatypes.Semesterbezeichnung;
import ch.metzenthin.svm.common.datatypes.Wochentag;
import ch.metzenthin.svm.persistence.entities.Kurs;
import ch.metzenthin.svm.persistence.entities.Kursanmeldung;
import ch.metzenthin.svm.persistence.entities.Semester;
import java.sql.Time;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.junit.Test;

/**
 * @author Martin Schraner
 */
public class CalculateAnzWochenCommandTest {

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
  private final CalculateAnzWochenCommand calculateAnzWochenCommandErstesSemester =
      new CalculateAnzWochenCommand(null, erstesSemester);
  private final CalculateAnzWochenCommand calculateAnzWochenCommandZweitesSemester =
      new CalculateAnzWochenCommand(null, zweitesSemester);

  @SuppressWarnings("java:S5961")
  @Test
  public void testCalculateAnzWochenKursanmeldung_MittwochKurs() {

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
    assertEquals(
        22, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // Anmeldung vor Semesterbeginn
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 16),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(
        22, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // Anmeldung einen Tag vor erstem Kurstag
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 18),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(
        22, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // Anmeldung am ersten Kurstag
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 19),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(
        22, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // Anmeldung einen Tag nach erstem Kurstag
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 20),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(
        21, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // Anmeldung Samstag erste Woche
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 22),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(
        21, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // Anmeldung Sonntag erste Woche
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 23),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(
        21, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // Anmeldung Montag zweite Woche
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 24),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(
        21, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // Anmeldung am Samstag erste Schulferienwoche
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 22),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(
        21, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // Anmeldung am Sonntag erste Schulferienwoche
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 23),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(
        21, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // Anmeldung am Montag zweite Schulferienwoche
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 24),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(
        21, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // Anmeldung am Kurstag zweite Schulferienwoche
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 26),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(
        21, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // letzter Schultag vor Herbstferien
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.OCTOBER, 3),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(
        15, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // Sonntag vor Herbstferien
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.OCTOBER, 4),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(
        15, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // während Herbstferien
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.OCTOBER, 10),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(
        15, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // Sonntag letzte Herbstferienwoche
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.OCTOBER, 18),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(
        15, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // Erster Schultag nach Herbstferien
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.OCTOBER, 19),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(
        15, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // erster Kurstag nach Herbstferien
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.OCTOBER, 21),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(
        15, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // nach Herbstferien
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.OCTOBER, 26),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(
        14, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // letzter Schultag vor Weihnachtsferien
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.DECEMBER, 19),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(
        6, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // Sonntag vor Weihnachtsferien
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.DECEMBER, 20),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(
        6, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // während Weihnachtsferien
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.DECEMBER, 29),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(
        6, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // Sonntag letzte Weihnachtsferienwoche
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2016, Calendar.JANUARY, 3),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(
        6, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // Erster Schultag nach Weihnachtsferien
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2016, Calendar.JANUARY, 4),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(
        6, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // erster Kurstag nach Weihnachtsferien
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2016, Calendar.JANUARY, 6),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(
        6, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // Anmeldung nach Semesterende
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 14),
            null);
    assertEquals(
        22, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // Frühere Abmeldung
    // Abmeldung am Semesterende
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(
        22, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // Abmeldung einen Tag nach letztem Kurstag
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 11),
            null);
    assertEquals(
        22, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // Abmeldung am letzten Kurstag
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 10),
            null);
    assertEquals(
        22, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // Abmeldung einen Tag vor letztem Kurstag
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 9),
            null);
    assertEquals(
        21, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // Abmeldung am Montag letzte Kurswoche
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 8),
            null);
    assertEquals(
        21, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // Abmeldung am Sonntag vorletzte Kurswoche
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 7),
            null);
    assertEquals(
        21, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // Abmeldung am Samstag vorletzte Kurswoche
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 6),
            null);
    assertEquals(
        21, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // Abmeldung am Freitag vorletzte Kurswoche
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 5),
            null);
    assertEquals(
        21, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // Abmeldung am Kurstag vorletzte Kurswoche
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 3),
            null);
    assertEquals(
        21, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // nach Weihnachtsferien
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2016, Calendar.JANUARY, 9),
            null);
    assertEquals(
        17, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // erster Kurstag nach Weihnachtsferien
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2016, Calendar.JANUARY, 6),
            null);
    assertEquals(
        17, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // Erster Schultag nach Weihnachtsferien
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2016, Calendar.JANUARY, 4),
            null);
    assertEquals(
        16, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // Sonntag letzte Weihnachtsferienwoche
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2016, Calendar.JANUARY, 3),
            null);
    assertEquals(
        16, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // während Weihnachtsferien
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2015, Calendar.DECEMBER, 29),
            null);
    assertEquals(
        16, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // Sonntag vor Weihnachtsferien
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2015, Calendar.DECEMBER, 20),
            null);
    assertEquals(
        16, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // letzter Schultag vor Weihnachtsferien
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2015, Calendar.DECEMBER, 19),
            null);
    assertEquals(
        16, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // letzter Kurstag vor Weihnachtsferien
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2015, Calendar.DECEMBER, 16),
            null);
    assertEquals(
        16, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // erster Kurstag nach Herbstferien
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2015, Calendar.OCTOBER, 21),
            null);
    assertEquals(
        8, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // Erster Schultag nach Herbstferien
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2015, Calendar.OCTOBER, 19),
            null);
    assertEquals(
        7, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // Sonntag letzte Herbstferienwoche
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2015, Calendar.OCTOBER, 18),
            null);
    assertEquals(
        7, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // während Herbstferien
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2015, Calendar.OCTOBER, 8),
            null);
    assertEquals(
        7, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // Sonntag vor Herbstferien
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2015, Calendar.OCTOBER, 4),
            null);
    assertEquals(
        7, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // letzter Schultag vor Herbstferien
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2015, Calendar.OCTOBER, 3),
            null);
    assertEquals(
        7, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // letzter Kurstag vor Herbstferien
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2015, Calendar.SEPTEMBER, 30),
            null);
    assertEquals(
        7, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // 2. Semester
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2016, Calendar.FEBRUARY, 29),
            new GregorianCalendar(2016, Calendar.JULY, 16),
            null);
    assertEquals(
        18,
        calculateAnzWochenCommandZweitesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

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
        11,
        calculateAnzWochenCommandZweitesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // Nach Frühlingsferien
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2016, Calendar.MAY, 17),
            new GregorianCalendar(2016, Calendar.JULY, 16),
            null);
    assertEquals(
        9, calculateAnzWochenCommandZweitesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // Frühere Abmeldung
    // Vor Frühlingsferien
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2016, Calendar.FEBRUARY, 29),
            new GregorianCalendar(2016, Calendar.APRIL, 2),
            null);
    assertEquals(
        5, calculateAnzWochenCommandZweitesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // Nach Frühlingsferien
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2016, Calendar.FEBRUARY, 29),
            new GregorianCalendar(2016, Calendar.JUNE, 18),
            null);
    assertEquals(
        14,
        calculateAnzWochenCommandZweitesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));
  }

  @Test
  public void testCalculateAnzWochenKursanmeldung_MontagKurs() {

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
    assertEquals(
        22, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // Anmeldung vor Semesterbeginn
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 16),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(
        22, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // Anmeldung am ersten Kurstag
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(
        22, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // Anmeldung einen Tag nach erstem Kurstag
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 18),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(
        21, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // Anmeldung Samstag erste Woche
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 22),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(
        21, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // Anmeldung Sonntag erste Woche
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 23),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(
        21, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // Anmeldung Montag zweite Woche
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 24),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(
        21, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // Frühere Abmeldung
    // Abmeldung am Semesterende
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(
        22, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // Abmeldung einen Tag nach letztem Kurstag
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 9),
            null);
    assertEquals(
        22, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // Abmeldung am letzten Kurstag
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 8),
            null);
    assertEquals(
        22, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // Abmeldung am Sonntag vorletzte Kurswoche
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 7),
            null);
    assertEquals(
        21, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // Abmeldung am Samstag vorletzte Kurswoche
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 6),
            null);
    assertEquals(
        21, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // Abmeldung am Freitag vorletzte Kurswoche
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 5),
            null);
    assertEquals(
        21, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // Abmeldung am Kurstag vorletzte Kurswoche
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 1),
            null);
    assertEquals(
        21, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));
  }

  @Test
  public void testCalculateAnzWochenKursanmeldung_SamstagKurs() {

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
    assertEquals(
        22, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // Anmeldung vor Semesterbeginn
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 16),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(
        22, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // Anmeldung einen Tag vor erstem Kurstag
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 21),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(
        22, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // Anmeldung am ersten Kurstag
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 22),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(
        22, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // Anmeldung einen Tag nach erstem Kurstag
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 23),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(
        21, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // Anmeldung Montag zweite Woche
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 24),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(
        21, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // Frühere Abmeldung
    // Abmeldung am Semesterende
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(
        22, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // Abmeldung einen Tag nach letztem Kurstag
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 13),
            null);
    assertEquals(
        22, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // Abmeldung einen Tag vor letztem Kurstag
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 12),
            null);
    assertEquals(
        21, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // Abmeldung am Montag letzte Kurswoche
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 8),
            null);
    assertEquals(
        21, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // Abmeldung am Sonntag vorletzte Kurswoche
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 7),
            null);
    assertEquals(
        21, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // Abmeldung am Kurstag vorletzte Kurswoche
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 6),
            null);
    assertEquals(
        21, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));

    // Abmeldung am Freitag vorletzte Kurswoche
    kursanmeldung =
        new Kursanmeldung(
            null,
            kurs,
            new GregorianCalendar(2015, Calendar.AUGUST, 17),
            new GregorianCalendar(2016, Calendar.FEBRUARY, 5),
            null);
    assertEquals(
        20, calculateAnzWochenCommandErstesSemester.calculateAnzWochenKursanmeldung(kursanmeldung));
  }
}
