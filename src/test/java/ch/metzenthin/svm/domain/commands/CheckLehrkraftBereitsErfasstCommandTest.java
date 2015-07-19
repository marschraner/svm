package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.dataTypes.Anrede;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Lehrkraft;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Martin Schraner
 */
public class CheckLehrkraftBereitsErfasstCommandTest {

    private CommandInvoker commandInvoker = new CommandInvokerImpl();
    private List<Lehrkraft> bereitsErfassteLehrkraefte = new ArrayList<>();

    @Before
    public void setUp() {
        Lehrkraft lehrkraft = new Lehrkraft(Anrede.FRAU, "Noémi", "Roos", new GregorianCalendar(1994, Calendar.MARCH, 18), "044 391 45 35", "076 384 45 35", "nroos@gmx.ch", "756.3943.8722.22", "Mi, Fr, Sa", true);
        Adresse adresse = new Adresse("Rebwiesenstrasse", "54", "8702", "Zollikon");
        lehrkraft.setAdresse(adresse);
        bereitsErfassteLehrkraefte.add(lehrkraft);
    }

    @Test
    public void testExecute_LehrkraftBereitsInVerwendung() throws Exception {
        Lehrkraft lehrkraft = new Lehrkraft(Anrede.FRAU, "Noémi", "Roos", new GregorianCalendar(1994, Calendar.MARCH, 18), "043 111 11 11", "076 384 45 35", "nroos@gmx.ch", "756.3943.8722.22", "Mi, Fr, Sa", true);
        Adresse adresse = new Adresse("Rebwiesenstrasse", "77", "8702", "Zollikon");
        lehrkraft.setAdresse(adresse);
        CheckLehrkraftBereitsErfasstCommand checkLehrkraftBereitsErfasstCommand = new CheckLehrkraftBereitsErfasstCommand(lehrkraft, null, bereitsErfassteLehrkraefte);
        commandInvoker.executeCommand(checkLehrkraftBereitsErfasstCommand);
        assertTrue(checkLehrkraftBereitsErfasstCommand.isBereitsErfasst());
    }

    @Test
    public void testExecute_LehrkraftNochNichtErfasst() throws Exception {
        Lehrkraft lehrkraft = new Lehrkraft(Anrede.FRAU, "Nathalie", "Delley", new GregorianCalendar(1971, Calendar.DECEMBER, 16), "044 261 27 20", "076 338 05 36", "ndelley@sunrise.ch", "756.8274.3263.17", "Mi, Fr, Sa", true);
        Adresse adresse = new Adresse("Im Schilf", "7", "8044", "Zürich");
        lehrkraft.setAdresse(adresse);
        CheckLehrkraftBereitsErfasstCommand checkLehrkraftBereitsErfasstCommand = new CheckLehrkraftBereitsErfasstCommand(lehrkraft, null, bereitsErfassteLehrkraefte);
        commandInvoker.executeCommand(checkLehrkraftBereitsErfasstCommand);
        assertFalse(checkLehrkraftBereitsErfasstCommand.isBereitsErfasst());
    }

    @Test
    public void testExecute_LehrkraftOrigin() throws Exception {
        Lehrkraft lehrkraft = new Lehrkraft(Anrede.FRAU, "Noémi", "Roos", new GregorianCalendar(1994, Calendar.MARCH, 18), "043 111 11 11", "076 384 45 35", "nroos@gmx.ch", "756.3943.8722.22", "Mi, Fr, Sa", true);
        Adresse adresse = new Adresse("Rebwiesenstrasse", "77", "8702", "Zollikon");
        lehrkraft.setAdresse(adresse);
        CheckLehrkraftBereitsErfasstCommand checkLehrkraftBereitsErfasstCommand = new CheckLehrkraftBereitsErfasstCommand(lehrkraft, bereitsErfassteLehrkraefte.get(0), bereitsErfassteLehrkraefte);
        commandInvoker.executeCommand(checkLehrkraftBereitsErfasstCommand);
        assertFalse(checkLehrkraftBereitsErfasstCommand.isBereitsErfasst());
    }
}