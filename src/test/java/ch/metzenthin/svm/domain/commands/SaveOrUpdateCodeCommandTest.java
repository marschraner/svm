package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.SvmDbException;
import ch.metzenthin.svm.persistence.daos.CodeDao;
import ch.metzenthin.svm.persistence.entities.Code;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Martin Schraner
 */
public class SaveOrUpdateCodeCommandTest {

    private CommandInvoker commandInvoker = new CommandInvokerImpl();
    private EntityManagerFactory entityManagerFactory;

    @Before
    public void setUp() throws Exception {
        commandInvoker.openSession();
        entityManagerFactory = Persistence.createEntityManagerFactory("svm");
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

        // Vor Transaktionen
        assertFalse(checkIfCodeAvailable("tz", "ZirkusTest"));
        assertFalse(checkIfCodeAvailable("tj", "JugendprojektTest"));

        List<Code> codesSaved = new ArrayList<>();

        // Code hinzufügen
        Code code1 = new Code("tz", "ZirkusTest");
        SaveOrUpdateCodeCommand saveOrUpdateCodeCommand = new SaveOrUpdateCodeCommand(code1, null, codesSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateCodeCommand);
        assertEquals(1, codesSaved.size());
        assertTrue(checkIfCodeAvailable("tz", "ZirkusTest"));
        assertFalse(checkIfCodeAvailable("tj", "JugendprojektTest"));

        // Weiteren Code hinzufügen
        Code code2 = new Code("tj", "JugendprojektTest");
        saveOrUpdateCodeCommand = new SaveOrUpdateCodeCommand(code2, null, codesSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateCodeCommand);
        assertEquals(2, codesSaved.size());
        // Alphabetisch geordnet?
        assertEquals("tj", codesSaved.get(0).getKuerzel());
        assertEquals("tz", codesSaved.get(1).getKuerzel());
        assertTrue(checkIfCodeAvailable("tz", "ZirkusTest"));
        assertTrue(checkIfCodeAvailable("tj", "JugendprojektTest"));

        // Code bearbeiten
        Code code2Modif = new Code("tj", "JugendprojektModifTest");
        saveOrUpdateCodeCommand = new SaveOrUpdateCodeCommand(code2Modif, code2, codesSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateCodeCommand);
        assertEquals(2, codesSaved.size());
        assertTrue(checkIfCodeAvailable("tz", "ZirkusTest"));
        assertFalse(checkIfCodeAvailable("tj", "JugendprojektTest"));
        assertTrue(checkIfCodeAvailable("tj", "JugendprojektModifTest"));

        // Testdaten löschen
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            CodeDao codeDao = new CodeDao(entityManager);
            for (Code code : codesSaved) {
                Code codeToBeDeleted = codeDao.findById(code.getCodeId());
                if (codeToBeDeleted != null) {
                    codeDao.remove(codeToBeDeleted);
                }
            }
            entityManager.getTransaction().commit();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }


    }

    private boolean checkIfCodeAvailable(String kuerzel, String beschreibung) throws SvmDbException {
        FindAllCodesCommand findAllCodesCommand = new FindAllCodesCommand();
        commandInvoker.executeCommandAsTransactionWithOpenAndClose(findAllCodesCommand);
        List<Code> codesAll = findAllCodesCommand.getCodesAll();
        for (Code code : codesAll) {
            if (code.getKuerzel().equals(kuerzel) && code.getBeschreibung().equals(beschreibung)) {
                return true;
            }
        }
        return false;
    }
}