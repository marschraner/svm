package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.SvmValidationException;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Hans Stamm
 */
public class PersonModelImplTest {

    //------------------------------------------------------------------------------------------------------------------
    // Schüler
    //------------------------------------------------------------------------------------------------------------------

    @Test
    public void test_EmptySchueler() {
        PersonModel personModel = createSchuelerModel();
        assertTrue(personModel.isEmpty());
        assertFalse(personModel.isCompleted());
        validate(personModel, 2000);
    }

    @Test
    public void test_SchuelerWithNachname() throws SvmValidationException {
        PersonModel personModel = createSchuelerModel();
        personModel.setNachname("Nachname");
        assertFalse(personModel.isEmpty());
        assertFalse(personModel.isCompleted());
        validate(personModel, 2000);
    }

    @Test
    public void test_SchuelerWithVorname() throws SvmValidationException {
        PersonModel personModel = createSchuelerModel();
        personModel.setVorname("Vorname");
        assertFalse(personModel.isEmpty());
        assertFalse(personModel.isCompleted());
        validate(personModel, 2000);
    }

    @Test
    public void test_SchuelerWithName() throws SvmValidationException {
        PersonModel personModel = createSchuelerModel();
        personModel.setNachname("Nachname");
        personModel.setVorname("Vorname");
        assertFalse(personModel.isEmpty());
        assertFalse(personModel.isCompleted());
        validate(personModel, 2001);
    }

    @Test
    public void test_SchuelerWithStrasse() throws SvmValidationException {
        PersonModel personModel = createSchuelerModel();
        personModel.setStrasseHausnummer("Strasse");
        assertFalse(personModel.isEmpty());
        assertFalse(personModel.isCompleted());
        validate(personModel, 2000);
    }

    @Test
    public void test_SchuelerWithAnschrift() throws SvmValidationException {
        PersonModel personModel = createSchuelerModel();
        personModel.setStrasseHausnummer("Strasse");
        personModel.setPlz("Plzz");
        personModel.setOrt("Ort");
        assertFalse(personModel.isEmpty());
        assertFalse(personModel.isCompleted());
        validate(personModel, 2000);
    }

    @Test
    public void test_SchuelerWithNameAnschriftComplete() throws SvmValidationException {
        PersonModel personModel = createSchuelerModel();
        personModel.setNachname("Nachname");
        personModel.setVorname("Vorname");
        personModel.setStrasseHausnummer("Strasse");
        personModel.setPlz("Plzz");
        personModel.setOrt("Ort");
        assertFalse(personModel.isEmpty());
        assertTrue(personModel.isCompleted());
        validate(personModel, null);
    }

    @Test
    public void test_SchuelerWithNameAnschriftFestnetzComplete() throws SvmValidationException {
        PersonModel personModel = createSchuelerModel();
        personModel.setNachname("Nachname");
        personModel.setVorname("Vorname");
        personModel.setStrasseHausnummer("Strasse");
        personModel.setPlz("Plzz");
        personModel.setOrt("Ort");
        personModel.setFestnetz("052 555 33 44");
        assertFalse(personModel.isEmpty());
        assertTrue(personModel.isCompleted());
        validate(personModel, null);
    }

    @Test
    public void test_SchuelerWithNameNoAnschriftFestnetz() throws SvmValidationException {
        PersonModel personModel = createSchuelerModel();
        personModel.setNachname("Nachname");
        personModel.setVorname("Vorname");
        personModel.setFestnetz("052 555 33 44");
        assertFalse(personModel.isEmpty());
        assertFalse(personModel.isCompleted());
        validate(personModel, 2001);
    }

    @Test
    public void test_SchuelerWithVornameNoAnschriftFestnetz() throws SvmValidationException {
        PersonModel personModel = createSchuelerModel();
        personModel.setVorname("Vorname");
        personModel.setFestnetz("052 555 33 44");
        assertFalse(personModel.isEmpty());
        assertFalse(personModel.isCompleted());
        validate(personModel, 2000);
    }

