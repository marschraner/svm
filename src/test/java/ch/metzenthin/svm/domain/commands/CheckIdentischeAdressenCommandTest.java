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
        schueler1 = new Schueler("Carla", "Müller", new GregorianCalendar(2000, Calendar.MAY, 2), null, null, Geschlecht.W, new GregorianCalendar(2015, Calendar.JANUARY, 1), null, null);
        Adresse adresseSchueler1 = new Adresse("Wiesenstrasse", "5", "5430", "Wettingen", "056 426 69 15");
        schueler1.setAdresse(adresseSchueler1);
        schueler2 = new Schueler("Carla", "Müller", new GregorianCalendar(2000, Calendar.MAY, 2), null, null, Geschlecht.W, new GregorianCalendar(2015, Calendar.JANUARY, 1), null, null);
        Adresse adresseSchueler2 = new Adresse("Weidstrasse", "55", "8803", "Rüschlikon", "044 724 11 20");
        schueler2.setAdresse(adresseSchueler2);
        schueler3 = new Schueler("Carla", "Müller", new GregorianCalendar(2000, Calendar.MAY, 2), null, null, Geschlecht.W, new GregorianCalendar(2015, Calendar.JANUARY, 1), null, null);
        Adresse adresseSchueler3 = new Adresse("Tödistrasse", "2", "8800", "Thalwil", "044 720 12 15");
        schueler3.setAdresse(adresseSchueler3);

        mutter1 = new Angehoeriger(Anrede.FRAU, "Susanne", "Müller", null, null);
        Adresse adresseMutter1 = new Adresse("Wiesenstrasse", "5", "5430", "Wettingen", "056 426 69 15");
        mutter1.setAdresse(adresseMutter1);
        mutter2 = new Angehoeriger(Anrede.FRAU, "Susanne", "Müller", null, null);
        Adresse adresseMutter2 = new Adresse("Weidstrasse", "55", "8803", "Rüschlikon", "044 724 11 20");
        mutter2.setAdresse(adresseMutter2);
        mutter3 = new Angehoeriger(Anrede.FRAU, "Susanne", "Müller", null, null);
        Adresse adresseMutter3 = new Adresse("Tödistrasse", "4", "8800", "Thalwil", "044 720 12 17");
        mutter3.setAdresse(adresseMutter3);
        mutter4 = new Angehoeriger(Anrede.FRAU, "Susanne", "Müller", null, null);

        vater1 = new Angehoeriger(Anrede.HERR, "Andreas", "Bruggisser", null, null);
        Adresse adresseVater1 = new Adresse("Wiesenstrasse", "5", "5430", "Wettingen", "056 426 69 15");
        vater1.setAdresse(adresseVater1);
        vater2 = new Angehoeriger(Anrede.HERR, "Andreas", "Bruggisser", null, null);
        Adresse adresseVater2 = new Adresse("Weidstrasse", "55", "8803", "Rüschlikon", "044 724 11 20");
        vater2.setAdresse(adresseVater2);
        vater3 = new Angehoeriger(Anrede.HERR, "Andreas", "Bruggisser", null, null);
        Adresse adresseVater3 = new Adresse("Tödistrasse", "6", "8800", "Thalwil", "044 720 12 19");
        vater3.setAdresse(adresseVater3);
        vater4 = new Angehoeriger(Anrede.HERR, "Andreas", "Bruggisser", null, null);

        rechnungsempfaengerDrittperson1 = new Angehoeriger(Anrede.FRAU, "Hanny", "Bruggisser", null, null);
        Adresse adresseRechnungsempfaengerDrittperson1 = new Adresse("Wiesenstrasse", "5", "5430", "Wettingen", "056 426 69 15");
        rechnungsempfaengerDrittperson1.setAdresse(adresseRechnungsempfaengerDrittperson1);
        rechnungsempfaengerDrittperson2 = new Angehoeriger(Anrede.FRAU, "Hanny", "Bruggisser", null, null);
        Adresse adresseRechnungsempfaengerDrittperson2 = new Adresse("Weidstrasse", "55", "8803", "Rüschlikon", "044 724 11 20");
        rechnungsempfaengerDrittperson2.setAdresse(adresseRechnungsempfaengerDrittperson2);
        rechnungsempfaengerDrittperson3 = new Angehoeriger(Anrede.FRAU, "Hanny", "Bruggisser", null, null);
        Adresse adresseRechnungsempfaengerDrittperson3 = new Adresse("Tödistrasse", "8", "8800", "Thalwil", "044 720 12 21");
        rechnungsempfaengerDrittperson3.setAdresse(adresseRechnungsempfaengerDrittperson3);
    }

    // 1.
    @Test
    public void testExecute_SCHUELER_MUTTER_VATER_DRITTPERSON_IDENTISCH() {
        CheckIdentischeAdressenCommand checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler1, mutter1, vater1, rechnungsempfaengerDrittperson1);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getInfoIdentischeAdressen().contains("Schüler, Mutter, Vater und Rechnungsempfänger Drittperson"));
        assertTrue(checkIdentischeAdressenCommand.getInfoAbweichendeAdressen().isEmpty());
        assertEquals(mutter1.getAdresse(), schueler1.getAdresse());
        assertEquals(mutter1.getAdresse(), vater1.getAdresse());
        assertEquals(mutter1.getAdresse(), rechnungsempfaengerDrittperson1.getAdresse());
    }

    // 2.
    @Test
    public void testExecute_SCHUELER_MUTTER_VATER_IDENTISCH() {
        // mit Drittperson
        CheckIdentischeAdressenCommand checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler1, mutter1, vater1, rechnungsempfaengerDrittperson2);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getInfoIdentischeAdressen().contains("Schüler, Mutter und Vater"));
        assertTrue(checkIdentischeAdressenCommand.getInfoAbweichendeAdressen().contains("Rechnungsempfänger"));
        assertEquals(mutter1.getAdresse(), schueler1.getAdresse());
        assertEquals(mutter1.getAdresse(), vater1.getAdresse());
        assertNotEquals(mutter1.getAdresse(), rechnungsempfaengerDrittperson2.getAdresse());

        // ohne Drittperson
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler1, mutter1, vater1, null);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getInfoIdentischeAdressen().contains("Schüler, Mutter und Vater"));
        assertTrue(checkIdentischeAdressenCommand.getInfoAbweichendeAdressen().isEmpty());
        assertEquals(mutter1.getAdresse(), schueler1.getAdresse());
        assertEquals(mutter1.getAdresse(), vater1.getAdresse());
    }

    @Test
    public void testExecute_SCHUELER_MUTTER_DRITTPERSON_IDENTISCH() {
        // Vater mit Adresse
        CheckIdentischeAdressenCommand checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler1, mutter1, vater2, rechnungsempfaengerDrittperson1);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getInfoIdentischeAdressen().contains("Schüler, Mutter und Rechnungsempfänger"));
        assertTrue(checkIdentischeAdressenCommand.getInfoAbweichendeAdressen().contains("Vater"));
        assertEquals(mutter1.getAdresse(), schueler1.getAdresse());
        assertNotEquals(mutter1.getAdresse(), vater2.getAdresse());
        assertEquals(mutter1.getAdresse(), rechnungsempfaengerDrittperson1.getAdresse());

        // Vater ohne Adresse
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler1, mutter1, vater4, rechnungsempfaengerDrittperson1);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getInfoIdentischeAdressen().contains("Schüler, Mutter und Rechnungsempfänger"));
        assertTrue(checkIdentischeAdressenCommand.getInfoAbweichendeAdressen().isEmpty());
        assertEquals(mutter1.getAdresse(), schueler1.getAdresse());
        assertEquals(mutter1.getAdresse(), rechnungsempfaengerDrittperson1.getAdresse());

        // ohne Vater
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler1, mutter1, null, rechnungsempfaengerDrittperson1);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getInfoIdentischeAdressen().contains("Schüler, Mutter und Rechnungsempfänger"));
        assertTrue(checkIdentischeAdressenCommand.getInfoAbweichendeAdressen().isEmpty());
        assertEquals(mutter1.getAdresse(), schueler1.getAdresse());
        assertEquals(mutter1.getAdresse(), rechnungsempfaengerDrittperson1.getAdresse());
    }

    @Test
    public void testExecute_SCHUELER_VATER_DRITTPERSON_IDENTISCH() {
        // Mutter mit Adresse
        CheckIdentischeAdressenCommand checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler1, mutter2, vater1, rechnungsempfaengerDrittperson1);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getInfoIdentischeAdressen().contains("Schüler, Vater und Rechnungsempfänger"));
        assertTrue(checkIdentischeAdressenCommand.getInfoAbweichendeAdressen().contains("Mutter"));
        assertEquals(vater1.getAdresse(), schueler1.getAdresse());
        assertNotEquals(vater1.getAdresse(), mutter2.getAdresse());
        assertEquals(vater1.getAdresse(), rechnungsempfaengerDrittperson1.getAdresse());

        // Mutter ohne Adresse
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler1, mutter4, vater1, rechnungsempfaengerDrittperson1);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getInfoIdentischeAdressen().contains("Schüler, Vater und Rechnungsempfänger"));
        assertTrue(checkIdentischeAdressenCommand.getInfoAbweichendeAdressen().isEmpty());
        assertEquals(vater1.getAdresse(), schueler1.getAdresse());
        assertEquals(vater1.getAdresse(), rechnungsempfaengerDrittperson1.getAdresse());

        // ohne Mutter
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler1, null, vater1, rechnungsempfaengerDrittperson1);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getInfoIdentischeAdressen().contains("Schüler, Vater und Rechnungsempfänger"));
        assertTrue(checkIdentischeAdressenCommand.getInfoAbweichendeAdressen().isEmpty());
        assertEquals(vater1.getAdresse(), schueler1.getAdresse());
        assertEquals(vater1.getAdresse(), rechnungsempfaengerDrittperson1.getAdresse());
    }

    @Test
    public void testExecute_MUTTER_VATER_DRITTPERSON_IDENTISCH() {
        CheckIdentischeAdressenCommand checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler2, mutter1, vater1, rechnungsempfaengerDrittperson1);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getInfoIdentischeAdressen().contains("Mutter, Vater und Rechnungsempfänger"));
        assertTrue(checkIdentischeAdressenCommand.getInfoAbweichendeAdressen().contains("Schüler"));
        assertNotEquals(mutter1.getAdresse(), schueler2.getAdresse());
        assertEquals(mutter1.getAdresse(), vater1.getAdresse());
        assertEquals(mutter1.getAdresse(), rechnungsempfaengerDrittperson1.getAdresse());
    }

    // 3.a
    @Test
    public void testExecute_SCHUELER_MUTTER_IDENTISCH_VATER_DRITTPERSON_IDENTISCH() {
        CheckIdentischeAdressenCommand checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler1, mutter1, vater2, rechnungsempfaengerDrittperson2);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getInfoIdentischeAdressen().contains("Schüler und Mutter"));
        assertTrue(checkIdentischeAdressenCommand.getInfoIdentischeAdressen().contains("Vater und Rechnungsempfänger"));
        assertTrue(checkIdentischeAdressenCommand.getInfoAbweichendeAdressen().isEmpty());
        assertEquals(mutter1.getAdresse(), schueler1.getAdresse());
        assertNotEquals(mutter1.getAdresse(), vater2.getAdresse());
        assertEquals(vater2.getAdresse(), rechnungsempfaengerDrittperson2.getAdresse());
    }

    @Test
    public void testExecute_SCHUELER_VATER_IDENTISCH_MUTTER_DRITTPERSON_IDENTISCH() {
        CheckIdentischeAdressenCommand checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler1, mutter2, vater1, rechnungsempfaengerDrittperson2);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getInfoIdentischeAdressen().contains("Schüler und Vater"));
        assertTrue(checkIdentischeAdressenCommand.getInfoIdentischeAdressen().contains("Mutter und Rechnungsempfänger"));
        assertTrue(checkIdentischeAdressenCommand.getInfoAbweichendeAdressen().isEmpty());
        assertEquals(vater1.getAdresse(), schueler1.getAdresse());
        assertNotEquals(mutter2.getAdresse(), schueler1.getAdresse());
        assertEquals(mutter2.getAdresse(), rechnungsempfaengerDrittperson2.getAdresse());
    }

    @Test
    public void testExecute_SCHUELER_DRITTPERSON_IDENTISCH_MUTTER_VATER_IDENTISCH() {
        CheckIdentischeAdressenCommand checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler1, mutter2, vater2, rechnungsempfaengerDrittperson1);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getInfoIdentischeAdressen().contains("Schüler und Rechnungsempfänger"));
        assertTrue(checkIdentischeAdressenCommand.getInfoIdentischeAdressen().contains("Mutter und Vater"));
        assertTrue(checkIdentischeAdressenCommand.getInfoAbweichendeAdressen().isEmpty());
        assertEquals(rechnungsempfaengerDrittperson1.getAdresse(), schueler1.getAdresse());
        assertNotEquals(mutter2.getAdresse(), schueler1.getAdresse());
        assertEquals(mutter2.getAdresse(), vater2.getAdresse());
    }

    // 3.b
    @Test
    public void testExecute_SCHUELER_MUTTER_IDENTISCH() {
        // mit Drittperson, Vater mit Adresse
        CheckIdentischeAdressenCommand checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler1, mutter1, vater2, rechnungsempfaengerDrittperson3);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getInfoIdentischeAdressen().contains("Schüler und Mutter"));
        assertTrue(checkIdentischeAdressenCommand.getInfoAbweichendeAdressen().contains("Vater und Rechnungsempfänger"));
        assertEquals(mutter1.getAdresse(), schueler1.getAdresse());
        assertNotEquals(mutter1.getAdresse(), vater2.getAdresse());
        assertNotEquals(mutter1.getAdresse(), rechnungsempfaengerDrittperson3.getAdresse());
        assertNotEquals(vater2.getAdresse(), rechnungsempfaengerDrittperson3.getAdresse());

        // mit Drittperson, Vater ohne Adresse
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler1, mutter1, vater4, rechnungsempfaengerDrittperson3);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getInfoIdentischeAdressen().contains("Schüler und Mutter"));
        assertTrue(checkIdentischeAdressenCommand.getInfoAbweichendeAdressen().contains("Rechnungsempfänger"));
        assertEquals(mutter1.getAdresse(), schueler1.getAdresse());
        assertNotEquals(mutter1.getAdresse(), rechnungsempfaengerDrittperson3.getAdresse());

        // mit Drittperson, ohne Vater
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler1, mutter1, null, rechnungsempfaengerDrittperson3);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getInfoIdentischeAdressen().contains("Schüler und Mutter"));
        assertTrue(checkIdentischeAdressenCommand.getInfoAbweichendeAdressen().contains("Rechnungsempfänger"));
        assertEquals(mutter1.getAdresse(), schueler1.getAdresse());
        assertNotEquals(mutter1.getAdresse(), rechnungsempfaengerDrittperson3.getAdresse());

        // ohne Drittperson, Vater mit Adresse
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler1, mutter1, vater2, null);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getInfoIdentischeAdressen().contains("Schüler und Mutter"));
        assertTrue(checkIdentischeAdressenCommand.getInfoAbweichendeAdressen().contains("Vater"));
        assertEquals(mutter1.getAdresse(), schueler1.getAdresse());
        assertNotEquals(mutter1.getAdresse(), vater2.getAdresse());

        // ohne Drittperson, Vater ohne Adresse
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler1, mutter1, vater4, null);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getInfoIdentischeAdressen().contains("Schüler und Mutter"));
        assertTrue(checkIdentischeAdressenCommand.getInfoAbweichendeAdressen().isEmpty());
        assertEquals(mutter1.getAdresse(), schueler1.getAdresse());

        // ohne Drittperson, ohne Vater
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler1, mutter1, null, null);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getInfoIdentischeAdressen().contains("Schüler und Mutter"));
        assertTrue(checkIdentischeAdressenCommand.getInfoAbweichendeAdressen().isEmpty());
        assertEquals(mutter1.getAdresse(), schueler1.getAdresse());
    }

    @Test
    public void testExecute_SCHUELER_VATER_IDENTISCH() {
        // mit Drittperson, Mutter mit Adresse
        CheckIdentischeAdressenCommand checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler1, mutter2, vater1, rechnungsempfaengerDrittperson3);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getInfoIdentischeAdressen().contains("Schüler und Vater"));
        assertTrue(checkIdentischeAdressenCommand.getInfoAbweichendeAdressen().contains("Mutter und Rechnungsempfänger"));
        assertEquals(vater1.getAdresse(), schueler1.getAdresse());
        assertNotEquals(vater1.getAdresse(), mutter2.getAdresse());
        assertNotEquals(vater1.getAdresse(), rechnungsempfaengerDrittperson3.getAdresse());
        assertNotEquals(mutter2.getAdresse(), rechnungsempfaengerDrittperson3.getAdresse());

        // mit Drittperson, Mutter ohne Adresse
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler1, mutter4, vater1, rechnungsempfaengerDrittperson3);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getInfoIdentischeAdressen().contains("Schüler und Vater"));
        assertTrue(checkIdentischeAdressenCommand.getInfoAbweichendeAdressen().contains("Rechnungsempfänger"));
        assertEquals(vater1.getAdresse(), schueler1.getAdresse());
        assertNotEquals(vater1.getAdresse(), rechnungsempfaengerDrittperson3.getAdresse());

        // mit Drittperson, ohne Mutter
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler1, null, vater1, rechnungsempfaengerDrittperson3);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getInfoIdentischeAdressen().contains("Schüler und Vater"));
        assertTrue(checkIdentischeAdressenCommand.getInfoAbweichendeAdressen().contains("Rechnungsempfänger"));
        assertEquals(vater1.getAdresse(), schueler1.getAdresse());
        assertNotEquals(vater1.getAdresse(), rechnungsempfaengerDrittperson3.getAdresse());

        // ohne Drittperson, Mutter mit Adresse
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler1, mutter2, vater1, null);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getInfoIdentischeAdressen().contains("Schüler und Vater"));
        assertTrue(checkIdentischeAdressenCommand.getInfoAbweichendeAdressen().contains("Mutter"));
        assertEquals(vater1.getAdresse(), schueler1.getAdresse());
        assertNotEquals(vater1.getAdresse(), mutter2.getAdresse());

        // ohne Drittperson, Mutter ohne Adresse
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler1, mutter4, vater1, null);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getInfoIdentischeAdressen().contains("Schüler und Vater"));
        assertTrue(checkIdentischeAdressenCommand.getInfoAbweichendeAdressen().isEmpty());
        assertEquals(vater1.getAdresse(), schueler1.getAdresse());

        // ohne Drittperson, ohne Mutter
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler1, null, vater1, null);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getInfoIdentischeAdressen().contains("Schüler und Vater"));
        assertTrue(checkIdentischeAdressenCommand.getInfoAbweichendeAdressen().isEmpty());
        assertEquals(vater1.getAdresse(), schueler1.getAdresse());
    }

    @Test
    public void testExecute_SCHUELER_DRITTPERSON_IDENTISCH() {
        // Mutter und Vater mit Adresse
        CheckIdentischeAdressenCommand checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler1, mutter2, vater3, rechnungsempfaengerDrittperson1);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getInfoIdentischeAdressen().contains("Schüler und Rechnungsempfänger"));
        assertTrue(checkIdentischeAdressenCommand.getInfoAbweichendeAdressen().contains("Mutter und Vater"));
        assertEquals(rechnungsempfaengerDrittperson1.getAdresse(), schueler1.getAdresse());
        assertNotEquals(schueler1.getAdresse(), mutter2.getAdresse());
        assertNotEquals(schueler1.getAdresse(), vater3.getAdresse());
        assertNotEquals(mutter2.getAdresse(), vater3.getAdresse());

        // Mutter mit Adresse, Vater ohne Adresse
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler1, mutter2, vater4, rechnungsempfaengerDrittperson1);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getInfoIdentischeAdressen().contains("Schüler und Rechnungsempfänger"));
        assertTrue(checkIdentischeAdressenCommand.getInfoAbweichendeAdressen().contains("Mutter"));
        assertEquals(rechnungsempfaengerDrittperson1.getAdresse(), schueler1.getAdresse());
        assertNotEquals(schueler1.getAdresse(), mutter2.getAdresse());

        // Mutter mit Adresse, ohne Vater
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler1, mutter2, null, rechnungsempfaengerDrittperson1);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getInfoIdentischeAdressen().contains("Schüler und Rechnungsempfänger"));
        assertTrue(checkIdentischeAdressenCommand.getInfoAbweichendeAdressen().contains("Mutter"));
        assertEquals(rechnungsempfaengerDrittperson1.getAdresse(), schueler1.getAdresse());
        assertNotEquals(schueler1.getAdresse(), mutter2.getAdresse());

        // Mutter ohne Adresse, Vater mit Adresse
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler1, mutter4, vater3, rechnungsempfaengerDrittperson1);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getInfoIdentischeAdressen().contains("Schüler und Rechnungsempfänger"));
        assertTrue(checkIdentischeAdressenCommand.getInfoAbweichendeAdressen().contains("Vater"));
        assertEquals(rechnungsempfaengerDrittperson1.getAdresse(), schueler1.getAdresse());
        assertNotEquals(schueler1.getAdresse(), vater3.getAdresse());

        // Mutter und Vater ohne Adresse
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler1, mutter4, vater4, rechnungsempfaengerDrittperson1);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getInfoIdentischeAdressen().contains("Schüler und Rechnungsempfänger"));
        assertTrue(checkIdentischeAdressenCommand.getInfoAbweichendeAdressen().isEmpty());
        assertEquals(rechnungsempfaengerDrittperson1.getAdresse(), schueler1.getAdresse());

        // Mutter ohne Adresse, ohne Vater
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler1, mutter4, null, rechnungsempfaengerDrittperson1);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getInfoIdentischeAdressen().contains("Schüler und Rechnungsempfänger"));
        assertTrue(checkIdentischeAdressenCommand.getInfoAbweichendeAdressen().isEmpty());
        assertEquals(rechnungsempfaengerDrittperson1.getAdresse(), schueler1.getAdresse());

        // ohne Mutter, Vater mit Adresse
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler1, null, vater3, rechnungsempfaengerDrittperson1);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getInfoIdentischeAdressen().contains("Schüler und Rechnungsempfänger"));
        assertTrue(checkIdentischeAdressenCommand.getInfoAbweichendeAdressen().contains("Vater"));
        assertEquals(rechnungsempfaengerDrittperson1.getAdresse(), schueler1.getAdresse());
        assertNotEquals(schueler1.getAdresse(), vater3.getAdresse());

        // ohne Mutter, Vater ohne Adresse
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler1, null, vater4, rechnungsempfaengerDrittperson1);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getInfoIdentischeAdressen().contains("Schüler und Rechnungsempfänger"));
        assertTrue(checkIdentischeAdressenCommand.getInfoAbweichendeAdressen().isEmpty());
        assertEquals(rechnungsempfaengerDrittperson1.getAdresse(), schueler1.getAdresse());

        // ohne Mutter, ohne Vater
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler1, null, null, rechnungsempfaengerDrittperson1);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getInfoIdentischeAdressen().contains("Schüler und Rechnungsempfänger"));
        assertTrue(checkIdentischeAdressenCommand.getInfoAbweichendeAdressen().isEmpty());
        assertEquals(rechnungsempfaengerDrittperson1.getAdresse(), schueler1.getAdresse());
    }

    @Test
    public void testExecute_MUTTER_VATER_IDENTISCH() {
        // mit Drittperson
        CheckIdentischeAdressenCommand checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler1, mutter2, vater2, rechnungsempfaengerDrittperson3);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getInfoIdentischeAdressen().contains("Mutter und Vater"));
        assertTrue(checkIdentischeAdressenCommand.getInfoAbweichendeAdressen().contains("Schüler und Rechnungsempfänger"));
        assertNotEquals(mutter2.getAdresse(), schueler1.getAdresse());
        assertEquals(mutter2.getAdresse(), vater2.getAdresse());
        assertNotEquals(schueler1.getAdresse(), rechnungsempfaengerDrittperson3.getAdresse());
        assertNotEquals(mutter2.getAdresse(), rechnungsempfaengerDrittperson3.getAdresse());

        // ohne Drittperson
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler1, mutter2, vater2, null);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getInfoIdentischeAdressen().contains("Mutter und Vater"));
        assertTrue(checkIdentischeAdressenCommand.getInfoAbweichendeAdressen().contains("Schüler"));
        assertNotEquals(mutter2.getAdresse(), schueler1.getAdresse());
        assertEquals(mutter2.getAdresse(), vater2.getAdresse());
    }

    @Test
    public void testExecute_MUTTER_DRITTPERSON_IDENTISCH() {
        // Vater mit Adresse
        CheckIdentischeAdressenCommand checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler1, mutter2, vater3, rechnungsempfaengerDrittperson2);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getInfoIdentischeAdressen().contains("Mutter und Rechnungsempfänger"));
        assertTrue(checkIdentischeAdressenCommand.getInfoAbweichendeAdressen().contains("Schüler und Vater"));
        assertNotEquals(mutter2.getAdresse(), schueler1.getAdresse());
        assertNotEquals(schueler1.getAdresse(), vater3.getAdresse());
        assertNotEquals(mutter2.getAdresse(), vater3.getAdresse());
        assertEquals(mutter2.getAdresse(), rechnungsempfaengerDrittperson2.getAdresse());

        // Vater ohne Adresse
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler1, mutter2, vater4, rechnungsempfaengerDrittperson2);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getInfoIdentischeAdressen().contains("Mutter und Rechnungsempfänger"));
        assertTrue(checkIdentischeAdressenCommand.getInfoAbweichendeAdressen().contains("Schüler"));
        assertNotEquals(mutter2.getAdresse(), schueler1.getAdresse());
        assertEquals(mutter2.getAdresse(), rechnungsempfaengerDrittperson2.getAdresse());

        // ohne Vater
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler1, mutter2, null, rechnungsempfaengerDrittperson2);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getInfoIdentischeAdressen().contains("Mutter und Rechnungsempfänger"));
        assertTrue(checkIdentischeAdressenCommand.getInfoAbweichendeAdressen().contains("Schüler"));
        assertNotEquals(mutter2.getAdresse(), schueler1.getAdresse());
        assertEquals(mutter2.getAdresse(), rechnungsempfaengerDrittperson2.getAdresse());
    }

    @Test
    public void testExecute_VATER_DRITTPERSON_IDENTISCH() {
        // Mutter mit Adresse
        CheckIdentischeAdressenCommand checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler1, mutter3, vater2, rechnungsempfaengerDrittperson2);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getInfoIdentischeAdressen().contains("Vater und Rechnungsempfänger"));
        assertTrue(checkIdentischeAdressenCommand.getInfoAbweichendeAdressen().contains("Schüler und Mutter"));
        assertNotEquals(schueler1.getAdresse(), mutter3.getAdresse());
        assertNotEquals(schueler1.getAdresse(), vater2.getAdresse());
        assertNotEquals(mutter3.getAdresse(), vater2.getAdresse());
        assertEquals(vater2.getAdresse(), rechnungsempfaengerDrittperson2.getAdresse());

        // Mutter ohne Adresse
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler1, mutter4, vater2, rechnungsempfaengerDrittperson2);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getInfoIdentischeAdressen().contains("Vater und Rechnungsempfänger"));
        assertTrue(checkIdentischeAdressenCommand.getInfoAbweichendeAdressen().contains("Schüler"));
        assertNotEquals(schueler1.getAdresse(), vater2.getAdresse());
        assertEquals(vater2.getAdresse(), rechnungsempfaengerDrittperson2.getAdresse());

        // ohne Mutter
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler1, null, vater2, rechnungsempfaengerDrittperson2);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getInfoIdentischeAdressen().contains("Vater und Rechnungsempfänger"));
        assertTrue(checkIdentischeAdressenCommand.getInfoAbweichendeAdressen().contains("Schüler"));
        assertNotEquals(schueler1.getAdresse(), vater2.getAdresse());
        assertEquals(vater2.getAdresse(), rechnungsempfaengerDrittperson2.getAdresse());
    }

    // 4.
    @Test
    public void testExecute_SCHUELER_MUTTER_VATER_DRITTPERSON_VERSCHIEDEN() {
        CheckIdentischeAdressenCommand checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler3, mutter3, vater3, rechnungsempfaengerDrittperson3);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getInfoIdentischeAdressen().isEmpty());
        assertTrue(checkIdentischeAdressenCommand.getInfoAbweichendeAdressen().contains("Schüler, Mutter, Vater und Rechnungsempfänger"));
        assertNotEquals(schueler3.getAdresse(), mutter3.getAdresse());
        assertNotEquals(schueler3.getAdresse(), vater3.getAdresse());
        assertNotEquals(mutter3.getAdresse(), vater3.getAdresse());
        assertNotEquals(schueler3.getAdresse(), rechnungsempfaengerDrittperson3.getAdresse());
        assertNotEquals(mutter3.getAdresse(), rechnungsempfaengerDrittperson3.getAdresse());
        assertNotEquals(vater3.getAdresse(), rechnungsempfaengerDrittperson3.getAdresse());
    }

    @Test
    public void testExecute_SCHUELER_MUTTER_DRITTPERSON_VERSCHIEDEN() {
        // Vater ohne Adresse
        CheckIdentischeAdressenCommand checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler3, mutter3, vater4, rechnungsempfaengerDrittperson3);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getInfoIdentischeAdressen().isEmpty());
        assertTrue(checkIdentischeAdressenCommand.getInfoAbweichendeAdressen().contains("Schüler, Mutter und Rechnungsempfänger"));
        assertNotEquals(schueler3.getAdresse(), mutter3.getAdresse());
        assertNotEquals(schueler3.getAdresse(), rechnungsempfaengerDrittperson3.getAdresse());
        assertNotEquals(mutter3.getAdresse(), rechnungsempfaengerDrittperson3.getAdresse());

        // ohne Vater
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler3, mutter3, null, rechnungsempfaengerDrittperson3);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getInfoIdentischeAdressen().isEmpty());
        assertTrue(checkIdentischeAdressenCommand.getInfoAbweichendeAdressen().contains("Schüler, Mutter und Rechnungsempfänger"));
        assertNotEquals(schueler3.getAdresse(), mutter3.getAdresse());
        assertNotEquals(schueler3.getAdresse(), rechnungsempfaengerDrittperson3.getAdresse());
        assertNotEquals(mutter3.getAdresse(), rechnungsempfaengerDrittperson3.getAdresse());
    }

    @Test
    public void testExecute_SCHUELER_VATER_DRITTPERSON_VERSCHIEDEN() {
        // Mutter ohne Adresse
        CheckIdentischeAdressenCommand checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler3, mutter4, vater3, rechnungsempfaengerDrittperson3);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getInfoIdentischeAdressen().isEmpty());
        assertTrue(checkIdentischeAdressenCommand.getInfoAbweichendeAdressen().contains("Schüler, Vater und Rechnungsempfänger"));
        assertNotEquals(schueler3.getAdresse(), vater3.getAdresse());
        assertNotEquals(schueler3.getAdresse(), rechnungsempfaengerDrittperson3.getAdresse());
        assertNotEquals(vater3.getAdresse(), rechnungsempfaengerDrittperson3.getAdresse());

        // ohne Mutter
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler3, null, vater3, rechnungsempfaengerDrittperson3);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getInfoIdentischeAdressen().isEmpty());
        assertTrue(checkIdentischeAdressenCommand.getInfoAbweichendeAdressen().contains("Schüler, Vater und Rechnungsempfänger"));
    }

    @Test
    public void testExecute_SCHUELER_DRITTPERSON_VERSCHIEDEN() {
        // Vater und Mutter ohne Adresse
        CheckIdentischeAdressenCommand checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler3, mutter4, vater4, rechnungsempfaengerDrittperson3);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getInfoIdentischeAdressen().isEmpty());
        assertTrue(checkIdentischeAdressenCommand.getInfoAbweichendeAdressen().contains("Schüler und Rechnungsempfänger"));
        assertNotEquals(schueler3.getAdresse(), rechnungsempfaengerDrittperson3.getAdresse());

        // Mutter ohne Adresse, ohne Vater
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler3, mutter4, null, rechnungsempfaengerDrittperson3);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getInfoIdentischeAdressen().isEmpty());
        assertTrue(checkIdentischeAdressenCommand.getInfoAbweichendeAdressen().contains("Schüler und Rechnungsempfänger"));
        assertNotEquals(schueler3.getAdresse(), rechnungsempfaengerDrittperson3.getAdresse());

        // ohne Mutter, Vater ohne Adresse
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler3, null, vater4, rechnungsempfaengerDrittperson3);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getInfoIdentischeAdressen().isEmpty());
        assertTrue(checkIdentischeAdressenCommand.getInfoAbweichendeAdressen().contains("Schüler und Rechnungsempfänger"));
        assertNotEquals(schueler3.getAdresse(), rechnungsempfaengerDrittperson3.getAdresse());

        // ohne Mutter und ohne Vater
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler3, null, null, rechnungsempfaengerDrittperson3);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getInfoIdentischeAdressen().isEmpty());
        assertTrue(checkIdentischeAdressenCommand.getInfoAbweichendeAdressen().contains("Schüler und Rechnungsempfänger"));
        assertNotEquals(schueler3.getAdresse(), rechnungsempfaengerDrittperson3.getAdresse());
    }

    @Test
    public void testExecute_SCHUELER_MUTTER_VATER_VERSCHIEDEN() {
        CheckIdentischeAdressenCommand checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler3, mutter3, vater3, null);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getInfoIdentischeAdressen().isEmpty());
        assertTrue(checkIdentischeAdressenCommand.getInfoAbweichendeAdressen().contains("Schüler, Mutter und Vater"));
        assertNotEquals(schueler3.getAdresse(), mutter3.getAdresse());
        assertNotEquals(schueler3.getAdresse(), vater3.getAdresse());
        assertNotEquals(mutter3.getAdresse(), vater3.getAdresse());
    }

    @Test
    public void testExecute_SCHUELER_MUTTER_VERSCHIEDEN() {
        // Vater ohne Adresse
        CheckIdentischeAdressenCommand checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler3, mutter3, vater4, null);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getInfoIdentischeAdressen().isEmpty());
        assertTrue(checkIdentischeAdressenCommand.getInfoAbweichendeAdressen().contains("Schüler und Mutter"));
        assertNotEquals(schueler3.getAdresse(), mutter3.getAdresse());

        // ohne Vater
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler3, mutter3, null, null);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getInfoIdentischeAdressen().isEmpty());
        assertTrue(checkIdentischeAdressenCommand.getInfoAbweichendeAdressen().contains("Schüler und Mutter"));
        assertNotEquals(schueler3.getAdresse(), mutter3.getAdresse());
    }

    @Test
    public void testExecute_SCHUELER_VATER_VERSCHIEDEN() {
        // Mutter ohne Adresse
        CheckIdentischeAdressenCommand checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler3, mutter4, vater3, null);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getInfoIdentischeAdressen().isEmpty());
        assertTrue(checkIdentischeAdressenCommand.getInfoAbweichendeAdressen().contains("Schüler und Vater"));
        assertNotEquals(schueler3.getAdresse(), vater3.getAdresse());

        // ohne Mutter
        checkIdentischeAdressenCommand = new CheckIdentischeAdressenCommand(schueler3, null, vater3, null);
        commandInvoker.executeCommand(checkIdentischeAdressenCommand);
        assertTrue(checkIdentischeAdressenCommand.getInfoIdentischeAdressen().isEmpty());
        assertTrue(checkIdentischeAdressenCommand.getInfoAbweichendeAdressen().contains("Schüler und Vater"));
        assertNotEquals(schueler3.getAdresse(), vater3.getAdresse());
    }

}