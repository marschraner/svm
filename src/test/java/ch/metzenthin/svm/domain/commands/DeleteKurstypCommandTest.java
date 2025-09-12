package ch.metzenthin.svm.domain.commands;

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.entities.Kurstyp;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Martin Schraner
 */
public class DeleteKurstypCommandTest {

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

    List<Kurstyp> kurstypenSaved = new ArrayList<>();

    // 2 Kurstypen erfassen
    Kurstyp kurstyp1 = new Kurstyp("Saal Test1", true);
    Kurstyp kurstyp2 = new Kurstyp("Saal Test2", true);

    SaveOrUpdateKurstypCommand saveOrUpdateKurstypCommand =
        new SaveOrUpdateKurstypCommand(kurstyp1, null, kurstypenSaved);
    commandInvoker.executeCommandAsTransaction(saveOrUpdateKurstypCommand);

    saveOrUpdateKurstypCommand = new SaveOrUpdateKurstypCommand(kurstyp2, null, kurstypenSaved);
    commandInvoker.executeCommandAsTransaction(saveOrUpdateKurstypCommand);

    // Kurstypen löschen
    DeleteKurstypCommand deleteKurstypCommand = new DeleteKurstypCommand(kurstypenSaved, 1);
    commandInvoker.executeCommandAsTransaction(deleteKurstypCommand);
    assertEquals(
        DeleteKurstypCommand.Result.LOESCHEN_ERFOLGREICH, deleteKurstypCommand.getResult());
    assertEquals(1, kurstypenSaved.size());

    deleteKurstypCommand = new DeleteKurstypCommand(kurstypenSaved, 0);
    commandInvoker.executeCommandAsTransaction(deleteKurstypCommand);
    assertEquals(
        DeleteKurstypCommand.Result.LOESCHEN_ERFOLGREICH, deleteKurstypCommand.getResult());
    assertTrue(kurstypenSaved.isEmpty());
  }
}
