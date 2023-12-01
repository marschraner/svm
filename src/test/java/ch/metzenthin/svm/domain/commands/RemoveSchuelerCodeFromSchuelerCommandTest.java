package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Anrede;
import ch.metzenthin.svm.common.dataTypes.Geschlecht;
import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.daos.SchuelerCodeDao;
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

/**
 * @author Martin Schraner
 */
public class RemoveSchuelerCodeFromSchuelerCommandTest {

    private final SchuelerDao schuelerDao = new SchuelerDao();
    private final SchuelerCodeDao schuelerCodeDao = new SchuelerCodeDao();

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

        List<SchuelerCode> erfassteSchuelerCodes = new ArrayList<>();

        // Codes erzeugen
        SchuelerCode schuelerCode1 = new SchuelerCode("zt", "ZirkusTest", true);
        SchuelerCode schuelerCode2 = new SchuelerCode("jt", "JugendprojektTest", true);
        SaveOrUpdateSchuelerCodeCommand saveOrUpdateSchuelerCodeCommand = new SaveOrUpdateSchuelerCodeCommand(schuelerCode1, null, erfassteSchuelerCodes);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateSchuelerCodeCommand);
        saveOrUpdateSchuelerCodeCommand = new SaveOrUpdateSchuelerCodeCommand(schuelerCode2, null, erfassteSchuelerCodes);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateSchuelerCodeCommand);

        // Schueler erfassen und Codes hinzufügen
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

        SaveSchuelerCommand saveSchuelerCommand = new SaveSchuelerCommand(schueler);
        commandInvoker.executeCommandAsTransaction(saveSchuelerCommand);
        Schueler schuelerSaved = saveSchuelerCommand.getSavedSchueler();

        // Codes hinzufügen
        AddSchuelerCodeToSchuelerAndSaveCommand addSchuelerCodeToSchuelerAndSaveCommand = new AddSchuelerCodeToSchuelerAndSaveCommand(schuelerCode1, schuelerSaved);
        commandInvoker.executeCommandAsTransaction(addSchuelerCodeToSchuelerAndSaveCommand);
        addSchuelerCodeToSchuelerAndSaveCommand = new AddSchuelerCodeToSchuelerAndSaveCommand(schuelerCode2, schuelerSaved);
        commandInvoker.executeCommandAsTransaction(addSchuelerCodeToSchuelerAndSaveCommand);
        schuelerSaved = addSchuelerCodeToSchuelerAndSaveCommand.getSchuelerUpdated();

        assertEquals(2, schuelerSaved.getSchuelerCodes().size());
        assertEquals("jt", schuelerSaved.getSchuelerCodesAsList().get(0).getKuerzel());
        assertEquals("zt", schuelerSaved.getSchuelerCodesAsList().get(1).getKuerzel());

        // 2. SchuelerCode von Schüler löschen
        RemoveSchuelerCodeFromSchuelerCommand removeSchuelerCodeFromSchuelerCommand = new RemoveSchuelerCodeFromSchuelerCommand(schuelerSaved.getSchuelerCodesAsList().get(1), schuelerSaved);
        commandInvoker.executeCommandAsTransaction(removeSchuelerCodeFromSchuelerCommand);

        Schueler schuelerUpdated = removeSchuelerCodeFromSchuelerCommand.getSchuelerUpdated();
        assertEquals(1, schuelerUpdated.getSchuelerCodes().size());
        assertEquals("jt", schuelerUpdated.getSchuelerCodesAsList().get(0).getKuerzel());

        // 1. SchuelerCode von Schüler löschen
        removeSchuelerCodeFromSchuelerCommand = new RemoveSchuelerCodeFromSchuelerCommand(schuelerUpdated.getSchuelerCodesAsList().get(0), schuelerSaved);
        commandInvoker.executeCommandAsTransaction(removeSchuelerCodeFromSchuelerCommand);

        schuelerUpdated = removeSchuelerCodeFromSchuelerCommand.getSchuelerUpdated();
        assertEquals(0, schuelerUpdated.getSchuelerCodes().size());

        // Testdaten löschen
        EntityManager entityManager = db.getCurrentEntityManager();
        entityManager.getTransaction().begin();
        Schueler schuelerToBeDeleted = schuelerDao.findById(schuelerUpdated.getPersonId());
        for (SchuelerCode schuelerCode : erfassteSchuelerCodes) {
            SchuelerCode schuelerCodeToBeDeleted = schuelerCodeDao.findById(schuelerCode.getCodeId());
            schuelerCodeDao.removeFromSchuelerAndUpdate(schuelerCodeToBeDeleted, schuelerToBeDeleted);
            schuelerCodeDao.remove(schuelerCodeToBeDeleted);
        }
        schuelerDao.remove(schuelerToBeDeleted);
        entityManager.getTransaction().commit();
    }
}