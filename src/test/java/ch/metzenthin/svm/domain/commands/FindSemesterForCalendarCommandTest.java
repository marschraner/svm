package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Semesterbezeichnung;
import ch.metzenthin.svm.persistence.entities.Semester;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Martin Schraner
 */
public class FindSemesterForCalendarCommandTest {

    private final CommandInvoker commandInvoker = new CommandInvokerImpl();
    private final List<Semester> erfassteSemester = new ArrayList<>();

    @Before
    public void setUp() {
        erfassteSemester.add(new Semester("2011/2012", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(2011, Calendar.AUGUST, 20), new GregorianCalendar(2012, Calendar.FEBRUARY, 10), new GregorianCalendar(2011, Calendar.OCTOBER, 5), new GregorianCalendar(2011, Calendar.OCTOBER, 17), new GregorianCalendar(2011, Calendar.DECEMBER, 21), new GregorianCalendar(2012, Calendar.JANUARY, 2)));
        erfassteSemester.add(new Semester("2011/2012", Semesterbezeichnung.ZWEITES_SEMESTER, new GregorianCalendar(2012, Calendar.FEBRUARY, 20), new GregorianCalendar(2012, Calendar.JULY, 10), new GregorianCalendar(2012, Calendar.APRIL, 25), new GregorianCalendar(2012, Calendar.MAY, 7), null, null));
        erfassteSemester.add(new Semester("2012/2013", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(2012, Calendar.AUGUST, 21), new GregorianCalendar(2013, Calendar.FEBRUARY, 11), new GregorianCalendar(2012, Calendar.OCTOBER, 5), new GregorianCalendar(2012, Calendar.OCTOBER, 17), new GregorianCalendar(2012, Calendar.DECEMBER, 21), new GregorianCalendar(2013, Calendar.JANUARY, 2)));
    }

    @Test
    public void testExecute_CalendarWaehrendErstemSemester() {
        FindSemesterForCalendarCommand findSemesterForCalendarCommand = new FindSemesterForCalendarCommand(new GregorianCalendar(2011, Calendar.AUGUST, 21), erfassteSemester);
        commandInvoker.executeCommand(findSemesterForCalendarCommand);
        assertNull(findSemesterForCalendarCommand.getPreviousSemester());
        assertEquals(new GregorianCalendar(2011, Calendar.AUGUST, 20), findSemesterForCalendarCommand.getCurrentSemester().getSemesterbeginn());
        assertEquals(new GregorianCalendar(2012, Calendar.FEBRUARY, 20), findSemesterForCalendarCommand.getNextSemester().getSemesterbeginn());
    }

    @Test
    public void testExecute_CalendarWaehrendMittleremSemester() {
        FindSemesterForCalendarCommand findSemesterForCalendarCommand = new FindSemesterForCalendarCommand(new GregorianCalendar(2012, Calendar.FEBRUARY, 21), erfassteSemester);
        commandInvoker.executeCommand(findSemesterForCalendarCommand);
        assertEquals(new GregorianCalendar(2011, Calendar.AUGUST, 20), findSemesterForCalendarCommand.getPreviousSemester().getSemesterbeginn());
        assertEquals(new GregorianCalendar(2012, Calendar.FEBRUARY, 20), findSemesterForCalendarCommand.getCurrentSemester().getSemesterbeginn());
        assertEquals(new GregorianCalendar(2012, Calendar.AUGUST, 21), findSemesterForCalendarCommand.getNextSemester().getSemesterbeginn());
    }

    @Test
    public void testExecute_CalendarWaehrendMittleremSemesterErsterTag() {
        FindSemesterForCalendarCommand findSemesterForCalendarCommand = new FindSemesterForCalendarCommand(new GregorianCalendar(2012, Calendar.FEBRUARY, 20), erfassteSemester);
        commandInvoker.executeCommand(findSemesterForCalendarCommand);
        assertEquals(new GregorianCalendar(2011, Calendar.AUGUST, 20), findSemesterForCalendarCommand.getPreviousSemester().getSemesterbeginn());
        assertEquals(new GregorianCalendar(2012, Calendar.FEBRUARY, 20), findSemesterForCalendarCommand.getCurrentSemester().getSemesterbeginn());
        assertEquals(new GregorianCalendar(2012, Calendar.AUGUST, 21), findSemesterForCalendarCommand.getNextSemester().getSemesterbeginn());
    }

