package ch.metzenthin.svm.domain.commands;

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;
import static org.junit.Assert.assertEquals;

import ch.metzenthin.svm.common.datatypes.Anrede;
import ch.metzenthin.svm.common.datatypes.Geschlecht;
import ch.metzenthin.svm.common.utils.SvmProperties;
import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.daos.SchuelerDao;
import ch.metzenthin.svm.persistence.entities.*;
import jakarta.persistence.EntityManager;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Martin Schraner
 */
public class AddDispensationToSchuelerAndSaveCommandTest {

  private final SchuelerDao schuelerDao = new SchuelerDao();

  private DB db;
  private CommandInvoker commandInvoker;
  private boolean neusteZuoberst;

  @Before
  public void setUp() {
    createSvmPropertiesFileDefault();
    db = DBFactory.getInstance();
    commandInvoker = new CommandInvokerImpl();
    Properties svmProperties = SvmProperties.getSvmProperties();
    neusteZuoberst = !svmProperties.getProperty(SvmProperties.KEY_NEUSTE_ZUOBERST).equals("false");
  }

  @After
  public void tearDown() {
    db.closeSession();
  }

  @Test
  public void testExecute() {

    // Schueler erfassen und Dispensation hinzufügen
    Schueler schueler =
        new Schueler(
            "Jana",
            "Rösle",
            new GregorianCalendar(2012, Calendar.JULY, 24),
            "044 491 69 33",
            null,
            null,
            Geschlecht.W,
            "Schwester von Valentin");
    Adresse adresse = new Adresse("Hohenklingenstrasse", "15", "8049", "Zürich");
    schueler.setAdresse(adresse);
    schueler.addAnmeldung(new Anmeldung(new GregorianCalendar(2015, Calendar.MAY, 1), null));

    // Set Vater
    Angehoeriger vater =
        new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", "044 491 69 33", null, null, false);
    vater.setAdresse(adresse);
    schueler.setVater(vater);

    // Set Rechnungsempfänger
    schueler.setRechnungsempfaenger(vater);

    SaveSchuelerCommand saveSchuelerCommand = new SaveSchuelerCommand(schueler);
    commandInvoker.executeCommandAsTransaction(saveSchuelerCommand);
    Schueler schuelerSaved = saveSchuelerCommand.getSavedSchueler();

    // Dispensation hinzufügen
    Dispensation dispensation1 =
        new Dispensation(
            new GregorianCalendar(2014, Calendar.JANUARY, 15),
            new GregorianCalendar(2015, Calendar.MARCH, 31),
            null,
            "Zu klein");
    AddDispensationToSchuelerAndSaveCommand addDispensationToSchuelerAndSaveCommand =
        new AddDispensationToSchuelerAndSaveCommand(dispensation1, null, schuelerSaved);
    commandInvoker.executeCommandAsTransaction(addDispensationToSchuelerAndSaveCommand);
    schuelerSaved = addDispensationToSchuelerAndSaveCommand.getSchuelerUpdated();

    assertEquals(1, schuelerSaved.getDispensationen().size());

    // Weitere Dispensation hinzufügen:
    Dispensation dispensation2 =
        new Dispensation(
            new GregorianCalendar(2015, Calendar.MAY, 15), null, null, "Immer noch zu klein");
    addDispensationToSchuelerAndSaveCommand =
        new AddDispensationToSchuelerAndSaveCommand(dispensation2, null, schuelerSaved);
    commandInvoker.executeCommandAsTransaction(addDispensationToSchuelerAndSaveCommand);
    schuelerSaved = addDispensationToSchuelerAndSaveCommand.getSchuelerUpdated();

    assertEquals(2, schuelerSaved.getDispensationen().size());

    // Dispensation bearbeiten (Dispensationsende setzen)
    Dispensation dispensation2Modif =
        new Dispensation(
            new GregorianCalendar(2015, Calendar.MAY, 15),
            new GregorianCalendar(2015, Calendar.DECEMBER, 31),
            null,
            "Immer noch zu klein");
    addDispensationToSchuelerAndSaveCommand =
        new AddDispensationToSchuelerAndSaveCommand(
            dispensation2Modif, dispensation2, schuelerSaved);
    commandInvoker.executeCommandAsTransaction(addDispensationToSchuelerAndSaveCommand);
    schuelerSaved = addDispensationToSchuelerAndSaveCommand.getSchuelerUpdated();

    assertEquals(2, schuelerSaved.getDispensationen().size());
    Dispensation dispensation =
        (neusteZuoberst
            ? schuelerSaved.getDispensationen().get(0)
            : schuelerSaved.getDispensationen().get(schuelerSaved.getDispensationen().size() - 1));
    assertEquals(
        new GregorianCalendar(2015, Calendar.DECEMBER, 31), dispensation.getDispensationsende());

    // Testdaten löschen
    EntityManager entityManager = db.getCurrentEntityManager();
    entityManager.getTransaction().begin();
    Schueler schuelerToBeDeleted = schuelerDao.findById(schuelerSaved.getPersonId());
    schuelerDao.remove(schuelerToBeDeleted);
    entityManager.getTransaction().commit();
  }
}
