package ch.metzenthin.svm.domain.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ch.metzenthin.svm.common.datatypes.Anrede;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Martin Schraner
 */
class CheckMitarbeiterBereitsErfasstCommandTest {

  private final CommandInvoker commandInvoker = new CommandInvokerImpl();
  private final List<Mitarbeiter> bereitsErfassteLehrkraefte = new ArrayList<>();

  @BeforeEach
  void setUp() {
    Mitarbeiter mitarbeiter =
        new Mitarbeiter(
            Anrede.FRAU,
            "Noémi",
            "Roos",
            new GregorianCalendar(1994, Calendar.MARCH, 18),
            "044 391 45 35",
            "076 384 45 35",
            "nroos@gmx.ch",
            "756.3943.8722.22",
            "CH31 8123 9000 0012 4568 9",
            true,
            "Mi, Fr, Sa",
            null,
            true);
    Adresse adresse = new Adresse("Rebwiesenstrasse", "54", "8702", "Zollikon");
    mitarbeiter.setAdresse(adresse);
    bereitsErfassteLehrkraefte.add(mitarbeiter);
  }

  @Test
  void testExecute_LehrkraftBereitsErfasst() {
    Mitarbeiter mitarbeiter =
        new Mitarbeiter(
            Anrede.FRAU,
            "Noémi",
            "Roos",
            new GregorianCalendar(1994, Calendar.MARCH, 18),
            "043 111 11 11",
            "076 384 45 35",
            "nroos@gmx.ch",
            "756.3943.8722.22",
            "CH31 8123 9000 0012 4568 9",
            true,
            "Mi, Fr, Sa",
            null,
            true);
    Adresse adresse = new Adresse("Rebwiesenstrasse", "77", "8702", "Zollikon");
    mitarbeiter.setAdresse(adresse);
    CheckMitarbeiterBereitsErfasstCommand checkMitarbeiterBereitsErfasstCommand =
        new CheckMitarbeiterBereitsErfasstCommand(mitarbeiter, null, bereitsErfassteLehrkraefte);
    commandInvoker.executeCommand(checkMitarbeiterBereitsErfasstCommand);
    assertTrue(checkMitarbeiterBereitsErfasstCommand.isBereitsErfasst());
  }

  @Test
  void testExecute_LehrkraftNochNichtErfasst() {
    Mitarbeiter mitarbeiter =
        new Mitarbeiter(
            Anrede.FRAU,
            "Nathalie",
            "Delley",
            new GregorianCalendar(1971, Calendar.DECEMBER, 16),
            "044 261 27 20",
            "076 338 05 36",
            "ndelley@sunrise.ch",
            "756.8274.3263.17",
            "CH31 8123 9000 2322 4568 9",
            true,
            "Mi, Fr, Sa",
            null,
            true);
    Adresse adresse = new Adresse("Im Schilf", "7", "8044", "Zürich");
    mitarbeiter.setAdresse(adresse);
    CheckMitarbeiterBereitsErfasstCommand checkMitarbeiterBereitsErfasstCommand =
        new CheckMitarbeiterBereitsErfasstCommand(mitarbeiter, null, bereitsErfassteLehrkraefte);
    commandInvoker.executeCommand(checkMitarbeiterBereitsErfasstCommand);
    assertFalse(checkMitarbeiterBereitsErfasstCommand.isBereitsErfasst());
  }

  @Test
  void testExecute_LehrkraftOrigin() {
    Mitarbeiter mitarbeiter =
        new Mitarbeiter(
            Anrede.FRAU,
            "Noémi",
            "Roos",
            new GregorianCalendar(1994, Calendar.MARCH, 18),
            "043 111 11 11",
            "076 384 45 35",
            "nroos@gmx.ch",
            "756.3943.8722.22",
            "CH31 8123 9000 0012 4568 9",
            true,
            "Mi, Fr, Sa",
            null,
            true);
    Adresse adresse = new Adresse("Rebwiesenstrasse", "77", "8702", "Zollikon");
    mitarbeiter.setAdresse(adresse);
    CheckMitarbeiterBereitsErfasstCommand checkMitarbeiterBereitsErfasstCommand =
        new CheckMitarbeiterBereitsErfasstCommand(
            mitarbeiter, bereitsErfassteLehrkraefte.get(0), bereitsErfassteLehrkraefte);
    commandInvoker.executeCommand(checkMitarbeiterBereitsErfasstCommand);
    assertFalse(checkMitarbeiterBereitsErfasstCommand.isBereitsErfasst());
  }
}
