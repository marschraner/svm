package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.entities.ElternmithilfeCode;
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
public class ElternmithilfeCodeDaoTest {

    private DB db;
    private EntityManager entityManager;
    private ElternmithilfeCodeDao elternmithilfeCodeDao;

    @Before
    public void setUp() throws Exception {
        createSvmPropertiesFileDefault();
        db = DBFactory.getInstance();
        entityManager = db.getCurrentEntityManager();
        elternmithilfeCodeDao = new ElternmithilfeCodeDao(entityManager);
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

            ElternmithilfeCode elternmithilfeCode = new ElternmithilfeCode("k", "Kuchen", true);

            entityManager.persist(elternmithilfeCode);

            ElternmithilfeCode elternmithilfeCodeFound = elternmithilfeCodeDao.findById(elternmithilfeCode.getCodeId());

            assertEquals("Kuchen", elternmithilfeCodeFound.getBeschreibung());

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

            ElternmithilfeCode elternmithilfeCode = new ElternmithilfeCode("k", "Kuchen", true);
            ElternmithilfeCode elternmithilfeCodeSaved = elternmithilfeCodeDao.save(elternmithilfeCode);

            entityManager.flush();

            ElternmithilfeCode elternmithilfeCodeFound = elternmithilfeCodeDao.findById(elternmithilfeCodeSaved.getCodeId());

            // Erzwingen, dass von DB gelesen wird
            entityManager.refresh(elternmithilfeCodeFound);

            assertEquals("Kuchen", elternmithilfeCodeFound.getBeschreibung());

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

            ElternmithilfeCode elternmithilfeCode1 = new ElternmithilfeCode("k", "Kuchen", true);
            ElternmithilfeCode elternmithilfeCode1Saved = elternmithilfeCodeDao.save(elternmithilfeCode1);
            int code1Id = elternmithilfeCode1Saved.getCodeId();

            ElternmithilfeCode elternmithilfeCode2 = new ElternmithilfeCode("f", "Frisieren", true);
            ElternmithilfeCode elternmithilfeCode2Saved = elternmithilfeCodeDao.save(elternmithilfeCode2);
            int code2Id = elternmithilfeCode2Saved.getCodeId();

            entityManager.flush();

            assertNotNull(elternmithilfeCodeDao.findById(code1Id));
            assertNotNull(elternmithilfeCodeDao.findById(code2Id));

            // 1. ElternmithilfeCode löschen
            elternmithilfeCodeDao.remove(elternmithilfeCode1Saved);
            entityManager.flush();

            assertNull(elternmithilfeCodeDao.findById(code1Id));
            assertNotNull(elternmithilfeCodeDao.findById(code2Id));

            // 2. ElternmithilfeCode löschen
            elternmithilfeCodeDao.remove(elternmithilfeCode2Saved);
            entityManager.flush();

            assertNull(elternmithilfeCodeDao.findById(code2Id));
            assertNull(elternmithilfeCodeDao.findById(code2Id));

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
            ElternmithilfeCode elternmithilfeCode1 = new ElternmithilfeCode("kt", "KuchenTest", true);
            elternmithilfeCodeDao.save(elternmithilfeCode1);

            ElternmithilfeCode elternmithilfeCode2 = new ElternmithilfeCode("ft", "FrisierenTest", true);
            elternmithilfeCodeDao.save(elternmithilfeCode2);

            entityManager.flush();

            // Codes suchen
            List<ElternmithilfeCode> codesFound = elternmithilfeCodeDao.findAll();
            assertTrue(codesFound.size() >= 2);
            boolean found1 = false;
            boolean found2 = false;
            for (ElternmithilfeCode elternmithilfeCode : codesFound) {
                if (elternmithilfeCode.getBeschreibung().equals("KuchenTest")) {
                    found1 = true;
                }
                if (elternmithilfeCode.getBeschreibung().equals("FrisierenTest")) {
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