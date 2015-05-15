package ch.metzenthin.svm.model.daos;

import ch.metzenthin.svm.dataTypes.Anrede;
import ch.metzenthin.svm.dataTypes.Elternrolle;
import ch.metzenthin.svm.model.entities.Adresse;
import ch.metzenthin.svm.model.entities.Angehoeriger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import java.util.Objects;

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
        angehoerigerDao = new AngehoerigerDao();
        angehoerigerDao.setEntityManager(entityManager);
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
        Angehoeriger angehoeriger = angehoerigerDao.findById(3);
        assertEquals("Beruf not found", "Sportlehrerin", angehoeriger.getBeruf());
    }

    @Test
    public void testSave() {
        EntityTransaction tx = null;

        try {
            tx = entityManager.getTransaction();
            tx.begin();

            // Vater
            Angehoeriger vater = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", null, null, null, Elternrolle.VATER, true, "Jurist");
            Adresse adresse = new Adresse("Hohenklingenstrasse", 15, 8049, "Zürich", "044 491 69 33");
            vater.setAdresse(adresse);
            Angehoeriger vaterSaved = angehoerigerDao.save(vater);
            Angehoeriger vaterFound = angehoerigerDao.findById(vaterSaved.getPersonId());
            assertEquals("Beruf not found", "Jurist", vaterFound.getBeruf());
            assertEquals("Adresse not found", "Hohenklingenstrasse", vaterFound.getAdresse().getStrasse());

            // Mutter
            Angehoeriger mutter = new Angehoeriger(Anrede.FRAU, "Eugen", "Regula", null, null, null, Elternrolle.MUTTER, true, "Juristin");
            //mutter.setAdresse(adresse);
            mutter.setAdresse(vaterFound.getAdresse());
            Angehoeriger mutterSaved = angehoerigerDao.save(mutter);
            Angehoeriger mutterFound = angehoerigerDao.findById(mutterSaved.getPersonId());
            assertEquals("Beruf not found", "Juristin", mutterFound.getBeruf());
            assertEquals("Adresse not found", "Hohenklingenstrasse", mutterFound.getAdresse().getStrasse());

            // Are adresseIds equal?
            assertTrue("adresse_id not equal", Objects.equals(vater.getAdresse().getAdresseId(), mutter.getAdresse().getAdresseId()));

        } finally {
            if (tx != null)
                tx.rollback();
        }

    }

    @Test
    public void testRemove() {

        EntityTransaction tx = null;

        try {

            // Create 2 Angehoerige with the same adress
            tx = entityManager.getTransaction();
            tx.begin();

            // Vater
            Angehoeriger vater = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", null, null, null, Elternrolle.VATER, true, "Jurist");
            Adresse adresse = new Adresse("Hohenklingenstrasse", 15, 8049, "Zürich", "044 491 69 33");
            vater.setAdresse(adresse);
            Angehoeriger vaterSaved = angehoerigerDao.save(vater);
            int vaterId = vaterSaved.getPersonId();
            int adresseId = vaterSaved.getAdresse().getAdresseId();

            // Mutter
            Angehoeriger mutter = new Angehoeriger(Anrede.FRAU, "Eugen", "Regula", null, null, null, Elternrolle.MUTTER, true, "Juristin");
            mutter.setAdresse(adresse);
            Angehoeriger mutterSaved = angehoerigerDao.save(mutter);
            int mutterId = mutterSaved.getPersonId();

            entityManager.flush();

            assertNotNull(angehoerigerDao.findById(vaterId));
            assertNotNull(angehoerigerDao.findById(mutterId));
            assertNotNull(entityManager.find(Adresse.class, adresseId));

            // Delete data
            angehoerigerDao.remove(vaterSaved);
            entityManager.flush();
            assertNull(angehoerigerDao.findById(vaterId));
            assertNotNull(entityManager.find(Adresse.class, adresseId));

            angehoerigerDao.remove(mutterSaved);
            entityManager.flush();
            assertNull(angehoerigerDao.findById(mutterId));
            assertNull(entityManager.find(Adresse.class, adresseId));

            tx.commit();


        } catch (NullPointerException e){
            if (tx != null)
                tx.rollback();
        }

    }
}

