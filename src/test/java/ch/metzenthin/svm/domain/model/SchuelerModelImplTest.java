package ch.metzenthin.svm.domain.model;

import static org.junit.Assert.*;

import ch.metzenthin.svm.common.datatypes.Geschlecht;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.junit.Before;
import org.junit.Test;
import test.TestCompletedListener;
import test.TestPropertyChangeListener;

/**
 * @author Hans Stamm
 */
public class SchuelerModelImplTest {

  private SchuelerModel schuelerModel;

  @Before
  public void setUp() {
    schuelerModel = new SchuelerModelImpl();
  }

  @Test
  public void testSetGeschlecht() throws SvmRequiredException {
    schuelerModel.setGeschlecht(Geschlecht.M);
    assertEquals("Geschlecht.M erwartet", Geschlecht.M, schuelerModel.getGeschlecht());
  }

  @Test(expected = SvmRequiredException.class)
  public void testSetGeschlecht_Null() throws SvmRequiredException {
    schuelerModel.setGeschlecht(null);
  }

  @Test(expected = SvmRequiredException.class)
  public void testSetGeschlecht_NullAgain() throws SvmRequiredException {
    schuelerModel.setGeschlecht(Geschlecht.M);
    schuelerModel.setGeschlecht(null);
  }

  @Test
  public void testSetGeschlecht_PropertyChangeOneEvent() throws SvmRequiredException {
    TestPropertyChangeListener listener = new TestPropertyChangeListener();
    schuelerModel.addPropertyChangeListener(listener);
    schuelerModel.setGeschlecht(Geschlecht.M);
    assertEquals("Ein Event erwartet", 1, listener.eventsSize());
  }

  @Test
  public void testSetGeschlecht_PropertyChangeNoEvent() throws SvmRequiredException {
    schuelerModel.setGeschlecht(Geschlecht.M);
    TestPropertyChangeListener listener = new TestPropertyChangeListener();
    schuelerModel.addPropertyChangeListener(listener);
    schuelerModel.setGeschlecht(Geschlecht.M);
    assertEquals("Kein Event erwartet", 0, listener.eventsSize());
  }

  @Test
  public void testSetAnmeldedatum() {
    try {
      schuelerModel.setAnmeldedatum("12.06.2015");
    } catch (SvmValidationException e) {
      e.printStackTrace(System.err);
      fail("Keine Exception erwartet");
    }
  }

  @Test
  public void testSetAnmeldedatum_BadFormatNoException() {
    try {
      schuelerModel.setAnmeldedatum("12.16.2015");
      fail("Exception erwartet");
    } catch (SvmValidationException e) {
      e.printStackTrace(System.out);
    }
  }

  @Test
  public void testSetAnmeldedatum_BadFormatException() {
    try {
      schuelerModel.setAnmeldedatum("2015-06-12");
      fail("Exception erwartet");
    } catch (SvmValidationException e) {
      e.printStackTrace(System.out);
    }
  }

  @Test
  public void testSetGeburtsdatum() {
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
  public void testSetGeburtsdatum_BadFormatNoException() {
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
  public void testSetGeburtsdatum_BadFormatException() {
    try {
      schuelerModel.setGeburtsdatum("1999-06-12");
      fail("Exception erwartet");
    } catch (SvmValidationException e) {
      e.printStackTrace(System.out);
    }
  }

  @Test
  public void testGetSchueler() {
    assertNotNull("Schueler nicht null erwartet", schuelerModel.getSchueler());
  }

  @Test
  public void testGetAdresse() {
    assertNotNull("Adresse nicht null erwartet", schuelerModel.getAdresse());
  }

  @Test
  public void testIsCompleted() {
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
    assertTrue("IsCompleted true erwartet", schuelerModel.isCompleted());
    try {
      schuelerModel.validate();
    } catch (SvmValidationException e) {
      e.printStackTrace(System.err);
      fail("Keine Exception erwartet");
    }
    assertEquals(
        "Aufruf von CompletedListener einmal erwartet", 1, testCompletedListener.getCounter());
  }

  @Test
  public void testIsCompleted_False() {
    assertFalse("IsCompleted false erwartet", schuelerModel.isCompleted());
  }
}
