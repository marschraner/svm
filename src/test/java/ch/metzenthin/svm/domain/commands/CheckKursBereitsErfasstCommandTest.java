package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.dataTypes.Anrede;
import ch.metzenthin.svm.dataTypes.Semesterbezeichnung;
import ch.metzenthin.svm.dataTypes.Wochentag;
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

    private CommandInvoker commandInvoker = new CommandInvokerImpl();
    private List<Kurs> bereitsErfassteKurse = new ArrayList<>();
    private Semester semester1 = new Semester("2011/2012", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(2011, Calendar.AUGUST, 20), new GregorianCalendar(2012, Calendar.FEBRUARY, 10), 21);
    private Semester semester2 = new Semester("2012/2013", Semesterbezeichnung.ERSTES_SEMESTER, new GregorianCalendar(2012, Calendar.AUGUST, 20), new GregorianCalendar(2013, Calendar.FEBRUARY, 10), 21);
    private Kurstyp kurstyp1 = new Kurstyp("Testkurs1");
    private Kurstyp kurstyp2 = new Kurstyp("Testkurs1");
    private Kursort kursort1 = new Kursort("Testsaal1");
    private Kursort kursort2 = new Kursort("Testsaal2");
    private Lehrkraft lehrkraft1 = new Lehrkraft(Anrede.FRAU, "Noémie", "Roos1", new GregorianCalendar(1994, Calendar.MARCH, 18), "044 391 45 35", "076 384 45 35", "nroos@gmx.ch", "756.3943.8722.22", "Mi, Fr, Sa", true);
    private Lehrkraft lehrkraft2 = new Lehrkraft(Anrede.FRAU, "Noémie", "Roos2", new GregorianCalendar(1994, Calendar.MARCH, 18), "044 391 45 35", "076 384 45 35", "nroos@gmx.ch", "756.3943.8722.22", "Mi, Fr, Sa", true);


    @Before
    public void setUp() {
        Kurs kurs = new Kurs("2-3 J", "Vorkindergarten", Wochentag.DONNERSTAG, Time.valueOf("10:00:00"), Time.valueOf("10:50:00"), "Dies ist ein Test.");
        kurs.setSemester(semester1);
        kurs.setKurstyp(kurstyp1);
        kurs.setKursort(kursort1);
        kurs.addLehrkraft(lehrkraft1);
        bereitsErfassteKurse.add(kurs);
    }

    @Test
    public void testExecute_KursBereitsErfasst() throws Exception {
        Kurs kurs = new Kurs("2-3 J", "Vorkindergarten", Wochentag.DONNERSTAG, Time.valueOf("10:00:00"), Time.valueOf("10:50:00"), "Dies ist ein Test.");
        kurs.setSemester(semester1);
        kurs.setKurstyp(kurstyp2);
        kurs.setKursort(kursort2);
        kurs.addLehrkraft(lehrkraft1);
        CheckKursBereitsErfasstCommand checkKursBereitsErfasstCommand = new CheckKursBereitsErfasstCommand(kurs, null, bereitsErfassteKurse);
        commandInvoker.executeCommand(checkKursBereitsErfasstCommand);
        assertTrue(checkKursBereitsErfasstCommand.isBereitsErfasst());
    }

    @Test
    public void testExecute_KursNochNichtErfasstAndereLehrkraft() throws Exception {
        Kurs kurs = new Kurs("2-3 J", "Vorkindergarten", Wochentag.DONNERSTAG, Time.valueOf("10:00:00"), Time.valueOf("10:50:00"), "Dies ist ein Test.");
        kurs.setSemester(semester1);
        kurs.setKurstyp(kurstyp2);
        kurs.setKursort(kursort2);
        kurs.addLehrkraft(lehrkraft2);
        CheckKursBereitsErfasstCommand checkKursBereitsErfasstCommand = new CheckKursBereitsErfasstCommand(kurs, null, bereitsErfassteKurse);
        commandInvoker.executeCommand(checkKursBereitsErfasstCommand);
        assertFalse(checkKursBereitsErfasstCommand.isBereitsErfasst());
    }

    @Test
    public void testExecute_KursNochNichtErfasstAndereZeit() throws Exception {
        Kurs kurs = new Kurs("2-3 J", "Vorkindergarten", Wochentag.DONNERSTAG, Time.valueOf("10:30:00"), Time.valueOf("10:50:00"), "Dies ist ein Test.");
        kurs.setSemester(semester1);
        kurs.setKurstyp(kurstyp2);
        kurs.setKursort(kursort2);
        kurs.addLehrkraft(lehrkraft1);
        CheckKursBereitsErfasstCommand checkKursBereitsErfasstCommand = new CheckKursBereitsErfasstCommand(kurs, null, bereitsErfassteKurse);
        commandInvoker.executeCommand(checkKursBereitsErfasstCommand);
        assertFalse(checkKursBereitsErfasstCommand.isBereitsErfasst());
    }

    @Test
    public void testExecute_KursNochNichtErfasstAndererWochentag() throws Exception {
        Kurs kurs = new Kurs("2-3 J", "Vorkindergarten", Wochentag.FREITAG, Time.valueOf("10:00:00"), Time.valueOf("10:50:00"), "Dies ist ein Test.");
        kurs.setSemester(semester1);
        kurs.setKurstyp(kurstyp2);
        kurs.setKursort(kursort2);
        kurs.addLehrkraft(lehrkraft1);
        CheckKursBereitsErfasstCommand checkKursBereitsErfasstCommand = new CheckKursBereitsErfasstCommand(kurs, null, bereitsErfassteKurse);
        commandInvoker.executeCommand(checkKursBereitsErfasstCommand);
        assertFalse(checkKursBereitsErfasstCommand.isBereitsErfasst());
    }

    @Test
    public void testExecute_KursNochNichtErfasstAnderesSemester() throws Exception {
        Kurs kurs = new Kurs("2-3 J", "Vorkindergarten", Wochentag.DONNERSTAG, Time.valueOf("10:00:00"), Time.valueOf("10:50:00"), "Dies ist ein Test.");
        kurs.setSemester(semester2);
        kurs.setKurstyp(kurstyp2);
        kurs.setKursort(kursort2);
        kurs.addLehrkraft(lehrkraft1);
        CheckKursBereitsErfasstCommand checkKursBereitsErfasstCommand = new CheckKursBereitsErfasstCommand(kurs, null, bereitsErfassteKurse);
        commandInvoker.executeCommand(checkKursBereitsErfasstCommand);
        assertFalse(checkKursBereitsErfasstCommand.isBereitsErfasst());
    }

    @Test
    public void testExecute_KursOrigin() throws Exception {
        Kurs kurs = new Kurs("2-3 J", "Vorkindergarten", Wochentag.DONNERSTAG, Time.valueOf("10:00:00"), Time.valueOf("10:50:00"), "Dies ist ein Test.");
        kurs.setSemester(semester1);
        kurs.setKurstyp(kurstyp2);
        kurs.setKursort(kursort2);
        kurs.addLehrkraft(lehrkraft1);
        CheckKursBereitsErfasstCommand checkKursBereitsErfasstCommand = new CheckKursBereitsErfasstCommand(kurs, bereitsErfassteKurse.get(0), bereitsErfassteKurse);
        commandInvoker.executeCommand(checkKursBereitsErfasstCommand);
        assertFalse(checkKursBereitsErfasstCommand.isBereitsErfasst());
    }
}