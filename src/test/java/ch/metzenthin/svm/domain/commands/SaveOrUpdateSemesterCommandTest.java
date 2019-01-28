package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Semesterbezeichnung;
import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.daos.SemesterDao;
import ch.metzenthin.svm.persistence.entities.Semester;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;
import static org.junit.Assert.*;

/**
 * @author Martin Schraner
 */
public class SaveOrUpdateSemesterCommandTest {

    private final SemesterDao semesterDao = new SemesterDao();

    private DB db;
    private CommandInvoker commandInvoker;

    @Before
    public void setUp() throws Exception {
        createSvmPropertiesFileDefault();
        db = DBFactory.getInstance();
        commandInvoker = new CommandInvokerImpl();
    }

    @After
    public void tearDown() throws Exception {
        db.closeSession();
    }

    @Test
    public void testExecute() throws Exception {

        // Vor Transaktionen
        assertFalse(checkIfSemesterAvailable("1912/1913", Semesterbezeichnung.ZWEITES_SEMESTER, new GregorianCalendar(1913, Calendar.FEBRUARY, 20), new GregorianCalendar(1913, Calendar.JULY, 10), new GregorianCalendar(1912, Calendar.APRIL, 25), new GregorianCalendar(1912, Calendar.MAY, 7), null, null));
        assertFalse(checkIfSemesterAvailable("1911/1912", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(1911, Calendar.AUGUST, 20), new GregorianCalendar(1912, Calendar.FEBRUARY, 10), new GregorianCalendar(1911, Calendar.OCTOBER, 5), new GregorianCalendar(1911, Calendar.OCTOBER, 17), new GregorianCalendar(1911, Calendar.DECEMBER, 21), new GregorianCalendar(1912, Calendar.JANUARY, 2)));
        assertFalse(checkIfSemesterAvailable("1912/1913", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(1912, Calendar.AUGUST, 22), new GregorianCalendar(1913, Calendar.FEBRUARY, 22), new GregorianCalendar(1912, Calendar.OCTOBER, 5), new GregorianCalendar(1912, Calendar.OCTOBER, 17), new GregorianCalendar(1912, Calendar.DECEMBER, 21), new GregorianCalendar(1913, Calendar.JANUARY, 2)));
        List<Semester> semestersSaved = new ArrayList<>();

        // Semester hinzufügen
        Semester semester1 = new Semester("1912/1913", Semesterbezeichnung.ZWEITES_SEMESTER, new GregorianCalendar(1913, Calendar.FEBRUARY, 20), new GregorianCalendar(1913, Calendar.JULY, 10), new GregorianCalendar(1912, Calendar.APRIL, 25), new GregorianCalendar(1912, Calendar.MAY, 7), null, null);
        SaveOrUpdateSemesterCommand saveOrUpdateSemesterCommand = new SaveOrUpdateSemesterCommand(semester1, null, semestersSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateSemesterCommand);
        assertEquals(1, semestersSaved.size());
        assertTrue(checkIfSemesterAvailable("1912/1913", Semesterbezeichnung.ZWEITES_SEMESTER, new GregorianCalendar(1913, Calendar.FEBRUARY, 20), new GregorianCalendar(1913, Calendar.JULY, 10), new GregorianCalendar(1912, Calendar.APRIL, 25), new GregorianCalendar(1912, Calendar.MAY, 7), null, null));

        // Weiteres Semester hinzufügen
        Semester semester2 = new Semester("1911/1912", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(1911, Calendar.AUGUST, 20), new GregorianCalendar(1912, Calendar.FEBRUARY, 10), new GregorianCalendar(1911, Calendar.OCTOBER, 5), new GregorianCalendar(1911, Calendar.OCTOBER, 17), new GregorianCalendar(1911, Calendar.DECEMBER, 21), new GregorianCalendar(1912, Calendar.JANUARY, 2));
        saveOrUpdateSemesterCommand = new SaveOrUpdateSemesterCommand(semester2, null, semestersSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateSemesterCommand);
        assertEquals(2, semestersSaved.size());
        assertTrue(checkIfSemesterAvailable("1911/1912", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(1911, Calendar.AUGUST, 20), new GregorianCalendar(1912, Calendar.FEBRUARY, 10), new GregorianCalendar(1911, Calendar.OCTOBER, 5), new GregorianCalendar(1911, Calendar.OCTOBER, 17), new GregorianCalendar(1911, Calendar.DECEMBER, 21), new GregorianCalendar(1912, Calendar.JANUARY, 2)));

        // Weiteres Semester hinzufügen
        Semester semester3 = new Semester("1912/1913", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(1912, Calendar.AUGUST, 22), new GregorianCalendar(1913, Calendar.FEBRUARY, 22), new GregorianCalendar(1912, Calendar.OCTOBER, 5), new GregorianCalendar(1912, Calendar.OCTOBER, 17), new GregorianCalendar(1912, Calendar.DECEMBER, 21), new GregorianCalendar(1913, Calendar.JANUARY, 2));
        saveOrUpdateSemesterCommand = new SaveOrUpdateSemesterCommand(semester3, null, semestersSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateSemesterCommand);
        assertEquals(3, semestersSaved.size());
        assertTrue(checkIfSemesterAvailable("1912/1913", Semesterbezeichnung.ZWEITES_SEMESTER, new GregorianCalendar(1913, Calendar.FEBRUARY, 20), new GregorianCalendar(1913, Calendar.JULY, 10), new GregorianCalendar(1912, Calendar.APRIL, 25), new GregorianCalendar(1912, Calendar.MAY, 7), null, null));
        assertTrue(checkIfSemesterAvailable("1911/1912", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(1911, Calendar.AUGUST, 20), new GregorianCalendar(1912, Calendar.FEBRUARY, 10), new GregorianCalendar(1911, Calendar.OCTOBER, 5), new GregorianCalendar(1911, Calendar.OCTOBER, 17), new GregorianCalendar(1911, Calendar.DECEMBER, 21), new GregorianCalendar(1912, Calendar.JANUARY, 2)));
        assertTrue(checkIfSemesterAvailable("1912/1913", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(1912, Calendar.AUGUST, 22), new GregorianCalendar(1913, Calendar.FEBRUARY, 22), new GregorianCalendar(1912, Calendar.OCTOBER, 5), new GregorianCalendar(1912, Calendar.OCTOBER, 17), new GregorianCalendar(1912, Calendar.DECEMBER, 21), new GregorianCalendar(1913, Calendar.JANUARY, 2)));
        // Zeitlich absteigend geordnet?
        assertEquals("1911/1912", semestersSaved.get(2).getSchuljahr());
        assertEquals(Semesterbezeichnung.ERSTES_SEMESTER, semestersSaved.get(2).getSemesterbezeichnung());
        assertEquals("1912/1913", semestersSaved.get(1).getSchuljahr());
        assertEquals(Semesterbezeichnung.ERSTES_SEMESTER, semestersSaved.get(1).getSemesterbezeichnung());
        assertEquals("1912/1913", semestersSaved.get(0).getSchuljahr());
        assertEquals(Semesterbezeichnung.ZWEITES_SEMESTER, semestersSaved.get(0).getSemesterbezeichnung());

        // Semester bearbeiten (neuer Semesterbeginn, neues Semesterende)
        Semester semester1Modif = new Semester("1912/1913", Semesterbezeichnung.ZWEITES_SEMESTER, new GregorianCalendar(1913, Calendar.FEBRUARY, 27), new GregorianCalendar(1913, Calendar.JULY, 17), new GregorianCalendar(1912, Calendar.APRIL, 25), new GregorianCalendar(1912, Calendar.MAY, 7), null, null);
        saveOrUpdateSemesterCommand = new SaveOrUpdateSemesterCommand(semester1Modif, semester1, semestersSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateSemesterCommand);
        assertEquals(3, semestersSaved.size());
        assertFalse(checkIfSemesterAvailable("1912/1913", Semesterbezeichnung.ZWEITES_SEMESTER, new GregorianCalendar(1913, Calendar.FEBRUARY, 20), new GregorianCalendar(1913, Calendar.JULY, 10), new GregorianCalendar(1912, Calendar.APRIL, 25), new GregorianCalendar(1912, Calendar.MAY, 7), null, null));
        assertTrue(checkIfSemesterAvailable("1912/1913", Semesterbezeichnung.ZWEITES_SEMESTER, new GregorianCalendar(1913, Calendar.FEBRUARY, 27), new GregorianCalendar(1913, Calendar.JULY, 17), new GregorianCalendar(1912, Calendar.APRIL, 25), new GregorianCalendar(1912, Calendar.MAY, 7), null, null));

        // Testdaten löschen
        EntityManager entityManager = db.getCurrentEntityManager();
        entityManager.getTransaction().begin();
        for (Semester semester : semestersSaved) {
            Semester semesterToBeDeleted = semesterDao.findById(semester.getSemesterId());
            if (semesterToBeDeleted != null) {
                semesterDao.remove(semesterToBeDeleted);
            }
        }
        entityManager.getTransaction().commit();
    }

