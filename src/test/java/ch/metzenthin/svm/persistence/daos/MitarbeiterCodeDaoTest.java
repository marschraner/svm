package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.common.dataTypes.Anrede;
import ch.metzenthin.svm.common.utils.PersistenceProperties;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;
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

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;
import static org.junit.Assert.*;

/**
 * @author Martin Schraner
 */
public class MitarbeiterCodeDaoTest {

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private MitarbeiterCodeDao mitarbeiterCodeDao;
    private MitarbeiterDao mitarbeiterDao;

    @Before
    public void setUp() throws Exception {
        createSvmPropertiesFileDefault();
        entityManagerFactory = Persistence.createEntityManagerFactory("svm", PersistenceProperties.getPersistenceProperties());
        entityManager = entityManagerFactory.createEntityManager();
        mitarbeiterCodeDao = new MitarbeiterCodeDao(entityManager);
        mitarbeiterDao = new MitarbeiterDao(entityManager);
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

            MitarbeiterCode mitarbeiterCode = new MitarbeiterCode("ht", "HelferTest", true);

            entityManager.persist(mitarbeiterCode);

            MitarbeiterCode mitarbeiterCodeFound = mitarbeiterCodeDao.findById(mitarbeiterCode.getCodeId());

            assertEquals("HelferTest", mitarbeiterCodeFound.getBeschreibung());

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

            MitarbeiterCode mitarbeiterCode = new MitarbeiterCode("ht", "HelferTest", true);
            MitarbeiterCode mitarbeiterCodeSaved = mitarbeiterCodeDao.save(mitarbeiterCode);

            entityManager.flush();

            MitarbeiterCode mitarbeiterCodeFound = mitarbeiterCodeDao.findById(mitarbeiterCodeSaved.getCodeId());

            // Erzwingen, dass von DB gelesen wird
            entityManager.refresh(mitarbeiterCodeFound);

            assertEquals("HelferTest", mitarbeiterCodeFound.getBeschreibung());

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

            MitarbeiterCode mitarbeiterCode1 = new MitarbeiterCode("ht", "HelferTest", true);
            MitarbeiterCode mitarbeiterCode1Saved = mitarbeiterCodeDao.save(mitarbeiterCode1);
            int code1Id = mitarbeiterCode1Saved.getCodeId();

            MitarbeiterCode mitarbeiterCode2 = new MitarbeiterCode("vt", "VertretungTest", true);
            MitarbeiterCode mitarbeiterCode2Saved = mitarbeiterCodeDao.save(mitarbeiterCode2);
            int code2Id = mitarbeiterCode2Saved.getCodeId();

            entityManager.flush();

            assertNotNull(mitarbeiterCodeDao.findById(code1Id));
            assertNotNull(mitarbeiterCodeDao.findById(code2Id));

            // 1. MitarbeiterCode löschen
            mitarbeiterCodeDao.remove(mitarbeiterCode1Saved);
            entityManager.flush();

            assertNull(mitarbeiterCodeDao.findById(code1Id));
            assertNotNull(mitarbeiterCodeDao.findById(code2Id));

            // 2. MitarbeiterCode löschen
            mitarbeiterCodeDao.remove(mitarbeiterCode2Saved);
            entityManager.flush();

            assertNull(mitarbeiterCodeDao.findById(code2Id));
            assertNull(mitarbeiterCodeDao.findById(code2Id));

        } finally {
            if (tx != null) {
                tx.rollback();
            }
        }

    }

    @Test
    public void testAddToMitarbeiterAndSave() {
        EntityTransaction tx = null;
        try {
            tx = entityManager.getTransaction();
            tx.begin();

            // Mitarbeiter
            Mitarbeiter mitarbeiter = new Mitarbeiter(Anrede.FRAU, "Noémie", "Roos", new GregorianCalendar(1994, Calendar.MARCH, 18), "044 391 45 35", "076 384 45 35", "nroos@gmx.ch", "756.3943.8722.22", true, "Mi, Fr, Sa", null, true);
            Adresse adresse = new Adresse("Rebwiesenstrasse", "54", "8702", "Zollikon");
            mitarbeiter.setAdresse(adresse);

            // Codes hinzufügen
            MitarbeiterCode mitarbeiterCode1 = new MitarbeiterCode("ht", "Helfer", true);
            mitarbeiterCodeDao.save(mitarbeiterCode1);
            mitarbeiterCodeDao.addToMitarbeiterAndSave(mitarbeiterCode1, mitarbeiter);

            MitarbeiterCode mitarbeiterCode2 = new MitarbeiterCode("vt", "VertretungTest", true);
            mitarbeiterCodeDao.save(mitarbeiterCode2);
            Mitarbeiter mitarbeiterSaved = mitarbeiterCodeDao.addToMitarbeiterAndSave(mitarbeiterCode2, mitarbeiter);

            entityManager.flush();

            // Mitarbeiter prüfen
            Mitarbeiter mitarbeiterFound = mitarbeiterDao.findById(mitarbeiterSaved.getPersonId());
            entityManager.refresh(mitarbeiterFound);
            assertEquals(2, mitarbeiterFound.getMitarbeiterCodes().size());
            // Alphatbetisch geordnet?
            assertEquals("ht", mitarbeiterFound.getMitarbeiterCodesAsList().get(0).getKuerzel());
            assertEquals("vt", mitarbeiterFound.getMitarbeiterCodesAsList().get(1).getKuerzel());

            // MitarbeiterCode prüfen
            MitarbeiterCode mitarbeiterCode1Found = mitarbeiterCodeDao.findById(mitarbeiterCode1.getCodeId());
            entityManager.refresh(mitarbeiterCode1Found);
            assertEquals(1, mitarbeiterCode1Found.getMitarbeiters().size());

        } finally {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    @Test
    public void testRemoveFromMitarbeiterAndUpdate() {
        EntityTransaction tx = null;
        try {
            tx = entityManager.getTransaction();
            tx.begin();

            // Schüler
            Mitarbeiter mitarbeiter = new Mitarbeiter(Anrede.FRAU, "Noémie", "Roos", new GregorianCalendar(1994, Calendar.MARCH, 18), "044 391 45 35", "076 384 45 35", "nroos@gmx.ch", "756.3943.8722.22", true, "Mi, Fr, Sa", null, true);
            Adresse adresse = new Adresse("Rebwiesenstrasse", "54", "8702", "Zollikon");
            mitarbeiter.setAdresse(adresse);

            // Codes hinzufügen
            MitarbeiterCode mitarbeiterCode1 = new MitarbeiterCode("ht", "HelferTest", true);
            mitarbeiterCodeDao.save(mitarbeiterCode1);
            mitarbeiterCodeDao.addToMitarbeiterAndSave(mitarbeiterCode1, mitarbeiter);

            MitarbeiterCode mitarbeiterCode2 = new MitarbeiterCode("vt", "VertretungTest", true);
            mitarbeiterCodeDao.save(mitarbeiterCode2);
            Mitarbeiter mitarbeiterSaved = mitarbeiterCodeDao.addToMitarbeiterAndSave(mitarbeiterCode2, mitarbeiter);

            entityManager.flush();

            // Mitarbeiter prüfen
            Mitarbeiter mitarbeiterFound = mitarbeiterDao.findById(mitarbeiterSaved.getPersonId());
            entityManager.refresh(mitarbeiterFound);
            assertEquals(2, mitarbeiterFound.getMitarbeiterCodes().size());
            assertEquals("ht", mitarbeiterFound.getMitarbeiterCodesAsList().get(0).getKuerzel());
            assertEquals("vt", mitarbeiterFound.getMitarbeiterCodesAsList().get(1).getKuerzel());

            // Codes prüfen
            MitarbeiterCode mitarbeiterCode1Found = mitarbeiterCodeDao.findById(mitarbeiterCode1.getCodeId());
            entityManager.refresh(mitarbeiterCode1Found);
            assertEquals(1, mitarbeiterCode1Found.getMitarbeiters().size());
            MitarbeiterCode mitarbeiterCode2Found = mitarbeiterCodeDao.findById(mitarbeiterCode2.getCodeId());
            entityManager.refresh(mitarbeiterCode2Found);
            assertEquals(1, mitarbeiterCode2Found.getMitarbeiters().size());

            // 1. MitarbeiterCode von Schüler entfernen
            Mitarbeiter mitarbeiterUpdated = mitarbeiterCodeDao.removeFromMitarbeiterAndUpdate(mitarbeiterCode1Found, mitarbeiterFound);
            entityManager.flush();

            mitarbeiterFound = mitarbeiterDao.findById(mitarbeiterUpdated.getPersonId());
            entityManager.refresh(mitarbeiterFound);
            assertEquals(1, mitarbeiterFound.getMitarbeiterCodes().size());
            assertEquals("vt", mitarbeiterFound.getMitarbeiterCodesAsList().get(0).getKuerzel());

            assertEquals(0, mitarbeiterCode1Found.getMitarbeiters().size());
            assertEquals(1, mitarbeiterCode2Found.getMitarbeiters().size());

            // 2. MitarbeiterCode von Schüler entfernen
            mitarbeiterUpdated = mitarbeiterCodeDao.removeFromMitarbeiterAndUpdate(mitarbeiterCode2Found, mitarbeiterFound);
            entityManager.flush();

            mitarbeiterFound = mitarbeiterDao.findById(mitarbeiterUpdated.getPersonId());
            entityManager.refresh(mitarbeiterFound);
            assertEquals(0, mitarbeiterFound.getMitarbeiterCodes().size());

            assertEquals(0, mitarbeiterCode1Found.getMitarbeiters().size());
            assertEquals(0, mitarbeiterCode2Found.getMitarbeiters().size());

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
            MitarbeiterCode mitarbeiterCode1 = new MitarbeiterCode("z", "HelferTest", true);
            mitarbeiterCodeDao.save(mitarbeiterCode1);

            MitarbeiterCode mitarbeiterCode2 = new MitarbeiterCode("r6", "vertretungTestTest", true);
            mitarbeiterCodeDao.save(mitarbeiterCode2);

            entityManager.flush();

            // Codes suchen
            List<MitarbeiterCode> codesFound = mitarbeiterCodeDao.findAll();
            assertTrue(codesFound.size() >= 2);
            boolean found1 = false;
            boolean found2 = false;
            for (MitarbeiterCode mitarbeiterCode : codesFound) {
                if (mitarbeiterCode.getBeschreibung().equals("HelferTest")) {
                    found1 = true;
                }
                if (mitarbeiterCode.getBeschreibung().equals("vertretungTestTest")) {
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