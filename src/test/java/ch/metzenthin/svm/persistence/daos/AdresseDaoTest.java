package ch.metzenthin.svm.persistence.daos;

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;
import static org.junit.jupiter.api.Assertions.*;

import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.entities.Adresse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Hans Stamm
 */
class AdresseDaoTest {

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
      Adresse adresse = new Adresse("Buechackerstrasse", "4", "8234", "Stetten");
      entityManager.persist(adresse);
      Adresse adresseFound = adresseDao.findById(adresse.getAdresseId());
      assertEquals("Buechackerstrasse", adresseFound.getStrasse(), "Strasse falsch");

    } finally {
      if (tx != null) {
        tx.rollback();
      }
    }
  }

  @Test
  void testSave() {
    EntityManager entityManager = db.getCurrentEntityManager();
    EntityTransaction tx = null;
    try {
      tx = entityManager.getTransaction();
      tx.begin();
      Adresse adresse = new Adresse("Buechackerstrasse", "4", "8234", "Stetten");
      Adresse adresseSaved = adresseDao.save(adresse);
      Adresse adresseFound = adresseDao.findById(adresseSaved.getAdresseId());
      assertEquals("Buechackerstrasse", adresseFound.getStrasse(), "Adresse not correct");

    } finally {
      if (tx != null) {
        tx.rollback();
      }
    }
  }

  @Test
  void testRemove() {
    EntityManager entityManager = db.getCurrentEntityManager();
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
