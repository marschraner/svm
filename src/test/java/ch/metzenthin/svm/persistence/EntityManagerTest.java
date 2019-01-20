package ch.metzenthin.svm.persistence;

import ch.metzenthin.svm.common.utils.PersistenceProperties;
import ch.metzenthin.svm.persistence.entities.SchuelerCode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * EntityManagerFactory = persistence unit = logical database
 *     one shared instance
 * EntityManager:
 *     EntityManager interface = persistence manager API
 *     cheap, doesn't obtain a JDBC Connection
 *     EntityManagerFactory.createEntityManager() starts its persistence context
 *     interact with and use EntityManager interface for a single unit of work in a single thread
 *     scope: EntityManagerFactory.createEntityManager() ... EntityManager.close()
 * EntityTransaction:
 *     Everything between begin() and commit() occurs in one transaction
 *     commit(): performs dirty checking of the persistence context and synchronizes with the database
 *     EntityManager.flush(): forces dirty checking synchronization manually. Any time during a transaction.
 * Identifier strategy (siehe Kapitel 4.2.5):
 *     SVM: AUTO_INCREMENT (siehe DDLs) d.h. nach Insert wird der Identifier generiert bzw. hochgezählt.
 *         Nach EntityManager.persist(...) macht Hibernate einen Insert und der Identifier ist danach vorhanden
 *     Bevorzugt gemäss Buch (siehe Kapitel 11.3.2) wäre: pre-insert.
 *         Dann führt Hibernate den Insert so spät wie möglich aus.
 *         Bei EntityManager.persist(...) wird nur der Identifier generiert (ohne Insert)
 *
 * Fehlerhaftes Verhalten von EntityManager:
 * - Ausgangslage: Erzeugen von zwei EM-Instanzen. Mit der ersten Instanz: modifizieren einer Entität, Commit. Danach
 *   Lesen der modifizierten Instanz mit der zweiten Instanz.
 * - Symptom: Die Änderung der ersten Instanz ist in der zweiten Instanz nicht immer sichtbar.
 * - Das fehlerhafte Verhalten liegt nicht an der Verwendung des built-in connection pools (siehe Warn-Meldung unten).
 *   Es tritt mit älteren Versionen von Hibernate auf (getestet mit 4.3.9 und 5.0.1).
 *   Mit einer neueren Version von Hibernate (5.2.9) ist das fehlerhafte Verhalten weg.
 *   - Änderungen sind schon nach clear und erneutem Lesen sichtbar
 *   - Refresh verhält sich jetzt auch wie erwartet und wie im Buch Java Persistence with Hibernate beschrieben.
 *
 * Warnung sichtbar durch: log4j.logger.org.hibernate=INFO
 * WARN DriverManagerConnectionProviderImpl:93 - HHH000402: Using Hibernate built-in connection pool (not for production use!)
 * Lösung: Verwendung eines anderen Connection Pools, z.B. c3p0 oder Bitronix (wie im Buch Java Persistence with Hibernate)
 *
 * @author Hans Stamm
 */
public class EntityManagerTest {

    private EntityManagerFactory entityManagerFactory;
    private SchuelerCode detachedInitialSchuelerCode;

