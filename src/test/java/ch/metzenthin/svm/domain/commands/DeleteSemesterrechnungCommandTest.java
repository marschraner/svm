package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.datatypes.Anrede;
import ch.metzenthin.svm.common.datatypes.Geschlecht;
import ch.metzenthin.svm.common.datatypes.Semesterbezeichnung;
import ch.metzenthin.svm.common.datatypes.Stipendium;
import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.daos.SchuelerDao;
import ch.metzenthin.svm.persistence.daos.SemesterDao;
import ch.metzenthin.svm.persistence.daos.SemesterrechnungCodeDao;
import ch.metzenthin.svm.persistence.daos.SemesterrechnungDao;
import ch.metzenthin.svm.persistence.entities.*;
import jakarta.persistence.EntityManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;
import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Martin Schraner
 */
public class DeleteSemesterrechnungCommandTest {

    private final SemesterrechnungDao semesterrechnungDao = new SemesterrechnungDao();
    private final SemesterDao semesterDao = new SemesterDao();
    private final SemesterrechnungCodeDao semesterrechnungCodeDao = new SemesterrechnungCodeDao();
    private final SchuelerDao schuelerDao = new SchuelerDao();

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
        List<SemesterrechnungCode> erfassteSemesterrechnungCodes = new ArrayList<>();
        List<Semesterrechnung> erfassteSemesterrechnungen = new ArrayList<>();

