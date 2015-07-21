package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.dataTypes.Semesterbezeichnung;
import ch.metzenthin.svm.persistence.entities.Semester;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * @author Martin Schraner
 */
public class DeleteSemesterCommandTest {

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

        List<Semester> semestersSaved = new ArrayList<>();

        // 2 Semester erfassen
        Semester semester1 = new Semester("1912/1913", Semesterbezeichnung.ZWEITES_SEMESTER, new GregorianCalendar(1913, Calendar.FEBRUARY, 20), new GregorianCalendar(1913, Calendar.JULY, 10), 19);
        Semester semester2 = new Semester("1911/1912", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(1911, Calendar.AUGUST, 20), new GregorianCalendar(1912, Calendar.FEBRUARY, 10), 21);

        SaveOrUpdateSemesterCommand saveOrUpdateSemesterCommand = new SaveOrUpdateSemesterCommand(semester1, null, semestersSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateSemesterCommand);

        saveOrUpdateSemesterCommand = new SaveOrUpdateSemesterCommand(semester2, null, semestersSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateSemesterCommand);

        // Semesters löschen
        DeleteSemesterCommand deleteSemesterCommand = new DeleteSemesterCommand(semestersSaved, 1);
        commandInvoker.executeCommandAsTransaction(deleteSemesterCommand);
        assertEquals(DeleteSemesterCommand.Result.LOESCHEN_ERFOLGREICH, deleteSemesterCommand.getResult());
        assertEquals(1, semestersSaved.size());

        deleteSemesterCommand = new DeleteSemesterCommand(semestersSaved, 0);
        commandInvoker.executeCommandAsTransaction(deleteSemesterCommand);
        assertEquals(DeleteSemesterCommand.Result.LOESCHEN_ERFOLGREICH, deleteSemesterCommand.getResult());
        assertTrue(semestersSaved.isEmpty());

    }
}