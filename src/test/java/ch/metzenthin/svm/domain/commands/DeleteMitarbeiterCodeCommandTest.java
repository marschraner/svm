package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Anrede;
import ch.metzenthin.svm.persistence.daos.MitarbeiterDao;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;
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
import static org.junit.Assert.assertTrue;

/**
 * @author Martin Schraner
 */
public class DeleteMitarbeiterCodeCommandTest {

    private CommandInvoker commandInvoker = new CommandInvokerImpl();

    @Before
    public void setUp() throws Exception {
        createSvmPropertiesFileDefault();
    }

    @After
    public void tearDown() throws Exception {
        commandInvoker.closeSessionAndEntityManagerFactory();
    }

    @Test
    public void testExecute() throws Exception {

        List<MitarbeiterCode> codesSaved = new ArrayList<>();
        List<Mitarbeiter> mitarbeitersSaved = new ArrayList<>();

        // 2 Codes erfassen und den zweiten einem Mitarbeiter hinzufügen
        MitarbeiterCode mitarbeiterCode1 = new MitarbeiterCode("zt", "ZirkusTest", true);
        MitarbeiterCode mitarbeiterCode2 = new MitarbeiterCode("jt", "JugendprojektTest", true);

        SaveOrUpdateMitarbeiterCodeCommand saveOrUpdateMitarbeiterCodeCommand = new SaveOrUpdateMitarbeiterCodeCommand(mitarbeiterCode1, null, codesSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateMitarbeiterCodeCommand);

        saveOrUpdateMitarbeiterCodeCommand = new SaveOrUpdateMitarbeiterCodeCommand(mitarbeiterCode2, null, codesSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateMitarbeiterCodeCommand);

        Mitarbeiter mitarbeiter = new Mitarbeiter(Anrede.FRAU, "Noémie", "Roos", new GregorianCalendar(1994, Calendar.MARCH, 18), "044 391 45 35", "076 384 45 35", "nroos@gmx.ch", "756.3943.8722.22", true, "Mi, Fr, Sa", null, true);
        Adresse adresse = new Adresse("Rebwiesenstrasse", "54", "8702", "Zollikon");
        mitarbeiter.setAdresse(adresse);

        // MitarbeiterCode hinzufügen
        mitarbeiter.addCode(mitarbeiterCode2);

        SaveOrUpdateMitarbeiterCommand saveOrUpdateMitarbeiterCommand = new SaveOrUpdateMitarbeiterCommand(mitarbeiter, adresse, null, null, mitarbeitersSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateMitarbeiterCommand);

        assertEquals(2, codesSaved.size());
        assertEquals("jt", codesSaved.get(0).getKuerzel()); // alphabetisch geordnet
        assertEquals("zt", codesSaved.get(1).getKuerzel());
        assertEquals(1, mitarbeiter.getMitarbeiterCodes().size());

        // Codes löschen
        DeleteMitarbeiterCodeCommand deleteMitarbeiterCodeCommand = new DeleteMitarbeiterCodeCommand(codesSaved, 1);
        commandInvoker.executeCommandAsTransaction(deleteMitarbeiterCodeCommand);
        assertEquals(DeleteMitarbeiterCodeCommand.Result.LOESCHEN_ERFOLGREICH, deleteMitarbeiterCodeCommand.getResult());
        assertEquals(1, codesSaved.size());

        // Kann nicht gelöscht werden, da referenziert
        deleteMitarbeiterCodeCommand = new DeleteMitarbeiterCodeCommand(codesSaved, 0);
        commandInvoker.executeCommandAsTransaction(deleteMitarbeiterCodeCommand);
        assertEquals(DeleteMitarbeiterCodeCommand.Result.CODE_VON_MITARBEITER_REFERENZIERT, deleteMitarbeiterCodeCommand.getResult());
        assertEquals(1, codesSaved.size());

        // MitarbeiterCode vom Schüler entfernen, jetzt löschen möglich
        RemoveMitarbeiterCodeFromMitarbeiterCommand removeMitarbeiterCodeFromMitarbeiterCommand = new RemoveMitarbeiterCodeFromMitarbeiterCommand(codesSaved.get(0), mitarbeiter);
        commandInvoker.executeCommandAsTransaction(removeMitarbeiterCodeFromMitarbeiterCommand);
        deleteMitarbeiterCodeCommand = new DeleteMitarbeiterCodeCommand(codesSaved, 0);
        commandInvoker.executeCommandAsTransaction(deleteMitarbeiterCodeCommand);
        assertEquals(DeleteMitarbeiterCodeCommand.Result.LOESCHEN_ERFOLGREICH, deleteMitarbeiterCodeCommand.getResult());
        assertTrue(codesSaved.isEmpty());

        // Testdaten löschen
        EntityManager entityManager = commandInvoker.getEntityManager();
        entityManager.getTransaction().begin();
        MitarbeiterDao mitarbeiterDao = new MitarbeiterDao(entityManager);
        for (Mitarbeiter mitarbeiter1 : mitarbeitersSaved) {
            Mitarbeiter mitarbeiterToBeDeleted = mitarbeiterDao.findById(mitarbeiter1.getPersonId());
            if (mitarbeiterToBeDeleted != null) {
                mitarbeiterDao.remove(mitarbeiterToBeDeleted);
            }
        }
        entityManager.getTransaction().commit();
    }
}