package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.ElternmithilfeCodeDao;
import ch.metzenthin.svm.persistence.entities.ElternmithilfeCode;
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
public class FindAllElternmithilfeCodesCommandTest {

    private CommandInvoker commandInvoker = new CommandInvokerImpl();
    private EntityManagerFactory entityManagerFactory;
    private Set<ElternmithilfeCode> codesTestdata = new HashSet<>();

    @Before
    public void setUp() throws Exception {
        entityManagerFactory = Persistence.createEntityManagerFactory("svm");
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
        FindAllElternmithilfeCodesCommand findAllElternmithilfeCodesCommand = new FindAllElternmithilfeCodesCommand();
        commandInvoker.executeCommandAsTransactionWithOpenAndClose(findAllElternmithilfeCodesCommand);

        List<ElternmithilfeCode> codesFound = findAllElternmithilfeCodesCommand.getElternmithilfeCodesAll();
        assertTrue(codesFound.size() >= 2);
        boolean found1 = false;
        boolean found2 = false;
        for (ElternmithilfeCode elternmithilfeCode : codesTestdata) {
            if (elternmithilfeCode.getBeschreibung().equals("KuchenTest")) {
                found1 = true;
            }
            if (elternmithilfeCode.getBeschreibung().equals("FrisierenTest")) {
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

            ElternmithilfeCodeDao elternmithilfeCodeDao = new ElternmithilfeCodeDao(entityManager);

            ElternmithilfeCode elternmithilfeCodeSaved = elternmithilfeCodeDao.save(new ElternmithilfeCode("k", "KuchenTest", true));
            codesTestdata.add(elternmithilfeCodeSaved);

            elternmithilfeCodeSaved = elternmithilfeCodeDao.save(new ElternmithilfeCode("f", "FrisierenTest", true));
            codesTestdata.add(elternmithilfeCodeSaved);

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

            ElternmithilfeCodeDao elternmithilfeCodeDao = new ElternmithilfeCodeDao(entityManager);

            for (ElternmithilfeCode elternmithilfeCode : codesTestdata) {
                ElternmithilfeCode elternmithilfeCodeToBeRemoved = elternmithilfeCodeDao.findById(elternmithilfeCode.getCodeId());
                elternmithilfeCodeDao.remove(elternmithilfeCodeToBeRemoved);
            }

            entityManager.getTransaction().commit();

        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }
}