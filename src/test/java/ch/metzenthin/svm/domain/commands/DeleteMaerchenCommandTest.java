package ch.metzenthin.svm.domain.commands;

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.entities.Maerchen;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Martin Schraner
 */
public class DeleteMaerchenCommandTest {

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

    List<Maerchen> maerchensSaved = new ArrayList<>();

    // 2 Maerchen erfassen
    Maerchen maerchen1 = new Maerchen("1912/1913", "Schneewittchen", 7);
    Maerchen maerchen2 = new Maerchen("1911/1912", "Hans im Glück", 8);
    SaveOrUpdateMaerchenCommand saveOrUpdateMaerchenCommand =
        new SaveOrUpdateMaerchenCommand(maerchen1, null, maerchensSaved);
    commandInvoker.executeCommandAsTransaction(saveOrUpdateMaerchenCommand);

    saveOrUpdateMaerchenCommand = new SaveOrUpdateMaerchenCommand(maerchen2, null, maerchensSaved);
    commandInvoker.executeCommandAsTransaction(saveOrUpdateMaerchenCommand);

    // Maerchens löschen
    DeleteMaerchenCommand deleteMaerchenCommand = new DeleteMaerchenCommand(maerchensSaved, 1);
    commandInvoker.executeCommandAsTransaction(deleteMaerchenCommand);
    assertEquals(
        DeleteMaerchenCommand.Result.LOESCHEN_ERFOLGREICH, deleteMaerchenCommand.getResult());
    assertEquals(1, maerchensSaved.size());

    deleteMaerchenCommand = new DeleteMaerchenCommand(maerchensSaved, 0);
    commandInvoker.executeCommandAsTransaction(deleteMaerchenCommand);
    assertEquals(
        DeleteMaerchenCommand.Result.LOESCHEN_ERFOLGREICH, deleteMaerchenCommand.getResult());
    assertTrue(maerchensSaved.isEmpty());
  }
}
