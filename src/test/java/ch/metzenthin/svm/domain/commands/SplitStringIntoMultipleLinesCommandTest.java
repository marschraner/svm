package ch.metzenthin.svm.domain.commands;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Martin Schraner
 */
public class SplitStringIntoMultipleLinesCommandTest {

    @Test
    public void testExecuteLeerschlag() throws Exception {
        String string = "Dies ist eine Zeile, die zu lange ist.";
        SplitStringIntoMultipleLinesCommand splitStringIntoMultipleLinesCommand = new SplitStringIntoMultipleLinesCommand(string, 15);
        splitStringIntoMultipleLinesCommand.execute();
        List<String> lines = splitStringIntoMultipleLinesCommand.getLines();
        assertEquals(3, lines.size());
        assertEquals("Dies ist eine", lines.get(0));
        assertEquals("Zeile, die zu", lines.get(1));
        assertEquals("lange ist.", lines.get(2));
    }

    @Test
    public void testExecuteBindestrich() throws Exception {
        String string = "Rhythmik-Darstellendes Spiel";
        SplitStringIntoMultipleLinesCommand splitStringIntoMultipleLinesCommand = new SplitStringIntoMultipleLinesCommand(string, 20);
        splitStringIntoMultipleLinesCommand.execute();
        List<String> lines = splitStringIntoMultipleLinesCommand.getLines();
        assertEquals(2, lines.size());
        assertEquals("Rhythmik-", lines.get(0));
        assertEquals("Darstellendes Spiel", lines.get(1));
    }

    @Test
    public void testExecuteSchraegstrich() throws Exception {
        String string = "Mittwoch Morgen/Abend";
        SplitStringIntoMultipleLinesCommand splitStringIntoMultipleLinesCommand = new SplitStringIntoMultipleLinesCommand(string, 18);
        splitStringIntoMultipleLinesCommand.execute();
        List<String> lines = splitStringIntoMultipleLinesCommand.getLines();
        assertEquals(2, lines.size());
        assertEquals("Mittwoch Morgen/", lines.get(0));
        assertEquals("Abend", lines.get(1));
    }
}