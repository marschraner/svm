package ch.metzenthin.svm.domain.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Martin Schraner
 */
public class IbanNummerFormatterTest {

    private IbanNummerFormatter ibanNummerFormatter;

    @Before
    public void before() {
        ibanNummerFormatter = new IbanNummerFormatter();
    }

    @Test
    public void testFormat()  {
        assertEquals("CH31 8123 9000 0012 4568 9", ibanNummerFormatter.format("CH3181239000001245689"));
    }

}