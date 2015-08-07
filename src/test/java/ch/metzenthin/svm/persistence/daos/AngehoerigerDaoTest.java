package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.common.dataTypes.Anrede;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import java.util.List;

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
            Angehoeriger angehoeriger = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", "044 491 69 33", null, null);
            Adresse adresse = new Adresse("Hohenklingenstrasse", "15", "8049", "Zürich");
            angehoeriger.setAdresse(adresse);
            entityManager.persist(angehoeriger);
            Angehoeriger angehoerigerFound = angehoerigerDao.findById(angehoeriger.getPersonId());
            assertEquals("Adresse falsch", "Hohenklingenstrasse", angehoerigerFound.getAdresse().getStrasse());
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
            Angehoeriger vater = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", "044 491 69 33", null, null);
            Adresse adresse = new Adresse("Hohenklingenstrasse", "15", "8049", "Zürich");
            vater.setAdresse(adresse);
            Angehoeriger vaterSaved = angehoerigerDao.save(vater);
            Angehoeriger vaterFound = angehoerigerDao.findById(vaterSaved.getPersonId());
            assertEquals("Adresse falsch", "Hohenklingenstrasse", vaterFound.getAdresse().getStrasse());

            // Mutter
            Angehoeriger mutter = new Angehoeriger(Anrede.FRAU, "Eugen", "Regula", "044 491 69 33", null, null);
            mutter.setAdresse(vaterFound.getAdresse());
            Angehoeriger mutterSaved = angehoerigerDao.save(mutter);
            Angehoeriger mutterFound = angehoerigerDao.findById(mutterSaved.getPersonId());
            assertEquals("Adresse falsch", "Hohenklingenstrasse", mutterFound.getAdresse().getStrasse());

            // Are adresseIds equal?
            assertEquals("adresse_ids nicht identisch", vaterFound.getAdresse().getAdresseId(), mutterFound.getAdresse().getAdresseId());

            // Angehöriger ohne Adresse
            Angehoeriger vater2 = new Angehoeriger(Anrede.HERR, "Urs", "Müller", null, null, null);
            Angehoeriger vaterSaved2 = angehoerigerDao.save(vater2);
            assertNull("Adresse nicht null", vaterSaved2.getAdresse());

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

            // 2 Angehörige mit derselben Adresse erzeugen
            tx = entityManager.getTransaction();
            tx.begin();

            // Vater
            Angehoeriger vater = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", "044 491 69 33", null, null);
            Adresse adresse = new Adresse("Hohenklingenstrasse", "15", "8049", "Zürich");
            vater.setAdresse(adresse);
            Angehoeriger vaterSaved = angehoerigerDao.save(vater);
            int vaterId = vaterSaved.getPersonId();
            int adresseId = vaterSaved.getAdresse().getAdresseId();

            // Mutter
            Angehoeriger mutter = new Angehoeriger(Anrede.FRAU, "Eugen", "Regula", "044 491 69 33", null, null);
            mutter.setAdresse(adresse);
            Angehoeriger mutterSaved = angehoerigerDao.save(mutter);
            int mutterId = mutterSaved.getPersonId();

            entityManager.flush();

            assertNotNull(angehoerigerDao.findById(vaterId));
            assertNotNull(angehoerigerDao.findById(mutterId));
            assertNotNull(adresseDao.findById(adresseId));

            // Vater löschen
            angehoerigerDao.remove(vaterSaved);
            entityManager.flush();
            assertNull(angehoerigerDao.findById(vaterId));
            assertNotNull(adresseDao.findById(adresseId));

            // Mutter löschen
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
    public void testFindAngehoerige() {
        EntityTransaction tx = null;

        try {
            tx = entityManager.getTransaction();
            tx.begin();

            // Angehörigen einfügen
            Angehoeriger angehoeriger = new Angehoeriger(Anrede.HERR, "Urs", "Berger", "044 491 69 33", null, null);
            Adresse adresse = new Adresse("Gugusweg", "16", "8049", "Zürich");
            angehoeriger.setAdresse(adresse);
            Angehoeriger angehoerigerSaved = angehoerigerDao.save(angehoeriger);

            entityManager.flush();

            // Zweiten Angehörigen mit denselben Attributen einfügen
            Angehoeriger angehoeriger2 = new Angehoeriger(Anrede.HERR, "Urs", "Berger", "044 491 69 33", null, null);
            Adresse adresse2 = new Adresse("Gugusweg", "16", "8049", "Zürich");
            angehoeriger2.setAdresse(adresse2);

            List<Angehoeriger> angehoerigeFound2 = angehoerigerDao.findAngehoerige(angehoeriger2);
            assertEquals("Mehr als 1 Angehörigen gefunden", 1, angehoerigeFound2.size());

            // Ditto, aber ohne Adresse;
            Angehoeriger angehoeriger3 = new Angehoeriger(Anrede.HERR, "Urs", "Berger", null, null, null);

            List<Angehoeriger> angehoerigeFound3 = angehoerigerDao.findAngehoerige(angehoeriger3);
            assertEquals("Mehr als 1 Angehörigen gefunden", 1, angehoerigeFound3.size());
            // Adresse ist diejenige von Angehoeriger 1
            assertNotNull("Hat keine Adresse", angehoerigeFound3.get(0).getAdresse());
            assertEquals("Strasse nicht korrekt", "Gugusweg", angehoerigeFound3.get(0).getAdresse().getStrasse());

            // Ditto, aber andere Strasse:
            Angehoeriger angehoeriger4 = new Angehoeriger(Anrede.HERR, "Urs", "Berger", "044 491 69 33", null, null);
            Adresse adresse4 = new Adresse("Gugusstrasse", "16", "8049", "Zürich");
            angehoeriger4.setAdresse(adresse4);

            List<Angehoeriger> angehoerigeFound4 = angehoerigerDao.findAngehoerige(angehoeriger4);
            assertTrue("Angehörigen gefunden", angehoerigeFound4.isEmpty());

            // Sämtliche Angehörige suchen
            List<Angehoeriger> angehoerigeFound5 = angehoerigerDao.findAngehoerige(null);
            assertFalse("Keine Angehörigen gefunden", angehoerigeFound5.isEmpty());

            // Angehörigen löschen
            angehoerigerDao.remove(angehoerigerSaved);

            tx.commit();

        } catch (NullPointerException e){
            if (tx != null)
                tx.rollback();
        }

    }
}

