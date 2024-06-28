package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Anrede;
import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.daos.AngehoerigerDao;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import jakarta.persistence.EntityManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Martin Schraner.
 */
public class CheckAngehoerigerBereitsInDatenbankCommandTest {

    private final AngehoerigerDao angehoerigerDao = new AngehoerigerDao();

    private DB db;
    private CommandInvoker commandInvoker;
    private Angehoeriger angehoerigerTestdata0;
    private Angehoeriger angehoerigerTestdata1;
    private Angehoeriger angehoerigerTestdata2;

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
    public void testExecute_NICHT_IN_DATENBANK() {
        Angehoeriger angehoeriger = new Angehoeriger(Anrede.HERR, "Armin", "Bruggisser", "056 426 69 15", null, null, false);
        Adresse adresse = new Adresse("Wiesenstrasse", "5", "5430", "Wettingen");
        angehoeriger.setAdresse(adresse);

        CheckAngehoerigerBereitsInDatenbankCommand checkAngehoerigerBereitsInDatenbankCommand = new CheckAngehoerigerBereitsInDatenbankCommand(angehoeriger);
        commandInvoker.executeCommand(checkAngehoerigerBereitsInDatenbankCommand);

        assertEquals("Angehöriger in Datenbank", CheckAngehoerigerBereitsInDatenbankCommand.Result.NICHT_IN_DATENBANK, checkAngehoerigerBereitsInDatenbankCommand.getResult());
    }

    @Test
    public void testExecute_EIN_EINTRAG_PASST() {
        Angehoeriger angehoeriger = new Angehoeriger(Anrede.HERR, "Andreas", "Bruggisser", null, null, null, false);  // ohne Festnetz
        Adresse adresse = new Adresse("Wiesenstrasse", "5", "5430", "Wettingen");
        angehoeriger.setAdresse(adresse);

        CheckAngehoerigerBereitsInDatenbankCommand checkAngehoerigerBereitsInDatenbankCommand = new CheckAngehoerigerBereitsInDatenbankCommand(angehoeriger);
        commandInvoker.executeCommand(checkAngehoerigerBereitsInDatenbankCommand);

        assertEquals("Angehöriger nicht in Datenbank", CheckAngehoerigerBereitsInDatenbankCommand.Result.EIN_EINTRAG_PASST, checkAngehoerigerBereitsInDatenbankCommand.getResult());
        Angehoeriger angehoerigerFound = checkAngehoerigerBereitsInDatenbankCommand.getAngehoerigerFound();
        assertNotNull(angehoerigerFound);
        System.out.println("In der Datenbank wurde ein Eintrag gefunden, der auf die erfassten Angaben passt: " + angehoerigerFound);
    }

    @Test
    public void testExecute_MEHRERE_EINTRAEGE_PASSEN() {
        Angehoeriger angehoeriger = new Angehoeriger(Anrede.HERR, "Andreas", "Bruggisser", null, null, null, false);  // ohne Adresse

        CheckAngehoerigerBereitsInDatenbankCommand checkAngehoerigerBereitsInDatenbankCommand = new CheckAngehoerigerBereitsInDatenbankCommand(angehoeriger);
        commandInvoker.executeCommand(checkAngehoerigerBereitsInDatenbankCommand);
        assertEquals("Angehöriger nicht in Datenbank", CheckAngehoerigerBereitsInDatenbankCommand.Result.MEHRERE_EINTRAEGE_PASSEN, checkAngehoerigerBereitsInDatenbankCommand.getResult());
        List<Angehoeriger> angehoerigerFoundList = checkAngehoerigerBereitsInDatenbankCommand.getAngehoerigerFoundList();
        assertNotNull(angehoerigerFoundList);
        System.out.println("In der Datenbank wurden mehrere Einträge gefunden, die auf die erfassten Angaben passen: ");
        for (Angehoeriger ang : angehoerigerFoundList) {
            System.out.println(ang);
        }
    }

