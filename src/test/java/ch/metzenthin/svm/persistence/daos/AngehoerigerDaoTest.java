package ch.metzenthin.svm.persistence.daos;

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;
import static org.junit.jupiter.api.Assertions.*;

import ch.metzenthin.svm.common.datatypes.Anrede;
import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Hans Stamm
 */
class AngehoerigerDaoTest {

  private final AngehoerigerDao angehoerigerDao = new AngehoerigerDao();
  private final AdresseDao adresseDao = new AdresseDao();

  private DB db;

  @BeforeEach
  void setUp() {
    createSvmPropertiesFileDefault();
    db = DBFactory.getInstance();
  }

  @AfterEach
  void tearDown() {
    db.closeSession();
  }

  @Test
  void testFindById() {
    EntityManager entityManager = db.getCurrentEntityManager();
    EntityTransaction tx = null;
    try {
      tx = entityManager.getTransaction();
      tx.begin();
      Angehoeriger angehoeriger =
          new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", "044 491 69 33", null, null, false);
      Adresse adresse = new Adresse("Hohenklingenstrasse", "15", "8049", "Zürich");
      angehoeriger.setAdresse(adresse);
      entityManager.persist(angehoeriger);
      Angehoeriger angehoerigerFound = angehoerigerDao.findById(angehoeriger.getPersonId());
      assertEquals(
          "Hohenklingenstrasse", angehoerigerFound.getAdresse().getStrasse(), "Adresse falsch");
    } finally {
      if (tx != null) tx.rollback();
    }
  }

  @Test
  void testSave() {
    EntityManager entityManager = db.getCurrentEntityManager();
    EntityTransaction tx = null;
    try {
      tx = entityManager.getTransaction();
      tx.begin();

      // Vater
      Angehoeriger vater =
          new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", "044 491 69 33", null, null, false);
      Adresse adresse = new Adresse("Hohenklingenstrasse", "15", "8049", "Zürich");
      vater.setAdresse(adresse);
      Angehoeriger vaterSaved = angehoerigerDao.save(vater);
      Angehoeriger vaterFound = angehoerigerDao.findById(vaterSaved.getPersonId());
      assertEquals("Hohenklingenstrasse", vaterFound.getAdresse().getStrasse(), "Adresse falsch");

      // Mutter
      Angehoeriger mutter =
          new Angehoeriger(Anrede.FRAU, "Eugen", "Regula", "044 491 69 33", null, null, true);
      mutter.setAdresse(vaterFound.getAdresse());
      Angehoeriger mutterSaved = angehoerigerDao.save(mutter);
      Angehoeriger mutterFound = angehoerigerDao.findById(mutterSaved.getPersonId());
      assertEquals("Hohenklingenstrasse", mutterFound.getAdresse().getStrasse(), "Adresse falsch");

      // Are adresseIds equal?
      assertEquals(
          vaterFound.getAdresse().getAdresseId(),
          mutterFound.getAdresse().getAdresseId(),
          "adresse_ids nicht identisch");

      // Angehöriger ohne Adresse
      Angehoeriger vater2 = new Angehoeriger(Anrede.HERR, "Urs", "Müller", null, null, null, false);
      Angehoeriger vaterSaved2 = angehoerigerDao.save(vater2);
      assertNull(vaterSaved2.getAdresse(), "Adresse nicht null");

    } finally {
      if (tx != null) tx.rollback();
    }
  }

  @Test
  void testRemove() {
    EntityManager entityManager = db.getCurrentEntityManager();
    EntityTransaction tx = null;
    try {

      // 2 Angehörige mit derselben Adresse erzeugen
      tx = entityManager.getTransaction();
      tx.begin();

      // Vater
      Angehoeriger vater =
          new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", "044 491 69 33", null, null, false);
      Adresse adresse = new Adresse("Hohenklingenstrasse", "15", "8049", "Zürich");
      vater.setAdresse(adresse);
      Angehoeriger vaterSaved = angehoerigerDao.save(vater);
      int vaterId = vaterSaved.getPersonId();
      int adresseId = vaterSaved.getAdresse().getAdresseId();

      // Mutter
      Angehoeriger mutter =
          new Angehoeriger(Anrede.FRAU, "Eugen", "Regula", "044 491 69 33", null, null, true);
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

    } catch (NullPointerException e) {
      if (tx != null) tx.rollback();
    }
  }

  @Test
  void testFindAngehoerige() {
    EntityManager entityManager = db.getCurrentEntityManager();
    EntityTransaction tx = null;
    try {
      tx = entityManager.getTransaction();
      tx.begin();

      // Angehörigen einfügen
      Angehoeriger angehoeriger =
          new Angehoeriger(Anrede.HERR, "Urs", "Berger", "044 491 69 33", null, null, false);
      Adresse adresse = new Adresse("Gugusweg", "16", "8049", "Zürich");
      angehoeriger.setAdresse(adresse);
      Angehoeriger angehoerigerSaved = angehoerigerDao.save(angehoeriger);

      entityManager.flush();

      // Zweiten Angehörigen mit denselben Attributen einfügen
      Angehoeriger angehoeriger2 =
          new Angehoeriger(Anrede.HERR, "Urs", "Berger", "044 491 69 33", null, null, false);
      Adresse adresse2 = new Adresse("Gugusweg", "16", "8049", "Zürich");
      angehoeriger2.setAdresse(adresse2);

      List<Angehoeriger> angehoerigeFound2 = angehoerigerDao.findAngehoerige(angehoeriger2);
      assertEquals(1, angehoerigeFound2.size(), "Mehr als 1 Angehörigen gefunden");

      // Ditto, aber ohne Adresse
      Angehoeriger angehoeriger3 =
          new Angehoeriger(Anrede.HERR, "Urs", "Berger", null, null, null, false);

      List<Angehoeriger> angehoerigeFound3 = angehoerigerDao.findAngehoerige(angehoeriger3);
      assertEquals(1, angehoerigeFound3.size(), "Mehr als 1 Angehörigen gefunden");
      // Adresse ist diejenige von Angehoeriger 1
      assertNotNull(angehoerigeFound3.get(0).getAdresse(), "Hat keine Adresse");
      assertEquals(
          "Gugusweg", angehoerigeFound3.get(0).getAdresse().getStrasse(), "Strasse nicht korrekt");

      // Ditto, aber andere Strasse:
      Angehoeriger angehoeriger4 =
          new Angehoeriger(Anrede.HERR, "Urs", "Berger", "044 491 69 33", null, null, false);
      Adresse adresse4 = new Adresse("Gugusstrasse", "16", "8049", "Zürich");
      angehoeriger4.setAdresse(adresse4);

      List<Angehoeriger> angehoerigeFound4 = angehoerigerDao.findAngehoerige(angehoeriger4);
      assertTrue(angehoerigeFound4.isEmpty(), "Angehörigen gefunden");

      // Sämtliche Angehörige suchen
      List<Angehoeriger> angehoerigeFound5 = angehoerigerDao.findAngehoerige(null);
      assertFalse(angehoerigeFound5.isEmpty(), "Keine Angehörigen gefunden");

      // Angehörigen löschen
      angehoerigerDao.remove(angehoerigerSaved);

      tx.commit();

    } catch (NullPointerException e) {
      if (tx != null) tx.rollback();
    }
  }
}
