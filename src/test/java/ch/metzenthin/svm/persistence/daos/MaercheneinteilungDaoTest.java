package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.common.dataTypes.Anrede;
import ch.metzenthin.svm.common.dataTypes.Elternmithilfe;
import ch.metzenthin.svm.common.dataTypes.Geschlecht;
import ch.metzenthin.svm.common.dataTypes.Gruppe;
import ch.metzenthin.svm.common.utils.SvmProperties;
import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.entities.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;
import static org.junit.Assert.*;

/**
 * @author Martin Schraner
 */
public class MaercheneinteilungDaoTest {

    private DB db;
    private EntityManager entityManager;
    private MaercheneinteilungDao maercheneinteilungDao;
    private boolean neusteZuoberst;


    @Before
    public void setUp() throws Exception {
        createSvmPropertiesFileDefault();
        Properties svmProperties = SvmProperties.getSvmProperties();
        neusteZuoberst = !svmProperties.getProperty(SvmProperties.KEY_NEUSTE_ZUOBERST).equals("false");
        db = DBFactory.getInstance();
        entityManager = db.getCurrentEntityManager();
        maercheneinteilungDao = new MaercheneinteilungDao(entityManager);
    }

    @After
    public void tearDown() throws Exception {
        db.closeSession();
    }

    @Test
    public void testFindById() {
        EntityTransaction tx = null;
        try {
            tx = entityManager.getTransaction();
            tx.begin();

            // Schüler, Märchen, ElternmithilfeCode erzeugen
            Schueler schueler = new Schueler("Jana", "Rösle", new GregorianCalendar(2012, Calendar.JULY, 24), "044 491 69 33", null, null, Geschlecht.W, "Schwester von Valentin");
            Adresse adresse = new Adresse("Hohenklingenstrasse", "15", "8049", "Zürich");
            schueler.setAdresse(adresse);
            schueler.addAnmeldung(new Anmeldung(new GregorianCalendar(2015, Calendar.JANUARY, 1), null));
            Angehoeriger vater = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", "044 491 69 33", null, null);
            vater.setAdresse(adresse);
            schueler.setVater(vater);
            schueler.setRechnungsempfaenger(vater);
            entityManager.persist(schueler);
            Maerchen maerchen = new Maerchen("2011/2012", "Schneewittchen", 7);
            entityManager.persist(maerchen);
            ElternmithilfeCode elternmithilfeCode = new ElternmithilfeCode("f", "Frisieren", true);
            entityManager.persist(elternmithilfeCode);

            // Märcheneinteilung
            Maercheneinteilung maercheneinteilung = new Maercheneinteilung(schueler, maerchen, Gruppe.A, "Komödiant 1", "1, 2", "Hase 2", "2, 3", "Frosch 3", "3, 4", Elternmithilfe.VATER,
                    true, true, true, false, false, false, false, false, false, null, null);
            maercheneinteilung.setElternmithilfeCode(elternmithilfeCode);
            entityManager.persist(maercheneinteilung);

            Maercheneinteilung maercheneinteilungFound = maercheneinteilungDao.findById(new MaercheneinteilungId(maercheneinteilung.getSchueler().getPersonId(), maercheneinteilung.getMaerchen().getMaerchenId()));
            assertEquals("Jana", maercheneinteilungFound.getSchueler().getVorname());
            assertEquals("Schneewittchen", maercheneinteilungFound.getMaerchen().getBezeichnung());
            assertEquals(Gruppe.A, maercheneinteilungFound.getGruppe());
            assertEquals("Komödiant 1", maercheneinteilungFound.getRolle1());
            assertEquals("1, 2", maercheneinteilungFound.getBilderRolle1());
            assertEquals("Hase 2", maercheneinteilungFound.getRolle2());
            assertEquals("2, 3", maercheneinteilungFound.getBilderRolle2());
            assertEquals("Frosch 3", maercheneinteilungFound.getRolle3());
            assertEquals("3, 4", maercheneinteilungFound.getBilderRolle3());
            assertEquals(Elternmithilfe.VATER, maercheneinteilungFound.getElternmithilfe());
            assertEquals("f", maercheneinteilung.getElternmithilfeCode().getKuerzel());
            assertTrue(maercheneinteilung.getKuchenVorstellung1());
            assertTrue(maercheneinteilung.getKuchenVorstellung2());
            assertTrue(maercheneinteilung.getKuchenVorstellung3());
            assertFalse(maercheneinteilung.getKuchenVorstellung4());
            assertFalse(maercheneinteilung.getKuchenVorstellung5());
            assertFalse(maercheneinteilung.getKuchenVorstellung6());
            assertFalse(maercheneinteilung.getKuchenVorstellung7());
            assertFalse(maercheneinteilung.getKuchenVorstellung8());
            assertFalse(maercheneinteilung.getKuchenVorstellung9());
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

            // Schüler, Märchen, ElternmithilfeCode erzeugen
            Schueler schueler = new Schueler("Jana", "Rösle", new GregorianCalendar(2012, Calendar.JULY, 24), "044 491 69 33", null, null, Geschlecht.W, "Schwester von Valentin");
            Adresse adresse = new Adresse("Hohenklingenstrasse", "15", "8049", "Zürich");
            schueler.setAdresse(adresse);
            schueler.addAnmeldung(new Anmeldung(new GregorianCalendar(2015, Calendar.JANUARY, 1), null));
            Angehoeriger vater = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", "044 491 69 33", null, null);
            vater.setAdresse(adresse);
            schueler.setVater(vater);
            schueler.setRechnungsempfaenger(vater);
            entityManager.persist(schueler);
            Maerchen maerchen = new Maerchen("2011/2012", "Schneewittchen", 7);
            entityManager.persist(maerchen);
            ElternmithilfeCode elternmithilfeCode = new ElternmithilfeCode("f", "Frisieren", true);
            entityManager.persist(elternmithilfeCode);

            // Märcheneinteilung
            Maercheneinteilung maercheneinteilung = new Maercheneinteilung(schueler, maerchen, Gruppe.A, "Komödiant 1", "1, 2", "Hase 2", "2, 3", "Frosch 3", "3, 4", Elternmithilfe.VATER,
                    true, true, true, false, false, false, false, false, false, null, null);
            maercheneinteilung.setElternmithilfeCode(elternmithilfeCode);
            maercheneinteilungDao.save(maercheneinteilung);

            Maercheneinteilung maercheneinteilungFound = maercheneinteilungDao.findById(new MaercheneinteilungId(maercheneinteilung.getSchueler().getPersonId(), maercheneinteilung.getMaerchen().getMaerchenId()));
            assertEquals("Jana", maercheneinteilungFound.getSchueler().getVorname());
            assertEquals("Schneewittchen", maercheneinteilungFound.getMaerchen().getBezeichnung());
            assertEquals(Gruppe.A, maercheneinteilungFound.getGruppe());
            assertEquals("Komödiant 1", maercheneinteilungFound.getRolle1());

            assertEquals(1, schueler.getMaercheneinteilungen().size());
            assertEquals(1, maerchen.getMaercheneinteilungen().size());

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

            // Schüler, Märchen, ElternmithilfeCode erzeugen
            Schueler schueler = new Schueler("Jana", "Rösle", new GregorianCalendar(2012, Calendar.JULY, 24), "044 491 69 33", null, null, Geschlecht.W, "Schwester von Valentin");
            Adresse adresse = new Adresse("Hohenklingenstrasse", "15", "8049", "Zürich");
            schueler.setAdresse(adresse);
            schueler.addAnmeldung(new Anmeldung(new GregorianCalendar(2015, Calendar.JANUARY, 1), null));
            Angehoeriger vater = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", "044 491 69 33", null, null);
            vater.setAdresse(adresse);
            schueler.setVater(vater);
            schueler.setRechnungsempfaenger(vater);
            entityManager.persist(schueler);
            Maerchen maerchen = new Maerchen("2011/2012", "Schneewittchen", 7);
            entityManager.persist(maerchen);
            ElternmithilfeCode elternmithilfeCode = new ElternmithilfeCode("f", "Frisieren", true);
            entityManager.persist(elternmithilfeCode);

            // Märcheneinteilung
            Maercheneinteilung maercheneinteilung = new Maercheneinteilung(schueler, maerchen, Gruppe.A, "Komödiant 1", "1, 2", "Hase 2", "2, 3", "Frosch 3", "3, 4", Elternmithilfe.VATER,
                    true, true, true, false, false, false, false, false, false, null, null);
            maercheneinteilung.setElternmithilfeCode(elternmithilfeCode);
            Maercheneinteilung maercheneinteilungSaved = maercheneinteilungDao.save(maercheneinteilung);
            int personId = maercheneinteilungSaved.getSchueler().getPersonId();
            int maerchenId = maercheneinteilungSaved.getMaerchen().getMaerchenId();

            entityManager.flush();
            assertNotNull(maercheneinteilungDao.findById(new MaercheneinteilungId(personId, maerchenId)));
            assertEquals(1, schueler.getMaercheneinteilungen().size());
            assertEquals(1, maerchen.getMaercheneinteilungen().size());

            // Delete Maercheneinteilung
            maercheneinteilungDao.remove(maercheneinteilungSaved);
            entityManager.flush();
            assertNull(maercheneinteilungDao.findById(new MaercheneinteilungId(personId, maerchenId)));
            assertEquals(0, schueler.getMaercheneinteilungen().size());
            assertEquals(0, maerchen.getMaercheneinteilungen().size());

        } finally {
            if (tx != null) {
                tx.rollback();
            }
        }

    }

