package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.KurstypDao;
import ch.metzenthin.svm.persistence.entities.Kurstyp;
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
public class FindAllKurstypenCommandTest {

    private CommandInvoker commandInvoker = new CommandInvokerImpl();
    private EntityManagerFactory entityManagerFactory;
    private Set<Kurstyp> kurstypenTestdata = new HashSet<>();

    @Before
    public void setUp() throws Exception {
        entityManagerFactory = Persistence.createEntityManagerFactory("svmtest");
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
        FindAllKurstypenCommand findAllKurstypenCommand = new FindAllKurstypenCommand();
        commandInvoker.executeCommandAsTransactionWithOpenAndCloseSvmTest(findAllKurstypenCommand);

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
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            KurstypDao kurstypDao = new KurstypDao(entityManager);

            Kurstyp kurstypenaved = kurstypDao.save(new Kurstyp("Kurs Test1", true));
            kurstypenTestdata.add(kurstypenaved);

            kurstypenaved = kurstypDao.save(new Kurstyp("Kurs Test2", true));
            kurstypenTestdata.add(kurstypenaved);

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

            KurstypDao kurstypDao = new KurstypDao(entityManager);

            for (Kurstyp kurstyp : kurstypenTestdata) {
                Kurstyp kurstypToBeRemoved = kurstypDao.findById(kurstyp.getKurstypId());
                kurstypDao.remove(kurstypToBeRemoved);
            }

            entityManager.getTransaction().commit();

        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }
}