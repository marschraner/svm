package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.utils.PersistenceProperties;
import ch.metzenthin.svm.persistence.entities.Lektionsgebuehren;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * @author Martin Schraner
 */
public class DeleteLektionsgebuehrenCommandTest {

    private CommandInvoker commandInvoker = new CommandInvokerImpl();
    private EntityManagerFactory entityManagerFactory;

    @Before
    public void setUp() throws Exception {
        createSvmPropertiesFileDefault();
        entityManagerFactory = Persistence.createEntityManagerFactory("svm", PersistenceProperties.getPersistenceProperties());
    }

    @After
    public void tearDown() throws Exception {
        commandInvoker.close();
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }

    @Test
    public void testExecute() throws Exception {

        List<Lektionsgebuehren> lektionsgebuehrenSaved = new ArrayList<>();

        // 2 Lektionsgebuehren erfassen
        Lektionsgebuehren lektionsgebuehren1 = new Lektionsgebuehren(57, new BigDecimal("22.50"), new BigDecimal("21.00"), new BigDecimal("19.00"), new BigDecimal("18.00"), new BigDecimal("17.00"), new BigDecimal("16.00"));
        Lektionsgebuehren lektionsgebuehren2 = new Lektionsgebuehren(67, new BigDecimal("24.50"), new BigDecimal("23.00"), new BigDecimal("21.00"), new BigDecimal("20.00"), new BigDecimal("19.00"), new BigDecimal("18.00"));

        SaveOrUpdateLektionsgebuehrenCommand saveOrUpdateLektionsgebuehrenCommand = new SaveOrUpdateLektionsgebuehrenCommand(lektionsgebuehren1, null, lektionsgebuehrenSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateLektionsgebuehrenCommand);

        saveOrUpdateLektionsgebuehrenCommand = new SaveOrUpdateLektionsgebuehrenCommand(lektionsgebuehren2, null, lektionsgebuehrenSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateLektionsgebuehrenCommand);

        // Lektionsgebuehren löschen
        DeleteLektionsgebuehrenCommand deleteLektionsgebuehrenCommand = new DeleteLektionsgebuehrenCommand(lektionsgebuehrenSaved, 1);
        commandInvoker.executeCommandAsTransaction(deleteLektionsgebuehrenCommand);
        assertEquals(1, lektionsgebuehrenSaved.size());

        deleteLektionsgebuehrenCommand = new DeleteLektionsgebuehrenCommand(lektionsgebuehrenSaved, 0);
        commandInvoker.executeCommandAsTransaction(deleteLektionsgebuehrenCommand);
        assertTrue(lektionsgebuehrenSaved.isEmpty());

    }
}