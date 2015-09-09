package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.persistence.entities.Lektionsgebuehren;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class LektionsgebuehrenTableData {

    private List<Lektionsgebuehren> lektionsgebuehrenList;

    public LektionsgebuehrenTableData(List<Lektionsgebuehren> lektionsgebuehrenList) {
        this.lektionsgebuehrenList = lektionsgebuehrenList;
    }

    private static final Field[] COLUMNS = {Field.LEKTIONSLAENGE, Field.BETRAG_1_KIND, Field.BETRAG_2_KINDER, Field.BETRAG_3_KINDER, Field.BETRAG_4_KINDER, Field.BETRAG_5_KINDER, Field.BETRAG_6_KINDER};

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
            case LEKTIONSLAENGE:
                value = lektionsgebuehren.getLektionslaenge();
                break;
            case BETRAG_1_KIND:
                value = lektionsgebuehren.getBetrag1Kind();
                break;
            case BETRAG_2_KINDER:
                value = lektionsgebuehren.getBetrag2Kinder();
                break;
            case BETRAG_3_KINDER:
                value = lektionsgebuehren.getBetrag3Kinder();
                break;
            case BETRAG_4_KINDER:
                value = lektionsgebuehren.getBetrag4Kinder();
                break;
            case BETRAG_5_KINDER:
                value = lektionsgebuehren.getBetrag5Kinder();
                break;
            case BETRAG_6_KINDER:
                value = lektionsgebuehren.getBetrag6Kinder();
                break;
            default:
                break;
        }
        return value;
    }

    public Class<?> getColumnClass(int columnIndex) {
        switch (COLUMNS[columnIndex]) {
            case LEKTIONSLAENGE:
                return Integer.class;
            case BETRAG_1_KIND:
                return BigDecimal.class;
            case BETRAG_2_KINDER:
                return BigDecimal.class;
            case BETRAG_3_KINDER:
                return BigDecimal.class;
            case BETRAG_4_KINDER:
                return BigDecimal.class;
            case BETRAG_5_KINDER:
                return BigDecimal.class;
            case BETRAG_6_KINDER:
                return BigDecimal.class;
            default:
                return String.class;
        }
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
