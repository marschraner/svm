package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.dataTypes.Anrede;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.AngehoerigerModel;
import ch.metzenthin.svm.domain.model.AngehoerigerModelImpl;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Martin Schraner
 */
public class CheckDrittpersonIdentischMitElternteilCommandTest {

    private CommandInvoker commandInvoker = new CommandInvokerImpl();
    private AngehoerigerModel mutter1;
    private AngehoerigerModel mutter2;
    private AngehoerigerModel vater1;
    private AngehoerigerModel vater2;

    @Before
    public void setUp() {
        try {
            mutter1 = new AngehoerigerModelImpl(commandInvoker);
            mutter1.setAnrede(Anrede.FRAU);
            mutter1.setVorname("Susanne");
            mutter1.setNachname("Müller");
            mutter1.setStrasse("Wiesenstrasse");
            mutter1.setHausnummer("5");
            mutter1.setPlz("5430");
            mutter1.setOrt("Wettingen");
            mutter1.setFestnetz("056 426 69 15");
            mutter2 = new AngehoerigerModelImpl(commandInvoker);
            mutter2.setAnrede(Anrede.FRAU);
            mutter2.setVorname("Susanne");
            mutter2.setNachname("Müller");
            vater1 = new AngehoerigerModelImpl(commandInvoker);
            vater1.setAnrede(Anrede.HERR);
            vater1.setVorname("Andreas");
            vater1.setNachname("Bruggisser");
            vater1.setStrasse("Wiesenstrasse");
            vater1.setHausnummer("5");
            vater1.setPlz("5430");
            vater1.setOrt("Wettingen");
            vater1.setFestnetz("056 426 69 15");
            vater2 = new AngehoerigerModelImpl(commandInvoker);
            vater2.setAnrede(Anrede.HERR);
            vater2.setVorname("Andreas");
            vater2.setNachname("Bruggisser");
        } catch (SvmValidationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testExecute_NICHT_IDENTISCH() throws SvmValidationException {
        AngehoerigerModel rechnungsempfaenger = new AngehoerigerModelImpl(commandInvoker);
        rechnungsempfaenger.setAnrede(Anrede.HERR);
        rechnungsempfaenger.setVorname("Urs");
        rechnungsempfaenger.setNachname("Meister");
        CheckDrittpersonIdentischMitElternteilCommand checkDrittpersonIdentischMitElternteilCommand = new CheckDrittpersonIdentischMitElternteilCommand(mutter1, vater1, rechnungsempfaenger);
        commandInvoker.executeCommand(checkDrittpersonIdentischMitElternteilCommand);
        assertFalse(checkDrittpersonIdentischMitElternteilCommand.isIdentical());
    }

    @Test
    public void testExecute_IDENTISCH_MIT_MUTTER() throws SvmValidationException {
        AngehoerigerModel rechnungsempfaenger = new AngehoerigerModelImpl(commandInvoker);
        rechnungsempfaenger.setAnrede(Anrede.FRAU);
        rechnungsempfaenger.setVorname("Susanne");
        rechnungsempfaenger.setNachname("Müller");
        rechnungsempfaenger.setStrasse("Wiesenstrasse");
        rechnungsempfaenger.setHausnummer("5");
        rechnungsempfaenger.setPlz("5430");
        rechnungsempfaenger.setOrt("Wettingen");
        rechnungsempfaenger.setFestnetz("056 426 69 15");
        rechnungsempfaenger.setIsRechnungsempfaenger(true);
        CheckDrittpersonIdentischMitElternteilCommand checkDrittpersonIdentischMitElternteilCommand = new CheckDrittpersonIdentischMitElternteilCommand(mutter1, vater1, rechnungsempfaenger);
        commandInvoker.executeCommand(checkDrittpersonIdentischMitElternteilCommand);
        assertTrue(checkDrittpersonIdentischMitElternteilCommand.isIdentical());
        String errorMessage = checkDrittpersonIdentischMitElternteilCommand.getErrorDrittpersonIdentischMitElternteil();
        assertTrue(errorMessage.equals(CheckDrittpersonIdentischMitElternteilCommand.ERROR_IDENTISCH_MIT_MUTTER));
    }

    @Test
    public void testExecute_SCHEINT_IDENTISCH_MIT_MUTTER_ZU_SEIN() throws SvmValidationException {
        AngehoerigerModel rechnungsempfaenger = new AngehoerigerModelImpl(commandInvoker);
        rechnungsempfaenger.setAnrede(Anrede.FRAU);
        rechnungsempfaenger.setVorname("Susanne");
        rechnungsempfaenger.setNachname("Müller");
        rechnungsempfaenger.setStrasse("Wiesenstrasse");
        rechnungsempfaenger.setHausnummer("5");
        rechnungsempfaenger.setPlz("5430");
        rechnungsempfaenger.setOrt("Wettingen");
        rechnungsempfaenger.setFestnetz("056 426 69 15");
        rechnungsempfaenger.setIsRechnungsempfaenger(true);
        CheckDrittpersonIdentischMitElternteilCommand checkDrittpersonIdentischMitElternteilCommand = new CheckDrittpersonIdentischMitElternteilCommand(mutter2, vater2, rechnungsempfaenger);
        commandInvoker.executeCommand(checkDrittpersonIdentischMitElternteilCommand);
        assertTrue(checkDrittpersonIdentischMitElternteilCommand.isIdentical());
        String errorMessage = checkDrittpersonIdentischMitElternteilCommand.getErrorDrittpersonIdentischMitElternteil();
        assertTrue(errorMessage.contains(CheckDrittpersonIdentischMitElternteilCommand.ERROR_WAHRSCHEINLICH_IDENTISCH_MIT_MUTTER));
    }

    @Test
    public void testExecute_IDENTISCH_MIT_VATER() throws SvmValidationException {
        AngehoerigerModel rechnungsempfaenger = new AngehoerigerModelImpl(commandInvoker);
        rechnungsempfaenger.setAnrede(Anrede.HERR);
        rechnungsempfaenger.setVorname("Andreas");
        rechnungsempfaenger.setNachname("Bruggisser");
        rechnungsempfaenger.setStrasse("Wiesenstrasse");
        rechnungsempfaenger.setHausnummer("5");
        rechnungsempfaenger.setPlz("5430");
        rechnungsempfaenger.setOrt("Wettingen");
        rechnungsempfaenger.setFestnetz("056 426 69 15");
        rechnungsempfaenger.setIsRechnungsempfaenger(true);
        CheckDrittpersonIdentischMitElternteilCommand checkDrittpersonIdentischMitElternteilCommand = new CheckDrittpersonIdentischMitElternteilCommand(mutter1, vater1, rechnungsempfaenger);
        commandInvoker.executeCommand(checkDrittpersonIdentischMitElternteilCommand);
        assertTrue(checkDrittpersonIdentischMitElternteilCommand.isIdentical());
        String errorMessage = checkDrittpersonIdentischMitElternteilCommand.getErrorDrittpersonIdentischMitElternteil();
        assertTrue(errorMessage.equals(CheckDrittpersonIdentischMitElternteilCommand.ERROR_IDENTISCH_MIT_VATER));
    }

    @Test
    public void testExecute_SCHEINT_IDENTISCH_MIT_VATER_ZU_SEIN() throws SvmValidationException {
        AngehoerigerModel rechnungsempfaenger = new AngehoerigerModelImpl(commandInvoker);
        rechnungsempfaenger.setAnrede(Anrede.HERR);
        rechnungsempfaenger.setVorname("Andreas");
        rechnungsempfaenger.setNachname("Bruggisser");
        rechnungsempfaenger.setStrasse("Wiesenstrasse");
        rechnungsempfaenger.setHausnummer("5");
        rechnungsempfaenger.setPlz("5430");
        rechnungsempfaenger.setOrt("Wettingen");
        rechnungsempfaenger.setFestnetz("056 426 69 15");
        rechnungsempfaenger.setIsRechnungsempfaenger(true);
        CheckDrittpersonIdentischMitElternteilCommand checkDrittpersonIdentischMitElternteilCommand = new CheckDrittpersonIdentischMitElternteilCommand(mutter2, vater2, rechnungsempfaenger);
        commandInvoker.executeCommand(checkDrittpersonIdentischMitElternteilCommand);
        assertTrue(checkDrittpersonIdentischMitElternteilCommand.isIdentical());
        String errorMessage = checkDrittpersonIdentischMitElternteilCommand.getErrorDrittpersonIdentischMitElternteil();
        assertTrue(errorMessage.contains(CheckDrittpersonIdentischMitElternteilCommand.ERROR_WAHRSCHEINLICH_IDENTISCH_MIT_VATER));
    }
}