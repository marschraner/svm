package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.entities.SemesterrechnungCode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Martin Schraner
 */
public class DeleteSemesterrechnungCodeCommandTest {

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

        List<SemesterrechnungCode> codesSaved = new ArrayList<>();

        // 2 Codes erfassen
        SemesterrechnungCode semesterrechnungCode1 = new SemesterrechnungCode("2t", "Handrechnung Test", true);
        SemesterrechnungCode semesterrechnungCode2 = new SemesterrechnungCode("1t", "Stipendium Test", true);

        SaveOrUpdateSemesterrechnungCodeCommand saveOrUpdateSemesterrechnungCodeCommand = new SaveOrUpdateSemesterrechnungCodeCommand(semesterrechnungCode1, null, codesSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateSemesterrechnungCodeCommand);

        saveOrUpdateSemesterrechnungCodeCommand = new SaveOrUpdateSemesterrechnungCodeCommand(semesterrechnungCode2, null, codesSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateSemesterrechnungCodeCommand);

        assertEquals(2, codesSaved.size());
        assertEquals("1t", codesSaved.get(0).getKuerzel()); // alphabetisch geordnet
        assertEquals("2t", codesSaved.get(1).getKuerzel());

        // Codes löschen
        DeleteSemesterrechnungCodeCommand deleteSemesterrechnungCodeCommand = new DeleteSemesterrechnungCodeCommand(codesSaved, 1);
        commandInvoker.executeCommandAsTransaction(deleteSemesterrechnungCodeCommand);
        assertEquals(DeleteSemesterrechnungCodeCommand.Result.LOESCHEN_ERFOLGREICH, deleteSemesterrechnungCodeCommand.getResult());
        assertEquals(1, codesSaved.size());

        deleteSemesterrechnungCodeCommand = new DeleteSemesterrechnungCodeCommand(codesSaved, 0);
        commandInvoker.executeCommandAsTransaction(deleteSemesterrechnungCodeCommand);
        assertEquals(DeleteSemesterrechnungCodeCommand.Result.LOESCHEN_ERFOLGREICH, deleteSemesterrechnungCodeCommand.getResult());
        assertTrue(codesSaved.isEmpty());

        // Testdaten löschen
        EntityManager entityManager = db.getCurrentEntityManager();
        entityManager.getTransaction().begin();
        entityManager.getTransaction().commit();
    }
}