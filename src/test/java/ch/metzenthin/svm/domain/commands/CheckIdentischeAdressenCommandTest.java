package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.dataTypes.Anrede;
import ch.metzenthin.svm.dataTypes.Geschlecht;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Schueler;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;

/**
 * @author Martin Schraner
 */
public class CheckIdentischeAdressenCommandTest {

    private CommandInvoker commandInvoker = new CommandInvokerImpl();
    private Schueler schueler1;
    private Schueler schueler2;
    private Schueler schueler3;
    private Angehoeriger mutter1;
    private Angehoeriger mutter2;
    private Angehoeriger mutter3;
    private Angehoeriger mutter4;
    private Angehoeriger vater1;
    private Angehoeriger vater2;
    private Angehoeriger vater3;
    private Angehoeriger vater4;
    private Angehoeriger rechnungsempfaengerDrittperson1;
    private Angehoeriger rechnungsempfaengerDrittperson2;
    private Angehoeriger rechnungsempfaengerDrittperson3;

    @Before
    public void setUp() {
        schueler1 = new Schueler("Carla", "Müller", new GregorianCalendar(2000, Calendar.MAY, 2), "056 426 69 15", null, null, Geschlecht.W, null);
        Adresse adresseSchueler1 = new Adresse("Wiesenstrasse", "5", "5430", "Wettingen");
        schueler1.setAdresse(adresseSchueler1);
        schueler2 = new Schueler("Carla", "Müller", new GregorianCalendar(2000, Calendar.MAY, 2), "044 724 11 20", null, null, Geschlecht.W, null);
        Adresse adresseSchueler2 = new Adresse("Weidstrasse", "55", "8803", "Rüschlikon");
        schueler2.setAdresse(adresseSchueler2);
        schueler3 = new Schueler("Carla", "Müller", new GregorianCalendar(2000, Calendar.MAY, 2), "044 720 12 15", null, null, Geschlecht.W, null);
        Adresse adresseSchueler3 = new Adresse("Tödistrasse", "2", "8800", "Thalwil");
        schueler3.setAdresse(adresseSchueler3);

        mutter1 = new Angehoeriger(Anrede.FRAU, "Susanne", "Müller", "056 426 69 15", null, null);
        Adresse adresseMutter1 = new Adresse("Wiesenstrasse", "5", "5430", "Wettingen");
        mutter1.setAdresse(adresseMutter1);
        mutter2 = new Angehoeriger(Anrede.FRAU, "Susanne", "Müller", "044 724 11 20", null, null);
        Adresse adresseMutter2 = new Adresse("Weidstrasse", "55", "8803", "Rüschlikon");
        mutter2.setAdresse(adresseMutter2);
        mutter3 = new Angehoeriger(Anrede.FRAU, "Susanne", "Müller", "044 720 12 17", null, null);
        Adresse adresseMutter3 = new Adresse("Tödistrasse", "4", "8800", "Thalwil");
        mutter3.setAdresse(adresseMutter3);
        mutter4 = new Angehoeriger(Anrede.FRAU, "Susanne", "Müller", null, null, null);

        vater1 = new Angehoeriger(Anrede.HERR, "Andreas", "Bruggisser", "056 426 69 15", null, null);
        Adresse adresseVater1 = new Adresse("Wiesenstrasse", "5", "5430", "Wettingen");
        vater1.setAdresse(adresseVater1);
        vater2 = new Angehoeriger(Anrede.HERR, "Andreas", "Bruggisser", "044 724 11 20", null, null);
        Adresse adresseVater2 = new Adresse("Weidstrasse", "55", "8803", "Rüschlikon");
        vater2.setAdresse(adresseVater2);
        vater3 = new Angehoeriger(Anrede.HERR, "Andreas", "Bruggisser", "044 720 12 19", null, null);
        Adresse adresseVater3 = new Adresse("Tödistrasse", "6", "8800", "Thalwil");
        vater3.setAdresse(adresseVater3);
        vater4 = new Angehoeriger(Anrede.HERR, "Andreas", "Bruggisser", null, null, null);

        rechnungsempfaengerDrittperson1 = new Angehoeriger(Anrede.FRAU, "Hanny", "Bruggisser", "056 426 69 15", null, null);
        Adresse adresseRechnungsempfaengerDrittperson1 = new Adresse("Wiesenstrasse", "5", "5430", "Wettingen");
        rechnungsempfaengerDrittperson1.setAdresse(adresseRechnungsempfaengerDrittperson1);
        rechnungsempfaengerDrittperson2 = new Angehoeriger(Anrede.FRAU, "Hanny", "Bruggisser", "044 724 11 20", null, null);
        Adresse adresseRechnungsempfaengerDrittperson2 = new Adresse("Weidstrasse", "55", "8803", "Rüschlikon");
        rechnungsempfaengerDrittperson2.setAdresse(adresseRechnungsempfaengerDrittperson2);
        rechnungsempfaengerDrittperson3 = new Angehoeriger(Anrede.FRAU, "Hanny", "Bruggisser", "044 720 12 21", null, null);
        Adresse adresseRechnungsempfaengerDrittperson3 = new Adresse("Tödistrasse", "8", "8800", "Thalwil");
        rechnungsempfaengerDrittperson3.setAdresse(adresseRechnungsempfaengerDrittperson3);
    }

