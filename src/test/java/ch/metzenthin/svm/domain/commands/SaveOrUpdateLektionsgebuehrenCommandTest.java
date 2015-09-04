package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.LektionsgebuehrenDao;
import ch.metzenthin.svm.persistence.entities.Lektionsgebuehren;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Martin Schraner
 */
public class SaveOrUpdateLektionsgebuehrenCommandTest {

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
        assertFalse(checkIfLektionsgebuehrenAvailable(57, new BigDecimal("22.50")));
        assertFalse(checkIfLektionsgebuehrenAvailable(67, new BigDecimal("24.50")));

        List<Lektionsgebuehren> lektionsgebuehrenSaved = new ArrayList<>();

        // Lektionsgebuehren hinzufügen
        Lektionsgebuehren lektionsgebuehren1 = new Lektionsgebuehren(67, new BigDecimal("24.50"), new BigDecimal("23.00"), new BigDecimal("21.00"), new BigDecimal("20.00"), new BigDecimal("19.00"), new BigDecimal("18.00"));
        SaveOrUpdateLektionsgebuehrenCommand saveOrUpdateLektionsgebuehrenCommand = new SaveOrUpdateLektionsgebuehrenCommand(lektionsgebuehren1, null, lektionsgebuehrenSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateLektionsgebuehrenCommand);
        assertEquals(1, lektionsgebuehrenSaved.size());
        assertFalse(checkIfLektionsgebuehrenAvailable(57, new BigDecimal("22.50")));
        assertTrue(checkIfLektionsgebuehrenAvailable(67, new BigDecimal("24.50")));

        // Weiteren Lektionsgebuehren hinzufügen
        Lektionsgebuehren lektionsgebuehren2 = new Lektionsgebuehren(57, new BigDecimal("22.50"), new BigDecimal("21.00"), new BigDecimal("19.00"), new BigDecimal("18.00"), new BigDecimal("17.00"), new BigDecimal("16.00"));
        saveOrUpdateLektionsgebuehrenCommand = new SaveOrUpdateLektionsgebuehrenCommand(lektionsgebuehren2, null, lektionsgebuehrenSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateLektionsgebuehrenCommand);
        assertEquals(2, lektionsgebuehrenSaved.size());
        // Nach Lektionslänge geordnet?
        assertEquals(new Integer(57), lektionsgebuehrenSaved.get(0).getLektionslaenge());
        assertEquals(new Integer(67), lektionsgebuehrenSaved.get(1).getLektionslaenge());
        assertTrue(checkIfLektionsgebuehrenAvailable(57, new BigDecimal("22.50")));
        assertTrue(checkIfLektionsgebuehrenAvailable(67, new BigDecimal("24.50")));

        // Lektionsgebuehren bearbeiten
        Lektionsgebuehren lektionsgebuehren2Modif = new Lektionsgebuehren(57, new BigDecimal("23.00"), new BigDecimal("21.00"), new BigDecimal("19.00"), new BigDecimal("18.00"), new BigDecimal("17.00"), new BigDecimal("16.00"));

        saveOrUpdateLektionsgebuehrenCommand = new SaveOrUpdateLektionsgebuehrenCommand(lektionsgebuehren2Modif, lektionsgebuehren2, lektionsgebuehrenSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateLektionsgebuehrenCommand);
        assertEquals(2, lektionsgebuehrenSaved.size());
        assertTrue(checkIfLektionsgebuehrenAvailable(67, new BigDecimal("24.50")));
        assertFalse(checkIfLektionsgebuehrenAvailable(57, new BigDecimal("22.50")));
        assertTrue(checkIfLektionsgebuehrenAvailable(57, new BigDecimal("23.00")));

        // Testdaten löschen
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            LektionsgebuehrenDao lektionsgebuehrenDao = new LektionsgebuehrenDao(entityManager);
            for (Lektionsgebuehren lektionsgebuehren : lektionsgebuehrenSaved) {
                Lektionsgebuehren lektionsgebuehrenToBeDeleted = lektionsgebuehrenDao.findById(lektionsgebuehren.getLektionslaenge());
                if (lektionsgebuehrenToBeDeleted != null) {
                    lektionsgebuehrenDao.remove(lektionsgebuehrenToBeDeleted);
                }
            }
            entityManager.getTransaction().commit();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    private boolean checkIfLektionsgebuehrenAvailable(Integer lektionslaenge, BigDecimal betrag1Kind) {
        FindAllLektionsgebuehrenCommand findAllLektionsgebuehrenCommand = new FindAllLektionsgebuehrenCommand();
        commandInvoker.executeCommandAsTransactionWithOpenAndClose(findAllLektionsgebuehrenCommand);
        List<Lektionsgebuehren> lektionsgebuehrenAll = findAllLektionsgebuehrenCommand.getLektionsgebuehrenAllList();
        for (Lektionsgebuehren lektionsgebuehren : lektionsgebuehrenAll) {
            if (lektionsgebuehren.getLektionslaenge().equals(lektionslaenge) && lektionsgebuehren.getBetrag1Kind().equals(betrag1Kind)) {
                return true;
            }
        }
        return false;
    }
}