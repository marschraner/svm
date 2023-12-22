package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.daos.KursortDao;
import ch.metzenthin.svm.persistence.entities.Kursort;
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
public class FindAllKursorteCommandTest {

    private final KursortDao kursortDao = new KursortDao();
    private final Set<Kursort> kursorteTestdata = new HashSet<>();

    private DB db;
    private CommandInvoker commandInvoker;

    @Before
    public void setUp() throws Exception {
        createSvmPropertiesFileDefault();
        db = DBFactory.getInstance();
        commandInvoker = new CommandInvokerImpl();
        createTestdata();
    }

    @After
    public void tearDown() throws Exception {
        deleteTestdata();
        db.closeSession();
    }

    @Test
    public void testExecute() {
        FindAllKursorteCommand findAllKursorteCommand = new FindAllKursorteCommand();
        commandInvoker.executeCommand(findAllKursorteCommand);

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
        EntityManager entityManager = db.getCurrentEntityManager();
        entityManager.getTransaction().begin();

        Kursort kursorteSaved = kursortDao.save(new Kursort("Saal Test1", true));
        kursorteTestdata.add(kursorteSaved);

        kursorteSaved = kursortDao.save(new Kursort("Saal Test2", true));
        kursorteTestdata.add(kursorteSaved);

        entityManager.getTransaction().commit();
        db.closeSession();
    }

    private void deleteTestdata() {
        EntityManager entityManager = db.getCurrentEntityManager();
        entityManager.getTransaction().begin();

        for (Kursort kursort : kursorteTestdata) {
            Kursort kursortToBeRemoved = kursortDao.findById(kursort.getKursortId());
            kursortDao.remove(kursortToBeRemoved);
        }

        entityManager.getTransaction().commit();
        db.closeSession();
    }
}