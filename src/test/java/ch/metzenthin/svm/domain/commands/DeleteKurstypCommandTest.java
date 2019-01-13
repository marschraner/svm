package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.utils.PersistenceProperties;
import ch.metzenthin.svm.persistence.entities.Kurstyp;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * @author Martin Schraner
 */
public class DeleteKurstypCommandTest {

    private CommandInvoker commandInvoker = new CommandInvokerImpl();
    private EntityManagerFactory entityManagerFactory;

    @Before
    public void setUp() throws Exception {
        createSvmPropertiesFileDefault();
        entityManagerFactory = Persistence.createEntityManagerFactory("svm", PersistenceProperties.getPersistenceProperties());
    }

    @After
    public void tearDown() throws Exception {
        commandInvoker.close();
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }

    @Test
    public void testExecute() throws Exception {

        List<Kurstyp> kurstypenSaved = new ArrayList<>();

        // 2 Kurstypen erfassen
        Kurstyp kurstyp1 = new Kurstyp("Saal Test1", true);
        Kurstyp kurstyp2 = new Kurstyp("Saal Test2", true);

        SaveOrUpdateKurstypCommand saveOrUpdateKurstypCommand = new SaveOrUpdateKurstypCommand(kurstyp1, null, kurstypenSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateKurstypCommand);

        saveOrUpdateKurstypCommand = new SaveOrUpdateKurstypCommand(kurstyp2, null, kurstypenSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateKurstypCommand);

        // Kurstypen löschen
        DeleteKurstypCommand deleteKurstypCommand = new DeleteKurstypCommand(kurstypenSaved, 1);
        commandInvoker.executeCommandAsTransaction(deleteKurstypCommand);
        assertEquals(DeleteKurstypCommand.Result.LOESCHEN_ERFOLGREICH, deleteKurstypCommand.getResult());
        assertEquals(1, kurstypenSaved.size());

        deleteKurstypCommand = new DeleteKurstypCommand(kurstypenSaved, 0);
        commandInvoker.executeCommandAsTransaction(deleteKurstypCommand);
        assertEquals(DeleteKurstypCommand.Result.LOESCHEN_ERFOLGREICH, deleteKurstypCommand.getResult());
        assertTrue(kurstypenSaved.isEmpty());

    }
}