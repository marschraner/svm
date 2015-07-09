package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.dataTypes.Anrede;
import ch.metzenthin.svm.dataTypes.Geschlecht;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Anmeldung;
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

import static org.junit.Assert.assertEquals;

/**
 * @author Hans Stamm
 */
public class AnmeldungDaoTest {

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private AnmeldungDao anmeldungDao;

    @Before
    public void setUp() throws Exception {
        entityManagerFactory = Persistence.createEntityManagerFactory("svm");
        entityManager = entityManagerFactory.createEntityManager();
        anmeldungDao = new AnmeldungDao(entityManager);
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
            Schueler schueler = new Schueler("Jana", "Rösle", new GregorianCalendar(2012, Calendar.JULY, 24), "044 491 69 33", null, null, Geschlecht.W, "Schwester von Valentin");
            Adresse adresse = new Adresse("Hohenklingenstrasse", "15", "8049", "Zürich");
            schueler.setAdresse(adresse);

            // Set Vater
            Angehoeriger vater = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", "044 491 69 33", null, null);
            vater.setAdresse(adresse);
            schueler.setVater(vater);

            // Set Rechnungsempfänger
            schueler.setRechnungsempfaenger(vater);

            Anmeldung anmeldung = new Anmeldung(new GregorianCalendar(2014, Calendar.JANUARY, 15), new GregorianCalendar(2015, Calendar.MARCH, 31));
            schueler.addAnmeldung(anmeldung);

            entityManager.persist(schueler);

            Anmeldung anmeldungFound = anmeldungDao.findById(anmeldung.getAnmeldungId());

            // Erzwingen, dass von DB gelesen wird
            entityManager.refresh(anmeldungFound);

            assertEquals("Anmeldedatum falsch", new GregorianCalendar(2014, Calendar.JANUARY, 15), anmeldungFound.getAnmeldedatum());
            assertEquals("Abmeldedatum falsch", new GregorianCalendar(2015, Calendar.MARCH, 31), anmeldungFound.getAbmeldedatum());

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

            // Schueler
            Schueler schueler = new Schueler("Jana", "Rösle", new GregorianCalendar(2012, Calendar.JULY, 24), "044 491 69 33", null, null, Geschlecht.W, "Schwester von Valentin");
            Adresse adresse = new Adresse("Hohenklingenstrasse", "15", "8049", "Zürich");
            schueler.setAdresse(adresse);

            // Set Vater
            Angehoeriger vater = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", "044 491 69 33", null, null);
            vater.setAdresse(adresse);
            schueler.setVater(vater);

            // Set Rechnungsempfänger
            schueler.setRechnungsempfaenger(vater);

            Anmeldung anmeldung = new Anmeldung(new GregorianCalendar(2014, Calendar.JANUARY, 15), new GregorianCalendar(2015, Calendar.MARCH, 31));
            schueler.addAnmeldung(anmeldung);

            anmeldungDao.save(anmeldung, schueler);

            Anmeldung anmeldungFound = anmeldungDao.findById(anmeldung.getAnmeldungId());

            // Erzwingen, dass von DB gelesen wird
            entityManager.refresh(anmeldungFound);

            assertEquals("Anmeldedatum falsch", new GregorianCalendar(2014, Calendar.JANUARY, 15), anmeldungFound.getAnmeldedatum());

        } finally {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

}