package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.daos.SemesterrechnungCodeDao;
import ch.metzenthin.svm.persistence.entities.SemesterrechnungCode;
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
public class FindAllSemesterrechnungCodesCommandTest {

    private DB db;
    private CommandInvoker commandInvoker;
    private Set<SemesterrechnungCode> codesTestdata = new HashSet<>();

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
        FindAllSemesterrechnungCodesCommand findAllSemesterrechnungCodesCommand = new FindAllSemesterrechnungCodesCommand();
        commandInvoker.executeCommand(findAllSemesterrechnungCodesCommand);

        List<SemesterrechnungCode> codesFound = findAllSemesterrechnungCodesCommand.getSemesterrechnungCodesAll();
        assertTrue(codesFound.size() >= 2);
        boolean found1 = false;
        boolean found2 = false;
        for (SemesterrechnungCode semesterrechnungCode : codesFound) {
            if (semesterrechnungCode.getBeschreibung().equals("Handrechnung Test")) {
                found1 = true;
            }
            if (semesterrechnungCode.getBeschreibung().equals("Stipendium Test")) {
                found2 = true;
            }
        }
        assertTrue(found1);
        assertTrue(found2);
    }

    private void createTestdata() {
        EntityManager entityManager = db.getCurrentEntityManager();
        entityManager.getTransaction().begin();

        SemesterrechnungCodeDao semesterrechnungCodeDao = new SemesterrechnungCodeDao(entityManager);

        SemesterrechnungCode semesterrechnungCodeSaved = semesterrechnungCodeDao.save(new SemesterrechnungCode("2t", "Handrechnung Test", true));
        codesTestdata.add(semesterrechnungCodeSaved);

        semesterrechnungCodeSaved = semesterrechnungCodeDao.save(new SemesterrechnungCode("1t", "Stipendium Test", true));
        codesTestdata.add(semesterrechnungCodeSaved);

        entityManager.getTransaction().commit();
        db.closeSession();
    }

    private void deleteTestdata() {
        EntityManager entityManager = db.getCurrentEntityManager();
        entityManager.getTransaction().begin();

        SemesterrechnungCodeDao semesterrechnungCodeDao = new SemesterrechnungCodeDao(entityManager);

        for (SemesterrechnungCode semesterrechnungCode : codesTestdata) {
            SemesterrechnungCode semesterrechnungCodeToBeRemoved = semesterrechnungCodeDao.findById(semesterrechnungCode.getCodeId());
            semesterrechnungCodeDao.remove(semesterrechnungCodeToBeRemoved);
        }

        entityManager.getTransaction().commit();
        db.closeSession();
    }
}