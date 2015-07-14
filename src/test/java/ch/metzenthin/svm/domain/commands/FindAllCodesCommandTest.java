package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.SvmDbException;
import ch.metzenthin.svm.persistence.daos.CodeDao;
import ch.metzenthin.svm.persistence.entities.Code;
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
public class FindAllCodesCommandTest {

    private CommandInvoker commandInvoker = new CommandInvokerImpl();
    private EntityManagerFactory entityManagerFactory;
    private Set<Code> codesTestdata = new HashSet<>();

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
        FindAllCodesCommand findAllCodesCommand = new FindAllCodesCommand();
        try {
            commandInvoker.executeCommand(findAllCodesCommand);
        } catch (SvmDbException e) {
            e.printStackTrace();
        }

        List<Code> codesFound = findAllCodesCommand.getCodesAll();
        assertTrue(codesFound.size() >= 2);
        boolean found1 = false;
        boolean found2 = false;
        for (Code code : codesTestdata) {
            if (code.getBeschreibung().equals("ZirkusTest")) {
                found1 = true;
            }
            if (code.getBeschreibung().equals("JugendprojektTest")) {
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

            CodeDao codeDao = new CodeDao(entityManager);

            Code codeSaved = codeDao.save(new Code("z", "ZirkusTest"));
            codesTestdata.add(codeSaved);

            codeSaved = codeDao.save(new Code("j", "JugendprojektTest"));
            codesTestdata.add(codeSaved);

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

            CodeDao codeDao = new CodeDao(entityManager);

            for (Code code : codesTestdata) {
                Code codeToBeRemoved = codeDao.findById(code.getCodeId());
                codeDao.remove(codeToBeRemoved);
            }

            entityManager.getTransaction().commit();

        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }
}