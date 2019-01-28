package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.entities.Lektionsgebuehren;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.math.BigDecimal;
import java.util.List;

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;
import static org.junit.Assert.*;

/**
 * @author Martin Schraner
 */
public class LektionsgebuehrenDaoTest {

    private final LektionsgebuehrenDao lektionsgebuehrenDao = new LektionsgebuehrenDao();

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

            Lektionsgebuehren lektionsgebuehren = new Lektionsgebuehren(57, new BigDecimal("22.50"), new BigDecimal("21.00"), new BigDecimal("19.00"), new BigDecimal("18.00"), new BigDecimal("17.00"), new BigDecimal("16.00"));

            entityManager.persist(lektionsgebuehren);

            Lektionsgebuehren lektionsgebuehrenFound = lektionsgebuehrenDao.findById(lektionsgebuehren.getLektionslaenge());

            assertEquals(new BigDecimal("22.50"), lektionsgebuehrenFound.getBetrag1Kind());

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

            Lektionsgebuehren lektionsgebuehren = new Lektionsgebuehren(57, new BigDecimal("22.50"), new BigDecimal("21.00"), new BigDecimal("19.00"), new BigDecimal("18.00"), new BigDecimal("17.00"), new BigDecimal("16.00"));

            Lektionsgebuehren lektionsgebuehrenSaved = lektionsgebuehrenDao.save(lektionsgebuehren);

            entityManager.flush();

            Lektionsgebuehren lektionsgebuehrenFound = lektionsgebuehrenDao.findById(lektionsgebuehrenSaved.getLektionslaenge());

            // Erzwingen, dass von DB gelesen wird
            entityManager.refresh(lektionsgebuehrenFound);

            assertEquals(new BigDecimal("22.50"), lektionsgebuehrenFound.getBetrag1Kind());
            assertEquals(new BigDecimal("16.00"), lektionsgebuehrenFound.getBetrag6Kinder());

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

            Lektionsgebuehren lektionsgebuehren1 = new Lektionsgebuehren(57, new BigDecimal("22.50"), new BigDecimal("21.00"), new BigDecimal("19.00"), new BigDecimal("18.00"), new BigDecimal("17.00"), new BigDecimal("16.00"));
            Lektionsgebuehren lektionsgebuehren1Saved = lektionsgebuehrenDao.save(lektionsgebuehren1);

            Lektionsgebuehren lektionsgebuehren2 = new Lektionsgebuehren(67, new BigDecimal("24.50"), new BigDecimal("23.00"), new BigDecimal("21.00"), new BigDecimal("20.00"), new BigDecimal("19.00"), new BigDecimal("18.00"));
            Lektionsgebuehren lektionsgebuehren2Saved = lektionsgebuehrenDao.save(lektionsgebuehren2);

            entityManager.flush();

            assertNotNull(lektionsgebuehrenDao.findById(57));
            assertNotNull(lektionsgebuehrenDao.findById(67));

            // 1. Lektionsgebuehren löschen
            lektionsgebuehrenDao.remove(lektionsgebuehren1Saved);
            entityManager.flush();

            assertNull(lektionsgebuehrenDao.findById(57));
            assertNotNull(lektionsgebuehrenDao.findById(67));

            // 2. Lektionsgebuehren löschen
            lektionsgebuehrenDao.remove(lektionsgebuehren2Saved);
            entityManager.flush();

            assertNull(lektionsgebuehrenDao.findById(57));
            assertNull(lektionsgebuehrenDao.findById(67));

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

            // Lektionsgebuehren hinzufügen
            Lektionsgebuehren lektionsgebuehren1 = new Lektionsgebuehren(57, new BigDecimal("22.50"), new BigDecimal("21.00"), new BigDecimal("19.00"), new BigDecimal("18.00"), new BigDecimal("17.00"), new BigDecimal("16.00"));
            lektionsgebuehrenDao.save(lektionsgebuehren1);

            Lektionsgebuehren lektionsgebuehren2 = new Lektionsgebuehren(67, new BigDecimal("24.50"), new BigDecimal("23.00"), new BigDecimal("21.00"), new BigDecimal("20.00"), new BigDecimal("19.00"), new BigDecimal("18.00"));
            lektionsgebuehrenDao.save(lektionsgebuehren2);

            entityManager.flush();

            // Lektionsgebuehren suchen
            List<Lektionsgebuehren> lektionsgebuehrensFound = lektionsgebuehrenDao.findAll();
            assertTrue(lektionsgebuehrensFound.size() >= 2);
            boolean found1 = false;
            boolean found2 = false;
            for (Lektionsgebuehren lektionsgebuehren : lektionsgebuehrensFound) {
                if (lektionsgebuehren.getLektionslaenge() == 57) {
                    found1 = true;
                }
                if (lektionsgebuehren.getLektionslaenge() == 67) {
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