package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.*;
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
import static org.junit.Assert.*;

/**
 * @author Martin Schraner
 */
public class DeleteKursanmeldungCommandTest {

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
        List<Kursanmeldung> erfassteKursanmeldungen = new ArrayList<>();

        // Schüler und Kurse erzeugen
        Schueler schueler1 = new Schueler("Jana", "Rösle", new GregorianCalendar(2012, Calendar.JULY, 24), "044 491 69 33", null, null, Geschlecht.W, "Schwester von Valentin");
        Adresse adresse1 = new Adresse("Hohenklingenstrasse", "15", "8049", "Zürich");
        schueler1.setAdresse(adresse1);
        schueler1.addAnmeldung(new Anmeldung(new GregorianCalendar(2015, Calendar.JANUARY, 1), null));
        Angehoeriger vater = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", "044 491 69 33", null, null);
        vater.setAdresse(adresse1);
        schueler1.setVater(vater);
        schueler1.setRechnungsempfaenger(vater);
        SaveSchuelerCommand saveSchuelerCommand = new SaveSchuelerCommand(schueler1);
        commandInvoker.executeCommandAsTransaction(saveSchuelerCommand);

        Schueler schueler2 = new Schueler("Hanna", "Hasler", new GregorianCalendar(2010, Calendar.JULY, 24), "044 422 69 33", null, null, Geschlecht.W, null);
        Adresse adresse2 = new Adresse("Hohenklingenstrasse", "22", "8049", "Zürich");
        schueler2.setAdresse(adresse2);
        schueler2.addAnmeldung(new Anmeldung(new GregorianCalendar(2013, Calendar.JANUARY, 1), null));
        Angehoeriger mutter2 = new Angehoeriger(Anrede.FRAU, "Adriana", "Hasler", "044 422 69 33", null, null);
        mutter2.setAdresse(adresse2);
        schueler2.setMutter(mutter2);
        schueler2.setRechnungsempfaenger(mutter2);
        saveSchuelerCommand = new SaveSchuelerCommand(schueler2);
        commandInvoker.executeCommandAsTransaction(saveSchuelerCommand);

