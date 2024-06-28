package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.persistence.entities.Dispensation;

import java.util.List;

import static ch.metzenthin.svm.common.utils.Converter.asString;

/**
 * @author Hans Stamm
 */
public class DispensationenTableData {

    private static final Field[] COLUMNS = {
            Field.DISPENSATIONSBEGINN,
            Field.DISPENSATIONSENDE,
            Field.VORAUSSICHTLICHE_DAUER,
            Field.GRUND};

    private List<Dispensation> dispensationen;

    public DispensationenTableData(List<Dispensation> dispensationen) {
        this.dispensationen = dispensationen;
    }

    public int getColumnCount() {
        return COLUMNS.length;
    }

    public int size() {
        return dispensationen.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Dispensation dispensation = dispensationen.get(rowIndex);
        Object value = null;
        switch (COLUMNS[columnIndex]) {
            case DISPENSATIONSBEGINN -> value = asString(dispensation.getDispensationsbeginn());
            case DISPENSATIONSENDE -> value = asString(dispensation.getDispensationsende());
            case VORAUSSICHTLICHE_DAUER -> value = dispensation.getVoraussichtlicheDauer();
            case GRUND -> value = dispensation.getGrund();
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

    public void setDispensationen(List<Dispensation> dispensationen) {
        this.dispensationen = dispensationen;
    }
}