    @Test
    public void testFindMaercheneinteilungenSchueler() {
        EntityTransaction tx = null;

        try {
            tx = entityManager.getTransaction();
            tx.begin();

            // Schüler, Märchen, ElternmithilfeCode erzeugen
            Schueler schueler1 = new Schueler("Jana", "Rösle", new GregorianCalendar(2012, Calendar.JULY, 24), "044 491 69 33", null, null, Geschlecht.W, "Schwester von Valentin");
            Adresse adresse = new Adresse("Hohenklingenstrasse", "15", "8049", "Zürich");
            schueler1.setAdresse(adresse);
            schueler1.addAnmeldung(new Anmeldung(new GregorianCalendar(2015, Calendar.JANUARY, 1), null));
            Angehoeriger vater = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", "044 491 69 33", null, null);
            vater.setAdresse(adresse);
            schueler1.setVater(vater);
            schueler1.setRechnungsempfaenger(vater);
            entityManager.persist(schueler1);

            Schueler schueler2 = new Schueler("Hanna", "Hasler", new GregorianCalendar(2010, Calendar.JULY, 24), "044 422 69 33", null, null, Geschlecht.W, null);
            Adresse adresse2 = new Adresse("Hohenklingenstrasse", "22", "8049", "Zürich");
            schueler2.setAdresse(adresse2);
            schueler2.addAnmeldung(new Anmeldung(new GregorianCalendar(2013, Calendar.JANUARY, 1), null));
            Angehoeriger mutter2 = new Angehoeriger(Anrede.FRAU, "Adriana", "Hasler", "044 422 69 33", null, null);
            mutter2.setAdresse(adresse2);
            schueler2.setMutter(mutter2);
            schueler2.setRechnungsempfaenger(mutter2);
            entityManager.persist(schueler2);

            Schueler schueler3 = new Schueler("Lina", "Hasler", new GregorianCalendar(2010, Calendar.JULY, 24), "044 422 69 33", null, null, Geschlecht.W, null);
            Adresse adresse3 = new Adresse("Hohenklingenstrasse", "22", "8049", "Zürich");
            schueler3.setAdresse(adresse3);
            schueler3.addAnmeldung(new Anmeldung(new GregorianCalendar(2013, Calendar.JANUARY, 1), null));
            Angehoeriger mutter3 = new Angehoeriger(Anrede.FRAU, "Adriana", "Hasler", "044 422 69 33", null, null);
            mutter3.setAdresse(adresse3);
            schueler3.setMutter(mutter3);
            schueler3.setRechnungsempfaenger(mutter3);
            entityManager.persist(schueler3);

            Maerchen maerchen1 = new Maerchen("1911/1912", "Schneewittchen", 7);
            entityManager.persist(maerchen1);
            Maerchen maerchen2 = new Maerchen("1912/2013", "Froschkönig", 8);
            entityManager.persist(maerchen2);

            ElternmithilfeCode elternmithilfeCode1 = new ElternmithilfeCode("f", "Frisieren", true);
            entityManager.persist(elternmithilfeCode1);
            ElternmithilfeCode elternmithilfeCode2 = new ElternmithilfeCode("k", "Kuchen", true);
            entityManager.persist(elternmithilfeCode2);

            // Märcheneinteilung
            Maercheneinteilung maercheneinteilung1 = new Maercheneinteilung(schueler1, maerchen1, Gruppe.A, "Komödiant 1", "1, 2", "Hase 1", "2, 3", "Frosch 1", "3, 4", Elternmithilfe.VATER,
                    true, true, true, false, false, false, false, false, false, null, null);
            maercheneinteilung1.setElternmithilfeCode(elternmithilfeCode1);
            entityManager.persist(maercheneinteilung1);
            Maercheneinteilung maercheneinteilung2 = new Maercheneinteilung(schueler2, maerchen1, Gruppe.A, "Komödiant 2", "1, 2", "Hase 2", "2, 3", "Frosch 2", "3, 4", Elternmithilfe.MUTTER,
                    true, true, true, false, false, false, false, false, false, null, null);
            maercheneinteilung1.setElternmithilfeCode(elternmithilfeCode1);
            entityManager.persist(maercheneinteilung2);
            Maercheneinteilung maercheneinteilung3 = new Maercheneinteilung(schueler1, maerchen2, Gruppe.B, "Erzähltaube 2", "1, 2", null, null, null, null, Elternmithilfe.MUTTER,
                    true, true, true, false, false, false, false, false, false, null, null);
            entityManager.persist(maercheneinteilung3);

            maercheneinteilung1.setElternmithilfeCode(elternmithilfeCode1);
            entityManager.persist(maercheneinteilung2);

            entityManager.flush();

            // Nach Märcheneinteilungen von Schüler 1 suchen
            List<Maercheneinteilung> maercheneinteilungList1 = maercheneinteilungDao.findMaercheneinteilungenSchueler(schueler1);
            assertEquals(2, maercheneinteilungList1.size());
            if (neusteZuoberst) {
                assertEquals("Erzähltaube 2", maercheneinteilungList1.get(0).getRolle1());
                assertEquals("Komödiant 1", maercheneinteilungList1.get(1).getRolle1());
            } else {
                assertEquals("Erzähltaube 2", maercheneinteilungList1.get(1).getRolle1());
                assertEquals("Komödiant 1", maercheneinteilungList1.get(0).getRolle1());
            }

            // Nach Märcheneinteilungen von Schüler 2 suchen
            List<Maercheneinteilung> maercheneinteilungList2 = maercheneinteilungDao.findMaercheneinteilungenSchueler(schueler2);
            assertEquals(1, maercheneinteilungList2.size());
            assertEquals("Komödiant 2", maercheneinteilungList2.get(0).getRolle1());

            // Nach Märcheneinteilungen von Schüler 3 suchen
            List<Maercheneinteilung> maercheneinteilungList3 = maercheneinteilungDao.findMaercheneinteilungenSchueler(schueler3);
            assertTrue(maercheneinteilungList3.isEmpty());

        } finally {
            if (tx != null) {
                tx.rollback();
            }
        }

    }

}