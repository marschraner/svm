package ch.metzenthin.svm.domain.commands;

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

import ch.metzenthin.svm.common.datatypes.Semesterbezeichnung;
import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.entities.Semester;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Martin Schraner
 */
public class DeleteSemesterCommandTest {

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

    List<Semester> semestersSaved = new ArrayList<>();

    // 2 Semester erfassen
    Semester semester1 =
        new Semester(
            "1912/1913",
            Semesterbezeichnung.ZWEITES_SEMESTER,
            new GregorianCalendar(1913, Calendar.FEBRUARY, 20),
            new GregorianCalendar(1913, Calendar.JULY, 10),
            new GregorianCalendar(1913, Calendar.APRIL, 25),
            new GregorianCalendar(1913, Calendar.MAY, 7),
            null,
            null);
    Semester semester2 =
        new Semester(
            "1911/1912",
            Semesterbezeichnung.ERSTES_SEMESTER,
            new GregorianCalendar(1911, Calendar.AUGUST, 20),
            new GregorianCalendar(1912, Calendar.FEBRUARY, 10),
            new GregorianCalendar(1911, Calendar.OCTOBER, 5),
            new GregorianCalendar(1911, Calendar.OCTOBER, 17),
            new GregorianCalendar(1911, Calendar.DECEMBER, 21),
            new GregorianCalendar(1912, Calendar.JANUARY, 2));

    SaveOrUpdateSemesterCommand saveOrUpdateSemesterCommand =
        new SaveOrUpdateSemesterCommand(semester1, null, semestersSaved);
    commandInvoker.executeCommandAsTransaction(saveOrUpdateSemesterCommand);

    saveOrUpdateSemesterCommand = new SaveOrUpdateSemesterCommand(semester2, null, semestersSaved);
    commandInvoker.executeCommandAsTransaction(saveOrUpdateSemesterCommand);

    // Semesters löschen
    DeleteSemesterCommand deleteSemesterCommand = new DeleteSemesterCommand(semestersSaved, 1);
    commandInvoker.executeCommandAsTransaction(deleteSemesterCommand);
    assertEquals(
        DeleteSemesterCommand.Result.LOESCHEN_ERFOLGREICH, deleteSemesterCommand.getResult());
    assertEquals(1, semestersSaved.size());

    deleteSemesterCommand = new DeleteSemesterCommand(semestersSaved, 0);
    commandInvoker.executeCommandAsTransaction(deleteSemesterCommand);
    assertEquals(
        DeleteSemesterCommand.Result.LOESCHEN_ERFOLGREICH, deleteSemesterCommand.getResult());
    assertTrue(semestersSaved.isEmpty());
  }
}
