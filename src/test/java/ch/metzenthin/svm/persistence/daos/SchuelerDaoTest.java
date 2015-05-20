package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.dataTypes.Anrede;
import ch.metzenthin.svm.dataTypes.Geschlecht;
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
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * @author Hans Stamm
 */
public class SchuelerDaoTest {

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private SchuelerDao schuelerDao;

    @Before
    public void setUp() throws Exception {
        entityManagerFactory = Persistence.createEntityManagerFactory("svm");
        entityManager = entityManagerFactory.createEntityManager();
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
            Schueler schueler = new Schueler("Jana", "Rösle", new GregorianCalendar(2012, Calendar.JULY, 24), null, null, Geschlecht.W, new GregorianCalendar(2015, Calendar.JANUARY, 1), null, "Schwester von Valentin");
            Adresse adresse = new Adresse("Hohenklingenstrasse", "15", "8049", "Zürich", "044 491 69 33");
            schueler.setAdresse(adresse);

            // Set Vater
            Angehoeriger vater = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", null, null, null);
            vater.setAdresse(adresse);
            schueler.setVater(vater);

            // Set Rechnungsempfänger
            schueler.setRechnungsempfaenger(vater);

            entityManager.persist(schueler);

            Schueler schuelerFound = schuelerDao.findById(schueler.getPersonId());
            assertEquals("Anmeldedatum not correct", new GregorianCalendar(2015, Calendar.JANUARY, 1), schuelerFound.getAnmeldedatum());

        } finally {
            if (tx != null)
                tx.rollback();
        }

    }

    @Test
    public void testSave() {
        EntityTransaction tx = null;

        try {
            tx = entityManager.getTransaction();
            tx.begin();

            // Schueler
            Schueler schueler = new Schueler("Jana", "Rösle", new GregorianCalendar(2012, Calendar.JULY, 24), null, null, Geschlecht.W, new GregorianCalendar(2015, Calendar.JANUARY, 1), null, "Schwester von Valentin");
            Adresse adresse = new Adresse("Hohenklingenstrasse", "15", "8049", "Zürich", "044 491 69 33");
            schueler.setAdresse(adresse);

            // Set Vater
            Angehoeriger vater = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", null, null, null);
            vater.setAdresse(adresse);
            schueler.setVater(vater);

            // Set Mutter
            Angehoeriger mutter = new Angehoeriger(Anrede.FRAU, "Regula", "Rösle", null, null, null);
            mutter.setAdresse(adresse);
            schueler.setMutter(mutter);

            // Set Rechnungsempfänger
            schueler.setRechnungsempfaenger(vater);

            // Set Dispensation
            Dispensation dispensation0 = new Dispensation(new GregorianCalendar(2014, Calendar.JANUARY, 15), new GregorianCalendar(2015, Calendar.MARCH, 31), "Zu klein");
            schueler.addDispensation(dispensation0);
            Dispensation dispensation1 = new Dispensation(new GregorianCalendar(2015, Calendar.MAY, 1), new GregorianCalendar(2015, Calendar.JULY, 31), "Beinbruch");
            schueler.addDispensation(dispensation1);

            Schueler schuelerSaved = schuelerDao.save(schueler);

            Schueler schuelerFound = schuelerDao.findById(schuelerSaved.getPersonId());
            assertEquals("Anmeldedatum not correct", new GregorianCalendar(2015, Calendar.JANUARY, 1), schuelerFound.getAnmeldedatum());
            assertEquals("Vater not correct", "Eugen", schuelerFound.getVater().getVorname());
            assertEquals("Mutter not correct", "Regula", schuelerFound.getMutter().getVorname());
            assertEquals("Vater not correct", "Eugen", schuelerFound.getRechnungsempfaenger().getVorname());
            assertEquals("Adresse not correct", "Hohenklingenstrasse", schuelerFound.getAdresse().getStrasse());

            // Are Vater and Rechnungsempfänger equal?
            assertEquals("person_id not equal", schuelerFound.getVater().getPersonId(), schuelerFound.getRechnungsempfaenger().getPersonId());

            // Are adresseIds equal?
            assertEquals("adresse_id not equal", schuelerFound.getAdresse().getAdresseId(), schuelerFound.getMutter().getAdresse().getAdresseId());

            // Dispensationen
            Set<Dispensation> dispensationen = schuelerFound.getDispensationen();
            assertEquals("Dispensationen not correct", 2, dispensationen.size());
            Set<String> dispensationsgruende = new HashSet<>();
            for (Dispensation dispensation : dispensationen) {
                dispensationsgruende.add(dispensation.getGrund());
            }
            assertTrue("Dispensationsgrund not found", dispensationsgruende.contains("Beinbruch"));
            assertTrue("Dispensationsgrund not found", dispensationsgruende.contains("Zu klein"));

            // Remove Dispensation Beinbruch
            Dispensation dispensationToBeDeleted = null;
            for (Dispensation dispensation : dispensationen) {
                if (dispensation.getGrund().equals("Beinbruch")) {
                    dispensationToBeDeleted = dispensation;
                }
            }
            schuelerFound.deleteDispensation(dispensationToBeDeleted);

            Set<Dispensation> dispensationen2 = schuelerFound.getDispensationen();
            assertEquals("Dispensationen not correct", 1, dispensationen2.size());
            Set<String> dispensationsgruende2 = new HashSet<>();
            for (Dispensation dispensation : dispensationen2) {
                dispensationsgruende2.add(dispensation.getGrund());
            }
            assertFalse("Dispensationsgrund not found", dispensationsgruende2.contains("Beinbruch"));
            assertTrue("Dispensationsgrund not found", dispensationsgruende2.contains("Zu klein"));

        } finally {
            if (tx != null)
                tx.rollback();
        }

    }

    @Test
    public void testRemove() {
        EntityTransaction tx = null;

        try {

            AdresseDao adresseDao = new AdresseDao(entityManager);
            AngehoerigerDao angehoerigerDao = new AngehoerigerDao(entityManager);
            DispensationDao dispensationDao = new DispensationDao(entityManager);

            // Create 2 Schueler with the same parents, but different Rechnungsempfaenger
            tx = entityManager.getTransaction();
            tx.begin();

            // Schueler1
            Schueler schueler1 = new Schueler("Jana", "Rösle", new GregorianCalendar(2012, Calendar.JULY, 24), null, null, Geschlecht.W, new GregorianCalendar(2015, Calendar.MAY, 15), null, "Schwester von Valentin Dan");
            Adresse adresse = new Adresse("Hohenklingenstrasse", "15", "8049", "Zürich", "044 491 69 33");
            schueler1.setAdresse(adresse);

            Angehoeriger vater = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", null, null, null);
            vater.setAdresse(adresse);
            schueler1.setVater(vater);

            Angehoeriger mutter = new Angehoeriger(Anrede.FRAU, "Regula", "Rösle", null, null, null);
            mutter.setAdresse(adresse);
            schueler1.setMutter(mutter);

            Adresse adresseRechnungsempfaenger = new Adresse("Hintere Bergstrassse", "15", "8942", "Oberrieden", "044 720 85 51");
            Angehoeriger rechnungsempfaenger1 = new Angehoeriger(Anrede.FRAU, "Käthi", "Schraner", null, null, null);
            rechnungsempfaenger1.setAdresse(adresseRechnungsempfaenger);
            schueler1.setRechnungsempfaenger(rechnungsempfaenger1);

            Schueler schueler1Saved = schuelerDao.save(schueler1);

            int schuelerId1 = schueler1Saved.getPersonId();
            int vaterId = schueler1Saved.getVater().getPersonId();
            int mutterId = schueler1Saved.getMutter().getPersonId();
            int rechungsempfaengerId1 = schueler1Saved.getRechnungsempfaenger().getPersonId();
            int adresseId = schueler1Saved.getAdresse().getAdresseId();

            // Schueler2
            Schueler schueler2 = new Schueler("Valentin Dan", "Rösle", new GregorianCalendar(2014, Calendar.SEPTEMBER, 24), null, null, Geschlecht.M, new GregorianCalendar(2015, Calendar.MAY, 15), null, "Bruder von Jana");
            schueler2.setAdresse(adresse);
            schueler2.setVater(vater);
            schueler2.setMutter(mutter);
            schueler2.setRechnungsempfaenger(vater);
            schueler2.addDispensation(new Dispensation(new GregorianCalendar(2015, Calendar.MAY, 15), null, "Viel zu klein"));

            Schueler schueler2Saved = schuelerDao.save(schueler2);

            int schuelerId2 = schueler2Saved.getPersonId();
            int rechungsempfaengerId2 = schueler2Saved.getRechnungsempfaenger().getPersonId();

            Set<Dispensation> dispensationen2 = schueler2Saved.getDispensationen();
            assertEquals(1, schueler2Saved.getDispensationen().size());
            Integer dispensationId2 = null;
            for (Dispensation dispensation : dispensationen2) {
                dispensationId2 = dispensation.getDispensationId();  // We only have one element
            }

            entityManager.flush();

            assertNotNull(schuelerDao.findById(schuelerId1));
            assertNotNull(schuelerDao.findById(schuelerId2));
            assertNotNull(angehoerigerDao.findById(vaterId));
            assertNotNull(angehoerigerDao.findById(mutterId));
            assertNotNull(angehoerigerDao.findById(rechungsempfaengerId1));
            assertNotNull(angehoerigerDao.findById(rechungsempfaengerId2));
            assertNotNull(adresseDao.findById(adresseId));
            assertNotNull(dispensationDao.findById(dispensationId2));

            // Delete 1st Schueler
            schuelerDao.remove(schueler1Saved);
            entityManager.flush();

            assertNull(schuelerDao.findById(schuelerId1));
            assertNotNull(schuelerDao.findById(schuelerId2));
            assertNotNull(angehoerigerDao.findById(vaterId));
            assertNotNull(angehoerigerDao.findById(mutterId));
            assertNull(angehoerigerDao.findById(rechungsempfaengerId1));
            assertNotNull(angehoerigerDao.findById(rechungsempfaengerId2));
            assertNotNull(adresseDao.findById(adresseId));
            assertNotNull(dispensationDao.findById(dispensationId2));

            // Delete 2nd Schueler
            schuelerDao.remove(schueler2Saved);
            entityManager.flush();

            assertNull(schuelerDao.findById(schuelerId1));
            assertNull(schuelerDao.findById(schuelerId2));
            assertNull(angehoerigerDao.findById(vaterId));
            assertNull(angehoerigerDao.findById(mutterId));
            assertNull(angehoerigerDao.findById(rechungsempfaengerId1));
            assertNull(angehoerigerDao.findById(rechungsempfaengerId2));
            assertNull(adresseDao.findById(adresseId));
            assertNull(dispensationDao.findById(dispensationId2));

            tx.commit();

        } catch (NullPointerException e){
            if (tx != null)
                tx.rollback();
        }

    }

    @Test
    public void testFindSpecificSchueler() {

        EntityTransaction tx = null;

        try {
            tx = entityManager.getTransaction();
            tx.begin();

            // Schüler
            Schueler schueler = new Schueler("Lea", "Müller", new GregorianCalendar(2000, Calendar.MAY, 2), null, null, Geschlecht.W, new GregorianCalendar(2015, Calendar.JANUARY, 1), null, null);
            Adresse adresse = new Adresse("Gugusweg", "16", "8049", "Zürich", "044 491 69 33");
            schueler.setAdresse(adresse);

            Angehoeriger rechnungsempfaenger = new Angehoeriger(Anrede.FRAU, "Käthi", "Schraner", null, null, null);
            Adresse adresseRechnungsempfaenger = new Adresse("Hintere Bergstrassse", "15", "8942", "Oberrieden", "044 720 85 51");
            rechnungsempfaenger.setAdresse(adresseRechnungsempfaenger);
            schueler.setRechnungsempfaenger(rechnungsempfaenger);

            Schueler schuelerSaved = schuelerDao.save(schueler);

            entityManager.flush();

            // Create second Schüler with the same attributes
            Schueler schueler2 = new Schueler("Lea", "Müller", new GregorianCalendar(2000, Calendar.MAY, 2), null, null, Geschlecht.W, new GregorianCalendar(2015, Calendar.JANUARY, 1), null, null);
            Adresse adresse2 = new Adresse("Gugusweg", "16", "8049", "Zürich", "044 491 69 33");
            schueler2.setAdresse(adresse2);
            schueler2.setRechnungsempfaenger(rechnungsempfaenger);

            Schueler schuelerFound2 = schuelerDao.findSpecificSchueler(schueler2);
            assertNotNull("Schüler nicht gefunden", schuelerFound2);

            // Ditto, but Schüler with another strasse:
            Schueler schueler3 = new Schueler("Lea", "Müller", new GregorianCalendar(2000, Calendar.MAY, 2), null, null, Geschlecht.W, new GregorianCalendar(2015, Calendar.JANUARY, 1), null, null);
            Adresse adresse3 = new Adresse("Gugusstrasse", "16", "8049", "Zürich", "044 491 69 33");
            schueler3.setAdresse(adresse3);
            schueler3.setRechnungsempfaenger(rechnungsempfaenger);

            Schueler schuelerFound3 = schuelerDao.findSpecificSchueler(schueler3);
            assertNull("Schüler gefunden", schuelerFound3);

            // Delete Schüler
            schuelerDao.remove(schuelerSaved);

            tx.commit();

        } catch (NullPointerException e){
            if (tx != null)
                tx.rollback();
        }

    }
}

