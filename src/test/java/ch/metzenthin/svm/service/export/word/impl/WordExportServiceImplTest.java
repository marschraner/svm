package ch.metzenthin.svm.service.export.word.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ch.metzenthin.svm.common.datatypes.Anrede;
import ch.metzenthin.svm.common.datatypes.Semesterbezeichnung;
import ch.metzenthin.svm.common.datatypes.Wochentag;
import ch.metzenthin.svm.domain.model.KursAndLehrkraefteAndNumberOfKursanmeldungen;
import ch.metzenthin.svm.persistence.entities.Kurs;
import ch.metzenthin.svm.persistence.entities.Kursort;
import ch.metzenthin.svm.persistence.entities.Kurstyp;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.service.export.word.CellLayout;
import ch.metzenthin.svm.service.export.word.WordExportService;
import ch.metzenthin.svm.service.export.word.WordTableLayout;
import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * @author Hans Stamm
 */
class WordExportServiceImplTest {

  WordExportService wordExportService = new WordExportServiceImpl();

  @Test
  void testExportList() throws IOException {
    File outputFile = new File("target/Test.docx");
    boolean success;
    success = outputFile.createNewFile();
    if (!success) {
      boolean delete = outputFile.delete();
      assertTrue(delete);
      success = outputFile.createNewFile();
    }
    assertTrue(success);

    WordTableLayout wordTableLayout = createWordTableLayout();

    List<KursAndLehrkraefteAndNumberOfKursanmeldungen> kursList = new ArrayList<>();
    for (int i = 1; i <= 50; i++) {
      kursList.add(
          new KursAndLehrkraefteAndNumberOfKursanmeldungen(
              createKurs(i, createSemester()), List.of(createMitarbeiter(i)), i));
    }
    String title1 =
        "Kinder- und Jugendtheater Metzenthin AG                              2018/2019";
    String title2 = "Kursliste";

    wordExportService.exportList(
        wordTableLayout,
        title1,
        title2,
        List.of(
            List.of("", "Kurstyp", "Alter", "Tag", "Leitung", "Bemerkungen"),
            List.of("", "", "Stufe", "Zeit", "Ort", "")),
        kursList.iterator(),
        k ->
            List.of(
                List.of(
                    Long.toString(k.numberOfKursanmeldungen()),
                    k.kurs().getKurstyp().getBezeichnung(),
                    k.kurs().getAltersbereich(),
                    k.kurs().getWochentag().toString(),
                    k.lehrkraefte().get(0).getNachname()
                        + " "
                        + k.lehrkraefte().get(0).getVorname(),
                    k.kurs().getBemerkungen()),
                List.of(
                    "",
                    "",
                    k.kurs().getStufe(),
                    k.kurs().getZeitBeginn() + " - " + k.kurs().getZeitEnde(),
                    k.kurs().getKursort().getBezeichnung(),
                    "")),
        outputFile);
  }

  @Test
  void testGetFontSize() {
    // Max length:   22    23    24    25    26    28
    // Text length:  23    24    25    26    27    29
    //                                       28    30 ...
    // FontSize:    "20", "19", "18", "17", "16", "14", "12", "10"
    // FontSize = FontSize[erster Index (Text length > Max length) + 1]
    CellLayout cellLayout = new CellLayout(false, 0, new int[] {22, 23, 24, 25, 26, 28});
    String fontSize;
    fontSize = WordExportServiceImpl.getFontSize(cellLayout, "123");
    assertEquals("20", fontSize);
    fontSize = WordExportServiceImpl.getFontSize(cellLayout, "1234567890123456789012");
    assertEquals("20", fontSize);
    fontSize = WordExportServiceImpl.getFontSize(cellLayout, "12345678901234567890123");
    assertEquals("19", fontSize);
    fontSize = WordExportServiceImpl.getFontSize(cellLayout, "123456789012345678901234");
    assertEquals("18", fontSize);
    fontSize = WordExportServiceImpl.getFontSize(cellLayout, "1234567890123456789012345");
    assertEquals("17", fontSize);
    fontSize = WordExportServiceImpl.getFontSize(cellLayout, "12345678901234567890123456");
    assertEquals("16", fontSize);
    fontSize = WordExportServiceImpl.getFontSize(cellLayout, "123456789012345678901234567");
    assertEquals("14", fontSize);
    fontSize = WordExportServiceImpl.getFontSize(cellLayout, "1234567890123456789012345678");
    assertEquals("14", fontSize);
    fontSize = WordExportServiceImpl.getFontSize(cellLayout, "12345678901234567890123456789");
    assertEquals("12", fontSize);
    fontSize = WordExportServiceImpl.getFontSize(cellLayout, "123456789012345678901234567890");
    assertEquals("12", fontSize);
    fontSize = WordExportServiceImpl.getFontSize(cellLayout, "1234567890123456789012345678901");
    assertEquals("12", fontSize);
  }

