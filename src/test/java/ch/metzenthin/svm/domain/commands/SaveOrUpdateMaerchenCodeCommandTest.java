package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.MaerchenCodeDao;
import ch.metzenthin.svm.persistence.entities.MaerchenCode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Martin Schraner
 */
public class SaveOrUpdateMaerchenCodeCommandTest {

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
        assertFalse(checkIfCodeAvailable("tk", "KuchenTest"));
        assertFalse(checkIfCodeAvailable("tf", "FrisierenTest"));

        List<MaerchenCode> codesSaved = new ArrayList<>();

        // MaerchenCode hinzufügen
        MaerchenCode maerchenCode1 = new MaerchenCode("tk", "KuchenTest");
        SaveOrUpdateMaerchenCodeCommand saveOrUpdateMaerchenCodeCommand = new SaveOrUpdateMaerchenCodeCommand(maerchenCode1, null, codesSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateMaerchenCodeCommand);
        assertEquals(1, codesSaved.size());
        assertTrue(checkIfCodeAvailable("tk", "KuchenTest"));
        assertFalse(checkIfCodeAvailable("tf", "FrisierenTest"));

        // Weiteren MaerchenCode hinzufügen
        MaerchenCode maerchenCode2 = new MaerchenCode("tf", "FrisierenTest");
        saveOrUpdateMaerchenCodeCommand = new SaveOrUpdateMaerchenCodeCommand(maerchenCode2, null, codesSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateMaerchenCodeCommand);
        assertEquals(2, codesSaved.size());
        // Alphabetisch geordnet?
        assertEquals("tf", codesSaved.get(0).getKuerzel());
        assertEquals("tk", codesSaved.get(1).getKuerzel());
        assertTrue(checkIfCodeAvailable("tk", "KuchenTest"));
        assertTrue(checkIfCodeAvailable("tf", "FrisierenTest"));

        // MaerchenCode bearbeiten
        MaerchenCode maerchenCode2Modif = new MaerchenCode("tf", "FrisierenModifTest");
        saveOrUpdateMaerchenCodeCommand = new SaveOrUpdateMaerchenCodeCommand(maerchenCode2Modif, maerchenCode2, codesSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateMaerchenCodeCommand);
        assertEquals(2, codesSaved.size());
        assertTrue(checkIfCodeAvailable("tk", "KuchenTest"));
        assertFalse(checkIfCodeAvailable("tf", "FrisierenTest"));
        assertTrue(checkIfCodeAvailable("tf", "FrisierenModifTest"));

        // Testdaten löschen
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            MaerchenCodeDao maerchenCodeDao = new MaerchenCodeDao(entityManager);
            for (MaerchenCode maerchenCode : codesSaved) {
                MaerchenCode maerchenCodeToBeDeleted = maerchenCodeDao.findById(maerchenCode.getCodeId());
                if (maerchenCodeToBeDeleted != null) {
                    maerchenCodeDao.remove(maerchenCodeToBeDeleted);
                }
            }
            entityManager.getTransaction().commit();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }


    }

    private boolean checkIfCodeAvailable(String kuerzel, String beschreibung) {
        FindAllMaerchenCodesCommand findAllMaerchenCodesCommand = new FindAllMaerchenCodesCommand();
        commandInvoker.executeCommandAsTransactionWithOpenAndClose(findAllMaerchenCodesCommand);
        List<MaerchenCode> codesAll = findAllMaerchenCodesCommand.getMaerchenCodesAll();
        for (MaerchenCode maerchenCode : codesAll) {
            if (maerchenCode.getKuerzel().equals(kuerzel) && maerchenCode.getBeschreibung().equals(beschreibung)) {
                return true;
            }
        }
        return false;
    }
}