    @Test
    public void test_SchuelerWithNameAnschriftNachnameMissing() throws SvmValidationException {
        PersonModel personModel = createSchuelerModel();
        personModel.setVorname("Vorname");
        personModel.setStrasseHausnummer("Strasse");
        personModel.setPlz("Plzz");
        personModel.setOrt("Ort");
        assertFalse(personModel.isEmpty());
        assertFalse(personModel.isCompleted());
        validate(personModel, 2000);
    }

    @Test
    public void test_SchuelerWithNameAnschriftOrtMissing() throws SvmValidationException {
        PersonModel personModel = createSchuelerModel();
        personModel.setNachname("Nachname");
        personModel.setVorname("Vorname");
        personModel.setStrasseHausnummer("Strasse");
        personModel.setPlz("Plzz");
        assertFalse(personModel.isEmpty());
        assertFalse(personModel.isCompleted());
        validate(personModel, 2001);
    }

    //------------------------------------------------------------------------------------------------------------------
    // Angehöriger kein Rechnungsempfänger
    //------------------------------------------------------------------------------------------------------------------

    @Test
    public void test_EmptyAngehoerigerIsRechnungsempfaengerFalse() {
        PersonModel personModel = createAngehoerigerModel();
        assertTrue(personModel.isEmpty());
        assertTrue(personModel.isCompleted());
        validate(personModel, null);
    }

    @Test
    public void test_AngehoerigerWithNachname() throws SvmValidationException {
        PersonModel personModel = createAngehoerigerModel();
        personModel.setNachname("Nachname");
        assertFalse(personModel.isEmpty());
        assertFalse(personModel.isCompleted());
        validate(personModel, 2002);
    }

    @Test
    public void test_AngehoerigerWithVorname() throws SvmValidationException {
        PersonModel personModel = createAngehoerigerModel();
        personModel.setVorname("Vorname");
        assertFalse(personModel.isEmpty());
        assertFalse(personModel.isCompleted());
        validate(personModel, 2002);
    }

    @Test
    public void test_AngehoerigerWithName() throws SvmValidationException {
        PersonModel personModel = createAngehoerigerModel();
        personModel.setNachname("Nachname");
        personModel.setVorname("Vorname");
        assertFalse(personModel.isEmpty());
        assertTrue(personModel.isCompleted());
        validate(personModel, null);
    }

    @Test
    public void test_AngehoerigerWithStrasse() throws SvmValidationException {
        PersonModel personModel = createAngehoerigerModel();
        personModel.setStrasseHausnummer("Strasse");
        assertFalse(personModel.isEmpty());
        assertFalse(personModel.isCompleted());
        validate(personModel, 2003);
    }

    @Test
    public void test_AngehoerigerWithAnschrift() throws SvmValidationException {
        PersonModel personModel = createAngehoerigerModel();
        personModel.setStrasseHausnummer("Strasse");
        personModel.setPlz("Plzz");
        personModel.setOrt("Ort");
        assertFalse(personModel.isEmpty());
        assertFalse(personModel.isCompleted());
        validate(personModel, 2003);
    }

    @Test
    public void test_AngehoerigerWithNameAnschriftComplete() throws SvmValidationException {
        PersonModel personModel = createAngehoerigerModel();
        personModel.setNachname("Nachname");
        personModel.setVorname("Vorname");
        personModel.setStrasseHausnummer("Strasse");
        personModel.setPlz("Plzz");
        personModel.setOrt("Ort");
        assertFalse(personModel.isEmpty());
        assertTrue(personModel.isCompleted());
        validate(personModel, null);
    }

    @Test
    public void test_AngehoerigerWithNameAnschriftFestnetzComplete() throws SvmValidationException {
        PersonModel personModel = createAngehoerigerModel();
        personModel.setNachname("Nachname");
        personModel.setVorname("Vorname");
        personModel.setStrasseHausnummer("Strasse");
        personModel.setPlz("Plzz");
        personModel.setOrt("Ort");
        personModel.setFestnetz("052 555 33 44");
        assertFalse(personModel.isEmpty());
        assertTrue(personModel.isCompleted());
        validate(personModel, null);
    }

