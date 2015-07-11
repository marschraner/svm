package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.entities.Dispensation;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Martin Schraner
 */
public class CheckDispensationUeberlapptAndereDispensationenCommandTest {

    private CommandInvoker commandInvoker = new CommandInvokerImpl();
    private List<Dispensation> bereitsErfassteDispensationen = new ArrayList<>();

    @Before
    public void setUp() {
        bereitsErfassteDispensationen.add(new Dispensation(new GregorianCalendar(2010, Calendar.JULY, 1), new GregorianCalendar(2010, Calendar.AUGUST, 31), null, null));
        bereitsErfassteDispensationen.add(new Dispensation(new GregorianCalendar(2011, Calendar.JANUARY, 1), new GregorianCalendar(2011, Calendar.AUGUST, 31), null, null));
        bereitsErfassteDispensationen.add(new Dispensation(new GregorianCalendar(2015, Calendar.JULY, 1), null, null, null));
    }

    @Test
    public void testExecute_NichtUeberlappend() throws Exception {
        Dispensation dispensation = new Dispensation(new GregorianCalendar(2010, Calendar.SEPTEMBER, 1), new GregorianCalendar(2010, Calendar.DECEMBER, 31), null, null);
        CheckDispensationUeberlapptAndereDispensationenCommand checkDispensationUeberlapptAndereDispensationenCommand = new CheckDispensationUeberlapptAndereDispensationenCommand(dispensation, null, bereitsErfassteDispensationen);
        commandInvoker.executeCommand(checkDispensationUeberlapptAndereDispensationenCommand);
        assertFalse(checkDispensationUeberlapptAndereDispensationenCommand.isUeberlappt());
    }

    @Test
    public void testExecute_UeberlappendAmEnde() throws Exception {
        Dispensation dispensation = new Dispensation(new GregorianCalendar(2010, Calendar.SEPTEMBER, 1), new GregorianCalendar(2011, Calendar.JANUARY, 2), null, null);
        CheckDispensationUeberlapptAndereDispensationenCommand checkDispensationUeberlapptAndereDispensationenCommand = new CheckDispensationUeberlapptAndereDispensationenCommand(dispensation, null, bereitsErfassteDispensationen);
        commandInvoker.executeCommand(checkDispensationUeberlapptAndereDispensationenCommand);
        assertTrue(checkDispensationUeberlapptAndereDispensationenCommand.isUeberlappt());
    }

    @Test
    public void testExecute_UeberlappendAmAnfang() throws Exception {
        Dispensation dispensation = new Dispensation(new GregorianCalendar(2010, Calendar.AUGUST, 30), new GregorianCalendar(2010, Calendar.DECEMBER, 31), null, null);
        CheckDispensationUeberlapptAndereDispensationenCommand checkDispensationUeberlapptAndereDispensationenCommand = new CheckDispensationUeberlapptAndereDispensationenCommand(dispensation, null, bereitsErfassteDispensationen);
        commandInvoker.executeCommand(checkDispensationUeberlapptAndereDispensationenCommand);
        assertTrue(checkDispensationUeberlapptAndereDispensationenCommand.isUeberlappt());
    }

    @Test
    public void testExecute_EndeIdentischMitBereitsErfasstemAnfang() throws Exception {
        Dispensation dispensation = new Dispensation(new GregorianCalendar(2010, Calendar.SEPTEMBER, 1), new GregorianCalendar(2011, Calendar.JANUARY, 1), null, null);
        CheckDispensationUeberlapptAndereDispensationenCommand checkDispensationUeberlapptAndereDispensationenCommand = new CheckDispensationUeberlapptAndereDispensationenCommand(dispensation, null, bereitsErfassteDispensationen);
        commandInvoker.executeCommand(checkDispensationUeberlapptAndereDispensationenCommand);
        assertTrue(checkDispensationUeberlapptAndereDispensationenCommand.isUeberlappt());
    }

    @Test
    public void testExecute_AnfangIdentischMitBereitsErfasstemEnde() throws Exception {
        Dispensation dispensation = new Dispensation(new GregorianCalendar(2010, Calendar.AUGUST, 31), new GregorianCalendar(2010, Calendar.DECEMBER, 31), null, null);
        CheckDispensationUeberlapptAndereDispensationenCommand checkDispensationUeberlapptAndereDispensationenCommand = new CheckDispensationUeberlapptAndereDispensationenCommand(dispensation, null, bereitsErfassteDispensationen);
        commandInvoker.executeCommand(checkDispensationUeberlapptAndereDispensationenCommand);
        assertTrue(checkDispensationUeberlapptAndereDispensationenCommand.isUeberlappt());
    }

    @Test
    public void testExecute_AlleUeberlappend() throws Exception {
        Dispensation dispensation = new Dispensation(new GregorianCalendar(2008, Calendar.AUGUST, 31), new GregorianCalendar(2015, Calendar.DECEMBER, 31), null, null);
        CheckDispensationUeberlapptAndereDispensationenCommand checkDispensationUeberlapptAndereDispensationenCommand = new CheckDispensationUeberlapptAndereDispensationenCommand(dispensation, null, bereitsErfassteDispensationen);
        commandInvoker.executeCommand(checkDispensationUeberlapptAndereDispensationenCommand);
        assertTrue(checkDispensationUeberlapptAndereDispensationenCommand.isUeberlappt());
    }

    @Test
    public void testExecute_NachHintenOffenePeriodeUeberlappend() throws Exception {
        Dispensation dispensation = new Dispensation(new GregorianCalendar(2015, Calendar.AUGUST, 31), null, null, null);
        CheckDispensationUeberlapptAndereDispensationenCommand checkDispensationUeberlapptAndereDispensationenCommand = new CheckDispensationUeberlapptAndereDispensationenCommand(dispensation, null, bereitsErfassteDispensationen);
        commandInvoker.executeCommand(checkDispensationUeberlapptAndereDispensationenCommand);
        assertTrue(checkDispensationUeberlapptAndereDispensationenCommand.isUeberlappt());
    }

    @Test
    public void testExecute_DispensationOrig() throws Exception {
        Dispensation dispensation = new Dispensation(new GregorianCalendar(2010, Calendar.JULY, 2), new GregorianCalendar(2010, Calendar.AUGUST, 30), null, null);
        Dispensation dispensationOrig = new Dispensation(new GregorianCalendar(2010, Calendar.JULY, 1), new GregorianCalendar(2010, Calendar.AUGUST, 31), null, null);
        CheckDispensationUeberlapptAndereDispensationenCommand checkDispensationUeberlapptAndereDispensationenCommand = new CheckDispensationUeberlapptAndereDispensationenCommand(dispensation, dispensationOrig, bereitsErfassteDispensationen);
        commandInvoker.executeCommand(checkDispensationUeberlapptAndereDispensationenCommand);
        assertFalse(checkDispensationUeberlapptAndereDispensationenCommand.isUeberlappt());
    }
}