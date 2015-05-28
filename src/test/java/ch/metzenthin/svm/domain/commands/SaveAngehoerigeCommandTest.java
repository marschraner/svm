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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Hans Stamm
 */
public class SaveAngehoerigeCommandTest {

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

        List<Angehoeriger> angehoerige = new ArrayList<>();

        Angehoeriger angehoeriger0 = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", null, null, null);
        Adresse adresse = new Adresse("Hohenklingenstrasse", "15", "8049", "Zürich", "044 491 69 33");
        angehoeriger0.setNewAdresse(adresse);
        angehoerige.add(angehoeriger0);

        // Second Angehoeriger with the same address
        Angehoeriger angehoeriger1 = new Angehoeriger(Anrede.FRAU, "Regula", "Rösle", null, null, null);
        angehoeriger1.setNewAdresse(adresse);
        angehoerige.add(angehoeriger1);

        SaveAngehoerigeCommand saveAngehoerigeCommand = new SaveAngehoerigeCommand(angehoerige);
        commandInvoker.executeCommand(saveAngehoerigeCommand);
        List<Angehoeriger> savedAngehoerige = saveAngehoerigeCommand.getSavedAngehoeriger();

        Angehoeriger savedAngehoeriger0 = savedAngehoerige.get(0);
        assertEquals("Vorname not found", "Eugen", savedAngehoeriger0.getVorname());
        assertEquals("Strasse not found", "Hohenklingenstrasse", savedAngehoeriger0.getAdresse().getStrasse());

        Angehoeriger savedAngehoeriger1 = savedAngehoerige.get(1);
        assertEquals("Vorname not found", "Regula", savedAngehoeriger1.getVorname());
        assertEquals("Strasse not found", "Hohenklingenstrasse", savedAngehoeriger1.getAdresse().getStrasse());

        // Do both Angehoeriger have the same adresseId?
        assertTrue("Adresse_id not equal", Objects.equals(savedAngehoeriger0.getAdresse().getAdresseId(), savedAngehoeriger1.getAdresse().getAdresseId()));

        // Delete
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            AngehoerigerDao angehoerigerDao = new AngehoerigerDao(entityManager);

            Angehoeriger angehoerigerToBeRemoved0 = angehoerigerDao.findById(savedAngehoeriger0.getPersonId());
            angehoerigerDao.remove(angehoerigerToBeRemoved0);

            Angehoeriger angehoerigerToBeRemoved1 = angehoerigerDao.findById(savedAngehoeriger1.getPersonId());
            angehoerigerDao.remove(angehoerigerToBeRemoved1);

            entityManager.getTransaction().commit();

        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }
}