    private boolean checkIfSemesterAvailable(String schuljahr, Semesterbezeichnung semesterbezeichnung, Calendar semesterbeginn, Calendar semesterende, Calendar ferienbeginn1, Calendar ferienende1, Calendar ferienbeginn2, Calendar ferienende2) {
        FindAllSemestersCommand findAllSemestersCommand = new FindAllSemestersCommand();
        commandInvoker.executeCommand(findAllSemestersCommand);
        List<Semester> semestersAll = findAllSemestersCommand.getSemestersAll();
        for (Semester semester : semestersAll) {
            if (semester.getSchuljahr().equals(schuljahr) && semester.getSemesterbezeichnung().equals(semesterbezeichnung)
                    && semester.getSemesterbeginn().equals(semesterbeginn) && semester.getSemesterende().equals(semesterende)
                    && semester.getFerienbeginn1().equals(ferienbeginn1) && semester.getFerienende1().equals(ferienende1)
                    && ((semester.getFerienbeginn2() == null && ferienbeginn2 == null) || (semester.getFerienbeginn2() != null && semester.getFerienbeginn2().equals(ferienbeginn2)))
                    && ((semester.getFerienende2() == null && ferienende2 == null) || (semester.getFerienende2() != null && semester.getFerienende2().equals(ferienende2)))) {
                return true;
            }
        }
        return false;
    }
}