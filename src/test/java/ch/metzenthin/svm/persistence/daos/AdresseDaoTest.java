package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.entities.Adresse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;
import static org.junit.Assert.*;

/**
 * @author Hans Stamm
 */
public class AdresseDaoTest {

    private DB db;
    private EntityManager entityManager;
    private AdresseDao adresseDao;

    @Before
    public void setUp() throws Exception {
        createSvmPropertiesFileDefault();
        db = DBFactory.getInstance();
        entityManager = db.getCurrentEntityManager();
        adresseDao = new AdresseDao(entityManager);
    }

    @After
    public void tearDown() throws Exception {
        db.closeSession();
    }

    @Test
    public void testFindById() {
        EntityTransaction tx = null;
        try {
            tx = entityManager.getTransaction();
            tx.begin();
            Adresse adresse = new Adresse("Buechackerstrasse", "4", "8234", "Stetten");
            entityManager.persist(adresse);
            Adresse adresseFound = adresseDao.findById(adresse.getAdresseId());
            assertEquals("Strasse falsch", "Buechackerstrasse", adresseFound.getStrasse());

        } finally {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    @Test
    public void testSave() {
        EntityTransaction tx = null;
        try {
            tx = entityManager.getTransaction();
            tx.begin();
            Adresse adresse = new Adresse("Buechackerstrasse", "4", "8234", "Stetten");
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

            Adresse adresse = new Adresse("Buechackerstrasse", "4", "8234", "Stetten");
            Adresse adresseSaved = adresseDao.save(adresse);
            int adresseId = adresseSaved.getAdresseId();

            entityManager.flush();
            assertNotNull(adresseDao.findById(adresseId));

            // Delete Adresse
            adresseDao.remove(adresseSaved);
            entityManager.flush();
            assertNull(adresseDao.findById(adresseId));

        } finally {
            if (tx != null) {
                tx.rollback();
            }
        }

    }

}