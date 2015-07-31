package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.dataTypes.Anrede;
import ch.metzenthin.svm.dataTypes.Geschlecht;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Code;
import ch.metzenthin.svm.persistence.entities.Schueler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.*;

import static org.junit.Assert.*;

/**
 * @author Martin Schraner
 */
public class CodeDaoTest {

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private CodeDao codeDao;
    private SchuelerDao schuelerDao;

    @Before
    public void setUp() throws Exception {
        entityManagerFactory = Persistence.createEntityManagerFactory("svm");
        entityManager = entityManagerFactory.createEntityManager();
        codeDao = new CodeDao(entityManager);
        schuelerDao = new SchuelerDao(entityManager);
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

            Code code = new Code("z", "Zirkusprojekt");

            entityManager.persist(code);

            Code codeFound = codeDao.findById(code.getCodeId());

            assertEquals("Zirkusprojekt", codeFound.getBeschreibung());

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

            Code code = new Code("z", "Zirkusprojekt");
            Code codeSaved = codeDao.save(code);

            entityManager.flush();

            Code codeFound = codeDao.findById(codeSaved.getCodeId());

            // Erzwingen, dass von DB gelesen wird
            entityManager.refresh(codeFound);

            assertEquals("Zirkusprojekt", codeFound.getBeschreibung());

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

            Code code1 = new Code("z", "Zirkusprojekt");
            Code code1Saved = codeDao.save(code1);
            int code1Id = code1Saved.getCodeId();

            Code code2 = new Code("r6", "6-Jahres-Rabatt");
            Code code2Saved = codeDao.save(code2);
            int code2Id = code2Saved.getCodeId();

            entityManager.flush();

            assertNotNull(codeDao.findById(code1Id));
            assertNotNull(codeDao.findById(code2Id));

            // 1. Code löschen
            codeDao.remove(code1Saved);
            entityManager.flush();

            assertNull(codeDao.findById(code1Id));
            assertNotNull(codeDao.findById(code2Id));

            // 2. Code löschen
            codeDao.remove(code2Saved);
            entityManager.flush();

            assertNull(codeDao.findById(code2Id));
            assertNull(codeDao.findById(code2Id));

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
            Code code1 = new Code("z", "Zirkusprojekt");
            codeDao.save(code1);
            codeDao.addToSchuelerAndSave(code1, schueler);

            Code code2 = new Code("r6", "6-Jahres-Rabatt");
            codeDao.save(code2);
            Schueler schuelerSaved = codeDao.addToSchuelerAndSave(code2, schueler);

            entityManager.flush();

            // Schueler prüfen
            Schueler schuelerFound = schuelerDao.findById(schuelerSaved.getPersonId());
            entityManager.refresh(schuelerFound);
            assertEquals(2, schuelerFound.getCodes().size());
            // Alphatbetisch geordnet?
            assertEquals("r6", schuelerFound.getCodesAsList().get(0).getKuerzel());
            assertEquals("z", schuelerFound.getCodesAsList().get(1).getKuerzel());

            // Code prüfen
            Code code1Found = codeDao.findById(code1.getCodeId());
            entityManager.refresh(code1Found);
            assertEquals(1, code1Found.getSchueler().size());

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
            Code code1 = new Code("z", "Zirkusprojekt");
            codeDao.save(code1);
            codeDao.addToSchuelerAndSave(code1, schueler);

            Code code2 = new Code("r6", "6-Jahres-Rabatt");
            codeDao.save(code2);
            Schueler schuelerSaved = codeDao.addToSchuelerAndSave(code2, schueler);

            entityManager.flush();

            // Schüler prüfen
            Schueler schuelerFound = schuelerDao.findById(schuelerSaved.getPersonId());
            entityManager.refresh(schuelerFound);
            assertEquals(2, schuelerFound.getCodes().size());
            assertEquals("r6", schuelerFound.getCodesAsList().get(0).getKuerzel());
            assertEquals("z", schuelerFound.getCodesAsList().get(1).getKuerzel());

            // Codes prüfen
            Code code1Found = codeDao.findById(code1.getCodeId());
            entityManager.refresh(code1Found);
            assertEquals(1, code1Found.getSchueler().size());
            Code code2Found = codeDao.findById(code2.getCodeId());
            entityManager.refresh(code2Found);
            assertEquals(1, code2Found.getSchueler().size());

            // 1. Code von Schüler entfernen
            Schueler schuelerUpdated = codeDao.removeFromSchuelerAndUpdate(code1Found, schuelerFound);
            entityManager.flush();

            schuelerFound = schuelerDao.findById(schuelerUpdated.getPersonId());
            entityManager.refresh(schuelerFound);
            assertEquals(1, schuelerFound.getCodes().size());
            assertEquals("r6", schuelerFound.getCodesAsList().get(0).getKuerzel());

            assertEquals(0, code1Found.getSchueler().size());
            assertEquals(1, code2Found.getSchueler().size());

            // 2. Code von Schüler entfernen
            schuelerUpdated = codeDao.removeFromSchuelerAndUpdate(code2Found, schuelerFound);
            entityManager.flush();

            schuelerFound = schuelerDao.findById(schuelerUpdated.getPersonId());
            entityManager.refresh(schuelerFound);
            assertEquals(0, schuelerFound.getCodes().size());

            assertEquals(0, code1Found.getSchueler().size());
            assertEquals(0, code2Found.getSchueler().size());

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
            Code code1 = new Code("z", "ZirkusprojektTest");
            codeDao.save(code1);

            Code code2 = new Code("r6", "6-Jahres-RabattTest");
            codeDao.save(code2);

            entityManager.flush();

            // Codes suchen
            List<Code> codesFound = codeDao.findAll();
            assertTrue(codesFound.size() >= 2);
            boolean found1 = false;
            boolean found2 = false;
            for (Code code : codesFound) {
                if (code.getBeschreibung().equals("ZirkusprojektTest")) {
                    found1 = true;
                }
                if (code.getBeschreibung().equals("6-Jahres-RabattTest")) {
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