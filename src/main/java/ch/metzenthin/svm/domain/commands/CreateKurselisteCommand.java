package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.entities.Kurs;
import ch.metzenthin.svm.ui.componentmodel.KurseTableModel;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.Tbl;

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
        headerCellLinesColumn2.add("Kurstyp");
        header.add(headerCellLinesColumn2);
        columnWidths.add(2100);
        // 3. Spalte
        List<String> headerCellLinesColumn3 = new ArrayList<>();
        headerCellLinesColumn3.add("Alter");
        headerCellLinesColumn3.add("Stufe");
        header.add(headerCellLinesColumn3);
        columnWidths.add(2100);
        // 4. Spalte
        List<String> headerCellLinesColumn4 = new ArrayList<>();
        headerCellLinesColumn4.add("Tag");
        headerCellLinesColumn4.add("Zeit");
        header.add(headerCellLinesColumn4);
        columnWidths.add(1900);
        // 5. Spalte
        List<String> headerCellLinesColumn5 = new ArrayList<>();
        headerCellLinesColumn5.add("Ort");
        headerCellLinesColumn5.add("Leitung");
        header.add(headerCellLinesColumn5);
        columnWidths.add(2800);
        // 6. Spalte
        List<String> headerCellLinesColumn6 = new ArrayList<>();
        headerCellLinesColumn6.add("Bemerkungen");
        header.add(headerCellLinesColumn6);
        columnWidths.add(1400);

        // Inhalt
        List<Kurs> kurse = kurseTableModel.getKurse();

        List<List<List<String>>> rows = new ArrayList<>();
        int i = 0;
        for (Kurs kurs : kurse) {
            List<List<String>> row = new ArrayList<>();
            // 1. Spalte
            List<String> cellLinesColumn1 = new ArrayList<>();
            cellLinesColumn1.add(Integer.toString(i+1));
            row.add(cellLinesColumn1);
            // 2. Spalte
            List<String> cellLinesColumn2 = new ArrayList<>();
            cellLinesColumn2.add(kurs.getKurstyp().getBezeichnung());
            row.add(cellLinesColumn2);
            // 3. Spalte
            List<String> cellLinesColumn3 = new ArrayList<>();
            cellLinesColumn3.add(kurs.getAltersbereich());
            cellLinesColumn3.add(kurs.getStufe());
            row.add(cellLinesColumn3);
            // 4. Spalte
            List<String> cellLinesColumn4 = new ArrayList<>();
            cellLinesColumn4.add(kurs.getWochentag().toString());
            String zeit = asString(kurs.getZeitBeginn()) + " - " + asString(kurs.getZeitEnde());
            cellLinesColumn4.add(zeit);
            row.add(cellLinesColumn4);
            // 5. Spalte
            List<String> cellLinesColumn5 = new ArrayList<>();
            cellLinesColumn5.add(kurs.getKursort().getBezeichnung());
            cellLinesColumn5.add(kurs.getLehrkraefteShortAsStr());
            row.add(cellLinesColumn5);
            // 6. Spalte
            List<String> cellLinesColumn6 = new ArrayList<>();
            cellLinesColumn6.add(kurs.getBemerkungen());
            row.add(cellLinesColumn6);
            // Spalten der Zeile hinzufügen
            rows.add(row);
            i++;
        }

        // Tabelle erzeugen
        CreateWordTableCommand createWordTableCommand = new CreateWordTableCommand(objectFactory, header, rows, columnWidths, false, "20");
        createWordTableCommand.execute();
        Tbl table = createWordTableCommand.getTable();
        wordMLPackage.getMainDocumentPart().addObject(table);

        // Seitenränder anpassen
        SetWordPageMarginsCommand setWordPageMarginsCommand = new SetWordPageMarginsCommand(wordMLPackage, objectFactory, 50, 50, 650, 650);
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
