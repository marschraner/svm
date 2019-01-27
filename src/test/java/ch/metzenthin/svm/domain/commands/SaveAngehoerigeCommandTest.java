package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Anrede;
import ch.metzenthin.svm.persistence.daos.AngehoerigerDao;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;
import static org.junit.Assert.assertEquals;

/**
 * @author Hans Stamm
 */
public class SaveAngehoerigeCommandTest {

    private CommandInvoker commandInvoker = new CommandInvokerImpl();

    @Before
    public void setUp() throws Exception {
        createSvmPropertiesFileDefault();
    }

    @After
    public void tearDown() throws Exception {
        commandInvoker.closeSessionAndEntityManagerFactory();
    }

    @Test
    public void testExecute() throws Exception {

        List<Angehoeriger> angehoerige = new ArrayList<>();

        Angehoeriger angehoeriger0 = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", "044 491 69 33", null, null);
        Adresse adresse = new Adresse("Hohenklingenstrasse", "15", "8049", "Zürich");
        angehoeriger0.setAdresse(adresse);
        angehoerige.add(angehoeriger0);

        // Second Angehoeriger with the same address
        Angehoeriger angehoeriger1 = new Angehoeriger(Anrede.FRAU, "Regula", "Rösle", "044 491 69 33", null, null);
        angehoeriger1.setAdresse(adresse);
        angehoerige.add(angehoeriger1);

        SaveAngehoerigeCommand saveAngehoerigeCommand = new SaveAngehoerigeCommand(angehoerige);
        commandInvoker.executeCommandAsTransactionWithOpenAndClose(saveAngehoerigeCommand);
        List<Angehoeriger> savedAngehoerige = saveAngehoerigeCommand.getSavedAngehoerige();

        Angehoeriger savedAngehoeriger0 = savedAngehoerige.get(0);
        assertEquals("Vorname not found", "Eugen", savedAngehoeriger0.getVorname());
        assertEquals("Strasse not found", "Hohenklingenstrasse", savedAngehoeriger0.getAdresse().getStrasse());

        Angehoeriger savedAngehoeriger1 = savedAngehoerige.get(1);
        assertEquals("Vorname not found", "Regula", savedAngehoeriger1.getVorname());
        assertEquals("Strasse not found", "Hohenklingenstrasse", savedAngehoeriger1.getAdresse().getStrasse());

        // Do both Angehoeriger have the same adresseId?
        assertEquals("Adresse_id not equal", savedAngehoeriger0.getAdresse().getAdresseId(), savedAngehoeriger1.getAdresse().getAdresseId());

        // Delete
        commandInvoker.openSession();
        EntityManager entityManager = commandInvoker.getEntityManager();
        entityManager.getTransaction().begin();

        AngehoerigerDao angehoerigerDao = new AngehoerigerDao(entityManager);

        Angehoeriger angehoerigerToBeRemoved0 = angehoerigerDao.findById(savedAngehoeriger0.getPersonId());
        angehoerigerDao.remove(angehoerigerToBeRemoved0);

        Angehoeriger angehoerigerToBeRemoved1 = angehoerigerDao.findById(savedAngehoeriger1.getPersonId());
        angehoerigerDao.remove(angehoerigerToBeRemoved1);

        entityManager.getTransaction().commit();
    }
}