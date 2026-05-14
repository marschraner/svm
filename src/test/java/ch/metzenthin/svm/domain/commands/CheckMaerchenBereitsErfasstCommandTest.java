package ch.metzenthin.svm.domain.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ch.metzenthin.svm.persistence.entities.Maerchen;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Martin Schraner
 */
class CheckMaerchenBereitsErfasstCommandTest {

  private final CommandInvoker commandInvoker = new CommandInvokerImpl();
  private final List<Maerchen> bereitsErfassteMaerchens = new ArrayList<>();

  @BeforeEach
  void setUp() {
    bereitsErfassteMaerchens.add(new Maerchen("1911/1912", "Schneewittchen", 7));
    bereitsErfassteMaerchens.add(new Maerchen("1912/1913", "Gestiefelter Kater", 8));
  }

  @Test
  void testExecute_BereitsErfasst() {
    Maerchen maerchen = new Maerchen("1911/1912", "Hans im Glück", 9);
    CheckMaerchenBereitsErfasstCommand checkMaerchenBereitsErfasstCommand =
        new CheckMaerchenBereitsErfasstCommand(maerchen, null, bereitsErfassteMaerchens);
    commandInvoker.executeCommand(checkMaerchenBereitsErfasstCommand);
    assertTrue(checkMaerchenBereitsErfasstCommand.isBereitsErfasst());
  }

  @Test
  void testExecute_MaerchenNochNichtErfasst() {
    Maerchen maerchen = new Maerchen("1913/1914", "Schneewittchen", 7);
    CheckMaerchenBereitsErfasstCommand checkMaerchenBereitsErfasstCommand =
        new CheckMaerchenBereitsErfasstCommand(maerchen, null, bereitsErfassteMaerchens);
    commandInvoker.executeCommand(checkMaerchenBereitsErfasstCommand);
    assertFalse(checkMaerchenBereitsErfasstCommand.isBereitsErfasst());
  }

  @Test
  void testExecute_MaerchenOrigin() {
    Maerchen maerchen = new Maerchen("1911/1912", "Schneewittchen", 7);
    CheckMaerchenBereitsErfasstCommand checkMaerchenBereitsErfasstCommand =
        new CheckMaerchenBereitsErfasstCommand(
            maerchen, bereitsErfassteMaerchens.get(0), bereitsErfassteMaerchens);
    commandInvoker.executeCommand(checkMaerchenBereitsErfasstCommand);
    assertFalse(checkMaerchenBereitsErfasstCommand.isBereitsErfasst());
  }
}
