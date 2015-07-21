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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * @author Martin Schraner
 */
public class DeleteCodeCommandTest {

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

        // Code hinzufügen
        schueler.addCode(code2);

        SaveSchuelerCommand saveSchuelerCommand = new SaveSchuelerCommand(schueler);
        commandInvoker.executeCommandAsTransaction(saveSchuelerCommand);
        Schueler savedSchueler = saveSchuelerCommand.getSavedSchueler();

        assertEquals(2, codesSaved.size());
        assertEquals("jt", codesSaved.get(0).getKuerzel()); // alphabetisch geordnet
        assertEquals("zt", codesSaved.get(1).getKuerzel());
        assertEquals(1, savedSchueler.getCodes().size());


        // Codes löschen
        DeleteCodeCommand deleteCodeCommand = new DeleteCodeCommand(codesSaved, 1);
        commandInvoker.executeCommandAsTransaction(deleteCodeCommand);
        assertEquals(DeleteCodeCommand.Result.LOESCHEN_ERFOLGREICH, deleteCodeCommand.getResult());
        assertEquals(1, codesSaved.size());

        // Kann nicht gelöscht werden, da referenziert
        deleteCodeCommand = new DeleteCodeCommand(codesSaved, 0);
        commandInvoker.executeCommandAsTransaction(deleteCodeCommand);
        assertEquals(DeleteCodeCommand.Result.CODE_VON_SCHUELER_REFERENZIERT, deleteCodeCommand.getResult());
        assertEquals(1, codesSaved.size());

        // Code vom Schüler entfernen, jetzt löschen möglich
        savedSchueler.deleteCode(codesSaved.get(0));
        deleteCodeCommand = new DeleteCodeCommand(codesSaved, 0);
        commandInvoker.executeCommandAsTransaction(deleteCodeCommand);
        assertEquals(DeleteCodeCommand.Result.LOESCHEN_ERFOLGREICH, deleteCodeCommand.getResult());
        assertTrue(codesSaved.isEmpty());


        // Testdaten löschen
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