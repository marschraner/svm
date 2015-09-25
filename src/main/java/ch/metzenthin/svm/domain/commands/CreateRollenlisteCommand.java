package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.utils.StringNumberComparator;
import ch.metzenthin.svm.common.dataTypes.Elternmithilfe;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Maercheneinteilung;
import ch.metzenthin.svm.persistence.entities.Schueler;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.ui.componentmodel.SchuelerSuchenTableModel;

import java.io.File;
import java.util.*;

import static ch.metzenthin.svm.common.utils.Converter.nullAsEmptyString;
import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;

/**
 * @author Martin Schraner
 */
public class CreateRollenlisteCommand extends CreateListeCommand {

    // input
    private final SchuelerSuchenTableModel schuelerSuchenTableModel;
    private final String titel;
    private final File outputFile;

    public CreateRollenlisteCommand(SchuelerSuchenTableModel schuelerSuchenTableModel, String titel, File outputFile) {
        this.schuelerSuchenTableModel = schuelerSuchenTableModel;
        this.titel = titel;
        this.outputFile = outputFile;
    }

    @Override
    public void execute() {

        // Spaltenbreiten
        // ACHTUNG: Summe muss <= 11200 (wenn nicht anders möglich: <= 11500) sein (bei linkem Default-Rand von 650)!
        //          Bei > 11200 hinten schmalerer Rand!
        //          Bei > 11500 Spaltenbreite durch Inhalt beieinflusst!!!
        // Hier linker-Rand auf 850 gesetzt, d.h. von obigen Werten muss 200 subtrahiert werden.
        List<Integer> columnWidths = new ArrayList<>();
        columnWidths.add(2300);
        columnWidths.add(2500);
        columnWidths.add(1400);
        columnWidths.add(1400);
        columnWidths.add(2100);
        columnWidths.add(1600);

        // Bold / horiz. merged (Anzahl zu mergende Zellen; 0: kein Merging):
        List<List<Boolean>> boldCells = new ArrayList<>();
        List<List<Integer>> mergedCells = new ArrayList<>();
        // 1. Zeile
        List<Boolean> boldRow1 = new ArrayList<>();
        boldRow1.add(true);
        boldRow1.add(true);
        boldRow1.add(false);
        boldRow1.add(false);
        boldRow1.add(true);
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
        mergedRow3.add(2);
        mergedRow3.add(0);
        mergedRow3.add(0);
        mergedRow3.add(0);
        mergedRow3.add(0);
        mergedCells.add(mergedRow3);

        // Maximale Anzahl Zeichen (wenn überschritten wird Schrift verkleinert),
        // wenn 0 nicht zu prüfen
        List<List<int[]>> maxLengths = new ArrayList<>();
        // 1. Zeile
        List<int[]> maxLengthsRow1 = new ArrayList<>();
        maxLengthsRow1.add(new int[]{21, 22, 23, 24, 25, 27});
        maxLengthsRow1.add(new int[]{22, 23, 24, 25, 26, 28});
        maxLengthsRow1.add(new int[]{0});
        maxLengthsRow1.add(new int[]{0});
        maxLengthsRow1.add(new int[]{20, 21, 22, 23, 24, 26});
        maxLengthsRow1.add(new int[]{18, 19, 20, 21, 22, 23});
        maxLengths.add(maxLengthsRow1);
        // 2. Zeile
        List<int[]> maxLengthsRow2 = new ArrayList<>();
        maxLengthsRow2.add(new int[]{21, 22, 23, 24, 25, 27});
        maxLengthsRow2.add(new int[]{22, 23, 24, 25, 26, 28});
        maxLengthsRow2.add(new int[]{0});
        maxLengthsRow2.add(new int[]{0});
        maxLengthsRow2.add(new int[]{20, 21, 22, 23, 24, 26});
        maxLengthsRow2.add(new int[]{18, 19, 20, 21, 22, 23});
        maxLengths.add(maxLengthsRow2);
        // 3. Zeile
        List<int[]> maxLengthsRow3 = new ArrayList<>();
        maxLengthsRow3.add(new int[]{0});
        maxLengthsRow3.add(new int[]{38, 39, 40, 41, 43, 45});
        maxLengthsRow3.add(new int[]{0});
        maxLengthsRow3.add(new int[]{0});
        maxLengthsRow3.add(new int[]{20, 21, 22, 23, 24, 26});
        maxLengthsRow3.add(new int[]{18, 19, 20, 21, 22, 23});
        maxLengths.add(maxLengthsRow3);

        // Header
        List<List<String>> header = new ArrayList<>();
        // 1. Zeile
        List<String> headerCellsRow1 = new ArrayList<>();
        headerCellsRow1.add("Name");
        headerCellsRow1.add("Vorname");
        headerCellsRow1.add("Gruppe");
        headerCellsRow1.add("Natel Mutter");
        headerCellsRow1.add("Rolle(n)");
        headerCellsRow1.add("Bilder");
        header.add(headerCellsRow1);
        // 2. Zeile
        List<String> headerCellsRow2 = new ArrayList<>();
        headerCellsRow2.add("Strasse/Nr.");
        headerCellsRow2.add("PLZ/Ort");
        headerCellsRow2.add("Festnetz");
        headerCellsRow2.add("Natel Vater");
        headerCellsRow2.add("");
        headerCellsRow2.add("");
        header.add(headerCellsRow2);
        // 3. Spalte
        List<String> headerCellsRow3 = new ArrayList<>();
        headerCellsRow3.add("");
        headerCellsRow3.add("E-Mail");
        headerCellsRow3.add("");
        headerCellsRow3.add("Natel Schüler");
        headerCellsRow3.add("");
        headerCellsRow3.add("");
        header.add(headerCellsRow3);

        // Inhalt
        Map<Schueler, Maercheneinteilung> maercheneinteilungen = schuelerSuchenTableModel.getMaercheneinteilungen();
        List<List<List<String>>> datasets = new ArrayList<>();
        // Wenn nach Rollen gesucht wurde, muss nach Rollen sortiert werden, sonst nach Schülern
        Set<Schueler> keys;
        if (schuelerSuchenTableModel.isNachRollenGesucht()) {
            // Wenn nach Rollen gesucht wurde, gibt es keine Keys mit leeren Values
            maercheneinteilungen = sortMaercheneinteilungenByRollen(maercheneinteilungen);
            keys = maercheneinteilungen.keySet();
        } else {
            // Sortierung nach Keys
            keys = new TreeSet<>(maercheneinteilungen.keySet());
        }
        for (Schueler schueler : keys) {
            Maercheneinteilung maercheneinteilung = maercheneinteilungen.get(schueler);
            if (maercheneinteilung == null) {
                continue;
            }
            List<List<String>> dataset = new ArrayList<>();

            // Auf mehrere Zeilen aufzusplittende Felder:
            // Rolle1 / Bilder Rolle1
            int maxLines;
            if (maercheneinteilung.getRolle2() == null && maercheneinteilung.getBilderRolle2() == null && maercheneinteilung.getRolle3() == null && maercheneinteilung.getBilderRolle3() == null) {
                maxLines = 3;
            } else {
                maxLines = 1;
            }
            List<String> bilderRolle1Lines = null;
            SplitStringIntoMultipleLinesCommand splitStringIntoMultipleLinesCommand = new SplitStringIntoMultipleLinesCommand(maercheneinteilung.getRolle1(), 18, maxLines);
            splitStringIntoMultipleLinesCommand.execute();
            List<String> rolle1Lines = splitStringIntoMultipleLinesCommand.getLines();
            if (maercheneinteilung.getBilderRolle1() != null) {
                splitStringIntoMultipleLinesCommand = new SplitStringIntoMultipleLinesCommand(maercheneinteilung.getBilderRolle1(), 18, maxLines);
                splitStringIntoMultipleLinesCommand.execute();
                bilderRolle1Lines = splitStringIntoMultipleLinesCommand.getLines();
            }
            if (maercheneinteilung.getRolle3() == null && maercheneinteilung.getBilderRolle3() == null) {
                maxLines = 2;
            } else {
                maxLines = 1;
            }
            // Rolle 2 / Bilder Rolle 2
            List<String> rolle2Lines = null;
            List<String> bilderRolle2Lines = null;
            if (maercheneinteilung.getRolle2() != null) {
                splitStringIntoMultipleLinesCommand = new SplitStringIntoMultipleLinesCommand(maercheneinteilung.getRolle2(), 18, maxLines);
                splitStringIntoMultipleLinesCommand.execute();
                rolle2Lines = splitStringIntoMultipleLinesCommand.getLines();
            }
            if (maercheneinteilung.getBilderRolle2() != null) {
                splitStringIntoMultipleLinesCommand = new SplitStringIntoMultipleLinesCommand(maercheneinteilung.getBilderRolle2(), 18, maxLines);
                splitStringIntoMultipleLinesCommand.execute();
                bilderRolle2Lines = splitStringIntoMultipleLinesCommand.getLines();
            }

            // 1. Zeile
            List<String> cellsRow1 = new ArrayList<>();
            cellsRow1.add(schueler.getNachname());
            cellsRow1.add(schueler.getVorname());
            cellsRow1.add(maercheneinteilung.getGruppe().toString());
            if (schueler.getMutter() != null) {
                cellsRow1.add(nullAsEmptyString(schueler.getMutter().getNatel()));
            } else {
                cellsRow1.add("");
            }
            cellsRow1.add(rolle1Lines.get(0));
            if (bilderRolle1Lines != null) {
                cellsRow1.add(bilderRolle1Lines.get(0));
            } else {
                cellsRow1.add("");
            }
            dataset.add(cellsRow1);

            // 2. Zeile
            List<String> cellsRow2 = new ArrayList<>();
            cellsRow2.add(schueler.getAdresse().getStrHausnummer());
            cellsRow2.add(schueler.getAdresse().getPlz() + " " + schueler.getAdresse().getOrt());
            cellsRow2.add(nullAsEmptyString(schueler.getFestnetz()));
            if (schueler.getVater() != null) {
                cellsRow2.add(nullAsEmptyString(schueler.getVater().getNatel()));
            } else {
                cellsRow2.add("");
            }
            if (rolle2Lines != null) {
                cellsRow2.add(rolle2Lines.get(0));
            } else if (rolle1Lines.size() > 1) {
                cellsRow2.add(rolle1Lines.get(1));
            } else {
                cellsRow2.add("");
            }
            if (bilderRolle2Lines != null) {
                cellsRow2.add(bilderRolle2Lines.get(0));
            } else if (bilderRolle1Lines != null && bilderRolle1Lines.size() > 1) {
                cellsRow2.add(bilderRolle1Lines.get(1));
            } else {
                cellsRow2.add("");
            }
            dataset.add(cellsRow2);

            // 3. Zeile
            List<String> cellsRow3 = new ArrayList<>();
            cellsRow3.add("");
            String email = "";
            // Wenn vorhanden Email des Schülers, sonst der Elternmithilfe (falls nicht Drittperson), sonst der Mutter, sonst des Vaters, sonst Elternmithilfe Drittperson; andernfalls leer
            Angehoeriger elternmithilfe = null;
            if (maercheneinteilung.getElternmithilfe() != null && maercheneinteilung.getElternmithilfe() != Elternmithilfe.DRITTPERSON) {
                elternmithilfe = (maercheneinteilung.getElternmithilfe() == Elternmithilfe.MUTTER ? schueler.getMutter() : schueler.getVater());
            }
            if (checkNotEmpty(schueler.getEmail())) {
                email = schueler.getEmail();
            } else if (elternmithilfe != null && checkNotEmpty(elternmithilfe.getEmail())) {
                email = elternmithilfe.getEmail();
            } else if (schueler.getMutter() != null && checkNotEmpty(schueler.getMutter().getEmail())) {
                email = schueler.getMutter().getEmail();
            } else if (schueler.getVater() != null && checkNotEmpty(schueler.getVater().getEmail())) {
                email = schueler.getVater().getEmail();
            } else if (maercheneinteilung.getElternmithilfeDrittperson() != null && checkNotEmpty(maercheneinteilung.getElternmithilfeDrittperson().getEmail())) {
                email = maercheneinteilung.getElternmithilfeDrittperson().getEmail();
            }
            cellsRow3.add(email);
            cellsRow3.add("");
            cellsRow3.add(nullAsEmptyString(schueler.getNatel()));
            if (maercheneinteilung.getRolle3() != null) {
                cellsRow3.add(maercheneinteilung.getRolle3());
            } else if (rolle1Lines.size() > 2) {
                cellsRow3.add(rolle1Lines.get(2));
            } else if (rolle2Lines != null && rolle2Lines.size() > 1) {
                cellsRow3.add(rolle2Lines.get(1));
            } else {
                cellsRow3.add("");
            }
            if (maercheneinteilung.getBilderRolle3() != null) {
                cellsRow3.add(maercheneinteilung.getBilderRolle3());
            } else if (bilderRolle1Lines != null && bilderRolle1Lines.size() > 2) {
                cellsRow3.add(bilderRolle1Lines.get(2));
            } else if (bilderRolle2Lines != null && bilderRolle2Lines.size() > 1) {
                cellsRow3.add(bilderRolle2Lines.get(1));
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
        String titel1 = "Kinder- und Jugendtheater Metzenthin AG                                             " + maerchenspielSchuljahr;
        CreateWordTableCommand createWordTableCommand = new CreateWordTableCommand(header, datasets, columnWidths, boldCells, mergedCells, maxLengths, titel1, titel, outputFile, 850, 1, 850, 1, 0, 0);
        createWordTableCommand.execute();

        result = Result.LISTE_ERFOLGREICH_ERSTELLT;
    }



    private Map<Schueler, Maercheneinteilung> sortMaercheneinteilungenByRollen(Map<Schueler, Maercheneinteilung> maercheneinteilungen) {
        List<Map.Entry<Schueler, Maercheneinteilung>> list = new LinkedList<>(maercheneinteilungen.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Schueler, Maercheneinteilung>>() {
            @Override
            public int compare(Map.Entry<Schueler, Maercheneinteilung> o1, Map.Entry<Schueler, Maercheneinteilung> o2) {
                Comparator<String> stringNumberComparator = new StringNumberComparator();
                return stringNumberComparator.compare(o1.getValue().getRolle1(), o2.getValue().getRolle1());
            }
        });

        Map<Schueler, Maercheneinteilung> result = new LinkedHashMap<>();
        for (Map.Entry<Schueler, Maercheneinteilung> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

}