    @Test
    public void testExecute_EIN_EINTRAG_PASST_TEILWEISE() {
        Angehoeriger angehoeriger = new Angehoeriger(Anrede.FRAU, "Hanny", "Bruggisser", "056 426 69 15", null, null, true);
        Adresse adresse1 = new Adresse("Wiesenstrasse", "55", "5430", "Wettingen");   // andere Hausnummer
        angehoeriger.setAdresse(adresse1);

        CheckAngehoerigerBereitsInDatenbankCommand checkAngehoerigerBereitsInDatenbankCommand = new CheckAngehoerigerBereitsInDatenbankCommand(angehoeriger);
        commandInvoker.executeCommand(checkAngehoerigerBereitsInDatenbankCommand);

        assertEquals("Angehöriger nicht in Datenbank", CheckAngehoerigerBereitsInDatenbankCommand.Result.EIN_EINTRAG_GLEICHER_NAME_ANDERE_ATTRIBUTE, checkAngehoerigerBereitsInDatenbankCommand.getResult());
        Angehoeriger angehoerigerFound = checkAngehoerigerBereitsInDatenbankCommand.getAngehoerigerFound();
        assertNotNull(angehoerigerFound);
        System.out.println("In der Datenbank wurde ein Eintrag gefunden, der mit den erfassten Angaben teilweise übereinstimmt: " + angehoerigerFound);
    }

    @Test
    public void testExecute_MEHRERE_EINTRAEGE_PASSEN_TEILWEISE() {
        Angehoeriger angehoeriger = new Angehoeriger(Anrede.HERR, "Andreas", "Bruggisser", null, null, null, false);
        Adresse adresse1 = new Adresse("Wiesenstrasse", "5", "8803", "Rüschlikon");  // anderer Ort
        angehoeriger.setAdresse(adresse1);

        CheckAngehoerigerBereitsInDatenbankCommand checkAngehoerigerBereitsInDatenbankCommand = new CheckAngehoerigerBereitsInDatenbankCommand(angehoeriger);
        commandInvoker.executeCommand(checkAngehoerigerBereitsInDatenbankCommand);

        assertEquals("Angehöriger nicht in Datenbank", CheckAngehoerigerBereitsInDatenbankCommand.Result.MEHRERE_EINTRAEGE_GLEICHER_NAME_ANDERE_ATTRIBUTE, checkAngehoerigerBereitsInDatenbankCommand.getResult());
        List<Angehoeriger> angehoerigerFoundList = checkAngehoerigerBereitsInDatenbankCommand.getAngehoerigerFoundList();
        assertNotNull(angehoerigerFoundList);
        System.out.println("In der Datenbank wurden mehrere Einträge gefunden, die mit den erfassten Angaben teilweise übereinstimmen: ");
        for (Angehoeriger ang : angehoerigerFoundList) {
            System.out.println(ang);
        }
    }

    private void createTestdata() {
        EntityManager entityManager = db.getCurrentEntityManager();
        entityManager.getTransaction().begin();

        Angehoeriger angehoeriger0 = new Angehoeriger(Anrede.HERR, "Andreas", "Bruggisser", "056 426 69 15", null, null, false);
        Adresse adresse0 = new Adresse("Wiesenstrasse", "5", "5430", "Wettingen");
        angehoeriger0.setAdresse(adresse0);
        angehoerigerTestdata0 = angehoerigerDao.save(angehoeriger0);

        Angehoeriger angehoeriger1 = new Angehoeriger(Anrede.HERR, "Andreas", "Bruggisser", "056 426 69 15", null, null, false);
        Adresse adresse1 = new Adresse("Freudenbergstrasse", "5", "8002", "Zürich");
        angehoeriger1.setAdresse(adresse1);
        angehoerigerTestdata1 = angehoerigerDao.save(angehoeriger1);

        Angehoeriger angehoeriger2 = new Angehoeriger(Anrede.FRAU, "Hanny", "Bruggisser", "056 426 69 15", null, null, false);
        angehoeriger2.setAdresse(adresse0);
        angehoerigerTestdata2 = angehoerigerDao.save(angehoeriger2);

        entityManager.getTransaction().commit();
        db.closeSession();
    }

    private void deleteTestdata() {
        EntityManager entityManager = db.getCurrentEntityManager();
        entityManager.getTransaction().begin();

        Angehoeriger angehoerigerToBeRemoved0 = angehoerigerDao.findById(angehoerigerTestdata0.getPersonId());
        angehoerigerDao.remove(angehoerigerToBeRemoved0);

        Angehoeriger angehoerigerToBeRemoved1 = angehoerigerDao.findById(angehoerigerTestdata1.getPersonId());
        angehoerigerDao.remove(angehoerigerToBeRemoved1);

        Angehoeriger angehoerigerToBeRemoved2 = angehoerigerDao.findById(angehoerigerTestdata2.getPersonId());
        angehoerigerDao.remove(angehoerigerToBeRemoved2);

        entityManager.getTransaction().commit();
        db.closeSession();
    }
}