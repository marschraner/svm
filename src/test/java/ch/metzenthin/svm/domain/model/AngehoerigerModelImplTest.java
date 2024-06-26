package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.SvmValidationException;
import org.junit.Before;
import org.junit.Test;
import test.TestCompletedListener;

import static org.junit.Assert.*;

/**
 * @author Hans Stamm
 */
public class AngehoerigerModelImplTest {

    private AngehoerigerModel angehoerigerModel;

    @Before
    public void setUp() {
        angehoerigerModel = new AngehoerigerModelImpl();
    }

    @Test
    public void testSetIsRechnungsempfaenger() {
        angehoerigerModel.setIsRechnungsempfaenger(true);
        assertTrue("IsRechnungsempfaenger true erwartet", angehoerigerModel.isRechnungsempfaenger());
    }

    @Test
    public void testSetIsRechnungsempfaenger_False() {
        angehoerigerModel.setIsRechnungsempfaenger(false);
        assertFalse("IsRechnungsempfaenger false erwartet", angehoerigerModel.isRechnungsempfaenger());
    }

    @Test
    public void testSetIsRechnungsempfaenger_FalseAgain() {
        angehoerigerModel.setIsRechnungsempfaenger(true);
        angehoerigerModel.setIsRechnungsempfaenger(false);
        assertFalse("IsRechnungsempfaenger false erwartet", angehoerigerModel.isRechnungsempfaenger());
    }

    @Test
    public void testGetAngehoeriger() {
        assertNotNull("Angehoeriger nicht null erwartet", angehoerigerModel.getAngehoeriger());
    }

    @Test
    public void testGetAdresse() {
        assertNotNull("Adresse nicht null erwartet", angehoerigerModel.getAdresse());
    }

    @Test
    public void testIsCompleted() {
        TestCompletedListener testCompletedListener = new TestCompletedListener();
        angehoerigerModel.addCompletedListener(testCompletedListener);
        try {
            angehoerigerModel.setNachname("Nachname");
            angehoerigerModel.setVorname("Vorname");
            angehoerigerModel.setStrasseHausnummer("StrasseHausnummer");
            angehoerigerModel.setPlz("Plzz");
            angehoerigerModel.setOrt("Ort");
        } catch (SvmValidationException e) {
            e.printStackTrace(System.err);
            fail("Keine Exception erwartet");
        }
        assertTrue("IsCompleted true erwartet", angehoerigerModel.isCompleted());
        try {
            angehoerigerModel.validate();
        } catch (SvmValidationException e) {
            e.printStackTrace(System.err);
            fail("Keine Exception erwartet");
        }
        assertEquals("Aufruf von CompletedListener einmal erwartet", 1, testCompletedListener.getCounter());
    }

    @Test
    public void testIsCompleted_True() {
        assertTrue("IsCompleted true erwartet", angehoerigerModel.isCompleted());
    }

    @Test
    public void testIsCompleted_False() {
        TestCompletedListener testCompletedListener = new TestCompletedListener();
        angehoerigerModel.addCompletedListener(testCompletedListener);
        try {
            angehoerigerModel.setStrasseHausnummer("StrasseHausnummer");
        } catch (SvmValidationException e) {
            e.printStackTrace(System.err);
            fail("Keine Exception erwartet");
        }
        assertFalse("IsCompleted false erwartet", angehoerigerModel.isCompleted());
        try {
            angehoerigerModel.validate();
            fail("Exception erwartet");
        } catch (SvmValidationException e) {
            e.printStackTrace(System.out);
        }
        assertEquals("Aufruf von CompletedListener einmal erwartet", 1, testCompletedListener.getCounter());
    }

    @Test
    public void testIsEmpty() {
        assertTrue("Empty erwartet", angehoerigerModel.isEmpty());
    }

    @Test
    public void testIsEmpty_FalseNachname() throws SvmValidationException {
        angehoerigerModel.setNachname("Nachname");
        assertFalse("Not Empty erwartet (Nachname)", angehoerigerModel.isEmpty());
    }

    @Test
    public void testIsEmpty_FalseVorname() throws SvmValidationException {
        angehoerigerModel.setVorname("Vorname");
        assertFalse("Not Empty erwartet (Vorname)", angehoerigerModel.isEmpty());
    }

    @Test
    public void testIsEmpty_FalseStrasse() throws SvmValidationException {
        angehoerigerModel.setStrasseHausnummer("StrasseHausnummer");
        assertFalse("Not Empty erwartet (StrasseHausnummer)", angehoerigerModel.isEmpty());
    }

    @Test
    public void testIsEmpty_FalseNameAdresse() throws SvmValidationException {
        angehoerigerModel.setNachname("Nachname");
        angehoerigerModel.setVorname("Vorname");
        angehoerigerModel.setStrasseHausnummer("StrasseHausnummer");
        angehoerigerModel.setPlz("Plzz");
        angehoerigerModel.setOrt("Ort");
        assertFalse("Not Empty erwartet (Name, Adresse)", angehoerigerModel.isEmpty());
    }

}