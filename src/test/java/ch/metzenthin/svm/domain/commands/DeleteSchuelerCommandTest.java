package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Anrede;
import ch.metzenthin.svm.common.dataTypes.Geschlecht;
import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.daos.AngehoerigerDao;
import ch.metzenthin.svm.persistence.daos.SchuelerDao;
import ch.metzenthin.svm.persistence.entities.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Martin Schraner
 */
public class DeleteSchuelerCommandTest {

    private DB db;
    private CommandInvoker commandInvoker;

    @Before
    public void setUp() throws Exception {
        createSvmPropertiesFileDefault();
        db = DBFactory.getInstance();
        commandInvoker = new CommandInvokerImpl();
    }

    @After
    public void tearDown() throws Exception {
        db.closeSession();
    }

    @Test
    public void testExecute() throws Exception {

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
        Angehoeriger vater = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", "044 491 69 33", null, null);
        vater.setAdresse(adresse);
        schueler.setVater(vater);

        // Set Rechnungsempfänger
        schueler.setRechnungsempfaenger(vater);

        // Dispensation
        Dispensation dispensation = new Dispensation(new GregorianCalendar(2014, Calendar.JANUARY, 15), new GregorianCalendar(2015, Calendar.MARCH, 31), null, "Zu klein");
        schueler.addDispensation(dispensation);

        // Codes hinzufügen
        schueler.addCode(schuelerCode1);
        schueler.addCode(schuelerCode2);

        SaveSchuelerCommand saveSchuelerCommand = new SaveSchuelerCommand(schueler);
        commandInvoker.executeCommandAsTransaction(saveSchuelerCommand);
        Schueler schuelerSaved = saveSchuelerCommand.getSavedSchueler();
        int schuelerId = schuelerSaved.getPersonId();
        int vaterId = schuelerSaved.getVater().getPersonId();

        assertEquals("Jana", schuelerSaved.getVorname());
        assertEquals("Eugen", schuelerSaved.getVater().getVorname());
        assertEquals(2, schuelerSaved.getSchuelerCodes().size());
        assertEquals(1, schuelerSaved.getDispensationen().size());

        // Schüler löschen
        DeleteSchuelerCommand deleteSchuelerCommand = new DeleteSchuelerCommand(schuelerSaved);
        commandInvoker.executeCommandAsTransaction(deleteSchuelerCommand);
        assertEquals(DeleteSchuelerCommand.Result.LOESCHEN_ERFOLGREICH, deleteSchuelerCommand.getResult());

        // Codes löschen
        DeleteSchuelerCodeCommand deleteSchuelerCodeCommand = new DeleteSchuelerCodeCommand(codesSaved, 0);
        commandInvoker.executeCommandAsTransaction(deleteSchuelerCodeCommand);
        deleteSchuelerCodeCommand = new DeleteSchuelerCodeCommand(codesSaved, 0);
        commandInvoker.executeCommandAsTransaction(deleteSchuelerCodeCommand);

        // Prüfen, ob Schüler und Vater gelöscht
        EntityManager entityManager = db.getCurrentEntityManager();
        SchuelerDao schuelerDao = new SchuelerDao(entityManager);
        assertNull(schuelerDao.findById(schuelerId));
        AngehoerigerDao angehoerigerDao = new AngehoerigerDao(entityManager);
        assertNull(angehoerigerDao.findById(vaterId));
    }
}