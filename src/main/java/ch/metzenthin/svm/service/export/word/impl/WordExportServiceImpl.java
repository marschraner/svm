package ch.metzenthin.svm.service.export.word.impl;

import ch.metzenthin.svm.service.export.word.WordExportService;
import ch.metzenthin.svm.service.export.word.WordTableLayout;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import org.springframework.stereotype.Service;

/**
 * @author Hans Stamm
 */
@Service
public class WordExportServiceImpl implements WordExportService {

  @Override
  public <T> void exportList(
      WordTableLayout wordTableLayout,
      String title1,
      String title2,
      List<List<String>> headerColumnsRows,
      Iterator<T> dataIterator,
      Function<T, List<List<String>>> columnsSupplier,
      File outputFile) {

    WordExportDocx4j wordExportDocx4j = new WordExportDocx4j();
    wordExportDocx4j.exportList(
        wordTableLayout,
        title1,
        title2,
        headerColumnsRows,
        dataIterator,
        columnsSupplier,
        outputFile);
  }
}
