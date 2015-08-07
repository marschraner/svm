package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Anrede;
import ch.metzenthin.svm.common.dataTypes.Semesterbezeichnung;
import ch.metzenthin.svm.common.dataTypes.Wochentag;
import ch.metzenthin.svm.persistence.daos.*;
import ch.metzenthin.svm.persistence.entities.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Martin Schraner
 */
public class ImportKurseFromPreviousSemesterCommandTest {

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

        List<Semester> erfassteSemester = new ArrayList<>();
        List<Kurstyp> erfassteKurstypen = new ArrayList<>();
        List<Kursort> erfassteKursorte = new ArrayList<>();
        List<Lehrkraft> erfassteLehrkraefte = new ArrayList<>();
        List<Kurs> erfassteKurse = new ArrayList<>();

        // Semester, Kurstyp, Kursort und Lehrkräfte erzeugen
        Semester semester1 = new Semester("1911/1912", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(1911, Calendar.AUGUST, 20), new GregorianCalendar(1912, Calendar.FEBRUARY, 10), 21);
        SaveOrUpdateSemesterCommand saveOrUpdateSemesterCommand = new SaveOrUpdateSemesterCommand(semester1, null, erfassteSemester);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateSemesterCommand);
        Semester semester2 = new Semester("1911/1912", Semesterbezeichnung.ZWEITES_SEMESTER, new GregorianCalendar(1912, Calendar.FEBRUARY, 20), new GregorianCalendar(1912, Calendar.JULY, 10), 21);
        saveOrUpdateSemesterCommand = new SaveOrUpdateSemesterCommand(semester2, null, erfassteSemester);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateSemesterCommand);
        Semester semester3 = new Semester("1912/1913", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(1912, Calendar.AUGUST, 20), new GregorianCalendar(1913, Calendar.FEBRUARY, 10), 21);
        saveOrUpdateSemesterCommand = new SaveOrUpdateSemesterCommand(semester3, null, erfassteSemester);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateSemesterCommand);

        Kurstyp kurstyp1 = new Kurstyp("Kurs Test");
        SaveOrUpdateKurstypCommand saveOrUpdateKurstypCommand = new SaveOrUpdateKurstypCommand(kurstyp1, null, erfassteKurstypen);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateKurstypCommand);

        Kursort kursort1 = new Kursort("Saal Test");
        SaveOrUpdateKursortCommand saveOrUpdateKursortCommand = new SaveOrUpdateKursortCommand(kursort1, null, erfassteKursorte);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateKursortCommand);

        Lehrkraft lehrkraft1 = new Lehrkraft(Anrede.FRAU, "Noémie", "Roostest", new GregorianCalendar(1994, Calendar.MARCH, 18), "044 391 45 35", "076 384 45 35", "nroos@gmx.ch", "756.3943.8722.22", "Mi, Fr, Sa", true);
        Adresse adresse1 = new Adresse("Rebwiesenstrasse", "54", "8702", "Zollikon");
        SaveOrUpdateLehrkraftCommand saveOrUpdateLehrkraftCommand = new SaveOrUpdateLehrkraftCommand(lehrkraft1, adresse1, null, erfassteLehrkraefte);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateLehrkraftCommand);

        // 2 Kurse für semester1 erfassen
        Kurs kurs1 = new Kurs("2-3 J", "Vorkindergarten", Wochentag.DONNERSTAG, Time.valueOf("10:00:00"), Time.valueOf("10:50:00"), null);
        SaveOrUpdateKursCommand saveOrUpdateKursCommand = new SaveOrUpdateKursCommand(kurs1, semester1, kurstyp1, kursort1, lehrkraft1, null, null, erfassteKurse);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateKursCommand);
        Kurs kurs2 = new Kurs("2-3 J", "Vorkindergarten", Wochentag.FREITAG, Time.valueOf("10:00:00"), Time.valueOf("10:50:00"), null);
        saveOrUpdateKursCommand = new SaveOrUpdateKursCommand(kurs2, semester1, kurstyp1, kursort1, lehrkraft1, null, null, erfassteKurse);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateKursCommand);

        assertEquals(2, erfassteKurse.size());

        // Für semester3 Kurse (von semester2) importieren
        List<Kurs> kurseSemester3 = new ArrayList<>();
        ImportKurseFromPreviousSemesterCommand importKurseFromPreviousSemesterCommand = new ImportKurseFromPreviousSemesterCommand(kurseSemester3, semester3);
        commandInvoker.executeCommandAsTransaction(importKurseFromPreviousSemesterCommand);
        assertTrue(kurseSemester3.isEmpty());

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

        // Testdaten löschen
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            KursDao kursDao = new KursDao(entityManager);
            for (Kurs kurs : erfassteKurse) {
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
            SemesterDao semesterDao = new SemesterDao(entityManager);
            for (Semester semester : erfassteSemester) {
                Semester semesterToBeDeleted = semesterDao.findById(semester.getSemesterId());
                if (semesterToBeDeleted != null) {
                    semesterDao.remove(semesterToBeDeleted);
                }
            }
            entityManager.getTransaction().commit();
            entityManager.getTransaction().begin();
            LehrkraftDao lehrkraftDao = new LehrkraftDao(entityManager);
            for (Lehrkraft lehrkraft : erfassteLehrkraefte) {
                Lehrkraft lehrkraftToBeDeleted = lehrkraftDao.findById(lehrkraft.getPersonId());
                if (lehrkraftToBeDeleted != null) {
                    lehrkraftDao.remove(lehrkraftToBeDeleted);
                }
            }
            entityManager.getTransaction().commit();
            entityManager.getTransaction().begin();
            KursortDao kursortDao = new KursortDao(entityManager);
            for (Kursort kursort : erfassteKursorte) {
                Kursort kursortToBeDeleted = kursortDao.findById(kursort.getKursortId());
                if (kursortToBeDeleted != null) {
                    kursortDao.remove(kursortToBeDeleted);
                }
            }
            entityManager.getTransaction().commit();
            entityManager.getTransaction().begin();
            KurstypDao kurstypDao = new KurstypDao(entityManager);
            for (Kurstyp kurstyp : erfassteKurstypen) {
                Kurstyp kurstypToBeDeleted = kurstypDao.findById(kurstyp.getKurstypId());
                if (kurstypToBeDeleted != null) {
                    kurstypDao.remove(kurstypToBeDeleted);
                }
            }
            entityManager.getTransaction().commit();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }

    }

}