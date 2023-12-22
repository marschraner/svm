package ch.metzenthin.svm.persistence.entities;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Hans Stamm
 */
public class AdresseTest {

    @Test
    public void testGetStrasseHausnummer_ohneHausnummer() {
        Adresse adresse = new Adresse("Strasse", null, "8000", "Zürich");
        assertEquals("Strasse", adresse.getStrasseHausnummer());
    }

    @Test
    public void testGetStrasseHausnummer_mitHausnummer() {
        Adresse adresse = new Adresse("Strasse", "12", "8000", "Zürich");
        assertEquals("Strasse 12", adresse.getStrasseHausnummer());
    }

    @Test
    public void testIsPartOf() {
        Adresse adresse = new Adresse("Strasse", null, "8000", "Zürich");
        Adresse other = new Adresse("Strasse", null, "8000", "Zürich");
        assertTrue(adresse.isPartOf(other));
    }

    @Test
    public void testIsPartOf_StrasseDifferent() {
        Adresse adresse = new Adresse("Strasse", null, "8000", "Zürich");
        Adresse other = new Adresse("Andere Strasse", null, "8000", "Zürich");
        assertFalse(adresse.isPartOf(other));
    }

    @Test
    public void testIsPartOf_PlzDifferent() {
        Adresse adresse = new Adresse("Strasse", null, "8000", "Zürich");
        Adresse other = new Adresse("Strasse", null, "8010", "Zürich");
        assertFalse(adresse.isPartOf(other));
    }

    @Test
    public void testIsEmpty() {
        Adresse adresse = new Adresse();
        assertTrue(adresse.isEmpty());
    }

    @Test
    public void testIsEmpty_False() {
        Adresse adresse = new Adresse("Strasse", null, null, null);
        assertFalse(adresse.isEmpty());
    }

    @Test
    public void testCopyFieldValuesFrom() {
        Adresse to = new Adresse("Strasse", "Hausnummer", "PLZ", "Ort");
        Adresse from = new Adresse("Neue Strasse", "Neue Hausnummer", "Neue PLZ", "Neuer Ort");
        to.copyAttributesFrom(from);
        assertTrue(to.isIdenticalWith(from));
    }

}