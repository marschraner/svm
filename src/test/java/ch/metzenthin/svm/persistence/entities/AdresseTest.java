package ch.metzenthin.svm.persistence.entities;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Hans Stamm
 */
public class AdresseTest {

    @Test
    public void testGetStrasseHausnummer_ohneHausnummer() throws Exception {
        Adresse adresse = new Adresse("Strasse", null, "8000", "Zürich", "055 444 33 22");
        assertEquals("Strasse", adresse.getStrasseHausnummer());
    }

    @Test
    public void testGetStrasseHausnummer_mitHausnummer() throws Exception {
        Adresse adresse = new Adresse("Strasse", "12", "8000", "Zürich", "055 444 33 22");
        assertEquals("Strasse 12", adresse.getStrasseHausnummer());
    }

    @Test
    public void testIsPartOf() throws Exception {
        Adresse adresse = new Adresse("Strasse", null, "8000", "Zürich", "055 444 33 22");
        Adresse other = new Adresse("Strasse", null, "8000", "Zürich", "055 444 33 22");
        assertTrue(adresse.isPartOf(other));
    }

    @Test
    public void testIsPartOf_StrasseDifferent() throws Exception {
        Adresse adresse = new Adresse("Strasse", null, "8000", "Zürich", "055 444 33 22");
        Adresse other = new Adresse("Andere Strasse", null, "8000", "Zürich", "055 444 33 22");
        assertFalse(adresse.isPartOf(other));
    }

    @Test
    public void testIsPartOf_PlzDifferent() throws Exception {
        Adresse adresse = new Adresse("Strasse", null, "8000", "Zürich", "055 444 33 22");
        Adresse other = new Adresse("Strasse", null, "8010", "Zürich", "055 444 33 22");
        assertFalse(adresse.isPartOf(other));
    }

    @Test
    public void testIsEmpty() throws Exception {
        Adresse adresse = new Adresse();
        assertTrue(adresse.isEmpty());
    }

    @Test
    public void testIsEmpty_False() throws Exception {
        Adresse adresse = new Adresse("Strasse", null, null, null, null);
        assertFalse(adresse.isEmpty());
    }

}