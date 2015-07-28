package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.dataTypes.Anrede;
import ch.metzenthin.svm.dataTypes.Geschlecht;
import ch.metzenthin.svm.dataTypes.Semesterbezeichnung;
import ch.metzenthin.svm.dataTypes.Wochentag;
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

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Martin Schraner
 */
public class RemoveKursFromSchuelerCommandTest {

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

        Kurstyp kurstyp1 = new Kurstyp("Kurs Test1");
        SaveOrUpdateKurstypCommand saveOrUpdateKurstypCommand = new SaveOrUpdateKurstypCommand(kurstyp1, null, erfassteKurstypen);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateKurstypCommand);
        Kurstyp kurstyp2 = new Kurstyp("Kurs Test2");
        saveOrUpdateKurstypCommand = new SaveOrUpdateKurstypCommand(kurstyp2, null, erfassteKurstypen);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateKurstypCommand);

        Kursort kursort1 = new Kursort("Saal Test");
        SaveOrUpdateKursortCommand saveOrUpdateKursortCommand = new SaveOrUpdateKursortCommand(kursort1, null, erfassteKursorte);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateKursortCommand);
        Kursort kursort2 = new Kursort("Saal Test");
        saveOrUpdateKursortCommand = new SaveOrUpdateKursortCommand(kursort2, null, erfassteKursorte);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateKursortCommand);

        Lehrkraft lehrkraft1 = new Lehrkraft(Anrede.FRAU, "Noémie", "Roostest1", new GregorianCalendar(1994, Calendar.MARCH, 18), "044 391 45 35", "076 384 45 35", "nroos@gmx.ch", "756.3943.8722.22", "Mi, Fr, Sa", true);
        Adresse adresse1 = new Adresse("Rebwiesenstrasse", "54", "8702", "Zollikon");
        SaveOrUpdateLehrkraftCommand saveOrUpdateLehrkraftCommand = new SaveOrUpdateLehrkraftCommand(lehrkraft1, adresse1, null, erfassteLehrkraefte);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateLehrkraftCommand);
        Lehrkraft lehrkraft2 = new Lehrkraft(Anrede.FRAU, "Noémie", "Roostest2", new GregorianCalendar(1994, Calendar.MARCH, 18), "044 391 45 35", "076 384 45 35", "nroos@gmx.ch", "756.3943.8722.22", "Mi, Fr, Sa", true);
        Adresse adresse2 = new Adresse("Rebwiesenstrasse", "54", "8702", "Zollikon");
        saveOrUpdateLehrkraftCommand = new SaveOrUpdateLehrkraftCommand(lehrkraft2, adresse2, null, erfassteLehrkraefte);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateLehrkraftCommand);

        assertFalse(checkIfKursAvailable(semester1, kurstyp1, "2-3 J", "Vorkindergarten", Wochentag.DONNERSTAG, Time.valueOf("10:00:00"), Time.valueOf("10:50:00"), kursort1, lehrkraft1, null));
        assertFalse(checkIfKursAvailable(semester2, kurstyp2, "2-3 J", "Vorkindergarten", Wochentag.FREITAG, Time.valueOf("10:00:00"), Time.valueOf("10:50:00"), kursort2, lehrkraft2, lehrkraft1));

        // 1. Kurs erfassen
        Kurs kurs1 = new Kurs("2-3 J", "Vorkindergarten", Wochentag.DONNERSTAG, Time.valueOf("10:00:00"), Time.valueOf("10:50:00"), null);
        SaveOrUpdateKursCommand saveOrUpdateKursCommand = new SaveOrUpdateKursCommand(kurs1, semester1, kurstyp1, kursort1, lehrkraft1, null, null, erfassteKurse);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateKursCommand);

        assertTrue(checkIfKursAvailable(semester1, kurstyp1, "2-3 J", "Vorkindergarten", Wochentag.DONNERSTAG, Time.valueOf("10:00:00"), Time.valueOf("10:50:00"), kursort1, lehrkraft1, null));

        // 2. Kurs erfassen
        Kurs kurs2 = new Kurs("2-3 J", "Vorkindergarten", Wochentag.FREITAG, Time.valueOf("10:00:00"), Time.valueOf("10:50:00"), null);
        saveOrUpdateKursCommand = new SaveOrUpdateKursCommand(kurs2, semester2, kurstyp2, kursort2, lehrkraft2, lehrkraft1, null, erfassteKurse);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateKursCommand);

        assertTrue(checkIfKursAvailable(semester2, kurstyp2, "2-3 J", "Vorkindergarten", Wochentag.FREITAG, Time.valueOf("10:00:00"), Time.valueOf("10:50:00"), kursort2, lehrkraft2, lehrkraft1));


        // Schueler erfassen und Kurs hinzufügen
        Schueler schueler = new Schueler("Jana", "Rösle", new GregorianCalendar(2012, Calendar.JULY, 24), "044 491 69 33", null, null, Geschlecht.W, "Schwester von Valentin");
        Adresse adresse = new Adresse("Hohenklingenstrasse", "15", "8049", "Zürich");
        schueler.setAdresse(adresse);
        schueler.addAnmeldung(new Anmeldung(new GregorianCalendar(2015, Calendar.MAY, 1), null));
        Angehoeriger vater = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", "044 491 69 33", null, null);
        vater.setAdresse(adresse);
        schueler.setVater(vater);
        schueler.setRechnungsempfaenger(vater);

        SaveSchuelerCommand saveSchuelerCommand = new SaveSchuelerCommand(schueler);
        commandInvoker.executeCommandAsTransaction(saveSchuelerCommand);
        Schueler schuelerSaved = saveSchuelerCommand.getSavedSchueler();

        // Kurse hinzufügen
        AddKursToSchuelerAndSaveCommand addKursToSchuelerAndSaveCommand = new AddKursToSchuelerAndSaveCommand(semester1, Wochentag.DONNERSTAG, Time.valueOf("10:00:00"), lehrkraft1, schuelerSaved);
        commandInvoker.executeCommandAsTransaction(addKursToSchuelerAndSaveCommand);
        schuelerSaved = addKursToSchuelerAndSaveCommand.getSchuelerUpdated();
        addKursToSchuelerAndSaveCommand = new AddKursToSchuelerAndSaveCommand(semester2, Wochentag.FREITAG, Time.valueOf("10:00:00"), lehrkraft2, schuelerSaved);
        commandInvoker.executeCommandAsTransaction(addKursToSchuelerAndSaveCommand);
        schuelerSaved = addKursToSchuelerAndSaveCommand.getSchuelerUpdated();

        assertEquals(2, schuelerSaved.getKurse().size());
        assertEquals(kursort2, schuelerSaved.getKurse().get(0).getKursort());
        assertEquals(kursort1, schuelerSaved.getKurse().get(1).getKursort());

        // 2. Kurs von Schüler löschen
        RemoveKursFromSchuelerCommand removeKursFromSchuelerCommand = new RemoveKursFromSchuelerCommand(1, schuelerSaved);
        commandInvoker.executeCommandAsTransaction(removeKursFromSchuelerCommand);

        Schueler schuelerUpdated = removeKursFromSchuelerCommand.getSchuelerUpdated();
        assertEquals(1, schuelerUpdated.getKurse().size());
        assertEquals(kursort2, schuelerSaved.getKurse().get(0).getKursort());

        // 1. Kurs von Schüler löschen
        removeKursFromSchuelerCommand = new RemoveKursFromSchuelerCommand(0, schuelerSaved);
        commandInvoker.executeCommandAsTransaction(removeKursFromSchuelerCommand);

        schuelerUpdated = removeKursFromSchuelerCommand.getSchuelerUpdated();
        assertEquals(0, schuelerUpdated.getKurse().size());

        // Testdaten löschen
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            SchuelerDao schuelerDao = new SchuelerDao(entityManager);
            Schueler schuelerToBeDeleted = schuelerDao.findById(schuelerSaved.getPersonId());
            schuelerDao.remove(schuelerToBeDeleted);
            entityManager.getTransaction().commit();
            entityManager.getTransaction().begin();
            KursDao kursDao = new KursDao(entityManager);
            for (Kurs kurs : erfassteKurse) {
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

    private boolean checkIfKursAvailable(Semester semester, Kurstyp kurstyp, String altersbereich, String stufe, Wochentag wochentag, Time zeitBeginn, Time zeitEnde, Kursort kursort, Lehrkraft lehrkraft1, Lehrkraft lehrkraft2) {
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
                    && kurs.getLehrkraefte().get(0).equals(lehrkraft1)) {
                if (kurs.getLehrkraefte().size() == 1) {
                    if (lehrkraft2 == null) {
                        return true;
                    }
                } else {
                    if (kurs.getLehrkraefte().get(1).equals(lehrkraft2)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}