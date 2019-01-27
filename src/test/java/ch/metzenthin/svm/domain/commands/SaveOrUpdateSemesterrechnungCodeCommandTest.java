package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.daos.SemesterrechnungCodeDao;
import ch.metzenthin.svm.persistence.entities.SemesterrechnungCode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;
import static org.junit.Assert.*;

/**
 * @author Martin Schraner
 */
public class SaveOrUpdateSemesterrechnungCodeCommandTest {

    private DB db;
    private CommandInvoker commandInvoker;

    @Before
    public void setUp() throws Exception {
        createSvmPropertiesFileDefault();
        db = DBFactory.getInstance();
        commandInvoker = new CommandInvokerImpl();
    }

    @After
    public void tearDown() throws Exception {
        db.closeSession();
    }

    @Test
    public void testExecute() throws Exception {

        // Vor Transaktionen
        assertFalse(checkIfCodeAvailable("2t", "Handrechnung Test"));
        assertFalse(checkIfCodeAvailable("1t", "Stipendium Test"));

        List<SemesterrechnungCode> codesSaved = new ArrayList<>();

        // SemesterrechnungCode hinzufügen
        SemesterrechnungCode semesterrechnungCode1 = new SemesterrechnungCode("2t", "Handrechnung Test", true);
        SaveOrUpdateSemesterrechnungCodeCommand saveOrUpdateSemesterrechnungCodeCommand = new SaveOrUpdateSemesterrechnungCodeCommand(semesterrechnungCode1, null, codesSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateSemesterrechnungCodeCommand);
        assertEquals(1, codesSaved.size());
        assertTrue(checkIfCodeAvailable("2t", "Handrechnung Test"));
        assertFalse(checkIfCodeAvailable("1t", "Stipendium Test"));

        // Weiteren SemesterrechnungCode hinzufügen
        SemesterrechnungCode semesterrechnungCode2 = new SemesterrechnungCode("1t", "Stipendium Test", true);
        saveOrUpdateSemesterrechnungCodeCommand = new SaveOrUpdateSemesterrechnungCodeCommand(semesterrechnungCode2, null, codesSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateSemesterrechnungCodeCommand);
        assertEquals(2, codesSaved.size());
        // Alphabetisch geordnet?
        assertEquals("1t", codesSaved.get(0).getKuerzel());
        assertEquals("2t", codesSaved.get(1).getKuerzel());
        assertTrue(checkIfCodeAvailable("2t", "Handrechnung Test"));
        assertTrue(checkIfCodeAvailable("1t", "Stipendium Test"));

        // SemesterrechnungCode bearbeiten
        SemesterrechnungCode semesterrechnungCode2Modif = new SemesterrechnungCode("2t", "HandrechnungModif Test", true);
        saveOrUpdateSemesterrechnungCodeCommand = new SaveOrUpdateSemesterrechnungCodeCommand(semesterrechnungCode2Modif, semesterrechnungCode2, codesSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateSemesterrechnungCodeCommand);
        assertEquals(2, codesSaved.size());
        assertTrue(checkIfCodeAvailable("2t", "Handrechnung Test"));
        assertFalse(checkIfCodeAvailable("1t", "Stipendium Test"));
        assertTrue(checkIfCodeAvailable("2t", "HandrechnungModif Test"));

        // Testdaten löschen
        EntityManager entityManager = db.getCurrentEntityManager();
        entityManager.getTransaction().begin();
        SemesterrechnungCodeDao semesterrechnungCodeDao = new SemesterrechnungCodeDao(entityManager);
        for (SemesterrechnungCode semesterrechnungCode : codesSaved) {
            SemesterrechnungCode semesterrechnungCodeToBeDeleted = semesterrechnungCodeDao.findById(semesterrechnungCode.getCodeId());
            if (semesterrechnungCodeToBeDeleted != null) {
                semesterrechnungCodeDao.remove(semesterrechnungCodeToBeDeleted);
            }
        }
        entityManager.getTransaction().commit();
        db.closeSession();
    }

    private boolean checkIfCodeAvailable(String kuerzel, String beschreibung) {
        FindAllSemesterrechnungCodesCommand findAllSemesterrechnungCodesCommand = new FindAllSemesterrechnungCodesCommand();
        commandInvoker.executeCommand(findAllSemesterrechnungCodesCommand);
        List<SemesterrechnungCode> codesAll = findAllSemesterrechnungCodesCommand.getSemesterrechnungCodesAll();
        for (SemesterrechnungCode semesterrechnungCode : codesAll) {
            if (semesterrechnungCode.getKuerzel().equals(kuerzel) && semesterrechnungCode.getBeschreibung().equals(beschreibung)) {
                return true;
            }
        }
        return false;
    }
}