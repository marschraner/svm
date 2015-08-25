package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Anrede;
import ch.metzenthin.svm.persistence.daos.LehrkraftDao;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Lehrkraft;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.*;

import static org.junit.Assert.assertTrue;

/**
 * @author Martin Schraner
 */
public class FindAllLehrkraefteCommandTest {

    private CommandInvoker commandInvoker = new CommandInvokerImpl();
    private EntityManagerFactory entityManagerFactory;
    private Set<Lehrkraft> lehrkraefteTestdata = new HashSet<>();

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
        FindAllLehrkraefteCommand findAllLehrkraefteCommand = new FindAllLehrkraefteCommand();
            commandInvoker.executeCommandAsTransactionWithOpenAndClose(findAllLehrkraefteCommand);

        List<Lehrkraft> lehrkraefteFound = findAllLehrkraefteCommand.getLehrkraefteAll();
        assertTrue(lehrkraefteFound.size() >= 2);
        boolean found1 = false;
        boolean found2 = false;
        for (Lehrkraft lehrkraft : lehrkraefteFound) {
            if (lehrkraft.getVorname().equals("NoémiTest")) {
                found1 = true;
            }
            if (lehrkraft.getVorname().equals("NathalieTest")) {
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

            LehrkraftDao lehrkraftDao = new LehrkraftDao(entityManager);

            Lehrkraft lehrkraft1 = new Lehrkraft(Anrede.FRAU, "NoémiTest", "RoosTest", new GregorianCalendar(1994, Calendar.MARCH, 18), "044 391 45 35", "076 384 45 35", "nroos@gmx.ch", "756.3943.8722.22", "Mi, Fr, Sa", true);
            Adresse adresse1 = new Adresse("Rebwiesenstrasse", "54", "8702", "Zollikon");
            lehrkraft1.setAdresse(adresse1);
            Lehrkraft lehrkraftSaved = lehrkraftDao.save(lehrkraft1);
            lehrkraefteTestdata.add(lehrkraftSaved);

            Lehrkraft lehrkraft2 = new Lehrkraft(Anrede.FRAU, "NathalieTest", "DelleyTest", new GregorianCalendar(1971, Calendar.DECEMBER, 16), "044 261 27 20", "076 338 05 36", "ndelley@sunrise.ch", "756.8274.3263.17", "Mi, Fr, Sa", true);
            Adresse adresse2 = new Adresse("Im Schilf", "7", "8044", "Zürich");
            lehrkraft2.setAdresse(adresse2);
            lehrkraftSaved = lehrkraftDao.save(lehrkraft2);
            lehrkraefteTestdata.add(lehrkraftSaved);

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

            LehrkraftDao lehrkraftDao = new LehrkraftDao(entityManager);

            for (Lehrkraft lehrkraft : lehrkraefteTestdata) {
                Lehrkraft lehrkraftToBeRemoved = lehrkraftDao.findById(lehrkraft.getPersonId());
                lehrkraftDao.remove(lehrkraftToBeRemoved);
            }

            entityManager.getTransaction().commit();

        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }
}