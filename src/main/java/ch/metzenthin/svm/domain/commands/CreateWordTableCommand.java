package ch.metzenthin.svm.domain.commands;

import org.docx4j.jaxb.Context;
import org.docx4j.model.structure.PageSizePaper;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.StyleDefinitionsPart;
import org.docx4j.wml.*;

import java.io.File;
import java.math.BigInteger;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class CreateWordTableCommand implements Command {

    private static final String[] FONT_SIZE_CELLS = {"20", "19", "18", "17", "16", "14", "12", "10"};  // Calibri Font size: 10, 9.5, 9, 8.5, 8, 7, 6, 5
    private static final String FONT_SIZE_TITLE = "28";

    private WordprocessingMLPackage wordMLPackage;
    private ObjectFactory objectFactory;

    // input
    private final List<List<String>> headerRows;
    private final List<List<List<String>>> datasets;
    private final List<Integer> columnWidths;
    private final List<List<Boolean>> boldCells;
    private final List<List<Integer>> mergedCells;
    private final List<List<int[]>> maxLenghts;
    private final String title1;
    private final String title2;
    private final File outputFile;
    private final int topMargin;
    private final int bottomMargin;
    private final int leftMargin;
    private final int rightMargin;
    private int numberOfDatasetsFirstPage;
    private int numberOfDatasetsNormalPage;

    CreateWordTableCommand(List<List<String>> headerRows, List<List<List<String>>> datasets, List<Integer> columnWidths, List<List<Boolean>> boldCells, List<List<Integer>> mergedCells, List<List<int[]>> maxLenghts, String title1, String title2, File outputFile, int topMargin, int bottomMargin, int leftMargin, int rightMargin, int numberOfDatasetsFirstPage, int numberOfDatasetsNormalPage) {
        this.headerRows = headerRows;
        this.datasets = datasets;
        this.columnWidths = columnWidths;
        this.boldCells = boldCells;
        this.mergedCells = mergedCells;
        this.maxLenghts = maxLenghts;
        this.title1 = title1;
        this.title2 = title2;
        this.outputFile = outputFile;
        this.topMargin = topMargin;
        this.bottomMargin = bottomMargin;
        this.leftMargin = leftMargin;
        this.rightMargin = rightMargin;
        this.numberOfDatasetsFirstPage = numberOfDatasetsFirstPage;
        this.numberOfDatasetsNormalPage = numberOfDatasetsNormalPage;
    }

    CreateWordTableCommand(List<List<String>> headerRows, List<List<List<String>>> datasets, List<Integer> columnWidths, List<List<Boolean>> boldCells, List<List<Integer>> mergedCells, List<List<int[]>> maxLenghts, String title1, String title2, File outputFile) {
        this(headerRows, datasets, columnWidths, boldCells, mergedCells, maxLenghts, title1, title2, outputFile, 850, 1, 580, 1, 0, 0);
    }

    @Override
    public void execute() {

        if (numberOfDatasetsFirstPage == 0 || numberOfDatasetsNormalPage == 0) {
            determineNumberOfDatasetsPerPage();
        }

        // Source:http://blog.iprofs.nl/2012/09/06/creating-word-documents-with-docx4j/ (adapted)
        try {
            wordMLPackage = WordprocessingMLPackage.createPackage(PageSizePaper.A4, false);
        } catch (InvalidFormatException e) {
            throw new RuntimeException(e);
        }
        objectFactory = Context.getWmlObjectFactory();

        // Default-Schrift und -Schriftgrösse ändern
        alterStyleSheet();

        // Titel
        addTitle(FONT_SIZE_TITLE);

        // Einzelne Seiten
        int n = 0;
        int numberOfDatasetsPerPage = numberOfDatasetsFirstPage;
        boolean header = true;
        while (n < datasets.size()) {

            // Tabelle
            addTable(n, numberOfDatasetsPerPage, header);

            n += numberOfDatasetsPerPage;
            numberOfDatasetsPerPage = numberOfDatasetsNormalPage;
            header = false;

            // Zeilenumbruch
            if (n < datasets.size()) {
                addPageBreak();
            }
        }

        // Seitenränder anpassen
        SetWordPageMarginsCommand setWordPageMarginsCommand = new SetWordPageMarginsCommand(wordMLPackage, objectFactory, topMargin, bottomMargin, leftMargin, rightMargin);
        setWordPageMarginsCommand.execute();

        // Speichern
        try {
            wordMLPackage.save(outputFile);
        } catch (Docx4JException e) {
            throw new RuntimeException(e);
        }

    }

    private void determineNumberOfDatasetsPerPage() {
        if (headerRows.size() == 3) {
            numberOfDatasetsFirstPage = 12;
            numberOfDatasetsNormalPage = 14;
        } else if (headerRows.size() == 2) {
            numberOfDatasetsFirstPage = 17;
            numberOfDatasetsNormalPage = 19;
        } else {
            numberOfDatasetsFirstPage = 28;
            numberOfDatasetsNormalPage = 31;
        }
    }

    @SuppressWarnings("SameParameterValue")
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

    private void addTable(int datasetNumberStart, int numberOfDatasetsPerPage, boolean header) {
        // Tabelle
        Tbl table = objectFactory.createTbl();

        // Header
        // Iteration über Zeilen
        if (header) {
            for (int i = 0; i < headerRows.size(); i++) {
                Tr tableRow = objectFactory.createTr();
                List<String> headerRow = headerRows.get(i);
                List<Integer> mergedRow = mergedCells.get(i);
                boolean verticalSpace = (i == headerRows.size() - 1);
                // Iteration über Spalten
                for (int j = 0; j < headerRow.size(); j++) {
                    addTableCell(tableRow, headerRow.get(j), columnWidths.get(j), true, mergedRow.get(j), FONT_SIZE_CELLS[0], verticalSpace);
                    if (mergedRow.get(j) > 0) {
                        j += mergedRow.get(j) - 1;
                    }
                }
                table.getContent().add(tableRow);
            }
        }

        // Daten
        for (int n = datasetNumberStart; n < datasetNumberStart + numberOfDatasetsPerPage; n++) {

            if (n == datasets.size()) {
                break;
            }

            List<List<String>> datasetRows = datasets.get(n);
            // Iteration über Zeilen
            for (int i = 0; i < datasetRows.size(); i++) {
                Tr tableRow = objectFactory.createTr();
                List<String> datasetRow = datasetRows.get(i);
                List<Boolean> boldsRow = boldCells.get(i);
                List<Integer> mergedRow = mergedCells.get(i);
                List<int[]> maxLenghtsRow = maxLenghts.get(i);

                // Abstand am Ende eines Datensatzes
                boolean verticalSpace = (i == datasetRows.size() - 1);

                // Iteration über Spalten
                for (int j = 0; j < datasetRow.size(); j++) {

                    // Fontsize je nach Textlänge
                    int k = 0;
                    int[] maxLengthsCell = maxLenghtsRow.get(j);
                    String fontSize = FONT_SIZE_CELLS[0];
                    while (k < maxLengthsCell.length && k < FONT_SIZE_CELLS.length - 1 && maxLengthsCell[k] > 0 && datasetRow.get(j).length() > maxLengthsCell[k]) {
                        fontSize = FONT_SIZE_CELLS[k + 1];
                        k++;
                    }

                    addTableCell(tableRow, datasetRow.get(j), columnWidths.get(j), boldsRow.get(j), mergedRow.get(j), fontSize, verticalSpace);
                    if (mergedRow.get(j) > 0) {
                        j += mergedRow.get(j) - 1;
                    }
                }
                table.getContent().add(tableRow);
            }
        }

        wordMLPackage.getMainDocumentPart().addObject(table);
    }

    private void addTableCell(Tr tableRow, String content, int width, boolean bold, int mergedCells, String fontSize, boolean verticalSpace) {
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
        if (mergedCells > 1) {
            TcPr tcpr = objectFactory.createTcPr();
            tableCell.setTcPr(tcpr);
            CTVerticalJc valign = objectFactory.createCTVerticalJc();
            valign.setVal(STVerticalJc.TOP);
            tcpr.setVAlign(valign);
            org.docx4j.wml.TcPrInner.GridSpan gspan = objectFactory.createTcPrInnerGridSpan();
            gspan.setVal(new BigInteger(String.valueOf(mergedCells)));
            tcpr.setGridSpan(gspan);
        }
        // Paragraph der Zelle hinzufügen
        tableCell.getContent().add(paragraph);
        // Zelle der Zeile hinzufügen
        tableRow.getContent().add(tableCell);
    }

    /**
     * In this method we create a table cell properties object and a table width
     * object. We set the given width on the width object and then add it to
     * the properties object. Finally, we set the properties on the table cell.
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
     * In this method we're going to add the font size information to the run
     * properties. First we'll create a half-point measurement. Then we'll
     * set the fontSize as the value of this measurement. Finally, we'll set
     * the non-complex and complex script font sizes, sz and szCs respectively.
     */
    private void setFontSize(RPr runProperties, String fontSize) {
        HpsMeasure size = new HpsMeasure();
        size.setVal(new BigInteger(fontSize));
        runProperties.setSz(size);
        runProperties.setSzCs(size);
    }

    /**
     * In this method we'll add the bold property to the run properties.
     * BooleanDefaultTrue is the Docx4j object for the b property.
     * Technically we wouldn't have to set the value to true, as this is
     * the default.
     */
    private void addBoldStyle(RPr runProperties) {
        BooleanDefaultTrue b = new BooleanDefaultTrue();
        b.setVal(true);
        runProperties.setB(b);
    }

    /**
     * Change the font of the given run properties to Calibri.
     * A run font specifies the fonts which shall be used to display the contents
     * of the run. Of the four possible types of content, we change the styling of
     * two of them: ASCII and High ANSI.
     * Finally, we add the run font to the run properties
     */
    private void changeFontToCalibri(RPr runProperties) {
        RFonts runFont = new RFonts();
        runFont.setAscii("Calibri");
        runFont.setHAnsi("Calibri");
        runProperties.setRFonts(runFont);
    }


    /**
     * Adds a page break to the document.
     */
    private void addPageBreak() {
        P paragraph = objectFactory.createP();
        Br breakObj = new Br();

        breakObj.setType(STBrType.PAGE);
        paragraph.getContent().add(breakObj);
        wordMLPackage.getMainDocumentPart().addObject(paragraph);
    }

    /**
     * This method alters the default style sheet that is part of each document.
     * To do this, we first retrieve the style sheet from the package and then
     * get the Styles object from it. From this object, we get the list of actual
     * styles and iterate over them.
     * We check against all styles we want to alter and apply the alterations if
     * applicable.
     */
    private void alterStyleSheet() {
        StyleDefinitionsPart styleDefinitionsPart = wordMLPackage.getMainDocumentPart().getStyleDefinitionsPart();
        Styles styles;
        try {
            styles = styleDefinitionsPart.getContents();
        } catch (Docx4JException e) {
            throw new RuntimeException(e);
        }
        List<Style> stylesList = styles.getStyle();
        for (Style style : stylesList) {
            if (style.getStyleId().equals("Normal")) {
                alterNormalStyle(style);
            }
            // Ändern Title, Subtitle (hier nicht benötigt) etc siehe
            // http://blog.iprofs.nl/2012/11/19/adding-layout-to-your-docx4j-generated-word-documents-part-2/
        }
    }

    /**
     * First we create a run properties object as we want to remove nearly all
     * the existing styling. Then we change the font and font size and set the
     * run properties on the given style. As in previous examples, the font size
     * is defined to be in half-point size.
     */
    private void alterNormalStyle(Style style) {
        // we want to change (or remove) almost all the run properties of the
        // normal style, so we create a new one.
        RPr runProperties = new RPr();
        changeFontToCalibri(runProperties);
        changeFontSize(runProperties, 6);
        style.setRPr(runProperties);
    }

    /**
     * Change the font size of the given run properties to the given value.
     */
    @SuppressWarnings("SameParameterValue")
    private void changeFontSize(RPr runProperties, int fontSize) {
        HpsMeasure size = new HpsMeasure();
        size.setVal(BigInteger.valueOf(fontSize));
        runProperties.setSz(size);
    }

}
