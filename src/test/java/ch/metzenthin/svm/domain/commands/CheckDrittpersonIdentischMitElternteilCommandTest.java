package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Anrede;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Martin Schraner
 */
public class CheckDrittpersonIdentischMitElternteilCommandTest {

    private final CommandInvoker commandInvoker = new CommandInvokerImpl();

    private Angehoeriger mutter1;
    private Angehoeriger mutter2;
    private Angehoeriger vater1;
    private Angehoeriger vater2;

    @Before
    public void setUp() {
        mutter1 = new Angehoeriger(Anrede.FRAU, "Susanne", "Müller", "056 426 69 15", null, null, true);
        Adresse adresseMutter1 = new Adresse("Wiesenstrasse", "5", "5430", "Wettingen");
        mutter1.setAdresse(adresseMutter1);
        mutter2 = new Angehoeriger(Anrede.FRAU, "Susanne", "Müller", null, null, null, true);

        vater1 = new Angehoeriger(Anrede.HERR, "Andreas", "Bruggisser", "056 426 69 15", null, null, false);
        Adresse adresseVater1 = new Adresse("Wiesenstrasse", "5", "5430", "Wettingen");
        vater1.setAdresse(adresseVater1);
        vater2 = new Angehoeriger(Anrede.HERR, "Andreas", "Bruggisser", null, null, null, false);
    }

    @Test
    public void testExecute_NICHT_IDENTISCH() {
        Angehoeriger rechnungsempfaenger = new Angehoeriger(Anrede.HERR, "Urs", "Meister", null, null, null, false);
        CheckDrittpersonIdentischMitElternteilCommand checkDrittpersonIdentischMitElternteilCommand = new CheckDrittpersonIdentischMitElternteilCommand(mutter1, vater1, rechnungsempfaenger);
        commandInvoker.executeCommand(checkDrittpersonIdentischMitElternteilCommand);
        assertFalse(checkDrittpersonIdentischMitElternteilCommand.isIdentical());
    }

    @Test
    public void testExecute_IDENTISCH_MIT_MUTTER() {
        Angehoeriger rechnungsempfaenger = new Angehoeriger(Anrede.FRAU, "Susanne", "Müller", "056 426 69 15", null, null, true);
        Adresse adresseRechnungsempfaenger = new Adresse("Wiesenstrasse", "5", "5430", "Wettingen");
        rechnungsempfaenger.setAdresse(adresseRechnungsempfaenger);
        CheckDrittpersonIdentischMitElternteilCommand checkDrittpersonIdentischMitElternteilCommand = new CheckDrittpersonIdentischMitElternteilCommand(mutter1, vater1, rechnungsempfaenger);
        commandInvoker.executeCommand(checkDrittpersonIdentischMitElternteilCommand);
        assertTrue(checkDrittpersonIdentischMitElternteilCommand.isIdentical());
        String errorMessage = checkDrittpersonIdentischMitElternteilCommand.getErrorMessage();
        assertEquals(CheckDrittpersonIdentischMitElternteilCommand.ERROR_IDENTISCH_MIT_MUTTER, errorMessage);
    }

    @Test
    public void testExecute_SCHEINT_IDENTISCH_MIT_MUTTER_ZU_SEIN() {
        Angehoeriger rechnungsempfaenger = new Angehoeriger(Anrede.FRAU, "Susanne", "Müller", "056 426 69 15", null, null, true);
        Adresse adresseRechnungsempfaenger = new Adresse("Wiesenstrasse", "5", "5430", "Wettingen");
        rechnungsempfaenger.setAdresse(adresseRechnungsempfaenger);
        CheckDrittpersonIdentischMitElternteilCommand checkDrittpersonIdentischMitElternteilCommand = new CheckDrittpersonIdentischMitElternteilCommand(mutter2, vater2, rechnungsempfaenger);
        commandInvoker.executeCommand(checkDrittpersonIdentischMitElternteilCommand);
        assertTrue(checkDrittpersonIdentischMitElternteilCommand.isIdentical());
        String errorMessage = checkDrittpersonIdentischMitElternteilCommand.getErrorMessage();
        assertTrue(errorMessage.contains(CheckDrittpersonIdentischMitElternteilCommand.ERROR_WAHRSCHEINLICH_IDENTISCH_MIT_MUTTER));
    }

    @Test
    public void testExecute_IDENTISCH_MIT_VATER() {
        Angehoeriger rechnungsempfaenger = new Angehoeriger(Anrede.HERR, "Andreas", "Bruggisser", "056 426 69 15", null, null, false);
        Adresse adresseRechnungsempfaenger = new Adresse("Wiesenstrasse", "5", "5430", "Wettingen");
        rechnungsempfaenger.setAdresse(adresseRechnungsempfaenger);
        CheckDrittpersonIdentischMitElternteilCommand checkDrittpersonIdentischMitElternteilCommand = new CheckDrittpersonIdentischMitElternteilCommand(mutter1, vater1, rechnungsempfaenger);
        commandInvoker.executeCommand(checkDrittpersonIdentischMitElternteilCommand);
        assertTrue(checkDrittpersonIdentischMitElternteilCommand.isIdentical());
        String errorMessage = checkDrittpersonIdentischMitElternteilCommand.getErrorMessage();
        assertEquals(CheckDrittpersonIdentischMitElternteilCommand.ERROR_IDENTISCH_MIT_VATER, errorMessage);
    }

    @Test
    public void testExecute_SCHEINT_IDENTISCH_MIT_VATER_ZU_SEIN() {
        Angehoeriger rechnungsempfaenger = new Angehoeriger(Anrede.HERR, "Andreas", "Bruggisser", "056 426 69 15", null, null, false);
        Adresse adresseRechnungsempfaenger = new Adresse("Wiesenstrasse", "5", "5430", "Wettingen");
        rechnungsempfaenger.setAdresse(adresseRechnungsempfaenger);
        CheckDrittpersonIdentischMitElternteilCommand checkDrittpersonIdentischMitElternteilCommand = new CheckDrittpersonIdentischMitElternteilCommand(mutter2, vater2, rechnungsempfaenger);
        commandInvoker.executeCommand(checkDrittpersonIdentischMitElternteilCommand);
        assertTrue(checkDrittpersonIdentischMitElternteilCommand.isIdentical());
        String errorMessage = checkDrittpersonIdentischMitElternteilCommand.getErrorMessage();
        assertTrue(errorMessage.contains(CheckDrittpersonIdentischMitElternteilCommand.ERROR_WAHRSCHEINLICH_IDENTISCH_MIT_VATER));
    }
}