package ch.metzenthin.svm.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import ch.metzenthin.svm.common.datatypes.Geschlecht;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.TestCompletedListener;
import test.TestPropertyChangeListener;

/**
 * @author Hans Stamm
 */
class SchuelerModelImplTest {

  private SchuelerModel schuelerModel;

  @BeforeEach
  void setUp() {
    schuelerModel = new SchuelerModelImpl();
  }

  @Test
  void testSetGeschlecht() throws SvmRequiredException {
    schuelerModel.setGeschlecht(Geschlecht.M);
    assertEquals(Geschlecht.M, schuelerModel.getGeschlecht(), "Geschlecht.M erwartet");
  }

  @Test
  void testSetGeschlecht_Null() {
    assertThrows(SvmRequiredException.class, () -> schuelerModel.setGeschlecht(null));
  }

  @Test
  void testSetGeschlecht_NullAgain() throws SvmRequiredException {
    schuelerModel.setGeschlecht(Geschlecht.M);
    assertThrows(SvmRequiredException.class, () -> schuelerModel.setGeschlecht(null));
  }

  @Test
  void testSetGeschlecht_PropertyChangeOneEvent() throws SvmRequiredException {
    TestPropertyChangeListener listener = new TestPropertyChangeListener();
    schuelerModel.addPropertyChangeListener(listener);
    schuelerModel.setGeschlecht(Geschlecht.M);
    assertEquals(1, listener.eventsSize(), "Ein Event erwartet");
  }

  @Test
  void testSetGeschlecht_PropertyChangeNoEvent() throws SvmRequiredException {
    schuelerModel.setGeschlecht(Geschlecht.M);
    TestPropertyChangeListener listener = new TestPropertyChangeListener();
    schuelerModel.addPropertyChangeListener(listener);
    schuelerModel.setGeschlecht(Geschlecht.M);
    assertEquals(0, listener.eventsSize(), "Kein Event erwartet");
  }

  @Test
  void testSetAnmeldedatum() {
    try {
      schuelerModel.setAnmeldedatum("12.06.2015");
    } catch (SvmValidationException e) {
      e.printStackTrace(System.err);
      fail("Keine Exception erwartet");
    }
  }

  @Test
  void testSetAnmeldedatum_BadFormatNoException() {
    try {
      schuelerModel.setAnmeldedatum("12.16.2015");
      fail("Exception erwartet");
    } catch (SvmValidationException e) {
      e.printStackTrace(System.out);
    }
  }

  @Test
  void testSetAnmeldedatum_BadFormatException() {
    try {
      schuelerModel.setAnmeldedatum("2015-06-12");
      fail("Exception erwartet");
    } catch (SvmValidationException e) {
      e.printStackTrace(System.out);
    }
  }

  @Test
  void testSetGeburtsdatum() {
    try {
      Calendar cal = new GregorianCalendar();
      cal.add(Calendar.YEAR, -10);
      int year = cal.get(Calendar.YEAR);
      String yearAsString = String.valueOf(year);
      String datum = "12.06." + yearAsString;
      schuelerModel.setGeburtsdatum(datum);
    } catch (SvmValidationException e) {
      e.printStackTrace(System.err);
      fail("Keine Exception erwartet");
    }
  }

  @Test
  void testSetGeburtsdatum_BadFormatNoException() {
    try {
      Calendar cal = new GregorianCalendar();
      cal.add(Calendar.YEAR, -10);
      int year = cal.get(Calendar.YEAR);
      String yearAsString = String.valueOf(year);
      String datum = "24.07." + yearAsString;
      schuelerModel.setGeburtsdatum(datum);
    } catch (SvmValidationException e) {
      e.printStackTrace(System.err);
      fail("Keine Exception erwartet");
    }
  }

  @Test
  void testSetGeburtsdatum_BadFormatException() {
    try {
      schuelerModel.setGeburtsdatum("1999-06-12");
      fail("Exception erwartet");
    } catch (SvmValidationException e) {
      e.printStackTrace(System.out);
    }
  }

  @Test
  void testGetSchueler() {
    assertNotNull(schuelerModel.getSchueler(), "Schueler nicht null erwartet");
  }

  @Test
  void testGetAdresse() {
    assertNotNull(schuelerModel.getAdresse(), "Adresse nicht null erwartet");
  }

  @Test
  void testIsCompleted() {
    TestCompletedListener testCompletedListener = new TestCompletedListener();
    schuelerModel.addCompletedListener(testCompletedListener);
    try {
      schuelerModel.setNachname("Nachname");
      schuelerModel.setVorname("Vorname");
      schuelerModel.setStrasseHausnummer("Strasse");
      schuelerModel.setPlz("Plzz");
      schuelerModel.setOrt("Ort");
    } catch (SvmValidationException e) {
      e.printStackTrace(System.err);
      fail("Keine Exception erwartet");
    }
    assertTrue(schuelerModel.isCompleted(), "IsCompleted true erwartet");
    try {
      schuelerModel.validate();
    } catch (SvmValidationException e) {
      e.printStackTrace(System.err);
      fail("Keine Exception erwartet");
    }
    assertEquals(
        1, testCompletedListener.getCounter(), "Aufruf von CompletedListener einmal erwartet");
  }

  @Test
  void testIsCompleted_False() {
    assertFalse(schuelerModel.isCompleted(), "IsCompleted false erwartet");
  }
}