    // 1.
    @Test
    public void testExecute_SCHUELER_MUTTER_VATER_DRITTPERSON_IDENTISCH() {
        Schueler schueler = schueler1;
        schueler.setMutter(mutter1);
        schueler.setVater(vater1);
        schueler.setRechnungsempfaenger(rechnungsempfaengerDrittperson1);
        CheckIdentischeAdressenCommand checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getIdentischeAdressen().contains("Schülerin, Mutter, Vater und Rechnungsempfänger Drittperson"));
        assertTrue(checkIdentischeAdressenCommand.getAbweichendeAdressen().isEmpty());
        assertEquals(schueler.getMutter().getAdresse(), schueler.getAdresse());
        assertEquals(schueler.getMutter().getAdresse(), schueler.getVater().getAdresse());
        assertEquals(schueler.getMutter().getAdresse(), schueler.getRechnungsempfaenger().getAdresse());
    }

    // 2.
    @Test
    public void testExecute_SCHUELER_MUTTER_VATER_IDENTISCH() {
        // mit Drittperson
        Schueler schueler = schueler1;
        schueler.setMutter(mutter1);
        schueler.setVater(vater1);
        schueler.setRechnungsempfaenger(rechnungsempfaengerDrittperson2);
        CheckIdentischeAdressenCommand checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getIdentischeAdressen().contains("Schülerin, Mutter und Vater"));
        assertTrue(checkIdentischeAdressenCommand.getAbweichendeAdressen().contains("Rechnungsempfänger"));
        assertEquals(schueler.getMutter().getAdresse(), schueler.getAdresse());
        assertEquals(schueler.getMutter().getAdresse(), schueler.getVater().getAdresse());
        assertNotEquals(schueler.getMutter().getAdresse(), schueler.getRechnungsempfaenger().getAdresse());

        // ohne Drittperson
        schueler.setRechnungsempfaenger(vater1);
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getIdentischeAdressen().contains("Schülerin, Mutter und Vater"));
        assertTrue(checkIdentischeAdressenCommand.getAbweichendeAdressen().isEmpty());
        assertEquals(schueler.getMutter().getAdresse(), schueler.getAdresse());
        assertEquals(schueler.getMutter().getAdresse(), schueler.getVater().getAdresse());
    }

    @Test
    public void testExecute_SCHUELER_MUTTER_DRITTPERSON_IDENTISCH() {
        // Vater mit Adresse
        Schueler schueler = schueler1;
        schueler.setMutter(mutter1);
        schueler.setVater(vater2);
        schueler.setRechnungsempfaenger(rechnungsempfaengerDrittperson1);
        CheckIdentischeAdressenCommand checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getIdentischeAdressen().contains("Schülerin, Mutter und Rechnungsempfänger"));
        assertTrue(checkIdentischeAdressenCommand.getAbweichendeAdressen().contains("Vater"));
        assertEquals(schueler.getMutter().getAdresse(), schueler.getAdresse());
        assertNotEquals(schueler.getMutter().getAdresse(), schueler.getVater().getAdresse());
        assertEquals(schueler.getMutter().getAdresse(), schueler.getRechnungsempfaenger().getAdresse());

        // Vater ohne Adresse
        schueler.setVater(vater4);
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getIdentischeAdressen().contains("Schülerin, Mutter und Rechnungsempfänger"));
        assertTrue(checkIdentischeAdressenCommand.getAbweichendeAdressen().isEmpty());
        assertEquals(schueler.getMutter().getAdresse(), schueler.getAdresse());
        assertEquals(schueler.getMutter().getAdresse(), schueler.getRechnungsempfaenger().getAdresse());

        // ohne Vater
        schueler.setVater(null);
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getIdentischeAdressen().contains("Schülerin, Mutter und Rechnungsempfänger"));
        assertTrue(checkIdentischeAdressenCommand.getAbweichendeAdressen().isEmpty());
        assertEquals(schueler.getMutter().getAdresse(), schueler.getAdresse());
        assertEquals(schueler.getMutter().getAdresse(), schueler.getRechnungsempfaenger().getAdresse());
    }

    @Test
    public void testExecute_SCHUELER_VATER_DRITTPERSON_IDENTISCH() {
        // Mutter mit Adresse
        Schueler schueler = schueler1;
        schueler.setMutter(mutter2);
        schueler.setVater(vater1);
        schueler.setRechnungsempfaenger(rechnungsempfaengerDrittperson1);
        CheckIdentischeAdressenCommand checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getIdentischeAdressen().contains("Schülerin, Vater und Rechnungsempfänger"));
        assertTrue(checkIdentischeAdressenCommand.getAbweichendeAdressen().contains("Mutter"));
        assertEquals(schueler.getVater().getAdresse(), schueler.getAdresse());
        assertNotEquals(schueler.getVater().getAdresse(), schueler.getMutter().getAdresse());
        assertEquals(schueler.getVater().getAdresse(), schueler.getRechnungsempfaenger().getAdresse());

        // Mutter ohne Adresse
        schueler.setMutter(mutter4);
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getIdentischeAdressen().contains("Schülerin, Vater und Rechnungsempfänger"));
        assertTrue(checkIdentischeAdressenCommand.getAbweichendeAdressen().isEmpty());
        assertEquals(schueler.getVater().getAdresse(), schueler.getAdresse());
        assertEquals(schueler.getVater().getAdresse(), schueler.getRechnungsempfaenger().getAdresse());

        // ohne Mutter
        schueler.setMutter(null);
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getIdentischeAdressen().contains("Schülerin, Vater und Rechnungsempfänger"));
        assertTrue(checkIdentischeAdressenCommand.getAbweichendeAdressen().isEmpty());
        assertEquals(schueler.getVater().getAdresse(), schueler.getAdresse());
        assertEquals(schueler.getVater().getAdresse(), schueler.getRechnungsempfaenger().getAdresse());
    }

    @Test
    public void testExecute_MUTTER_VATER_DRITTPERSON_IDENTISCH() {
        Schueler schueler = schueler2;
        schueler.setMutter(mutter1);
        schueler.setVater(vater1);
        schueler.setRechnungsempfaenger(rechnungsempfaengerDrittperson1);
        CheckIdentischeAdressenCommand checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getIdentischeAdressen().contains("Mutter, Vater und Rechnungsempfänger"));
        assertTrue(checkIdentischeAdressenCommand.getAbweichendeAdressen().contains("Schüler"));
        assertNotEquals(schueler.getMutter().getAdresse(), schueler.getAdresse());
        assertEquals(schueler.getMutter().getAdresse(), schueler.getVater().getAdresse());
        assertEquals(schueler.getMutter().getAdresse(), schueler.getRechnungsempfaenger().getAdresse());
    }

    // 3.a
    @Test
    public void testExecute_SCHUELER_MUTTER_IDENTISCH_VATER_DRITTPERSON_IDENTISCH() {
        Schueler schueler = schueler1;
        schueler.setMutter(mutter1);
        schueler.setVater(vater2);
        schueler.setRechnungsempfaenger(rechnungsempfaengerDrittperson2);
        CheckIdentischeAdressenCommand checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getIdentischeAdressen().contains("Schülerin und Mutter"));
        assertTrue(checkIdentischeAdressenCommand.getIdentischeAdressen().contains("Vater und Rechnungsempfänger"));
        assertTrue(checkIdentischeAdressenCommand.getAbweichendeAdressen().isEmpty());
        assertEquals(schueler.getMutter().getAdresse(), schueler.getAdresse());
        assertNotEquals(schueler.getMutter().getAdresse(), schueler.getVater().getAdresse());
        assertEquals(schueler.getVater().getAdresse(), schueler.getRechnungsempfaenger().getAdresse());
    }

    @Test
    public void testExecute_SCHUELER_VATER_IDENTISCH_MUTTER_DRITTPERSON_IDENTISCH() {
        Schueler schueler = schueler1;
        schueler.setMutter(mutter2);
        schueler.setVater(vater1);
        schueler.setRechnungsempfaenger(rechnungsempfaengerDrittperson2);
        CheckIdentischeAdressenCommand checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getIdentischeAdressen().contains("Schülerin und Vater"));
        assertTrue(checkIdentischeAdressenCommand.getIdentischeAdressen().contains("Mutter und Rechnungsempfänger"));
        assertTrue(checkIdentischeAdressenCommand.getAbweichendeAdressen().isEmpty());
        assertEquals(schueler.getVater().getAdresse(), schueler.getAdresse());
        assertNotEquals(schueler.getMutter().getAdresse(), schueler.getAdresse());
        assertEquals(schueler.getMutter().getAdresse(), schueler.getRechnungsempfaenger().getAdresse());
    }

    @Test
    public void testExecute_SCHUELER_DRITTPERSON_IDENTISCH_MUTTER_VATER_IDENTISCH() {
        Schueler schueler = schueler1;
        schueler.setMutter(mutter2);
        schueler.setVater(vater2);
        schueler.setRechnungsempfaenger(rechnungsempfaengerDrittperson1);
        CheckIdentischeAdressenCommand checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getIdentischeAdressen().contains("Schülerin und Rechnungsempfänger"));
        assertTrue(checkIdentischeAdressenCommand.getIdentischeAdressen().contains("Mutter und Vater"));
        assertTrue(checkIdentischeAdressenCommand.getAbweichendeAdressen().isEmpty());
        assertEquals(schueler.getRechnungsempfaenger().getAdresse(), schueler.getAdresse());
        assertNotEquals(schueler.getMutter().getAdresse(), schueler.getAdresse());
        assertEquals(schueler.getMutter().getAdresse(), schueler.getVater().getAdresse());
    }

    // 3.b
    @Test
    public void testExecute_SCHUELER_MUTTER_IDENTISCH() {
        // mit Drittperson, Vater mit Adresse
        Schueler schueler = schueler1;
        schueler.setMutter(mutter1);
        schueler.setVater(vater2);
        schueler.setRechnungsempfaenger(rechnungsempfaengerDrittperson3);
        CheckIdentischeAdressenCommand checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getIdentischeAdressen().contains("Schülerin und Mutter"));
        assertTrue(checkIdentischeAdressenCommand.getAbweichendeAdressen().contains("Vater und Rechnungsempfänger"));
        assertEquals(schueler.getMutter().getAdresse(), schueler.getAdresse());
        assertNotEquals(schueler.getMutter().getAdresse(), schueler.getVater().getAdresse());
        assertNotEquals(schueler.getMutter().getAdresse(), schueler.getRechnungsempfaenger().getAdresse());
        assertNotEquals(vater2.getAdresse(), schueler.getRechnungsempfaenger().getAdresse());

        // mit Drittperson, Vater ohne Adresse
        schueler.setVater(vater4);
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getIdentischeAdressen().contains("Schülerin und Mutter"));
        assertTrue(checkIdentischeAdressenCommand.getAbweichendeAdressen().contains("Rechnungsempfänger"));
        assertEquals(schueler.getMutter().getAdresse(), schueler.getAdresse());
        assertNotEquals(schueler.getMutter().getAdresse(), schueler.getRechnungsempfaenger().getAdresse());

        // mit Drittperson, ohne Vater
        schueler.setVater(null);
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getIdentischeAdressen().contains("Schülerin und Mutter"));
        assertTrue(checkIdentischeAdressenCommand.getAbweichendeAdressen().contains("Rechnungsempfänger"));
        assertEquals(schueler.getMutter().getAdresse(), schueler.getAdresse());
        assertNotEquals(schueler.getMutter().getAdresse(), schueler.getRechnungsempfaenger().getAdresse());

        // ohne Drittperson, Vater mit Adresse
        schueler.setRechnungsempfaenger(mutter1);
        schueler.setVater(vater2);
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getIdentischeAdressen().contains("Schülerin und Mutter"));
        assertTrue(checkIdentischeAdressenCommand.getAbweichendeAdressen().contains("Vater"));
        assertEquals(schueler.getMutter().getAdresse(), schueler.getAdresse());
        assertNotEquals(schueler.getMutter().getAdresse(), schueler.getVater().getAdresse());

        // ohne Drittperson, Vater ohne Adresse
        schueler.setVater(vater4);
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getIdentischeAdressen().contains("Schülerin und Mutter"));
        assertTrue(checkIdentischeAdressenCommand.getAbweichendeAdressen().isEmpty());
        assertEquals(schueler.getMutter().getAdresse(), schueler.getAdresse());

        // ohne Drittperson, ohne Vater
        schueler.setVater(null);
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getIdentischeAdressen().contains("Schülerin und Mutter"));
        assertTrue(checkIdentischeAdressenCommand.getAbweichendeAdressen().isEmpty());
        assertEquals(schueler.getMutter().getAdresse(), schueler.getAdresse());
    }

    @Test
    public void testExecute_SCHUELER_VATER_IDENTISCH() {
        // mit Drittperson, Mutter mit Adresse
        Schueler schueler = schueler1;
        schueler.setMutter(mutter2);
        schueler.setVater(vater1);
        schueler.setRechnungsempfaenger(rechnungsempfaengerDrittperson3);
        CheckIdentischeAdressenCommand checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getIdentischeAdressen().contains("Schülerin und Vater"));
        assertTrue(checkIdentischeAdressenCommand.getAbweichendeAdressen().contains("Mutter und Rechnungsempfänger"));
        assertEquals(schueler.getVater().getAdresse(), schueler.getAdresse());
        assertNotEquals(schueler.getVater().getAdresse(), schueler.getMutter().getAdresse());
        assertNotEquals(schueler.getVater().getAdresse(), schueler.getRechnungsempfaenger().getAdresse());
        assertNotEquals(schueler.getMutter().getAdresse(), schueler.getRechnungsempfaenger().getAdresse());

        // mit Drittperson, Mutter ohne Adresse
        schueler.setMutter(mutter4);
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getIdentischeAdressen().contains("Schülerin und Vater"));
        assertTrue(checkIdentischeAdressenCommand.getAbweichendeAdressen().contains("Rechnungsempfänger"));
        assertEquals(schueler.getVater().getAdresse(), schueler.getAdresse());
        assertNotEquals(schueler.getVater().getAdresse(), schueler.getRechnungsempfaenger().getAdresse());

        // mit Drittperson, ohne Mutter
        schueler.setMutter(null);
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getIdentischeAdressen().contains("Schülerin und Vater"));
        assertTrue(checkIdentischeAdressenCommand.getAbweichendeAdressen().contains("Rechnungsempfänger"));
        assertEquals(schueler.getVater().getAdresse(), schueler.getAdresse());
        assertNotEquals(schueler.getVater().getAdresse(), schueler.getRechnungsempfaenger().getAdresse());

        // ohne Drittperson, Mutter mit Adresse
        schueler.setMutter(mutter2);
        schueler.setRechnungsempfaenger(vater1);
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getIdentischeAdressen().contains("Schülerin und Vater"));
        assertTrue(checkIdentischeAdressenCommand.getAbweichendeAdressen().contains("Mutter"));
        assertEquals(schueler.getVater().getAdresse(), schueler.getAdresse());
        assertNotEquals(schueler.getVater().getAdresse(), schueler.getMutter().getAdresse());

        // ohne Drittperson, Mutter ohne Adresse
        schueler.setMutter(mutter4);
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getIdentischeAdressen().contains("Schülerin und Vater"));
        assertTrue(checkIdentischeAdressenCommand.getAbweichendeAdressen().isEmpty());
        assertEquals(schueler.getVater().getAdresse(), schueler.getAdresse());

        // ohne Drittperson, ohne Mutter
        schueler.setMutter(null);
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getIdentischeAdressen().contains("Schülerin und Vater"));
        assertTrue(checkIdentischeAdressenCommand.getAbweichendeAdressen().isEmpty());
        assertEquals(schueler.getVater().getAdresse(), schueler.getAdresse());
    }

    @Test
    public void testExecute_SCHUELER_DRITTPERSON_IDENTISCH() {
        // Mutter und Vater mit Adresse
        Schueler schueler = schueler1;
        schueler.setMutter(mutter2);
        schueler.setVater(vater3);
        schueler.setRechnungsempfaenger(rechnungsempfaengerDrittperson1);
        CheckIdentischeAdressenCommand checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getIdentischeAdressen().contains("Schülerin und Rechnungsempfänger"));
        assertTrue(checkIdentischeAdressenCommand.getAbweichendeAdressen().contains("Mutter und Vater"));
        assertEquals(schueler.getRechnungsempfaenger().getAdresse(), schueler.getAdresse());
        assertNotEquals(schueler.getAdresse(), schueler.getMutter().getAdresse());
        assertNotEquals(schueler.getAdresse(), schueler.getVater().getAdresse());
        assertNotEquals(schueler.getMutter().getAdresse(), schueler.getVater().getAdresse());

        // Mutter mit Adresse, Vater ohne Adresse
        schueler.setVater(vater4);
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getIdentischeAdressen().contains("Schülerin und Rechnungsempfänger"));
        assertTrue(checkIdentischeAdressenCommand.getAbweichendeAdressen().contains("Mutter"));
        assertEquals(schueler.getRechnungsempfaenger().getAdresse(), schueler.getAdresse());
        assertNotEquals(schueler.getAdresse(), schueler.getMutter().getAdresse());

        // Mutter mit Adresse, ohne Vater
        schueler.setVater(null);
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getIdentischeAdressen().contains("Schülerin und Rechnungsempfänger"));
        assertTrue(checkIdentischeAdressenCommand.getAbweichendeAdressen().contains("Mutter"));
        assertEquals(schueler.getRechnungsempfaenger().getAdresse(), schueler.getAdresse());
        assertNotEquals(schueler.getAdresse(), schueler.getMutter().getAdresse());

        // Mutter ohne Adresse, Vater mit Adresse
        schueler.setMutter(mutter4);
        schueler.setVater(vater3);
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getIdentischeAdressen().contains("Schülerin und Rechnungsempfänger"));
        assertTrue(checkIdentischeAdressenCommand.getAbweichendeAdressen().contains("Vater"));
        assertEquals(schueler.getRechnungsempfaenger().getAdresse(), schueler.getAdresse());
        assertNotEquals(schueler.getAdresse(), schueler.getVater().getAdresse());

        // Mutter und Vater ohne Adresse
        schueler.setVater(vater4);
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getIdentischeAdressen().contains("Schülerin und Rechnungsempfänger"));
        assertTrue(checkIdentischeAdressenCommand.getAbweichendeAdressen().isEmpty());
        assertEquals(schueler.getRechnungsempfaenger().getAdresse(), schueler.getAdresse());

        // Mutter ohne Adresse, ohne Vater
        schueler.setVater(null);
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getIdentischeAdressen().contains("Schülerin und Rechnungsempfänger"));
        assertTrue(checkIdentischeAdressenCommand.getAbweichendeAdressen().isEmpty());
        assertEquals(schueler.getRechnungsempfaenger().getAdresse(), schueler.getAdresse());

        // ohne Mutter, Vater mit Adresse
        schueler.setMutter(null);
        schueler.setVater(vater3);
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getIdentischeAdressen().contains("Schülerin und Rechnungsempfänger"));
        assertTrue(checkIdentischeAdressenCommand.getAbweichendeAdressen().contains("Vater"));
        assertEquals(schueler.getRechnungsempfaenger().getAdresse(), schueler.getAdresse());
        assertNotEquals(schueler.getAdresse(), schueler.getVater().getAdresse());

        // ohne Mutter, Vater ohne Adresse
        schueler.setVater(vater4);
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getIdentischeAdressen().contains("Schülerin und Rechnungsempfänger"));
        assertTrue(checkIdentischeAdressenCommand.getAbweichendeAdressen().isEmpty());
        assertEquals(schueler.getRechnungsempfaenger().getAdresse(), schueler.getAdresse());

        // ohne Mutter, ohne Vater
        schueler.setVater(null);
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getIdentischeAdressen().contains("Schülerin und Rechnungsempfänger"));
        assertTrue(checkIdentischeAdressenCommand.getAbweichendeAdressen().isEmpty());
        assertEquals(schueler.getRechnungsempfaenger().getAdresse(), schueler.getAdresse());
    }

    @Test
    public void testExecute_MUTTER_VATER_IDENTISCH() {
        // mit Drittperson
        Schueler schueler = schueler1;
        schueler.setMutter(mutter2);
        schueler.setVater(vater2);
        schueler.setRechnungsempfaenger(rechnungsempfaengerDrittperson3);
        CheckIdentischeAdressenCommand checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getIdentischeAdressen().contains("Mutter und Vater"));
        assertTrue(checkIdentischeAdressenCommand.getAbweichendeAdressen().contains("Schülerin und Rechnungsempfänger"));
        assertNotEquals(schueler.getMutter().getAdresse(), schueler.getAdresse());
        assertEquals(schueler.getMutter().getAdresse(), schueler.getVater().getAdresse());
        assertNotEquals(schueler.getAdresse(), schueler.getRechnungsempfaenger().getAdresse());
        assertNotEquals(schueler.getMutter().getAdresse(), schueler.getRechnungsempfaenger().getAdresse());

        // ohne Drittperson
        schueler.setRechnungsempfaenger(mutter2);
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getIdentischeAdressen().contains("Mutter und Vater"));
        assertTrue(checkIdentischeAdressenCommand.getAbweichendeAdressen().contains("Schüler"));
        assertNotEquals(schueler.getMutter().getAdresse(), schueler.getAdresse());
        assertEquals(schueler.getMutter().getAdresse(), schueler.getVater().getAdresse());
    }

    @Test
    public void testExecute_MUTTER_DRITTPERSON_IDENTISCH() {
        // Vater mit Adresse
        Schueler schueler = schueler1;
        schueler.setMutter(mutter2);
        schueler.setVater(vater3);
        schueler.setRechnungsempfaenger(rechnungsempfaengerDrittperson2);
        CheckIdentischeAdressenCommand checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getIdentischeAdressen().contains("Mutter und Rechnungsempfänger"));
        assertTrue(checkIdentischeAdressenCommand.getAbweichendeAdressen().contains("Schülerin und Vater"));
        assertNotEquals(schueler.getMutter().getAdresse(), schueler.getAdresse());
        assertNotEquals(schueler.getAdresse(), schueler.getVater().getAdresse());
        assertNotEquals(schueler.getMutter().getAdresse(), schueler.getVater().getAdresse());
        assertEquals(schueler.getMutter().getAdresse(), schueler.getRechnungsempfaenger().getAdresse());

        // Vater ohne Adresse
        schueler.setVater(vater4);
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getIdentischeAdressen().contains("Mutter und Rechnungsempfänger"));
        assertTrue(checkIdentischeAdressenCommand.getAbweichendeAdressen().contains("Schüler"));
        assertNotEquals(schueler.getMutter().getAdresse(), schueler.getAdresse());
        assertEquals(schueler.getMutter().getAdresse(), schueler.getRechnungsempfaenger().getAdresse());

        // ohne Vater
        schueler.setVater(null);
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getIdentischeAdressen().contains("Mutter und Rechnungsempfänger"));
        assertTrue(checkIdentischeAdressenCommand.getAbweichendeAdressen().contains("Schüler"));
        assertNotEquals(schueler.getMutter().getAdresse(), schueler.getAdresse());
        assertEquals(schueler.getMutter().getAdresse(), schueler.getRechnungsempfaenger().getAdresse());
    }

    @Test
    public void testExecute_VATER_DRITTPERSON_IDENTISCH() {
        // Mutter mit Adresse
        Schueler schueler = schueler1;
        schueler.setMutter(mutter3);
        schueler.setVater(vater2);
        schueler.setRechnungsempfaenger(rechnungsempfaengerDrittperson2);
        CheckIdentischeAdressenCommand checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getIdentischeAdressen().contains("Vater und Rechnungsempfänger"));
        assertTrue(checkIdentischeAdressenCommand.getAbweichendeAdressen().contains("Schülerin und Mutter"));
        assertNotEquals(schueler.getAdresse(), schueler.getMutter().getAdresse());
        assertNotEquals(schueler.getAdresse(), schueler.getVater().getAdresse());
        assertNotEquals(schueler.getMutter().getAdresse(), schueler.getVater().getAdresse());
        assertEquals(schueler.getVater().getAdresse(), schueler.getRechnungsempfaenger().getAdresse());

        // Mutter ohne Adresse
        schueler.setMutter(mutter4);
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getIdentischeAdressen().contains("Vater und Rechnungsempfänger"));
        assertTrue(checkIdentischeAdressenCommand.getAbweichendeAdressen().contains("Schüler"));
        assertNotEquals(schueler.getAdresse(), schueler.getVater().getAdresse());
        assertEquals(schueler.getVater().getAdresse(), schueler.getRechnungsempfaenger().getAdresse());

        // ohne Mutter
        schueler.setMutter(null);
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getIdentischeAdressen().contains("Vater und Rechnungsempfänger"));
        assertTrue(checkIdentischeAdressenCommand.getAbweichendeAdressen().contains("Schüler"));
        assertNotEquals(schueler.getAdresse(), schueler.getVater().getAdresse());
        assertEquals(schueler.getVater().getAdresse(), schueler.getRechnungsempfaenger().getAdresse());
    }

    // 4.
    @Test
    public void testExecute_SCHUELER_MUTTER_VATER_DRITTPERSON_VERSCHIEDEN() {
        Schueler schueler = schueler3;
        schueler.setMutter(mutter3);
        schueler.setVater(vater3);
        schueler.setRechnungsempfaenger(rechnungsempfaengerDrittperson3);
        CheckIdentischeAdressenCommand checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getIdentischeAdressen().isEmpty());
        assertTrue(checkIdentischeAdressenCommand.getAbweichendeAdressen().contains("Schülerin, Mutter, Vater und Rechnungsempfänger"));
        assertNotEquals(schueler.getAdresse(), schueler.getMutter().getAdresse());
        assertNotEquals(schueler.getAdresse(), schueler.getVater().getAdresse());
        assertNotEquals(schueler.getMutter().getAdresse(), schueler.getVater().getAdresse());
        assertNotEquals(schueler.getAdresse(), schueler.getRechnungsempfaenger().getAdresse());
        assertNotEquals(schueler.getMutter().getAdresse(), schueler.getRechnungsempfaenger().getAdresse());
        assertNotEquals(schueler.getVater().getAdresse(), schueler.getRechnungsempfaenger().getAdresse());
    }

    @Test
    public void testExecute_SCHUELER_MUTTER_DRITTPERSON_VERSCHIEDEN() {
        // Vater ohne Adresse
        Schueler schueler = schueler3;
        schueler.setMutter(mutter3);
        schueler.setVater(vater4);
        schueler.setRechnungsempfaenger(rechnungsempfaengerDrittperson3);
        CheckIdentischeAdressenCommand checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getIdentischeAdressen().isEmpty());
        assertTrue(checkIdentischeAdressenCommand.getAbweichendeAdressen().contains("Schülerin, Mutter und Rechnungsempfänger"));
        assertNotEquals(schueler.getAdresse(), schueler.getMutter().getAdresse());
        assertNotEquals(schueler.getAdresse(), schueler.getRechnungsempfaenger().getAdresse());
        assertNotEquals(schueler.getMutter().getAdresse(), schueler.getRechnungsempfaenger().getAdresse());

        // ohne Vater
        schueler.setVater(null);
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getIdentischeAdressen().isEmpty());
        assertTrue(checkIdentischeAdressenCommand.getAbweichendeAdressen().contains("Schülerin, Mutter und Rechnungsempfänger"));
        assertNotEquals(schueler.getAdresse(), schueler.getMutter().getAdresse());
        assertNotEquals(schueler.getAdresse(), schueler.getRechnungsempfaenger().getAdresse());
        assertNotEquals(schueler.getMutter().getAdresse(), schueler.getRechnungsempfaenger().getAdresse());
    }

    @Test
    public void testExecute_SCHUELER_VATER_DRITTPERSON_VERSCHIEDEN() {
        // Mutter ohne Adresse
        Schueler schueler = schueler3;
        schueler.setMutter(mutter4);
        schueler.setVater(vater3);
        schueler.setRechnungsempfaenger(rechnungsempfaengerDrittperson3);
        CheckIdentischeAdressenCommand checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getIdentischeAdressen().isEmpty());
        assertTrue(checkIdentischeAdressenCommand.getAbweichendeAdressen().contains("Schülerin, Vater und Rechnungsempfänger"));
        assertNotEquals(schueler.getAdresse(), schueler.getVater().getAdresse());
        assertNotEquals(schueler.getAdresse(), schueler.getRechnungsempfaenger().getAdresse());
        assertNotEquals(schueler.getVater().getAdresse(), schueler.getRechnungsempfaenger().getAdresse());

        // ohne Mutter
        schueler.setMutter(null);
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getIdentischeAdressen().isEmpty());
        assertTrue(checkIdentischeAdressenCommand.getAbweichendeAdressen().contains("Schülerin, Vater und Rechnungsempfänger"));
    }

    @Test
    public void testExecute_SCHUELER_DRITTPERSON_VERSCHIEDEN() {
        // Vater und Mutter ohne Adresse
        Schueler schueler = schueler3;
        schueler.setMutter(mutter4);
        schueler.setVater(vater4);
        schueler.setRechnungsempfaenger(rechnungsempfaengerDrittperson3);
        CheckIdentischeAdressenCommand checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getIdentischeAdressen().isEmpty());
        assertTrue(checkIdentischeAdressenCommand.getAbweichendeAdressen().contains("Schülerin und Rechnungsempfänger"));
        assertNotEquals(schueler.getAdresse(), schueler.getRechnungsempfaenger().getAdresse());

        // Mutter ohne Adresse, ohne Vater
        schueler.setVater(null);
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getIdentischeAdressen().isEmpty());
        assertTrue(checkIdentischeAdressenCommand.getAbweichendeAdressen().contains("Schülerin und Rechnungsempfänger"));
        assertNotEquals(schueler.getAdresse(), schueler.getRechnungsempfaenger().getAdresse());

        // ohne Mutter, Vater ohne Adresse
        schueler.setMutter(null);
        schueler.setVater(vater4);
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getIdentischeAdressen().isEmpty());
        assertTrue(checkIdentischeAdressenCommand.getAbweichendeAdressen().contains("Schülerin und Rechnungsempfänger"));
        assertNotEquals(schueler.getAdresse(), schueler.getRechnungsempfaenger().getAdresse());

        // ohne Mutter und ohne Vater
        schueler.setVater(null);
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getIdentischeAdressen().isEmpty());
        assertTrue(checkIdentischeAdressenCommand.getAbweichendeAdressen().contains("Schülerin und Rechnungsempfänger"));
        assertNotEquals(schueler.getAdresse(), schueler.getRechnungsempfaenger().getAdresse());
    }

    @Test
    public void testExecute_SCHUELER_MUTTER_VATER_VERSCHIEDEN() {
        Schueler schueler = schueler3;
        schueler.setMutter(mutter3);
        schueler.setVater(vater3);
        schueler.setRechnungsempfaenger(mutter3);
        CheckIdentischeAdressenCommand checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getIdentischeAdressen().isEmpty());
        assertTrue(checkIdentischeAdressenCommand.getAbweichendeAdressen().contains("Schülerin, Mutter und Vater"));
        assertNotEquals(schueler.getAdresse(), schueler.getMutter().getAdresse());
        assertNotEquals(schueler.getAdresse(), schueler.getVater().getAdresse());
        assertNotEquals(schueler.getMutter().getAdresse(), schueler.getVater().getAdresse());
    }

    @Test
    public void testExecute_SCHUELER_MUTTER_VERSCHIEDEN() {
        // Vater ohne Adresse
        Schueler schueler = schueler3;
        schueler.setMutter(mutter3);
        schueler.setVater(vater4);
        schueler.setRechnungsempfaenger(mutter3);
        CheckIdentischeAdressenCommand checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getIdentischeAdressen().isEmpty());
        assertTrue(checkIdentischeAdressenCommand.getAbweichendeAdressen().contains("Schülerin und Mutter"));
        assertNotEquals(schueler.getAdresse(), schueler.getMutter().getAdresse());

        // ohne Vater
        schueler.setVater(null);
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getIdentischeAdressen().isEmpty());
        assertTrue(checkIdentischeAdressenCommand.getAbweichendeAdressen().contains("Schülerin und Mutter"));
        assertNotEquals(schueler.getAdresse(), schueler.getMutter().getAdresse());
    }

    @Test
    public void testExecute_SCHUELER_VATER_VERSCHIEDEN() {
        // Mutter ohne Adresse
        Schueler schueler = schueler3;
        schueler.setMutter(mutter4);
        schueler.setVater(vater3);
        schueler.setRechnungsempfaenger(vater3);
        CheckIdentischeAdressenCommand checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getIdentischeAdressen().isEmpty());
        assertTrue(checkIdentischeAdressenCommand.getAbweichendeAdressen().contains("Schülerin und Vater"));
        assertNotEquals(schueler.getAdresse(), schueler.getVater().getAdresse());

        // ohne Mutter
        schueler.setMutter(null);
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getIdentischeAdressen().isEmpty());
        assertTrue(checkIdentischeAdressenCommand.getAbweichendeAdressen().contains("Schülerin und Vater"));
        assertNotEquals(schueler.getAdresse(), schueler.getVater().getAdresse());
    }

}