package ch.metzenthin.svm.model.daos;

import ch.metzenthin.svm.dataTypes.Anrede;
import ch.metzenthin.svm.model.entities.Adresse;
import ch.metzenthin.svm.model.entities.Angehoeriger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import static org.junit.Assert.*;

/**
 * @author Hans Stamm
 */
public class AngehoerigerDaoTest {

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private AngehoerigerDao angehoerigerDao;

    @Before
    public void setUp() throws Exception {
        entityManagerFactory = Persistence.createEntityManagerFactory("svm");
        entityManager = entityManagerFactory.createEntityManager();
        angehoerigerDao = new AngehoerigerDao(entityManager);
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
            Angehoeriger angehoeriger = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", null, null, null, "Jurist");
            Adresse adresse = new Adresse("Hohenklingenstrasse", 15, 8049, "Zürich", "044 491 69 33");
            angehoeriger.setAdresse(adresse);
            entityManager.persist(angehoeriger);
            Angehoeriger angehoerigerFound = angehoerigerDao.findById(angehoeriger.getPersonId());
            assertEquals("Beruf not correct", "Jurist", angehoerigerFound.getBeruf());

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

            // Vater
            Angehoeriger vater = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", null, null, null, "Jurist");
            Adresse adresse = new Adresse("Hohenklingenstrasse", 15, 8049, "Zürich", "044 491 69 33");
            vater.setAdresse(adresse);
            Angehoeriger vaterSaved = angehoerigerDao.save(vater);
            Angehoeriger vaterFound = angehoerigerDao.findById(vaterSaved.getPersonId());
            assertEquals("Beruf not correct", "Jurist", vaterFound.getBeruf());
            assertEquals("Adresse not correct", "Hohenklingenstrasse", vaterFound.getAdresse().getStrasse());

            // Mutter
            Angehoeriger mutter = new Angehoeriger(Anrede.FRAU, "Eugen", "Regula", null, null, null, "Juristin");
            //mutter.setAdresse(adresse);
            mutter.setAdresse(vaterFound.getAdresse());
            Angehoeriger mutterSaved = angehoerigerDao.save(mutter);
            Angehoeriger mutterFound = angehoerigerDao.findById(mutterSaved.getPersonId());
            assertEquals("Beruf not correct", "Juristin", mutterFound.getBeruf());
            assertEquals("Adresse not correct", "Hohenklingenstrasse", mutterFound.getAdresse().getStrasse());

            // Are adresseIds equal?
            assertEquals("adresse_ids not equal", vaterFound.getAdresse().getAdresseId(), mutterFound.getAdresse().getAdresseId());

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

            // Create 2 Angehoerige with the same adress
            tx = entityManager.getTransaction();
            tx.begin();

            // Vater
            Angehoeriger vater = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", null, null, null, "Jurist");
            Adresse adresse = new Adresse("Hohenklingenstrasse", 15, 8049, "Zürich", "044 491 69 33");
            vater.setAdresse(adresse);
            Angehoeriger vaterSaved = angehoerigerDao.save(vater);
            int vaterId = vaterSaved.getPersonId();
            int adresseId = vaterSaved.getAdresse().getAdresseId();

            // Mutter
            Angehoeriger mutter = new Angehoeriger(Anrede.FRAU, "Eugen", "Regula", null, null, null, "Juristin");
            mutter.setAdresse(adresse);
            Angehoeriger mutterSaved = angehoerigerDao.save(mutter);
            int mutterId = mutterSaved.getPersonId();

            entityManager.flush();

            assertNotNull(angehoerigerDao.findById(vaterId));
            assertNotNull(angehoerigerDao.findById(mutterId));
            assertNotNull(adresseDao.findById(adresseId));

            // Delete Vater
            angehoerigerDao.remove(vaterSaved);
            entityManager.flush();
            assertNull(angehoerigerDao.findById(vaterId));
            assertNotNull(adresseDao.findById(adresseId));

            // Delete Mutter
            angehoerigerDao.remove(mutterSaved);
            entityManager.flush();
            assertNull(angehoerigerDao.findById(mutterId));
            assertNull(adresseDao.findById(adresseId));

            tx.commit();


        } catch (NullPointerException e){
            if (tx != null)
                tx.rollback();
        }

    }
}

