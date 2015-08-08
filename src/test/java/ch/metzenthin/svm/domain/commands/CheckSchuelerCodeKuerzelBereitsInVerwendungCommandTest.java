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
public class CheckSchuelerCodeKuerzelBereitsInVerwendungCommandTest {

    private CommandInvoker commandInvoker = new CommandInvokerImpl();
    private List<SchuelerCode> bereitsErfassteSchuelerCodes = new ArrayList<>();

    @Before
    public void setUp() {
        bereitsErfassteSchuelerCodes.add(new SchuelerCode("z", "Zirkus"));
        bereitsErfassteSchuelerCodes.add(new SchuelerCode("j", "Jugendprojekt"));
        bereitsErfassteSchuelerCodes.add(new SchuelerCode("6", "6-Jahres-Rabatt"));
    }

    @Test
    public void testExecute_KuerzelBereitsInVerwendung() throws Exception {
        SchuelerCode schuelerCode = new SchuelerCode("z", "Zirkusprojekt");
        CheckCodeKuerzelBereitsInVerwendungCommand checkCodeKuerzelBereitsInVerwendungCommand = new CheckCodeKuerzelBereitsInVerwendungCommand(schuelerCode, null, bereitsErfassteSchuelerCodes);
        commandInvoker.executeCommand(checkCodeKuerzelBereitsInVerwendungCommand);
        assertTrue(checkCodeKuerzelBereitsInVerwendungCommand.isBereitsInVerwendung());
    }

    @Test
    public void testExecute_KuerzelNochNichtInVerwendung() throws Exception {
        SchuelerCode schuelerCode = new SchuelerCode("Z", "Zirkusprojekt");
        CheckCodeKuerzelBereitsInVerwendungCommand checkCodeKuerzelBereitsInVerwendungCommand = new CheckCodeKuerzelBereitsInVerwendungCommand(schuelerCode, null, bereitsErfassteSchuelerCodes);
        commandInvoker.executeCommand(checkCodeKuerzelBereitsInVerwendungCommand);
        assertFalse(checkCodeKuerzelBereitsInVerwendungCommand.isBereitsInVerwendung());
    }

    @Test
    public void testExecute_CodeOrigin() throws Exception {
        SchuelerCode schuelerCode = new SchuelerCode("z", "Zirkusprojekt");
        CheckCodeKuerzelBereitsInVerwendungCommand checkCodeKuerzelBereitsInVerwendungCommand = new CheckCodeKuerzelBereitsInVerwendungCommand(schuelerCode, bereitsErfassteSchuelerCodes.get(0), bereitsErfassteSchuelerCodes);
        commandInvoker.executeCommand(checkCodeKuerzelBereitsInVerwendungCommand);
        assertFalse(checkCodeKuerzelBereitsInVerwendungCommand.isBereitsInVerwendung());
    }
}