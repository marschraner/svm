package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Anrede;
import ch.metzenthin.svm.common.utils.PersistenceProperties;
import ch.metzenthin.svm.persistence.daos.MitarbeiterCodeDao;
import ch.metzenthin.svm.persistence.daos.MitarbeiterDao;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;
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

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;
import static org.junit.Assert.assertEquals;

/**
 * @author Martin Schraner
 */
public class AddMitarbeiterCodeToMitarbeiterAndSaveCommandTest {

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
        Mitarbeiter mitarbeiter = new Mitarbeiter(Anrede.FRAU, "Noémie", "Roos", new GregorianCalendar(1994, Calendar.MARCH, 18), "044 391 45 35", "076 384 45 35", "nroos@gmx.ch", "756.3943.8722.22", true, "Mi, Fr, Sa", null, true);
        Adresse adresse = new Adresse("Rebwiesenstrasse", "54", "8702", "Zollikon");
        mitarbeiter.setAdresse(adresse);

        SaveOrUpdateMitarbeiterCommand saveOrUpdateMitarbeiterCommand = new SaveOrUpdateMitarbeiterCommand(mitarbeiter, adresse, null, null, mitarbeitersSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateMitarbeiterCommand);

        // Codes hinzufügen
        AddMitarbeiterCodeToMitarbeiterAndSaveCommand addMitarbeiterCodeToMitarbeiterAndSaveCommand = new AddMitarbeiterCodeToMitarbeiterAndSaveCommand(mitarbeiterCode1, mitarbeiter);
        commandInvoker.executeCommandAsTransaction(addMitarbeiterCodeToMitarbeiterAndSaveCommand);
        Mitarbeiter mitarbeiterSaved = addMitarbeiterCodeToMitarbeiterAndSaveCommand.getMitarbeiterUpdated();

        assertEquals(1, mitarbeiterSaved.getMitarbeiterCodes().size());
        assertEquals("vt", mitarbeiterSaved.getMitarbeiterCodesAsList().get(0).getKuerzel());

        // Weiteren MitarbeiterCode hinzufügen:
        addMitarbeiterCodeToMitarbeiterAndSaveCommand = new AddMitarbeiterCodeToMitarbeiterAndSaveCommand(mitarbeiterCode2, mitarbeiterSaved);
        commandInvoker.executeCommandAsTransaction(addMitarbeiterCodeToMitarbeiterAndSaveCommand);
        mitarbeiterSaved = addMitarbeiterCodeToMitarbeiterAndSaveCommand.getMitarbeiterUpdated();

        assertEquals(2, mitarbeiterSaved.getMitarbeiterCodes().size());
        // Alphabetisch geordnet?
        assertEquals("ht", mitarbeiterSaved.getMitarbeiterCodesAsList().get(0).getKuerzel());
        assertEquals("vt", mitarbeiterSaved.getMitarbeiterCodesAsList().get(1).getKuerzel());

        // Testdaten löschen
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            MitarbeiterDao mitarbeiterDao = new MitarbeiterDao(entityManager);
            Mitarbeiter mitarbeiterToBeDeleted = mitarbeiterDao.findById(mitarbeiterSaved.getPersonId());
            MitarbeiterCodeDao mitarbeiterCodeDao = new MitarbeiterCodeDao(entityManager);
            for (MitarbeiterCode mitarbeiterCode : erfassteMitarbeiterCodes) {
                MitarbeiterCode mitarbeiterCodeToBeDeleted = mitarbeiterCodeDao.findById(mitarbeiterCode.getCodeId());
                mitarbeiterCodeDao.removeFromMitarbeiterAndUpdate(mitarbeiterCodeToBeDeleted, mitarbeiterToBeDeleted);
                mitarbeiterCodeDao.remove(mitarbeiterCodeToBeDeleted);
            }
            mitarbeiterDao.remove(mitarbeiterToBeDeleted);
            entityManager.getTransaction().commit();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }

    }
}