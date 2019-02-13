package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.entities.SemesterrechnungCode;
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
public class SemesterrechnungCodeDaoTest {

    private final SemesterrechnungCodeDao semesterrechnungCodeDao = new SemesterrechnungCodeDao();

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

            SemesterrechnungCode semesterrechnungCode = new SemesterrechnungCode("2t", "Handrechnung Test", true);

            entityManager.persist(semesterrechnungCode);

            SemesterrechnungCode semesterrechnungCodeFound = semesterrechnungCodeDao.findById(semesterrechnungCode.getCodeId());

            assertEquals("Handrechnung Test", semesterrechnungCodeFound.getBeschreibung());

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

            SemesterrechnungCode semesterrechnungCode = new SemesterrechnungCode("2t", "Handrechnung Test", true);
            SemesterrechnungCode semesterrechnungCodeSaved = semesterrechnungCodeDao.save(semesterrechnungCode);

            entityManager.flush();

            SemesterrechnungCode semesterrechnungCodeFound = semesterrechnungCodeDao.findById(semesterrechnungCodeSaved.getCodeId());

            // Erzwingen, dass von DB gelesen wird
            entityManager.refresh(semesterrechnungCodeFound);

            assertEquals("Handrechnung Test", semesterrechnungCodeFound.getBeschreibung());

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

            SemesterrechnungCode semesterrechnungCode1 = new SemesterrechnungCode("2t", "Handrechnung Test", true);
            SemesterrechnungCode semesterrechnungCode1Saved = semesterrechnungCodeDao.save(semesterrechnungCode1);
            int code1Id = semesterrechnungCode1Saved.getCodeId();

            SemesterrechnungCode semesterrechnungCode2 = new SemesterrechnungCode("1t", "Stipendium Test", true);
            SemesterrechnungCode semesterrechnungCode2Saved = semesterrechnungCodeDao.save(semesterrechnungCode2);
            int code2Id = semesterrechnungCode2Saved.getCodeId();

            entityManager.flush();

            assertNotNull(semesterrechnungCodeDao.findById(code1Id));
            assertNotNull(semesterrechnungCodeDao.findById(code2Id));

            // 1. SemesterrechnungCode löschen
            semesterrechnungCodeDao.remove(semesterrechnungCode1Saved);
            entityManager.flush();

            assertNull(semesterrechnungCodeDao.findById(code1Id));
            assertNotNull(semesterrechnungCodeDao.findById(code2Id));

            // 2. SemesterrechnungCode löschen
            semesterrechnungCodeDao.remove(semesterrechnungCode2Saved);
            entityManager.flush();

            assertNull(semesterrechnungCodeDao.findById(code2Id));
            assertNull(semesterrechnungCodeDao.findById(code2Id));

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

            // Codes hinzufügen
            SemesterrechnungCode semesterrechnungCode1 = new SemesterrechnungCode("2t", "Handrechnung Test", true);
            semesterrechnungCodeDao.save(semesterrechnungCode1);

            SemesterrechnungCode semesterrechnungCode2 = new SemesterrechnungCode("1t", "Stipendium Test", true);
            semesterrechnungCodeDao.save(semesterrechnungCode2);

            entityManager.flush();

            // Codes suchen
            List<SemesterrechnungCode> codesFound = semesterrechnungCodeDao.findAll();
            assertTrue(codesFound.size() >= 2);
            boolean found1 = false;
            boolean found2 = false;
            for (SemesterrechnungCode semesterrechnungCode : codesFound) {
                if (semesterrechnungCode.getBeschreibung().equals("Handrechnung Test")) {
                    found1 = true;
                }
                if (semesterrechnungCode.getBeschreibung().equals("Stipendium Test")) {
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