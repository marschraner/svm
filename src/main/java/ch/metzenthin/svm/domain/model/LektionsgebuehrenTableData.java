package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.persistence.entities.Lektionsgebuehren;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class LektionsgebuehrenTableData {

  private static final Field[] COLUMNS = {
    Field.LEKTIONSLAENGE,
    Field.BETRAG_1_KIND,
    Field.BETRAG_2_KINDER,
    Field.BETRAG_3_KINDER,
    Field.BETRAG_4_KINDER,
    Field.BETRAG_5_KINDER,
    Field.BETRAG_6_KINDER
  };

  private List<Lektionsgebuehren> lektionsgebuehrenList;

  public LektionsgebuehrenTableData(List<Lektionsgebuehren> lektionsgebuehrenList) {
    this.lektionsgebuehrenList = lektionsgebuehrenList;
  }

  public int getColumnCount() {
    return COLUMNS.length;
  }

  public int size() {
    return lektionsgebuehrenList.size();
  }

  public Object getValueAt(int rowIndex, int columnIndex) {
    Lektionsgebuehren lektionsgebuehren = lektionsgebuehrenList.get(rowIndex);
    Object value = null;
    switch (COLUMNS[columnIndex]) {
      case LEKTIONSLAENGE -> value = lektionsgebuehren.getLektionslaenge();
      case BETRAG_1_KIND -> value = lektionsgebuehren.getBetrag1Kind();
      case BETRAG_2_KINDER -> value = lektionsgebuehren.getBetrag2Kinder();
      case BETRAG_3_KINDER -> value = lektionsgebuehren.getBetrag3Kinder();
      case BETRAG_4_KINDER -> value = lektionsgebuehren.getBetrag4Kinder();
      case BETRAG_5_KINDER -> value = lektionsgebuehren.getBetrag5Kinder();
      case BETRAG_6_KINDER -> value = lektionsgebuehren.getBetrag6Kinder();
      default -> {
        // Nothing to do
      }
    }
    return value;
  }

  public Class<?> getColumnClass(int columnIndex) {
    return switch (COLUMNS[columnIndex]) {
      case LEKTIONSLAENGE -> Integer.class;
      case BETRAG_1_KIND,
              BETRAG_2_KINDER,
              BETRAG_3_KINDER,
              BETRAG_4_KINDER,
              BETRAG_5_KINDER,
              BETRAG_6_KINDER ->
          BigDecimal.class;
      default -> String.class;
    };
  }

  public String getColumnName(int column) {
    return COLUMNS[column].toString();
  }

  public List<Lektionsgebuehren> getLektionsgebuehrenList() {
    return lektionsgebuehrenList;
  }

  public void setLektionsgebuehrenList(List<Lektionsgebuehren> lektionsgebuehrenList) {
    this.lektionsgebuehrenList = lektionsgebuehrenList;
  }
}
