package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.entities.Kurs;
import ch.metzenthin.svm.persistence.entities.Schueler;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.ui.componentmodel.SchuelerSuchenTableModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ch.metzenthin.svm.common.utils.Converter.asString;
import static ch.metzenthin.svm.common.utils.Converter.nullAsEmptyString;
import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;

/**
 * @author Martin Schraner
 */
public class CreateSchuelerAdresslisteCommand extends CreateListeCommand {

    // input
    private final SchuelerSuchenTableModel schuelerSuchenTableModel;
    private final String titel;
    private final File outputFile;

    public CreateSchuelerAdresslisteCommand(SchuelerSuchenTableModel schuelerSuchenTableModel, String titel, File outputFile) {
        this.schuelerSuchenTableModel = schuelerSuchenTableModel;
        this.titel = titel;
        this.outputFile = outputFile;
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public void execute() {

        // Spaltenbreiten
        // ACHTUNG: Summe muss <= 11200 (wenn nicht anders möglich: <= 11500) sein!
        //          Bei > 11200 hinten schmalerer Rand!
        //          Bei > 11500 Spaltenbreite durch Inhalt beeinflusst!!!
        List<Integer> columnWidths = new ArrayList<>();
        columnWidths.add(0);  // entspricht einer Breite von max. 550 (wenn 3-stellig)
        columnWidths.add(2300);
        columnWidths.add(2500);
        columnWidths.add(1400);
        columnWidths.add(1400);
        columnWidths.add(1850);
        columnWidths.add(1200);

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
        boldRow1.add(false);
        boldCells.add(boldRow1);
        List<Integer> mergedRow1 = new ArrayList<>();
        mergedRow1.add(0);
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
        boldRow2.add(false);
        boldCells.add(boldRow2);
        List<Integer> mergedRow2 = new ArrayList<>();
        mergedRow2.add(0);
        mergedRow2.add(0);
        mergedRow2.add(0);
        mergedRow2.add(0);
        mergedRow2.add(0);
        mergedRow2.add(2);
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
        boldRow3.add(false);
        boldCells.add(boldRow3);
        List<Integer> mergedRow3 = new ArrayList<>();
        mergedRow3.add(0);
        mergedRow3.add(0);
        mergedRow3.add(2);
        mergedRow3.add(0);
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
        maxLengthsRow1.add(new int[]{0});
        maxLengthsRow1.add(new int[]{13, 13, 13, 14, 16, 18});
        maxLengthsRow1.add(new int[]{17, 18, 19, 20, 21, 23});
        maxLengthsRow1.add(new int[]{0});
        maxLengths.add(maxLengthsRow1);
        // 2. Zeile
        List<int[]> maxLengthsRow2 = new ArrayList<>();
        maxLengthsRow2.add(new int[]{0});
        maxLengthsRow2.add(new int[]{22, 23, 24, 25, 26, 28});
        maxLengthsRow2.add(new int[]{23, 24, 25, 26, 27, 29});
        maxLengthsRow2.add(new int[]{13, 13, 13, 14, 16, 18});
        maxLengthsRow2.add(new int[]{13, 13, 13, 14, 16, 18});
        maxLengthsRow2.add(new int[]{0});
        maxLengthsRow2.add(new int[]{0});
        maxLengths.add(maxLengthsRow2);
        // 3. Zeile
        List<int[]> maxLengthsRow3 = new ArrayList<>();
        maxLengthsRow3.add(new int[]{0});
        maxLengthsRow3.add(new int[]{0});
        maxLengthsRow3.add(new int[]{38, 39, 40, 41, 43, 45});
        maxLengthsRow3.add(new int[]{0});
        maxLengthsRow3.add(new int[]{13, 13, 13, 14, 16, 18});
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
        headerCellsRow1.add("Geb.Datum");
        headerCellsRow1.add("Natel Mutter");
        headerCellsRow1.add("Lehrkraft");
        headerCellsRow1.add("Eintritt");
        header.add(headerCellsRow1);
        // 2. Zeile
        List<String> headerCellsRow2 = new ArrayList<>();
        headerCellsRow2.add("");
        headerCellsRow2.add("Strasse/Nr.");
        headerCellsRow2.add("PLZ/Ort");
        headerCellsRow2.add("Festnetz");
        headerCellsRow2.add("Natel Vater");
        headerCellsRow2.add("Tag");
        headerCellsRow2.add("");
        header.add(headerCellsRow2);
        // 3. Spalte
        List<String> headerCellsRow3 = new ArrayList<>();
        headerCellsRow3.add("");
        headerCellsRow3.add("");
        headerCellsRow3.add("E-Mail");
        headerCellsRow3.add("");
        headerCellsRow3.add("Natel Schüler");
        headerCellsRow3.add("Zeit");
        headerCellsRow3.add("");
        header.add(headerCellsRow3);

        // Inhalt
        Map<Schueler, List<Kurs>> kurse = schuelerSuchenTableModel.getKurse();
        List<List<List<String>>> datasets = new ArrayList<>();
        int i = 0;
        for (Schueler schueler : schuelerSuchenTableModel.getSelektierteSchuelerList()) {
            List<Kurs> schuelerKurse = kurse.get(schueler);
            int j = 0;
            do {
                List<List<String>> dataset = new ArrayList<>();
                // 1. Zeile
                List<String> cellsRow1 = new ArrayList<>();
                cellsRow1.add(Integer.toString(i + 1));
                cellsRow1.add(schueler.getNachname());
                cellsRow1.add(schueler.getVorname());
                cellsRow1.add(asString(schueler.getGeburtsdatum()));
                if (schueler.getMutter() != null) {
                    cellsRow1.add(nullAsEmptyString(schueler.getMutter().getNatel()));
                } else {
                    cellsRow1.add("");
                }
                String lehrkraft2 = "";
                if (schuelerKurse != null && schuelerKurse.size() > 0) {
                    Kurs kurs = schuelerKurse.get(j);
                    String lehrkraft1 = kurs.getLehrkraefte().get(0).toStringShort();
                    if (kurs.getLehrkraefte().size() == 2) {
                        lehrkraft1 = lehrkraft1 + " /";
                        lehrkraft2 = kurs.getLehrkraefte().get(1).toStringShort();
                    }
                    cellsRow1.add(lehrkraft1);
                } else {
                    cellsRow1.add("");
                }
                cellsRow1.add(asString(schueler.getAnmeldungen().get(schueler.getAnmeldungen().size() - 1).getAnmeldedatum()));
                dataset.add(cellsRow1);

                // 2. Zeile
                List<String> cellsRow2 = new ArrayList<>();
                cellsRow2.add("");
                cellsRow2.add(schueler.getAdresse().getStrHausnummer());
                cellsRow2.add(schueler.getAdresse().getPlz() + " " + schueler.getAdresse().getOrt());
                cellsRow2.add(nullAsEmptyString(schueler.getFestnetz()));
                if (schueler.getVater() != null) {
                    cellsRow2.add(nullAsEmptyString(schueler.getVater().getNatel()));
                } else {
                    cellsRow2.add("");
                }
                if (schuelerKurse != null && schuelerKurse.size() > 0) {
                    Kurs kurs = schuelerKurse.get(j);
                    if (!lehrkraft2.isEmpty()) {
                        cellsRow2.add(lehrkraft2);
                    } else {
                        cellsRow2.add(kurs.getWochentag().toString());
                    }
                } else {
                    cellsRow2.add("");
                }
                dataset.add(cellsRow2);

                // 3. Zeile
                List<String> cellsRow3 = new ArrayList<>();
                cellsRow3.add("");
                cellsRow3.add("");
                String email = "";
                // Wenn vorhanden E-Mail des Schülers, sonst der Mutter, sonst des Vaters; andernfalls leer
                if (checkNotEmpty(schueler.getEmail())) {
                    email = schueler.getEmailToBeDisplayedInWord();
                } else if (schueler.getMutter() != null && checkNotEmpty(schueler.getMutter().getEmail())) {
                    email = schueler.getMutter().getEmailToBeDisplayedInWord();
                } else if (schueler.getVater() != null && checkNotEmpty(schueler.getVater().getEmail())) {
                    email = schueler.getVater().getEmailToBeDisplayedInWord();
                }
                cellsRow3.add(email);
                cellsRow3.add("");
                cellsRow3.add(nullAsEmptyString(schueler.getNatel()));
                if (schuelerKurse != null && schuelerKurse.size() > 0) {
                    Kurs kurs = schuelerKurse.get(j);
                    String kursDauer = asString(kurs.getZeitBeginn()) + " - " + asString(kurs.getZeitEnde());
                    if (!lehrkraft2.isEmpty()) {
                        cellsRow3.add(kurs.getWochentag().toString() + " " + kursDauer);
                    } else {
                        cellsRow3.add(kursDauer);
                    }
                } else {
                    cellsRow3.add("");
                }
                dataset.add(cellsRow3);

                // Einzelner Datensatz der Liste von Datensätzen hinzufügen
                datasets.add(dataset);
                i++;
                j++;
            } while (schuelerKurse != null && j < schuelerKurse.size());
        }

        // Tabelle erzeugen
        Semester semester = schuelerSuchenTableModel.getSemester();
        String schuljahrSemester = (semester == null) ? "" : "Schuljahr " + semester.getSchuljahr() + ", " + semester.getSemesterbezeichnung();
        String titel1 = "Kinder- und Jugendtheater Metzenthin AG                                 " + schuljahrSemester;
        CreateWordTableCommand createWordTableCommand = new CreateWordTableCommand(header, datasets, columnWidths, boldCells, mergedCells, maxLengths, titel1, titel, outputFile);
        createWordTableCommand.execute();

        result = Result.LISTE_ERFOLGREICH_ERSTELLT;
    }

}
