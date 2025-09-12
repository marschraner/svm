package ch.metzenthin.svm.domain.commands;

import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;

import ch.metzenthin.svm.common.SvmRuntimeException;
import ch.metzenthin.svm.persistence.entities.Kurs;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class CreateKurslisteCsvFileCommand extends CreateListeCommand {

  // input
  private final List<? extends Kurs> kursList;
  private final File outputFile;

  public CreateKurslisteCsvFileCommand(List<? extends Kurs> kursList, File outputFile) {
    this.kursList = kursList;
    this.outputFile = outputFile;
  }

  @Override
  public void execute() {

    char separator = ';';

    try {
      Writer out =
          new BufferedWriter(
              new OutputStreamWriter(
                  new FileOutputStream(outputFile), StandardCharsets.ISO_8859_1));

      // Header
      out.write("Kurstyp");
      out.write(separator);
      out.write("Alter");
      out.write(separator);
      out.write("Stufe");
      out.write(separator);
      out.write("Tag");
      out.write(separator);
      out.write("Von");
      out.write(separator);
      out.write("Bis");
      out.write(separator);
      out.write("Ort");
      out.write(separator);
      out.write("Leitung");
      out.write(separator);
      out.write("Bemerkungen");
      out.write('\n');

      // Daten
      for (Kurs kurs : kursList) {
        out.write(kurs.getKurstyp().getBezeichnung());
        out.write(separator);
        out.write(kurs.getAltersbereich());
        out.write(separator);
        out.write(kurs.getStufe());
        out.write(separator);
        out.write(kurs.getWochentag().toString());
        out.write(separator);
        out.write(kurs.getZeitBeginn().toString());
        out.write(separator);
        out.write(kurs.getZeitEnde().toString());
        out.write(separator);
        out.write(kurs.getKursort().getBezeichnung());
        out.write(separator);
        out.write(kurs.getLehrkraefteShortAsStr());
        out.write(separator);
        if (checkNotEmpty(kurs.getBemerkungen())) {
          out.write(kurs.getBemerkungen().replace(";", ","));
        }
        out.write('\n');
      }

      out.close();

      result = Result.LISTE_ERFOLGREICH_ERSTELLT;

    } catch (IOException e) {
      throw new SvmRuntimeException("Fehler beim Erstellen der csv-Datei", e);
    }
  }
}
