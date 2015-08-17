package ch.metzenthin.svm.domain.commands;

import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.*;

import java.io.File;
import java.math.BigInteger;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class CreateWordTableCommand implements Command {

    private WordprocessingMLPackage wordMLPackage;
    private ObjectFactory objectFactory;

    // input
    private final List<List<String>> headerRows;
    private final List<List<List<String>>> datasets;
    private final List<Integer> columnWidths;
    private final List<List<Boolean>> boldCells;
    private final List<List<Boolean>> mergedCells;
    private final List<List<Integer>> maxLenghts;
    private final String title1;
    private final String title2;
    private final File outputFile;

    public CreateWordTableCommand(List<List<String>> headerRows, List<List<List<String>>> datasets, List<Integer> columnWidths, List<List<Boolean>> boldCells, List<List<Boolean>> mergedCells, List<List<Integer>> maxLenghts, String title1, String title2, File outputFile) {
        this.headerRows = headerRows;
        this.datasets = datasets;
        this.columnWidths = columnWidths;
        this.boldCells = boldCells;
        this.mergedCells = mergedCells;
        this.maxLenghts = maxLenghts;
        this.title1 = title1;
        this.title2 = title2;
        this.outputFile = outputFile;
    }

    @Override
    public void execute() {
        // Source:http://blog.iprofs.nl/2012/09/06/creating-word-documents-with-docx4j/ (adapted)

        String FONT_SIZE_TITLE = "28";
        String FONT_SIZE_CELLS_NORMAL = "20";
        String FONT_SIZE_CELLS_SMALL = "16";

        try {
            wordMLPackage = WordprocessingMLPackage.createPackage();
        } catch (InvalidFormatException e) {
            throw new RuntimeException(e);
        }
        objectFactory = Context.getWmlObjectFactory();

        // Titel
        addTitle(FONT_SIZE_TITLE);

        // Tabelle
        Tbl table = objectFactory.createTbl();

        // Header
        // Iteration über Zeilen
        for (int i = 0; i < headerRows.size(); i++) {
            Tr tableRow = objectFactory.createTr();
            List<String> headerRow = headerRows.get(i);
            List<Boolean> mergedRow = mergedCells.get(i);
            boolean verticalSpace = (i == headerRows.size() - 1);
            // Iteration über Spalten
            for (int j = 0; j < headerRow.size(); j++) {
                addTableCell(tableRow, headerRow.get(j), columnWidths.get(j), true, mergedRow.get(j), FONT_SIZE_CELLS_NORMAL, verticalSpace);
                if (mergedRow.get(j)) {
                    j++;
                }
            }
            table.getContent().add(tableRow);
        }

        // Daten
        for (List<List<String>> datasetRows : datasets) {
            // Iteration über Zeilen
            for (int i = 0; i < datasetRows.size(); i++) {
                Tr tableRow = objectFactory.createTr();
                List<String> datasetRow = datasetRows.get(i);
                List<Boolean> boldsRow = boldCells.get(i);
                List<Boolean> mergedRow = mergedCells.get(i);
                List<Integer> maxLenghtsRow = maxLenghts.get(i);
                boolean verticalSpace = (i == datasetRows.size() - 1);
                // Iteration über Spalten
                for (int j = 0; j < datasetRow.size(); j++) {
                    String fontSize = FONT_SIZE_CELLS_NORMAL;
                    if (maxLenghtsRow.get(j) > 0 && datasetRow.get(j).length() > maxLenghtsRow.get(j)) {
                        fontSize = FONT_SIZE_CELLS_SMALL;
                    }
                    addTableCell(tableRow, datasetRow.get(j), columnWidths.get(j), boldsRow.get(j), mergedRow.get(j), fontSize, verticalSpace);
                    if (mergedRow.get(j)) {
                        j++;
                    }
                }
                table.getContent().add(tableRow);
            }
        }

        wordMLPackage.getMainDocumentPart().addObject(table);

        // Seitenränder anpassen
        SetWordPageMarginsCommand setWordPageMarginsCommand = new SetWordPageMarginsCommand(wordMLPackage, objectFactory, 90, 10, 650, 650);
        setWordPageMarginsCommand.execute();

        // Speichern
        try {
            wordMLPackage.save(outputFile);
        } catch (Docx4JException e) {
            throw new RuntimeException(e);
        }

    }

    private void addTitle(String fontSize) {

        P paragraph = objectFactory.createP();
        R run = objectFactory.createR();
        Br br = objectFactory.createBr(); // this Br element is used break the current and go for next line
        RPr runProperties = objectFactory.createRPr();

        // Abstand nach Paragraph
        PPr pPr = objectFactory.createPPr();
        PPrBase.Spacing spacing = new PPrBase.Spacing();
        spacing.setAfter(BigInteger.valueOf(350));
        pPr.setSpacing(spacing);
        paragraph.setPPr(pPr);

        Text t1 = objectFactory.createText();
        t1.setValue(title1);
        run.getContent().add(t1);
        Text t2 = objectFactory.createText();
        run.getContent().add(br);
        t2.setValue("\r\n " + title2);
        run.getContent().add(t2);
        paragraph.getContent().add(run);

        // Font-Size
        setFontSize(runProperties, fontSize);

        run.setRPr(runProperties);
        wordMLPackage.getMainDocumentPart().addObject(paragraph);

    }

    private void addTableCell(Tr tableRow, String content, int width, boolean bold, boolean mergedCell, String fontSize, boolean verticalSpace) {
        Tc tableCell = objectFactory.createTc();
        P paragraph = objectFactory.createP();
        R run = objectFactory.createR();

        // Text
        Text t = objectFactory.createText();
        t.setValue(content);
        run.getContent().add(t);
        paragraph.getContent().add(run);

        // Paragraph-Abstand am Zellenunterrand entfernen
        if (!verticalSpace) {
            PPr pPr = objectFactory.createPPr();
            PPrBase.Spacing spacing = new PPrBase.Spacing();
            spacing.setAfter(BigInteger.ZERO);
            pPr.setSpacing(spacing);
            paragraph.setPPr(pPr);
        }
        // Zellenbreite
        if (width > 0) {
            setCellWidth(tableCell, width);
        }
        // Fett
        RPr runProperties = objectFactory.createRPr();
        if (bold) {
            addBoldStyle(runProperties);
        }
        // Font-Size
        if (fontSize != null && !fontSize.isEmpty()) {
            setFontSize(runProperties, fontSize);
        }
        run.setRPr(runProperties);

        // Merged cell
        if (mergedCell) {
            TcPr tcpr = objectFactory.createTcPr();
            tableCell.setTcPr(tcpr);
            CTVerticalJc valign = objectFactory.createCTVerticalJc();
            valign.setVal(STVerticalJc.TOP);
            tcpr.setVAlign(valign);
            org.docx4j.wml.TcPrInner.GridSpan gspan = objectFactory.createTcPrInnerGridSpan();
            gspan.setVal(new BigInteger("" + 2));
            tcpr.setGridSpan(gspan);
        }
        // Paragraph der Zelle hinzufügen
        tableCell.getContent().add(paragraph);
        // Zelle der Zeile hinzufügen
        tableRow.getContent().add(tableCell);
    }

    /**
     *  In this method we create a table cell properties object and a table width
     *  object. We set the given width on the width object and then add it to
     *  the properties object. Finally we set the properties on the table cell.
     */
    private void setCellWidth(Tc tableCell, int width) {
        TcPr tableCellProperties = new TcPr();
        TblWidth tableWidth = new TblWidth();
        tableWidth.setType("dxa");
        tableWidth.setW(BigInteger.valueOf(width));
        tableCellProperties.setTcW(tableWidth);
        tableCell.setTcPr(tableCellProperties);
    }

    /**
     *  In this method we're going to add the font size information to the run
     *  properties. First we'll create a half-point measurement. Then we'll
     *  set the fontSize as the value of this measurement. Finally we'll set
     *  the non-complex and complex script font sizes, sz and szCs respectively.
     */
    private void setFontSize(RPr runProperties, String fontSize) {
        HpsMeasure size = new HpsMeasure();
        size.setVal(new BigInteger(fontSize));
        runProperties.setSz(size);
        runProperties.setSzCs(size);
    }

    /**
     *  In this method we'll add the bold property to the run properties.
     *  BooleanDefaultTrue is the Docx4j object for the b property.
     *  Technically we wouldn't have to set the value to true, as this is
     *  the default.
     */
    private void addBoldStyle(RPr runProperties) {
        BooleanDefaultTrue b = new BooleanDefaultTrue();
        b.setVal(true);
        runProperties.setB(b);
    }

}
