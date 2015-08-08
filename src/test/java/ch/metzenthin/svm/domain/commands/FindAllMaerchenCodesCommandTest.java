package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.MaerchenCodeDao;
import ch.metzenthin.svm.persistence.entities.MaerchenCode;
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
public class FindAllMaerchenCodesCommandTest {

    private CommandInvoker commandInvoker = new CommandInvokerImpl();
    private EntityManagerFactory entityManagerFactory;
    private Set<MaerchenCode> codesTestdata = new HashSet<>();

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
        FindAllMaerchenCodesCommand findAllMaerchenCodesCommand = new FindAllMaerchenCodesCommand();
        commandInvoker.executeCommandAsTransactionWithOpenAndClose(findAllMaerchenCodesCommand);

        List<MaerchenCode> codesFound = findAllMaerchenCodesCommand.getMaerchenCodesAll();
        assertTrue(codesFound.size() >= 2);
        boolean found1 = false;
        boolean found2 = false;
        for (MaerchenCode maerchenCode : codesTestdata) {
            if (maerchenCode.getBeschreibung().equals("KuchenTest")) {
                found1 = true;
            }
            if (maerchenCode.getBeschreibung().equals("FrisierenTest")) {
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

            MaerchenCodeDao maerchenCodeDao = new MaerchenCodeDao(entityManager);

            MaerchenCode maerchenCodeSaved = maerchenCodeDao.save(new MaerchenCode("k", "KuchenTest"));
            codesTestdata.add(maerchenCodeSaved);

            maerchenCodeSaved = maerchenCodeDao.save(new MaerchenCode("f", "FrisierenTest"));
            codesTestdata.add(maerchenCodeSaved);

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

            MaerchenCodeDao maerchenCodeDao = new MaerchenCodeDao(entityManager);

            for (MaerchenCode maerchenCode : codesTestdata) {
                MaerchenCode maerchenCodeToBeRemoved = maerchenCodeDao.findById(maerchenCode.getCodeId());
                maerchenCodeDao.remove(maerchenCodeToBeRemoved);
            }

            entityManager.getTransaction().commit();

        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }
}