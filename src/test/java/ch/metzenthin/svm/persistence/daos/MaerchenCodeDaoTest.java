package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.persistence.entities.MaerchenCode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Martin Schraner
 */
public class MaerchenCodeDaoTest {

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private MaerchenCodeDao maerchenCodeDao;

    @Before
    public void setUp() throws Exception {
        entityManagerFactory = Persistence.createEntityManagerFactory("svm");
        entityManager = entityManagerFactory.createEntityManager();
        maerchenCodeDao = new MaerchenCodeDao(entityManager);
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

            MaerchenCode maerchenCode = new MaerchenCode("k", "Kuchen");

            entityManager.persist(maerchenCode);

            MaerchenCode maerchenCodeFound = maerchenCodeDao.findById(maerchenCode.getCodeId());

            assertEquals("Kuchen", maerchenCodeFound.getBeschreibung());

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

            MaerchenCode maerchenCode = new MaerchenCode("k", "Kuchen");
            MaerchenCode maerchenCodeSaved = maerchenCodeDao.save(maerchenCode);

            entityManager.flush();

            MaerchenCode maerchenCodeFound = maerchenCodeDao.findById(maerchenCodeSaved.getCodeId());

            // Erzwingen, dass von DB gelesen wird
            entityManager.refresh(maerchenCodeFound);

            assertEquals("Kuchen", maerchenCodeFound.getBeschreibung());

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

            MaerchenCode maerchenCode1 = new MaerchenCode("k", "Kuchen");
            MaerchenCode maerchenCode1Saved = maerchenCodeDao.save(maerchenCode1);
            int code1Id = maerchenCode1Saved.getCodeId();

            MaerchenCode maerchenCode2 = new MaerchenCode("f", "Frisieren");
            MaerchenCode maerchenCode2Saved = maerchenCodeDao.save(maerchenCode2);
            int code2Id = maerchenCode2Saved.getCodeId();

            entityManager.flush();

            assertNotNull(maerchenCodeDao.findById(code1Id));
            assertNotNull(maerchenCodeDao.findById(code2Id));

            // 1. MaerchenCode löschen
            maerchenCodeDao.remove(maerchenCode1Saved);
            entityManager.flush();

            assertNull(maerchenCodeDao.findById(code1Id));
            assertNotNull(maerchenCodeDao.findById(code2Id));

            // 2. MaerchenCode löschen
            maerchenCodeDao.remove(maerchenCode2Saved);
            entityManager.flush();

            assertNull(maerchenCodeDao.findById(code2Id));
            assertNull(maerchenCodeDao.findById(code2Id));

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

            // Codes hinzufügen
            MaerchenCode maerchenCode1 = new MaerchenCode("kt", "KuchenTest");
            maerchenCodeDao.save(maerchenCode1);

            MaerchenCode maerchenCode2 = new MaerchenCode("ft", "FrisierenTest");
            maerchenCodeDao.save(maerchenCode2);

            entityManager.flush();

            // Codes suchen
            List<MaerchenCode> codesFound = maerchenCodeDao.findAll();
            assertTrue(codesFound.size() >= 2);
            boolean found1 = false;
            boolean found2 = false;
            for (MaerchenCode maerchenCode : codesFound) {
                if (maerchenCode.getBeschreibung().equals("KuchenTest")) {
                    found1 = true;
                }
                if (maerchenCode.getBeschreibung().equals("FrisierenTest")) {
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