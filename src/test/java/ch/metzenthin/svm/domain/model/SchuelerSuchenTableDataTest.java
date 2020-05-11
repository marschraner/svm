package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Anrede;
import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.common.dataTypes.Geschlecht;
import ch.metzenthin.svm.common.dataTypes.Semesterbezeichnung;
import ch.metzenthin.svm.persistence.entities.*;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * @author Hans Stamm
 */
public class SchuelerSuchenTableDataTest {

    private SchuelerSuchenTableData schuelerSuchenTableData;

    @Before
    public void setUp() throws Exception {
        List<Schueler> schuelerList = new ArrayList<>();
        Schueler schueler1 = new Schueler("V1", "N1", new GregorianCalendar(2001, Calendar.APRIL, 11), "Festnetz1", "Natel1", "Email1", Geschlecht.W, null);
        schuelerList.add(schueler1);
        Angehoeriger angehoeriger1 = new Angehoeriger(Anrede.FRAU, "V1A", "N1A", "Festnetz1A", "Natel1A", "Email1A", true);
        schueler1.setMutter(angehoeriger1);
        schueler1.setRechnungsempfaenger(angehoeriger1);
        Schueler schueler2 = new Schueler("V2", "N2", new GregorianCalendar(2002, Calendar.APRIL, 12), "Festnetz2", "Natel2", "Email2", Geschlecht.M, null);
        schuelerList.add(schueler2);
        Adresse adresse2 = new Adresse("Strasse2", "99", "8000", "Ort2");
        schueler2.setAdresse(adresse2);
        Angehoeriger angehoeriger2 = new Angehoeriger(Anrede.HERR, "V2A", "N2A", "Festnetz2A", "Natel2A", "Email2A", false);
        schueler2.setVater(angehoeriger2);
        schueler2.setRechnungsempfaenger(angehoeriger2);
        Schueler schueler3 = new Schueler("V3", "N3", new GregorianCalendar(2003, Calendar.APRIL, 13), "Festnetz3", "Natel3", "Email3", Geschlecht.W, null);
        schuelerList.add(schueler3);
        Angehoeriger angehoeriger3 = new Angehoeriger(Anrede.FRAU, "V3A", "N3A", "Festnetz3A", "Natel3A", "Email3A", true);
        schueler3.setRechnungsempfaenger(angehoeriger3);
        Semester semester = new Semester("2011/2012", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(2011, Calendar.AUGUST, 20), new GregorianCalendar(2012, Calendar.FEBRUARY, 10), new GregorianCalendar(2011, Calendar.OCTOBER, 5), new GregorianCalendar(2011, Calendar.OCTOBER, 17), new GregorianCalendar(2011, Calendar.DECEMBER, 21), new GregorianCalendar(2012, Calendar.JANUARY, 2));
        schuelerSuchenTableData = new SchuelerSuchenTableData(schuelerList, new HashMap<Schueler, List<Kurs>>(), semester, null, null, null, null, null, new HashMap<Schueler, Maercheneinteilung>(), null, null, null, false, false, false, null, false);
    }

    @Test
    public void testGetColumnCount() {
        assertTrue(schuelerSuchenTableData.getColumnCount() > 0);
    }

    @Test
    public void testSize() {
        assertEquals(3, schuelerSuchenTableData.size());
    }

    @Test
    public void testGetValueAt() {
        assertEquals("V2", schuelerSuchenTableData.getValueAt(1, getColumnIndex(Field.VORNAME.toString())));
        assertEquals("N2", schuelerSuchenTableData.getValueAt(1, getColumnIndex(Field.NACHNAME.toString())));
        assertEquals(new GregorianCalendar(2002, Calendar.APRIL, 12), schuelerSuchenTableData.getValueAt(1, getColumnIndex(Field.GEBURTSDATUM_SHORT.toString())));
        assertEquals("Str.2 99", schuelerSuchenTableData.getValueAt(1, getColumnIndex(Field.STRASSE_HAUSNUMMER.toString())));
        assertEquals("8000", schuelerSuchenTableData.getValueAt(1, getColumnIndex(Field.PLZ.toString())));
        assertEquals("Ort2", schuelerSuchenTableData.getValueAt(1, getColumnIndex(Field.ORT.toString())));
    }

    @Test
    public void testGetValueAt_Mutter() {
        assertEquals("N1A V1A", schuelerSuchenTableData.getValueAt(0, getColumnIndex(Field.MUTTER.toString())));
        assertNull(schuelerSuchenTableData.getValueAt(0, getColumnIndex(Field.VATER.toString())));
        assertEquals("Mutter", schuelerSuchenTableData.getValueAt(0, getColumnIndex(Field.RECHNUNGSEMPFAENGER.toString())));
    }

    @Test
    public void testGetValueAt_Vater() {
        assertEquals("N2A V2A", schuelerSuchenTableData.getValueAt(1, getColumnIndex(Field.VATER.toString())));
        assertNull(schuelerSuchenTableData.getValueAt(1, getColumnIndex(Field.MUTTER.toString())));
        assertEquals("Vater", schuelerSuchenTableData.getValueAt(1, getColumnIndex(Field.RECHNUNGSEMPFAENGER.toString())));
    }

    @Test
    public void testGetValueAt_Rechnungsempfaenger() {
        assertEquals("N3A V3A", schuelerSuchenTableData.getValueAt(2, getColumnIndex(Field.RECHNUNGSEMPFAENGER.toString())));
        assertNull(schuelerSuchenTableData.getValueAt(2, getColumnIndex(Field.MUTTER.toString())));
        assertNull(schuelerSuchenTableData.getValueAt(2, getColumnIndex(Field.VATER.toString())));
    }

    @Test
    public void testGetColumnName() {
        assertEquals(1, getColumnIndex(Field.NACHNAME.toString()));
    }

    @Test
    public void testGetSchuelerDatenblattModel() {
        assertNotNull(schuelerSuchenTableData.getSchuelerDatenblattModel(2));
    }

    private int getColumnIndex(String column) {
        for (int i = 0; i < schuelerSuchenTableData.getColumnCount(); i++) {
            if (column.equals(schuelerSuchenTableData.getColumnName(i))) {
                return i;
            }
        }
        fail("Column not found");
        return -1;
    }

}
