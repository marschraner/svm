package ch.metzenthin.svm.domain.commands;

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.entities.Maerchen;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Martin Schraner
 */
class DeleteMaerchenCommandTest {

  private DB db;
  private CommandInvoker commandInvoker;

  @BeforeEach
  void setUp() {
    createSvmPropertiesFileDefault();
    db = DBFactory.getInstance();
    commandInvoker = new CommandInvokerImpl();
  }

  @AfterEach
  void tearDown() {
    db.closeSession();
  }

  @Test
  void testExecute() {

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
