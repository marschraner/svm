package ch.metzenthin.svm.domain.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * @author Martin Schraner
 */
class SplitStringIntoMultipleLinesCommandTest {

  @Test
  void testExecuteLeerschlag() {
    String string = "Dies ist eine Zeile, die zu lange ist.";
    SplitStringIntoMultipleLinesCommand splitStringIntoMultipleLinesCommand =
        new SplitStringIntoMultipleLinesCommand(string, 15, 3);
    splitStringIntoMultipleLinesCommand.execute();
    List<String> lines = splitStringIntoMultipleLinesCommand.getLines();
    assertEquals(3, lines.size());
    assertEquals("Dies ist eine", lines.get(0));
    assertEquals("Zeile, die zu", lines.get(1));
    assertEquals("lange ist.", lines.get(2));
  }

  @Test
  void testExecuteLeerschlagMaxLines() {
    String string = "Dies ist eine Zeile, die zu lange ist.";
    SplitStringIntoMultipleLinesCommand splitStringIntoMultipleLinesCommand =
        new SplitStringIntoMultipleLinesCommand(string, 15, 2);
    splitStringIntoMultipleLinesCommand.execute();
    List<String> lines = splitStringIntoMultipleLinesCommand.getLines();
    assertEquals(2, lines.size());
    assertEquals("Dies ist eine", lines.get(0));
    assertEquals("Zeile, die zu lange ist.", lines.get(1));
  }

  @Test
  void testExecuteBindestrich() {
    String string = "Rhythmik-Darstellendes Spiel";
    SplitStringIntoMultipleLinesCommand splitStringIntoMultipleLinesCommand =
        new SplitStringIntoMultipleLinesCommand(string, 20, 2);
    splitStringIntoMultipleLinesCommand.execute();
    List<String> lines = splitStringIntoMultipleLinesCommand.getLines();
    assertEquals(2, lines.size());
    assertEquals("Rhythmik-", lines.get(0));
    assertEquals("Darstellendes Spiel", lines.get(1));
  }

  @Test
  void testExecuteSchraegstrich() {
    String string = "Mittwoch Morgen/Abend";
    SplitStringIntoMultipleLinesCommand splitStringIntoMultipleLinesCommand =
        new SplitStringIntoMultipleLinesCommand(string, 18, 2);
    splitStringIntoMultipleLinesCommand.execute();
    List<String> lines = splitStringIntoMultipleLinesCommand.getLines();
    assertEquals(2, lines.size());
    assertEquals("Mittwoch Morgen/", lines.get(0));
    assertEquals("Abend", lines.get(1));
  }
}
