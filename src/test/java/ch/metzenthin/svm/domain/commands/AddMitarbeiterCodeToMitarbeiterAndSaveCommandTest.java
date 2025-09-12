package ch.metzenthin.svm.domain.commands;

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;
import static org.junit.Assert.assertEquals;

import ch.metzenthin.svm.common.datatypes.Anrede;
import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.daos.MitarbeiterCodeDao;
import ch.metzenthin.svm.persistence.daos.MitarbeiterDao;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Martin Schraner
 */
public class AddMitarbeiterCodeToMitarbeiterAndSaveCommandTest {

  private final MitarbeiterDao mitarbeiterDao = new MitarbeiterDao();
  private final MitarbeiterCodeDao mitarbeiterCodeDao = new MitarbeiterCodeDao();

  private DB db;
  private CommandInvoker commandInvoker;

  @Before
  public void setUp() {
    createSvmPropertiesFileDefault();
    db = DBFactory.getInstance();
    commandInvoker = new CommandInvokerImpl();
  }

  @After
  public void tearDown() {
    db.closeSession();
  }

  @Test
  public void testExecute() {

    List<MitarbeiterCode> erfassteMitarbeiterCodes = new ArrayList<>();
    List<Mitarbeiter> mitarbeitersSaved = new ArrayList<>();

    // Codes erzeugen
    MitarbeiterCode mitarbeiterCode1 = new MitarbeiterCode("vt", "VertretungTest", true);
    MitarbeiterCode mitarbeiterCode2 = new MitarbeiterCode("ht", "HelferTest", true);
    SaveOrUpdateMitarbeiterCodeCommand saveOrUpdateMitarbeiterCodeCommand =
        new SaveOrUpdateMitarbeiterCodeCommand(mitarbeiterCode1, null, erfassteMitarbeiterCodes);
    commandInvoker.executeCommandAsTransaction(saveOrUpdateMitarbeiterCodeCommand);
    saveOrUpdateMitarbeiterCodeCommand =
        new SaveOrUpdateMitarbeiterCodeCommand(mitarbeiterCode2, null, erfassteMitarbeiterCodes);
    commandInvoker.executeCommandAsTransaction(saveOrUpdateMitarbeiterCodeCommand);

    // Mitarbeiter erfassen und Codes hinzufügen
    Mitarbeiter mitarbeiter =
        new Mitarbeiter(
            Anrede.FRAU,
            "Noémie",
            "Roos",
            new GregorianCalendar(1994, Calendar.MARCH, 18),
            "044 391 45 35",
            "076 384 45 35",
            "nroos@gmx.ch",
            "756.3943.8722.22",
            "CH31 8123 9000 0012 4568 9",
            true,
            "Mi, Fr, Sa",
            null,
            true);
    Adresse adresse = new Adresse("Rebwiesenstrasse", "54", "8702", "Zollikon");
    mitarbeiter.setAdresse(adresse);

    SaveOrUpdateMitarbeiterCommand saveOrUpdateMitarbeiterCommand =
        new SaveOrUpdateMitarbeiterCommand(mitarbeiter, adresse, null, null, mitarbeitersSaved);
    commandInvoker.executeCommandAsTransaction(saveOrUpdateMitarbeiterCommand);

    // Codes hinzufügen
    AddMitarbeiterCodeToMitarbeiterAndSaveCommand addMitarbeiterCodeToMitarbeiterAndSaveCommand =
        new AddMitarbeiterCodeToMitarbeiterAndSaveCommand(mitarbeiterCode1, mitarbeiter);
    commandInvoker.executeCommandAsTransaction(addMitarbeiterCodeToMitarbeiterAndSaveCommand);
    Mitarbeiter mitarbeiterSaved =
        addMitarbeiterCodeToMitarbeiterAndSaveCommand.getMitarbeiterUpdated();

    assertEquals(1, mitarbeiterSaved.getMitarbeiterCodes().size());
    assertEquals("vt", mitarbeiterSaved.getSortedMitarbeiterCodes().get(0).getKuerzel());

    // Weiteren MitarbeiterCode hinzufügen:
    addMitarbeiterCodeToMitarbeiterAndSaveCommand =
        new AddMitarbeiterCodeToMitarbeiterAndSaveCommand(mitarbeiterCode2, mitarbeiterSaved);
    commandInvoker.executeCommandAsTransaction(addMitarbeiterCodeToMitarbeiterAndSaveCommand);
    mitarbeiterSaved = addMitarbeiterCodeToMitarbeiterAndSaveCommand.getMitarbeiterUpdated();

    assertEquals(2, mitarbeiterSaved.getMitarbeiterCodes().size());
    // Alphabetisch geordnet?
    assertEquals("ht", mitarbeiterSaved.getSortedMitarbeiterCodes().get(0).getKuerzel());
    assertEquals("vt", mitarbeiterSaved.getSortedMitarbeiterCodes().get(1).getKuerzel());

    // Testdaten löschen
    EntityManager entityManager = db.getCurrentEntityManager();
    entityManager.getTransaction().begin();
    Mitarbeiter mitarbeiterToBeDeleted = mitarbeiterDao.findById(mitarbeiterSaved.getPersonId());
    for (MitarbeiterCode mitarbeiterCode : erfassteMitarbeiterCodes) {
      MitarbeiterCode mitarbeiterCodeToBeDeleted =
          mitarbeiterCodeDao.findById(mitarbeiterCode.getCodeId());
      mitarbeiterCodeDao.removeFromMitarbeiterAndUpdate(
          mitarbeiterCodeToBeDeleted, mitarbeiterToBeDeleted);
      mitarbeiterCodeDao.remove(mitarbeiterCodeToBeDeleted);
    }
    mitarbeiterDao.remove(mitarbeiterToBeDeleted);
    entityManager.getTransaction().commit();
  }
}