    @Test
    public void testExecute_CalendarWaehrendMittleremSemesterLetzterTag() {
        FindSemesterForCalendarCommand findSemesterForCalendarCommand = new FindSemesterForCalendarCommand(new GregorianCalendar(2012, Calendar.JULY, 10), erfassteSemester);
        commandInvoker.executeCommand(findSemesterForCalendarCommand);
        assertEquals(new GregorianCalendar(2011, Calendar.AUGUST, 20), findSemesterForCalendarCommand.getPreviousSemester().getSemesterbeginn());
        assertEquals(new GregorianCalendar(2012, Calendar.FEBRUARY, 20), findSemesterForCalendarCommand.getCurrentSemester().getSemesterbeginn());
        assertEquals(new GregorianCalendar(2012, Calendar.AUGUST, 21), findSemesterForCalendarCommand.getNextSemester().getSemesterbeginn());
    }

    @Test
    public void testExecute_CalendarWaehrendLetztemSemester() {
        FindSemesterForCalendarCommand findSemesterForCalendarCommand = new FindSemesterForCalendarCommand(new GregorianCalendar(2012, Calendar.AUGUST, 21), erfassteSemester);
        commandInvoker.executeCommand(findSemesterForCalendarCommand);
        assertEquals(new GregorianCalendar(2012, Calendar.FEBRUARY, 20), findSemesterForCalendarCommand.getPreviousSemester().getSemesterbeginn());
        assertEquals(new GregorianCalendar(2012, Calendar.AUGUST, 21), findSemesterForCalendarCommand.getCurrentSemester().getSemesterbeginn());
        assertNull(findSemesterForCalendarCommand.getNextSemester());
    }

    @Test
    public void testExecute_CalendarVorErstemSemester() {
        FindSemesterForCalendarCommand findSemesterForCalendarCommand = new FindSemesterForCalendarCommand(new GregorianCalendar(2011, Calendar.AUGUST, 19), erfassteSemester);
        commandInvoker.executeCommand(findSemesterForCalendarCommand);
        assertNull(findSemesterForCalendarCommand.getPreviousSemester());
        assertNull(findSemesterForCalendarCommand.getCurrentSemester());
        assertEquals(new GregorianCalendar(2011, Calendar.AUGUST, 20), findSemesterForCalendarCommand.getNextSemester().getSemesterbeginn());
    }

    @Test
    public void testExecute_CalendarZwischenZweiSemestern() {
        FindSemesterForCalendarCommand findSemesterForCalendarCommand = new FindSemesterForCalendarCommand(new GregorianCalendar(2012, Calendar.FEBRUARY, 11), erfassteSemester);
        commandInvoker.executeCommand(findSemesterForCalendarCommand);
        assertEquals(new GregorianCalendar(2011, Calendar.AUGUST, 20), findSemesterForCalendarCommand.getPreviousSemester().getSemesterbeginn());
        assertNull(findSemesterForCalendarCommand.getCurrentSemester());
        assertEquals(new GregorianCalendar(2012, Calendar.FEBRUARY, 20), findSemesterForCalendarCommand.getNextSemester().getSemesterbeginn());
    }

    @Test
    public void testExecute_NachLetztemSemester() {
        FindSemesterForCalendarCommand findSemesterForCalendarCommand = new FindSemesterForCalendarCommand(new GregorianCalendar(2013, Calendar.FEBRUARY, 12), erfassteSemester);
        commandInvoker.executeCommand(findSemesterForCalendarCommand);
        assertEquals(new GregorianCalendar(2012, Calendar.AUGUST, 21), findSemesterForCalendarCommand.getPreviousSemester().getSemesterbeginn());
        assertNull(findSemesterForCalendarCommand.getCurrentSemester());
        assertNull(findSemesterForCalendarCommand.getNextSemester());
    }

