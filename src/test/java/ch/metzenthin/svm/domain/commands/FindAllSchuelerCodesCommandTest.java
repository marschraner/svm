package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.SchuelerCodeDao;
import ch.metzenthin.svm.persistence.entities.SchuelerCode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;
import static org.junit.Assert.assertTrue;

/**
 * @author Martin Schraner
 */
public class FindAllSchuelerCodesCommandTest {

    private CommandInvoker commandInvoker = new CommandInvokerImpl();
    private Set<SchuelerCode> codesTestdata = new HashSet<>();

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
    public void testExecute() {
        FindAllSchuelerCodesCommand findAllSchuelerCodesCommand = new FindAllSchuelerCodesCommand();
        commandInvoker.executeCommandAsTransactionWithOpenAndClose(findAllSchuelerCodesCommand);

        List<SchuelerCode> codesFound = findAllSchuelerCodesCommand.getSchuelerCodesAll();
        assertTrue(codesFound.size() >= 2);
        boolean found1 = false;
        boolean found2 = false;
        for (SchuelerCode schuelerCode : codesFound) {
            if (schuelerCode.getBeschreibung().equals("ZirkusTest")) {
                found1 = true;
            }
            if (schuelerCode.getBeschreibung().equals("JugendprojektTest")) {
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

        SchuelerCodeDao schuelerCodeDao = new SchuelerCodeDao(entityManager);

        SchuelerCode schuelerCodeSaved = schuelerCodeDao.save(new SchuelerCode("z", "ZirkusTest", true));
        codesTestdata.add(schuelerCodeSaved);

        schuelerCodeSaved = schuelerCodeDao.save(new SchuelerCode("j", "JugendprojektTest", true));
        codesTestdata.add(schuelerCodeSaved);

        entityManager.getTransaction().commit();
        commandInvoker.closeSession();
    }

    private void deleteTestdata() {
        commandInvoker.openSession();
        EntityManager entityManager = commandInvoker.getEntityManager();
        entityManager.getTransaction().begin();

        SchuelerCodeDao schuelerCodeDao = new SchuelerCodeDao(entityManager);

        for (SchuelerCode schuelerCode : codesTestdata) {
            SchuelerCode schuelerCodeToBeRemoved = schuelerCodeDao.findById(schuelerCode.getCodeId());
            schuelerCodeDao.remove(schuelerCodeToBeRemoved);
        }

        entityManager.getTransaction().commit();
        commandInvoker.closeSession();
    }
}