        // Semester, Rechnungsempfänger, SemesterrechnungCode erzeugen
        Semester semester1 = new Semester("1911/1912", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(1911, Calendar.AUGUST, 20), new GregorianCalendar(1912, Calendar.FEBRUARY, 10), new GregorianCalendar(1911, Calendar.OCTOBER, 5), new GregorianCalendar(1911, Calendar.OCTOBER, 17), new GregorianCalendar(1911, Calendar.DECEMBER, 21), new GregorianCalendar(1912, Calendar.JANUARY, 2));
        SaveOrUpdateSemesterCommand saveOrUpdateSemesterCommand = new SaveOrUpdateSemesterCommand(semester1, null, erfassteSemester);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateSemesterCommand);
        Semester semester2 = new Semester("1911/1912", Semesterbezeichnung.ZWEITES_SEMESTER, new GregorianCalendar(1912, Calendar.FEBRUARY, 20), new GregorianCalendar(1912, Calendar.JULY, 10), new GregorianCalendar(1912, Calendar.APRIL, 25), new GregorianCalendar(1912, Calendar.MAY, 7), null, null);
        saveOrUpdateSemesterCommand = new SaveOrUpdateSemesterCommand(semester2, null, erfassteSemester);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateSemesterCommand);

        Schueler schueler1 = new Schueler("Jana", "Rösle", new GregorianCalendar(2012, Calendar.JULY, 24), "044 491 69 33", null, null, Geschlecht.W, "Schwester von Valentin");
        Adresse adresse1 = new Adresse("Hohenklingenstrasse", "15", "8049", "Zürich");
        schueler1.setAdresse(adresse1);
        schueler1.addAnmeldung(new Anmeldung(new GregorianCalendar(2015, Calendar.JANUARY, 1), null));
        Angehoeriger rechnungsempfaenger1 = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", "044 491 69 33", null, null, false);
        rechnungsempfaenger1.setAdresse(adresse1);
        schueler1.setRechnungsempfaenger(rechnungsempfaenger1);
        SaveSchuelerCommand saveSchuelerCommand = new SaveSchuelerCommand(schueler1);
        commandInvoker.executeCommandAsTransaction(saveSchuelerCommand);

        Schueler schueler2 = new Schueler("Hanna", "Hasler", new GregorianCalendar(2010, Calendar.JULY, 24), "044 422 69 33", null, null, Geschlecht.W, null);
        Adresse adresse2 = new Adresse("Hohenklingenstrasse", "22", "8049", "Zürich");
        schueler2.setAdresse(adresse2);
        schueler2.addAnmeldung(new Anmeldung(new GregorianCalendar(2013, Calendar.JANUARY, 1), null));
        Angehoeriger rechnungsempfaenger2 = new Angehoeriger(Anrede.FRAU, "Adriana", "Hasler", "044 422 69 33", null, null, true);
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
                new GregorianCalendar(1912, Calendar.FEBRUARY, 24),
                new BigDecimal("101.00"),
                new GregorianCalendar(1912, Calendar.MARCH, 13),
                new BigDecimal("151.00"),
                new GregorianCalendar(1912, Calendar.APRIL, 22),
                new BigDecimal("147.00"),
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
                semesterrechnungCode1,
                false));
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
                new GregorianCalendar(1912, Calendar.FEBRUARY, 29),
                new BigDecimal("201.00"),
                new GregorianCalendar(1912, Calendar.MARCH, 11),
                new BigDecimal("161.00"),
                new GregorianCalendar(1912, Calendar.APRIL, 21),
                new BigDecimal("157.00"),
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
                semesterrechnungCode2,
                true));

        // 2 Semesterrechnungen erfassen
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
                new GregorianCalendar(1912, Calendar.FEBRUARY, 24),
                new BigDecimal("101.00"),
                new GregorianCalendar(1912, Calendar.MARCH, 13),
                new BigDecimal("151.00"),
                new GregorianCalendar(1912, Calendar.APRIL, 22),
                new BigDecimal("147.00"),
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
                false);
        SaveOrUpdateSemesterrechnungCommand saveOrUpdateSemesterrechnungCommand = new SaveOrUpdateSemesterrechnungCommand(semesterrechnung1, semesterrechnungCode1, null, erfassteSemesterrechnungen);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateSemesterrechnungCommand);
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
                new GregorianCalendar(1912, Calendar.FEBRUARY, 29),
                new BigDecimal("201.00"),
                new GregorianCalendar(1912, Calendar.MARCH, 11),
                new BigDecimal("161.00"),
                new GregorianCalendar(1912, Calendar.APRIL, 21),
                new BigDecimal("157.00"),
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
                true);
        saveOrUpdateSemesterrechnungCommand = new SaveOrUpdateSemesterrechnungCommand(semesterrechnung2, semesterrechnungCode2, null, erfassteSemesterrechnungen);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateSemesterrechnungCommand);

        assertEquals(2, erfassteSemesterrechnungen.size());

        // Semesterrechnungen löschen
        DeleteSemesterrechnungCommand deleteSemesterrechnungCommand = new DeleteSemesterrechnungCommand(erfassteSemesterrechnungen, 1);
        commandInvoker.executeCommandAsTransaction(deleteSemesterrechnungCommand);
        assertEquals(1, erfassteSemesterrechnungen.size());

        deleteSemesterrechnungCommand = new DeleteSemesterrechnungCommand(erfassteSemesterrechnungen, 0);
        commandInvoker.executeCommandAsTransaction(deleteSemesterrechnungCommand);
        assertTrue(erfassteSemesterrechnungen.isEmpty());

        // Testdaten löschen
        EntityManager entityManager = db.getCurrentEntityManager();
        entityManager.getTransaction().begin();
        for (Semesterrechnung semesterrechnung : erfassteSemesterrechnungen) {
            Semesterrechnung semesterrechnungToBeDeleted = semesterrechnungDao.findById(new SemesterrechnungId(semesterrechnung.getSemester().getSemesterId(), semesterrechnung.getRechnungsempfaenger().getPersonId()));
            if (semesterrechnungToBeDeleted != null) {
                semesterrechnungDao.remove(semesterrechnungToBeDeleted);
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
        for (SemesterrechnungCode semesterrechnungCode : erfassteSemesterrechnungCodes) {
            SemesterrechnungCode semesterrechnungCodeToBeDeleted = semesterrechnungCodeDao.findById(semesterrechnungCode.getCodeId());
            if (semesterrechnungCodeToBeDeleted != null) {
                semesterrechnungCodeDao.remove(semesterrechnungCodeToBeDeleted);
            }
        }
        entityManager.getTransaction().commit();
        entityManager.getTransaction().begin();
        Schueler schuelerToBeRemoved1 = schuelerDao.findById(schueler1.getPersonId());
        Schueler schuelerToBeRemoved2 = schuelerDao.findById(schueler2.getPersonId());
        schuelerDao.remove(schuelerToBeRemoved1);
        schuelerDao.remove(schuelerToBeRemoved2);
        entityManager.getTransaction().commit();
    }

    private boolean checkIfSemesterrechnungAvailable(Semester semester, Angehoeriger rechnungsempfaenger, Stipendium stipendium, Boolean gratiskinder,
                                                     Calendar rechnungsdatumVorrechnung, BigDecimal ermaessigungVorrechnung, String ermaessigungsgrundVorrechnung, BigDecimal zuschlagVorrechnung, String zuschlagsgrundVorrechnung, Integer anzahlWochenVorrechnung, BigDecimal wochenbetragVorrechnung, Calendar datumZahlung1Vorrechnung, BigDecimal betragZahlung1Vorrechnung, Calendar datumZahlung2Vorrechnung, BigDecimal betragZahlung2Vorrechnung, Calendar datumZahlung3Vorrechnung, BigDecimal betragZahlung3Vorrechnung,
                                                     Calendar rechnungsdatumNachrechnung, BigDecimal ermaessigungNachrechnung, String ermaessigungsgrundNachrechnung, BigDecimal zuschlagNachrechnung, String zuschlagsgrundNachrechnung, Integer anzahlWochenNachrechnung, BigDecimal wochenbetragNachrechnung, Calendar datumZahlung1Nachrechnung, BigDecimal betragZahlung1Nachrechnung, Calendar datumZahlung2Nachrechnung, BigDecimal betragZahlung2Nachrechnung, Calendar datumZahlung3Nachrechnung, BigDecimal betragZahlung3Nachrechnung,
                                                     String bemerkungen, SemesterrechnungCode semesterrechnungCode, Boolean deleted) {
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
                    && semesterrechnung.getDatumZahlung1Vorrechnung().equals(datumZahlung1Vorrechnung)
                    && semesterrechnung.getBetragZahlung1Vorrechnung().compareTo(betragZahlung1Vorrechnung) == 0
                    && semesterrechnung.getDatumZahlung2Vorrechnung().equals(datumZahlung2Vorrechnung)
                    && semesterrechnung.getBetragZahlung2Vorrechnung().compareTo(betragZahlung2Vorrechnung) == 0
                    && semesterrechnung.getDatumZahlung3Vorrechnung().equals(datumZahlung3Vorrechnung)
                    && semesterrechnung.getBetragZahlung3Vorrechnung().compareTo(betragZahlung3Vorrechnung) == 0
                    && semesterrechnung.getRechnungsdatumNachrechnung().equals(rechnungsdatumNachrechnung)
                    && semesterrechnung.getErmaessigungNachrechnung().compareTo(ermaessigungNachrechnung) == 0
                    && semesterrechnung.getErmaessigungsgrundNachrechnung().equals(ermaessigungsgrundNachrechnung)
                    && semesterrechnung.getZuschlagNachrechnung().compareTo(zuschlagNachrechnung) == 0
                    && semesterrechnung.getZuschlagsgrundNachrechnung().equals(zuschlagsgrundNachrechnung)
                    && semesterrechnung.getAnzahlWochenNachrechnung().equals(anzahlWochenNachrechnung)
                    && semesterrechnung.getWochenbetragNachrechnung().compareTo(wochenbetragNachrechnung) == 0
                    && semesterrechnung.getDatumZahlung1Nachrechnung().equals(datumZahlung1Nachrechnung)
                    && semesterrechnung.getBetragZahlung1Nachrechnung().compareTo(betragZahlung1Nachrechnung) == 0
                    && semesterrechnung.getDatumZahlung2Nachrechnung().equals(datumZahlung2Nachrechnung)
                    && semesterrechnung.getBetragZahlung2Nachrechnung().compareTo(betragZahlung2Nachrechnung) == 0
                    && semesterrechnung.getDatumZahlung3Nachrechnung().equals(datumZahlung3Nachrechnung)
                    && semesterrechnung.getBetragZahlung3Nachrechnung().compareTo(betragZahlung3Nachrechnung) == 0
                    && semesterrechnung.getBemerkungen().equals(bemerkungen)
                    && semesterrechnung.getSemesterrechnungCode().isIdenticalWith(semesterrechnungCode)
                    && semesterrechnung.getDeleted().equals(deleted)) {
                return true;
            }
        }
        return false;
    }

}