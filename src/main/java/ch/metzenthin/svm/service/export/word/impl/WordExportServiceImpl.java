package ch.metzenthin.svm.service.export.word.impl;

import ch.metzenthin.svm.common.SvmRuntimeException;
import ch.metzenthin.svm.service.export.word.CellLayout;
import ch.metzenthin.svm.service.export.word.WordExportService;
import ch.metzenthin.svm.service.export.word.WordTableLayout;
import jakarta.xml.bind.JAXBElement;
import java.io.File;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import org.docx4j.jaxb.Context;
import org.docx4j.model.structure.PageDimensions;
import org.docx4j.model.structure.PageSizePaper;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.openpackaging.parts.WordprocessingML.StyleDefinitionsPart;
import org.docx4j.wml.Body;
import org.docx4j.wml.BooleanDefaultTrue;
import org.docx4j.wml.Br;
import org.docx4j.wml.CTVerticalJc;
import org.docx4j.wml.HpsMeasure;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.PPr;
import org.docx4j.wml.PPrBase;
import org.docx4j.wml.R;
import org.docx4j.wml.RFonts;
import org.docx4j.wml.RPr;
import org.docx4j.wml.STVerticalJc;
import org.docx4j.wml.SectPr;
import org.docx4j.wml.SectPr.PgMar;
import org.docx4j.wml.Style;
import org.docx4j.wml.Styles;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.TblGrid;
import org.docx4j.wml.TblGridCol;
import org.docx4j.wml.TblWidth;
import org.docx4j.wml.Tc;
import org.docx4j.wml.TcPr;
import org.docx4j.wml.Text;
import org.docx4j.wml.Tr;
import org.docx4j.wml.TrPr;
import org.springframework.stereotype.Service;

/**
 * @author Hans Stamm
 */
@Service
public class WordExportServiceImpl implements WordExportService {

  private static final String[] FONT_SIZE_CELLS = {
    "20", "19", "18", "17", "16", "14", "12", "10"
  }; // Calibri Font size: 10, 9.5, 9, 8.5, 8, 7, 6, 5
  private static final String FONT_SIZE_TITLE = "28";
  private static final String FEHLER_BEIM_ERSTELLEN_DER_DOCX_DATEI =
      "Fehler beim Erstellen der docx-Datei";

  private final WordprocessingMLPackage wordMLPackage;
  private final ObjectFactory objectFactory;

  public WordExportServiceImpl() {
    // Source:http://blog.iprofs.nl/2012/09/06/creating-word-documents-with-docx4j/ (adapted)
    try {
      wordMLPackage = WordprocessingMLPackage.createPackage(PageSizePaper.A4, false);
    } catch (InvalidFormatException e) {
      throw new SvmRuntimeException(FEHLER_BEIM_ERSTELLEN_DER_DOCX_DATEI, e);
    }
    objectFactory = Context.getWmlObjectFactory();

    // Default-Schrift und -Schriftgrösse ändern
    alterStyleSheet();
  }

  @Override
  public <T> void exportList(
      WordTableLayout wordTableLayout,
      String title1,
      String title2,
      List<List<String>> headerColumnsRows,
      Iterator<T> dataIterator,
      Function<T, List<List<String>>> columnsSupplier,
      File outputFile) {

    // Titel
    addTitle(title1, title2);

    Tbl table = objectFactory.createTbl();

    setColumnWidths(wordTableLayout, table);
    addHeader(wordTableLayout, table, headerColumnsRows);
    addData(wordTableLayout, table, dataIterator, columnsSupplier);

    wordMLPackage.getMainDocumentPart().addObject(table);

    setWordPageMargins(wordTableLayout);
    saveDocumentToOutputFile(outputFile);
  }

  private void addTitle(String title1, String title2) {
    P paragraph = objectFactory.createP();
    R run = objectFactory.createR();
    Br br =
        objectFactory.createBr(); // this Br element is used break the current and go for next line
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
    setFontSize(runProperties, WordExportServiceImpl.FONT_SIZE_TITLE);

    run.setRPr(runProperties);
    wordMLPackage.getMainDocumentPart().addObject(paragraph);
  }

