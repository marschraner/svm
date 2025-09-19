package ch.metzenthin.svm.domain.commands;

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;
import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import ch.metzenthin.svm.common.datatypes.Anrede;
import ch.metzenthin.svm.common.datatypes.Semesterbezeichnung;
import ch.metzenthin.svm.common.datatypes.Wochentag;
import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.daos.*;
import ch.metzenthin.svm.persistence.entities.*;
import jakarta.persistence.EntityManager;
import java.sql.Time;
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
public class DeleteKursCommandTest {

  private final KursDao kursDao = new KursDao();
  private final SemesterDao semesterDao = new SemesterDao();
  private final MitarbeiterDao mitarbeiterDao = new MitarbeiterDao();
  private final KursortDao kursortDao = new KursortDao();
  private final KurstypDao kurstypDao = new KurstypDao();

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

    List<Semester> erfassteSemester = new ArrayList<>();
    List<Kurstyp> erfassteKurstypen = new ArrayList<>();
    List<Kursort> erfassteKursorte = new ArrayList<>();
    List<Mitarbeiter> erfassteLehrkraefte = new ArrayList<>();
    List<Kurs> erfassteKurse = new ArrayList<>();

    // Semester, Kurstyp, Kursort und Lehrkräfte erzeugen
    Semester semester1 =
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
        new SaveOrUpdateSemesterCommand(semester1, null, erfassteSemester);
    commandInvoker.executeCommandAsTransaction(saveOrUpdateSemesterCommand);
    Semester semester2 =
        new Semester(
            "1911/1912",
            Semesterbezeichnung.ZWEITES_SEMESTER,
            new GregorianCalendar(1912, Calendar.FEBRUARY, 20),
            new GregorianCalendar(1912, Calendar.JULY, 10),
            new GregorianCalendar(1912, Calendar.APRIL, 25),
            new GregorianCalendar(1912, Calendar.MAY, 7),
            null,
            null);
    saveOrUpdateSemesterCommand =
        new SaveOrUpdateSemesterCommand(semester2, null, erfassteSemester);
    commandInvoker.executeCommandAsTransaction(saveOrUpdateSemesterCommand);

    Kurstyp kurstyp1 = new Kurstyp("Kurs Test1", true);
    SaveOrUpdateKurstypCommand saveOrUpdateKurstypCommand =
        new SaveOrUpdateKurstypCommand(kurstyp1, null, erfassteKurstypen);
    commandInvoker.executeCommandAsTransaction(saveOrUpdateKurstypCommand);
    Kurstyp kurstyp2 = new Kurstyp("Kurs Test2", true);
    saveOrUpdateKurstypCommand = new SaveOrUpdateKurstypCommand(kurstyp2, null, erfassteKurstypen);
    commandInvoker.executeCommandAsTransaction(saveOrUpdateKurstypCommand);

    Kursort kursort1 = new Kursort("Saal Test", true);
    SaveOrUpdateKursortCommand saveOrUpdateKursortCommand =
        new SaveOrUpdateKursortCommand(kursort1, null, erfassteKursorte);
    commandInvoker.executeCommandAsTransaction(saveOrUpdateKursortCommand);
    Kursort kursort2 = new Kursort("Saal Test", true);
    saveOrUpdateKursortCommand = new SaveOrUpdateKursortCommand(kursort2, null, erfassteKursorte);
    commandInvoker.executeCommandAsTransaction(saveOrUpdateKursortCommand);

    Mitarbeiter lehrkraft1 =
        new Mitarbeiter(
            Anrede.FRAU,
            "Noémie",
            "Roostest1",
            new GregorianCalendar(1994, Calendar.MARCH, 18),
            "044 391 45 35",
            "076 384 45 35",
            "nroos@gmx.ch",
            "756.3943.8722.22",
            "CH31 8123 9000 0012 4568 9",
            true,
            "Mi, Fr, Sa",
            null,
            true);
    Adresse adresse1 = new Adresse("Rebwiesenstrasse", "54", "8702", "Zollikon");
    SaveOrUpdateMitarbeiterCommand saveOrUpdateMitarbeiterCommand =
        new SaveOrUpdateMitarbeiterCommand(lehrkraft1, adresse1, null, null, erfassteLehrkraefte);
    commandInvoker.executeCommandAsTransaction(saveOrUpdateMitarbeiterCommand);
    Mitarbeiter lehrkraft2 =
        new Mitarbeiter(
            Anrede.FRAU,
            "Noémie",
            "Roostest2",
            new GregorianCalendar(1994, Calendar.MARCH, 18),
            "044 391 45 35",
            "076 384 45 35",
            "nroos@gmx.ch",
            "756.3943.8722.22",
            "CH31 8123 9000 0012 4568 9",
            true,
            "Mi, Fr, Sa",
            null,
            true);
    Adresse adresse2 = new Adresse("Rebwiesenstrasse", "54", "8702", "Zollikon");
    saveOrUpdateMitarbeiterCommand =
        new SaveOrUpdateMitarbeiterCommand(lehrkraft2, adresse2, null, null, erfassteLehrkraefte);
    commandInvoker.executeCommandAsTransaction(saveOrUpdateMitarbeiterCommand);

