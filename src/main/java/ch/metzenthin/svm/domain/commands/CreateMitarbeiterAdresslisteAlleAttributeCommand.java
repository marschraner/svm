package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import ch.metzenthin.svm.ui.componentmodel.MitarbeitersTableModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static ch.metzenthin.svm.common.utils.Converter.asString;
import static ch.metzenthin.svm.common.utils.Converter.nullAsEmptyString;

/**
 * @author Martin Schraner
 */
public class CreateMitarbeiterAdresslisteAlleAttributeCommand extends CreateListeCommand {

    // input
    private final MitarbeitersTableModel mitarbeitersTableModel;
    private final String titel;
    private final File outputFile;

    public CreateMitarbeiterAdresslisteAlleAttributeCommand(MitarbeitersTableModel mitarbeitersTableModel, String titel, File outputFile) {
        this.mitarbeitersTableModel = mitarbeitersTableModel;
        this.titel = titel;
        this.outputFile = outputFile;
    }

    @Override
    public void execute() {

        // Spaltenbreiten
        // ACHTUNG: Summe muss <= 11200 (wenn nicht anders möglich: <= 11500) sein!
        //          Bei > 11200 hinten schmalerer Rand!
        //          Bei > 11500 Spaltenbreite durch Inhalt beieinflusst!!!
        List<Integer> columnWidths = new ArrayList<>();
        columnWidths.add(0);  // entspricht einer Breite von max 550 (wenn 3-stellig)
        columnWidths.add(2300);
        columnWidths.add(2500);
        columnWidths.add(1400);
        columnWidths.add(1750);
        columnWidths.add(3050);

        // Bold / horiz. merged (Anzahl zu mergende Zellen; 0: kein Merging):
        List<List<Boolean>> boldCells = new ArrayList<>();
        List<List<Integer>> mergedCells = new ArrayList<>();
        // 1. Zeile
        List<Boolean> boldRow1 = new ArrayList<>();
        boldRow1.add(false);
        boldRow1.add(true);
        boldRow1.add(true);
        boldRow1.add(false);
        boldRow1.add(false);
        boldRow1.add(false);
        boldCells.add(boldRow1);
        List<Integer> mergedRow1 = new ArrayList<>();
        mergedRow1.add(0);
        mergedRow1.add(0);
        mergedRow1.add(0);
        mergedRow1.add(0);
        mergedRow1.add(0);
        mergedRow1.add(0);
        mergedCells.add(mergedRow1);
        // 2. Zeile
        List<Boolean> boldRow2 = new ArrayList<>();
        boldRow2.add(false);
        boldRow2.add(false);
        boldRow2.add(false);
        boldRow2.add(false);
        boldRow2.add(false);
        boldRow2.add(false);
        boldCells.add(boldRow2);
        List<Integer> mergedRow2 = new ArrayList<>();
        mergedRow2.add(0);
        mergedRow2.add(0);
        mergedRow2.add(0);
        mergedRow2.add(0);
        mergedRow2.add(0);
        mergedRow2.add(0);
        mergedCells.add(mergedRow2);
        // 3. Zeile
        List<Boolean> boldRow3 = new ArrayList<>();
        boldRow3.add(false);
        boldRow3.add(false);
        boldRow3.add(false);
        boldRow3.add(false);
        boldRow3.add(false);
        boldRow3.add(false);
        boldCells.add(boldRow3);
        List<Integer> mergedRow3 = new ArrayList<>();
        mergedRow3.add(0);
        mergedRow3.add(0);
        mergedRow3.add(2);
        mergedRow3.add(0);
        mergedRow3.add(2);
        mergedRow3.add(0);
        mergedCells.add(mergedRow3);

        // Maximale Anzahl Zeichen (wenn überschritten wird Schrift verkleinert),
        // wenn 0 nicht zu prüfen
        List<List<int[]>> maxLengths = new ArrayList<>();
        // 1. Zeile
        List<int[]> maxLengthsRow1 = new ArrayList<>();
        maxLengthsRow1.add(new int[]{0});
        maxLengthsRow1.add(new int[]{21, 22, 23, 24, 25, 27});
        maxLengthsRow1.add(new int[]{22, 23, 24, 25, 26, 28});
        maxLengthsRow1.add(new int[]{13, 13, 13, 14, 16, 18});
        maxLengthsRow1.add(new int[]{0});
        maxLengthsRow1.add(new int[]{29, 30, 31, 32, 33, 35});
        maxLengths.add(maxLengthsRow1);
        // 2. Zeile
        List<int[]> maxLengthsRow2 = new ArrayList<>();
        maxLengthsRow2.add(new int[]{0});
        maxLengthsRow2.add(new int[]{22, 23, 24, 25, 26, 28});
        maxLengthsRow2.add(new int[]{23, 24, 25, 26, 27, 29});
        maxLengthsRow2.add(new int[]{13, 13, 13, 14, 16, 18});
        maxLengthsRow2.add(new int[]{0});
        maxLengthsRow2.add(new int[]{29, 30, 31, 32, 33, 35});
        maxLengths.add(maxLengthsRow2);
        // 3. Zeile
        List<int[]> maxLengthsRow3 = new ArrayList<>();
        maxLengthsRow3.add(new int[]{0});
        maxLengthsRow3.add(new int[]{0});
        maxLengthsRow3.add(new int[]{38, 39, 40, 41, 43, 45});
        maxLengthsRow3.add(new int[]{0});
        maxLengthsRow3.add(new int[]{0});
        maxLengthsRow3.add(new int[]{0});
        maxLengths.add(maxLengthsRow3);

        // Header
        List<List<String>> header = new ArrayList<>();
        // 1. Zeile
        List<String> headerCellsRow1 = new ArrayList<>();
        headerCellsRow1.add("");
        headerCellsRow1.add("Name");
        headerCellsRow1.add("Vorname");
        headerCellsRow1.add("Festnetz");
        headerCellsRow1.add("Geburtsdatum");
        headerCellsRow1.add("Vertretungsmöglichkeiten");
        header.add(headerCellsRow1);
        // 2. Zeile
        List<String> headerCellsRow2 = new ArrayList<>();
        headerCellsRow2.add("");
        headerCellsRow2.add("Strasse/Nr.");
        headerCellsRow2.add("PLZ/Ort");
        headerCellsRow2.add("Natel");
        headerCellsRow2.add("AHV-Nummer");
        headerCellsRow2.add("");
        header.add(headerCellsRow2);
        // 3. Spalte
        List<String> headerCellsRow3 = new ArrayList<>();
        headerCellsRow3.add("");
        headerCellsRow3.add("");
        headerCellsRow3.add("E-Mail");
        headerCellsRow3.add("");
        headerCellsRow3.add("IBAN-Nummer");
        headerCellsRow3.add("");
        header.add(headerCellsRow3);

        // Inhalt
        List<List<List<String>>> datasets = new ArrayList<>();
        int i = 0;
        for (Mitarbeiter mitarbeiter : mitarbeitersTableModel.getSelektierteMitarbeiters()) {
            List<List<String>> dataset = new ArrayList<>();
            // Auf mehrere Zeilen aufzusplittende Felder:
            String vertretungsmoeglichkeiten = mitarbeiter.getVertretungsmoeglichkeitenLineBreaksReplacedBySemicolonOrPeriod();
            int maxLengthPerLine;
            if (vertretungsmoeglichkeiten == null || vertretungsmoeglichkeiten.length() <= 58) {
                maxLengthPerLine = 29;
            } else if (vertretungsmoeglichkeiten.length() <= 70) {
                maxLengthPerLine = 35;
            } else if (vertretungsmoeglichkeiten.length() <= 80) {
                maxLengthPerLine = 40;
            } else if (vertretungsmoeglichkeiten.length() <= 90) {
                maxLengthPerLine = 45;
            } else if (vertretungsmoeglichkeiten.length() <= 100) {
                maxLengthPerLine = 50;
            } else {
                maxLengthPerLine = 60;
            }
            SplitStringIntoMultipleLinesCommand splitStringIntoMultipleLinesCommand
                    = new SplitStringIntoMultipleLinesCommand(vertretungsmoeglichkeiten, maxLengthPerLine, 2);
            splitStringIntoMultipleLinesCommand.execute();
            List<String> vertretungsmoeglichkeitenLines = splitStringIntoMultipleLinesCommand.getLines();
            // 1. Zeile
            List<String> cellsRow1 = new ArrayList<>();
            cellsRow1.add(Integer.toString(i + 1));
            cellsRow1.add(mitarbeiter.getNachname());
            cellsRow1.add(mitarbeiter.getVorname());
            cellsRow1.add(nullAsEmptyString(mitarbeiter.getFestnetz()));
            cellsRow1.add(nullAsEmptyString(asString(mitarbeiter.getGeburtsdatum())));
            if (!vertretungsmoeglichkeitenLines.isEmpty()) {
                cellsRow1.add(vertretungsmoeglichkeitenLines.get(0));
            } else {
                cellsRow1.add("");
            }
            dataset.add(cellsRow1);

            // 2. Zeile
            List<String> cellsRow2 = new ArrayList<>();
            cellsRow2.add("");
            cellsRow2.add(mitarbeiter.getAdresse() == null ? "" : nullAsEmptyString(mitarbeiter.getAdresse().getStrHausnummer()));
            cellsRow2.add(mitarbeiter.getAdresse() == null ? "" : nullAsEmptyString(mitarbeiter.getAdresse().getPlz() + " " + mitarbeiter.getAdresse().getOrt()));
            cellsRow2.add(nullAsEmptyString(mitarbeiter.getNatel()));
            cellsRow2.add(nullAsEmptyString(mitarbeiter.getAhvNummer()));
            if (vertretungsmoeglichkeitenLines.size() > 1) {
                cellsRow2.add(vertretungsmoeglichkeitenLines.get(1));
            } else {
                cellsRow2.add("");
            }
            dataset.add(cellsRow2);

            // 3. Zeile
            List<String> cellsRow3 = new ArrayList<>();
            cellsRow3.add("");
            cellsRow3.add("");
            cellsRow3.add(nullAsEmptyString(mitarbeiter.getEmailToBeDisplayedInWord()));
            cellsRow3.add("");
            cellsRow3.add(nullAsEmptyString(mitarbeiter.getIbanNummer()));
            cellsRow3.add("");
            dataset.add(cellsRow3);

            // Einzelner Datensatz der Liste von Datensätzen hinzufügen
            datasets.add(dataset);
            i++;
        }

        // Tabelle erzeugen
        Calendar today = new GregorianCalendar();
        String titel1 = "Kinder- und Jugendtheater Metzenthin AG                                                                         " + asString(today);
        CreateWordTableCommand createWordTableCommand = new CreateWordTableCommand(header, datasets, columnWidths, boldCells, mergedCells, maxLengths, titel1, titel, outputFile);
        createWordTableCommand.execute();

        result = Result.LISTE_ERFOLGREICH_ERSTELLT;
    }

}