  private void setColumnWidths(WordTableLayout wordTableLayout, Tbl table) {
    TblGrid tblGrid = objectFactory.createTblGrid();
    List<Integer> columnWidths = wordTableLayout.columnWidths();
    for (Integer columnWidth : columnWidths) {
      TblGridCol tblGridCol = objectFactory.createTblGridCol();
      tblGridCol.setW(BigInteger.valueOf(columnWidth));
      tblGrid.getGridCol().add(tblGridCol);
    }
    table.setTblGrid(tblGrid);
  }

  private void addHeader(
      WordTableLayout wordTableLayout, Tbl table, List<List<String>> headerRows) {
    List<List<CellLayout>> datasetRowCellLayouts = wordTableLayout.datasetRowCellLayouts();
    List<Integer> columnWidths = wordTableLayout.columnWidths();
    for (int i = 0; i < headerRows.size(); i++) {
      List<CellLayout> cellLayouts = datasetRowCellLayouts.get(i);
      Tr tableRow = objectFactory.createTr();
      List<String> headerRow = headerRows.get(i);
      boolean isLastRowOfDataset = (i == headerRows.size() - 1);
      // Iteration über Spalten
      int j = 0;
      while (j < headerRow.size()) {
        CellLayout cellLayout = cellLayouts.get(j);
        addTableCell(
            tableRow,
            headerRow.get(j),
            columnWidths.get(j),
            true,
            cellLayout.merged(),
            FONT_SIZE_CELLS[0],
            isLastRowOfDataset);
        if (cellLayout.merged() > 0) {
          j += cellLayout.merged() - 1;
        }
        j++;
      }
      table.getContent().add(tableRow);
    }
  }

  private <T> void addData(
      WordTableLayout wordTableLayout,
      Tbl table,
      Iterator<T> dataIterator,
      Function<T, List<List<String>>> columnsSupplier) {
    List<List<CellLayout>> datasetRowCellLayouts = wordTableLayout.datasetRowCellLayouts();
    List<Integer> columnWidths = wordTableLayout.columnWidths();

    while (dataIterator.hasNext()) {
      T next = dataIterator.next();
      List<List<String>> datasetRows = columnsSupplier.apply(next);

      // Iteration über Zeilen
      for (int i = 0; i < datasetRows.size(); i++) {
        List<CellLayout> cellLayouts = datasetRowCellLayouts.get(i);
        Tr tableRow = objectFactory.createTr();

        // Verhindern, dass eine einzelne Tabellenzeile über zwei Seiten verteilt wird
        setPreventSplittingOfTableRow(tableRow);

        List<String> datasetRow = datasetRows.get(i);

        // Abstand am Ende eines Datensatzes
        boolean isLastRowOfDataset = (i == datasetRows.size() - 1);

        // Iteration über Spalten
        int j = 0;
        while (j < datasetRow.size()) {
          CellLayout cellLayout = cellLayouts.get(j);
          String fontSize = getFontSize(cellLayout, datasetRow.get(j));

          addTableCell(
              tableRow,
              datasetRow.get(j),
              columnWidths.get(j),
              cellLayout.bold(),
              cellLayout.merged(),
              fontSize,
              isLastRowOfDataset);
          if (cellLayout.merged() > 0) {
            j += cellLayout.merged() - 1;
          }
          j++;
        }
        table.getContent().add(tableRow);
      }
    }
  }

  static String getFontSize(CellLayout cellLayout, String textValue) {
    // Fontsize je nach Textlänge
    int k = 0;
    int[] maxLengthsCell = cellLayout.maxLengths();
    String fontSize = FONT_SIZE_CELLS[0];
    while (k < maxLengthsCell.length
        && k < FONT_SIZE_CELLS.length - 1
        && maxLengthsCell[k] > 0
        && textValue.length() > maxLengthsCell[k]) {
      fontSize = FONT_SIZE_CELLS[k + 1];
      k++;
    }
    return fontSize;
  }

