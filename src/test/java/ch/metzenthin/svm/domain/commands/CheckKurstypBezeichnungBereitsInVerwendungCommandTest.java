package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.entities.Kurstyp;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Martin Schraner
 */
public class CheckKurstypBezeichnungBereitsInVerwendungCommandTest {

    private CommandInvoker commandInvoker = new CommandInvokerImpl();
    private List<Kurstyp> bereitsErfassteKurstypen = new ArrayList<>();

    @Before
    public void setUp() {
        bereitsErfassteKurstypen.add(new Kurstyp("Kurs Test1"));
        bereitsErfassteKurstypen.add(new Kurstyp("Kurs Test2"));
        bereitsErfassteKurstypen.add(new Kurstyp("Kurs Test3"));
    }

    @Test
    public void testExecute_BezeichnungBereitsInVerwendung() throws Exception {
        Kurstyp kurstyp = new Kurstyp("Kurs Test2");
        CheckKurstypBezeichnungBereitsInVerwendungCommand checkKurstypBezeichnungBereitsInVerwendungCommand = new CheckKurstypBezeichnungBereitsInVerwendungCommand(kurstyp, null, bereitsErfassteKurstypen);
        commandInvoker.executeCommand(checkKurstypBezeichnungBereitsInVerwendungCommand);
        assertTrue(checkKurstypBezeichnungBereitsInVerwendungCommand.isBereitsInVerwendung());
    }

    @Test
    public void testExecute_BezeichnungNochNichtInVerwendung() throws Exception {
        Kurstyp kurstyp = new Kurstyp("Kurs Test4");
        CheckKurstypBezeichnungBereitsInVerwendungCommand checkKurstypBezeichnungBereitsInVerwendungCommand = new CheckKurstypBezeichnungBereitsInVerwendungCommand(kurstyp, null, bereitsErfassteKurstypen);
        commandInvoker.executeCommand(checkKurstypBezeichnungBereitsInVerwendungCommand);
        assertFalse(checkKurstypBezeichnungBereitsInVerwendungCommand.isBereitsInVerwendung());
    }

    @Test
    public void testExecute_KurstypOrigin() throws Exception {
        Kurstyp kurstyp = new Kurstyp("Kurs Test1");
        CheckKurstypBezeichnungBereitsInVerwendungCommand checkKurstypBezeichnungBereitsInVerwendungCommand = new CheckKurstypBezeichnungBereitsInVerwendungCommand(kurstyp, bereitsErfassteKurstypen.get(0), bereitsErfassteKurstypen);
        commandInvoker.executeCommand(checkKurstypBezeichnungBereitsInVerwendungCommand);
        assertFalse(checkKurstypBezeichnungBereitsInVerwendungCommand.isBereitsInVerwendung());
    }
}