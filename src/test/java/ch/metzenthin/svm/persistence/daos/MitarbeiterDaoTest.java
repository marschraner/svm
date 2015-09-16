package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.common.dataTypes.Anrede;
import ch.metzenthin.svm.common.utils.PersistenceProperties;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
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
public class MitarbeiterDaoTest {

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private MitarbeiterDao mitarbeiterDao;

    @Before
    public void setUp() throws Exception {
        createSvmPropertiesFileDefault();
        entityManagerFactory = Persistence.createEntityManagerFactory("svm", PersistenceProperties.getPersistenceProperties());
        entityManager = entityManagerFactory.createEntityManager();
        mitarbeiterDao = new MitarbeiterDao(entityManager);
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

            Mitarbeiter mitarbeiter = new Mitarbeiter(Anrede.FRAU, "Noémie", "Roos", new GregorianCalendar(1994, Calendar.MARCH, 18), "044 391 45 35", "076 384 45 35", "nroos@gmx.ch", "756.3943.8722.22", "Mi, Fr, Sa", true);
            Adresse adresse = new Adresse("Rebwiesenstrasse", "54", "8702", "Zollikon");
            mitarbeiter.setAdresse(adresse);

            entityManager.persist(mitarbeiter);

            Mitarbeiter mitarbeiterFound = mitarbeiterDao.findById(mitarbeiter.getPersonId());
            assertEquals("Roos", mitarbeiterFound.getNachname());

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

            Mitarbeiter mitarbeiter = new Mitarbeiter(Anrede.FRAU, "Noémie", "Roos", new GregorianCalendar(1994, Calendar.MARCH, 18), "044 391 45 35", "076 384 45 35", "nroos@gmx.ch", "756.3943.8722.22", "Mi, Fr, Sa", true);
            Adresse adresse = new Adresse("Rebwiesenstrasse", "54", "8702", "Zollikon");
            mitarbeiter.setAdresse(adresse);

            Mitarbeiter mitarbeiterSaved = mitarbeiterDao.save(mitarbeiter);
            entityManager.flush();

            Mitarbeiter mitarbeiterFound = mitarbeiterDao.findById(mitarbeiterSaved.getPersonId());

            // Erzwingen, dass von DB gelesen wird
            entityManager.refresh(mitarbeiterFound);

            assertEquals("Noémie", mitarbeiterFound.getVorname());

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

            Mitarbeiter mitarbeiter1 = new Mitarbeiter(Anrede.FRAU, "Noémie", "Roos", new GregorianCalendar(1994, Calendar.MARCH, 18), "044 391 45 35", "076 384 45 35", "nroos@gmx.ch", "756.3943.8722.22", "Mi, Fr, Sa", true);
            Adresse adresse1 = new Adresse("Rebwiesenstrasse", "54", "8702", "Zollikon");
            mitarbeiter1.setAdresse(adresse1);
            Mitarbeiter mitarbeiter1Saved = mitarbeiterDao.save(mitarbeiter1);
            int lehrkraft1Id = mitarbeiter1Saved.getPersonId();

            Mitarbeiter mitarbeiter2 = new Mitarbeiter(Anrede.FRAU, "Nathalie", "Delley", new GregorianCalendar(1971, Calendar.DECEMBER, 16), "044 261 27 20", "076 338 05 36", "ndelley@sunrise.ch", "756.8274.3263.17", "Mi, Fr, Sa", true);
            Adresse adresse2 = new Adresse("Im Schilf", "7", "8044", "Zürich");
            mitarbeiter2.setAdresse(adresse2);
            Mitarbeiter mitarbeiter2Saved = mitarbeiterDao.save(mitarbeiter2);
            int lehrkraft2Id = mitarbeiter2Saved.getPersonId();

            entityManager.flush();

            assertNotNull(mitarbeiterDao.findById(lehrkraft1Id));
            assertNotNull(mitarbeiterDao.findById(lehrkraft2Id));

            // 1. Lehrkraft löschen
            mitarbeiterDao.remove(mitarbeiter1Saved);
            entityManager.flush();

            assertNull(mitarbeiterDao.findById(lehrkraft1Id));
            assertNotNull(mitarbeiterDao.findById(lehrkraft2Id));

            // 2. Lehrkraft löschen
            mitarbeiterDao.remove(mitarbeiter2Saved);
            entityManager.flush();

            assertNull(mitarbeiterDao.findById(lehrkraft2Id));
            assertNull(mitarbeiterDao.findById(lehrkraft2Id));

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

            Mitarbeiter mitarbeiter1 = new Mitarbeiter(Anrede.FRAU, "NoémieTest", "RoosTest", new GregorianCalendar(1994, Calendar.MARCH, 18), "044 391 45 35", "076 384 45 35", "nroos@gmx.ch", "756.3943.8722.22", "Mi, Fr, Sa", true);
            Adresse adresse1 = new Adresse("Rebwiesenstrasse", "54", "8702", "Zollikon");
            mitarbeiter1.setAdresse(adresse1);
            mitarbeiterDao.save(mitarbeiter1);

            Mitarbeiter mitarbeiter2 = new Mitarbeiter(Anrede.FRAU, "NathalieTest", "DelleyTest", new GregorianCalendar(1971, Calendar.DECEMBER, 16), "044 261 27 20", "076 338 05 36", "ndelley@sunrise.ch", "756.8274.3263.17", "Mi, Fr, Sa", true);
            Adresse adresse2 = new Adresse("Im Schilf", "7", "8044", "Zürich");
            mitarbeiter2.setAdresse(adresse2);
            mitarbeiterDao.save(mitarbeiter2);

            entityManager.flush();

            // Codes suchen
            List<Mitarbeiter> lehrkraefteFound = mitarbeiterDao.findAll();
            assertTrue(lehrkraefteFound.size() >= 2);
            boolean found1 = false;
            boolean found2 = false;
            for (Mitarbeiter mitarbeiter : lehrkraefteFound) {
                if (mitarbeiter.getNachname().equals("RoosTest")) {
                    found1 = true;
                }
                if (mitarbeiter.getNachname().equals("DelleyTest")) {
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