package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.KursortDao;
import ch.metzenthin.svm.persistence.entities.Kursort;
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
public class FindAllKursorteCommandTest {

    private CommandInvoker commandInvoker = new CommandInvokerImpl();
    private Set<Kursort> kursorteTestdata = new HashSet<>();

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
        FindAllKursorteCommand findAllKursorteCommand = new FindAllKursorteCommand();
        commandInvoker.executeCommandAsTransactionWithOpenAndClose(findAllKursorteCommand);

        List<Kursort> kursorteFound = findAllKursorteCommand.getKursorteAll();
        assertTrue(kursorteFound.size() >= 2);
        boolean found1 = false;
        boolean found2 = false;
        for (Kursort kursort : kursorteFound) {
            if (kursort.getBezeichnung().equals("Saal Test1")) {
                found1 = true;
            }
            if (kursort.getBezeichnung().equals("Saal Test2")) {
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

        KursortDao kursortDao = new KursortDao(entityManager);

        Kursort kursorteaved = kursortDao.save(new Kursort("Saal Test1", true));
        kursorteTestdata.add(kursorteaved);

        kursorteaved = kursortDao.save(new Kursort("Saal Test2", true));
        kursorteTestdata.add(kursorteaved);

        entityManager.getTransaction().commit();
        commandInvoker.closeSession();
    }

    private void deleteTestdata() {
        commandInvoker.openSession();
        EntityManager entityManager = commandInvoker.getEntityManager();
        entityManager.getTransaction().begin();

        KursortDao kursortDao = new KursortDao(entityManager);

        for (Kursort kursort : kursorteTestdata) {
            Kursort kursortToBeRemoved = kursortDao.findById(kursort.getKursortId());
            kursortDao.remove(kursortToBeRemoved);
        }

        entityManager.getTransaction().commit();
        commandInvoker.closeSession();
    }
}