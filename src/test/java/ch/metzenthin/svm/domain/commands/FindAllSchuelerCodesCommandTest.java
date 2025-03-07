package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.daos.SchuelerCodeDao;
import ch.metzenthin.svm.persistence.entities.SchuelerCode;
import jakarta.persistence.EntityManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;
import static org.junit.Assert.assertTrue;

/**
 * @author Martin Schraner
 */
public class FindAllSchuelerCodesCommandTest {

    private final SchuelerCodeDao schuelerCodeDao = new SchuelerCodeDao();
    private final Set<SchuelerCode> codesTestdata = new HashSet<>();

    private DB db;
    private CommandInvoker commandInvoker;

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
    public void testExecute() {
        FindAllSchuelerCodesCommand findAllSchuelerCodesCommand = new FindAllSchuelerCodesCommand();
        commandInvoker.executeCommand(findAllSchuelerCodesCommand);

        List<SchuelerCode> codesFound = findAllSchuelerCodesCommand.getSchuelerCodesAll();
        assertTrue(codesFound.size() >= 2);
        boolean found1 = false;
        boolean found2 = false;
        for (SchuelerCode schuelerCode : codesFound) {
            if (schuelerCode.getBeschreibung().equals("ZirkusTest")) {
                found1 = true;
            }
            if (schuelerCode.getBeschreibung().equals("JugendprojektTest")) {
                found2 = true;
            }
        }
        assertTrue(found1);
        assertTrue(found2);
    }

    private void createTestdata() {
        EntityManager entityManager = db.getCurrentEntityManager();
        entityManager.getTransaction().begin();

        SchuelerCode schuelerCodeSaved = schuelerCodeDao.save(new SchuelerCode("z", "ZirkusTest", true));
        codesTestdata.add(schuelerCodeSaved);

        schuelerCodeSaved = schuelerCodeDao.save(new SchuelerCode("j", "JugendprojektTest", true));
        codesTestdata.add(schuelerCodeSaved);

        entityManager.getTransaction().commit();
        db.closeSession();
    }

    private void deleteTestdata() {
        EntityManager entityManager = db.getCurrentEntityManager();
        entityManager.getTransaction().begin();

        for (SchuelerCode schuelerCode : codesTestdata) {
            SchuelerCode schuelerCodeToBeRemoved = schuelerCodeDao.findById(schuelerCode.getCodeId());
            schuelerCodeDao.remove(schuelerCodeToBeRemoved);
        }

        entityManager.getTransaction().commit();
        db.closeSession();
    }
}