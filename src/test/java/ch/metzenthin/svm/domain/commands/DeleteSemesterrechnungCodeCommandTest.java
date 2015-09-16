package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.entities.SemesterrechnungCode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Martin Schraner
 */
public class DeleteSemesterrechnungCodeCommandTest {

    private CommandInvoker commandInvoker = new CommandInvokerImpl();
    private EntityManagerFactory entityManagerFactory;

    @Before
    public void setUp() throws Exception {
        commandInvoker.openSessionSvmTest();
        entityManagerFactory = Persistence.createEntityManagerFactory("svmtest");
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
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.getTransaction().commit();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }

    }
}