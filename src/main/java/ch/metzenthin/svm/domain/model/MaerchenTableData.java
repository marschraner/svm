package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.datatypes.Field;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class MaerchenTableData extends AbstractTableData<MaerchenAndNumberOfMaercheneinteilungen> {

  private static final Field[] COLUMNS = {
    Field.SCHULJAHR, Field.BEZEICHNUNG, Field.ANZAHL_VORSTELLUNGEN, Field.ANZAHL_KINDER
  };

  public MaerchenTableData(
      List<MaerchenAndNumberOfMaercheneinteilungen> maerchenAndNumberOfMaercheneinteilungenList) {
    super(COLUMNS, maerchenAndNumberOfMaercheneinteilungenList);
  }

  public Object getValueAt(int rowIndex, int columnIndex) {
    MaerchenAndNumberOfMaercheneinteilungen maerchenAndNumberOfMaercheneinteilungen =
        data.get(rowIndex);
    Object value = null;
    switch (COLUMNS[columnIndex]) {
      case SCHULJAHR -> value = maerchenAndNumberOfMaercheneinteilungen.maerchen().getSchuljahr();
      case BEZEICHNUNG ->
          value = maerchenAndNumberOfMaercheneinteilungen.maerchen().getBezeichnung();
      case ANZAHL_VORSTELLUNGEN ->
          value = maerchenAndNumberOfMaercheneinteilungen.maerchen().getAnzahlVorstellungen();
      case ANZAHL_KINDER ->
          value = maerchenAndNumberOfMaercheneinteilungen.numberOfMaercheneinteilungen();
      default -> {
        // Nothing to do
      }
    }
    return value;
  }

  public Class<?> getColumnClass(int columnIndex) {
    return switch (COLUMNS[columnIndex]) {
      case ANZAHL_VORSTELLUNGEN, ANZAHL_KINDER -> Integer.class;
      default -> String.class;
    };
  }
}
