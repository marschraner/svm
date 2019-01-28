package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.common.dataTypes.Semesterbezeichnung;
import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.entities.Semester;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;
import static org.junit.Assert.*;

/**
 * @author Martin Schraner
 */
public class SemesterDaoTest {

    private final SemesterDao semesterDao = new SemesterDao();

    private DB db;

    @Before
    public void setUp() throws Exception {
        createSvmPropertiesFileDefault();
        db = DBFactory.getInstance();
    }

    @After
    public void tearDown() throws Exception {
        db.closeSession();
    }

    @Test
    public void testFindById() {
        EntityManager entityManager = db.getCurrentEntityManager();
        EntityTransaction tx = null;
        try {
            tx = entityManager.getTransaction();
            tx.begin();

            Semester semester = new Semester("2011/2012", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(2011, Calendar.AUGUST, 20), new GregorianCalendar(2012, Calendar.FEBRUARY, 10), new GregorianCalendar(2011, Calendar.OCTOBER, 5), new GregorianCalendar(2011, Calendar.OCTOBER, 17), new GregorianCalendar(2011, Calendar.DECEMBER, 21), new GregorianCalendar(2012, Calendar.JANUARY, 2));

            entityManager.persist(semester);

            Semester semesterFound = semesterDao.findById(semester.getSemesterId());

            assertEquals("2011/2012", semesterFound.getSchuljahr());
            assertEquals(Semesterbezeichnung.ERSTES_SEMESTER, semesterFound.getSemesterbezeichnung());
            assertEquals(new GregorianCalendar(2011, Calendar.AUGUST, 20), semesterFound.getSemesterbeginn());
            assertEquals(new GregorianCalendar(2012, Calendar.FEBRUARY, 10), semesterFound.getSemesterende());
            assertEquals(new GregorianCalendar(2011, Calendar.OCTOBER, 5), semesterFound.getFerienbeginn1());
            assertEquals(new GregorianCalendar(2011, Calendar.OCTOBER, 17), semesterFound.getFerienende1());
            assertEquals(new GregorianCalendar(2011, Calendar.DECEMBER, 21), semesterFound.getFerienbeginn2());
            assertEquals(new GregorianCalendar(2012, Calendar.JANUARY, 2), semesterFound.getFerienende2());

        } finally {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    @Test
    public void save() {
        EntityManager entityManager = db.getCurrentEntityManager();
        EntityTransaction tx = null;
        try {
            tx = entityManager.getTransaction();
            tx.begin();

            Semester semester = new Semester("2011/2012", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(2011, Calendar.AUGUST, 20), new GregorianCalendar(2012, Calendar.FEBRUARY, 10), new GregorianCalendar(2011, Calendar.OCTOBER, 5), new GregorianCalendar(2011, Calendar.OCTOBER, 17), new GregorianCalendar(2011, Calendar.DECEMBER, 21), new GregorianCalendar(2012, Calendar.JANUARY, 2));
            Semester semesterSaved = semesterDao.save(semester);

            entityManager.flush();

            Semester semesterFound = semesterDao.findById(semesterSaved.getSemesterId());

            // Erzwingen, dass von DB gelesen wird
            entityManager.refresh(semesterFound);

            assertEquals("2011/2012", semesterFound.getSchuljahr());
            assertEquals(Semesterbezeichnung.ERSTES_SEMESTER, semesterFound.getSemesterbezeichnung());
            assertEquals(new GregorianCalendar(2011, Calendar.AUGUST, 20), semesterFound.getSemesterbeginn());
            assertEquals(new GregorianCalendar(2012, Calendar.FEBRUARY, 10), semesterFound.getSemesterende());
            assertEquals(new GregorianCalendar(2011, Calendar.OCTOBER, 5), semesterFound.getFerienbeginn1());
            assertEquals(new GregorianCalendar(2011, Calendar.OCTOBER, 17), semesterFound.getFerienende1());
            assertEquals(new GregorianCalendar(2011, Calendar.DECEMBER, 21), semesterFound.getFerienbeginn2());
            assertEquals(new GregorianCalendar(2012, Calendar.JANUARY, 2), semesterFound.getFerienende2());


        } finally {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    @Test
    public void testRemove() {
        EntityManager entityManager = db.getCurrentEntityManager();
        EntityTransaction tx = null;
        try {
            tx = entityManager.getTransaction();
            tx.begin();

            Semester semester1 = new Semester("2011/2012", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(2011, Calendar.AUGUST, 20), new GregorianCalendar(2012, Calendar.FEBRUARY, 10), new GregorianCalendar(2011, Calendar.OCTOBER, 5), new GregorianCalendar(2011, Calendar.OCTOBER, 17), new GregorianCalendar(2011, Calendar.DECEMBER, 21), new GregorianCalendar(2012, Calendar.JANUARY, 2));
            Semester semester1Saved = semesterDao.save(semester1);
            int semester1Id = semester1Saved.getSemesterId();

            Semester semester2 = new Semester("2011/2012", Semesterbezeichnung.ZWEITES_SEMESTER, new GregorianCalendar(2012, Calendar.FEBRUARY, 20), new GregorianCalendar(2012, Calendar.JULY, 10), new GregorianCalendar(2012, Calendar.APRIL, 25), new GregorianCalendar(2012, Calendar.MAY, 7), null, null);
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
        EntityManager entityManager = db.getCurrentEntityManager();
        EntityTransaction tx = null;
        try {
            tx = entityManager.getTransaction();
            tx.begin();

            // Semester hinzufügen
            Semester semester1 = new Semester("2011/2012", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(2011, Calendar.AUGUST, 20), new GregorianCalendar(2012, Calendar.FEBRUARY, 10), new GregorianCalendar(2011, Calendar.OCTOBER, 5), new GregorianCalendar(2011, Calendar.OCTOBER, 17), new GregorianCalendar(2011, Calendar.DECEMBER, 21), new GregorianCalendar(2012, Calendar.JANUARY, 2));
            semesterDao.save(semester1);

            Semester semester2 = new Semester("2011/2012", Semesterbezeichnung.ZWEITES_SEMESTER, new GregorianCalendar(2012, Calendar.FEBRUARY, 20), new GregorianCalendar(2012, Calendar.JULY, 10), new GregorianCalendar(2012, Calendar.APRIL, 25), new GregorianCalendar(2012, Calendar.MAY, 7), null, null);
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