package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.RechnungSerienbrief;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class ReadRechnungenSerienbriefCsvCommand implements Command {

    public enum Result {
        DATEI_EXISTIERT_NICHT_ODER_NICHT_LESBAR,
        FEHLER_BEIM_EINLESEN,
        DATEI_LEER,
        UNGUELTIGER_HEADER,
        UNGUELTIGE_DATEN_ZEILE,
        ERFOLGREICH_EINGELESEN
    }


    private static final String[] COLUMN_NAMES = {"Anrede", "Vorname", "Nachname", "Strasse", "PLZ",
            "Ort", "E-Mail", "Rechnungsdatum", "Bemerkungen", "Anzahl Wochen", "Wochenbetrag",
            "Schulgeld", "Ermässigung", "Zuschlag", "Stipendium", "Rechnungsbetrag", "Schüler"};

    // input
    private final File csvSerienbriefDatei;

    // output
    private Result result;
    private String errLine;
    private final List<RechnungSerienbrief> rechnungenSerienbrief = new ArrayList<>();


    public ReadRechnungenSerienbriefCsvCommand(File csvSerienbriefDatei) {
        this.csvSerienbriefDatei = csvSerienbriefDatei;
    }

    @Override
    public void execute() {

        List<String> lines;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(csvSerienbriefDatei), StandardCharsets.ISO_8859_1));

            lines = new ArrayList<>();
            while (reader.ready()) {
                String line = reader.readLine();
                lines.add(line);
            }

            reader.close();

        } catch (FileNotFoundException e) {
            result = Result.DATEI_EXISTIERT_NICHT_ODER_NICHT_LESBAR;
            return;
        } catch (IOException e) {
            result = Result.FEHLER_BEIM_EINLESEN;
            return;
        }

        // Datei leer?
        if (lines.isEmpty()) {
            result = Result.DATEI_LEER;
            return;
        }

        // Überprüfung Header
        String[] headerColumns = lines.get(0).split(";");
        if (headerColumns.length != COLUMN_NAMES.length) {
            errLine = lines.get(0);
            result = Result.UNGUELTIGER_HEADER;
            return;
        }
        for (int i = 0; i < headerColumns.length; i++) {
            if (!headerColumns[i].equals(COLUMN_NAMES[i])) {
                errLine = lines.get(0);
                result = Result.UNGUELTIGER_HEADER;
                return;
            }
        }

        // Daten einlesen
        for (int i = 1; i < lines.size(); i++) {
            String[] data = lines.get(i).split(";");
            if (data.length != COLUMN_NAMES.length) {
                errLine = lines.get(i);
                result = Result.UNGUELTIGER_HEADER;
                return;
            }
            RechnungSerienbrief rechnungSerienbrief = new RechnungSerienbrief(data[0], data[1],
                    data[2], data[3], data[4], data[5], data[6], data[7], data[8], data[9],
                    data[10], data[11], data[12], data[13], data[14], data[15], data[16]);
            rechnungenSerienbrief.add(rechnungSerienbrief);
        }

        result = Result.ERFOLGREICH_EINGELESEN;
    }

    public Result getResult() {
        return result;
    }

    public String getErrLine() {
        return errLine;
    }

    public List<RechnungSerienbrief> getRechnungenSerienbrief() {
        return rechnungenSerienbrief;
    }
}
