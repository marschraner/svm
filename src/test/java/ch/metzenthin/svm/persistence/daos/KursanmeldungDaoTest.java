package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.common.dataTypes.Anrede;
import ch.metzenthin.svm.common.dataTypes.Geschlecht;
import ch.metzenthin.svm.common.dataTypes.Semesterbezeichnung;
import ch.metzenthin.svm.common.dataTypes.Wochentag;
import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.entities.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Time;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;
import static org.junit.Assert.*;

/**
 * @author Martin Schraner
 */
public class KursanmeldungDaoTest {

    private final KursanmeldungDao kursanmeldungDao = new KursanmeldungDao();
    private final KursDao kursDao = new KursDao();
    private final SemesterDao semesterDao = new SemesterDao();
    private final KurstypDao kurstypDao = new KurstypDao();
    private final KursortDao kursortDao = new KursortDao();
    private final MitarbeiterDao mitarbeiterDao = new MitarbeiterDao();

    private DB db;

    @Before
    public void setUp() {
        createSvmPropertiesFileDefault();
        db = DBFactory.getInstance();
    }

    @After
    public void tearDown() {
        db.closeSession();
    }

    @SuppressWarnings("ExtractMethodRecommender")
    @Test
    public void testFindById() {
        EntityManager entityManager = db.getCurrentEntityManager();
        EntityTransaction tx = null;
        try {
            tx = entityManager.getTransaction();
            tx.begin();

            // Kurs
            Semester semester = new Semester("2011/2012", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(2011, Calendar.AUGUST, 20), new GregorianCalendar(2012, Calendar.FEBRUARY, 10), new GregorianCalendar(2011, Calendar.OCTOBER, 5), new GregorianCalendar(2011, Calendar.OCTOBER, 17), new GregorianCalendar(2011, Calendar.DECEMBER, 21), new GregorianCalendar(2012, Calendar.JANUARY, 2));
            semesterDao.save(semester);
            Kurstyp kurstyp = new Kurstyp("Testkurs", true);
            kurstypDao.save(kurstyp);
            Kursort kursort = new Kursort("Testsaal", true);
            kursortDao.save(kursort);
            Mitarbeiter mitarbeiter1 = new Mitarbeiter(Anrede.FRAU, "Noémie", "Roos", new GregorianCalendar(1994, Calendar.MARCH, 18), "044 391 45 35", "076 384 45 35", "nroos@gmx.ch", "756.3943.8722.22", "CH31 8123 9000 0012 4568 9", true, "Mi, Fr, Sa", null, true);
            Adresse adresse1 = new Adresse("Rebwiesenstrasse", "54", "8702", "Zollikon");
            mitarbeiter1.setAdresse(adresse1);
            mitarbeiterDao.save(mitarbeiter1);
            Kurs kurs = new Kurs("2-3 J", "Vorkindergarten", Wochentag.DONNERSTAG, Time.valueOf("10:00:00"), Time.valueOf("10:50:00"), null);
            kurs.setSemester(semester);
            kurs.setKurstyp(kurstyp);
            kurs.setKursort(kursort);
            kurs.addLehrkraft(mitarbeiter1);
            kursDao.save(kurs);

            // Schüler
            Schueler schueler = new Schueler("Jana", "Rösle", new GregorianCalendar(2012, Calendar.JULY, 24), null, null, null, Geschlecht.W, "Schwester von Valentin");
            Adresse adresse = new Adresse("Hohenklingenstrasse", "15", "8049", "Zürich");
            schueler.setAdresse(adresse);
            Angehoeriger vater = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", null, null, null, false);
            vater.setAdresse(adresse);
            schueler.setVater(vater);
            schueler.setRechnungsempfaenger(vater);
            entityManager.persist(schueler);

            // Kurseinteilung
            Kursanmeldung kursanmeldung = new Kursanmeldung(schueler, kurs, new GregorianCalendar(2015, Calendar.AUGUST, 30), new GregorianCalendar(2016, Calendar.FEBRUARY, 2), "Testbemerkung");
            entityManager.persist(kursanmeldung);

            Kursanmeldung kursanmeldungFound = kursanmeldungDao.findById(new KursanmeldungId(kursanmeldung.getSchueler().getPersonId(), kursanmeldung.getKurs().getKursId()));
            assertEquals("Jana", kursanmeldungFound.getSchueler().getVorname());
            assertEquals(Wochentag.DONNERSTAG, kursanmeldungFound.getKurs().getWochentag());
            assertEquals(new GregorianCalendar(2015, Calendar.AUGUST, 30), kursanmeldungFound.getAnmeldedatum());
            assertEquals(new GregorianCalendar(2016, Calendar.FEBRUARY, 2), kursanmeldungFound.getAbmeldedatum());
            assertEquals("Testbemerkung", kursanmeldungFound.getBemerkungen());
        } finally {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    @SuppressWarnings("ExtractMethodRecommender")
    @Test
    public void testSave() {
        EntityManager entityManager = db.getCurrentEntityManager();
        EntityTransaction tx = null;
        try {
            tx = entityManager.getTransaction();
            tx.begin();

            // Kurs
            Semester semester = new Semester("2011/2012", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(2011, Calendar.AUGUST, 20), new GregorianCalendar(2012, Calendar.FEBRUARY, 10), new GregorianCalendar(2011, Calendar.OCTOBER, 5), new GregorianCalendar(2011, Calendar.OCTOBER, 17), new GregorianCalendar(2011, Calendar.DECEMBER, 21), new GregorianCalendar(2012, Calendar.JANUARY, 2));
            semesterDao.save(semester);
            Kurstyp kurstyp = new Kurstyp("Testkurs", true);
            kurstypDao.save(kurstyp);
            Kursort kursort = new Kursort("Testsaal", true);
            kursortDao.save(kursort);
            Mitarbeiter mitarbeiter1 = new Mitarbeiter(Anrede.FRAU, "Noémie", "Roos", new GregorianCalendar(1994, Calendar.MARCH, 18), "044 391 45 35", "076 384 45 35", "nroos@gmx.ch", "756.3943.8722.22", "CH31 8123 9000 0012 4568 9", true, "Mi, Fr, Sa", null, true);
            Adresse adresse1 = new Adresse("Rebwiesenstrasse", "54", "8702", "Zollikon");
            mitarbeiter1.setAdresse(adresse1);
            mitarbeiterDao.save(mitarbeiter1);
            Kurs kurs = new Kurs("2-3 J", "Vorkindergarten", Wochentag.DONNERSTAG, Time.valueOf("10:00:00"), Time.valueOf("10:50:00"), null);
            kurs.setSemester(semester);
            kurs.setKurstyp(kurstyp);
            kurs.setKursort(kursort);
            kurs.addLehrkraft(mitarbeiter1);
            kursDao.save(kurs);

            // Schüler
            Schueler schueler = new Schueler("Jana", "Rösle", new GregorianCalendar(2012, Calendar.JULY, 24), null, null, null, Geschlecht.W, "Schwester von Valentin");
            Adresse adresse = new Adresse("Hohenklingenstrasse", "15", "8049", "Zürich");
            schueler.setAdresse(adresse);
            Angehoeriger vater = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", null, null, null, false);
            vater.setAdresse(adresse);
            schueler.setVater(vater);
            schueler.setRechnungsempfaenger(vater);
            entityManager.persist(schueler);

            // Kurseinteilung
            Kursanmeldung kursanmeldung = new Kursanmeldung(schueler, kurs, new GregorianCalendar(2015, Calendar.AUGUST, 30), new GregorianCalendar(2016, Calendar.FEBRUARY, 2), null);
            kursanmeldungDao.save(kursanmeldung);

            Kursanmeldung kursanmeldungFound = kursanmeldungDao.findById(new KursanmeldungId(kursanmeldung.getSchueler().getPersonId(), kursanmeldung.getKurs().getKursId()));
            assertEquals("Jana", kursanmeldungFound.getSchueler().getVorname());
            assertEquals(Wochentag.DONNERSTAG, kursanmeldungFound.getKurs().getWochentag());

            assertEquals(1, schueler.getKursanmeldungen().size());
            assertEquals(1, kurs.getKursanmeldungen().size());

        } finally {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    @SuppressWarnings("ExtractMethodRecommender")
    @Test
    public void testRemove() {
        EntityManager entityManager = db.getCurrentEntityManager();
        EntityTransaction tx = null;
        try {
            tx = entityManager.getTransaction();
            tx.begin();

            // Kurs
            Semester semester = new Semester("2011/2012", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(2011, Calendar.AUGUST, 20), new GregorianCalendar(2012, Calendar.FEBRUARY, 10), new GregorianCalendar(2011, Calendar.OCTOBER, 5), new GregorianCalendar(2011, Calendar.OCTOBER, 17), new GregorianCalendar(2011, Calendar.DECEMBER, 21), new GregorianCalendar(2012, Calendar.JANUARY, 2));
            semesterDao.save(semester);
            Kurstyp kurstyp = new Kurstyp("Testkurs", true);
            kurstypDao.save(kurstyp);
            Kursort kursort = new Kursort("Testsaal", true);
            kursortDao.save(kursort);
            Mitarbeiter mitarbeiter1 = new Mitarbeiter(Anrede.FRAU, "Noémie", "Roos", new GregorianCalendar(1994, Calendar.MARCH, 18), "044 391 45 35", "076 384 45 35", "nroos@gmx.ch", "756.3943.8722.22", "CH31 8123 9000 0012 4568 9", true, "Mi, Fr, Sa", null, true);
            Adresse adresse1 = new Adresse("Rebwiesenstrasse", "54", "8702", "Zollikon");
            mitarbeiter1.setAdresse(adresse1);
            mitarbeiterDao.save(mitarbeiter1);
            Kurs kurs = new Kurs("2-3 J", "Vorkindergarten", Wochentag.DONNERSTAG, Time.valueOf("10:00:00"), Time.valueOf("10:50:00"), null);
            kurs.setSemester(semester);
            kurs.setKurstyp(kurstyp);
            kurs.setKursort(kursort);
            kurs.addLehrkraft(mitarbeiter1);
            kursDao.save(kurs);

            // Schüler
            Schueler schueler = new Schueler("Jana", "Rösle", new GregorianCalendar(2012, Calendar.JULY, 24), null, null, null, Geschlecht.W, "Schwester von Valentin");
            Adresse adresse = new Adresse("Hohenklingenstrasse", "15", "8049", "Zürich");
            schueler.setAdresse(adresse);
            Angehoeriger vater = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", null, null, null, false);
            vater.setAdresse(adresse);
            schueler.setVater(vater);
            schueler.setRechnungsempfaenger(vater);
            entityManager.persist(schueler);

            // Kurseinteilung
            Kursanmeldung kursanmeldung = new Kursanmeldung(schueler, kurs, new GregorianCalendar(2015, Calendar.AUGUST, 30), null, null);
            Kursanmeldung kursanmeldungSaved = kursanmeldungDao.save(kursanmeldung);
            int personId = kursanmeldungSaved.getSchueler().getPersonId();
            int kursId = kursanmeldungSaved.getKurs().getKursId();

            entityManager.flush();
            assertNotNull(kursanmeldungDao.findById(new KursanmeldungId(personId, kursId)));
            assertEquals(1, schueler.getKursanmeldungen().size());
            assertEquals(1, kurs.getKursanmeldungen().size());

            // Delete Kurseinteilung
            kursanmeldungDao.remove(kursanmeldungSaved);
            entityManager.flush();
            assertNull(kursanmeldungDao.findById(new KursanmeldungId(personId, kursId)));
            assertEquals(0, schueler.getKursanmeldungen().size());
            assertEquals(0, kurs.getKursanmeldungen().size());

        } finally {
            if (tx != null) {
                tx.rollback();
            }
        }

    }

    @Test
    public void testFindKurseinteilungenSchueler() {
        EntityManager entityManager = db.getCurrentEntityManager();
        EntityTransaction tx = null;
        try {
            tx = entityManager.getTransaction();
            tx.begin();

            // Schüler, Kurse erzeugen
            Schueler schueler1 = new Schueler("Jana", "Rösle", new GregorianCalendar(2012, Calendar.JULY, 24), "044 491 69 33", null, null, Geschlecht.W, "Schwester von Valentin");
            Adresse adresse = new Adresse("Hohenklingenstrasse", "15", "8049", "Zürich");
            schueler1.setAdresse(adresse);
            schueler1.addAnmeldung(new Anmeldung(new GregorianCalendar(2015, Calendar.JANUARY, 1), null));
            Angehoeriger vater = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", "044 491 69 33", null, null, false);
            vater.setAdresse(adresse);
            schueler1.setVater(vater);
            schueler1.setRechnungsempfaenger(vater);
            entityManager.persist(schueler1);

            Schueler schueler2 = new Schueler("Hanna", "Hasler", new GregorianCalendar(2010, Calendar.JULY, 24), "044 422 69 33", null, null, Geschlecht.W, null);
            Adresse adresse2 = new Adresse("Hohenklingenstrasse", "22", "8049", "Zürich");
            schueler2.setAdresse(adresse2);
            schueler2.addAnmeldung(new Anmeldung(new GregorianCalendar(2013, Calendar.JANUARY, 1), null));
            Angehoeriger mutter2 = new Angehoeriger(Anrede.FRAU, "Adriana", "Hasler", "044 422 69 33", null, null, false);
            mutter2.setAdresse(adresse2);
            schueler2.setMutter(mutter2);
            schueler2.setRechnungsempfaenger(mutter2);
            entityManager.persist(schueler2);

            Schueler schueler3 = new Schueler("Lina", "Hasler", new GregorianCalendar(2010, Calendar.JULY, 24), "044 422 69 33", null, null, Geschlecht.W, null);
            Adresse adresse3 = new Adresse("Hohenklingenstrasse", "22", "8049", "Zürich");
            schueler3.setAdresse(adresse3);
            schueler3.addAnmeldung(new Anmeldung(new GregorianCalendar(2013, Calendar.JANUARY, 1), null));
            Angehoeriger mutter3 = new Angehoeriger(Anrede.FRAU, "Adriana", "Hasler", "044 422 69 33", null, null, true);
            mutter3.setAdresse(adresse3);
            schueler3.setMutter(mutter3);
            schueler3.setRechnungsempfaenger(mutter3);
            entityManager.persist(schueler3);

            // Semester, Kurstyp, Kursort und Lehrkräfte erzeugen
            Semester semester = new Semester("2011/2012", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(2011, Calendar.AUGUST, 20), new GregorianCalendar(2012, Calendar.FEBRUARY, 10), new GregorianCalendar(2011, Calendar.OCTOBER, 5), new GregorianCalendar(2011, Calendar.OCTOBER, 17), new GregorianCalendar(2011, Calendar.DECEMBER, 21), new GregorianCalendar(2012, Calendar.JANUARY, 2));
            semesterDao.save(semester);
            Kurstyp kurstyp = new Kurstyp("Testkurs", true);
            kurstypDao.save(kurstyp);
            Kursort kursort = new Kursort("Testsaal", true);
            kursortDao.save(kursort);
            Mitarbeiter mitarbeiter1 = new Mitarbeiter(Anrede.FRAU, "Noémie", "Roos", new GregorianCalendar(1994, Calendar.MARCH, 18), "044 391 45 35", "076 384 45 35", "nroos@gmx.ch", "756.3943.8722.22", "CH31 8123 9000 0012 4568 9", true, "Mi, Fr, Sa", null, true);
            Adresse adresse1 = new Adresse("Rebwiesenstrasse", "54", "8702", "Zollikon");
            mitarbeiter1.setAdresse(adresse1);
            mitarbeiterDao.save(mitarbeiter1);

            // Kurse
            Kurs kurs1 = new Kurs("2-3 J", "Vorkindergarten", Wochentag.DONNERSTAG, Time.valueOf("10:00:00"), Time.valueOf("10:50:00"), null);
            kurs1.setSemester(semester);
            kurs1.setKurstyp(kurstyp);
            kurs1.setKursort(kursort);
            kurs1.addLehrkraft(mitarbeiter1);
            kursDao.save(kurs1);
            Kurs kurs2 = new Kurs("2-3 J", "Vorkindergarten", Wochentag.FREITAG, Time.valueOf("10:00:00"), Time.valueOf("10:50:00"), null);
            kurs2.setSemester(semester);
            kurs2.setKurstyp(kurstyp);
            kurs2.setKursort(kursort);
            kurs2.addLehrkraft(mitarbeiter1);
            kursDao.save(kurs2);

            // Kurseinteilungen
            Kursanmeldung kursanmeldung1 = new Kursanmeldung(schueler1, kurs1, new GregorianCalendar(2015, Calendar.AUGUST, 30), null, null);
            entityManager.persist(kursanmeldung1);
            Kursanmeldung kursanmeldung2 = new Kursanmeldung(schueler2, kurs1, new GregorianCalendar(2015, Calendar.AUGUST, 30), null, null);
            entityManager.persist(kursanmeldung2);
            Kursanmeldung kursanmeldung3 = new Kursanmeldung(schueler1, kurs2, new GregorianCalendar(2015, Calendar.AUGUST, 30), null, null);
            entityManager.persist(kursanmeldung3);

            entityManager.persist(kursanmeldung2);

            entityManager.flush();

            // Nach Kurseinteilungen von Schüler 1 suchen
            List<Kursanmeldung> kursanmeldungList1 = kursanmeldungDao.findKursanmeldungenSchueler(schueler1);
            assertEquals(2, kursanmeldungList1.size());
            assertEquals(Wochentag.DONNERSTAG, kursanmeldungList1.get(0).getKurs().getWochentag());
            assertEquals(Wochentag.FREITAG, kursanmeldungList1.get(1).getKurs().getWochentag());

            // Nach Märcheneinteilungen von Schüler 2 suchen
            List<Kursanmeldung> kursanmeldungList2 = kursanmeldungDao.findKursanmeldungenSchueler(schueler2);
            assertEquals(1, kursanmeldungList2.size());
            assertEquals(Wochentag.DONNERSTAG, kursanmeldungList2.get(0).getKurs().getWochentag());

            // Nach Märcheneinteilungen von Schüler 3 suchen
            List<Kursanmeldung> kursanmeldungList3 = kursanmeldungDao.findKursanmeldungenSchueler(schueler3);
            assertTrue(kursanmeldungList3.isEmpty());

        } finally {
            if (tx != null) {
                tx.rollback();
            }
        }

    }

}