  private static WordTableLayout createWordTableLayout() {
    List<Integer> columnWidths = List.of(500, 2100, 2100, 1900, 2800, 1600);

    List<List<CellLayout>> datasetRowCellLayouts =
        List.of(
            List.of(
                new CellLayout(false, 0, new int[] {0}),
                new CellLayout(false, 0, new int[] {0}),
                new CellLayout(false, 0, new int[] {22, 23, 24, 25, 26, 28}),
                new CellLayout(false, 0, new int[] {0}),
                new CellLayout(false, 0, new int[] {26, 27, 28, 29, 30, 32}),
                new CellLayout(false, 0, new int[] {16, 17, 18, 19, 20, 22})),
            List.of(
                new CellLayout(false, 0, new int[] {0}),
                new CellLayout(false, 0, new int[] {20, 21, 22, 23, 24, 26}),
                new CellLayout(false, 0, new int[] {22, 23, 24, 25, 26, 28}),
                new CellLayout(false, 0, new int[] {0}),
                new CellLayout(false, 0, new int[] {26, 27, 28, 29, 30, 32}),
                new CellLayout(false, 0, new int[] {16, 17, 18, 19, 20, 22})));

    return new WordTableLayout(columnWidths, datasetRowCellLayouts);
  }

  private Semester createSemester() {
    Semester semester;
    semester = new Semester();
    semester.setSchuljahr("2025/2026");
    semester.setSemesterbezeichnung(Semesterbezeichnung.ERSTES_SEMESTER);
    semester.setSemesterbeginn(createCalendar("2025-08-18"));
    semester.setSemesterende(createCalendar("2026-02-07"));
    semester.setFerienbeginn1(createCalendar("2025-10-06"));
    semester.setFerienende1(createCalendar("2025-10-18"));
    semester.setFerienbeginn2(createCalendar("2025-12-22"));
    semester.setFerienende2(createCalendar("2026-01-03"));
    return semester;
  }

  private Kurs createKurs(int i, Semester semester) {
    Kurs kurs = new Kurs();
    kurs.setStufe("Stufe" + i);
    kurs.setAltersbereich("Altersbereich" + i);
    kurs.setWochentag(Wochentag.MITTWOCH);
    kurs.setZeitBeginn(createTime("14:00:00"));
    kurs.setZeitEnde(createTime("14:50:00"));
    kurs.setBemerkungen("Bemerkungen" + i);
    kurs.setKurstyp(createKurstyp(i));
    kurs.setKursort(createKursort(i));
    kurs.setSemester(semester);
    return kurs;
  }

  private Kurstyp createKurstyp(int i) {
    return new Kurstyp("Kurstyp" + i, true);
  }

  private Kursort createKursort(int i) {
    return new Kursort("Kursort" + i, true);
  }

  private Mitarbeiter createMitarbeiter(int i) {
    Mitarbeiter mitarbeiter;
    mitarbeiter = new Mitarbeiter();
    mitarbeiter.setAnrede(Anrede.KEINE);
    mitarbeiter.setVorname("Markus" + i);
    mitarbeiter.setNachname("Meier" + i);
    mitarbeiter.setSelektiert(false);
    mitarbeiter.setAktiv(true);
    mitarbeiter.setLehrkraft(true);
    return mitarbeiter;
  }

  private Calendar createCalendar(String dateAsString) {
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    Date date;
    try {
      date = df.parse(dateAsString);
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    return calendar;
  }

  private Time createTime(String timeAsString) {
    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
    Date date;
    try {
      date = sdf.parse(timeAsString);
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
    return new Time(date.getTime());
  }
}
