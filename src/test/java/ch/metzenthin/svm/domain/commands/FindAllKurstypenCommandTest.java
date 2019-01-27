package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.KurstypDao;
import ch.metzenthin.svm.persistence.entities.Kurstyp;
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
public class FindAllKurstypenCommandTest {

    private CommandInvoker commandInvoker = new CommandInvokerImpl();
    private Set<Kurstyp> kurstypenTestdata = new HashSet<>();

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
        FindAllKurstypenCommand findAllKurstypenCommand = new FindAllKurstypenCommand();
        commandInvoker.executeCommandAsTransactionWithOpenAndClose(findAllKurstypenCommand);

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
        commandInvoker.openSession();
        EntityManager entityManager = commandInvoker.getEntityManager();
        entityManager.getTransaction().begin();

        KurstypDao kurstypDao = new KurstypDao(entityManager);

        Kurstyp kurstypenaved = kurstypDao.save(new Kurstyp("Kurs Test1", true));
        kurstypenTestdata.add(kurstypenaved);

        kurstypenaved = kurstypDao.save(new Kurstyp("Kurs Test2", true));
        kurstypenTestdata.add(kurstypenaved);

        entityManager.getTransaction().commit();
        commandInvoker.closeSession();
    }

    private void deleteTestdata() {
        commandInvoker.openSession();
        EntityManager entityManager = commandInvoker.getEntityManager();
        entityManager.getTransaction().begin();

        KurstypDao kurstypDao = new KurstypDao(entityManager);

        for (Kurstyp kurstyp : kurstypenTestdata) {
            Kurstyp kurstypToBeRemoved = kurstypDao.findById(kurstyp.getKurstypId());
            kurstypDao.remove(kurstypToBeRemoved);
        }

        entityManager.getTransaction().commit();
        commandInvoker.closeSession();
    }
}