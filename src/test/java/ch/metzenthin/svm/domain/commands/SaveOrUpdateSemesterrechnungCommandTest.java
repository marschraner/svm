package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Anrede;
import ch.metzenthin.svm.common.dataTypes.Geschlecht;
import ch.metzenthin.svm.common.dataTypes.Semesterbezeichnung;
import ch.metzenthin.svm.common.dataTypes.Stipendium;
import ch.metzenthin.svm.persistence.daos.SchuelerDao;
import ch.metzenthin.svm.persistence.daos.SemesterDao;
import ch.metzenthin.svm.persistence.daos.SemesterrechnungCodeDao;
import ch.metzenthin.svm.persistence.daos.SemesterrechnungDao;
import ch.metzenthin.svm.persistence.entities.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Martin Schraner
 */
public class SaveOrUpdateSemesterrechnungCommandTest {

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
        List<SemesterrechnungCode> erfassteSemesterrechnungCodes = new ArrayList<>();
        List<Semesterrechnung> erfassteSemesterrechnungen = new ArrayList<>();

        // Semester, Rechnungsempfänger, SemesterrechnungCode erzeugen
        Semester semester1 = new Semester("1911/1912", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(1911, Calendar.AUGUST, 20), new GregorianCalendar(1912, Calendar.FEBRUARY, 10), 21);
        SaveOrUpdateSemesterCommand saveOrUpdateSemesterCommand = new SaveOrUpdateSemesterCommand(semester1, null, erfassteSemester);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateSemesterCommand);
        Semester semester2 = new Semester("1911/1912", Semesterbezeichnung.ZWEITES_SEMESTER, new GregorianCalendar(1912, Calendar.FEBRUARY, 20), new GregorianCalendar(1912, Calendar.JULY, 10), 21);
        saveOrUpdateSemesterCommand = new SaveOrUpdateSemesterCommand(semester2, null, erfassteSemester);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateSemesterCommand);

        Schueler schueler1 = new Schueler("Jana", "Rösle", new GregorianCalendar(2012, Calendar.JULY, 24), "044 491 69 33", null, null, Geschlecht.W, "Schwester von Valentin");
        Adresse adresse1 = new Adresse("Hohenklingenstrasse", "15", "8049", "Zürich");
        schueler1.setAdresse(adresse1);
        schueler1.addAnmeldung(new Anmeldung(new GregorianCalendar(2015, Calendar.JANUARY, 1), null));
        Angehoeriger rechnungsempfaenger1 = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", "044 491 69 33", null, null);
        rechnungsempfaenger1.setAdresse(adresse1);
        schueler1.setRechnungsempfaenger(rechnungsempfaenger1);
        SaveSchuelerCommand saveSchuelerCommand = new SaveSchuelerCommand(schueler1);
        commandInvoker.executeCommandAsTransaction(saveSchuelerCommand);

        Schueler schueler2 = new Schueler("Hanna", "Hasler", new GregorianCalendar(2010, Calendar.JULY, 24), "044 422 69 33", null, null, Geschlecht.W, null);
        Adresse adresse2 = new Adresse("Hohenklingenstrasse", "22", "8049", "Zürich");
        schueler2.setAdresse(adresse2);
        schueler2.addAnmeldung(new Anmeldung(new GregorianCalendar(2013, Calendar.JANUARY, 1), null));
        Angehoeriger rechnungsempfaenger2 = new Angehoeriger(Anrede.FRAU, "Adriana", "Hasler", "044 422 69 33", null, null);
        rechnungsempfaenger2.setAdresse(adresse2);
        schueler2.setRechnungsempfaenger(rechnungsempfaenger2);
        saveSchuelerCommand = new SaveSchuelerCommand(schueler2);
        commandInvoker.executeCommandAsTransaction(saveSchuelerCommand);

        SemesterrechnungCode semesterrechnungCode1 = new SemesterrechnungCode("1t", "Stipendium Test2", true);
        SaveOrUpdateSemesterrechnungCodeCommand saveOrUpdateSemesterrechnungCodeCommand = new SaveOrUpdateSemesterrechnungCodeCommand(semesterrechnungCode1, null, erfassteSemesterrechnungCodes);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateSemesterrechnungCodeCommand);
        SemesterrechnungCode semesterrechnungCode2 = new SemesterrechnungCode("2t", "Stipendium Test2", true);
        saveOrUpdateSemesterrechnungCodeCommand = new SaveOrUpdateSemesterrechnungCodeCommand(semesterrechnungCode2, null, erfassteSemesterrechnungCodes);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateSemesterrechnungCodeCommand);

        assertFalse(checkIfSemesterrechnungAvailable(semester1, rechnungsempfaenger1,
                Stipendium.STIPENDIUM_60,
                true,
                new GregorianCalendar(1912, Calendar.FEBRUARY, 5),
                new BigDecimal("17.00"),
                "2 Wochen dispensiert",
                new BigDecimal("23.00"),
                "versäumte Zahlungen",
                17,
                new BigDecimal("22.00"),
                new GregorianCalendar(1912, Calendar.APRIL, 2),
                new BigDecimal("25.00"),
                "2.5 Wochen dispensiert",
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
                "Zahlt immer in Raten",
                semesterrechnungCode1));
        assertFalse(checkIfSemesterrechnungAvailable(semester2, rechnungsempfaenger2,
                Stipendium.STIPENDIUM_70,
                false,
                new GregorianCalendar(1912, Calendar.FEBRUARY, 1),
                new BigDecimal("20.00"),
                "1 Woche dispensiert",
                new BigDecimal("30.00"),
                "versäumte Zahlung",
                18,
                new BigDecimal("32.00"),
                new GregorianCalendar(1912, Calendar.MAY, 1),
                new BigDecimal("23.00"),
                "1.5 Wochen dispensiert",
                new BigDecimal("31.00"),
                "versäumte Zahlung",
                18,
                new BigDecimal("32.00"),
                new GregorianCalendar(1912, Calendar.FEBRUARY, 28),
                new BigDecimal("200.00"),
                new GregorianCalendar(1912, Calendar.MARCH, 10),
                new BigDecimal("160.00"),
                new GregorianCalendar(1912, Calendar.APRIL, 20),
                new BigDecimal("156.00"),
                "Zahlt in Raten",
                semesterrechnungCode2));

        // 1. Semesterrechnung erfassen
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
                new GregorianCalendar(1912, Calendar.APRIL, 2),
                new BigDecimal("25.00"),
                "2.5 Wochen dispensiert",
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
        SaveOrUpdateSemesterrechnungCommand saveOrUpdateSemesterrechnungCommand = new SaveOrUpdateSemesterrechnungCommand(semesterrechnung1, semesterrechnungCode1, null, erfassteSemesterrechnungen);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateSemesterrechnungCommand);

        assertTrue(checkIfSemesterrechnungAvailable(semester1, rechnungsempfaenger1,
                Stipendium.STIPENDIUM_60,
                true,
                new GregorianCalendar(1912, Calendar.FEBRUARY, 5),
                new BigDecimal("17.00"),
                "2 Wochen dispensiert",
                new BigDecimal("23.00"),
                "versäumte Zahlungen",
                17,
                new BigDecimal("22.00"),
                new GregorianCalendar(1912, Calendar.APRIL, 2),
                new BigDecimal("25.00"),
                "2.5 Wochen dispensiert",
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
                "Zahlt immer in Raten",
                semesterrechnungCode1));

        // 2. Semesterrechnung erfassen
        Semesterrechnung semesterrechnung2 = new Semesterrechnung(semester2, rechnungsempfaenger2,
                Stipendium.STIPENDIUM_70,
                false,
                new GregorianCalendar(1912, Calendar.FEBRUARY, 1),
                new BigDecimal("20.00"),
                "1 Woche dispensiert",
                new BigDecimal("30.00"),
                "versäumte Zahlung",
                18,
                new BigDecimal("32.00"),
                new GregorianCalendar(1912, Calendar.MAY, 1),
                new BigDecimal("23.00"),
                "1.5 Wochen dispensiert",
                new BigDecimal("31.00"),
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
        saveOrUpdateSemesterrechnungCommand = new SaveOrUpdateSemesterrechnungCommand(semesterrechnung2, semesterrechnungCode2, null, erfassteSemesterrechnungen);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateSemesterrechnungCommand);

        assertTrue(checkIfSemesterrechnungAvailable(semester2, rechnungsempfaenger2,
                Stipendium.STIPENDIUM_70,
                false,
                new GregorianCalendar(1912, Calendar.FEBRUARY, 1),
                new BigDecimal("20.00"),
                "1 Woche dispensiert",
                new BigDecimal("30.00"),
                "versäumte Zahlung",
                18,
                new BigDecimal("32.00"),
                new GregorianCalendar(1912, Calendar.MAY, 1),
                new BigDecimal("23.00"),
                "1.5 Wochen dispensiert",
                new BigDecimal("31.00"),
                "versäumte Zahlung",
                18,
                new BigDecimal("32.00"),
                new GregorianCalendar(1912, Calendar.FEBRUARY, 28),
                new BigDecimal("200.00"),
                new GregorianCalendar(1912, Calendar.MARCH, 10),
                new BigDecimal("160.00"),
                new GregorianCalendar(1912, Calendar.APRIL, 20),
                new BigDecimal("156.00"),
                "Zahlt in Raten",
                semesterrechnungCode2));

        // 2. Semesterrechnung bearbeiten
        Semesterrechnung semesterrechnung2Modif = new Semesterrechnung(semester2, rechnungsempfaenger2,
                Stipendium.STIPENDIUM_70,
                false,
                new GregorianCalendar(1912, Calendar.FEBRUARY, 1),
                new BigDecimal("26.00"),
                "1 Woche dispensiert",
                new BigDecimal("32.00"),
                "versäumte Zahlung",
                18,
                new BigDecimal("34.00"),
                new GregorianCalendar(1912, Calendar.MAY, 1),
                new BigDecimal("23.00"),
                "1.5 Wochen dispensiert",
                new BigDecimal("31.00"),
                "versäumte Zahlung",
                18,
                new BigDecimal("34.00"),
                new GregorianCalendar(1912, Calendar.FEBRUARY, 28),
                new BigDecimal("201.00"),
                new GregorianCalendar(1912, Calendar.MARCH, 10),
                new BigDecimal("161.00"),
                new GregorianCalendar(1912, Calendar.APRIL, 20),
                new BigDecimal("157.00"),
                "Zahlt in Raten modif");
        saveOrUpdateSemesterrechnungCommand = new SaveOrUpdateSemesterrechnungCommand(semesterrechnung2Modif, semesterrechnungCode1, semesterrechnung2, erfassteSemesterrechnungen);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateSemesterrechnungCommand);

        assertFalse(checkIfSemesterrechnungAvailable(semester2, rechnungsempfaenger2,
                Stipendium.STIPENDIUM_70,
                false,
                new GregorianCalendar(1912, Calendar.FEBRUARY, 1),
                new BigDecimal("20.00"),
                "1 Woche dispensiert",
                new BigDecimal("30.00"),
                "versäumte Zahlung",
                18,
                new BigDecimal("32.00"),
                new GregorianCalendar(1912, Calendar.MAY, 1),
                new BigDecimal("23.00"),
                "1.5 Wochen dispensiert",
                new BigDecimal("31.00"),
                "versäumte Zahlung",
                18,
                new BigDecimal("32.00"),
                new GregorianCalendar(1912, Calendar.FEBRUARY, 28),
                new BigDecimal("200.00"),
                new GregorianCalendar(1912, Calendar.MARCH, 10),
                new BigDecimal("160.00"),
                new GregorianCalendar(1912, Calendar.APRIL, 20),
                new BigDecimal("156.00"),
                "Zahlt in Raten",
                semesterrechnungCode1));
        assertTrue(checkIfSemesterrechnungAvailable(semester2, rechnungsempfaenger2,
                Stipendium.STIPENDIUM_70,
                false,
                new GregorianCalendar(1912, Calendar.FEBRUARY, 1),
                new BigDecimal("26.00"),
                "1 Woche dispensiert",
                new BigDecimal("32.00"),
                "versäumte Zahlung",
                18,
                new BigDecimal("34.00"),
                new GregorianCalendar(1912, Calendar.MAY, 1),
                new BigDecimal("23.00"),
                "1.5 Wochen dispensiert",
                new BigDecimal("31.00"),
                "versäumte Zahlung",
                18,
                new BigDecimal("34.00"),
                new GregorianCalendar(1912, Calendar.FEBRUARY, 28),
                new BigDecimal("201.00"),
                new GregorianCalendar(1912, Calendar.MARCH, 10),
                new BigDecimal("161.00"),
                new GregorianCalendar(1912, Calendar.APRIL, 20),
                new BigDecimal("157.00"),
                "Zahlt in Raten modif",
                semesterrechnungCode1));

        // Testdaten löschen
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            SemesterrechnungDao semesterrechnungDao = new SemesterrechnungDao(entityManager);
            for (Semesterrechnung semesterrechnung : erfassteSemesterrechnungen) {
                Semesterrechnung semesterrechnungToBeDeleted = semesterrechnungDao.findById(new SemesterrechnungId(semesterrechnung.getSemester().getSemesterId(), semesterrechnung.getRechnungsempfaenger().getPersonId()));
                if (semesterrechnungToBeDeleted != null) {
                    semesterrechnungDao.remove(semesterrechnungToBeDeleted);
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
            SemesterrechnungCodeDao semesterrechnungCodeDao = new SemesterrechnungCodeDao(entityManager);
            for (SemesterrechnungCode semesterrechnungCode : erfassteSemesterrechnungCodes) {
                SemesterrechnungCode semesterrechnungCodeToBeDeleted = semesterrechnungCodeDao.findById(semesterrechnungCode.getCodeId());
                if (semesterrechnungCodeToBeDeleted != null) {
                    semesterrechnungCodeDao.remove(semesterrechnungCodeToBeDeleted);
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

    private boolean checkIfSemesterrechnungAvailable(Semester semester, Angehoeriger rechnungsempfaenger, Stipendium stipendium, Boolean gratiskinder, Calendar rechnungsdatumVorrechnung, BigDecimal ermaessigungVorrechnung, String ermaessigungsgrundVorrechnung, BigDecimal zuschlagVorrechnung, String zuschlagsgrundVorrechnung, Integer anzahlWochenVorrechnung, BigDecimal wochenbetragVorrechnung, Calendar rechnungsdatumNachrechnung, BigDecimal ermaessigungNachrechnung, String ermaessigungsgrundNachrechnung, BigDecimal zuschlagNachrechnung, String zuschlagsgrundNachrechnung, Integer anzahlWochenNachrechnung, BigDecimal wochenbetragNachrechnung, Calendar datumZahlung1, BigDecimal betragZahlung1, Calendar datumZahlung2, BigDecimal betragZahlung2, Calendar datumZahlung3, BigDecimal betragZahlung3, String bemerkungen, SemesterrechnungCode semesterrechnungCode) {
        FindSemesterrechnungenSemesterCommand findSemesterrechnungenSemesterCommand = new FindSemesterrechnungenSemesterCommand(semester);
        commandInvoker.executeCommandAsTransaction(findSemesterrechnungenSemesterCommand);
        List<Semesterrechnung> semesterrechnungenRechnungsempfaenger = findSemesterrechnungenSemesterCommand.getSemesterrechnungenFound();
        for (Semesterrechnung semesterrechnung : semesterrechnungenRechnungsempfaenger) {
            if (semesterrechnung.getRechnungsempfaenger().equals(rechnungsempfaenger)
                    && semesterrechnung.getStipendium().equals(stipendium)
                    && semesterrechnung.getGratiskinder().equals(gratiskinder)
                    && semesterrechnung.getRechnungsdatumVorrechnung().equals(rechnungsdatumVorrechnung)
                    && semesterrechnung.getErmaessigungVorrechnung().compareTo(ermaessigungVorrechnung) == 0
                    && semesterrechnung.getErmaessigungsgrundVorrechnung().equals(ermaessigungsgrundVorrechnung)
                    && semesterrechnung.getZuschlagVorrechnung().compareTo(zuschlagVorrechnung) == 0
                    && semesterrechnung.getZuschlagsgrundVorrechnung().equals(zuschlagsgrundVorrechnung)
                    && semesterrechnung.getAnzahlWochenVorrechnung().equals(anzahlWochenVorrechnung)
                    && semesterrechnung.getWochenbetragVorrechnung().compareTo(wochenbetragVorrechnung) == 0
                    && semesterrechnung.getRechnungsdatumNachrechnung().equals(rechnungsdatumNachrechnung)
                    && semesterrechnung.getErmaessigungNachrechnung().compareTo(ermaessigungNachrechnung) == 0
                    && semesterrechnung.getErmaessigungsgrundNachrechnung().equals(ermaessigungsgrundNachrechnung)
                    && semesterrechnung.getZuschlagNachrechnung().compareTo(zuschlagNachrechnung) == 0
                    && semesterrechnung.getZuschlagsgrundNachrechnung().equals(zuschlagsgrundNachrechnung)
                    && semesterrechnung.getAnzahlWochenNachrechnung().equals(anzahlWochenNachrechnung)
                    && semesterrechnung.getWochenbetragNachrechnung().compareTo(wochenbetragNachrechnung) == 0
                    && semesterrechnung.getDatumZahlung1().equals(datumZahlung1)
                    && semesterrechnung.getBetragZahlung1().compareTo(betragZahlung1) == 0
                    && semesterrechnung.getDatumZahlung2().equals(datumZahlung2)
                    && semesterrechnung.getBetragZahlung2().compareTo(betragZahlung2) == 0
                    && semesterrechnung.getDatumZahlung3().equals(datumZahlung3)
                    && semesterrechnung.getBetragZahlung3().compareTo(betragZahlung3) == 0
                    && semesterrechnung.getBemerkungen().equals(bemerkungen)
                    && semesterrechnung.getSemesterrechnungCode().isIdenticalWith(semesterrechnungCode)) {
                return true;
            }
        }
        return false;
    }

}