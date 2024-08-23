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
    private final List<String> lines = new ArrayList<>();

    SplitStringIntoMultipleLinesCommand(String string, int maxLength, int maxLines) {
        this.string = string;
        this.maxLength = maxLength;
        this.maxLines = maxLines;
    }

    @SuppressWarnings("java:S3776")
    @Override
    public void execute() {

        if (string == null) {
            return;
        }

        // Alle "-" durch "- " ersetzen, damit Leerschlag-Trennung wirksam wird
        String stringTmp = string.replace("-", "- ");
        // Alle "/" durch "/ " ersetzen, damit Leerschlag-Trennung wirksam wird
        stringTmp = stringTmp.replace("/", "/ ");

        String[] stringSpl = stringTmp.split("\\s");
        StringBuilder line = new StringBuilder();
        int length = 0;
        int j = 0;
        for (int i = 0; i < stringSpl.length; i++) {
            if (line.isEmpty()) {
                line = new StringBuilder(stringSpl[i]);
            } else {
                line.append(" ").append(stringSpl[i]);
            }
            length += stringSpl[i].length() + 1;   // + 1 wegen Leerzeichen
            if (i == stringSpl.length - 1 || length + stringSpl[i + 1].length() > maxLength) {
                // Maximal zulässige Anzahl Zeilen erreicht -> den Rest auch rausschreiben
                if (j == maxLines - 1) {
                    for (int ii = i + 1; ii < stringSpl.length; ii++) {
                        line.append(" ").append(stringSpl[ii]);
                    }
                }
                // Alle "- " wieder durch "-" ersetzen
                line = new StringBuilder(line.toString().replace("- ", "-"));
                // Alle "/ " wieder durch "/" ersetzen
                line = new StringBuilder(line.toString().replace("/ ", "/"));
                // Jetzige Zeile schreiben und neue Zeile beginnen
                lines.add(line.toString());
                if (j == maxLines - 1) {
                    return;
                }
                line = new StringBuilder();
                length = 0;
                j++;
            }
        }

    }

    List<String> getLines() {
        return lines;
    }
}
