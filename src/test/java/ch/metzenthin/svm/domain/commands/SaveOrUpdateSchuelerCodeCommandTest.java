package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.daos.SchuelerCodeDao;
import ch.metzenthin.svm.persistence.entities.SchuelerCode;
import jakarta.persistence.EntityManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;
import static org.junit.Assert.*;

/**
 * @author Martin Schraner
 */
public class SaveOrUpdateSchuelerCodeCommandTest {

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

        // Vor Transaktionen
        assertFalse(checkIfCodeAvailable("tz", "ZirkusTest"));
        assertFalse(checkIfCodeAvailable("tj", "JugendprojektTest"));

        List<SchuelerCode> codesSaved = new ArrayList<>();

        // SchuelerCode hinzufügen
        SchuelerCode schuelerCode1 = new SchuelerCode("tz", "ZirkusTest", true);
        SaveOrUpdateSchuelerCodeCommand saveOrUpdateSchuelerCodeCommand = new SaveOrUpdateSchuelerCodeCommand(schuelerCode1, null, codesSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateSchuelerCodeCommand);
        assertEquals(1, codesSaved.size());
        assertTrue(checkIfCodeAvailable("tz", "ZirkusTest"));
        assertFalse(checkIfCodeAvailable("tj", "JugendprojektTest"));

        // Weiteren SchuelerCode hinzufügen
        SchuelerCode schuelerCode2 = new SchuelerCode("tj", "JugendprojektTest", true);
        saveOrUpdateSchuelerCodeCommand = new SaveOrUpdateSchuelerCodeCommand(schuelerCode2, null, codesSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateSchuelerCodeCommand);
        assertEquals(2, codesSaved.size());
        // Alphabetisch geordnet?
        assertEquals("tj", codesSaved.get(0).getKuerzel());
        assertEquals("tz", codesSaved.get(1).getKuerzel());
        assertTrue(checkIfCodeAvailable("tz", "ZirkusTest"));
        assertTrue(checkIfCodeAvailable("tj", "JugendprojektTest"));

        // SchuelerCode bearbeiten
        SchuelerCode schuelerCode2Modif = new SchuelerCode("tj", "JugendprojektModifTest", true);
        saveOrUpdateSchuelerCodeCommand = new SaveOrUpdateSchuelerCodeCommand(schuelerCode2Modif, schuelerCode2, codesSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateSchuelerCodeCommand);
        assertEquals(2, codesSaved.size());
        assertTrue(checkIfCodeAvailable("tz", "ZirkusTest"));
        assertFalse(checkIfCodeAvailable("tj", "JugendprojektTest"));
        assertTrue(checkIfCodeAvailable("tj", "JugendprojektModifTest"));

        // Testdaten löschen
        EntityManager entityManager = db.getCurrentEntityManager();
        entityManager.getTransaction().begin();
        for (SchuelerCode schuelerCode : codesSaved) {
            SchuelerCode schuelerCodeToBeDeleted = schuelerCodeDao.findById(schuelerCode.getCodeId());
            if (schuelerCodeToBeDeleted != null) {
                schuelerCodeDao.remove(schuelerCodeToBeDeleted);
            }
        }
        entityManager.getTransaction().commit();
    }

    private boolean checkIfCodeAvailable(String kuerzel, String beschreibung) {
        FindAllSchuelerCodesCommand findAllSchuelerCodesCommand = new FindAllSchuelerCodesCommand();
        commandInvoker.executeCommand(findAllSchuelerCodesCommand);
        List<SchuelerCode> codesAll = findAllSchuelerCodesCommand.getSchuelerCodesAll();
        for (SchuelerCode schuelerCode : codesAll) {
            if (schuelerCode.getKuerzel().equals(kuerzel) && schuelerCode.getBeschreibung().equals(beschreibung)) {
                return true;
            }
        }
        return false;
    }
}