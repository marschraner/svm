package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.datatypes.Anrede;
import ch.metzenthin.svm.common.datatypes.Semesterbezeichnung;
import ch.metzenthin.svm.common.datatypes.Wochentag;
import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.daos.*;
import ch.metzenthin.svm.persistence.entities.*;
import jakarta.persistence.EntityManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Martin Schraner
 */
public class ImportKurseFromPreviousSemesterCommandTest {

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
        Semester semester1 = new Semester("1911/1912", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(1911, Calendar.AUGUST, 20), new GregorianCalendar(1912, Calendar.FEBRUARY, 10), new GregorianCalendar(1911, Calendar.OCTOBER, 5), new GregorianCalendar(1911, Calendar.OCTOBER, 17), new GregorianCalendar(1911, Calendar.DECEMBER, 21), new GregorianCalendar(1912, Calendar.JANUARY, 2));
        SaveOrUpdateSemesterCommand saveOrUpdateSemesterCommand = new SaveOrUpdateSemesterCommand(semester1, null, erfassteSemester);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateSemesterCommand);
        Semester semester2 = new Semester("1911/1912", Semesterbezeichnung.ZWEITES_SEMESTER, new GregorianCalendar(1912, Calendar.FEBRUARY, 20), new GregorianCalendar(1912, Calendar.JULY, 10), new GregorianCalendar(1912, Calendar.APRIL, 25), new GregorianCalendar(1912, Calendar.MAY, 7), null, null);
        saveOrUpdateSemesterCommand = new SaveOrUpdateSemesterCommand(semester2, null, erfassteSemester);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateSemesterCommand);
        Semester semester3 = new Semester("1912/1913", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(1912, Calendar.AUGUST, 20), new GregorianCalendar(1913, Calendar.FEBRUARY, 10), new GregorianCalendar(1912, Calendar.OCTOBER, 5), new GregorianCalendar(1912, Calendar.OCTOBER, 17), new GregorianCalendar(1912, Calendar.DECEMBER, 21), new GregorianCalendar(1913, Calendar.JANUARY, 2));
        saveOrUpdateSemesterCommand = new SaveOrUpdateSemesterCommand(semester3, null, erfassteSemester);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateSemesterCommand);

        Kurstyp kurstyp1 = new Kurstyp("Kurs Test", true);
        SaveOrUpdateKurstypCommand saveOrUpdateKurstypCommand = new SaveOrUpdateKurstypCommand(kurstyp1, null, erfassteKurstypen);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateKurstypCommand);

        Kursort kursort1 = new Kursort("Saal Test", true);
        SaveOrUpdateKursortCommand saveOrUpdateKursortCommand = new SaveOrUpdateKursortCommand(kursort1, null, erfassteKursorte);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateKursortCommand);

        Mitarbeiter mitarbeiter1 = new Mitarbeiter(Anrede.FRAU, "Noémie", "Roostest", new GregorianCalendar(1994, Calendar.MARCH, 18), "044 391 45 35", "076 384 45 35", "nroos@gmx.ch", "756.3943.8722.22", "CH31 8123 9000 0012 4568 9", true, "Mi, Fr, Sa", null, true);
        Adresse adresse1 = new Adresse("Rebwiesenstrasse", "54", "8702", "Zollikon");
        SaveOrUpdateMitarbeiterCommand saveOrUpdateMitarbeiterCommand = new SaveOrUpdateMitarbeiterCommand(mitarbeiter1, adresse1, null, null, erfassteLehrkraefte);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateMitarbeiterCommand);

        // 2 Kurse für semester1 erfassen
        Kurs kurs1 = new Kurs("2-3 J", "Vorkindergarten", Wochentag.DONNERSTAG, Time.valueOf("10:00:00"), Time.valueOf("10:50:00"), null);
        SaveOrUpdateKursCommand saveOrUpdateKursCommand = new SaveOrUpdateKursCommand(kurs1, semester1, kurstyp1, kursort1, mitarbeiter1, null, null, erfassteKurse);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateKursCommand);
        Kurs kurs2 = new Kurs("2-3 J", "Vorkindergarten", Wochentag.FREITAG, Time.valueOf("10:00:00"), Time.valueOf("10:50:00"), null);
        saveOrUpdateKursCommand = new SaveOrUpdateKursCommand(kurs2, semester1, kurstyp1, kursort1, mitarbeiter1, null, null, erfassteKurse);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateKursCommand);

        assertEquals(2, erfassteKurse.size());

        // Für semester3 Kurse (von semester1) importieren
        List<Kurs> kurseSemester3 = new ArrayList<>();
        ImportKurseFromPreviousSemesterCommand importKurseFromPreviousSemesterCommand = new ImportKurseFromPreviousSemesterCommand(kurseSemester3, semester3);
        commandInvoker.executeCommandAsTransaction(importKurseFromPreviousSemesterCommand);
        assertEquals(2, kurseSemester3.size());
        assertEquals(Wochentag.DONNERSTAG, kurseSemester3.get(0).getWochentag());
        assertEquals(Wochentag.FREITAG, kurseSemester3.get(1).getWochentag());
        assertEquals("Roostest", kurseSemester3.get(0).getLehrkraefte().get(0).getNachname());
        assertEquals("Saal Test", kurseSemester3.get(0).getKursort().getBezeichnung());
        assertEquals("Kurs Test", kurseSemester3.get(0).getKurstyp().getBezeichnung());

        // Für semester2 Kurse (von semester1) importieren
        List<Kurs> kurseSemester2 = new ArrayList<>();
        importKurseFromPreviousSemesterCommand = new ImportKurseFromPreviousSemesterCommand(kurseSemester2, semester2);
        commandInvoker.executeCommandAsTransaction(importKurseFromPreviousSemesterCommand);
        assertEquals(2, kurseSemester2.size());
        assertEquals(Wochentag.DONNERSTAG, kurseSemester2.get(0).getWochentag());
        assertEquals(Wochentag.FREITAG, kurseSemester2.get(1).getWochentag());
        assertEquals("Roostest", kurseSemester2.get(0).getLehrkraefte().get(0).getNachname());
        assertEquals("Saal Test", kurseSemester2.get(0).getKursort().getBezeichnung());
        assertEquals("Kurs Test", kurseSemester2.get(0).getKurstyp().getBezeichnung());

        // Für semester1 Kurse importieren (-> leer)
        List<Kurs> kurseSemester1 = new ArrayList<>();
        importKurseFromPreviousSemesterCommand = new ImportKurseFromPreviousSemesterCommand(kurseSemester1, semester1);
        commandInvoker.executeCommandAsTransaction(importKurseFromPreviousSemesterCommand);
        assertTrue(kurseSemester1.isEmpty());

        // Testdaten löschen
        EntityManager entityManager = db.getCurrentEntityManager();
        entityManager.getTransaction().begin();
        for (Kurs kurs : erfassteKurse) {
            Kurs kursToBeDeleted = kursDao.findById(kurs.getKursId());
            if (kursToBeDeleted != null) {
                kursDao.remove(kursToBeDeleted);
            }
        }
        for (Kurs kurs : kurseSemester3) {
            Kurs kursToBeDeleted = kursDao.findById(kurs.getKursId());
            if (kursToBeDeleted != null) {
                kursDao.remove(kursToBeDeleted);
            }
        }
        for (Kurs kurs : kurseSemester2) {
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

}