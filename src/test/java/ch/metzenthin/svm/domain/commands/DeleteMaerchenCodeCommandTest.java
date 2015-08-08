package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.entities.MaerchenCode;
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
public class DeleteMaerchenCodeCommandTest {

    private CommandInvoker commandInvoker = new CommandInvokerImpl();
    private EntityManagerFactory entityManagerFactory;

    @Before
    public void setUp() throws Exception {
        commandInvoker.openSession();
        entityManagerFactory = Persistence.createEntityManagerFactory("svm");
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

        List<MaerchenCode> codesSaved = new ArrayList<>();

        // 2 Codes erfassen und den zweiten einem Maerchen hinzufügen
        MaerchenCode maerchenCode1 = new MaerchenCode("kt", "KuchenTest");
        MaerchenCode maerchenCode2 = new MaerchenCode("ft", "FrisierenTest");

        SaveOrUpdateMaerchenCodeCommand saveOrUpdateMaerchenCodeCommand = new SaveOrUpdateMaerchenCodeCommand(maerchenCode1, null, codesSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateMaerchenCodeCommand);

        saveOrUpdateMaerchenCodeCommand = new SaveOrUpdateMaerchenCodeCommand(maerchenCode2, null, codesSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateMaerchenCodeCommand);

        assertEquals(2, codesSaved.size());
        assertEquals("ft", codesSaved.get(0).getKuerzel()); // alphabetisch geordnet
        assertEquals("kt", codesSaved.get(1).getKuerzel());

        // Codes löschen
        DeleteMaerchenCodeCommand deleteMaerchenCodeCommand = new DeleteMaerchenCodeCommand(codesSaved, 1);
        commandInvoker.executeCommandAsTransaction(deleteMaerchenCodeCommand);
        assertEquals(DeleteMaerchenCodeCommand.Result.LOESCHEN_ERFOLGREICH, deleteMaerchenCodeCommand.getResult());
        assertEquals(1, codesSaved.size());

        deleteMaerchenCodeCommand = new DeleteMaerchenCodeCommand(codesSaved, 0);
        commandInvoker.executeCommandAsTransaction(deleteMaerchenCodeCommand);
        assertEquals(DeleteMaerchenCodeCommand.Result.LOESCHEN_ERFOLGREICH, deleteMaerchenCodeCommand.getResult());
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