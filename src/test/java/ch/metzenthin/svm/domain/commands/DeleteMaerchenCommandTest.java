package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.entities.Maerchen;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * @author Martin Schraner
 */
public class DeleteMaerchenCommandTest {

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

        List<Maerchen> maerchensSaved = new ArrayList<>();

        // 2 Maerchen erfassen
        Maerchen maerchen1 = new Maerchen("1912/1913", "Schneewittchen", 7);
        Maerchen maerchen2 = new Maerchen("1911/1912", "Hans im Glück", 8);
        SaveOrUpdateMaerchenCommand saveOrUpdateMaerchenCommand = new SaveOrUpdateMaerchenCommand(maerchen1, null, maerchensSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateMaerchenCommand);

        saveOrUpdateMaerchenCommand = new SaveOrUpdateMaerchenCommand(maerchen2, null, maerchensSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateMaerchenCommand);

        // Maerchens löschen
        DeleteMaerchenCommand deleteMaerchenCommand = new DeleteMaerchenCommand(maerchensSaved, 1);
        commandInvoker.executeCommandAsTransaction(deleteMaerchenCommand);
        assertEquals(DeleteMaerchenCommand.Result.LOESCHEN_ERFOLGREICH, deleteMaerchenCommand.getResult());
        assertEquals(1, maerchensSaved.size());

        deleteMaerchenCommand = new DeleteMaerchenCommand(maerchensSaved, 0);
        commandInvoker.executeCommandAsTransaction(deleteMaerchenCommand);
        assertEquals(DeleteMaerchenCommand.Result.LOESCHEN_ERFOLGREICH, deleteMaerchenCommand.getResult());
        assertTrue(maerchensSaved.isEmpty());

    }
}