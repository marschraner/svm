package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Anrede;
import ch.metzenthin.svm.common.dataTypes.Semesterbezeichnung;
import ch.metzenthin.svm.common.dataTypes.Wochentag;
import ch.metzenthin.svm.persistence.entities.*;
import org.junit.Before;
import org.junit.Test;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Martin Schraner
 */
public class CheckKursBereitsErfasstCommandTest {

    private final CommandInvoker commandInvoker = new CommandInvokerImpl();
    private final List<Kurs> bereitsErfassteKurse = new ArrayList<>();
    private final Semester semester1 = new Semester("2011/2012", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(2011, Calendar.AUGUST, 20), new GregorianCalendar(2012, Calendar.FEBRUARY, 10), new GregorianCalendar(2011, Calendar.OCTOBER, 5), new GregorianCalendar(2011, Calendar.OCTOBER, 17), new GregorianCalendar(2011, Calendar.DECEMBER, 21), new GregorianCalendar(2012, Calendar.JANUARY, 2));
    private final Semester semester2 = new Semester("2012/2013", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(2012, Calendar.AUGUST, 20), new GregorianCalendar(2013, Calendar.FEBRUARY, 10), new GregorianCalendar(2012, Calendar.OCTOBER, 5), new GregorianCalendar(2012, Calendar.OCTOBER, 17), new GregorianCalendar(2012, Calendar.DECEMBER, 21), new GregorianCalendar(2013, Calendar.JANUARY, 2));
    private final Kurstyp kurstyp1 = new Kurstyp("Testkurs1", true);
    private final Kursort kursort1 = new Kursort("Testsaal1", true);
    private final Mitarbeiter mitarbeiter1 = new Mitarbeiter(Anrede.FRAU, "Noémie", "Roos1", new GregorianCalendar(1994, Calendar.MARCH, 18), "044 391 45 35", "076 384 45 35", "nroos@gmx.ch", "756.3943.8722.22", "CH31 8123 9000 0012 4568 9", true, "Mi, Fr, Sa", null, true);
    private final Mitarbeiter mitarbeiter2 = new Mitarbeiter(Anrede.FRAU, "Noémie", "Roos2", new GregorianCalendar(1994, Calendar.MARCH, 18), "044 391 45 35", "076 384 45 35", "nroos@gmx.ch", "756.3943.8722.22", "CH31 8123 9000 0012 4568 9", true, "Mi, Fr, Sa", null, true);

    @Before
    public void setUp() {
        Kurs kurs = new Kurs("2-3 J", "Vorkindergarten", Wochentag.DONNERSTAG, Time.valueOf("10:00:00"), Time.valueOf("10:50:00"), "Dies ist ein Test.");
        kurs.setSemester(semester1);
        kurs.setKurstyp(kurstyp1);
        kurs.setKursort(kursort1);
        kurs.addLehrkraft(mitarbeiter1);
        bereitsErfassteKurse.add(kurs);
    }

    @Test
    public void testExecute_KursBereitsErfasst() {
        Kurs kurs = new Kurs("2-3 J", "Vorkindergarten", Wochentag.DONNERSTAG, Time.valueOf("10:00:00"), Time.valueOf("10:50:00"), "Dies ist ein Test.");
        CheckKursBereitsErfasstCommand checkKursBereitsErfasstCommand = new CheckKursBereitsErfasstCommand(kurs, semester1, mitarbeiter1, null,null, bereitsErfassteKurse);
        commandInvoker.executeCommand(checkKursBereitsErfasstCommand);
        assertTrue(checkKursBereitsErfasstCommand.isBereitsErfasst());
    }

    @Test
    public void testExecute_KursNochNichtErfasstAndereLehrkraft() {
        Kurs kurs = new Kurs("2-3 J", "Vorkindergarten", Wochentag.DONNERSTAG, Time.valueOf("10:00:00"), Time.valueOf("10:50:00"), "Dies ist ein Test.");
        CheckKursBereitsErfasstCommand checkKursBereitsErfasstCommand = new CheckKursBereitsErfasstCommand(kurs, semester1, mitarbeiter2, null, null, bereitsErfassteKurse);
        commandInvoker.executeCommand(checkKursBereitsErfasstCommand);
        assertFalse(checkKursBereitsErfasstCommand.isBereitsErfasst());
    }

    @Test
    public void testExecute_KursNochNichtErfasstAndereZeit() {
        Kurs kurs = new Kurs("2-3 J", "Vorkindergarten", Wochentag.DONNERSTAG, Time.valueOf("10:30:00"), Time.valueOf("10:50:00"), "Dies ist ein Test.");
        CheckKursBereitsErfasstCommand checkKursBereitsErfasstCommand = new CheckKursBereitsErfasstCommand(kurs, semester1, mitarbeiter1, null, null, bereitsErfassteKurse);
        commandInvoker.executeCommand(checkKursBereitsErfasstCommand);
        assertFalse(checkKursBereitsErfasstCommand.isBereitsErfasst());
    }

    @Test
    public void testExecute_KursNochNichtErfasstAndererWochentag() {
        Kurs kurs = new Kurs("2-3 J", "Vorkindergarten", Wochentag.FREITAG, Time.valueOf("10:00:00"), Time.valueOf("10:50:00"), "Dies ist ein Test.");
        CheckKursBereitsErfasstCommand checkKursBereitsErfasstCommand = new CheckKursBereitsErfasstCommand(kurs, semester1, mitarbeiter1, null, null, bereitsErfassteKurse);
        commandInvoker.executeCommand(checkKursBereitsErfasstCommand);
        assertFalse(checkKursBereitsErfasstCommand.isBereitsErfasst());
    }

    @Test
    public void testExecute_KursNochNichtErfasstAnderesSemester() {
        Kurs kurs = new Kurs("2-3 J", "Vorkindergarten", Wochentag.DONNERSTAG, Time.valueOf("10:00:00"), Time.valueOf("10:50:00"), "Dies ist ein Test.");
        CheckKursBereitsErfasstCommand checkKursBereitsErfasstCommand = new CheckKursBereitsErfasstCommand(kurs, semester2, mitarbeiter1, null, null, bereitsErfassteKurse);
        commandInvoker.executeCommand(checkKursBereitsErfasstCommand);
        assertFalse(checkKursBereitsErfasstCommand.isBereitsErfasst());
    }

    @Test
    public void testExecute_KursOrigin() {
        Kurs kurs = new Kurs("2-3 J", "Vorkindergarten", Wochentag.DONNERSTAG, Time.valueOf("10:00:00"), Time.valueOf("10:50:00"), "Dies ist ein Test.");
        CheckKursBereitsErfasstCommand checkKursBereitsErfasstCommand = new CheckKursBereitsErfasstCommand(kurs, semester1, mitarbeiter1, null, bereitsErfassteKurse.get(0), bereitsErfassteKurse);
        commandInvoker.executeCommand(checkKursBereitsErfasstCommand);
        assertFalse(checkKursBereitsErfasstCommand.isBereitsErfasst());
    }
}