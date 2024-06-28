package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Semesterbezeichnung;
import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.daos.SemesterDao;
import ch.metzenthin.svm.persistence.entities.Semester;
import jakarta.persistence.EntityManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;
import static org.junit.Assert.assertTrue;

/**
 * @author Martin Schraner
 */
public class FindAllSemestersCommandTest {

    private final SemesterDao semesterDao = new SemesterDao();
    private final Set<Semester> semesterTestdata = new HashSet<>();

    private DB db;
    private CommandInvoker commandInvoker;

    @Before
    public void setUp() {
        createSvmPropertiesFileDefault();
        db = DBFactory.getInstance();
        commandInvoker = new CommandInvokerImpl();
        createTestdata();
    }

    @After
    public void tearDown() {
        deleteTestdata();
        db.closeSession();
    }

    @Test
    public void testExecute() {
        FindAllSemestersCommand findAllSemestersCommand = new FindAllSemestersCommand();
        commandInvoker.executeCommand(findAllSemestersCommand);

        List<Semester> semesterFound = findAllSemestersCommand.getSemestersAll();
        assertTrue(semesterFound.size() >= 2);
        boolean found1 = false;
        boolean found2 = false;
        for (Semester semester : semesterFound) {
            if (semester.getSemesterbeginn().equals(new GregorianCalendar(2011, Calendar.AUGUST, 20))) {
                found1 = true;
            }
            if (semester.getSemesterbeginn().equals(new GregorianCalendar(2012, Calendar.FEBRUARY, 20))) {
                found2 = true;
            }
        }
        assertTrue(found1);
        assertTrue(found2);
    }

    private void createTestdata() {
        EntityManager entityManager = db.getCurrentEntityManager();
        entityManager.getTransaction().begin();

        Semester semester1 = new Semester("2011/2012", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(2011, Calendar.AUGUST, 20), new GregorianCalendar(2012, Calendar.FEBRUARY, 10), new GregorianCalendar(2011, Calendar.OCTOBER, 5), new GregorianCalendar(2011, Calendar.OCTOBER, 17), new GregorianCalendar(2011, Calendar.DECEMBER, 21), new GregorianCalendar(2012, Calendar.JANUARY, 2));
        Semester semesterSaved = semesterDao.save(semester1);
        semesterTestdata.add(semesterSaved);

        Semester semester2 = new Semester("2011/2012", Semesterbezeichnung.ZWEITES_SEMESTER, new GregorianCalendar(2012, Calendar.FEBRUARY, 20), new GregorianCalendar(2012, Calendar.JULY, 10), new GregorianCalendar(2012, Calendar.APRIL, 25), new GregorianCalendar(2012, Calendar.MAY, 7), null, null);
        semesterSaved = semesterDao.save(semester2);
        semesterTestdata.add(semesterSaved);

        entityManager.getTransaction().commit();
        db.closeSession();
    }

    private void deleteTestdata() {
        EntityManager entityManager = db.getCurrentEntityManager();
        entityManager.getTransaction().begin();

        for (Semester semester : semesterTestdata) {
            Semester semesterToBeRemoved = semesterDao.findById(semester.getSemesterId());
            semesterDao.remove(semesterToBeRemoved);
        }

        entityManager.getTransaction().commit();
        db.closeSession();
    }
}