  private void setWordPageMargins(WordTableLayout wordTableLayout) {
    MainDocumentPart mainDocumentPart = wordMLPackage.getMainDocumentPart();
    Body body;
    try {
      body = mainDocumentPart.getContents().getBody();
    } catch (Docx4JException e) {
      throw new SvmRuntimeException(FEHLER_BEIM_ERSTELLEN_DER_DOCX_DATEI, e);
    }
    PageDimensions page = new PageDimensions();
    PgMar pgMar = getPageMargins(wordTableLayout, page);
    SectPr sectPr = objectFactory.createSectPr();
    body.setSectPr(sectPr);
    sectPr.setPgMar(pgMar);
  }

  private PgMar getPageMargins(WordTableLayout wordTableLayout, PageDimensions page) {
    int top = wordTableLayout.topMargin();
    int bottom = wordTableLayout.bottomMargin();
    int left = wordTableLayout.leftMargin();
    int right = wordTableLayout.rightMargin();
    PgMar pgMar = page.getPgMar();
    if (top > 0) { // default: 720
      pgMar.setTop(BigInteger.valueOf(top));
    }
    if (bottom > 0) { // default: 720
      pgMar.setBottom(BigInteger.valueOf(bottom));
    }
    if (left > 0) { // default: 720
      pgMar.setLeft(BigInteger.valueOf(left));
    }
    if (right > 0) { // default: 720
      pgMar.setRight(BigInteger.valueOf(right));
    }
    return pgMar;
  }

  private void saveDocumentToOutputFile(File outputFile) {
    // Speichern
    try {
      wordMLPackage.save(outputFile);
    } catch (Docx4JException e) {
      throw new SvmRuntimeException(FEHLER_BEIM_ERSTELLEN_DER_DOCX_DATEI, e);
    }
  }

  private void addTableCell(
      Tr tableRow,
      String content,
      int width,
      boolean bold,
      int mergedCells,
      String fontSize,
      boolean isLastRowOfDataset) {
    Tc tableCell = objectFactory.createTc();
    P paragraph = objectFactory.createP();
    R run = objectFactory.createR();

    // Text
    Text t = objectFactory.createText();
    t.setValue(content);
    run.getContent().add(t);
    paragraph.getContent().add(run);

    if (!isLastRowOfDataset) {
      PPr pPr = objectFactory.createPPr();
      // Paragraph-Abstand am Zellenunterrand entfernen
      PPrBase.Spacing spacing = new PPrBase.Spacing();
      spacing.setAfter(BigInteger.ZERO);
      pPr.setSpacing(spacing);
      // Zusammengehörende Tabellenzeilen auf derselben Seite halten
      setKeepNext(pPr);
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
      org.docx4j.wml.TcPrInner.GridSpan gridSpan = objectFactory.createTcPrInnerGridSpan();
      gridSpan.setVal(new BigInteger(String.valueOf(mergedCells)));
      tcpr.setGridSpan(gridSpan);
    }
    // Paragraph der Zelle hinzufügen
    tableCell.getContent().add(paragraph);
    // Zelle der Zeile hinzufügen
    tableRow.getContent().add(tableCell);
  }

  /**
   * In this method we create a table cell properties object and a table width object. We set the
   * given width on the width object and then add it to the properties object. Finally, we set the
   * properties on the table cell.
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
   * In this method we're going to add the font size information to the run properties. First we'll
   * create a half-point measurement. Then we'll set the fontSize as the value of this measurement.
   * Finally, we'll set the non-complex and complex script font sizes, sz and szCs respectively.
   */
  private void setFontSize(RPr runProperties, String fontSize) {
    HpsMeasure size = new HpsMeasure();
    size.setVal(new BigInteger(fontSize));
    runProperties.setSz(size);
    runProperties.setSzCs(size);
  }

  /**
   * In this method we'll add the bold property to the run properties. BooleanDefaultTrue is the
   * Docx4j object for the b property. Technically we wouldn't have to set the value to true, as
   * this is the default.
   */
  private void addBoldStyle(RPr runProperties) {
    BooleanDefaultTrue b = createBooleanDefaultTrue();
    runProperties.setB(b);
  }