    private List<String> createdCodes = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        // createSvmPropertiesFileDefault();
        entityManagerFactory = createEntityManagerFactory();
        EntityManager em = entityManagerFactory.createEntityManager();
        SchuelerCode schuelerCode = createSchuelerCode("ini", "initial test insert");
        em.getTransaction().begin();
        em.persist(schuelerCode);
        em.getTransaction().commit();
        detachedInitialSchuelerCode = schuelerCode;
        em.close();
    }

    private EntityManagerFactory createEntityManagerFactory() {
        return Persistence.createEntityManagerFactory("svm", PersistenceProperties.getPersistenceProperties());
    }

    @After
    public void tearDown() throws Exception {
        EntityManager em = entityManagerFactory.createEntityManager(); // starts persistence context
        em.getTransaction().begin();
        for (String kuerzel : createdCodes) {
            SchuelerCode toDelete = getSchuelerCodeByKuerzel(em, kuerzel);
            if (toDelete != null) {
                em.remove(toDelete);
            }
        }
        em.getTransaction().commit();
        em.close();
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }

    /**
     * Unsynchronized mode, reading data in auto-commit mode, nontransactional: No flushing (or commit) allowed
     */
    @Test
    public void testUnsynchronized() {
        EntityManager emA = entityManagerFactory.createEntityManager();
        SchuelerCode schuelerCodeA = getInitialSchuelerCode(emA);
        assertEquals(schuelerCodeA.getCodeId(), detachedInitialSchuelerCode.getCodeId());
        schuelerCodeA.setBeschreibung("Test unsynchronized");
        try {
            // flush needs a transaction => javax.persistence.TransactionRequiredException
            emA.flush();
            fail();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        } finally {
            if (emA.isOpen()) {
                emA.close();
            }
        }

        EntityManager emB = entityManagerFactory.createEntityManager();
        SchuelerCode schuelerCodeB = getInitialSchuelerCode(emB);
        assertEquals(schuelerCodeB.getBeschreibung(), detachedInitialSchuelerCode.getBeschreibung());
        schuelerCodeB.setBeschreibung("Test unsynchronized");
        emB.close();

        EntityManager emC = entityManagerFactory.createEntityManager();
        SchuelerCode schuelerCodeC = getInitialSchuelerCode(emC);
        assertEquals(schuelerCodeC.getBeschreibung(), detachedInitialSchuelerCode.getBeschreibung());
        emC.close();
    }

    /**
     * Modification in unsynchronised mode without transaction (begin..commit) has no effect
     */
    @Test
    public void testUnsynchronizedNewSchuelerCode() {
        EntityManager em = entityManagerFactory.createEntityManager();
        SchuelerCode schuelerCodeA = createSchuelerCode("xx", "xx");
        em.persist(schuelerCodeA);

        em.close();

        em = entityManagerFactory.createEntityManager();
        SchuelerCode schuelerCodeB = getSchuelerCodeByKuerzel(em, "xx");
        assertNull("SchuelerCode not expected to be persistent.", schuelerCodeB);

        em.close();
    }

    /**
     * Modification in unsynchronised mode followed by a transaction (begin..commit) has effect
     */
    @Test
    public void testUnsynchronizedModifySchuelerCodeAndJoinTransaction() {
        EntityManager em = entityManagerFactory.createEntityManager();
        SchuelerCode schuelerCodeA = getInitialSchuelerCode(em);
        assertEquals(detachedInitialSchuelerCode.getBeschreibung(), schuelerCodeA.getBeschreibung());
        schuelerCodeA.setBeschreibung("Test unsynchronized"); // Note: outside of transaction
        schuelerCodeA = getInitialSchuelerCode(em);
        assertEquals("Modification was expected to be still here", "Test unsynchronized", schuelerCodeA.getBeschreibung());
        assertFalse(em.isJoinedToTransaction());

        em.getTransaction().begin(); // Note: begin() seems to join to transaction
        assertTrue(em.isJoinedToTransaction());
//        em.joinTransaction(); // Note: has no effect when not using JTA (see begin() above)
//        assertTrue(em.isJoinedToTransaction());
        em.getTransaction().commit(); // persists modification made outside of transaction
        em.close();

        em = entityManagerFactory.createEntityManager();
        SchuelerCode schuelerCodeC = getInitialSchuelerCode(em);
        assertEquals("Modification was expected to be persisted", "Test unsynchronized", schuelerCodeC.getBeschreibung());
        em.close();
    }

    /**
     * New instance in unsynchronised mode followed by a transaction (begin..commit) has effect
     */
    @Test
    public void testUnsynchronizedNewSchuelerCodeAndJoinTransaction() {
        EntityManager em = entityManagerFactory.createEntityManager();
        SchuelerCode schuelerCodeA = createSchuelerCode("xx", "xx");
        em.persist(schuelerCodeA);

        assertFalse(em.isJoinedToTransaction());
        em.getTransaction().begin(); // Note: begin() seems to join to transaction
        assertTrue(em.isJoinedToTransaction());
//        em.joinTransaction(); // Note: has no effect when not using JTA (see begin() above)
//        assertTrue(em.isJoinedToTransaction());
        em.getTransaction().commit(); // persists modification made outside of transaction
        em.close();

        em = entityManagerFactory.createEntityManager();
        SchuelerCode schuelerCodeB = getSchuelerCodeByKuerzel(em, "xx");
        assertNotNull("SchuelerCode expected to be persistent.", schuelerCodeB);

        em.getTransaction().begin();
        em.remove(schuelerCodeB);
        em.getTransaction().commit();
        em.close();

        em = entityManagerFactory.createEntityManager();
        schuelerCodeB = getSchuelerCodeByKuerzel(em, "xx");
        assertNull("SchuelerCode not expected to be persistent.", schuelerCodeB);
        em.close();
    }

    @Test
    public void testVisibilityOfModificationInSecondEntityManager_afterCloseOfA() {
        EntityManager emA = entityManagerFactory.createEntityManager();
        SchuelerCode schuelerCodeA = getInitialSchuelerCode(emA);

        EntityManager emB = entityManagerFactory.createEntityManager();
        getInitialSchuelerCode(emB);

        String beschreibungNew = "emA";
        schuelerCodeA.setBeschreibung(beschreibungNew);
        emA.getTransaction().begin();
        emA.persist(schuelerCodeA);
        emA.getTransaction().commit();

        // emB sieht Änderung von emA noch nicht
        SchuelerCode schuelerCodeB = getInitialSchuelerCode(emB);
        assertEquals(detachedInitialSchuelerCode.getBeschreibung(), schuelerCodeB.getBeschreibung());

        emA.clear();

        // emB sieht Änderung von emA auch nach clear von emA nicht
        schuelerCodeB = getInitialSchuelerCode(emB);
        assertEquals(detachedInitialSchuelerCode.getBeschreibung(), schuelerCodeB.getBeschreibung());

        emA.close();

        // emB sieht Änderung von emA auch nach close von emA nicht
        schuelerCodeB = getInitialSchuelerCode(emB);
        assertEquals(detachedInitialSchuelerCode.getBeschreibung(), schuelerCodeB.getBeschreibung());

        emB.close();

        // erst nach close und create ist die Änderung sichtbar
        emB = entityManagerFactory.createEntityManager();
        schuelerCodeB = getInitialSchuelerCode(emB);
        assertEquals(beschreibungNew, schuelerCodeB.getBeschreibung());
        emB.close();

/* Folgendes Fehlverhalten tritt ab Hibernate 5.2.9. nicht mehr auf (daher auskommentiert):
        // nochmals mit einem neuen EntityManager, der aber komischerweise wieder den alten Wert liefert?!?
        emB = entityManagerFactory.createEntityManager();
        schuelerCodeB = getInitialSchuelerCode(emB);
        assertEquals(detachedInitialSchuelerCode.getBeschreibung(), schuelerCodeB.getBeschreibung());
        emB.close();

        // und nochmals mit einem neuen EntityManager, der dann wieder den neuen Wert liefert?!?
        emB = entityManagerFactory.createEntityManager();
        schuelerCodeB = getInitialSchuelerCode(emB);
        assertEquals(beschreibungNew, schuelerCodeB.getBeschreibung());
        emB.close();

        // neue Factory erzeugen
        entityManagerFactory = createEntityManagerFactory();
*/
        // mit einem neuen EntityManager von der neuen Factory. Die Änderung ist erwartungsgemäss sichtbar.
        emB = entityManagerFactory.createEntityManager();
        schuelerCodeB = getInitialSchuelerCode(emB);
        assertEquals(beschreibungNew, schuelerCodeB.getBeschreibung());
        emB.close();

        // und nochmals mit einem neuen EntityManager. Die Änderung ist erwartungsgemäss sichtbar.
        emB = entityManagerFactory.createEntityManager();
        schuelerCodeB = getInitialSchuelerCode(emB);
        assertEquals(beschreibungNew, schuelerCodeB.getBeschreibung());
        emB.close();
    }

    @Test
    public void testVisibilityOfModificationInSecondEntityManager_afterCloseOfA_synchronize() {
        EntityManager emA = entityManagerFactory.createEntityManager();
        SchuelerCode schuelerCodeA = getInitialSchuelerCode(emA);

        EntityManager emB = entityManagerFactory.createEntityManager();
        getInitialSchuelerCode(emB);

        String beschreibungNew = "emA";
        schuelerCodeA.setBeschreibung(beschreibungNew);
        emA.getTransaction().begin();
        emA.persist(schuelerCodeA);
        emA.getTransaction().commit();

        emB.getTransaction().begin(); // synchronize
        // emB sieht Änderung von emA noch nicht
        SchuelerCode schuelerCodeB = getInitialSchuelerCode(emB);
        assertEquals(detachedInitialSchuelerCode.getBeschreibung(), schuelerCodeB.getBeschreibung());

        emA.clear();

        // emB sieht Änderung von emA auch nach clear von emA nicht
        schuelerCodeB = getInitialSchuelerCode(emB);
        assertEquals(detachedInitialSchuelerCode.getBeschreibung(), schuelerCodeB.getBeschreibung());

        emA.close();

        // emB sieht Änderung von emA auch nach close von emA nicht
        schuelerCodeB = getInitialSchuelerCode(emB);
        assertEquals(detachedInitialSchuelerCode.getBeschreibung(), schuelerCodeB.getBeschreibung());

        emB.getTransaction().commit();
        emB.close();

        // erst nach close und neuem EntityManager ist die Änderung sichtbar
        emB = entityManagerFactory.createEntityManager();
        schuelerCodeB = getInitialSchuelerCode(emB);
        assertEquals(beschreibungNew, schuelerCodeB.getBeschreibung());
        emB.close();

        // mit neuem EntityManager, der auch den richtigen Wert liefert, dank emB.getTransaction().begin()!?
        emB = entityManagerFactory.createEntityManager();
        schuelerCodeB = getInitialSchuelerCode(emB);
        assertEquals(beschreibungNew, schuelerCodeB.getBeschreibung());
        emB.close();
    }

    @Test
    public void testVisibilityOfModificationInSecondEntityManager_afterCloseOfB() {
        EntityManager emA = entityManagerFactory.createEntityManager();
        SchuelerCode schuelerCodeA = getInitialSchuelerCode(emA);

        EntityManager emB = entityManagerFactory.createEntityManager();
        getInitialSchuelerCode(emB);

        String beschreibungNew = "emA";
        schuelerCodeA.setBeschreibung(beschreibungNew);
        emA.getTransaction().begin();
        emA.persist(schuelerCodeA);
        emA.getTransaction().commit();

        // emB sieht Änderung von emA noch nicht
        SchuelerCode schuelerCodeB = getInitialSchuelerCode(emB);
        assertEquals(detachedInitialSchuelerCode.getBeschreibung(), schuelerCodeB.getBeschreibung());

        emB.close();

/* Folgendes Fehlverhalten tritt ab Hibernate 5.2.9. nicht mehr auf (daher auskommentiert):
        // auch nach close und neuem EntityManager ist die Änderung nicht sichtbar
        emB = entityManagerFactory.createEntityManager();
        schuelerCodeB = getInitialSchuelerCode(emB);
        assertEquals(detachedInitialSchuelerCode.getBeschreibung(), schuelerCodeB.getBeschreibung());

        emA.clear();

        emB.close();
        emB = entityManagerFactory.createEntityManager();
        // auch nach clear von emA und close und neuem EntityManager ist die Änderung nicht sichtbar
        schuelerCodeB = getInitialSchuelerCode(emB);
        assertEquals(detachedInitialSchuelerCode.getBeschreibung(), schuelerCodeB.getBeschreibung());

        emA.close();

        emB.close();
        emB = entityManagerFactory.createEntityManager();
        // erst nach close von emA ist die Änderung sichtbar
        schuelerCodeB = getInitialSchuelerCode(emB);
        assertEquals(beschreibungNew, schuelerCodeB.getBeschreibung());

        emB.close();
        // nochmals mit neuem EntityManager, der aber komischerweise wieder den alten Wert liefert?!?
        emB = entityManagerFactory.createEntityManager();
        schuelerCodeB = getInitialSchuelerCode(emB);
        assertEquals(detachedInitialSchuelerCode.getBeschreibung(), schuelerCodeB.getBeschreibung());
        emB.close();

        // und nochmals mit einem neuen EntityManager, der dann wieder den neuen Wert liefert?!?
        emB = entityManagerFactory.createEntityManager();
        schuelerCodeB = getInitialSchuelerCode(emB);
        assertEquals(beschreibungNew, schuelerCodeB.getBeschreibung());
        emB.close();

        // neue Factory erzeugen
        entityManagerFactory = createEntityManagerFactory();
*/

        // mit neuem EntityManager von neuer Factory ist die Änderung erwartungsgemäss sichtbar
        emB = entityManagerFactory.createEntityManager();
        schuelerCodeB = getInitialSchuelerCode(emB);
        assertEquals(beschreibungNew, schuelerCodeB.getBeschreibung());
        emB.close();

        // nochmals mit neuem EntityManager. Die Änderung ist erwartungsgemäss sichtbar.
        emB = entityManagerFactory.createEntityManager();
        schuelerCodeB = getInitialSchuelerCode(emB);
        assertEquals(beschreibungNew, schuelerCodeB.getBeschreibung());
        emB.close();

        // und nochmals mit neuem EntityManager. Die Änderung ist erwartungsgemäss sichtbar.
        emB = entityManagerFactory.createEntityManager();
        schuelerCodeB = getInitialSchuelerCode(emB);
        assertEquals(beschreibungNew, schuelerCodeB.getBeschreibung());
        emB.close();
    }

    @Test
    public void testVisibilityOfModificationInSecondEntityManager_afterCloseOfB_synchronize() {
        EntityManager emA = entityManagerFactory.createEntityManager();
        SchuelerCode schuelerCodeA = getInitialSchuelerCode(emA);

        EntityManager emB = entityManagerFactory.createEntityManager();
        getInitialSchuelerCode(emB);

        String beschreibungNew = "emA";
        schuelerCodeA.setBeschreibung(beschreibungNew);
        emA.getTransaction().begin();
        emA.persist(schuelerCodeA);
        emA.getTransaction().commit();

        emB.getTransaction().begin();
        // emB sieht Änderung von emA noch nicht
        SchuelerCode schuelerCodeB = getInitialSchuelerCode(emB);
        assertEquals(detachedInitialSchuelerCode.getBeschreibung(), schuelerCodeB.getBeschreibung());

        emB.getTransaction().commit();
        emB.close();

        // nach close und create von emB ist die Änderung sichtbar
        emB = entityManagerFactory.createEntityManager();
        schuelerCodeB = getInitialSchuelerCode(emB);
        assertEquals(beschreibungNew, schuelerCodeB.getBeschreibung());

        emA.clear();

        emB.close();
        emB = entityManagerFactory.createEntityManager();
        // auch nach clear von emA und close und create von emB ist die Änderung natürlich sichtbar
        schuelerCodeB = getInitialSchuelerCode(emB);
        assertEquals(beschreibungNew, schuelerCodeB.getBeschreibung());

        emA.close();

        emB.close();
        emB = entityManagerFactory.createEntityManager();
        // und auch nach close von emA ist die Änderung natürlich sichtbar
        schuelerCodeB = getInitialSchuelerCode(emB);
        assertEquals(beschreibungNew, schuelerCodeB.getBeschreibung());
    }

    @Test
    public void testVisibilityOfModificationInSecondEntityManager_afterCommitOfB_synchronize() {
        EntityManager emA = entityManagerFactory.createEntityManager();
        SchuelerCode schuelerCodeA = getInitialSchuelerCode(emA);

        EntityManager emB = entityManagerFactory.createEntityManager();
        getInitialSchuelerCode(emB);

        String beschreibungNew = "emA";
        schuelerCodeA.setBeschreibung(beschreibungNew);
        emA.getTransaction().begin();
        emA.persist(schuelerCodeA);
        emA.getTransaction().commit();

        emB.getTransaction().begin();
        // emB sieht Änderung von emA noch nicht
        SchuelerCode schuelerCodeB = getInitialSchuelerCode(emB);
        assertEquals(detachedInitialSchuelerCode.getBeschreibung(), schuelerCodeB.getBeschreibung());

        emB.getTransaction().commit();
        // emB sieht Änderung von emA noch nicht
        schuelerCodeB = getInitialSchuelerCode(emB);
        assertEquals(detachedInitialSchuelerCode.getBeschreibung(), schuelerCodeB.getBeschreibung());
        emB.close();

        emB = entityManagerFactory.createEntityManager();
        // erst jetzt ist die Änderung sichtbar
        schuelerCodeB = getInitialSchuelerCode(emB);
        assertEquals(beschreibungNew, schuelerCodeB.getBeschreibung());
        emB.close();

        emA.close();
    }

    @Test
    public void testVisibilityOfModificationInSecondEntityManager_afterImmediateCloseOfA() {
        EntityManager emA = entityManagerFactory.createEntityManager();
        SchuelerCode schuelerCodeA = getInitialSchuelerCode(emA);

        EntityManager emB = entityManagerFactory.createEntityManager();
        getInitialSchuelerCode(emB);

        String beschreibungNew = "emA";
        schuelerCodeA.setBeschreibung(beschreibungNew);
        emA.getTransaction().begin();
        emA.persist(schuelerCodeA);
        emA.getTransaction().commit();

        emA.close();

        // emB sieht Änderung von emA auch nach close von emA nicht
        SchuelerCode schuelerCodeB = getInitialSchuelerCode(emB);
        assertEquals(detachedInitialSchuelerCode.getBeschreibung(), schuelerCodeB.getBeschreibung());

        emB.clear();

        // auch nach clear von emB ist die Änderung nicht sichtbar
        schuelerCodeB = getInitialSchuelerCode(emB);
/* Folgendes Fehlverhalten tritt ab Hibernate 5.2.9. nicht mehr auf (daher auskommentiert):
        assertEquals(detachedInitialSchuelerCode.getBeschreibung(), schuelerCodeB.getBeschreibung());
*/
        // Ab Hibernate 5.2.9. korrektes Verhalten
        assertEquals(beschreibungNew, schuelerCodeB.getBeschreibung());

        emB.close();

        // erst nach close und create ist die Änderung sichtbar
        emB = entityManagerFactory.createEntityManager();
        schuelerCodeB = getInitialSchuelerCode(emB);
        assertEquals(beschreibungNew, schuelerCodeB.getBeschreibung());
        emB.close();

/* Folgendes Fehlverhalten tritt ab Hibernate 5.2.9. nicht mehr auf (daher auskommentiert):
        // nochmals mit neuem EntityManager, der aber komischerweise wieder den alten Wert liefert?!?
        emB = entityManagerFactory.createEntityManager();
        schuelerCodeB = getInitialSchuelerCode(emB);
        assertEquals(detachedInitialSchuelerCode.getBeschreibung(), schuelerCodeB.getBeschreibung());
        emB.close();

        // und nochmals mit neuem EntityManager, der dann wieder den neuen Wert liefert?!?
        emB = entityManagerFactory.createEntityManager();
        schuelerCodeB = getInitialSchuelerCode(emB);
        assertEquals(beschreibungNew, schuelerCodeB.getBeschreibung());
        emB.close();

        // neue Factory erzeugen
        entityManagerFactory = createEntityManagerFactory();
*/

        // mit neuem EntityManager von einer neuen Factory. Die Änderung ist erwartungsgemäss sichtbar.
        emB = entityManagerFactory.createEntityManager();
        schuelerCodeB = getInitialSchuelerCode(emB);
        assertEquals(beschreibungNew, schuelerCodeB.getBeschreibung());
        emB.close();

        // und nochmals mit neuem EntityManager. Die Änderung ist erwartungsgemäss sichtbar.
        emB = entityManagerFactory.createEntityManager();
        schuelerCodeB = getInitialSchuelerCode(emB);
        assertEquals(beschreibungNew, schuelerCodeB.getBeschreibung());
        emB.close();
    }

    @Test
    public void testVisibilityOfModificationInSecondEntityManager_afterImmediateCloseOfA_synchronize() {
        EntityManager emA = entityManagerFactory.createEntityManager();
        SchuelerCode schuelerCodeA = getInitialSchuelerCode(emA);

        EntityManager emB = entityManagerFactory.createEntityManager();
        getInitialSchuelerCode(emB);
        emB.getTransaction().begin(); // synchronize

        String beschreibungNew = "emA";
        schuelerCodeA.setBeschreibung(beschreibungNew);
        emA.getTransaction().begin();
        emA.persist(schuelerCodeA);
        emA.getTransaction().commit();

        emA.close();

        // emB sieht Änderung von emA auch nach close von emA nicht
        SchuelerCode schuelerCodeB = getInitialSchuelerCode(emB);
        assertEquals(detachedInitialSchuelerCode.getBeschreibung(), schuelerCodeB.getBeschreibung());

        emB.clear();

        // auch nach clear von emB ist die Änderung nicht sichtbar
        schuelerCodeB = getInitialSchuelerCode(emB);
/* Folgendes stimmt nicht ab Hibernate 5.2.9.
        assertEquals(detachedInitialSchuelerCode.getBeschreibung(), schuelerCodeB.getBeschreibung());
*/
///* Folgendes stimmt mit Hibernate 5.2.9.
        assertEquals(beschreibungNew, schuelerCodeB.getBeschreibung());
//*/

        emB.getTransaction().commit();
        emB.close();

        // erst nach close und neuem EntityManager ist die Änderung sichtbar
        emB = entityManagerFactory.createEntityManager();
        schuelerCodeB = getInitialSchuelerCode(emB);
        assertEquals(beschreibungNew, schuelerCodeB.getBeschreibung());
        emB.close();

        // auch beim zweiten Mal wird der korrekte Wert geliefert
        emB = entityManagerFactory.createEntityManager();
        schuelerCodeB = getInitialSchuelerCode(emB);
        assertEquals(beschreibungNew, schuelerCodeB.getBeschreibung());
        emB.close();
    }

    @Test
    public void testVisibilityOfModificationInSecondEntityManager_createEmBAfterCommit() {
        EntityManager emA = entityManagerFactory.createEntityManager();
        SchuelerCode schuelerCodeA = getInitialSchuelerCode(emA);

        String beschreibungNew = "emA";
        schuelerCodeA.setBeschreibung(beschreibungNew);
        emA.getTransaction().begin();
        emA.persist(schuelerCodeA);
        emA.getTransaction().commit();

        EntityManager emB = entityManagerFactory.createEntityManager();
        // emB sieht Änderung von emA bereits nach commit von emA, wenn emB erst nach dem commit erzeugt wird
        SchuelerCode schuelerCodeB = getInitialSchuelerCode(emB);
        assertEquals(beschreibungNew, schuelerCodeB.getBeschreibung());

        emA.close();

        emB.close();

        // und nochmals mit einem neuen EntityManager, der hier wieder korrekt den neuen Wert liefert
        emA = entityManagerFactory.createEntityManager();
        schuelerCodeB = getInitialSchuelerCode(emA);
        assertEquals(beschreibungNew, schuelerCodeB.getBeschreibung());
        emA.close();

        // und noch ein neuer EntityManager, der den korrekten neuen Wert liefert
        emB = entityManagerFactory.createEntityManager();
        schuelerCodeB = getInitialSchuelerCode(emB);
        assertEquals(beschreibungNew, schuelerCodeB.getBeschreibung());
        emB.close();

        // und noch einer
        emB = entityManagerFactory.createEntityManager();
        schuelerCodeB = getInitialSchuelerCode(emB);
        assertEquals(beschreibungNew, schuelerCodeB.getBeschreibung());
        emB.close();
    }

    @Test
    public void testVisibilityOfModificationInSecondEntityManager_refresh() {
        EntityManager emA = entityManagerFactory.createEntityManager();
        SchuelerCode schuelerCodeA = getInitialSchuelerCode(emA);

        EntityManager emB = entityManagerFactory.createEntityManager();
        getInitialSchuelerCode(emB);

        String beschreibungNew = "emA";
        schuelerCodeA.setBeschreibung(beschreibungNew);
        emA.getTransaction().begin();
        emA.persist(schuelerCodeA);
        emA.getTransaction().commit();

        emB.getTransaction().begin(); // synchronize
        // emB sieht Änderung von emA nicht
        SchuelerCode schuelerCodeB = getInitialSchuelerCode(emB);
        assertEquals(detachedInitialSchuelerCode.getBeschreibung(), schuelerCodeB.getBeschreibung());

        schuelerCodeB.setBeschreibung("xxx");
        emB.refresh(schuelerCodeB);

        // emB sieht Änderung von emA auch nach refresh nicht
/* Folgendes stimmt nicht ab Hibernate 5.2.9.
        assertEquals(detachedInitialSchuelerCode.getBeschreibung(), schuelerCodeB.getBeschreibung());
*/
///* Folgendes stimmt mit Hibernate 5.2.9.
        assertEquals(beschreibungNew, schuelerCodeB.getBeschreibung());
//*/

        emA.close();

        schuelerCodeB.setBeschreibung("xxx");
        emB.refresh(schuelerCodeB);

        // emB sieht Änderung von emA auch nach close von emA und refresh nicht
        // Dies widerspricht dem Kapitel 10.2.6. Refreshing data auf Seite 241 in Java Persistence with Hibernate Second Edition
        // Aber mit Repeatable Read lässt sich das erklären?
/* Folgendes stimmt nicht ab Hibernate 5.2.9.
        assertEquals(detachedInitialSchuelerCode.getBeschreibung(), schuelerCodeB.getBeschreibung());
*/
///* Folgendes stimmt mit Hibernate 5.2.9.
        assertEquals(beschreibungNew, schuelerCodeB.getBeschreibung());
//*/

        emB.getTransaction().commit();
        emB.close();

        // erst jetzt sieht emB die Änderung
        emB = entityManagerFactory.createEntityManager();
        schuelerCodeB = getInitialSchuelerCode(emB);
        assertEquals(beschreibungNew, schuelerCodeB.getBeschreibung());
        emB.close();

        // und auch beim 2. Mal klappt's
        emB = entityManagerFactory.createEntityManager();
        schuelerCodeB = getInitialSchuelerCode(emB);
        assertEquals(beschreibungNew, schuelerCodeB.getBeschreibung());
        emB.close();
    }

    @Test
    public void testBeginTransactionCloseEntityManager() {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        assertTrue(em.getTransaction().isActive());
        SchuelerCode schuelerCodeA = getInitialSchuelerCode(em);
        schuelerCodeA.setBeschreibung("Test Beschreibung");
        em.close();
        assertTrue(em.getTransaction().isActive());
        em.getTransaction().commit(); // commits transaction even when EntityManager is closed!
        em = entityManagerFactory.createEntityManager();
        schuelerCodeA = getInitialSchuelerCode(em);
        assertEquals("Test Beschreibung", schuelerCodeA.getBeschreibung());
        em.close();
    }

    @Test
    public void testBeginTransactionCloseEntityManagerNewEntityManager() {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        assertTrue(em.getTransaction().isActive());
        SchuelerCode schuelerCodeA = getInitialSchuelerCode(em);
        schuelerCodeA.setBeschreibung("Test Beschreibung");
        em.close();
        em = entityManagerFactory.createEntityManager();
        schuelerCodeA = getInitialSchuelerCode(em);
        assertEquals("Rollback expected", detachedInitialSchuelerCode.getBeschreibung(), schuelerCodeA.getBeschreibung());
        em.close();
    }

    @Test
    public void testBeginTransactionFlushAndCloseEntityManager() {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        assertTrue(em.getTransaction().isActive());
        SchuelerCode schuelerCodeA = getInitialSchuelerCode(em);
        schuelerCodeA.setBeschreibung("Test Beschreibung");
        em.flush();
        em.close();
        assertTrue(em.getTransaction().isActive());
        em.getTransaction().commit(); // commits transaction even when EntityManager is closed!
        em = entityManagerFactory.createEntityManager();
        schuelerCodeA = getInitialSchuelerCode(em);
        assertEquals("Test Beschreibung", schuelerCodeA.getBeschreibung());
        em.close();
    }

    @Test
    public void testBeginTransactionBeforeCommit() {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        assertTrue(em.getTransaction().isActive());
/*      This would result in a timeout exception trying to restart transaction and acquiring a lock.
        SchuelerCode schuelerCodeA = getInitialSchuelerCode(em);
        schuelerCodeA.setBeschreibung("Test Beschreibung");
        em.flush();
*/
        try {
            em.getTransaction().begin();
            fail("IllegalStateException expected");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Test
    public void testConcurrentUpdate() {
        sleepOneSecond(); // wegen Versionierung mit Timestamp-Problem.
        // Alternative:
        //-- Ergänzen des Timestamp um Millisekunden (Fraction). Ohne die Millisekunden kann es Probleme geben,
        //-- wenn zwei Prozesse in der gleichen Sekunde das gleiche Objekt updaten.
        //-- In der Praxis kommt das selten bis nie vor, oder?
        //-- alter table svm.Code MODIFY last_updated TIMESTAMP(3);
        EntityManager emA = entityManagerFactory.createEntityManager();
        SchuelerCode schuelerCodeA = getInitialSchuelerCode(emA);

        EntityManager emB = entityManagerFactory.createEntityManager();
        SchuelerCode schuelerCodeB = getInitialSchuelerCode(emB);

        schuelerCodeA.setBeschreibung("emA");
        emA.getTransaction().begin();
        emA.getTransaction().commit();
        emA.close();

        schuelerCodeB.setBeschreibung("emB");
        emB.getTransaction().begin();
        try {
            emB.getTransaction().commit();
            fail("javax.persistence.OptimisticLockException expected");
        } catch (Exception e) {
            e.printStackTrace(System.out);
        } finally {
            emB.close();
        }

        EntityManager emC = entityManagerFactory.createEntityManager();
        assertEquals("emA", getInitialSchuelerCode(emC).getBeschreibung());
        emC.close();
    }

    //------------------------------------------------------------------------------------------------------------------

    @Test
    public void testPersistenceState_unsynchronized() {
        EntityManager em = entityManagerFactory.createEntityManager();
        SchuelerCode schuelerCodeA = getInitialSchuelerCode(em);
        SchuelerCode schuelerCodeB = createSchuelerCode("xx", "xx");
        em.persist(schuelerCodeB);

        assertTrue("schuelerCodeA persisted expected", isPersisted(em, schuelerCodeA));
        assertTrue("schuelerCodeA persistent expected", isPersistent(em, schuelerCodeA));
        assertTrue("schuelerCodeA database persistent expected", isDatabasePersistent(schuelerCodeA));
        assertFalse("schuelerCodeA not detached expected", isDetached(em, schuelerCodeA));
        assertFalse("schuelerCodeA not transient expected", isTransient(em, schuelerCodeA));
        assertTrue("schuelerCodeB persisted expected", isPersisted(em, schuelerCodeB));
        assertTrue("schuelerCodeB persistent expected", isPersistent(em, schuelerCodeB));
        assertFalse("schuelerCodeB not database persistent expected", isDatabasePersistent(schuelerCodeB));
        assertFalse("schuelerCodeB not detached expected", isDetached(em, schuelerCodeB));
        assertFalse("schuelerCodeB not transient expected", isTransient(em, schuelerCodeB));

        em.clear();

        assertFalse("schuelerCodeA not persisted expected", isPersisted(em, schuelerCodeA));
        assertTrue("schuelerCodeA persistent expected", isPersistent(em, schuelerCodeA));
        assertTrue("schuelerCodeA database persistent expected", isDatabasePersistent(schuelerCodeA));
        assertTrue("schuelerCodeA detached expected", isDetached(em, schuelerCodeA));
        assertFalse("schuelerCodeA not transient expected", isTransient(em, schuelerCodeA));
        assertFalse("schuelerCodeB not persisted expected", isPersisted(em, schuelerCodeB));
        assertFalse("schuelerCodeB not persistent expected", isPersistent(em, schuelerCodeB));
        assertFalse("schuelerCodeB not database persistent expected", isDatabasePersistent(schuelerCodeB));
        assertFalse("schuelerCodeB not detached expected", isDetached(em, schuelerCodeB));
        assertTrue("schuelerCodeB transient expected", isTransient(em, schuelerCodeB));

        em.close();

        em = entityManagerFactory.createEntityManager();
        SchuelerCode schuelerCode = getSchuelerCodeByKuerzel(em, "xx");
        assertNull("SchuelerCode not expected to be persistent.", schuelerCode);

        em.close();
    }

    @Test
    public void testPersistenceState_synchronized() {
        EntityManager em = entityManagerFactory.createEntityManager();
        SchuelerCode schuelerCodeA = getInitialSchuelerCode(em);
        SchuelerCode schuelerCodeB = createSchuelerCode("xx", "xx");
        em.persist(schuelerCodeB);

        em.getTransaction().begin();

        assertTrue("schuelerCodeA persisted expected", isPersisted(em, schuelerCodeA));
        assertTrue("schuelerCodeA persistent expected", isPersistent(em, schuelerCodeA));
        assertTrue("schuelerCodeA database persistent expected", isDatabasePersistent(schuelerCodeA));
        assertFalse("schuelerCodeA not detached expected", isDetached(em, schuelerCodeA));
        assertFalse("schuelerCodeA not transient expected", isTransient(em, schuelerCodeA));
        assertTrue("schuelerCodeB persisted expected", isPersisted(em, schuelerCodeB));
        assertTrue("schuelerCodeB persistent expected", isPersistent(em, schuelerCodeB));
        assertFalse("schuelerCodeB not database persistent expected", isDatabasePersistent(schuelerCodeB));
        assertFalse("schuelerCodeB not detached expected", isDetached(em, schuelerCodeB));
        assertFalse("schuelerCodeB not transient expected", isTransient(em, schuelerCodeB));

        em.getTransaction().commit();

        assertTrue("schuelerCodeA persisted expected", isPersisted(em, schuelerCodeA));
        assertTrue("schuelerCodeA persistent expected", isPersistent(em, schuelerCodeA));
        assertTrue("schuelerCodeA database persistent expected", isDatabasePersistent(schuelerCodeA));
        assertFalse("schuelerCodeA not detached expected", isDetached(em, schuelerCodeA));
        assertFalse("schuelerCodeA not transient expected", isTransient(em, schuelerCodeA));
        assertTrue("schuelerCodeB persisted expected", isPersisted(em, schuelerCodeB));
        assertTrue("schuelerCodeB persistent expected", isPersistent(em, schuelerCodeB));
        assertTrue("schuelerCodeB database persistent expected", isDatabasePersistent(schuelerCodeB));
        assertFalse("schuelerCodeB not detached expected", isDetached(em, schuelerCodeB));
        assertFalse("schuelerCodeB not transient expected", isTransient(em, schuelerCodeB));

        em.close();
    }

    private boolean isTransient(EntityManager em, SchuelerCode schuelerCode) {
        return  !isPersisted(em, schuelerCode) && !isDatabasePersistent(schuelerCode);
    }

    private boolean isDetached(EntityManager em, SchuelerCode schuelerCode) {
        return !isPersisted(em, schuelerCode) && isDatabasePersistent(schuelerCode);
    }

    private boolean isPersistent(EntityManager em, SchuelerCode schuelerCode) {
        return isDatabasePersistent(schuelerCode) || isPersisted(em, schuelerCode);
    }

    private boolean isDatabasePersistent(SchuelerCode schuelerCode) {
        return getIdentifier(schuelerCode) != null;
    }

    private boolean isPersisted(EntityManager em, SchuelerCode schuelerCode) {
        return em.contains(schuelerCode);
    }

    private Object getIdentifier(SchuelerCode schuelerCode) {
        return entityManagerFactory.getPersistenceUnitUtil().getIdentifier(schuelerCode);
    }

    //------------------------------------------------------------------------------------------------------------------

    private SchuelerCode createSchuelerCode(String kuerzel, String beschreibung) {
        SchuelerCode schuelerCode = new SchuelerCode(kuerzel, beschreibung, false);
        createdCodes.add(schuelerCode.getKuerzel());
        return schuelerCode;
    }

    private SchuelerCode getInitialSchuelerCode(EntityManager em) {
        return em.find(SchuelerCode.class, detachedInitialSchuelerCode.getCodeId());
    }

    private SchuelerCode getSchuelerCodeByKuerzel(EntityManager em, String kuerzel) {
        List<SchuelerCode> resultList = getSchuelerCodesByKuerzel(em, kuerzel);
        if (resultList.size() == 1) {
            return resultList.get(0);
        } else if (resultList.isEmpty()) {
            return null;
        }
        throw new RuntimeException("One result expected but found " + resultList.size() + " looking up SchuelerCode with Kuerzel " + kuerzel);
    }

    private List<SchuelerCode> getSchuelerCodesByKuerzel(EntityManager em, String kuerzel) {
        TypedQuery<SchuelerCode> typedQuery = em.createQuery("select c from SchuelerCode c where kuerzel = :kuerzel", SchuelerCode.class);
        typedQuery.setParameter("kuerzel", kuerzel);
        return typedQuery.getResultList();
    }

    @SuppressWarnings("unused")
    private void sleepOneSecond() {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}