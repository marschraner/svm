package ch.metzenthin.svm.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import ch.metzenthin.svm.domain.SvmValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.TestCompletedListener;

/**
 * @author Hans Stamm
 */
class AngehoerigerModelImplTest {

  private AngehoerigerModel angehoerigerModel;

  @BeforeEach
  void setUp() {
    angehoerigerModel = new AngehoerigerModelImpl();
  }

  @Test
  void testSetIsRechnungsempfaenger() {
    angehoerigerModel.setIsRechnungsempfaenger(true);
    assertTrue(angehoerigerModel.isRechnungsempfaenger(), "IsRechnungsempfaenger true erwartet");
  }

  @Test
  void testSetIsRechnungsempfaenger_False() {
    angehoerigerModel.setIsRechnungsempfaenger(false);
    assertFalse(angehoerigerModel.isRechnungsempfaenger(), "IsRechnungsempfaenger false erwartet");
  }

  @Test
  void testSetIsRechnungsempfaenger_FalseAgain() {
    angehoerigerModel.setIsRechnungsempfaenger(true);
    angehoerigerModel.setIsRechnungsempfaenger(false);
    assertFalse(angehoerigerModel.isRechnungsempfaenger(), "IsRechnungsempfaenger false erwartet");
  }

  @Test
  void testGetAngehoeriger() {
    assertNotNull(angehoerigerModel.getAngehoeriger(), "Angehoeriger nicht null erwartet");
  }

  @Test
  void testGetAdresse() {
    assertNotNull(angehoerigerModel.getAdresse(), "Adresse nicht null erwartet");
  }

  @Test
  void testIsCompleted() {
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
    assertTrue(angehoerigerModel.isCompleted(), "IsCompleted true erwartet");
    try {
      angehoerigerModel.validate();
    } catch (SvmValidationException e) {
      e.printStackTrace(System.err);
      fail("Keine Exception erwartet");
    }
    assertEquals(
        1, testCompletedListener.getCounter(), "Aufruf von CompletedListener einmal erwartet");
  }

  @Test
  void testIsCompleted_True() {
    assertTrue(angehoerigerModel.isCompleted(), "IsCompleted true erwartet");
  }

  @Test
  void testIsCompleted_False() {
    TestCompletedListener testCompletedListener = new TestCompletedListener();
    angehoerigerModel.addCompletedListener(testCompletedListener);
    try {
      angehoerigerModel.setStrasseHausnummer("StrasseHausnummer");
    } catch (SvmValidationException e) {
      e.printStackTrace(System.err);
      fail("Keine Exception erwartet");
    }
    assertFalse(angehoerigerModel.isCompleted(), "IsCompleted false erwartet");
    try {
      angehoerigerModel.validate();
      fail("Exception erwartet");
    } catch (SvmValidationException e) {
      e.printStackTrace(System.out);
    }
    assertEquals(
        1, testCompletedListener.getCounter(), "Aufruf von CompletedListener einmal erwartet");
  }

  @Test
  void testIsEmpty() {
    assertTrue(angehoerigerModel.isEmpty(), "Empty erwartet");
  }

  @Test
  void testIsEmpty_FalseNachname() throws SvmValidationException {
    angehoerigerModel.setNachname("Nachname");
    assertFalse(angehoerigerModel.isEmpty(), "Not Empty erwartet (Nachname)");
  }

  @Test
  void testIsEmpty_FalseVorname() throws SvmValidationException {
    angehoerigerModel.setVorname("Vorname");
    assertFalse(angehoerigerModel.isEmpty(), "Not Empty erwartet (Vorname)");
  }

  @Test
  void testIsEmpty_FalseStrasse() throws SvmValidationException {
    angehoerigerModel.setStrasseHausnummer("StrasseHausnummer");
    assertFalse(angehoerigerModel.isEmpty(), "Not Empty erwartet (StrasseHausnummer)");
  }

  @Test
  void testIsEmpty_FalseNameAdresse() throws SvmValidationException {
    angehoerigerModel.setNachname("Nachname");
    angehoerigerModel.setVorname("Vorname");
    angehoerigerModel.setStrasseHausnummer("StrasseHausnummer");
    angehoerigerModel.setPlz("Plzz");
    angehoerigerModel.setOrt("Ort");
    assertFalse(angehoerigerModel.isEmpty(), "Not Empty erwartet (Name, Adresse)");
  }
}
