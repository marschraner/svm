package ch.metzenthin.svm.domain.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import ch.metzenthin.svm.persistence.entities.Kursort;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Martin Schraner
 */
public class CheckKursortBezeichnungBereitsInVerwendungCommandTest {

  private final CommandInvoker commandInvoker = new CommandInvokerImpl();
  private final List<Kursort> bereitsErfassteKursorte = new ArrayList<>();

  @Before
  public void setUp() {
    bereitsErfassteKursorte.add(new Kursort("Saal Test1", true));
    bereitsErfassteKursorte.add(new Kursort("Saal Test2", true));
    bereitsErfassteKursorte.add(new Kursort("Saal Test3", true));
  }

  @Test
  public void testExecute_BezeichnungBereitsInVerwendung() {
    Kursort kursort = new Kursort("Saal Test2", true);
    CheckKursortBezeichnungBereitsInVerwendungCommand
        checkKursortBezeichnungBereitsInVerwendungCommand =
            new CheckKursortBezeichnungBereitsInVerwendungCommand(
                kursort, null, bereitsErfassteKursorte);
    commandInvoker.executeCommand(checkKursortBezeichnungBereitsInVerwendungCommand);
    assertTrue(checkKursortBezeichnungBereitsInVerwendungCommand.isBereitsInVerwendung());
  }

  @Test
  public void testExecute_BezeichnungNochNichtInVerwendung() {
    Kursort kursort = new Kursort("Saal Test4", true);
    CheckKursortBezeichnungBereitsInVerwendungCommand
        checkKursortBezeichnungBereitsInVerwendungCommand =
            new CheckKursortBezeichnungBereitsInVerwendungCommand(
                kursort, null, bereitsErfassteKursorte);
    commandInvoker.executeCommand(checkKursortBezeichnungBereitsInVerwendungCommand);
    assertFalse(checkKursortBezeichnungBereitsInVerwendungCommand.isBereitsInVerwendung());
  }

  @Test
  public void testExecute_KursortOrigin() {
    Kursort kursort = new Kursort("Saal Test1", true);
    CheckKursortBezeichnungBereitsInVerwendungCommand
        checkKursortBezeichnungBereitsInVerwendungCommand =
            new CheckKursortBezeichnungBereitsInVerwendungCommand(
                kursort, bereitsErfassteKursorte.get(0), bereitsErfassteKursorte);
    commandInvoker.executeCommand(checkKursortBezeichnungBereitsInVerwendungCommand);
    assertFalse(checkKursortBezeichnungBereitsInVerwendungCommand.isBereitsInVerwendung());
  }
}
