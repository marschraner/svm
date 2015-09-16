package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.SchuelerCodeDao;
import ch.metzenthin.svm.persistence.entities.SchuelerCode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertTrue;

/**
 * @author Martin Schraner
 */
public class FindAllSchuelerCodesCommandTest {

    private CommandInvoker commandInvoker = new CommandInvokerImpl();
    private EntityManagerFactory entityManagerFactory;
    private Set<SchuelerCode> codesTestdata = new HashSet<>();

    @Before
    public void setUp() throws Exception {
        entityManagerFactory = Persistence.createEntityManagerFactory("svmtest");
        createTestdata();
    }

    @After
    public void tearDown() throws Exception {
        deleteTestdata();
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }

    @Test
    public void testExecute() {
        FindAllSchuelerCodesCommand findAllSchuelerCodesCommand = new FindAllSchuelerCodesCommand();
        commandInvoker.executeCommandAsTransactionWithOpenAndCloseSvmTest(findAllSchuelerCodesCommand);

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
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            SchuelerCodeDao schuelerCodeDao = new SchuelerCodeDao(entityManager);

            SchuelerCode schuelerCodeSaved = schuelerCodeDao.save(new SchuelerCode("z", "ZirkusTest", true));
            codesTestdata.add(schuelerCodeSaved);

            schuelerCodeSaved = schuelerCodeDao.save(new SchuelerCode("j", "JugendprojektTest", true));
            codesTestdata.add(schuelerCodeSaved);

            entityManager.getTransaction().commit();

        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    private void deleteTestdata() {
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            SchuelerCodeDao schuelerCodeDao = new SchuelerCodeDao(entityManager);

            for (SchuelerCode schuelerCode : codesTestdata) {
                SchuelerCode schuelerCodeToBeRemoved = schuelerCodeDao.findById(schuelerCode.getCodeId());
                schuelerCodeDao.remove(schuelerCodeToBeRemoved);
            }

            entityManager.getTransaction().commit();

        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }
}