    @Test
    public void test_AngehoerigerWithNameNoAnschriftFestnetz() throws SvmValidationException {
        PersonModel personModel = createAngehoerigerModel();
        personModel.setNachname("Nachname");
        personModel.setVorname("Vorname");
        personModel.setFestnetz("052 555 33 44");
        assertFalse(personModel.isEmpty());
        assertTrue(personModel.isCompleted());
        validate(personModel, null);
    }

    @Test
    public void test_AngehoerigerWithVornameNoAnschriftFestnetz() throws SvmValidationException {
        PersonModel personModel = createAngehoerigerModel();
        personModel.setVorname("Vorname");
        personModel.setFestnetz("052 555 33 44");
        assertFalse(personModel.isEmpty());
        assertFalse(personModel.isCompleted());
        validate(personModel, 2002);
    }

    @Test
    public void test_AngehoerigerWithNameAnschriftNachnameMissing() throws SvmValidationException {
        PersonModel personModel = createAngehoerigerModel();
        personModel.setVorname("Vorname");
        personModel.setStrasseHausnummer("Strasse");
        personModel.setPlz("Plzz");
        personModel.setOrt("Ort");
        assertFalse(personModel.isEmpty());
        assertFalse(personModel.isCompleted());
        validate(personModel, 2002);
    }

    @Test
    public void test_AngehoerigerWithNameAnschriftOrtMissing() throws SvmValidationException {
        PersonModel personModel = createAngehoerigerModel();
        personModel.setNachname("Nachname");
        personModel.setVorname("Vorname");
        personModel.setStrasseHausnummer("Strasse");
        personModel.setPlz("Plzz");
        assertFalse(personModel.isEmpty());
        assertFalse(personModel.isCompleted());
        validate(personModel, 2004);
    }

    //------------------------------------------------------------------------------------------------------------------
    // Angehöriger Rechnungsempfänger
    //------------------------------------------------------------------------------------------------------------------

    @Test
    public void test_EmptyAngehoerigerIsRechnungsempfaengerTrue() {
        PersonModel personModel = createAngehoerigerModelRechnungsempfaenger();
        assertTrue(personModel.isEmpty());
        assertFalse(personModel.isCompleted());
        validate(personModel, 2000);
    }

    @Test
    public void test_AngehoerigerRechnungsempfaengerWithNachname() throws SvmValidationException {
        PersonModel personModel = createAngehoerigerModelRechnungsempfaenger();
        personModel.setNachname("Nachname");
        assertFalse(personModel.isEmpty());
        assertFalse(personModel.isCompleted());
        validate(personModel, 2000);
    }

    @Test
    public void test_AngehoerigerRechnungsempfaengerWithVorname() throws SvmValidationException {
        PersonModel personModel = createAngehoerigerModelRechnungsempfaenger();
        personModel.setVorname("Vorname");
        assertFalse(personModel.isEmpty());
        assertFalse(personModel.isCompleted());
        validate(personModel, 2000);
    }

    @Test
    public void test_AngehoerigerRechnungsempfaengerWithName() throws SvmValidationException {
        PersonModel personModel = createAngehoerigerModelRechnungsempfaenger();
        personModel.setNachname("Nachname");
        personModel.setVorname("Vorname");
        assertFalse(personModel.isEmpty());
        assertFalse(personModel.isCompleted());
        validate(personModel, 2001);
    }

    @Test
    public void test_AngehoerigerRechnungsempfaengerWithStrasse() throws SvmValidationException {
        PersonModel personModel = createAngehoerigerModelRechnungsempfaenger();
        personModel.setStrasseHausnummer("Strasse");
        assertFalse(personModel.isEmpty());
        assertFalse(personModel.isCompleted());
        validate(personModel, 2000);
    }

    @Test
    public void test_AngehoerigerRechnungsempfaengerWithAnschrift() throws SvmValidationException {
        PersonModel personModel = createAngehoerigerModelRechnungsempfaenger();
        personModel.setStrasseHausnummer("Strasse");
        personModel.setPlz("Plzz");
        personModel.setOrt("Ort");
        assertFalse(personModel.isEmpty());
        assertFalse(personModel.isCompleted());
        validate(personModel, 2000);
    }

