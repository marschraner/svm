package ch.metzenthin.svm.domain.commands;

import static ch.metzenthin.svm.common.utils.Converter.calendarToDdMmYy;

import ch.metzenthin.svm.common.SvmRuntimeException;
import ch.metzenthin.svm.common.datatypes.Listentyp;
import ch.metzenthin.svm.persistence.entities.Schueler;
import ch.metzenthin.svm.ui.componentmodel.SchuelerSuchenTableModel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.*;

/**
 * @author Martin Schraner
 */
public class CreateListeFromTemplateCommand extends CreateListeCommand {

  private static final Logger LOGGER = LogManager.getLogger(CreateListeFromTemplateCommand.class);

  // input
  private final Listentyp listentyp;
  private final File outputFile;
  private final SchuelerSuchenTableModel schuelerSuchenTableModel;
  private final String titel;

  // output
  private File templateFile;

  public CreateListeFromTemplateCommand(
      SchuelerSuchenTableModel schuelerSuchenTableModel,
      String titel,
      Listentyp listentyp,
      File outputFile) {
    this.titel = titel;
    this.schuelerSuchenTableModel = schuelerSuchenTableModel;
    this.listentyp = listentyp;
    this.outputFile = outputFile;
  }

  @SuppressWarnings({"java:S3776", "java:S6541"})
  @Override
  public void execute() {
    // Template-File suchen
    FindTemplateFileCommand findTemplateFileCommand =
        new FindTemplateFileCommand(
            listentyp,
            schuelerSuchenTableModel.getSemester(),
            schuelerSuchenTableModel.getWochentag());
    findTemplateFileCommand.execute();
    templateFile = findTemplateFileCommand.getTemplateFile();

    if (findTemplateFileCommand.getResult()
        == FindTemplateFileCommand.Result.TEMPLATE_FILE_EXISTIERT_NICHT_ODER_NICHT_LESBAR) {
      result = Result.TEMPLATE_FILE_EXISTIERT_NICHT_ODER_NICHT_LESBAR;
      return;
    } else if (findTemplateFileCommand.getResult()
        == FindTemplateFileCommand.Result.FEHLER_BEIM_LESEN_DES_PROPERTY_FILE) {
      result = Result.FEHLER_BEIM_LESEN_DES_PROPERTY_FILE;
      return;
    }

    // Platzhalter in Template-File ersetzen
    List<Schueler> schuelerList = schuelerSuchenTableModel.getSelektierteSchuelerList();
    int maxRows = 50;
    try (XWPFDocument doc = new XWPFDocument(OPCPackage.open(templateFile))) {
      for (XWPFParagraph p : doc.getParagraphs()) {
        List<XWPFRun> runs = p.getRuns();
        if (runs != null) {
          for (XWPFRun r : runs) {
            String text = r.getText(0);
            if (text != null && text.contains("Titel")) {
              text = text.replace("Titel", titel);
              r.setText(text, 0);
            }
          }
        }
      }
      for (XWPFTable tbl : doc.getTables()) {
        for (XWPFTableRow row : tbl.getRows()) {
          for (XWPFTableCell cell : row.getTableCells()) {
            for (XWPFParagraph p : cell.getParagraphs()) {
              for (XWPFRun r : p.getRuns()) {
                String text = r.getText(0);
                LOGGER.debug("CreateListeFromTemplateCommand: text = {}", text);
                for (int i = 0; i < maxRows; i++) {
                  String ip = Integer.toString(i + 1);
                  // 01, 02,...
                  if (ip.length() == 1) {
                    ip = "0" + ip;
                  }
                  if (text.contains("I" + ip)) {
                    text =
                        text.replace(
                            "I" + ip, (schuelerList.size() > i ? Integer.toString(i + 1) : ""));
                  } else if (text.contains("VornameS" + ip)) {
                    text =
                        text.replace(
                            "VornameS" + ip,
                            (schuelerList.size() > i ? schuelerList.get(i).getVorname() : ""));
                  } else if (text.contains("NameS" + ip)) {
                    text =
                        text.replace(
                            "NameS" + ip,
                            (schuelerList.size() > i ? schuelerList.get(i).getNachname() : ""));
                  } else if (text.contains("GebS" + ip)) {
                    String geburtsdatumAsString;
                    if (schuelerList.size() > i) {
                      String ddMmYy = calendarToDdMmYy(schuelerList.get(i).getGeburtsdatum());
                      geburtsdatumAsString = (ddMmYy != null) ? ddMmYy : "";
                    } else {
                      geburtsdatumAsString = "";
                    }
                    text = text.replace("GebS" + ip, geburtsdatumAsString);
                  }
                  r.setText(text, 0);
                }
              }
            }
          }
        }
      }

      doc.write(new FileOutputStream(outputFile));

      result = Result.LISTE_ERFOLGREICH_ERSTELLT;

    } catch (InvalidFormatException | IOException e) {
      throw new SvmRuntimeException("Fehler beim Erstellen des Template-Files", e);
    }
  }

  public File getTemplateFile() {
    return templateFile;
  }
}
