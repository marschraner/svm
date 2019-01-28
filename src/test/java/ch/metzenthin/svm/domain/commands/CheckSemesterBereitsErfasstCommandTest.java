package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Semesterbezeichnung;
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
public class CheckSemesterBereitsErfasstCommandTest {

    private final CommandInvoker commandInvoker = new CommandInvokerImpl();
    private final List<Semester> bereitsErfassteSemesters = new ArrayList<>();

    @Before
    public void setUp() {
        bereitsErfassteSemesters.add(new Semester("2011/2012", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(2011, Calendar.AUGUST, 20), new GregorianCalendar(2012, Calendar.FEBRUARY, 10), new GregorianCalendar(2011, Calendar.OCTOBER, 5), new GregorianCalendar(2011, Calendar.OCTOBER, 17), new GregorianCalendar(2011, Calendar.DECEMBER, 21), new GregorianCalendar(2012, Calendar.JANUARY, 2)));
        bereitsErfassteSemesters.add(new Semester("2011/2012", Semesterbezeichnung.ZWEITES_SEMESTER, new GregorianCalendar(2012, Calendar.FEBRUARY, 20), new GregorianCalendar(2012, Calendar.JULY, 10), new GregorianCalendar(2012, Calendar.APRIL, 25), new GregorianCalendar(2012, Calendar.MAY, 7), null, null));
    }

    @Test
    public void testExecute_BereitsErfasst() {
        Semester semester = new Semester("2011/2012", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(2011, Calendar.AUGUST, 28), new GregorianCalendar(2012, Calendar.FEBRUARY, 12), new GregorianCalendar(2011, Calendar.OCTOBER, 5), new GregorianCalendar(2011, Calendar.OCTOBER, 17), new GregorianCalendar(2011, Calendar.DECEMBER, 21), new GregorianCalendar(2012, Calendar.JANUARY, 2));
        CheckSemesterBereitsErfasstCommand checkSemesterBereitsErfasstCommand = new CheckSemesterBereitsErfasstCommand(semester, null, bereitsErfassteSemesters);
        commandInvoker.executeCommand(checkSemesterBereitsErfasstCommand);
        assertTrue(checkSemesterBereitsErfasstCommand.isBereitsErfasst());
    }

    @Test
    public void testExecute_SemesterNochNichtErfasst() {
        Semester semester = new Semester("2012/2013", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(2013, Calendar.AUGUST, 20), new GregorianCalendar(2013, Calendar.FEBRUARY, 10), new GregorianCalendar(2012, Calendar.OCTOBER, 5), new GregorianCalendar(2012, Calendar.OCTOBER, 17), new GregorianCalendar(2012, Calendar.DECEMBER, 21), new GregorianCalendar(2013, Calendar.JANUARY, 2));
        CheckSemesterBereitsErfasstCommand checkSemesterBereitsErfasstCommand = new CheckSemesterBereitsErfasstCommand(semester, null, bereitsErfassteSemesters);
        commandInvoker.executeCommand(checkSemesterBereitsErfasstCommand);
        assertFalse(checkSemesterBereitsErfasstCommand.isBereitsErfasst());
    }

    @Test
    public void testExecute_SemesterOrigin() {
        Semester semester = new Semester("2011/2012", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(2011, Calendar.AUGUST, 20), new GregorianCalendar(2012, Calendar.FEBRUARY, 10), new GregorianCalendar(2011, Calendar.OCTOBER, 5), new GregorianCalendar(2011, Calendar.OCTOBER, 17), new GregorianCalendar(2011, Calendar.DECEMBER, 21), new GregorianCalendar(2012, Calendar.JANUARY, 2));
        CheckSemesterBereitsErfasstCommand checkSemesterBereitsErfasstCommand = new CheckSemesterBereitsErfasstCommand(semester, bereitsErfassteSemesters.get(0), bereitsErfassteSemesters);
        commandInvoker.executeCommand(checkSemesterBereitsErfasstCommand);
        assertFalse(checkSemesterBereitsErfasstCommand.isBereitsErfasst());
    }
}