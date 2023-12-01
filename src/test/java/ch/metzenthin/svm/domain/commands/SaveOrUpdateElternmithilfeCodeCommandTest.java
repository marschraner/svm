package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.daos.ElternmithilfeCodeDao;
import ch.metzenthin.svm.persistence.entities.ElternmithilfeCode;
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
public class SaveOrUpdateElternmithilfeCodeCommandTest {

    private final ElternmithilfeCodeDao elternmithilfeCodeDao = new ElternmithilfeCodeDao();

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
        assertFalse(checkIfCodeAvailable("tk", "KuchenTest"));
        assertFalse(checkIfCodeAvailable("tf", "FrisierenTest"));

        List<ElternmithilfeCode> codesSaved = new ArrayList<>();

        // ElternmithilfeCode hinzufügen
        ElternmithilfeCode elternmithilfeCode1 = new ElternmithilfeCode("tk", "KuchenTest", true);
        SaveOrUpdateElternmithilfeCodeCommand saveOrUpdateElternmithilfeCodeCommand = new SaveOrUpdateElternmithilfeCodeCommand(elternmithilfeCode1, null, codesSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateElternmithilfeCodeCommand);
        assertEquals(1, codesSaved.size());
        assertTrue(checkIfCodeAvailable("tk", "KuchenTest"));
        assertFalse(checkIfCodeAvailable("tf", "FrisierenTest"));

        // Weiteren ElternmithilfeCode hinzufügen
        ElternmithilfeCode elternmithilfeCode2 = new ElternmithilfeCode("tf", "FrisierenTest", true);
        saveOrUpdateElternmithilfeCodeCommand = new SaveOrUpdateElternmithilfeCodeCommand(elternmithilfeCode2, null, codesSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateElternmithilfeCodeCommand);
        assertEquals(2, codesSaved.size());
        // Alphabetisch geordnet?
        assertEquals("tf", codesSaved.get(0).getKuerzel());
        assertEquals("tk", codesSaved.get(1).getKuerzel());
        assertTrue(checkIfCodeAvailable("tk", "KuchenTest"));
        assertTrue(checkIfCodeAvailable("tf", "FrisierenTest"));

        // ElternmithilfeCode bearbeiten
        ElternmithilfeCode elternmithilfeCode2Modif = new ElternmithilfeCode("tf", "FrisierenModifTest", true);
        saveOrUpdateElternmithilfeCodeCommand = new SaveOrUpdateElternmithilfeCodeCommand(elternmithilfeCode2Modif, elternmithilfeCode2, codesSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateElternmithilfeCodeCommand);
        assertEquals(2, codesSaved.size());
        assertTrue(checkIfCodeAvailable("tk", "KuchenTest"));
        assertFalse(checkIfCodeAvailable("tf", "FrisierenTest"));
        assertTrue(checkIfCodeAvailable("tf", "FrisierenModifTest"));

        // Testdaten löschen
        EntityManager entityManager = db.getCurrentEntityManager();
        entityManager.getTransaction().begin();
        for (ElternmithilfeCode elternmithilfeCode : codesSaved) {
            ElternmithilfeCode elternmithilfeCodeToBeDeleted = elternmithilfeCodeDao.findById(elternmithilfeCode.getCodeId());
            if (elternmithilfeCodeToBeDeleted != null) {
                elternmithilfeCodeDao.remove(elternmithilfeCodeToBeDeleted);
            }
        }
        entityManager.getTransaction().commit();
    }

    private boolean checkIfCodeAvailable(String kuerzel, String beschreibung) {
        FindAllElternmithilfeCodesCommand findAllElternmithilfeCodesCommand = new FindAllElternmithilfeCodesCommand();
        commandInvoker.executeCommand(findAllElternmithilfeCodesCommand);
        List<ElternmithilfeCode> codesAll = findAllElternmithilfeCodesCommand.getElternmithilfeCodesAll();
        for (ElternmithilfeCode elternmithilfeCode : codesAll) {
            if (elternmithilfeCode.getKuerzel().equals(kuerzel) && elternmithilfeCode.getBeschreibung().equals(beschreibung)) {
                return true;
            }
        }
        return false;
    }
}