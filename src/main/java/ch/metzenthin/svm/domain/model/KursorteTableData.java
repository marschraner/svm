package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.persistence.entities.Kursort;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class KursorteTableData {

    private List<Kursort> kursorte;

    public KursorteTableData(List<Kursort> kursorte) {
        this.kursorte = kursorte;
    }

    private static final Field[] COLUMNS = {Field.BEZEICHNUNG, Field.SELEKTIERBAR};

    public int getColumnCount() {
        return COLUMNS.length;
    }

    public int size() {
        return kursorte.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Kursort kursort = kursorte.get(rowIndex);
        Object value = null;
        switch (COLUMNS[columnIndex]) {
            case BEZEICHNUNG:
                value = kursort.getBezeichnung();
                break;
            case SELEKTIERBAR:
                value = (kursort.getSelektierbar() ? "ja" : "nein");
                break;
            default:
                break;
        }
        return value;
    }

    public Class<?> getColumnClass(int columnIndex) {
        switch (COLUMNS[columnIndex]) {
            default:
                return String.class;
        }
    }

    public String getColumnName(int column) {
        return COLUMNS[column].toString();
    }

    public void setKursorte(List<Kursort> kursorte) {
        this.kursorte = kursorte;
    }
}
