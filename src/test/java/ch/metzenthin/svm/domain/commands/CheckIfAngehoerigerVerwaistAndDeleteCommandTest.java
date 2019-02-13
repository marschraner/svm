package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Anrede;
import ch.metzenthin.svm.common.dataTypes.Geschlecht;
import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.daos.AngehoerigerDao;
import ch.metzenthin.svm.persistence.daos.SchuelerDao;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Anmeldung;
import ch.metzenthin.svm.persistence.entities.Schueler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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

    private final SchuelerDao schuelerDao = new SchuelerDao();
    private final AngehoerigerDao angehoerigerDao = new AngehoerigerDao();

    private DB db;
    private CommandInvoker commandInvoker;

    @Before
    public void setUp() throws Exception {
        createSvmPropertiesFileDefault();
        db = DBFactory.getInstance();
        commandInvoker = new CommandInvokerImpl();
    }

    @After
    public void tearDown() throws Exception {
        db.closeSession();
    }

    @Test
    public void testExecute_VerwaisteAngehoerige() {

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
        assertNull(schuelerDao.findById(schuelerId));
        assertNull(angehoerigerDao.findById(vaterId));
        assertNull(angehoerigerDao.findById(mutterId));
        assertNull(angehoerigerDao.findById(rechnungsempfaengerId));
    }
}