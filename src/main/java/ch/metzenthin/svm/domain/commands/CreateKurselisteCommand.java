package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.entities.Kurs;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.ui.componentmodel.KurseTableModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static ch.metzenthin.svm.common.utils.Converter.asString;

/**
 * @author Martin Schraner
 */
public class CreateKurselisteCommand extends CreateListeCommand {

    // input
    private final KurseTableModel kurseTableModel;
    private final String titel;
    private final File outputFile;

    public CreateKurselisteCommand(KurseTableModel kurseTableModel, String titel, File outputFile) {
        this.kurseTableModel = kurseTableModel;
        this.titel = titel;
        this.outputFile = outputFile;
    }

    @Override
    public void execute() {

        // Spaltenbreiten
        List<Integer> columnWidths = new ArrayList<>();
        columnWidths.add(0);
        columnWidths.add(2100);
        columnWidths.add(2100);
        columnWidths.add(1900);
        columnWidths.add(2800);
        columnWidths.add(0);

        // Bold / horiz. merged:
        List<List<Boolean>> boldCells = new ArrayList<>();
        List<List<Integer>> mergedCells = new ArrayList<>();
        // 1. Zeile
        List<Boolean> boldRow1 = new ArrayList<>();
        boldRow1.add(false);
        boldRow1.add(false);
        boldRow1.add(false);
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

        // Maximale Anzahl Zeichen (wenn überschritten wird Schrift verkleinert),
        // wenn 0 nicht zu prüfen
        List<List<int[]>> maxLengths = new ArrayList<>();
        // 1. Zeile
        List<int[]> maxLengthsRow1 = new ArrayList<>();
        maxLengthsRow1.add(new int[]{0});
        maxLengthsRow1.add(new int[]{0});
        maxLengthsRow1.add(new int[]{22, 23, 24, 25, 26, 28});
        maxLengthsRow1.add(new int[]{0});
        maxLengthsRow1.add(new int[]{28, 29, 30, 31, 32, 34});
        maxLengthsRow1.add(new int[]{0});
        maxLengths.add(maxLengthsRow1);
        // 2. Zeile
        List<int[]> maxLengthsRow2 = new ArrayList<>();
        maxLengthsRow2.add(new int[]{0});
        maxLengthsRow2.add(new int[]{20, 21, 22, 23, 24, 26});
        maxLengthsRow2.add(new int[]{22, 23, 24, 25, 26, 28});
        maxLengthsRow2.add(new int[]{0});
        maxLengthsRow2.add(new int[]{28, 29, 30, 31, 32, 34});
        maxLengthsRow2.add(new int[]{20, 21, 22, 23, 24, 26});
        maxLengths.add(maxLengthsRow2);

        // Header
        List<List<String>> header = new ArrayList<>();
        // 1. Zeile
        List<String> headerCellsRow1 = new ArrayList<>();
        headerCellsRow1.add("");
        headerCellsRow1.add("Kurstyp");
        headerCellsRow1.add("Alter");
        headerCellsRow1.add("Tag");
        headerCellsRow1.add("Ort");
        headerCellsRow1.add("Bemerkungen");
        header.add(headerCellsRow1);
        // 2. Zeile
        List<String> headerCellsRow2 = new ArrayList<>();
        headerCellsRow2.add("");
        headerCellsRow2.add("");
        headerCellsRow2.add("Stufe");
        headerCellsRow2.add("Zeit");
        headerCellsRow2.add("Leitung");
        headerCellsRow2.add("");
        header.add(headerCellsRow2);

        // Inhalt
        List<Kurs> kurse = kurseTableModel.getKurse();

        List<List<List<String>>> datasets = new ArrayList<>();
        int i = 0;
        for (Kurs kurs : kurse) {
            List<List<String>> dataset = new ArrayList<>();
            // Auf mehrere Zeilen aufzusplittende Felder:
            SplitStringIntoMultipleLinesCommand splitStringIntoMultipleLinesCommand = new SplitStringIntoMultipleLinesCommand(kurs.getKurstyp().getBezeichnung(), 20, 2);
            splitStringIntoMultipleLinesCommand.execute();
            List<String> kurstypLines = splitStringIntoMultipleLinesCommand.getLines();
            splitStringIntoMultipleLinesCommand = new SplitStringIntoMultipleLinesCommand(kurs.getBemerkungen(), 20, 2);
            splitStringIntoMultipleLinesCommand.execute();
            List<String> bemerkungenLines = splitStringIntoMultipleLinesCommand.getLines();
            // 1. Zeile
            List<String> cellsRow1 = new ArrayList<>();
            cellsRow1.add(Integer.toString(i + 1));
            cellsRow1.add(kurstypLines.get(0));
            cellsRow1.add(kurs.getAltersbereich());
            cellsRow1.add(kurs.getWochentag().toString());
            cellsRow1.add(kurs.getKursort().getBezeichnung());
            if (!bemerkungenLines.isEmpty()) {
                cellsRow1.add(bemerkungenLines.get(0));
            } else {
                cellsRow1.add("");
            }
            dataset.add(cellsRow1);

            // 2. Zeile
            List<String> cellsRow2 = new ArrayList<>();
            cellsRow2.add("");
            if (kurstypLines.size() > 1) {
                cellsRow2.add(kurstypLines.get(1));
            } else {
                cellsRow2.add("");
            }
            cellsRow2.add(kurs.getStufe());
            cellsRow2.add(asString(kurs.getZeitBeginn()) + " - " + asString(kurs.getZeitEnde()));
            cellsRow2.add(kurs.getLehrkraefteShortAsStr());
            if (bemerkungenLines.size() > 1) {
                cellsRow2.add(bemerkungenLines.get(1));
            } else {
                cellsRow2.add("");
            }
            dataset.add(cellsRow2);

            // Einzelner Datensatz der Liste von Datensätzen hinzufügen
            datasets.add(dataset);
            i++;
        }

        // Tabelle erzeugen
        String schuljahrSemester = "";
        if (kurse.size() > 0) {
            Semester semester = kurse.get(0).getSemester();
            schuljahrSemester = "Schuljahr " + semester.getSchuljahr() + ", " + semester.getSemesterbezeichnung();
        }
        String titel1 = "Kinder- und Jugendtheater Metzenthin AG                                 " + schuljahrSemester;
        CreateWordTableCommand createWordTableCommand = new CreateWordTableCommand(header, datasets, columnWidths, boldCells, mergedCells, maxLengths, titel1, titel, outputFile);
        createWordTableCommand.execute();

        result = Result.LISTE_ERFOLGREICH_ERSTELLT;
    }

}
