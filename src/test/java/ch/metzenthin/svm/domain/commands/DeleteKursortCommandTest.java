package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.entities.Kursort;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * @author Martin Schraner
 */
public class DeleteKursortCommandTest {

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