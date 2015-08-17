package ch.metzenthin.svm.domain.commands;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class SplitStringIntoMultipleLinesCommand implements Command {

    // Input
    private final String string;
    private final int maxLength;

    // Output
    private List<String> lines = new ArrayList<>();

    public SplitStringIntoMultipleLinesCommand(String string, int maxLength) {
        this.string = string;
        this.maxLength = maxLength;
    }

    @Override
    public void execute() {

        if (string == null) {
            return;
        }

        // Alle "-" durch "- " ersetzen, damit Leerschlag-Trennung wirksam wird
        String stringTmp = string.replaceAll("-", "- ");
        // Alle "/" durch "/ " ersetzen, damit Leerschlag-Trennung wirksam wird
        stringTmp = stringTmp.replaceAll("/", "/ ");

        String[] stringSpl = stringTmp.split("[\\s]");
        String line = "";
        int length = 0;
        for (int i = 0; i < stringSpl.length; i++) {
            if (line.isEmpty()) {
                line = stringSpl[i];
            } else {
                line = line + " " + stringSpl[i];
            }
            length += stringSpl[i].length() + 1;   // + 1 wegen Leerzeichen
            if (i == stringSpl.length - 1 || length + stringSpl[i+1].length() > maxLength) {
                // Alle "- " wieder durch "-" ersetzen
                line = line.replaceAll("- ", "-");
                // Alle "/ " wieder durch "/" ersetzen
                line = line.replaceAll("/ ", "/");
                // Jetzige Zeile schreiben und neue Zeile beginnen
                lines.add(line);
                line = "";
                length = 0;
            }
        }

    }

    public List<String> getLines() {
        return lines;
    }
}
