package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Anrede;
import ch.metzenthin.svm.common.dataTypes.Geschlecht;
import ch.metzenthin.svm.common.utils.PersistenceProperties;
import ch.metzenthin.svm.persistence.daos.SchuelerCodeDao;
import ch.metzenthin.svm.persistence.daos.SchuelerDao;
import ch.metzenthin.svm.persistence.entities.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.*;

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;
import static org.junit.Assert.assertEquals;

/**
 * @author Martin Schraner
 */
public class AddSchuelerCodeToSchuelerAndSaveCommandTest {

    private CommandInvoker commandInvoker = new CommandInvokerImpl();
    private EntityManagerFactory entityManagerFactory;

    @Before
    public void setUp() throws Exception {
        createSvmPropertiesFileDefault();
        entityManagerFactory = Persistence.createEntityManagerFactory("svm", PersistenceProperties.getPersistenceProperties());
        commandInvoker.openSession();
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

        List<SchuelerCode> erfassteSchuelerCodes = new ArrayList<>();

        // Codes erzeugen
        SchuelerCode schuelerCode1 = new SchuelerCode("zt", "ZirkusTest", true);
        SchuelerCode schuelerCode2 = new SchuelerCode("jt", "JugendprojektTest", true);
        SaveOrUpdateSchuelerCodeCommand saveOrUpdateSchuelerCodeCommand = new SaveOrUpdateSchuelerCodeCommand(schuelerCode1, null, erfassteSchuelerCodes);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateSchuelerCodeCommand);
        saveOrUpdateSchuelerCodeCommand = new SaveOrUpdateSchuelerCodeCommand(schuelerCode2, null, erfassteSchuelerCodes);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateSchuelerCodeCommand);


        // Schueler erfassen und SchuelerCode hinzufügen
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

        // SchuelerCode hinzufügen
        AddSchuelerCodeToSchuelerAndSaveCommand addSchuelerCodeToSchuelerAndSaveCommand = new AddSchuelerCodeToSchuelerAndSaveCommand(schuelerCode1, schuelerSaved);
        commandInvoker.executeCommandAsTransaction(addSchuelerCodeToSchuelerAndSaveCommand);
        schuelerSaved = addSchuelerCodeToSchuelerAndSaveCommand.getSchuelerUpdated();

        assertEquals(1, schuelerSaved.getSchuelerCodes().size());
        assertEquals("zt", schuelerSaved.getCodesAsList().get(0).getKuerzel());


        // Weiteren SchuelerCode hinzufügen:
        addSchuelerCodeToSchuelerAndSaveCommand = new AddSchuelerCodeToSchuelerAndSaveCommand(schuelerCode2, schuelerSaved);
        commandInvoker.executeCommandAsTransaction(addSchuelerCodeToSchuelerAndSaveCommand);
        schuelerSaved = addSchuelerCodeToSchuelerAndSaveCommand.getSchuelerUpdated();

        assertEquals(2, schuelerSaved.getSchuelerCodes().size());
        // Alphabetisch geordnet?
        assertEquals("jt", schuelerSaved.getCodesAsList().get(0).getKuerzel());
        assertEquals("zt", schuelerSaved.getCodesAsList().get(1).getKuerzel());


        // Testdaten löschen
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            SchuelerDao schuelerDao = new SchuelerDao(entityManager);
            Schueler schuelerToBeDeleted = schuelerDao.findById(schuelerSaved.getPersonId());
            SchuelerCodeDao schuelerCodeDao = new SchuelerCodeDao(entityManager);
            for (SchuelerCode schuelerCode : erfassteSchuelerCodes) {
                SchuelerCode schuelerCodeToBeDeleted = schuelerCodeDao.findById(schuelerCode.getCodeId());
                schuelerCodeDao.removeFromSchuelerAndUpdate(schuelerCodeToBeDeleted, schuelerToBeDeleted);
                schuelerCodeDao.remove(schuelerCodeToBeDeleted);
            }
            schuelerDao.remove(schuelerToBeDeleted);
            entityManager.getTransaction().commit();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }

    }
}