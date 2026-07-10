package ch.metzenthin.svm.ui.componentmodel;

import ch.metzenthin.svm.domain.model.KursAndLehrkraefteAndNumberOfKursanmeldungen;
import ch.metzenthin.svm.domain.model.KursTableData;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Hans Stamm
 */
public class KursTableModel
    extends TableModel<KursTableData, KursAndLehrkraefteAndNumberOfKursanmeldungen> {

  public KursTableModel(KursTableData tableData, double... columnWidthsPercentages) {
    super(tableData, columnWidthsPercentages);
  }

  @Override
  public String getTotalText() {
    int totalKurse = getRowCount();
    AtomicReference<Long> totalSchueler = new AtomicReference<>(0L);
    forEachRow(
        kursAndLehrkraefteAndNumberOfSchueler ->
            totalSchueler.updateAndGet(
                v -> v + kursAndLehrkraefteAndNumberOfSchueler.numberOfKursanmeldungen()));
    return "Total: "
        + totalKurse
        + " "
        + (totalKurse == 1 ? "Kurs" : "Kurse")
        + ", "
        + totalSchueler
        + " Schüler";
  }
}
