package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.utils.PersistenceProperties;
import ch.metzenthin.svm.persistence.daos.MitarbeiterCodeDao;
import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;
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
public class FindAllMitarbeiterCodesCommandTest {

    private CommandInvoker commandInvoker = new CommandInvokerImpl();
    private EntityManagerFactory entityManagerFactory;
    private Set<MitarbeiterCode> codesTestdata = new HashSet<>();

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
        FindAllMitarbeiterCodesCommand findAllMitarbeiterCodesCommand = new FindAllMitarbeiterCodesCommand();
        commandInvoker.executeCommandAsTransactionWithOpenAndClose(findAllMitarbeiterCodesCommand);

        List<MitarbeiterCode> codesFound = findAllMitarbeiterCodesCommand.getMitarbeiterCodesAll();
        assertTrue(codesFound.size() >= 2);
        boolean found1 = false;
        boolean found2 = false;
        for (MitarbeiterCode mitarbeiterCode : codesFound) {
            if (mitarbeiterCode.getBeschreibung().equals("VertretungTest")) {
                found1 = true;
            }
            if (mitarbeiterCode.getBeschreibung().equals("HelferTest")) {
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

            MitarbeiterCodeDao mitarbeiterCodeDao = new MitarbeiterCodeDao(entityManager);

            MitarbeiterCode mitarbeiterCodeSaved = mitarbeiterCodeDao.save(new MitarbeiterCode("v", "VertretungTest", true));
            codesTestdata.add(mitarbeiterCodeSaved);

            mitarbeiterCodeSaved = mitarbeiterCodeDao.save(new MitarbeiterCode("h", "HelferTest", true));
            codesTestdata.add(mitarbeiterCodeSaved);

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

            MitarbeiterCodeDao mitarbeiterCodeDao = new MitarbeiterCodeDao(entityManager);

            for (MitarbeiterCode mitarbeiterCode : codesTestdata) {
                MitarbeiterCode mitarbeiterCodeToBeRemoved = mitarbeiterCodeDao.findById(mitarbeiterCode.getCodeId());
                mitarbeiterCodeDao.remove(mitarbeiterCodeToBeRemoved);
            }

            entityManager.getTransaction().commit();

        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }
}