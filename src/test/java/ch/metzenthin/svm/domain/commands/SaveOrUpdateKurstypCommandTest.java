package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.SvmDbException;
import ch.metzenthin.svm.persistence.daos.KurstypDao;
import ch.metzenthin.svm.persistence.entities.Kurstyp;
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
public class SaveOrUpdateKurstypCommandTest {

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
        assertFalse(checkIfKurstypAvailable("Kurs Test1"));
        assertFalse(checkIfKurstypAvailable("Kurs Test2"));

        List<Kurstyp> kurstypenSaved = new ArrayList<>();

        // Kurstyp hinzufügen
        Kurstyp kurstyp1 = new Kurstyp("Kurs Test1");
        SaveOrUpdateKurstypCommand saveOrUpdateKurstypCommand = new SaveOrUpdateKurstypCommand(kurstyp1, null, kurstypenSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateKurstypCommand);
        assertEquals(1, kurstypenSaved.size());
        assertTrue(checkIfKurstypAvailable("Kurs Test1"));
        assertFalse(checkIfKurstypAvailable("Kurs Test2"));

        // Weiteren Kurstyp hinzufügen
        Kurstyp kurstyp2 = new Kurstyp("Kurs Test2");
        saveOrUpdateKurstypCommand = new SaveOrUpdateKurstypCommand(kurstyp2, null, kurstypenSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateKurstypCommand);
        assertEquals(2, kurstypenSaved.size());
        // Alphabetisch geordnet?
        assertEquals("Kurs Test1", kurstypenSaved.get(0).getBezeichnung());
        assertEquals("Kurs Test2", kurstypenSaved.get(1).getBezeichnung());
        assertTrue(checkIfKurstypAvailable("Kurs Test1"));
        assertTrue(checkIfKurstypAvailable("Kurs Test2"));

        // Kurstyp bearbeiten
        Kurstyp kurstyp2Modif = new Kurstyp("Kurs Test3");
        saveOrUpdateKurstypCommand = new SaveOrUpdateKurstypCommand(kurstyp2Modif, kurstyp2, kurstypenSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateKurstypCommand);
        assertEquals(2, kurstypenSaved.size());
        assertTrue(checkIfKurstypAvailable("Kurs Test1"));
        assertFalse(checkIfKurstypAvailable("Kurs Test2"));
        assertTrue(checkIfKurstypAvailable("Kurs Test3"));

        // Testdaten löschen
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            KurstypDao kurstypDao = new KurstypDao(entityManager);
            for (Kurstyp kurstyp : kurstypenSaved) {
                Kurstyp kurstypToBeDeleted = kurstypDao.findById(kurstyp.getKurstypId());
                if (kurstypToBeDeleted != null) {
                    kurstypDao.remove(kurstypToBeDeleted);
                }
            }
            entityManager.getTransaction().commit();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    private boolean checkIfKurstypAvailable(String bezeichnung) throws SvmDbException {
        FindAllKurstypenCommand findAllKurstypenCommand = new FindAllKurstypenCommand();
        commandInvoker.executeCommandAsTransactionWithOpenAndClose(findAllKurstypenCommand);
        List<Kurstyp> kurstypenAll = findAllKurstypenCommand.getKurstypenAll();
        for (Kurstyp kurstyp : kurstypenAll) {
            if (kurstyp.getBezeichnung().equals(bezeichnung)) {
                return true;
            }
        }
        return false;
    }
}