package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.common.dataTypes.Anrede;
import ch.metzenthin.svm.common.dataTypes.Semesterbezeichnung;
import ch.metzenthin.svm.common.dataTypes.Wochentag;
import ch.metzenthin.svm.common.utils.PersistenceProperties;
import ch.metzenthin.svm.persistence.entities.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.sql.Time;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;
import static org.junit.Assert.*;

/**
 * @author Martin Schraner
 */
public class KursDaoTest {

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private KursDao kursDao;
    private SemesterDao semesterDao;
    private KurstypDao kurstypDao;
    private KursortDao kursortDao;
    private MitarbeiterDao mitarbeiterDao;

    @Before
    public void setUp() throws Exception {
        createSvmPropertiesFileDefault();
        entityManagerFactory = Persistence.createEntityManagerFactory("svm", PersistenceProperties.getPersistenceProperties());
        entityManager = entityManagerFactory.createEntityManager();
        kursDao = new KursDao(entityManager);
        semesterDao = new SemesterDao(entityManager);
        kurstypDao = new KurstypDao(entityManager);
        kursortDao = new KursortDao(entityManager);
        mitarbeiterDao = new MitarbeiterDao(entityManager);
    }

    @After
    public void tearDown() throws Exception {
        if (entityManager != null) {
            entityManager.close();
        }
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }

    @Test
    public void testFindById() {
        EntityTransaction tx = null;
        try {
            tx = entityManager.getTransaction();
            tx.begin();

            // Semester, Kurstyp, Kursort und Lehrkraft erzeugen
            Semester semester = new Semester("2011/2012", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(2011, Calendar.AUGUST, 20), new GregorianCalendar(2012, Calendar.FEBRUARY, 10), 21);
            semesterDao.save(semester);
            Kurstyp kurstyp = new Kurstyp("Testkurs", true);
            kurstypDao.save(kurstyp);
            Kursort kursort = new Kursort("Testsaal", true);
            kursortDao.save(kursort);
            Mitarbeiter mitarbeiter = new Mitarbeiter(Anrede.FRAU, "Noémie", "Roos", new GregorianCalendar(1994, Calendar.MARCH, 18), "044 391 45 35", "076 384 45 35", "nroos@gmx.ch", "756.3943.8722.22", true, "Mi, Fr, Sa", null, true);
            Adresse adresse = new Adresse("Rebwiesenstrasse", "54", "8702", "Zollikon");
            mitarbeiter.setAdresse(adresse);
            mitarbeiterDao.save(mitarbeiter);

            // Kurs
            Kurs kurs = new Kurs("2-3 J", "Vorkindergarten", Wochentag.DONNERSTAG, Time.valueOf("10:00:00"), Time.valueOf("10:50:00"), "Dies ist ein Test.");
            kurs.setSemester(semester);
            kurs.setKurstyp(kurstyp);
            kurs.setKursort(kursort);
            kurs.addLehrkraft(mitarbeiter);

            entityManager.persist(kurs);

            Kurs kursFound = kursDao.findById(kurs.getKursId());

            assertEquals("2-3 J", kursFound.getAltersbereich());
            assertEquals("Vorkindergarten", kursFound.getStufe());
            assertEquals(Wochentag.DONNERSTAG, kursFound.getWochentag());
            assertEquals(Time.valueOf("10:00:00"), kursFound.getZeitBeginn());
            assertEquals(Time.valueOf("10:50:00"), kursFound.getZeitEnde());
            assertEquals("Dies ist ein Test.", kursFound.getBemerkungen());
            assertEquals("2011/2012", kursFound.getSemester().getSchuljahr());
            assertEquals("Testkurs", kursFound.getKurstyp().getBezeichnung());
            assertEquals("Testsaal", kursFound.getKursort().getBezeichnung());
            assertEquals("Roos", kursFound.getLehrkraefte().get(0).getNachname());

        } finally {
            if (tx != null)
                tx.rollback();
        }

    }