    @Test
    public void testExecute_ErfassteSemesterNurEinSemester_WaehrendSemester() {
        List<Semester> erfassteSemester1 = new ArrayList<>();
        erfassteSemester1.add(new Semester("2011/2012", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(2011, Calendar.AUGUST, 20), new GregorianCalendar(2012, Calendar.FEBRUARY, 10), new GregorianCalendar(2011, Calendar.OCTOBER, 5), new GregorianCalendar(2011, Calendar.OCTOBER, 17), new GregorianCalendar(2011, Calendar.DECEMBER, 21), new GregorianCalendar(2012, Calendar.JANUARY, 2)));
        FindSemesterForCalendarCommand findSemesterForCalendarCommand = new FindSemesterForCalendarCommand(new GregorianCalendar(2011, Calendar.AUGUST, 21), erfassteSemester1);
        commandInvoker.executeCommand(findSemesterForCalendarCommand);
        assertNull(findSemesterForCalendarCommand.getPreviousSemester());
        assertEquals(new GregorianCalendar(2011, Calendar.AUGUST, 20), findSemesterForCalendarCommand.getCurrentSemester().getSemesterbeginn());
        assertNull(findSemesterForCalendarCommand.getNextSemester());
    }

    @Test
    public void testExecute_ErfassteSemesterNurEinSemester_VorSemester() {
        List<Semester> erfassteSemester1 = new ArrayList<>();
        erfassteSemester1.add(new Semester("2011/2012", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(2011, Calendar.AUGUST, 20), new GregorianCalendar(2012, Calendar.FEBRUARY, 10), new GregorianCalendar(2011, Calendar.OCTOBER, 5), new GregorianCalendar(2011, Calendar.OCTOBER, 17), new GregorianCalendar(2011, Calendar.DECEMBER, 21), new GregorianCalendar(2012, Calendar.JANUARY, 2)));
        FindSemesterForCalendarCommand findSemesterForCalendarCommand = new FindSemesterForCalendarCommand(new GregorianCalendar(2011, Calendar.AUGUST, 19), erfassteSemester1);
        commandInvoker.executeCommand(findSemesterForCalendarCommand);
        assertNull(findSemesterForCalendarCommand.getPreviousSemester());
        assertNull(findSemesterForCalendarCommand.getCurrentSemester());
        assertEquals(new GregorianCalendar(2011, Calendar.AUGUST, 20), findSemesterForCalendarCommand.getNextSemester().getSemesterbeginn());
    }

    @Test
    public void testExecute_ErfassteSemesterNurEinSemester_NachSemester() {
        List<Semester> erfassteSemester1 = new ArrayList<>();
        erfassteSemester1.add(new Semester("2011/2012", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(2011, Calendar.AUGUST, 20), new GregorianCalendar(2012, Calendar.FEBRUARY, 10), new GregorianCalendar(2011, Calendar.OCTOBER, 5), new GregorianCalendar(2011, Calendar.OCTOBER, 17), new GregorianCalendar(2011, Calendar.DECEMBER, 21), new GregorianCalendar(2012, Calendar.JANUARY, 2)));
        FindSemesterForCalendarCommand findSemesterForCalendarCommand = new FindSemesterForCalendarCommand(new GregorianCalendar(2012, Calendar.FEBRUARY, 11), erfassteSemester1);
        commandInvoker.executeCommand(findSemesterForCalendarCommand);
        assertEquals(new GregorianCalendar(2011, Calendar.AUGUST, 20), findSemesterForCalendarCommand.getPreviousSemester().getSemesterbeginn());
        assertNull(findSemesterForCalendarCommand.getCurrentSemester());
        assertNull(findSemesterForCalendarCommand.getNextSemester());
    }

}