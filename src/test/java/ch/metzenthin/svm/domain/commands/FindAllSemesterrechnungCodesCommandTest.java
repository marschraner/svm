package ch.metzenthin.svm.domain.commands;

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

    private CommandInvoker commandInvoker = new CommandInvokerImpl();
    private Set<SemesterrechnungCode> codesTestdata = new HashSet<>();

    @Before
    public void setUp() throws Exception {
        createSvmPropertiesFileDefault();
        createTestdata();
    }

    @After
    public void tearDown() throws Exception {
        deleteTestdata();
        commandInvoker.closeSessionAndEntityManagerFactory();
    }

    @Test
    public void testExecute() {
        FindAllSemesterrechnungCodesCommand findAllSemesterrechnungCodesCommand = new FindAllSemesterrechnungCodesCommand();
        commandInvoker.executeCommandAsTransactionWithOpenAndClose(findAllSemesterrechnungCodesCommand);

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
        commandInvoker.openSession();
        EntityManager entityManager = commandInvoker.getEntityManager();
        entityManager.getTransaction().begin();

        SemesterrechnungCodeDao semesterrechnungCodeDao = new SemesterrechnungCodeDao(entityManager);

        SemesterrechnungCode semesterrechnungCodeSaved = semesterrechnungCodeDao.save(new SemesterrechnungCode("2t", "Handrechnung Test", true));
        codesTestdata.add(semesterrechnungCodeSaved);

        semesterrechnungCodeSaved = semesterrechnungCodeDao.save(new SemesterrechnungCode("1t", "Stipendium Test", true));
        codesTestdata.add(semesterrechnungCodeSaved);

        entityManager.getTransaction().commit();
        commandInvoker.closeSession();
    }

    private void deleteTestdata() {
        commandInvoker.openSession();
        EntityManager entityManager = commandInvoker.getEntityManager();
        entityManager.getTransaction().begin();

        SemesterrechnungCodeDao semesterrechnungCodeDao = new SemesterrechnungCodeDao(entityManager);

        for (SemesterrechnungCode semesterrechnungCode : codesTestdata) {
            SemesterrechnungCode semesterrechnungCodeToBeRemoved = semesterrechnungCodeDao.findById(semesterrechnungCode.getCodeId());
            semesterrechnungCodeDao.remove(semesterrechnungCodeToBeRemoved);
        }

        entityManager.getTransaction().commit();
        commandInvoker.closeSession();
    }
}