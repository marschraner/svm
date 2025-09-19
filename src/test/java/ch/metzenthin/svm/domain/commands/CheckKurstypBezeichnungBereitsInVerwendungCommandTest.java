package ch.metzenthin.svm.domain.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import ch.metzenthin.svm.persistence.entities.Kurstyp;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Martin Schraner
 */
public class CheckKurstypBezeichnungBereitsInVerwendungCommandTest {

  private final CommandInvoker commandInvoker = new CommandInvokerImpl();
  private final List<Kurstyp> bereitsErfassteKurstypen = new ArrayList<>();

  @Before
  public void setUp() {
    bereitsErfassteKurstypen.add(new Kurstyp("Kurs Test1", true));
    bereitsErfassteKurstypen.add(new Kurstyp("Kurs Test2", true));
    bereitsErfassteKurstypen.add(new Kurstyp("Kurs Test3", true));
  }

  @Test
  public void testExecute_BezeichnungBereitsInVerwendung() {
    Kurstyp kurstyp = new Kurstyp("Kurs Test2", true);
    CheckKurstypBezeichnungBereitsInVerwendungCommand
        checkKurstypBezeichnungBereitsInVerwendungCommand =
            new CheckKurstypBezeichnungBereitsInVerwendungCommand(
                kurstyp, null, bereitsErfassteKurstypen);
    commandInvoker.executeCommand(checkKurstypBezeichnungBereitsInVerwendungCommand);
    assertTrue(checkKurstypBezeichnungBereitsInVerwendungCommand.isBereitsInVerwendung());
  }

  @Test
  public void testExecute_BezeichnungNochNichtInVerwendung() {
    Kurstyp kurstyp = new Kurstyp("Kurs Test4", true);
    CheckKurstypBezeichnungBereitsInVerwendungCommand
        checkKurstypBezeichnungBereitsInVerwendungCommand =
            new CheckKurstypBezeichnungBereitsInVerwendungCommand(
                kurstyp, null, bereitsErfassteKurstypen);
    commandInvoker.executeCommand(checkKurstypBezeichnungBereitsInVerwendungCommand);
    assertFalse(checkKurstypBezeichnungBereitsInVerwendungCommand.isBereitsInVerwendung());
  }

  @Test
  public void testExecute_KurstypOrigin() {
    Kurstyp kurstyp = new Kurstyp("Kurs Test1", true);
    CheckKurstypBezeichnungBereitsInVerwendungCommand
        checkKurstypBezeichnungBereitsInVerwendungCommand =
            new CheckKurstypBezeichnungBereitsInVerwendungCommand(
                kurstyp, bereitsErfassteKurstypen.get(0), bereitsErfassteKurstypen);
    commandInvoker.executeCommand(checkKurstypBezeichnungBereitsInVerwendungCommand);
    assertFalse(checkKurstypBezeichnungBereitsInVerwendungCommand.isBereitsInVerwendung());
  }
}
