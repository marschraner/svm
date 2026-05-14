package ch.metzenthin.svm.domain.model;

import static ch.metzenthin.svm.common.utils.Converter.asString;
import static org.junit.jupiter.api.Assertions.*;

import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.ValidateSchuelerModel;
import ch.metzenthin.svm.persistence.entities.Anmeldung;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.TestCompletedListener;

/**
 * @author Hans Stamm
 */
class SchuelerErfassenModelImplTest {

  private SchuelerErfassenModel schuelerErfassenModel;
  private ValidateSchuelerModel validateSchuelerModel;
  private SchuelerModel schuelerModel;
  private AngehoerigerModel mutterModel;

  @BeforeEach
  void setUp() {
    schuelerErfassenModel = new SchuelerErfassenModelImpl();
    validateSchuelerModel = (ValidateSchuelerModel) schuelerErfassenModel;
    schuelerModel = new SchuelerModelImpl();
    schuelerErfassenModel.setSchuelerModel(schuelerModel);
    mutterModel = new AngehoerigerModelImpl();
    schuelerErfassenModel.setMutterModel(mutterModel);
    AngehoerigerModel vaterModel = new AngehoerigerModelImpl();
    schuelerErfassenModel.setVaterModel(vaterModel);
    AngehoerigerModel drittempfaengerModel = new AngehoerigerModelImpl();
    schuelerErfassenModel.setDrittempfaengerModel(drittempfaengerModel);
  }

  @Test
  void testIsCompleted_false() {
    TestCompletedListener testCompletedListener = new TestCompletedListener();
    schuelerErfassenModel.addCompletedListener(testCompletedListener);
    schuelerErfassenModel.initializeCompleted();
    assertFalse(schuelerErfassenModel.isCompleted(), "IsCompleted false erwartet");
    assertEquals(4, testCompletedListener.getCounter(), "Aufruf CompletedListener 4x erwartet");
    assertFalse(
        testCompletedListener.isCompleted(), "IsCompleted von CompletedListener false erwartet");
  }

  @Test
  void testIsCompleted_true() {
    TestCompletedListener testCompletedListener = new TestCompletedListener();
    schuelerErfassenModel.addCompletedListener(testCompletedListener);
    schuelerErfassenModel.initializeCompleted();
    try {
      schuelerModel.setNachname("Nachname");
      schuelerModel.setVorname("Vorname");
      schuelerModel.setStrasseHausnummer("Strasse");
      schuelerModel.setPlz("Plzz");
      schuelerModel.setOrt("Ort");
      schuelerModel.validate();
      mutterModel.setIsRechnungsempfaenger(true);
      try {
        mutterModel.validate();
        fail("Exception erwarten");
      } catch (SvmValidationException e) {
        e.printStackTrace(System.out);
      }
      mutterModel.setNachname("Nachname");
      mutterModel.setVorname("Vorname");
      mutterModel.setStrasseHausnummer("Strasse");
      mutterModel.setPlz("Plzz");
      mutterModel.setOrt("Ort");
      mutterModel.validate();
    } catch (SvmValidationException e) {
      e.printStackTrace(System.err);
      fail("Keine Exception erwartet");
    }
    assertTrue(schuelerErfassenModel.isCompleted(), "IsCompleted true erwartet");
    assertTrue(
        testCompletedListener.isCompleted(), "IsCompleted von CompletedListener true erwartet");
  }

  @Test
  void testIsRechnungsempfaenger() {
    mutterModel.setIsRechnungsempfaenger(true);
    assertTrue(validateSchuelerModel.isRechnungsempfaengerMutter());
    assertFalse(validateSchuelerModel.isRechnungsempfaengerVater());
    assertFalse(validateSchuelerModel.isRechnungsempfaengerDrittperson());
  }

  @Test
  void testGetSchueler() {
    assertNotNull(validateSchuelerModel.getSchueler());
  }

  @Test
  void testGetAdresseSchueler() {
    assertNotNull(validateSchuelerModel.getAdresseSchueler());
  }

  @Test
  void testGetVater_Null() {
    assertNull(validateSchuelerModel.getVater());
  }

  @Test
  void testGetAdresseVater() {
    assertNotNull(validateSchuelerModel.getAdresseVater());
  }

  @Test
  void testGetMutter_Null() {
    assertNull(validateSchuelerModel.getMutter());
  }

  @Test
  void testGetAdresseMutter() {
    assertNotNull(validateSchuelerModel.getAdresseMutter());
  }

  @Test
  void testGetMutter_NotNull() throws Exception {
    mutterModel.setNachname("Leu");
    mutterModel.setVorname("Mia");
    assertNotNull(validateSchuelerModel.getMutter());
  }

  @Test
  void testGetRechnungsempfaengerDrittperson_Null() {
    assertNull(validateSchuelerModel.getRechnungsempfaengerDrittperson());
  }

  @Test
  void testGetAdresseRechnungsempfaengerDrittperson() {
    assertNotNull(validateSchuelerModel.getAdresseRechnungsempfaengerDrittperson());
  }

  @Test
  void testGetAnmdeldung() {
    assertNotNull(validateSchuelerModel.getAnmeldung());
  }

  @Test
  void testGetAnmdeldung_Datum() throws Exception {
    String anmeldedatum = "01.06.2015";
    schuelerModel.setAnmeldedatum(anmeldedatum);
    Anmeldung anmeldung = validateSchuelerModel.getAnmeldung();
    assertEquals(anmeldedatum, asString(anmeldung.getAnmeldedatum()));
  }
}
