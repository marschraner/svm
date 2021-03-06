package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Elternmithilfe;
import ch.metzenthin.svm.domain.model.NachnameGratiskindFormatter;
import ch.metzenthin.svm.persistence.entities.Person;
import ch.metzenthin.svm.persistence.entities.Maercheneinteilung;
import ch.metzenthin.svm.persistence.entities.Schueler;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.ui.componentmodel.SchuelerSuchenTableModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static ch.metzenthin.svm.common.utils.Converter.nullAsEmptyString;

/**
 * @author Martin Schraner
 */
public class CreateElternmithilfeListeCommand extends CreateListeCommand {

    // input
    private final SchuelerSuchenTableModel schuelerSuchenTableModel;
    private final String titel;
    private final File outputFile;

    public CreateElternmithilfeListeCommand(SchuelerSuchenTableModel schuelerSuchenTableModel, String titel, File outputFile) {
        this.schuelerSuchenTableModel = schuelerSuchenTableModel;
        this.titel = titel;
        this.outputFile = outputFile;
    }

    @Override
    public void execute() {

        NachnameGratiskindFormatter nachnameGratiskindFormatter = new NachnameGratiskindFormatter();

        // Spaltenbreiten
        // ACHTUNG: Summe muss <= 11200 (wenn nicht anders möglich: <= 11500) sein!
        //          Bei > 11200 hinten schmalerer Rand!
        //          Bei > 11500 Spaltenbreite durch Inhalt beieinflusst!!!
        List<Integer> columnWidths = new ArrayList<>();
        columnWidths.add(2700);
        columnWidths.add(2900);
        columnWidths.add(1700);
        columnWidths.add(2100);
        columnWidths.add(900);

        // Bold / horiz. merged (Anzahl zu mergende Zellen; 0: kein Merging)::
        List<List<Boolean>> boldCells = new ArrayList<>();
        List<List<Integer>> mergedCells = new ArrayList<>();
        // 1. Zeile
        List<Boolean> boldRow1 = new ArrayList<>();
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
        mergedCells.add(mergedRow1);
        // 2. Zeile
        List<Boolean> boldRow2 = new ArrayList<>();
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
        mergedCells.add(mergedRow2);
        // 3. Zeile
        List<Boolean> boldRow3 = new ArrayList<>();
        boldRow3.add(false);
        boldRow3.add(false);
        boldRow3.add(false);
        boldRow3.add(false);
        boldRow3.add(false);
        boldCells.add(boldRow3);
        List<Integer> mergedRow3 = new ArrayList<>();
        mergedRow3.add(0);
        mergedRow3.add(2);
        mergedRow3.add(0);
        mergedRow3.add(0);
        mergedRow3.add(0);
        mergedCells.add(mergedRow3);

        // Maximale Anzahl Zeichen (wenn überschritten wird Schrift verkleinert),
        // wenn 0 nicht zu prüfen
        List<List<int[]>> maxLengths = new ArrayList<>();
        // 1. Zeile
        List<int[]> maxLengthsRow1 = new ArrayList<>();
        maxLengthsRow1.add(new int[]{25, 26, 27, 29, 31, 33});
        maxLengthsRow1.add(new int[]{26, 27, 28, 30, 32, 34});
        maxLengthsRow1.add(new int[]{15, 16, 17, 18, 19, 20});
        maxLengthsRow1.add(new int[]{0});
        maxLengthsRow1.add(new int[]{0});
        maxLengths.add(maxLengthsRow1);
        // 2. Zeile
        List<int[]> maxLengthsRow2 = new ArrayList<>();
        maxLengthsRow2.add(new int[]{25, 26, 27, 29, 31, 33});
        maxLengthsRow2.add(new int[]{26, 27, 28, 30, 32, 34});
        maxLengthsRow2.add(new int[]{15, 16, 17, 18, 19, 20});
        maxLengthsRow2.add(new int[]{0});
        maxLengthsRow2.add(new int[]{0});
        maxLengths.add(maxLengthsRow2);
        // 3. Zeile
        List<int[]> maxLengthsRow3 = new ArrayList<>();
        maxLengthsRow3.add(new int[]{0});
        maxLengthsRow3.add(new int[]{39, 40, 41, 42, 44, 46});
        maxLengthsRow3.add(new int[]{0});
        maxLengthsRow3.add(new int[]{0});
        maxLengthsRow3.add(new int[]{21, 22, 23, 24, 25, 27});
        maxLengths.add(maxLengthsRow3);

        // Header
        List<List<String>> header = new ArrayList<>();
        // 1. Zeile
        List<String> headerCellsRow1 = new ArrayList<>();
        headerCellsRow1.add("Name");
        headerCellsRow1.add("Vorname");
        headerCellsRow1.add("Festnetz");
        headerCellsRow1.add("Mithilfe");
        headerCellsRow1.add("Gruppe");
        header.add(headerCellsRow1);
        // 2. Zeile
        List<String> headerCellsRow2 = new ArrayList<>();
        headerCellsRow2.add("Strasse/Nr.");
        headerCellsRow2.add("PLZ/Ort");
        headerCellsRow2.add("Natel");
        headerCellsRow2.add("");
        headerCellsRow2.add("");
        header.add(headerCellsRow2);
        // 3. Spalte
        List<String> headerCellsRow3 = new ArrayList<>();
        headerCellsRow3.add("");
        headerCellsRow3.add("E-Mail");
        headerCellsRow3.add("");
        headerCellsRow3.add("");
        headerCellsRow3.add("");

        header.add(headerCellsRow3);

        // Inhalt
        Map<Schueler, Maercheneinteilung> maercheneinteilungen = schuelerSuchenTableModel.getMaercheneinteilungen();

        // Map mit Elternmithilfe als Key, damit nach Elternmithilfe sortiert werden kann
        Map<Person, Maercheneinteilung> maercheneinteilungenElternmithilfe = new TreeMap<>();
        for (Schueler schueler : schuelerSuchenTableModel.getSelektierteSchuelerList()) {
            Maercheneinteilung maercheneinteilung = maercheneinteilungen.get(schueler);
            if (maercheneinteilung == null || maercheneinteilung.getElternmithilfe() == null) {
                continue;
            }
            Person elternmithilfe;
            if (maercheneinteilung.getElternmithilfe() == Elternmithilfe.MUTTER) {
                elternmithilfe = schueler.getMutter();
            } else if (maercheneinteilung.getElternmithilfe() == Elternmithilfe.VATER) {
                elternmithilfe = schueler.getVater();
            } else {
                elternmithilfe = maercheneinteilung.getElternmithilfeDrittperson();
            }
            // Falls Elternteil nach Erfassen der Eltern-Mithilfe gelöscht wurde, kann Elternmithilfe null sein.
            if (elternmithilfe != null) {
                maercheneinteilungenElternmithilfe.put(elternmithilfe, maercheneinteilung);
            }
        }

        List<List<List<String>>> datasets = new ArrayList<>();
        for (Person elternmithilfe : maercheneinteilungenElternmithilfe.keySet()) {
            List<List<String>> dataset = new ArrayList<>();
            Maercheneinteilung maercheneinteilung = maercheneinteilungenElternmithilfe.get(elternmithilfe);
            // Auf mehrere Zeilen aufzusplittende Felder:
            List<String> elternmithilfeCodeLines = null;
            if (maercheneinteilung.getElternmithilfeCode() != null) {
                SplitStringIntoMultipleLinesCommand splitStringIntoMultipleLinesCommand = new SplitStringIntoMultipleLinesCommand(maercheneinteilung.getElternmithilfeCode().getBeschreibung(), 21, 3);
                splitStringIntoMultipleLinesCommand.execute();
                elternmithilfeCodeLines = splitStringIntoMultipleLinesCommand.getLines();
            }

            // 1. Zeile
            List<String> cellsRow1 = new ArrayList<>();
            // Wenn Elternmithilfe und Schüler unterschiedliche Nachnamen haben, soll zudem Nachname des Schülers angegeben werden
            String nachnameElternmithilfeUndGgfSchueler = elternmithilfe.getNachname();
            String nachnameSchueler = maercheneinteilung.getSchueler().getNachname();
            if (!nachnameSchueler.contains(nachnameElternmithilfeUndGgfSchueler) &&
                    !nachnameElternmithilfeUndGgfSchueler.contains(nachnameSchueler)) {
                nachnameElternmithilfeUndGgfSchueler += " " + nachnameSchueler;
            }
            cellsRow1.add(nachnameGratiskindFormatter.format(nachnameElternmithilfeUndGgfSchueler));
            cellsRow1.add(elternmithilfe.getVorname());
            cellsRow1.add(nullAsEmptyString(elternmithilfe.getFestnetz()));
            if (elternmithilfeCodeLines != null) {
                cellsRow1.add(elternmithilfeCodeLines.get(0));
            } else {
                cellsRow1.add("");
            }
            cellsRow1.add(maercheneinteilung.getGruppe().toString());
            dataset.add(cellsRow1);

            // 2. Zeile
            List<String> cellsRow2 = new ArrayList<>();
            cellsRow2.add(elternmithilfe.getAdresse().getStrHausnummer());
            cellsRow2.add(elternmithilfe.getAdresse().getPlz() + " " + elternmithilfe.getAdresse().getOrt());
            cellsRow2.add(nullAsEmptyString(elternmithilfe.getNatel()));
            cellsRow2.add("");
            if (elternmithilfeCodeLines != null && elternmithilfeCodeLines.size() > 1) {
                cellsRow2.add(elternmithilfeCodeLines.get(1));
            } else {
                cellsRow2.add("");
            }
            dataset.add(cellsRow2);

            // 3. Zeile
            List<String> cellsRow3 = new ArrayList<>();
            cellsRow3.add("");
            cellsRow3.add(nullAsEmptyString(elternmithilfe.getEmailToBeDisplayedInWord()));
            cellsRow3.add("");
            cellsRow3.add("");
            if (elternmithilfeCodeLines != null && elternmithilfeCodeLines.size() > 2) {
                cellsRow3.add(elternmithilfeCodeLines.get(2));
            } else {
                cellsRow3.add("");
            }
            dataset.add(cellsRow3);

            // Einzelner Datensatz der Liste von Datensätzen hinzufügen
            datasets.add(dataset);
        }

        // Tabelle erzeugen
        Semester semester = schuelerSuchenTableModel.getSemester();
        String maerchenspielSchuljahr = "Märchenspiel " + semester.getSchuljahr();
        String titel1 = "Kinder- und Jugendtheater Metzenthin AG                                      " + maerchenspielSchuljahr;
        CreateWordTableCommand createWordTableCommand = new CreateWordTableCommand(header, datasets, columnWidths, boldCells, mergedCells, maxLengths, titel1, titel, outputFile, 850, 1, 1000, 1, 0, 0);
        createWordTableCommand.execute();

        result = Result.LISTE_ERFOLGREICH_ERSTELLT;
    }

}
