package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.common.dataTypes.Anrede;
import ch.metzenthin.svm.common.dataTypes.Geschlecht;
import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Anmeldung;
import ch.metzenthin.svm.persistence.entities.Schueler;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;
import static org.junit.Assert.*;

/**
 * @author Hans Stamm
 */
public class AnmeldungDaoTest {

    private final AnmeldungDao anmeldungDao = new AnmeldungDao();
    private final SchuelerDao schuelerDao = new SchuelerDao();

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

            Anmeldung anmeldung = new Anmeldung(new GregorianCalendar(2014, Calendar.JANUARY, 15), new GregorianCalendar(2015, Calendar.MARCH, 31));
            schueler.addAnmeldung(anmeldung);

            entityManager.persist(schueler);

            Anmeldung anmeldungFound = anmeldungDao.findById(anmeldung.getAnmeldungId());

            // Erzwingen, dass von DB gelesen wird
            entityManager.refresh(anmeldungFound);

            assertEquals("Anmeldedatum falsch", new GregorianCalendar(2014, Calendar.JANUARY, 15), anmeldungFound.getAnmeldedatum());
            assertEquals("Abmeldedatum falsch", new GregorianCalendar(2015, Calendar.MARCH, 31), anmeldungFound.getAbmeldedatum());

        } finally {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

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

            // Anmeldungen hinzufügen
            Anmeldung anmeldung1 = new Anmeldung(new GregorianCalendar(2013, Calendar.JANUARY, 15), new GregorianCalendar(2014, Calendar.MARCH, 31));
            anmeldungDao.addToSchuelerAndSave(anmeldung1, schueler);

            Anmeldung anmeldung2 = new Anmeldung(new GregorianCalendar(2015, Calendar.JANUARY, 1), null);
            Schueler schuelerSaved = anmeldungDao.addToSchuelerAndSave(anmeldung2, schueler);

            entityManager.flush();

            // Schüler prüfen
            Schueler schuelerFound = schuelerDao.findById(schuelerSaved.getPersonId());
            entityManager.refresh(schuelerFound);
            assertEquals(2, schuelerFound.getAnmeldungen().size());
            // Neuste zuerst?
            assertEquals(new GregorianCalendar(2015, Calendar.JANUARY, 1), schuelerFound.getAnmeldungen().get(0).getAnmeldedatum());
            assertEquals(new GregorianCalendar(2013, Calendar.JANUARY, 15), schuelerFound.getAnmeldungen().get(1).getAnmeldedatum());

            // Anmeldungen prüfen
            assertNotNull(anmeldungDao.findById(anmeldung1.getAnmeldungId()));
            assertNotNull(anmeldungDao.findById(anmeldung2.getAnmeldungId()));
            Anmeldung anmeldung1Found = anmeldungDao.findById(anmeldung1.getAnmeldungId());
            entityManager.refresh(anmeldung1Found);
            assertEquals(new GregorianCalendar(2013, Calendar.JANUARY, 15), anmeldung1Found.getAnmeldedatum());
            assertEquals(new GregorianCalendar(2014, Calendar.MARCH, 31), anmeldung1Found.getAbmeldedatum());

        } finally {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

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

            // Anmeldungen hinzufügen
            Anmeldung anmeldung1 = new Anmeldung(new GregorianCalendar(2013, Calendar.JANUARY, 15), new GregorianCalendar(2014, Calendar.MARCH, 31));
            anmeldungDao.addToSchuelerAndSave(anmeldung1, schueler);

            Anmeldung anmeldung2 = new Anmeldung(new GregorianCalendar(2015, Calendar.JANUARY, 1), null);
            Schueler schuelerSaved = anmeldungDao.addToSchuelerAndSave(anmeldung2, schueler);

            entityManager.flush();

            Schueler schuelerFound = schuelerDao.findById(schuelerSaved.getPersonId());
            entityManager.refresh(schuelerFound);
            assertEquals(2, schuelerFound.getAnmeldungen().size());
            assertEquals(new GregorianCalendar(2015, Calendar.JANUARY, 1), schuelerFound.getAnmeldungen().get(0).getAnmeldedatum());
            assertEquals(new GregorianCalendar(2013, Calendar.JANUARY, 15), schuelerFound.getAnmeldungen().get(1).getAnmeldedatum());
            assertNotNull(anmeldungDao.findById(anmeldung1.getAnmeldungId()));
            assertNotNull(anmeldungDao.findById(anmeldung2.getAnmeldungId()));

            // 2. Anmeldung entfernen
            Schueler schuelerUpdated = anmeldungDao.removeFromSchuelerAndUpdate(anmeldung2, schueler);
            entityManager.flush();

            schuelerFound = schuelerDao.findById(schuelerUpdated.getPersonId());
            entityManager.refresh(schuelerFound);
            assertEquals(1, schuelerFound.getAnmeldungen().size());
            assertEquals(new GregorianCalendar(2013, Calendar.JANUARY, 15), schuelerFound.getAnmeldungen().get(0).getAnmeldedatum());
            assertNotNull(anmeldungDao.findById(anmeldung1.getAnmeldungId()));
            assertNull(anmeldungDao.findById(anmeldung2.getAnmeldungId()));

            // 1. Anmeldung entfernen
            schuelerUpdated = anmeldungDao.removeFromSchuelerAndUpdate(anmeldung1, schueler);
            entityManager.flush();

            schuelerFound = schuelerDao.findById(schuelerUpdated.getPersonId());
            entityManager.refresh(schuelerFound);
            assertEquals(0, schuelerFound.getAnmeldungen().size());
            assertNull(anmeldungDao.findById(anmeldung1.getAnmeldungId()));
            assertNull(anmeldungDao.findById(anmeldung2.getAnmeldungId()));

        } finally {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

}