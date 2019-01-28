package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.daos.KursortDao;
import ch.metzenthin.svm.persistence.entities.Kursort;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;
import static org.junit.Assert.*;

/**
 * @author Martin Schraner
 */
public class SaveOrUpdateKursortCommandTest {

    private final KursortDao kursortDao = new KursortDao();

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

        // Vor Transaktionen
        assertFalse(checkIfKursortAvailable("Saal Test1"));
        assertFalse(checkIfKursortAvailable("Saal Test2"));

        List<Kursort> kursorteSaved = new ArrayList<>();

        // Kursort hinzufügen
        Kursort kursort1 = new Kursort("Saal Test1", true);
        SaveOrUpdateKursortCommand saveOrUpdateKursortCommand = new SaveOrUpdateKursortCommand(kursort1, null, kursorteSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateKursortCommand);
        assertEquals(1, kursorteSaved.size());
        assertTrue(checkIfKursortAvailable("Saal Test1"));
        assertFalse(checkIfKursortAvailable("Saal Test2"));

        // Weiteren Kursort hinzufügen
        Kursort kursort2 = new Kursort("Saal Test2", true);
        saveOrUpdateKursortCommand = new SaveOrUpdateKursortCommand(kursort2, null, kursorteSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateKursortCommand);
        assertEquals(2, kursorteSaved.size());
        // Alphabetisch geordnet?
        assertEquals("Saal Test1", kursorteSaved.get(0).getBezeichnung());
        assertEquals("Saal Test2", kursorteSaved.get(1).getBezeichnung());
        assertTrue(checkIfKursortAvailable("Saal Test1"));
        assertTrue(checkIfKursortAvailable("Saal Test2"));

        // Kursort bearbeiten
        Kursort kursort2Modif = new Kursort("Saal Test3", true);
        saveOrUpdateKursortCommand = new SaveOrUpdateKursortCommand(kursort2Modif, kursort2, kursorteSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateKursortCommand);
        assertEquals(2, kursorteSaved.size());
        assertTrue(checkIfKursortAvailable("Saal Test1"));
        assertFalse(checkIfKursortAvailable("Saal Test2"));
        assertTrue(checkIfKursortAvailable("Saal Test3"));

        // Testdaten löschen
        EntityManager entityManager = db.getCurrentEntityManager();
        entityManager.getTransaction().begin();
        for (Kursort kursort : kursorteSaved) {
            Kursort kursortToBeDeleted = kursortDao.findById(kursort.getKursortId());
            if (kursortToBeDeleted != null) {
                kursortDao.remove(kursortToBeDeleted);
            }
        }
        entityManager.getTransaction().commit();
    }

    private boolean checkIfKursortAvailable(String bezeichnung) {
        FindAllKursorteCommand findAllKursorteCommand = new FindAllKursorteCommand();
        commandInvoker.executeCommand(findAllKursorteCommand);
        List<Kursort> kursorteAll = findAllKursorteCommand.getKursorteAll();
        for (Kursort kursort : kursorteAll) {
            if (kursort.getBezeichnung().equals(bezeichnung)) {
                return true;
            }
        }
        return false;
    }
}