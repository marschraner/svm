package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.entities.SchuelerCode;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Martin Schraner
 */
public class CheckCodeKuerzelBereitsInVerwendungCommandTest {

    private CommandInvoker commandInvoker = new CommandInvokerImpl();
    private List<SchuelerCode> bereitsErfassteSchuelerCodes = new ArrayList<>();

    @Before
    public void setUp() {
        bereitsErfassteSchuelerCodes.add(new SchuelerCode("z", "Zirkus", true));
        bereitsErfassteSchuelerCodes.add(new SchuelerCode("j", "Jugendprojekt", true));
        bereitsErfassteSchuelerCodes.add(new SchuelerCode("6", "6-Jahres-Rabatt", true));
    }

    @Test
    public void testExecute_KuerzelBereitsInVerwendung() {
        CheckCodeKuerzelBereitsInVerwendungCommand checkCodeKuerzelBereitsInVerwendungCommand = new CheckCodeKuerzelBereitsInVerwendungCommand("z", null, bereitsErfassteSchuelerCodes);
        commandInvoker.executeCommand(checkCodeKuerzelBereitsInVerwendungCommand);
        assertTrue(checkCodeKuerzelBereitsInVerwendungCommand.isBereitsInVerwendung());
    }

    @Test
    public void testExecute_KuerzelNochNichtInVerwendung() {
        CheckCodeKuerzelBereitsInVerwendungCommand checkCodeKuerzelBereitsInVerwendungCommand = new CheckCodeKuerzelBereitsInVerwendungCommand("Z", null, bereitsErfassteSchuelerCodes);
        commandInvoker.executeCommand(checkCodeKuerzelBereitsInVerwendungCommand);
        assertFalse(checkCodeKuerzelBereitsInVerwendungCommand.isBereitsInVerwendung());
    }

    @Test
    public void testExecute_CodeOrigin() {
        CheckCodeKuerzelBereitsInVerwendungCommand checkCodeKuerzelBereitsInVerwendungCommand = new CheckCodeKuerzelBereitsInVerwendungCommand("z", bereitsErfassteSchuelerCodes.get(0), bereitsErfassteSchuelerCodes);
        commandInvoker.executeCommand(checkCodeKuerzelBereitsInVerwendungCommand);
        assertFalse(checkCodeKuerzelBereitsInVerwendungCommand.isBereitsInVerwendung());
    }
}