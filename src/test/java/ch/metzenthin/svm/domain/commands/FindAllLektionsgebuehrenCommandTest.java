package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.LektionsgebuehrenDao;
import ch.metzenthin.svm.persistence.entities.Lektionsgebuehren;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;
import static org.junit.Assert.assertTrue;

/**
 * @author Martin Schraner
 */
public class FindAllLektionsgebuehrenCommandTest {

    private CommandInvoker commandInvoker = new CommandInvokerImpl();
    private Set<Lektionsgebuehren> lektionsgebuehrenTestdata = new HashSet<>();

    @Before
    public void setUp() throws Exception {
        createSvmPropertiesFileDefault();
        createTestdata();
    }

    @After
    public void tearDown() throws Exception {
        deleteTestdata();
        commandInvoker.closeSessionAndEntityManagerFactory();
    }

    @Test
    public void testExecute_getList() {
        FindAllLektionsgebuehrenCommand findAllLektionsgebuehrenCommand = new FindAllLektionsgebuehrenCommand();
        commandInvoker.executeCommandAsTransactionWithOpenAndClose(findAllLektionsgebuehrenCommand);

        List<Lektionsgebuehren> lektionsgebuehrenListFound = findAllLektionsgebuehrenCommand.getLektionsgebuehrenAllList();
        assertTrue(lektionsgebuehrenListFound.size() >= 2);
        boolean found1 = false;
        boolean found2 = false;
        for (Lektionsgebuehren lektionsgebuehren : lektionsgebuehrenListFound) {
            if (lektionsgebuehren.getLektionslaenge().equals(57) && lektionsgebuehren.getBetrag1Kind().equals(new BigDecimal("22.50"))) {
                found1 = true;
            }
            if (lektionsgebuehren.getLektionslaenge().equals(67) && lektionsgebuehren.getBetrag1Kind().equals(new BigDecimal("24.50"))) {
                found2 = true;
            }
        }
        assertTrue(found1);
        assertTrue(found2);
    }

    @Test
    public void testExecute_getMap() {
        FindAllLektionsgebuehrenCommand findAllLektionsgebuehrenCommand = new FindAllLektionsgebuehrenCommand();
        commandInvoker.executeCommandAsTransactionWithOpenAndClose(findAllLektionsgebuehrenCommand);

        Map<Integer, BigDecimal[]> lektionsgebuehrenMapFound = findAllLektionsgebuehrenCommand.getLektionsgebuehrenAllMap();
        assertTrue(lektionsgebuehrenMapFound.size() >= 2);
        boolean found1 = false;
        boolean found2 = false;
        for (Integer lektionslaenge : lektionsgebuehrenMapFound.keySet()) {
            if (lektionslaenge.equals(57)
                    && lektionsgebuehrenMapFound.get(lektionslaenge).length == Lektionsgebuehren.MAX_KINDER
                    && lektionsgebuehrenMapFound.get(lektionslaenge)[0].equals(new BigDecimal("22.50"))
                    && lektionsgebuehrenMapFound.get(lektionslaenge)[1].equals(new BigDecimal("21.00"))
                    && lektionsgebuehrenMapFound.get(lektionslaenge)[2].equals(new BigDecimal("19.00"))
                    && lektionsgebuehrenMapFound.get(lektionslaenge)[3].equals(new BigDecimal("18.00"))
                    && lektionsgebuehrenMapFound.get(lektionslaenge)[4].equals(new BigDecimal("17.00"))
                    && lektionsgebuehrenMapFound.get(lektionslaenge)[5].equals(new BigDecimal("16.00"))) {
                found1 = true;
            }
            if (lektionslaenge.equals(67)
                    && lektionsgebuehrenMapFound.get(lektionslaenge).length == Lektionsgebuehren.MAX_KINDER
                    && lektionsgebuehrenMapFound.get(lektionslaenge)[0].equals(new BigDecimal("24.50"))) {
                found2 = true;
            }
        }
        assertTrue(found1);
        assertTrue(found2);
    }

    private void createTestdata() {
        commandInvoker.openSession();
        EntityManager entityManager = commandInvoker.getEntityManager();
        entityManager.getTransaction().begin();

        LektionsgebuehrenDao lektionsgebuehrenDao = new LektionsgebuehrenDao(entityManager);

        Lektionsgebuehren lektionsgebuehrenaved = lektionsgebuehrenDao.save(new Lektionsgebuehren(57, new BigDecimal("22.50"), new BigDecimal("21.00"), new BigDecimal("19.00"), new BigDecimal("18.00"), new BigDecimal("17.00"), new BigDecimal("16.00")));
        lektionsgebuehrenTestdata.add(lektionsgebuehrenaved);

        lektionsgebuehrenaved = lektionsgebuehrenDao.save(new Lektionsgebuehren(67, new BigDecimal("24.50"), new BigDecimal("23.00"), new BigDecimal("21.00"), new BigDecimal("20.00"), new BigDecimal("19.00"), new BigDecimal("18.00")));
        lektionsgebuehrenTestdata.add(lektionsgebuehrenaved);

        entityManager.getTransaction().commit();
        commandInvoker.closeSession();
    }

    private void deleteTestdata() {
        commandInvoker.openSession();
        EntityManager entityManager = commandInvoker.getEntityManager();
        entityManager.getTransaction().begin();

        LektionsgebuehrenDao lektionsgebuehrenDao = new LektionsgebuehrenDao(entityManager);

        for (Lektionsgebuehren lektionsgebuehren : lektionsgebuehrenTestdata) {
            Lektionsgebuehren lektionsgebuehrenToBeRemoved = lektionsgebuehrenDao.findById(lektionsgebuehren.getLektionslaenge());
            lektionsgebuehrenDao.remove(lektionsgebuehrenToBeRemoved);
        }

        entityManager.getTransaction().commit();
        commandInvoker.closeSession();
    }
}