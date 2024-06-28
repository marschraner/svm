package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.daos.KurstypDao;
import ch.metzenthin.svm.persistence.entities.Kurstyp;
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
public class FindAllKurstypenCommandTest {

    private final KurstypDao kurstypDao = new KurstypDao();
    private final Set<Kurstyp> kurstypenTestdata = new HashSet<>();

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
        FindAllKurstypenCommand findAllKurstypenCommand = new FindAllKurstypenCommand();
        commandInvoker.executeCommand(findAllKurstypenCommand);

        List<Kurstyp> kurstypenFound = findAllKurstypenCommand.getKurstypenAll();
        assertTrue(kurstypenFound.size() >= 2);
        boolean found1 = false;
        boolean found2 = false;
        for (Kurstyp kurstyp : kurstypenFound) {
            if (kurstyp.getBezeichnung().equals("Kurs Test1")) {
                found1 = true;
            }
            if (kurstyp.getBezeichnung().equals("Kurs Test2")) {
                found2 = true;
            }
        }
        assertTrue(found1);
        assertTrue(found2);
    }

    private void createTestdata() {
        EntityManager entityManager = db.getCurrentEntityManager();
        entityManager.getTransaction().begin();

        Kurstyp kurstypenSaved = kurstypDao.save(new Kurstyp("Kurs Test1", true));
        kurstypenTestdata.add(kurstypenSaved);

        kurstypenSaved = kurstypDao.save(new Kurstyp("Kurs Test2", true));
        kurstypenTestdata.add(kurstypenSaved);

        entityManager.getTransaction().commit();
        db.closeSession();
    }

    private void deleteTestdata() {
        EntityManager entityManager = db.getCurrentEntityManager();
        entityManager.getTransaction().begin();

        for (Kurstyp kurstyp : kurstypenTestdata) {
            Kurstyp kurstypToBeRemoved = kurstypDao.findById(kurstyp.getKurstypId());
            kurstypDao.remove(kurstypToBeRemoved);
        }

        entityManager.getTransaction().commit();
        db.closeSession();
    }
}