package ch.metzenthin.svm.persistence.entities;

import ch.metzenthin.svm.common.dataTypes.Anrede;
import ch.metzenthin.svm.common.dataTypes.Wochentag;
import org.junit.Test;

import static ch.metzenthin.svm.common.utils.Converter.toCalendar;
import static ch.metzenthin.svm.common.utils.Converter.toTime;
import static org.junit.Assert.assertEquals;

/**
 * @author Hans Stamm
 */
public class KursTest {

    @Test
    public void testToString() throws Exception {
        Kurs kurs = new Kurs("11/12", "1", Wochentag.DIENSTAG, toTime("18.00"), toTime("19.00"), "test");
        Lehrkraft lehrkraft = new Lehrkraft(Anrede.FRAU, "Vorname", "Lehrkraft1", toCalendar("11.11.95"), null, null, null, "111.11.111.1111.11", null, true);
        Lehrkraft lehrkraft2 = new Lehrkraft(Anrede.FRAU, "Vorname", "Lehrkraft2", toCalendar("11.11.95"), null, null, null, "222.22.222.2222.22", null, true);
        kurs.addLehrkraft(lehrkraft);
        kurs.addLehrkraft(lehrkraft2);
        kurs.setKurstyp(new Kurstyp("Kurstyp Test", true));
        assertEquals("Kurstyp Test 1, Dienstag 18.00-19.00 (Vorname Lehrkraft1 / Vorname Lehrkraft2)", kurs.toString());
    }

}