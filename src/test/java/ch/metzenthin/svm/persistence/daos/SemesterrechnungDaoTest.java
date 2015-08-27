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
        entityManagerFactory = Persistence.createEntityManagerFactory("svm");
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

            // Märcheneinteilung
            Semesterrechnung semesterrechnung = new Semesterrechnung(semester, rechnungsempfaenger,
                    new BigDecimal("10.00"),
                    "1 Woche dispensiert",
                    new BigDecimal("20.00"),
                    "versäumte Zahlung",
                    Stipendium.STIPENDIUM_70,
                    false,
                    18,
                    new BigDecimal("22.00"),
                    new GregorianCalendar(1912, Calendar.FEBRUARY, 1),
                    new GregorianCalendar(1912, Calendar.FEBRUARY, 28),
                    new BigDecimal("100.00"),
                    new GregorianCalendar(1912, Calendar.MARCH, 10),
                    new BigDecimal("150.00"),
                    new GregorianCalendar(1912, Calendar.APRIL, 20),
                    new BigDecimal("146.00"),
                    new BigDecimal("0.00"),
                    "Zahlt in Raten");
            semesterrechnung.setSemesterrechnungCode(semesterrechnungCode);
            entityManager.persist(semesterrechnung);

            Semesterrechnung semesterrechnungFound = semesterrechnungDao.findById(new SemesterrechnungId(semesterrechnung.getSemester().getSemesterId(), semesterrechnung.getRechnungsempfaenger().getPersonId()));
            assertEquals(new BigDecimal("10.00"), semesterrechnungFound.getErmaessigung());
            assertEquals("1 Woche dispensiert", semesterrechnungFound.getErmaessigungsgrund());
            assertEquals(new BigDecimal("20.00"), semesterrechnungFound.getZuschlag());
            assertEquals("versäumte Zahlung", semesterrechnungFound.getZuschlagsgrund());
            assertEquals(Stipendium.STIPENDIUM_70, semesterrechnungFound.getStipendium());
            assertFalse(semesterrechnungFound.getGratiskind());
            assertEquals(new Integer(18), semesterrechnungFound.getAnzahlWochen());
            assertEquals(new BigDecimal("22.00"), semesterrechnungFound.getWochenbetrag());
            assertEquals(new GregorianCalendar(1912, Calendar.FEBRUARY, 1), semesterrechnungFound.getRechnungsdatum());
            assertEquals(new GregorianCalendar(1912, Calendar.FEBRUARY, 28), semesterrechnungFound.getDatumZahlung1());
            assertEquals(new BigDecimal("100.00"), semesterrechnungFound.getBetragZahlung1());
            assertEquals(new GregorianCalendar(1912, Calendar.MARCH, 10), semesterrechnungFound.getDatumZahlung2());
            assertEquals(new BigDecimal("150.00"), semesterrechnungFound.getBetragZahlung2());
            assertEquals(new GregorianCalendar(1912, Calendar.APRIL, 20), semesterrechnungFound.getDatumZahlung3());
            assertEquals(new BigDecimal("146.00"), semesterrechnungFound.getBetragZahlung3());
            assertEquals(new BigDecimal("0.00"), semesterrechnungFound.getRestbetrag());
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
                    new BigDecimal("10.00"),
                    "1 Woche dispensiert",
                    new BigDecimal("20.00"),
                    "versäumte Zahlung",
                    Stipendium.STIPENDIUM_70,
                    false,
                    18,
                    new BigDecimal("22.00"),
                    new GregorianCalendar(1912, Calendar.FEBRUARY, 1),
                    new GregorianCalendar(1912, Calendar.FEBRUARY, 28),
                    new BigDecimal("100.00"),
                    new GregorianCalendar(1912, Calendar.MARCH, 10),
                    new BigDecimal("150.00"),
                    new GregorianCalendar(1912, Calendar.APRIL, 20),
                    new BigDecimal("146.00"),
                    new BigDecimal("0.00"),
                    "Zahlt in Raten");
            semesterrechnung.setSemesterrechnungCode(semesterrechnungCode);
            semesterrechnungDao.save(semesterrechnung);

            Semesterrechnung semesterrechnungFound = semesterrechnungDao.findById(new SemesterrechnungId(semesterrechnung.getSemester().getSemesterId(), semesterrechnung.getRechnungsempfaenger().getPersonId()));
            assertEquals(new BigDecimal("10.00"), semesterrechnungFound.getErmaessigung());
            assertEquals("1 Woche dispensiert", semesterrechnungFound.getErmaessigungsgrund());
            assertEquals(new BigDecimal("20.00"), semesterrechnungFound.getZuschlag());
            assertEquals("versäumte Zahlung", semesterrechnungFound.getZuschlagsgrund());
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
                    new BigDecimal("10.00"),
                    "1 Woche dispensiert",
                    new BigDecimal("20.00"),
                    "versäumte Zahlung",
                    Stipendium.STIPENDIUM_70,
                    false,
                    18,
                    new BigDecimal("22.00"),
                    new GregorianCalendar(1912, Calendar.FEBRUARY, 1),
                    new GregorianCalendar(1912, Calendar.FEBRUARY, 28),
                    new BigDecimal("100.00"),
                    new GregorianCalendar(1912, Calendar.MARCH, 10),
                    new BigDecimal("150.00"),
                    new GregorianCalendar(1912, Calendar.APRIL, 20),
                    new BigDecimal("146.00"),
                    new BigDecimal("0.00"),
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

}