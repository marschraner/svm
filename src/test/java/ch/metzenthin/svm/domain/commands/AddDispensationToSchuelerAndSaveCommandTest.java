package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.dataTypes.Anrede;
import ch.metzenthin.svm.dataTypes.Geschlecht;
import ch.metzenthin.svm.persistence.daos.SchuelerDao;
import ch.metzenthin.svm.persistence.entities.*;
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
 * @author Martin Schraner
 */
public class AddDispensationToSchuelerAndSaveCommandTest {

    private CommandInvoker commandInvoker = new CommandInvokerImpl();
    private EntityManagerFactory entityManagerFactory;

    @Before
    public void setUp() throws Exception {
        entityManagerFactory = Persistence.createEntityManagerFactory("svm");
        commandInvoker.openSession();
    }

    @After
    public void tearDown() throws Exception {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
        commandInvoker.closeSession();
    }

    @Test
    public void testExecute() throws Exception {

        // Schueler erfassen und Dispensation hinzufügen
        Schueler schueler = new Schueler("Jana", "Rösle", new GregorianCalendar(2012, Calendar.JULY, 24), "044 491 69 33", null, null, Geschlecht.W, "Schwester von Valentin");
        Adresse adresse = new Adresse("Hohenklingenstrasse", "15", "8049", "Zürich");
        schueler.setAdresse(adresse);
        schueler.addAnmeldung(new Anmeldung(new GregorianCalendar(2015, Calendar.MAY, 1), null));

        // Set Vater
        Angehoeriger vater = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", "044 491 69 33", null, null);
        vater.setAdresse(adresse);
        schueler.setVater(vater);

        // Set Rechnungsempfänger
        schueler.setRechnungsempfaenger(vater);

        SaveSchuelerCommand saveSchuelerCommand = new SaveSchuelerCommand(schueler);
        commandInvoker.executeCommandAsTransaction(saveSchuelerCommand);
        Schueler schuelerSaved = saveSchuelerCommand.getSavedSchueler();

        // Dispensation hinzufügen
        Dispensation dispensation1 = new Dispensation(new GregorianCalendar(2014, Calendar.JANUARY, 15), new GregorianCalendar(2015, Calendar.MARCH, 31), null, "Zu klein");
        AddDispensationToSchuelerAndSaveCommand addDispensationToSchuelerAndSaveCommand = new AddDispensationToSchuelerAndSaveCommand(dispensation1, null, schuelerSaved);
        commandInvoker.executeCommandAsTransaction(addDispensationToSchuelerAndSaveCommand);
        schuelerSaved = addDispensationToSchuelerAndSaveCommand.getSchuelerUpdated();

        assertEquals(1, schuelerSaved.getDispensationen().size());


        // Weitere Dispensation hinzufügen:
        Dispensation dispensation2 = new Dispensation(new GregorianCalendar(2015, Calendar.MAY, 15), null, null, "Immer noch zu klein");
        addDispensationToSchuelerAndSaveCommand = new AddDispensationToSchuelerAndSaveCommand(dispensation2, null, schuelerSaved);
        commandInvoker.executeCommandAsTransaction(addDispensationToSchuelerAndSaveCommand);
        schuelerSaved = addDispensationToSchuelerAndSaveCommand.getSchuelerUpdated();

        assertEquals(2, schuelerSaved.getDispensationen().size());


        // Dispensation bearbeiten (Dispensationsende setzen)
        Dispensation dispensation2Modif = new Dispensation(new GregorianCalendar(2015, Calendar.MAY, 15), new GregorianCalendar(2015, Calendar.DECEMBER, 31), null, "Immer noch zu klein");
        addDispensationToSchuelerAndSaveCommand = new AddDispensationToSchuelerAndSaveCommand(dispensation2Modif, dispensation2, schuelerSaved);
        commandInvoker.executeCommandAsTransaction(addDispensationToSchuelerAndSaveCommand);
        schuelerSaved = addDispensationToSchuelerAndSaveCommand.getSchuelerUpdated();

        assertEquals(2, schuelerSaved.getDispensationen().size());
        assertEquals(new GregorianCalendar(2015, Calendar.DECEMBER, 31), schuelerSaved.getDispensationen().get(0).getDispensationsende());


        // Testdaten löschen
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            SchuelerDao schuelerDao = new SchuelerDao(entityManager);
            Schueler schuelerToBeDeleted = schuelerDao.findById(schuelerSaved.getPersonId());
            schuelerDao.remove(schuelerToBeDeleted);
            entityManager.getTransaction().commit();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }

    }
}