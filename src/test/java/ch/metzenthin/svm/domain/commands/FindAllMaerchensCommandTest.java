package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.utils.PersistenceProperties;
import ch.metzenthin.svm.persistence.daos.MaerchenDao;
import ch.metzenthin.svm.persistence.entities.Maerchen;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;
import static org.junit.Assert.assertTrue;

/**
 * @author Martin Schraner
 */
public class FindAllMaerchensCommandTest {

    private CommandInvoker commandInvoker = new CommandInvokerImpl();
    private EntityManagerFactory entityManagerFactory;
    private Set<Maerchen> maerchenTestdata = new HashSet<>();

    @Before
    public void setUp() throws Exception {
        createSvmPropertiesFileDefault();
        entityManagerFactory = Persistence.createEntityManagerFactory("svm", PersistenceProperties.getPersistenceProperties());
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
        FindAllMaerchensCommand findAllMaerchensCommand = new FindAllMaerchensCommand();
            commandInvoker.executeCommandAsTransactionWithOpenAndClose(findAllMaerchensCommand);

        List<Maerchen> maerchenFound = findAllMaerchensCommand.getMaerchensAll();
        assertTrue(maerchenFound.size() >= 2);
        boolean found1 = false;
        boolean found2 = false;
        for (Maerchen maerchen : maerchenFound) {
            if (maerchen.getBezeichnung().equals("Gestiefelter Kater")) {
                found1 = true;
            }
            if (maerchen.getBezeichnung().equals("Schneewittchen")) {
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

            MaerchenDao maerchenDao = new MaerchenDao(entityManager);

            Maerchen maerchen1 = new Maerchen("1911/1912", "Gestiefelter Kater", 7);
            Maerchen maerchenSaved = maerchenDao.save(maerchen1);
            maerchenTestdata.add(maerchenSaved);

            Maerchen maerchen2 = new Maerchen("1912/2013", "Schneewittchen", 8);
            maerchenSaved = maerchenDao.save(maerchen2);
            maerchenTestdata.add(maerchenSaved);

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

            MaerchenDao maerchenDao = new MaerchenDao(entityManager);

            for (Maerchen maerchen : maerchenTestdata) {
                Maerchen maerchenToBeRemoved = maerchenDao.findById(maerchen.getMaerchenId());
                maerchenDao.remove(maerchenToBeRemoved);
            }

            entityManager.getTransaction().commit();

        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }
}