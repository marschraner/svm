package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.entities.Lehrkraft;
import ch.metzenthin.svm.ui.componentmodel.LehrkraefteTableModel;

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
public class CreateLehrkraefteAdresslisteCommand extends CreateListeCommand {

    // input
    private final LehrkraefteTableModel lehrkraefteTableModel;
    private final String titel;
    private final File outputFile;

    public CreateLehrkraefteAdresslisteCommand(LehrkraefteTableModel lehrkraefteTableModel, String titel, File outputFile) {
        this.lehrkraefteTableModel = lehrkraefteTableModel;
        this.titel = titel;
        this.outputFile = outputFile;
    }

    @Override
    public void execute() {

        // Spaltenbreiten
        List<Integer> columnWidths = new ArrayList<>();
        columnWidths.add(0);
        columnWidths.add(2500);
        columnWidths.add(2800);
        columnWidths.add(1800);
        columnWidths.add(1600);
        columnWidths.add(2900);

        // Bold / horiz. merged:
        List<List<Boolean>> boldCells = new ArrayList<>();
        List<List<Boolean>> mergedCells = new ArrayList<>();
        // 1. Zeile
        List<Boolean> boldRow1 = new ArrayList<>();
        boldRow1.add(false);
        boldRow1.add(true);
        boldRow1.add(true);
        boldRow1.add(false);
        boldRow1.add(false);
        boldRow1.add(false);
        boldCells.add(boldRow1);
        List<Boolean> mergedRow1 = new ArrayList<>();
        mergedRow1.add(false);
        mergedRow1.add(false);
        mergedRow1.add(false);
        mergedRow1.add(false);
        mergedRow1.add(false);
        mergedRow1.add(false);
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
        List<Boolean> mergedRow2 = new ArrayList<>();
        mergedRow2.add(false);
        mergedRow2.add(false);
        mergedRow2.add(false);
        mergedRow2.add(false);
        mergedRow2.add(false);
        mergedRow2.add(false);
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
        List<Boolean> mergedRow3 = new ArrayList<>();
        mergedRow3.add(false);
        mergedRow3.add(false);
        mergedRow3.add(true);
        mergedRow3.add(false);
        mergedRow3.add(false);
        mergedRow3.add(false);
        mergedCells.add(mergedRow3);

        // Maximale Anzahl Zeichen (wenn überschritten wird Schrift verkleinert),
        // wenn 0 nicht zu prüfen
        List<List<Integer>> maxLengths = new ArrayList<>();
        // 1. Zeile
        List<Integer> maxLengthsRow1 = new ArrayList<>();
        maxLengthsRow1.add(0);
        maxLengthsRow1.add(21);
        maxLengthsRow1.add(25);
        maxLengthsRow1.add(0);
        maxLengthsRow1.add(0);
        maxLengthsRow1.add(0);
        maxLengths.add(maxLengthsRow1);
        // 2. Zeile
        List<Integer> maxLengthsRow2 = new ArrayList<>();
        maxLengthsRow2.add(0);
        maxLengthsRow2.add(21);
        maxLengthsRow2.add(25);
        maxLengthsRow2.add(0);
        maxLengthsRow2.add(0);
        maxLengthsRow2.add(0);
        maxLengths.add(maxLengthsRow2);
        // 3. Zeile
        List<Integer> maxLengthsRow3 = new ArrayList<>();
        maxLengthsRow3.add(0);
        maxLengthsRow3.add(0);
        maxLengthsRow3.add(38);
        maxLengthsRow3.add(0);
        maxLengthsRow3.add(0);
        maxLengthsRow3.add(0);
        maxLengths.add(maxLengthsRow3);

        // Header
        List<List<String>> header = new ArrayList<>();
        // 1. Zeile
        List<String> headerCellsRow1 = new ArrayList<>();
        headerCellsRow1.add("");
        headerCellsRow1.add("Name");
        headerCellsRow1.add("Vorname");
        headerCellsRow1.add("Geb.Datum");
        headerCellsRow1.add("Festnetz");
        headerCellsRow1.add("Vertretungsmöglichkeiten");
        header.add(headerCellsRow1);
        // 2. Zeile
        List<String> headerCellsRow2 = new ArrayList<>();
        headerCellsRow2.add("");
        headerCellsRow2.add("Strasse/Nr.");
        headerCellsRow2.add("PLZ/Ort");
        headerCellsRow2.add("AHV-Nummer");
        headerCellsRow2.add("Natel");
        headerCellsRow2.add("");
        header.add(headerCellsRow2);
        // 3. Spalte
        List<String> headerCellsRow3 = new ArrayList<>();
        headerCellsRow3.add("");
        headerCellsRow3.add("");
        headerCellsRow3.add("E-Mail");
        headerCellsRow3.add("");
        headerCellsRow3.add("");
        headerCellsRow3.add("");
        header.add(headerCellsRow3);

        // Inhalt
        List<Lehrkraft> lehrkraefte = lehrkraefteTableModel.getLehrkraefte();
        List<List<List<String>>> datasets = new ArrayList<>();
        int i = 0;
        for (Lehrkraft lehrkraft : lehrkraefte) {
            // Nur aktive Lehkräfte auflisten
            if (!lehrkraft.getAktiv()) {
                continue;
            }
            List<List<String>> dataset = new ArrayList<>();
            // Auf mehrere Zeilen aufzusplittende Felder:
            SplitStringIntoMultipleLinesCommand splitStringIntoMultipleLinesCommand = new SplitStringIntoMultipleLinesCommand(lehrkraft.getVertretungsmoeglichkeiten(), 27);
            splitStringIntoMultipleLinesCommand.execute();
            List<String> vertretungsmoeglichkeitenLines = splitStringIntoMultipleLinesCommand.getLines();
            // 1. Zeile
            List<String> cellsRow1 = new ArrayList<>();
            cellsRow1.add(Integer.toString(i + 1));
            cellsRow1.add(lehrkraft.getNachname());
            cellsRow1.add(lehrkraft.getVorname());
            cellsRow1.add(asString(lehrkraft.getGeburtsdatum()));
            cellsRow1.add(nullAsEmptyString(lehrkraft.getFestnetz()));
            if (!vertretungsmoeglichkeitenLines.isEmpty()) {
                cellsRow1.add(vertretungsmoeglichkeitenLines.get(0));
            } else {
                cellsRow1.add("");
            }
            dataset.add(cellsRow1);

            // 2. Zeile
            List<String> cellsRow2 = new ArrayList<>();
            cellsRow2.add("");
            cellsRow2.add(lehrkraft.getAdresse().getStrHausnummer());
            cellsRow2.add(lehrkraft.getAdresse().getPlz() + " " + lehrkraft.getAdresse().getOrt());
            cellsRow2.add(lehrkraft.getAhvNummer());
            cellsRow2.add(nullAsEmptyString(lehrkraft.getNatel()));
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
            cellsRow3.add(nullAsEmptyString(lehrkraft.getEmail()));
            cellsRow3.add("");
            cellsRow3.add("");
            if (vertretungsmoeglichkeitenLines.size() > 2) {
                cellsRow3.add(vertretungsmoeglichkeitenLines.get(2));
            } else {
                cellsRow3.add("");
            }
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
