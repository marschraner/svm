package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.entities.Kurs;
import ch.metzenthin.svm.persistence.entities.Schueler;
import ch.metzenthin.svm.ui.componentmodel.SchuelerSuchenTableModel;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.Tbl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ch.metzenthin.svm.common.utils.Converter.asString;
import static ch.metzenthin.svm.common.utils.Converter.nullAsEmptyString;

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

    @Override
    public void execute() {
        WordprocessingMLPackage wordMLPackage;
        try {
            wordMLPackage = WordprocessingMLPackage.createPackage();
        } catch (InvalidFormatException e) {
            throw new RuntimeException(e);
        }
        ObjectFactory objectFactory = Context.getWmlObjectFactory();

        // Titel
        wordMLPackage.getMainDocumentPart().addStyledParagraphOfText("Heading1", titel);

        // Header
        List<List<String>> header = new ArrayList<>();
        List<Integer> columnWidths = new ArrayList<>();
        // 1. Spalte
        List<String> headerCellLinesColumn1 = new ArrayList<>();
        headerCellLinesColumn1.add("");
        header.add(headerCellLinesColumn1);
        columnWidths.add(0);
        // 2. Spalte
        List<String> headerCellLinesColumn2 = new ArrayList<>();
        headerCellLinesColumn2.add("Name");
        headerCellLinesColumn2.add("Strasse/Nr.");
        headerCellLinesColumn2.add("Email");
        header.add(headerCellLinesColumn2);
        columnWidths.add(2300);
        // 3. Spalte
        List<String> headerCellLinesColumn3 = new ArrayList<>();
        headerCellLinesColumn3.add("Vorname");
        headerCellLinesColumn3.add("PLZ/Ort");
        headerCellLinesColumn3.add("Festnetz");
        header.add(headerCellLinesColumn3);
        columnWidths.add(2300);
        // 4. Spalte
        List<String> headerCellLinesColumn4 = new ArrayList<>();
        headerCellLinesColumn4.add("Natel Mutter");
        headerCellLinesColumn4.add("Natel Vater");
        headerCellLinesColumn4.add("Natel Schüler");
        header.add(headerCellLinesColumn4);
        columnWidths.add(1700);
        // 5. Spalte
        List<String> headerCellLinesColumn5 = new ArrayList<>();
        headerCellLinesColumn5.add("Lehrkraft");
        headerCellLinesColumn5.add("Tag");
        headerCellLinesColumn5.add("Zeit");
        header.add(headerCellLinesColumn5);
        columnWidths.add(1700);
        // 6. Spalte
        List<String> headerCellLinesColumn6 = new ArrayList<>();
        headerCellLinesColumn6.add("Geb.Datum");
        headerCellLinesColumn6.add("Eintritt");
        header.add(headerCellLinesColumn6);
        columnWidths.add(0);

        // Inhalt
        List<Schueler> schuelerList = schuelerSuchenTableModel.getSchuelerList();
        Map<Schueler, List<Kurs>> kurse = schuelerSuchenTableModel.getKurse();

        List<List<List<String>>> rows = new ArrayList<>();
        int i = 0;
        for (Schueler schueler : schuelerList) {
            for (Kurs kurs : kurse.get(schueler)) {
                List<List<String>> row = new ArrayList<>();
                // 1. Spalte
                List<String> cellLinesColumn1 = new ArrayList<>();
                cellLinesColumn1.add(Integer.toString(i+1));
                row.add(cellLinesColumn1);
                // 2. Spalte
                List<String> cellLinesColumn2 = new ArrayList<>();
                cellLinesColumn2.add(schueler.getNachname());
                cellLinesColumn2.add(schueler.getAdresse().getStrHausnummer());
                cellLinesColumn2.add(nullAsEmptyString(schueler.getEmail()));
                row.add(cellLinesColumn2);
                // 3. Spalte
                List<String> cellLinesColumn3 = new ArrayList<>();
                cellLinesColumn3.add(schueler.getVorname());
                cellLinesColumn3.add(schueler.getAdresse().getPlz() + " " + schueler.getAdresse().getOrt());
                cellLinesColumn3.add(nullAsEmptyString(schueler.getFestnetz()));
                row.add(cellLinesColumn3);
                // 4. Spalte
                List<String> cellLinesColumn4 = new ArrayList<>();
                if (schueler.getMutter() != null) {
                    cellLinesColumn4.add(nullAsEmptyString(schueler.getMutter().getNatel()));
                }
                if (schueler.getVater() != null) {
                    cellLinesColumn4.add(nullAsEmptyString(schueler.getVater().getNatel()));
                }
                cellLinesColumn4.add(nullAsEmptyString(schueler.getNatel()));
                row.add(cellLinesColumn4);
                // 5. Spalte
                List<String> cellLinesColumn5 = new ArrayList<>();
                cellLinesColumn5.add(kurs.getLehrkraefteShortAsStr());
                cellLinesColumn5.add(kurs.getWochentag().toString());
                cellLinesColumn5.add(asString(kurs.getZeitBeginn()) + " - " + asString(kurs.getZeitEnde()));
                row.add(cellLinesColumn5);
                // 6. Spalte
                List<String> cellLinesColumn6 = new ArrayList<>();
                cellLinesColumn6.add(asString(schueler.getGeburtsdatum()));
                cellLinesColumn6.add(asString(schueler.getAnmeldungen().get(schueler.getAnmeldungen().size()-1).getAnmeldedatum()));
                row.add(cellLinesColumn6);
                // Spalten der Zeile hinzufügen
                rows.add(row);
                i++;
            }
        }

        // Tabelle erzeugen
        CreateWordTableCommand createWordTableCommand = new CreateWordTableCommand(objectFactory, header, rows, columnWidths, false, "20");
        createWordTableCommand.execute();
        Tbl table = createWordTableCommand.getTable();
        wordMLPackage.getMainDocumentPart().addObject(table);

        // Seitenränder anpassen
        SetWordPageMarginsCommand setWordPageMarginsCommand = new SetWordPageMarginsCommand(wordMLPackage, objectFactory, 50, 50, 0, 0);
        setWordPageMarginsCommand.execute();

        // Speichern
        try {
            wordMLPackage.save(outputFile);
        } catch (Docx4JException e) {
            throw new RuntimeException(e);
        }

        result = Result.LISTE_ERFOLGREICH_ERSTELLT;
    }

}
