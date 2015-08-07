package ch.metzenthin.svm.domain.commands;

import org.docx4j.wml.*;

import java.math.BigInteger;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class CreateWordTableCommand extends GenericDaoCommand {

    // input
    private final ObjectFactory objectFactory;
    private final List<List<String>> header;
    private final List<List<List<String>>> rows;
    private final List<Integer> columnWidths;
    private final boolean isBorder;
    private final String fontsize;

    // output
    private Tbl table;

    public CreateWordTableCommand(ObjectFactory objectFactory, List<List<String>> header, List<List<List<String>>> rows, List<Integer> columnWidths, boolean isBorder, String fontsize) {
        this.objectFactory = objectFactory;
        this.header = header;
        this.rows = rows;
        this.columnWidths = columnWidths;
        this.isBorder = isBorder;
        this.fontsize = fontsize;
    }

    @Override
    public void execute() {

        // Source:http://blog.iprofs.nl/2012/09/06/creating-word-documents-with-docx4j/ (adapted)

        // Create Table
        table = objectFactory.createTbl();

        // Header
        Tr tableHeader = objectFactory.createTr();
        for (int i = 0; i < header.size(); i++) {
            addTableCellWithMultipleLines(objectFactory, tableHeader, header.get(i), columnWidths.get(i), true, fontsize);
        }
        table.getContent().add(tableHeader);

        // Rows
        for (List<List<String>> row : rows) {
            Tr tableRow = objectFactory.createTr();
            for (int i = 0; i < row.size(); i++) {
                addTableCellWithMultipleLines(objectFactory, tableRow, row.get(i), columnWidths.get(i), false, fontsize);
            }
            table.getContent().add(tableRow);
        }

        if (isBorder) {
            addBorders(table);
        }

    }

    private void addBorders(Tbl table) {
        table.setTblPr(new TblPr());
        CTBorder border = new CTBorder();
        border.setColor("auto");
        border.setSz(new BigInteger("4"));
        border.setSpace(new BigInteger("0"));
        border.setVal(STBorder.SINGLE);

        TblBorders borders = new TblBorders();
        borders.setBottom(border);
        borders.setLeft(border);
        borders.setRight(border);
        borders.setTop(border);
        borders.setInsideH(border);
        borders.setInsideV(border);
        table.getTblPr().setTblBorders(borders);
    }

    private void addTableCellWithMultipleLines(ObjectFactory factory, Tr tableRow, List<String> cellLines, int width, boolean bold, String fontSize) {
        Tc tableCell = factory.createTc();
        P paragraph = factory.createP();
        R run = factory.createR();
        Br br = factory.createBr(); // this Br element is used break the current and go for next line

        for (int i = 0; i < cellLines.size(); i++) {
            String content = cellLines.get(i);
            Text t = factory.createText();
            if (i >= 1) {
                run.getContent().add(br);
                t.setValue("\r\n " + content);
            } else {
                t.setValue(content);
            }
            run.getContent().add(t);
        }
        paragraph.getContent().add(run);

        // Zellenbreite
        if (width > 0) {
            setCellWidth(tableCell, width);
        }

        // Fett
        RPr runProperties = factory.createRPr();
        if (bold) {
            addBoldStyle(runProperties);
        }

        // Font-Size
        if (fontSize != null && !fontSize.isEmpty()) {
            setFontSize(runProperties, fontSize);
        }

        run.setRPr(runProperties);

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

    public Tbl getTable() {
        return table;
    }
}
