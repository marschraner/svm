package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Listentyp;
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
public class CreateMitarbeiterAdresslisteCommand extends CreateListeCommand {

    // input
    private final MitarbeitersTableModel mitarbeitersTableModel;
    private final String titel;
    private final File outputFile;
    private Listentyp listentyp;

    public CreateMitarbeiterAdresslisteCommand(MitarbeitersTableModel mitarbeitersTableModel, String titel, File outputFile, Listentyp listentyp) {
        this.mitarbeitersTableModel = mitarbeitersTableModel;
        this.titel = titel;
        this.outputFile = outputFile;
        this.listentyp = listentyp;
    }

    @Override
    public void execute() {

        // Spaltenbreiten
        // ACHTUNG: Summe muss <= 11200 (wenn nicht anders möglich: <= 11500) sein!
        //          Bei > 11200 hinten schmalerer Rand!
        //          Bei > 11500 Spaltenbreite durch Inhalt beieinflusst!!!
        List<Integer> columnWidths = new ArrayList<>();
        columnWidths.add(0);  // enspricht einer Breite von max 550 (wenn 3-stellig)
        columnWidths.add(2700);
        columnWidths.add(2900);
        columnWidths.add(1700);
        columnWidths.add(3500);

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

        // Maximale Anzahl Zeichen (wenn überschritten wird Schrift verkleinert),
        // wenn 0 nicht zu prüfen
        List<List<int[]>> maxLengths = new ArrayList<>();
        // 1. Zeile
        List<int[]> maxLengthsRow1 = new ArrayList<>();
        maxLengthsRow1.add(new int[]{0});
        maxLengthsRow1.add(new int[]{23, 24, 25, 26, 27, 29});
        maxLengthsRow1.add(new int[]{24, 25, 26, 27, 28, 30});
        maxLengthsRow1.add(new int[]{15, 16, 17, 18, 19, 20});
        maxLengthsRow1.add(new int[]{35, 36, 37, 38, 39, 41});
        maxLengths.add(maxLengthsRow1);
        // 2. Zeile
        List<int[]> maxLengthsRow2 = new ArrayList<>();
        maxLengthsRow2.add(new int[]{0});
        maxLengthsRow2.add(new int[]{24, 25, 26, 27, 28, 30});
        maxLengthsRow2.add(new int[]{25, 26, 27, 28, 29, 31});
        maxLengthsRow2.add(new int[]{15, 16, 17, 18, 19, 20});
        maxLengthsRow2.add(new int[]{35, 36, 37, 38, 39, 41});
        maxLengths.add(maxLengthsRow2);

        // Header
        List<List<String>> header = new ArrayList<>();
        // 1. Zeile
        List<String> headerCellsRow1 = new ArrayList<>();
        headerCellsRow1.add("");
        headerCellsRow1.add("Name");
        headerCellsRow1.add("Vorname");
        headerCellsRow1.add("Festnetz");
        String title;
        if (listentyp == Listentyp.MITARBEITER_ADRESSLISTE_MIT_GEBURTSDATUM) {
            title = "Geburtsdatum";
        } else {
            title = "E-Mail";
        }
        headerCellsRow1.add(title);
        header.add(headerCellsRow1);
        // 2. Zeile
        List<String> headerCellsRow2 = new ArrayList<>();
        headerCellsRow2.add("");
        headerCellsRow2.add("Strasse/Nr.");
        headerCellsRow2.add("PLZ/Ort");
        headerCellsRow2.add("Natel");
        title = "";
        if (listentyp == Listentyp.MITARBEITER_ADRESSLISTE_MIT_GEBURTSDATUM) {
            title = "E-Mail";
        }
        headerCellsRow2.add(title);
        header.add(headerCellsRow2);

        // Inhalt
        List<List<List<String>>> datasets = new ArrayList<>();
        int i = 0;
        for (Mitarbeiter mitarbeiter : mitarbeitersTableModel.getZuExportierendeMitarbeiters()) {
            List<List<String>> dataset = new ArrayList<>();
            // 1. Zeile
            List<String> cellsRow1 = new ArrayList<>();
            cellsRow1.add(Integer.toString(i + 1));
            cellsRow1.add(mitarbeiter.getNachname());
            cellsRow1.add(mitarbeiter.getVorname());
            cellsRow1.add(nullAsEmptyString(mitarbeiter.getFestnetz()));
            String value;
            if (listentyp == Listentyp.MITARBEITER_ADRESSLISTE_MIT_GEBURTSDATUM) {
                value = nullAsEmptyString(asString(mitarbeiter.getGeburtsdatum()));
            } else {
                value = nullAsEmptyString(mitarbeiter.getEmailToBeDisplayedInWord());
            }
            cellsRow1.add(value);
            dataset.add(cellsRow1);

            // 2. Zeile
            List<String> cellsRow2 = new ArrayList<>();
            cellsRow2.add("");
            cellsRow2.add(mitarbeiter.getAdresse() == null ? "" : nullAsEmptyString(mitarbeiter.getAdresse().getStrHausnummer()));
            cellsRow2.add(mitarbeiter.getAdresse() == null ? "" : nullAsEmptyString(mitarbeiter.getAdresse().getPlz() + " " + mitarbeiter.getAdresse().getOrt()));
            cellsRow2.add(nullAsEmptyString(mitarbeiter.getNatel()));
            value = "";
            if (listentyp == Listentyp.MITARBEITER_ADRESSLISTE_MIT_GEBURTSDATUM) {
                value = nullAsEmptyString(mitarbeiter.getEmailToBeDisplayedInWord());
            }
            cellsRow2.add(value);
            dataset.add(cellsRow2);

            // Einzelner Datensatz der Liste von Datensätzen hinzufügen
            datasets.add(dataset);
            i++;
        }

        // Tabelle erzeugen
        Calendar today = new GregorianCalendar();
        String titel1 = "Kinder- und Jugendtheater Metzenthin AG                                                                      " + asString(today);
        CreateWordTableCommand createWordTableCommand = new CreateWordTableCommand(header, datasets, columnWidths, boldCells, mergedCells, maxLengths, titel1, titel, outputFile);
        createWordTableCommand.execute();

        result = Result.LISTE_ERFOLGREICH_ERSTELLT;
    }

}
