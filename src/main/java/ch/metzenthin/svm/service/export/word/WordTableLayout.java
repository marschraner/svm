package ch.metzenthin.svm.service.export.word;

import java.util.List;

/**
 * @author Hans Stamm
 */
public record WordTableLayout(
    int topMargin,
    int bottomMargin,
    int leftMargin,
    int rightMargin,
    List<Integer> columnWidths,
    List<List<CellLayout>> datasetRowCellLayouts) {

  public WordTableLayout(List<Integer> columnWidths, List<List<CellLayout>> datasetRowCellLayouts) {
    this(850, 1, 580, 1, columnWidths, datasetRowCellLayouts);
  }
}