    @Test
    public void testSave() {
        EntityTransaction tx = null;

        try {
            tx = entityManager.getTransaction();
            tx.begin();

            // Semester, Kurstyp, Kursort und Lehrkräfte erzeugen
            Semester semester = new Semester("2011/2012", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(2011, Calendar.AUGUST, 20), new GregorianCalendar(2012, Calendar.FEBRUARY, 10), 21);
            semesterDao.save(semester);
            Kurstyp kurstyp = new Kurstyp("Testkurs", true);
            kurstypDao.save(kurstyp);
            Kursort kursort = new Kursort("Testsaal", true);
            kursortDao.save(kursort);
            Mitarbeiter mitarbeiter1 = new Mitarbeiter(Anrede.FRAU, "Noémie", "Roos", new GregorianCalendar(1994, Calendar.MARCH, 18), "044 391 45 35", "076 384 45 35", "nroos@gmx.ch", "756.3943.8722.22", true, "Mi, Fr, Sa", null, true);
            Adresse adresse1 = new Adresse("Rebwiesenstrasse", "54", "8702", "Zollikon");
            mitarbeiter1.setAdresse(adresse1);
            mitarbeiterDao.save(mitarbeiter1);
            Mitarbeiter mitarbeiter2 = new Mitarbeiter(Anrede.FRAU, "Nathalie", "Delley", new GregorianCalendar(1971, Calendar.DECEMBER, 16), "044 261 27 20", "076 338 05 36", "ndelley@sunrise.ch", "756.8274.3263.17", true, "Mi, Fr, Sa", null, true);
            Adresse adresse2 = new Adresse("Im Schilf", "7", "8044", "Zürich");
            mitarbeiter2.setAdresse(adresse2);
            mitarbeiterDao.save(mitarbeiter2);

            // Kurs
            Kurs kurs = new Kurs("2-3 J", "Vorkindergarten", Wochentag.DONNERSTAG, Time.valueOf("10:00:00"), Time.valueOf("10:50:00"), null);
            kurs.setSemester(semester);
            kurs.setKurstyp(kurstyp);
            kurs.setKursort(kursort);
            kurs.addLehrkraft(mitarbeiter1);
            kurs.addLehrkraft(mitarbeiter2);

            Kurs kursSaved = kursDao.save(kurs);

            entityManager.flush();

            Kurs kursFound = kursDao.findById(kursSaved.getKursId());

            // Erzwingen, dass von DB gelesen wird
            entityManager.refresh(kursFound);

            assertEquals("Vorkindergarten", kursFound.getStufe());

            // Semester
            assertEquals("2011/2012", kursFound.getSemester().getSchuljahr());
            assertEquals(1, kursFound.getSemester().getKurse().size());
            assertTrue(kursFound.getSemester().getKurse().contains(kursFound));

            // Kurstyp
            assertEquals("Testkurs", kursFound.getKurstyp().getBezeichnung());
            assertEquals(1, kursFound.getKurstyp().getKurse().size());
            assertTrue(kursFound.getKurstyp().getKurse().contains(kursFound));

            // Kursort
            assertEquals("Testsaal", kursFound.getKursort().getBezeichnung());
            assertEquals(1, kursFound.getKursort().getKurse().size());
            assertTrue(kursFound.getKursort().getKurse().contains(kursFound));

            // Lehrkräfte in Reihenfolge der Erfassung geordnet?
            assertEquals(2, kursFound.getLehrkraefte().size());
            assertEquals("Roos", kursFound.getLehrkraefte().get(0).getNachname());
            assertEquals("Delley", kursFound.getLehrkraefte().get(1).getNachname());
            assertEquals(1, kursFound.getLehrkraefte().get(0).getKurse().size());
            assertTrue(kursFound.getLehrkraefte().get(0).getKurse().contains(kursFound));

            // 1. Lehrkraft löschen
            kurs.deleteLehrkraft(kursFound.getLehrkraefte().get(0));
            assertEquals(1, kursFound.getLehrkraefte().size());
            assertEquals("Delley", kursFound.getLehrkraefte().get(0).getNachname());

        } finally {
            if (tx != null)
                tx.rollback();
        }

    }

