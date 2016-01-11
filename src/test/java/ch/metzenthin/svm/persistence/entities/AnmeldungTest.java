package ch.metzenthin.svm.persistence.entities;

import org.junit.Test;

import java.util.*;

import static ch.metzenthin.svm.common.utils.Converter.toCalendar;
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

    @Test
    public void testCopyFieldValuesFrom() throws Exception{
        Anmeldung to = new Anmeldung(toCalendar("01.01.2015"), null);
        Anmeldung from = new Anmeldung(toCalendar("01.02.2015"), toCalendar("31.12.2015"));
        to.copyFieldValuesFrom(from);
        assertEquals(to.getAnmeldedatum(), from.getAnmeldedatum());
        assertEquals(to.getAbmeldedatum(), from.getAbmeldedatum());
    }

    @Test
    public void testIsInPast_NoAbmeldung() throws Exception{
        Anmeldung anmeldung = new Anmeldung(toCalendar("01.01.2015"), null);
        assertFalse(anmeldung.isInPast());
    }

    @Test
    public void testIsInPast_AbmeldungInFuture() throws Exception{
        Anmeldung anmeldung = new Anmeldung(toCalendar("01.01.2015"), toCalendar("31.12.2115"));
        assertFalse(anmeldung.isInPast());
    }

    @Test
    public void testIsInPast_True() throws Exception{
        Anmeldung anmeldung = new Anmeldung(toCalendar("01.01.2015"), toCalendar("30.06.2015"));
        assertTrue(anmeldung.isInPast());
    }

    @Test
    public void testIsInPast_AnmeldungInFuture() throws Exception{
        Anmeldung anmeldung = new Anmeldung(toCalendar("01.01.2116"), toCalendar("30.06.2116"));
        assertFalse(anmeldung.isInPast());
    }

}