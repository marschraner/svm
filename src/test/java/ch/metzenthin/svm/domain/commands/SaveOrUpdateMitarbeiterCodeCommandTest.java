package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.daos.MitarbeiterCodeDao;
import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;
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
public class SaveOrUpdateMitarbeiterCodeCommandTest {

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
        assertFalse(checkIfCodeAvailable("tv", "VertretungTest"));
        assertFalse(checkIfCodeAvailable("th", "HelferTest"));

        List<MitarbeiterCode> codesSaved = new ArrayList<>();

        // MitarbeiterCode hinzufügen
        MitarbeiterCode mitarbeiterCode1 = new MitarbeiterCode("tv", "VertretungTest", true);
        SaveOrUpdateMitarbeiterCodeCommand saveOrUpdateMitarbeiterCodeCommand = new SaveOrUpdateMitarbeiterCodeCommand(mitarbeiterCode1, null, codesSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateMitarbeiterCodeCommand);
        assertEquals(1, codesSaved.size());
        assertTrue(checkIfCodeAvailable("tv", "VertretungTest"));
        assertFalse(checkIfCodeAvailable("th", "HelferTest"));

        // Weiteren MitarbeiterCode hinzufügen
        MitarbeiterCode mitarbeiterCode2 = new MitarbeiterCode("th", "HelferTest", true);
        saveOrUpdateMitarbeiterCodeCommand = new SaveOrUpdateMitarbeiterCodeCommand(mitarbeiterCode2, null, codesSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateMitarbeiterCodeCommand);
        assertEquals(2, codesSaved.size());
        // Alphabetisch geordnet?
        assertEquals("th", codesSaved.get(0).getKuerzel());
        assertEquals("tv", codesSaved.get(1).getKuerzel());
        assertTrue(checkIfCodeAvailable("tv", "VertretungTest"));
        assertTrue(checkIfCodeAvailable("th", "HelferTest"));

        // MitarbeiterCode bearbeiten
        MitarbeiterCode mitarbeiterCode2Modif = new MitarbeiterCode("th", "HelferModifTest", true);
        saveOrUpdateMitarbeiterCodeCommand = new SaveOrUpdateMitarbeiterCodeCommand(mitarbeiterCode2Modif, mitarbeiterCode2, codesSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateMitarbeiterCodeCommand);
        assertEquals(2, codesSaved.size());
        assertTrue(checkIfCodeAvailable("tv", "VertretungTest"));
        assertFalse(checkIfCodeAvailable("th", "HelferTest"));
        assertTrue(checkIfCodeAvailable("th", "HelferModifTest"));

        // Testdaten löschen
        EntityManager entityManager = db.getCurrentEntityManager();
        entityManager.getTransaction().begin();
        MitarbeiterCodeDao mitarbeiterCodeDao = new MitarbeiterCodeDao(entityManager);
        for (MitarbeiterCode mitarbeiterCode : codesSaved) {
            MitarbeiterCode mitarbeiterCodeToBeDeleted = mitarbeiterCodeDao.findById(mitarbeiterCode.getCodeId());
            if (mitarbeiterCodeToBeDeleted != null) {
                mitarbeiterCodeDao.remove(mitarbeiterCodeToBeDeleted);
            }
        }
        entityManager.getTransaction().commit();
    }

    private boolean checkIfCodeAvailable(String kuerzel, String beschreibung) {
        FindAllMitarbeiterCodesCommand findAllMitarbeiterCodesCommand = new FindAllMitarbeiterCodesCommand();
        commandInvoker.executeCommand(findAllMitarbeiterCodesCommand);
        List<MitarbeiterCode> codesAll = findAllMitarbeiterCodesCommand.getMitarbeiterCodesAll();
        for (MitarbeiterCode mitarbeiterCode : codesAll) {
            if (mitarbeiterCode.getKuerzel().equals(kuerzel) && mitarbeiterCode.getBeschreibung().equals(beschreibung)) {
                return true;
            }
        }
        return false;
    }
}