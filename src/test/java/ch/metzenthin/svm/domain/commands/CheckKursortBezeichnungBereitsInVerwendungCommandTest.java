package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.entities.Kursort;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Martin Schraner
 */
public class CheckKursortBezeichnungBereitsInVerwendungCommandTest {

    private CommandInvoker commandInvoker = new CommandInvokerImpl();
    private List<Kursort> bereitsErfassteKursorte = new ArrayList<>();

    @Before
    public void setUp() {
        bereitsErfassteKursorte.add(new Kursort("Saal Test1"));
        bereitsErfassteKursorte.add(new Kursort("Saal Test2"));
        bereitsErfassteKursorte.add(new Kursort("Saal Test3"));
    }

    @Test
    public void testExecute_BezeichnungBereitsInVerwendung() throws Exception {
        Kursort kursort = new Kursort("Saal Test2");
        CheckKursortBezeichnungBereitsInVerwendungCommand checkKursortBezeichnungBereitsInVerwendungCommand = new CheckKursortBezeichnungBereitsInVerwendungCommand(kursort, null, bereitsErfassteKursorte);
        commandInvoker.executeCommand(checkKursortBezeichnungBereitsInVerwendungCommand);
        assertTrue(checkKursortBezeichnungBereitsInVerwendungCommand.isBereitsInVerwendung());
    }

    @Test
    public void testExecute_BezeichnungNochNichtInVerwendung() throws Exception {
        Kursort kursort = new Kursort("Saal Test4");
        CheckKursortBezeichnungBereitsInVerwendungCommand checkKursortBezeichnungBereitsInVerwendungCommand = new CheckKursortBezeichnungBereitsInVerwendungCommand(kursort, null, bereitsErfassteKursorte);
        commandInvoker.executeCommand(checkKursortBezeichnungBereitsInVerwendungCommand);
        assertFalse(checkKursortBezeichnungBereitsInVerwendungCommand.isBereitsInVerwendung());
    }

    @Test
    public void testExecute_KursortOrigin() throws Exception {
        Kursort kursort = new Kursort("Saal Test1");
        CheckKursortBezeichnungBereitsInVerwendungCommand checkKursortBezeichnungBereitsInVerwendungCommand = new CheckKursortBezeichnungBereitsInVerwendungCommand(kursort, bereitsErfassteKursorte.get(0), bereitsErfassteKursorte);
        commandInvoker.executeCommand(checkKursortBezeichnungBereitsInVerwendungCommand);
        assertFalse(checkKursortBezeichnungBereitsInVerwendungCommand.isBereitsInVerwendung());
    }
}