package ch.metzenthin.svm.domain.commands;

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;
import static org.junit.Assert.*;

import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.daos.SemesterrechnungCodeDao;
import ch.metzenthin.svm.persistence.entities.SemesterrechnungCode;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Martin Schraner
 */
public class SaveOrUpdateSemesterrechnungCodeCommandTest {

  private final SemesterrechnungCodeDao semesterrechnungCodeDao = new SemesterrechnungCodeDao();

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

    // Vor Transaktionen
    assertFalse(checkIfCodeAvailable("2t", "Handrechnung Test"));
    assertFalse(checkIfCodeAvailable("1t", "Stipendium Test"));

    List<SemesterrechnungCode> codesSaved = new ArrayList<>();

    // SemesterrechnungCode hinzufügen
    SemesterrechnungCode semesterrechnungCode1 =
        new SemesterrechnungCode("2t", "Handrechnung Test", true);
    SaveOrUpdateSemesterrechnungCodeCommand saveOrUpdateSemesterrechnungCodeCommand =
        new SaveOrUpdateSemesterrechnungCodeCommand(semesterrechnungCode1, null, codesSaved);
    commandInvoker.executeCommandAsTransaction(saveOrUpdateSemesterrechnungCodeCommand);
    assertEquals(1, codesSaved.size());
    assertTrue(checkIfCodeAvailable("2t", "Handrechnung Test"));
    assertFalse(checkIfCodeAvailable("1t", "Stipendium Test"));

    // Weiteren SemesterrechnungCode hinzufügen
    SemesterrechnungCode semesterrechnungCode2 =
        new SemesterrechnungCode("1t", "Stipendium Test", true);
    saveOrUpdateSemesterrechnungCodeCommand =
        new SaveOrUpdateSemesterrechnungCodeCommand(semesterrechnungCode2, null, codesSaved);
    commandInvoker.executeCommandAsTransaction(saveOrUpdateSemesterrechnungCodeCommand);
    assertEquals(2, codesSaved.size());
    // Alphabetisch geordnet?
    assertEquals("1t", codesSaved.get(0).getKuerzel());
    assertEquals("2t", codesSaved.get(1).getKuerzel());
    assertTrue(checkIfCodeAvailable("2t", "Handrechnung Test"));
    assertTrue(checkIfCodeAvailable("1t", "Stipendium Test"));

    // SemesterrechnungCode bearbeiten
    SemesterrechnungCode semesterrechnungCode2Modif =
        new SemesterrechnungCode("2t", "HandrechnungModif Test", true);
    saveOrUpdateSemesterrechnungCodeCommand =
        new SaveOrUpdateSemesterrechnungCodeCommand(
            semesterrechnungCode2Modif, semesterrechnungCode2, codesSaved);
    commandInvoker.executeCommandAsTransaction(saveOrUpdateSemesterrechnungCodeCommand);
    assertEquals(2, codesSaved.size());
    assertTrue(checkIfCodeAvailable("2t", "Handrechnung Test"));
    assertFalse(checkIfCodeAvailable("1t", "Stipendium Test"));
    assertTrue(checkIfCodeAvailable("2t", "HandrechnungModif Test"));

    // Testdaten löschen
    EntityManager entityManager = db.getCurrentEntityManager();
    entityManager.getTransaction().begin();
    for (SemesterrechnungCode semesterrechnungCode : codesSaved) {
      SemesterrechnungCode semesterrechnungCodeToBeDeleted =
          semesterrechnungCodeDao.findById(semesterrechnungCode.getCodeId());
      if (semesterrechnungCodeToBeDeleted != null) {
        semesterrechnungCodeDao.remove(semesterrechnungCodeToBeDeleted);
      }
    }
    entityManager.getTransaction().commit();
    db.closeSession();
  }

  private boolean checkIfCodeAvailable(String kuerzel, String beschreibung) {
    FindAllSemesterrechnungCodesCommand findAllSemesterrechnungCodesCommand =
        new FindAllSemesterrechnungCodesCommand();
    commandInvoker.executeCommand(findAllSemesterrechnungCodesCommand);
    List<SemesterrechnungCode> codesAll =
        findAllSemesterrechnungCodesCommand.getSemesterrechnungCodesAll();
    for (SemesterrechnungCode semesterrechnungCode : codesAll) {
      if (semesterrechnungCode.getKuerzel().equals(kuerzel)
          && semesterrechnungCode.getBeschreibung().equals(beschreibung)) {
        return true;
      }
    }
    return false;
  }
}
