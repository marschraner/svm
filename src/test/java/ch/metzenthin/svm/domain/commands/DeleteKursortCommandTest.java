package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.entities.Kursort;
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
public class DeleteKursortCommandTest {

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

        List<Kursort> kursorteSaved = new ArrayList<>();

        // 2 Kursorte erfassen
        Kursort kursort1 = new Kursort("Saal Test1", true);
        Kursort kursort2 = new Kursort("Saal Test2", true);

        SaveOrUpdateKursortCommand saveOrUpdateKursortCommand = new SaveOrUpdateKursortCommand(kursort1, null, kursorteSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateKursortCommand);

        saveOrUpdateKursortCommand = new SaveOrUpdateKursortCommand(kursort2, null, kursorteSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateKursortCommand);

        // Kursorte löschen
        DeleteKursortCommand deleteKursortCommand = new DeleteKursortCommand(kursorteSaved, 1);
        commandInvoker.executeCommandAsTransaction(deleteKursortCommand);
        assertEquals(DeleteKursortCommand.Result.LOESCHEN_ERFOLGREICH, deleteKursortCommand.getResult());
        assertEquals(1, kursorteSaved.size());

        deleteKursortCommand = new DeleteKursortCommand(kursorteSaved, 0);
        commandInvoker.executeCommandAsTransaction(deleteKursortCommand);
        assertEquals(DeleteKursortCommand.Result.LOESCHEN_ERFOLGREICH, deleteKursortCommand.getResult());
        assertTrue(kursorteSaved.isEmpty());

    }
}