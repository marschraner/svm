package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.entities.Code;
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
    private List<Code> bereitsErfassteCodes = new ArrayList<>();

    @Before
    public void setUp() {
        bereitsErfassteCodes.add(new Code("z", "Zirkus"));
        bereitsErfassteCodes.add(new Code("j", "Jugendprojekt"));
        bereitsErfassteCodes.add(new Code("6", "6-Jahres-Rabatt"));
    }

    @Test
    public void testExecute_KuerzelBereitsInVerwendung() throws Exception {
        Code code = new Code("z", "Zirkusprojekt");
        CheckCodeKuerzelBereitsInVerwendungCommand checkCodeKuerzelBereitsInVerwendungCommand = new CheckCodeKuerzelBereitsInVerwendungCommand(code, null, bereitsErfassteCodes);
        commandInvoker.executeCommand(checkCodeKuerzelBereitsInVerwendungCommand);
        assertTrue(checkCodeKuerzelBereitsInVerwendungCommand.isBereitsInVerwendung());
    }

    @Test
    public void testExecute_KuerzelNochNichtInVerwendung() throws Exception {
        Code code = new Code("Z", "Zirkusprojekt");
        CheckCodeKuerzelBereitsInVerwendungCommand checkCodeKuerzelBereitsInVerwendungCommand = new CheckCodeKuerzelBereitsInVerwendungCommand(code, null, bereitsErfassteCodes);
        commandInvoker.executeCommand(checkCodeKuerzelBereitsInVerwendungCommand);
        assertFalse(checkCodeKuerzelBereitsInVerwendungCommand.isBereitsInVerwendung());
    }

    @Test
    public void testExecute_CodeOrigin() throws Exception {
        Code code = new Code("z", "Zirkusprojekt");
        CheckCodeKuerzelBereitsInVerwendungCommand checkCodeKuerzelBereitsInVerwendungCommand = new CheckCodeKuerzelBereitsInVerwendungCommand(code, bereitsErfassteCodes.get(0), bereitsErfassteCodes);
        commandInvoker.executeCommand(checkCodeKuerzelBereitsInVerwendungCommand);
        assertFalse(checkCodeKuerzelBereitsInVerwendungCommand.isBereitsInVerwendung());
    }
}