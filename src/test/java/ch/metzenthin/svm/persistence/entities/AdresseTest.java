package ch.metzenthin.svm.persistence.entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * @author Hans Stamm
 */
class AdresseTest {

  @Test
  void testGetStrasseHausnummer_ohneHausnummer() {
    Adresse adresse = new Adresse("Strasse", null, "8000", "Zürich");
    assertEquals("Strasse", adresse.getStrasseHausnummer());
  }

  @Test
  void testGetStrasseHausnummer_mitHausnummer() {
    Adresse adresse = new Adresse("Strasse", "12", "8000", "Zürich");
    assertEquals("Strasse 12", adresse.getStrasseHausnummer());
  }

  @Test
  void testIsPartOf() {
    Adresse adresse = new Adresse("Strasse", null, "8000", "Zürich");
    Adresse other = new Adresse("Strasse", null, "8000", "Zürich");
    assertTrue(adresse.isPartOf(other));
  }

  @Test
  void testIsPartOf_StrasseDifferent() {
    Adresse adresse = new Adresse("Strasse", null, "8000", "Zürich");
    Adresse other = new Adresse("Andere Strasse", null, "8000", "Zürich");
    assertFalse(adresse.isPartOf(other));
  }

  @Test
  void testIsPartOf_PlzDifferent() {
    Adresse adresse = new Adresse("Strasse", null, "8000", "Zürich");
    Adresse other = new Adresse("Strasse", null, "8010", "Zürich");
    assertFalse(adresse.isPartOf(other));
  }

  @Test
  void testIsEmpty() {
    Adresse adresse = new Adresse();
    assertTrue(adresse.isEmpty());
  }

  @Test
  void testIsEmpty_False() {
    Adresse adresse = new Adresse("Strasse", null, null, null);
    assertFalse(adresse.isEmpty());
  }

  @Test
  void testCopyFieldValuesFrom() {
    Adresse to = new Adresse("Strasse", "Hausnummer", "PLZ", "Ort");
    Adresse from = new Adresse("Neue Strasse", "Neue Hausnummer", "Neue PLZ", "Neuer Ort");
    to.copyAttributesFrom(from);
    assertTrue(to.isIdenticalWith(from));
  }
}
