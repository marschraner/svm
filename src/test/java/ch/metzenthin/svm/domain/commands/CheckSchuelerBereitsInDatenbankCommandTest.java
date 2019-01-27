package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Anrede;
import ch.metzenthin.svm.common.dataTypes.Geschlecht;
import ch.metzenthin.svm.persistence.daos.SchuelerDao;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Anmeldung;
import ch.metzenthin.svm.persistence.entities.Schueler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by Martin Schraner.
 */
public class CheckSchuelerBereitsInDatenbankCommandTest {

    private CommandInvoker commandInvoker = new CommandInvokerImpl();
    private Schueler schuelerTestdata;

    @Before
    public void setUp() throws Exception {
        createSvmPropertiesFileDefault();
        createTestdata();
    }

    @After
    public void tearDown() throws Exception {
        deleteTestdata();
        commandInvoker.closeSessionAndEntityManagerFactory();
    }

    @Test
    public void testExecute_NICHT_IN_DATENBANK() {

        Schueler schueler = new Schueler("Yolanda", "Bruggisser", new GregorianCalendar(2000, Calendar.JANUARY, 20), "056 426 69 15", null, null, Geschlecht.W, null);
        Angehoeriger rechnungsempfaenger = new Angehoeriger(Anrede.HERR, "Andreas", "Bruggisser", "056 426 69 15", null, null);
        Adresse adresse = new Adresse("Wiesenstrasse", "5", "5430", "Wettingen");
        rechnungsempfaenger.setAdresse(adresse);
        schueler.setAdresse(adresse);
        schueler.setRechnungsempfaenger(rechnungsempfaenger);
        schueler.addAnmeldung(new Anmeldung(new GregorianCalendar(2015, Calendar.MAY, 1), null));

        CheckSchuelerBereitsInDatenbankCommand checkSchuelerBereitsInDatenbankCommand = new CheckSchuelerBereitsInDatenbankCommand(schueler);
        commandInvoker.executeCommandAsTransactionWithOpenAndClose(checkSchuelerBereitsInDatenbankCommand);

        assertNull(checkSchuelerBereitsInDatenbankCommand.getSchuelerFound(null));
    }

    @Test
    public void testExecute_IN_DATENBANK() {
        Schueler schueler = new Schueler("Carla", "Bruggisser", new GregorianCalendar(2000, Calendar.JANUARY, 20), "056 426 77 15", null, null, Geschlecht.W, null);
        Angehoeriger rechnungsempfaenger = new Angehoeriger(Anrede.HERR, "Andreas", "Bruggisser", "056 426 77 15", null, null);
        Adresse adresse = new Adresse("Wiesenstrasse", "5", "5430", "Wettingen");  // andere Telefonnummer
        rechnungsempfaenger.setAdresse(adresse);
        schueler.setAdresse(adresse);
        schueler.setRechnungsempfaenger(rechnungsempfaenger);
        schueler.addAnmeldung(new Anmeldung(new GregorianCalendar(2015, Calendar.MAY, 1), null));

        CheckSchuelerBereitsInDatenbankCommand checkSchuelerBereitsInDatenbankCommand = new CheckSchuelerBereitsInDatenbankCommand(schueler);
        commandInvoker.executeCommandAsTransactionWithOpenAndClose(checkSchuelerBereitsInDatenbankCommand);

        Schueler schuelerFound = checkSchuelerBereitsInDatenbankCommand.getSchuelerFound(null);
        assertNotNull(schuelerFound);
        System.out.println("Schüler bereits in Datenbank erfasst: " + schuelerFound);
    }

    @Test
    public void testExecute_IN_DATENBANK_EIGENE() {
        CheckSchuelerBereitsInDatenbankCommand checkSchuelerBereitsInDatenbankCommand = new CheckSchuelerBereitsInDatenbankCommand(schuelerTestdata);
        commandInvoker.executeCommandAsTransactionWithOpenAndClose(checkSchuelerBereitsInDatenbankCommand);

        Schueler schuelerFound = checkSchuelerBereitsInDatenbankCommand.getSchuelerFound(schuelerTestdata);
        assertNull(schuelerFound);
    }

    private void createTestdata() {
        commandInvoker.openSession();
        EntityManager entityManager = commandInvoker.getEntityManager();
        entityManager.getTransaction().begin();

        SchuelerDao schuelerDao = new SchuelerDao(entityManager);

        Schueler schueler = new Schueler("Carla", "Bruggisser", new GregorianCalendar(2000, Calendar.JANUARY, 20), "056 426 69 15", null, null, Geschlecht.W, null);
        Angehoeriger rechnungsempfaenger = new Angehoeriger(Anrede.HERR, "Andreas", "Bruggisser", "056 426 69 15", null, null);
        Adresse adresse = new Adresse("Wiesenstrasse", "5", "5430", "Wettingen");
        rechnungsempfaenger.setAdresse(adresse);
        schueler.setAdresse(adresse);
        schueler.setRechnungsempfaenger(rechnungsempfaenger);
        schueler.addAnmeldung(new Anmeldung(new GregorianCalendar(2015, Calendar.MAY, 1), null));
        schuelerTestdata = schuelerDao.save(schueler);

        entityManager.getTransaction().commit();
        commandInvoker.closeSession();
    }

    private void deleteTestdata() {
        commandInvoker.openSession();
        EntityManager entityManager = commandInvoker.getEntityManager();
        entityManager.getTransaction().begin();

        SchuelerDao schuelerDao = new SchuelerDao(entityManager);

        Schueler schuelerToBeRemoved = schuelerDao.findById(schuelerTestdata.getPersonId());
        schuelerDao.remove(schuelerToBeRemoved);

        entityManager.getTransaction().commit();
        commandInvoker.closeSession();
    }
}