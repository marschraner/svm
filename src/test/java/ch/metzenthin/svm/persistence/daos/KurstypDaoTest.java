package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.entities.Kurstyp;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;
import static org.junit.Assert.*;

/**
 * @author Martin Schraner
 */
public class KurstypDaoTest {

    private DB db;
    private EntityManager entityManager;
    private KurstypDao kurstypDao;

    @Before
    public void setUp() throws Exception {
        createSvmPropertiesFileDefault();
        db = DBFactory.getInstance();
        entityManager = db.getCurrentEntityManager();
        kurstypDao = new KurstypDao(entityManager);
    }

    @After
    public void tearDown() throws Exception {
        db.closeSession();
    }

    @Test
    public void testFindById() {
        EntityTransaction tx = null;
        try {
            tx = entityManager.getTransaction();
            tx.begin();

            Kurstyp kurstyp = new Kurstyp("Kurs Test", true);

            entityManager.persist(kurstyp);

            Kurstyp kurstypFound = kurstypDao.findById(kurstyp.getKurstypId());

            assertEquals("Kurs Test", kurstypFound.getBezeichnung());

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

            Kurstyp kurstyp = new Kurstyp("Kurs Test", true);
            Kurstyp kurstypSaved = kurstypDao.save(kurstyp);

            entityManager.flush();

            Kurstyp kurstypFound = kurstypDao.findById(kurstypSaved.getKurstypId());

            // Erzwingen, dass von DB gelesen wird
            entityManager.refresh(kurstypFound);

            assertEquals("Kurs Test", kurstypFound.getBezeichnung());

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

            Kurstyp kurstyp1 = new Kurstyp("Kurs Test1", true);
            Kurstyp kurstyp1Saved = kurstypDao.save(kurstyp1);
            int kurstyp1Id = kurstyp1Saved.getKurstypId();

            Kurstyp kurstyp2 = new Kurstyp("Kurs Test2", true);
            Kurstyp kurstyp2Saved = kurstypDao.save(kurstyp2);
            int kurstyp2Id = kurstyp2Saved.getKurstypId();

            entityManager.flush();

            assertNotNull(kurstypDao.findById(kurstyp1Id));
            assertNotNull(kurstypDao.findById(kurstyp2Id));

            // 1. Kurstyp löschen
            kurstypDao.remove(kurstyp1Saved);
            entityManager.flush();

            assertNull(kurstypDao.findById(kurstyp1Id));
            assertNotNull(kurstypDao.findById(kurstyp2Id));

            // 2. Kurstyp löschen
            kurstypDao.remove(kurstyp2Saved);
            entityManager.flush();

            assertNull(kurstypDao.findById(kurstyp2Id));
            assertNull(kurstypDao.findById(kurstyp2Id));

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

            // Kurstypen hinzufügen
            Kurstyp kurstyp1 = new Kurstyp("Kurs Test1", true);
            kurstypDao.save(kurstyp1);

            Kurstyp kurstyp2 = new Kurstyp("Kurs Test2", true);
            kurstypDao.save(kurstyp2);

            entityManager.flush();

            // Kurstypen suchen
            List<Kurstyp> kurstypenFound = kurstypDao.findAll();
            assertTrue(kurstypenFound.size() >= 2);
            boolean found1 = false;
            boolean found2 = false;
            for (Kurstyp kurstyp : kurstypenFound) {
                if (kurstyp.getBezeichnung().equals("Kurs Test1")) {
                    found1 = true;
                }
                if (kurstyp.getBezeichnung().equals("Kurs Test2")) {
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