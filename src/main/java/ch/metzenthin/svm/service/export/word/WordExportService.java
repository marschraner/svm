package ch.metzenthin.svm.service.export.word;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

/**
 * @author Hans Stamm
 */
public interface WordExportService {

  <T> void exportList(
      WordTableLayout wordTableLayout,
      String title1,
      String title2,
      List<List<String>> headerColumnsRows,
      Iterator<T> dataIterator,
      Function<T, List<List<String>>> columnsSupplier,
      File outputFile);
}
