package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.MaerchenDao;
import ch.metzenthin.svm.persistence.entities.Maerchen;
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
public class SaveOrUpdateMaerchenCommandTest {

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
        assertFalse(checkIfMaerchenAvailable("1912/1913", "Schneewittchen"));
        assertFalse(checkIfMaerchenAvailable("1911/1912", "Hans im Glück"));
        assertFalse(checkIfMaerchenAvailable("1914/1915", "Erlkönig"));
        List<Maerchen> maerchensSaved = new ArrayList<>();

        // Maerchen hinzufügen
        Maerchen maerchen1 = new Maerchen("1912/1913", "Schneewittchen");
        SaveOrUpdateMaerchenCommand saveOrUpdateMaerchenCommand = new SaveOrUpdateMaerchenCommand(maerchen1, null, maerchensSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateMaerchenCommand);
        assertEquals(1, maerchensSaved.size());
        assertTrue(checkIfMaerchenAvailable("1912/1913", "Schneewittchen"));

        // Weiteres Maerchen hinzufügen
        Maerchen maerchen2 = new Maerchen("1911/1912", "Hans im Glück");
        saveOrUpdateMaerchenCommand = new SaveOrUpdateMaerchenCommand(maerchen2, null, maerchensSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateMaerchenCommand);
        assertEquals(2, maerchensSaved.size());
        assertTrue(checkIfMaerchenAvailable("1911/1912", "Hans im Glück"));

        // Weiteres Maerchen hinzufügen
        Maerchen maerchen3 = new Maerchen("1914/1915", "Erlkönig");
        saveOrUpdateMaerchenCommand = new SaveOrUpdateMaerchenCommand(maerchen3, null, maerchensSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateMaerchenCommand);
        assertEquals(3, maerchensSaved.size());
        assertTrue(checkIfMaerchenAvailable("1912/1913", "Schneewittchen"));
        assertTrue(checkIfMaerchenAvailable("1911/1912", "Hans im Glück"));
        assertTrue(checkIfMaerchenAvailable("1914/1915", "Erlkönig"));
        // Zeitlich absteigend geordnet?
        assertEquals("1911/1912", maerchensSaved.get(2).getSchuljahr());
        assertEquals("Hans im Glück", maerchensSaved.get(2).getBezeichnung());
        assertEquals("1912/1913", maerchensSaved.get(1).getSchuljahr());
        assertEquals("Schneewittchen", maerchensSaved.get(1).getBezeichnung());
        assertEquals("1914/1915", maerchensSaved.get(0).getSchuljahr());
        assertEquals("Erlkönig", maerchensSaved.get(0).getBezeichnung());

        // Maerchen bearbeiten (neue Bezeichnung)
        Maerchen maerchen1Modif = new Maerchen("1912/1913", "Rumpelstilzchen");
        saveOrUpdateMaerchenCommand = new SaveOrUpdateMaerchenCommand(maerchen1Modif, maerchen1, maerchensSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateMaerchenCommand);
        assertEquals(3, maerchensSaved.size());
        assertFalse(checkIfMaerchenAvailable("1912/1913", "Schneewittchen"));
        assertTrue(checkIfMaerchenAvailable("1912/1913", "Rumpelstilzchen"));

        // Testdaten löschen
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            MaerchenDao maerchenDao = new MaerchenDao(entityManager);
            for (Maerchen maerchen : maerchensSaved) {
                Maerchen maerchenToBeDeleted = maerchenDao.findById(maerchen.getMaerchenId());
                if (maerchenToBeDeleted != null) {
                    maerchenDao.remove(maerchenToBeDeleted);
                }
            }
            entityManager.getTransaction().commit();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }


    }

    private boolean checkIfMaerchenAvailable(String schuljahr, String bezeichnung) {
        FindAllMaerchensCommand findAllMaerchensCommand = new FindAllMaerchensCommand();
        commandInvoker.executeCommandAsTransactionWithOpenAndClose(findAllMaerchensCommand);
        List<Maerchen> maerchensAll = findAllMaerchensCommand.getMaerchensAll();
        for (Maerchen maerchen : maerchensAll) {
            if (maerchen.getSchuljahr().equals(schuljahr) && maerchen.getBezeichnung().equals(bezeichnung)) {
                return true;
            }
        }
        return false;
    }
}