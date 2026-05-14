package ch.metzenthin.svm.domain.commands;

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.metzenthin.svm.common.datatypes.Anrede;
import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.daos.AngehoerigerDao;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Hans Stamm
 */
class SaveAngehoerigeCommandTest {

  private final AngehoerigerDao angehoerigerDao = new AngehoerigerDao();

  private DB db;
  private CommandInvoker commandInvoker;

  @BeforeEach
  void setUp() {
    createSvmPropertiesFileDefault();
    db = DBFactory.getInstance();
    commandInvoker = new CommandInvokerImpl();
  }

  @AfterEach
  void tearDown() {
    db.closeSession();
  }

  @SuppressWarnings("ExtractMethodRecommender")
  @Test
  void testExecute() {

    List<Angehoeriger> angehoerige = new ArrayList<>();

    Angehoeriger angehoeriger0 =
        new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", "044 491 69 33", null, null, false);
    Adresse adresse = new Adresse("Hohenklingenstrasse", "15", "8049", "Zürich");
    angehoeriger0.setAdresse(adresse);
    angehoerige.add(angehoeriger0);

    // Second Angehoeriger with the same address
    Angehoeriger angehoeriger1 =
        new Angehoeriger(Anrede.FRAU, "Regula", "Rösle", "044 491 69 33", null, null, true);
    angehoeriger1.setAdresse(adresse);
    angehoerige.add(angehoeriger1);

    SaveAngehoerigeCommand saveAngehoerigeCommand = new SaveAngehoerigeCommand(angehoerige);
    commandInvoker.executeCommandAsTransaction(saveAngehoerigeCommand);
    List<Angehoeriger> savedAngehoerige = saveAngehoerigeCommand.getSavedAngehoerige();

    Angehoeriger savedAngehoeriger0 = savedAngehoerige.get(0);
    assertEquals("Eugen", savedAngehoeriger0.getVorname(), "Vorname not found");
    assertEquals(
        "Hohenklingenstrasse", savedAngehoeriger0.getAdresse().getStrasse(), "Strasse not found");

    Angehoeriger savedAngehoeriger1 = savedAngehoerige.get(1);
    assertEquals("Regula", savedAngehoeriger1.getVorname(), "Vorname not found");
    assertEquals(
        "Hohenklingenstrasse", savedAngehoeriger1.getAdresse().getStrasse(), "Strasse not found");

    // Do both Angehoeriger have the same adresseId?
    assertEquals(
        savedAngehoeriger0.getAdresse().getAdresseId(),
        savedAngehoeriger1.getAdresse().getAdresseId(),
        "Adresse_id not equal");

    // Delete
    EntityManager entityManager = db.getCurrentEntityManager();
    entityManager.getTransaction().begin();

    Angehoeriger angehoerigerToBeRemoved0 =
        angehoerigerDao.findById(savedAngehoeriger0.getPersonId());
    angehoerigerDao.remove(angehoerigerToBeRemoved0);

    Angehoeriger angehoerigerToBeRemoved1 =
        angehoerigerDao.findById(savedAngehoeriger1.getPersonId());
    angehoerigerDao.remove(angehoerigerToBeRemoved1);

    entityManager.getTransaction().commit();
  }
}
