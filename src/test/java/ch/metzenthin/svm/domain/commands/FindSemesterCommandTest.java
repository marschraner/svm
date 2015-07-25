package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.dataTypes.Semesterbezeichnung;
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
public class FindSemesterCommandTest {

    private CommandInvoker commandInvoker = new CommandInvokerImpl();
    private List<Semester> erfassteSemesters = new ArrayList<>();

    @Before
    public void setUp() {
        erfassteSemesters.add(new Semester("2011/2012", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(2011, Calendar.AUGUST, 20), new GregorianCalendar(2012, Calendar.FEBRUARY, 10), 21));
        erfassteSemesters.add(new Semester("2011/2012", Semesterbezeichnung.ZWEITES_SEMESTER, new GregorianCalendar(2012, Calendar.FEBRUARY, 20), new GregorianCalendar(2012, Calendar.JULY, 10), 21));
    }

    @Test
    public void testExecute_SemesterFound() throws Exception {
        FindSemesterCommand findSemesterCommand = new FindSemesterCommand("2011/2012", Semesterbezeichnung.ERSTES_SEMESTER, erfassteSemesters);
        commandInvoker.executeCommand(findSemesterCommand);
        assertEquals(erfassteSemesters.get(0), findSemesterCommand.getSemesterFound());
        findSemesterCommand = new FindSemesterCommand("2011/2012", Semesterbezeichnung.ZWEITES_SEMESTER, erfassteSemesters);
        commandInvoker.executeCommand(findSemesterCommand);
        assertEquals(erfassteSemesters.get(1), findSemesterCommand.getSemesterFound());
    }

    @Test
    public void testExecute_NoSemesterFound() throws Exception {
        FindSemesterCommand findSemesterCommand = new FindSemesterCommand("2012/2013", Semesterbezeichnung.ERSTES_SEMESTER, erfassteSemesters);
        commandInvoker.executeCommand(findSemesterCommand);
        assertNull(findSemesterCommand.getSemesterFound());
    }
}