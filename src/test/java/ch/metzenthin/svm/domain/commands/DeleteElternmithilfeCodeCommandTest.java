package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.entities.ElternmithilfeCode;
import jakarta.persistence.EntityManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Martin Schraner
 */
public class DeleteElternmithilfeCodeCommandTest {

    private DB db;
    private CommandInvoker commandInvoker;

    @Before
    public void setUp() {
        createSvmPropertiesFileDefault();
        db = DBFactory.getInstance();
        commandInvoker = new CommandInvokerImpl();
    }

    @After
    public void tearDown() {
        db.closeSession();
    }

    @Test
    public void testExecute() {

        List<ElternmithilfeCode> codesSaved = new ArrayList<>();

        // 2 Codes erfassen
        ElternmithilfeCode elternmithilfeCode1 = new ElternmithilfeCode("kt", "KuchenTest", true);
        ElternmithilfeCode elternmithilfeCode2 = new ElternmithilfeCode("ft", "FrisierenTest", true);

        SaveOrUpdateElternmithilfeCodeCommand saveOrUpdateElternmithilfeCodeCommand = new SaveOrUpdateElternmithilfeCodeCommand(elternmithilfeCode1, null, codesSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateElternmithilfeCodeCommand);

        saveOrUpdateElternmithilfeCodeCommand = new SaveOrUpdateElternmithilfeCodeCommand(elternmithilfeCode2, null, codesSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateElternmithilfeCodeCommand);

        assertEquals(2, codesSaved.size());
        assertEquals("ft", codesSaved.get(0).getKuerzel()); // alphabetisch geordnet
        assertEquals("kt", codesSaved.get(1).getKuerzel());

        // Codes löschen
        DeleteElternmithilfeCodeCommand deleteElternmithilfeCodeCommand = new DeleteElternmithilfeCodeCommand(codesSaved, 1);
        commandInvoker.executeCommandAsTransaction(deleteElternmithilfeCodeCommand);
        assertEquals(DeleteElternmithilfeCodeCommand.Result.LOESCHEN_ERFOLGREICH, deleteElternmithilfeCodeCommand.getResult());
        assertEquals(1, codesSaved.size());

        deleteElternmithilfeCodeCommand = new DeleteElternmithilfeCodeCommand(codesSaved, 0);
        commandInvoker.executeCommandAsTransaction(deleteElternmithilfeCodeCommand);
        assertEquals(DeleteElternmithilfeCodeCommand.Result.LOESCHEN_ERFOLGREICH, deleteElternmithilfeCodeCommand.getResult());
        assertTrue(codesSaved.isEmpty());

        // Testdaten löschen
        EntityManager entityManager = db.getCurrentEntityManager();
        entityManager.getTransaction().begin();
        entityManager.getTransaction().commit();
    }
}