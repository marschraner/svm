package ch.metzenthin.svm.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import ch.metzenthin.svm.domain.SvmValidationException;
import org.junit.jupiter.api.Test;

/**
 * @author Hans Stamm
 */
class PersonModelImplTest {

  // ------------------------------------------------------------------------------------------------------------------
  // Schüler
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  void test_EmptySchueler() {
    PersonModel personModel = createSchuelerModel();
    assertTrue(personModel.isEmpty());
    assertFalse(personModel.isCompleted());
    validate(personModel, 2000);
  }

  @Test
  void test_SchuelerWithNachname() throws SvmValidationException {
    PersonModel personModel = createSchuelerModel();
    personModel.setNachname("Nachname");
    assertFalse(personModel.isEmpty());
    assertFalse(personModel.isCompleted());
    validate(personModel, 2000);
  }

  @Test
  void test_SchuelerWithVorname() throws SvmValidationException {
    PersonModel personModel = createSchuelerModel();
    personModel.setVorname("Vorname");
    assertFalse(personModel.isEmpty());
    assertFalse(personModel.isCompleted());
    validate(personModel, 2000);
  }

  @Test
  void test_SchuelerWithName() throws SvmValidationException {
    PersonModel personModel = createSchuelerModel();
    personModel.setNachname("Nachname");
    personModel.setVorname("Vorname");
    assertFalse(personModel.isEmpty());
    assertFalse(personModel.isCompleted());
    validate(personModel, 2001);
  }

  @Test
  void test_SchuelerWithStrasse() throws SvmValidationException {
    PersonModel personModel = createSchuelerModel();
    personModel.setStrasseHausnummer("Strasse");
    assertFalse(personModel.isEmpty());
    assertFalse(personModel.isCompleted());
    validate(personModel, 2000);
  }

  @Test
  void test_SchuelerWithAnschrift() throws SvmValidationException {
    PersonModel personModel = createSchuelerModel();
    personModel.setStrasseHausnummer("Strasse");
    personModel.setPlz("Plzz");
    personModel.setOrt("Ort");
    assertFalse(personModel.isEmpty());
    assertFalse(personModel.isCompleted());
    validate(personModel, 2000);
  }

