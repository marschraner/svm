package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.datatypes.Anrede;
import ch.metzenthin.svm.common.datatypes.Geschlecht;
import ch.metzenthin.svm.common.utils.SvmProperties;
import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.daos.SchuelerDao;
import ch.metzenthin.svm.persistence.entities.*;
import jakarta.persistence.EntityManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;
import static org.junit.Assert.assertEquals;

/**
 * @author Martin Schraner
 */
public class RemoveDispensationFromSchuelerCommandTest {

    private final SchuelerDao schuelerDao = new SchuelerDao();

    private DB db;
    private CommandInvoker commandInvoker;
    private boolean neusteZuoberst;

    @Before
    public void setUp() {
        createSvmPropertiesFileDefault();
        db = DBFactory.getInstance();
        commandInvoker = new CommandInvokerImpl();
        Properties svmProperties = SvmProperties.getSvmProperties();
        neusteZuoberst = !svmProperties.getProperty(SvmProperties.KEY_NEUSTE_ZUOBERST).equals("false");
    }

    @After
    public void tearDown() {
        db.closeSession();
    }

    @Test
    public void testExecute() {

        // Schueler erfassen und 3 Dispensationen hinzufügen
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
        Schueler savedSchueler = saveSchuelerCommand.getSavedSchueler();

        // 3 Dispensationen hinzufügen
        Dispensation dispensation1 = new Dispensation(new GregorianCalendar(2012, Calendar.JANUARY, 15), new GregorianCalendar(2012, Calendar.MARCH, 31), null, "Zu klein");
        AddDispensationToSchuelerAndSaveCommand addDispensationToSchuelerAndSaveCommand = new AddDispensationToSchuelerAndSaveCommand(dispensation1, null, savedSchueler);
        commandInvoker.executeCommandAsTransaction(addDispensationToSchuelerAndSaveCommand);

        Dispensation dispensation2 = new Dispensation(new GregorianCalendar(2013, Calendar.JANUARY, 15), new GregorianCalendar(2013, Calendar.MARCH, 31), null, "Beinbruch");
        addDispensationToSchuelerAndSaveCommand = new AddDispensationToSchuelerAndSaveCommand(dispensation2, null, savedSchueler);
        commandInvoker.executeCommandAsTransaction(addDispensationToSchuelerAndSaveCommand);

        Dispensation dispensation3 = new Dispensation(new GregorianCalendar(2014, Calendar.JANUARY, 15), new GregorianCalendar(2014, Calendar.MARCH, 31), null, "Armbruch");
        addDispensationToSchuelerAndSaveCommand = new AddDispensationToSchuelerAndSaveCommand(dispensation3, null, savedSchueler);
        commandInvoker.executeCommandAsTransaction(addDispensationToSchuelerAndSaveCommand);

        savedSchueler = addDispensationToSchuelerAndSaveCommand.getSchuelerUpdated();

        assertEquals(3, savedSchueler.getDispensationen().size());


        // zweite Dispensation löschen
        RemoveDispensationFromSchuelerCommand removeDispensationFromSchuelerCommand = new RemoveDispensationFromSchuelerCommand(1, savedSchueler);
        commandInvoker.executeCommandAsTransaction(removeDispensationFromSchuelerCommand);

        Schueler updatedSchueler = removeDispensationFromSchuelerCommand.getSchuelerUpdated();

        assertEquals(2, updatedSchueler.getDispensationen().size());
        if (neusteZuoberst) {
            assertEquals("Armbruch", updatedSchueler.getDispensationen().get(0).getGrund());  // neuste zuoberst
            assertEquals("Zu klein", updatedSchueler.getDispensationen().get(1).getGrund());
        } else {
            assertEquals("Armbruch", updatedSchueler.getDispensationen().get(1).getGrund());  // neuste zuletzt
            assertEquals("Zu klein", updatedSchueler.getDispensationen().get(0).getGrund());
        }

        // Testdaten löschen
        EntityManager entityManager = db.getCurrentEntityManager();
        entityManager.getTransaction().begin();
        Schueler schuelerToBeDeleted = schuelerDao.findById(updatedSchueler.getPersonId());
        schuelerDao.remove(schuelerToBeDeleted);
        entityManager.getTransaction().commit();
    }
}