package ch.metzenthin.svm.model.daos;

import ch.metzenthin.svm.domain.SchuelerErfassen;
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
public class AdresseDaoJpaTest {

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private AdresseDaoJpa adresseDao;

    @Before
    public void setUp() throws Exception {
        entityManagerFactory = Persistence.createEntityManagerFactory("svm");
        entityManager = entityManagerFactory.createEntityManager();
        adresseDao = new AdresseDaoJpa();
        adresseDao.setEntityManager(entityManager);
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
        assertEquals("Adresse not found", "Hintere Bergstrasse", adresse.getStrasse());
    }

    @Test
    public void testSave() {
        EntityTransaction tx = null;
        try {
            tx = entityManager.getTransaction();
            tx.begin();
            Adresse adresse = new Adresse("Buechackerstrasse", 4, 8234, "Stetten", "052 643 38 48");
            Adresse result = adresseDao.save(adresse);
            Adresse result2 = adresseDao.findById(result.getAdresseId());
            assertEquals("Adresse not found", "Buechackerstrasse", result2.getStrasse());
        } finally {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

}