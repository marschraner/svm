package ch.metzenthin.svm.domain.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ch.metzenthin.svm.persistence.entities.Dispensation;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Martin Schraner
 */
@SuppressWarnings("java:S5976")
class CheckDispensationUeberlapptAndereDispensationenCommandTest {

  private final CommandInvoker commandInvoker = new CommandInvokerImpl();
  private final List<Dispensation> bereitsErfassteDispensationen = new ArrayList<>();

  @BeforeEach
  void setUp() {
    bereitsErfassteDispensationen.add(
        new Dispensation(
            new GregorianCalendar(2010, Calendar.JULY, 1),
            new GregorianCalendar(2010, Calendar.AUGUST, 31),
            null,
            null));
    bereitsErfassteDispensationen.add(
        new Dispensation(
            new GregorianCalendar(2011, Calendar.JANUARY, 1),
            new GregorianCalendar(2011, Calendar.AUGUST, 31),
            null,
            null));
    bereitsErfassteDispensationen.add(
        new Dispensation(new GregorianCalendar(2015, Calendar.JULY, 1), null, null, null));
  }

  @Test
  void testExecute_NichtUeberlappend() {
    Dispensation dispensation =
        new Dispensation(
            new GregorianCalendar(2010, Calendar.SEPTEMBER, 1),
            new GregorianCalendar(2010, Calendar.DECEMBER, 31),
            null,
            null);
    CheckDispensationUeberlapptAndereDispensationenCommand
        checkDispensationUeberlapptAndereDispensationenCommand =
            new CheckDispensationUeberlapptAndereDispensationenCommand(
                dispensation, null, bereitsErfassteDispensationen);
    commandInvoker.executeCommand(checkDispensationUeberlapptAndereDispensationenCommand);
    assertFalse(checkDispensationUeberlapptAndereDispensationenCommand.isUeberlappt());
  }

  @Test
  void testExecute_UeberlappendAmEnde() {
    Dispensation dispensation =
        new Dispensation(
            new GregorianCalendar(2010, Calendar.SEPTEMBER, 1),
            new GregorianCalendar(2011, Calendar.JANUARY, 2),
            null,
            null);
    CheckDispensationUeberlapptAndereDispensationenCommand
        checkDispensationUeberlapptAndereDispensationenCommand =
            new CheckDispensationUeberlapptAndereDispensationenCommand(
                dispensation, null, bereitsErfassteDispensationen);
    commandInvoker.executeCommand(checkDispensationUeberlapptAndereDispensationenCommand);
    assertTrue(checkDispensationUeberlapptAndereDispensationenCommand.isUeberlappt());
  }

  @Test
  void testExecute_UeberlappendAmAnfang() {
    Dispensation dispensation =
        new Dispensation(
            new GregorianCalendar(2010, Calendar.AUGUST, 30),
            new GregorianCalendar(2010, Calendar.DECEMBER, 31),
            null,
            null);
    CheckDispensationUeberlapptAndereDispensationenCommand
        checkDispensationUeberlapptAndereDispensationenCommand =
            new CheckDispensationUeberlapptAndereDispensationenCommand(
                dispensation, null, bereitsErfassteDispensationen);
    commandInvoker.executeCommand(checkDispensationUeberlapptAndereDispensationenCommand);
    assertTrue(checkDispensationUeberlapptAndereDispensationenCommand.isUeberlappt());
  }

  @Test
  void testExecute_EndeIdentischMitBereitsErfasstemAnfang() {
    Dispensation dispensation =
        new Dispensation(
            new GregorianCalendar(2010, Calendar.SEPTEMBER, 1),
            new GregorianCalendar(2011, Calendar.JANUARY, 1),
            null,
            null);
    CheckDispensationUeberlapptAndereDispensationenCommand
        checkDispensationUeberlapptAndereDispensationenCommand =
            new CheckDispensationUeberlapptAndereDispensationenCommand(
                dispensation, null, bereitsErfassteDispensationen);
    commandInvoker.executeCommand(checkDispensationUeberlapptAndereDispensationenCommand);
    assertTrue(checkDispensationUeberlapptAndereDispensationenCommand.isUeberlappt());
  }

  @Test
  void testExecute_AnfangIdentischMitBereitsErfasstemEnde() {
    Dispensation dispensation =
        new Dispensation(
            new GregorianCalendar(2010, Calendar.AUGUST, 31),
            new GregorianCalendar(2010, Calendar.DECEMBER, 31),
            null,
            null);
    CheckDispensationUeberlapptAndereDispensationenCommand
        checkDispensationUeberlapptAndereDispensationenCommand =
            new CheckDispensationUeberlapptAndereDispensationenCommand(
                dispensation, null, bereitsErfassteDispensationen);
    commandInvoker.executeCommand(checkDispensationUeberlapptAndereDispensationenCommand);
    assertTrue(checkDispensationUeberlapptAndereDispensationenCommand.isUeberlappt());
  }

  @Test
  void testExecute_AlleUeberlappend() {
    Dispensation dispensation =
        new Dispensation(
            new GregorianCalendar(2008, Calendar.AUGUST, 31),
            new GregorianCalendar(2015, Calendar.DECEMBER, 31),
            null,
            null);
    CheckDispensationUeberlapptAndereDispensationenCommand
        checkDispensationUeberlapptAndereDispensationenCommand =
            new CheckDispensationUeberlapptAndereDispensationenCommand(
                dispensation, null, bereitsErfassteDispensationen);
    commandInvoker.executeCommand(checkDispensationUeberlapptAndereDispensationenCommand);
    assertTrue(checkDispensationUeberlapptAndereDispensationenCommand.isUeberlappt());
  }

  @Test
  void testExecute_NachHintenOffenePeriodeUeberlappend() {
    Dispensation dispensation =
        new Dispensation(new GregorianCalendar(2015, Calendar.AUGUST, 31), null, null, null);
    CheckDispensationUeberlapptAndereDispensationenCommand
        checkDispensationUeberlapptAndereDispensationenCommand =
            new CheckDispensationUeberlapptAndereDispensationenCommand(
                dispensation, null, bereitsErfassteDispensationen);
    commandInvoker.executeCommand(checkDispensationUeberlapptAndereDispensationenCommand);
    assertTrue(checkDispensationUeberlapptAndereDispensationenCommand.isUeberlappt());
  }

  @Test
  void testExecute_DispensationOrig() {
    Dispensation dispensation =
        new Dispensation(
            new GregorianCalendar(2010, Calendar.JULY, 2),
            new GregorianCalendar(2010, Calendar.AUGUST, 30),
            null,
            null);
    Dispensation dispensationOrig =
        new Dispensation(
            new GregorianCalendar(2010, Calendar.JULY, 1),
            new GregorianCalendar(2010, Calendar.AUGUST, 31),
            null,
            null);
    CheckDispensationUeberlapptAndereDispensationenCommand
        checkDispensationUeberlapptAndereDispensationenCommand =
            new CheckDispensationUeberlapptAndereDispensationenCommand(
                dispensation, dispensationOrig, bereitsErfassteDispensationen);
    commandInvoker.executeCommand(checkDispensationUeberlapptAndereDispensationenCommand);
    assertFalse(checkDispensationUeberlapptAndereDispensationenCommand.isUeberlappt());
  }
}
