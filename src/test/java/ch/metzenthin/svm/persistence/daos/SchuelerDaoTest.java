package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.dataTypes.Anrede;
import ch.metzenthin.svm.dataTypes.Geschlecht;
import ch.metzenthin.svm.persistence.entities.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.*;

import static org.junit.Assert.*;

/**
 * @author Hans Stamm
 */
public class SchuelerDaoTest {

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private SchuelerDao schuelerDao;

    @Before
    public void setUp() throws Exception {
        entityManagerFactory = Persistence.createEntityManagerFactory("svm");
        entityManager = entityManagerFactory.createEntityManager();
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

            // Schueler
            Schueler schueler = new Schueler("Jana", "Rösle", new GregorianCalendar(2012, Calendar.JULY, 24), null, null, Geschlecht.W, "Schwester von Valentin");
            Adresse adresse = new Adresse("Hohenklingenstrasse", "15", "8049", "Zürich", "044 491 69 33");
            schueler.setAdresse(adresse);
            schueler.addAnmeldung(new Anmeldung(new GregorianCalendar(2015, Calendar.JANUARY, 1), null));

            // Vater
            Angehoeriger vater = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", null, null);
            vater.setAdresse(adresse);
            schueler.setVater(vater);

            // Rechnungsempfänger
            schueler.setRechnungsempfaenger(vater);

            entityManager.persist(schueler);

            Schueler schuelerFound = schuelerDao.findById(schueler.getPersonId());

            // Erzwingen, dass von DB gelesen wird
            entityManager.refresh(schuelerFound);

            assertEquals("Anmeldedatum falsch", new GregorianCalendar(2015, Calendar.JANUARY, 1), schuelerFound.getAnmeldungen().get(0).getAnmeldedatum());

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

            // Schueler
            Schueler schueler = new Schueler("Jana", "Rösle", new GregorianCalendar(2012, Calendar.JULY, 24), null, null, Geschlecht.W, "Schwester von Valentin");
            Adresse adresse = new Adresse("Hohenklingenstrasse", "15", "8049", "Zürich", "044 491 69 33");
            schueler.setAdresse(adresse);

            // Vater
            Angehoeriger vater = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", null, null);
            vater.setAdresse(adresse);
            schueler.setVater(vater);

            // Mutter
            Angehoeriger mutter = new Angehoeriger(Anrede.FRAU, "Regula", "Rösle", null, null);
            mutter.setAdresse(adresse);
            schueler.setMutter(mutter);

            // Rechnungsempfänger
            schueler.setRechnungsempfaenger(vater);

            // Anmeldungen
            schueler.addAnmeldung(new Anmeldung(new GregorianCalendar(2014, Calendar.JANUARY, 1), new GregorianCalendar(2014, Calendar.JANUARY, 10)));
            schueler.addAnmeldung(new Anmeldung(new GregorianCalendar(2015, Calendar.FEBRUARY, 1), null));

            // Dispensationen
            Dispensation dispensation0 = new Dispensation(new GregorianCalendar(2015, Calendar.JUNE, 15), new GregorianCalendar(2015, Calendar.AUGUST, 31), "Arm gebrochen");
            schueler.addDispensation(dispensation0);
            Dispensation dispensation1 = new Dispensation(new GregorianCalendar(2015, Calendar.MAY, 1), new GregorianCalendar(2015, Calendar.JULY, 31), "Beinbruch");
            schueler.addDispensation(dispensation1);

            Schueler schuelerSaved = schuelerDao.save(schueler);

            entityManager.flush();

            Schueler schuelerFound = schuelerDao.findById(schuelerSaved.getPersonId());

            // Erzwingen, dass von DB gelesen wird
            entityManager.refresh(schuelerFound);

            assertEquals("Vater not correct", "Eugen", schuelerFound.getVater().getVorname());
            assertEquals("Mutter not correct", "Regula", schuelerFound.getMutter().getVorname());
            assertEquals("Vater not correct", "Eugen", schuelerFound.getRechnungsempfaenger().getVorname());
            assertEquals("Adresse not correct", "Hohenklingenstrasse", schuelerFound.getAdresse().getStrasse());

            // Ist Vater identisch mit Rechnungsempfänger?
            assertEquals("person_id not equal", schuelerFound.getVater().getPersonId(), schuelerFound.getRechnungsempfaenger().getPersonId());

            // Sind adresseIds identisch?
            assertEquals("adresse_id not equal", schuelerFound.getAdresse().getAdresseId(), schuelerFound.getMutter().getAdresse().getAdresseId());

            // Anmeldungen
            List<Anmeldung> anmeldungen = schuelerFound.getAnmeldungen();
            assertEquals("2 Anmeldungen erwartet", 2, anmeldungen.size());
            assertEquals("Falsches Anmeldedatum", new GregorianCalendar(2015, Calendar.FEBRUARY, 1), anmeldungen.get(0).getAnmeldedatum());  // neuste zuerst
            assertEquals("Falsches Anmeldedatum", new GregorianCalendar(2014, Calendar.JANUARY, 1), anmeldungen.get(1).getAnmeldedatum());

            // Dispensationen
            List<Dispensation> dispensationen = schuelerFound.getDispensationen();
            assertEquals("2 Dispensationen erwartet", 2, dispensationen.size());
            assertEquals("Arm gebrochen erwartet", "Arm gebrochen", dispensationen.get(0).getGrund());   // neuste zuerst
            assertEquals("Beinbruch erwartet", "Beinbruch", dispensationen.get(1).getGrund());

            // Arm gebrochen löschen
            schuelerFound.deleteDispensation(dispensationen.get(0));

            List<Dispensation> dispensationen2 = schuelerFound.getDispensationen();
            assertEquals("1 Dispensation erwartet", 1, dispensationen2.size());
            assertEquals("Beinbruch erwartet", "Beinbruch", dispensationen.get(0).getGrund());

            // Alte Anmeldung löschen
            schuelerFound.deleteAnmeldung(anmeldungen.get(1));

            List<Anmeldung> anmeldungen2 = schuelerFound.getAnmeldungen();
            assertEquals("1 Anmeldung erwartet", 1, anmeldungen2.size());
            assertEquals("Falsches Anmeldedatum", new GregorianCalendar(2015, Calendar.FEBRUARY, 1), anmeldungen2.get(0).getAnmeldedatum());

        } finally {
            if (tx != null)
                tx.rollback();
        }

    }

    @Test
    public void testRemove() {
        EntityTransaction tx = null;

        try {

            AdresseDao adresseDao = new AdresseDao(entityManager);
            AngehoerigerDao angehoerigerDao = new AngehoerigerDao(entityManager);
            AnmeldungDao anmeldungDao = new AnmeldungDao(entityManager);
            DispensationDao dispensationDao = new DispensationDao(entityManager);

            // Create 2 Schueler with the same parents, but different Rechnungsempfaenger
            tx = entityManager.getTransaction();
            tx.begin();

            // Schueler1
            Schueler schueler1 = new Schueler("Jana", "Rösle", new GregorianCalendar(2012, Calendar.JULY, 24), null, null, Geschlecht.W, "Schwester von Valentin Dan");
            Adresse adresse = new Adresse("Hohenklingenstrasse", "15", "8049", "Zürich", "044 491 69 33");
            schueler1.setAdresse(adresse);
            Anmeldung anmeldungSchueler1 = new Anmeldung(new GregorianCalendar(2015, Calendar.JANUARY, 1), null);
            schueler1.addAnmeldung(anmeldungSchueler1);

            Angehoeriger vater = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", null, null);
            vater.setAdresse(adresse);
            schueler1.setVater(vater);

            Angehoeriger mutter = new Angehoeriger(Anrede.FRAU, "Regula", "Rösle", null, null);
            mutter.setAdresse(adresse);
            schueler1.setMutter(mutter);

            Adresse adresseRechnungsempfaenger = new Adresse("Hintere Bergstrassse", "15", "8942", "Oberrieden", "044 720 85 51");
            Angehoeriger rechnungsempfaenger1 = new Angehoeriger(Anrede.FRAU, "Käthi", "Schraner", null, null);
            rechnungsempfaenger1.setAdresse(adresseRechnungsempfaenger);
            schueler1.setRechnungsempfaenger(rechnungsempfaenger1);

            Schueler schueler1Saved = schuelerDao.save(schueler1);

            int schuelerId1 = schueler1Saved.getPersonId();
            int vaterId = schueler1Saved.getVater().getPersonId();
            int mutterId = schueler1Saved.getMutter().getPersonId();
            int rechungsempfaengerId1 = schueler1Saved.getRechnungsempfaenger().getPersonId();
            int adresseId = schueler1Saved.getAdresse().getAdresseId();

            // Schueler2
            Schueler schueler2 = new Schueler("Valentin Dan", "Rösle", new GregorianCalendar(2014, Calendar.SEPTEMBER, 24), null, null, Geschlecht.M, "Bruder von Jana");
            schueler2.setAdresse(adresse);
            schueler2.setVater(vater);
            schueler2.setMutter(mutter);
            schueler2.setRechnungsempfaenger(vater);
            schueler2.addAnmeldung(new Anmeldung(new GregorianCalendar(2015, Calendar.MAY, 15), null));
            schueler2.addDispensation(new Dispensation(new GregorianCalendar(2015, Calendar.MAY, 15), null, "Viel zu klein"));

            Schueler schueler2Saved = schuelerDao.save(schueler2);

            int schuelerId2 = schueler2Saved.getPersonId();
            int rechungsempfaengerId2 = schueler2Saved.getRechnungsempfaenger().getPersonId();

            assertEquals(1, schueler2Saved.getAnmeldungen().size());
            int anmeldungId2 = schueler2Saved.getAnmeldungen().get(0).getAnmeldungId();

            List<Dispensation> dispensationen2 = schueler2Saved.getDispensationen();
            assertEquals(1, dispensationen2.size());
            Integer dispensationId2 = dispensationen2.get(0).getDispensationId();

            entityManager.flush();

            assertNotNull(schuelerDao.findById(schuelerId1));
            assertNotNull(schuelerDao.findById(schuelerId2));
            assertNotNull(angehoerigerDao.findById(vaterId));
            assertNotNull(angehoerigerDao.findById(mutterId));
            assertNotNull(angehoerigerDao.findById(rechungsempfaengerId1));
            assertNotNull(angehoerigerDao.findById(rechungsempfaengerId2));
            assertNotNull(adresseDao.findById(adresseId));
            assertNotNull(anmeldungDao.findById(anmeldungId2));
            assertNotNull(dispensationDao.findById(dispensationId2));

            // 1. Schüler löschen
            schuelerDao.remove(schueler1Saved);
            entityManager.flush();

            assertNull(schuelerDao.findById(schuelerId1));
            assertNotNull(schuelerDao.findById(schuelerId2));
            assertNotNull(angehoerigerDao.findById(vaterId));
            assertNotNull(angehoerigerDao.findById(mutterId));
            assertNull(angehoerigerDao.findById(rechungsempfaengerId1));
            assertNotNull(angehoerigerDao.findById(rechungsempfaengerId2));
            assertNotNull(adresseDao.findById(adresseId));
            assertNotNull(anmeldungDao.findById(anmeldungId2));
            assertNotNull(dispensationDao.findById(dispensationId2));

            // Delete 2nd Schueler
            schuelerDao.remove(schueler2Saved);
            entityManager.flush();

            assertNull(schuelerDao.findById(schuelerId1));
            assertNull(schuelerDao.findById(schuelerId2));
            assertNull(angehoerigerDao.findById(vaterId));
            assertNull(angehoerigerDao.findById(mutterId));
            assertNull(angehoerigerDao.findById(rechungsempfaengerId1));
            assertNull(angehoerigerDao.findById(rechungsempfaengerId2));
            assertNull(adresseDao.findById(adresseId));
            assertNull(anmeldungDao.findById(anmeldungId2));
            assertNull(dispensationDao.findById(dispensationId2));

            tx.commit();

        } catch (NullPointerException e){
            if (tx != null)
                tx.rollback();
        }

    }

    @Test
    public void testFindSchueler() {

        EntityTransaction tx = null;

        try {
            tx = entityManager.getTransaction();
            tx.begin();

            // Schüler einfügen
            Schueler schueler = new Schueler("Lea", "Müller", new GregorianCalendar(2000, Calendar.MAY, 2), null, null, Geschlecht.W, null);
            Adresse adresse = new Adresse("Gugusweg", "16", "8049", "Zürich", "044 491 69 33");
            schueler.setAdresse(adresse);
            schueler.addAnmeldung(new Anmeldung(new GregorianCalendar(2014, Calendar.JANUARY, 1), new GregorianCalendar(2014, Calendar.JANUARY, 15)));
            schueler.addAnmeldung(new Anmeldung(new GregorianCalendar(2015, Calendar.JANUARY, 1), null));

            Angehoeriger rechnungsempfaenger = new Angehoeriger(Anrede.FRAU, "Käthi", "Schraner", null, null);
            Adresse adresseRechnungsempfaenger = new Adresse("Hintere Bergstrassse", "15", "8942", "Oberrieden", "044 720 85 51");
            rechnungsempfaenger.setAdresse(adresseRechnungsempfaenger);
            schueler.setRechnungsempfaenger(rechnungsempfaenger);

            // Dispensationen
            Dispensation dispensation0 = new Dispensation(new GregorianCalendar(2015, Calendar.JUNE, 15), new GregorianCalendar(2015, Calendar.AUGUST, 31), "Arm gebrochen");
            schueler.addDispensation(dispensation0);
            Dispensation dispensation1 = new Dispensation(new GregorianCalendar(2015, Calendar.MAY, 1), new GregorianCalendar(2015, Calendar.JULY, 31), "Beinbruch");
            schueler.addDispensation(dispensation1);

            Schueler schuelerSaved = schuelerDao.save(schueler);

            entityManager.flush();

            // Nach Schüler suchen (zweites Schüler-Objekt mit denselben Attributen erstellen)
            Schueler schueler2 = new Schueler("Lea", "Müller", new GregorianCalendar(2000, Calendar.MAY, 2), null, null, Geschlecht.W, null);
            Adresse adresse2 = new Adresse("Gugusweg", "16", "8049", "Zürich", "044 491 69 33");
            schueler2.setAdresse(adresse2);
            Anmeldung anmeldung2 = new Anmeldung(new GregorianCalendar(2015, Calendar.JANUARY, 1), null);
            schueler2.addAnmeldung(anmeldung2);
            schueler2.setRechnungsempfaenger(rechnungsempfaenger);

            Angehoeriger rechnungsempfaenger2 = new Angehoeriger(Anrede.FRAU, "Käthi", "Schraner", null, null);
            Adresse adresseRechnungsempfaenger2 = new Adresse("Hintere Bergstrassse", "15", "8942", "Oberrieden", "044 720 85 51");
            rechnungsempfaenger2.setAdresse(adresseRechnungsempfaenger2);
            schueler2.setRechnungsempfaenger(rechnungsempfaenger2);

            List<Schueler> schuelerList2 = schuelerDao.findSchueler(schueler2);
            assertEquals("Nur 1 Schüler erwartet", 1, schuelerList2.size());
            Schueler schuelerFound2 = schuelerList2.get(0);

            // Erzwingen, dass von DB gelesen wird
            entityManager.refresh(schuelerFound2);

            assertNotNull("Schüler nicht gefunden", schuelerFound2);

            // Anmeldung: Reihenfolge prüfen:
            List<Anmeldung> anmeldungen= schuelerFound2.getAnmeldungen();
            assertEquals("2 Anmeldungen erwartet", 2, anmeldungen.size());
            assertEquals("Falsches Anmeldedatum", new GregorianCalendar(2015, Calendar.JANUARY, 1), anmeldungen.get(0).getAnmeldedatum());   // neuste zuerst
            assertEquals("Falsches Abmeldedatum", new GregorianCalendar(2014, Calendar.JANUARY, 15), anmeldungen.get(1).getAbmeldedatum());

            // Dispensationen: Reihenfolge prüfen:
            List<Dispensation> dispensationen = schuelerFound2.getDispensationen();
            assertEquals("2 Dispensationen erwartet", 2, dispensationen.size());
            assertEquals("Arm gebrochen erwartet", "Arm gebrochen", dispensationen.get(0).getGrund());   // neuste zuerst
            assertEquals("Beinbruch erwartet", "Beinbruch", dispensationen.get(1).getGrund());

            // Ditto, aber andere Strasse:
            Schueler schueler3 = new Schueler("Lea", "Müller", new GregorianCalendar(2000, Calendar.MAY, 2), null, null, Geschlecht.W, null);
            Adresse adresse3 = new Adresse("Gugusstrasse", "16", "8049", "Zürich", "044 491 69 33");
            schueler3.setAdresse(adresse3);
            schueler3.addAnmeldung(anmeldung2);
            schueler3.setRechnungsempfaenger(rechnungsempfaenger2);

            List<Schueler> schuelerList3 = schuelerDao.findSchueler(schueler3);
            assertNull("Schüler gefunden", schuelerList3);

            // Sämtliche Schüler suchen
            List<Schueler> schuelerList4 = schuelerDao.findSchueler(null);
            assertNotNull("Keine Schüler gefunden", schuelerList4);

            // Schüler löschen
            schuelerDao.remove(schuelerSaved);

            tx.commit();

        } catch (NullPointerException e){
            if (tx != null)
                tx.rollback();
        }

    }
}

