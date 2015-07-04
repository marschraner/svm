package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.dataTypes.Anrede;
import ch.metzenthin.svm.dataTypes.Geschlecht;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Schueler;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Hans Stamm
 */
public class SchuelerSuchenResultTest {

    private SchuelerSuchenResult schuelerSuchenResult;

    @Before
    public void setUp() throws Exception {
        List<Schueler> schuelerList = new ArrayList<>();
        Schueler schueler1 = new Schueler("V1", "N1", new GregorianCalendar(2001, Calendar.APRIL, 11), "Natel1", "Email1", Geschlecht.W, null);
        schuelerList.add(schueler1);
        Angehoeriger angehoeriger1 = new Angehoeriger(Anrede.FRAU, "V1A", "N1A", "Natel1A", "Email1A");
        schueler1.setMutter(angehoeriger1);
        schueler1.setRechnungsempfaenger(angehoeriger1);
        Schueler schueler2 = new Schueler("V2", "N2", new GregorianCalendar(2002, Calendar.APRIL, 12), "Natel2", "Email2", Geschlecht.M, null);
        schuelerList.add(schueler2);
        Adresse adresse2 = new Adresse("Strasse2", "99", "8000", "Ort2", "055 444 33 22");
        schueler2.setAdresse(adresse2);
        Angehoeriger angehoeriger2 = new Angehoeriger(Anrede.HERR, "V2A", "N2A", "Natel2A", "Email2A");
        schueler2.setVater(angehoeriger2);
        schueler2.setRechnungsempfaenger(angehoeriger2);
        Schueler schueler3 = new Schueler("V3", "N3", new GregorianCalendar(2003, Calendar.APRIL, 13), "Natel3", "Email3", Geschlecht.W, null);
        schuelerList.add(schueler3);
        Angehoeriger angehoeriger3 = new Angehoeriger(Anrede.FRAU, "V3A", "N3A", "Natel3A", "Email3A");
        schueler3.setRechnungsempfaenger(angehoeriger3);
        schuelerSuchenResult = new SchuelerSuchenResult(schuelerList);
    }

    @Test
    public void testGetColumnCount() throws Exception {
        assertTrue(schuelerSuchenResult.getColumnCount() > 0);
    }

    @Test
    public void testSize() throws Exception {
        assertEquals(3, schuelerSuchenResult.size());
    }

    @Test
    public void testGetValueAt() throws Exception {
        assertEquals("V2", schuelerSuchenResult.getValueAt(1, getColumnIndex("Vorname")));
        assertEquals("N2", schuelerSuchenResult.getValueAt(1, getColumnIndex("Nachname")));
        assertEquals(Geschlecht.M, schuelerSuchenResult.getValueAt(1, getColumnIndex("Geschlecht")));
        assertEquals("12.04.2002", schuelerSuchenResult.getValueAt(1, getColumnIndex("Geburtsdatum")));
        assertEquals("Natel2", schuelerSuchenResult.getValueAt(1, getColumnIndex("Natel")));
        assertEquals("Email2", schuelerSuchenResult.getValueAt(1, getColumnIndex("Email")));
        assertEquals("Strasse2 99", schuelerSuchenResult.getValueAt(1, getColumnIndex("Strasse/Hausnummer")));
        assertEquals("8000", schuelerSuchenResult.getValueAt(1, getColumnIndex("PLZ")));
        assertEquals("Ort2", schuelerSuchenResult.getValueAt(1, getColumnIndex("Ort")));
        assertEquals("055 444 33 22", schuelerSuchenResult.getValueAt(1, getColumnIndex("Festnetz")));
    }

    @Test
    public void testGetValueAt_Mutter() throws Exception {
        assertEquals("V1A N1A", schuelerSuchenResult.getValueAt(0, getColumnIndex("Mutter")));
        assertNull(schuelerSuchenResult.getValueAt(0, getColumnIndex("Vater")));
        assertEquals("V1A N1A", schuelerSuchenResult.getValueAt(0, getColumnIndex("Rechnungsempfänger")));
    }

    @Test
    public void testGetValueAt_Vater() throws Exception {
        assertEquals("V2A N2A", schuelerSuchenResult.getValueAt(1, getColumnIndex("Vater")));
        assertNull(schuelerSuchenResult.getValueAt(1, getColumnIndex("Mutter")));
        assertEquals("V2A N2A", schuelerSuchenResult.getValueAt(1, getColumnIndex("Rechnungsempfänger")));
    }

    @Test
    public void testGetValueAt_Rechnungsempfaenger() throws Exception {
        assertEquals("V3A N3A", schuelerSuchenResult.getValueAt(2, getColumnIndex("Rechnungsempfänger")));
        assertNull(schuelerSuchenResult.getValueAt(2, getColumnIndex("Mutter")));
        assertNull(schuelerSuchenResult.getValueAt(2, getColumnIndex("Vater")));
    }

    @Test
    public void testGetColumnName() throws Exception {
        assertTrue(getColumnIndex("Nachname") > 0);
    }

    @Test
    public void testGetSchuelerDatenblattModel() throws Exception {
        assertNotNull(schuelerSuchenResult.getSchuelerDatenblattModel(2));
    }

    private int getColumnIndex(String column) {
        for (int i = 0; i < schuelerSuchenResult.getColumnCount(); i++) {
            if (column.equals(schuelerSuchenResult.getColumnName(i))) {
                return i;
            }
        }
        fail("Column not found");
        return -1;
    }

}