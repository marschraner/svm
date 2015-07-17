package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.dataTypes.Anrede;
import ch.metzenthin.svm.dataTypes.Geschlecht;
import ch.metzenthin.svm.persistence.daos.AngehoerigerDao;
import ch.metzenthin.svm.persistence.daos.SchuelerDao;
import ch.metzenthin.svm.persistence.entities.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Martin Schraner
 */
public class DeleteSchuelerCommandTest {

    private CommandInvoker commandInvoker = new CommandInvokerImpl();
    private EntityManagerFactory entityManagerFactory;

    @Before
    public void setUp() throws Exception {
        commandInvoker.openSession();
        entityManagerFactory = Persistence.createEntityManagerFactory("svm");
    }

    @After
    public void tearDown() throws Exception {
        commandInvoker.closeSession();
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }

    @Test
    public void testExecute() throws Exception {

        List<Code> codesSaved = new ArrayList<>();

        // 2 Codes erfassen und den zweiten einem Schueler hinzufügen
        Code code1 = new Code("zt", "ZirkusTest");
        Code code2 = new Code("jt", "JugendprojektTest");

        SaveOrUpdateCodeCommand saveOrUpdateCodeCommand = new SaveOrUpdateCodeCommand(code1, null, codesSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateCodeCommand);

        saveOrUpdateCodeCommand = new SaveOrUpdateCodeCommand(code2, null, codesSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateCodeCommand);

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

        // Dispensation
        Dispensation dispensation = new Dispensation(new GregorianCalendar(2014, Calendar.JANUARY, 15), new GregorianCalendar(2015, Calendar.MARCH, 31), null, "Zu klein");
        schueler.addDispensation(dispensation);

        // Codes hinzufügen
        schueler.addCode(code1);
        schueler.addCode(code2);

        SaveSchuelerCommand saveSchuelerCommand = new SaveSchuelerCommand(schueler);
        commandInvoker.executeCommandAsTransaction(saveSchuelerCommand);
        Schueler schuelerSaved = saveSchuelerCommand.getSavedSchueler();
        int schuelerId = schuelerSaved.getPersonId();
        int vaterId = schuelerSaved.getVater().getPersonId();

        assertEquals("Jana", schuelerSaved.getVorname());
        assertEquals("Eugen", schuelerSaved.getVater().getVorname());
        assertEquals(2, schuelerSaved.getCodes().size());
        assertEquals(1, schuelerSaved.getDispensationen().size());

        // Schüler löschen
        DeleteSchuelerCommand deleteSchuelerCommand = new DeleteSchuelerCommand(schuelerSaved);
        commandInvoker.executeCommandAsTransaction(deleteSchuelerCommand);
        assertEquals(DeleteSchuelerCommand.Result.LOESCHEN_ERFOLGREICH, deleteSchuelerCommand.getResult());

        // Codes löschen
        DeleteCodeCommand deleteCodeCommand = new DeleteCodeCommand(codesSaved, 0);
        commandInvoker.executeCommandAsTransaction(deleteCodeCommand);
        deleteCodeCommand = new DeleteCodeCommand(codesSaved, 0);
        commandInvoker.executeCommandAsTransaction(deleteCodeCommand);

        // Prüfen, ob Schüler und Vater gelöscht
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            SchuelerDao schuelerDao = new SchuelerDao(entityManager);
            assertNull(schuelerDao.findById(schuelerId));
            AngehoerigerDao angehoerigerDao = new AngehoerigerDao(entityManager);
            assertNull(angehoerigerDao.findById(vaterId));
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }

    }
}