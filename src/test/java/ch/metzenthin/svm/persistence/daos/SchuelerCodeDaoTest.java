package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.common.dataTypes.Anrede;
import ch.metzenthin.svm.common.dataTypes.Geschlecht;
import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Schueler;
import ch.metzenthin.svm.persistence.entities.SchuelerCode;
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
public class SchuelerCodeDaoTest {

    private DB db;
    private EntityManager entityManager;
    private SchuelerCodeDao schuelerCodeDao;
    private SchuelerDao schuelerDao;

    @Before
    public void setUp() throws Exception {
        createSvmPropertiesFileDefault();
        db = DBFactory.getInstance();
        entityManager = db.getCurrentEntityManager();
        schuelerCodeDao = new SchuelerCodeDao(entityManager);
        schuelerDao = new SchuelerDao(entityManager);
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

            SchuelerCode schuelerCode = new SchuelerCode("tz", "ZirkusprojektTest", true);

            entityManager.persist(schuelerCode);

            SchuelerCode schuelerCodeFound = schuelerCodeDao.findById(schuelerCode.getCodeId());

            assertEquals("ZirkusprojektTest", schuelerCodeFound.getBeschreibung());

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

            SchuelerCode schuelerCode = new SchuelerCode("tz", "ZirkusprojektTest", true);
            SchuelerCode schuelerCodeSaved = schuelerCodeDao.save(schuelerCode);

            entityManager.flush();

            SchuelerCode schuelerCodeFound = schuelerCodeDao.findById(schuelerCodeSaved.getCodeId());

            // Erzwingen, dass von DB gelesen wird
            entityManager.refresh(schuelerCodeFound);

            assertEquals("ZirkusprojektTest", schuelerCodeFound.getBeschreibung());

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

            SchuelerCode schuelerCode1 = new SchuelerCode("z", "Zirkusprojekt", true);
            SchuelerCode schuelerCode1Saved = schuelerCodeDao.save(schuelerCode1);
            int code1Id = schuelerCode1Saved.getCodeId();

            SchuelerCode schuelerCode2 = new SchuelerCode("r6", "6-Jahres-Rabatt", true);
            SchuelerCode schuelerCode2Saved = schuelerCodeDao.save(schuelerCode2);
            int code2Id = schuelerCode2Saved.getCodeId();

            entityManager.flush();

            assertNotNull(schuelerCodeDao.findById(code1Id));
            assertNotNull(schuelerCodeDao.findById(code2Id));

            // 1. SchuelerCode löschen
            schuelerCodeDao.remove(schuelerCode1Saved);
            entityManager.flush();

            assertNull(schuelerCodeDao.findById(code1Id));
            assertNotNull(schuelerCodeDao.findById(code2Id));

            // 2. SchuelerCode löschen
            schuelerCodeDao.remove(schuelerCode2Saved);
            entityManager.flush();

            assertNull(schuelerCodeDao.findById(code2Id));
            assertNull(schuelerCodeDao.findById(code2Id));

        } finally {
            if (tx != null) {
                tx.rollback();
            }
        }

    }

    @Test
    public void testAddToSchuelerAndSave() {
        EntityTransaction tx = null;
        try {
            tx = entityManager.getTransaction();
            tx.begin();

            // Schüler
            Schueler schueler = new Schueler("Jana", "Rösle", new GregorianCalendar(2012, Calendar.JULY, 24), null, null, null, Geschlecht.W, "Schwester von Valentin");
            Adresse adresse = new Adresse("Hohenklingenstrasse", "15", "8049", "Zürich");
            schueler.setAdresse(adresse);
            Angehoeriger vater = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", null, null, null);
            vater.setAdresse(adresse);
            schueler.setVater(vater);
            schueler.setRechnungsempfaenger(vater);

            // Codes hinzufügen
            SchuelerCode schuelerCode1 = new SchuelerCode("z", "Zirkusprojekt", true);
            schuelerCodeDao.save(schuelerCode1);
            schuelerCodeDao.addToSchuelerAndSave(schuelerCode1, schueler);

            SchuelerCode schuelerCode2 = new SchuelerCode("r6", "6-Jahres-Rabatt", true);
            schuelerCodeDao.save(schuelerCode2);
            Schueler schuelerSaved = schuelerCodeDao.addToSchuelerAndSave(schuelerCode2, schueler);

            entityManager.flush();

            // Schueler prüfen
            Schueler schuelerFound = schuelerDao.findById(schuelerSaved.getPersonId());
            entityManager.refresh(schuelerFound);
            assertEquals(2, schuelerFound.getSchuelerCodes().size());
            // Alphatbetisch geordnet?
            assertEquals("r6", schuelerFound.getSchuelerCodesAsList().get(0).getKuerzel());
            assertEquals("z", schuelerFound.getSchuelerCodesAsList().get(1).getKuerzel());

            // SchuelerCode prüfen
            SchuelerCode schuelerCode1Found = schuelerCodeDao.findById(schuelerCode1.getCodeId());
            entityManager.refresh(schuelerCode1Found);
            assertEquals(1, schuelerCode1Found.getSchueler().size());

        } finally {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    @Test
    public void testRemoveFromSchuelerAndUpdate() {
        EntityTransaction tx = null;
        try {
            tx = entityManager.getTransaction();
            tx.begin();

            // Schüler
            Schueler schueler = new Schueler("Jana", "Rösle", new GregorianCalendar(2012, Calendar.JULY, 24), null, null, null, Geschlecht.W, "Schwester von Valentin");
            Adresse adresse = new Adresse("Hohenklingenstrasse", "15", "8049", "Zürich");
            schueler.setAdresse(adresse);
            Angehoeriger vater = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", null, null, null);
            vater.setAdresse(adresse);
            schueler.setVater(vater);
            schueler.setRechnungsempfaenger(vater);

            // Codes hinzufügen
            SchuelerCode schuelerCode1 = new SchuelerCode("z", "Zirkusprojekt", true);
            schuelerCodeDao.save(schuelerCode1);
            schuelerCodeDao.addToSchuelerAndSave(schuelerCode1, schueler);

            SchuelerCode schuelerCode2 = new SchuelerCode("r6", "6-Jahres-Rabatt", true);
            schuelerCodeDao.save(schuelerCode2);
            Schueler schuelerSaved = schuelerCodeDao.addToSchuelerAndSave(schuelerCode2, schueler);

            entityManager.flush();

            // Schüler prüfen
            Schueler schuelerFound = schuelerDao.findById(schuelerSaved.getPersonId());
            entityManager.refresh(schuelerFound);
            assertEquals(2, schuelerFound.getSchuelerCodes().size());
            assertEquals("r6", schuelerFound.getSchuelerCodesAsList().get(0).getKuerzel());
            assertEquals("z", schuelerFound.getSchuelerCodesAsList().get(1).getKuerzel());

            // Codes prüfen
            SchuelerCode schuelerCode1Found = schuelerCodeDao.findById(schuelerCode1.getCodeId());
            entityManager.refresh(schuelerCode1Found);
            assertEquals(1, schuelerCode1Found.getSchueler().size());
            SchuelerCode schuelerCode2Found = schuelerCodeDao.findById(schuelerCode2.getCodeId());
            entityManager.refresh(schuelerCode2Found);
            assertEquals(1, schuelerCode2Found.getSchueler().size());

            // 1. SchuelerCode von Schüler entfernen
            Schueler schuelerUpdated = schuelerCodeDao.removeFromSchuelerAndUpdate(schuelerCode1Found, schuelerFound);
            entityManager.flush();

            schuelerFound = schuelerDao.findById(schuelerUpdated.getPersonId());
            entityManager.refresh(schuelerFound);
            assertEquals(1, schuelerFound.getSchuelerCodes().size());
            assertEquals("r6", schuelerFound.getSchuelerCodesAsList().get(0).getKuerzel());

            assertEquals(0, schuelerCode1Found.getSchueler().size());
            assertEquals(1, schuelerCode2Found.getSchueler().size());

            // 2. SchuelerCode von Schüler entfernen
            schuelerUpdated = schuelerCodeDao.removeFromSchuelerAndUpdate(schuelerCode2Found, schuelerFound);
            entityManager.flush();

            schuelerFound = schuelerDao.findById(schuelerUpdated.getPersonId());
            entityManager.refresh(schuelerFound);
            assertEquals(0, schuelerFound.getSchuelerCodes().size());

            assertEquals(0, schuelerCode1Found.getSchueler().size());
            assertEquals(0, schuelerCode2Found.getSchueler().size());

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
            SchuelerCode schuelerCode1 = new SchuelerCode("z", "ZirkusprojektTest", true);
            schuelerCodeDao.save(schuelerCode1);

            SchuelerCode schuelerCode2 = new SchuelerCode("r6", "6-Jahres-RabattTest", true);
            schuelerCodeDao.save(schuelerCode2);

            entityManager.flush();

            // Codes suchen
            List<SchuelerCode> codesFound = schuelerCodeDao.findAll();
            assertTrue(codesFound.size() >= 2);
            boolean found1 = false;
            boolean found2 = false;
            for (SchuelerCode schuelerCode : codesFound) {
                if (schuelerCode.getBeschreibung().equals("ZirkusprojektTest")) {
                    found1 = true;
                }
                if (schuelerCode.getBeschreibung().equals("6-Jahres-RabattTest")) {
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