package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Anrede;
import ch.metzenthin.svm.common.dataTypes.Geschlecht;
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

import static org.junit.Assert.assertEquals;

/**
 * @author Hans Stamm
 */
public class SaveSchuelerCommandTest {

    private CommandInvoker commandInvoker = new CommandInvokerImpl();
    private EntityManagerFactory entityManagerFactory;

    @Before
    public void setUp() throws Exception {
        entityManagerFactory = Persistence.createEntityManagerFactory("svmtest");
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

        Dispensation dispensation = new Dispensation(new GregorianCalendar(2014, Calendar.JANUARY, 15), new GregorianCalendar(2015, Calendar.MARCH, 31), null, "Zu klein");
        schueler.addDispensation(dispensation);

        SaveSchuelerCommand saveSchuelerCommand = new SaveSchuelerCommand(schueler);
        commandInvoker.executeCommandAsTransactionWithOpenAndCloseSvmTest(saveSchuelerCommand);
        Schueler savedSchueler = saveSchuelerCommand.getSavedSchueler();

        assertEquals("Vorname not found", "Jana", savedSchueler.getVorname());

        // Löschen
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