package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.dataTypes.Geschlecht;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.Command;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.GenericDaoCommand;
import ch.metzenthin.svm.persistence.SvmDbException;
import ch.metzenthin.svm.ui.control.CompletedListener;
import org.junit.Before;
import org.junit.Test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Hans Stamm
 */
public class SchuelerModelImplTest {

    private static final CommandInvoker DUMMY_COMMAND_INVOKER = new CommandInvoker() {
        @Override
        public Command executeCommand(Command c) {
            return null;
        }
        @Override
        public GenericDaoCommand executeCommand(GenericDaoCommand genericDaoCommand) throws SvmDbException {
            return null;
        }
    };

    private SchuelerModel schuelerModel;

    @Before
    public void setUp() {
        schuelerModel = new SchuelerModelImpl(DUMMY_COMMAND_INVOKER);
    }

    @Test
    public void testSetGeschlecht() {
        schuelerModel.setGeschlecht(Geschlecht.M);
        assertEquals("Geschlecht.M erwartet", Geschlecht.M, schuelerModel.getGeschlecht());
    }

    @Test
    public void testSetGeschlecht_Null() {
        schuelerModel.setGeschlecht(null);
        assertNull("Geschlecht null erwartet", schuelerModel.getGeschlecht());
    }

    @Test
    public void testSetGeschlecht_NullAgain() {
        schuelerModel.setGeschlecht(Geschlecht.M);
        schuelerModel.setGeschlecht(null);
        assertNull("Geschlecht null erwartet", schuelerModel.getGeschlecht());
    }

    @Test
    public void testSetGeschlecht_PropertyChangeOneEvent() {
        TestPropertyChangeListener listener = new TestPropertyChangeListener();
        schuelerModel.addPropertyChangeListener(listener);
        schuelerModel.setGeschlecht(Geschlecht.M);
        assertEquals("Ein Event erwartet", 1, listener.eventsSize());
    }

    @Test
    public void testSetGeschlecht_PropertyChangeNoEvent() {
        schuelerModel.setGeschlecht(Geschlecht.M);
        TestPropertyChangeListener listener = new TestPropertyChangeListener();
        schuelerModel.addPropertyChangeListener(listener);
        schuelerModel.setGeschlecht(Geschlecht.M);
        assertEquals("Kein Event erwartet", 0, listener.eventsSize());
    }

    @Test
    public void testSetAnmeldedatum() {
        try {
            schuelerModel.setAnmeldedatum("12.06.2015");
        } catch (SvmValidationException e) {
            e.printStackTrace(System.err);
            fail("Keine Exception erwartet");
        }
    }

    @Test
    public void testSetAnmeldedatum_BadFormatNoException() {
        try {
            schuelerModel.setAnmeldedatum("12.16.2015");
        } catch (SvmValidationException e) {
            e.printStackTrace(System.err);
            fail("Keine Exception erwartet");
        }
    }

    @Test
    public void testSetAnmeldedatum_BadFormatException() {
        try {
            schuelerModel.setAnmeldedatum("2015-06-12");
            fail("Exception erwartet");
        } catch (SvmValidationException e) {
            e.printStackTrace(System.out);
        }
    }

    @Test
    public void testGetSchueler() {
        assertNotNull("Schueler nicht null erwartet", schuelerModel.getSchueler());
    }

    @Test
    public void testGetAdresse() {
        assertNotNull("Adresse nicht null erwartet", schuelerModel.getAdresse());
    }

    @Test
    public void testIsCompleted() {
        TestCompletedListener testCompletedListener = new TestCompletedListener();
        schuelerModel.addCompletedListener(testCompletedListener);
        try {
            schuelerModel.setNachname("Nachname");
            schuelerModel.setVorname("Vorname");
            schuelerModel.setStrasse("Strasse");
            schuelerModel.setPlz("Plz");
            schuelerModel.setOrt("Ort");
        } catch (SvmValidationException e) {
            e.printStackTrace(System.err);
            fail("Keine Exception erwartet");
        }
        assertTrue("IsCompleted true erwartet", schuelerModel.isCompleted());
        try {
            schuelerModel.validate();
        } catch (SvmValidationException e) {
            e.printStackTrace();
            fail("Keine Exception erwartet");
        }
        assertEquals("Aufruf von CompletedListener einmal erwartet", 1, testCompletedListener.counter);
    }

    @Test
    public void testIsCompleted_False() {
        assertFalse("IsCompleted false erwartet", schuelerModel.isCompleted());
    }

    //------------------------------------------------------------------------------------------------------------------

    private class TestPropertyChangeListener implements PropertyChangeListener {
        private List<PropertyChangeEvent> events = new ArrayList<>();
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            events.add(evt);
        }
        private int eventsSize() {
            return events.size();
        }
    }

    private class TestCompletedListener implements CompletedListener {
        private int counter;
        @Override
        public void completed(boolean completed) {
            counter++;
        }
    }
}
