package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.datatypes.Anrede;
import ch.metzenthin.svm.common.datatypes.Geschlecht;
import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.daos.SchuelerDao;
import ch.metzenthin.svm.persistence.entities.*;
import jakarta.persistence.EntityManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Martin Schraner
 */
public class DeleteSchuelerCodeCommandTest {

    private final SchuelerDao schuelerDao = new SchuelerDao();

    private DB db;
    private CommandInvoker commandInvoker;

    @Before
    public void setUp() {
        createSvmPropertiesFileDefault();
        db = DBFactory.getInstance();
        commandInvoker = new CommandInvokerImpl();
    }

    @After
    public void tearDown() {
        db.closeSession();
    }

    @Test
    public void testExecute() {

        List<SchuelerCode> codesSaved = new ArrayList<>();

        // 2 Codes erfassen und den zweiten einem Schueler hinzufügen
        SchuelerCode schuelerCode1 = new SchuelerCode("zt", "ZirkusTest", true);
        SchuelerCode schuelerCode2 = new SchuelerCode("jt", "JugendprojektTest", true);

        SaveOrUpdateSchuelerCodeCommand saveOrUpdateSchuelerCodeCommand = new SaveOrUpdateSchuelerCodeCommand(schuelerCode1, null, codesSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateSchuelerCodeCommand);

        saveOrUpdateSchuelerCodeCommand = new SaveOrUpdateSchuelerCodeCommand(schuelerCode2, null, codesSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateSchuelerCodeCommand);

        Schueler schueler = new Schueler("Jana", "Rösle", new GregorianCalendar(2012, Calendar.JULY, 24), "044 491 69 33", null, null, Geschlecht.W, "Schwester von Valentin");
        Adresse adresse = new Adresse("Hohenklingenstrasse", "15", "8049", "Zürich");
        schueler.setAdresse(adresse);
        schueler.addAnmeldung(new Anmeldung(new GregorianCalendar(2015, Calendar.MAY, 1), null));

        // Set Vater
        Angehoeriger vater = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", "044 491 69 33", null, null, false);
        vater.setAdresse(adresse);
        schueler.setVater(vater);

        // Set Rechnungsempfänger
        schueler.setRechnungsempfaenger(vater);

        // SchuelerCode hinzufügen
        schueler.addCode(schuelerCode2);

        SaveSchuelerCommand saveSchuelerCommand = new SaveSchuelerCommand(schueler);
        commandInvoker.executeCommandAsTransaction(saveSchuelerCommand);
        Schueler savedSchueler = saveSchuelerCommand.getSavedSchueler();

        assertEquals(2, codesSaved.size());
        assertEquals("jt", codesSaved.get(0).getKuerzel()); // alphabetisch geordnet
        assertEquals("zt", codesSaved.get(1).getKuerzel());
        assertEquals(1, savedSchueler.getSchuelerCodes().size());

        // Codes löschen
        DeleteSchuelerCodeCommand deleteSchuelerCodeCommand = new DeleteSchuelerCodeCommand(codesSaved, 1);
        commandInvoker.executeCommandAsTransaction(deleteSchuelerCodeCommand);
        assertEquals(DeleteSchuelerCodeCommand.Result.LOESCHEN_ERFOLGREICH, deleteSchuelerCodeCommand.getResult());
        assertEquals(1, codesSaved.size());

        // Kann nicht gelöscht werden, da referenziert
        deleteSchuelerCodeCommand = new DeleteSchuelerCodeCommand(codesSaved, 0);
        commandInvoker.executeCommandAsTransaction(deleteSchuelerCodeCommand);
        assertEquals(DeleteSchuelerCodeCommand.Result.CODE_VON_SCHUELER_REFERENZIERT, deleteSchuelerCodeCommand.getResult());
        assertEquals(1, codesSaved.size());

        // SchuelerCode vom Schüler entfernen, jetzt löschen möglich
        RemoveSchuelerCodeFromSchuelerCommand removeSchuelerCodeFromSchuelerCommand = new RemoveSchuelerCodeFromSchuelerCommand(codesSaved.get(0), savedSchueler);
        commandInvoker.executeCommandAsTransaction(removeSchuelerCodeFromSchuelerCommand);
        deleteSchuelerCodeCommand = new DeleteSchuelerCodeCommand(codesSaved, 0);
        commandInvoker.executeCommandAsTransaction(deleteSchuelerCodeCommand);
        assertEquals(DeleteSchuelerCodeCommand.Result.LOESCHEN_ERFOLGREICH, deleteSchuelerCodeCommand.getResult());
        assertTrue(codesSaved.isEmpty());

        // Testdaten löschen
        EntityManager entityManager = db.getCurrentEntityManager();
        entityManager.getTransaction().begin();
        Schueler schuelerToBeDeleted = schuelerDao.findById(savedSchueler.getPersonId());
        schuelerDao.remove(schuelerToBeDeleted);
        entityManager.getTransaction().commit();
    }
}