        Semester semester1 = new Semester("1911/1912", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(1911, Calendar.AUGUST, 20), new GregorianCalendar(1912, Calendar.FEBRUARY, 10), 21);
        SaveOrUpdateSemesterCommand saveOrUpdateSemesterCommand = new SaveOrUpdateSemesterCommand(semester1, null, erfassteSemester);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateSemesterCommand);
        Semester semester2 = new Semester("1911/1912", Semesterbezeichnung.ZWEITES_SEMESTER, new GregorianCalendar(1912, Calendar.FEBRUARY, 20), new GregorianCalendar(1912, Calendar.JULY, 10), 21);
        saveOrUpdateSemesterCommand = new SaveOrUpdateSemesterCommand(semester2, null, erfassteSemester);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateSemesterCommand);

        Kurstyp kurstyp1 = new Kurstyp("Kurs Test1", true);
        SaveOrUpdateKurstypCommand saveOrUpdateKurstypCommand = new SaveOrUpdateKurstypCommand(kurstyp1, null, erfassteKurstypen);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateKurstypCommand);
        Kurstyp kurstyp2 = new Kurstyp("Kurs Test2", true);
        saveOrUpdateKurstypCommand = new SaveOrUpdateKurstypCommand(kurstyp2, null, erfassteKurstypen);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateKurstypCommand);

        Kursort kursort1 = new Kursort("Saal Test", true);
        SaveOrUpdateKursortCommand saveOrUpdateKursortCommand = new SaveOrUpdateKursortCommand(kursort1, null, erfassteKursorte);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateKursortCommand);
        Kursort kursort2 = new Kursort("Saal Test", true);
        saveOrUpdateKursortCommand = new SaveOrUpdateKursortCommand(kursort2, null, erfassteKursorte);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateKursortCommand);

        Lehrkraft lehrkraft1 = new Lehrkraft(Anrede.FRAU, "Noémie", "Roostest1", new GregorianCalendar(1994, Calendar.MARCH, 18), "044 391 45 35", "076 384 45 35", "nroos@gmx.ch", "756.3943.8722.22", "Mi, Fr, Sa", true);
        Adresse adresselk1 = new Adresse("Rebwiesenstrasse", "54", "8702", "Zollikon");
        SaveOrUpdateLehrkraftCommand saveOrUpdateLehrkraftCommand = new SaveOrUpdateLehrkraftCommand(lehrkraft1, adresselk1, null, erfassteLehrkraefte);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateLehrkraftCommand);
        Lehrkraft lehrkraft2 = new Lehrkraft(Anrede.FRAU, "Noémie", "Roostest2", new GregorianCalendar(1994, Calendar.MARCH, 18), "044 391 45 35", "076 384 45 35", "nroos@gmx.ch", "756.3943.8722.22", "Mi, Fr, Sa", true);
        Adresse adresselk2 = new Adresse("Rebwiesenstrasse", "54", "8702", "Zollikon");
        saveOrUpdateLehrkraftCommand = new SaveOrUpdateLehrkraftCommand(lehrkraft2, adresselk2, null, erfassteLehrkraefte);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateLehrkraftCommand);

        Kurs kurs1 = new Kurs("2-3 J", "Vorkindergarten", Wochentag.DONNERSTAG, Time.valueOf("10:00:00"), Time.valueOf("10:50:00"), null);
        SaveOrUpdateKursCommand saveOrUpdateKursCommand = new SaveOrUpdateKursCommand(kurs1, semester1, kurstyp1, kursort1, lehrkraft1, null, null, erfassteKurse);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateKursCommand);

        Kurs kurs2 = new Kurs("2-3 J", "Vorkindergarten", Wochentag.FREITAG, Time.valueOf("10:00:00"), Time.valueOf("10:50:00"), null);
        saveOrUpdateKursCommand = new SaveOrUpdateKursCommand(kurs2, semester2, kurstyp2, kursort2, lehrkraft2, lehrkraft1, null, erfassteKurse);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateKursCommand);

        assertFalse(checkIfKursanmeldungAvailable(schueler1, kurs1, new GregorianCalendar(2015, Calendar.AUGUST, 27), null, "Testbemerkung1"));
        assertFalse(checkIfKursanmeldungAvailable(schueler2, kurs2, new GregorianCalendar(2015, Calendar.AUGUST, 30), null, "Testbemerkung2"));

        // 2 Kursanmeldungen erfassen
        Kursanmeldung kursanmeldung1 = new Kursanmeldung(schueler1, kurs1, new GregorianCalendar(2015, Calendar.AUGUST, 27), null, "Testbemerkung1");
        SaveOrUpdateKursanmeldungCommand saveOrUpdateKursanmeldungCommand = new SaveOrUpdateKursanmeldungCommand(kursanmeldung1, null, erfassteKursanmeldungen);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateKursanmeldungCommand);
        Kursanmeldung kursanmeldung2 = new Kursanmeldung(schueler2, kurs2, new GregorianCalendar(2015, Calendar.AUGUST, 30), null, "Testbemerkung2");
        saveOrUpdateKursanmeldungCommand = new SaveOrUpdateKursanmeldungCommand(kursanmeldung2, null, erfassteKursanmeldungen);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateKursanmeldungCommand);

        assertEquals(2, erfassteKursanmeldungen.size());

        // Kursanmeldungen löschen
        DeleteKursanmeldungCommand deleteKursanmeldungCommand = new DeleteKursanmeldungCommand(erfassteKursanmeldungen, 1);
        commandInvoker.executeCommandAsTransaction(deleteKursanmeldungCommand);
        assertEquals(1, erfassteKursanmeldungen.size());

        deleteKursanmeldungCommand = new DeleteKursanmeldungCommand(erfassteKursanmeldungen, 0);
        commandInvoker.executeCommandAsTransaction(deleteKursanmeldungCommand);
        assertTrue(erfassteKursanmeldungen.isEmpty());

        // Testdaten löschen
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            KursanmeldungDao kursanmeldungDao = new KursanmeldungDao(entityManager);
            for (Kursanmeldung kursanmeldung : erfassteKursanmeldungen) {
                Kursanmeldung kursanmeldungToBeDeleted = kursanmeldungDao.findById(new KursanmeldungId(kursanmeldung.getSchueler().getPersonId(), kursanmeldung.getKurs().getKursId()));
                if (kursanmeldungToBeDeleted != null) {
                    kursanmeldungDao.remove(kursanmeldungToBeDeleted);
                }
            }
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
            entityManager.getTransaction().begin();
            SchuelerDao schuelerDao = new SchuelerDao(entityManager);
            Schueler schuelerToBeRemoved1 = schuelerDao.findById(schueler1.getPersonId());
            Schueler schuelerToBeRemoved2 = schuelerDao.findById(schueler2.getPersonId());
            schuelerDao.remove(schuelerToBeRemoved1);
            schuelerDao.remove(schuelerToBeRemoved2);
            entityManager.getTransaction().commit();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    private boolean checkIfKursanmeldungAvailable(Schueler schueler, Kurs kurs, Calendar anmeldedatum, Calendar abmeldedatum, String bemerkungen) {
        FindKursanmeldungenSchuelerCommand findKursanmeldungenSchuelerCommand = new FindKursanmeldungenSchuelerCommand(schueler);
        commandInvoker.executeCommandAsTransaction(findKursanmeldungenSchuelerCommand);
        List<Kursanmeldung> kursanmeldungenSchueler = findKursanmeldungenSchuelerCommand.getKursanmeldungenFound();
        for (Kursanmeldung kursanmeldung : kursanmeldungenSchueler) {
            if (kursanmeldung.getKurs().equals(kurs)
                    && ((kursanmeldung.getAnmeldedatum() == null && anmeldedatum == null) || (kursanmeldung.getAnmeldedatum() != null && kursanmeldung.getAnmeldedatum().equals(anmeldedatum)))
                    && ((kursanmeldung.getAbmeldedatum() == null && abmeldedatum == null) || (kursanmeldung.getAbmeldedatum() != null && kursanmeldung.getAbmeldedatum().equals(abmeldedatum)))
                    && kursanmeldung.getBemerkungen().equals(bemerkungen)) {
                return true;
            }
        }
        return false;
    }

}