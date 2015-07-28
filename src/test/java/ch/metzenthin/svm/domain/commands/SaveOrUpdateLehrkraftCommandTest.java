package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.dataTypes.Anrede;
import ch.metzenthin.svm.persistence.daos.LehrkraftDao;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Lehrkraft;
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

import static org.junit.Assert.*;

/**
 * @author Martin Schraner
 */
public class SaveOrUpdateLehrkraftCommandTest {

    private CommandInvoker commandInvoker = new CommandInvokerImpl();
    private EntityManagerFactory entityManagerFactory;

    @Before
    public void setUp() throws Exception {
        commandInvoker.openSession();
        entityManagerFactory = Persistence.createEntityManagerFactory("svm");
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

        List<Lehrkraft> lehrkraefteSaved = new ArrayList<>();

        // Lehrkraft hinzufügen
        Lehrkraft lehrkraft1 = new Lehrkraft(Anrede.FRAU, "Noémie", "RoosTest", new GregorianCalendar(1994, Calendar.MARCH, 18), "043 111 11 11", "076 384 45 35", "nroos@gmx.ch", "756.3943.8722.22", "Mi, Fr, Sa", true);
        Adresse adresse1 = new Adresse("Rebwiesenstrasse", "77", "8702", "Zollikon");
        lehrkraft1.setAdresse(adresse1);
        SaveOrUpdateLehrkraftCommand saveOrUpdateLehrkraftCommand = new SaveOrUpdateLehrkraftCommand(lehrkraft1, adresse1, null, lehrkraefteSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateLehrkraftCommand);
        assertEquals(1, lehrkraefteSaved.size());
        assertTrue(checkIfLehrkraftAvailable("RoosTest", "Noémie", "nroos@gmx.ch", "Rebwiesenstrasse"));
        assertFalse(checkIfLehrkraftAvailable("DelleyTest", "Nathalie", "ndelley@sunrise.ch", "Im Schilf"));

        // Weitere Lehrkraft hinzufügen
        Lehrkraft lehrkraft2 = new Lehrkraft(Anrede.FRAU, "Nathalie", "DelleyTest", new GregorianCalendar(1971, Calendar.DECEMBER, 16), "044 261 27 20", "076 338 05 36", "ndelley@sunrise.ch", "756.8274.3263.17", "Mi, Fr, Sa", true);
        Adresse adresse2 = new Adresse("Im Schilf", "7", "8044", "Zürich");
        lehrkraft2.setAdresse(adresse2);
        saveOrUpdateLehrkraftCommand = new SaveOrUpdateLehrkraftCommand(lehrkraft2, adresse2, null, lehrkraefteSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateLehrkraftCommand);
        assertEquals(2, lehrkraefteSaved.size());
        // Alphabetisch geordnet?
        assertEquals("DelleyTest", lehrkraefteSaved.get(0).getNachname());
        assertEquals("RoosTest", lehrkraefteSaved.get(1).getNachname());
        assertTrue(checkIfLehrkraftAvailable("RoosTest", "Noémie", "nroos@gmx.ch", "Rebwiesenstrasse"));
        assertTrue(checkIfLehrkraftAvailable("DelleyTest", "Nathalie", "ndelley@sunrise.ch", "Im Schilf"));

        // Lehrkraft bearbeiten (neue Email, neue Strasse)
        Lehrkraft lehrkraft1Modif = new Lehrkraft(Anrede.FRAU, "Noémie", "RoosTest", new GregorianCalendar(1994, Calendar.MARCH, 18), "043 111 11 11", "076 384 45 35", "nroos@gmail.com", "756.3943.8722.22", "Mi, Fr, Sa", true);
        Adresse adresse1Modif = new Adresse("Rebwiesenweg", "3", "8702", "Zollikon");
        lehrkraft1.setAdresse(adresse1Modif);
        saveOrUpdateLehrkraftCommand = new SaveOrUpdateLehrkraftCommand(lehrkraft1Modif, adresse1Modif, lehrkraft1, lehrkraefteSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateLehrkraftCommand);
        assertEquals(2, lehrkraefteSaved.size());
        assertFalse(checkIfLehrkraftAvailable("RoosTest", "Noémie", "nroos@gmx.ch", "Rebwiesenstrasse"));
        assertTrue(checkIfLehrkraftAvailable("RoosTest", "Noémie", "nroos@gmail.com", "Rebwiesenweg"));
        assertTrue(checkIfLehrkraftAvailable("DelleyTest", "Nathalie", "ndelley@sunrise.ch", "Im Schilf"));

        // Testdaten löschen
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            LehrkraftDao lehrkraftDao = new LehrkraftDao(entityManager);
            for (Lehrkraft lehrkraft : lehrkraefteSaved) {
                Lehrkraft lehrkraftToBeDeleted = lehrkraftDao.findById(lehrkraft.getPersonId());
                if (lehrkraftToBeDeleted != null) {
                    lehrkraftDao.remove(lehrkraftToBeDeleted);
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
        FindAllLehrkraefteCommand findAllLehrkraefteCommand = new FindAllLehrkraefteCommand();
        commandInvoker.executeCommandAsTransactionWithOpenAndClose(findAllLehrkraefteCommand);
        List<Lehrkraft> lehrkraefteAll = findAllLehrkraefteCommand.getLehrkraefteAll();
        for (Lehrkraft lehrkraft : lehrkraefteAll) {
            if (lehrkraft.getNachname().equals(nachname) && lehrkraft.getVorname().equals(vorname) && lehrkraft.getEmail().equals(email) && lehrkraft.getAdresse().getStrasse().equals(strasse)) {
                return true;
            }
        }
        return false;
    }
}