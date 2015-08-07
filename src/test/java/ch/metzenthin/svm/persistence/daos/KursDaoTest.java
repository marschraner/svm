package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.dataTypes.Anrede;
import ch.metzenthin.svm.dataTypes.Geschlecht;
import ch.metzenthin.svm.dataTypes.Semesterbezeichnung;
import ch.metzenthin.svm.dataTypes.Wochentag;
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

import static org.junit.Assert.*;

/**
 * @author Hans Stamm
 */
public class KursDaoTest {

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private KursDao kursDao;
    private SemesterDao semesterDao;
    private KurstypDao kurstypDao;
    private KursortDao kursortDao;
    private LehrkraftDao lehrkraftDao;
    private SchuelerDao schuelerDao;

    @Before
    public void setUp() throws Exception {
        entityManagerFactory = Persistence.createEntityManagerFactory("svm");
        entityManager = entityManagerFactory.createEntityManager();
        kursDao = new KursDao(entityManager);
        semesterDao = new SemesterDao(entityManager);
        kurstypDao = new KurstypDao(entityManager);
        kursortDao = new KursortDao(entityManager);
        lehrkraftDao = new LehrkraftDao(entityManager);
        schuelerDao = new SchuelerDao(entityManager);
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
            Kurstyp kurstyp = new Kurstyp("Testkurs");
            kurstypDao.save(kurstyp);
            Kursort kursort = new Kursort("Testsaal");
            kursortDao.save(kursort);
            Lehrkraft lehrkraft = new Lehrkraft(Anrede.FRAU, "Noémie", "Roos", new GregorianCalendar(1994, Calendar.MARCH, 18), "044 391 45 35", "076 384 45 35", "nroos@gmx.ch", "756.3943.8722.22", "Mi, Fr, Sa", true);
            Adresse adresse = new Adresse("Rebwiesenstrasse", "54", "8702", "Zollikon");
            lehrkraft.setAdresse(adresse);
            lehrkraftDao.save(lehrkraft);

            // Kurs
            Kurs kurs = new Kurs("2-3 J", "Vorkindergarten", Wochentag.DONNERSTAG, Time.valueOf("10:00:00"), Time.valueOf("10:50:00"), "Dies ist ein Test.");
            kurs.setSemester(semester);
            kurs.setKurstyp(kurstyp);
            kurs.setKursort(kursort);
            kurs.addLehrkraft(lehrkraft);

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
            Kurstyp kurstyp = new Kurstyp("Testkurs");
            kurstypDao.save(kurstyp);
            Kursort kursort = new Kursort("Testsaal");
            kursortDao.save(kursort);
            Lehrkraft lehrkraft1 = new Lehrkraft(Anrede.FRAU, "Noémie", "Roos", new GregorianCalendar(1994, Calendar.MARCH, 18), "044 391 45 35", "076 384 45 35", "nroos@gmx.ch", "756.3943.8722.22", "Mi, Fr, Sa", true);
            Adresse adresse1 = new Adresse("Rebwiesenstrasse", "54", "8702", "Zollikon");
            lehrkraft1.setAdresse(adresse1);
            lehrkraftDao.save(lehrkraft1);
            Lehrkraft lehrkraft2 = new Lehrkraft(Anrede.FRAU, "Nathalie", "Delley", new GregorianCalendar(1971, Calendar.DECEMBER, 16), "044 261 27 20", "076 338 05 36", "ndelley@sunrise.ch", "756.8274.3263.17", "Mi, Fr, Sa", true);
            Adresse adresse2 = new Adresse("Im Schilf", "7", "8044", "Zürich");
            lehrkraft2.setAdresse(adresse2);
            lehrkraftDao.save(lehrkraft2);

            // Kurs
            Kurs kurs = new Kurs("2-3 J", "Vorkindergarten", Wochentag.DONNERSTAG, Time.valueOf("10:00:00"), Time.valueOf("10:50:00"), null);
            kurs.setSemester(semester);
            kurs.setKurstyp(kurstyp);
            kurs.setKursort(kursort);
            kurs.addLehrkraft(lehrkraft1);
            kurs.addLehrkraft(lehrkraft2);

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
            Kurstyp kurstyp = new Kurstyp("Testkurs");
            kurstypDao.save(kurstyp);
            Kursort kursort = new Kursort("Testsaal");
            kursortDao.save(kursort);
            Lehrkraft lehrkraft1 = new Lehrkraft(Anrede.FRAU, "Noémie", "Roos", new GregorianCalendar(1994, Calendar.MARCH, 18), "044 391 45 35", "076 384 45 35", "nroos@gmx.ch", "756.3943.8722.22", "Mi, Fr, Sa", true);
            Adresse adresse1 = new Adresse("Rebwiesenstrasse", "54", "8702", "Zollikon");
            lehrkraft1.setAdresse(adresse1);
            lehrkraftDao.save(lehrkraft1);
            Lehrkraft lehrkraft2 = new Lehrkraft(Anrede.FRAU, "Nathalie", "Delley", new GregorianCalendar(1971, Calendar.DECEMBER, 16), "044 261 27 20", "076 338 05 36", "ndelley@sunrise.ch", "756.8274.3263.17", "Mi, Fr, Sa", true);
            Adresse adresse2 = new Adresse("Im Schilf", "7", "8044", "Zürich");
            lehrkraft2.setAdresse(adresse2);
            lehrkraftDao.save(lehrkraft2);

            // Kurs
            Kurs kurs = new Kurs("2-3 J", "Vorkindergarten", Wochentag.DONNERSTAG, Time.valueOf("10:00:00"), Time.valueOf("10:50:00"), null);
            kurs.setSemester(semester);
            kurs.setKurstyp(kurstyp);
            kurs.setKursort(kursort);
            kurs.addLehrkraft(lehrkraft1);
            kurs.addLehrkraft(lehrkraft2);

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
            Lehrkraft lehrkraft1Found = lehrkraftDao.findById(lehrkraft1Id);
            Lehrkraft lehrkraft2Found = lehrkraftDao.findById(lehrkraft2Id);

            // Erzwingen, dass von DB gelesen wird
            entityManager.refresh(kursFound);
            entityManager.refresh(semesterFound);
            entityManager.refresh(kurstypFound);
            entityManager.refresh(kursortFound);
            entityManager.refresh(lehrkraft1Found);
            entityManager.refresh(lehrkraft2Found);

            assertEquals("Vorkindergarten", kursFound.getStufe());
            assertEquals(1, semesterFound.getKurse().size());
            assertEquals(1, kurstypFound.getKurse().size());
            assertEquals(1, kursortFound.getKurse().size());
            assertEquals(1, lehrkraft1Found.getKurse().size());
            assertEquals(1, lehrkraft2Found.getKurse().size());

            // Kurs löschen
            kursDao.remove(kursSaved);
            entityManager.flush();

            assertNull(kursDao.findById(kursId));
            assertEquals(0, semesterFound.getKurse().size());
            assertEquals(0, kurstypFound.getKurse().size());
            assertEquals(0, kursortFound.getKurse().size());
            assertEquals(0, lehrkraft1Found.getKurse().size());
            assertEquals(0, lehrkraft2Found.getKurse().size());

        } finally {
            if (tx != null) {
                tx.rollback();
            }
        }

    }

    @Test
    public void testFindKurseSemester_and_testFindKurs_and_findKurseSchuelerSemester() {

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
            Kurstyp kurstyp1 = new Kurstyp("Testkurs1");
            kurstypDao.save(kurstyp1);
            Kurstyp kurstyp2 = new Kurstyp("Testkurs2");
            kurstypDao.save(kurstyp2);
            Kurstyp kurstyp3 = new Kurstyp("Testkurs3");
            kurstypDao.save(kurstyp3);
            Kurstyp kurstyp4 = new Kurstyp("Testkurs4");
            kurstypDao.save(kurstyp4);

            // Kursorte
            Kursort kursort1 = new Kursort("Testsaal1");
            kursortDao.save(kursort1);
            Kursort kursort2 = new Kursort("Testsaal2");
            kursortDao.save(kursort2);
            Kursort kursort3 = new Kursort("Testsaal3");
            kursortDao.save(kursort3);
            Kursort kursort4 = new Kursort("Testsaal4");
            kursortDao.save(kursort4);

            // Lehrkräfte
            Lehrkraft lehrkraft1 = new Lehrkraft(Anrede.FRAU, "Noémie", "Roos", new GregorianCalendar(1994, Calendar.MARCH, 18), "044 391 45 35", "076 384 45 35", "nroos@gmx.ch", "756.3943.8722.22", "Mi, Fr, Sa", true);
            Adresse adresse1 = new Adresse("Rebwiesenstrasse", "54", "8702", "Zollikon");
            lehrkraft1.setAdresse(adresse1);
            lehrkraftDao.save(lehrkraft1);

            Lehrkraft lehrkraft2 = new Lehrkraft(Anrede.FRAU, "Nathalie", "Delley", new GregorianCalendar(1971, Calendar.DECEMBER, 16), "044 261 27 20", "076 338 05 36", "ndelley@sunrise.ch", "756.8274.3263.17", "Mi, Fr, Sa", true);
            Adresse adresse2 = new Adresse("Im Schilf", "7", "8044", "Zürich");
            lehrkraft2.setAdresse(adresse2);
            lehrkraftDao.save(lehrkraft2);

            // 2 Kurse für Semester 1
            // Kurs1
            Kurs kurs1 = new Kurs("2-3 J", "Vorkindergarten", Wochentag.DONNERSTAG, Time.valueOf("10:00:00"), Time.valueOf("10:50:00"), "Dies ist ein Test.");
            kurs1.setSemester(semester1);
            kurs1.setKurstyp(kurstyp1);
            kurs1.setKursort(kursort1);
            kurs1.addLehrkraft(lehrkraft1);
            kursDao.save(kurs1);

            // Kurs2
            Kurs kurs2 = new Kurs("2-3 J", "Vorkindergarten", Wochentag.MONTAG, Time.valueOf("10:00:00"), Time.valueOf("10:50:00"), "Dies ist ein Test.");
            kurs2.setSemester(semester1);
            kurs2.setKurstyp(kurstyp2);
            kurs2.setKursort(kursort2);
            kurs2.addLehrkraft(lehrkraft2);
            kursDao.save(kurs2);

            // 1 Kurs für Semester 2
            Kurs kurs3 = new Kurs("2-3 J", "Vorkindergarten", Wochentag.DONNERSTAG, Time.valueOf("10:00:00"), Time.valueOf("10:50:00"), "Dies ist ein Test.");
            kurs3.setSemester(semester2);
            kurs3.setKurstyp(kurstyp3);
            kurs3.setKursort(kursort3);
            kurs3.addLehrkraft(lehrkraft1);
            kursDao.save(kurs3);

            // 0 Kuse für Semester 3

            // 1 Kurs für Semester 4
            Kurs kurs4 = new Kurs("2-3 J", "Vorkindergarten", Wochentag.DONNERSTAG, Time.valueOf("10:00:00"), Time.valueOf("10:50:00"), "Dies ist ein Test.");
            kurs4.setSemester(semester4);
            kurs4.setKurstyp(kurstyp4);
            kurs4.setKursort(kursort4);
            kurs4.addLehrkraft(lehrkraft1);
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
            Kurs kursFound = kursDao.findKurs(semester1, Wochentag.DONNERSTAG, Time.valueOf("10:00:00"), lehrkraft1);
            assertNotNull(kursFound);
            assertTrue(kursFound.isIdenticalWith(kurs1));

            // Nach nicht vorhandenem Kurs suchen
            assertNull(kursDao.findKurs(semester3, Wochentag.DONNERSTAG, Time.valueOf("10:00:00"), lehrkraft1));
            assertNull(kursDao.findKurs(semester1, Wochentag.MITTWOCH, Time.valueOf("10:00:00"), lehrkraft1));
            assertNull(kursDao.findKurs(semester1, Wochentag.DONNERSTAG, Time.valueOf("10:10:00"), lehrkraft1));
            assertNull(kursDao.findKurs(semester1, Wochentag.DONNERSTAG, Time.valueOf("10:00:00"), lehrkraft2));

            // Kurse 1 und 2 einem Schüler hinzufügen
            Schueler schueler1 = new Schueler("Jana", "Rösle", new GregorianCalendar(2012, Calendar.JULY, 24), "044 491 69 33", null, null, Geschlecht.W, "Schwester von Valentin");
            Adresse adresse = new Adresse("Hohenklingenstrasse", "15", "8049", "Zürich");
            schueler1.setAdresse(adresse);
            Angehoeriger vater = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", "044 491 69 33", null, null);
            vater.setAdresse(adresse);
            schueler1.setVater(vater);
            schueler1.setRechnungsempfaenger(vater);

            kursDao.addToSchuelerAndSave(kurs1, schueler1);
            kursDao.addToSchuelerAndSave(kurs2, schueler1);
            entityManager.flush();

            List<Kurs> kurseSchueler1 = kursDao.findKurseSchuelerSemester(schueler1, semester1, null, null, null);
            assertEquals(2, kurseSchueler1.size());
            assertEquals("Testkurs1", kurseSchueler1.get(0).getKurstyp().getBezeichnung());
            assertEquals("Testkurs2", kurseSchueler1.get(1).getKurstyp().getBezeichnung());

            // Ditto, mit genauer Kursangabe
            kurseSchueler1 = kursDao.findKurseSchuelerSemester(schueler1, semester1, Wochentag.DONNERSTAG, Time.valueOf("10:00:00"), lehrkraft1);
            assertEquals(1, kurseSchueler1.size());
            assertEquals("Testkurs1", kurseSchueler1.get(0).getKurstyp().getBezeichnung());

            // falscher Tag
            kurseSchueler1 = kursDao.findKurseSchuelerSemester(schueler1, semester1, Wochentag.FREITAG, Time.valueOf("10:00:00"), lehrkraft1);
            assertEquals(0, kurseSchueler1.size());

            // falsche Zeit
            kurseSchueler1 = kursDao.findKurseSchuelerSemester(schueler1, semester1, Wochentag.DONNERSTAG, Time.valueOf("10:10:00"), lehrkraft1);
            assertEquals(0, kurseSchueler1.size());

            // falsche Lehrkraft
            kurseSchueler1 = kursDao.findKurseSchuelerSemester(schueler1, semester1, Wochentag.DONNERSTAG, Time.valueOf("10:00:00"), lehrkraft2);
            assertEquals(0, kurseSchueler1.size());

            // Ditto für einen Schüler ohne Kurse
            Schueler schueler2 = new Schueler("Valentin", "Rösle", new GregorianCalendar(2012, Calendar.JULY, 24), "044 491 69 33", null, null, Geschlecht.M, null);
            schueler2.setAdresse(adresse);
            schueler2.setVater(vater);
            schueler2.setRechnungsempfaenger(vater);
            entityManager.flush();

            List<Kurs> kurseSchueler2 = kursDao.findKurseSchuelerSemester(schueler2, semester1, null, null, null);
            assertEquals(0, kurseSchueler2.size());


        } finally {
            if (tx != null) {
                tx.rollback();
            }
        }

    }

    @Test
    public void testAddToSchuelerAndSave() {
        EntityTransaction tx = null;
        try {
            tx = entityManager.getTransaction();
            tx.begin();

            // Semester, Kurstyp, Kursort und Lehrkräfte erzeugen
            Semester semester = new Semester("2011/2012", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(2011, Calendar.AUGUST, 20), new GregorianCalendar(2012, Calendar.FEBRUARY, 10), 21);
            semesterDao.save(semester);
            Kurstyp kurstyp = new Kurstyp("Testkurs");
            kurstypDao.save(kurstyp);
            Kursort kursort = new Kursort("Testsaal");
            kursortDao.save(kursort);
            Lehrkraft lehrkraft1 = new Lehrkraft(Anrede.FRAU, "Noémie", "Roos", new GregorianCalendar(1994, Calendar.MARCH, 18), "044 391 45 35", "076 384 45 35", "nroos@gmx.ch", "756.3943.8722.22", "Mi, Fr, Sa", true);
            Adresse adresse1 = new Adresse("Rebwiesenstrasse", "54", "8702", "Zollikon");
            lehrkraft1.setAdresse(adresse1);
            lehrkraftDao.save(lehrkraft1);

            // Kurse
            Kurs kurs1 = new Kurs("2-3 J", "Vorkindergarten", Wochentag.DONNERSTAG, Time.valueOf("10:00:00"), Time.valueOf("10:50:00"), null);
            kurs1.setSemester(semester);
            kurs1.setKurstyp(kurstyp);
            kurs1.setKursort(kursort);
            kurs1.addLehrkraft(lehrkraft1);
            kursDao.save(kurs1);
            Kurs kurs2 = new Kurs("2-3 J", "Vorkindergarten", Wochentag.FREITAG, Time.valueOf("10:00:00"), Time.valueOf("10:50:00"), null);
            kurs2.setSemester(semester);
            kurs2.setKurstyp(kurstyp);
            kurs2.setKursort(kursort);
            kurs2.addLehrkraft(lehrkraft1);
            kursDao.save(kurs2);

            // Schüler
            Schueler schueler = new Schueler("Jana", "Rösle", new GregorianCalendar(2012, Calendar.JULY, 24), null, null, null, Geschlecht.W, "Schwester von Valentin");
            Adresse adresse = new Adresse("Hohenklingenstrasse", "15", "8049", "Zürich");
            schueler.setAdresse(adresse);
            Angehoeriger vater = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", null, null, null);
            vater.setAdresse(adresse);
            schueler.setVater(vater);
            schueler.setRechnungsempfaenger(vater);

            // Kurse hinzufügen
            kursDao.addToSchuelerAndSave(kurs1, schueler);
            Schueler schuelerSaved = kursDao.addToSchuelerAndSave(kurs2, schueler);

            entityManager.flush();

            // Schueler prüfen
            Schueler schuelerFound = schuelerDao.findById(schuelerSaved.getPersonId());
            entityManager.refresh(schuelerFound);
            assertEquals(2, schuelerFound.getKurse().size());
            // Zeitlich geordnet?
            assertEquals(Wochentag.DONNERSTAG, schuelerFound.getKurseAsList().get(0).getWochentag());
            assertEquals(Wochentag.FREITAG, schuelerFound.getKurseAsList().get(1).getWochentag());

            // Kurs prüfen
            Kurs kurs1Found = kursDao.findById(kurs1.getKursId());
            entityManager.refresh(kurs1Found);
            assertEquals(1, kurs1Found.getSchueler().size());

        } finally {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    @Test
    public void testRemoveFromSchuelerAndUpdate() {
        EntityTransaction tx = null;
        try {
            tx = entityManager.getTransaction();
            tx.begin();

            // Semester, Kurstyp, Kursort und Lehrkräfte erzeugen
            Semester semester = new Semester("2011/2012", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(2011, Calendar.AUGUST, 20), new GregorianCalendar(2012, Calendar.FEBRUARY, 10), 21);
            semesterDao.save(semester);
            Kurstyp kurstyp = new Kurstyp("Testkurs");
            kurstypDao.save(kurstyp);
            Kursort kursort = new Kursort("Testsaal");
            kursortDao.save(kursort);
            Lehrkraft lehrkraft1 = new Lehrkraft(Anrede.FRAU, "Noémie", "Roos", new GregorianCalendar(1994, Calendar.MARCH, 18), "044 391 45 35", "076 384 45 35", "nroos@gmx.ch", "756.3943.8722.22", "Mi, Fr, Sa", true);
            Adresse adresse1 = new Adresse("Rebwiesenstrasse", "54", "8702", "Zollikon");
            lehrkraft1.setAdresse(adresse1);
            lehrkraftDao.save(lehrkraft1);

            // Kurse
            Kurs kurs1 = new Kurs("2-3 J", "Vorkindergarten", Wochentag.DONNERSTAG, Time.valueOf("10:00:00"), Time.valueOf("10:50:00"), null);
            kurs1.setSemester(semester);
            kurs1.setKurstyp(kurstyp);
            kurs1.setKursort(kursort);
            kurs1.addLehrkraft(lehrkraft1);
            kursDao.save(kurs1);
            Kurs kurs2 = new Kurs("2-3 J", "Vorkindergarten", Wochentag.FREITAG, Time.valueOf("10:00:00"), Time.valueOf("10:50:00"), null);
            kurs2.setSemester(semester);
            kurs2.setKurstyp(kurstyp);
            kurs2.setKursort(kursort);
            kurs2.addLehrkraft(lehrkraft1);
            kursDao.save(kurs2);

            // Schüler
            Schueler schueler = new Schueler("Jana", "Rösle", new GregorianCalendar(2012, Calendar.JULY, 24), null, null, null, Geschlecht.W, "Schwester von Valentin");
            Adresse adresse = new Adresse("Hohenklingenstrasse", "15", "8049", "Zürich");
            schueler.setAdresse(adresse);
            Angehoeriger vater = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", null, null, null);
            vater.setAdresse(adresse);
            schueler.setVater(vater);
            schueler.setRechnungsempfaenger(vater);

            // Kurse hinzufügen
            kursDao.addToSchuelerAndSave(kurs1, schueler);
            Schueler schuelerSaved = kursDao.addToSchuelerAndSave(kurs2, schueler);

            entityManager.flush();

            // Schüler prüfen
            Schueler schuelerFound = schuelerDao.findById(schuelerSaved.getPersonId());
            entityManager.refresh(schuelerFound);
            assertEquals(2, schuelerFound.getKurse().size());
            assertEquals(Wochentag.DONNERSTAG, schuelerFound.getKurseAsList().get(0).getWochentag());
            assertEquals(Wochentag.FREITAG, schuelerFound.getKurseAsList().get(1).getWochentag());

            // Kurse prüfen
            Kurs kurs1Found = kursDao.findById(kurs1.getKursId());
            entityManager.refresh(kurs1Found);
            assertEquals(1, kurs1Found.getSchueler().size());
            Kurs kurs2Found = kursDao.findById(kurs2.getKursId());
            entityManager.refresh(kurs2Found);
            assertEquals(1, kurs2Found.getSchueler().size());

            // 1. Kurs von Schüler entfernen
            Schueler schuelerUpdated = kursDao.removeFromSchuelerAndUpdate(kurs1Found, schuelerFound);
            entityManager.flush();

            schuelerFound = schuelerDao.findById(schuelerUpdated.getPersonId());
            entityManager.refresh(schuelerFound);
            assertEquals(1, schuelerFound.getKurse().size());
            assertEquals(Wochentag.FREITAG, schuelerFound.getKurseAsList().get(0).getWochentag());

            entityManager.refresh(kurs1Found);
            assertEquals(0, kurs1Found.getSchueler().size());
            entityManager.refresh(kurs2Found);
            assertEquals(1, kurs2Found.getSchueler().size());

            // 2. Kurs von Schüler entfernen
            schuelerUpdated = kursDao.removeFromSchuelerAndUpdate(kurs2Found, schuelerFound);
            entityManager.flush();

            schuelerFound = schuelerDao.findById(schuelerUpdated.getPersonId());
            entityManager.refresh(schuelerFound);
            assertEquals(0, schuelerFound.getKurse().size());

            entityManager.refresh(kurs1Found);
            assertEquals(0, kurs1Found.getSchueler().size());
            entityManager.refresh(kurs2Found);
            assertEquals(0, kurs2Found.getSchueler().size());

        } finally {
            if (tx != null) {
                tx.rollback();
            }
        }

    }


}

