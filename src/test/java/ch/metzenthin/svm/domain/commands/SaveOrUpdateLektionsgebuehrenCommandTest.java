package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.daos.LektionsgebuehrenDao;
import ch.metzenthin.svm.persistence.entities.Lektionsgebuehren;
import jakarta.persistence.EntityManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;
import static org.junit.Assert.*;

/**
 * @author Martin Schraner
 */
public class SaveOrUpdateLektionsgebuehrenCommandTest {

    private final LektionsgebuehrenDao lektionsgebuehrenDao = new LektionsgebuehrenDao();

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
        assertEquals(Integer.valueOf(57), lektionsgebuehrenSaved.get(0).getLektionslaenge());
        assertEquals(Integer.valueOf(67), lektionsgebuehrenSaved.get(1).getLektionslaenge());
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
        EntityManager entityManager = db.getCurrentEntityManager();
        entityManager.getTransaction().begin();
        for (Lektionsgebuehren lektionsgebuehren : lektionsgebuehrenSaved) {
            Lektionsgebuehren lektionsgebuehrenToBeDeleted = lektionsgebuehrenDao.findById(lektionsgebuehren.getLektionslaenge());
            if (lektionsgebuehrenToBeDeleted != null) {
                lektionsgebuehrenDao.remove(lektionsgebuehrenToBeDeleted);
            }
        }
        entityManager.getTransaction().commit();
    }

    private boolean checkIfLektionsgebuehrenAvailable(Integer lektionslaenge, BigDecimal betrag1Kind) {
        FindAllLektionsgebuehrenCommand findAllLektionsgebuehrenCommand = new FindAllLektionsgebuehrenCommand();
        commandInvoker.executeCommand(findAllLektionsgebuehrenCommand);
        List<Lektionsgebuehren> lektionsgebuehrenAll = findAllLektionsgebuehrenCommand.getLektionsgebuehrenAllList();
        for (Lektionsgebuehren lektionsgebuehren : lektionsgebuehrenAll) {
            if (lektionsgebuehren.getLektionslaenge().equals(lektionslaenge) && lektionsgebuehren.getBetrag1Kind().equals(betrag1Kind)) {
                return true;
            }
        }
        return false;
    }
}