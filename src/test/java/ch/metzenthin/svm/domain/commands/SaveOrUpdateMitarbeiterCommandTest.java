package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Anrede;
import ch.metzenthin.svm.common.utils.PersistenceProperties;
import ch.metzenthin.svm.persistence.daos.MitarbeiterDao;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
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
public class SaveOrUpdateMitarbeiterCommandTest {

    private CommandInvoker commandInvoker = new CommandInvokerImpl();
    private EntityManagerFactory entityManagerFactory;

    @Before
    public void setUp() throws Exception {
        createSvmPropertiesFileDefault();
        entityManagerFactory = Persistence.createEntityManagerFactory("svm", PersistenceProperties.getPersistenceProperties());
        commandInvoker.openSession();
    }

    @After
    public void tearDown() throws Exception {
        commandInvoker.closeSession();
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }

    @Test
    public void testExecute() throws Exception {

        // Vor Transaktionen
        assertFalse(checkIfLehrkraftAvailable("RoosTest", "Noémie", "nroos@gmx.ch", "Rebwiesenstrasse"));
        assertFalse(checkIfLehrkraftAvailable("DelleyTest", "Nathalie", "ndelley@sunrise.ch", "Im Schilf"));

        List<Mitarbeiter> lehrkraefteSaved = new ArrayList<>();

        // Lehrkraft hinzufügen
        Mitarbeiter mitarbeiter1 = new Mitarbeiter(Anrede.FRAU, "Noémie", "RoosTest", new GregorianCalendar(1994, Calendar.MARCH, 18), "043 111 11 11", "076 384 45 35", "nroos@gmx.ch", "756.3943.8722.22", "Mi, Fr, Sa", true);
        Adresse adresse1 = new Adresse("Rebwiesenstrasse", "77", "8702", "Zollikon");
        mitarbeiter1.setAdresse(adresse1);
        SaveOrUpdateMitarbeiterCommand saveOrUpdateMitarbeiterCommand = new SaveOrUpdateMitarbeiterCommand(mitarbeiter1, adresse1, null, lehrkraefteSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateMitarbeiterCommand);
        assertEquals(1, lehrkraefteSaved.size());
        assertTrue(checkIfLehrkraftAvailable("RoosTest", "Noémie", "nroos@gmx.ch", "Rebwiesenstrasse"));
        assertFalse(checkIfLehrkraftAvailable("DelleyTest", "Nathalie", "ndelley@sunrise.ch", "Im Schilf"));

        // Weitere Lehrkraft hinzufügen
        Mitarbeiter mitarbeiter2 = new Mitarbeiter(Anrede.FRAU, "Nathalie", "DelleyTest", new GregorianCalendar(1971, Calendar.DECEMBER, 16), "044 261 27 20", "076 338 05 36", "ndelley@sunrise.ch", "756.8274.3263.17", "Mi, Fr, Sa", true);
        Adresse adresse2 = new Adresse("Im Schilf", "7", "8044", "Zürich");
        mitarbeiter2.setAdresse(adresse2);
        saveOrUpdateMitarbeiterCommand = new SaveOrUpdateMitarbeiterCommand(mitarbeiter2, adresse2, null, lehrkraefteSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateMitarbeiterCommand);
        assertEquals(2, lehrkraefteSaved.size());
        // Alphabetisch geordnet?
        assertEquals("DelleyTest", lehrkraefteSaved.get(0).getNachname());
        assertEquals("RoosTest", lehrkraefteSaved.get(1).getNachname());
        assertTrue(checkIfLehrkraftAvailable("RoosTest", "Noémie", "nroos@gmx.ch", "Rebwiesenstrasse"));
        assertTrue(checkIfLehrkraftAvailable("DelleyTest", "Nathalie", "ndelley@sunrise.ch", "Im Schilf"));

        // Lehrkraft bearbeiten (neue Email, neue Strasse)
        Mitarbeiter mitarbeiter1Modif = new Mitarbeiter(Anrede.FRAU, "Noémie", "RoosTest", new GregorianCalendar(1994, Calendar.MARCH, 18), "043 111 11 11", "076 384 45 35", "nroos@gmail.com", "756.3943.8722.22", "Mi, Fr, Sa", true);
        Adresse adresse1Modif = new Adresse("Rebwiesenweg", "3", "8702", "Zollikon");
        mitarbeiter1.setAdresse(adresse1Modif);
        saveOrUpdateMitarbeiterCommand = new SaveOrUpdateMitarbeiterCommand(mitarbeiter1Modif, adresse1Modif, mitarbeiter1, lehrkraefteSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateMitarbeiterCommand);
        assertEquals(2, lehrkraefteSaved.size());
        assertFalse(checkIfLehrkraftAvailable("RoosTest", "Noémie", "nroos@gmx.ch", "Rebwiesenstrasse"));
        assertTrue(checkIfLehrkraftAvailable("RoosTest", "Noémie", "nroos@gmail.com", "Rebwiesenweg"));
        assertTrue(checkIfLehrkraftAvailable("DelleyTest", "Nathalie", "ndelley@sunrise.ch", "Im Schilf"));

        // Testdaten löschen
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            MitarbeiterDao mitarbeiterDao = new MitarbeiterDao(entityManager);
            for (Mitarbeiter mitarbeiter : lehrkraefteSaved) {
                Mitarbeiter mitarbeiterToBeDeleted = mitarbeiterDao.findById(mitarbeiter.getPersonId());
                if (mitarbeiterToBeDeleted != null) {
                    mitarbeiterDao.remove(mitarbeiterToBeDeleted);
                }
            }
            entityManager.getTransaction().commit();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }


    }

    private boolean checkIfLehrkraftAvailable(String nachname, String vorname, String email, String strasse) {
        FindAllMitarbeitersCommand findAllMitarbeitersCommand = new FindAllMitarbeitersCommand();
        commandInvoker.executeCommandAsTransactionWithOpenAndClose(findAllMitarbeitersCommand);
        List<Mitarbeiter> lehrkraefteAll = findAllMitarbeitersCommand.getLehrkraefteAll();
        for (Mitarbeiter mitarbeiter : lehrkraefteAll) {
            if (mitarbeiter.getNachname().equals(nachname) && mitarbeiter.getVorname().equals(vorname) && mitarbeiter.getEmail().equals(email) && mitarbeiter.getAdresse().getStrasse().equals(strasse)) {
                return true;
            }
        }
        return false;
    }
}