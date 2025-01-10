package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.daos.MaerchenDao;
import ch.metzenthin.svm.persistence.entities.Maerchen;
import jakarta.persistence.EntityManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;
import static org.junit.Assert.assertTrue;

/**
 * @author Martin Schraner
 */
public class FindAllMaerchensCommandTest {

    private final MaerchenDao maerchenDao = new MaerchenDao();
    private final Set<Maerchen> maerchenTestdata = new HashSet<>();

    private DB db;
    private CommandInvoker commandInvoker;

    @Before
    public void setUp() {
        createSvmPropertiesFileDefault();
        db = DBFactory.getInstance();
        commandInvoker = new CommandInvokerImpl();
        createTestdata();
    }

    @After
    public void tearDown() {
        deleteTestdata();
        db.closeSession();
    }

    @Test
    public void testExecute() {
        FindAllMaerchensCommand findAllMaerchensCommand = new FindAllMaerchensCommand();
        commandInvoker.executeCommand(findAllMaerchensCommand);

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
        EntityManager entityManager = db.getCurrentEntityManager();
        entityManager.getTransaction().begin();

        Maerchen maerchen1 = new Maerchen("1911/1912", "Gestiefelter Kater", 7);
        Maerchen maerchenSaved = maerchenDao.save(maerchen1);
        maerchenTestdata.add(maerchenSaved);

        Maerchen maerchen2 = new Maerchen("1912/2013", "Schneewittchen", 8);
        maerchenSaved = maerchenDao.save(maerchen2);
        maerchenTestdata.add(maerchenSaved);

        entityManager.getTransaction().commit();
        db.closeSession();
    }

    private void deleteTestdata() {
        EntityManager entityManager = db.getCurrentEntityManager();
        entityManager.getTransaction().begin();

        for (Maerchen maerchen : maerchenTestdata) {
            Maerchen maerchenToBeRemoved = maerchenDao.findById(maerchen.getMaerchenId());
            maerchenDao.remove(maerchenToBeRemoved);
        }

        entityManager.getTransaction().commit();
        db.closeSession();
    }
}