package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Anrede;
import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.daos.MitarbeiterDao;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import jakarta.persistence.EntityManager;
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
public class SaveOrUpdateMitarbeiterCommandTest {

    private final MitarbeiterDao mitarbeiterDao = new MitarbeiterDao();

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

        // Vor Transaktionen
        assertFalse(checkIfMitarbeiterAvailable("RoosTest", "Noémie", "nroos@gmx.ch", "Rebwiesenstrasse"));
        assertFalse(checkIfMitarbeiterAvailable("DelleyTest", "Nathalie", "ndelley@sunrise.ch", "Im Schilf"));

        List<Mitarbeiter> mitarbeitersSaved = new ArrayList<>();

        // Mitarbeiter hinzufügen
        Mitarbeiter mitarbeiter1 = new Mitarbeiter(Anrede.FRAU, "Noémie", "RoosTest", new GregorianCalendar(1994, Calendar.MARCH, 18), "043 111 11 11", "076 384 45 35", "nroos@gmx.ch", "756.3943.8722.22", "CH31 8123 9000 0012 4568 9", true, "Mi, Fr, Sa", null, true);
        Adresse adresse1 = new Adresse("Rebwiesenstrasse", "77", "8702", "Zollikon");
        mitarbeiter1.setAdresse(adresse1);
        SaveOrUpdateMitarbeiterCommand saveOrUpdateMitarbeiterCommand = new SaveOrUpdateMitarbeiterCommand(mitarbeiter1, adresse1, null, null, mitarbeitersSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateMitarbeiterCommand);
        assertEquals(1, mitarbeitersSaved.size());
        assertTrue(checkIfMitarbeiterAvailable("RoosTest", "Noémie", "nroos@gmx.ch", "Rebwiesenstrasse"));
        assertFalse(checkIfMitarbeiterAvailable("DelleyTest", "Nathalie", "ndelley@sunrise.ch", "Im Schilf"));

        // Weitere Mitarbeiter hinzufügen
        Mitarbeiter mitarbeiter2 = new Mitarbeiter(Anrede.FRAU, "Nathalie", "DelleyTest", new GregorianCalendar(1971, Calendar.DECEMBER, 16), "044 261 27 20", "076 338 05 36", "ndelley@sunrise.ch", "756.8274.3263.17", "CH31 8123 9000 0022 4568 9", true, "Mi, Fr, Sa", null, true);
        Adresse adresse2 = new Adresse("Im Schilf", "7", "8044", "Zürich");
        mitarbeiter2.setAdresse(adresse2);
        saveOrUpdateMitarbeiterCommand = new SaveOrUpdateMitarbeiterCommand(mitarbeiter2, adresse2, null, null, mitarbeitersSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateMitarbeiterCommand);
        assertEquals(2, mitarbeitersSaved.size());
        // Alphabetisch geordnet?
        assertEquals("DelleyTest", mitarbeitersSaved.get(0).getNachname());
        assertEquals("RoosTest", mitarbeitersSaved.get(1).getNachname());
        assertTrue(checkIfMitarbeiterAvailable("RoosTest", "Noémie", "nroos@gmx.ch", "Rebwiesenstrasse"));
        assertTrue(checkIfMitarbeiterAvailable("DelleyTest", "Nathalie", "ndelley@sunrise.ch", "Im Schilf"));

        // Mitarbeiter bearbeiten (neue E-Mail, neue Strasse)
        Mitarbeiter mitarbeiter1Modif = new Mitarbeiter(Anrede.FRAU, "Noémie", "RoosTest", new GregorianCalendar(1994, Calendar.MARCH, 18), "043 111 11 11", "076 384 45 35", "nroos@gmail.com", "756.3943.8722.22", "CH31 8123 9000 0012 4568 9", true, "Mi, Fr, Sa", null, true);
        Adresse adresse1Modif = new Adresse("Rebwiesenweg", "3", "8702", "Zollikon");
        mitarbeiter1.setAdresse(adresse1Modif);
        saveOrUpdateMitarbeiterCommand = new SaveOrUpdateMitarbeiterCommand(mitarbeiter1Modif, adresse1Modif, null, mitarbeiter1, mitarbeitersSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateMitarbeiterCommand);
        assertEquals(2, mitarbeitersSaved.size());
        assertFalse(checkIfMitarbeiterAvailable("RoosTest", "Noémie", "nroos@gmx.ch", "Rebwiesenstrasse"));
        assertTrue(checkIfMitarbeiterAvailable("RoosTest", "Noémie", "nroos@gmail.com", "Rebwiesenweg"));
        assertTrue(checkIfMitarbeiterAvailable("DelleyTest", "Nathalie", "ndelley@sunrise.ch", "Im Schilf"));

        // Testdaten löschen
        EntityManager entityManager = db.getCurrentEntityManager();
        entityManager.getTransaction().begin();
        for (Mitarbeiter mitarbeiter : mitarbeitersSaved) {
            Mitarbeiter mitarbeiterToBeDeleted = mitarbeiterDao.findById(mitarbeiter.getPersonId());
            if (mitarbeiterToBeDeleted != null) {
                mitarbeiterDao.remove(mitarbeiterToBeDeleted);
            }
        }
        entityManager.getTransaction().commit();
    }

    private boolean checkIfMitarbeiterAvailable(String nachname, String vorname, String email, String strasse) {
        FindAllMitarbeitersCommand findAllMitarbeitersCommand = new FindAllMitarbeitersCommand();
        commandInvoker.executeCommand(findAllMitarbeitersCommand);
        List<Mitarbeiter> lehrkraefteAll = findAllMitarbeitersCommand.getMitarbeitersAll();
        for (Mitarbeiter mitarbeiter : lehrkraefteAll) {
            if (mitarbeiter.getNachname().equals(nachname) && mitarbeiter.getVorname().equals(vorname) && mitarbeiter.getEmail().equals(email) && mitarbeiter.getAdresse().getStrasse().equals(strasse)) {
                return true;
            }
        }
        return false;
    }
}