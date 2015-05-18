package ch.metzenthin.svm.model.daos;

import ch.metzenthin.svm.dataTypes.Anrede;
import ch.metzenthin.svm.dataTypes.Geschlecht;
import ch.metzenthin.svm.model.entities.Adresse;
import ch.metzenthin.svm.model.entities.Angehoeriger;
import ch.metzenthin.svm.model.entities.Schueler;
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
        Schueler schueler = schuelerDao.findById(4);
        assertEquals("Anmeldedatum not correct", new GregorianCalendar(2015, Calendar.MAY, 9), schueler.getAnmeldedatum());
    }

    @Test
    public void testSave() {
        EntityTransaction tx = null;

        try {
            tx = entityManager.getTransaction();
            tx.begin();

            // Schueler
            Schueler schueler = new Schueler("Jana", "Rösle", new GregorianCalendar(2012, Calendar.JULY, 24), null, null, Geschlecht.W, new GregorianCalendar(2015, Calendar.MAY, 15), null, null, null, "Schwester von Valentin");
            Adresse adresse = new Adresse("Hohenklingenstrasse", 15, 8049, "Zürich", "044 491 69 33");
            schueler.setAdresse(adresse);

            // Set Vater
            Angehoeriger vater = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", null, null, null, "Jurist");
            vater.setAdresse(adresse);
            schueler.setVater(vater);

            // Set Mutter
            Angehoeriger mutter = new Angehoeriger(Anrede.FRAU, "Regula", "Rösle", null, null, null, "Juristin");
            mutter.setAdresse(adresse);
            schueler.setMutter(mutter);

            // Set Rechnungsempfänger
            schueler.setRechnungsempfaenger(vater);

            Schueler schuelerSaved = schuelerDao.save(schueler);

            Schueler schuelerFound = schuelerDao.findById(schuelerSaved.getPersonId());
            assertEquals("Anmeldedatum not found", new GregorianCalendar(2015, Calendar.MAY, 15), schuelerFound.getAnmeldedatum());
            assertEquals("Vater not correct", "Eugen", schuelerFound.getVater().getVorname());
            assertEquals("Mutter not correct", "Regula", schuelerFound.getMutter().getVorname());
            assertEquals("Vater not correct", "Eugen", schuelerFound.getRechnungsempfaenger().getVorname());
            assertEquals("Adresse not correct", "Hohenklingenstrasse", schuelerFound.getAdresse().getStrasse());

            // Are Vater and Rechnungsempfänger equal?
            assertEquals("person_id not equal", schuelerFound.getVater().getPersonId(), schuelerFound.getRechnungsempfaenger().getPersonId());

            // Are adresseIds equal?
            assertEquals("adresse_id not equal", schuelerFound.getAdresse().getAdresseId(), schuelerFound.getMutter().getAdresse().getAdresseId());

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

            // Create 2 Schueler with the same parents, but different Rechnungsempfaenger
            tx = entityManager.getTransaction();
            tx.begin();

            // Schueler1
            Schueler schueler1 = new Schueler("Jana", "Rösle", new GregorianCalendar(2012, Calendar.JULY, 24), null, null, Geschlecht.W, new GregorianCalendar(2015, Calendar.MAY, 15), null, null, null, "Schwester von Valentin Dan");
            Adresse adresse = new Adresse("Hohenklingenstrasse", 15, 8049, "Zürich", "044 491 69 33");
            schueler1.setAdresse(adresse);

            Angehoeriger vater = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", null, null, null, "Jurist");
            vater.setAdresse(adresse);
            schueler1.setVater(vater);

            Angehoeriger mutter = new Angehoeriger(Anrede.FRAU, "Regula", "Rösle", null, null, null, "Juristin");
            mutter.setAdresse(adresse);
            schueler1.setMutter(mutter);

            schueler1.setRechnungsempfaenger(mutter);

            Schueler schueler1Saved = schuelerDao.save(schueler1);

            int schuelerId1 = schueler1Saved.getPersonId();
            int vaterId = schueler1Saved.getVater().getPersonId();
            int mutterId = schueler1Saved.getMutter().getPersonId();
            int rechungsempfaengerId1 = schueler1Saved.getRechnungsempfaenger().getPersonId();
            int adresseId = schueler1Saved.getAdresse().getAdresseId();

            // Schueler2
            Schueler schueler2 = new Schueler("Valentin Dan", "Rösle", new GregorianCalendar(2014, Calendar.SEPTEMBER, 24), null, null, Geschlecht.M, new GregorianCalendar(2015, Calendar.MAY, 15), null, null, null, "Bruder von Jana");
            schueler2.setAdresse(adresse);
            schueler2.setVater(vater);
            schueler2.setMutter(mutter);

            Angehoeriger rechnungsempfaenger = angehoerigerDao.findById(1);
            schueler2.setRechnungsempfaenger(rechnungsempfaenger);

            Schueler schueler2Saved = schuelerDao.save(schueler2);
            int schuelerId2 = schueler2Saved.getPersonId();
            int rechungsempfaengerId2 = schueler2Saved.getRechnungsempfaenger().getPersonId();

            entityManager.flush();

            assertNotNull(schuelerDao.findById(schuelerId1));
            assertNotNull(schuelerDao.findById(schuelerId2));
            assertNotNull(angehoerigerDao.findById(vaterId));
            assertNotNull(angehoerigerDao.findById(mutterId));
            assertNotNull(angehoerigerDao.findById(rechungsempfaengerId1));
            assertNotNull(angehoerigerDao.findById(rechungsempfaengerId2));
            assertNotNull(adresseDao.findById(adresseId));

            // Delete 1st Schueler
            schuelerDao.remove(schueler1Saved);
            entityManager.flush();

            assertNull(schuelerDao.findById(schuelerId1));
            assertNotNull(schuelerDao.findById(schuelerId2));
            assertNotNull(angehoerigerDao.findById(vaterId));
            assertNotNull(angehoerigerDao.findById(mutterId));
            assertNotNull(angehoerigerDao.findById(rechungsempfaengerId1));
            assertNotNull(angehoerigerDao.findById(rechungsempfaengerId2));
            assertNotNull(adresseDao.findById(adresseId));

            // Delete 2nd Schueler
            schuelerDao.remove(schueler2Saved);
            entityManager.flush();

            assertNull(schuelerDao.findById(schuelerId1));
            assertNull(schuelerDao.findById(schuelerId2));
            assertNull(angehoerigerDao.findById(vaterId));
            assertNull(angehoerigerDao.findById(mutterId));
            assertNull(angehoerigerDao.findById(rechungsempfaengerId1));
            assertNotNull(angehoerigerDao.findById(rechungsempfaengerId2));
            assertNull(adresseDao.findById(adresseId));

            tx.commit();

        } catch (NullPointerException e){
            if (tx != null)
                tx.rollback();
        }

    }
}

