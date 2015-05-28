package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.dataTypes.Anrede;
import ch.metzenthin.svm.persistence.daos.AngehoerigerDao;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.junit.Assert.*;

/**
 * Created by Martin Schraner on 27.05.15.
 */
public class CheckAngehoerigerBereitsInDatenbankCommandTest {

    private CommandInvoker commandInvoker;
    private EntityManagerFactory entityManagerFactory;
    private Angehoeriger angehoerigerTestdata0;
    private Angehoeriger angehoerigerTestdata1;
    private Angehoeriger angehoerigerTestdata2;

    @Before
    public void setUp() throws Exception {
        entityManagerFactory = Persistence.createEntityManagerFactory("svm");
        commandInvoker = new CommandInvokerImpl(entityManagerFactory);
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
    public void testExecute_NICHT_IN_DATENBANK() {
        Angehoeriger angehoeriger1 = new Angehoeriger(Anrede.HERR, "Armin", "Bruggisser", null, null, null);
        Adresse adresse1 = new Adresse("Wiesenstrasse", "5", "5430", "Wettingen", "056 426 69 15");
        angehoeriger1.setAdresse(adresse1);

        CheckAngehoerigerBereitsInDatenbankCommand checkAngehoerigerBereitsInDatenbankCommand = new CheckAngehoerigerBereitsInDatenbankCommand(angehoeriger1);
        commandInvoker.executeCommand(checkAngehoerigerBereitsInDatenbankCommand);

        assertEquals("Angehöriger in Datenbank", CheckAngehoerigerBereitsInDatenbankCommand.Result.NICHT_IN_DATENBANK, checkAngehoerigerBereitsInDatenbankCommand.getResult());
    }

    @Test
    public void testExecute_EIN_EINTRAG_PASST() {
        Angehoeriger angehoeriger1 = new Angehoeriger(Anrede.HERR, "Andreas", "Bruggisser", null, null, null);
        Adresse adresse1 = new Adresse("Wiesenstrasse", "5", "5430", "Wettingen", null);  // ohne Festnetz
        angehoeriger1.setAdresse(adresse1);

        CheckAngehoerigerBereitsInDatenbankCommand checkAngehoerigerBereitsInDatenbankCommand = new CheckAngehoerigerBereitsInDatenbankCommand(angehoeriger1);
        commandInvoker.executeCommand(checkAngehoerigerBereitsInDatenbankCommand);

        assertEquals("Angehöriger nicht in Datenbank", CheckAngehoerigerBereitsInDatenbankCommand.Result.EIN_EINTRAG_PASST, checkAngehoerigerBereitsInDatenbankCommand.getResult());
        System.out.println("In der Datenbank wurde ein Eintrag gefunden, der auf die erfassten Angaben passt: " + checkAngehoerigerBereitsInDatenbankCommand.getAngehoeriger());
    }

    @Test
    public void testExecute_EIN_EINTRAG_PASST_TEILWEISE() {
        Angehoeriger angehoeriger1 = new Angehoeriger(Anrede.HERR, "Hanny", "Bruggisser", null, null, null);
        Adresse adresse1 = new Adresse("Wiesenstrasse", "55", "5430", "Wettingen", "056 426 69 15");   // andere Hausnummer
        angehoeriger1.setAdresse(adresse1);

        CheckAngehoerigerBereitsInDatenbankCommand checkAngehoerigerBereitsInDatenbankCommand = new CheckAngehoerigerBereitsInDatenbankCommand(angehoeriger1);
        commandInvoker.executeCommand(checkAngehoerigerBereitsInDatenbankCommand);

        assertEquals("Angehöriger nicht in Datenbank", CheckAngehoerigerBereitsInDatenbankCommand.Result.EIN_EINTRAG_PASST_TEILWEISE, checkAngehoerigerBereitsInDatenbankCommand.getResult());
        System.out.println("In der Datenbank wurde ein Eintrag gefunden, der mit den erfassten Angaben teilweise übereinstimmt: " + checkAngehoerigerBereitsInDatenbankCommand.getAngehoeriger());
    }

    //TODO restliche Tests


    private void createTestdata() {
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            AngehoerigerDao angehoerigerDao = new AngehoerigerDao(entityManager);

            Angehoeriger angehoeriger0 = new Angehoeriger(Anrede.HERR, "Andreas", "Bruggisser", null, null, null);
            Adresse adresse0 = new Adresse("Wiesenstrasse", "5", "5430", "Wettingen", "056 426 69 15");
            angehoeriger0.setAdresse(adresse0);
            angehoerigerTestdata0 = angehoerigerDao.save(angehoeriger0);

            Angehoeriger angehoeriger1 = new Angehoeriger(Anrede.HERR, "Andreas", "Bruggisser", null, null, null);
            Adresse adresse1 = new Adresse("Freudenbergstrasse", "5", "8002", "Zürich", "056 426 69 15");
            angehoeriger1.setAdresse(adresse1);
            angehoerigerTestdata1 = angehoerigerDao.save(angehoeriger1);

            Angehoeriger angehoeriger2 = new Angehoeriger(Anrede.FRAU, "Hanny", "Bruggisser", null, null, null);
            angehoeriger2.setAdresse(adresse0);
            angehoerigerTestdata2 = angehoerigerDao.save(angehoeriger2);

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

            AngehoerigerDao angehoerigerDao = new AngehoerigerDao(entityManager);

            Angehoeriger angehoerigerToBeRemoved0 = angehoerigerDao.findById(angehoerigerTestdata0.getPersonId());
            angehoerigerDao.remove(angehoerigerToBeRemoved0);

            Angehoeriger angehoerigerToBeRemoved1 = angehoerigerDao.findById(angehoerigerTestdata1.getPersonId());
            angehoerigerDao.remove(angehoerigerToBeRemoved1);

            Angehoeriger angehoerigerToBeRemoved2 = angehoerigerDao.findById(angehoerigerTestdata2.getPersonId());
            angehoerigerDao.remove(angehoerigerToBeRemoved2);

            entityManager.getTransaction().commit();

        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }
}