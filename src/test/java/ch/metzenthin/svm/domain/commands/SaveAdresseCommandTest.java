package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.AdresseDao;
import ch.metzenthin.svm.persistence.entities.Adresse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.junit.Assert.*;

/**
 * @author Hans Stamm
 */
public class SaveAdresseCommandTest {

    private CommandInvoker commandInvoker;
    private EntityManagerFactory entityManagerFactory;

    @Before
    public void setUp() throws Exception {
        entityManagerFactory = Persistence.createEntityManagerFactory("svm");
        commandInvoker = new CommandInvokerImpl(entityManagerFactory);
    }

    @After
    public void tearDown() throws Exception {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }

    @Test
    public void testExecute() throws Exception {
        Adresse adresse = new Adresse("Buechackerstrasse", "4", "8234", "Stetten", "052 643 38 48");
        SaveAdresseCommand saveAdresseCommand = new SaveAdresseCommand(adresse);
        commandInvoker.executeCommand(saveAdresseCommand);
        Adresse adresseFound = saveAdresseCommand.getSavedAdresse();
        assertEquals("Strasse not correct", "Buechackerstrasse", adresseFound.getStrasse());

        // delete
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            AdresseDao adresseDao = new AdresseDao(entityManager);
            Adresse adresseToBeRemoved = adresseDao.findById(adresseFound.getAdresseId());
            adresseDao.remove(adresseToBeRemoved);
            entityManager.getTransaction().commit();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }
}