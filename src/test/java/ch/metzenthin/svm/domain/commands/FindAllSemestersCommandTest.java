package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Semesterbezeichnung;
import ch.metzenthin.svm.common.utils.PersistenceProperties;
import ch.metzenthin.svm.persistence.daos.SemesterDao;
import ch.metzenthin.svm.persistence.entities.Semester;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.*;

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;
import static org.junit.Assert.assertTrue;

/**
 * @author Martin Schraner
 */
public class FindAllSemestersCommandTest {

    private CommandInvoker commandInvoker = new CommandInvokerImpl();
    private EntityManagerFactory entityManagerFactory;
    private Set<Semester> semesterTestdata = new HashSet<>();

    @Before
    public void setUp() throws Exception {
        createSvmPropertiesFileDefault();
        entityManagerFactory = Persistence.createEntityManagerFactory("svm", PersistenceProperties.getPersistenceProperties());
        createTestdata();
    }

    @After
    public void tearDown() throws Exception {
        deleteTestdata();
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }

    @Test
    public void testExecute() {
        FindAllSemestersCommand findAllSemestersCommand = new FindAllSemestersCommand();
            commandInvoker.executeCommandAsTransactionWithOpenAndClose(findAllSemestersCommand);

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
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            SemesterDao semesterDao = new SemesterDao(entityManager);

            Semester semester1 = new Semester("2011/2012", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(2011, Calendar.AUGUST, 20), new GregorianCalendar(2012, Calendar.FEBRUARY, 10), 21);
            Semester semesterSaved = semesterDao.save(semester1);
            semesterTestdata.add(semesterSaved);

            Semester semester2 = new Semester("2011/2012", Semesterbezeichnung.ZWEITES_SEMESTER, new GregorianCalendar(2012, Calendar.FEBRUARY, 20), new GregorianCalendar(2012, Calendar.JULY, 10), 21);
            semesterSaved = semesterDao.save(semester2);
            semesterTestdata.add(semesterSaved);

            entityManager.getTransaction().commit();

        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    private void deleteTestdata() {
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            SemesterDao semesterDao = new SemesterDao(entityManager);

            for (Semester semester : semesterTestdata) {
                Semester semesterToBeRemoved = semesterDao.findById(semester.getSemesterId());
                semesterDao.remove(semesterToBeRemoved);
            }

            entityManager.getTransaction().commit();

        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }
}