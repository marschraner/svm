package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Anrede;
import ch.metzenthin.svm.common.dataTypes.Geschlecht;
import ch.metzenthin.svm.common.utils.PersistenceProperties;
import ch.metzenthin.svm.persistence.daos.AngehoerigerDao;
import ch.metzenthin.svm.persistence.daos.SchuelerDao;
import ch.metzenthin.svm.persistence.entities.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;
import static org.junit.Assert.*;

/**
 * @author Martin Schraner
 */
public class CheckIfAngehoerigerVerwaistAndDeleteCommandTest {

    private CommandInvoker commandInvoker = new CommandInvokerImpl();
    private EntityManagerFactory entityManagerFactory;

    @Before
    public void setUp() throws Exception {
        createSvmPropertiesFileDefault();
        entityManagerFactory = Persistence.createEntityManagerFactory("svm", PersistenceProperties.getPersistenceProperties());
    }

    @After
    public void tearDown() throws Exception {
        commandInvoker.close();
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }

    @Test
    public void testExecute_VerwaisteAngehoerige() throws Exception {

        List<Angehoeriger> angehoerigere = new ArrayList<>();

        // Angehoerige ohne Schueler erfassen
        Angehoeriger vater = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", "044 491 69 33", null, null);
        Adresse adresse = new Adresse("Hohenklingenstrasse", "15", "8049", "Zürich");
        vater.setAdresse(adresse);
        angehoerigere.add(vater);

        Angehoeriger mutter = new Angehoeriger(Anrede.FRAU, "Regula", "Rösle", "044 491 69 33", null, null);
        mutter.setAdresse(adresse);
        angehoerigere.add(mutter);

        Angehoeriger rechnungsempfaenger = new Angehoeriger(Anrede.FRAU, "Katharina", "Schraner", null, null, null);
        rechnungsempfaenger.setAdresse(adresse);
        angehoerigere.add(rechnungsempfaenger);

        SaveAngehoerigeCommand saveAngehoerigeCommand = new SaveAngehoerigeCommand(angehoerigere);
        commandInvoker.executeCommandAsTransaction(saveAngehoerigeCommand);
        List<Angehoeriger> angehoerigereSaved = saveAngehoerigeCommand.getSavedAngehoerige();

        assertEquals(3, angehoerigereSaved.size());

        // Verwaiste Angehörige löschen
        CheckIfAngehoerigerVerwaistAndDeleteCommand checkIfAngehoerigerVerwaistAndDeleteCommand = new CheckIfAngehoerigerVerwaistAndDeleteCommand(angehoerigereSaved.get(0));
        commandInvoker.executeCommandAsTransaction(checkIfAngehoerigerVerwaistAndDeleteCommand);
        assertTrue(checkIfAngehoerigerVerwaistAndDeleteCommand.isDeleted());
        checkIfAngehoerigerVerwaistAndDeleteCommand = new CheckIfAngehoerigerVerwaistAndDeleteCommand(angehoerigereSaved.get(1));
        commandInvoker.executeCommandAsTransaction(checkIfAngehoerigerVerwaistAndDeleteCommand);
        assertTrue(checkIfAngehoerigerVerwaistAndDeleteCommand.isDeleted());
        checkIfAngehoerigerVerwaistAndDeleteCommand = new CheckIfAngehoerigerVerwaistAndDeleteCommand(angehoerigereSaved.get(2));
        commandInvoker.executeCommandAsTransaction(checkIfAngehoerigerVerwaistAndDeleteCommand);
        assertTrue(checkIfAngehoerigerVerwaistAndDeleteCommand.isDeleted());


        // Angehöriger einem Schüler zuweisen
        adresse = new Adresse("Hohenklingenstrasse", "15", "8049", "Zürich");
        vater = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", "044 491 69 33", null, null);
        vater.setAdresse(adresse);

        Schueler schueler = new Schueler("Jana", "Rösle", new GregorianCalendar(2012, Calendar.JULY, 24), "044 491 69 33", null, null, Geschlecht.W, "Schwester von Valentin");
        schueler.setAdresse(adresse);
        schueler.addAnmeldung(new Anmeldung(new GregorianCalendar(2015, Calendar.MAY, 1), null));

        vater = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", "044 491 69 33", null, null);
        vater.setAdresse(adresse);
        schueler.setVater(vater);

        mutter = new Angehoeriger(Anrede.FRAU, "Regula", "Rösle", "044 491 69 33", null, null);
        mutter.setAdresse(adresse);
        schueler.setMutter(mutter);

        rechnungsempfaenger = new Angehoeriger(Anrede.FRAU, "Katharina", "Schraner", null, null, null);
        rechnungsempfaenger.setAdresse(adresse);
        schueler.setRechnungsempfaenger(rechnungsempfaenger);

        SaveSchuelerCommand saveSchuelerCommand = new SaveSchuelerCommand(schueler);
        commandInvoker.executeCommandAsTransaction(saveSchuelerCommand);
        Schueler schuelerSaved = saveSchuelerCommand.getSavedSchueler();
        int schuelerId = schuelerSaved.getPersonId();
        int vaterId = schuelerSaved.getVater().getPersonId();
        int mutterId = schuelerSaved.getMutter().getPersonId();
        int rechnungsempfaengerId = schuelerSaved.getRechnungsempfaenger().getPersonId();

        // Angehörige versuchen zu löschen -> nicht möglich
        checkIfAngehoerigerVerwaistAndDeleteCommand = new CheckIfAngehoerigerVerwaistAndDeleteCommand(schuelerSaved.getVater());
        commandInvoker.executeCommandAsTransaction(checkIfAngehoerigerVerwaistAndDeleteCommand);
        assertFalse(checkIfAngehoerigerVerwaistAndDeleteCommand.isDeleted());
        checkIfAngehoerigerVerwaistAndDeleteCommand = new CheckIfAngehoerigerVerwaistAndDeleteCommand(schuelerSaved.getMutter());
        commandInvoker.executeCommandAsTransaction(checkIfAngehoerigerVerwaistAndDeleteCommand);
        assertFalse(checkIfAngehoerigerVerwaistAndDeleteCommand.isDeleted());
        checkIfAngehoerigerVerwaistAndDeleteCommand = new CheckIfAngehoerigerVerwaistAndDeleteCommand(schuelerSaved.getRechnungsempfaenger());
        commandInvoker.executeCommandAsTransaction(checkIfAngehoerigerVerwaistAndDeleteCommand);
        assertFalse(checkIfAngehoerigerVerwaistAndDeleteCommand.isDeleted());

        // Schüler löschen
        DeleteSchuelerCommand deleteSchuelerCommand = new DeleteSchuelerCommand(schuelerSaved);
        commandInvoker.executeCommandAsTransaction(deleteSchuelerCommand);
        assertEquals(DeleteSchuelerCommand.Result.LOESCHEN_ERFOLGREICH, deleteSchuelerCommand.getResult());

        // Prüfen, ob Schüler und Vater gelöscht
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            SchuelerDao schuelerDao = new SchuelerDao(entityManager);
            assertNull(schuelerDao.findById(schuelerId));
            AngehoerigerDao angehoerigerDao = new AngehoerigerDao(entityManager);
            assertNull(angehoerigerDao.findById(vaterId));
            assertNull(angehoerigerDao.findById(mutterId));
            assertNull(angehoerigerDao.findById(rechnungsempfaengerId));
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }

    }
}