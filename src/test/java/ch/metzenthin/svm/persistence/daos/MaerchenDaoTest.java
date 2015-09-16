package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.common.utils.PersistenceProperties;
import ch.metzenthin.svm.persistence.entities.Maerchen;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;
import static org.junit.Assert.*;

/**
 * @author Martin Schraner
 */
public class MaerchenDaoTest {

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private MaerchenDao maerchenDao;

    @Before
    public void setUp() throws Exception {
        createSvmPropertiesFileDefault();
        entityManagerFactory = Persistence.createEntityManagerFactory("svm", PersistenceProperties.getPersistenceProperties());
        entityManager = entityManagerFactory.createEntityManager();
        maerchenDao = new MaerchenDao(entityManager);
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

            Maerchen maerchen = new Maerchen("2011/2012", "Schneewittchen", 7);

            entityManager.persist(maerchen);

            Maerchen maerchenFound = maerchenDao.findById(maerchen.getMaerchenId());

            assertEquals("2011/2012", maerchenFound.getSchuljahr());
            assertEquals("Schneewittchen", maerchenFound.getBezeichnung());
            assertEquals(7, maerchen.getAnzahlVorstellungen().intValue());

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

            Maerchen maerchen = new Maerchen("2011/2012", "Schneewittchen", 7);
            Maerchen maerchenSaved = maerchenDao.save(maerchen);

            entityManager.flush();

            Maerchen maerchenFound = maerchenDao.findById(maerchenSaved.getMaerchenId());

            // Erzwingen, dass von DB gelesen wird
            entityManager.refresh(maerchenFound);

            assertEquals("2011/2012", maerchenFound.getSchuljahr());
            assertEquals("Schneewittchen", maerchenFound.getBezeichnung());
            assertEquals(7, maerchen.getAnzahlVorstellungen().intValue());

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

            Maerchen maerchen1 = new Maerchen("2010/2011", "Gestiefelter Kater", 8);
            Maerchen maerchen1Saved = maerchenDao.save(maerchen1);
            int maerchen1Id = maerchen1Saved.getMaerchenId();

            Maerchen maerchen2 = new Maerchen("2011/2012", "Schneewittchen", 7);
            Maerchen maerchen2Saved = maerchenDao.save(maerchen2);
            int maerchen2Id = maerchen2Saved.getMaerchenId();

            entityManager.flush();

            assertNotNull(maerchenDao.findById(maerchen1Id));
            assertNotNull(maerchenDao.findById(maerchen2Id));

            // 1. Maerchen löschen
            maerchenDao.remove(maerchen1Saved);
            entityManager.flush();

            assertNull(maerchenDao.findById(maerchen1Id));
            assertNotNull(maerchenDao.findById(maerchen2Id));

            // 2. Maerchen löschen
            maerchenDao.remove(maerchen2Saved);
            entityManager.flush();

            assertNull(maerchenDao.findById(maerchen2Id));
            assertNull(maerchenDao.findById(maerchen2Id));

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

            // Maerchen hinzufügen
            Maerchen maerchen1 = new Maerchen("1910/1911", "Gestiefelter Kater", 8);
            maerchenDao.save(maerchen1);

            Maerchen maerchen2 = new Maerchen("1911/1912", "Schneewittchen", 7);
            maerchenDao.save(maerchen2);

            entityManager.flush();

            // Maerchen suchen
            List<Maerchen> maerchensFound = maerchenDao.findAll();
            assertTrue(maerchensFound.size() >= 2);
            boolean found1 = false;
            boolean found2 = false;
            for (Maerchen maerchen : maerchensFound) {
                if (maerchen.getSchuljahr().equals("1910/1911")) {
                    found1 = true;
                }
                if (maerchen.getSchuljahr().equals("1911/1912")) {
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