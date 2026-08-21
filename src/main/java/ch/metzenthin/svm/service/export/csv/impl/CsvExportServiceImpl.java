package ch.metzenthin.svm.service.export.csv.impl;

import ch.metzenthin.svm.common.SvmRuntimeException;
import ch.metzenthin.svm.service.export.csv.CsvExportService;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import org.springframework.stereotype.Service;

/**
 * @author Hans Stamm
 */
@Service
public class CsvExportServiceImpl implements CsvExportService {

  private static final String CSV_SEPARATOR = ";";

  @Override
  public <T> void exportList(
      List<String> headerColumns,
      Iterator<T> dataIterator,
      Function<T, List<String>> columnsSupplier,
      File outputFile) {

    try (BufferedWriter writer =
        Files.newBufferedWriter(outputFile.toPath(), StandardCharsets.UTF_8)) {
      writer.write(String.join(CSV_SEPARATOR, headerColumns));
      writer.newLine();
      while (dataIterator.hasNext()) {
        T row = dataIterator.next();
        List<String> columns = columnsSupplier.apply(row);
        boolean first = true;
        for (String column : columns) {
          if (!first) {
            writer.write(CSV_SEPARATOR);
          }
          first = false;
          String escapedValue = escapeCsv(column);
          writer.write(escapedValue);
        }
        writer.newLine();
      }

    } catch (IOException e) {
      throw new SvmRuntimeException("Fehler beim Erstellen oder Schreiben des CSV-Files", e);
    }
  }

  private static String escapeCsv(String value) {
    if (value == null) {
      return "";
    }

    if (value.contains(";") || value.contains("\"") || value.contains("\n")) {
      value = value.replace("\"", "\"\"");
      return "\"" + value + "\"";
    }

    return value;
  }
}
