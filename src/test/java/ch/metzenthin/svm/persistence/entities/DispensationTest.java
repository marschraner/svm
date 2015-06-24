package ch.metzenthin.svm.persistence.entities;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * @author Martin Schraner
 */
public class DispensationTest {

    @Test
    public void testCompareTo() throws Exception {
        List<Dispensation> dispensationen = new ArrayList<>();
        dispensationen.add(new Dispensation(new GregorianCalendar(2014, Calendar.JUNE, 2), new GregorianCalendar(2014, Calendar.AUGUST, 31), "Beinbruch"));
        dispensationen.add(new Dispensation(new GregorianCalendar(2014, Calendar.JULY, 1), new GregorianCalendar(2014, Calendar.JULY, 31), "Auslandaufenthalt Italien"));
        dispensationen.add(new Dispensation(new GregorianCalendar(2015, Calendar.APRIL, 8), null, "Arm gebrochen"));
        dispensationen.add(new Dispensation(new GregorianCalendar(2015, Calendar.APRIL, 8), new GregorianCalendar(2015, Calendar.JUNE, 20), "Auslandaufenthalt USA"));
        dispensationen.add(new Dispensation(new GregorianCalendar(2015, Calendar.APRIL, 8), new GregorianCalendar(2015, Calendar.APRIL, 15), "Impfung"));
        Collections.sort(dispensationen);

        // Neuste zuoberst
        assertEquals("Arm gebrochen", dispensationen.get(0).getGrund());
        assertEquals("Auslandaufenthalt USA", dispensationen.get(1).getGrund());
        assertEquals("Impfung", dispensationen.get(2).getGrund());
        assertEquals("Auslandaufenthalt Italien", dispensationen.get(3).getGrund());
        assertEquals("Beinbruch", dispensationen.get(4).getGrund());
    }
}