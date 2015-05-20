package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.dataTypes.Anrede;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
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
            Angehoeriger angehoeriger = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", null, null, null);
            Adresse adresse = new Adresse("Hohenklingenstrasse", 15, 8049, "Zürich", "044 491 69 33");
            angehoeriger.setAdresse(adresse);
            entityManager.persist(angehoeriger);
            Angehoeriger angehoerigerFound = angehoerigerDao.findById(angehoeriger.getPersonId());
            assertEquals("Adresse not correct", "Hohenklingenstrasse", angehoerigerFound.getAdresse().getStrasse());
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
            Angehoeriger vater = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", null, null, null);
            Adresse adresse = new Adresse("Hohenklingenstrasse", 15, 8049, "Zürich", "044 491 69 33");
            vater.setAdresse(adresse);
            Angehoeriger vaterSaved = angehoerigerDao.save(vater);
            Angehoeriger vaterFound = angehoerigerDao.findById(vaterSaved.getPersonId());
            assertEquals("Adresse not correct", "Hohenklingenstrasse", vaterFound.getAdresse().getStrasse());

            // Mutter
            Angehoeriger mutter = new Angehoeriger(Anrede.FRAU, "Eugen", "Regula", null, null, null);
            //mutter.setAdresse(adresse);
            mutter.setAdresse(vaterFound.getAdresse());
            Angehoeriger mutterSaved = angehoerigerDao.save(mutter);
            Angehoeriger mutterFound = angehoerigerDao.findById(mutterSaved.getPersonId());
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
            Angehoeriger vater = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", null, null, null);
            Adresse adresse = new Adresse("Hohenklingenstrasse", 15, 8049, "Zürich", "044 491 69 33");
            vater.setAdresse(adresse);
            Angehoeriger vaterSaved = angehoerigerDao.save(vater);
            int vaterId = vaterSaved.getPersonId();
            int adresseId = vaterSaved.getAdresse().getAdresseId();

            // Mutter
            Angehoeriger mutter = new Angehoeriger(Anrede.FRAU, "Eugen", "Regula", null, null, null);
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

    @Test
    public void testFindSpecificAngehoeriger() {
        EntityTransaction tx = null;

        try {
            tx = entityManager.getTransaction();
            tx.begin();

            // Angehoeriger
            Angehoeriger angehoeriger = new Angehoeriger(Anrede.HERR, "Urs", "Berger", null, null, null);
            Adresse adresse = new Adresse("Gugusweg", 16, 8049, "Zürich", "044 491 69 33");
            angehoeriger.setAdresse(adresse);
            Angehoeriger angehoerigerSaved = angehoerigerDao.save(angehoeriger);

            entityManager.flush();

            // Create second Angehoeriger with the same attributes
            Angehoeriger angehoeriger2 = new Angehoeriger(Anrede.HERR, "Urs", "Berger", null, null, null);
            Adresse adresse2 = new Adresse("Gugusweg", 16, 8049, "Zürich", "044 491 69 33");
            angehoeriger2.setAdresse(adresse2);

            Angehoeriger angehoerigerFound2 = angehoerigerDao.findSpecificAngehoeriger(angehoeriger2);
            assertNotNull("Angehörigen nicht gefunden", angehoerigerFound2);

            // Ditto, but Angehoeriger with another strasse:
            Angehoeriger angehoeriger3 = new Angehoeriger(Anrede.HERR, "Urs", "Berger", null, null, null);
            Adresse adresse3 = new Adresse("Gugusstrasse", 16, 8049, "Zürich", "044 491 69 33");
            angehoeriger3.setAdresse(adresse3);

            Angehoeriger angehoerigerFound3 = angehoerigerDao.findSpecificAngehoeriger(angehoeriger3);
            assertNull("Angehörigen gefunden", angehoerigerFound3);

            // Delete Angehoeriger
            angehoerigerDao.remove(angehoerigerSaved);

            tx.commit();

        } catch (NullPointerException e){
            if (tx != null)
                tx.rollback();
        }

    }
}

