package ch.metzenthin.svm.domain.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import ch.metzenthin.svm.persistence.entities.Semester;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Martin Schraner
 */
public class CheckSemesterUeberlapptAndereSemesterCommandTest {

  private final CommandInvoker commandInvoker = new CommandInvokerImpl();
  private final List<Semester> bereitsErfassteSemester = new ArrayList<>();

  @Before
  public void setUp() {
    bereitsErfassteSemester.add(
        new Semester(
            null,
            null,
            new GregorianCalendar(2010, Calendar.JULY, 1),
            new GregorianCalendar(2010, Calendar.AUGUST, 31),
            null,
            null,
            null,
            null));
    bereitsErfassteSemester.add(
        new Semester(
            null,
            null,
            new GregorianCalendar(2011, Calendar.JANUARY, 1),
            new GregorianCalendar(2011, Calendar.AUGUST, 31),
            null,
            null,
            null,
            null));
  }

  @Test
  public void testExecute_NichtUeberlappend() {
    Semester semester =
        new Semester(
            null,
            null,
            new GregorianCalendar(2010, Calendar.SEPTEMBER, 1),
            new GregorianCalendar(2010, Calendar.DECEMBER, 31),
            null,
            null,
            null,
            null);
    CheckSemesterUeberlapptAndereSemesterCommand checkSemesterUeberlapptAndereSemesterCommand =
        new CheckSemesterUeberlapptAndereSemesterCommand(semester, null, bereitsErfassteSemester);
    commandInvoker.executeCommand(checkSemesterUeberlapptAndereSemesterCommand);
    assertFalse(checkSemesterUeberlapptAndereSemesterCommand.isUeberlappt());
  }

  @Test
  public void testExecute_UeberlappendAmEnde() {
    Semester semester =
        new Semester(
            null,
            null,
            new GregorianCalendar(2010, Calendar.SEPTEMBER, 1),
            new GregorianCalendar(2011, Calendar.JANUARY, 2),
            null,
            null,
            null,
            null);
    CheckSemesterUeberlapptAndereSemesterCommand checkSemesterUeberlapptAndereSemesterCommand =
        new CheckSemesterUeberlapptAndereSemesterCommand(semester, null, bereitsErfassteSemester);
    commandInvoker.executeCommand(checkSemesterUeberlapptAndereSemesterCommand);
    assertTrue(checkSemesterUeberlapptAndereSemesterCommand.isUeberlappt());
  }

  @Test
  public void testExecute_UeberlappendAmAnfang() {
    Semester semester =
        new Semester(
            null,
            null,
            new GregorianCalendar(2010, Calendar.AUGUST, 30),
            new GregorianCalendar(2010, Calendar.DECEMBER, 31),
            null,
            null,
            null,
            null);
    CheckSemesterUeberlapptAndereSemesterCommand checkSemesterUeberlapptAndereSemesterCommand =
        new CheckSemesterUeberlapptAndereSemesterCommand(semester, null, bereitsErfassteSemester);
    commandInvoker.executeCommand(checkSemesterUeberlapptAndereSemesterCommand);
    assertTrue(checkSemesterUeberlapptAndereSemesterCommand.isUeberlappt());
  }

  @Test
  public void testExecute_EndeIdentischMitBereitsErfasstemAnfang() {
    Semester semester =
        new Semester(
            null,
            null,
            new GregorianCalendar(2010, Calendar.SEPTEMBER, 1),
            new GregorianCalendar(2011, Calendar.JANUARY, 1),
            null,
            null,
            null,
            null);
    CheckSemesterUeberlapptAndereSemesterCommand checkSemesterUeberlapptAndereSemesterCommand =
        new CheckSemesterUeberlapptAndereSemesterCommand(semester, null, bereitsErfassteSemester);
    commandInvoker.executeCommand(checkSemesterUeberlapptAndereSemesterCommand);
    assertTrue(checkSemesterUeberlapptAndereSemesterCommand.isUeberlappt());
  }

  @Test
  public void testExecute_AnfangIdentischMitBereitsErfasstemEnde() {
    Semester semester =
        new Semester(
            null,
            null,
            new GregorianCalendar(2010, Calendar.AUGUST, 31),
            new GregorianCalendar(2010, Calendar.DECEMBER, 31),
            null,
            null,
            null,
            null);
    CheckSemesterUeberlapptAndereSemesterCommand checkSemesterUeberlapptAndereSemesterCommand =
        new CheckSemesterUeberlapptAndereSemesterCommand(semester, null, bereitsErfassteSemester);
    commandInvoker.executeCommand(checkSemesterUeberlapptAndereSemesterCommand);
    assertTrue(checkSemesterUeberlapptAndereSemesterCommand.isUeberlappt());
  }

  @Test
  public void testExecute_SemesterOrig() {
    Semester semester =
        new Semester(
            null,
            null,
            new GregorianCalendar(2010, Calendar.JULY, 2),
            new GregorianCalendar(2010, Calendar.AUGUST, 30),
            null,
            null,
            null,
            null);
    Semester semesterOrig =
        new Semester(
            null,
            null,
            new GregorianCalendar(2010, Calendar.JULY, 1),
            new GregorianCalendar(2010, Calendar.AUGUST, 31),
            null,
            null,
            null,
            null);
    CheckSemesterUeberlapptAndereSemesterCommand checkSemesterUeberlapptAndereSemesterCommand =
        new CheckSemesterUeberlapptAndereSemesterCommand(
            semester, semesterOrig, bereitsErfassteSemester);
    commandInvoker.executeCommand(checkSemesterUeberlapptAndereSemesterCommand);
    assertFalse(checkSemesterUeberlapptAndereSemesterCommand.isUeberlappt());
  }
}