    assertFalse(
        checkIfKursAvailable(
            semester1,
            kurstyp1,
            "2-3 J",
            "Vorkindergarten",
            Wochentag.DONNERSTAG,
            Time.valueOf("10:00:00"),
            Time.valueOf("10:50:00"),
            kursort1,
            lehrkraft1,
            null));
    assertFalse(
        checkIfKursAvailable(
            semester2,
            kurstyp2,
            "3-4 J",
            "1. Kindergarten",
            Wochentag.FREITAG,
            Time.valueOf("10:00:00"),
            Time.valueOf("10:50:00"),
            kursort2,
            lehrkraft2,
            lehrkraft1));

    // 2 Kurse erfassen
    Kurs kurs1 =
        new Kurs(
            "2-3 J",
            "Vorkindergarten",
            Wochentag.DONNERSTAG,
            Time.valueOf("10:00:00"),
            Time.valueOf("10:50:00"),
            null);
    SaveOrUpdateKursCommand saveOrUpdateKursCommand =
        new SaveOrUpdateKursCommand(
            kurs1, semester1, kurstyp1, kursort1, lehrkraft1, null, null, erfassteKurse);
    commandInvoker.executeCommandAsTransaction(saveOrUpdateKursCommand);

    Kurs kurs2 =
        new Kurs(
            "3-4 J",
            "1. Kindergarten",
            Wochentag.FREITAG,
            Time.valueOf("10:00:00"),
            Time.valueOf("10:50:00"),
            null);
    saveOrUpdateKursCommand =
        new SaveOrUpdateKursCommand(
            kurs2, semester2, kurstyp2, kursort2, lehrkraft2, lehrkraft1, null, erfassteKurse);
    commandInvoker.executeCommandAsTransaction(saveOrUpdateKursCommand);

    assertEquals(2, erfassteKurse.size());

    // Kurse löschen
    DeleteKursCommand deleteKursCommand = new DeleteKursCommand(erfassteKurse, 1);
    commandInvoker.executeCommandAsTransaction(deleteKursCommand);
    assertEquals(1, erfassteKurse.size());

    deleteKursCommand = new DeleteKursCommand(erfassteKurse, 0);
    commandInvoker.executeCommandAsTransaction(deleteKursCommand);
    assertTrue(erfassteKurse.isEmpty());

    // Testdaten löschen
    EntityManager entityManager = db.getCurrentEntityManager();
    entityManager.getTransaction().begin();
    for (Kurs kurs : erfassteKurse) {
      Kurs kursToBeDeleted = kursDao.findById(kurs.getKursId());
      if (kursToBeDeleted != null) {
        kursDao.remove(kursToBeDeleted);
      }
    }
    entityManager.getTransaction().commit();
    entityManager.getTransaction().begin();
    for (Semester semester : erfassteSemester) {
      Semester semesterToBeDeleted = semesterDao.findById(semester.getSemesterId());
      if (semesterToBeDeleted != null) {
        semesterDao.remove(semesterToBeDeleted);
      }
    }
    entityManager.getTransaction().commit();
    entityManager.getTransaction().begin();
    for (Mitarbeiter mitarbeiter : erfassteLehrkraefte) {
      Mitarbeiter mitarbeiterToBeDeleted = mitarbeiterDao.findById(mitarbeiter.getPersonId());
      if (mitarbeiterToBeDeleted != null) {
        mitarbeiterDao.remove(mitarbeiterToBeDeleted);
      }
    }
    entityManager.getTransaction().commit();
    entityManager.getTransaction().begin();
    for (Kursort kursort : erfassteKursorte) {
      Kursort kursortToBeDeleted = kursortDao.findById(kursort.getKursortId());
      if (kursortToBeDeleted != null) {
        kursortDao.remove(kursortToBeDeleted);
      }
    }
    entityManager.getTransaction().commit();
    entityManager.getTransaction().begin();
    for (Kurstyp kurstyp : erfassteKurstypen) {
      Kurstyp kurstypToBeDeleted = kurstypDao.findById(kurstyp.getKurstypId());
      if (kurstypToBeDeleted != null) {
        kurstypDao.remove(kurstypToBeDeleted);
      }
    }
    entityManager.getTransaction().commit();
  }

  private boolean checkIfKursAvailable(
      Semester semester,
      Kurstyp kurstyp,
      String altersbereich,
      String stufe,
      Wochentag wochentag,
      Time zeitBeginn,
      Time zeitEnde,
      Kursort kursort,
      Mitarbeiter mitarbeiter1,
      Mitarbeiter mitarbeiter2) {
    FindKurseSemesterCommand kurseSemesterCommand = new FindKurseSemesterCommand(semester);
    commandInvoker.executeCommandAsTransaction(kurseSemesterCommand);
    List<Kurs> kurseAll = kurseSemesterCommand.getKurseFound();
    for (Kurs kurs : kurseAll) {
      if (kurs.getSemester().equals(semester)
          && kurs.getKurstyp().equals(kurstyp)
          && kurs.getAltersbereich().equals(altersbereich)
          && kurs.getStufe().equals(stufe)
          && kurs.getWochentag().equals(wochentag)
          && kurs.getZeitBeginn().equals(zeitBeginn)
          && kurs.getZeitEnde().equals(zeitEnde)
          && kurs.getKursort().equals(kursort)
          && kurs.getLehrkraefte().get(0).equals(mitarbeiter1)) {
        if (kurs.getLehrkraefte().size() == 1) {
          if (mitarbeiter2 == null) {
            return true;
          }
        } else {
          if (kurs.getLehrkraefte().get(1).equals(mitarbeiter2)) {
            return true;
          }
        }
      }
    }
    return false;
  }
}
