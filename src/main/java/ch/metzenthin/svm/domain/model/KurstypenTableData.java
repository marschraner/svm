package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.persistence.entities.Kurstyp;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class KurstypenTableData {

    private static final Field[] COLUMNS = {Field.BEZEICHNUNG, Field.SELEKTIERBAR};

    private List<Kurstyp> kurstypen;

    public KurstypenTableData(List<Kurstyp> kurstypen) {
        this.kurstypen = kurstypen;
    }

    public int getColumnCount() {
        return COLUMNS.length;
    }

    public int size() {
        return kurstypen.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Kurstyp kurstyp = kurstypen.get(rowIndex);
        Object value = null;
        switch (COLUMNS[columnIndex]) {
            case BEZEICHNUNG -> value = kurstyp.getBezeichnung();
            case SELEKTIERBAR -> value = (kurstyp.getSelektierbar() ? "ja" : "nein");
            default -> {
            }
        }
        return value;
    }

    public Class<?> getColumnClass() {
        return String.class;
    }

    public String getColumnName(int column) {
        return COLUMNS[column].toString();
    }

    public void setKurstypen(List<Kurstyp> kurstypen) {
        this.kurstypen = kurstypen;
    }
}
