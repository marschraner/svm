package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Anrede;
import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.daos.MitarbeiterDao;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import java.util.*;

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;
import static org.junit.Assert.assertTrue;

/**
 * @author Martin Schraner
 */
public class FindAllMitarbeitersCommandTest {

    private final MitarbeiterDao mitarbeiterDao = new MitarbeiterDao();
    private final Set<Mitarbeiter> mitarbeitersTestdata = new HashSet<>();

    private DB db;
    private CommandInvoker commandInvoker;

    @Before
    public void setUp() throws Exception {
        createSvmPropertiesFileDefault();
        db = DBFactory.getInstance();
        commandInvoker = new CommandInvokerImpl();
        createTestdata();
    }

    @After
    public void tearDown() throws Exception {
        deleteTestdata();
        db.closeSession();
    }

    @Test
    public void testExecute() {
        FindAllMitarbeitersCommand findAllMitarbeitersCommand = new FindAllMitarbeitersCommand();
            commandInvoker.executeCommand(findAllMitarbeitersCommand);

        List<Mitarbeiter> lehrkraefteFound = findAllMitarbeitersCommand.getMitarbeitersAll();
        assertTrue(lehrkraefteFound.size() >= 2);
        boolean found1 = false;
        boolean found2 = false;
        for (Mitarbeiter mitarbeiter : lehrkraefteFound) {
            if (mitarbeiter.getVorname().equals("NoémiTest")) {
                found1 = true;
            }
            if (mitarbeiter.getVorname().equals("NathalieTest")) {
                found2 = true;
            }
        }
        assertTrue(found1);
        assertTrue(found2);
    }

    private void createTestdata() {
        EntityManager entityManager = db.getCurrentEntityManager();
        entityManager.getTransaction().begin();

        Mitarbeiter mitarbeiter1 = new Mitarbeiter(Anrede.FRAU, "NoémiTest", "RoosTest", new GregorianCalendar(1994, Calendar.MARCH, 18), "044 391 45 35", "076 384 45 35", "nroos@gmx.ch", "756.3943.8722.22", true, "Mi, Fr, Sa", null, true);
        Adresse adresse1 = new Adresse("Rebwiesenstrasse", "54", "8702", "Zollikon");
        mitarbeiter1.setAdresse(adresse1);
        Mitarbeiter mitarbeiterSaved = mitarbeiterDao.save(mitarbeiter1);
        mitarbeitersTestdata.add(mitarbeiterSaved);

        Mitarbeiter mitarbeiter2 = new Mitarbeiter(Anrede.FRAU, "NathalieTest", "DelleyTest", new GregorianCalendar(1971, Calendar.DECEMBER, 16), "044 261 27 20", "076 338 05 36", "ndelley@sunrise.ch", "756.8274.3263.17", true, "Mi, Fr, Sa", null, true);
        Adresse adresse2 = new Adresse("Im Schilf", "7", "8044", "Zürich");
        mitarbeiter2.setAdresse(adresse2);
        mitarbeiterSaved = mitarbeiterDao.save(mitarbeiter2);
        mitarbeitersTestdata.add(mitarbeiterSaved);

        entityManager.getTransaction().commit();
        db.closeSession();
    }

    private void deleteTestdata() {
        EntityManager entityManager = db.getCurrentEntityManager();
        entityManager.getTransaction().begin();

        for (Mitarbeiter mitarbeiter : mitarbeitersTestdata) {
            Mitarbeiter mitarbeiterToBeRemoved = mitarbeiterDao.findById(mitarbeiter.getPersonId());
            mitarbeiterDao.remove(mitarbeiterToBeRemoved);
        }

        entityManager.getTransaction().commit();
        db.closeSession();
    }
}