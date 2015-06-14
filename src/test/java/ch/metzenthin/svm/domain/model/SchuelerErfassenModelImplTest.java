package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import org.junit.Before;
import org.junit.Test;
import test.DummyCommandInvoker;
import test.TestCompletedListener;

import static org.junit.Assert.*;

/**
 * @author Hans Stamm
 */
public class SchuelerErfassenModelImplTest {

    private static final CommandInvoker DUMMY_COMMAND_INVOKER = new DummyCommandInvoker();

    private SchuelerErfassenModel schuelerErfassenModel;
    private SchuelerModel schuelerModel;
    private AngehoerigerModel mutterModel;

    @Before
    public void setUp() throws Exception {
        schuelerErfassenModel = new SchuelerErfassenModelImpl(DUMMY_COMMAND_INVOKER);
        schuelerModel = new SchuelerModelImpl(DUMMY_COMMAND_INVOKER);
        schuelerErfassenModel.setSchuelerModel(schuelerModel);
        mutterModel = new AngehoerigerModelImpl(DUMMY_COMMAND_INVOKER);
        schuelerErfassenModel.setMutterModel(mutterModel);
        AngehoerigerModel vaterModel = new AngehoerigerModelImpl(DUMMY_COMMAND_INVOKER);
        schuelerErfassenModel.setVaterModel(vaterModel);
        AngehoerigerModel drittempfaengerModel = new AngehoerigerModelImpl(DUMMY_COMMAND_INVOKER);
        schuelerErfassenModel.setDrittempfaengerModel(drittempfaengerModel);
    }

    @Test
    public void testIsCompleted_false() {
        TestCompletedListener testCompletedListener = new TestCompletedListener();
        schuelerErfassenModel.addCompletedListener(testCompletedListener);
        schuelerErfassenModel.initializeCompleted();
        assertFalse("IsCompleted false erwartet", schuelerErfassenModel.isCompleted());
        assertEquals("Aufruf CompletedListener 4x erwartet", 4, testCompletedListener.getCounter());
        assertFalse("IsCompleted von CompletedListener false erwartet", testCompletedListener.isCompleted());
    }

    @Test
    public void testIsCompleted_true() {
        TestCompletedListener testCompletedListener = new TestCompletedListener();
        schuelerErfassenModel.addCompletedListener(testCompletedListener);
        schuelerErfassenModel.initializeCompleted();
        try {
            schuelerModel.setNachname("Nachname");
            schuelerModel.setVorname("Vorname");
            schuelerModel.setStrasse("Strasse");
            schuelerModel.setPlz("Plzz");
            schuelerModel.setOrt("Ort");
            schuelerModel.validate();
            mutterModel.setIsRechnungsempfaenger(true);
            try {
                mutterModel.validate();
                fail("Exception erwarten");
            } catch (SvmValidationException e) {
                e.printStackTrace();
            }
            mutterModel.setNachname("Nachname");
            mutterModel.setVorname("Vorname");
            mutterModel.setStrasse("Strasse");
            mutterModel.setPlz("Plzz");
            mutterModel.setOrt("Ort");
            mutterModel.validate();
        } catch (SvmValidationException e) {
            e.printStackTrace();
            fail("Keine Exception erwartet");
        }
        assertTrue("IsCompleted true erwartet", schuelerErfassenModel.isCompleted());
        assertTrue("IsCompleted von CompletedListener true erwartet", testCompletedListener.isCompleted());
    }
}