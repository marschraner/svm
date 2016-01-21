package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.entities.Semester;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Martin Schraner
 */
public class CheckSemesterUeberlapptAndereSemesterCommandTest {

    private CommandInvoker commandInvoker = new CommandInvokerImpl();
    private List<Semester> bereitsErfassteSemester = new ArrayList<>();

    @Before
    public void setUp() {
        bereitsErfassteSemester.add(new Semester(null, null, new GregorianCalendar(2010, Calendar.JULY, 1), new GregorianCalendar(2010, Calendar.AUGUST, 31), null, null, null, null));
        bereitsErfassteSemester.add(new Semester(null, null, new GregorianCalendar(2011, Calendar.JANUARY, 1), new GregorianCalendar(2011, Calendar.AUGUST, 31), null, null, null, null));
    }

    @Test
    public void testExecute_NichtUeberlappend() throws Exception {
        Semester semester = new Semester(null, null, new GregorianCalendar(2010, Calendar.SEPTEMBER, 1), new GregorianCalendar(2010, Calendar.DECEMBER, 31), null, null, null, null);
        CheckSemesterUeberlapptAndereSemesterCommand checkSemesterUeberlapptAndereSemesterCommand = new CheckSemesterUeberlapptAndereSemesterCommand(semester, null, bereitsErfassteSemester);
        commandInvoker.executeCommand(checkSemesterUeberlapptAndereSemesterCommand);
        assertFalse(checkSemesterUeberlapptAndereSemesterCommand.isUeberlappt());
    }

    @Test
    public void testExecute_UeberlappendAmEnde() throws Exception {
        Semester semester = new Semester(null, null, new GregorianCalendar(2010, Calendar.SEPTEMBER, 1), new GregorianCalendar(2011, Calendar.JANUARY, 2), null, null, null, null);
        CheckSemesterUeberlapptAndereSemesterCommand checkSemesterUeberlapptAndereSemesterCommand = new CheckSemesterUeberlapptAndereSemesterCommand(semester, null, bereitsErfassteSemester);
        commandInvoker.executeCommand(checkSemesterUeberlapptAndereSemesterCommand);
        assertTrue(checkSemesterUeberlapptAndereSemesterCommand.isUeberlappt());
    }

    @Test
    public void testExecute_UeberlappendAmAnfang() throws Exception {
        Semester semester = new Semester(null, null, new GregorianCalendar(2010, Calendar.AUGUST, 30), new GregorianCalendar(2010, Calendar.DECEMBER, 31), null, null, null, null);
        CheckSemesterUeberlapptAndereSemesterCommand checkSemesterUeberlapptAndereSemesterCommand = new CheckSemesterUeberlapptAndereSemesterCommand(semester, null, bereitsErfassteSemester);
        commandInvoker.executeCommand(checkSemesterUeberlapptAndereSemesterCommand);
        assertTrue(checkSemesterUeberlapptAndereSemesterCommand.isUeberlappt());
    }

    @Test
    public void testExecute_EndeIdentischMitBereitsErfasstemAnfang() throws Exception {
        Semester semester = new Semester(null, null, new GregorianCalendar(2010, Calendar.SEPTEMBER, 1), new GregorianCalendar(2011, Calendar.JANUARY, 1), null, null, null, null);
        CheckSemesterUeberlapptAndereSemesterCommand checkSemesterUeberlapptAndereSemesterCommand = new CheckSemesterUeberlapptAndereSemesterCommand(semester, null, bereitsErfassteSemester);
        commandInvoker.executeCommand(checkSemesterUeberlapptAndereSemesterCommand);
        assertTrue(checkSemesterUeberlapptAndereSemesterCommand.isUeberlappt());
    }

    @Test
    public void testExecute_AnfangIdentischMitBereitsErfasstemEnde() throws Exception {
        Semester semester = new Semester(null, null, new GregorianCalendar(2010, Calendar.AUGUST, 31), new GregorianCalendar(2010, Calendar.DECEMBER, 31), null, null, null, null);
        CheckSemesterUeberlapptAndereSemesterCommand checkSemesterUeberlapptAndereSemesterCommand = new CheckSemesterUeberlapptAndereSemesterCommand(semester, null, bereitsErfassteSemester);
        commandInvoker.executeCommand(checkSemesterUeberlapptAndereSemesterCommand);
        assertTrue(checkSemesterUeberlapptAndereSemesterCommand.isUeberlappt());
    }

    @Test
    public void testExecute_SemesterOrig() throws Exception {
        Semester semester = new Semester(null, null, new GregorianCalendar(2010, Calendar.JULY, 2), new GregorianCalendar(2010, Calendar.AUGUST, 30), null, null, null, null);
        Semester semesterOrig = new Semester(null, null, new GregorianCalendar(2010, Calendar.JULY, 1), new GregorianCalendar(2010, Calendar.AUGUST, 31), null, null, null, null);
        CheckSemesterUeberlapptAndereSemesterCommand checkSemesterUeberlapptAndereSemesterCommand = new CheckSemesterUeberlapptAndereSemesterCommand(semester, semesterOrig, bereitsErfassteSemester);
        commandInvoker.executeCommand(checkSemesterUeberlapptAndereSemesterCommand);
        assertFalse(checkSemesterUeberlapptAndereSemesterCommand.isUeberlappt());
    }
}