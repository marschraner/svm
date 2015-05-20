package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.dataTypes.Anrede;
import ch.metzenthin.svm.dataTypes.Geschlecht;
import ch.metzenthin.svm.persistence.daos.SchuelerDao;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Dispensation;
import ch.metzenthin.svm.persistence.entities.Schueler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;

/**
 * @author Hans Stamm
 */
public class SaveSchuelerCommandTest {

    private CommandInvoker commandInvoker;
    private EntityManagerFactory entityManagerFactory;

    @Before
    public void setUp() throws Exception {
        entityManagerFactory = Persistence.createEntityManagerFactory("svm");
        commandInvoker = new CommandInvokerImpl(entityManagerFactory);
    }

    @After
    public void tearDown() throws Exception {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }

    @Test
    public void testExecute() throws Exception {

        // Schueler
        Schueler schueler = new Schueler("Jana", "Rösle", new GregorianCalendar(2012, Calendar.JULY, 24), null, null, Geschlecht.W, new GregorianCalendar(2015, Calendar.JANUARY, 1), null, "Schwester von Valentin");
        Adresse adresse = new Adresse("Hohenklingenstrasse", "15", "8049", "Zürich", "044 491 69 33");
        schueler.setAdresse(adresse);

        // Set Vater
        Angehoeriger vater = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", null, null, null);
        vater.setAdresse(adresse);
        schueler.setVater(vater);

        // Set Rechnungsempfänger
        schueler.setRechnungsempfaenger(vater);

        Dispensation dispensation = new Dispensation(new GregorianCalendar(2014, Calendar.JANUARY, 15), new GregorianCalendar(2015, Calendar.MARCH, 31), "Zu klein");
        schueler.addDispensation(dispensation);

        SaveSchuelerCommand saveSchuelerCommand = new SaveSchuelerCommand(schueler);
        commandInvoker.executeCommand(saveSchuelerCommand);
        Schueler savedSchueler = saveSchuelerCommand.getSavedSchueler();

        assertEquals("Vorname not found", "Jana", savedSchueler.getVorname());
        assertEquals("Dispensation not correct", 1, savedSchueler.getDispensationen().size());

        // delete
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            SchuelerDao schuelerDao = new SchuelerDao(entityManager);
            Schueler schuelerToBeDeleted = schuelerDao.findById(savedSchueler.getPersonId());
            schuelerDao.remove(schuelerToBeDeleted);
            entityManager.getTransaction().commit();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }
}