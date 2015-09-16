package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.common.dataTypes.Anrede;
import ch.metzenthin.svm.common.dataTypes.Geschlecht;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Dispensation;
import ch.metzenthin.svm.persistence.entities.Schueler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;

/**
 * @author Hans Stamm
 */
public class DispensationDaoTest {

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private DispensationDao dispensationDao;
    private SchuelerDao schuelerDao;

    @Before
    public void setUp() throws Exception {
        entityManagerFactory = Persistence.createEntityManagerFactory("svmtest");
        entityManager = entityManagerFactory.createEntityManager();
        dispensationDao = new DispensationDao(entityManager);
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

            // Schueler
            Schueler schueler = new Schueler("Jana", "Rösle", new GregorianCalendar(2012, Calendar.JULY, 24), "044 491 69 33", null, null, Geschlecht.W, "Schwester von Valentin");
            Adresse adresse = new Adresse("Hohenklingenstrasse", "15", "8049", "Zürich");
            schueler.setAdresse(adresse);

            // Set Vater
            Angehoeriger vater = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", "044 491 69 33", null, null);
            vater.setAdresse(adresse);
            schueler.setVater(vater);

            // Set Rechnungsempfänger
            schueler.setRechnungsempfaenger(vater);

            Dispensation dispensation = new Dispensation(new GregorianCalendar(2014, Calendar.JANUARY, 15), new GregorianCalendar(2015, Calendar.MARCH, 31), null, "Zu klein");
            schueler.addDispensation(dispensation);

            entityManager.persist(schueler);

            Dispensation dispensationFound = dispensationDao.findById(dispensation.getDispensationId());

            assertEquals("Dispensationsbeginn not correct", new GregorianCalendar(2014, Calendar.JANUARY, 15), dispensationFound.getDispensationsbeginn());

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

            // Schueler
            Schueler schueler = new Schueler("Jana", "Rösle", new GregorianCalendar(2012, Calendar.JULY, 24), "044 491 69 33", null, null, Geschlecht.W, "Schwester von Valentin");
            Adresse adresse = new Adresse("Hohenklingenstrasse", "15", "8049", "Zürich");
            schueler.setAdresse(adresse);

            // Set Vater
            Angehoeriger vater = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", "044 491 69 33", null, null);
            vater.setAdresse(adresse);
            schueler.setVater(vater);

            // Set Rechnungsempfänger
            schueler.setRechnungsempfaenger(vater);

            // Dispensationen hinzufügen
            Dispensation dispensation1 = new Dispensation(new GregorianCalendar(2013, Calendar.JANUARY, 15), new GregorianCalendar(2015, Calendar.MARCH, 31), "2 Jahre", "Zu klein");
            dispensationDao.addToSchuelerAndSave(dispensation1, schueler);

            Dispensation dispensation2 = new Dispensation(new GregorianCalendar(2015, Calendar.MAY, 15), null, "2-4 Wochen", "Armbruch");
            Schueler schuelerSaved = dispensationDao.addToSchuelerAndSave(dispensation2, schueler);

            entityManager.flush();

            // Schüler prüfen
            Schueler schuelerFound = schuelerDao.findById(schuelerSaved.getPersonId());
            entityManager.refresh(schuelerFound);
            assertEquals(2, schuelerFound.getDispensationen().size());
            // Neuste zuerst?
            assertEquals("Armbruch", schuelerFound.getDispensationen().get(0).getGrund());
            assertEquals("Zu klein", schuelerFound.getDispensationen().get(1).getGrund());

            // Dispensationen prüfen
            assertNotNull(dispensationDao.findById(dispensation1.getDispensationId()));
            assertNotNull(dispensationDao.findById(dispensation2.getDispensationId()));
            Dispensation dispensation1Found = dispensationDao.findById(dispensation1.getDispensationId());
            entityManager.refresh(dispensation1Found);
            assertEquals("Dispensationsbeginn falsch", new GregorianCalendar(2013, Calendar.JANUARY, 15), dispensation1Found.getDispensationsbeginn());
            assertEquals("Dispensationsende falsch", new GregorianCalendar(2015, Calendar.MARCH, 31), dispensation1Found.getDispensationsende());
            assertEquals("Voraussichtliche Dauer falsch", "2 Jahre", dispensation1Found.getVoraussichtlicheDauer());
            assertEquals("Grund falsch", "Zu klein", dispensation1Found.getGrund());

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

            // Schueler
            Schueler schueler = new Schueler("Jana", "Rösle", new GregorianCalendar(2012, Calendar.JULY, 24), "044 491 69 33", null, null, Geschlecht.W, "Schwester von Valentin");
            Adresse adresse = new Adresse("Hohenklingenstrasse", "15", "8049", "Zürich");
            schueler.setAdresse(adresse);

            // Set Vater
            Angehoeriger vater = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", "044 491 69 33", null, null);
            vater.setAdresse(adresse);
            schueler.setVater(vater);

            // Set Rechnungsempfänger
            schueler.setRechnungsempfaenger(vater);

            // Dispensationen hinzufügen
            Dispensation dispensation1 = new Dispensation(new GregorianCalendar(2013, Calendar.JANUARY, 15), new GregorianCalendar(2015, Calendar.MARCH, 31), "2 Jahre", "Zu klein");
            dispensationDao.addToSchuelerAndSave(dispensation1, schueler);

            Dispensation dispensation2 = new Dispensation(new GregorianCalendar(2015, Calendar.MAY, 15), null, "2-4 Wochen", "Armbruch");
            Schueler schuelerSaved = dispensationDao.addToSchuelerAndSave(dispensation2, schueler);

            entityManager.flush();

            Schueler schuelerFound = schuelerDao.findById(schuelerSaved.getPersonId());
            entityManager.refresh(schuelerFound);
            assertEquals(2, schuelerFound.getDispensationen().size());
            assertEquals("Armbruch", schuelerFound.getDispensationen().get(0).getGrund());
            assertEquals("Zu klein", schuelerFound.getDispensationen().get(1).getGrund());
            assertNotNull(dispensationDao.findById(dispensation1.getDispensationId()));
            assertNotNull(dispensationDao.findById(dispensation2.getDispensationId()));

            // 2. Dispensation von Schüler entfernen
            Schueler schuelerUpdated = dispensationDao.removeFromSchuelerAndUpdate(dispensation2, schuelerFound);
            entityManager.flush();

            schuelerFound = schuelerDao.findById(schuelerUpdated.getPersonId());
            entityManager.refresh(schuelerFound);
            assertEquals(1, schuelerFound.getDispensationen().size());
            assertEquals("Zu klein", schuelerFound.getDispensationen().get(0).getGrund());
            assertNotNull(dispensationDao.findById(dispensation1.getDispensationId()));
            assertNull(dispensationDao.findById(dispensation2.getDispensationId()));

            // 1. Dispensation von Schüler entfernen
            schuelerUpdated = dispensationDao.removeFromSchuelerAndUpdate(dispensation1, schuelerFound);
            entityManager.flush();

            schuelerFound = schuelerDao.findById(schuelerUpdated.getPersonId());
            entityManager.refresh(schuelerFound);
            assertEquals(0, schuelerFound.getDispensationen().size());
            assertNull(dispensationDao.findById(dispensation1.getDispensationId()));
            assertNull(dispensationDao.findById(dispensation2.getDispensationId()));

        } finally {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

}