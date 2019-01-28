package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.daos.MaerchenDao;
import ch.metzenthin.svm.persistence.entities.Maerchen;
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
public class SaveOrUpdateMaerchenCommandTest {

    private final MaerchenDao maerchenDao = new MaerchenDao();

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
        assertFalse(checkIfMaerchenAvailable("1912/1913", "Schneewittchen", 7));
        assertFalse(checkIfMaerchenAvailable("1911/1912", "Hans im Glück", 8));
        assertFalse(checkIfMaerchenAvailable("1914/1915", "Erlkönig", 9));
        List<Maerchen> maerchensSaved = new ArrayList<>();

        // Maerchen hinzufügen
        Maerchen maerchen1 = new Maerchen("1912/1913", "Schneewittchen", 7);
        SaveOrUpdateMaerchenCommand saveOrUpdateMaerchenCommand = new SaveOrUpdateMaerchenCommand(maerchen1, null, maerchensSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateMaerchenCommand);
        assertEquals(1, maerchensSaved.size());
        assertTrue(checkIfMaerchenAvailable("1912/1913", "Schneewittchen", 7));

        // Weiteres Maerchen hinzufügen
        Maerchen maerchen2 = new Maerchen("1911/1912", "Hans im Glück", 8);
        saveOrUpdateMaerchenCommand = new SaveOrUpdateMaerchenCommand(maerchen2, null, maerchensSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateMaerchenCommand);
        assertEquals(2, maerchensSaved.size());
        assertTrue(checkIfMaerchenAvailable("1911/1912", "Hans im Glück", 8));

        // Weiteres Maerchen hinzufügen
        Maerchen maerchen3 = new Maerchen("1914/1915", "Erlkönig", 9);
        saveOrUpdateMaerchenCommand = new SaveOrUpdateMaerchenCommand(maerchen3, null, maerchensSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateMaerchenCommand);
        assertEquals(3, maerchensSaved.size());
        assertTrue(checkIfMaerchenAvailable("1912/1913", "Schneewittchen", 7));
        assertTrue(checkIfMaerchenAvailable("1911/1912", "Hans im Glück", 8));
        assertTrue(checkIfMaerchenAvailable("1914/1915", "Erlkönig", 9));
        // Zeitlich absteigend geordnet?
        assertEquals("1911/1912", maerchensSaved.get(2).getSchuljahr());
        assertEquals("Hans im Glück", maerchensSaved.get(2).getBezeichnung());
        assertEquals("1912/1913", maerchensSaved.get(1).getSchuljahr());
        assertEquals("Schneewittchen", maerchensSaved.get(1).getBezeichnung());
        assertEquals("1914/1915", maerchensSaved.get(0).getSchuljahr());
        assertEquals("Erlkönig", maerchensSaved.get(0).getBezeichnung());

        // Maerchen bearbeiten (neue Bezeichnung)
        Maerchen maerchen1Modif = new Maerchen("1912/1913", "Rumpelstilzchen", 9);
        saveOrUpdateMaerchenCommand = new SaveOrUpdateMaerchenCommand(maerchen1Modif, maerchen1, maerchensSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateMaerchenCommand);
        assertEquals(3, maerchensSaved.size());
        assertFalse(checkIfMaerchenAvailable("1912/1913", "Schneewittchen", 7));
        assertTrue(checkIfMaerchenAvailable("1912/1913", "Rumpelstilzchen", 9));

        // Testdaten löschen
        EntityManager entityManager = db.getCurrentEntityManager();
        entityManager.getTransaction().begin();
        for (Maerchen maerchen : maerchensSaved) {
            Maerchen maerchenToBeDeleted = maerchenDao.findById(maerchen.getMaerchenId());
            if (maerchenToBeDeleted != null) {
                maerchenDao.remove(maerchenToBeDeleted);
            }
        }
        entityManager.getTransaction().commit();
    }

    private boolean checkIfMaerchenAvailable(String schuljahr, String bezeichnung, int anzahlVorstellungen) {
        FindAllMaerchensCommand findAllMaerchensCommand = new FindAllMaerchensCommand();
        commandInvoker.executeCommand(findAllMaerchensCommand);
        List<Maerchen> maerchensAll = findAllMaerchensCommand.getMaerchensAll();
        for (Maerchen maerchen : maerchensAll) {
            if (maerchen.getSchuljahr().equals(schuljahr) && maerchen.getBezeichnung().equals(bezeichnung) && maerchen.getAnzahlVorstellungen().equals(anzahlVorstellungen)) {
                return true;
            }
        }
        return false;
    }
}