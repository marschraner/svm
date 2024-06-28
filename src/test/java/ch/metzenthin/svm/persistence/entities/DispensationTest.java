package ch.metzenthin.svm.persistence.entities;

import ch.metzenthin.svm.common.utils.SvmProperties;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;
import static org.junit.Assert.assertEquals;

/**
 * @author Martin Schraner
 */
public class DispensationTest {

    private boolean neusteZuoberst;

    @Before
    public void setUp() {
        createSvmPropertiesFileDefault();
        Properties svmProperties = SvmProperties.getSvmProperties();
        neusteZuoberst = !svmProperties.getProperty(SvmProperties.KEY_NEUSTE_ZUOBERST).equals("false");
    }

    @Test
    public void testCompareTo() {
        List<Dispensation> dispensationen = new ArrayList<>();
        dispensationen.add(new Dispensation(new GregorianCalendar(2014, Calendar.JUNE, 2), new GregorianCalendar(2014, Calendar.AUGUST, 31), null, "Beinbruch"));
        dispensationen.add(new Dispensation(new GregorianCalendar(2014, Calendar.JULY, 1), new GregorianCalendar(2014, Calendar.JULY, 31), null, "Auslandaufenthalt Italien"));
        dispensationen.add(new Dispensation(new GregorianCalendar(2015, Calendar.APRIL, 8), null, null, "Arm gebrochen"));
        dispensationen.add(new Dispensation(new GregorianCalendar(2015, Calendar.APRIL, 8), new GregorianCalendar(2015, Calendar.JUNE, 20), null, "Auslandaufenthalt USA"));
        dispensationen.add(new Dispensation(new GregorianCalendar(2015, Calendar.APRIL, 8), new GregorianCalendar(2015, Calendar.APRIL, 15), null, "Impfung"));
        Collections.sort(dispensationen);

        // Neuste zuoberst
        if (neusteZuoberst) {
            assertEquals("Arm gebrochen", dispensationen.get(0).getGrund());
            assertEquals("Auslandaufenthalt USA", dispensationen.get(1).getGrund());
            assertEquals("Impfung", dispensationen.get(2).getGrund());
            assertEquals("Auslandaufenthalt Italien", dispensationen.get(3).getGrund());
            assertEquals("Beinbruch", dispensationen.get(4).getGrund());
        } else {
            assertEquals("Arm gebrochen", dispensationen.get(4).getGrund());
            assertEquals("Auslandaufenthalt USA", dispensationen.get(3).getGrund());
            assertEquals("Impfung", dispensationen.get(2).getGrund());
            assertEquals("Auslandaufenthalt Italien", dispensationen.get(1).getGrund());
            assertEquals("Beinbruch", dispensationen.get(0).getGrund());
        }
    }
}