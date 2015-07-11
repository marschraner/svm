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
public class DeleteDispensationCommandTest {

    private CommandInvoker commandInvoker = new CommandInvokerImpl();
    private EntityManagerFactory entityManagerFactory;

    @Before
    public void setUp() throws Exception {
        entityManagerFactory = Persistence.createEntityManagerFactory("svm");
    }

    @After
    public void tearDown() throws Exception {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }

    @Test
    public void testExecute() throws Exception {

        // 1. Transaktion: Schueler erfassen und 3 Dispensationen hinzufügen
        commandInvoker.beginTransaction();

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
        commandInvoker.executeCommandWithinTransaction(saveSchuelerCommand);
        Schueler savedSchueler = saveSchuelerCommand.getSavedSchueler();

        // 3 Dispensationen hinzufügen
        Dispensation dispensation1 = new Dispensation(new GregorianCalendar(2012, Calendar.JANUARY, 15), new GregorianCalendar(2012, Calendar.MARCH, 31), null, "Zu klein");
        SaveDispensationCommand saveDispensationCommand = new SaveDispensationCommand(dispensation1, null, savedSchueler);
        commandInvoker.executeCommandWithinTransaction(saveDispensationCommand);

        Dispensation dispensation2 = new Dispensation(new GregorianCalendar(2013, Calendar.JANUARY, 15), new GregorianCalendar(2013, Calendar.MARCH, 31), null, "Beinbruch");
        saveDispensationCommand = new SaveDispensationCommand(dispensation2, null, savedSchueler);
        commandInvoker.executeCommandWithinTransaction(saveDispensationCommand);

        Dispensation dispensation3 = new Dispensation(new GregorianCalendar(2014, Calendar.JANUARY, 15), new GregorianCalendar(2014, Calendar.MARCH, 31), null, "Armbruch");
        saveDispensationCommand = new SaveDispensationCommand(dispensation3, null, savedSchueler);
        commandInvoker.executeCommandWithinTransaction(saveDispensationCommand);

        savedSchueler = saveDispensationCommand.getSavedSchueler();

        commandInvoker.commitTransaction();

        assertEquals(3, savedSchueler.getDispensationen().size());


        // 2. Transaktion: zweite Dispensation löschen
        commandInvoker.beginTransaction();

        DeleteDispensationCommand deleteDispensationCommand = new DeleteDispensationCommand(1, savedSchueler);
        commandInvoker.executeCommandWithinTransaction(deleteDispensationCommand);

        commandInvoker.commitTransaction();

        Schueler updatedSchueler = deleteDispensationCommand.getUpdatedSchueler();

        assertEquals(2, updatedSchueler.getDispensationen().size());
        assertEquals("Armbruch", updatedSchueler.getDispensationen().get(0).getGrund());  // neuster zuoberst
        assertEquals("Zu klein", updatedSchueler.getDispensationen().get(1).getGrund());


        // Testdaten löschen
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            SchuelerDao schuelerDao = new SchuelerDao(entityManager);
            Schueler schuelerToBeDeleted = schuelerDao.findById(updatedSchueler.getPersonId());
            schuelerDao.remove(schuelerToBeDeleted);
            entityManager.getTransaction().commit();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }

    }
}