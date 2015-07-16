package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.dataTypes.Anrede;
import ch.metzenthin.svm.dataTypes.Geschlecht;
import ch.metzenthin.svm.persistence.daos.CodeDao;
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

/**
 * @author Martin Schraner
 */
public class AddCodeToSchuelerAndSaveCommandTest {

    private CommandInvoker commandInvoker = new CommandInvokerImpl();
    private EntityManagerFactory entityManagerFactory;

    @Before
    public void setUp() throws Exception {
        entityManagerFactory = Persistence.createEntityManagerFactory("svm");
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

        List<Code> erfassteCodes = new ArrayList<>();

        // Codes erzeugen
        Code code1 = new Code("zt", "ZirkusTest");
        Code code2 = new Code("jt", "JugendprojektTest");
        SaveOrUpdateCodeCommand saveOrUpdateCodeCommand = new SaveOrUpdateCodeCommand(code1, null, erfassteCodes);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateCodeCommand);
        saveOrUpdateCodeCommand = new SaveOrUpdateCodeCommand(code2, null, erfassteCodes);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateCodeCommand);


        // Schueler erfassen und Code hinzufügen
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

        // Code hinzufügen
        AddCodeToSchuelerAndSaveCommand addCodeToSchuelerAndSaveCommand = new AddCodeToSchuelerAndSaveCommand(code1, schuelerSaved);
        commandInvoker.executeCommandAsTransaction(addCodeToSchuelerAndSaveCommand);
        schuelerSaved = addCodeToSchuelerAndSaveCommand.getSchuelerUpdated();

        assertEquals(1, schuelerSaved.getCodes().size());
        assertEquals("zt", schuelerSaved.getCodes().get(0).getKuerzel());


        // Weiteren Code hinzufügen:
        addCodeToSchuelerAndSaveCommand = new AddCodeToSchuelerAndSaveCommand(code2, schuelerSaved);
        commandInvoker.executeCommandAsTransaction(addCodeToSchuelerAndSaveCommand);
        schuelerSaved = addCodeToSchuelerAndSaveCommand.getSchuelerUpdated();

        assertEquals(2, schuelerSaved.getCodes().size());
        // Alphabetisch geordnet?
        assertEquals("jt", schuelerSaved.getCodes().get(0).getKuerzel());
        assertEquals("zt", schuelerSaved.getCodes().get(1).getKuerzel());


        // Testdaten löschen
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            SchuelerDao schuelerDao = new SchuelerDao(entityManager);
            Schueler schuelerToBeDeleted = schuelerDao.findById(schuelerSaved.getPersonId());
            CodeDao codeDao = new CodeDao(entityManager);
            for (Code code : erfassteCodes) {
                Code codeToBeDeleted = codeDao.findById(code.getCodeId());
                codeDao.removeFromSchuelerAndUpdate(codeToBeDeleted, schuelerToBeDeleted);
                codeDao.remove(codeToBeDeleted);
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