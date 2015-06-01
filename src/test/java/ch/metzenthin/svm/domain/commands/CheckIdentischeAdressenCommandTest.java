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
    private Angehoeriger mutter1;
    private Angehoeriger mutter2;
    private Angehoeriger mutter3;
    private Angehoeriger vater1;
    private Angehoeriger vater2;
    private Angehoeriger vater3;
    private Angehoeriger rechnungsempfaengerDrittperson1;
    private Angehoeriger rechnungsempfaengerDrittperson2;

    @Before
    public void setUp() {
        schueler1 = new Schueler("Carla", "Müller", new GregorianCalendar(2000, Calendar.MAY, 2), null, null, Geschlecht.W, new GregorianCalendar(2015, Calendar.JANUARY, 1), null, null);
        Adresse adresseSchueler1 = new Adresse("Wiesenstrasse", "5", "5430", "Wettingen", "056 426 69 15");
        schueler1.setNewAdresse(adresseSchueler1);
        schueler2 = new Schueler("Carla", "Müller", new GregorianCalendar(2000, Calendar.MAY, 2), null, null, Geschlecht.W, new GregorianCalendar(2015, Calendar.JANUARY, 1), null, null);
        Adresse adresseSchueler2 = new Adresse("Wiesenstrasse", "55", "5430", "Wettingen", "056 222 11 20");
        schueler2.setNewAdresse(adresseSchueler2);

        mutter1 = new Angehoeriger(Anrede.FRAU, "Susanne", "Müller", null, null);
        Adresse adresseMutter1 = new Adresse("Wiesenstrasse", "5", "5430", "Wettingen", "056 426 69 15");
        mutter1.setNewAdresse(adresseMutter1);
        mutter2 = new Angehoeriger(Anrede.FRAU, "Susanne", "Müller", null, null);
        Adresse adresseMutter2 = new Adresse("Wiesenstrasse", "55", "5430", "Wettingen", "056 222 11 20");
        mutter2.setNewAdresse(adresseMutter2);
        mutter3 = new Angehoeriger(Anrede.FRAU, "Susanne", "Müller", null, null);

        vater1 = new Angehoeriger(Anrede.HERR, "Andreas", "Bruggisser", null, null);
        Adresse adresseVater1 = new Adresse("Wiesenstrasse", "5", "5430", "Wettingen", "056 426 69 15");
        vater1.setNewAdresse(adresseVater1);
        vater2 = new Angehoeriger(Anrede.HERR, "Andreas", "Bruggisser", null, null);
        Adresse adresseVater2 = new Adresse("Wiesenstrasse", "55", "5430", "Wettingen", "056 222 11 20");
        vater2.setNewAdresse(adresseVater2);
        vater3 = new Angehoeriger(Anrede.HERR, "Andreas", "Bruggisser", null, null);

        rechnungsempfaengerDrittperson1 = new Angehoeriger(Anrede.FRAU, "Hanny", "Bruggisser", null, null);
        Adresse adresseRechnungsempfaengerDrittperson1 = new Adresse("Wiesenstrasse", "5", "5430", "Wettingen", "056 426 69 15");
        rechnungsempfaengerDrittperson1.setNewAdresse(adresseRechnungsempfaengerDrittperson1);
        rechnungsempfaengerDrittperson2 = new Angehoeriger(Anrede.FRAU, "Hanny", "Bruggisser", null, null);
        Adresse adresseRechnungsempfaengerDrittperson2 = new Adresse("Wiesenstrasse", "55", "5430", "Wettingen", "056 222 11 20");
        rechnungsempfaengerDrittperson2.setNewAdresse(adresseRechnungsempfaengerDrittperson2);
    }

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

}