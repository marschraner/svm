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
    private final int maxLines;

    // Output
    private List<String> lines = new ArrayList<>();

    public SplitStringIntoMultipleLinesCommand(String string, int maxLength, int maxLines) {
        this.string = string;
        this.maxLength = maxLength;
        this.maxLines = maxLines;
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
        int j = 0;
        for (int i = 0; i < stringSpl.length; i++) {
            if (line.isEmpty()) {
                line = stringSpl[i];
            } else {
                line = line + " " + stringSpl[i];
            }
            length += stringSpl[i].length() + 1;   // + 1 wegen Leerzeichen
            if (i == stringSpl.length - 1 || length + stringSpl[i+1].length() > maxLength) {
                // Maximal zulässige Anzahl Zeilen erreicht -> den Rest auch rausschreiben
                if (j == maxLines - 1) {
                    for (int ii = i + 1; ii < stringSpl.length; ii++) {
                        line = line + " " + stringSpl[ii];
                    }
                }
                // Alle "- " wieder durch "-" ersetzen
                line = line.replaceAll("- ", "-");
                // Alle "/ " wieder durch "/" ersetzen
                line = line.replaceAll("/ ", "/");
                // Jetzige Zeile schreiben und neue Zeile beginnen
                lines.add(line);
                if (j == maxLines - 1) {
                    return;
                }
                line = "";
                length = 0;
                j++;
            }
        }

    }

    public List<String> getLines() {
        return lines;
    }
}
