package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.entities.Kursort;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;
import static org.junit.Assert.*;

/**
 * @author Martin Schraner
 */
public class KursortDaoTest {

    private final KursortDao kursortDao = new KursortDao();

    private DB db;

    @Before
    public void setUp() {
        createSvmPropertiesFileDefault();
        db = DBFactory.getInstance();
    }

    @After
    public void tearDown() {
        db.closeSession();
    }

    @Test
    public void testFindById() {
        EntityManager entityManager = db.getCurrentEntityManager();
        EntityTransaction tx = null;
        try {
            tx = entityManager.getTransaction();
            tx.begin();

            Kursort kursort = new Kursort("Saal Test", true);

            entityManager.persist(kursort);

            Kursort kursortFound = kursortDao.findById(kursort.getKursortId());

            assertEquals("Saal Test", kursortFound.getBezeichnung());

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

            Kursort kursort = new Kursort("Saal Test", true);
            Kursort kursortSaved = kursortDao.save(kursort);

            entityManager.flush();

            Kursort kursortFound = kursortDao.findById(kursortSaved.getKursortId());

            // Erzwingen, dass von DB gelesen wird
            entityManager.refresh(kursortFound);

            assertEquals("Saal Test", kursortFound.getBezeichnung());

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

            Kursort kursort1 = new Kursort("Saal Test1", true);
            Kursort kursort1Saved = kursortDao.save(kursort1);
            int kursort1Id = kursort1Saved.getKursortId();

            Kursort kursort2 = new Kursort("Saal Test2", true);
            Kursort kursort2Saved = kursortDao.save(kursort2);
            int kursort2Id = kursort2Saved.getKursortId();

            entityManager.flush();

            assertNotNull(kursortDao.findById(kursort1Id));
            assertNotNull(kursortDao.findById(kursort2Id));

            // 1. Kursort löschen
            kursortDao.remove(kursort1Saved);
            entityManager.flush();

            assertNull(kursortDao.findById(kursort1Id));
            assertNotNull(kursortDao.findById(kursort2Id));

            // 2. Kursort löschen
            kursortDao.remove(kursort2Saved);
            entityManager.flush();

            assertNull(kursortDao.findById(kursort2Id));
            assertNull(kursortDao.findById(kursort2Id));

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

            // Kursorts hinzufügen
            Kursort kursort1 = new Kursort("Saal Test1", true);
            kursortDao.save(kursort1);

            Kursort kursort2 = new Kursort("Saal Test2", true);
            kursortDao.save(kursort2);

            entityManager.flush();

            // Kursorts suchen
            List<Kursort> kursortsFound = kursortDao.findAll();
            assertTrue(kursortsFound.size() >= 2);
            boolean found1 = false;
            boolean found2 = false;
            for (Kursort kursort : kursortsFound) {
                if (kursort.getBezeichnung().equals("Saal Test1")) {
                    found1 = true;
                }
                if (kursort.getBezeichnung().equals("Saal Test2")) {
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