  @Test
  void test_SchuelerWithNameAnschriftComplete() throws SvmValidationException {
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
  void test_SchuelerWithNameAnschriftFestnetzComplete() throws SvmValidationException {
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
  void test_SchuelerWithNameNoAnschriftFestnetz() throws SvmValidationException {
    PersonModel personModel = createSchuelerModel();
    personModel.setNachname("Nachname");
    personModel.setVorname("Vorname");
    personModel.setFestnetz("052 555 33 44");
    assertFalse(personModel.isEmpty());
    assertFalse(personModel.isCompleted());
    validate(personModel, 2001);
  }

  @Test
  void test_SchuelerWithVornameNoAnschriftFestnetz() throws SvmValidationException {
    PersonModel personModel = createSchuelerModel();
    personModel.setVorname("Vorname");
    personModel.setFestnetz("052 555 33 44");
    assertFalse(personModel.isEmpty());
    assertFalse(personModel.isCompleted());
    validate(personModel, 2000);
  }

  @Test
  void test_SchuelerWithNameAnschriftNachnameMissing() throws SvmValidationException {
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
  void test_SchuelerWithNameAnschriftOrtMissing() throws SvmValidationException {
    PersonModel personModel = createSchuelerModel();
    personModel.setNachname("Nachname");
    personModel.setVorname("Vorname");
    personModel.setStrasseHausnummer("Strasse");
    personModel.setPlz("Plzz");
    assertFalse(personModel.isEmpty());
    assertFalse(personModel.isCompleted());
    validate(personModel, 2001);
  }

  // ------------------------------------------------------------------------------------------------------------------
  // Angehöriger kein Rechnungsempfänger
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  void test_EmptyAngehoerigerIsRechnungsempfaengerFalse() {
    PersonModel personModel = createAngehoerigerModel();
    assertTrue(personModel.isEmpty());
    assertTrue(personModel.isCompleted());
    validate(personModel, null);
  }

  @Test
  void test_AngehoerigerWithNachname() throws SvmValidationException {
    PersonModel personModel = createAngehoerigerModel();
    personModel.setNachname("Nachname");
    assertFalse(personModel.isEmpty());
    assertFalse(personModel.isCompleted());
    validate(personModel, 2002);
  }

  @Test
  void test_AngehoerigerWithVorname() throws SvmValidationException {
    PersonModel personModel = createAngehoerigerModel();
    personModel.setVorname("Vorname");
    assertFalse(personModel.isEmpty());
    assertFalse(personModel.isCompleted());
    validate(personModel, 2002);
  }

  @Test
  void test_AngehoerigerWithName() throws SvmValidationException {
    PersonModel personModel = createAngehoerigerModel();
    personModel.setNachname("Nachname");
    personModel.setVorname("Vorname");
    assertFalse(personModel.isEmpty());
    assertTrue(personModel.isCompleted());
    validate(personModel, null);
  }

  @Test
  void test_AngehoerigerWithStrasse() throws SvmValidationException {
    PersonModel personModel = createAngehoerigerModel();
    personModel.setStrasseHausnummer("Strasse");
    assertFalse(personModel.isEmpty());
    assertFalse(personModel.isCompleted());
    validate(personModel, 2003);
  }

  @Test
  void test_AngehoerigerWithAnschrift() throws SvmValidationException {
    PersonModel personModel = createAngehoerigerModel();
    personModel.setStrasseHausnummer("Strasse");
    personModel.setPlz("Plzz");
    personModel.setOrt("Ort");
    assertFalse(personModel.isEmpty());
    assertFalse(personModel.isCompleted());
    validate(personModel, 2003);
  }

  @Test
  void test_AngehoerigerWithNameAnschriftComplete() throws SvmValidationException {
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
  void test_AngehoerigerWithNameAnschriftFestnetzComplete() throws SvmValidationException {
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
  void test_AngehoerigerWithNameNoAnschriftFestnetz() throws SvmValidationException {
    PersonModel personModel = createAngehoerigerModel();
    personModel.setNachname("Nachname");
    personModel.setVorname("Vorname");
    personModel.setFestnetz("052 555 33 44");
    assertFalse(personModel.isEmpty());
    assertTrue(personModel.isCompleted());
    validate(personModel, null);
  }

  @Test
  void test_AngehoerigerWithVornameNoAnschriftFestnetz() throws SvmValidationException {
    PersonModel personModel = createAngehoerigerModel();
    personModel.setVorname("Vorname");
    personModel.setFestnetz("052 555 33 44");
    assertFalse(personModel.isEmpty());
    assertFalse(personModel.isCompleted());
    validate(personModel, 2002);
  }

  @Test
  void test_AngehoerigerWithNameAnschriftNachnameMissing() throws SvmValidationException {
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
  void test_AngehoerigerWithNameAnschriftOrtMissing() throws SvmValidationException {
    PersonModel personModel = createAngehoerigerModel();
    personModel.setNachname("Nachname");
    personModel.setVorname("Vorname");
    personModel.setStrasseHausnummer("Strasse");
    personModel.setPlz("Plzz");
    assertFalse(personModel.isEmpty());
    assertFalse(personModel.isCompleted());
    validate(personModel, 2004);
  }

  // ------------------------------------------------------------------------------------------------------------------
  // Angehöriger Rechnungsempfänger
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  void test_EmptyAngehoerigerIsRechnungsempfaengerTrue() {
    PersonModel personModel = createAngehoerigerModelRechnungsempfaenger();
    assertTrue(personModel.isEmpty());
    assertFalse(personModel.isCompleted());
    validate(personModel, 2000);
  }

  @Test
  void test_AngehoerigerRechnungsempfaengerWithNachname() throws SvmValidationException {
    PersonModel personModel = createAngehoerigerModelRechnungsempfaenger();
    personModel.setNachname("Nachname");
    assertFalse(personModel.isEmpty());
    assertFalse(personModel.isCompleted());
    validate(personModel, 2000);
  }

  @Test
  void test_AngehoerigerRechnungsempfaengerWithVorname() throws SvmValidationException {
    PersonModel personModel = createAngehoerigerModelRechnungsempfaenger();
    personModel.setVorname("Vorname");
    assertFalse(personModel.isEmpty());
    assertFalse(personModel.isCompleted());
    validate(personModel, 2000);
  }

  @Test
  void test_AngehoerigerRechnungsempfaengerWithName() throws SvmValidationException {
    PersonModel personModel = createAngehoerigerModelRechnungsempfaenger();
    personModel.setNachname("Nachname");
    personModel.setVorname("Vorname");
    assertFalse(personModel.isEmpty());
    assertFalse(personModel.isCompleted());
    validate(personModel, 2001);
  }

  @Test
  void test_AngehoerigerRechnungsempfaengerWithStrasse() throws SvmValidationException {
    PersonModel personModel = createAngehoerigerModelRechnungsempfaenger();
    personModel.setStrasseHausnummer("Strasse");
    assertFalse(personModel.isEmpty());
    assertFalse(personModel.isCompleted());
    validate(personModel, 2000);
  }

  @Test
  void test_AngehoerigerRechnungsempfaengerWithAnschrift() throws SvmValidationException {
    PersonModel personModel = createAngehoerigerModelRechnungsempfaenger();
    personModel.setStrasseHausnummer("Strasse");
    personModel.setPlz("Plzz");
    personModel.setOrt("Ort");
    assertFalse(personModel.isEmpty());
    assertFalse(personModel.isCompleted());
    validate(personModel, 2000);
  }

  @Test
  void test_AngehoerigerRechnungsempfaengerWithNameAnschriftComplete()
      throws SvmValidationException {
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
  void test_AngehoerigerRechnungsempfaengerWithNameAnschriftFestnetzComplete()
      throws SvmValidationException {
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
  void test_AngehoerigerRechnungsempfaengerWithNameNoAnschriftFestnetz()
      throws SvmValidationException {
    PersonModel personModel = createAngehoerigerModelRechnungsempfaenger();
    personModel.setNachname("Nachname");
    personModel.setVorname("Vorname");
    personModel.setFestnetz("052 555 33 44");
    assertFalse(personModel.isEmpty());
    assertFalse(personModel.isCompleted());
    validate(personModel, 2001);
  }

  @Test
  void test_AngehoerigerRechnungsempfaengerWithVornameNoAnschriftFestnetz()
      throws SvmValidationException {
    PersonModel personModel = createAngehoerigerModelRechnungsempfaenger();
    personModel.setVorname("Vorname");
    personModel.setFestnetz("052 555 33 44");
    assertFalse(personModel.isEmpty());
    assertFalse(personModel.isCompleted());
    validate(personModel, 2000);
  }

  @Test
  void test_AngehoerigerRechnungsempfaengerWithNameAnschriftNachnameMissing()
      throws SvmValidationException {
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
  void test_AngehoerigerRechnungsempfaengerWithNameAnschriftOrtMissing()
      throws SvmValidationException {
    PersonModel personModel = createAngehoerigerModelRechnungsempfaenger();
    personModel.setNachname("Nachname");
    personModel.setVorname("Vorname");
    personModel.setStrasseHausnummer("Strasse");
    personModel.setPlz("Plzz");
    assertFalse(personModel.isEmpty());
    assertFalse(personModel.isCompleted());
    validate(personModel, 2001);
  }

  // ------------------------------------------------------------------------------------------------------------------

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
      assertNull(
          expectedErrorId, "Keine SvmValidationException erwartet mit FehlerId " + expectedErrorId);
    } catch (SvmValidationException e) {
      e.printStackTrace(System.out);
      assertNotNull(expectedErrorId, "SvmValidationException erwartet");
      assertEquals(
          expectedErrorId,
          (Integer) e.getErrorId(),
          "SvmValidationException erwartet mit FehlerId " + expectedErrorId);
    } catch (Throwable e) {
      e.printStackTrace(System.err);
      fail("Unexpected exception: " + e.getMessage());
    }
  }
}
