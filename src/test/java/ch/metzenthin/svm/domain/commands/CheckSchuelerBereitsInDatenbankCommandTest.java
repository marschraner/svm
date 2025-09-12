package ch.metzenthin.svm.domain.commands;

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import ch.metzenthin.svm.common.datatypes.Anrede;
import ch.metzenthin.svm.common.datatypes.Geschlecht;
import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.daos.SchuelerDao;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Anmeldung;
import ch.metzenthin.svm.persistence.entities.Schueler;
import jakarta.persistence.EntityManager;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/** Created by Martin Schraner. */
public class CheckSchuelerBereitsInDatenbankCommandTest {

  private final SchuelerDao schuelerDao = new SchuelerDao();

  private DB db;
  private CommandInvoker commandInvoker;
  private Schueler schuelerTestdata;

  @Before
  public void setUp() {
    createSvmPropertiesFileDefault();
    db = DBFactory.getInstance();
    commandInvoker = new CommandInvokerImpl();
    createTestdata();
  }

  @After
  public void tearDown() {
    deleteTestdata();
    db.closeSession();
  }

  @SuppressWarnings("ExtractMethodRecommender")
  @Test
  public void testExecute_NICHT_IN_DATENBANK() {

    Schueler schueler =
        new Schueler(
            "Yolanda",
            "Bruggisser",
            new GregorianCalendar(2000, Calendar.JANUARY, 20),
            "056 426 69 15",
            null,
            null,
            Geschlecht.W,
            null);
    Angehoeriger rechnungsempfaenger =
        new Angehoeriger(Anrede.HERR, "Andreas", "Bruggisser", "056 426 69 15", null, null, false);
    Adresse adresse = new Adresse("Wiesenstrasse", "5", "5430", "Wettingen");
    rechnungsempfaenger.setAdresse(adresse);
    schueler.setAdresse(adresse);
    schueler.setRechnungsempfaenger(rechnungsempfaenger);
    schueler.addAnmeldung(new Anmeldung(new GregorianCalendar(2015, Calendar.MAY, 1), null));

    CheckSchuelerBereitsInDatenbankCommand checkSchuelerBereitsInDatenbankCommand =
        new CheckSchuelerBereitsInDatenbankCommand(schueler);
    commandInvoker.executeCommand(checkSchuelerBereitsInDatenbankCommand);

    assertNull(checkSchuelerBereitsInDatenbankCommand.getSchuelerFound(null));
  }

  @SuppressWarnings("ExtractMethodRecommender")
  @Test
  public void testExecute_IN_DATENBANK() {
    Schueler schueler =
        new Schueler(
            "Carla",
            "Bruggisser",
            new GregorianCalendar(2000, Calendar.JANUARY, 20),
            "056 426 77 15",
            null,
            null,
            Geschlecht.W,
            null);
    Angehoeriger rechnungsempfaenger =
        new Angehoeriger(Anrede.HERR, "Andreas", "Bruggisser", "056 426 77 15", null, null, false);
    Adresse adresse =
        new Adresse("Wiesenstrasse", "5", "5430", "Wettingen"); // andere Telefonnummer
    rechnungsempfaenger.setAdresse(adresse);
    schueler.setAdresse(adresse);
    schueler.setRechnungsempfaenger(rechnungsempfaenger);
    schueler.addAnmeldung(new Anmeldung(new GregorianCalendar(2015, Calendar.MAY, 1), null));

    CheckSchuelerBereitsInDatenbankCommand checkSchuelerBereitsInDatenbankCommand =
        new CheckSchuelerBereitsInDatenbankCommand(schueler);
    commandInvoker.executeCommand(checkSchuelerBereitsInDatenbankCommand);

    Schueler schuelerFound = checkSchuelerBereitsInDatenbankCommand.getSchuelerFound(null);
    assertNotNull(schuelerFound);
    System.out.println("Schüler bereits in Datenbank erfasst: " + schuelerFound);
  }

  @Test
  public void testExecute_IN_DATENBANK_EIGENE() {
    CheckSchuelerBereitsInDatenbankCommand checkSchuelerBereitsInDatenbankCommand =
        new CheckSchuelerBereitsInDatenbankCommand(schuelerTestdata);
    commandInvoker.executeCommand(checkSchuelerBereitsInDatenbankCommand);

    Schueler schuelerFound =
        checkSchuelerBereitsInDatenbankCommand.getSchuelerFound(schuelerTestdata);
    assertNull(schuelerFound);
  }

  @SuppressWarnings("ExtractMethodRecommender")
  private void createTestdata() {
    EntityManager entityManager = db.getCurrentEntityManager();
    entityManager.getTransaction().begin();

    Schueler schueler =
        new Schueler(
            "Carla",
            "Bruggisser",
            new GregorianCalendar(2000, Calendar.JANUARY, 20),
            "056 426 69 15",
            null,
            null,
            Geschlecht.W,
            null);
    Angehoeriger rechnungsempfaenger =
        new Angehoeriger(Anrede.HERR, "Andreas", "Bruggisser", "056 426 69 15", null, null, false);
    Adresse adresse = new Adresse("Wiesenstrasse", "5", "5430", "Wettingen");
    rechnungsempfaenger.setAdresse(adresse);
    schueler.setAdresse(adresse);
    schueler.setRechnungsempfaenger(rechnungsempfaenger);
    schueler.addAnmeldung(new Anmeldung(new GregorianCalendar(2015, Calendar.MAY, 1), null));
    schuelerTestdata = schuelerDao.save(schueler);

    entityManager.getTransaction().commit();
    db.closeSession();
  }

  private void deleteTestdata() {
    EntityManager entityManager = db.getCurrentEntityManager();
    entityManager.getTransaction().begin();

    Schueler schuelerToBeRemoved = schuelerDao.findById(schuelerTestdata.getPersonId());
    schuelerDao.remove(schuelerToBeRemoved);

    entityManager.getTransaction().commit();
    db.closeSession();
  }
}
