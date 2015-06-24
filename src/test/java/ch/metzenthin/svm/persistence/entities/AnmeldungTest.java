package ch.metzenthin.svm.persistence.entities;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * @author Martin Schraner
 */
public class AnmeldungTest {

    @Test
    public void testCompareTo() throws Exception {
        List<Anmeldung> anmeldungen = new ArrayList<>();
        anmeldungen.add(new Anmeldung(new GregorianCalendar(2014, Calendar.JULY, 1), new GregorianCalendar(2014, Calendar.DECEMBER, 31)));
        anmeldungen.add(new Anmeldung(new GregorianCalendar(2013, Calendar.MAY, 1), new GregorianCalendar(2014, Calendar.AUGUST, 31)));
        anmeldungen.add(new Anmeldung(new GregorianCalendar(2015, Calendar.JUNE, 1), null));
        Collections.sort(anmeldungen);

        // Neuste zuoberst
        assertEquals(new GregorianCalendar(2015, Calendar.JUNE, 1), anmeldungen.get(0).getAnmeldedatum());
        assertEquals(new GregorianCalendar(2014, Calendar.JULY, 1), anmeldungen.get(1).getAnmeldedatum());
        assertEquals(new GregorianCalendar(2013, Calendar.MAY, 1), anmeldungen.get(2).getAnmeldedatum());
    }
}