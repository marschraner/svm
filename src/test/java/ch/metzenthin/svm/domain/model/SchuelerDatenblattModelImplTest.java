package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Anrede;
import ch.metzenthin.svm.common.dataTypes.Geschlecht;
import ch.metzenthin.svm.common.utils.SvmProperties;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Anmeldung;
import ch.metzenthin.svm.persistence.entities.Schueler;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;
import static org.junit.Assert.*;

/**
 * @author Hans Stamm
 */
public class SchuelerDatenblattModelImplTest {

    private SchuelerDatenblattModel schuelerDatenblattModel;
    private boolean neusteZuoberst;

    @Before
    public void setup() {
        createSvmPropertiesFileDefault();
        Properties svmProperties = SvmProperties.getSvmProperties();
        neusteZuoberst = !svmProperties.getProperty(SvmProperties.KEY_NEUSTE_ZUOBERST).equals("false");
        Schueler schueler = new Schueler("Vorname", "Nachname", new GregorianCalendar(2000, Calendar.MAY, 12), "Festnetz", "Natel", "Email", Geschlecht.W, "Bemerkungen");
        schueler.addAnmeldung(new Anmeldung(new GregorianCalendar(2015, Calendar.APRIL, 1), null));
        schueler.addAnmeldung(new Anmeldung(new GregorianCalendar(2011, Calendar.JUNE, 1), new GregorianCalendar(2012, Calendar.DECEMBER, 31)));
        schueler.addAnmeldung(new Anmeldung(new GregorianCalendar(2009, Calendar.NOVEMBER, 11), new GregorianCalendar(2010, Calendar.MARCH, 31)));
        Angehoeriger mutter = new Angehoeriger(Anrede.FRAU, "Vorname Mutter", "Nachname Mutter", "Festnetz Mutter", "Natel Mutter", "Email Mutter");
        schueler.setMutter(mutter);
        schueler.setRechnungsempfaenger(mutter);
        schuelerDatenblattModel = new SchuelerDatenblattModelImpl(schueler);
    }

    @Test
    public void testGetSchuelerNachname() throws Exception {
        assertEquals("Nachname", schuelerDatenblattModel.getSchuelerNachname());
    }

    @Test
    public void testGetSchuelerVorname() throws Exception {
        assertEquals("Vorname", schuelerDatenblattModel.getSchuelerVorname());
    }

    @Test
    public void testGetLabelSchueler() throws Exception {
        assertEquals("Schülerin:", schuelerDatenblattModel.getLabelSchueler());

    }

    @Test
    public void testGetSchuelerAsString() throws Exception {
        assertTrue(schuelerDatenblattModel.getSchuelerAsString().contains("Vorname"));
        assertTrue(schuelerDatenblattModel.getSchuelerAsString().contains("Nachname"));
        assertTrue(schuelerDatenblattModel.getSchuelerAsString().contains("Natel"));
        assertTrue(schuelerDatenblattModel.getSchuelerAsString().contains("Email"));
    }

    @Test
    public void testGetMutterAsString() throws Exception {
        assertEquals("Frau Vorname Mutter Nachname Mutter, Festnetz Mutter, Natel Mutter, Email Mutter", schuelerDatenblattModel.getMutterAsString());
    }

    @Test
    public void testGetVaterAsString() throws Exception {
        assertEquals("-", schuelerDatenblattModel.getVaterAsString());
    }

    @Test
    public void testGetLabelRechnungsempfaenger() throws Exception {
        assertEquals("Rechnungsempfängerin:", schuelerDatenblattModel.getLabelRechnungsempfaenger());
    }

    @Test
    public void testGetRechnungsempfaengerAsString() throws Exception {
        assertEquals("Mutter", schuelerDatenblattModel.getRechnungsempfaengerAsString());
    }

    @Test
    public void testGetGeschwisterAsString() throws Exception {
        assertEquals("-", schuelerDatenblattModel.getGeschwisterAsString());
    }

    @Test
    public void testGetLabelSchuelerGleicherRechnungsempfaenger1() throws Exception {
        assertEquals("Andere Schüler mit gleicher", schuelerDatenblattModel.getLabelSchuelerGleicherRechnungsempfaenger1());
    }

    @Test
    public void testGetLabelSchuelerGleicherRechnungsempfaenger2() throws Exception {
        assertEquals("Rechnungsempfängerin:", schuelerDatenblattModel.getLabelSchuelerGleicherRechnungsempfaenger2());
    }

    @Test
    public void testGetSchuelerGleicherRechnungsempfaengerAsString() throws Exception {
        assertEquals("", schuelerDatenblattModel.getSchuelerGleicherRechnungsempfaengerAsString());
    }

    @Test
    public void testGetSchuelerGeburtsdatumAsString() throws Exception {
        assertEquals("12.05.2000", schuelerDatenblattModel.getSchuelerGeburtsdatumAsString());
    }

    @Test
    public void testGetAnmeldedatumAsString() throws Exception {
        assertEquals("01.04.2015", schuelerDatenblattModel.getAnmeldedatumAsString());
    }

    @Test
    public void testGetAbmeldedatumAsString() throws Exception {
        assertEquals("", schuelerDatenblattModel.getAbmeldedatumAsString());
    }

    @Test
    public void testGetFruehereAnmeldungenAsString() throws Exception {
        String expected = (neusteZuoberst ? "<html>01.06.2011 - 30.12.2012<br>11.11.2009 - 30.03.2010</html>" : "<html>11.11.2009 - 30.03.2010<br>01.06.2011 - 30.12.2012</html>");
        assertEquals(expected, schuelerDatenblattModel.getFruehereAnmeldungenAsString());
    }

}