    @Test
    public void testRemove() {
        EntityTransaction tx = null;

        try {
            tx = entityManager.getTransaction();
            tx.begin();

            // Semester, Kurstyp, Kursort und Lehrkräfte erzeugen
            Semester semester = new Semester("2011/2012", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(2011, Calendar.AUGUST, 20), new GregorianCalendar(2012, Calendar.FEBRUARY, 10), 21);
            semesterDao.save(semester);
            Kurstyp kurstyp = new Kurstyp("Testkurs", true);
            kurstypDao.save(kurstyp);
            Kursort kursort = new Kursort("Testsaal", true);
            kursortDao.save(kursort);
            Mitarbeiter mitarbeiter1 = new Mitarbeiter(Anrede.FRAU, "Noémie", "Roos", new GregorianCalendar(1994, Calendar.MARCH, 18), "044 391 45 35", "076 384 45 35", "nroos@gmx.ch", "756.3943.8722.22", true, "Mi, Fr, Sa", null, true);
            Adresse adresse1 = new Adresse("Rebwiesenstrasse", "54", "8702", "Zollikon");
            mitarbeiter1.setAdresse(adresse1);
            mitarbeiterDao.save(mitarbeiter1);
            Mitarbeiter mitarbeiter2 = new Mitarbeiter(Anrede.FRAU, "Nathalie", "Delley", new GregorianCalendar(1971, Calendar.DECEMBER, 16), "044 261 27 20", "076 338 05 36", "ndelley@sunrise.ch", "756.8274.3263.17", true, "Mi, Fr, Sa", null, true);
            Adresse adresse2 = new Adresse("Im Schilf", "7", "8044", "Zürich");
            mitarbeiter2.setAdresse(adresse2);
            mitarbeiterDao.save(mitarbeiter2);

            // Kurs
            Kurs kurs = new Kurs("2-3 J", "Vorkindergarten", Wochentag.DONNERSTAG, Time.valueOf("10:00:00"), Time.valueOf("10:50:00"), null);
            kurs.setSemester(semester);
            kurs.setKurstyp(kurstyp);
            kurs.setKursort(kursort);
            kurs.addLehrkraft(mitarbeiter1);
            kurs.addLehrkraft(mitarbeiter2);

            Kurs kursSaved = kursDao.save(kurs);

            entityManager.flush();

            int kursId = kursSaved.getKursId();
            int semesterId = kursSaved.getSemester().getSemesterId();
            int kurstypId = kursSaved.getKurstyp().getKurstypId();
            int kursortId = kursSaved.getKursort().getKursortId();
            int lehrkraft1Id = kursSaved.getLehrkraefte().get(0).getPersonId();
            int lehrkraft2Id = kursSaved.getLehrkraefte().get(1).getPersonId();

            Kurs kursFound = kursDao.findById(kursId);
            Semester semesterFound = semesterDao.findById(semesterId);
            Kurstyp kurstypFound = kurstypDao.findById(kurstypId);
            Kursort kursortFound = kursortDao.findById(kursortId);
            Mitarbeiter mitarbeiter1Found = mitarbeiterDao.findById(lehrkraft1Id);
            Mitarbeiter mitarbeiter2Found = mitarbeiterDao.findById(lehrkraft2Id);

            // Erzwingen, dass von DB gelesen wird
            entityManager.refresh(kursFound);
            entityManager.refresh(semesterFound);
            entityManager.refresh(kurstypFound);
            entityManager.refresh(kursortFound);
            entityManager.refresh(mitarbeiter1Found);
            entityManager.refresh(mitarbeiter2Found);

            assertEquals("Vorkindergarten", kursFound.getStufe());
            assertEquals(1, semesterFound.getKurse().size());
            assertEquals(1, kurstypFound.getKurse().size());
            assertEquals(1, kursortFound.getKurse().size());
            assertEquals(1, mitarbeiter1Found.getKurse().size());
            assertEquals(1, mitarbeiter2Found.getKurse().size());

            // Kurs löschen
            kursDao.remove(kursSaved);
            entityManager.flush();

            assertNull(kursDao.findById(kursId));
            assertEquals(0, semesterFound.getKurse().size());
            assertEquals(0, kurstypFound.getKurse().size());
            assertEquals(0, kursortFound.getKurse().size());
            assertEquals(0, mitarbeiter1Found.getKurse().size());
            assertEquals(0, mitarbeiter2Found.getKurse().size());

        } finally {
            if (tx != null) {
                tx.rollback();
            }
        }

    }

