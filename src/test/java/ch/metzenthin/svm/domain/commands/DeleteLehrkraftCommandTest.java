package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Anrede;
import ch.metzenthin.svm.common.utils.PersistenceProperties;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Lehrkraft;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * @author Martin Schraner
 */
public class DeleteLehrkraftCommandTest {

    private CommandInvoker commandInvoker = new CommandInvokerImpl();
    private EntityManagerFactory entityManagerFactory;

    @Before
    public void setUp() throws Exception {
        createSvmPropertiesFileDefault();
        entityManagerFactory = Persistence.createEntityManagerFactory("svm", PersistenceProperties.getPersistenceProperties());
        commandInvoker.openSession();
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

        List<Lehrkraft> lehrkraefteSaved = new ArrayList<>();

        // 2 Lehrkräfte erfassen
        Lehrkraft lehrkraft1 = new Lehrkraft(Anrede.FRAU, "Noémie", "RoosTest", new GregorianCalendar(1994, Calendar.MARCH, 18), "043 111 11 11", "076 384 45 35", "nroos@gmx.ch", "756.3943.8722.22", "Mi, Fr, Sa", true);
        Adresse adresse1 = new Adresse("Rebwiesenstrasse", "77", "8702", "Zollikon");
        lehrkraft1.setAdresse(adresse1);

        Lehrkraft lehrkraft2 = new Lehrkraft(Anrede.FRAU, "Nathalie", "DelleyTest", new GregorianCalendar(1971, Calendar.DECEMBER, 16), "044 261 27 20", "076 338 05 36", "ndelley@sunrise.ch", "756.8274.3263.17", "Mi, Fr, Sa", true);
        Adresse adresse2 = new Adresse("Im Schilf", "7", "8044", "Zürich");
        lehrkraft2.setAdresse(adresse2);

        SaveOrUpdateLehrkraftCommand saveOrUpdateLehrkraftCommand = new SaveOrUpdateLehrkraftCommand(lehrkraft1, adresse1, null, lehrkraefteSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateLehrkraftCommand);

        saveOrUpdateLehrkraftCommand = new SaveOrUpdateLehrkraftCommand(lehrkraft2, adresse2, null, lehrkraefteSaved);
        commandInvoker.executeCommandAsTransaction(saveOrUpdateLehrkraftCommand);

        // Lehrkraefte löschen
        DeleteLehrkraftCommand deleteLehrkraftCommand = new DeleteLehrkraftCommand(lehrkraefteSaved, 1);
        commandInvoker.executeCommandAsTransaction(deleteLehrkraftCommand);
        assertEquals(DeleteLehrkraftCommand.Result.LOESCHEN_ERFOLGREICH, deleteLehrkraftCommand.getResult());
        assertEquals(1, lehrkraefteSaved.size());

        deleteLehrkraftCommand = new DeleteLehrkraftCommand(lehrkraefteSaved, 0);
        commandInvoker.executeCommandAsTransaction(deleteLehrkraftCommand);
        assertEquals(DeleteLehrkraftCommand.Result.LOESCHEN_ERFOLGREICH, deleteLehrkraftCommand.getResult());
        assertTrue(lehrkraefteSaved.isEmpty());

    }
}