package ch.metzenthin.svm.domain.model;

import static ch.metzenthin.svm.common.utils.Converter.asString;
import static org.junit.Assert.*;

import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.ValidateSchuelerModel;
import ch.metzenthin.svm.persistence.entities.Anmeldung;
import org.junit.Before;
import org.junit.Test;
import test.TestCompletedListener;

/**
 * @author Hans Stamm
 */
public class SchuelerErfassenModelImplTest {

  private SchuelerErfassenModel schuelerErfassenModel;
  private ValidateSchuelerModel validateSchuelerModel;
  private SchuelerModel schuelerModel;
  private AngehoerigerModel mutterModel;

  @Before
  public void setUp() {
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
  public void testIsCompleted_false() {
    TestCompletedListener testCompletedListener = new TestCompletedListener();
    schuelerErfassenModel.addCompletedListener(testCompletedListener);
    schuelerErfassenModel.initializeCompleted();
    assertFalse("IsCompleted false erwartet", schuelerErfassenModel.isCompleted());
    assertEquals("Aufruf CompletedListener 4x erwartet", 4, testCompletedListener.getCounter());
    assertFalse(
        "IsCompleted von CompletedListener false erwartet", testCompletedListener.isCompleted());
  }

  @Test
  public void testIsCompleted_true() {
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
    assertTrue("IsCompleted true erwartet", schuelerErfassenModel.isCompleted());
    assertTrue(
        "IsCompleted von CompletedListener true erwartet", testCompletedListener.isCompleted());
  }

  @Test
  public void testIsRechnungsempfaenger() {
    mutterModel.setIsRechnungsempfaenger(true);
    assertTrue(validateSchuelerModel.isRechnungsempfaengerMutter());
    assertFalse(validateSchuelerModel.isRechnungsempfaengerVater());
    assertFalse(validateSchuelerModel.isRechnungsempfaengerDrittperson());
  }

  @Test
  public void testGetSchueler() {
    assertNotNull(validateSchuelerModel.getSchueler());
  }

  @Test
  public void testGetAdresseSchueler() {
    assertNotNull(validateSchuelerModel.getAdresseSchueler());
  }

  @Test
  public void testGetVater_Null() {
    assertNull(validateSchuelerModel.getVater());
  }

  @Test
  public void testGetAdresseVater() {
    assertNotNull(validateSchuelerModel.getAdresseVater());
  }

  @Test
  public void testGetMutter_Null() {
    assertNull(validateSchuelerModel.getMutter());
  }

  @Test
  public void testGetAdresseMutter() {
    assertNotNull(validateSchuelerModel.getAdresseMutter());
  }

  @Test
  public void testGetMutter_NotNull() throws Exception {
    mutterModel.setNachname("Leu");
    mutterModel.setVorname("Mia");
    assertNotNull(validateSchuelerModel.getMutter());
  }

  @Test
  public void testGetRechnungsempfaengerDrittperson_Null() {
    assertNull(validateSchuelerModel.getRechnungsempfaengerDrittperson());
  }

  @Test
  public void testGetAdresseRechnungsempfaengerDrittperson() {
    assertNotNull(validateSchuelerModel.getAdresseRechnungsempfaengerDrittperson());
  }

  @Test
  public void testGetAnmdeldung() {
    assertNotNull(validateSchuelerModel.getAnmeldung());
  }

  @Test
  public void testGetAnmdeldung_Datum() throws Exception {
    String anmeldedatum = "01.06.2015";
    schuelerModel.setAnmeldedatum(anmeldedatum);
    Anmeldung anmeldung = validateSchuelerModel.getAnmeldung();
    assertEquals(anmeldedatum, asString(anmeldung.getAnmeldedatum()));
  }
}
