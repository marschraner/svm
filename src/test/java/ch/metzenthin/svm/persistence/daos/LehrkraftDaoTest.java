package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.common.dataTypes.Anrede;
import ch.metzenthin.svm.common.utils.PersistenceProperties;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Lehrkraft;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;
import static org.junit.Assert.*;

/**
 * @author Martin Schraner
 */
public class LehrkraftDaoTest {

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private LehrkraftDao lehrkraftDao;

    @Before
    public void setUp() throws Exception {
        createSvmPropertiesFileDefault();
        entityManagerFactory = Persistence.createEntityManagerFactory("svm", PersistenceProperties.getPersistenceProperties());
        entityManager = entityManagerFactory.createEntityManager();
        lehrkraftDao = new LehrkraftDao(entityManager);
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

            Lehrkraft lehrkraft = new Lehrkraft(Anrede.FRAU, "Noémie", "Roos", new GregorianCalendar(1994, Calendar.MARCH, 18), "044 391 45 35", "076 384 45 35", "nroos@gmx.ch", "756.3943.8722.22", "Mi, Fr, Sa", true);
            Adresse adresse = new Adresse("Rebwiesenstrasse", "54", "8702", "Zollikon");
            lehrkraft.setAdresse(adresse);

            entityManager.persist(lehrkraft);

            Lehrkraft lehrkraftFound = lehrkraftDao.findById(lehrkraft.getPersonId());
            assertEquals("Roos", lehrkraftFound.getNachname());

        } finally {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    @Test
    public void save() {
        EntityTransaction tx = null;
        try {
            tx = entityManager.getTransaction();
            tx.begin();

            Lehrkraft lehrkraft = new Lehrkraft(Anrede.FRAU, "Noémie", "Roos", new GregorianCalendar(1994, Calendar.MARCH, 18), "044 391 45 35", "076 384 45 35", "nroos@gmx.ch", "756.3943.8722.22", "Mi, Fr, Sa", true);
            Adresse adresse = new Adresse("Rebwiesenstrasse", "54", "8702", "Zollikon");
            lehrkraft.setAdresse(adresse);

            Lehrkraft lehrkraftSaved = lehrkraftDao.save(lehrkraft);
            entityManager.flush();

            Lehrkraft lehrkraftFound = lehrkraftDao.findById(lehrkraftSaved.getPersonId());

            // Erzwingen, dass von DB gelesen wird
            entityManager.refresh(lehrkraftFound);

            assertEquals("Noémie", lehrkraftFound.getVorname());

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

            Lehrkraft lehrkraft1 = new Lehrkraft(Anrede.FRAU, "Noémie", "Roos", new GregorianCalendar(1994, Calendar.MARCH, 18), "044 391 45 35", "076 384 45 35", "nroos@gmx.ch", "756.3943.8722.22", "Mi, Fr, Sa", true);
            Adresse adresse1 = new Adresse("Rebwiesenstrasse", "54", "8702", "Zollikon");
            lehrkraft1.setAdresse(adresse1);
            Lehrkraft lehrkraft1Saved = lehrkraftDao.save(lehrkraft1);
            int lehrkraft1Id = lehrkraft1Saved.getPersonId();

            Lehrkraft lehrkraft2 = new Lehrkraft(Anrede.FRAU, "Nathalie", "Delley", new GregorianCalendar(1971, Calendar.DECEMBER, 16), "044 261 27 20", "076 338 05 36", "ndelley@sunrise.ch", "756.8274.3263.17", "Mi, Fr, Sa", true);
            Adresse adresse2 = new Adresse("Im Schilf", "7", "8044", "Zürich");
            lehrkraft2.setAdresse(adresse2);
            Lehrkraft lehrkraft2Saved = lehrkraftDao.save(lehrkraft2);
            int lehrkraft2Id = lehrkraft2Saved.getPersonId();

            entityManager.flush();

            assertNotNull(lehrkraftDao.findById(lehrkraft1Id));
            assertNotNull(lehrkraftDao.findById(lehrkraft2Id));

            // 1. Lehrkraft löschen
            lehrkraftDao.remove(lehrkraft1Saved);
            entityManager.flush();

            assertNull(lehrkraftDao.findById(lehrkraft1Id));
            assertNotNull(lehrkraftDao.findById(lehrkraft2Id));

            // 2. Lehrkraft löschen
            lehrkraftDao.remove(lehrkraft2Saved);
            entityManager.flush();

            assertNull(lehrkraftDao.findById(lehrkraft2Id));
            assertNull(lehrkraftDao.findById(lehrkraft2Id));

        } finally {
            if (tx != null) {
                tx.rollback();
            }
        }

    }

    @Test
    public void testFindAll() {
        EntityTransaction tx = null;
        try {
            tx = entityManager.getTransaction();
            tx.begin();

            Lehrkraft lehrkraft1 = new Lehrkraft(Anrede.FRAU, "NoémieTest", "RoosTest", new GregorianCalendar(1994, Calendar.MARCH, 18), "044 391 45 35", "076 384 45 35", "nroos@gmx.ch", "756.3943.8722.22", "Mi, Fr, Sa", true);
            Adresse adresse1 = new Adresse("Rebwiesenstrasse", "54", "8702", "Zollikon");
            lehrkraft1.setAdresse(adresse1);
            lehrkraftDao.save(lehrkraft1);

            Lehrkraft lehrkraft2 = new Lehrkraft(Anrede.FRAU, "NathalieTest", "DelleyTest", new GregorianCalendar(1971, Calendar.DECEMBER, 16), "044 261 27 20", "076 338 05 36", "ndelley@sunrise.ch", "756.8274.3263.17", "Mi, Fr, Sa", true);
            Adresse adresse2 = new Adresse("Im Schilf", "7", "8044", "Zürich");
            lehrkraft2.setAdresse(adresse2);
            lehrkraftDao.save(lehrkraft2);

            entityManager.flush();

            // Codes suchen
            List<Lehrkraft> lehrkraefteFound = lehrkraftDao.findAll();
            assertTrue(lehrkraefteFound.size() >= 2);
            boolean found1 = false;
            boolean found2 = false;
            for (Lehrkraft lehrkraft : lehrkraefteFound) {
                if (lehrkraft.getNachname().equals("RoosTest")) {
                    found1 = true;
                }
                if (lehrkraft.getNachname().equals("DelleyTest")) {
                    found2 = true;
                }
            }
            assertTrue(found1);
            assertTrue(found2);

        } finally {
            if (tx != null) {
                tx.rollback();
            }
        }
    }


}