package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Anrede;
import ch.metzenthin.svm.common.dataTypes.Elternmithilfe;
import ch.metzenthin.svm.common.dataTypes.Geschlecht;
import ch.metzenthin.svm.common.dataTypes.Gruppe;
import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.daos.ElternmithilfeCodeDao;
import ch.metzenthin.svm.persistence.daos.MaerchenDao;
import ch.metzenthin.svm.persistence.daos.MaercheneinteilungDao;
import ch.metzenthin.svm.persistence.daos.SchuelerDao;
import ch.metzenthin.svm.persistence.entities.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;
import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Martin Schraner
 */
public class SaveOrUpdateMaercheneinteilungCommandTest {

    private final MaercheneinteilungDao maercheneinteilungDao = new MaercheneinteilungDao();
    private final MaerchenDao maerchenDao = new MaerchenDao();
    private final ElternmithilfeCodeDao elternmithilfeCodeDao = new ElternmithilfeCodeDao();
    private final SchuelerDao schuelerDao = new SchuelerDao();

    private DB db;
    private CommandInvoker commandInvoker;

    @Before
    public void setUp() throws Exception {
        createSvmPropertiesFileDefault();
        db = DBFactory.getInstance();
        commandInvoker = new CommandInvokerImpl();
    }

    @After
    public void tearDown() throws Exception {
        db.closeSession();
    }

