package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.dataTypes.Anrede;
import ch.metzenthin.svm.dataTypes.Geschlecht;
import ch.metzenthin.svm.persistence.SvmDbException;
import ch.metzenthin.svm.persistence.daos.SchuelerDao;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Anmeldung;
import ch.metzenthin.svm.persistence.entities.Schueler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;

/**
 * Created by Martin Schraner.
 */
public class CheckSchuelerBereitsInDatenbankCommandTest {

    private CommandInvoker commandInvoker = new CommandInvokerImpl();
    private EntityManagerFactory entityManagerFactory;
    private Schueler schuelerTestdata;

    @Before
    public void setUp() throws Exception {
        entityManagerFactory = Persistence.createEntityManagerFactory("svm");
        createTestdata();
    }

    @After
    public void tearDown() throws Exception {
        deleteTestdata();
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
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
        try {
            commandInvoker.executeCommand(checkSchuelerBereitsInDatenbankCommand);
        } catch (SvmDbException e) {
            e.printStackTrace();
        }

        assertFalse("Schüler in Datenbank", checkSchuelerBereitsInDatenbankCommand.isInDatenbank());
        assertNull(checkSchuelerBereitsInDatenbankCommand.getSchuelerFound());
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
        try {
            commandInvoker.executeCommand(checkSchuelerBereitsInDatenbankCommand);
        } catch (SvmDbException e) {
            e.printStackTrace();
        }

        assertTrue("Schüler in Datenbank", checkSchuelerBereitsInDatenbankCommand.isInDatenbank());
        Schueler schuelerFound = checkSchuelerBereitsInDatenbankCommand.getSchuelerFound();
        assertNotNull(schuelerFound);
        System.out.println("Schüler bereits in Datenbank erfasst: " + schuelerFound);
    }

    private void createTestdata() {
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
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

        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    private void deleteTestdata() {
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            SchuelerDao schuelerDao = new SchuelerDao(entityManager);

            Schueler schuelerToBeRemoved = schuelerDao.findById(schuelerTestdata.getPersonId());
            schuelerDao.remove(schuelerToBeRemoved);

            entityManager.getTransaction().commit();

        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }
}