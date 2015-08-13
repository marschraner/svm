package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.entities.Maerchen;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Martin Schraner
 */
public class CheckMaerchenBereitsErfasstCommandTest {

    private CommandInvoker commandInvoker = new CommandInvokerImpl();
    private List<Maerchen> bereitsErfassteMaerchens = new ArrayList<>();

    @Before
    public void setUp() {
        bereitsErfassteMaerchens.add(new Maerchen("1911/1912", "Schneewittchen", 7));
        bereitsErfassteMaerchens.add(new Maerchen("1912/1913", "Gestiefelter Kater", 8));
    }

    @Test
    public void testExecute_BereitsErfasst() throws Exception {
        Maerchen maerchen = new Maerchen("1911/1912", "Hans im Glück", 9);
        CheckMaerchenBereitsErfasstCommand checkMaerchenBereitsErfasstCommand = new CheckMaerchenBereitsErfasstCommand(maerchen, null, bereitsErfassteMaerchens);
        commandInvoker.executeCommand(checkMaerchenBereitsErfasstCommand);
        assertTrue(checkMaerchenBereitsErfasstCommand.isBereitsErfasst());
    }

    @Test
    public void testExecute_MaerchenNochNichtErfasst() throws Exception {
        Maerchen maerchen = new Maerchen("1913/1914", "Schneewittchen", 7);
        CheckMaerchenBereitsErfasstCommand checkMaerchenBereitsErfasstCommand = new CheckMaerchenBereitsErfasstCommand(maerchen, null, bereitsErfassteMaerchens);
        commandInvoker.executeCommand(checkMaerchenBereitsErfasstCommand);
        assertFalse(checkMaerchenBereitsErfasstCommand.isBereitsErfasst());
    }

    @Test
    public void testExecute_MaerchenOrigin() throws Exception {
        Maerchen maerchen = new Maerchen("1911/1912", "Schneewittchen", 7);
        CheckMaerchenBereitsErfasstCommand checkMaerchenBereitsErfasstCommand = new CheckMaerchenBereitsErfasstCommand(maerchen, bereitsErfassteMaerchens.get(0), bereitsErfassteMaerchens);
        commandInvoker.executeCommand(checkMaerchenBereitsErfasstCommand);
        assertFalse(checkMaerchenBereitsErfasstCommand.isBereitsErfasst());
    }
}