    @Test
    public void testExecute() throws Exception {

        List<Maerchen> erfassteMaerchen = new ArrayList<>();
        List<ElternmithilfeCode> erfassteElternmithilfeCodes = new ArrayList<>();
        List<Maercheneinteilung> erfassteMaercheneinteilungen = new ArrayList<>();

        // Schüler, Maerchen und ElternmithilfeCode erzeugen
        Schueler schueler1 = new Schueler("Jana", "Rösle", new GregorianCalendar(2012, Calendar.JULY, 24), "044 491 69 33", null, null, Geschlecht.W, "Schwester von Valentin");
        Adresse adresse1 = new Adresse("Hohenklingenstrasse", "15", "8049", "Zürich");
        schueler1.setAdresse(adresse1);
        schueler1.addAnmeldung(new Anmeldung(new GregorianCalendar(2015, Calendar.JANUARY, 1), null));
        Angehoeriger vater = new Angehoeriger(Anrede.HERR, "Eugen", "Rösle", "044 491 69 33", null, null);
        vater.setAdresse(adresse1);
        schueler1.setVater(vater);
        schueler1.setRechnungsempfaenger(vater);
        SaveSchuelerCommand saveSchuelerCommand = new SaveSchuelerCommand(schueler1);
        commandInvoker.executeCommandAsTransaction(saveSchuelerCommand);

        Schueler schueler2 = new Schueler("Hanna", "Hasler", new GregorianCalendar(2010, Calendar.JULY, 24), "044 422 69 33", null, null, Geschlecht.W, null);
        Adresse adresse2 = new Adresse("Hohenklingenstrasse", "22", "8049", "Zürich");
        schueler2.setAdresse(adresse2);
        schueler2.addAnmeldung(new Anmeldung(new GregorianCalendar(2013, Calendar.JANUARY, 1), null));
        Angehoeriger mutter2 = new Angehoeriger(Anrede.FRAU, "Adriana", "Hasler", "044 422 69 33", null, null);
        mutter2.setAdresse(adresse2);
        schueler2.setMutter(mutter2);
        schueler2.setRechnungsempfaenger(mutter2);
        saveSchuelerCommand = new SaveSchuelerCommand(schueler2);
        commandInvoker.executeCommandAsTransaction(saveSchuelerCommand);

        Maerchen maerchen1 = new Maerchen("1911/1912", "Scheewittchen", 7);
        SaveOrUpdateMaerchenCommand saveOrUpdateMaerchenCommand = new SaveOrUpdateMaerchenCommand(maerchen1, null, erfassteMaerchen);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateMaerchenCommand);
        Maerchen maerchen2 = new Maerchen("1912/1913", "Hans im Glück", 8);
        saveOrUpdateMaerchenCommand = new SaveOrUpdateMaerchenCommand(maerchen2, null, erfassteMaerchen);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateMaerchenCommand);

        ElternmithilfeCode elternmithilfeCode1 = new ElternmithilfeCode("tf", "Frisieren Test1", true);
        SaveOrUpdateElternmithilfeCodeCommand saveOrUpdateElternmithilfeCodeCommand = new SaveOrUpdateElternmithilfeCodeCommand(elternmithilfeCode1, null, erfassteElternmithilfeCodes);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateElternmithilfeCodeCommand);
        ElternmithilfeCode elternmithilfeCode2 = new ElternmithilfeCode("tk", "Kuchen Test2", true);
        saveOrUpdateElternmithilfeCodeCommand = new SaveOrUpdateElternmithilfeCodeCommand(elternmithilfeCode2, null, erfassteElternmithilfeCodes);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateElternmithilfeCodeCommand);

        ElternmithilfeDrittperson elternmithilfeDrittperson = new ElternmithilfeDrittperson(Anrede.HERR, "Hans", "Schraner", "044 720 85 51", null, "hschraner@bluewin.ch");
        Adresse adresseElternmithilfeDrittperson = new Adresse("Hintere Bergstrasse", "15", "8942", "Oberrieden");
        elternmithilfeDrittperson.setAdresse(adresseElternmithilfeDrittperson);

        assertFalse(checkIfMaercheneinteilungAvailable(schueler1, maerchen1, Gruppe.A, Elternmithilfe.VATER, elternmithilfeCode1, "Komödiant 1", "1, 2", elternmithilfeDrittperson));
        assertFalse(checkIfMaercheneinteilungAvailable(schueler2, maerchen2, Gruppe.B, Elternmithilfe.MUTTER, elternmithilfeCode2, "Komödiant 2", "1, 2", null));
        
        // 1. Maercheneinteilung erfassen
        Maercheneinteilung maercheneinteilung1 = new Maercheneinteilung(schueler1, maerchen1, Gruppe.A, "Komödiant 1", "1, 2", "Hase 1", "2, 3", "Frosch 1", "3, 4", Elternmithilfe.VATER,
                true, true, true, false, false, false, false, false, false, null, null);
        SaveOrUpdateMaercheneinteilungCommand saveOrUpdateMaercheneinteilungCommand = new SaveOrUpdateMaercheneinteilungCommand(maercheneinteilung1, elternmithilfeCode1, elternmithilfeDrittperson, adresseElternmithilfeDrittperson, null, erfassteMaercheneinteilungen);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateMaercheneinteilungCommand);

        assertTrue(checkIfMaercheneinteilungAvailable(schueler1, maerchen1, Gruppe.A, Elternmithilfe.VATER, elternmithilfeCode1, "Komödiant 1", "1, 2", elternmithilfeDrittperson));

        // 2. Maercheneinteilung erfassen
        Maercheneinteilung maercheneinteilung2 = new Maercheneinteilung(schueler2, maerchen2, Gruppe.B, "Komödiant 2", "1, 2", "Hase 2", "2, 3", "Frosch 2", "3, 4", Elternmithilfe.MUTTER,
                true, true, true, false, false, false, false, false, false, null, null);
        saveOrUpdateMaercheneinteilungCommand = new SaveOrUpdateMaercheneinteilungCommand(maercheneinteilung2, elternmithilfeCode2, null, null, null, erfassteMaercheneinteilungen);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateMaercheneinteilungCommand);

        assertTrue(checkIfMaercheneinteilungAvailable(schueler2, maerchen2, Gruppe.B, Elternmithilfe.MUTTER, elternmithilfeCode2, "Komödiant 2", "1, 2", null));

        // 2. Maercheneinteilung bearbeiten
        Maercheneinteilung maercheneinteilung2Modif = new Maercheneinteilung(schueler2, maerchen2, Gruppe.A, "Pilz 2", "2, 7", "Hase 2", "2, 3", "Frosch 2", "3, 4", Elternmithilfe.VATER,
                true, true, true, false, false, false, false, false, false, null, null);
        saveOrUpdateMaercheneinteilungCommand = new SaveOrUpdateMaercheneinteilungCommand(maercheneinteilung2Modif, elternmithilfeCode1, null, null, maercheneinteilung2, erfassteMaercheneinteilungen);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateMaercheneinteilungCommand);

        assertFalse(checkIfMaercheneinteilungAvailable(schueler2, maerchen2, Gruppe.B, Elternmithilfe.MUTTER, elternmithilfeCode2, "Komödiant 2", "1, 2", null));
        assertTrue(checkIfMaercheneinteilungAvailable(schueler2, maerchen2, Gruppe.A, Elternmithilfe.VATER, elternmithilfeCode1, "Pilz 2", "2, 7", null));

        // Testdaten löschen
        EntityManager entityManager = db.getCurrentEntityManager();
        entityManager.getTransaction().begin();
        for (Maercheneinteilung maercheneinteilung : erfassteMaercheneinteilungen) {
            Maercheneinteilung maercheneinteilungToBeDeleted = maercheneinteilungDao.findById(new MaercheneinteilungId(maercheneinteilung.getSchueler().getPersonId(), maercheneinteilung.getMaerchen().getMaerchenId()));
            if (maercheneinteilungToBeDeleted != null) {
                maercheneinteilungDao.remove(maercheneinteilungToBeDeleted);
            }
        }
        entityManager.getTransaction().commit();
        entityManager.getTransaction().begin();
        for (Maerchen maerchen : erfassteMaerchen) {
            Maerchen maerchenToBeDeleted = maerchenDao.findById(maerchen.getMaerchenId());
            if (maerchenToBeDeleted != null) {
                maerchenDao.remove(maerchenToBeDeleted);
            }
        }
        entityManager.getTransaction().commit();
        entityManager.getTransaction().begin();
        for (ElternmithilfeCode elternmithilfeCode : erfassteElternmithilfeCodes) {
            ElternmithilfeCode elternmithilfeCodeToBeDeleted = elternmithilfeCodeDao.findById(elternmithilfeCode.getCodeId());
            if (elternmithilfeCodeToBeDeleted != null) {
                elternmithilfeCodeDao.remove(elternmithilfeCodeToBeDeleted);
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

    private boolean checkIfMaercheneinteilungAvailable(Schueler schueler, Maerchen maerchen, Gruppe gruppe, Elternmithilfe elternmithilfe, ElternmithilfeCode elternmithilfeCode, String rolle1, String bilderRolle1, ElternmithilfeDrittperson elternmithilfeDrittperson) {
        FindMaercheneinteilungenSchuelerCommand findMaercheneinteilungenSchuelerCommand = new FindMaercheneinteilungenSchuelerCommand(schueler);
        commandInvoker.executeCommandAsTransaction(findMaercheneinteilungenSchuelerCommand);
        List<Maercheneinteilung> maercheneinteilungenSchueler = findMaercheneinteilungenSchuelerCommand.getMaercheneinteilungenFound();
        for (Maercheneinteilung maercheneinteilung : maercheneinteilungenSchueler) {
            if (maercheneinteilung.getMaerchen().equals(maerchen)
                    && maercheneinteilung.getGruppe().equals(gruppe)
                    && maercheneinteilung.getElternmithilfe().equals(elternmithilfe)
                    && maercheneinteilung.getElternmithilfeCode().equals(elternmithilfeCode)
                    && maercheneinteilung.getRolle1().equals(rolle1)
                    && maercheneinteilung.getBilderRolle1().equals(bilderRolle1)
                    && ((maercheneinteilung.getElternmithilfeDrittperson() == null && elternmithilfeDrittperson == null)
                    || (maercheneinteilung.getElternmithilfeDrittperson() != null && elternmithilfeDrittperson != null && maercheneinteilung.getElternmithilfeDrittperson().isIdenticalWith(elternmithilfeDrittperson)))) {
                return true;
            }
        }
        return false;
    }

}