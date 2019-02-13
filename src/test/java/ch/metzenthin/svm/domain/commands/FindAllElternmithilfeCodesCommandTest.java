package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.daos.ElternmithilfeCodeDao;
import ch.metzenthin.svm.persistence.entities.ElternmithilfeCode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;
import static org.junit.Assert.assertTrue;

/**
 * @author Martin Schraner
 */
public class FindAllElternmithilfeCodesCommandTest {

    private final ElternmithilfeCodeDao elternmithilfeCodeDao = new ElternmithilfeCodeDao();
    private final Set<ElternmithilfeCode> codesTestdata = new HashSet<>();

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
        FindAllElternmithilfeCodesCommand findAllElternmithilfeCodesCommand = new FindAllElternmithilfeCodesCommand();
        commandInvoker.executeCommand(findAllElternmithilfeCodesCommand);

        List<ElternmithilfeCode> codesFound = findAllElternmithilfeCodesCommand.getElternmithilfeCodesAll();
        assertTrue(codesFound.size() >= 2);
        boolean found1 = false;
        boolean found2 = false;
        for (ElternmithilfeCode elternmithilfeCode : codesFound) {
            if (elternmithilfeCode.getBeschreibung().equals("KuchenTest")) {
                found1 = true;
            }
            if (elternmithilfeCode.getBeschreibung().equals("FrisierenTest")) {
                found2 = true;
            }
        }
        assertTrue(found1);
        assertTrue(found2);
    }

    private void createTestdata() {
        EntityManager entityManager = db.getCurrentEntityManager();
        entityManager.getTransaction().begin();

        ElternmithilfeCode elternmithilfeCodeSaved = elternmithilfeCodeDao.save(new ElternmithilfeCode("k", "KuchenTest", true));
        codesTestdata.add(elternmithilfeCodeSaved);

        elternmithilfeCodeSaved = elternmithilfeCodeDao.save(new ElternmithilfeCode("f", "FrisierenTest", true));
        codesTestdata.add(elternmithilfeCodeSaved);

        entityManager.getTransaction().commit();
        db.closeSession();
    }

    private void deleteTestdata() {
        EntityManager entityManager = db.getCurrentEntityManager();
        entityManager.getTransaction().begin();

        for (ElternmithilfeCode elternmithilfeCode : codesTestdata) {
            ElternmithilfeCode elternmithilfeCodeToBeRemoved = elternmithilfeCodeDao.findById(elternmithilfeCode.getCodeId());
            elternmithilfeCodeDao.remove(elternmithilfeCodeToBeRemoved);
        }

        entityManager.getTransaction().commit();
        db.closeSession();
    }
}