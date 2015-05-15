package ch.metzenthin.svm.commands;

import ch.metzenthin.svm.model.entities.Adresse;
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
        Adresse adresse = new Adresse("Buechackerstrasse", 4, 8234, "Stetten", "052 643 38 48");
        SaveAdresseCommand saveAdresseCommand = new SaveAdresseCommand(adresse);
        commandInvoker.executeCommand(saveAdresseCommand);
        Adresse newAdresse = saveAdresseCommand.getSavedAdresse();
        assertEquals("", "Buechackerstrasse", newAdresse.getStrasse());

        // delete
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            Adresse delAdresse = entityManager.find(Adresse.class, newAdresse.getAdresseId());
            entityManager.remove(delAdresse);
            entityManager.getTransaction().commit();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }
}