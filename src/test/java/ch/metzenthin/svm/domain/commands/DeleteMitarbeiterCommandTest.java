package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.datatypes.Anrede;
import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * @author Martin Schraner
 */
public class DeleteMitarbeiterCommandTest {

    private DB db;
    private CommandInvoker commandInvoker;

    @Before
    public void setUp() {
        createSvmPropertiesFileDefault();
        db = DBFactory.getInstance();
        commandInvoker = new CommandInvokerImpl();
    }

    @After
    public void tearDown() {
        db.closeSession();
    }

    @Test
    public void testExecute() {

        List<Mitarbeiter> lehrkraefteSaved = new ArrayList<>();

        // 2 Lehrkräfte erfassen
        Mitarbeiter mitarbeiter1 = new Mitarbeiter(Anrede.FRAU, "Noémie", "RoosTest", new GregorianCalendar(1994, Calendar.MARCH, 18), "043 111 11 11", "076 384 45 35", "nroos@gmx.ch", "756.3943.8722.22", "CH31 8123 9000 0012 4568 9", true, "Mi, Fr, Sa", null, true);
        Adresse adresse1 = new Adresse("Rebwiesenstrasse", "77", "8702", "Zollikon");
        mitarbeiter1.setAdresse(adresse1);

        Mitarbeiter mitarbeiter2 = new Mitarbeiter(Anrede.FRAU, "Nathalie", "DelleyTest", new GregorianCalendar(1971, Calendar.DECEMBER, 16), "044 261 27 20", "076 338 05 36", "ndelley@sunrise.ch", "756.8274.3263.17", "CH31 8123 9000 0232 4568 9", true, "Mi, Fr, Sa", null, true);
        Adresse adresse2 = new Adresse("Im Schilf", "7", "8044", "Zürich");
        mitarbeiter2.setAdresse(adresse2);

        SaveOrUpdateMitarbeiterCommand saveOrUpdateMitarbeiterCommand = new SaveOrUpdateMitarbeiterCommand(mitarbeiter1, adresse1, null, null, lehrkraefteSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateMitarbeiterCommand);

        saveOrUpdateMitarbeiterCommand = new SaveOrUpdateMitarbeiterCommand(mitarbeiter2, adresse2, null, null, lehrkraefteSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateMitarbeiterCommand);

        // Lehrkraefte löschen
        DeleteMitarbeiterCommand deleteMitarbeiterCommand = new DeleteMitarbeiterCommand(lehrkraefteSaved, 1);
        commandInvoker.executeCommandAsTransaction(deleteMitarbeiterCommand);
        assertEquals(DeleteMitarbeiterCommand.Result.LOESCHEN_ERFOLGREICH, deleteMitarbeiterCommand.getResult());
        assertEquals(1, lehrkraefteSaved.size());

        deleteMitarbeiterCommand = new DeleteMitarbeiterCommand(lehrkraefteSaved, 0);
        commandInvoker.executeCommandAsTransaction(deleteMitarbeiterCommand);
        assertEquals(DeleteMitarbeiterCommand.Result.LOESCHEN_ERFOLGREICH, deleteMitarbeiterCommand.getResult());
        assertTrue(lehrkraefteSaved.isEmpty());
    }
}