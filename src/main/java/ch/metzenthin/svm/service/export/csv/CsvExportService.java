package ch.metzenthin.svm.service.export.csv;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

/**
 * @author Martin Schraner
 */
public interface CsvExportService {
  <T> void exportList(
      List<String> headerColumns,
      Iterator<T> dataIterator,
      Function<T, List<String>> columnsSupplier,
      File outputFile);
}
