package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.common.dataTypes.Anrede;
import ch.metzenthin.svm.common.dataTypes.Geschlecht;
import ch.metzenthin.svm.common.utils.SvmProperties;
import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Dispensation;
import ch.metzenthin.svm.persistence.entities.Schueler;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;
import static org.junit.Assert.*;

/**
 * @author Hans Stamm
 */
public class DispensationDaoTest {

    private final DispensationDao dispensationDao = new DispensationDao();
    private final SchuelerDao schuelerDao = new SchuelerDao();

    private DB db;
    private boolean neusteZuoberst;

    @Before
    public void setUp() {
        createSvmPropertiesFileDefault();
        db = DBFactory.getInstance();
        Properties svmProperties = SvmProperties.getSvmProperties();
        neusteZuoberst = !svmProperties.getProperty(SvmProperties.KEY_NEUSTE_ZUOBERST).equals("false");
    }

    @After
    public void tearDown() {
        db.closeSession();
    }

    @SuppressWarnings("ExtractMethodRecommender")
    @Test
    public void testFindById() {
        EntityManager entityManager = db.getCurrentEntityManager();
        EntityTransaction tx = null;
        try {
            tx = entityManager.getTransaction();
            tx.begin();

            // Schueler
            Schueler schueler = new Schueler("Jana", "Rösle", new GregorianCalendar(2012, Calendar.JULY, 24), "044 491 69 33", null, null, Geschlecht.W, "Schwester von Valentin");
            Adresse adresse = new Adresse("Hohenklingenstrasse", "15", "8049", "Zürich");
            schueler.setAdresse(adresse);

            // Set Vater
            Angehoeriger vater = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", "044 491 69 33", null, null, false);
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

    @SuppressWarnings("ExtractMethodRecommender")
    @Test
    public void testAddToSchuelerAndSave() {
        EntityManager entityManager = db.getCurrentEntityManager();
        EntityTransaction tx = null;
        try {
            tx = entityManager.getTransaction();
            tx.begin();

            // Schueler
            Schueler schueler = new Schueler("Jana", "Rösle", new GregorianCalendar(2012, Calendar.JULY, 24), "044 491 69 33", null, null, Geschlecht.W, "Schwester von Valentin");
            Adresse adresse = new Adresse("Hohenklingenstrasse", "15", "8049", "Zürich");
            schueler.setAdresse(adresse);

            // Set Vater
            Angehoeriger vater = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", "044 491 69 33", null, null, false);
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
            if (neusteZuoberst) {
                assertEquals("Armbruch", schuelerFound.getDispensationen().get(0).getGrund());
                assertEquals("Zu klein", schuelerFound.getDispensationen().get(1).getGrund());
            } else {
                assertEquals("Armbruch", schuelerFound.getDispensationen().get(1).getGrund());
                assertEquals("Zu klein", schuelerFound.getDispensationen().get(0).getGrund());
            }

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

    @SuppressWarnings("ExtractMethodRecommender")
    @Test
    public void testRemoveFromSchuelerAndUpdate() {
        EntityManager entityManager = db.getCurrentEntityManager();
        EntityTransaction tx = null;
        try {
            tx = entityManager.getTransaction();
            tx.begin();

            // Schueler
            Schueler schueler = new Schueler("Jana", "Rösle", new GregorianCalendar(2012, Calendar.JULY, 24), "044 491 69 33", null, null, Geschlecht.W, "Schwester von Valentin");
            Adresse adresse = new Adresse("Hohenklingenstrasse", "15", "8049", "Zürich");
            schueler.setAdresse(adresse);

            // Set Vater
            Angehoeriger vater = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", "044 491 69 33", null, null, false);
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
            if (neusteZuoberst) {
                assertEquals("Armbruch", schuelerFound.getDispensationen().get(0).getGrund());
                assertEquals("Zu klein", schuelerFound.getDispensationen().get(1).getGrund());
            } else {
                assertEquals("Armbruch", schuelerFound.getDispensationen().get(1).getGrund());
                assertEquals("Zu klein", schuelerFound.getDispensationen().get(0).getGrund());
            }
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