  /**
   * Change the font of the given run properties to Calibri. A run font specifies the fonts which
   * shall be used to display the contents of the run. Of the four possible types of content, we
   * change the styling of two of them: ASCII and High ANSI. Finally, we add the run font to the run
   * properties
   */
  private void changeFontToCalibri(RPr runProperties) {
    RFonts runFont = new RFonts();
    runFont.setAscii("Calibri");
    runFont.setHAnsi("Calibri");
    runProperties.setRFonts(runFont);
  }

  /**
   * This method alters the default style sheet that is part of each document. To do this, we first
   * retrieve the style sheet from the package and then get the Styles object from it. From this
   * object, we get the list of actual styles and iterate over them. We check against all styles we
   * want to alter and apply the alterations if applicable.
   */
  private void alterStyleSheet() {
    StyleDefinitionsPart styleDefinitionsPart =
        wordMLPackage.getMainDocumentPart().getStyleDefinitionsPart();
    Styles styles;
    try {
      styles = styleDefinitionsPart.getContents();
    } catch (Docx4JException e) {
      throw new SvmRuntimeException(FEHLER_BEIM_ERSTELLEN_DER_DOCX_DATEI, e);
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
   * First we create a run properties object as we want to remove nearly all the existing styling.
   * Then we change the font and font size and set the run properties on the given style. As in
   * previous examples, the font size is defined to be in half-point size.
   */
  private void alterNormalStyle(Style style) {
    // we want to change (or remove) almost all the run properties of the
    // normal style, so we create a new one.
    RPr runProperties = new RPr();
    changeFontToCalibri(runProperties);
    changeFontSize(runProperties, 6);
    style.setRPr(runProperties);
  }

  /** Change the font size of the given run properties to the given value. */
  @SuppressWarnings("SameParameterValue")
  private void changeFontSize(RPr runProperties, int fontSize) {
    HpsMeasure size = new HpsMeasure();
    size.setVal(BigInteger.valueOf(fontSize));
    runProperties.setSz(size);
  }

  /**
   * Verhindern, dass eine einzelne Tabellenzeile über zwei Seiten verteilt wird:
   *
   * <p>Entspricht in LibreOffice: Klicke in die Tabelle. Wähle Tabelle → Eigenschaften…. Gehe zur
   * Registerkarte Textfluss (je nach Version kann sie auch anders heißen). Deaktiviere
   * Zeilenumbruch über Seiten und Spalten zulassen (oder eine ähnlich benannte Option).
   *
   * @param tableRow Tabellenzeile, für die das Splitten verhindert werden soll
   */
  private void setPreventSplittingOfTableRow(Tr tableRow) {
    BooleanDefaultTrue b = createBooleanDefaultTrue();
    JAXBElement<BooleanDefaultTrue> ctTrPrBaseCantSplit =
        objectFactory.createCTTrPrBaseCantSplit(b);
    TrPr trPr = objectFactory.createTrPr();
    trPr.getCnfStyleOrDivIdOrGridBefore().add(ctTrPrBaseCantSplit);
    tableRow.setTrPr(trPr);
  }

  private static BooleanDefaultTrue createBooleanDefaultTrue() {
    BooleanDefaultTrue b = new BooleanDefaultTrue();
    b.setVal(true);
    return b;
  }

  /**
   * Zusammengehörende Tabellenzeilen auf derselben Seite halten.
   *
   * <p>Entspricht in LibreOffice: Markiere den Inhalt der ersten der beiden Zeilen. Wähle Format →
   * Absatz…. Wechsle zur Registerkarte Textfluss. Aktiviere Mit nächstem Absatz zusammenhalten.
   * Bestätige mit OK.
   *
   * @param pPr ParagraphProperties
   */
  private static void setKeepNext(PPr pPr) {
    pPr.setKeepNext(createBooleanDefaultTrue());
  }
}
