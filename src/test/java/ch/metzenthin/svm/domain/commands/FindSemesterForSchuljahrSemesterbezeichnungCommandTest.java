package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Semesterbezeichnung;
import ch.metzenthin.svm.persistence.entities.Semester;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Martin Schraner
 */
public class FindSemesterForSchuljahrSemesterbezeichnungCommandTest {

    private final CommandInvoker commandInvoker = new CommandInvokerImpl();
    private final List<Semester> erfassteSemesters = new ArrayList<>();

    @Before
    public void setUp() {
        erfassteSemesters.add(new Semester("2011/2012", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(2011, Calendar.AUGUST, 20), new GregorianCalendar(2012, Calendar.FEBRUARY, 10), new GregorianCalendar(2012, Calendar.OCTOBER, 5), new GregorianCalendar(2012, Calendar.OCTOBER, 17), new GregorianCalendar(2012, Calendar.DECEMBER, 21), new GregorianCalendar(2013, Calendar.JANUARY, 2)));
        erfassteSemesters.add(new Semester("2011/2012", Semesterbezeichnung.ZWEITES_SEMESTER, new GregorianCalendar(2012, Calendar.FEBRUARY, 20), new GregorianCalendar(2012, Calendar.JULY, 10), new GregorianCalendar(2011, Calendar.APRIL, 25), new GregorianCalendar(2011, Calendar.MAY, 7), null, null));
    }

    @Test
    public void testExecute_SemesterFound() {
        FindSemesterForSchuljahrSemesterbezeichnungCommand findSemesterForSchuljahrSemesterbezeichnungCommand = new FindSemesterForSchuljahrSemesterbezeichnungCommand("2011/2012", Semesterbezeichnung.ERSTES_SEMESTER, erfassteSemesters);
        commandInvoker.executeCommand(findSemesterForSchuljahrSemesterbezeichnungCommand);
        assertEquals(erfassteSemesters.get(0), findSemesterForSchuljahrSemesterbezeichnungCommand.getSemesterFound());
        findSemesterForSchuljahrSemesterbezeichnungCommand = new FindSemesterForSchuljahrSemesterbezeichnungCommand("2011/2012", Semesterbezeichnung.ZWEITES_SEMESTER, erfassteSemesters);
        commandInvoker.executeCommand(findSemesterForSchuljahrSemesterbezeichnungCommand);
        assertEquals(erfassteSemesters.get(1), findSemesterForSchuljahrSemesterbezeichnungCommand.getSemesterFound());
    }

    @Test
    public void testExecute_NoSemesterFound() {
        FindSemesterForSchuljahrSemesterbezeichnungCommand findSemesterForSchuljahrSemesterbezeichnungCommand = new FindSemesterForSchuljahrSemesterbezeichnungCommand("2012/2013", Semesterbezeichnung.ERSTES_SEMESTER, erfassteSemesters);
        commandInvoker.executeCommand(findSemesterForSchuljahrSemesterbezeichnungCommand);
        assertNull(findSemesterForSchuljahrSemesterbezeichnungCommand.getSemesterFound());
    }
}