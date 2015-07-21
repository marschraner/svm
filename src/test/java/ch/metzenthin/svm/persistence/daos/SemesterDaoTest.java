package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.dataTypes.Semesterbezeichnung;
import ch.metzenthin.svm.persistence.entities.Semester;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Martin Schraner
 */
public class SemesterDaoTest {

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private SemesterDao semesterDao;

    @Before
    public void setUp() throws Exception {
        entityManagerFactory = Persistence.createEntityManagerFactory("svm");
        entityManager = entityManagerFactory.createEntityManager();
        semesterDao = new SemesterDao(entityManager);
    }

    @After
    public void tearDown() throws Exception {
        if (entityManager != null) {
            entityManager.close();
        }
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }

    @Test
    public void testFindById() {
        EntityTransaction tx = null;
        try {
            tx = entityManager.getTransaction();
            tx.begin();

            Semester semester = new Semester("2011/2012", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(2011, Calendar.AUGUST, 20), new GregorianCalendar(2012, Calendar.FEBRUARY, 10), 21);

            entityManager.persist(semester);

            Semester semesterFound = semesterDao.findById(semester.getSemesterId());

            assertEquals("2011/2012", semesterFound.getSchuljahr());
            assertEquals(Semesterbezeichnung.ERSTES_SEMESTER, semesterFound.getSemesterbezeichnung());
            assertEquals(new GregorianCalendar(2011, Calendar.AUGUST, 20), semesterFound.getSemesterbeginn());
            assertEquals(new GregorianCalendar(2012, Calendar.FEBRUARY, 10), semesterFound.getSemesterende());
            assertEquals(new Integer(21), semesterFound.getAnzahlSchulwochen());

        } finally {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    @Test
    public void save() {
        EntityTransaction tx = null;
        try {
            tx = entityManager.getTransaction();
            tx.begin();

            Semester semester = new Semester("2011/2012", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(2011, Calendar.AUGUST, 20), new GregorianCalendar(2012, Calendar.FEBRUARY, 10), 21);
            Semester semesterSaved = semesterDao.save(semester);

            entityManager.flush();

            Semester semesterFound = semesterDao.findById(semesterSaved.getSemesterId());

            // Erzwingen, dass von DB gelesen wird
            entityManager.refresh(semesterFound);

            assertEquals("2011/2012", semesterFound.getSchuljahr());
            assertEquals(Semesterbezeichnung.ERSTES_SEMESTER, semesterFound.getSemesterbezeichnung());
            assertEquals(new GregorianCalendar(2011, Calendar.AUGUST, 20), semesterFound.getSemesterbeginn());
            assertEquals(new GregorianCalendar(2012, Calendar.FEBRUARY, 10), semesterFound.getSemesterende());
            assertEquals(new Integer(21), semesterFound.getAnzahlSchulwochen());

        } finally {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    @Test
    public void testRemove() {
        EntityTransaction tx = null;

        try {
            tx = entityManager.getTransaction();
            tx.begin();

            Semester semester1 = new Semester("2011/2012", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(2011, Calendar.AUGUST, 20), new GregorianCalendar(2012, Calendar.FEBRUARY, 10), 21);
            Semester semester1Saved = semesterDao.save(semester1);
            int semester1Id = semester1Saved.getSemesterId();

            Semester semester2 = new Semester("2011/2012", Semesterbezeichnung.ZWEITES_SEMESTER, new GregorianCalendar(2012, Calendar.FEBRUARY, 20), new GregorianCalendar(2012, Calendar.JULY, 10), 21);
            Semester semester2Saved = semesterDao.save(semester2);
            int semester2Id = semester2Saved.getSemesterId();

            entityManager.flush();

            assertNotNull(semesterDao.findById(semester1Id));
            assertNotNull(semesterDao.findById(semester2Id));

            // 1. Semester löschen
            semesterDao.remove(semester1Saved);
            entityManager.flush();

            assertNull(semesterDao.findById(semester1Id));
            assertNotNull(semesterDao.findById(semester2Id));

            // 2. Semester löschen
            semesterDao.remove(semester2Saved);
            entityManager.flush();

            assertNull(semesterDao.findById(semester2Id));
            assertNull(semesterDao.findById(semester2Id));

        } finally {
            if (tx != null) {
                tx.rollback();
            }
        }

    }

    @Test
    public void testFindAll() {
        EntityTransaction tx = null;
        try {
            tx = entityManager.getTransaction();
            tx.begin();

            // Semester hinzufügen
            Semester semester1 = new Semester("2011/2012", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(2011, Calendar.AUGUST, 20), new GregorianCalendar(2012, Calendar.FEBRUARY, 10), 21);
            semesterDao.save(semester1);

            Semester semester2 = new Semester("2011/2012", Semesterbezeichnung.ZWEITES_SEMESTER, new GregorianCalendar(2012, Calendar.FEBRUARY, 20), new GregorianCalendar(2012, Calendar.JULY, 10), 21);
            semesterDao.save(semester2);

            entityManager.flush();

            // Semester suchen
            List<Semester> semestersFound = semesterDao.findAll();
            assertTrue(semestersFound.size() >= 2);
            boolean found1 = false;
            boolean found2 = false;
            for (Semester semester : semestersFound) {
                if (semester.getSemesterbeginn().equals(new GregorianCalendar(2012, Calendar.FEBRUARY, 20))) {
                    found1 = true;
                }
                if (semester.getSemesterbeginn().equals(new GregorianCalendar(2011, Calendar.AUGUST, 20))) {
                    found2 = true;
                }
            }
            assertTrue(found1);
            assertTrue(found2);

        } finally {
            if (tx != null) {
                tx.rollback();
            }
        }
    }


}