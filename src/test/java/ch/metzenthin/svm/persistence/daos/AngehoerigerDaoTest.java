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
            Angehoeriger angehoeriger = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", null, null);
            Adresse adresse = new Adresse("Hohenklingenstrasse", "15", "8049", "Zürich", "044 491 69 33");
            angehoeriger.setNewAdresse(adresse);
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
            Angehoeriger vater = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", null, null);
            Adresse adresse = new Adresse("Hohenklingenstrasse", "15", "8049", "Zürich", "044 491 69 33");
            vater.setNewAdresse(adresse);
            Angehoeriger vaterSaved = angehoerigerDao.save(vater);
            Angehoeriger vaterFound = angehoerigerDao.findById(vaterSaved.getPersonId());
            assertEquals("Adresse not correct", "Hohenklingenstrasse", vaterFound.getAdresse().getStrasse());

            // Mutter
            Angehoeriger mutter = new Angehoeriger(Anrede.FRAU, "Eugen", "Regula", null, null);
            //mutter.setNewAdresse(adresse);
            mutter.setNewAdresse(vaterFound.getAdresse());
            Angehoeriger mutterSaved = angehoerigerDao.save(mutter);
            Angehoeriger mutterFound = angehoerigerDao.findById(mutterSaved.getPersonId());
            assertEquals("Adresse not correct", "Hohenklingenstrasse", mutterFound.getAdresse().getStrasse());

            // Are adresseIds equal?
            assertEquals("adresse_ids not equal", vaterFound.getAdresse().getAdresseId(), mutterFound.getAdresse().getAdresseId());

            // Angehöriger ohne Adresse
            Angehoeriger vater2 = new Angehoeriger(Anrede.HERR, "Urs", "Müller", null, null);
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

            // Create 2 Angehoerige with the same adress
            tx = entityManager.getTransaction();
            tx.begin();

            // Vater
            Angehoeriger vater = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", null, null);
            Adresse adresse = new Adresse("Hohenklingenstrasse", "15", "8049", "Zürich", "044 491 69 33");
            vater.setNewAdresse(adresse);
            Angehoeriger vaterSaved = angehoerigerDao.save(vater);
            int vaterId = vaterSaved.getPersonId();
            int adresseId = vaterSaved.getAdresse().getAdresseId();

            // Mutter
            Angehoeriger mutter = new Angehoeriger(Anrede.FRAU, "Eugen", "Regula", null, null);
            mutter.setNewAdresse(adresse);
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
    public void testFindAngehoerige() {
        EntityTransaction tx = null;

        try {
            tx = entityManager.getTransaction();
            tx.begin();

            // Angehörigen einfügen
            Angehoeriger angehoeriger = new Angehoeriger(Anrede.HERR, "Urs", "Berger", null, null);
            Adresse adresse = new Adresse("Gugusweg", "16", "8049", "Zürich", "044 491 69 33");
            angehoeriger.setNewAdresse(adresse);
            Angehoeriger angehoerigerSaved = angehoerigerDao.save(angehoeriger);

            entityManager.flush();

            // Zweiten Angehörigen mit denselben Attributen einfügen
            Angehoeriger angehoeriger2 = new Angehoeriger(Anrede.HERR, "Urs", "Berger", null, null);
            Adresse adresse2 = new Adresse("Gugusweg", "16", "8049", "Zürich", "044 491 69 33");
            angehoeriger2.setNewAdresse(adresse2);

            List<Angehoeriger> angehoerigeFound2 = angehoerigerDao.findAngehoerige(angehoeriger2);
            assertNotNull("Angehörigen nicht gefunden", angehoerigeFound2);
            assertEquals("Mehr als 1 Angehörigen gefunden", 1, angehoerigeFound2.size());

            // Ditto, aber ohne Adresse;
            Angehoeriger angehoeriger3 = new Angehoeriger(Anrede.HERR, "Urs", "Berger", null, null);

            List<Angehoeriger> angehoerigeFound3 = angehoerigerDao.findAngehoerige(angehoeriger3);
            assertNotNull("Angehörigen nicht gefunden", angehoerigeFound3);
            assertEquals("Mehr als 1 Angehörigen gefunden", 1, angehoerigeFound3.size());
            // Adresse ist diejenige von Angehoeriger 1
            assertNotNull("Hat keine Adresse", angehoerigeFound3.get(0).getAdresse());
            assertEquals("Strasse nicht korrekt", "Gugusweg", angehoerigeFound3.get(0).getAdresse().getStrasse());

            // Ditto, aber andere Strasse:
            Angehoeriger angehoeriger4 = new Angehoeriger(Anrede.HERR, "Urs", "Berger", null, null);
            Adresse adresse4 = new Adresse("Gugusstrasse", "16", "8049", "Zürich", "044 491 69 33");
            angehoeriger4.setNewAdresse(adresse4);

            List<Angehoeriger> angehoerigeFound4 = angehoerigerDao.findAngehoerige(angehoeriger4);
            assertNull("Angehörigen gefunden", angehoerigeFound4);

            // Sämtliche Angehörige suchen
            List<Angehoeriger> angehoerigeFound5 = angehoerigerDao.findAngehoerige(null);
            assertNotNull("Keine Angehörigen gefunden", angehoerigeFound5);

            // Angehörigen löschen
            angehoerigerDao.remove(angehoerigerSaved);

            tx.commit();

        } catch (NullPointerException e){
            if (tx != null)
                tx.rollback();
        }

    }
}

