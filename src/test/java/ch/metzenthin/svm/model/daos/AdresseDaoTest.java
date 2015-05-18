package ch.metzenthin.svm.model.daos;

import ch.metzenthin.svm.model.entities.Adresse;
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
public class AdresseDaoTest {

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private AdresseDao adresseDao;

    @Before
    public void setUp() throws Exception {
        entityManagerFactory = Persistence.createEntityManagerFactory("svm");
        entityManager = entityManagerFactory.createEntityManager();
        adresseDao = new AdresseDao(entityManager);
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
        Adresse adresse = adresseDao.findById(1);
        assertEquals("Adresse not correct", "Hintere Bergstrasse", adresse.getStrasse());
    }

    @Test
    public void testSave() {
        EntityTransaction tx = null;
        try {
            tx = entityManager.getTransaction();
            tx.begin();
            Adresse adresse = new Adresse("Buechackerstrasse", 4, 8234, "Stetten", "052 643 38 48");
            Adresse adresseSaved = adresseDao.save(adresse);
            Adresse adresseFound = adresseDao.findById(adresseSaved.getAdresseId());
            assertEquals("Adresse not correct", "Buechackerstrasse", adresseFound.getStrasse());
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

            Adresse adresse = new Adresse("Buechackerstrasse", 4, 8234, "Stetten", "052 643 38 48");
            Adresse adresseSaved = adresseDao.save(adresse);
            int adresseId = adresseSaved.getAdresseId();

            entityManager.flush();
            assertNotNull(adresseDao.findById(adresseId));

            // Delete Adresse
            adresseDao.remove(adresseSaved);
            entityManager.flush();
            assertNull(adresseDao.findById(adresseId));

            tx.commit();

        } catch (NullPointerException e){
            if (tx != null)
                tx.rollback();
        }

    }

}