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
        Angehoeriger mutter = new Angehoeriger(Anrede.FRAU, "Vorname Mutter", "Nachname Mutter", "Festnetz Mutter", "Natel Mutter", "Email Mutter", true);
        schueler.setMutter(mutter);
        schueler.setRechnungsempfaenger(mutter);
        schuelerDatenblattModel = new SchuelerDatenblattModelImpl(schueler);
    }

    @Test
    public void testGetSchuelerNachname() {
        assertEquals("Nachname", schuelerDatenblattModel.getSchuelerNachname());
    }

    @Test
    public void testGetSchuelerVorname() {
        assertEquals("Vorname", schuelerDatenblattModel.getSchuelerVorname());
    }

    @Test
    public void testGetLabelSchueler() {
        assertEquals("Schülerin:", schuelerDatenblattModel.getLabelSchueler());

    }

    @Test
    public void testGetSchuelerAsString() {
        assertTrue(schuelerDatenblattModel.getSchuelerAsString().contains("Vorname"));
        assertTrue(schuelerDatenblattModel.getSchuelerAsString().contains("Nachname"));
        assertTrue(schuelerDatenblattModel.getSchuelerAsString().contains("Natel"));
        assertTrue(schuelerDatenblattModel.getSchuelerAsString().contains("Email"));
    }

    @Test
    public void testGetMutterAsString() {
        assertEquals("Frau Vorname Mutter Nachname Mutter, Festnetz Mutter, Natel Mutter, Email Mutter", schuelerDatenblattModel.getMutterAsString());
    }

    @Test
    public void testGetVaterAsString() {
        assertEquals("-", schuelerDatenblattModel.getVaterAsString());
    }

    @Test
    public void testGetLabelRechnungsempfaenger() {
        assertEquals("Rechnungsempfängerin:", schuelerDatenblattModel.getLabelRechnungsempfaenger());
    }

    @Test
    public void testGetRechnungsempfaengerAsString() {
        assertEquals("Mutter", schuelerDatenblattModel.getRechnungsempfaengerAsString());
    }

    @Test
    public void testGetGeschwisterAsString() {
        assertEquals("-", schuelerDatenblattModel.getGeschwisterAsString());
    }

    @Test
    public void testGetLabelSchuelerGleicherRechnungsempfaenger1() {
        assertEquals("Andere Schüler mit gleicher", schuelerDatenblattModel.getLabelSchuelerGleicherRechnungsempfaenger1());
    }

    @Test
    public void testGetLabelSchuelerGleicherRechnungsempfaenger2() {
        assertEquals("Rechnungsempfängerin:", schuelerDatenblattModel.getLabelSchuelerGleicherRechnungsempfaenger2());
    }

    @Test
    public void testGetSchuelerGleicherRechnungsempfaengerAsString() {
        assertEquals("", schuelerDatenblattModel.getSchuelerGleicherRechnungsempfaengerAsString());
    }

    @Test
    public void testGetSchuelerGeburtsdatumAsString() {
        assertEquals("12.05.2000", schuelerDatenblattModel.getSchuelerGeburtsdatumAsString());
    }

    @Test
    public void testGetAnmeldedatumAsString() {
        assertEquals("01.04.2015", schuelerDatenblattModel.getAnmeldedatumAsString());
    }

    @Test
    public void testGetAbmeldedatumAsString() {
        assertEquals("", schuelerDatenblattModel.getAbmeldedatumAsString());
    }

    @Test
    public void testGetFruehereAnmeldungenAsString() {
        String expected = (neusteZuoberst ? "<html>01.06.2011 - 31.12.2012<br>11.11.2009 - 31.03.2010</html>" : "<html>11.11.2009 - 30.03.2010<br>01.06.2011 - 30.12.2012</html>");
        assertEquals(expected, schuelerDatenblattModel.getFruehereAnmeldungenAsString());
    }

}