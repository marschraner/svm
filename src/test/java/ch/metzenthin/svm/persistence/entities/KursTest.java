package ch.metzenthin.svm.persistence.entities;

import static ch.metzenthin.svm.common.utils.Converter.toCalendar;
import static ch.metzenthin.svm.common.utils.Converter.toTime;
import static org.junit.Assert.assertEquals;

import ch.metzenthin.svm.common.datatypes.Anrede;
import ch.metzenthin.svm.common.datatypes.Wochentag;
import org.junit.Test;

/**
 * @author Hans Stamm
 */
public class KursTest {

  @Test
  public void testToString() throws Exception {
    Kurs kurs =
        new Kurs("11/12", "1", Wochentag.DIENSTAG, toTime("18.00"), toTime("19.00"), "test");
    Mitarbeiter mitarbeiter =
        new Mitarbeiter(
            Anrede.FRAU,
            "Vorname",
            "Lehrkraft1",
            toCalendar("11.11.95"),
            null,
            null,
            null,
            "111.11.111.1111.11",
            "CH31 8123 9000 0012 4568 9",
            true,
            null,
            null,
            true);
    Mitarbeiter mitarbeiter2 =
        new Mitarbeiter(
            Anrede.FRAU,
            "Vorname",
            "Lehrkraft2",
            toCalendar("11.11.95"),
            null,
            null,
            null,
            "222.22.222.2222.22",
            "CH31 8123 9000 0012 6666 9",
            true,
            null,
            null,
            true);
    kurs.addLehrkraft(mitarbeiter);
    kurs.addLehrkraft(mitarbeiter2);
    kurs.setKurstyp(new Kurstyp("Kurstyp Test", true));
    assertEquals(
        "Kurstyp Test 1, Dienstag 18.00-19.00 (Vorname Lehrkraft1 / Vorname Lehrkraft2)",
        kurs.toString());
  }
}