    @Test
    public void test_AngehoerigerRechnungsempfaengerWithNameAnschriftComplete() throws SvmValidationException {
        PersonModel personModel = createAngehoerigerModelRechnungsempfaenger();
        personModel.setNachname("Nachname");
        personModel.setVorname("Vorname");
        personModel.setStrasseHausnummer("Strasse");
        personModel.setPlz("Plzz");
        personModel.setOrt("Ort");
        assertFalse(personModel.isEmpty());
        assertTrue(personModel.isCompleted());
        validate(personModel, null);
    }

    @Test
    public void test_AngehoerigerRechnungsempfaengerWithNameAnschriftFestnetzComplete() throws SvmValidationException {
        PersonModel personModel = createAngehoerigerModelRechnungsempfaenger();
        personModel.setNachname("Nachname");
        personModel.setVorname("Vorname");
        personModel.setStrasseHausnummer("Strasse");
        personModel.setPlz("Plzz");
        personModel.setOrt("Ort");
        personModel.setFestnetz("052 555 33 44");
        assertFalse(personModel.isEmpty());
        assertTrue(personModel.isCompleted());
        validate(personModel, null);
    }

    @Test
    public void test_AngehoerigerRechnungsempfaengerWithNameNoAnschriftFestnetz() throws SvmValidationException {
        PersonModel personModel = createAngehoerigerModelRechnungsempfaenger();
        personModel.setNachname("Nachname");
        personModel.setVorname("Vorname");
        personModel.setFestnetz("052 555 33 44");
        assertFalse(personModel.isEmpty());
        assertFalse(personModel.isCompleted());
        validate(personModel, 2001);
    }

    @Test
    public void test_AngehoerigerRechnungsempfaengerWithVornameNoAnschriftFestnetz() throws SvmValidationException {
        PersonModel personModel = createAngehoerigerModelRechnungsempfaenger();
        personModel.setVorname("Vorname");
        personModel.setFestnetz("052 555 33 44");
        assertFalse(personModel.isEmpty());
        assertFalse(personModel.isCompleted());
        validate(personModel, 2000);
    }

    @Test
    public void test_AngehoerigerRechnungsempfaengerWithNameAnschriftNachnameMissing() throws SvmValidationException {
        PersonModel personModel = createAngehoerigerModelRechnungsempfaenger();
        personModel.setVorname("Vorname");
        personModel.setStrasseHausnummer("Strasse");
        personModel.setPlz("Plzz");
        personModel.setOrt("Ort");
        assertFalse(personModel.isEmpty());
        assertFalse(personModel.isCompleted());
        validate(personModel, 2000);
    }

    @Test
    public void test_AngehoerigerRechnungsempfaengerWithNameAnschriftOrtMissing() throws SvmValidationException {
        PersonModel personModel = createAngehoerigerModelRechnungsempfaenger();
        personModel.setNachname("Nachname");
        personModel.setVorname("Vorname");
        personModel.setStrasseHausnummer("Strasse");
        personModel.setPlz("Plzz");
        assertFalse(personModel.isEmpty());
        assertFalse(personModel.isCompleted());
        validate(personModel, 2001);
    }

    //------------------------------------------------------------------------------------------------------------------

    private SchuelerModel createSchuelerModel() {
        return new SchuelerModelImpl();
    }

    private AngehoerigerModel createAngehoerigerModel() {
        return new AngehoerigerModelImpl();
    }

    private AngehoerigerModel createAngehoerigerModelRechnungsempfaenger() {
        AngehoerigerModel angehoerigerModel = createAngehoerigerModel();
        angehoerigerModel.setIsRechnungsempfaenger(true);
        return angehoerigerModel;
    }

    private void validate(PersonModel personModel, Integer expectedErrorId) {
        try {
            personModel.validate();
            assertNull("Keine SvmValidationException erwartet mit FehlerId " + expectedErrorId, expectedErrorId);
        } catch (SvmValidationException e) {
            e.printStackTrace();
            assertNotNull("SvmValidationException erwartet", expectedErrorId);
            assertEquals("SvmValidationException erwartet mit FehlerId " + expectedErrorId, expectedErrorId, (Integer) e.getErrorId());
        } catch (Throwable e) {
            e.printStackTrace(System.err);
            fail("Unexpected exception: " + e.getMessage());
        }
    }

}
