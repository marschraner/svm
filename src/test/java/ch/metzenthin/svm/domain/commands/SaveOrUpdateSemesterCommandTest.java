package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.dataTypes.Semesterbezeichnung;
import ch.metzenthin.svm.persistence.daos.SemesterDao;
import ch.metzenthin.svm.persistence.entities.Semester;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Martin Schraner
 */
public class SaveOrUpdateSemesterCommandTest {

    private CommandInvoker commandInvoker = new CommandInvokerImpl();
    private EntityManagerFactory entityManagerFactory;

    @Before
    public void setUp() throws Exception {
        commandInvoker.openSession();
        entityManagerFactory = Persistence.createEntityManagerFactory("svm");
    }

    @After
    public void tearDown() throws Exception {
        commandInvoker.closeSession();
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }

    @Test
    public void testExecute() throws Exception {

        // Vor Transaktionen
        assertFalse(checkIfSemesterAvailable("1912/1913", Semesterbezeichnung.ZWEITES_SEMESTER, new GregorianCalendar(1913, Calendar.FEBRUARY, 20), new GregorianCalendar(1913, Calendar.JULY, 10), 19));
        assertFalse(checkIfSemesterAvailable("1911/1912", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(1911, Calendar.AUGUST, 20), new GregorianCalendar(1912, Calendar.FEBRUARY, 10), 21));
        assertFalse(checkIfSemesterAvailable("1912/1913", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(1912, Calendar.AUGUST, 22), new GregorianCalendar(1913, Calendar.FEBRUARY, 22), 23));
        List<Semester> semestersSaved = new ArrayList<>();

        // Semester hinzufügen
        Semester semester1 = new Semester("1912/1913", Semesterbezeichnung.ZWEITES_SEMESTER, new GregorianCalendar(1913, Calendar.FEBRUARY, 20), new GregorianCalendar(1913, Calendar.JULY, 10), 19);
        SaveOrUpdateSemesterCommand saveOrUpdateSemesterCommand = new SaveOrUpdateSemesterCommand(semester1, null, semestersSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateSemesterCommand);
        assertEquals(1, semestersSaved.size());
        assertTrue(checkIfSemesterAvailable("1912/1913", Semesterbezeichnung.ZWEITES_SEMESTER, new GregorianCalendar(1913, Calendar.FEBRUARY, 20), new GregorianCalendar(1913, Calendar.JULY, 10), 19));

        // Weiteres Semester hinzufügen
        Semester semester2 = new Semester("1911/1912", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(1911, Calendar.AUGUST, 20), new GregorianCalendar(1912, Calendar.FEBRUARY, 10), 21);
        saveOrUpdateSemesterCommand = new SaveOrUpdateSemesterCommand(semester2, null, semestersSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateSemesterCommand);
        assertEquals(2, semestersSaved.size());
        assertTrue(checkIfSemesterAvailable("1911/1912", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(1911, Calendar.AUGUST, 20), new GregorianCalendar(1912, Calendar.FEBRUARY, 10), 21));

        // Weiteres Semester hinzufügen
        Semester semester3 = new Semester("1912/1913", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(1912, Calendar.AUGUST, 22), new GregorianCalendar(1913, Calendar.FEBRUARY, 22), 23);
        saveOrUpdateSemesterCommand = new SaveOrUpdateSemesterCommand(semester3, null, semestersSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateSemesterCommand);
        assertEquals(3, semestersSaved.size());
        assertTrue(checkIfSemesterAvailable("1912/1913", Semesterbezeichnung.ZWEITES_SEMESTER, new GregorianCalendar(1913, Calendar.FEBRUARY, 20), new GregorianCalendar(1913, Calendar.JULY, 10), 19));
        assertTrue(checkIfSemesterAvailable("1911/1912", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(1911, Calendar.AUGUST, 20), new GregorianCalendar(1912, Calendar.FEBRUARY, 10), 21));
        assertTrue(checkIfSemesterAvailable("1912/1913", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(1912, Calendar.AUGUST, 22), new GregorianCalendar(1913, Calendar.FEBRUARY, 22), 23));
        // Zeitlich absteigend geordnet?
        assertEquals("1911/1912", semestersSaved.get(2).getSchuljahr());
        assertEquals(Semesterbezeichnung.ERSTES_SEMESTER, semestersSaved.get(2).getSemesterbezeichnung());
        assertEquals("1912/1913", semestersSaved.get(1).getSchuljahr());
        assertEquals(Semesterbezeichnung.ERSTES_SEMESTER, semestersSaved.get(1).getSemesterbezeichnung());
        assertEquals("1912/1913", semestersSaved.get(0).getSchuljahr());
        assertEquals(Semesterbezeichnung.ZWEITES_SEMESTER, semestersSaved.get(0).getSemesterbezeichnung());

        // Semester bearbeiten (neuer Semesterbeginn, neues Semesterende)
        Semester semester1Modif = new Semester("1912/1913", Semesterbezeichnung.ZWEITES_SEMESTER, new GregorianCalendar(1913, Calendar.FEBRUARY, 27), new GregorianCalendar(1913, Calendar.JULY, 17), 19);
        saveOrUpdateSemesterCommand = new SaveOrUpdateSemesterCommand(semester1Modif, semester1, semestersSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateSemesterCommand);
        assertEquals(3, semestersSaved.size());
        assertFalse(checkIfSemesterAvailable("1912/1913", Semesterbezeichnung.ZWEITES_SEMESTER, new GregorianCalendar(1913, Calendar.FEBRUARY, 20), new GregorianCalendar(1913, Calendar.JULY, 10), 19));
        assertTrue(checkIfSemesterAvailable("1912/1913", Semesterbezeichnung.ZWEITES_SEMESTER, new GregorianCalendar(1913, Calendar.FEBRUARY, 27), new GregorianCalendar(1913, Calendar.JULY, 17), 19));

        // Testdaten löschen
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            SemesterDao semesterDao = new SemesterDao(entityManager);
            for (Semester semester : semestersSaved) {
                Semester semesterToBeDeleted = semesterDao.findById(semester.getSemesterId());
                if (semesterToBeDeleted != null) {
                    semesterDao.remove(semesterToBeDeleted);
                }
            }
            entityManager.getTransaction().commit();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }


    }

    private boolean checkIfSemesterAvailable(String schuljahr, Semesterbezeichnung semesterbezeichnung, Calendar semesterbeginn, Calendar semesterende, int anzahlSchulwochen) {
        FindAllSemestersCommand findAllSemestersCommand = new FindAllSemestersCommand();
        commandInvoker.executeCommandAsTransactionWithOpenAndClose(findAllSemestersCommand);
        List<Semester> semestersAll = findAllSemestersCommand.getSemesterAll();
        for (Semester semester : semestersAll) {
            if (semester.getSchuljahr().equals(schuljahr) && semester.getSemesterbezeichnung().equals(semesterbezeichnung) && semester.getSemesterbeginn().equals(semesterbeginn) && semester.getSemesterende().equals(semesterende) && semester.getAnzahlSchulwochen().equals(anzahlSchulwochen)) {
                return true;
            }
        }
        return false;
    }
}