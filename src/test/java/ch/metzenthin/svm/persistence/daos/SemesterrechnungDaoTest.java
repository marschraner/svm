package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.common.dataTypes.Anrede;
import ch.metzenthin.svm.common.dataTypes.Geschlecht;
import ch.metzenthin.svm.common.dataTypes.Semesterbezeichnung;
import ch.metzenthin.svm.common.dataTypes.Stipendium;
import ch.metzenthin.svm.persistence.entities.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Martin Schraner
 */
public class SemesterrechnungDaoTest {

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private SemesterrechnungDao semesterrechnungDao;

    @Before
    public void setUp() throws Exception {
        entityManagerFactory = Persistence.createEntityManagerFactory("svmtest");
        entityManager = entityManagerFactory.createEntityManager();
        semesterrechnungDao = new SemesterrechnungDao(entityManager);

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

            // Semester, Rechnungsempfänger, SemesterrechnungCode erzeugen
            Semester semester = new Semester("1911/1912", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(1911, Calendar.AUGUST, 20), new GregorianCalendar(1912, Calendar.FEBRUARY, 10), 21);
            entityManager.persist(semester);
            Schueler schueler = new Schueler("Jana", "Rösle", new GregorianCalendar(2012, Calendar.JULY, 24), "044 491 69 33", null, null, Geschlecht.W, "Schwester von Valentin");
            Adresse adresse = new Adresse("Hohenklingenstrasse", "15", "8049", "Zürich");
            schueler.setAdresse(adresse);
            schueler.addAnmeldung(new Anmeldung(new GregorianCalendar(2015, Calendar.JANUARY, 1), null));
            Angehoeriger rechnungsempfaenger = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", "044 491 69 33", null, null);
            rechnungsempfaenger.setAdresse(adresse);
            schueler.setRechnungsempfaenger(rechnungsempfaenger);
            entityManager.persist(schueler);
            SemesterrechnungCode semesterrechnungCode = new SemesterrechnungCode("1t", "Stipendium Test", true);
            entityManager.persist(semesterrechnungCode);

            // Semesterrechnung
            Semesterrechnung semesterrechnung = new Semesterrechnung(semester, rechnungsempfaenger,
                    Stipendium.STIPENDIUM_70,
                    false,
                    new GregorianCalendar(1912, Calendar.FEBRUARY, 1),
                    new BigDecimal("10.00"),
                    "1 Woche dispensiert",
                    new BigDecimal("20.00"),
                    "versäumte Zahlung",
                    18,
                    new BigDecimal("23.00"),
                    new GregorianCalendar(1912, Calendar.MARCH, 1),
                    new BigDecimal("11.00"),
                    "1 Woche dispensiert",
                    new BigDecimal("21.00"),
                    "versäumte Zahlung",
                    18,
                    new BigDecimal("22.00"),
                    new GregorianCalendar(1912, Calendar.FEBRUARY, 28),
                    new BigDecimal("100.00"),
                    new GregorianCalendar(1912, Calendar.MARCH, 10),
                    new BigDecimal("150.00"),
                    new GregorianCalendar(1912, Calendar.APRIL, 20),
                    new BigDecimal("146.00"),
                    "Zahlt in Raten");
            semesterrechnung.setSemesterrechnungCode(semesterrechnungCode);
            entityManager.persist(semesterrechnung);

            Semesterrechnung semesterrechnungFound = semesterrechnungDao.findById(new SemesterrechnungId(semesterrechnung.getSemester().getSemesterId(), semesterrechnung.getRechnungsempfaenger().getPersonId()));
            assertEquals(new BigDecimal("10.00"), semesterrechnungFound.getErmaessigungVorrechnung());
            assertEquals("1 Woche dispensiert", semesterrechnungFound.getErmaessigungsgrundVorrechnung());
            assertEquals(new BigDecimal("20.00"), semesterrechnungFound.getZuschlagVorrechnung());
            assertEquals("versäumte Zahlung", semesterrechnungFound.getZuschlagsgrundVorrechnung());
            assertEquals(Stipendium.STIPENDIUM_70, semesterrechnungFound.getStipendium());
            assertFalse(semesterrechnungFound.getGratiskinder());
            assertEquals(new Integer(18), semesterrechnungFound.getAnzahlWochenVorrechnung());
            assertEquals(new BigDecimal("23.00"), semesterrechnungFound.getWochenbetragVorrechnung());
            assertEquals(new GregorianCalendar(1912, Calendar.FEBRUARY, 1), semesterrechnungFound.getRechnungsdatumVorrechnung());
            assertEquals(new GregorianCalendar(1912, Calendar.FEBRUARY, 28), semesterrechnungFound.getDatumZahlung1());
            assertEquals(new BigDecimal("100.00"), semesterrechnungFound.getBetragZahlung1());
            assertEquals(new GregorianCalendar(1912, Calendar.MARCH, 10), semesterrechnungFound.getDatumZahlung2());
            assertEquals(new BigDecimal("150.00"), semesterrechnungFound.getBetragZahlung2());
            assertEquals(new GregorianCalendar(1912, Calendar.APRIL, 20), semesterrechnungFound.getDatumZahlung3());
            assertEquals(new BigDecimal("146.00"), semesterrechnungFound.getBetragZahlung3());
            assertEquals("Zahlt in Raten", semesterrechnungFound.getBemerkungen());
        } finally {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    @Test
    public void testSave() {
        EntityTransaction tx = null;
        try {
            tx = entityManager.getTransaction();
            tx.begin();

            // Semester, Rechnungsempfänger, SemesterrechnungCode erzeugen
            Semester semester = new Semester("1911/1912", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(1911, Calendar.AUGUST, 20), new GregorianCalendar(1912, Calendar.FEBRUARY, 10), 21);
            entityManager.persist(semester);
            Schueler schueler = new Schueler("Jana", "Rösle", new GregorianCalendar(2012, Calendar.JULY, 24), "044 491 69 33", null, null, Geschlecht.W, "Schwester von Valentin");
            Adresse adresse = new Adresse("Hohenklingenstrasse", "15", "8049", "Zürich");
            schueler.setAdresse(adresse);
            schueler.addAnmeldung(new Anmeldung(new GregorianCalendar(2015, Calendar.JANUARY, 1), null));
            Angehoeriger rechnungsempfaenger = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", "044 491 69 33", null, null);
            rechnungsempfaenger.setAdresse(adresse);
            schueler.setRechnungsempfaenger(rechnungsempfaenger);
            entityManager.persist(schueler);
            SemesterrechnungCode semesterrechnungCode = new SemesterrechnungCode("1t", "Stipendium Test", true);
            entityManager.persist(semesterrechnungCode);

            Semesterrechnung semesterrechnung = new Semesterrechnung(semester, rechnungsempfaenger,
                    Stipendium.STIPENDIUM_70,
                    false,
                    new GregorianCalendar(1912, Calendar.FEBRUARY, 1),
                    new BigDecimal("10.00"),
                    "1 Woche dispensiert",
                    new BigDecimal("20.00"),
                    "versäumte Zahlung",
                    18,
                    new BigDecimal("23.00"),
                    new GregorianCalendar(1912, Calendar.MARCH, 1),
                    new BigDecimal("11.00"),
                    "1 Woche dispensiert",
                    new BigDecimal("21.00"),
                    "versäumte Zahlung",
                    18,
                    new BigDecimal("22.00"),
                    new GregorianCalendar(1912, Calendar.FEBRUARY, 28),
                    new BigDecimal("100.00"),
                    new GregorianCalendar(1912, Calendar.MARCH, 10),
                    new BigDecimal("150.00"),
                    new GregorianCalendar(1912, Calendar.APRIL, 20),
                    new BigDecimal("146.00"),
                    "Zahlt in Raten");
            semesterrechnung.setSemesterrechnungCode(semesterrechnungCode);
            semesterrechnungDao.save(semesterrechnung);

            Semesterrechnung semesterrechnungFound = semesterrechnungDao.findById(new SemesterrechnungId(semesterrechnung.getSemester().getSemesterId(), semesterrechnung.getRechnungsempfaenger().getPersonId()));
            assertEquals(new BigDecimal("10.00"), semesterrechnungFound.getErmaessigungVorrechnung());
            assertEquals("1 Woche dispensiert", semesterrechnungFound.getErmaessigungsgrundVorrechnung());
            assertEquals(new BigDecimal("20.00"), semesterrechnungFound.getZuschlagVorrechnung());
            assertEquals("versäumte Zahlung", semesterrechnungFound.getZuschlagsgrundVorrechnung());
            assertEquals(1, semester.getSemesterrechnungen().size());
            assertEquals(1, rechnungsempfaenger.getSemesterrechnungen().size());

        } finally {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

    @Test
    public void testRemove() {
        EntityTransaction tx = null;

        try {
            tx = entityManager.getTransaction();
            tx.begin();

            // Semester, Rechnungsempfänger, SemesterrechnungCode erzeugen
            Semester semester = new Semester("1911/1912", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(1911, Calendar.AUGUST, 20), new GregorianCalendar(1912, Calendar.FEBRUARY, 10), 21);
            entityManager.persist(semester);
            Schueler schueler = new Schueler("Jana", "Rösle", new GregorianCalendar(2012, Calendar.JULY, 24), "044 491 69 33", null, null, Geschlecht.W, "Schwester von Valentin");
            Adresse adresse = new Adresse("Hohenklingenstrasse", "15", "8049", "Zürich");
            schueler.setAdresse(adresse);
            schueler.addAnmeldung(new Anmeldung(new GregorianCalendar(2015, Calendar.JANUARY, 1), null));
            Angehoeriger rechnungsempfaenger = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", "044 491 69 33", null, null);
            rechnungsempfaenger.setAdresse(adresse);
            schueler.setRechnungsempfaenger(rechnungsempfaenger);
            entityManager.persist(schueler);
            SemesterrechnungCode semesterrechnungCode = new SemesterrechnungCode("1t", "Stipendium Test", true);
            entityManager.persist(semesterrechnungCode);

            Semesterrechnung semesterrechnung = new Semesterrechnung(semester, rechnungsempfaenger,
                    Stipendium.STIPENDIUM_70,
                    false,
                    new GregorianCalendar(1912, Calendar.FEBRUARY, 1),
                    new BigDecimal("10.00"),
                    "1 Woche dispensiert",
                    new BigDecimal("20.00"),
                    "versäumte Zahlung",
                    18,
                    new BigDecimal("23.00"),
                    new GregorianCalendar(1912, Calendar.MARCH, 1),
                    new BigDecimal("11.00"),
                    "1 Woche dispensiert",
                    new BigDecimal("21.00"),
                    "versäumte Zahlung",
                    18,
                    new BigDecimal("22.00"),
                    new GregorianCalendar(1912, Calendar.FEBRUARY, 28),
                    new BigDecimal("100.00"),
                    new GregorianCalendar(1912, Calendar.MARCH, 10),
                    new BigDecimal("150.00"),
                    new GregorianCalendar(1912, Calendar.APRIL, 20),
                    new BigDecimal("146.00"),
                    "Zahlt in Raten");
            semesterrechnung.setSemesterrechnungCode(semesterrechnungCode);
            Semesterrechnung semesterrechnungSaved = semesterrechnungDao.save(semesterrechnung);

            int semesterId = semesterrechnungSaved.getSemester().getSemesterId();
            int personId = semesterrechnungSaved.getRechnungsempfaenger().getPersonId();

            entityManager.flush();
            assertNotNull(semesterrechnungDao.findById(new SemesterrechnungId(semesterId, personId)));
            assertEquals(1, semester.getSemesterrechnungen().size());
            assertEquals(1, rechnungsempfaenger.getSemesterrechnungen().size());

            // Delete Semesterrechnung
            semesterrechnungDao.remove(semesterrechnungSaved);
            entityManager.flush();
            assertNull(semesterrechnungDao.findById(new SemesterrechnungId(semesterId, personId)));
            assertEquals(0, semester.getSemesterrechnungen().size());
            assertEquals(0, rechnungsempfaenger.getSemesterrechnungen().size());

        } finally {
            if (tx != null) {
                tx.rollback();
            }
        }

    }

    @Test
    public void testFindSemesterrechnungenRechnungsempfaenger() {
        EntityTransaction tx = null;

        try {
            tx = entityManager.getTransaction();
            tx.begin();

            // Semester, Rechnungsempfänger, SemesterrechnungCode erzeugen
            Semester semester1 = new Semester("1911/1912", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(1911, Calendar.AUGUST, 20), new GregorianCalendar(1912, Calendar.FEBRUARY, 10), 21);
            entityManager.persist(semester1);
            Semester semester2 = new Semester("1911/1912", Semesterbezeichnung.ZWEITES_SEMESTER, new GregorianCalendar(1912, Calendar.FEBRUARY, 20), new GregorianCalendar(1912, Calendar.JULY, 10), 21);
            entityManager.persist(semester2);
            Semester semester3 = new Semester("1912/1913", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(1912, Calendar.AUGUST, 20), new GregorianCalendar(1913, Calendar.FEBRUARY, 10), 21);
            entityManager.persist(semester1);

            Schueler schueler1 = new Schueler("Jana", "Rösle", new GregorianCalendar(2012, Calendar.JULY, 24), "044 491 69 33", null, null, Geschlecht.W, "Schwester von Valentin");
            Adresse adresse = new Adresse("Hohenklingenstrasse", "15", "8049", "Zürich");
            schueler1.setAdresse(adresse);
            schueler1.addAnmeldung(new Anmeldung(new GregorianCalendar(2015, Calendar.JANUARY, 1), null));
            Angehoeriger rechnungsempfaenger1 = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", "044 491 69 33", null, null);
            rechnungsempfaenger1.setAdresse(adresse);
            schueler1.setRechnungsempfaenger(rechnungsempfaenger1);
            entityManager.persist(schueler1);

            Schueler schueler2 = new Schueler("Hanna", "Hasler", new GregorianCalendar(2010, Calendar.JULY, 24), "044 422 69 33", null, null, Geschlecht.W, null);
            Adresse adresse2 = new Adresse("Hohenklingenstrasse", "22", "8049", "Zürich");
            schueler2.setAdresse(adresse2);
            schueler2.addAnmeldung(new Anmeldung(new GregorianCalendar(2013, Calendar.JANUARY, 1), null));
            Angehoeriger rechnungsempfaenger2 = new Angehoeriger(Anrede.FRAU, "Adriana", "Hasler", "044 422 69 33", null, null);
            rechnungsempfaenger2.setAdresse(adresse2);
            schueler2.setRechnungsempfaenger(rechnungsempfaenger2);
            entityManager.persist(schueler2);

            SemesterrechnungCode semesterrechnungCode1 = new SemesterrechnungCode("1t", "Stipendium Test2", true);
            entityManager.persist(semesterrechnungCode1);
            SemesterrechnungCode semesterrechnungCode2 = new SemesterrechnungCode("2t", "Stipendium Test2", true);
            entityManager.persist(semesterrechnungCode2);

            // Märcheneinteilung
            Semesterrechnung semesterrechnung1 = new Semesterrechnung(semester1, rechnungsempfaenger1,
                    Stipendium.STIPENDIUM_60,
                    true,
                    new GregorianCalendar(1912, Calendar.FEBRUARY, 5),
                    new BigDecimal("17.00"),
                    "2 Wochen dispensiert",
                    new BigDecimal("23.00"),
                    "versäumte Zahlungen",
                    17,
                    new BigDecimal("22.00"),
                    new GregorianCalendar(1912, Calendar.MARCH, 5),
                    new BigDecimal("17.00"),
                    "2 Wochen dispensiert",
                    new BigDecimal("23.00"),
                    "versäumte Zahlungen",
                    17,
                    new BigDecimal("22.00"),
                    new GregorianCalendar(1912, Calendar.FEBRUARY, 23),
                    new BigDecimal("100.00"),
                    new GregorianCalendar(1912, Calendar.MARCH, 12),
                    new BigDecimal("150.00"),
                    new GregorianCalendar(1912, Calendar.APRIL, 21),
                    new BigDecimal("146.00"),
                    "Zahlt immer in Raten");
            semesterrechnung1.setSemesterrechnungCode(semesterrechnungCode1);
            entityManager.persist(semesterrechnung1);
            Semesterrechnung semesterrechnung2 = new Semesterrechnung(semester1, rechnungsempfaenger2,
                    Stipendium.STIPENDIUM_60,
                    true,
                    new GregorianCalendar(1912, Calendar.FEBRUARY, 5),
                    new BigDecimal("17.00"),
                    "3 Wochen dispensiert",
                    new BigDecimal("23.00"),
                    "versäumte Zahlungen",
                    17,
                    new BigDecimal("22.00"),
                    new GregorianCalendar(1912, Calendar.FEBRUARY, 5),
                    new BigDecimal("17.00"),
                    "3 Wochen dispensiert",
                    new BigDecimal("23.00"),
                    "versäumte Zahlungen",
                    17,
                    new BigDecimal("22.00"),
                    new GregorianCalendar(1912, Calendar.FEBRUARY, 23),
                    new BigDecimal("100.00"),
                    new GregorianCalendar(1912, Calendar.MARCH, 12),
                    new BigDecimal("150.00"),
                    new GregorianCalendar(1912, Calendar.APRIL, 21),
                    new BigDecimal("146.00"),
                    "Zahlt immer in Raten");
            semesterrechnung2.setSemesterrechnungCode(semesterrechnungCode1);
            entityManager.persist(semesterrechnung2);
            Semesterrechnung semesterrechnung3 = new Semesterrechnung(semester2, rechnungsempfaenger1,
                    Stipendium.STIPENDIUM_70,
                    false,
                    new GregorianCalendar(1912, Calendar.FEBRUARY, 1),
                    new BigDecimal("20.00"),
                    "1 Woche dispensiert",
                    new BigDecimal("30.00"),
                    "versäumte Zahlung",
                    18,
                    new BigDecimal("32.00"),
                    new GregorianCalendar(1912, Calendar.FEBRUARY, 1),
                    new BigDecimal("20.00"),
                    "1 Woche dispensiert",
                    new BigDecimal("30.00"),
                    "versäumte Zahlung",
                    18,
                    new BigDecimal("32.00"),
                    new GregorianCalendar(1912, Calendar.FEBRUARY, 28),
                    new BigDecimal("200.00"),
                    new GregorianCalendar(1912, Calendar.MARCH, 10),
                    new BigDecimal("160.00"),
                    new GregorianCalendar(1912, Calendar.APRIL, 20),
                    new BigDecimal("156.00"),
                    "Zahlt in Raten");
            semesterrechnung3.setSemesterrechnungCode(semesterrechnungCode2);
            entityManager.persist(semesterrechnung3);

            entityManager.flush();

            // Nach Semesterrechnungen Semester 1 suchen
            List<Semesterrechnung> semesterrechnungList1 = semesterrechnungDao.findSemesterrechnungenSemester(semester1);
            assertEquals(2, semesterrechnungList1.size());
            assertEquals("3 Wochen dispensiert", semesterrechnungList1.get(0).getErmaessigungsgrundVorrechnung());
            assertEquals("2 Wochen dispensiert", semesterrechnungList1.get(1).getErmaessigungsgrundVorrechnung());

            // Nach Semesterrechnungen Semester 2 suchen
            List<Semesterrechnung> semesterrechnungList2 = semesterrechnungDao.findSemesterrechnungenSemester(semester2);
            assertEquals(1, semesterrechnungList2.size());
            assertEquals("1 Woche dispensiert", semesterrechnungList2.get(0).getErmaessigungsgrundVorrechnung());

            // Nach Semesterrechnungen Semester 3 suchen
            List<Semesterrechnung> semesterrechnungList3 = semesterrechnungDao.findSemesterrechnungenSemester(semester3);
            assertTrue(semesterrechnungList3.isEmpty());


        } finally {
            if (tx != null) {
                tx.rollback();
            }
        }
    }

}