    @Test
    public void testFindKurseSemester_and_testFindKurs() {

        EntityTransaction tx = null;

        try {
            tx = entityManager.getTransaction();
            tx.begin();

            // Semester
            Semester semester1 = new Semester("2011/2012", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(2011, Calendar.AUGUST, 20), new GregorianCalendar(2012, Calendar.FEBRUARY, 10), 21);
            semesterDao.save(semester1);
            Semester semester2 = new Semester("2011/2012", Semesterbezeichnung.ZWEITES_SEMESTER, new GregorianCalendar(2012, Calendar.FEBRUARY, 20), new GregorianCalendar(2012, Calendar.JULY, 10), 21);
            semesterDao.save(semester2);
            Semester semester3 = new Semester("2012/2013", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(2012, Calendar.AUGUST, 20), new GregorianCalendar(2013, Calendar.FEBRUARY, 10), 21);
            semesterDao.save(semester3);
            Semester semester4 = new Semester("2013/2014", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(2013, Calendar.AUGUST, 20), new GregorianCalendar(2014, Calendar.FEBRUARY, 10), 21);
            semesterDao.save(semester4);

            // Kurstyp
            Kurstyp kurstyp1 = new Kurstyp("Testkurs1", true);
            kurstypDao.save(kurstyp1);
            Kurstyp kurstyp2 = new Kurstyp("Testkurs2", true);
            kurstypDao.save(kurstyp2);
            Kurstyp kurstyp3 = new Kurstyp("Testkurs3", true);
            kurstypDao.save(kurstyp3);
            Kurstyp kurstyp4 = new Kurstyp("Testkurs4", true);
            kurstypDao.save(kurstyp4);

            // Kursorte
            Kursort kursort1 = new Kursort("Testsaal1", true);
            kursortDao.save(kursort1);
            Kursort kursort2 = new Kursort("Testsaal2", true);
            kursortDao.save(kursort2);
            Kursort kursort3 = new Kursort("Testsaal3", true);
            kursortDao.save(kursort3);
            Kursort kursort4 = new Kursort("Testsaal4", true);
            kursortDao.save(kursort4);

            // Lehrkräfte
            Mitarbeiter mitarbeiter1 = new Mitarbeiter(Anrede.FRAU, "Noémie", "Roos", new GregorianCalendar(1994, Calendar.MARCH, 18), "044 391 45 35", "076 384 45 35", "nroos@gmx.ch", "756.3943.8722.22", true, "Mi, Fr, Sa", null, true);
            Adresse adresse1 = new Adresse("Rebwiesenstrasse", "54", "8702", "Zollikon");
            mitarbeiter1.setAdresse(adresse1);
            mitarbeiterDao.save(mitarbeiter1);

            Mitarbeiter mitarbeiter2 = new Mitarbeiter(Anrede.FRAU, "Nathalie", "Delley", new GregorianCalendar(1971, Calendar.DECEMBER, 16), "044 261 27 20", "076 338 05 36", "ndelley@sunrise.ch", "756.8274.3263.17", true, "Mi, Fr, Sa", null, true);
            Adresse adresse2 = new Adresse("Im Schilf", "7", "8044", "Zürich");
            mitarbeiter2.setAdresse(adresse2);
            mitarbeiterDao.save(mitarbeiter2);

            // 2 Kurse für Semester 1
            // Kurs1
            Kurs kurs1 = new Kurs("2-3 J", "Vorkindergarten", Wochentag.DONNERSTAG, Time.valueOf("10:00:00"), Time.valueOf("10:50:00"), "Dies ist ein Test.");
            kurs1.setSemester(semester1);
            kurs1.setKurstyp(kurstyp1);
            kurs1.setKursort(kursort1);
            kurs1.addLehrkraft(mitarbeiter1);
            kursDao.save(kurs1);

            // Kurs2
            Kurs kurs2 = new Kurs("2-3 J", "Vorkindergarten", Wochentag.MONTAG, Time.valueOf("10:00:00"), Time.valueOf("10:50:00"), "Dies ist ein Test.");
            kurs2.setSemester(semester1);
            kurs2.setKurstyp(kurstyp2);
            kurs2.setKursort(kursort2);
            kurs2.addLehrkraft(mitarbeiter2);
            kursDao.save(kurs2);

            // 1 Kurs für Semester 2
            Kurs kurs3 = new Kurs("2-3 J", "Vorkindergarten", Wochentag.DONNERSTAG, Time.valueOf("10:00:00"), Time.valueOf("10:50:00"), "Dies ist ein Test.");
            kurs3.setSemester(semester2);
            kurs3.setKurstyp(kurstyp3);
            kurs3.setKursort(kursort3);
            kurs3.addLehrkraft(mitarbeiter1);
            kursDao.save(kurs3);

            // 0 Kuse für Semester 3

            // 1 Kurs für Semester 4
            Kurs kurs4 = new Kurs("2-3 J", "Vorkindergarten", Wochentag.DONNERSTAG, Time.valueOf("10:00:00"), Time.valueOf("10:50:00"), "Dies ist ein Test.");
            kurs4.setSemester(semester4);
            kurs4.setKurstyp(kurstyp4);
            kurs4.setKursort(kursort4);
            kurs4.addLehrkraft(mitarbeiter1);
            kursDao.save(kurs4);

            entityManager.flush();

            // Nach Kursen für Semester 1 suchen
            List<Kurs> kursList1 = kursDao.findKurseSemester(semester1);
            assertEquals(2, kursList1.size());
            assertEquals("Testkurs1", kursList1.get(0).getKurstyp().getBezeichnung());
            assertEquals("Testkurs2", kursList1.get(1).getKurstyp().getBezeichnung());

            // Nach Kursen für Semester 2 suchen
            List<Kurs> kursList2 = kursDao.findKurseSemester(semester2);
            assertEquals(1, kursList2.size());
            assertEquals("Testkurs3", kursList2.get(0).getKurstyp().getBezeichnung());

            // Nach Kursen für Semester 3 suchen
            List<Kurs> kursList3 = kursDao.findKurseSemester(semester3);
            assertTrue(kursList3.isEmpty());

            // Nach Kursen für Semester 4 suchen
            List<Kurs> kursList4 = kursDao.findKurseSemester(semester4);
            assertEquals(1, kursList4.size());
            assertEquals("Testkurs4", kursList4.get(0).getKurstyp().getBezeichnung());

            // Nach Kurs 1 suchen
            Kurs kursFound = kursDao.findKurs(semester1, Wochentag.DONNERSTAG, Time.valueOf("10:00:00"), mitarbeiter1);
            assertNotNull(kursFound);
            assertTrue(kursFound.isIdenticalWith(kurs1));

            // Nach nicht vorhandenem Kurs suchen
            assertNull(kursDao.findKurs(semester3, Wochentag.DONNERSTAG, Time.valueOf("10:00:00"), mitarbeiter1));
            assertNull(kursDao.findKurs(semester1, Wochentag.MITTWOCH, Time.valueOf("10:00:00"), mitarbeiter1));
            assertNull(kursDao.findKurs(semester1, Wochentag.DONNERSTAG, Time.valueOf("10:10:00"), mitarbeiter1));
            assertNull(kursDao.findKurs(semester1, Wochentag.DONNERSTAG, Time.valueOf("10:00:00"), mitarbeiter2));

        } finally {
            if (tx != null) {
                tx.rollback();
            }
        }

    }

}

