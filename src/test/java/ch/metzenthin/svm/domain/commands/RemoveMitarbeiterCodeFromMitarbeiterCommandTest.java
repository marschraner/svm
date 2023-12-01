package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Anrede;
import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.daos.MitarbeiterCodeDao;
import ch.metzenthin.svm.persistence.daos.MitarbeiterDao;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;
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
public class RemoveMitarbeiterCodeFromMitarbeiterCommandTest {

    private final MitarbeiterDao mitarbeiterDao = new MitarbeiterDao();
    private final MitarbeiterCodeDao mitarbeiterCodeDao = new MitarbeiterCodeDao();

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

        List<MitarbeiterCode> erfassteMitarbeiterCodes = new ArrayList<>();
        List<Mitarbeiter> mitarbeitersSaved = new ArrayList<>();

        // Codes erzeugen
        MitarbeiterCode mitarbeiterCode1 = new MitarbeiterCode("vt", "VertretungTest", true);
        MitarbeiterCode mitarbeiterCode2 = new MitarbeiterCode("ht", "HelferTest", true);
        SaveOrUpdateMitarbeiterCodeCommand saveOrUpdateMitarbeiterCodeCommand = new SaveOrUpdateMitarbeiterCodeCommand(mitarbeiterCode1, null, erfassteMitarbeiterCodes);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateMitarbeiterCodeCommand);
        saveOrUpdateMitarbeiterCodeCommand = new SaveOrUpdateMitarbeiterCodeCommand(mitarbeiterCode2, null, erfassteMitarbeiterCodes);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateMitarbeiterCodeCommand);

        // Mitarbeiter erfassen und Codes hinzufügen
        Mitarbeiter mitarbeiter = new Mitarbeiter(Anrede.FRAU, "Noémie", "Roos", new GregorianCalendar(1994, Calendar.MARCH, 18), "044 391 45 35", "076 384 45 35", "nroos@gmx.ch", "756.3943.8722.22", "CH31 8123 9000 0012 4568 9", true, "Mi, Fr, Sa", null, true);
        Adresse adresse = new Adresse("Rebwiesenstrasse", "54", "8702", "Zollikon");
        mitarbeiter.setAdresse(adresse);

        SaveOrUpdateMitarbeiterCommand saveOrUpdateMitarbeiterCommand = new SaveOrUpdateMitarbeiterCommand(mitarbeiter, adresse, null, null, mitarbeitersSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateMitarbeiterCommand);

        // Codes hinzufügen
        AddMitarbeiterCodeToMitarbeiterAndSaveCommand addMitarbeiterCodeToMitarbeiterAndSaveCommand = new AddMitarbeiterCodeToMitarbeiterAndSaveCommand(mitarbeiterCode1, mitarbeiter);
        commandInvoker.executeCommandAsTransaction(addMitarbeiterCodeToMitarbeiterAndSaveCommand);
        addMitarbeiterCodeToMitarbeiterAndSaveCommand = new AddMitarbeiterCodeToMitarbeiterAndSaveCommand(mitarbeiterCode2, mitarbeiter);
        commandInvoker.executeCommandAsTransaction(addMitarbeiterCodeToMitarbeiterAndSaveCommand);
        Mitarbeiter mitarbeiterUpdated = addMitarbeiterCodeToMitarbeiterAndSaveCommand.getMitarbeiterUpdated();

        assertEquals(2, mitarbeiterUpdated.getMitarbeiterCodes().size());
        assertEquals("ht", mitarbeiterUpdated.getMitarbeiterCodesAsList().get(0).getKuerzel());
        assertEquals("vt", mitarbeiterUpdated.getMitarbeiterCodesAsList().get(1).getKuerzel());

        // 2. MitarbeiterCode von Schüler löschen
        RemoveMitarbeiterCodeFromMitarbeiterCommand removeMitarbeiterCodeFromMitarbeiterCommand = new RemoveMitarbeiterCodeFromMitarbeiterCommand(mitarbeiterUpdated.getMitarbeiterCodesAsList().get(1), mitarbeiterUpdated);
        commandInvoker.executeCommandAsTransaction(removeMitarbeiterCodeFromMitarbeiterCommand);

        mitarbeiterUpdated = removeMitarbeiterCodeFromMitarbeiterCommand.getMitarbeiterUpdated();
        assertEquals(1, mitarbeiterUpdated.getMitarbeiterCodes().size());
        assertEquals("ht", mitarbeiterUpdated.getMitarbeiterCodesAsList().get(0).getKuerzel());

        // 1. MitarbeiterCode von Schüler löschen
        removeMitarbeiterCodeFromMitarbeiterCommand = new RemoveMitarbeiterCodeFromMitarbeiterCommand(mitarbeiterUpdated.getMitarbeiterCodesAsList().get(0), mitarbeiterUpdated);
        commandInvoker.executeCommandAsTransaction(removeMitarbeiterCodeFromMitarbeiterCommand);

        mitarbeiterUpdated = removeMitarbeiterCodeFromMitarbeiterCommand.getMitarbeiterUpdated();
        assertEquals(0, mitarbeiterUpdated.getMitarbeiterCodes().size());

        // Testdaten löschen
        EntityManager entityManager = db.getCurrentEntityManager();
        entityManager.getTransaction().begin();
        Mitarbeiter mitarbeiterToBeDeleted = mitarbeiterDao.findById(mitarbeiterUpdated.getPersonId());
        for (MitarbeiterCode mitarbeiterCode : erfassteMitarbeiterCodes) {
            MitarbeiterCode mitarbeiterCodeToBeDeleted = mitarbeiterCodeDao.findById(mitarbeiterCode.getCodeId());
            mitarbeiterCodeDao.removeFromMitarbeiterAndUpdate(mitarbeiterCodeToBeDeleted, mitarbeiterToBeDeleted);
            mitarbeiterCodeDao.remove(mitarbeiterCodeToBeDeleted);
        }
        mitarbeiterDao.remove(mitarbeiterToBeDeleted);
        entityManager.getTransaction().commit();
    }
}