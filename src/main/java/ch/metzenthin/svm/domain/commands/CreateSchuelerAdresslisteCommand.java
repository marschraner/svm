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
        header.add(headerCellLinesColumn2);
        columnWidths.add(2400);
        // 3. Spalte
        List<String> headerCellLinesColumn3 = new ArrayList<>();
        headerCellLinesColumn3.add("Vorname");
        headerCellLinesColumn3.add("PLZ/Ort");
        headerCellLinesColumn3.add("E-Mail");
        header.add(headerCellLinesColumn3);
        columnWidths.add(2800);
        // 4. Spalte
        List<String> headerCellLinesColumn4 = new ArrayList<>();
        headerCellLinesColumn4.add("Geb.Datum");
        headerCellLinesColumn4.add("Festnetz");
        header.add(headerCellLinesColumn4);
        columnWidths.add(1700);
        // 5. Spalte
        List<String> headerCellLinesColumn5 = new ArrayList<>();
        headerCellLinesColumn5.add("Natel Mutter");
        headerCellLinesColumn5.add("Natel Vater");
        headerCellLinesColumn5.add("Natel Schüler");
        header.add(headerCellLinesColumn5);
        columnWidths.add(1700);
        // 6. Spalte
        List<String> headerCellLinesColumn6 = new ArrayList<>();
        headerCellLinesColumn6.add("Lehrkraft");
        headerCellLinesColumn6.add("Tag");
        headerCellLinesColumn6.add("Zeit");
        header.add(headerCellLinesColumn6);
        columnWidths.add(1700);
        // 7. Spalte
        List<String> headerCellLinesColumn7 = new ArrayList<>();
        headerCellLinesColumn7.add("Eintritt");
        header.add(headerCellLinesColumn7);
        columnWidths.add(0);

        // Inhalt
        List<Schueler> schuelerList = schuelerSuchenTableModel.getSchuelerList();
        Map<Schueler, List<Kurs>> kurse = schuelerSuchenTableModel.getKurse();

        List<List<List<String>>> rows = new ArrayList<>();
        int i = 0;
        for (Schueler schueler : schuelerList) {
            List<Kurs> schuelerKurse = kurse.get(schueler);
            int j = 0;
            do {
                List<List<String>> row = new ArrayList<>();
                // 1. Spalte
                List<String> cellLinesColumn1 = new ArrayList<>();
                cellLinesColumn1.add(Integer.toString(i+1));
                row.add(cellLinesColumn1);
                // 2. Spalte
                List<String> cellLinesColumn2 = new ArrayList<>();
                cellLinesColumn2.add(schueler.getNachname());
                cellLinesColumn2.add(schueler.getAdresse().getStrHausnummer());
                row.add(cellLinesColumn2);
                // 3. Spalte
                List<String> cellLinesColumn3 = new ArrayList<>();
                cellLinesColumn3.add(schueler.getVorname());
                cellLinesColumn3.add(schueler.getAdresse().getPlz() + " " + schueler.getAdresse().getOrt());
                String email = "";
                // Wenn vorhanden Email des Schülers, sonst der Mutter, sonst des Vaters; andernfalls leer
                if (checkNotEmpty(schueler.getEmail())) {
                    email = schueler.getEmail();
                } else if (schueler.getMutter() != null && checkNotEmpty(schueler.getMutter().getEmail())) {
                    email = schueler.getMutter().getEmail();
                } else if (schueler.getVater() != null && checkNotEmpty(schueler.getVater().getEmail())) {
                    email = schueler.getVater().getEmail();
                }
                cellLinesColumn3.add(email);
                row.add(cellLinesColumn3);
                // 4. Spalte
                List<String> cellLinesColumn4 = new ArrayList<>();
                cellLinesColumn4.add(asString(schueler.getGeburtsdatum()));
                cellLinesColumn4.add(nullAsEmptyString(schueler.getFestnetz()));
                row.add(cellLinesColumn4);
                // 5. Spalte
                List<String> cellLinesColumn5 = new ArrayList<>();
                if (schueler.getMutter() != null) {
                    cellLinesColumn5.add(nullAsEmptyString(schueler.getMutter().getNatel()));
                }
                if (schueler.getVater() != null) {
                    cellLinesColumn5.add(nullAsEmptyString(schueler.getVater().getNatel()));
                }
                cellLinesColumn5.add(nullAsEmptyString(schueler.getNatel()));
                row.add(cellLinesColumn5);
                // 6. Spalte
                List<String> cellLinesColumn6 = new ArrayList<>();
                if (schuelerKurse != null && schuelerKurse.size() > 0) {
                    Kurs kurs = schuelerKurse.get(j);
                    cellLinesColumn6.add(kurs.getLehrkraefteShortAsStr());
                    cellLinesColumn6.add(kurs.getWochentag().toString());
                    cellLinesColumn6.add(asString(kurs.getZeitBeginn()) + " - " + asString(kurs.getZeitEnde()));

                } else {
                    cellLinesColumn6.add(" ");
                }
                row.add(cellLinesColumn6);
                // 7. Spalte
                List<String> cellLinesColumn7 = new ArrayList<>();
                cellLinesColumn7.add(asString(schueler.getAnmeldungen().get(schueler.getAnmeldungen().size() - 1).getAnmeldedatum()));
                row.add(cellLinesColumn7);
                // Spalten der Zeile hinzufügen
                rows.add(row);
                i++;
                j++;
            } while (schuelerKurse != null && j < schuelerKurse.size());
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
